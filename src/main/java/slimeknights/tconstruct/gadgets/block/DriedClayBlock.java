package slimeknights.tconstruct.gadgets.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class DriedClayBlock extends Block {
    
    public DriedClayBlock() {
        super(Block.Settings.create(Material.STONE).hardnessAndResistance(1.5F, 20.0F).sound(BlockSoundGroup.STONE));
    }
    
}
