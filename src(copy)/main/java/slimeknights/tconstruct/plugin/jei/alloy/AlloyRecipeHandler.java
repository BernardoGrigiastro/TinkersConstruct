package slimeknights.tconstruct.plugin.jei.alloy;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

import javax.annotation.Nonnull;

public class AlloyRecipeHandler implements IRecipeWrapperFactory<AlloyRecipe> {
    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull AlloyRecipe recipe) {
        return new AlloyRecipeWrapper(recipe);
    }
}
