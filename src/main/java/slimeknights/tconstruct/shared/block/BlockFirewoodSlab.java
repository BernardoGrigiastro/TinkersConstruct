package slimeknights.tconstruct.shared.block;

import net.minecraft.block.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.sound.BlockSoundGroup;
import slimeknights.mantle.block.EnumBlockSlab;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.TinkerCommons;

public class BlockFirewoodSlab extends EnumBlockSlab<BlockFirewood.FirewoodType> {

  public BlockFirewoodSlab() {
    super(Material.WOOD, BlockFirewood.TYPE, BlockFirewood.FirewoodType.class);

    this.setHardness(2f);
    this.setResistance(7f);
    this.setCreativeTab(TinkerRegistry.tabGeneral);
    this.setLightLevel(0.5f);
    this.setSoundType(BlockSoundGroup.WOOD);
  }

  @Override
  public IBlockState getFullBlock(IBlockState state) {
    if(TinkerCommons.blockFirewood == null) {
      return null;
    }
    return TinkerCommons.blockFirewood.getDefaultState().withProperty(BlockFirewood.TYPE, state.getValue(BlockFirewood.TYPE));
  }
}
