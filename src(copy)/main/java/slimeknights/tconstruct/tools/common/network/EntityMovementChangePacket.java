package slimeknights.tconstruct.tools.common.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class EntityMovementChangePacket implements Packet {
    
    public int entityID;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    
    public EntityMovementChangePacket() {
    }
    
    public EntityMovementChangePacket(Entity entity) {
        this.entityID = entity.getEntityId();
        this.x = entity.getPos().x;
        this.y = entity.getPos().y;
        this.z = entity.getPos().z;
        this.yaw = entity.yaw;
        this.pitch = entity.pitch;
    }
    
    @Environment(EnvType.CLIENT)
    public void handleClientSafe() {
        Entity entity = net.minecraft.client.MinecraftClient.getInstance().world.getEntityById(entityID);
        if (entity != null) {
            entity.setPosition(x, y, z);
            entity.yaw = yaw;
            entity.pitch = pitch;
        }
    }
    
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
    }
    
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
    }
    
    @Override
    public void read(PacketByteBuf packetByteBuf) throws IOException {
        fromBytes(packetByteBuf);
    }
    
    @Override
    public void write(PacketByteBuf packetByteBuf) throws IOException {
        toBytes(packetByteBuf);
    }
    
    @Override
    public void apply(PacketListener packetListener) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            try {
                getClass().getDeclaredMethod("handleClientSafe").invoke(this);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
