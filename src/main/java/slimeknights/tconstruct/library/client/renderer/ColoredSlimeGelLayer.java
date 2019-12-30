package slimeknights.tconstruct.library.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.library.client.RenderUtil;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class ColoredSlimeGelLayer<T extends Entity> extends FeatureRenderer<T, SlimeEntityModel<T>> {
    
    public static boolean magicMushrooms = false;
    private final EntityModel<T> slimeModel = new SlimeEntityModel<>(0);
    private final int color;
    private float ticking;
    
    public ColoredSlimeGelLayer(FeatureRendererContext<T, SlimeEntityModel<T>> entityRenderer, int color) {
        super(entityRenderer);
        this.color = color;
    }
    
    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.ticking += partialTicks;
        
        if (!entityIn.isInvisible()) {
            if (magicMushrooms) {
                RenderUtil.setColorRGBA(Color.HSBtoRGB(this.ticking / 100f, 0.65f, 0.8f) | (this.color & (0xFF << 24)));
            } else {
                RenderUtil.setColorRGBA(this.color);
            }
            
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            
            this.getEntityModel().setModelAttributes(this.slimeModel);
            this.slimeModel.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}