package slimeknights.tconstruct.gadgets.block;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.container.NameableContainerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.TinkerRegistry;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockPunji extends Block {

    public static final DirectionProperty FACING = DirectionProperty.create("facing");
    //public static final PropertyEnum<ConnectionHorizontal> CON_HOR = PropertyEnum.create("connection_horizontal", ConnectionHorizontal.class);
    //public static final PropertyEnum<ConnectionDiagonal> CON_DIA = PropertyEnum.create("connection_diagonal", ConnectionDiagonal.class);
    //public static final PropertyEnum<ConnectionVertical> CON_VER = PropertyEnum.create("connection_vertical", ConnectionVertical.class);
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty NORTHEAST = BooleanProperty.create("northeast");
    public static final BooleanProperty NORTHWEST = BooleanProperty.create("northwest");
    /* Bounds */
    private static final ImmutableMap<Direction, BoundingBox> BOUNDS;

    static {
        ImmutableMap.Builder<Direction, BoundingBox> builder = ImmutableMap.builder();
        builder.put(Direction.DOWN, new BoundingBox(0.1875, 0, 0.1875, 0.8125, 0.375, 0.8125));
        builder.put(Direction.UP, new BoundingBox(0.1875, 0.625, 0.1875, 0.8125, 1, 0.8125));
        builder.put(Direction.NORTH, new BoundingBox(0.1875, 0.1875, 0, 0.8125, 0.8125, 0.375));
        builder.put(Direction.SOUTH, new BoundingBox(0.1875, 0.1875, 0.625, 0.8125, 0.8125, 1));
        builder.put(Direction.EAST, new BoundingBox(0.625, 0.1875, 0.1875, 1, 0.8125, 0.8125));
        builder.put(Direction.WEST, new BoundingBox(0, 0.1875, 0.1875, 0.375, 0.8125, 0.8125));

        BOUNDS = builder.build();
    }

    public BlockPunji() {
        super(FabricBlockSettings.of(Material.PLANT).sounds(BlockSoundGroup.GRASS).hardness(3f).build());
        this.setDefaultState(getDefaultState().with(FACING, Direction.DOWN).with(NORTH, false).with(EAST, false).with(NORTHEAST, false).with(NORTHWEST, false));
    }
    
    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, NORTH, EAST, NORTHEAST, NORTHWEST);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta >= EnumFacing.values().length) {
            meta = EnumFacing.DOWN.ordinal();
        }
        EnumFacing face = EnumFacing.values()[meta];

        return this.getDefaultState().withProperty(FACING, face);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);

        int off = -facing.ordinal() % 2;

        EnumFacing face1 = EnumFacing.values()[(facing.ordinal() + 2) % 6];
        EnumFacing face2 = EnumFacing.values()[(facing.ordinal() + 4 + off) % 6];

        // North/East Connector
        IBlockState north = worldIn.getBlockState(pos.offset(face1));
        IBlockState east = worldIn.getBlockState(pos.offset(face2));
        if (north.getBlock() == this && north.getValue(FACING) == facing) {
            state = state.withProperty(NORTH, true);
        }
        if (east.getBlock() == this && east.getValue(FACING) == facing) {
            state = state.withProperty(EAST, true);
        }

        // Diagonal connections
        IBlockState northeast = worldIn.getBlockState(pos.offset(face1).offset(face2));
        IBlockState northwest = worldIn.getBlockState(pos.offset(face1).offset(face2.getOpposite()));
        if (northeast.getBlock() == this && northeast.getValue(FACING) == facing) {
            state = state.withProperty(NORTHEAST, true);
        }
        if (northwest.getBlock() == this && northwest.getValue(FACING) == facing) {
            state = state.withProperty(NORTHWEST, true);
        }


        return state;
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing enumfacing = facing.getOpposite();

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos pos, EnumFacing side) {
        return worldIn.isSideSolid(pos.offset(side.getOpposite()), side, true);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        EnumFacing facing = state.getValue(FACING);

        if (!worldIn.isSideSolid(pos.offset(facing), facing.getOpposite(), true)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Nonnull
    @Override
    public BoundingBox getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS.get(state.getValue(FACING));
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            float damage = 3f;
            if (entityIn.fallDistance > 0) {
                damage += entityIn.fallDistance * 1.5f + 2f;
            }
            entityIn.attackEntityFrom(DamageSource.CACTUS, damage);
            ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 1));
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
        return BlockFaceShape.UNDEFINED;
    }

    private enum Corner implements StringRepresentable {
        NONE_UP,
        NORTH_DOWN,
        EAST_UP,
        EAST_DOWN,
        SOUTH_UP,
        SOUTH_DOWN,
        WEST_UP,
        WEST_DOWN;

        @Override
        public String getName() {
            return this.toString().toLowerCase(Locale.US);
        }
    }
}
