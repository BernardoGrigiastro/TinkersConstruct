package slimeknights.tconstruct.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;
import slimeknights.tconstruct.blocks.WorldBlocks;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

// todo: evaluate block
public class SlimeVineBlock extends VineBlock {
    
    private final SlimeGrassBlock.FoliageType foliage;
    private final VineStage vineStage;
    
    public SlimeVineBlock(Settings properties, SlimeGrassBlock.FoliageType foliage, VineStage vineStage) {
        super(properties);
        this.foliage = foliage;
        this.vineStage = vineStage;
    }
    
    public static boolean canAttachTo(BlockView worldIn, BlockPos pos, Direction direction) {
        BlockState blockstate = worldIn.getBlockState(pos);
        return Block.doesSideFillSquare(blockstate.getCollisionShape(worldIn, pos), direction.getOpposite());
    }
    
    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        if (!worldIn.isClient) {
            if (random.nextInt(4) == 0) {
                this.grow(worldIn, random, pos, state);
            }
        }
    }
    
    public void grow(IWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        // end parts don't grow
        if (this.getStateFromStage() == null) {
            return;
        }
        
        // we only grow down
        BlockPos below = pos.down();
        if (worldIn.isAirBlock(below)) {
            // free floating position?
            if (this.freeFloating(worldIn, pos, state)) {
                // at most 3 middle parts
                int i = 0;
                while (worldIn.getBlockState(pos.up(i)).getBlock() == this) {
                    i++;
                }
                
                if (i > 2 || rand.nextInt(2) == 0) {
                    state = this.getStateFromStage().getDefaultState().with(NORTH, state.get(NORTH)).with(EAST, state.get(EAST)).with(SOUTH, state.get(SOUTH)).with(WEST, state.get(WEST));
                }
            }
            
            state = state.with(UP, false);
            
            worldIn.setBlockState(below, state, 3);
        }
    }
    
    private Block getStateFromStage() {
        switch (this.vineStage) {
            case START:
                if (this.foliage == SlimeGrassBlock.FoliageType.BLUE) {
                    return WorldBlocks.blue_slime_vine_middle;
                } else if (this.foliage == SlimeGrassBlock.FoliageType.PURPLE) {
                    return WorldBlocks.purple_slime_vine_middle;
                }
            case MIDDLE:
                if (this.foliage == SlimeGrassBlock.FoliageType.BLUE) {
                    return WorldBlocks.blue_slime_vine_end;
                } else if (this.foliage == SlimeGrassBlock.FoliageType.PURPLE) {
                    return WorldBlocks.purple_slime_vine_end;
                }
            case END:
                return null;
        }
        return null;
    }
    
    private boolean freeFloating(IWorld world, BlockPos pos, BlockState state) {
        for (Direction side : Direction.Type.field_11062) {
            if (state.get(getPropertyFor(side)) && canAttachTo(world, pos.offset(side), side.getOpposite())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    @Deprecated
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.isClient) {
            return;
        }
        
        BlockState oldState = state;
        
        // check each side to see if it can stay
        state = this.getCurrentState(state, worldIn, pos);
        
        // is our position still valid?
        if (this.getNumOfFaces(state) < 0) {
            spawnDrops(state, worldIn, pos);
            worldIn.removeBlock(pos, false);
        } else if (oldState != state) {
            worldIn.setBlockState(pos, state, 2);
        }
        
        // notify bottom block to update its state since ours might have changed as well
        BlockPos down = pos.down();
        BlockState state2;
        while ((state2 = worldIn.getBlockState(down)).getBlock() instanceof SlimeVineBlock) {
            worldIn.notifyBlockUpdate(down, state2, state2, 3);
            down = down.down();
        }
    }
    
    private BlockState getCurrentState(BlockState state, BlockView world, BlockPos pos) {
        BlockPos blockpos = pos.up();
        if (state.get(UP)) {
            state = state.with(UP, canAttachTo(world, blockpos, Direction.field_11033));
        }
        
        BlockState blockstate = null;
        
        for (Direction direction : Direction.Type.field_11062) {
            BooleanProperty booleanproperty = getPropertyFor(direction);
            if (state.get(booleanproperty)) {
                boolean flag = this.getFlagFromState(world, pos, direction);
                if (!flag) {
                    if (blockstate == null) {
                        blockstate = world.getBlockState(blockpos);
                    }
                    
                    flag = blockstate.getBlock() instanceof SlimeLeavesBlock || (blockstate.getBlock() instanceof SlimeVineBlock && blockstate.get(booleanproperty));
                }
                
                state = state.with(booleanproperty, flag);
            }
        }
        
        return state;
    }
    
    private boolean getFlagFromState(BlockView world, BlockPos pos, Direction direction) {
        if (direction == Direction.field_11033) {
            return false;
        } else {
            BlockPos blockpos = pos.offset(direction);
            if (canAttachTo(world, blockpos, direction)) {
                return true;
            } else if (direction.getAxis() == Direction.Axis.field_11052) {
                return false;
            } else {
                BooleanProperty booleanproperty = FACING_PROPERTIES.get(direction);
                BlockState blockstate = world.getBlockState(pos.up());
                return blockstate.getBlock() instanceof SlimeVineBlock && blockstate.get(booleanproperty);
            }
        }
    }
    
    private int getNumOfFaces(BlockState state) {
        int i = 0;
        
        for (BooleanProperty booleanproperty : FACING_PROPERTIES.values()) {
            if (state.get(booleanproperty)) {
                ++i;
            }
        }
        
        return i;
    }
    
    @Override
    public boolean isValidPosition(BlockState state, ViewableWorld worldIn, BlockPos pos) {
        return this.getNumOfFaces(this.getCurrentState(state, worldIn, pos)) > 0;
    }
    
    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.field_11033) {
            return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        } else {
            BlockState blockstate = this.getCurrentState(stateIn, worldIn, currentPos);
            return !(this.getNumOfFaces(blockstate) > 0) ? Blocks.field_10124.getDefaultState() : blockstate;
        }
    }
    
    @Override
    @Nullable
    public BlockState getStateForPlacement(ItemPlacementContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getPos());
        boolean flag = blockstate.getBlock() == this;
        BlockState blockstate1 = flag ? blockstate : this.getDefaultState();
        
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction != Direction.field_11033) {
                BooleanProperty booleanproperty = getPropertyFor(direction);
                boolean flag1 = flag && blockstate.get(booleanproperty);
                if (!flag1 && this.getFlagFromState(context.getWorld(), context.getPos(), direction)) {
                    return blockstate1.with(booleanproperty, Boolean.TRUE);
                }
            }
        }
        
        return flag ? blockstate1 : null;
    }
    
    public BlockState getStateToPlace(IWorld world, BlockPos pos) {
        BlockState blockstate = world.getBlockState(pos);
        boolean flag = blockstate.getBlock() == this;
        BlockState blockstate1 = flag ? blockstate : this.getDefaultState();
        
        for (Direction direction : new Direction[]{Direction.field_11034, Direction.field_11036, Direction.field_11035, Direction.field_11043, Direction.field_11039}) {
            if (direction != Direction.field_11033) {
                BooleanProperty booleanproperty = getPropertyFor(direction);
                boolean flag1 = flag && blockstate.get(booleanproperty);
                if (!flag1 && this.getFlagFromState(world, pos, direction)) {
                    blockstate1 = blockstate1.with(booleanproperty, Boolean.TRUE);
                }
            }
        }
        
        return blockstate1;
    }
    
    public SlimeGrassBlock.FoliageType getFoliageType() {
        return this.foliage;
    }
    
    public enum VineStage implements StringIdentifiable {
        START,
        MIDDLE,
        END;
        
        @Override
        public String getName() {
            return this.toString().toLowerCase(Locale.US);
        }
    }
    
}
