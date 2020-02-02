package gtc_expansion.jei.wrapper;

import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.jei.GTJeiMultiRecipeWrapper;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.common.GTConfig;
import ic2.api.energy.EnergyNet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.Color;

public class GTCXJeiHeatWrapper extends GTJeiMultiRecipeWrapper {
    public GTCXJeiHeatWrapper(GTRecipeMultiInputList.MultiRecipe multiRecipe) {
        super(multiRecipe);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        FontRenderer font = minecraft.fontRenderer;
        font.drawString("Ticks: " + getEntryTicks(this.getMultiRecipe().getOutputs()), 0, 40, Color.black.getRGB());
        font.drawString("Seconds: " + (float)getEntryTicks(this.getMultiRecipe().getOutputs()) / 20.0F, 0, 50, Color.black.getRGB());
        font.drawString("Tier: " + GTValues.getTierString(EnergyNet.instance.getTierFromPower((double)this.getMultiRecipe().getMachineEu())), 0, 60, Color.black.getRGB());
        font.drawString("Usage: " + this.getMultiRecipe().getMachineEu() + " EU/t", 0, 70, Color.black.getRGB());
        font.drawString("Cost: " + getEntryTicks(this.getMultiRecipe().getOutputs()) * this.getMultiRecipe().getMachineEu() + " EU", 0, 80, Color.black.getRGB());
        String heatInfo = this.getMultiRecipe().getMachineEu() == 64 ? "Min Needed Heat: " : "Heat Capacity: ";
        font.drawString(heatInfo + GTCXTileMultiIndustrialBlastFurnace.getRequiredHeat(this.getMultiRecipe().getOutputs()) + " K", 0, 90, Color.black.getRGB());
        if (GTConfig.general.debugMode) {
            font.drawString("Recipe Id: " + this.getMultiRecipe().getRecipeID(), 0,     100, Color.black.getRGB());
        }

    }

}
