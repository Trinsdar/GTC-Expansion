package gtc_expansion.recipes;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEItems;
import gtc_expansion.tile.GETileAlloySmelter;
import gtc_expansion.tile.GETileElectrolyzer;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiVacuumFreezer;
import gtclassic.GTBlocks;
import gtclassic.GTConfig;
import gtclassic.GTItems;
import gtclassic.material.GTMaterialGen;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Items;

public class GERecipe {
    static GERecipe instance = new GERecipe();
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    static String ingotRefinedIron = IC2.getRefinedIron();
    static IRecipeInput ingotMachine = new RecipeInputCombined(1, new IRecipeInput[] {
            new RecipeInputOreDict(ingotRefinedIron), new RecipeInputOreDict("ingotAluminium") });
    static IRecipeInput plateMachine = new RecipeInputCombined(1, new IRecipeInput[] {
            new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateAluminium") });
    static IRecipeInput reinforcedGlass = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.reinforcedGlass), new RecipeInputItemStack(Ic2Items.reinforcedGlassClear));
    static IRecipeInput grinder = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(GEItems.diamondGrinder)), new RecipeInputItemStack(GTMaterialGen.get(GEItems.wolframiumGrinder)));

    public static String getRefinedIronPlate() {
        return IC2.config.getFlag("SteelRecipes") ? "plateSteel" : "plateRefinedIron";
    }

    public static void init(){
        GERecipeRemove.init();
        GERecipeProcessing.init();
        GETileElectrolyzer.init();
        GETileMultiVacuumFreezer.init();
        GETileMultiImplosionCompressor.init();
        GETileAlloySmelter.init();
        initIc2();
        initOverrideGTClassic();
        initShapedItemRecipes();
        initShapedBlockRecipes();
        GERecipeIterators.init();
    }

    public static void postInit(){
        GERecipeProcessing.removals();

    }

    public static void initShapedItemRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GEItems.computerMonitor), "IGI", "RPB", "IgI", 'I', "ingotAluminium", 'G', "dyeGreen", 'R', "dyeRed", 'P', "paneGlass", 'B', "dyeBlue", 'g', "dustGlowstone");
        recipes.addRecipe(GTMaterialGen.get(GEItems.conveyorModule), "GGG", "AAA", "CBC", 'G', "blockGlass", 'A', "ingotAluminium", 'C', "circuitBasic", 'B', Ic2Items.battery.copy());
        recipes.addRecipe(GTMaterialGen.get(GTItems.chipData, 4), "GGG", "GCG", "GGG", 'G', "gemOlivine", 'C', "circuitAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GEItems.diamondGrinder, 2), "DSD", "SdS", "DSD", 'D', "dustDiamond", 'S', "ingotSteel", 'd', "gemDiamond");
        recipes.addRecipe(GTMaterialGen.get(GEItems.wolframiumGrinder, 2), "TST", "SBS", "TST", 'T', "ingotTungsten", 'S', "ingotSteel", 'B', "blockSteel");
    }

    public static void initShapedBlockRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.implosionCompressor), "AMA", "cCc", "AMA", 'A', Ic2Items.advancedAlloy.copy(), 'M', Ic2Items.advMachine.copy(), 'c', "circuitBasic", 'C', Ic2Items.compressor.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.electrolyzer), "IEI", "AeA", "IEI", 'I', ingotMachine, 'E', Ic2Items.extractor.copy(), 'A', "circuitAdvanced", 'e', Ic2Items.electrolyzer.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.vacuumFreezer), "IPI", "AGA", "IPI", 'I', ingotMachine, 'P', Ic2Items.pump.copy(), 'A', "circuitAdvanced", 'G', reinforcedGlass);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.alloySmelter), "IcI", "CFC", "IMI", 'I', "ingotInvar", 'c', "ingotConstantan", 'C', "circuitBasic", 'F', Ic2Items.electroFurnace.copy(), 'M', GEItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.industrialGrinder), "ECP", "GGG", "CMC", 'E', GEBlocks.electrolyzer, 'C', "circuitAdvanced", 'P', Ic2Items.pump.copy(), 'G', grinder, 'M', Ic2Items.advMachine);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.casingStandard, 4), "III", "CBC", "III", 'I', ingotMachine, 'C', "circuitBasic", 'B', Ic2Items.machine.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.casingAdvanced, 4), "III", "CBC", "III", 'I', "ingotChrome", 'C', "circuitElite", 'B', GTBlocks.casingHighlyAdvanced);
    }

    public static void initIc2(){
        recipes.overrideRecipe("shaped_item.itempartiridium_1100834802", GTMaterialGen.get(GEItems.iridiumAlloyIngot), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'A', Ic2Items.advancedAlloy, 'D', "dustDiamond");
        if (GTConfig.harderIC2Macerator) {
            recipes.overrideRecipe("shaped_tile.blockMacerator_127744036", Ic2Items.macerator.copy(), "III", "IMI", "ICI", 'I', ingotRefinedIron, 'M', Ic2Items.stoneMacerator.copy(), 'C',
                    "circuitAdvanced");
            recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), "FDF", "DMD", "FCF", 'D', "gemDiamond", 'F', Items.FLINT, 'M', Ic2Items.machine, 'C',
                    "circuitAdvanced");
            recipes.addRecipe(Ic2Items.macerator.copy(), "FGF", "CMC", "FCF", 'G', grinder, 'F', Items.FLINT, 'M', Ic2Items.machine, 'C',
                    "circuitBasic");
        }
    }

    public static void initOverrideGTClassic(){
        //instance.overrideGTRecipe("shaped_tile.gtclassic.computercube_404275118", GTMaterialGen.get(GTBlocks.tileComputer), "CMO", "MAM", "OMC", 'C', "circuitMaster", 'M', GTItems2.computerMonitor, 'O', "circuitUltimate", 'A', Ic2Items.advMachine);
    }

//    public void overrideGTRecipe(String recipeId, ItemStack output, Object... input) {
//        Loader loader = Loader.instance();
//        ModContainer old = loader.activeModContainer();
//        loader.setActiveModContainer(loader.getIndexedModList().get("gtclassic"));
//        recipes.getRecipes().add(overrideRecipe(new AdvRecipe(recipeId, true, output, input));
//        loader.setActiveModContainer(old);
//    }
//
//    public void overrideShapelessGTRecipe(String recipeId, ItemStack output, Object... input) {
//        Loader loader = Loader.instance();
//        ModContainer old = loader.activeModContainer();
//        loader.setActiveModContainer(loader.getIndexedModList().get("gtclassic"));
//        recipes.getRecipes().add(overrideRecipe(new AdvShapelessRecipe(recipeId, true, output, input)));
//        loader.setActiveModContainer(old);
//    }
//
//    public static <T extends AdvRecipeBase> T overrideRecipe(T recipe) {
//        ((ForgeRegistry) ForgeRegistries.RECIPES).remove(new ResourceLocation("gtclassic", recipe.getRecipeID()));
//        AdvRecipeBase.duplicates.remove(new ResourceLocation("gtclassic", recipe.getRecipeID()));
//        return AdvRecipeBase.registerRecipe(recipe);
//    }
}
