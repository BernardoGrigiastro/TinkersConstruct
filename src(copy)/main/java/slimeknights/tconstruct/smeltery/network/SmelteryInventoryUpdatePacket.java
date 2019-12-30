package slimeknights.tconstruct.smeltery.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import slimeknights.mantle.network.AbstractPacketThreadsafe;

// Sent to the client when smeltery contents get updated on the server
// Needed to display items without open GUI
public class SmelteryInventoryUpdatePacket extends AbstractPacketThreadsafe {

    public int slot;
    public ItemStack stack;
    public BlockPos pos;

    public SmelteryInventoryUpdatePacket() {
    }

    public SmelteryInventoryUpdatePacket(ItemStack stack, int slot, BlockPos pos) {
        this.slot = slot;
        this.stack = stack;
        this.pos = pos;
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient netHandler) {
        BlockEntity te = MinecraftClient.getMinecraft().world.getTileEntity(pos);
        if (te instanceof Inventory) {
            ((Inventory) te).setInventorySlotContents(slot, stack);
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer netHandler) {
        // Clientside only
        throw new UnsupportedOperationException("Clientside only");
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
        stack = ByteBufUtils.readItemStack(buf);
        pos = readPos(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        ByteBufUtils.writeItemStack(buf, stack);
        writePos(pos, buf);
    }
}
