package slimeknights.tconstruct.gadgets.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import slimeknights.tconstruct.gadgets.Exploder;
import slimeknights.tconstruct.gadgets.TinkerGadgets;
import slimeknights.tconstruct.items.GadgetItems;

import javax.annotation.Nonnull;

public class EflnBallEntity extends ThrownItemEntity implements IEntityAdditionalSpawnData {
    
    public EflnBallEntity(EntityType<? extends EflnBallEntity> p_i50159_1_, World p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }
    
    public EflnBallEntity(World worldIn, LivingEntity throwerIn) {
        super(TinkerGadgets.throwable_efln_ball, throwerIn, worldIn);
    }
    
    public EflnBallEntity(World worldIn, double x, double y, double z) {
        super(TinkerGadgets.throwable_efln_ball, x, y, z, worldIn);
    }
    
    @Override
    protected Item getDefaultItem() {
        return GadgetItems.efln_ball;
    }
    
    @Override
    protected void onImpact(HitResult result) {
        if (!this.world.isClient) {
            EFLNExplosion explosion = new EFLNExplosion(this.world, this, this.x, this.y, this.z, 6f, false, Explosion.DestructionType.field_18685);
            if (!ForgeEventFactory.onExplosionStart(this.world, explosion)) {
                Exploder.startExplosion(this.world, explosion, this, new BlockPos(this.x, this.y, this.z), 6f, 6f);
            }
        }
        
        if (!this.world.isClient) {
            this.world.setEntityState(this, (byte) 3);
            this.remove();
        }
    }
    
    @Override
    public void writeSpawnData(PacketByteBuf buffer) {
        buffer.writeItemStack(this.func_213882_k());
    }
    
    @Override
    public void readSpawnData(PacketByteBuf additionalData) {
        this.setItem(additionalData.readItemStack());
    }
    
    @Nonnull
    @Override
    public Packet<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
