package slimeknights.tconstruct.library.tools.ranged;

import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface ILauncher {

  void modifyProjectileAttributes(Multimap<String, EntityAttributeModifier> projectileAttributes, @Nullable ItemStack launcher, ItemStack projectile, float power);
}
