package slimeknights.tconstruct.smeltery.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.smeltery.tileentity.TileFaucet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockFaucet extends BlockContainer {

  // Facing == input, can be any side except bottom, because down always is output direction
  public static final PropertyDirection FACING = PropertyDirection.create("facing", (@Nullable EnumFacing input) -> input != EnumFacing.DOWN);

  public BlockFaucet() {
    super(Material.STONE);

    this.setCreativeTab(TinkerRegistry.tabSmeltery);
    this.setHardness(3F);
    this.setResistance(20F);
    this.setSoundType(BlockSoundGroup.METAL);
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  /**
   * Convert the given metadata into a BlockState for this Block
   */
  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {
    if(meta >= EnumFacing.values().length) {
      meta = 1;
    }
    EnumFacing face = EnumFacing.values()[meta];
    if(face == EnumFacing.DOWN) {
      face = EnumFacing.UP;
    }

    return this.getDefaultState().withProperty(FACING, face);
  }

  /**
   * Convert the BlockState into the correct metadata value
   */
  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).ordinal();
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if(playerIn.isSneaking()) {
      return false;
    }
    BlockEntity te = worldIn.getTileEntity(pos);
    if(te instanceof TileFaucet) {
      ((TileFaucet) te).activate();
      return true;
    }
    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }

  /* Redstone */

  @Override
  public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return true;
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
    if (world.isClient) {
      return;
    }
    BlockEntity te = world.getTileEntity(pos);
    if (te instanceof TileFaucet) {
      ((TileFaucet) te).handleRedstone(world.isBlockPowered(pos));
    }
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    BlockEntity te = world.getTileEntity(pos);
    if(te instanceof TileFaucet) {
      ((TileFaucet) te).activate();
    }
  }

  /* Bounds */

  private static final ImmutableMap<EnumFacing, BoundingBox> BOUNDS;

  static {
    ImmutableMap.Builder<EnumFacing, BoundingBox> builder = ImmutableMap.builder();
    builder.put(EnumFacing.UP, new BoundingBox(0.25, 0.625, 0.25, 0.75, 1, 0.75));
    builder.put(EnumFacing.NORTH, new BoundingBox(0.25, 0.25, 0, 0.75, 0.625, 0.375));
    builder.put(EnumFacing.SOUTH, new BoundingBox(0.25, 0.25, 0.625, 0.75, 0.625, 1.0));
    builder.put(EnumFacing.EAST, new BoundingBox(0.625, 0.25, 0.25, 1, 0.625, 0.75));
    builder.put(EnumFacing.WEST, new BoundingBox(0, 0.25, 0.25, 0.375, 0.625, 0.75));
    builder.put(EnumFacing.DOWN, FULL_BLOCK_AABB);

    BOUNDS = builder.build();
  }

  @Nonnull
  @Override
  public BoundingBox getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return BOUNDS.get(state.getValue(FACING));
  }

  @Nonnull
  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {
    return true;
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  @Deprecated
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
    return BlockFaceShape.UNDEFINED;
  }

  @Nonnull
  @Override
  public BlockEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    return new TileFaucet();
  }

  /**
   * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
   * IBlockstate
   */
  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    EnumFacing enumfacing = facing.getOpposite();

    if(enumfacing == EnumFacing.DOWN) {
      enumfacing = placer.getHorizontalFacing().getOpposite();
    }

    return this.getDefaultState().withProperty(FACING, enumfacing);
  }
}
