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
import gtclassic.recipe.GTRecipe;
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

public class GERecipeGT4 {
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
    static String invar = GEConfiguration.usePlates ? "plateInvar" : "ingotInvar";
    static String titanium = GEConfiguration.usePlates ? "plateTitanium" : "ingotTitanium";
    static String platinum = GEConfiguration.usePlates ? "platePlatinum" : "ingotPlatinum";
    static String electrum = GEConfiguration.usePlates ? "plateElectrum" : "ingotElectrum";

    static IRecipeInput getSteelMachineBlock(){
        return IC2.config.getFlag("SteelRecipes") ? new RecipeInputCombined(1,input(Ic2Items.machine), input(GEMaterialGen.getHull(GEMaterial.StainlessSteel,1))) : new RecipeInputCombined(1,input(GEMaterialGen.getHull(GEMaterial.Steel, 1)), input(GEMaterialGen.getHull(GEMaterial.StainlessSteel,1)));
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
        recipes.addRecipe(GTMaterialGen.get(GTItems.lithiumBatpack), "BCB", " A ", 'B', GEItems.largeLithiumBattery, 'C', "circuitAdvanced", 'A', aluminium);
    }

    public static void initRemainingToolRecipes(){
        String stick = "stickWood";
        recipes.addRecipe(GEToolGen.getFile(GEMaterial.Bronze), "P", "P", "S", 'P', "plateBronze", 'S', stick);
        recipes.addRecipe(GEToolGen.getHammer(GEMaterial.Bronze), "PPP", "PPP", " S ", 'P', "ingotBronze", 'S', stick);
        recipes.overrideRecipe("shaped_item.itemtoolwrench_-354759652", GEToolGen.getWrench(GEMaterial.Bronze), "I I", "III", " I ", 'I', "ingotBronze");
        recipes.addRecipe(GEToolGen.getFile(GEMaterial.Iron), "P", "P", "S", 'P', "plateIron", 'S', stick);
        recipes.addRecipe(GEToolGen.getHammer(GEMaterial.Iron), "PPP", "PPP", " S ", 'P', "ingotIron", 'S', stick);
        recipes.addRecipe(GEToolGen.getWrench(GEMaterial.Iron), "I I", "III", " I ", 'I', "ingotIron");
    }

    public static void initIc2(){
        recipes.overrideRecipe("shaped_item.itempartcircuit_1058514721", Ic2Items.electricCircuit, "CCC", "RER", "CCC", 'C', Ic2Items.copperCable, 'R', "plateRedAlloy", 'E', plateElectric);
        recipes.overrideRecipe("shaped_item.itempartcircuit_1521116961", Ic2Items.electricCircuit, "CRC", "CEC", "CRC", 'C', Ic2Items.copperCable, 'R', "plateRedAlloy", 'E', plateElectric);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FFF", "TTT", "FFF", 'F', "dustFlint", 'T', Blocks.TNT);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FTF", "FTF", "FTF", 'F', "dustFlint", 'T', Blocks.TNT);
        String machineId = IC2.config.getFlag("SteelRecipes") ? "480320652" : "527557260";
        recipes.overrideRecipe("shaped_tile.blockmachine_" + machineId, Ic2Items.machine, "PPP", "PWP", "PPP", 'P', getRefinedIronPlate(), 'W', "craftingToolWrench");
        recipes.overrideRecipe("shaped_item.upgradekit.mfs_1186329581", Ic2Items.mfsuUpgradeKid, "BMB", "BBB", " B ", 'B', "ingotBronze", 'M', Ic2Items.mfsu);
        GTHelperAdvRecipe.removeRecipe("ic2", "shaped_item.upgradekit.mfs_-1749227982");
        ItemStack battery = Ic2Items.battery;
        String circuit = "circuitBasic";
        recipes.overrideRecipe("shaped_item.itemtoolwrenchelectric_883008511", Ic2Items.electricWrench, "S S", "SCS", " B ",'S', materialSteels, 'C', circuit, 'B', battery);
        if (GTConfig.harderIC2Macerator) {
            recipes.overrideRecipe("shaped_tile.blockMacerator_127744036", Ic2Items.macerator.copy(), "III", "IMI", "ICI", 'I', ingotRefinedIron, 'M', Ic2Items.stoneMacerator.copy(), 'C',
                    "circuitAdvanced");
            recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), "FDF", "DMD", "FCF", 'D', "gemDiamond", 'F', materialSteels, 'M', getSteelMachineBlock(), 'C',
                    "circuitAdvanced");
            recipes.addRecipe(Ic2Items.macerator.copy(), "FGF", "CMC", "FCF", 'G', grinder, 'F', materialSteels, 'M', getSteelMachineBlock(), 'C',
                    "circuitBasic");
        }
        if (GTConfig.addAdvCircuitRecipes){
            recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-1948043137", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RGR", "LCL", "RGR", 'R', "plateRedAlloy", 'G', "dustGlowstone", 'C', "circuitBasic", 'L', anyLapis);
            recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-205948801", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RLR", "GCG", "RLR", 'R', "plateRedAlloy", 'G', "dustGlowstone", 'C', "circuitBasic", 'L', anyLapis);
        }
    }

    public static void initOverrideGTClassic(){
        instance.removeGTRecipe("shaped_item.itempartcircuit_1303953836");
        instance.removeGTRecipe("shaped_item.itempartcircuit_-181743188");
        instance.removeGTRecipe("shaped_item.itempartcircuitadv_1366235615");
        instance.removeGTRecipe("shaped_item.itempartcircuitadv_55792611");
        IRecipeInput rod = new RecipeInputCombined(1, input("rodTitanium", 1), input("rodTungstensteel", 1));
        IRecipeInput plate = new RecipeInputCombined(1, input(titanium, 1), input(tungstenSteel, 1));
        instance.overrideGTRecipe("shaped_item.gtclassic.rockcutter_1664690250", GTMaterialGen.get(GTItems.rockCutter), "DR ", "DT ", "DCB", new EnchantmentModifier(GTMaterialGen.get(GTItems.rockCutter), Enchantments.SILK_TOUCH).setUsesInput(), 'R', rod, 'T', plate, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        instance.overrideGTRecipe("shaped_item.gtclassic.jackhammer_2107301811", GTMaterialGen.get(GTItems.jackHammer), "TBT", " C ", " D ", 'T', "rodTungstensteel", 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        instance.overrideGTRecipe("shaped_tile.gtclassic.industrialcentrifuge_-1110475998", GTMaterialGen.get(GTBlocks.tileCentrifuge), "RCR", "MEM", "RCR", 'R', materialSteels, 'C', "circuitAdvanced", 'M', "machineBlockAdvanced", 'E', Ic2Items.extractor);
    }

    public static IRecipeInput input(String name, int size){
        return GTTileBaseMachine.input(name, size);
    }

    public static IRecipeInput input(ItemStack stack){
        return GTTileBaseMachine.input(stack);
    }
}
