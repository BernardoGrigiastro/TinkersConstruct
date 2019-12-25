package slimeknights.tconstruct.tools.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import slimeknights.mantle.network.AbstractPacketThreadsafe;
import slimeknights.mantle.tileentity.TileInventory;

public class InventorySlotSyncPacket extends AbstractPacketThreadsafe {

    public ItemStack itemStack;
    public int slot;
    public BlockPos pos;

    public InventorySlotSyncPacket() {
    }

    public InventorySlotSyncPacket(ItemStack itemStack, int slot, BlockPos pos) {
        this.itemStack = itemStack;
        this.pos = pos;
        this.slot = slot;
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient netHandler) {
        // only ever sent to players in the same dimension as the position
        BlockEntity tileEntity = MinecraftClient.getMinecraft().player.getEntityWorld().getTileEntity(pos);
        if (tileEntity == null || !(tileEntity instanceof TileInventory)) {
            return;
        }

        TileInventory tile = (TileInventory) tileEntity;
        tile.setInventorySlotContents(slot, itemStack);
        MinecraftClient.getMinecraft().worldRenderer.notifyBlockUpdate(null, pos, null, null, 0);
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer netHandler) {
        // only send to clients
        throw new UnsupportedOperationException("Clientside only");
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = readPos(buf);
        this.slot = buf.readShort();
        this.itemStack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writePos(pos, buf);
        buf.writeShort(slot);
        ByteBufUtils.writeItemStack(buf, itemStack);
    }
}
