package gtc_expansion.recipes;

import gtc_expansion.GTBlocks2;
import gtc_expansion.GTItems2;
import gtc_expansion.tile.GTTileAlloySmelter;
import gtc_expansion.tile.GTTileElectrolyzer;
import gtc_expansion.tile.multi.GTTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTTileMultiVacuumFreezer;
import gtclassic.GTBlocks;
import gtclassic.material.GTMaterialGen;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;

public class GTRecipe2 {
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    static String ingotRefinedIron = IC2.getRefinedIron();
    static IRecipeInput ingotMachine = new RecipeInputCombined(1, new IRecipeInput[] {
            new RecipeInputOreDict(ingotRefinedIron), new RecipeInputOreDict("ingotAluminium") });
    static IRecipeInput reinforcedGlass = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.reinforcedGlass.copy()), new RecipeInputItemStack(Ic2Items.reinforcedGlassClear.copy()));

    public static void init(){
        GTRecipeRemove.init();
        GTRecipeBlastFurnace.init();
        GTRecipeCentrifuge.init();
        GTTileElectrolyzer.init();
        GTTileMultiVacuumFreezer.init();
        GTTileMultiImplosionCompressor.init();
        GTTileAlloySmelter.init();
        initCraftingOverrideRecipe();
        initShapedRecipes();
        GTRecipeIterators2.init();
    }

    public static void postInit(){
        GTRecipeBlastFurnace.removals();

    }

    public static void initShapedRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GTItems2.computerMonitor), "IGI", "RPB", "IgI", 'I', "ingotAluminium", 'G', "dyeGreen", 'R', "dyeRed", 'P', "paneGlass", 'B', "dyeBlue", 'g', "dustGlowstone");
        recipes.addRecipe(GTMaterialGen.get(GTItems2.conveyorModule), "GGG", "AAA", "CBC", 'G', "blockGlass", 'A', "ingotAluminium", 'C', "circuitBasic", 'B', Ic2Items.battery.copy());
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.implosionCompressor), "AMA", "cCc", "AMA", 'A', Ic2Items.advancedAlloy.copy(), 'M', Ic2Items.advMachine.copy(), 'c', "circuitBasic", 'C', Ic2Items.compressor.copy());
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.electrolyzer), "IEI", "AeA", "IEI", 'I', ingotMachine, 'E', Ic2Items.extractor.copy(), 'A', "circuitAdvanced", 'e', Ic2Items.electrolyzer.copy());
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.vacuumFreezer), "IPI", "AGA", "IPI", 'I', ingotMachine, 'P', Ic2Items.pump.copy(), 'A', "circuitAdvanced", 'G', reinforcedGlass);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.alloySmelter), "IcI", "CFC", "IMI", 'I', "ingotInvar", 'c', "ingotConstantan", 'C', "circuitBasic", 'F', Ic2Items.electroFurnace.copy(), 'M', GTItems2.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.casingStandard, 4), "III", "CBC", "III", 'I', ingotMachine, 'C', "circuitBasic", 'B', Ic2Items.machine.copy());
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.casingAdvanced, 4), "III", "CBC", "III", 'I', "ingotChrome", 'C', "circuitElite", 'B', GTBlocks.casingHighlyAdvanced);
    }

    public static void initCraftingOverrideRecipe(){
        recipes.overrideRecipe("shaped_item.itempartiridium_1100834802", GTMaterialGen.get(GTItems2.iridiumAlloyIngot), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'A', Ic2Items.advancedAlloy, 'D', "dustDiamond");
    }
}
