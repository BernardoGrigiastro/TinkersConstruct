package slimeknights.tconstruct.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.registry.BaseRegistryAdapter;
import slimeknights.tconstruct.library.TinkerRegistry;

import static slimeknights.tconstruct.common.TinkerPulse.injected;

@ObjectHolder(TConstruct.modID)
@Mod.EventBusSubscriber(modid = TConstruct.modID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ToolItems {
    
    public static final Item green_slime_crystal = injected();
    public static final Item blue_slime_crystal = injected();
    public static final Item magma_slime_crystal = injected();
    public static final Item width_expander = injected();
    public static final Item height_expander = injected();
    public static final Item reinforcement = injected();
    public static final Item silky_cloth = injected();
    public static final Item silky_jewel = injected();
    public static final Item necrotic_bone = injected();
    public static final Item moss = injected();
    public static final Item mending_moss = injected();
    public static final Item creative_modifier = injected();
    
    private ToolItems() {}
    
    @SubscribeEvent
    static void registerItems(final RegistryEvent.Register<Item> event) {
        BaseRegistryAdapter<Item> registry = new BaseRegistryAdapter<>(event.getRegistry());
        ItemGroup tabGeneral = TinkerRegistry.tabGeneral;
        
        // modifier items
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "green_slime_crystal");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "blue_slime_crystal");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "magma_slime_crystal");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "width_expander");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "height_expander");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "reinforcement");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "silky_cloth");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "silky_jewel");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "necrotic_bone");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "moss");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "mending_moss");
        registry.register(new Item(new Item.Settings().group(tabGeneral)), "creative_modifier");
    }
}
