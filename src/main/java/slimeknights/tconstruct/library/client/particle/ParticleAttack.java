package slimeknights.tconstruct.library.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ParticleAttack extends Particle {

  public static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(VertexFormats.POSITION_ELEMENT).addElement(VertexFormats.UV_ELEMENT).addElement(VertexFormats.COLOR_ELEMENT).addElement(VertexFormats.LMAP_ELEMENT).addElement(VertexFormats.NORMAL_ELEMENT).addElement(VertexFormats.PADDING_ELEMENT);

  protected TextureManager textureManager;
  protected int life;

  protected int lifeTime;
  protected float size;
  protected double spacingY;

  protected int animPhases;
  protected int animPerRow;

  public ParticleAttack(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, TextureManager textureManager) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    this.textureManager = textureManager;

    this.life = 0;
    init();
  }

  protected void init() {
    this.lifeTime = 4;
    this.size = 1f;
    this.spacingY = 1f;

    this.animPerRow = 4;
    this.animPhases = 8;
  }

  protected abstract Identifier getTexture();

  protected VertexFormat getVertexFormat() {
    return VERTEX_FORMAT;
  }

  @Override
  public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
    float progress = (life + partialTicks) / lifeTime;
    int i = (int) (progress * animPhases);
    int rows = MathHelper.ceil((float) animPhases / (float) animPerRow);

    if(i < animPhases) {
      this.textureManager.bindTexture(getTexture());
      float f = (float) (i % animPerRow) / (float) animPerRow;
      float f1 = f + 1f / animPerRow - 0.005f;
      float f2 = (float) (i / animPerRow) / (float) rows;
      float f3 = f2 + 1f / rows - 0.005f;
      float f4 = 0.5F * this.size;
      float f5 = (float) (this.prevPosX + (this.x - this.prevPosX) * partialTicks - cameraX);
      float f6 = (float) (this.prevPosY + (this.y - this.prevPosY) * partialTicks - cameraY);
      float f7 = (float) (this.prevPosZ + (this.z - this.prevPosZ) * partialTicks - cameraZ);

      // mirror the attack for left handed
      if(MinecraftClient.getMinecraft().options.mainHand == EnumHandSide.LEFT) {
        // we just swap the x UVs to mirror it
        float t = f;
        f = f1;
        f1 = t;
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableLighting();
      GuiLighting.disableStandardItemLighting();
      worldRendererIn.begin(7, getVertexFormat());
      worldRendererIn.pos(f5 - rotationX * f4 - rotationXY * f4, (f6 - rotationZ * f4 * spacingY), f7 - rotationYZ * f4 - rotationXZ * f4).tex(f1, f3).color(this.colorRed, this.colorGreen, this.colorBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
      worldRendererIn.pos(f5 - rotationX * f4 + rotationXY * f4, (f6 + rotationZ * f4 * spacingY), f7 - rotationYZ * f4 + rotationXZ * f4).tex(f1, f2).color(this.colorRed, this.colorGreen, this.colorBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
      worldRendererIn.pos(f5 + rotationX * f4 + rotationXY * f4, (f6 + rotationZ * f4 * spacingY), f7 + rotationYZ * f4 + rotationXZ * f4).tex(f, f2).color(this.colorRed, this.colorGreen, this.colorBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
      worldRendererIn.pos(f5 + rotationX * f4 - rotationXY * f4, (f6 - rotationZ * f4 * spacingY), f7 + rotationYZ * f4 - rotationXZ * f4).tex(f, f3).color(this.colorRed, this.colorGreen, this.colorBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
      Tessellator.getInstance().draw();
      GlStateManager.enableLighting();
    }
  }

  @Override
  public int getBrightnessForRender(float p_189214_1_) {
    return 61680;
  }

  @Override
  public void onUpdate() {
    this.prevPosX = this.x;
    this.prevPosY = this.y;
    this.prevPosZ = this.z;
    ++this.life;

    if(this.life == this.lifeTime) {
      this.setExpired();
    }
  }

  @Override
  public int getFXLayer() {
    return 3;
  }
}
