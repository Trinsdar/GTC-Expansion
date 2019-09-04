package gtc_expansion.recipes;

import gtc_expansion.GTBlocks2;
import gtc_expansion.GTItems2;
import gtc_expansion.tile.GTTileAlloySmelter;
import gtc_expansion.tile.GTTileElectrolyzer;
import gtc_expansion.tile.multi.GTTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTTileMultiVacuumFreezer;
import gtclassic.GTBlocks;
import gtclassic.GTConfig;
import gtclassic.GTItems;
import gtclassic.material.GTMaterialGen;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.item.recipe.AdvRecipe;
import ic2.core.item.recipe.AdvRecipeBase;
import ic2.core.item.recipe.AdvShapelessRecipe;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class GTRecipe2 {
    static GTRecipe2 instance = new GTRecipe2();
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    static String ingotRefinedIron = IC2.getRefinedIron();
    static IRecipeInput ingotMachine = new RecipeInputCombined(1, new IRecipeInput[] {
            new RecipeInputOreDict(ingotRefinedIron), new RecipeInputOreDict("ingotAluminium") });
    static IRecipeInput plateMachine = new RecipeInputCombined(1, new IRecipeInput[] {
            new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateAluminium") });
    static IRecipeInput reinforcedGlass = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.reinforcedGlass), new RecipeInputItemStack(Ic2Items.reinforcedGlassClear));
    static IRecipeInput grinder = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(GTItems2.diamondGrinder)), new RecipeInputItemStack(GTMaterialGen.get(GTItems2.wolframiumGrinder)));

    public static String getRefinedIronPlate() {
        return IC2.config.getFlag("SteelRecipes") ? "plateSteel" : "plateRefinedIron";
    }

    public static void init(){
        GTRecipeRemove.init();
        GTRecipeProcessing.init();
        GTTileElectrolyzer.init();
        GTTileMultiVacuumFreezer.init();
        GTTileMultiImplosionCompressor.init();
        GTTileAlloySmelter.init();
        initIc2();
        initOverrideGTClassic();
        initShapedItemRecipes();
        initShapedBlockRecipes();
        GTRecipeIterators2.init();
    }

    public static void postInit(){
        GTRecipeProcessing.removals();

    }

    public static void initShapedItemRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GTItems2.computerMonitor), "IGI", "RPB", "IgI", 'I', "ingotAluminium", 'G', "dyeGreen", 'R', "dyeRed", 'P', "paneGlass", 'B', "dyeBlue", 'g', "dustGlowstone");
        recipes.addRecipe(GTMaterialGen.get(GTItems2.conveyorModule), "GGG", "AAA", "CBC", 'G', "blockGlass", 'A', "ingotAluminium", 'C', "circuitBasic", 'B', Ic2Items.battery.copy());
        recipes.addRecipe(GTMaterialGen.get(GTItems.chipData, 4), "GGG", "GCG", "GGG", 'G', "gemOlivine", 'C', "circuitAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GTItems2.diamondGrinder, 2), "DSD", "SdS", "DSD", 'D', "dustDiamond", 'S', "ingotSteel", 'd', "gemDiamond");
        recipes.addRecipe(GTMaterialGen.get(GTItems2.wolframiumGrinder, 2), "TST", "SBS", "TST", 'T', "ingotTungsten", 'S', "ingotSteel", 'B', "blockSteel");
    }

    public static void initShapedBlockRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.implosionCompressor), "AMA", "cCc", "AMA", 'A', Ic2Items.advancedAlloy.copy(), 'M', Ic2Items.advMachine.copy(), 'c', "circuitBasic", 'C', Ic2Items.compressor.copy());
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.electrolyzer), "IEI", "AeA", "IEI", 'I', ingotMachine, 'E', Ic2Items.extractor.copy(), 'A', "circuitAdvanced", 'e', Ic2Items.electrolyzer.copy());
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.vacuumFreezer), "IPI", "AGA", "IPI", 'I', ingotMachine, 'P', Ic2Items.pump.copy(), 'A', "circuitAdvanced", 'G', reinforcedGlass);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.alloySmelter), "IcI", "CFC", "IMI", 'I', "ingotInvar", 'c', "ingotConstantan", 'C', "circuitBasic", 'F', Ic2Items.electroFurnace.copy(), 'M', GTItems2.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.industrialGrinder), "ECP", "GGG", "CMC", 'E', GTBlocks2.electrolyzer, 'C', "circuitAdvanced", 'P', Ic2Items.pump.copy(), 'G', grinder, 'M', Ic2Items.advMachine);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.casingStandard, 4), "III", "CBC", "III", 'I', ingotMachine, 'C', "circuitBasic", 'B', Ic2Items.machine.copy());
        recipes.addRecipe(GTMaterialGen.get(GTBlocks2.casingAdvanced, 4), "III", "CBC", "III", 'I', "ingotChrome", 'C', "circuitElite", 'B', GTBlocks.casingHighlyAdvanced);
    }

    public static void initIc2(){
        recipes.overrideRecipe("shaped_item.itempartiridium_1100834802", GTMaterialGen.get(GTItems2.iridiumAlloyIngot), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'A', Ic2Items.advancedAlloy, 'D', "dustDiamond");
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
