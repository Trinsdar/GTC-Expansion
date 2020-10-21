package gtc_expansion.jei.wrapper;

import gtclassic.api.helpers.GTValues;
import gtclassic.api.jei.GTJeiMultiRecipeWrapper;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.common.GTConfig;
import ic2.api.energy.EnergyNet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Locale;

public class GTCXJeiFusionWrapper extends GTJeiMultiRecipeWrapper {
    private MultiRecipe multiRecipe;
    public GTCXJeiFusionWrapper(MultiRecipe multiRecipe) {
        super(multiRecipe);
        this.multiRecipe = multiRecipe;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int extraHeight = 0;
        FontRenderer font = minecraft.fontRenderer;
        font.drawString("Ticks: " + getEntryTicks(multiRecipe.getOutputs()), 0, 40, Color.black.getRGB());
        font.drawString("Seconds: " + getEntryTicks(multiRecipe.getOutputs()) / 20.0F, 0, 50, Color.black.getRGB());
        font.drawString("Tier: "
                + GTValues.getTierString(EnergyNet.instance.getTierFromPower(multiRecipe.getMachineEu())), 0, 60, Color.black.getRGB());
        font.drawString("Usage: " + multiRecipe.getMachineEu() + " EU/t", 0, 70, Color.black.getRGB());
        font.drawString("Cost: "
                + NumberFormat.getNumberInstance(Locale.US).format(getEntryTicks(multiRecipe.getOutputs())
                * multiRecipe.getMachineEu())
                + " EU", 0, 80, Color.black.getRGB());
        NBTTagCompound nbt = multiRecipe.getOutputs().getMetadata();
        int startEu = nbt.hasKey("startEu") ? nbt.getInteger("startEu") : 0;
        if (startEu != 0) {
            extraHeight = 10;
            font.drawString("Start Eu: "
                    + NumberFormat.getNumberInstance(Locale.US).format(startEu)
                    + " EU", 0, 90, Color.black.getRGB());
        }
        int rTime = getEntryTicks(multiRecipe.getOutputs());
        int generateEu = startEu == 40000000 ? rTime * 60000 : startEu == 60000000 ? rTime * 62000 : rTime * 12000;
        font.drawString("Gain: "
                + NumberFormat.getNumberInstance(Locale.US).format(generateEu - (getEntryTicks(multiRecipe.getOutputs()) * multiRecipe.getMachineEu()))
                + " EU Out", 0, 90 + extraHeight, Color.black.getRGB());
        if (GTConfig.general.debugMode) {
            font.drawString("Recipe Id: " + multiRecipe.getRecipeID(), 0, 100 + extraHeight, Color.black.getRGB());
        }
    }
}
