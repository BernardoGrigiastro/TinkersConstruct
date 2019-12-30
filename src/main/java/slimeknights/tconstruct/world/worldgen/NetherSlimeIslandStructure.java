package slimeknights.tconstruct.world.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;
import java.util.function.Function;

public class NetherSlimeIslandStructure extends AbstractTempleFeature<DefaultFeatureConfig> {
    
    public NetherSlimeIslandStructure(Function<Dynamic<?>, ? extends DefaultFeatureConfig> p_i51476_1_) {
        super(p_i51476_1_);
    }
    
    @Override
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
        random.setSeed(this.getSeedModifier());
        int distance = this.getDistance();
        int separation = this.getSeparation();
        int x1 = x + distance * spacingOffsetsX;
        int z1 = z + distance * spacingOffsetsZ;
        int x2 = x1 < 0 ? x1 - distance + 1 : x1;
        int z2 = z1 < 0 ? z1 - distance + 1 : z1;
        int x3 = x2 / distance;
        int z3 = z2 / distance;
        ((ChunkRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), x3, z3, this.getSeedModifier());
        x3 = x3 * distance;
        z3 = z3 * distance;
        x3 = x3 + random.nextInt(distance - separation);
        z3 = z3 + random.nextInt(distance - separation);
        
        return new ChunkPos(x3, z3);
    }
    
    protected int getDistance() {
        return 20;
    }
    
    protected int getSeparation() {
        return 11;
    }
    
    @Override
    protected int getSeedModifier() {
        return 14357800;
    }
    
    @Override
    public StructureStartFactory getStartFactory() {
        return NetherSlimeIslandStructure.Start::new;
    }
    
    @Override
    public String getStructureName() {
        return "Magma_Slime_Island";
    }
    
    @Override
    public int getSize() {
        return 8;
    }
    
    public static class Start extends StructureStart {
        
        public Start(StructureFeature<?> structureIn, int p_i50515_2_, int p_i50515_3_, Biome p_i50515_4_, MutableIntBoundingBox p_i50515_5_, int p_i50515_6_, long p_i50515_7_) {
            super(structureIn, p_i50515_2_, p_i50515_3_, p_i50515_4_, p_i50515_5_, p_i50515_6_, p_i50515_7_);
        }
        
        @Override
        public void init(ChunkGenerator<?> generator, StructureManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            int x = chunkX * 16 + 4 + this.random.nextInt(8);
            int y = 25;
            int z = chunkZ * 16 + 4 + this.random.nextInt(8);
            
            BlockRotation rotation = BlockRotation.values()[this.random.nextInt(BlockRotation.values().length)];
            
            SlimeIslandVariant variant = SlimeIslandVariant.MAGMA;
            String[] sizes = new String[]{"0x1x0", "2x2x4", "4x1x6", "8x1x11", "11x1x11"};
            
            BlockPos pos = new BlockPos(x, y, z);
            
            NetherSlimeIslandPiece slimeIslandPiece = new NetherSlimeIslandPiece(templateManagerIn, variant, sizes[this.random.nextInt(sizes.length)], pos, rotation);
            this.children.add(slimeIslandPiece);
            this.recalculateStructureSize();
        }
    }
}