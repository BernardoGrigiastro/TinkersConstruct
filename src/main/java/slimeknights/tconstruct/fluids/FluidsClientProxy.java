package slimeknights.tconstruct.fluids;

import net.minecraft.client.MinecraftClient;
import slimeknights.tconstruct.common.ClientProxy;

public class FluidsClientProxy extends ClientProxy {
    
    public static MinecraftClient minecraft = MinecraftClient.getInstance();
    
    @Override
    public void construct() {
        super.construct();
    }
    
    @Override
    public void init() {
    }
}
