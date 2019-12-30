package slimeknights.tconstruct.common;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import slimeknights.tconstruct.items.CommonItems;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.particle.SlimeFxParticle;

public class ClientProxy extends ServerProxy {
    
    public static final Identifier BOOK_MODIFY = Util.getResource("textures/screen/book/modify.png");
    
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    @Override
    public void spawnSlimeParticle(World world, double x, double y, double z) {
        mc.particleManager.addEffect(new SlimeFxParticle(world, x, y, z, new ItemStack(CommonItems.blue_slime_ball)));
    }
}
