package slimeknights.tconstruct.tools.tools;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import java.util.List;

import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.AoeToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerTools;

public class Pickaxe extends AoeToolCore {

  public static final ImmutableSet<net.minecraft.block.Material> effective_materials =
      ImmutableSet.of(net.minecraft.block.Material.METAL,
                      net.minecraft.block.Material.ANVIL,
                      net.minecraft.block.Material.STONE,
                      net.minecraft.block.Material.ICE,
                      net.minecraft.block.Material.GLASS,
                      net.minecraft.block.Material.PACKED_ICE,
                      net.minecraft.block.Material.PISTON);

  // Pick-head, binding, tool-rod
  public Pickaxe() {
    this(PartMaterialType.handle(TinkerTools.toolRod),
         PartMaterialType.head(TinkerTools.pickHead),
         PartMaterialType.extra(TinkerTools.binding));
  }

  public Pickaxe(PartMaterialType... requiredComponents) {
    super(requiredComponents);

    addCategory(Category.HARVEST);

    // set the toolclass, actual harvestlevel is done by the overridden callback
    this.setHarvestLevel("pickaxe", 0);
  }

  @Override
  public void getSubItems(CreativeTabs tab, DefaultedList<ItemStack> subItems) {
    if(this.isInCreativeTab(tab)) {
      addDefaultSubItems(subItems);
      addInfiTool(subItems, "InfiHarvester");
    }
  }

  @Override
  public boolean isEffective(IBlockState state) {
    return effective_materials.contains(state.getMaterial()) || ItemPickaxe.EFFECTIVE_ON.contains(state.getBlock());
  }

  @Override
  public float damagePotential() {
    return 1f;
  }

  @Override
  public double attackSpeed() {
    return 1.2f;
  }

  @Override
  protected ToolNBT buildTagData(List<Material> materials) {
    return buildDefaultTag(materials);
  }
}
