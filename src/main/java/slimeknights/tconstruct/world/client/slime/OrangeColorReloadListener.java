package slimeknights.tconstruct.world.client.slime;

import net.minecraft.client.util.RawTextureDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.world.client.SlimeColorizer;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class OrangeColorReloadListener extends SinglePreparationResourceReloadListener<int[]> {
    
    private static final Identifier ORANGE_GRASS_LOCATION = Util.getResource("textures/colormap/orange_grass_color.png");
    
    /**
     * Performs any reloading that can be done off-thread, such as file IO
     */
    @Override
    protected int[] prepare(ResourceManager resourceManager, Profiler profiler) {
        try {
            return RawTextureDataLoader.loadColors(resourceManager, ORANGE_GRASS_LOCATION);
        } catch (IOException ioexception) {
            throw new IllegalStateException("Failed to load orange grass color texture", ioexception);
        }
    }
    
    @Override
    protected void apply(int[] buffer, ResourceManager resourceManager, Profiler profiler) {
        SlimeColorizer.setOrangeGrassBiomeColorizer(buffer);
    }
    
    //@Override //Forge: TODO: Filtered resource reloading
    public net.minecraftforge.resource.IResourceType getResourceType() {
        return net.minecraftforge.resource.VanillaResourceType.TEXTURES;
    }
}
