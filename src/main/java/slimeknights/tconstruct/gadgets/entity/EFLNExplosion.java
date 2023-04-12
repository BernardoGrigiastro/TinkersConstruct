package slimeknights.tconstruct.gadgets.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class EFLNExplosion extends Explosion {
    
    protected ImmutableSet<BlockPos> affectedBlockPositionsInternal;
    
    @OnlyIn(Dist.CLIENT)
    public EFLNExplosion(World worldIn,
            @Nullable Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        super(worldIn, entityIn, x, y, z, size, false, Explosion.DestructionType.field_18687, affectedPositions);
    }
    
    @OnlyIn(Dist.CLIENT)
    public EFLNExplosion(World worldIn, @Nullable
            Entity exploderIn, double xIn, double yIn, double zIn, float sizeIn, boolean causesFireIn, Explosion.DestructionType modeIn, List<BlockPos> affectedBlockPositionsIn) {
        super(worldIn, exploderIn, xIn, yIn, zIn, sizeIn, causesFireIn, modeIn);
    }
    
    public EFLNExplosion(World worldIn, @Nullable
            Entity exploderIn, double xIn, double yIn, double zIn, float sizeIn, boolean causesFireIn, Explosion.DestructionType modeIn) {
        super(worldIn, exploderIn, xIn, yIn, zIn, sizeIn, causesFireIn, modeIn);
    }
    
    /**
     * Does the first part of the explosion (destroy blocks)
     */
    @Override
    public void doExplosionA() {
        ImmutableSet.Builder<BlockPos> builder = ImmutableSet.builder();
        
        // we do a sphere of a certain radius, and check if the blockpos is inside the radius
        float r = this.power * this.power;
        int i = (int) r + 1;
        
        for (int j = -i; j < i; ++j) {
            for (int k = -i; k < i; ++k) {
                for (int l = -i; l < i; ++l) {
                    int d = j * j + k * k + l * l;
                    // inside the sphere?
                    if (d <= r) {
                        BlockPos blockpos = new BlockPos(j, k, l).add(this.x, this.y, this.z);
                        // no air blocks
                        if (this.world.isAirBlock(blockpos)) {
                            continue;
                        }
                        
                        // explosion "strength" at the current position
                        float f = this.power * (1f - d / (r));
                        BlockState blockstate = this.world.getBlockState(blockpos);
                        
                        FluidState ifluidstate = this.world.getFluidState(blockpos);
                        
                        float f2 = Math.max(blockstate.getExplosionResistance(this.world, blockpos, this.entity, this), ifluidstate.getExplosionResistance(this.world, blockpos, this.entity, this));
                        if (this.entity != null) {
                            f2 = this.entity.getExplosionResistance(this, this.world, blockpos, blockstate, ifluidstate, f2);
                        }
                        
                        f -= (f2 + 0.3F) * 0.3F;
                        
                        if (f > 0.0F && (this.entity == null || this.entity.canExplosionDestroyBlock(this, this.world, blockpos, blockstate, f))) {
                            builder.add(blockpos);
                        }
                    }
                }
            }
        }
        
        this.affectedBlockPositionsInternal = builder.build();
    }
    
    @Override
    public void doExplosionB(boolean spawnParticles) {
        this.world.playSound(null, this.x, this.y, this.z, SoundEvents.field_15152, SoundCategory.field_15245, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
        
        this.world.addParticle(ParticleTypes.field_11236, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        
        for (BlockPos blockpos : this.affectedBlockPositionsInternal) {
            BlockState blockstate = this.world.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            
            if (!blockstate.isAir(this.world, blockpos)) {
                if (this.world instanceof ServerWorld && blockstate.canDropFromExplosion(this.world, blockpos, this)) {
                    BlockEntity tileentity = blockstate.hasTileEntity() ? this.world.getTileEntity(blockpos) : null;
                    LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.world)).withRandom(this.world.random).withParameter(LootContextParameters.field_1232, blockpos).withParameter(LootContextParameters.field_1229, ItemStack.EMPTY).withNullableParameter(LootContextParameters.field_1228, tileentity);
                    if (this.blockDestructionType == Explosion.DestructionType.field_18687) {
                        lootcontext$builder.withParameter(LootContextParameters.field_1225, this.power);
                    }
                    
                    Block.spawnDrops(blockstate, lootcontext$builder);
                }
                
                blockstate.onBlockExploded(this.world, blockpos, this);
            }
        }
    }
    
    public void addAffectedBlock(BlockPos blockPos) {
        this.affectedBlocks.add(blockPos);
    }
    
}
