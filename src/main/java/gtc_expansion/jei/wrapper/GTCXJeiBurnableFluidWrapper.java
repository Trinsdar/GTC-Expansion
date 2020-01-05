package gtc_expansion.jei.wrapper;

import gtc_expansion.tile.base.GTCXTileBaseBurnableFluidGenerator;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.awt.Color;
import java.util.ArrayList;

public class GTCXJeiBurnableFluidWrapper implements IRecipeWrapper {

	private MultiRecipe multiRecipe;

	public GTCXJeiBurnableFluidWrapper(MultiRecipe multiRecipe) {
		this.multiRecipe = multiRecipe;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<FluidStack> inputFluids = new ArrayList<>();
		for (IRecipeInput input : multiRecipe.getInputs()) {
			if (input instanceof RecipeInputFluid) {
				inputFluids.add(((RecipeInputFluid) input).fluid);
			}
		}
		if (!inputFluids.isEmpty()) {
			ingredients.setInputs(FluidStack.class, inputFluids);
		}
		ingredients.setOutputs(ItemStack.class, multiRecipe.getOutputs().getAllOutputs());
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		FontRenderer font = minecraft.fontRenderer;
		if (multiRecipe.getInputs().get(0) instanceof RecipeInputFluid) {
			int euPerTick = GTCXTileBaseBurnableFluidGenerator.getRecipeEu(multiRecipe.getOutputs());
			int totalTicks = GTCXTileBaseBurnableFluidGenerator.getRecipeTicks(multiRecipe.getOutputs());
			int totalEu = euPerTick * totalTicks;
			font.drawString("Total: " + totalEu +  " EU", 0, 40, Color.black.getRGB());
			font.drawString("For: " + totalTicks +  " Ticks", 0, 50, Color.black.getRGB());
			font.drawString("Per: 1000ml", 0, 60, Color.black.getRGB());
			font.drawString("Production: " + euPerTick + " EU/t", 0, 70, Color.black.getRGB());
		}
	}

	public MultiRecipe getMultiRecipe() {
		return multiRecipe;
	}

	public static int getEntryValue(MachineOutput output) {
		if (output == null || output.getMetadata() == null) {
			return 0;
		}
		return (output.getMetadata().getInteger("RecipeTime"));
	}
}