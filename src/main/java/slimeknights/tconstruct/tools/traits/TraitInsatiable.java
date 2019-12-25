package slimeknights.tconstruct.tools.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextFormat;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.potion.TinkerPotion;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitInsatiable extends AbstractTrait {

  public static TinkerPotion Insatiable = new TinkerPotion(Util.getResource("insatiable"), true, false);

  public TraitInsatiable() {
    super("insatiable", TextFormat.field_1064);
  }

  @Override
  public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
    float bonus = Insatiable.getLevel(player) / 3f;
    return super.damage(tool, player, target, damage, newDamage, isCritical) + bonus;
  }

  @Override
  public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
    int level = 1;
    level += Insatiable.getLevel(player);

    level = Math.min(10, level);

    Insatiable.apply(player, 5 * 20, level);
  }

  @Override
  public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
    int level = Insatiable.getLevel(entity) / 3;
    return super.onToolDamage(tool, damage, newDamage, entity) + level;
  }
}
