package slimeknights.tconstruct.smeltery.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import slimeknights.mantle.network.AbstractPacketThreadsafe;
import slimeknights.tconstruct.library.smeltery.ISmelteryTankHandler;

import java.util.ArrayList;
import java.util.List;

public class SmelteryFluidUpdatePacket extends AbstractPacketThreadsafe {

    public BlockPos pos;
    public List<FluidStack> liquids;

    public SmelteryFluidUpdatePacket() {
    }

    public SmelteryFluidUpdatePacket(BlockPos pos, List<FluidStack> liquids) {
        this.pos = pos;
        this.liquids = liquids;
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient netHandler) {
        BlockEntity te = MinecraftClient.getMinecraft().world.getTileEntity(pos);
        if (te instanceof ISmelteryTankHandler) {
            ISmelteryTankHandler handler = (ISmelteryTankHandler) te;
            handler.updateFluidsFromPacket(liquids);
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer netHandler) {
        // Clientside only
        throw new UnsupportedOperationException("Clientside only");
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = readPos(buf);
        int size = buf.readInt();
        liquids = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            NBTTagCompound fluidTag = ByteBufUtils.readTag(buf);
            FluidStack liquid = FluidStack.loadFluidStackFromNBT(fluidTag);
            liquids.add(liquid);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writePos(pos, buf);
        buf.writeInt(liquids.size());
        for (FluidStack liquid : liquids) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            liquid.writeToNBT(fluidTag);
            ByteBufUtils.writeTag(buf, fluidTag);
        }
    }
}
