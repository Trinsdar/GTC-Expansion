package gtc_expansion.recipes;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEConfiguration;
import gtc_expansion.GEItems;
import gtc_expansion.item.tools.GEToolGen;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtclassic.GTBlocks;
import gtclassic.GTConfig;
import gtclassic.GTItems;
import gtclassic.helpers.GTHelperAdvRecipe;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
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

public class GERecipeGT2 {
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

    static IRecipeInput plateElectric = new RecipeInputCombined(1, new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateSilicon"),
            new RecipeInputOreDict("plateAluminium"), new RecipeInputOreDict("plateSilver"),
            new RecipeInputOreDict("plateElectrum"), new RecipeInputOreDict("platePlatinum"));
    public static IRecipeInput anyLapis = new RecipeInputCombined(1, new RecipeInputOreDict("gemLapis"),
            new RecipeInputOreDict("dustLazurite"), new RecipeInputOreDict("dustSodalite"));
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

    public static void initIc2(){
        recipes.overrideRecipe("shaped_item.itempartcircuit_1058514721", Ic2Items.electricCircuit, "CCC", "RER", "CCC", 'C', Ic2Items.copperCable, 'R', "plateRedAlloy", 'E', plateElectric);
        recipes.overrideRecipe("shaped_item.itempartcircuit_1521116961", Ic2Items.electricCircuit, "CRC", "CEC", "CRC", 'C', Ic2Items.copperCable, 'R', "plateRedAlloy", 'E', plateElectric);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FFF", "TTT", "FFF", 'F', "dustFlint", 'T', Blocks.TNT);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FTF", "FTF", "FTF", 'F', "dustFlint", 'T', Blocks.TNT);
        String machineId = IC2.config.getFlag("SteelRecipes") ? "480320652" : "527557260";
        recipes.overrideRecipe("shaped_tile.blockmachine_" + machineId, Ic2Items.machine, "PPP", "P P", "PPP", 'P', ingotRefinedIron);
        ItemStack battery = Ic2Items.battery;
        String circuit = "circuitBasic";
        recipes.overrideRecipe("shaped_item.itemtoolwrenchelectric_883008511", Ic2Items.electricWrench, "SWS", "SCS", " B ",'S', materialSteels, 'W', Ic2Items.wrench, 'C', circuit, 'B', battery);
        if (GTConfig.harderIC2Macerator) {
            recipes.overrideRecipe("shaped_tile.blockMacerator_127744036", Ic2Items.macerator.copy(), "III", "IMI", "ICI", 'I', ingotRefinedIron, 'M', Ic2Items.stoneMacerator.copy(), 'C',
                    "circuitAdvanced");
            recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), "FDF", "DMD", "FCF", 'D', "gemDiamond", 'F', Items.FLINT, 'M', "machineBlockBasic", 'C',
                    "circuitAdvanced");
            recipes.addRecipe(Ic2Items.macerator.copy(), "FGF", "CMC", "FCF", 'G', grinder, 'F', Items.FLINT, 'M', "machineBlockBasic", 'C',
                    "circuitBasic");
        }
    }

    public static void initOverrideGTClassic(){
        instance.removeGTRecipe("shaped_item.itempartcircuit_1303953836");
        instance.removeGTRecipe("shaped_item.itempartcircuit_-181743188");
        instance.removeGTRecipe("shaped_item.gtclassic.data_chip_-270689326");
        instance.overrideGTRecipe("shaped_tile.gtclassic.computercube_404275118", GTMaterialGen.get(GTBlocks.tileComputer), "CMO", "MAM", "OMC", 'C', "circuitMaster", 'M', GEItems.computerMonitor, 'O', "circuitUltimate", 'A', "machineBlockAdvanced");
        instance.overrideGTRecipe("shaped_item.itemingotalloy_1703663469", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 3), "TTT", "MMM", "BBB", 'T', materialRefinedIron, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        instance.overrideGTRecipe("shaped_item.itemingotalloy_1844373769", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 6), "TTT", "MMM", "BBB", 'T', materialMixedMetal2, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        instance.overrideGTRecipe("shaped_item.itemingotalloy_-470293062", GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 8), "TTT", "MMM", "BBB", 'T', tungstenSteel, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        instance.overrideGTRecipe("shaped_item.gtclassic.rockcutter_1664690250", GTMaterialGen.get(GTItems.rockCutter), "DT ", "DT ", "DCB", new EnchantmentModifier(GTMaterialGen.get(GTItems.rockCutter), Enchantments.SILK_TOUCH).setUsesInput(), 'T', titanium, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        instance.overrideGTRecipe("shaped_item.gtclassic.jackhammer_2107301811", GTMaterialGen.get(GTItems.jackHammer), "TBT", " C ", " D ", 'T', titanium, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        instance.removeGTRecipe("shaped_item.itemtoolmininglaser_1482495812");
        if (GEConfiguration.usePlates){
            instance.overrideGTRecipe("shaped_item.reactorvent_-735496828", Ic2Items.reactorVent, "PBP", "B B", "PBP", 'P', "plateAluminium", 'B', Blocks.IRON_BARS);
            instance.overrideGTRecipe("shaped_tile.gtclassic.industrialcentrifuge_-1110475998", GTMaterialGen.get(GTBlocks.tileCentrifuge), "RCR", "MEM", "RCR", 'R', materialSteels, 'C', "circuitAdvanced", 'M', "machineBlockAdvanced", 'E', Ic2Items.extractor);
        }
        instance.overrideGTRecipe("shaped_tile.gtclassic.translocator_-1819938803", GTMaterialGen.get(GTBlocks.tileTranslocator), "EWE", "CBC", "EME", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitBasic", 'B', GEMaterialGen.getHull(GEMaterial.Brass, 1), 'M', GEItems.conveyorModule);
        instance.overrideGTRecipe("shaped_tile.gtclassic.bufferlarge_1044342104", GTMaterialGen.get(GTBlocks.tileBufferLarge), "EWE", "CBC", "EcE", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitAdvanced", 'B', GEMaterialGen.getHull(GEMaterial.Brass, 1), 'c', "chestWood");
    }
}
