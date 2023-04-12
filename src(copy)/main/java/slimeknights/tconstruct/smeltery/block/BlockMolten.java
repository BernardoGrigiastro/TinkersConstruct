package slimeknights.tconstruct.smeltery.block;


import net.minecraft.block.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockMolten extends BlockTinkerFluid {

    public BlockMolten(Fluid fluid) {
        super(fluid, Material.LAVA);
    }
}
