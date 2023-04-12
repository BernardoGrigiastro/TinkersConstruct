package slimeknights.tconstruct.tools.tools;

import com.google.common.collect.ImmutableList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

public class Excavator extends Shovel {

    public static final float DURABILITY_MODIFIER = 1.75f;

    public Excavator() {
        super(PartMaterialType.handle(TinkerTools.toughToolRod), PartMaterialType.head(TinkerTools.excavatorHead), PartMaterialType.head(TinkerTools.largePlate), PartMaterialType.extra(TinkerTools.toughBinding));
    }

    @Override
    public void getSubItems(CreativeTabs tab, DefaultedList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            addDefaultSubItems(subItems);
            addInfiTool(subItems, "InfiDigger");
        }
    }

    @Override
    public float miningSpeedModifier() {
        return 0.28f; // a bit faster than hammers to make terraforming easier
    }

    @Override
    public float damagePotential() {
        return 1.25f;
    }

    @Override
    public double attackSpeed() {
        return 0.7f;
    }

    @Override
    public ImmutableList<BlockPos> getAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin) {
        return ToolHelper.calcAOEBlocks(stack, world, player, origin, 3, 3, 1);
    }

    @Override
    public int[] getRepairParts() {
        return new int[]{1, 2};
    }

    @Override
    public float getRepairModifierForPart(int index) {
        return index == 1 ? DURABILITY_MODIFIER : DURABILITY_MODIFIER * 0.75f;
    }

    @Override
    public ToolNBT buildTagData(List<Material> materials) {
        HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
        HeadMaterialStats head = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        HeadMaterialStats plate = materials.get(2).getStatsOrUnknown(MaterialTypes.HEAD);
        ExtraMaterialStats binding = materials.get(3).getStatsOrUnknown(MaterialTypes.EXTRA);

        ToolNBT data = new ToolNBT();
        data.head(head, plate);
        data.extra(binding);
        data.handle(handle);

        data.durability *= DURABILITY_MODIFIER;

        return data;
    }
}
