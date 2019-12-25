package slimeknights.tconstruct.gadgets.block;

import net.minecraft.block.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.StringRepresentable;
import java.util.Locale;

import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;

public class BlockDriedClay extends EnumBlock<BlockDriedClay.DriedClayType> {

  public final static PropertyEnum<DriedClayType> TYPE = PropertyEnum.create("type", DriedClayType.class);

  public BlockDriedClay() {
    super(Material.STONE, TYPE, DriedClayType.class);
    this.setCreativeTab(TinkerRegistry.tabGadgets);
    this.setHardness(1.5F);
    this.setResistance(20F);
    this.setSoundType(BlockSoundGroup.STONE);
  }

  public enum DriedClayType implements StringRepresentable, EnumBlock.IEnumMeta {
    CLAY,
    BRICK;

    public final int meta;

    DriedClayType() {
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
