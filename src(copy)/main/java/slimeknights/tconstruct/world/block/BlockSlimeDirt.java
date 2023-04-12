package slimeknights.tconstruct.world.block;

import net.minecraft.block.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.world.TinkerWorld;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockSlimeDirt extends EnumBlock<BlockSlimeDirt.DirtType> {

    public static PropertyEnum<DirtType> TYPE = PropertyEnum.create("type", DirtType.class);

    public BlockSlimeDirt() {
        super(Material.EARTH, TYPE, DirtType.class);
        this.setCreativeTab(TinkerRegistry.tabWorld);
        this.setHardness(0.55f);
        this.setSoundType(BlockSoundGroup.SLIME);
    }

    @Override
    public boolean canSustainPlant(
            @Nonnull IBlockState state,
            @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
        // can sustain both slimeplants and normal plants
        return plantable.getPlantType(world, pos) == TinkerWorld.slimePlantType || plantable.getPlantType(world, pos) == EnumPlantType.Plains;
    }

    public enum DirtType implements StringRepresentable, EnumBlock.IEnumMeta {
        GREEN,
        BLUE,
        PURPLE,
        MAGMA;

        public final int meta;

        DirtType() {
            this.meta = this.ordinal();
        }

        @Override
        public int getMeta() {
            return meta;
        }

        @Override
        public String getName() {
            return this.toString().toLowerCase(Locale.US);
        }
    }
}
