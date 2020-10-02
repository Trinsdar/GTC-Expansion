package gtc_expansion.util;

import ic2.api.recipe.IRecipeInput;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import trinsdar.ic2c_extras.Ic2cExtrasConfig;
import trinsdar.ic2c_extras.events.RadiationEvent;
import trinsdar.ic2c_extras.recipes.Ic2cExtrasRecipes;
import trinsdar.ic2c_extras.tileentity.TileEntityFluidCanningMachine;
import trinsdar.ic2c_extras.tileentity.TileEntityOreWashingPlant;
import trinsdar.ic2c_extras.tileentity.TileEntityThermalCentrifuge;

public class GTCXIc2cECompat {

    public static void addOreWashingMachineRecipe(String input, int count, ItemStack... output){
        TileEntityOreWashingPlant.addRecipe(new RecipeInputOreDict(input, count), 1000, output);
    }

    public static void addThermalCentrifugeRecipe(String input, int count, int heat, ItemStack... output){
        TileEntityThermalCentrifuge.addRecipe(new RecipeInputOreDict(input, count), heat, output);
    }

    public static void addFluidCanningMachineFillingRecipe(IRecipeInput input, FluidStack fluidInput, ItemStack output){
        TileEntityFluidCanningMachine.addFillingRecipe(input, fluidInput, output);
    }

    public static void removeOreWashingRecipe(String input){
        Ic2cExtrasRecipes.oreWashingPlant.removeRecipe(new RecipeInputOreDict(input, 1));
    }

    public static void removeThermalCentrifugeRecipe(String input){
        Ic2cExtrasRecipes.thermalCentrifuge.removeRecipe(new RecipeInputOreDict(input, 1));
    }

    public static void addToRadiationWhitelist(ItemStack stack){
        RadiationEvent.radiation.add(stack);
    }

    public static boolean isOverridingLossyWrench(){
        return Ic2cExtrasConfig.removeLossyWrenchMechanic;
    }

    public static boolean isForcingLead(){
        return Ic2cExtrasConfig.requiredLeadUses;
    }
}
