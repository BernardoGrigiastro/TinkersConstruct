package slimeknights.tconstruct.library.capability.piggyback;

import net.minecraft.client.network.packet.EntityPassengersSetS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import slimeknights.tconstruct.common.TinkerNetwork;

import java.util.List;

public class TinkerPiggybackHandler implements ITinkerPiggyback {
    
    private PlayerEntity riddenPlayer;
    private List<Entity> lastPassengers;
    
    @Override
    public void setRiddenPlayer(PlayerEntity player) {
        this.riddenPlayer = player;
    }
    
    @Override
    public void updatePassengers() {
        if (this.riddenPlayer != null) {
            // tell the player itself if his riders changed serverside
            if (!this.riddenPlayer.getPassengers().equals(this.lastPassengers)) {
                if (this.riddenPlayer instanceof ServerPlayerEntity) {
                    TinkerNetwork.instance.sendVanillaPacket(this.riddenPlayer, new EntityPassengersSetS2CPacket(this.riddenPlayer));
                }
            }
            this.lastPassengers = this.riddenPlayer.getPassengers();
        }
    }
}
