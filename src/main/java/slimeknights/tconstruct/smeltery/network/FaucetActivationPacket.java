package slimeknights.tconstruct.smeltery.network;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.smeltery.tileentity.TileFaucet;

/**
 * Sent to clients to activate the faucet animation clientside
 */
public class FaucetActivationPacket extends FluidUpdatePacket {

    public FaucetActivationPacket() {
    }

    public FaucetActivationPacket(BlockPos pos, FluidStack fluid) {
        super(pos, fluid);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient netHandler) {
        BlockEntity te = MinecraftClient.getMinecraft().world.getTileEntity(pos);
        if (te instanceof TileFaucet) {
            ((TileFaucet) te).onActivationPacket(fluid);
        }
    }
}
