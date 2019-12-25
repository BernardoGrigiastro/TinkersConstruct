package slimeknights.tconstruct.shared.block;

import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

import javax.annotation.Nonnull;

import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.utils.HarvestLevels;

public class BlockOre extends EnumBlock<BlockOre.OreTypes> {

  public static final PropertyEnum<OreTypes> TYPE = PropertyEnum.create("type", OreTypes.class);

  public BlockOre() {
    this(Material.STONE);
  }

  public BlockOre(Material material) {
    super(material, TYPE, OreTypes.class);

    setHardness(10f);
    setHarvestLevel("pickaxe", HarvestLevels.COBALT);
    setCreativeTab(TinkerRegistry.tabWorld);
  }

  @Nonnull
  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.MIPPED_CUTOUT;
  }

  public enum OreTypes implements StringRepresentable, EnumBlock.IEnumMeta {
    COBALT,
    ARDITE;

    public final int meta;

    OreTypes() {
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
