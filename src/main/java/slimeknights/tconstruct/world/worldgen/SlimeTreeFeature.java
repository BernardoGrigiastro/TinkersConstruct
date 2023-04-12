package slimeknights.tconstruct.world.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import slimeknights.tconstruct.blocks.WorldBlocks;
import slimeknights.tconstruct.common.Tags;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class SlimeTreeFeature extends AbstractTreeFeature<DefaultFeatureConfig> {
    
    private final int minTreeHeight;
    private final int treeHeightRange;
    private final BlockState trunk;
    private final BlockState leaf;
    private final BlockState vine;
    private final boolean seekHeight;
    
    /**
     * Slimetree feature with all the configs for it. Cannot use IFeatureConfig currently since vanilla trees don't support
     * it currently. Will be there in 1.15
     *
     * @param doBlockNotifyOnPlace True by default, set to false for biome worldgen stuff
     * @param minTreeHeightIn      Minimum tree height. This refers to the trunk
     * @param treeHeightRangeIn    Height variation, total tree height = minTreeHeight + variation
     * @param trunkState           Blockstate to use for the trunk, usually congealed slimeblocks
     * @param leafState            Blockstate to use for the leaves, usually slimeleaves
     * @param vineState            Blockstate to use for the vines at the leaves, can be null for no leaves. Trees out of saplings don't have vines.
     * @param sapling              Sapling that grows into this tree
     * @param seekHeightIn         If true the y-coordinate will be lowered until it hits the ground. Used for island generation
     */
    public SlimeTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configFactoryIn, boolean doBlockNotifyOnPlace, int minTreeHeightIn, int treeHeightRangeIn, BlockState trunkState, BlockState leafState, BlockState vineState, Block sapling, boolean seekHeightIn) {
        super(configFactoryIn, doBlockNotifyOnPlace);
        
        this.minTreeHeight = minTreeHeightIn;
        this.treeHeightRange = treeHeightRangeIn;
        this.trunk = trunkState;
        this.leaf = leafState;
        this.vine = vineState;
        this.seekHeight = seekHeightIn;
        this.setSapling((net.minecraftforge.common.IPlantable) sapling);
    }
    
    protected static boolean isAirOrLeaves(TestableWorld worldIn, BlockPos pos) {
        if (!(worldIn instanceof net.minecraft.world.ViewableWorld)) { // FORGE: Redirect to state method when possible
            return worldIn.hasBlockState(pos, (state) -> state.isAir() || state.isIn(BlockTags.field_15503) || state.isIn(Tags.Blocks.SLIMY_LEAVES));
        } else {
            return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLeaves((net.minecraft.world.ViewableWorld) worldIn, pos));
        }
    }
    
    @Deprecated
    protected static boolean isSlimyDirtOrGrass(TestableWorld worldIn, BlockPos pos) {
        return worldIn.hasBlockState(pos, (p_214582_0_) -> {
            Block block = p_214582_0_.getBlock();
            return block == WorldBlocks.green_slime_dirt || block == WorldBlocks.blue_slime_dirt || block == WorldBlocks.purple_slime_dirt || block == WorldBlocks.magma_slime_dirt || block == WorldBlocks.blue_vanilla_slime_grass || block == WorldBlocks.purple_vanilla_slime_grass || block == WorldBlocks.orange_vanilla_slime_grass || block == WorldBlocks.blue_green_slime_grass || block == WorldBlocks.purple_green_slime_grass || block == WorldBlocks.orange_green_slime_grass || block == WorldBlocks.blue_blue_slime_grass || block == WorldBlocks.purple_blue_slime_grass || block == WorldBlocks.orange_blue_slime_grass || block == WorldBlocks.blue_purple_slime_grass || block == WorldBlocks.purple_purple_slime_grass || block == WorldBlocks.orange_purple_slime_grass || block == WorldBlocks.blue_magma_slime_grass || block == WorldBlocks.purple_magma_slime_grass || block == WorldBlocks.orange_magma_slime_grass;
        });
    }
    
    protected static boolean isSoil(TestableWorld reader, BlockPos pos, net.minecraftforge.common.IPlantable sapling) {
        if (!(reader instanceof net.minecraft.world.BlockView) || sapling == null) {
            return isSlimyDirtOrGrass(reader, pos);
        }
        return reader.hasBlockState(pos, state -> state.canSustainPlant((net.minecraft.world.BlockView) reader, pos, Direction.field_11036, sapling));
    }
    
    @Override
    protected boolean place(Set<BlockPos> changedBlocks, ModifiableTestableWorld worldIn, Random rand, BlockPos position, MutableIntBoundingBox boundingBox) {
        int height = rand.nextInt(this.treeHeightRange) + this.minTreeHeight;
        
        if (this.seekHeight) {
            if (!(worldIn instanceof IWorld)) {
                return false;
            }
            
            position = this.findGround((IWorld) worldIn, position);
            if (position.getY() < 0) {
                return false;
            }
        }
        
        if (position.getY() >= 1 && position.getY() + height + 1 <= worldIn.getMaxHeight()) {
            if (isSoil(worldIn, position.down(), this.getSapling())) {
                this.setSlimeDirtAt(worldIn, position.down(), position);
                this.placeTrunk(changedBlocks, worldIn, position, height, boundingBox);
                this.placeCanopy(changedBlocks, worldIn, rand, position, height, boundingBox);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private void placeCanopy(Set<BlockPos> changedBlocks, ModifiableTestableWorld world, Random random, BlockPos position, int height, MutableIntBoundingBox boundingBox) {
        position = position.up(height);
        for (int i = 0; i < 4; i++) {
            this.placeDiamondLayer(changedBlocks, world, position.down(i), i + 1, boundingBox);
        }
        BlockState air = Blocks.field_10124.getDefaultState();
        
        position = position.down(3);
        this.placeAtPosition(changedBlocks, world, position.add(+4, 0, 0), air, boundingBox);
        this.placeAtPosition(changedBlocks, world, position.add(-4, 0, 0), air, boundingBox);
        this.placeAtPosition(changedBlocks, world, position.add(0, 0, +4), air, boundingBox);
        this.placeAtPosition(changedBlocks, world, position.add(0, 0, -4), air, boundingBox);
        if (this.vine != null) {
            this.placeAtPosition(changedBlocks, world, position.add(+1, 0, +1), air, boundingBox);
            this.placeAtPosition(changedBlocks, world, position.add(+1, 0, -1), air, boundingBox);
            this.placeAtPosition(changedBlocks, world, position.add(-1, 0, +1), air, boundingBox);
            this.placeAtPosition(changedBlocks, world, position.add(-1, 0, -1), air, boundingBox);
        }
        
        //Drippers
        // stuck with only one block down because of leaf decay distance
        position = position.down();
        this.placeAtPosition(changedBlocks, world, position.add(+3, 0, 0), this.leaf, boundingBox);
        this.placeAtPosition(changedBlocks, world, position.add(-3, 0, 0), this.leaf, boundingBox);
        this.placeAtPosition(changedBlocks, world, position.add(0, 0, -3), this.leaf, boundingBox);
        this.placeAtPosition(changedBlocks, world, position.add(0, 0, +3), this.leaf, boundingBox);
        if (this.vine == null) {
            this.placeAtPosition(changedBlocks, world, position.add(+1, 0, +1), this.leaf, boundingBox);
            this.placeAtPosition(changedBlocks, world, position.add(+1, 0, -1), this.leaf, boundingBox);
            this.placeAtPosition(changedBlocks, world, position.add(-1, 0, +1), this.leaf, boundingBox);
            this.placeAtPosition(changedBlocks, world, position.add(-1, 0, -1), this.leaf, boundingBox);
        }
        
        if (this.vine != null) {
            position = position.down();
            
            this.placeVineAtPosition(world, position.add(+3, 0, 0), this.getRandomizedVine(random).with(VineBlock.UP, true));
            this.placeVineAtPosition(world, position.add(-3, 0, 0), this.getRandomizedVine(random).with(VineBlock.UP, true));
            this.placeVineAtPosition(world, position.add(0, 0, -3), this.getRandomizedVine(random).with(VineBlock.UP, true));
            this.placeVineAtPosition(world, position.add(0, 0, +3), this.getRandomizedVine(random).with(VineBlock.UP, true));
            BlockState randomVine = this.getRandomizedVine(random);
            this.placeVineAtPosition(world, position.add(+2, 1, +2), randomVine.with(VineBlock.UP, true));
            this.placeVineAtPosition(world, position.add(+2, 0, +2), randomVine);
            randomVine = this.getRandomizedVine(random);
            this.placeVineAtPosition(world, position.add(+2, 1, -2), randomVine.with(VineBlock.UP, true));
            this.placeVineAtPosition(world, position.add(+2, 0, -2), randomVine);
            randomVine = this.getRandomizedVine(random);
            this.placeVineAtPosition(world, position.add(-2, 1, +2), randomVine.with(VineBlock.UP, true));
            this.placeVineAtPosition(world, position.add(-2, 0, +2), randomVine);
            randomVine = this.getRandomizedVine(random);
            this.placeVineAtPosition(world, position.add(-2, 1, -2), randomVine.with(VineBlock.UP, true));
            this.placeVineAtPosition(world, position.add(-2, 0, -2), randomVine);
        }
    }
    
    private BlockState getRandomizedVine(Random random) {
        BlockState state = this.vine;
        
        BooleanProperty[] sides = new BooleanProperty[]{VineBlock.NORTH, VineBlock.EAST, VineBlock.SOUTH, VineBlock.WEST};
        
        for (BooleanProperty side : sides) {
            state = state.with(side, false);
        }
        
        for (int i = random.nextInt(3) + 1; i > 0; i--) {
            state = state.with(sides[random.nextInt(sides.length)], true);
        }
        
        return state;
    }
    
    private void placeDiamondLayer(Set<BlockPos> changedBlocks, ModifiableTestableWorld world, BlockPos pos, int range, MutableIntBoundingBox boundingBox) {
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (Math.abs(x) + Math.abs(z) <= range) {
                    BlockPos blockpos = pos.add(x, 0, z);
                    if (isAirOrLeaves(world, blockpos)) {
                        this.setSlimyLogState(changedBlocks, world, blockpos, this.leaf, boundingBox);
                    }
                }
            }
        }
    }
    
    private void placeTrunk(Set<BlockPos> changedBlocks, ModifiableTestableWorld world, BlockPos pos, int height, MutableIntBoundingBox boundingBox) {
        while (height > 0) {
            if (isAirOrLeaves(world, pos)) {
                this.setSlimyLogState(changedBlocks, world, pos, this.trunk, boundingBox);
            }
            
            pos = pos.up();
            height--;
        }
    }
    
    private void placeVineAtPosition(ModifiableTestableWorld world, BlockPos pos, BlockState state) {
        if (isAirOrLeaves(world, pos)) {
            this.setBlockState(world, pos, state);
        }
    }
    
    private void placeAtPosition(Set<BlockPos> changedBlocks, ModifiableTestableWorld world, BlockPos pos, BlockState state, MutableIntBoundingBox boundingBox) {
        if (isAirOrLeaves(world, pos)) {
            this.setSlimyLogState(changedBlocks, world, pos, state, boundingBox);
        }
    }
    
    private BlockPos findGround(IWorld reader, BlockPos position) {
        do {
            BlockState state = reader.getBlockState(position);
            Block block = state.getBlock();
            BlockState upState = reader.getBlockState(position.up());
            if ((block == WorldBlocks.green_slime_dirt || block == WorldBlocks.blue_slime_dirt || block == WorldBlocks.purple_slime_dirt || block == WorldBlocks.magma_slime_dirt || block == WorldBlocks.blue_vanilla_slime_grass || block == WorldBlocks.purple_vanilla_slime_grass || block == WorldBlocks.orange_vanilla_slime_grass || block == WorldBlocks.blue_green_slime_grass || block == WorldBlocks.purple_green_slime_grass || block == WorldBlocks.orange_green_slime_grass || block == WorldBlocks.blue_blue_slime_grass || block == WorldBlocks.purple_blue_slime_grass || block == WorldBlocks.orange_blue_slime_grass || block == WorldBlocks.blue_purple_slime_grass || block == WorldBlocks.purple_purple_slime_grass || block == WorldBlocks.orange_purple_slime_grass || block == WorldBlocks.blue_magma_slime_grass || block == WorldBlocks.purple_magma_slime_grass || block == WorldBlocks.orange_magma_slime_grass) && !upState.getBlock().isOpaqueCube(upState, reader, position)) {
                return position.up();
            }
            position = position.down();
        } while (position.getY() > 0);
        
        return position;
    }
    
    private void setSlimeDirtAt(ModifiableTestableWorld reader, BlockPos pos, BlockPos origin) {
        if (!(reader instanceof IWorld)) {
            return;
        }
        ((IWorld) reader).getBlockState(pos).onPlantGrow((IWorld) reader, pos, origin);
    }
    
    private void setSlimyLogState(Set<BlockPos> changedBlocks, ModifiableWorld worldIn, BlockPos pos, BlockState state, MutableIntBoundingBox boundingBox) {
        if (this.emitNeighborBlockUpdates) {
            worldIn.setBlockState(pos, state, 19);
        } else {
            worldIn.setBlockState(pos, state, 18);
        }
        
        boundingBox.expandTo(new MutableIntBoundingBox(pos, pos));
        
        if (Tags.Blocks.SLIMY_LOGS.contains(state.getBlock())) {
            changedBlocks.add(pos.toImmutable());
        }
    }
}
