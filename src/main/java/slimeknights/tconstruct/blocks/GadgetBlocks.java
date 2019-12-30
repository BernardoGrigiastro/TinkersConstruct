package slimeknights.tconstruct.blocks;

import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerPulse;
import slimeknights.tconstruct.common.registry.BlockItemRegistryAdapter;
import slimeknights.tconstruct.common.registry.BlockRegistryAdapter;
import slimeknights.tconstruct.gadgets.block.DropperRailBlock;
import slimeknights.tconstruct.gadgets.block.PunjiBlock;
import slimeknights.tconstruct.library.TinkerRegistry;

@ObjectHolder(TConstruct.modID)
@Mod.EventBusSubscriber(modid = TConstruct.modID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GadgetBlocks {
    
    public static final LadderBlock stone_ladder = TinkerPulse.injected();
    public static final TorchBlock stone_torch = TinkerPulse.injected();
    public static final WallTorchBlock wall_stone_torch = TinkerPulse.injected();
    public static final PunjiBlock punji = TinkerPulse.injected();
    public static final RailBlock wooden_rail = TinkerPulse.injected();
    public static final DropperRailBlock wooden_dropper_rail = TinkerPulse.injected();
    
    private GadgetBlocks() {}
    
    @SubscribeEvent
    static void registerBlocks(final RegistryEvent.Register<Block> event) {
        BlockRegistryAdapter registry = new BlockRegistryAdapter(event.getRegistry());
        
        registry.register(new LadderBlock(BlockProperties.STONE_LADDER) {}, "stone_ladder");
        
        registry.register(new TorchBlock(BlockProperties.STONE_TORCH) {}, "stone_torch");
        registry.register(new WallTorchBlock(BlockProperties.STONE_TORCH) {}, "wall_stone_torch");
        
        registry.register(new RailBlock(BlockProperties.WOODEN_RAIL) {}, "wooden_rail");
        registry.register(new DropperRailBlock(BlockProperties.WOODEN_RAIL), "wooden_dropper_rail");
        
        registry.register(new PunjiBlock(BlockProperties.PUNJI), "punji");
    }
    
    @SubscribeEvent
    static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        BlockItemRegistryAdapter registry = new BlockItemRegistryAdapter(event.getRegistry(), TinkerRegistry.tabGadgets);
        
        registry.registerBlockItem(GadgetBlocks.stone_ladder);
        
        registry.registerBlockItem(new WallStandingBlockItem(GadgetBlocks.stone_torch, GadgetBlocks.wall_stone_torch, (new Item.Settings()).group(TinkerRegistry.tabGadgets)));
        
        registry.registerBlockItem(GadgetBlocks.punji);
        
        registry.registerBlockItem(GadgetBlocks.wooden_rail);
        registry.registerBlockItem(GadgetBlocks.wooden_dropper_rail);
    }
}
