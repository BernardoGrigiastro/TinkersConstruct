package slimeknights.tconstruct.gadgets.block;

import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.sound.BlockSoundGroup;

public class DriedClaySlabBlock extends SlabBlock {
    
    public DriedClaySlabBlock() {
        super(Settings.create(Material.STONE).hardnessAndResistance(3.0F, 20.0F).sound(BlockSoundGroup.STONE).lightValue(7));
    }
    
}
