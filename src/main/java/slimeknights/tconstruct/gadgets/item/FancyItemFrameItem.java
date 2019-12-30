package slimeknights.tconstruct.gadgets.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class FancyItemFrameItem extends Item {
    
    private final TriFunction<? extends AbstractDecorationEntity, World, BlockPos, Direction> entityProvider;
    
    public FancyItemFrameItem(TriFunction<? extends AbstractDecorationEntity, World, BlockPos, Direction> entityProvider) {
        super(new Settings().group(ItemGroup.DECORATIONS));
        this.entityProvider = entityProvider;
    }
    
    /**
     * Called when this item is used when targetting a Block
     */
    @Override
    @Nonnull
    public ActionResult onItemUse(ItemUsageContext context) {
        BlockPos pos = context.getPos();
        Direction facing = context.getFace();
        BlockPos placeLocation = pos.offset(facing);
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItem();
        if (player != null && !this.canPlace(player, facing, stack, placeLocation)) {
            return ActionResult.field_5814;
        } else {
            World world = context.getWorld();
            AbstractDecorationEntity frame = this.entityProvider.apply(world, placeLocation, facing);
            
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                EntityType.applyItemNBT(world, player, frame, tag);
            }
            
            if (frame.onValidSurface()) {
                if (!world.isClient) {
                    frame.playPlaceSound();
                    world.addEntity(frame);
                }
                
                stack.shrink(1);
            }
            
            return ActionResult.field_5812;
        }
    }
    
    private boolean canPlace(PlayerEntity player, Direction facing, ItemStack stack, BlockPos pos) {
        return !World.isOutsideBuildHeight(pos) && player.canPlayerEdit(pos, facing, stack);
    }
    
    @FunctionalInterface
    public interface TriFunction<R, T, U, V> {
        
        R apply(T t, U u, V v);
    }
}
