package slimeknights.tconstruct.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import slimeknights.mantle.network.AbstractPacket;
import slimeknights.mantle.network.NetworkWrapper;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.config.ConfigSyncPacket;
import slimeknights.tconstruct.common.network.SpawnParticlePacket;
import slimeknights.tconstruct.smeltery.network.*;
import slimeknights.tconstruct.tools.common.network.*;

public class TinkerNetwork extends NetworkWrapper {
    
    public static TinkerNetwork instance = new TinkerNetwork();
    
    public TinkerNetwork() {
        super(TConstruct.modID);
    }
    
    public static void sendPacket(Entity player, Packet<?> packet) {
        if (player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).networkHandler != null) {
            ((ServerPlayerEntity) player).networkHandler.sendPacket(packet);
        }
    }
    
    public static void sendToAll(Packet packet) {
        instance.network.sendToAll(packet);
    }
    
    public static void sendTo(Packet packet, ServerPlayerEntity player) {
        instance.network.sendTo(packet, player);
    }
    
    public static void sendToAllAround(Packet packet, NetworkRegistry.TargetPoint point) {
        instance.network.sendToAllAround(packet, point);
    }
    
    public static void sendToDimension(Packet packet, int dimensionId) {
        instance.network.sendToDimension(packet, dimensionId);
    }
    
    public static void sendToServer(Packet packet) {
        instance.network.sendToServer(packet);
    }
    
    public static void sendToClients(ServerWorld world, BlockPos pos, Packet packet) {
        Chunk chunk = world.getChunk(pos);
        for (PlayerEntity player : world.getPlayers()) {
            // only send to relevant players
            if (!(player instanceof ServerPlayerEntity)) {
                continue;
            }
            ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
            if (world.getChunkManager().shouldTickEntity(playerMP)) {
                TinkerNetwork.sendTo(packet, playerMP);
            }
        }
    }
    
    public void setup() {
        // register all the packets
        registerPacketClient(ConfigSyncPacket.class);
        registerPacketClient(SpawnParticlePacket.class);
        
        // TOOLS
        registerPacket(StencilTableSelectionPacket.class);
        registerPacket(PartCrafterSelectionPacket.class);
        registerPacket(ToolStationSelectionPacket.class);
        registerPacket(ToolStationTextPacket.class);
        registerPacketServer(TinkerStationTabPacket.class);
        registerPacketServer(InventoryCraftingSyncPacket.class);
        registerPacketClient(InventorySlotSyncPacket.class);
        registerPacketClient(EntityMovementChangePacket.class);
        registerPacketClient(ToolBreakAnimationPacket.class);
        
        // SMELTERY
        registerPacketClient(SmelteryFluidUpdatePacket.class);
        registerPacketClient(HeatingStructureFuelUpdatePacket.class);
        registerPacketClient(SmelteryInventoryUpdatePacket.class);
        registerPacketServer(SmelteryFluidClicked.class);
        registerPacketClient(FluidUpdatePacket.class);
        registerPacketClient(FaucetActivationPacket.class);
        registerPacketClient(ChannelConnectionPacket.class);
        registerPacketClient(ChannelFlowPacket.class);
        
        // OTHER STUFF
        registerPacketServer(BouncedPacket.class);
        registerPacketClient(LastRecipeMessage.class);
    }
}
