package gtc_expansion.recipes;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEConfiguration;
import gtc_expansion.GEItems;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.tile.GETileAlloySmelter;
import gtc_expansion.tile.GETileElectrolyzer;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GETileMultiVacuumFreezer;
import gtclassic.GTBlocks;
import gtclassic.GTConfig;
import gtclassic.GTItems;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class GERecipe {
    static GERecipe instance = new GERecipe();
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    static String ingotRefinedIron = IC2.getRefinedIron();
    static IRecipeInput ingotMachine = new RecipeInputCombined(1, new IRecipeInput[] {
            new RecipeInputOreDict(ingotRefinedIron), new RecipeInputOreDict("ingotAluminium") });
    static IRecipeInput plateMachine = new RecipeInputCombined(1, new IRecipeInput[] {
            new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateAluminium") });
    static IRecipeInput materialMachine = GEConfiguration.usePlates ? plateMachine : ingotMachine;
    static IRecipeInput reinforcedGlass = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.reinforcedGlass), new RecipeInputItemStack(Ic2Items.reinforcedGlassClear));
    static IRecipeInput grinder = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(GEItems.diamondGrinder)), new RecipeInputItemStack(GTMaterialGen.get(GEItems.wolframiumGrinder)));

    static String steel = GEConfiguration.usePlates ? "plateSteel" : "ingotSteel";
    static String tungsten = GEConfiguration.usePlates ? "plateTungsten" : "ingotTungsten";
    static String aluminium = GEConfiguration.usePlates ? "plateAluminium" : "ingotAluminium";
    static String titanium = GEConfiguration.usePlates ? "plateTitanium" : "ingotTitanium";

    public static String getRefinedIronPlate() {
        return IC2.config.getFlag("SteelRecipes") ? "plateSteel" : "plateRefinedIron";
    }

    public static void init(){
        GERecipeRemove.init();
        GERecipeProcessing.init();
        GETileElectrolyzer.init();
        GETileMultiVacuumFreezer.init();
        GETileMultiIndustrialGrinder.init();
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
        recipes.addRecipe(GTMaterialGen.get(GEItems.computerMonitor), "IGI", "RPB", "IgI", 'I', aluminium, 'G', "dyeGreen", 'R', "dyeRed", 'P', "paneGlass", 'B', "dyeBlue", 'g', "dustGlowstone");
        recipes.addRecipe(GTMaterialGen.get(GEItems.conveyorModule), "GGG", "AAA", "CBC", 'G', "blockGlass", 'A', materialMachine, 'C', "circuitBasic", 'B', Ic2Items.battery.copy());
        recipes.addRecipe(GTMaterialGen.get(GTItems.chipData, 4), "GGG", "GCG", "GGG", 'G', "gemOlivine", 'C', "circuitAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GEItems.diamondGrinder, 2), "DSD", "SdS", "DSD", 'D', "dustDiamond", 'S', steel, 'd', "gemDiamond");
        recipes.addRecipe(GTMaterialGen.get(GEItems.wolframiumGrinder, 2), "TST", "SBS", "TST", 'T', tungsten, 'S', steel, 'B', "blockSteel");
    }

    public static void initShapedBlockRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.implosionCompressor), "AMA", "cCc", "AMA", 'A', Ic2Items.advancedAlloy.copy(), 'M', Ic2Items.advMachine.copy(), 'c', "circuitBasic", 'C', Ic2Items.compressor.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.electrolyzer), "IEI", "AeA", "IEI", 'I', materialMachine, 'E', Ic2Items.extractor.copy(), 'A', "circuitAdvanced", 'e', Ic2Items.electrolyzer.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.vacuumFreezer), "IPI", "AGA", "IPI", 'I', materialMachine, 'P', Ic2Items.pump.copy(), 'A', "circuitAdvanced", 'G', reinforcedGlass);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.alloySmelter), "IcI", "CFC", "IMI", 'I', "ingotInvar", 'c', "ingotConstantan", 'C', "circuitBasic", 'F', Ic2Items.electroFurnace.copy(), 'M', GEItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.industrialGrinder), "ECP", "GGG", "CMC", 'E', GEBlocks.electrolyzer, 'C', "circuitAdvanced", 'P', Ic2Items.pump.copy(), 'G', grinder, 'M', Ic2Items.advMachine);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.casingStandard, 4), "III", "CBC", "III", 'I', materialMachine, 'C', "circuitBasic", 'B', Ic2Items.machine.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.casingAdvanced, 4), "III", "CBC", "III", 'I', "ingotChrome", 'C', "circuitElite", 'B', GTBlocks.casingHighlyAdvanced);
    }

    public static void initShapelessRecipes(){
        recipes.addShapelessRecipe(GTMaterialGen.get(Items.GUNPOWDER, 3), "dustCoal", "dustSulfur", "dustSaltpeter", "dustSaltpeter");
        recipes.addShapelessRecipe(GTMaterialGen.get(Items.GUNPOWDER, 2), "dustCharcoal", "dustSulfur", "dustSaltpeter", "dustSaltpeter");
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 3), Ic2Items.fertilizer, "dustSulfur", GTMaterialGen.getTube(GTMaterial.Calcium, 1));
        recipes.addShapelessRecipe(GTMaterialGen.getDust(GEMaterial.Brass, 4), "dustCopper", "dustCopper", "dustCopper", "dustZinc");
        recipes.addShapelessRecipe(GTMaterialGen.getDust(GEMaterial.Constantan, 3), "dustCopper", "dustCopper", "dustNickel");
        recipes.addShapelessRecipe(GTMaterialGen.getDust(GEMaterial.Invar, 3), "dustIron", "dustIron", "dustNickel");
    }

    public static void initIc2(){
        recipes.overrideRecipe("shaped_item.itempartiridium_1100834802", GTMaterialGen.get(GEItems.iridiumAlloyIngot), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'A', Ic2Items.advancedAlloy, 'D', "dustDiamond");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FFF", "TTT", "FFF", 'F', "dustFlint", 'T', Blocks.TNT);
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FTF", "FTF", "FTF", 'F', "dustFlint", 'T', Blocks.TNT);
        if (GEConfiguration.harderTools){
            recipes.overrideRecipe("shaped_item.itemtooldrill_1955483893", Ic2Items.diamondDrill, " D ", "DdD", "TCT", 'D', "dustDiamond", 'd', Ic2Items.electricDrill, 'T', titanium, 'C', "circuitAdvanced");
            recipes.overrideRecipe("shaped_item.itemtooldrill_-1588477206", Ic2Items.electricDrill, " S ", "SCS", "SBS", 'S', steel, 'C', "circuitBasic", 'B', Ic2Items.battery);
        }
        if (GTConfig.harderIC2Macerator) {
            recipes.overrideRecipe("shaped_tile.blockMacerator_127744036", Ic2Items.macerator.copy(), "III", "IMI", "ICI", 'I', ingotRefinedIron, 'M', Ic2Items.stoneMacerator.copy(), 'C',
                    "circuitAdvanced");
            recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), "FDF", "DMD", "FCF", 'D', "gemDiamond", 'F', Items.FLINT, 'M', Ic2Items.machine, 'C',
                    "circuitAdvanced");
            recipes.addRecipe(Ic2Items.macerator.copy(), "FGF", "CMC", "FCF", 'G', grinder, 'F', Items.FLINT, 'M', Ic2Items.machine, 'C',
                    "circuitBasic");
        }
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.suBattery, 32), "C", "S", "L", 'C', Ic2Items.insulatedCopperCable, 'S', GTMaterialGen.getTube(GEMaterial.SulfuricAcid, 1), 'L', "dustLead");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.suBattery, 32), "C", "L", "S", 'C', Ic2Items.insulatedCopperCable, 'S', GTMaterialGen.getTube(GEMaterial.SulfuricAcid, 1), 'L', "dustLead");
        String tin = GEConfiguration.usePlates ? "plateTin" : "ingotTin";
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.battery, 2), " C ", "TST", "TLT", 'C', Ic2Items.insulatedCopperCable, 'T', tin, 'S', GTMaterialGen.getTube(GEMaterial.SulfuricAcid, 1), 'L', "dustLead");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.glassFiberCable, 8), "GGG", "EDE", "GGG", 'G', "blockGlass", 'E', "ingotElectrum", 'D', "gemDiamond");
    }

    public static void initOverrideGTClassic(){
        //commented out this code till speiger makes some methods and variables public.
        //instance.overrideGTRecipe("shaped_tile.gtclassic.computercube_404275118", GTMaterialGen.get(GTBlocks.tileComputer), "CMO", "MAM", "OMC", 'C', "circuitMaster", 'M', GEItems.computerMonitor, 'O', "circuitUltimate", 'A', Ic2Items.advMachine);
        if (GEConfiguration.harderTools){
            //instance.overrideGTRecipe("shaped_item.gtclassic.rockcutter_1664690250", GTMaterialGen.get(GTItems.rockCutter), "DT ", "DT ", "DCB", 'T', titanium, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
            //instance.overrideGTRecipe("shaped_item.gtclassic.jackhammer_2107301811", GTMaterialGen.get(GTItems.jackHammer), "TBT", " C ", " D ", 'T', titanium, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        }

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
