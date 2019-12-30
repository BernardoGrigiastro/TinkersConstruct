package slimeknights.tconstruct.shared.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import slimeknights.tconstruct.world.TinkerWorld;

public class SlimyMudBlock extends Block {
    
    private final MudType mudType;
    
    public SlimyMudBlock(Settings properties, MudType mudType) {
        super(properties);
        this.mudType = mudType;
    }
    
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        switch (this.mudType) {
            case SLIMY_MUD_GREEN:
            case SLIMY_MUD_BLUE:
                this.processSlimyMud(entityIn);
                break;
        }
    }
    
    // slow down
    private void processSlimyMud(Entity entity) {
        entity.getMotion().mul(0.4D, 1.0D, 0.4D);
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addPotionEffect(new StatusEffectInstance(StatusEffects.field_5911, 1));
        }
    }
    
    @Override
    public boolean canSustainPlant(BlockState state, BlockView world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
        if (this.mudType == MudType.SLIMY_MUD_GREEN || this.mudType == MudType.SLIMY_MUD_BLUE) {
            return plantable.getPlantType(world, pos) == TinkerWorld.slimePlantType;
        }
        
        return super.canSustainPlant(state, world, pos, facing, plantable);
    }
    
    public enum MudType {
        SLIMY_MUD_GREEN,
        SLIMY_MUD_BLUE,
        SLIMY_MUD_MAGMA
    }
    
}
