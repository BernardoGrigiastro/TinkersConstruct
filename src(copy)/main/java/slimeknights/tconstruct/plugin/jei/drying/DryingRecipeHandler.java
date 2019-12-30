package slimeknights.tconstruct.plugin.jei.drying;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import slimeknights.tconstruct.library.DryingRecipe;

import javax.annotation.Nonnull;

public class DryingRecipeHandler implements IRecipeWrapperFactory<DryingRecipe> {
    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull DryingRecipe recipe) {
        return new DryingRecipeWrapper(recipe);
    }
}
