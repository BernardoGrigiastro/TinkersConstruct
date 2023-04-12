package slimeknights.tconstruct.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraftforge.common.ToolType;
import slimeknights.tconstruct.library.utils.HarvestLevels;

import javax.annotation.Nullable;

public final class BlockProperties {
    /* Properties should be set in the following order for consistency:
     * material/tooltype/soundtype via constructor.
     * harvestlevel if required
     * hardness and resistance always
     * other
     */
    
    private static final ToolType NO_TOOL = null;
    
    static Block.Settings GENERIC_METAL_BLOCK = builder(Material.METAL, ToolType.PICKAXE, BlockSoundGroup.METAL).hardnessAndResistance(5.0f);
    static Block.Settings GENERIC_GEM_BLOCK = GENERIC_METAL_BLOCK;
    static Block.Settings GENERIC_SAND_BLOCK = builder(Material.SAND, ToolType.SHOVEL, BlockSoundGroup.SAND).hardnessAndResistance(3.0f).slipperiness(0.8F);
    static Block.Settings GENERIC_EARTH_BLOCK = builder(Material.EARTH, ToolType.SHOVEL, BlockSoundGroup.GRAVEL).hardnessAndResistance(2.0F);
    static Block.Settings GENERIC_GLASS_BLOCK = builder(Material.GLASS, ToolType.PICKAXE, BlockSoundGroup.GLASS).hardnessAndResistance(0.3F);
    
    static Block.Settings DECO_GROUND_SLAB = builder(Material.STONE, ToolType.SHOVEL, BlockSoundGroup.GRAVEL).hardnessAndResistance(2.0F);// ?????
    
    static Block.Settings FIREWOOD = builder(Material.WOOD, ToolType.AXE, BlockSoundGroup.WOOD).hardnessAndResistance(2.0F, 7.0F).lightValue(7);
    static Block.Settings LAVAWOOD = FIREWOOD;
    
    static Block.Settings MUD_BRICKS = builder(Material.EARTH, ToolType.SHOVEL, BlockSoundGroup.GRAVEL).hardnessAndResistance(2.0F);
    static Block.Settings DRIED_CLAY = builder(Material.STONE, ToolType.PICKAXE, BlockSoundGroup.STONE).hardnessAndResistance(1.5F, 20.0F);
    static Block.Settings DRIED_CLAY_BRICKS = DRIED_CLAY;
    
    // WORLD
    static Block.Settings ORE = builder(Material.STONE, ToolType.PICKAXE, BlockSoundGroup.STONE).harvestLevel(HarvestLevels.COBALT).hardnessAndResistance(10.0F);
    static Block.Settings SLIME = Block.Settings.create(Material.CLAY, MaterialColor.GRASS).sound(BlockSoundGroup.SLIME).hardnessAndResistance(0.0f).slipperiness(0.8F);
    static Block.Settings CONGEALED_SLIME = builder(Material.CLAY, NO_TOOL, BlockSoundGroup.SLIME).hardnessAndResistance(0.5F).slipperiness(0.5F);
    static Block.Settings SLIME_DIRT = builder(Material.ORGANIC, NO_TOOL, BlockSoundGroup.SLIME).hardnessAndResistance(0.55F);
    static Block.Settings SLIME_GRASS = SLIME_DIRT;
    static Block.Settings SLIME_LEAVES = builder(Material.LEAVES, NO_TOOL, BlockSoundGroup.GRASS).hardnessAndResistance(0.3F).tickRandomly();
    
    static Block.Settings SAPLING = builder(Material.PLANT, NO_TOOL, BlockSoundGroup.GRASS).hardnessAndResistance(0.1F).doesNotBlockMovement().tickRandomly();
    static Block.Settings TALL_GRASS = builder(Material.PLANT, NO_TOOL, BlockSoundGroup.GRASS).hardnessAndResistance(0.1F).doesNotBlockMovement().tickRandomly();
    static Block.Settings VINE = builder(Material.REPLACEABLE_PLANT, NO_TOOL, BlockSoundGroup.GRASS).hardnessAndResistance(0.3F).doesNotBlockMovement().tickRandomly();
    
    // MISC
    static Block.Settings GLOW = builder(Material.PART, NO_TOOL, BlockSoundGroup.WOOL).hardnessAndResistance(0.0F).lightValue(14);
    static Block.Settings STONE_TORCH = builder(Material.PART, NO_TOOL, BlockSoundGroup.STONE).doesNotBlockMovement().hardnessAndResistance(0.0F).lightValue(14);
    static Block.Settings STONE_LADDER = builder(Material.PART, NO_TOOL, BlockSoundGroup.STONE).hardnessAndResistance(0.1F);
    static Block.Settings WOODEN_RAIL = builder(Material.PART, NO_TOOL, BlockSoundGroup.WOOD).doesNotBlockMovement().hardnessAndResistance(0.2F);
    static Block.Settings PUNJI = builder(Material.PLANT, NO_TOOL, BlockSoundGroup.GRASS).hardnessAndResistance(3.0F);
    
    private BlockProperties() {}
    
    /**
     * We use this builder to ensure that our blocks all have the most important properties set.
     * This way it'll stick out if a block doesn't have a tooltype or sound set.
     * It may be a bit less clear at first, since the actual builder methods tell you what each value means,
     * but as long as we don't statically import the enums it should be just as readable.
     */
    private static Block.Settings builder(Material material, @Nullable ToolType toolType, BlockSoundGroup soundType) {
        //noinspection ConstantConditions
        return Block.Settings.create(material).harvestTool(toolType).sound(soundType);
    }
}
