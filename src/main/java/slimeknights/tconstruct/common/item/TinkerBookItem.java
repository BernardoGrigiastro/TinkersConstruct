package slimeknights.tconstruct.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import slimeknights.mantle.item.TooltipItem;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.TinkerBook;

import javax.annotation.Nonnull;

public class TinkerBookItem extends TooltipItem {
    
    public TinkerBookItem() {
        super(new Item.Settings().group(TinkerRegistry.tabGeneral).maxStackSize(1));
    }
    
    @Nonnull
    @Override
    public TypedActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (worldIn.isClient) {
            TinkerBook.INSTANCE.openGui(new TranslatableText("item.tconstruct.book"), itemStack);
        }
        return new TypedActionResult<>(ActionResult.field_5812, itemStack);
    }
}
