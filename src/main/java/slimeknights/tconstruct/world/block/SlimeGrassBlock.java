package slimeknights.tconstruct.world.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import slimeknights.tconstruct.blocks.WorldBlocks;

import java.util.Locale;
import java.util.Random;

// todo: evaluate this block
public class SlimeGrassBlock extends Block implements Fertilizable {
    
    private static final BooleanProperty SNOWY = Properties.SNOWY;
    private final FoliageType foliageType;
    
    public SlimeGrassBlock(Settings properties, FoliageType foliageType) {
        super(properties);
        this.setDefaultState(this.stateFactory.getBaseState().with(SNOWY, Boolean.FALSE));
        this.foliageType = foliageType;
    }
    
    private static boolean canBecomeSlimeGrass(BlockState stateIn, ViewableWorld worldReader, BlockPos pos) {
        BlockPos blockpos = pos.up();
        BlockState state = worldReader.getBlockState(blockpos);
        int i = ChunkLightProvider.func_215613_a(worldReader, stateIn, pos, state, blockpos, Direction.field_11036, state.getOpacity(worldReader, blockpos));
        return i < worldReader.getMaxLightLevel();
    }
    
    private static boolean canSlimeGrassSpread(BlockState state, ViewableWorld worldReader, BlockPos pos) {
        BlockPos blockpos = pos.up();
        return canBecomeSlimeGrass(state, worldReader, pos) && !worldReader.getFluidState(blockpos).isTagged(FluidTags.field_15517);
    }
    
    @Override
    public boolean isSolid(BlockState state) {
        return true;
    }
    
    @Override
    protected void fillStateContainer(StateFactory.Builder<Block, BlockState> builder) {
        builder.add(SNOWY);
    }
    
    @Override
    public void fillItemGroup(ItemGroup group, DefaultedList<ItemStack> items) {
        items.add(new ItemStack(this));
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public boolean canGrow(BlockView worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return true;
    }
    
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, BlockState state) {
        BlockPos blockpos1 = pos.up();
        int i = 0;
        
        while (i < 128) {
            BlockPos blockpos2 = blockpos1;
            int j = 0;
            
            while (true) {
                if (j < i / 16) {
                    blockpos2 = blockpos2.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                    
                    if (worldIn.getBlockState(blockpos2.down()).getBlock() == this && !worldIn.getBlockState(blockpos2).getBlock().isNormalCube(state, worldIn, pos)) {
                        ++j;
                        continue;
                    }
                } else if (worldIn.isAirBlock(blockpos2)) {
                    BlockState plantState = null;
                    
                    if (rand.nextInt(8) == 0) {
                        switch (this.foliageType) {
                            case BLUE:
                                plantState = WorldBlocks.blue_slime_fern.getDefaultState();
                                break;
                            case PURPLE:
                                plantState = WorldBlocks.purple_slime_fern.getDefaultState();
                                break;
                            case ORANGE:
                                plantState = WorldBlocks.orange_slime_fern.getDefaultState();
                                break;
                        }
                    } else {
                        switch (this.foliageType) {
                            case BLUE:
                                plantState = WorldBlocks.blue_slime_tall_grass.getDefaultState();
                                break;
                            case PURPLE:
                                plantState = WorldBlocks.purple_slime_tall_grass.getDefaultState();
                                break;
                            case ORANGE:
                                plantState = WorldBlocks.orange_slime_tall_grass.getDefaultState();
                                break;
                        }
                    }
                    
                    if (plantState != null) {
                        if (plantState.isValidPosition(worldIn, blockpos2)) {
                            worldIn.setBlockState(blockpos2, plantState, 3);
                        }
                    }
                }
                
                ++i;
                break;
            }
        }
    }
    
    public FoliageType getFoliageType() {
        return this.foliageType;
    }
    
    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        if (!worldIn.isClient) {
            if (!worldIn.isAreaLoaded(pos, 3)) {
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            }
            if (!canBecomeSlimeGrass(state, worldIn, pos)) {
                BlockState dirtState = this.getDirtState(state);
                
                if (dirtState != null) {
                    worldIn.setBlockState(pos, dirtState);
                }
            } else {
                if (worldIn.getLight(pos.up()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                        BlockState newState = this.getStateFromDirt(worldIn.getBlockState(blockpos));
                        if (newState != null && canSlimeGrassSpread(newState, worldIn, blockpos)) {
                            worldIn.setBlockState(blockpos, newState);
                        }
                    }
                }
            }
        }
    }
    
    public BlockState getDirtState(BlockState grassState) {
        if (grassState.getBlock() == WorldBlocks.blue_vanilla_slime_grass || grassState.getBlock() == WorldBlocks.purple_vanilla_slime_grass || grassState.getBlock() == WorldBlocks.orange_vanilla_slime_grass) {
            return Blocks.field_10566.getDefaultState();
        }
        if (grassState.getBlock() == WorldBlocks.blue_green_slime_grass || grassState.getBlock() == WorldBlocks.purple_green_slime_grass || grassState.getBlock() == WorldBlocks.orange_green_slime_grass) {
            return WorldBlocks.green_slime_dirt.getDefaultState();
        }
        if (grassState.getBlock() == WorldBlocks.blue_blue_slime_grass || grassState.getBlock() == WorldBlocks.purple_blue_slime_grass || grassState.getBlock() == WorldBlocks.orange_blue_slime_grass) {
            return WorldBlocks.blue_slime_dirt.getDefaultState();
        }
        if (grassState.getBlock() == WorldBlocks.blue_purple_slime_grass || grassState.getBlock() == WorldBlocks.purple_purple_slime_grass || grassState.getBlock() == WorldBlocks.orange_purple_slime_grass) {
            return WorldBlocks.purple_slime_dirt.getDefaultState();
        }
        if (grassState.getBlock() == WorldBlocks.blue_magma_slime_grass || grassState.getBlock() == WorldBlocks.purple_magma_slime_grass || grassState.getBlock() == WorldBlocks.orange_magma_slime_grass) {
            return WorldBlocks.magma_slime_dirt.getDefaultState();
        }
        
        return null;
    }
    
    private BlockState getStateFromDirt(BlockState dirtState) {
        if (dirtState.getBlock() == Blocks.field_10566) {
            switch (this.foliageType) {
                case BLUE:
                    return WorldBlocks.blue_vanilla_slime_grass.getDefaultState();
                case PURPLE:
                    return WorldBlocks.purple_vanilla_slime_grass.getDefaultState();
                case ORANGE:
                    return WorldBlocks.orange_vanilla_slime_grass.getDefaultState();
            }
        }
        
        if (dirtState.getBlock() == WorldBlocks.green_slime_dirt) {
            switch (this.foliageType) {
                case BLUE:
                    return WorldBlocks.blue_green_slime_grass.getDefaultState();
                case PURPLE:
                    return WorldBlocks.purple_green_slime_grass.getDefaultState();
                case ORANGE:
                    return WorldBlocks.orange_green_slime_grass.getDefaultState();
            }
        } else if (dirtState.getBlock() == WorldBlocks.blue_slime_dirt) {
            switch (this.foliageType) {
                case BLUE:
                    return WorldBlocks.blue_blue_slime_grass.getDefaultState();
                case PURPLE:
                    return WorldBlocks.purple_blue_slime_grass.getDefaultState();
                case ORANGE:
                    return WorldBlocks.orange_blue_slime_grass.getDefaultState();
            }
        } else if (dirtState.getBlock() == WorldBlocks.purple_slime_dirt) {
            switch (this.foliageType) {
                case BLUE:
                    return WorldBlocks.blue_purple_slime_grass.getDefaultState();
                case PURPLE:
                    return WorldBlocks.purple_purple_slime_grass.getDefaultState();
                case ORANGE:
                    return WorldBlocks.orange_purple_slime_grass.getDefaultState();
            }
        } else if (dirtState.getBlock() == WorldBlocks.magma_slime_dirt) {
            switch (this.foliageType) {
                case BLUE:
                    return WorldBlocks.blue_magma_slime_grass.getDefaultState();
                case PURPLE:
                    return WorldBlocks.purple_magma_slime_grass.getDefaultState();
                case ORANGE:
                    return WorldBlocks.orange_magma_slime_grass.getDefaultState();
            }
        }
        
        return null;
    }
    
    public enum FoliageType implements StringIdentifiable {
        BLUE,
        PURPLE,
        ORANGE;
        
        @Override
        public String getName() {
            return this.toString().toLowerCase(Locale.US);
        }
    }
}
