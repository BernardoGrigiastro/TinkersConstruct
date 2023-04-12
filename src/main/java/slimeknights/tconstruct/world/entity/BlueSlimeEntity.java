package slimeknights.tconstruct.world.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.loot.LootTables;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.SlimeGrassBlock;

import java.util.Random;

public class BlueSlimeEntity extends SlimeEntity {
    
    public BlueSlimeEntity(EntityType<? extends SlimeEntity> type, World worldIn) {
        super(type, worldIn);
    }
    
    public static boolean canSpawnHere(EntityType<BlueSlimeEntity> entityType, IWorld world, SpawnType spawnReason, BlockPos pos, Random random) {
        //if (world.getBlockState(pos).getBlock() instanceof LiquidSlimeBlock) {
        //  return true;
        //}
        return world.getBlockState(pos.down()).getBlock() instanceof SlimeGrassBlock;
    }
    
    @Override
    protected Identifier getLootTable() {
        return this.getSlimeSize() == 1 ? this.getType().getLootTable() : LootTables.EMPTY;
    }
    
    @Override
    protected boolean spawnCustomParticles() {
        if (this.getEntityWorld().isClient) {
            int i = this.getSlimeSize();
            for (int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * (float) Math.PI * 2.0F;
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
                double d0 = this.x + (double) f2;
                double d1 = this.z + (double) f3;
                double d2 = this.getBoundingBox().minY;
                TinkerWorld.proxy.spawnSlimeParticle(this.getEntityWorld(), d0, d2, d1);
            }
        }
        return true;
    }
}
