package slimeknights.tconstruct.library.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.RenderUtil;

@OnlyIn(Dist.CLIENT)
public class BlueSlimeRenderer extends MobEntityRenderer<SlimeEntity, SlimeEntityModel<SlimeEntity>> {
    
    public static final Factory BLUE_SLIME_FACTORY = new Factory(0xff67f0f5);
    
    public static final Identifier SLIME_TEXTURE = Util.getResource("textures/entity/slime.png");
    
    private final int color;
    
    public BlueSlimeRenderer(EntityRenderDispatcher renderManager, int color) {
        this(renderManager, color, color);
    }
    
    public BlueSlimeRenderer(EntityRenderDispatcher renderManagerIn, int color, int colorLayer) {
        super(renderManagerIn, new SlimeEntityModel<>(16), 0.25F);
        
        this.color = color;
        
        this.addLayer(new ColoredSlimeGelLayer<>(this, colorLayer));
    }
    
    @Override
    public void doRender(SlimeEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        RenderUtil.setColorRGBA(this.color);
        this.field_4673 = 0.25F * (float) entity.getSlimeSize();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected void preRenderCallback(SlimeEntity entitylivingbaseIn, float partialTickTime) {
        float f = 0.999F;
        GlStateManager.scalef(0.999F, 0.999F, 0.999F);
        float f1 = (float) entitylivingbaseIn.getSlimeSize();
        float f2 = MathHelper.lerp(partialTickTime, entitylivingbaseIn.lastStretch, entitylivingbaseIn.stretch) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        GlStateManager.scalef(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }
    
    @Override
    protected int getColorMultiplier(SlimeEntity entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        return this.color;
    }
    
    @Override
    protected Identifier getEntityTexture(SlimeEntity entity) {
        return SLIME_TEXTURE;
    }
    
    private static class Factory implements IRenderFactory<SlimeEntity> {
        
        private final int color;
        
        public Factory(int color) {
            this.color = color;
        }
        
        @Override
        public EntityRenderer<? super SlimeEntity> createRenderFor(EntityRenderDispatcher manager) {
            return new BlueSlimeRenderer(manager, this.color);
        }
    }
}
