package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXItems;
import gtc_expansion.item.tools.GTCXToolGen;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeCraftingHandler;
import gtclassic.api.tile.GTTileBaseMachine;
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
import net.minecraft.item.ItemStack;

public class GTCXRecipeGT4 {
    static GTCXRecipe instance = new GTCXRecipe();
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    static String ingotRefinedIron = IC2.getRefinedIron();
    static IRecipeInput ingotMachine = new RecipeInputCombined(1, new RecipeInputOreDict(ingotRefinedIron), new RecipeInputOreDict("ingotAluminium"));
    public static IRecipeInput plateMachine = new RecipeInputCombined(1, new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateAluminium"));
    static IRecipeInput ingotSteels = new RecipeInputCombined(1, new RecipeInputOreDict("ingotSteel"), new RecipeInputOreDict("ingotStainlessSteel"));
    public static IRecipeInput plateSteels = new RecipeInputCombined(1, new RecipeInputOreDict("plateSteel"), new RecipeInputOreDict("plateStainlessSteel"));
    static IRecipeInput ingotBrassBronze = new RecipeInputCombined(1, new RecipeInputOreDict("ingotBronze"), new RecipeInputOreDict("ingotBrass"));
    static IRecipeInput plateBrassBronze = new RecipeInputCombined(1, new RecipeInputOreDict("plateBronze"), new RecipeInputOreDict("plateBrass"));
    static IRecipeInput materialBrassBronze = GTCXConfiguration.general.usePlates ? plateBrassBronze : ingotBrassBronze;
    static IRecipeInput ingotTinZinc = new RecipeInputCombined(1, new RecipeInputOreDict("ingotZinc"), new RecipeInputOreDict("ingotTin"));
    static IRecipeInput plateTinZinc = new RecipeInputCombined(1, new RecipeInputOreDict("plateZinc"), new RecipeInputOreDict("plateTin"));
    static IRecipeInput materialTinZinc = GTCXConfiguration.general.usePlates ? plateTinZinc : ingotTinZinc;
    static IRecipeInput ingotInvarAluminium = new RecipeInputCombined(1, new RecipeInputOreDict("ingotInvar"), new RecipeInputOreDict("ingotAluminium"));
    static IRecipeInput plateInvarAluminium = new RecipeInputCombined(1, new RecipeInputOreDict("plateInvar"), new RecipeInputOreDict("plateAluminium"));
    static IRecipeInput materialInvarAluminium = GTCXConfiguration.general.usePlates ? plateInvarAluminium : ingotInvarAluminium;
    static IRecipeInput ingotMixedMetal1 = new RecipeInputCombined(1, new RecipeInputOreDict("ingotAluminium"), new RecipeInputOreDict("ingotSilver"), new RecipeInputOreDict("ingotElectrum"));
    static IRecipeInput plateMixedMetal1 = new RecipeInputCombined(1, new RecipeInputOreDict("plateAluminium"), new RecipeInputOreDict("plateSilver"), new RecipeInputOreDict("plateElectrum"));
    static IRecipeInput materialMixedMetal1 = GTCXConfiguration.general.usePlates ? plateMixedMetal1 : ingotMixedMetal1;
    static IRecipeInput ingotMixedMetal2 = new RecipeInputCombined(1, new RecipeInputOreDict("ingotTungsten"), new RecipeInputOreDict("ingotTitanium"));
    static IRecipeInput plateMixedMetal2 = new RecipeInputCombined(1, new RecipeInputOreDict("plateTungsten"), new RecipeInputOreDict("plateTitanium"));
    static IRecipeInput materialMixedMetal2 = GTCXConfiguration.general.usePlates ? plateMixedMetal2 : ingotMixedMetal2;
    static String materialRefinedIron = GTCXConfiguration.general.usePlates ? getRefinedIronPlate() : ingotRefinedIron;
    static IRecipeInput materialMachine = GTCXConfiguration.general.usePlates ? plateMachine : ingotMachine;
    static IRecipeInput materialSteels = GTCXConfiguration.general.usePlates ? plateSteels : ingotSteels;

    static IRecipeInput plateElectric = new RecipeInputCombined(1, new RecipeInputOreDict(getRefinedIronPlate()), new RecipeInputOreDict("plateSilicon"),
            new RecipeInputOreDict("plateAluminium"), new RecipeInputOreDict("plateSilver"),
            new RecipeInputOreDict("plateElectrum"), new RecipeInputOreDict("platePlatinum"));
    public static IRecipeInput anyLapis = new RecipeInputCombined(1, new RecipeInputOreDict("gemLapis"),
            new RecipeInputOreDict("dustLazurite"), new RecipeInputOreDict("dustSodalite"));
    static IRecipeInput reinforcedGlass = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.reinforcedGlass), new RecipeInputItemStack(Ic2Items.reinforcedGlassClear));
    static IRecipeInput grinder = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(GTCXItems.diamondGrinder)), new RecipeInputItemStack(GTMaterialGen.get(GTCXItems.wolframiumGrinder)));
    static IRecipeInput tier2Energy = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.energyCrystal), new RecipeInputItemStack(GTMaterialGen.get(GTItems.lithiumBattery)));

    static String steel = GTCXConfiguration.general.usePlates ? "plateSteel" : "ingotSteel";
    static String tungsten = GTCXConfiguration.general.usePlates ? "plateTungsten" : "ingotTungsten";
    static String tungstenSteel = GTCXConfiguration.general.usePlates ? "plateTungstensteel" : "ingotTungstensteel";
    static String aluminium = GTCXConfiguration.general.usePlates ? "plateAluminium" : "ingotAluminium";
    static String invar = GTCXConfiguration.general.usePlates ? "plateInvar" : "ingotInvar";
    static String titanium = GTCXConfiguration.general.usePlates ? "plateTitanium" : "ingotTitanium";
    static String platinum = GTCXConfiguration.general.usePlates ? "platePlatinum" : "ingotPlatinum";
    static String electrum = GTCXConfiguration.general.usePlates ? "plateElectrum" : "ingotElectrum";
    static String lead = GTCXConfiguration.general.usePlates ? "plateLead" : "ingotLead";

    static IRecipeInput getSteelMachineBlock(){
        return IC2.config.getFlag("SteelRecipes") ? new RecipeInputCombined(1,input(Ic2Items.machine), input(GTCXMaterialGen.getHull(GTCXMaterial.StainlessSteel,1))) : new RecipeInputCombined(1,input(GTCXMaterialGen.getHull(GTCXMaterial.Steel, 1)), input(GTCXMaterialGen.getHull(GTCXMaterial.StainlessSteel,1)));
    }

    public static String getRefinedIronPlate() {
        return IC2.config.getFlag("SteelRecipes") ? "plateSteel" : "plateRefinedIron";
    }

    public static void init(){
        initShapedItemRecipes();
        initRemainingToolRecipes();
        initIc2();
        initOverrideGTClassic();
    }

    public static void initShapedItemRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GTItems.lithiumBatpack), "BCB", " A ", 'B', GTCXItems.largeLithiumBattery, 'C', "circuitAdvanced", 'A', aluminium);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.batteryHull), "C", "B", "B", 'C', Ic2Items.copperCable, 'B', "plateBatteryAlloy");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.largeBatteryHull), "C C", "BBB", "BBB", 'C', Ic2Items.goldCable, 'B', "plateBatteryAlloy");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.acidBattery), " C ", "LAL", "LAL", 'C', Ic2Items.copperCable, 'L', lead, 'A', GTMaterialGen.getTube(GTMaterial.SulfuricAcid, 1));
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.mercuryBattery), " C ", "LAL", "LAL", 'C', Ic2Items.copperCable, 'L', lead, 'A', GTMaterialGen.getTube(GTMaterial.Mercury, 1));
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.sodiumBattery), " C ", "LAL", "LAL", 'C', Ic2Items.goldCable, 'L', aluminium, 'A', GTMaterialGen.getTube(GTMaterial.Sodium, 1));
    }

    public static void initRemainingToolRecipes(){
        String stick = "stickWood";
        recipes.addRecipe(GTCXToolGen.getFile(GTCXMaterial.Bronze), "P", "P", "S", 'P', "plateBronze", 'S', stick);
        recipes.addRecipe(GTCXToolGen.getHammer(GTCXMaterial.Bronze), "PPP", "PPP", " S ", 'P', "ingotBronze", 'S', stick);
        recipes.overrideRecipe("shaped_item.itemtoolwrench_-354759652", GTCXToolGen.getWrench(GTCXMaterial.Bronze), "I I", "III", " I ", 'I', "ingotBronze");
        recipes.addRecipe(GTCXToolGen.getFile(GTCXMaterial.Iron), "P", "P", "S", 'P', "plateIron", 'S', stick);
        recipes.addRecipe(GTCXToolGen.getHammer(GTCXMaterial.Iron), "PPP", "PPP", " S ", 'P', "ingotIron", 'S', stick);
        recipes.addRecipe(GTCXToolGen.getWrench(GTCXMaterial.Iron), "I I", "III", " I ", 'I', "ingotIron");
    }

    public static void initIc2(){
        int recipeId = IC2.config.getFlag("SteelRecipes") ? 1921363733 : 1058514721;
        recipes.overrideRecipe("shaped_item.itempartcircuit_" + recipeId, Ic2Items.electricCircuit, "CCC", "RER", "CCC", 'C', Ic2Items.copperCable, 'R', "plateRedAlloy", 'E', plateElectric);
        recipeId = IC2.config.getFlag("SteelRecipes") ? -1911001323 : 1521116961;
        recipes.overrideRecipe("shaped_item.itempartcircuit_" + recipeId, Ic2Items.electricCircuit, "CRC", "CEC", "CRC", 'C', Ic2Items.copperCable, 'R', "plateRedAlloy", 'E', plateElectric);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FFF", "TTT", "FFF", 'F', "dustFlint", 'T', Blocks.TNT);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FTF", "FTF", "FTF", 'F', "dustFlint", 'T', Blocks.TNT);
        recipeId = IC2.config.getFlag("SteelRecipes") ? 480320652 : 527557260;
        recipes.overrideRecipe("shaped_tile.blockmachine_" + recipeId, Ic2Items.machine, "PPP", "PWP", "PPP", 'P', getRefinedIronPlate(), 'W', "craftingToolWrench");
        recipes.overrideRecipe("shaped_item.upgradekit.mfs_1186329581", Ic2Items.mfsuUpgradeKid, "BMB", "BBB", " B ", 'B', "ingotBronze", 'M', Ic2Items.mfsu);
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.upgradekit.mfs_-1749227982");
        ItemStack battery = Ic2Items.battery;
        String circuit = "circuitBasic";
        recipes.overrideRecipe("shaped_item.itemtoolwrenchelectric_883008511", Ic2Items.electricWrench, "S S", "SCS", " B ",'S', materialSteels, 'C', circuit, 'B', battery);
        if (GTConfig.general.harderIC2Macerator) {
            recipes.overrideRecipe("shaped_tile.blockMacerator_127744036", Ic2Items.macerator.copy(), "III", "IMI", "ICI", 'I', GTCXRecipe.materialRefinedIron, 'M', Ic2Items.stoneMacerator.copy(), 'C',
                    "circuitAdvanced");
            recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), "FDF", "DMD", "FCF", 'D', "gemDiamond", 'F', materialSteels, 'M', getSteelMachineBlock(), 'C',
                    "circuitAdvanced");
            recipes.addRecipe(Ic2Items.macerator.copy(), "FGF", "CMC", "FCF", 'G', grinder, 'F', materialSteels, 'M', getSteelMachineBlock(), 'C',
                    "circuitBasic");
        }
        if (GTConfig.general.addAdvCircuitRecipes){
            recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-1948043137", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RGR", "LCL", "RGR", 'R', "plateRedAlloy", 'G', "dustGlowstone", 'C', "circuitBasic", 'L', anyLapis);
            recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-205948801", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RLR", "GCG", "RLR", 'R', "plateRedAlloy", 'G', "dustGlowstone", 'C', "circuitBasic", 'L', anyLapis);
        }
    }

    public static void initOverrideGTClassic(){
        IRecipeInput rod = new RecipeInputCombined(1, input("rodTitanium", 1), input("rodTungstensteel", 1));
        IRecipeInput plate = new RecipeInputCombined(1, input(titanium, 1), input(tungstenSteel, 1));
        recipes.addRecipe(GTMaterialGen.get(GTItems.rockCutter), "DR ", "DT ", "DCB", new EnchantmentModifier(GTMaterialGen.get(GTItems.rockCutter), Enchantments.SILK_TOUCH).setUsesInput(), 'R', rod, 'T', plate, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        recipes.addRecipe(GTMaterialGen.get(GTItems.jackHammer), "TBT", " C ", " D ", 'T', "rodTungstensteel", 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileCentrifuge), "RCR", "MEM", "RCR", 'R', materialSteels, 'C', "circuitAdvanced", 'M', "machineBlockAdvanced", 'E', Ic2Items.extractor);
    }

    public static IRecipeInput input(String name, int size){
        return GTTileBaseMachine.input(name, size);
    }

    public static IRecipeInput input(ItemStack stack){
        return GTTileBaseMachine.input(stack);
    }
}
