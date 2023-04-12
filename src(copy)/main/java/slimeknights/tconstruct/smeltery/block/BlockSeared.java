package slimeknights.tconstruct.smeltery.block;

import net.minecraft.block.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.StringRepresentable;
import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;

import java.util.Locale;

public class BlockSeared extends BlockEnumSmeltery<BlockSeared.SearedType> {

    public final static PropertyEnum<SearedType> TYPE = PropertyEnum.create("type", SearedType.class);

    public BlockSeared() {
        super(Material.STONE, TYPE, SearedType.class);
        this.setCreativeTab(TinkerRegistry.tabSmeltery);
        this.setHardness(3F);
        this.setResistance(20F);
        this.setSoundType(BlockSoundGroup.METAL);
    }

    public enum SearedType implements StringRepresentable, EnumBlock.IEnumMeta {
        STONE,
        COBBLE,
        PAVER,
        BRICK,
        BRICK_CRACKED,
        BRICK_FANCY,
        BRICK_SQUARE,
        ROAD,
        CREEPER,
        BRICK_TRIANGLE,
        BRICK_SMALL,
        TILE;

        public final int meta;

        SearedType() {
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
