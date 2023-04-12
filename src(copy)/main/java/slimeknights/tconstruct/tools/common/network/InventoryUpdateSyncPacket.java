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

public class InventoryUpdateSyncPacket extends AbstractPacketThreadsafe {

    public ItemStack[] itemStacks;
    public BlockPos pos;

    public InventoryUpdateSyncPacket() {
    }

    public InventoryUpdateSyncPacket(ItemStack[] itemStacks, BlockPos pos) {
        this.itemStacks = itemStacks;
        this.pos = pos;
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient netHandler) {
        // only ever sent to players in the same dimension as the position
        BlockEntity tileEntity = MinecraftClient.getMinecraft().player.getEntityWorld().getTileEntity(pos);
        if (tileEntity == null || !(tileEntity instanceof TileInventory)) {
            return;
        }

        TileInventory tile = (TileInventory) tileEntity;
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] == null) {
                continue;
            }
            tile.setInventorySlotContents(i, itemStacks[i]);
        }
        MinecraftClient.getMinecraft().worldRenderer.notifyBlockUpdate(null, pos, null, null, 0);
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer netHandler) {
        // only send to clients
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = readPos(buf);
        int size = buf.readShort();
        this.itemStacks = new ItemStack[size];
        int count = buf.readShort();
        for (int i = 0; i < count; i++) {
            int index = buf.readShort();
            ItemStack stack = ByteBufUtils.readItemStack(buf);
            itemStacks[index] = stack;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writePos(pos, buf);
        buf.writeShort(itemStacks.length);
        int count = 0;
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] != null) {
                count++;
            }
        }
        buf.writeShort(count);
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] == null) {
                continue;
            }

            buf.writeShort(i);
            ByteBufUtils.writeItemStack(buf, itemStacks[i]);
        }
    }
}
