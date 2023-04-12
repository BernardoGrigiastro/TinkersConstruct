package slimeknights.tconstruct.common;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.mantle.block.BlockStairsBase;
import slimeknights.mantle.block.EnumBlock;
import slimeknights.mantle.block.EnumBlockSlab;
import slimeknights.mantle.item.ItemBlockMeta;
import slimeknights.mantle.item.ItemBlockSlab;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.gadgets.TinkerGadgets;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.plugin.Chisel;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.Locale;

/**
 * Just a small helper class that provides some function for cleaner Pulses.
 * <p>
 * Items should be registered during PreInit
 * <p>
 * Models should be registered during Init
 */
// MANTLE
public abstract class TinkerPulse {
    
    protected static boolean isToolsLoaded() {
        return TConstruct.pulseManager.isPulseLoaded(TinkerTools.PulseId);
    }
    
    protected static boolean isSmelteryLoaded() {
        return TConstruct.pulseManager.isPulseLoaded(TinkerSmeltery.PulseId);
    }
    
    protected static boolean isWorldLoaded() {
        return TConstruct.pulseManager.isPulseLoaded(TinkerWorld.PulseId);
    }
    
    protected static boolean isGadgetsLoaded() {
        return TConstruct.pulseManager.isPulseLoaded(TinkerGadgets.PulseId);
    }
    
    protected static boolean isChiselPluginLoaded() {
        return TConstruct.pulseManager.isPulseLoaded(Chisel.PulseId);
    }
    
    protected static <T extends Block> T registerBlock(Registry<Block> registry, T block, String name) {
        if (!name.equals(name.toLowerCase(Locale.US))) {
            throw new IllegalArgumentException(String.format("Unlocalized names need to be all lowercase! Block: %s", name));
        }
        
        Identifier id = Util.getResource(name);
        register(registry, block, id);
        return block;
    }
    
    protected static <E extends Enum<E> & EnumBlock.IEnumMeta & StringRepresentable> BlockStairsBase registerBlockStairsFrom(IForgeRegistry<Block> registry, EnumBlock<E> block, E value, String name) {
        return registerBlock(registry, new BlockStairsBase(block.getDefaultState().withProperty(block.prop, value)), name);
    }
    
    protected static <T extends Block> T registerItemBlock(Registry<Item> registry, T block) {
        return registerItemBlock(registry, block, new Item.Settings());
    }
    
    protected static <T extends Block> T registerItemBlock(Registry<Item> registry, T block, ItemGroup group) {
        return registerItemBlock(registry, block, new Item.Settings().itemGroup(group));
    }
    
    protected static <T extends Block> T registerItemBlock(Registry<Item> registry, T block, Item.Settings settings) {
        BlockItem itemBlock = new BlockItem(block, settings);
        register(registry, itemBlock, Registry.BLOCK.getId(block));
        return block;
    }
    
    protected static <T extends EnumBlock<?>> T registerEnumItemBlock(IForgeRegistry<Item> registry, T block) {
        ItemBlock itemBlock = new ItemBlockMeta(block);
        
        itemBlock.setUnlocalizedName(block.getUnlocalizedName());
        
        register(registry, itemBlock, block.getRegistryName());
        ItemBlockMeta.setMappingProperty(block, block.prop);
        return block;
    }
    
    @SuppressWarnings("unchecked")
    protected static <T extends Block> T registerItemBlock(IForgeRegistry<Item> registry, ItemBlock itemBlock) {
        itemBlock.setUnlocalizedName(itemBlock.getBlock().getUnlocalizedName());
        
        register(registry, itemBlock, itemBlock.getBlock().getRegistryName());
        return (T) itemBlock.getBlock();
    }
    
    @SuppressWarnings("unchecked")
    protected static <T extends Block> T registerItemBlockProp(IForgeRegistry<Item> registry, ItemBlock itemBlock, IProperty<?> property) {
        itemBlock.setUnlocalizedName(itemBlock.getBlock().getUnlocalizedName());
        
        register(registry, itemBlock, itemBlock.getBlock().getRegistryName());
        ItemBlockMeta.setMappingProperty(itemBlock.getBlock(), property);
        return (T) itemBlock.getBlock();
    }
    
    protected static <T extends EnumBlockSlab<?>> T registerEnumItemBlockSlab(IForgeRegistry<Item> registry, T block) {
        @SuppressWarnings({"unchecked", "rawtypes"}) ItemBlock itemBlock = new ItemBlockSlab(block);
        
        itemBlock.setUnlocalizedName(block.getUnlocalizedName());
        
        register(registry, itemBlock, block.getRegistryName());
        ItemBlockMeta.setMappingProperty(block, block.prop);
        return block;
    }
    
    /**
     * Sets the correct unlocalized name and registers the item.
     */
    protected static <T extends Item> T registerItem(Registry<Item> registry, T item, String name) {
        if (!name.equals(name.toLowerCase(Locale.US))) {
            throw new IllegalArgumentException(String.format("Unlocalized names need to be all lowercase! Item: %s", name));
        }
        return Registry.register(registry, Util.getResource(name), item);
    }
    
    protected static <T> T register(Registry<T> registry, T thing, Identifier name) {
        return Registry.register(registry, name, thing);
    }
    
    protected static void registerTE(Class<? extends BlockEntity> teClazz, String name) {
        if (!name.equals(name.toLowerCase(Locale.US))) {
            throw new IllegalArgumentException(String.format("Unlocalized names need to be all lowercase! TE: %s", name));
        }
        
        GameRegistry.registerTileEntity(teClazz, Util.prefix(name));
    }
}
