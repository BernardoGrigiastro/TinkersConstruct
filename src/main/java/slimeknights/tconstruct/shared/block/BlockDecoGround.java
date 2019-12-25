package slimeknights.tconstruct.shared.block;

import net.minecraft.block.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.StringRepresentable;
import java.util.Locale;

import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;

public class BlockDecoGround extends EnumBlock<BlockDecoGround.DecoGroundType> {

  public final static PropertyEnum<DecoGroundType> TYPE = PropertyEnum.create("type", DecoGroundType.class);

  public BlockDecoGround() {
    super(Material.EARTH, TYPE, DecoGroundType.class);

    this.setHardness(2.0f);

    this.setSoundType(BlockSoundGroup.GRAVEL);

    setHarvestLevel("shovel", -1);
    setCreativeTab(TinkerRegistry.tabGeneral);
  }

  public enum DecoGroundType implements StringRepresentable, EnumBlock.IEnumMeta {
    MUDBRICK;

    public final int meta;

    DecoGroundType() {
      meta = ordinal();
    }

    @Override
    public String getName() {
      return this.toString().toLowerCase(Locale.US);
    }

    @Override
    public int getMeta() {
      return meta;
    }
  }
}
