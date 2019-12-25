package slimeknights.tconstruct.gadgets.block;

import net.minecraft.block.BlockLadder;
import net.minecraft.sound.BlockSoundGroup;
import slimeknights.tconstruct.library.TinkerRegistry;

public class BlockStoneLadder extends BlockLadder {

  public BlockStoneLadder() {
    this.setHardness(0.1F); // much less than stone ladder
    this.setSoundType(BlockSoundGroup.STONE);

    this.setCreativeTab(TinkerRegistry.tabGadgets);
  }
}
