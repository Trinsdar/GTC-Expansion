package gtc_expansion.recipes;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEConfiguration;
import gtc_expansion.GEItems;
import gtc_expansion.item.tools.GEToolGen;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.tile.GETileAlloySmelter;
import gtc_expansion.tile.GETileElectrolyzer;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GETileMultiPrimitiveBlastFurnace;
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
import ic2.core.item.recipe.AdvRecipe;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.item.recipe.upgrades.EnchantmentModifier;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class GERecipe {
    static GERecipe instance = new GERecipe();
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    static String ingotRefinedIron = IC2.getRefinedIron();
    static IRecipeInput ingotMachine = new RecipeInputCombined(1, new RecipeInputOreDict(ingotRefinedIron), new RecipeInputOreDict("ingotAluminium"));
    static IRecipeInput plateMachine = new RecipeInputCombined(1, new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateAluminium"));
    static IRecipeInput ingotBrassBronze = new RecipeInputCombined(1, new RecipeInputOreDict("ingotBronze"), new RecipeInputOreDict("ingotBrass"));
    static IRecipeInput plateBrassBronze = new RecipeInputCombined(1, new RecipeInputOreDict("plateBronze"), new RecipeInputOreDict("plateBrass"));
    static IRecipeInput materialBrassBronze = GEConfiguration.usePlates ? plateBrassBronze : ingotBrassBronze;
    static IRecipeInput ingotTinZinc = new RecipeInputCombined(1, new RecipeInputOreDict("ingotZinc"), new RecipeInputOreDict("ingotTin"));
    static IRecipeInput plateTinZinc = new RecipeInputCombined(1, new RecipeInputOreDict("plateZinc"), new RecipeInputOreDict("plateTin"));
    static IRecipeInput materialTinZinc = GEConfiguration.usePlates ? plateTinZinc : ingotTinZinc;
    static IRecipeInput ingotMixedMetal1 = new RecipeInputCombined(1, new RecipeInputOreDict("ingotAluminium"), new RecipeInputOreDict("ingotSilver"), new RecipeInputOreDict("ingotElectrum"));
    static IRecipeInput plateMixedMetal1 = new RecipeInputCombined(1, new RecipeInputOreDict("plateAluminium"), new RecipeInputOreDict("plateSilver"), new RecipeInputOreDict("plateElectrum"));
    static IRecipeInput materialMixedMetal1 = GEConfiguration.usePlates ? plateMixedMetal1 : ingotMixedMetal1;
    static IRecipeInput ingotMixedMetal2 = new RecipeInputCombined(1, new RecipeInputOreDict("ingotTungsten"), new RecipeInputOreDict("ingotTitanium"));
    static IRecipeInput plateMixedMetal2 = new RecipeInputCombined(1, new RecipeInputOreDict("plateTungsten"), new RecipeInputOreDict("plateTitanium"));
    static IRecipeInput materialMixedMetal2 = GEConfiguration.usePlates ? plateMixedMetal2 : ingotMixedMetal2;
    static String materialRefinedIron = GEConfiguration.usePlates ? getRefinedIronPlate() : ingotRefinedIron;
    static IRecipeInput materialMachine = GEConfiguration.usePlates ? plateMachine : ingotMachine;

    static IRecipeInput reinforcedGlass = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.reinforcedGlass), new RecipeInputItemStack(Ic2Items.reinforcedGlassClear));
    static IRecipeInput grinder = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(GEItems.diamondGrinder)), new RecipeInputItemStack(GTMaterialGen.get(GEItems.wolframiumGrinder)));
    static IRecipeInput tier2Energy = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.energyCrystal), new RecipeInputItemStack(GTMaterialGen.get(GTItems.lithiumBattery)));

    static String steel = GEConfiguration.usePlates ? "plateSteel" : "ingotSteel";
    static String tungsten = GEConfiguration.usePlates ? "plateTungsten" : "ingotTungsten";
    static String tungstenSteel = GEConfiguration.usePlates ? "plateTungstensteel" : "ingotTungstensteel";
    static String aluminium = GEConfiguration.usePlates ? "plateAluminium" : "ingotAluminium";
    static String titanium = GEConfiguration.usePlates ? "plateTitanium" : "ingotTitanium";
    static String platinum = GEConfiguration.usePlates ? "platePlatinum" : "ingotPlatinum";
    static String electrum = GEConfiguration.usePlates ? "plateElectrum" : "ingotElectrum";

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
        GETileMultiPrimitiveBlastFurnace.init();
        GERecipeMods.init();
        initIc2();
        initOverrideGTClassic();
        initShapedItemRecipes();
        initShapedBlockRecipes();
        initRemainingToolRecipes();
        initShapelessRecipes();
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
        recipes.addRecipe(GTMaterialGen.get(GEItems.constantanHeatingCoil), " I ", "I I", " I ", 'I', "ingotConstantan");
        recipes.addRecipe(GTMaterialGen.get(GEItems.brickPress), "H", "S", "F", 'F', "craftingToolFile", 'S', "stone", 'H', "craftingToolForgeHammer");
        recipes.addRecipe(GTMaterialGen.get(GEItems.unfiredBrick), "CP", 'C', Items.CLAY_BALL, 'P', GEItems.brickPress);
        recipes.addRecipe(GTMaterialGen.get(GEItems.unfiredFireBrick), "CP", 'C', GEItems.fireClayBall, 'P', GEItems.brickPress);
    }

    public static void initRemainingToolRecipes(){
        String stick = "stickWood";
        recipes.addRecipe(GEToolGen.getPickaxe(GTMaterial.Flint), "FFF", " S ", " S ", new EnchantmentModifier(GEToolGen.getPickaxe(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GEToolGen.getAxe(GTMaterial.Flint), "FF ", "FS ", " S ", new EnchantmentModifier(GEToolGen.getAxe(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GEToolGen.getShovel(GTMaterial.Flint), "F", "S", "S", new EnchantmentModifier(GEToolGen.getShovel(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GEToolGen.getSword(GTMaterial.Flint), "F", "F", "S", new EnchantmentModifier(GEToolGen.getSword(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GTMaterialGen.get(GEItems.bronzeFile), "P", "P", "S", 'P', "plateBronze", 'S', stick);
        recipes.addRecipe(GTMaterialGen.get(GEItems.bronzeHammer), "PPP", "PPP", " S ", 'P', "ingotBronze", 'S', stick);
        recipes.addRecipe(GEToolGen.getFile(GEMaterial.Iron), "P", "P", "S", 'P', "plateIron", 'S', stick);
        recipes.addRecipe(GEToolGen.getHammer(GEMaterial.Iron), "PPP", "PPP", " S ", 'P', "ingotIron", 'S', stick);
        recipes.addRecipe(GEToolGen.getFile(GEMaterial.Diamond), "G", "G", "S", 'G', "gemDiamond", 'S', stick);
        recipes.addRecipe(GEToolGen.getHammer(GEMaterial.Diamond), "GGG", "GGG", " S ", 'G', "gemDiamond", 'S', stick);

    }

    public static void initShapedBlockRecipes(){
        String invar = GEConfiguration.usePlates ? "plateInvar" : "ingotInvar";
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.implosionCompressor), "AMA", "cCc", "AMA", 'A', Ic2Items.advancedAlloy.copy(), 'M', "machineBlockAdvanced", 'c', "circuitBasic", 'C', Ic2Items.compressor.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.electrolyzer), "IEI", "AeA", "IEI", 'I', materialMachine, 'E', Ic2Items.extractor.copy(), 'A', "circuitAdvanced", 'e', Ic2Items.electrolyzer.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.vacuumFreezer), "IPI", "AGA", "IPI", 'I', materialMachine, 'P', Ic2Items.pump.copy(), 'A', "circuitAdvanced", 'G', reinforcedGlass);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.alloySmelter), "IHI", "CFC", "IMI", 'I', invar, 'H', GEItems.constantanHeatingCoil, 'C', "circuitBasic", 'F', Ic2Items.electroFurnace.copy(), 'M', GEItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.industrialGrinder), "ECP", "GGG", "CMC", 'E', GEBlocks.electrolyzer, 'C', "circuitAdvanced", 'P', Ic2Items.pump.copy(), 'G', grinder, 'M', "machineBlockAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.primitiveBlastFurnace), "BBB", "BPB", "BBB", 'B', GEBlocks.fireBrickBlock, 'P', "plateIron");
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.alloyFurnace), "CCC", "FHF", "CCC", 'C', "cobblestone", 'F', Blocks.FURNACE, 'H', Blocks.HOPPER);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.casingStandard, 4), "III", "CBC", "III", 'I', materialMachine, 'C', "circuitBasic", 'B', "machineBlockBasic");
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.casingAdvanced, 4), "III", "CBC", "III", 'I', "ingotChrome", 'C', "circuitElite", 'B', "machineBlockElite");
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.fireBrickBlock), "BB", "BB", 'B', GEItems.fireBrick);
    }

    public static void initShapelessRecipes(){
        FluidStack water = new FluidStack(FluidRegistry.WATER, 1000);
        recipes.addShapelessRecipe(GTMaterialGen.get(Items.GUNPOWDER, 3), "dustCoal", "dustSulfur", "dustSaltpeter", "dustSaltpeter");
        recipes.addShapelessRecipe(GTMaterialGen.get(Items.GUNPOWDER, 2), "dustCharcoal", "dustSulfur", "dustSaltpeter", "dustSaltpeter");
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 3), Ic2Items.fertilizer, "dustSulfur", GTMaterialGen.getTube(GTMaterial.Calcium, 1));
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 2), Ic2Items.fertilizer, "dustAshes", "dustAshes", "dustAshes");
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 2), Ic2Items.fertilizer, "dustDarkAshes");
        recipes.addShapelessRecipe(GTMaterialGen.getDust(GEMaterial.Brass, 4), "dustCopper", "dustCopper", "dustCopper", "dustZinc");
        recipes.addShapelessRecipe(GTMaterialGen.getDust(GEMaterial.Constantan, 3), "dustCopper", "dustCopper", "dustNickel");
        recipes.addShapelessRecipe(GTMaterialGen.getDust(GEMaterial.Invar, 3), "dustIron", "dustIron", "dustNickel");
        recipes.addShapelessRecipe(GTMaterialGen.getDust(GEMaterial.Magnalium, 1), "dustMagnesium", "dustAluminum", "dustAluminum");
        String lead = GEConfiguration.usePlates ? "plateLead" : "ingotLead";
        recipes.addShapelessRecipe(Ic2Items.reactorPlatingExplosive, Ic2Items.reactorPlating, lead);
        recipes.addShapelessRecipe(GTMaterialGen.get(GEItems.fireClayBall, 2), Items.CLAY_BALL, "sand", "dustFlint", water);
    }

    public static void initIc2(){
        recipes.overrideRecipe("shaped_item.itempartiridium_1100834802", GTMaterialGen.get(GEItems.iridiumAlloyIngot), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'A', Ic2Items.advancedAlloy, 'D', "dustDiamond");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FFF", "TTT", "FFF", 'F', "dustFlint", 'T', Blocks.TNT);
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FTF", "FTF", "FTF", 'F', "dustFlint", 'T', Blocks.TNT);
        recipes.overrideRecipe("shaped_tile.blockmachine_527557260", Ic2Items.machine, "PPP", "P P", "PPP", 'P', getRefinedIronPlate());
        recipes.overrideRecipe("shaped_tile.blocknuke_-814805840", Ic2Items.nuke, "UCU", "BAB", "UCU", 'U', Ic2Items.reactorReEnrichedUraniumRod, 'C', "circuitAdvanced", 'B', "blockUranium", 'A', "machineBlockAdvanced");
        if (GEConfiguration.harderTools){
            ItemStack battery = Ic2Items.battery;
            String circuit = "circuitBasic";
            recipes.overrideRecipe("shaped_item.itemtooldrill_1955483893", Ic2Items.diamondDrill, " D ", "DdD", "TCT", 'D', "dustDiamond", 'd', Ic2Items.electricDrill, 'T', titanium, 'C', "circuitAdvanced");
            recipes.overrideRecipe("shaped_item.itemtooldrill_-1588477206", Ic2Items.electricDrill, " S ", "SCS", "SBS", 'S', steel, 'C', circuit, 'B', battery);
            recipes.overrideRecipe("shaped_item.itemtoolchainsaw_-824616294", Ic2Items.chainSaw, " SS", "SCS", "BS ", 'S', steel, 'C', circuit, 'B', battery);
            recipes.overrideRecipe("shaped_item.itemtoolhoe_-137638623", Ic2Items.electricHoe, "SS ", " C ", " B ", 'S', steel, 'C', circuit, 'B', battery);
            recipes.overrideRecipe("shaped_item.itemtreetapelectric_-1455688385", Ic2Items.electricTreeTap, " B ", "SCS", "T  ", 'T', Ic2Items.treeTap, 'S', steel, 'C', circuit, 'B', battery);
            recipes.overrideRecipe("shaped_item.electricsprayer_-335930196", Ic2Items.electricCfSprayer, "sS ", "SC ", "  B", 's', Ic2Items.cfSprayer, 'S', steel, 'C', circuit, 'B', battery);
            recipes.overrideRecipe("shaped_item.itemtoolwrenchelectric_883008511", Ic2Items.electricWrench, "SWS", "SCS", " B ", 'W', Ic2Items.wrench, 'S', steel, 'C', circuit, 'B', battery);
            recipes.overrideRecipe("shaped_item.itemnanosaber_644260803", Ic2Items.nanoSaber, "PI ", "PI ", "CEC", 'P', platinum, 'I', Ic2Items.iridiumPlate, 'C', Ic2Items.carbonPlate, 'E', Ic2Items.energyCrystal);
            recipes.overrideRecipe("shaped_item.itemtoolmininglaser_1732214669", Ic2Items.miningLaser,"RHE", "TTC", " AA", 'R', "gemRuby", 'H', GTItems.heatStorageHelium6, 'E', tier2Energy, 'T', titanium, 'C', "circuitAdvanced", 'A', Ic2Items.advancedAlloy);
        }
        if (GTConfig.harderIC2Macerator) {
            recipes.overrideRecipe("shaped_tile.blockMacerator_127744036", Ic2Items.macerator.copy(), "III", "IMI", "ICI", 'I', ingotRefinedIron, 'M', Ic2Items.stoneMacerator.copy(), 'C',
                    "circuitAdvanced");
            recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), "FDF", "DMD", "FCF", 'D', "gemDiamond", 'F', Items.FLINT, 'M', "machineBlockBasic", 'C',
                    "circuitAdvanced");
            recipes.addRecipe(Ic2Items.macerator.copy(), "FGF", "CMC", "FCF", 'G', grinder, 'F', Items.FLINT, 'M', "machineBlockBasic", 'C',
                    "circuitBasic");
        }
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.suBattery, 32), "C", "S", "L", 'C', Ic2Items.insulatedCopperCable, 'S', GTMaterialGen.getTube(GEMaterial.SulfuricAcid, 1), 'L', "dustLead");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.suBattery, 32), "C", "L", "S", 'C', Ic2Items.insulatedCopperCable, 'S', GTMaterialGen.getTube(GEMaterial.SulfuricAcid, 1), 'L', "dustLead");
        String tin = GEConfiguration.usePlates ? "plateTin" : "ingotTin";
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.battery, 2), " C ", "TST", "TLT", 'C', Ic2Items.insulatedCopperCable, 'T', tin, 'S', GTMaterialGen.getTube(GEMaterial.SulfuricAcid, 1), 'L', "dustLead");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.glassFiberCable, 8), "GGG", "EDE", "GGG", 'G', "blockGlass", 'E', "ingotElectrum", 'D', "gemDiamond");
        recipes.overrideRecipe("shaped_item.itemingotalloy_-650149377", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 2), "TTT", "MMM", "BBB", 'T', materialRefinedIron, 'M', materialBrassBronze, 'B', materialTinZinc);
        if (GEConfiguration.usePlates){
            recipes.overrideRecipe("shaped_item.reactorvent_-795420664", Ic2Items.reactorVent, "PBP", "B B", "PBP", 'P', getRefinedIronPlate(), 'B', Blocks.IRON_BARS);
        }
    }

    public static void initOverrideGTClassic(){
        //instance.overrideGTRecipe("shaped_tile.gtclassic.computercube_404275118", GTMaterialGen.get(GTBlocks.tileComputer), "CMO", "MAM", "OMC", 'C', "circuitMaster", 'M', GEItems.computerMonitor, 'O', "circuitUltimate", 'A', "machineBlockAdvanced");
        instance.overrideGTRecipe("shaped_item.itemingotalloy_1703663469", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 3), "TTT", "MMM", "BBB", 'T', materialRefinedIron, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        instance.overrideGTRecipe("shaped_item.itemingotalloy_1844373769", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 6), "TTT", "MMM", "BBB", 'T', materialMixedMetal2, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        instance.overrideGTRecipe("shaped_item.itemingotalloy_-470293062", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 8), "TTT", "MMM", "BBB", 'T', tungstenSteel, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        if (GEConfiguration.harderTools){
            instance.overrideGTRecipe("shaped_item.gtclassic.rockcutter_1664690250", GTMaterialGen.get(GTItems.rockCutter), "DT ", "DT ", "DCB", new EnchantmentModifier(GTMaterialGen.get(GTItems.rockCutter), Enchantments.SILK_TOUCH).setUsesInput(), 'T', titanium, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
            instance.overrideGTRecipe("shaped_item.gtclassic.jackhammer_2107301811", GTMaterialGen.get(GTItems.jackHammer), "TBT", " C ", " D ", 'T', titanium, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
            instance.overrideGTRecipe("shaped_item.itemtoolmininglaser_1482495812", Ic2Items.miningLaser,"REH", "TTC", " AA", 'R', "gemRuby", 'H', GTItems.heatStorageHelium6, 'E', tier2Energy, 'T', titanium, 'C', "circuitAdvanced", 'A', Ic2Items.advancedAlloy);
        }
        if (GEConfiguration.usePlates){
            instance.overrideGTRecipe("shaped_item.reactorvent_-735496828", Ic2Items.reactorVent, "PBP", "B B", "PBP", 'P', "plateAluminium", 'B', Blocks.IRON_BARS);
            instance.overrideGTRecipe("shaped_tile.gtclassic.industrialcentrifuge_-1533289434", GTMaterialGen.get(GTBlocks.tileCentrifuge), "RCR", "MEM", "RCR", 'R', getRefinedIronPlate(), 'C', "circuitAdvanced", 'M', "machineBlockAdvanced", 'E', Ic2Items.extractor);
        }
        instance.overrideGTRecipe("shaped_tile.gtclassic.translocator_-145313936", GTMaterialGen.get(GTBlocks.tileTranslocator), "EWE", "CBC", "EME", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitBasic", 'B', GEMaterialGen.getHull(GEMaterial.Brass, 1), 'M', GEItems.conveyorModule);
        instance.overrideGTRecipe("shaped_tile.gtclassic.bufferlarge_1098362261", GTMaterialGen.get(GTBlocks.tileBufferLarge), "EWE", "CBC", "EcE", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitAdvanced", 'B', GEMaterialGen.getHull(GEMaterial.Brass, 1), 'c', "chestWood");
    }

    public void overrideGTRecipe(String recipeId, ItemStack output, Object... input) {
        Loader loader = Loader.instance();
        ModContainer old = loader.activeModContainer();
        loader.setActiveModContainer(loader.getIndexedModList().get("gtclassic"));
        //temporarily doing it this way till speiger makes a variable and 2 contructors public.
        recipes.getRecipes().add(AdvRecipe.overrideAndGet(recipeId, output, input));
        loader.setActiveModContainer(old);
    }
      //commented out this code till speiger makes a variable and 2 contructors public.
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
