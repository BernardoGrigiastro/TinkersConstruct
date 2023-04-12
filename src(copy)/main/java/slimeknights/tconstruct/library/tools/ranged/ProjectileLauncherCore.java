package slimeknights.tconstruct.library.tools.ranged;

import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileLauncherNBT;
import slimeknights.tconstruct.library.tools.TinkerToolCore;

import java.util.List;

public abstract class ProjectileLauncherCore extends TinkerToolCore {
    
    public ProjectileLauncherCore(PartMaterialType... requiredComponents) {
        super(requiredComponents);
    }
    
    protected ProjectileLauncherNBT getData(ItemStack stack) {
        return ProjectileLauncherNBT.from(stack);
    }
    
    @Override
    public abstract ProjectileLauncherNBT buildTagData(List<Material> materials);
}
