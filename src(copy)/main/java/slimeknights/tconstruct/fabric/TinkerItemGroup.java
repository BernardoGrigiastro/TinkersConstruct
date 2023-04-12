package slimeknights.tconstruct.fabric;

import net.fabricmc.fabric.impl.itemgroup.ItemGroupExtensions;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public interface TinkerItemGroup {
    
    public static ItemGroup newItemGroup(String name, ItemStack icon) {
        ((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
        return new SelfItemGroup(ItemGroup.GROUPS.length - 1, name, icon);
    }
    
    void setIcon(ItemStack icon);
    
    class SelfItemGroup extends ItemGroup implements TinkerItemGroup {
        private ItemStack icon;
        
        public SelfItemGroup(int i, String string, ItemStack icon) {
            super(i, string);
            this.icon = icon;
        }
        
        @Override
        public void setIcon(ItemStack icon) {
            this.icon = icon;
        }
        
        @Override
        public ItemStack createIcon() {
            return icon;
        }
    }
    
}
