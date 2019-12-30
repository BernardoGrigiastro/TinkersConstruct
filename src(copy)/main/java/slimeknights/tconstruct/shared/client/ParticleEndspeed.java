package slimeknights.tconstruct.shared.client;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class ParticleEndspeed extends Particle {
    
    public ParticleEndspeed(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        
        //this.setAlphaF(0.9f);
        this.setParticleTextureIndex(176);
        this.particleScale = 1f;
        this.maxAge = 20;
        
        this.velocityX = xSpeedIn;
        this.velocityY = ySpeedIn;
        this.velocityZ = zSpeedIn;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        //this.particleScale -= 0.0001f;
        this.colorAlpha -= 0.05f;
    }
}
