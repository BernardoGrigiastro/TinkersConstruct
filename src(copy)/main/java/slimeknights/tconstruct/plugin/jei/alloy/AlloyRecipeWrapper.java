package slimeknights.tconstruct.plugin.jei.alloy;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

import java.util.List;

public class AlloyRecipeWrapper implements IRecipeWrapper {

    protected final List<FluidStack> inputs;
    protected final List<FluidStack> outputs;

    public AlloyRecipeWrapper(AlloyRecipe recipe) {
        this.inputs = recipe.getFluids();
        this.outputs = ImmutableList.of(recipe.getResult());
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(FluidStack.class, inputs);
        ingredients.setOutputs(FluidStack.class, outputs);
    }
}
