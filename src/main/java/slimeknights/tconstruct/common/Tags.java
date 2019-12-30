package slimeknights.tconstruct.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class Tags {
    
    public static class Blocks {
        
        public static final Tag<Block> SLIMY_LOGS = tag("slimy_logs");
        public static final Tag<Block> SLIMY_LEAVES = tag("slimy_leaves");
        
        public static final Tag<Block> STORAGE_BLOCKS_COBALT = forgeTag("storage_blocks/cobalt");
        public static final Tag<Block> STORAGE_BLOCKS_ARDITE = forgeTag("storage_blocks/ardite");
        public static final Tag<Block> STORAGE_BLOCKS_MANYULLYN = forgeTag("storage_blocks/manyullyn");
        public static final Tag<Block> STORAGE_BLOCKS_KNIGHTSLIME = forgeTag("storage_blocks/knightslime");
        public static final Tag<Block> STORAGE_BLOCKS_PIGIRON = forgeTag("storage_blocks/pigiron");
        public static final Tag<Block> STORAGE_BLOCKS_ALUBRASS = forgeTag("storage_blocks/alubrass");
        
        private static Tag<Block> tag(String name) {
            return new BlockTags.CachingTag(new Identifier("tconstruct", name));
        }
        
        private static Tag<Block> forgeTag(String name) {
            return new BlockTags.CachingTag(new Identifier("forge", name));
        }
    }
    
    public static class Items {
        
        public static final Tag<Item> SLIMY_LOGS = tag("slimy_logs");
        public static final Tag<Item> SLIMY_LEAVES = tag("slimy_leaves");
        
        public static final Tag<Item> STORAGE_BLOCKS_COBALT = forgeTag("storage_blocks/cobalt");
        public static final Tag<Item> STORAGE_BLOCKS_ARDITE = forgeTag("storage_blocks/ardite");
        public static final Tag<Item> STORAGE_BLOCKS_MANYULLYN = forgeTag("storage_blocks/manyullyn");
        public static final Tag<Item> STORAGE_BLOCKS_KNIGHTSLIME = forgeTag("storage_blocks/knightslime");
        public static final Tag<Item> STORAGE_BLOCKS_PIGIRON = forgeTag("storage_blocks/pigiron");
        public static final Tag<Item> STORAGE_BLOCKS_ALUBRASS = forgeTag("storage_blocks/alubrass");
        
        public static final Tag<Item> GREEN_SLIMEBALL = forgeTag("slimeball/green");
        public static final Tag<Item> BLUE_SLIMEBALL = forgeTag("slimeball/blue");
        public static final Tag<Item> PURPLE_SLIMEBALL = forgeTag("slimeball/purple");
        public static final Tag<Item> BLOOD_SLIMEBALL = forgeTag("slimeball/blood");
        public static final Tag<Item> MAGMA_SLIMEBALL = forgeTag("slimeball/magma");
        
        public static final Tag<Item> INGOTS_COBALT = forgeTag("ingots/cobalt");
        public static final Tag<Item> INGOTS_ARDITE = forgeTag("ingots/ardite");
        public static final Tag<Item> INGOTS_MANYULLYN = forgeTag("ingots/manyullyn");
        public static final Tag<Item> INGOTS_KNIGHTSLIME = forgeTag("ingots/knightslime");
        public static final Tag<Item> INGOTS_PIGIRON = forgeTag("ingots/pigiron");
        public static final Tag<Item> INGOTS_ALUBRASS = forgeTag("ingots/alubrass");
        
        public static final Tag<Item> NUGGETS_COBALT = forgeTag("nuggets/cobalt");
        public static final Tag<Item> NUGGETS_ARDITE = forgeTag("nuggets/ardite");
        public static final Tag<Item> NUGGETS_MANYULLYN = forgeTag("nuggets/manyullyn");
        public static final Tag<Item> NUGGETS_KNIGHTSLIME = forgeTag("nuggets/knightslime");
        public static final Tag<Item> NUGGETS_PIGIRON = forgeTag("nuggets/pigiron");
        public static final Tag<Item> NUGGETS_ALUBRASS = forgeTag("nuggets/alubrass");
        
        private static Tag<Item> tag(String name) {
            return new ItemTags.CachingTag(new Identifier("tconstruct", name));
        }
        
        private static Tag<Item> forgeTag(String name) {
            return new ItemTags.CachingTag(new Identifier("forge", name));
        }
    }
}