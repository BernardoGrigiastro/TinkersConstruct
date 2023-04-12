package slimeknights.tconstruct.tools.common.network;

import net.minecraft.util.PacketByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import slimeknights.mantle.network.AbstractPacket;

import java.util.function.Supplier;

public class BouncedPacket extends AbstractPacket {
    
    public BouncedPacket() {
    
    }
    
    public BouncedPacket(PacketByteBuf buffer) {
    
    }
    
    @Override
    public void encode(PacketByteBuf packetBuffer) {
    }
    
    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            if (supplier.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                if (supplier.get().getSender() != null) {
                    supplier.get().getSender().fallDistance = 0.0f;
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
    
}
