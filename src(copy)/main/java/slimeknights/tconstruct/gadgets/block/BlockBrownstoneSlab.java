package slimeknights.tconstruct.gadgets.block;

import net.minecraft.block.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.mantle.block.EnumBlock;
import slimeknights.mantle.block.EnumBlockSlab;
import slimeknights.tconstruct.gadgets.TinkerGadgets;
import slimeknights.tconstruct.library.TinkerRegistry;

import java.util.Locale;

public class BlockBrownstoneSlab extends EnumBlockSlab<BlockBrownstoneSlab.BrownstoneType> {

    public final static PropertyEnum<BrownstoneType> TYPE = PropertyEnum.create("type", BrownstoneType.class);

    public BlockBrownstoneSlab() {
        super(Material.STONE, TYPE, BrownstoneType.class);
        this.setCreativeTab(TinkerRegistry.tabGadgets);
        this.setHardness(3F);
        this.setResistance(20F);
        this.setSoundType(BlockSoundGroup.STONE);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entity) {
        if (entity.isInWater()) {
            entity.motionX *= 1.20;
            entity.motionZ *= 1.20;
        } else {
            entity.motionX *= 1.25;
            entity.motionZ *= 1.25;
        }
    }

    @Override
    public IBlockState getFullBlock(IBlockState state) {
        if (TinkerGadgets.brownstone == null) {
            return null;
        }
        return TinkerGadgets.brownstone.getDefaultState().withProperty(BlockBrownstone.TYPE, state.getValue(TYPE).asBrownstone());
    }

    // using a separate Enum since there are more variants than the 8 types slabs support
    public enum BrownstoneType implements StringRepresentable, EnumBlock.IEnumMeta {
        SMOOTH,
        ROUGH,
        PAVER,
        BRICK,
        BRICK_CRACKED,
        BRICK_FANCY,
        BRICK_SQUARE,
        ROAD;

        public final int meta;

        BrownstoneType() {
            meta = ordinal();
        }

        @Override
        public String getName() {
            return this.toString().toLowerCase(Locale.US);
        }

        public BlockBrownstone.BrownstoneType asBrownstone() {
            switch (this) {
                case SMOOTH:
                    return BlockBrownstone.BrownstoneType.SMOOTH;
                case ROUGH:
                    return BlockBrownstone.BrownstoneType.ROUGH;
                case PAVER:
                    return BlockBrownstone.BrownstoneType.PAVER;
                case BRICK:
                    return BlockBrownstone.BrownstoneType.BRICK;
                case BRICK_CRACKED:
                    return BlockBrownstone.BrownstoneType.BRICK_CRACKED;
                case BRICK_FANCY:
                    return BlockBrownstone.BrownstoneType.BRICK_FANCY;
                case BRICK_SQUARE:
                    return BlockBrownstone.BrownstoneType.BRICK_SQUARE;
                case ROAD:
                    return BlockBrownstone.BrownstoneType.ROAD;
                default:
                    throw new IllegalArgumentException("Unknown enum value? Impossibru!");
            }
        }

        @Override
        public int getMeta() {
            return meta;
        }
    }
}
