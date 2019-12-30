package slimeknights.tconstruct.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraftforge.common.PlantType;
import slimeknights.tconstruct.blocks.WorldBlocks;
import slimeknights.tconstruct.world.TinkerWorld;

import javax.annotation.Nonnull;

public class SlimeSaplingBlock extends SaplingBlock {
    
    public SlimeSaplingBlock(SaplingGenerator treeIn, Settings properties) {
        super(treeIn, properties);
    }
    
    @Override
    protected boolean isValidGround(BlockState state, BlockView worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return block == WorldBlocks.green_slime_dirt || block == WorldBlocks.blue_slime_dirt || block == WorldBlocks.purple_slime_dirt || block == WorldBlocks.magma_slime_dirt || block == WorldBlocks.blue_vanilla_slime_grass || block == WorldBlocks.purple_vanilla_slime_grass || block == WorldBlocks.orange_vanilla_slime_grass || block == WorldBlocks.blue_green_slime_grass || block == WorldBlocks.purple_green_slime_grass || block == WorldBlocks.orange_green_slime_grass || block == WorldBlocks.blue_blue_slime_grass || block == WorldBlocks.purple_blue_slime_grass || block == WorldBlocks.orange_blue_slime_grass || block == WorldBlocks.blue_purple_slime_grass || block == WorldBlocks.purple_purple_slime_grass || block == WorldBlocks.orange_purple_slime_grass || block == WorldBlocks.blue_magma_slime_grass || block == WorldBlocks.purple_magma_slime_grass || block == WorldBlocks.orange_magma_slime_grass;
    }
    
    @Nonnull
    @Override
    public PlantType getPlantType(BlockView world, BlockPos pos) {
        return TinkerWorld.slimePlantType;
    }
    
    @Override
    @Deprecated
    public boolean isReplaceable(BlockState state, ItemPlacementContext useContext) {
        return false;
    }
}
