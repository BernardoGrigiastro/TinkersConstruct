package slimeknights.tconstruct.library.tools;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.world.World;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;

import javax.annotation.Nullable;
import java.util.List;

public class Shard extends ToolPart {

    public Shard() {
        super(Material.VALUE_Shard);
    }

    @Override
    public void getSubItems(CreativeTabs tab, DefaultedList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            // this adds a variant of each material to the creative menu
            for (Material mat : TinkerRegistry.getAllMaterials()) {
                if (mat.hasStats(MaterialTypes.HEAD) && (mat.isCraftable() || mat.isCastable())) {
                    subItems.add(getItemstackWithMaterial(mat));
                    if (!Config.listAllPartMaterials) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean canUseMaterial(Material mat) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, TooltipContext flagIn) {
        // no stats n stuff
    }
}
