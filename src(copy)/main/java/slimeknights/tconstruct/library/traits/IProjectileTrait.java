package slimeknights.tconstruct.library.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;

import javax.annotation.Nullable;

/**
 * Traits that probive extra interactions for projectiles
 */
public interface IProjectileTrait extends ITrait {

    void onLaunch(EntityProjectileBase projectileBase, World world, @Nullable EntityLivingBase shooter);

    /**
     * Called each tick in the entity.
     */
    void onProjectileUpdate(EntityProjectileBase projectile, World world, ItemStack toolStack);


    void onMovement(EntityProjectileBase projectile, World world, double slowdown);

    /**
     * Called after an entity was hit.
     */
    void afterHit(EntityProjectileBase projectile, World world, ItemStack ammoStack, EntityLivingBase attacker, Entity target, double impactSpeed);
}
