package slimeknights.tconstruct.library;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.apache.logging.log4j.Logger;
import slimeknights.mantle.client.CreativeTab;

public final class TinkerRegistry {
    
    // the logger for the library
    public static final Logger log = Util.getLogger("API");
    /*---------------------------------------------------------------------------
    | ITEM GROUPS                                                               |
    ---------------------------------------------------------------------------*/
    public static CreativeTab tabGeneral = new CreativeTab("tinkers_general", new ItemStack(Items.field_8777));
    public static CreativeTab tabTools = new CreativeTab("tinkers_tools", new ItemStack(Items.field_8403));
    public static CreativeTab tabParts = new CreativeTab("tinkers_tool_parts", new ItemStack(Items.field_8600));
    public static CreativeTab tabSmeltery = new CreativeTab("tinkers_smeltery", new ItemStack(Item.getItemFromBlock(Blocks.field_10056)));
    public static CreativeTab tabWorld = new CreativeTab("tinkers_world", new ItemStack(Item.getItemFromBlock(Blocks.field_10030)));
    public static CreativeTab tabGadgets = new CreativeTab("tinkers_gadgets", new ItemStack(Blocks.field_10375));
    private TinkerRegistry() {
    }
    
    /*---------------------------------------------------------------------------
    | Traceability & Internal stuff                                             |
    ---------------------------------------------------------------------------*/
    private static void error(String message, Object... params) {
        throw new TinkerAPIException(String.format(message, params));
    }
}
