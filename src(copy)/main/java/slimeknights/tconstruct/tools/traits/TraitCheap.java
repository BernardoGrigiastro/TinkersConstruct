package slimeknights.tconstruct.tools.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextFormat;
import slimeknights.tconstruct.library.traits.AbstractTrait;

// You're so cheap, repairing gives you a bonus
public class TraitCheap extends AbstractTrait {

    public TraitCheap() {
        super("cheap", TextFormat.field_1063);
    }

    @Override
    public int onToolHeal(ItemStack tool, int amount, int newAmount, EntityLivingBase entity) {
        // 5% bonus durability repaired!
        return newAmount + amount * 5 / 100;
    }
}
