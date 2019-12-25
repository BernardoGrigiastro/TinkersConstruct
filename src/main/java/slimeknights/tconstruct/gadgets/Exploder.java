package slimeknights.tconstruct.gadgets;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.gadgets.entity.ExplosionEFLN;
import slimeknights.tconstruct.tools.common.network.EntityMovementChangePacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Exploder {
    
    private static List<Exploder> toTick = new ArrayList<>();
    
    static {
        WorldTickCallback.EVENT.register(world -> {
            Iterator<Exploder> iterator = toTick.iterator();
            while (iterator.hasNext()) {
                Exploder next = iterator.next();
                if (!next.onTick(world)) {
                    iterator.remove();
                }
            }
        });
    }
    
    public final double r;
    public final double rr;
    public final int dist;
    public final double explosionStrength;
    public final int blocksPerIteration;
    public final int x, y, z;
    public final World world;
    public final Entity exploder;
    public final ExplosionEFLN explosion;
    protected int currentRadius;
    protected List<ItemStack> droppedItems; // map containing all items dropped by the explosion and their amounts
    private int curX, curY, curZ;
    
    public Exploder(World world, ExplosionEFLN explosion, Entity exploder, BlockPos location, double r, double explosionStrength, int blocksPerIteration) {
        this.r = r;
        this.world = world;
        this.explosion = explosion;
        this.exploder = exploder;
        this.rr = r * r;
        this.dist = (int) r + 1;
        this.explosionStrength = explosionStrength;
        this.blocksPerIteration = blocksPerIteration;
        this.currentRadius = 0;
        
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        
        this.curX = 0;
        this.curY = 0;
        this.curZ = 0;
        
        this.droppedItems = Lists.newArrayList();
    }
    
    public static void startExplosion(World world, ExplosionEFLN explosion, Entity entity, BlockPos location, double r, double explosionStrength) {
        Exploder exploder = new Exploder(world, explosion, entity, location, r, explosionStrength, Math.max(50, (int) (r * r * r / 10d)));
        exploder.handleEntities();
        world.playSound(null, location, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
        toTick.add(exploder);
    }
    
    public boolean onTick(World world) {
        if (world == this.world) {
            if (!iteration()) {
                // goodbye world, we're done exploding
                return finish();
            }
        }
        return true;
    }
    
    void handleEntities() {
        final Predicate<Entity> predicate = entity -> entity != null && !entity.isImmuneToExplosion() && entity.isAlive() && (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isSpectator()) && entity.getPosVector().squaredDistanceTo(x, y, z) <= r * r;
        
        // damage and blast back entities
        List<Entity> list = world.getEntities(this.exploder, new BoundingBox(x - r - 1, y - r - 1, z - r - 1, x + r + 1, y + r + 1, z + r + 1), predicate);
        
        for (Entity entity : list) {
            //double str = 1f - (double)currentRadius/r;
            //str *= str;
            // move it away from the center depending on distance and explosion strength
            Vec3d dir = entity.getPosVector().subtract(exploder.getPosVector().add(0, -r / 2, 0));
            double str = (r - dir.length()) / r;
            str = Math.max(0.3, str);
            dir = dir.normalize();
            dir = dir.multiply(explosionStrength * str * 0.3);
            entity.addVelocity(dir.x, dir.y + 0.5, dir.z);
            entity.damage(DamageSource.explosion(explosion), (float) (str * explosionStrength));
            
            if (entity instanceof ServerPlayerEntity) {
                TinkerNetwork.sendTo(new EntityMovementChangePacket(entity), (ServerPlayerEntity) entity);
            }
        }
    }
    
    private boolean finish() {
        final int d = (int) r / 2;
        final BlockPos pos = new BlockPos(x - d, y - d, z - d);
        final Random random = new Random();
        
        List<ItemStack> aggregatedDrops = Lists.newArrayList();
        
        for (ItemStack drop : droppedItems) {
            boolean notInList = true;
            
            // check if it's already in our list
            for (ItemStack stack : aggregatedDrops) {
                if (ItemStack.areEqual(drop, stack)) {
                    stack.addAmount(drop.getAmount());
                    notInList = false;
                    break;
                }
            }
            
            if (notInList) {
                aggregatedDrops.add(drop);
            }
        }
        
        // actually drop the aggregated items
        for (ItemStack drop : aggregatedDrops) {
            int stacksize = drop.getAmount();
            do {
                BlockPos spawnPos = pos.add(random.nextInt((int) r), random.nextInt((int) r), random.nextInt((int) r));
                ItemStack dropItemstack = drop.copy();
                dropItemstack.setAmount(Math.min(stacksize, 64));
                Block.dropStack(world, spawnPos, dropItemstack);
                stacksize -= dropItemstack.getAmount();
            } while (stacksize > 0);
        }
        
        return false;
    }
    
    /**
     * Explodes away all blocks for the current iteration
     */
    private boolean iteration() {
        int count = 0;
        
        explosion.clearAffectedBlocks();
        
        while (count < blocksPerIteration && currentRadius < (int) r + 1) {
            double d = curX * curX + curY * curY + curZ * curZ;
            // inside the explosion?
            if (d <= rr) {
                BlockPos pos = new BlockPos(x + curX, y + curY, z + curZ);
                BlockState state = world.getBlockState(pos);
                FluidState fluidState = world.getFluidState(pos);
                
                // no air blocks
                if (!state.isAir() || !fluidState.isEmpty()) {
                    // explosion "strength" at the current position
                    double f = explosionStrength * (1f - d / rr);
                    
                    float f2 = Math.max(state.getBlock().getBlastResistance(), fluidState.getBlastResistance());
                    if (exploder != null) {
                        f2 = exploder.getEffectiveExplosionResistance(explosion, world, pos, state, fluidState, f2);
                    }
                    f -= (f2 + 0.3F) * 0.3F;
                    
                    if (f > 0.0F && (exploder == null || exploder.canExplosionDestroyBlock(explosion, world, pos, state, (float) f))) {
                        // block should be exploded
                        count++;
                        explosion.addAffectedBlock(pos);
                    }
                }
            }
            // get next coordinate;
            step();
        }
        
        explosion.getAffectedBlocks().forEach(this::explodeBlock);
        
        return count == blocksPerIteration; // can lead to 1 more call where nothing is done, but that's ok
    }
    
    // get the next coordinate
    private void step() {
        // we go X/Z plane wise from top to bottom
        if (++curX > currentRadius) {
            curX = -currentRadius;
            if (++curZ > currentRadius) {
                curZ = -currentRadius;
                if (--curY < -currentRadius) {
                    currentRadius++;
                    curX = curZ = -currentRadius;
                    curY = currentRadius;
                }
            }
        }
        // we skip the internals
        if (curY != -currentRadius && curY != currentRadius) {
            // we're not in the top or bottom plane
            if (curZ != -currentRadius && curZ != currentRadius) {
                // we're not in the X/Y planes of the cube, we can therefore skip the x to the end if we're inside
                if (curX > -currentRadius) {
                    curX = currentRadius;
                }
            }
        }
    }
    
    private void explodeBlock(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!world.isClient && block.shouldDropItemsOnExplosion(explosion)) {
            BlockEntity blockEntity = block.hasBlockEntity() ? this.world.getBlockEntity(pos) : null;
            LootContext.Builder builder = (new LootContext.Builder((ServerWorld) this.world)).setRandom(this.world.random).put(LootContextParameters.POSITION, pos).put(LootContextParameters.TOOL, ItemStack.EMPTY).putNullable(LootContextParameters.BLOCK_ENTITY, blockEntity);
            droppedItems.addAll(state.getDroppedStacks(builder));
        }
        
        if (world instanceof ServerWorld) {
            ((ServerWorld) world).addImportantParticle(ParticleTypes.POOF, true, pos.getX(), pos.getY(), pos.getZ(), 2, 0, 0);
            ((ServerWorld) world).addImportantParticle(ParticleTypes.SMOKE, true, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0);
        }
        
        block.onDestroyedByExplosion(world, pos, explosion);
    }
}
