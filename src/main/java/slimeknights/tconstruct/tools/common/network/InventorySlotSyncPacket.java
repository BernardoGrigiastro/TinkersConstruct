package slimeknights.tconstruct.tools.common.network;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import slimeknights.mantle.network.AbstractPacket;
import slimeknights.mantle.tileentity.InventoryTileEntity;

import java.util.function.Supplier;

public class InventorySlotSyncPacket extends AbstractPacket {
    
    public ItemStack itemStack;
    public int slot;
    public BlockPos pos;
    
    public InventorySlotSyncPacket(ItemStack itemStack, int slot, BlockPos pos) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.pos = pos;
    }
    
    public InventorySlotSyncPacket(PacketByteBuf buffer) {
        this.itemStack = buffer.readItemStack();
        this.slot = buffer.readShort();
        this.pos = buffer.readBlockPos();
    }
    
    @Override
    public void encode(PacketByteBuf packetBuffer) {
        packetBuffer.writeItemStack(this.itemStack);
        packetBuffer.writeShort(this.slot);
        packetBuffer.writeBlockPos(this.pos);
    }
    
    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            BlockEntity tileEntity = MinecraftClient.getInstance().player.getEntityWorld().getTileEntity(this.pos);
            if (tileEntity == null || !(tileEntity instanceof InventoryTileEntity)) {
                return;
            }
            
            InventoryTileEntity tile = (InventoryTileEntity) tileEntity;
            tile.setInventorySlotContents(this.slot, this.itemStack);
            MinecraftClient.getInstance().worldRenderer.notifyBlockUpdate(null, this.pos, null, null, 0);
        });
        supplier.get().setPacketHandled(true);
    }
}
