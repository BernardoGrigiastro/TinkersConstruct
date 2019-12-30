package slimeknights.tconstruct.gadgets.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.gadgets.entity.FancyItemFrameEntity;
import slimeknights.tconstruct.gadgets.entity.FrameType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class FancyItemFrameRenderer extends EntityRenderer<FancyItemFrameEntity> {
    
    private static final Identifier MAP_BACKGROUND_TEXTURES = new Identifier("textures/map/map_background.png");
    
    private static final Map<FrameType, ModelIdentifier> LOCATIONS_MODEL = new HashMap<>();
    private static final Map<FrameType, ModelIdentifier> LOCATIONS_MODEL_MAP = new HashMap<>();
    
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final ItemRenderer itemRenderer;
    private final ItemFrameEntityRenderer defaultRenderer;
    
    public FancyItemFrameRenderer(EntityRenderDispatcher renderManagerIn, ItemRenderer itemRendererIn) {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
        this.defaultRenderer = renderManagerIn.getRenderer(ItemFrameEntity.class);
        
        for (FrameType frameType : FrameType.values()) {
            // TODO: reinstate when Forge fixes itself
            // LOCATIONS_MODEL.put(color, new ModelResourceLocation(new ResourceLocation(TConstruct.modID, frameType.getName() + "_frame"), "map=false"));
            // LOCATIONS_MODEL_MAP.put(color, new ModelResourceLocation(new ResourceLocation(TConstruct.modID, frameType.getName() + "_frame"), "map=true"));
            
            LOCATIONS_MODEL.put(frameType, new ModelIdentifier(new Identifier(TConstruct.modID, frameType.getName() + "_frame_empty"), "inventory"));
            LOCATIONS_MODEL_MAP.put(frameType, new ModelIdentifier(new Identifier(TConstruct.modID, frameType.getName() + "_frame_map"), "inventory"));
        }
    }
    
    @Override
    public void doRender(FancyItemFrameEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        BlockPos blockpos = entity.getHangingPosition();
        double d0 = (double) blockpos.getX() - entity.x + x;
        double d1 = (double) blockpos.getY() - entity.y + y;
        double d2 = (double) blockpos.getZ() - entity.z + z;
        GlStateManager.translated(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.rotatef(entity.pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(180.0F - entity.yaw, 0.0F, 1.0F, 0.0F);
        this.renderManager.textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        BlockRenderManager blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        BakedModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        
        FrameType frameType = entity.getFrameType();
        ModelIdentifier modelresourcelocation = entity.getDisplayedItem().getItem() instanceof FilledMapItem ? LOCATIONS_MODEL_MAP.get(frameType) : LOCATIONS_MODEL.get(frameType);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }
        
        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(modelmanager.getModel(modelresourcelocation), 1.0F, 1.0F, 1.0F, 1.0F);
        if (this.renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }
        
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        if (entity.getDisplayedItem().getItem() == Items.field_8204) {
            GlStateManager.pushLightingAttributes();
            GuiLighting.enableStandardItemLighting();
        }
        
        GlStateManager.translatef(0.0F, 0.0F, 0.4375F);
        this.renderItem(entity);
        if (entity.getDisplayedItem().getItem() == Items.field_8204) {
            GuiLighting.disableStandardItemLighting();
            GlStateManager.popAttributes();
        }
        
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        this.renderName(entity, x + (double) ((float) entity.getHorizontalFacing().getXOffset() * 0.3F), y - 0.25D, z + (double) ((float) entity.getHorizontalFacing().getZOffset() * 0.3F));
    }
    
    @Nullable
    @Override
    protected Identifier getEntityTexture(@Nonnull FancyItemFrameEntity entity) {
        return null;
    }
    
    private void renderItem(FancyItemFrameEntity itemFrame) {
        ItemStack stack = itemFrame.getDisplayedItem();
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            MapState mapdata = FilledMapItem.getMapData(stack, itemFrame.world);
            int rotation = (mapdata != null) ? ((itemFrame.getRotation() % 4) * 2) : itemFrame.getRotation();
            GlStateManager.rotatef((float) rotation * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
            if (!MinecraftForge.EVENT_BUS.post(new RenderItemInFrameEvent(itemFrame, this.defaultRenderer))) {
                if (mapdata != null) {
                    GlStateManager.disableLighting();
                    this.renderManager.textureManager.bindTexture(MAP_BACKGROUND_TEXTURES);
                    GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.scalef(0.0078125F, 0.0078125F, 0.0078125F);
                    GlStateManager.translatef(-64.0F, -64.0F, 0.0F);
                    GlStateManager.translatef(0.0F, 0.0F, -1.0F);
                    this.mc.gameRenderer.getMapItemRenderer().renderMap(mapdata, true);
                } else {
                    GlStateManager.scalef(0.5F, 0.5F, 0.5F);
                    this.itemRenderer.renderItem(stack, ModelTransformation.Type.field_4319);
                }
            }
            
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    protected void renderName(@Nonnull FancyItemFrameEntity entity, double x, double y, double z) {
        if (MinecraftClient.isGuiEnabled() && !entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().hasDisplayName() && this.renderManager.targetedEntity == entity) {
            double d0 = entity.getDistanceSq(this.renderManager.camera.getProjectedView());
            float f = entity.shouldRenderSneaking() ? 32.0F : 64.0F;
            if (!(d0 >= (double) (f * f))) {
                String s = entity.getDisplayedItem().getDisplayName().getFormattedText();
                this.renderLivingLabel(entity, s, x, y, z, 64);
            }
        }
    }
}
