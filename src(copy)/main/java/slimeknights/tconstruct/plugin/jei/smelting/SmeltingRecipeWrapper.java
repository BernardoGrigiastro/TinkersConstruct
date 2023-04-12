package slimeknights.tconstruct.plugin.jei.smelting;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class SmeltingRecipeWrapper implements IRecipeWrapper {

    protected final List<List<ItemStack>> inputs;
    protected final List<FluidStack> outputs;
    protected final int temperature;
    protected final List<FluidStack> fuels;

    public SmeltingRecipeWrapper(MeltingRecipe recipe) {
        this.inputs = ImmutableList.of(recipe.input.getInputs());
        this.outputs = ImmutableList.of(recipe.getResult());
        this.temperature = recipe.getTemperature();

        ImmutableList.Builder<FluidStack> builder = ImmutableList.builder();
        for (FluidStack fs : TinkerRegistry.getSmelteryFuels()) {
            if (fs.getFluid().getTemperature(fs) >= temperature) {
                fs = fs.copy();
                fs.amount = 1000;
                builder.add(fs);
            }
        }
        fuels = builder.build();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutputs(FluidStack.class, outputs);
    }

    @Override
    public void drawInfo(
            @Nonnull MinecraftClient minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        String tmpStr = Util.temperatureString(temperature);
        int x = 80 - minecraft.textRenderer.getStringWidth(tmpStr) / 2;
        minecraft.textRenderer.drawString(tmpStr, x, 10, Color.gray.getRGB());
    }
}
