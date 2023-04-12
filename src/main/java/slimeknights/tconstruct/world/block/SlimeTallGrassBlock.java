package slimeknights.tconstruct.world.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.PlantType;
import slimeknights.tconstruct.blocks.WorldBlocks;
import slimeknights.tconstruct.world.TinkerWorld;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

// todo: evaluate block
public class SlimeTallGrassBlock extends PlantBlock implements IShearable {
    
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
    
    private final SlimeGrassBlock.FoliageType foliageType;
    private final SlimePlantType plantType;
    
    public SlimeTallGrassBlock(Settings properties, SlimeGrassBlock.FoliageType foliageType, SlimePlantType plantType) {
        super(properties);
        this.foliageType = foliageType;
        this.plantType = plantType;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context) {
        return SHAPE;
    }
    
    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public Block.OffsetType getOffsetType() {
        return Block.OffsetType.field_10655;
    }
    
    /* Forge/MC callbacks */
    @Nonnull
    @Override
    public PlantType getPlantType(BlockView world, BlockPos pos) {
        return TinkerWorld.slimePlantType;
    }
    
    @Override
    public boolean isShearable(@Nonnull ItemStack item, ViewableWorld world, BlockPos pos) {
        return true;
    }
    
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune) {
        ItemStack stack = new ItemStack(this, 1);
        return Lists.newArrayList(stack);
    }
    
    @Override
    protected boolean isValidGround(BlockState state, BlockView worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return block == WorldBlocks.green_slime_dirt || block == WorldBlocks.blue_slime_dirt || block == WorldBlocks.purple_slime_dirt || block == WorldBlocks.magma_slime_dirt || block == WorldBlocks.blue_vanilla_slime_grass || block == WorldBlocks.purple_vanilla_slime_grass || block == WorldBlocks.orange_vanilla_slime_grass || block == WorldBlocks.blue_green_slime_grass || block == WorldBlocks.purple_green_slime_grass || block == WorldBlocks.orange_green_slime_grass || block == WorldBlocks.blue_blue_slime_grass || block == WorldBlocks.purple_blue_slime_grass || block == WorldBlocks.orange_blue_slime_grass || block == WorldBlocks.blue_purple_slime_grass || block == WorldBlocks.purple_purple_slime_grass || block == WorldBlocks.orange_purple_slime_grass || block == WorldBlocks.blue_magma_slime_grass || block == WorldBlocks.purple_magma_slime_grass || block == WorldBlocks.orange_magma_slime_grass;
    }
    
    public SlimeGrassBlock.FoliageType getFoliageType() {
        return this.foliageType;
    }
    
    public SlimePlantType getPlantType() {
        return this.plantType;
    }
    
    public enum SlimePlantType implements StringIdentifiable {
        TALL_GRASS,
        FERN;
        
        @Override
        public String getName() {
            return this.toString().toLowerCase(Locale.US);
        }
    }
}
