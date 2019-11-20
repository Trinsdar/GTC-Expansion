package gtc_expansion.util;

import ic2.core.item.recipe.entry.RecipeInputOreDict;
import net.minecraft.item.ItemStack;
import trinsdar.ic2c_extras.recipes.MachineRecipes;
import trinsdar.ic2c_extras.tileentity.TileEntityOreWashingPlant;
import trinsdar.ic2c_extras.tileentity.TileEntityRoller;
import trinsdar.ic2c_extras.tileentity.TileEntityThermalCentrifuge;

public class GTCXIc2cECompat {

    public static void addToRollerIngotBlacklist(String name){
        MachineRecipes.ingotBlacklist.add("ingot" + name);
    }

    public static void addRollerRecipe(String input, int count, ItemStack output){
        TileEntityRoller.addRecipe(input, count, output);
    }

    public static void addOreWashingMachineRecipe(String input, int count, ItemStack... output){
        TileEntityOreWashingPlant.addRecipe(new RecipeInputOreDict(input, count), 1000, output);
    }

    public static void addThermalCentrifugeRecipe(String input, int count, int heat, ItemStack... output){
        TileEntityThermalCentrifuge.addRecipe(new RecipeInputOreDict(input, count), heat, output);
    }
}
