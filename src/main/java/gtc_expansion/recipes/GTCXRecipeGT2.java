package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXItems;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTConfig;
import gtclassic.common.GTItems;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.item.recipe.upgrades.EnchantmentModifier;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GTCXRecipeGT2 {
    static GTCXRecipe instance = new GTCXRecipe();
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    static String ingotRefinedIron = IC2.getRefinedIron();
    static IRecipeInput ingotMachine = new RecipeInputCombined(1, new RecipeInputOreDict(ingotRefinedIron), new RecipeInputOreDict("ingotAluminium"));
    public static IRecipeInput plateMachine = new RecipeInputCombined(1, new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateAluminium"));
    static IRecipeInput ingotSteels = new RecipeInputCombined(1, new RecipeInputOreDict("ingotSteel"), new RecipeInputOreDict("ingotStainlessSteel"));
    public static IRecipeInput plateSteels = new RecipeInputCombined(1, new RecipeInputOreDict("plateSteel"), new RecipeInputOreDict("plateStainlessSteel"));
    static IRecipeInput materialMachine = GTCXConfiguration.general.usePlates ? plateMachine : ingotMachine;
    static IRecipeInput materialSteels = GTCXConfiguration.general.usePlates ? plateSteels : ingotSteels;

    static IRecipeInput grinder = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(GTCXItems.diamondGrinder)), new RecipeInputItemStack(GTMaterialGen.get(GTCXItems.wolframiumGrinder)));

    static String titanium = GTCXConfiguration.general.usePlates ? "plateTitanium" : "ingotTitanium";

    public static String getRefinedIronPlate() {
        return IC2.config.getFlag("SteelRecipes") ? "plateSteel" : "plateRefinedIron";
    }

    public static void init(){
        initIc2();
        initOverrideGTClassic();
    }

    public static void initIc2(){
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FFF", "TTT", "FFF", 'F', "dustFlint", 'T', Blocks.TNT);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FTF", "FTF", "FTF", 'F', "dustFlint", 'T', Blocks.TNT);
        int recipeId = IC2.config.getFlag("SteelRecipes") ? 480320652 : 527557260;
        recipes.overrideRecipe("shaped_tile.blockmachine_" + recipeId, Ic2Items.machine, "PPP", "P P", "PPP", 'P', ingotRefinedIron);
        ItemStack battery = Ic2Items.battery;
        String circuit = "circuitBasic";
        recipes.overrideRecipe("shaped_item.itemtoolwrenchelectric_883008511", Ic2Items.electricWrench, "SWS", "SCS", " B ",'S', materialSteels, 'W', Ic2Items.wrench, 'C', circuit, 'B', battery);
        if (GTConfig.general.harderIC2Macerator) {
            recipes.overrideRecipe("shaped_tile.blockMacerator_127744036", Ic2Items.macerator.copy(), "III", "IMI", "ICI", 'I', ingotRefinedIron, 'M', Ic2Items.stoneMacerator.copy(), 'C',
                    "circuitAdvanced");
            recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), "FDF", "DMD", "FCF", 'D', "gemDiamond", 'F', Items.FLINT, 'M', "machineBlockBasic", 'C',
                    "circuitAdvanced");
            recipes.addRecipe(Ic2Items.macerator.copy(), "FGF", "CMC", "FCF", 'G', grinder, 'F', Items.FLINT, 'M', "machineBlockBasic", 'C',
                    "circuitBasic");
        }
    }

    public static void initOverrideGTClassic(){
        int recipeId = IC2.config.getFlag("SteelRecipes") ? -654096286 : 1664690250;
        instance.overrideGTRecipe("shaped_item.gtclassic.rockcutter_" + recipeId, GTMaterialGen.get(GTItems.rockCutter), "DT ", "DT ", "DCB", new EnchantmentModifier(GTMaterialGen.get(GTItems.rockCutter), Enchantments.SILK_TOUCH).setUsesInput(), 'T', titanium, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        instance.overrideGTRecipe("shaped_item.gtclassic.jackhammer_2107301811", GTMaterialGen.get(GTItems.jackHammer), "TBT", " C ", " D ", 'T', titanium, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        recipeId = IC2.config.getFlag("SteelRecipes") ? -825500942 : -1110475998;
        instance.overrideGTRecipe("shaped_tile.gtclassic.industrialcentrifuge_" + recipeId, GTMaterialGen.get(GTBlocks.tileCentrifuge), "RCR", "MEM", "RCR", 'R', materialMachine, 'C', "circuitAdvanced", 'M', "machineBlockAdvanced", 'E', Ic2Items.extractor);
    }
}
