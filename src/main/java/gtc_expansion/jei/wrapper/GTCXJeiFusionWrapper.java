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
        if ((nbt.hasKey("startEu") && nbt.getInteger("startEu") != 0)) {
            int startEu = nbt.getInteger("startEu");
            boolean helium = startEu == 40000000 || startEu == 60000000;
            extraHeight = helium ? 20 : 10;
            font.drawString("Start Eu: "
                    + NumberFormat.getNumberInstance(Locale.US).format(startEu
                    * multiRecipe.getMachineEu())
                    + " EU", 0, 90, Color.black.getRGB());
            if (helium){
                int rTime = getEntryTicks(multiRecipe.getOutputs());
                int generateEu = startEu == 40000000 ? rTime * 60000 : rTime * 62000;
                font.drawString("Output: "
                        + NumberFormat.getNumberInstance(Locale.US).format(generateEu)
                        + " EU Out", 0, 100, Color.black.getRGB());
            }
        }
        if (GTConfig.general.debugMode) {
            font.drawString("Recipe Id: " + multiRecipe.getRecipeID(), 0, 90 + extraHeight, Color.black.getRGB());
        }
    }
}
