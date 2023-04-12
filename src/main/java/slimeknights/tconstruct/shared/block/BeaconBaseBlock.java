package slimeknights.tconstruct.shared.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ViewableWorld;

public class BeaconBaseBlock extends Block {
    
    public BeaconBaseBlock(Settings properties) {
        super(properties);
    }
    
    @Override
    public boolean isBeaconBase(BlockState state, ViewableWorld world, BlockPos pos, BlockPos beacon) {
        return true;
    }
}
