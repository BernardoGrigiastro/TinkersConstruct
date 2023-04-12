package slimeknights.tconstruct.gadgets.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import slimeknights.tconstruct.blocks.CommonBlocks;
import slimeknights.tconstruct.gadgets.TinkerGadgets;
import slimeknights.tconstruct.items.GadgetItems;

import javax.annotation.Nonnull;

public class GlowballEntity extends ThrownItemEntity implements IEntityAdditionalSpawnData {
    
    public GlowballEntity(EntityType<? extends GlowballEntity> p_i50159_1_, World p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }
    
    public GlowballEntity(World worldIn, LivingEntity throwerIn) {
        super(TinkerGadgets.throwable_glow_ball, throwerIn, worldIn);
    }
    
    public GlowballEntity(World worldIn, double x, double y, double z) {
        super(TinkerGadgets.throwable_glow_ball, x, y, z, worldIn);
    }
    
    @Override
    protected Item getDefaultItem() {
        return GadgetItems.glow_ball;
    }
    
    @Override
    protected void onImpact(HitResult result) {
        if (!this.world.isClient) {
            BlockPos position = null;
            Direction direction = Direction.field_11033;
            
            if (result.getType() == HitResult.Type.field_1331) {
                position = ((EntityHitResult) result).getEntity().getPosition();
            }
            
            if (result.getType() == HitResult.Type.field_1332) {
                BlockHitResult blockraytraceresult = (BlockHitResult) result;
                position = blockraytraceresult.getPos().offset(blockraytraceresult.getFace());
                direction = blockraytraceresult.getFace().getOpposite();
            }
            
            if (position != null) {
                CommonBlocks.glow.addGlow(this.world, position, direction);
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
