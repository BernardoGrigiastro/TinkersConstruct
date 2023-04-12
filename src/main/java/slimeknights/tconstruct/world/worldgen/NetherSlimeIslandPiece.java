package slimeknights.tconstruct.world.worldgen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.SlimeTallGrassBlock;

import java.util.Random;

public class NetherSlimeIslandPiece extends SimpleStructurePiece {
    
    private static final MagmaSlimeTree magmaSlimeTree = new MagmaSlimeTree();
    private final String templateName;
    private final SlimeIslandVariant variant;
    private final BlockRotation rotation;
    private final BlockMirror mirror;
    private int numberOfTreesPlaced;
    
    public NetherSlimeIslandPiece(StructureManager templateManager, SlimeIslandVariant variant, String templateName, BlockPos templatePosition, BlockRotation rotation) {
        this(templateManager, variant, templateName, templatePosition, rotation, BlockMirror.field_11302);
    }
    
    public NetherSlimeIslandPiece(StructureManager templateManager, SlimeIslandVariant variant, String templateName, BlockPos templatePosition, BlockRotation rotation, BlockMirror mirror) {
        super(TinkerWorld.NETHER_SLIME_ISLAND_PIECE, 0);
        this.templateName = templateName;
        this.variant = variant;
        this.pos = templatePosition;
        this.rotation = rotation;
        this.mirror = mirror;
        this.numberOfTreesPlaced = 0;
        this.loadTemplate(templateManager);
    }
    
    public NetherSlimeIslandPiece(StructureManager templateManager, CompoundTag nbt) {
        super(TinkerWorld.NETHER_SLIME_ISLAND_PIECE, nbt);
        this.templateName = nbt.getString("Template");
        this.variant = SlimeIslandVariant.getVariantFromIndex(nbt.getInt("Variant"));
        this.rotation = BlockRotation.valueOf(nbt.getString("Rot"));
        this.mirror = BlockMirror.valueOf(nbt.getString("Mi"));
        this.numberOfTreesPlaced = nbt.getInt("NumberOfTreesPlaced");
        this.loadTemplate(templateManager);
    }
    
    private void loadTemplate(StructureManager templateManager) {
        Structure template = templateManager.getTemplateDefaulted(new Identifier("tconstruct:slime_islands/" + this.variant.getName() + "/" + this.templateName));
        StructurePlacementData placementsettings = (new StructurePlacementData()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
        this.setup(template, this.pos, placementsettings);
    }
    
    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    @Override
    protected void readAdditional(CompoundTag tagCompound) {
        super.readAdditional(tagCompound);
        tagCompound.putString("Template", this.templateName);
        tagCompound.putInt("Variant", this.variant.getIndex());
        tagCompound.putString("Rot", this.placementData.getRotation().name());
        tagCompound.putString("Mi", this.placementData.getMirror().name());
        tagCompound.putInt("NumberOfTreesPlaced", this.numberOfTreesPlaced);
    }
    
    @Override
    protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableIntBoundingBox sbb) {
        switch (function) {
            case "tconstruct:lake_bottom":
                worldIn.setBlockState(pos, this.variant.getLakeBottom(), 2);
                break;
            case "tconstruct:slime_fluid":
                worldIn.setBlockState(pos, this.variant.getLakeFluid(), 2);
                break;
            case "tconstruct:congealed_slime":
                int congealed_slime_random = rand.nextInt(this.variant.getCongealedSlime().length);
                worldIn.setBlockState(pos, this.variant.getCongealedSlime()[congealed_slime_random], 2);
                break;
            case "tconstruct:slime_tree":
                worldIn.setBlockState(pos, Blocks.field_10124.getDefaultState(), 2);
                
                if (rand.nextBoolean() && this.numberOfTreesPlaced < 3) {
                    AbstractTreeFeature<DefaultFeatureConfig> treeFeature = magmaSlimeTree.getTreeFeature(rand);
                    if (treeFeature != null) {
                        treeFeature.place(worldIn, worldIn.getChunkProvider().getChunkGenerator(), rand, pos, FeatureConfig.DEFAULT);
                    }
                }
                
                this.numberOfTreesPlaced++;
                break;
            case "tconstruct:slime_vine":
                worldIn.setBlockState(pos, Blocks.field_10124.getDefaultState(), 2);
                break;
            case "tconstruct:slime_tall_grass":
                worldIn.setBlockState(pos, Blocks.field_10124.getDefaultState(), 2);
                
                if (rand.nextBoolean()) {
                    int slime_grass_random = rand.nextInt(this.variant.getTallGrass().length);
                    BlockState state = this.variant.getTallGrass()[slime_grass_random];
                    
                    if (state.getBlock() instanceof SlimeTallGrassBlock) {
                        if (((SlimeTallGrassBlock) state.getBlock()).isValidPosition(state, worldIn, pos)) {
                            worldIn.setBlockState(pos, state, 2);
                        }
                    }
                }
                break;
        }
    }
    
    @Override
    public boolean addComponentParts(IWorld worldIn, Random randomIn, MutableIntBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
        if (this.isLava(worldIn, this.pos.up()) && this.isLava(worldIn, this.pos.up().north()) && this.isLava(worldIn, this.pos.up().east()) && this.isLava(worldIn, this.pos.up().south()) && this.isLava(worldIn, this.pos.up().west())) {
            return super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn, chunkPosIn);
        } else {
            return false;
        }
    }
    
    private boolean isLava(IWorld world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.field_10164;
    }
}