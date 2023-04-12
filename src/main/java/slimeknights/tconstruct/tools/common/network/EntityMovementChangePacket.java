package slimeknights.tconstruct.tools.common.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.PacketByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import slimeknights.mantle.network.AbstractPacket;

import java.util.function.Supplier;

public class EntityMovementChangePacket extends AbstractPacket {
    
    public double x;
    public double y;
    public double z;
    private int entityID;
    private float yaw;
    private float pitch;
    
    public EntityMovementChangePacket(Entity entity) {
        this.entityID = entity.getEntityId();
        this.x = entity.getMotion().x;
        this.y = entity.getMotion().y;
        this.z = entity.getMotion().z;
        this.yaw = entity.yaw;
        this.pitch = entity.pitch;
    }
    
    public EntityMovementChangePacket(PacketByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.yaw = buffer.readFloat();
        this.pitch = buffer.readFloat();
    }
    
    @Override
    public void encode(PacketByteBuf packetBuffer) {
        packetBuffer.writeInt(this.entityID);
        packetBuffer.writeDouble(this.x);
        packetBuffer.writeDouble(this.y);
        packetBuffer.writeDouble(this.z);
        packetBuffer.writeFloat(this.yaw);
        packetBuffer.writeFloat(this.pitch);
    }
    
    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            Entity entity = MinecraftClient.getInstance().world.getEntityByID(this.entityID);
            if (supplier.get().getSender() != null && entity != null) {
                entity.setMotion(this.x, this.y, this.z);
                entity.yaw = this.yaw;
                entity.pitch = this.pitch;
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
