package slimeknights.tconstruct.smeltery.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import slimeknights.mantle.network.AbstractPacketThreadsafe;
import slimeknights.tconstruct.smeltery.tileentity.TileChannel;

public class ChannelFlowPacket extends AbstractPacketThreadsafe {
    protected BlockPos pos;
    protected EnumFacing side;
    protected boolean flow;
    
    public ChannelFlowPacket() {}
    
    public ChannelFlowPacket(BlockPos pos, EnumFacing side, boolean flow) {
        this.pos = pos;
        this.side = side;
        this.flow = flow;
        
    }
    
    @Override
    public void handleClientSafe(NetHandlerPlayClient netHandler) {
        BlockEntity te = MinecraftClient.getMinecraft().world.getTileEntity(pos);
        if (te instanceof TileChannel) {
            ((TileChannel) te).updateFlow(side, flow);
        }
    }
    
    @Override
    public void handleServerSafe(NetHandlerPlayServer netHandler) {
        // clientside only
        throw new UnsupportedOperationException("Serverside only");
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        pos = readPos(buf);
        side = EnumFacing.getFront(buf.readByte());
        flow = buf.readBoolean();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        writePos(pos, buf);
        buf.writeByte(side.getIndex());
        buf.writeBoolean(flow);
    }
}
