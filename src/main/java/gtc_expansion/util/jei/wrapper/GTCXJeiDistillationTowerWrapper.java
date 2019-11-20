package gtc_expansion.util.jei.wrapper;

import gtc_expansion.util.GTFluidMachineOutput;
import gtclassic.api.helpers.GTHelperString;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.common.GTConfig;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.energy.EnergyNet;
import ic2.api.recipe.IRecipeInput;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.util.ArrayList;

public class GTCXJeiDistillationTowerWrapper implements IRecipeWrapper {

    private GTRecipeMultiInputList.MultiRecipe multiRecipe;

    public GTCXJeiDistillationTowerWrapper(GTRecipeMultiInputList.MultiRecipe multiRecipe) {
        this.multiRecipe = multiRecipe;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void getIngredients(IIngredients ingredients) {
        ArrayList<FluidStack> inputs = new ArrayList<>();
        for (IRecipeInput input : multiRecipe.getInputs()) {
            if (input instanceof RecipeInputFluid){
                inputs.add(((RecipeInputFluid)input).fluid);
            }
        }
        ingredients.setInputs(FluidStack.class, inputs);
        MachineOutput output = multiRecipe.getOutputs();
        if (output instanceof GTFluidMachineOutput){
            ingredients.setOutputs(FluidStack.class, ((GTFluidMachineOutput)output).getFluids());
        }
        ingredients.setOutputs(ItemStack.class, multiRecipe.getOutputs().getAllOutputs());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        FontRenderer font = minecraft.fontRenderer;
        font.drawString("Ticks: " + getEntryTicks(multiRecipe.getOutputs()), 0, 40, Color.black.getRGB());
        font.drawString("Seconds: " + getEntryTicks(multiRecipe.getOutputs()) / 20.0F, 0, 50, Color.black.getRGB());
        font.drawString("Tier: "
                + GTHelperString.getTierString(EnergyNet.instance.getTierFromPower(multiRecipe.getMachineEu())), 0, 60, Color.black.getRGB());
        font.drawString("Usage: " + multiRecipe.getMachineEu() + " EU/t", 0, 70, Color.black.getRGB());
        font.drawString("Cost: " + getEntryTicks(multiRecipe.getOutputs()) * multiRecipe.getMachineEu()
                + " EU", 0, 80, Color.black.getRGB());
        if (GTConfig.debugMode) {
            font.drawString("Recipe Id: " + multiRecipe.getRecipeID(), 0, 90, Color.black.getRGB());
        }
    }

    public GTRecipeMultiInputList.MultiRecipe getMultiRecipe() {
        return multiRecipe;
    }

    public static int getEntryTicks(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            return 100;
        }
        return (100 + output.getMetadata().getInteger("RecipeTime"));
    }
}
