package gtc_expansion.recipes;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEConfiguration;
import gtc_expansion.GEItems;
import gtc_expansion.item.tools.GEToolGen;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.tile.GETileAlloySmelter;
import gtc_expansion.tile.GETileAssemblingMachine;
import gtc_expansion.tile.GETileChemicalReactor;
import gtc_expansion.tile.GETileElectrolyzer;
import gtc_expansion.tile.multi.GETileMultiDistillationTower;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GETileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GETileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GETileMultiVacuumFreezer;
import gtclassic.helpers.GTHelperAdvRecipe;
import gtclassic.GTBlocks;
import gtclassic.GTConfig;
import gtclassic.GTItems;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.GTTileBaseMachine;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
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

public class GERecipe {
    static GERecipe instance = new GERecipe();
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    static String ingotRefinedIron = IC2.getRefinedIron();
    static IRecipeInput ingotMachine = new RecipeInputCombined(1, new RecipeInputOreDict(ingotRefinedIron), new RecipeInputOreDict("ingotAluminium"));
    public static IRecipeInput plateMachine = new RecipeInputCombined(1, new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateAluminium"));
    static IRecipeInput ingotSteels = new RecipeInputCombined(1, new RecipeInputOreDict("ingotSteel"), new RecipeInputOreDict("ingotStainlessSteel"));
    public static IRecipeInput plateSteels = new RecipeInputCombined(1, new RecipeInputOreDict("plateSteel"), new RecipeInputOreDict("plateStainlessSteel"));
    static IRecipeInput ingotBrassBronze = new RecipeInputCombined(1, new RecipeInputOreDict("ingotBronze"), new RecipeInputOreDict("ingotBrass"));
    static IRecipeInput plateBrassBronze = new RecipeInputCombined(1, new RecipeInputOreDict("plateBronze"), new RecipeInputOreDict("plateBrass"));
    static IRecipeInput materialBrassBronze = GEConfiguration.usePlates ? plateBrassBronze : ingotBrassBronze;
    static IRecipeInput ingotTinZinc = new RecipeInputCombined(1, new RecipeInputOreDict("ingotZinc"), new RecipeInputOreDict("ingotTin"));
    static IRecipeInput plateTinZinc = new RecipeInputCombined(1, new RecipeInputOreDict("plateZinc"), new RecipeInputOreDict("plateTin"));
    static IRecipeInput materialTinZinc = GEConfiguration.usePlates ? plateTinZinc : ingotTinZinc;
    static IRecipeInput ingotInvarAluminium = new RecipeInputCombined(1, new RecipeInputOreDict("ingotInvar"), new RecipeInputOreDict("ingotAluminium"));
    static IRecipeInput plateInvarAluminium = new RecipeInputCombined(1, new RecipeInputOreDict("plateInvar"), new RecipeInputOreDict("plateAluminium"));
    static IRecipeInput materialInvarAluminium = GEConfiguration.usePlates ? plateInvarAluminium : ingotInvarAluminium;
    static IRecipeInput ingotMixedMetal1 = new RecipeInputCombined(1, new RecipeInputOreDict("ingotAluminium"), new RecipeInputOreDict("ingotSilver"), new RecipeInputOreDict("ingotElectrum"));
    static IRecipeInput plateMixedMetal1 = new RecipeInputCombined(1, new RecipeInputOreDict("plateAluminium"), new RecipeInputOreDict("plateSilver"), new RecipeInputOreDict("plateElectrum"));
    static IRecipeInput materialMixedMetal1 = GEConfiguration.usePlates ? plateMixedMetal1 : ingotMixedMetal1;
    static IRecipeInput ingotMixedMetal2 = new RecipeInputCombined(1, new RecipeInputOreDict("ingotTungsten"), new RecipeInputOreDict("ingotTitanium"));
    static IRecipeInput plateMixedMetal2 = new RecipeInputCombined(1, new RecipeInputOreDict("plateTungsten"), new RecipeInputOreDict("plateTitanium"));
    static IRecipeInput materialMixedMetal2 = GEConfiguration.usePlates ? plateMixedMetal2 : ingotMixedMetal2;
    static String materialRefinedIron = GEConfiguration.usePlates ? getRefinedIronPlate() : ingotRefinedIron;
    static IRecipeInput materialMachine = GEConfiguration.usePlates ? plateMachine : ingotMachine;
    static IRecipeInput materialSteels = GEConfiguration.usePlates ? plateSteels : ingotSteels;
    static IRecipeInput materialMachineGT = GEConfiguration.gt2Mode ? materialMachine : materialSteels;

    static IRecipeInput plateElectric = new RecipeInputCombined(1, new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateSilicon"),
            new RecipeInputOreDict("plateAluminium"), new RecipeInputOreDict("plateSilver"),
            new RecipeInputOreDict("plateElectrum"), new RecipeInputOreDict("platePlatinum"));
    public static IRecipeInput anyLapis = new RecipeInputCombined(1, new RecipeInputOreDict("gemLapis"),
            new RecipeInputOreDict("dustLazurite"), new RecipeInputOreDict("dustSodalite"));
    static IRecipeInput reinforcedGlass = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.reinforcedGlass), new RecipeInputItemStack(Ic2Items.reinforcedGlassClear));
    static IRecipeInput grinder = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(GEItems.diamondGrinder)), new RecipeInputItemStack(GTMaterialGen.get(GEItems.wolframiumGrinder)));
    static IRecipeInput tier2Energy = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.energyCrystal), new RecipeInputItemStack(GTMaterialGen.get(GTItems.lithiumBattery)));
    static IRecipeInput ingotDigital = new RecipeInputCombined(1, new RecipeInputOreDict("ingotChrome"), new RecipeInputOreDict("ingotTitanium"), new RecipeInputOreDict("ingotPlatinum"));
    static IRecipeInput plateDigital = new RecipeInputCombined(1, new RecipeInputOreDict("plateChrome"), new RecipeInputOreDict("plateTitanium"), new RecipeInputOreDict("platePlatinum"));
    static IRecipeInput materialDigital = GEConfiguration.usePlates ? plateDigital : ingotDigital;

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
        GETileMultiIndustrialBlastFurnace.init();
        GETileMultiDistillationTower.init();
        GETileAlloySmelter.init();
        GETileAssemblingMachine.init();
        GETileChemicalReactor.init();
        GETileMultiPrimitiveBlastFurnace.init();
        GERecipeMods.init();
        initIc2();
        initOverrideGTClassic();
        if (GEConfiguration.gt2Mode){
            GERecipeGT2.init();
        } else {
            GERecipeGT4.init();
        }
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
        recipes.addRecipe(GTMaterialGen.get(GEItems.diamondGrinder, 2), "DSD", "SdS", "DSD", 'D', "dustDiamond", 'S', steel, 'd', "gemDiamond");
        recipes.addRecipe(GTMaterialGen.get(GEItems.wolframiumGrinder, 2), "TST", "SBS", "TST", 'T', tungsten, 'S', steel, 'B', "blockSteel");
        recipes.addRecipe(GTMaterialGen.get(GEItems.constantanHeatingCoil), " I ", "I I", " I ", 'I', "ingotConstantan");
        if (GEConfiguration.unfiredBricks){
            recipes.addRecipe(GTMaterialGen.get(GEItems.unfiredBrick, 2), "C", "C", 'C', Items.CLAY_BALL);
            recipes.addRecipe(GTMaterialGen.get(GEItems.unfiredFireBrick, 2), "C", "C", 'C', GEItems.fireClayBall);
        }
    }

    public static void initRemainingToolRecipes(){
        String stick = "stickWood";
        recipes.addRecipe(GEToolGen.getPickaxe(GTMaterial.Flint), "FFF", " S ", " S ", new EnchantmentModifier(GEToolGen.getPickaxe(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GEToolGen.getAxe(GTMaterial.Flint), "FF", "FS", " S", new EnchantmentModifier(GEToolGen.getAxe(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GEToolGen.getShovel(GTMaterial.Flint), "F", "S", "S", new EnchantmentModifier(GEToolGen.getShovel(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GEToolGen.getSword(GTMaterial.Flint), "F", "F", "S", new EnchantmentModifier(GEToolGen.getSword(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
    }

    public static void initShapedBlockRecipes(){
        // TODO: whole system for using only steel and stainless steel machine hulls in place of most basic machine casing recipes. Needs to factor in steel replace refined iron config.
        String invar = GEConfiguration.usePlates ? "plateInvar" : "ingotInvar";
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.assemblingMachine), "CPC", "SMS", "CSC", 'C', "circuitBasic", 'P', Blocks.PISTON, 'S', materialMachineGT, 'M', GEItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.chemicalReactor), "PMP", "CcC", "PEP", 'P', materialInvarAluminium, 'M', Ic2Items.magnetizer, 'C', "circuitAdvanced", 'c', Ic2Items.compressor, 'E', Ic2Items.extractor);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.distillationTower), "CEC", "PAP", "eEe", 'C', GTBlocks.tileCentrifuge, 'E', "circuitMaster", 'P', Ic2Items.pump.copy(), 'A', "machineBlockHighlyAdvanced", 'e', GEBlocks.electrolyzer);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.implosionCompressor), "AMA", "cCc", "AMA", 'A', Ic2Items.advancedAlloy.copy(), 'M', "machineBlockAdvanced", 'c', "circuitBasic", 'C', Ic2Items.compressor.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.electrolyzer), "IEI", "AeA", "IEI", 'I', materialMachineGT, 'E', Ic2Items.extractor.copy(), 'A', "circuitAdvanced", 'e', Ic2Items.electrolyzer.copy());
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.vacuumFreezer), "IPI", "AGA", "IPI", 'I', materialMachineGT, 'P', Ic2Items.pump.copy(), 'A', "circuitAdvanced", 'G', reinforcedGlass);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.alloySmelter), "IHI", "CFC", "IMI", 'I', invar, 'H', GEItems.constantanHeatingCoil, 'C', "circuitBasic", 'F', Ic2Items.electroFurnace.copy(), 'M', GEItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.industrialGrinder), "ECP", "GGG", "CMC", 'E', GEBlocks.electrolyzer, 'C', "circuitAdvanced", 'P', Ic2Items.pump.copy(), 'G', grinder, 'M', "machineBlockAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.industrialBlastFurnace, 1), "CcC", "cMc", "FcF", 'C', "circuitBasic", 'c', GEItems.constantanHeatingCoil, 'M', "machineBlockAdvanced", 'F', Ic2Items.inductionFurnace);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.primitiveBlastFurnace), "BBB", "BPB", "BBB", 'B', GEBlocks.fireBrickBlock, 'P', "plateIron");
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.alloyFurnace), "CCC", "FHF", "CCC", 'C', "cobblestone", 'F', Blocks.FURNACE, 'H', Blocks.HOPPER);
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.casingStandard, 4), "III", "CBC", "III", 'I', materialMachine, 'C', "circuitBasic", 'B', "machineBlockBasic");
        recipes.addRecipe(GTMaterialGen.get(GEBlocks.casingReinforced, 5), "III", "CMC", "III", 'I', materialSteels, 'C', "circuitAdvanced", 'M', "machineBlockAdvanced");
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
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FFF", "TTT", "FFF", 'F', "dustFlint", 'T', Blocks.TNT);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FTF", "FTF", "FTF", 'F', "dustFlint", 'T', Blocks.TNT);
        recipes.overrideRecipe("shaped_tile.blocknuke_-814805840", Ic2Items.nuke, "UCU", "BAB", "UCU", 'U', Ic2Items.reactorReEnrichedUraniumRod, 'C', "circuitAdvanced", 'B', "blockUranium", 'A', "machineBlockAdvanced");
        GTHelperAdvRecipe.removeRecipe("ic2", "shaped_tile.blockiridiumstone_-48520064");
        GTHelperAdvRecipe.removeRecipe("ic2", "shaped_tile.blockwatergenerator_-2059790844");
        GTHelperAdvRecipe.removeRecipe("ic2", "shaped_tile.blockwindgenerator_1669945012");
        ItemStack battery = Ic2Items.battery;
        String circuit = "circuitBasic";
        recipes.overrideRecipe("shaped_item.itemtooldrill_1955483893", Ic2Items.diamondDrill, " D ", "DdD", "TCT", 'D', "dustDiamond", 'd', Ic2Items.electricDrill, 'T', titanium, 'C', "circuitAdvanced");
        recipes.overrideRecipe("shaped_item.itemtooldrill_-1588477206", Ic2Items.electricDrill, " S ", "SCS", "SBS", 'S', materialSteels, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemtoolchainsaw_-824616294", Ic2Items.chainSaw, " SS", "SCS", "BS ", 'S', materialSteels, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemtoolhoe_-137638623", Ic2Items.electricHoe, "SS ", " C ", " B ", 'S', materialSteels, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemtreetapelectric_-1455688385", Ic2Items.electricTreeTap, " B ", "SCS", "T  ", 'T', Ic2Items.treeTap, 'S', materialSteels, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.electricsprayer_-335930196", Ic2Items.electricCfSprayer, "sS ", "SC ", "  B", 's', Ic2Items.cfSprayer, 'S', materialSteels, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemnanosaber_644260803", Ic2Items.nanoSaber, "PI ", "PI ", "CEC", 'P', platinum, 'I', Ic2Items.iridiumPlate, 'C', Ic2Items.carbonPlate, 'E', Ic2Items.energyCrystal);
        recipes.overrideRecipe("shaped_item.itemtoolmininglaser_1732214669", Ic2Items.miningLaser,"RHE", "TTC", " AA", 'R', "gemRuby", 'H', GTItems.heatStorageHelium6, 'E', "energyCrystal", 'T', titanium, 'C', "circuitAdvanced", 'A', Ic2Items.advancedAlloy);
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
        instance.removeGTRecipe("shaped_item.gtclassic.data_chip_-270689326");
        instance.removeGTRecipe("shaped_item.gtclassic.data_orb_1768015763");
        instance.removeGTRecipe("shaped_tile.blockwatergenerator_-495936330");
        instance.removeGTRecipe("shaped_tile.blockwindgenerator_-1843388748");
        instance.removeGTRecipe("shapeless_item.gtclassic.test_tube_-1487325092");
        instance.removeGTRecipe("shapeless_item.sulphur_1317221415");
        instance.overrideGTRecipe("shaped_item.gtclassic.lithiumbatpack_-1372018261", GTMaterialGen.get(GTItems.lithiumBatpack), "BCB", "BAB", "B B", 'B', GTItems.lithiumBattery, 'C', "circuitAdvanced", 'A', aluminium);
        instance.overrideGTRecipe("shaped_item.gtclassic.portable_scanner_1761100103", GTMaterialGen.get(GTItems.portableScanner), "AEA", "CcC", "ABA", 'A', aluminium, 'E', Ic2Items.euReader, 'C', "circuitAdvanced", 'c', Ic2Items.cropAnalyzer, 'B', "batteryRegular");
        instance.overrideGTRecipe("shaped_tile.gtclassic.quantumchest_771359385", GTMaterialGen.get(GTBlocks.tileQuantumChest), "DCD", "HTH", "DdD", 'D', GTItems.orbData, 'C', GEItems.computerMonitor, 'H', "machineBlockElite", 'T', Ic2Items.teleporter, 'd', GTBlocks.tileDigitalChest);
        instance.overrideGTRecipe("shaped_tile.gtclassic.digitalchest_1446441976", GTMaterialGen.get(GTBlocks.tileDigitalChest), "III", "SDS", "ICI", 'I', materialDigital, 'S', Items.SHULKER_SHELL, 'D', GTItems.orbData, 'C', GTBlocks.tileComputer);
        instance.overrideGTRecipe("shaped_tile.gtclassic.quantumtank_-1923909130", GTMaterialGen.get(GTBlocks.tileQuantumTank), "IBI", "CQC", "IBI", 'I', materialDigital, 'B', Items.BUCKET, 'C', "circuitMaster", 'Q', GTBlocks.tileQuantumChest);
        instance.overrideGTRecipe("shaped_tile.gtclassic.computercube_-2131399632", GTMaterialGen.get(GTBlocks.tileComputer), "CMO", "MAM", "OMC", 'C', "circuitMaster", 'M', GEItems.computerMonitor, 'O', "circuitUltimate", 'A', "machineBlockAdvanced");
        instance.overrideGTRecipe("shaped_tile.gtclassic.fusion_-786678229", GTMaterialGen.get(GTBlocks.casingFusion), "CSC", "NMN", "CRC", 'C', "circuitMaster", 'S', "craftingSuperconductor", 'N', GEItems.nichromeHeatingCoil, 'M',
                "machineBlockElite", 'R', Ic2Items.reactorReflectorIridium);
        if (GEConfiguration.usePlates){
            instance.overrideGTRecipe("shaped_tile.gtclassic.highlyadvanced_-1637699060", GTMaterialGen.get(GTBlocks.casingHighlyAdvanced), "CTC", "TMT", "CTC", 'C', "plateChrome", 'T', "plateTitanium", 'M', "machineBlockAdvanced");
            instance.overrideGTRecipe("shaped_tile.gtclassic.highlyadvanced_-1504256580", GTMaterialGen.get(GTBlocks.casingHighlyAdvanced), "TCT", "CMC", "TCT", 'C', "plateChrome", 'T', "plateTitanium", 'M', "machineBlockAdvanced");
        }
        instance.overrideGTRecipe("shaped_item.itemingotalloy_1703663469", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 3), "TTT", "MMM", "BBB", 'T', materialRefinedIron, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        instance.overrideGTRecipe("shaped_item.itemingotalloy_1844373769", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 6), "TTT", "MMM", "BBB", 'T', materialMixedMetal2, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        instance.overrideGTRecipe("shaped_item.itemingotalloy_-470293062", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 8), "TTT", "MMM", "BBB", 'T', tungstenSteel, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        instance.overrideGTRecipe("shaped_item.reactorvent_-735496828", Ic2Items.reactorVent, "PBP", "B B", "PBP", 'P', aluminium, 'B', Blocks.IRON_BARS);
        instance.removeGTRecipe("shaped_item.itemtoolmininglaser_1482495812");
        instance.overrideGTRecipe("shaped_tile.gtclassic.translocator_-1819938803", GTMaterialGen.get(GTBlocks.tileTranslocator), "EWE", "CBC", "EME", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitBasic", 'B', "machineBlockCheap", 'M', GEItems.conveyorModule);
        instance.overrideGTRecipe("shaped_tile.gtclassic.bufferlarge_1044342104", GTMaterialGen.get(GTBlocks.tileBufferLarge), "EWE", "CBC", "EcE", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitAdvanced", 'B', "machineBlockCheap", 'c', "chestWood");
    }

    public void overrideGTRecipe(String recipeId, ItemStack output, Object... input) {
        GTHelperAdvRecipe.overrideGTRecipe("gtclassic", recipeId, output, input);
    }
      //commented out this code till speiger makes a variable and 2 contructors public.
    public void overrideShapelessGTRecipe(String recipeId, ItemStack output, Object... input){
        GTHelperAdvRecipe.overrideShapelessGTRecipe("gtclassic", recipeId, output, input);
    }

    public void removeGTRecipe(String recipeId){
        GTHelperAdvRecipe.removeRecipe("gtclassic", recipeId);
    }
}
