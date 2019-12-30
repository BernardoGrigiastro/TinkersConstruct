package slimeknights.tconstruct.gadgets.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SnowballItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.mantle.util.LocUtils;
import slimeknights.tconstruct.gadgets.entity.GlowballEntity;
import slimeknights.tconstruct.library.TinkerRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class GlowBallItem extends SnowballItem {
    
    public GlowBallItem() {
        super((new Settings()).maxStackSize(16).group(TinkerRegistry.tabGadgets));
    }
    
    @Override
    public TypedActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!playerIn.abilities.creativeMode) {
            itemstack.shrink(1);
        }
        
        worldIn.playSound((PlayerEntity) null, playerIn.x, playerIn.y, playerIn.z, SoundEvents.field_14873, SoundCategory.field_15254, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isClient) {
            GlowballEntity glowballEntity = new GlowballEntity(worldIn, playerIn);
            glowballEntity.setItem(itemstack);
            glowballEntity.shoot(playerIn, playerIn.pitch, playerIn.yaw, 0.0F, 1.5F, 1.0F);
            worldIn.addEntity(glowballEntity);
        }
        
        playerIn.addStat(Stats.field_15372.get(this));
        return new TypedActionResult<>(ActionResult.field_5812, itemstack);
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        if (I18n.hasKey(stack.getTranslationKey() + ".tooltip")) {
            tooltip.addAll(LocUtils.getTooltips(Formatting.field_1080.toString() + LocUtils.translateRecursive(stack.getTranslationKey() + ".tooltip", new Object[0])));
        }
        
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
