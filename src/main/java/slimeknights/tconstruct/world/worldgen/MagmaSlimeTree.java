package slimeknights.tconstruct.world.worldgen;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import slimeknights.tconstruct.blocks.WorldBlocks;

import javax.annotation.Nullable;
import java.util.Random;

public class MagmaSlimeTree extends SaplingGenerator {
    
    @Override
    @Nullable
    protected AbstractTreeFeature<DefaultFeatureConfig> getTreeFeature(Random random) {
        return new SlimeTreeFeature(DefaultFeatureConfig::deserialize, true, 5, 4, WorldBlocks.congealed_magma_slime.getDefaultState(), WorldBlocks.orange_slime_leaves.getDefaultState(), null, WorldBlocks.orange_slime_sapling, true);
    }
}
