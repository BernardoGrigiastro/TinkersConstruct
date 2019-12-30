package slimeknights.tconstruct.world.worldgen;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import slimeknights.tconstruct.blocks.WorldBlocks;

import javax.annotation.Nullable;
import java.util.Random;

public class PurpleSlimeTree extends SaplingGenerator {
    
    private final boolean isIslandTree;
    
    public PurpleSlimeTree(boolean isIslandTree) {
        this.isIslandTree = isIslandTree;
    }
    
    @Override
    @Nullable
    protected AbstractTreeFeature<DefaultFeatureConfig> getTreeFeature(Random random) {
        if (this.isIslandTree) {
            return new SlimeTreeFeature(DefaultFeatureConfig::deserialize, true, 5, 4, WorldBlocks.congealed_green_slime.getDefaultState(), WorldBlocks.purple_slime_leaves.getDefaultState(), WorldBlocks.purple_slime_vine_middle.getDefaultState(), WorldBlocks.purple_slime_sapling, true);
        } else {
            return new SlimeTreeFeature(DefaultFeatureConfig::deserialize, true, 5, 4, WorldBlocks.congealed_green_slime.getDefaultState(), WorldBlocks.purple_slime_leaves.getDefaultState(), null, WorldBlocks.purple_slime_sapling, true);
        }
    }
}

