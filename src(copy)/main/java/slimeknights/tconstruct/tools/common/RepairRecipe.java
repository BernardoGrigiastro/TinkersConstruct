package slimeknights.tconstruct.tools.common;

import com.google.common.collect.ImmutableSet;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.tools.TinkerTools;

import javax.annotation.Nonnull;
import java.util.Set;

public class RepairRecipe extends Impl<Recipe> implements Recipe {

    private static final Set<Item> repairItems = ImmutableSet.of(TinkerTools.sharpeningKit);

    public RepairRecipe() {
        this.setRegistryName(Util.getResource("repair"));
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        return !getRepairedTool(inv, true).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        return getRepairedTool(inv, true);
    }

    @Nonnull
    private ItemStack getRepairedTool(@Nonnull InventoryCrafting inv, boolean simulate) {

        ItemStack tool = null;
        DefaultedList<ItemStack> input = DefaultedList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack slot = inv.getStackInSlot(i);
            // empty slot
            if (slot.isEmpty()) {
                continue;
            }

            slot = slot.copy();
            slot.setCount(1);

            Item item = slot.getItem();

            // is it the tool?
            if (item instanceof TinkersItem) {
                // stop if we already have a tool, 2 tools present
                if (tool != null) {
                    return ItemStack.EMPTY;
                }
                tool = slot;
            }
            // otherwise.. input material
            else if (repairItems.contains(item)) {
                input.set(i, slot);
            }
            // invalid item
            else {
                return ItemStack.EMPTY;
            }
        }
        // no tool found?
        if (tool == null) {
            return ItemStack.EMPTY;
        }

        if (simulate) {
            input = Util.deepCopyFixedNonNullList(input);
        }

        // do the repairing, also checks for valid input
        return ((TinkersItem) tool.getItem()).repair(tool.copy(), input);
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public DefaultedList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
        return DefaultedList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

}
