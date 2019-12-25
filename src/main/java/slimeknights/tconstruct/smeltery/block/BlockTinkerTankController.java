package slimeknights.tconstruct.smeltery.block;

import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.smeltery.tileentity.TileTinkerTank;

import javax.annotation.Nonnull;

public class BlockTinkerTankController extends BlockMultiblockController {

    public BlockTinkerTankController() {
        super(Material.STONE);
        this.setCreativeTab(TinkerRegistry.tabSmeltery);
        this.setHardness(3F);
        this.setResistance(20F);
        this.setSoundType(BlockSoundGroup.METAL);
    }

    @Nonnull
    @Override
    public BlockEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileTinkerTank();
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
