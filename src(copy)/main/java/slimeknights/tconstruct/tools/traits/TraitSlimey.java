package slimeknights.tconstruct.tools.traits;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextFormat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;

import java.lang.reflect.InvocationTargetException;

public class TraitSlimey extends AbstractTrait {

    private static float chance = 0.0033f;

    protected final Class<? extends EntitySlime> slime;

    public TraitSlimey(String suffix, Class<? extends EntitySlime> slime) {
        super("slimey_" + suffix, TextFormat.field_1060);
        this.slime = slime;
    }

    @Override
    public String getLocalizedName() {
        return Util.translate(String.format(LOC_Name, "slimey"));
    }

    @Override
    public String getLocalizedDesc() {
        return Util.translate(String.format(LOC_Desc, "slimey"));
    }

    @Override
    public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        if (wasEffective && !world.isClient && random.nextFloat() < chance) {
            spawnSlime(player, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world);
        }
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (!target.isEntityAlive() && !target.getEntityWorld().isClient && random.nextFloat() < chance) {
            spawnSlime(player, target.x, target.y, target.z, target.getEntityWorld());
        }
    }

    protected void spawnSlime(EntityLivingBase player, double x, double y, double z, World world) {
        try {
            EntitySlime entity = slime.getConstructor(World.class).newInstance(world);
            entity.setSlimeSize(1, true);
            entity.setPosition(x, y, z);
            world.spawnEntity(entity);
            entity.setLastAttackedEntity(player);
            entity.playLivingSound();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
