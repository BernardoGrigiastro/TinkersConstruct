package slimeknights.tconstruct.smeltery.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import slimeknights.mantle.block.BlockStairsBase;
import slimeknights.tconstruct.smeltery.tileentity.TileSmelteryComponent;

public class BlockSearedStairs extends BlockStairsBase implements BlockEntityProvider {

  private Block block;

  public BlockSearedStairs(IBlockState modelState) {
    super(modelState);
    this.block = modelState.getBlock();
    this.isBlockContainer = true; // has TE
  }

  @Nonnull
  @Override
  public BlockEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    return new TileSmelteryComponent();
  }

  /* Pass on the missing code for seared block TE stuff */

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    block.onBlockPlacedBy(worldIn, pos, state, placer, stack);
  }

  @Override
  @Deprecated
  public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
    return block.eventReceived(state, worldIn, pos, id, param);
  }
}
