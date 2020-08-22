package gtc_expansion.jei.wrapper;

import ic2.api.recipe.IRecipeInput;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import java.awt.Color;

public class GTCXJeiIntegratedCircuitWrapper implements IRecipeWrapper {
    IntegratedCircuitRecipe recipe;

    public GTCXJeiIntegratedCircuitWrapper(IntegratedCircuitRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, recipe.getInput().getInputs());
        ingredients.setOutputs(ItemStack.class, recipe.getOutput().getInputs());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        FontRenderer font = minecraft.fontRenderer;
        font.drawString("Integrated", 0, 20, Color.black.getRGB());
        font.drawString("Circuit is cycled", 0 , 30, Color.black.getRGB());
        font.drawString("with shift right click.", 0, 40, Color.black.getRGB());
    }

    public IntegratedCircuitRecipe getRecipe() {
        return recipe;
    }

    public static class IntegratedCircuitRecipe{
        IRecipeInput input;
        IRecipeInput output;
        public IntegratedCircuitRecipe(IRecipeInput input, IRecipeInput output){
            this.input = input;
            this.output = output;
        }

        public IRecipeInput getInput() {
            return input;
        }

        public IRecipeInput getOutput() {
            return output;
        }
    }
}
