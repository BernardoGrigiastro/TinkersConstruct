package slimeknights.tconstruct.library.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextFormat;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;

import javax.annotation.Nullable;

public abstract class AbstractProjectileTrait extends AbstractTrait implements IProjectileTrait {

    public AbstractProjectileTrait(String identifier, TextFormat color) {
        super(identifier, color);
    }

    public AbstractProjectileTrait(String identifier, int color) {
        super(identifier, color);
    }

    @Override
    public void onLaunch(EntityProjectileBase projectileBase, World world, @Nullable EntityLivingBase shooter) {

    }

    @Override
    public void onProjectileUpdate(EntityProjectileBase projectile, World world, ItemStack toolStack) {

    }

    @Override
    public void onMovement(EntityProjectileBase projectile, World world, double slowdown) {

    }

    @Override
    public void afterHit(EntityProjectileBase projectile, World world, ItemStack ammoStack, EntityLivingBase attacker, Entity target, double impactSpeed) {

    }
}
