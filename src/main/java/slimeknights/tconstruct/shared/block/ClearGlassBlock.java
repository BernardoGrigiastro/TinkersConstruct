package slimeknights.tconstruct.shared.block;

import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.mantle.block.ConnectedTextureBlock;

public class ClearGlassBlock extends ConnectedTextureBlock {
    
    public ClearGlassBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.field_9174;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, BlockView worldIn, BlockPos pos) {
        return 1.0F;
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockView reader, BlockPos pos) {
        return true;
    }
    
    @Override
    public boolean causesSuffocation(BlockState state, BlockView worldIn, BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean isNormalCube(BlockState state, BlockView worldIn, BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean canEntitySpawn(BlockState state, BlockView worldIn, BlockPos pos, EntityType<?> type) {
        return false;
    }
}
