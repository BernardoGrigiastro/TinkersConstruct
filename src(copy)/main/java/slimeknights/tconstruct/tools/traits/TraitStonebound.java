package slimeknights.tconstruct.tools.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.text.TextFormat;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;

import java.util.List;

public class TraitStonebound extends AbstractTrait {

    public TraitStonebound() {
        super("stonebound", TextFormat.field_1063);
    }

    private double calcBonus(ItemStack tool) {
        int durability = ToolHelper.getCurrentDurability(tool);
        int maxDurability = ToolHelper.getMaxDurability(tool);

        // old tcon stonebound formula
        return Math.log((maxDurability - durability) / 72d + 1d) * 2;
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        if (ToolHelper.isToolEffective2(tool, event.getState())) {

            event.setNewSpeed((float) (event.getNewSpeed() + calcBonus(tool)));
        }
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        String loc = String.format(LOC_Extra, getModifierIdentifier());

        return ImmutableList.of(Util.translateFormatted(loc, Util.df.format(calcBonus(tool))));
    }
}
