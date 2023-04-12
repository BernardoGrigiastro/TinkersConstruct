package slimeknights.tconstruct.gadgets.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import slimeknights.tconstruct.library.TinkerRegistry;

public class SpaghettiItem extends Item {
    
    public SpaghettiItem() {
        super((new Settings()).group(TinkerRegistry.tabGadgets));
    }
    
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void fillItemGroup(ItemGroup group, DefaultedList<ItemStack> items) {
    
    }
}
