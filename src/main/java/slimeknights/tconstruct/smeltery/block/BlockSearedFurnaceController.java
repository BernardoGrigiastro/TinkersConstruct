package slimeknights.tconstruct.smeltery.block;

import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

import javax.annotation.Nonnull;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.smeltery.tileentity.TileSearedFurnace;

public class BlockSearedFurnaceController extends BlockMultiblockController {

  public BlockSearedFurnaceController() {
    super(Material.STONE);
    this.setCreativeTab(TinkerRegistry.tabSmeltery);
    this.setHardness(3F);
    this.setResistance(20F);
    this.setSoundType(BlockSoundGroup.METAL);

    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
  }

  @Nonnull
  @Override
  public BlockEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    return new TileSearedFurnace();
  }

  // lit furnaces produce light
  @Override
  public int getLightValue(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos) {
    if(state.getBlock() == this && state.getActualState(world, pos).getValue(ACTIVE) == Boolean.TRUE) {
      return 15;
    }
    return super.getLightValue(state, world, pos);
  }

  /**
   * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
   * blockstate.
   */
  @Nonnull
  @Override
  public IBlockState withRotation(@Nonnull IBlockState state, BlockRotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  /**
   * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
   * blockstate.
   */
  @Nonnull
  @Override
  public IBlockState withMirror(@Nonnull IBlockState state, BlockMirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
  }

  /* Metadata */
  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing enumfacing = EnumFacing.getHorizontal(meta);

    return this.getDefaultState().withProperty(FACING, enumfacing);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex();
  }

  /* Rendering */

  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
    if(isActive(world, pos)) {
      EnumFacing enumfacing = state.getValue(FACING);
      double x = pos.getX() + 0.5D;
      double y = pos.getY() + 0.375D + (rand.nextFloat() * 8F) / 16F;
      double z = pos.getZ() + 0.5D;
      double frontOffset = 0.52D;
      double sideOffset = rand.nextDouble() * 0.4D - 0.2D;

      spawnFireParticles(world, enumfacing, x, y, z, frontOffset, sideOffset);
    }
  }
}
