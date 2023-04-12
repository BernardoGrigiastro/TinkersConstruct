package slimeknights.tconstruct.tools.ranged.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileLauncherNBT;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

public class LongBow extends ShortBow {
    
    // little more durability due to the plate
    public static final float DURABILITY_MODIFIER = 1.4f;
    
    public LongBow() {
        super(PartMaterialType.bow(TinkerTools.bowLimb), PartMaterialType.bow(TinkerTools.bowLimb), PartMaterialType.extra(TinkerTools.largePlate), PartMaterialType.bowstring(TinkerTools.bowString));
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, DefaultedList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            addDefaultSubItems(subItems, null, null, null, TinkerMaterials.string);
        }
    }
    
    /* Tic Tool Stuff */
    
    @Override
    public double attackSpeed() {
        return 1.3;
    }
    
    @Override
    public float baseProjectileDamage() {
        return 2.5f;
    }
    
    @Override
    protected float baseProjectileSpeed() {
        return 5.5f;
    }
    
    @Override
    protected float baseInaccuracy() {
        return 1.2f;
    }
    
    @Override
    public float projectileDamageModifier() {
        return 1.25f;
    }
    
    @Override
    public int getDrawTime() {
        return 30;
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        // no speedup on charging
        onUpdateTraits(stack, worldIn, entityIn, itemSlot, isSelected);
    }
    
    /* Data Stuff */
    
    @Override
    public ProjectileLauncherNBT buildTagData(List<Material> materials) {
        ProjectileLauncherNBT data = new ProjectileLauncherNBT();
        HeadMaterialStats head1 = materials.get(0).getStatsOrUnknown(MaterialTypes.HEAD);
        HeadMaterialStats head2 = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        BowMaterialStats limb1 = materials.get(0).getStatsOrUnknown(MaterialTypes.BOW);
        BowMaterialStats limb2 = materials.get(1).getStatsOrUnknown(MaterialTypes.BOW);
        ExtraMaterialStats grip = materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA);
        BowStringMaterialStats bowstring = materials.get(3).getStatsOrUnknown(MaterialTypes.BOWSTRING);
        
        data.head(head1, head2);
        data.limb(limb1, limb2);
        data.extra(grip);
        data.bowstring(bowstring);
        
        data.durability *= DURABILITY_MODIFIER;
        
        return data;
    }
}
