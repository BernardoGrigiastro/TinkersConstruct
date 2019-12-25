package slimeknights.tconstruct.shared.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleCrit;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.Util;

@SideOnly(Side.CLIENT)
public class ParticleEffect extends ParticleCrit {
    
    public static final Identifier TEXTURE = Util.getResource("textures/particle/particles.png");
    public static final Identifier VANILLA_PARTICLE_TEXTURES = new Identifier("textures/particle/particles.png");
    protected final Type type;
    protected TextureManager textureManager;
    private int layer = 0;
    
    public ParticleEffect(int typeId, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, 1f);
        
        if (typeId < 0 || typeId > Type.values().length) {
            typeId = 0;
        }
        
        this.type = Type.values()[typeId];
        
        this.maxAge = 20;
        this.particleTextureIndexX = type.x / 8;
        this.particleTextureIndexY = type.y / 8;
        
        this.velocityY += 0.1f;
        this.velocityX += -0.25f + random.nextFloat() * 0.5f;
        this.velocityZ += -0.25f + random.nextFloat() * 0.5f;
        
        colorRed = colorBlue = colorGreen = 1f;
        
        this.textureManager = MinecraftClient.getMinecraft().getTextureManager();
        
        // has to be set after constructor because parent class accesses layer-0-only functions
        this.layer = 3;
    }
    
    protected Identifier getTexture() {
        return TEXTURE;
    }
    
    @Override
    public void onUpdate() {
        float r = this.colorRed;
        float g = this.colorGreen;
        float b = this.colorBlue;
        super.onUpdate();
        
        this.colorRed = r * 0.975f;
        this.colorGreen = g * 0.975f;
        this.colorBlue = b * 0.975f;
    }
    
    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        buffer.begin(7, VertexFormats.POSITION_UV_COLOR_LMAP);
        textureManager.bindTexture(getTexture());
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        Tessellator.getInstance().draw();
    }
    
    @Override
    public int getFXLayer() {
        // layer 3 seems to be a "binds its own texture" layer
        return layer;
    }
    
    public enum Type {
        HEART_FIRE(0, 0),
        HEART_CACTUS(8, 0),
        HEART_ELECTRO(16, 0),
        HEART_BLOOD(24, 0),
        HEART_ARMOR(32, 0);
        
        int x, y;
        
        Type(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
