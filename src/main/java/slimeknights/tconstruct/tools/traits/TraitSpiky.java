package slimeknights.tconstruct.tools.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextFormat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.shared.client.ParticleEffect;
import slimeknights.tconstruct.tools.TinkerTools;

public class TraitSpiky extends AbstractTrait {

  public TraitSpiky() {
    super("spiky", TextFormat.field_1077);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void onPlayerHurt(ItemStack tool, EntityPlayer player, EntityLivingBase attacker, LivingHurtEvent event) {
    dealSpikyDamage(false, tool, player, attacker, event);
  }

  @Override
  public void onBlock(ItemStack tool, EntityPlayer player, LivingHurtEvent event) {
    Entity target = event.getSource().getTrueSource();
    dealSpikyDamage(true, tool, player, target, event);
  }

  private void dealSpikyDamage(boolean isBlocking, ItemStack tool, EntityPlayer player, Entity target, LivingHurtEvent event) {
    if(target instanceof EntityLivingBase && target.isEntityAlive() && target != player && !isThornsDamage(event.getSource())) {
      float damage = ToolHelper.getActualDamage(tool, player);
      if(!isBlocking) {
        damage /= 2;
      }
      EntityDamageSource damageSource = new EntityDamageSource(DamageSource.CACTUS.name, player);
      damageSource.setDamageBypassesArmor();
      damageSource.setDamageIsAbsolute();
      damageSource.setIsThornsDamage();

      int oldHurtResistantTime = target.field_6008;
      if(attackEntitySecondary(damageSource, damage, target, true, false)) {
        TinkerTools.proxy.spawnEffectParticle(ParticleEffect.Type.HEART_CACTUS, target, 1);
      }
      target.field_6008 = oldHurtResistantTime; // reset to old time
    }
  }

  private boolean isThornsDamage(DamageSource damageSource) {
    return damageSource instanceof EntityDamageSource && ((EntityDamageSource) damageSource).getIsThornsDamage();
  }
}
