package slimeknights.tconstruct.plugin.jei.smelting;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

import javax.annotation.Nonnull;

public class SmeltingRecipeHandler implements IRecipeWrapperFactory<MeltingRecipe> {
    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull MeltingRecipe recipe) {
        return new SmeltingRecipeWrapper(recipe);
    }
}
