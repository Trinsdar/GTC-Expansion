package gtc_expansion.recipes;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXConfiguration;
import gtc_expansion.block.GTCXBlockWire;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXPipes;
import gtc_expansion.item.tools.GTCXToolGen;
import gtc_expansion.jei.wrapper.GTCXJeiIntegratedCircuitWrapper;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.GTCXTileAlloySmelter;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileBath;
import gtc_expansion.tile.GTCXTileCentrifuge;
import gtc_expansion.tile.GTCXTileChemicalReactor;
import gtc_expansion.tile.GTCXTileDieselGenerator;
import gtc_expansion.tile.GTCXTileElectrolyzer;
import gtc_expansion.tile.GTCXTileExtruder;
import gtc_expansion.tile.GTCXTileFluidCaster;
import gtc_expansion.tile.GTCXTileFluidSmelter;
import gtc_expansion.tile.GTCXTileGasTurbine;
import gtc_expansion.tile.GTCXTileLathe;
import gtc_expansion.tile.GTCXTileMicrowave;
import gtc_expansion.tile.GTCXTilePlateBender;
import gtc_expansion.tile.GTCXTilePlateCutter;
import gtc_expansion.tile.GTCXTileWiremill;
import gtc_expansion.tile.multi.GTCXTileMultiCokeOven;
import gtc_expansion.tile.multi.GTCXTileMultiDistillationTower;
import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import gtc_expansion.tile.multi.GTCXTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialSawmill;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiVacuumFreezer;
import gtc_expansion.tile.steam.GTCXTileSteamCompressor;
import gtc_expansion.tile.steam.GTCXTileSteamExtractor;
import gtc_expansion.tile.steam.GTCXTileSteamForgeHammer;
import gtc_expansion.tile.steam.GTCXTileSteamFurnace;
import gtc_expansion.tile.steam.GTCXTileSteamMacerator;
import gtc_expansion.util.GTCXHelperPipe;
import gtc_expansion.util.GTCXIc2cECompat;
import gtc_expansion.util.GTCXRecipeInputIngredient;
import gtclassic.GTMod;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeCraftingHandler;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTConfig;
import gtclassic.common.GTItems;
import gtclassic.common.tile.GTTileUUMAssembler;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.item.recipe.upgrades.EnchantmentModifier;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.ArrayList;
import java.util.List;

import static gtc_expansion.data.GTCXValues.*;
import static gtclassic.api.helpers.GTValues.*;
import static gtclassic.api.recipe.GTRecipeCraftingHandler.combineRecipeObjects;
import static gtclassic.api.recipe.GTRecipeCraftingHandler.removeRecipe;

public class GTCXRecipe {
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    public static List<GTCXJeiIntegratedCircuitWrapper.IntegratedCircuitRecipe> integratedCircuitRecipes = new ArrayList<>();


    public static void init(){
        GTCXRecipeRemove.init();
        GTCXTileCentrifuge.init();
        GTCXRecipeProcessing.init();
        GTCXTileElectrolyzer.init();
        GTCXTileMultiVacuumFreezer.init();
        GTCXTileMultiIndustrialGrinder.init();
        GTCXTileBath.init();
        GTCXTileMultiImplosionCompressor.init();
        GTCXTileMultiIndustrialBlastFurnace.init();
        GTCXTileMultiDistillationTower.init();
        GTCXTileAlloySmelter.init();
        GTCXTileAssemblingMachine.init();
        GTCXTileChemicalReactor.init();
        GTCXTileMultiPrimitiveBlastFurnace.init();
        GTCXTileFluidCaster.init();
        GTCXTileFluidSmelter.init();
        GTCXTilePlateBender.init();
        GTCXTilePlateCutter.init();
        GTCXTileWiremill.init();
        GTCXTileExtruder.init();
        GTCXTileLathe.init();
        GTCXTileDieselGenerator.init();
        GTCXTileGasTurbine.init();
        GTCXTileMultiCokeOven.init();
        GTCXTileSteamForgeHammer.init();
        initUURecipes();
        initIc2();
        initOverrideGTClassic();
        initOverrideVanillaRecipes();
        initShapedItemRecipes();
        initPreElectric();
        initShapedBlockRecipes();
        initRemainingToolRecipes();
        initShapelessRecipes();
        GTCXRecipeIterators.init();
    }

    public static void postInit(){
        GTCXRecipeMods.init();
        GTCXRecipeIterators.initAutoOredictMachineRecipes();
        GTCXTileMicrowave.init();
        GTCXRecipeRemove.postInit();
        GTCXTileSteamCompressor.init();
        GTCXTileSteamExtractor.init();
        GTCXTileSteamMacerator.init();
        GTCXTileSteamFurnace.init();
        GTCXTileMultiIndustrialSawmill.init();
        GTCXTileMultiFusionReactor.postInit();
        initWoodRecipes();
        initIntegratedCircuit();
    }

    static void initIntegratedCircuit(){
        integratedCircuitRecipes.clear();
        List<IRecipeInput> inputs = new ArrayList<>();
        List<IRecipeInput> outputs = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            inputs.add(new RecipeInputItemStack(GTMaterialGen.get(GTCXItems.integratedCircuit, 1, i)));
            outputs.add(new RecipeInputItemStack(GTMaterialGen.get(GTCXItems.integratedCircuit, 1, i)));
        }
        integratedCircuitRecipes.add(new GTCXJeiIntegratedCircuitWrapper.IntegratedCircuitRecipe(new RecipeInputCombined(inputs), new RecipeInputCombined(outputs)));
    }

    static void initShapedItemRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.computerMonitor), "IGI", "RPB", "IgI", 'I', ALUMINIUM, 'G', "dyeGreen", 'R', "dyeRed", 'P', "paneGlass", 'B', "dyeBlue", 'g', DUST_GLOWSTONE);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.conveyorModule), "GGG", "AAA", "CBC", 'G', BLOCK_GLASS, 'A', MATERIAL_MACHINE, 'C', CIRCUIT_BASIC, 'B', Ic2Items.battery.copy());
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.diamondGrinder, 2), "DSD", "SdS", "DSD", 'D', "dustDiamond", 'S', STEEL, 'd', GEM_DIAMOND);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.wolframiumGrinder, 2), "TST", "SBS", "TST", 'T', TUNGSTEN, 'S', STEEL, 'B', "blockSteel");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.steelJackhammer), "SBS", " C ", " s ", 'S', ROD_STEELS, 'B', Ic2Items.battery, 'C', CIRCUIT_ADVANCED, 's', INGOT_STEELS);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.diamondChainsaw), " DD", "TdD", "CT ", 'D', "dustDiamond", 'd', Ic2Items.chainSaw, 'T', TITANIUM, 'C', CIRCUIT_ADVANCED);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.diamondSawblade), " D ", "DSD", " D ", 'D', "dustSmallDiamond", 'S', combineRecipeObjects("gearSteel", "gearStainlessSteel"));
        if (GTCXConfiguration.general.unfiredBricks){
            recipes.addRecipe(GTMaterialGen.get(GTCXItems.unfiredBrick, 2), "C", "C", 'C', Items.CLAY_BALL);
            recipes.addRecipe(GTMaterialGen.get(GTCXItems.unfiredFireBrick, 2), "C", "C", 'C', GTCXItems.fireClayBall);
        }
        recipes.addRecipe(GTMaterialGen.get(GTItems.lithiumBatpack), "BCB", " A ", 'B', GTCXItems.largeLithiumBattery, 'C', CIRCUIT_ADVANCED, 'A', ALUMINIUM);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.batteryHull), "C", "B", "B", 'C', Ic2Items.copperCable, 'B', "plateBatteryAlloy");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.largeBatteryHull), "C C", "BBB", "BBB", 'C', Ic2Items.goldCable, 'B', "plateBatteryAlloy");
        recipes.addRecipe(GTCXItems.acidBattery.getFull(), " C ", "LAL", "LAL", 'C', Ic2Items.copperCable, 'L', LEAD, 'A', GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 1));
        recipes.addRecipe(GTCXItems.mercuryBattery.getFull(), " C ", "LAL", "LAL", 'C', Ic2Items.copperCable, 'L', LEAD, 'A', GTMaterialGen.getTube(GTMaterial.Mercury, 1));
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.sodiumBattery), " C ", "LAL", "LAL", 'C', Ic2Items.goldCable, 'L', ALUMINIUM, 'A', GTMaterialGen.getTube(GTMaterial.Sodium, 1));
        if (GTCXConfiguration.general.enableCraftingTools){
            recipes.addRecipe(GTMaterialGen.get(GTCXItems.mold), "HF", "SS", "SS", 'H', "craftingToolForgeHammer", 'F', "craftingToolFile", 'S', STEEL);
        } else {
            recipes.addRecipe(GTMaterialGen.get(GTCXItems.mold), "SS", "SS", 'S', STEEL);
        }
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldPlate), "WM", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldRod), "M ", " W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldCell), "W", "M", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), " W", "M ", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldWire), "M", "W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), "W ", " M", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldSmallPipe), "M  ", "  W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldMediumPipe), "M ", "  ", " W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldLargePipe), "M  ", "   ", "  W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldBlock), "M W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldGear), "W", " ", "M", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        IRecipeInput hammer = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? input("craftingToolForgeHammer") : null;
        IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? input("craftingToolWrench") : null;
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.integratedCircuit), "PHP", "RRR", "PWP", 'P', combineRecipeObjects(REFINED_IRON, PRE + "Iron", STEEL), 'H', hammer, 'R', combineRecipeObjects("rodRefinedIron", "rodIron", "rodSteel"), 'W', wrench);
        addRotorRecipe(GTMaterialGen.get(GTCXItems.bronzeTurbineRotor) , GTCXMaterial.Bronze, "blockBronze");
        addRotorRecipe(GTMaterialGen.get(GTCXItems.steelTurbineRotor) , GTCXMaterial.Steel, "blockSteel");
        addRotorRecipe(GTMaterialGen.get(GTCXItems.magnaliumTurbineRotor) , GTCXMaterial.Magnalium, "blockMagnalium");
        addRotorRecipe(GTMaterialGen.get(GTCXItems.tungstensteelTurbineRotor) , GTCXMaterial.TungstenSteel, "blockSteel");
        addRotorRecipe(GTMaterialGen.get(GTCXItems.carbonTurbineRotor) , GTMaterial.Carbon, Ic2Items.carbonPlate);
        addRotorRecipe(GTMaterialGen.get(GTCXItems.osmiumTurbineRotor) , GTCXMaterial.Osmium, "blockOsmiumGT");
        addRotorRecipe(GTMaterialGen.get(GTCXItems.osmiridiumTurbineRotor) , GTCXMaterial.Osmiridium, "blockOsmiridium");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.electricScrewdriver), "R  ", " RC", "  B", 'R', "rodStainlessSteel", 'C', CIRCUIT_BASIC, 'B', Ic2Items.battery);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.coalCokeBlock), "CCC", "CCC", "CCC", 'C', "fuelCoke");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.drain), "BPB", "P P", "BPB", 'P', combineRecipeObjects(ALUMINIUM, REFINED_IRON), 'B', Blocks.IRON_BARS);
    }

    public static void addRotorRecipe(ItemStack rotor, GTMaterial material, Object center){
        recipes.addRecipe(rotor, "BBB", "BbB", "BBB", 'B', GTMaterialGen.getStack(material, GTCXMaterial.turbineBlade, 1), 'b', center);
        recipes.addRecipe(rotor, " B ", "BbB", " B ", 'B', GTMaterialGen.getStack(material, GTCXMaterial.turbineBlade, 1), 'b', GTMaterialGen.getStack(material, GTCXMaterial.brokenTurbineRotor, 1));
    }

    static void initUURecipes(){
        if (GTConfig.general.gregtechUURecipes){
            if (!GTCXConfiguration.general.removeCraftingUURecipes){
                recipes.addRecipe(GTMaterialGen.getGem(GTCXMaterial.Olivine, 1), "UU ", "UUU", "UU ", 'U', Ic2Items.uuMatter, true);
                recipes.addRecipe(GTMaterialGen.getDust(GTCXMaterial.Zinc, 10), "   ", "U U", "U  ", 'U', Ic2Items.uuMatter, true);
                recipes.addRecipe(GTMaterialGen.getDust(GTMaterial.Nickel, 10), "U  ", "U U", "   ", 'U', Ic2Items.uuMatter, true);
                recipes.addRecipe(GTCXMaterialGen.getSmallDust(GTCXMaterial.Osmium, 1), "U U", "UUU", "U U", 'U', Ic2Items.uuMatter, true);
            }
            GTTileUUMAssembler.addUUMAssemblerValue(7, GTMaterialGen.getGem(GTCXMaterial.Olivine, 1));
            GTTileUUMAssembler.addUUMAssemblerValue(3, GTMaterialGen.getDust(GTCXMaterial.Zinc, 10));
            GTTileUUMAssembler.addUUMAssemblerValue(3, GTMaterialGen.getDust(GTMaterial.Nickel, 10));
            GTTileUUMAssembler.addUUMAssemblerValue(7, GTCXMaterialGen.getSmallDust(GTCXMaterial.Osmium, 1));
            GTTileUUMAssembler.addUUMAssemblerValue(1, Ic2Items.uuMatter.copy());
        }
        if (GTCXConfiguration.general.removeCraftingUURecipes){
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.stonebrick_-602048670");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.stone.stone_-217206169");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.glass_1510125347");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.grass_1277632969");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.oak_-1277881396");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.spruce_1770332581");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.birch_-1277881334");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.jungle_1730114793");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.acacia_569794864");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.big_oak_529577045");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.dirt.default_294096457");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.sponge.dry_-486870232");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.whitestone_1292746542");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.stonemoss_-71282124");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.sandstone.default_-222457682");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.redsandstone.default_1661771456");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.snow_-66319588");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.water_2131885151");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.lava_1530544253");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.oreiron_-329870047");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.oregold_-862634671");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.obsidian_-440375730");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.hellrock_-1150777333");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.netherstalkseeds_-1945039845");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.lightgem_-1357010811");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.cactus_1557334976");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.vine_-128181428");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.chorusfruit_-621965693");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.cloth.white_77283415");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.coal_-894344071");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.diamond_-839125610");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.redstone_609035693");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.netherquartz_-150174040");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.dyepowder.blue_-1943975137");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.feather_1656145116");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.leather_2089489131");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.snowball_-427886876");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.sulphur_-1833859045");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.enderpearl_2025630033");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.clay_916245029");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.blazerod_1662380818");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.prismarinecrystals_-886701636");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.prismarineshard_-2053819173");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.dyepowder.brown_1426380757");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.dyepowder.black_953740984");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.reeds_-459416326");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.flint_-1284138319");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.bone_-835878595");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.itemharz_2066627912");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.itemoreiridium_358070459");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.mycel_1348556715");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.stonebricksmooth.chiseled_1563944091");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockorecopper_-1838757257");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockoretin_1214808534");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockoresilver_1084210042");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.emerald_981588030");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.gemruby_891389810");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.gemsapphire_-674562982");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_tile.gtclassic.orebauxite_-377989822");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.dusttitanium_171358254");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.dustaluminium_575613706");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.dustplatinum_645900471");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.dusttungsten_1935969503");
        }
    }

    static void initRemainingToolRecipes(){
        String stick = "stickWood";
        recipes.addRecipe(GTCXToolGen.getPickaxe(GTMaterial.Flint), "FFF", " S ", " S ", new EnchantmentModifier(GTCXToolGen.getPickaxe(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GTCXToolGen.getAxe(GTMaterial.Flint), "FF", "FS", " S", new EnchantmentModifier(GTCXToolGen.getAxe(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GTCXToolGen.getShovel(GTMaterial.Flint), "F", "S", "S", new EnchantmentModifier(GTCXToolGen.getShovel(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GTCXToolGen.getSword(GTMaterial.Flint), "F", "F", "S", new EnchantmentModifier(GTCXToolGen.getSword(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        IRecipeInput hammer = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? input("craftingToolForgeHammer") : null;
        recipes.overrideRecipe("shaped_item.itemtoolwrench_-354759652", GTCXToolGen.getWrench(GTCXMaterial.Bronze), "IHI", "III", " I ", 'I', "plateBronze", 'H', hammer);
        recipes.addRecipe(GTCXToolGen.getWrench(GTCXMaterial.Iron), "IHI", "III", " I ", 'I', "plateIron", 'H', hammer);
        if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
            recipes.addRecipe(GTCXToolGen.getFile(GTCXMaterial.Bronze), "P", "P", "S", 'P', "plateBronze", 'S', stick);
            recipes.addRecipe(GTCXToolGen.getFile(GTCXMaterial.Iron), "P", "P", "S", 'P', "plateIron", 'S', stick);
            recipes.addRecipe(GTCXToolGen.getSaw(GTCXMaterial.Bronze), "SSS", "PPS", "FH ", 'S', stick, 'P', "plateBronze", 'F', "craftingToolFile", 'H', "craftingToolForgeHammer");
            recipes.addRecipe(GTCXToolGen.getSaw(GTCXMaterial.Iron), "SSS", "PPS", "FH ", 'S', stick, 'P', "plateIron", 'F', "craftingToolFile", 'H', "craftingToolForgeHammer");
        }
        recipes.addRecipe(GTCXToolGen.getHammer(GTCXMaterial.Bronze), "PPP", "PPP", " S ", 'P', "ingotBronze", 'S', stick);
        recipes.addRecipe(GTCXToolGen.getHammer(GTCXMaterial.Iron), "PPP", "PPP", " S ", 'P', "ingotIron", 'S', stick);
        recipes.addRecipe(GTCXToolGen.getCrowbar(GTCXMaterial.Iron), " BR", "BRB", "RB ", 'B', "dyeBlue", 'R', "rodIron");
        recipes.addRecipe(GTCXToolGen.getCrowbar(GTCXMaterial.Bronze), " BR", "BRB", "RB ", 'B', "dyeBlue", 'R', "rodBronze");
        recipes.addRecipe(GTCXToolGen.getScrewdriver(GTCXMaterial.Iron), "R  ", " R ", "  S", 'S', "stickWood", 'R', "rodIron");
        recipes.addRecipe(GTCXToolGen.getScrewdriver(GTCXMaterial.Bronze), "R  ", " R ", "  S", 'S', "stickWood", 'R', "rodBronze");
        IRecipeInput file = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? input("craftingToolFile") : null;
        if (Loader.isModLoaded(GTValues.MOD_ID_FORESTRY)){
            recipes.addRecipe(GTCXToolGen.getBranchCutter(GTCXMaterial.Bronze), "PFP", "P P", "RRR", 'P', "plateBronze", 'F', file, 'R', "rodBronze");
            recipes.addRecipe(GTCXToolGen.getBranchCutter(GTCXMaterial.Iron), "PFP", "P P", "RRR", 'P', "plateIron", 'F', file, 'R', "rodIron");
        }
    }

    static void initOverrideVanillaRecipes(){
        ForgeRegistry registry = (ForgeRegistry) ForgeRegistries.RECIPES;
        registry.remove(new ResourceLocation("minecraft", "iron_bars"));
        registry.remove(new ResourceLocation("minecraft", "hopper"));
        registry.remove(new ResourceLocation("quark", "hopper"));
        registry.remove(new ResourceLocation("minecraft", "flint_and_steel"));
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.hopper_-82413824");
        if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
            recipes.addRecipe(GTMaterialGen.get(Blocks.IRON_BARS, 8), " W ", "RRR", "RRR", 'R', "rodIron", 'W', "craftingToolWrench");
        } else {
            recipes.addRecipe(GTMaterialGen.get(Blocks.IRON_BARS, 8), "RRR", "RRR", 'R', "rodIron");
        }
        IRecipeInput material = combineRecipeObjects(BRONZE, ALUMINIUM, ELECTRUM, PLATINUM, PRE + "Nickel", REFINED_IRON, PRE + "Silver", PRE + "Iron");
        IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? input("craftingToolWrench") : null;
        int recipeID = STEEL_MODE ? -305222786 : -156474894;
        GTRecipeCraftingHandler.overrideGTRecipe("gtclassic", "shaped_tile.hopper_" + recipeID, GTMaterialGen.get(Blocks.HOPPER), "IWI", "ICI", " I ", 'I', material, 'W', wrench, 'C', CHEST_WOOD);
        recipes.addShapelessRecipe(GTMaterialGen.get(Items.FLINT_AND_STEEL), "ingotSteel", Items.FLINT);
    }

    static void initWoodRecipes(){
        ForgeRegistry registry = (ForgeRegistry) ForgeRegistries.RECIPES;
        if (GTCXConfiguration.general.harderWood){
            GTCExpansion.logger.info("Initializing wood overrides");
            registry.remove(new ResourceLocation("minecraft", "stick"));
            registry.remove(new ResourceLocation("minecraft", "oak_planks"));
            registry.remove(new ResourceLocation("minecraft", "spruce_planks"));
            registry.remove(new ResourceLocation("minecraft", "birch_planks"));
            registry.remove(new ResourceLocation("minecraft", "jungle_planks"));
            registry.remove(new ResourceLocation("minecraft", "acacia_planks"));
            registry.remove(new ResourceLocation("minecraft", "dark_oak_planks"));
            recipes.addRecipe(GTMaterialGen.get(Items.STICK, 2), "P", "P", 'P', "plankWood");
            if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
                recipes.addRecipe(GTMaterialGen.get(Items.STICK, 4), "S", "P", "P", 'S', "craftingToolSaw", 'P', "plankWood");
            }
            List<IRecipe> recipeList = new ArrayList<>();

            for (IRecipe recipe : ForgeRegistries.RECIPES){
                if (GTHelperStack.matchOreDict(recipe.getRecipeOutput(), "plankWood") && recipe.getIngredients().size() == 1){
                    for (ItemStack stack : recipe.getIngredients().get(0).getMatchingStacks()){
                        if (GTHelperStack.matchOreDict(stack, "logWood")){
                            recipeList.add(recipe);
                            break;
                        }
                    }
                }
            }
            for (IRecipe recipe : recipeList){
                registry.remove(recipe.getRegistryName());
                recipes.addRecipe(StackUtil.copyWithSize(recipe.getRecipeOutput(), (recipe.getRecipeOutput().getCount() / 2)), "W", 'W', new GTCXRecipeInputIngredient(recipe.getIngredients().get(0)));
                if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
                    recipes.addRecipe(StackUtil.copyWithSize(recipe.getRecipeOutput(), (recipe.getRecipeOutput().getCount())), "S", "W", 'S', "craftingToolSaw", 'W', new GTCXRecipeInputIngredient(recipe.getIngredients().get(0)));
                }
            }
            recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 2, 0), "W", 'W', GTMaterialGen.get(Blocks.LOG, 1, 0));
            recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 2, 1), "W", 'W', GTMaterialGen.get(Blocks.LOG, 1, 1));
            recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 2, 2), "W", 'W', GTMaterialGen.get(Blocks.LOG, 1, 2));
            recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 2, 3), "W", 'W', GTMaterialGen.get(Blocks.LOG, 1, 3));
            recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 2, 4), "W", 'W', GTMaterialGen.get(Blocks.LOG2, 1, 0));
            recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 2, 5), "W", 'W', GTMaterialGen.get(Blocks.LOG2, 1, 1));
            if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
                recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 4, 0), "S", "W", 'S', "craftingToolSaw", 'W', GTMaterialGen.get(Blocks.LOG, 1, 0));
                recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 4, 1), "S", "W", 'S', "craftingToolSaw", 'W', GTMaterialGen.get(Blocks.LOG, 1, 1));
                recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 4, 2), "S", "W", 'S', "craftingToolSaw", 'W', GTMaterialGen.get(Blocks.LOG, 1, 2));
                recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 4, 3), "S", "W", 'S', "craftingToolSaw", 'W', GTMaterialGen.get(Blocks.LOG, 1, 3));
                recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 4, 4), "S", "W", 'S', "craftingToolSaw", 'W', GTMaterialGen.get(Blocks.LOG2, 1, 0));
                recipes.addRecipe(GTMaterialGen.get(Blocks.PLANKS, 4, 5), "S", "W", 'S', "craftingToolSaw", 'W', GTMaterialGen.get(Blocks.LOG2, 1, 1));
            }
        }
    }

    static void initShapedBlockRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.assemblingMachine), "CPC", "SMS", "CSC", 'C', CIRCUIT_BASIC, 'P', Blocks.PISTON, 'S', MATERIAL_STEELS, 'M', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.chemicalReactor), "PMP", "CcC", "PEP", 'P', MATERIAL_INVAR_ALUMINIUM, 'M', Ic2Items.magnetizer, 'C', CIRCUIT_ADVANCED, 'c', Ic2Items.compressor, 'E', Ic2Items.extractor);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.distillationTower), "CEC", "PAP", "eEe", 'C', GTBlocks.tileCentrifuge, 'E', "circuitMaster", 'P', PUMP, 'A', "machineBlockHighlyAdvanced", 'e', GTCXBlocks.electrolyzer);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.implosionCompressor), "AMA", "cCc", "AMA", 'A', Ic2Items.advancedAlloy.copy(), 'M', MACHINE_ADV, 'c', CIRCUIT_BASIC, 'C', Ic2Items.compressor.copy());
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.electrolyzer), "IEI", "AeA", "IEI", 'I', MATERIAL_STEELS, 'E', Ic2Items.extractor.copy(), 'A', CIRCUIT_ADVANCED, 'e', Ic2Items.magnetizer.copy());
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.vacuumFreezer), "IPI", "AGA", "IPI", 'I', STAINLESS_STEEL, 'P', PUMP, 'A', CIRCUIT_ADVANCED, 'G', REINFORCED_GLASS);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.alloySmelter), "IHI", "CFC", "IMI", 'I', INVAR, 'H', GTCXItems.constantanHeatingCoil, 'C', CIRCUIT_BASIC, 'F', Ic2Items.electroFurnace.copy(), 'M', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.industrialGrinder), "ECP", "GGG", "CMC", 'E', GTCXBlocks.electrolyzer, 'C', CIRCUIT_ADVANCED, 'P', PUMP, 'G', GRINDER, 'M', MACHINE_ADV);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.industrialBlastFurnace), "CcC", "cMc", "FcF", 'C', CIRCUIT_BASIC, 'c', GTCXItems.constantanHeatingCoil, 'M', MACHINE_ADV, 'F', Ic2Items.inductionFurnace);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.plateBender), "PCP", "cMc", "PCP", 'P', Blocks.PISTON, 'C', CIRCUIT_BASIC, 'c', Ic2Items.compressor, 'M', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.wiremill), "PDP", "CMC", "PcP", 'P', BRASS, 'D', GEM_DIAMOND, 'C', CIRCUIT_BASIC, 'M', MACHINE_BASIC, 'c', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.lathe), "PCP", "GMG", "PmP", 'P', MATERIAL_STEELS, 'C', CIRCUIT_ADVANCED, 'G', "gearSteel", 'M', GTCXItems.conveyorModule, 'm', MACHINE_BASIC);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.microwave), "AAA", "L M", "AAA", 'A', ALUMINIUM, 'L', LEAD, 'M', Ic2Items.magnetizer);
        IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? input("craftingToolWrench") : null;
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.bath), "SSS", "SWS", "SGS", 'S', STAINLESS_STEEL, 'W', MACHINE_BASIC, 'G', "paneGlass");
        if (GTCXConfiguration.general.overrideIc2cSawmill){
            recipes.overrideRecipe("shaped_tile.blocksawmill_-1444206344", GTMaterialGen.get(GTCXBlocks.industrialSawmill), "PCP", "DDD", "CMC", 'P', PUMP, 'C', CIRCUIT_ADVANCED, 'D', GTMaterialGen.get(GTCXItems.diamondSawblade), 'M', MACHINE_BASIC);
        } else {
            recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.industrialSawmill), "PCP", "DDD", "CMC", 'P', Ic2Items.pump, 'C', CIRCUIT_ADVANCED, 'D', GTMaterialGen.get(GTCXItems.diamondSawblade), 'M', MACHINE_BASIC);
        }
        IRecipeInput materialStainlessTitatium = combineRecipeObjects(STAINLESS_STEEL, TITANIUM);
        //IRecipeInput pipe = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.getFluidPipe(GTMaterial.Titanium, 1)), new RecipeInputItemStack(GTMaterialGen.getFluidPipe(GTCXMaterial.StainlessSteel, 1)));
        ItemStack pipe = GTCXPipes.getPipe(GTCXMaterial.StainlessSteel, GTCXHelperPipe.GTPipeModel.MED, 1);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fluidCaster), "IcI", "PMP", "ICI", 'I', materialStainlessTitatium, 'c', GTCXItems.mold, 'P', pipe, 'M', "machineBlockVeryAdvanced", 'C', "circuitElite");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fluidSmelter), "IbI", "PMP", "BCB", 'I', materialStainlessTitatium, 'c', GTCXBlocks.industrialBlastFurnace, 'P', pipe, 'M', "machineBlockVeryAdvanced", 'C', "circuitElite", 'B', Blocks.BRICK_BLOCK);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.primitiveBlastFurnace), "BBB", "BPB", "BBB", 'B', GTCXBlocks.fireBrickBlock, 'P', "plateIron");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.alloyFurnace), "CCC", "FHF", "CCC", 'C', Blocks.BRICK_BLOCK, 'F', Blocks.FURNACE, 'H', Blocks.HOPPER);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.casingStandard, 4), "III", "CBC", "III", 'I', combineRecipeObjects(REFINED_IRON, ALUMINIUM), 'C', CIRCUIT_BASIC, 'B', MACHINE_CHEAP);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.casingReinforced, 4), "III", "CMC", "III", 'I', MATERIAL_STEELS, 'C', CIRCUIT_ADVANCED, 'M', MACHINE_BASIC);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.casingAdvanced, 4), "III", "CBC", "III", 'I', CHROME, 'C', CIRCUIT_DATA, 'B', MACHINE_ELITE);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fireBrickBlock), "BB", "BB", 'B', GTCXItems.fireBrick);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.dieselGenerator), "PPP", "P P", "CGC", 'P', MATERIAL_MACHINE, 'C', CIRCUIT_BASIC, 'G', Ic2Items.generator);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.gasTurbine), "PCP", "WGW", "PCP", 'P', MATERIAL_INVAR_ALUMINIUM, 'C', CIRCUIT_ADVANCED, 'W', Ic2Items.windMill, 'G', REINFORCED_GLASS);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.thermalBoiler), "TcT", "GCG", "TcT", 'T', Ic2Items.thermalGenerator, 'c', GTBlocks.tileCentrifuge, 'G', combineRecipeObjects("gearTitanium", "gearTungstensteel"), 'C', CIRCUIT_MASTER);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.largeSteamTurbine), "GGG", "gMg", "GCG", 'G', Ic2Items.basicTurbine.copy(), 'g', combineRecipeObjects("gearSteel", "gearStainlessSteel"), 'M', MACHINE_BASIC, 'C', CIRCUIT_ADVANCED);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.largeGasTurbine), "GGG", "gMg", "GCG", 'G', GTCXBlocks.gasTurbine, 'g', combineRecipeObjects("gearTitanium", "gearTungstensteel"), 'M', MACHINE_VERY_ADV, 'C', CIRCUIT_MASTER);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.extruder), "PGP", "NMD", "PCP", 'P', combineRecipeObjects(TITANIUM, TUNGSTEN_STEEL), 'G', combineRecipeObjects("gearTitanium", "gearTungstensteel"), 'N', GTCXItems.nichromeHeatingCoil, 'M', MACHINE_VERY_ADV, 'D', GTCXItems.diamondSawblade, 'C', CIRCUIT_ELITE);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.plateCutter), "PCP", "GDG", "PMP", 'P', MATERIAL_STEELS, 'C', CIRCUIT_ADVANCED, 'G', combineRecipeObjects("gearSteel", "gearStainlessSteel"), 'D', GTCXItems.diamondSawblade, 'M', MACHINE_BASIC);
        IRecipeInput aluiron = combineRecipeObjects( REFINED_IRON, ALUMINIUM);
        IRecipeInput rodAluiron = combineRecipeObjects( "rodAluminium", "rodAluminum", "rodRefinedIron");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.locker), "RLR", "LCL", "PMP", colorTransfer(GTMaterialGen.get(GTBlocks.tileCabinet)), 'R', rodAluiron, 'L', Items.LEATHER, 'C', GTBlocks.tileCabinet, 'P', aluiron, 'M', MACHINE_CHEAP);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.electricLocker), "SLS", "SlS", "SCS", colorTransfer(GTMaterialGen.get(GTCXBlocks.locker)), 'S', MATERIAL_STEELS, 'L', Ic2Items.lapotronCrystal, 'l', GTCXBlocks.locker, 'C', CIRCUIT_ADVANCED);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.advancedWorktable), "EOE", "EWE", "ECE", colorTransfer(GTMaterialGen.get(GTBlocks.tileWorktable)), 'E', ELECTRUM, 'O', TIER_2_ENERGY, 'W', GTBlocks.tileWorktable, 'C', CIRCUIT_ADVANCED);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.dustBin), "IHI", "IWI", "IHI", colorTransfer(GTMaterialGen.get(GTBlocks.tileWorktable)), 'I', MATERIAL_REFINED_IRON, 'H', Blocks.HOPPER, 'W', GTBlocks.tileWorktable);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.trashBin), "I I", "ILI", "III", 'I', MATERIAL_REFINED_IRON, 'L', GTMaterialGen.getFluidStack("lava", 1000));
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fusionMaterialInjector), "PcP", "CAC", "PCP", 'P', PUMP, 'c', CHEST_WOOD, 'C', CIRCUIT_MASTER, 'A', GTBlocks.casingHighlyAdvanced);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fusionMaterialExtractor), "PCP", "CAC", "PcP", 'P', PUMP, 'c', CHEST_WOOD, 'C', CIRCUIT_MASTER, 'A', GTBlocks.casingHighlyAdvanced);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fusionEnergyInjector), "SCS", "CsC", "SCS", 'S', GTItems.superConductor, 'C', CIRCUIT_MASTER, 's', GTBlocks.tileSupercondensator);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fusionEnergyExtractor), "CSC", "SsS", "CSC", 'S', GTItems.superConductor, 'C', CIRCUIT_MASTER, 's', GTBlocks.tileSupercondensator);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.inputHatch), "SCS", "GHG", "SSS", 'S', STAINLESS_STEEL, 'C', "chest", 'G', combineRecipeObjects("gearSteel", "gearStainlessSteel"), 'H', MACHINE_BASIC);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.outputHatch), "SSS", "GHG", "SCS", 'S', STAINLESS_STEEL, 'C', "chest", 'G', combineRecipeObjects("gearSteel", "gearStainlessSteel"), 'H', MACHINE_BASIC);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.dynamoHatch), "SSS", "GHG", "SCS", 'S', STAINLESS_STEEL, 'C', Ic2Items.transformerHV, 'G', combineRecipeObjects("gearSteel", "gearStainlessSteel"), 'H', MACHINE_BASIC);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.machineControlHatch), "SSS", "GHG", "SCS", 'S', STAINLESS_STEEL, 'C', Ic2Items.redstoneSUpgrade, 'G', combineRecipeObjects("gearSteel", "gearStainlessSteel"), 'H', MACHINE_BASIC);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.digitalTank), "SSS", "SDS", "SCS", 'S', STAINLESS_STEEL, 'D', GTItems.orbData, 'C', GTCXItems.computerMonitor);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.cokeOven), "FFF", "FCF", "FFF", 'F', GTCXBlocks.fireBrickBlock, 'C', GTBlocks.tileCharcoalPit);
        ItemStack cable = GTMaterialGen.get(GTCXBlocks.electrumCable);
        IRecipeInput rubber = combineRecipeObjects("itemRubber", "craftingToolDuctTape");
        if (GTCXConfiguration.general.plateCableRecipes && !GTCXConfiguration.general.gt2Mode){
            recipes.addShapelessRecipe(GTMaterialGen.getIc2(cable.copy(), 4), "plateElectrum", "craftingToolWireCutter");
        } else {
            recipes.addRecipe(GTMaterialGen.getIc2(cable.copy(), 12), "EEE", 'E', INGOT_ELECTRUM);
            recipes.addRecipe(GTMaterialGen.getIc2(cableWithInsulationTag(cable.copy(), 1), 4),  " R ", "RER", " R ", 'R', "itemRubber", 'E', INGOT_ELECTRUM);
            recipes.addRecipe(GTMaterialGen.getIc2(cableWithInsulationTag(cable.copy(), 2), 4),  "RRR", "RER", "RRR", 'R', "itemRubber", 'E', INGOT_ELECTRUM);
        }
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 1), insulationSetting(cable, 0, 1), cable, rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 2), insulationSetting(cable, 1, 2), cableWithInsulationTag(cable.copy(), 1), rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 2), insulationSetting(cable, 0, 2), cable, rubber, rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 3), insulationSetting(cable, 0, 3), cable, rubber, rubber, rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 3), insulationSetting(cable, 1, 3), cableWithInsulationTag(cable.copy(), 1), rubber, rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 3), insulationSetting(cable, 2, 3), cableWithInsulationTag(cable.copy(), 2), rubber);
        cable = GTMaterialGen.get(GTCXBlocks.aluminiumCable);
        if (GTCXConfiguration.general.plateCableRecipes && !GTCXConfiguration.general.gt2Mode){
            recipes.addShapelessRecipe(GTMaterialGen.getIc2(cable.copy(), 4), "plateAluminium", "craftingToolWireCutter");
        } else {
            recipes.addRecipe(GTMaterialGen.getIc2(cable.copy(), 12), "EEE", 'E', INGOT_ALUMINIUM);
            recipes.addRecipe(GTMaterialGen.getIc2(cableWithInsulationTag(cable.copy(), 1), 4),  " R ", "RER", " R ", 'R', "itemRubber", 'E', INGOT_ALUMINIUM);
            recipes.addRecipe(GTMaterialGen.getIc2(cableWithInsulationTag(cable.copy(), 2), 4),  "RRR", "RER", "RRR", 'R', "itemRubber", 'E', INGOT_ALUMINIUM);
        }
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 1), insulationSetting(cable, 0, 1), cable, rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 2), insulationSetting(cable, 1, 2), cableWithInsulationTag(cable.copy(), 1), rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 2), insulationSetting(cable, 0, 2), cable, rubber, rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 3), insulationSetting(cable, 0, 3), cable, rubber, rubber, rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 3), insulationSetting(cable, 1, 3), cableWithInsulationTag(cable.copy(), 1), rubber, rubber);
        recipes.addShapelessRecipe(cableWithInsulationTag(cable.copy(), 3), insulationSetting(cable, 2, 3), cableWithInsulationTag(cable.copy(), 2), rubber);
    }

    static void initPreElectric(){
        if (!GTCXConfiguration.general.gt2Mode){
            if (GTCXConfiguration.general.enableSteamMachines){
                IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools ? input("craftingToolWrench") : null;
                IRecipeInput hammer = GTCXConfiguration.general.enableCraftingTools ? input("craftingToolForgeHammer") : null;
                recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.coalBoiler), "PPP", "PWP", "BFB", 'P', BRONZE, 'W', wrench, 'B', Blocks.BRICK_BLOCK, 'F', Blocks.FURNACE);
                IRecipeInput gem = GTConfig.general.harderIC2Macerator ? input("gemDiamond", 3) : input(GTMaterialGen.get(Items.FLINT, 3));
                recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.steamMacerator), "WDH", "GMG", "BPB", 'W', wrench, 'D', gem, 'H', hammer, 'G', "gearBronze", 'M', MACHINE_CHEAP, 'B', BRONZE, 'P', ANY_PISTON);
                recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.steamFurnace), "BWB", "BFB", "bMb", 'B', BRONZE, 'W', wrench, 'F', Blocks.FURNACE, 'b', Blocks.BRICK_BLOCK, 'M', MACHINE_CHEAP);
                recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.steamAlloySmelter), "BBB", "FWF", "bbb", 'B', BRONZE, 'F', GTCXBlocks.steamFurnace, 'W', wrench, 'b', Blocks.BRICK_BLOCK, 'M', MACHINE_CHEAP);
                recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.steamForgeHammer), "GPG", "BWB", "BMB", 'G', "gearBronze", 'P', ANY_PISTON, 'B', BRONZE, 'W', wrench, 'M', MACHINE_CHEAP);
                recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.steamCompressor), "BGB", "PWP", "BMB", 'G', "gearBronze", 'P', ANY_PISTON, 'B', BRONZE, 'W', wrench, 'M', MACHINE_CHEAP);
                recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.steamExtractor), "BBB", "PWP", "BMB", 'P', ANY_PISTON, 'B', BRONZE, 'W', wrench, 'M', MACHINE_CHEAP);
                removeRecipe("ic2", "shaped_tile.blockStoneMacerator_-130868445");
                IRecipeInput advancedAlloy = GTCXConfiguration.general.forcePreElectricMachines ? input(Ic2Items.advancedAlloy.copy()) : MATERIAL_STEELS;
                recipes.addRecipe(Ic2Items.macerator, "AAA", "SMS", "SCS", 'A', advancedAlloy, 'S', MATERIAL_STEELS, 'M', GTCXBlocks.steamMacerator, 'C', CIRCUIT_BASIC);
                recipes.addRecipe(Ic2Items.compressor, "AAA", "SMS", "SCS", 'A', advancedAlloy, 'S', MATERIAL_STEELS, 'M', GTCXBlocks.steamCompressor, 'C', CIRCUIT_BASIC);
                recipes.addRecipe(Ic2Items.extractor, "AAA", "SMS", "SCS", 'A', advancedAlloy, 'S', MATERIAL_STEELS, 'M', GTCXBlocks.steamExtractor, 'C', CIRCUIT_BASIC);
                recipes.addRecipe(Ic2Items.electroFurnace, "AAA", "SMS", 'A', advancedAlloy, "SCS", 'S', MATERIAL_STEELS, 'M', GTCXBlocks.steamFurnace, 'C', CIRCUIT_BASIC);
            } else {
                if (GTConfig.general.harderIC2Macerator){
                    recipes.overrideRecipe("shaped_tile.blockStoneMacerator_-130868445", Ic2Items.stoneMacerator.copy(), "FDF", "DPD", "FBF", 'D', GEM_DIAMOND, 'F', Items.FLINT, 'P', Blocks.PISTON, 'B',
                            Blocks.FURNACE);
                }
                recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.stoneCompressor), "SHS", "SFS", "SPS", 'S', "stone", 'H', Blocks.HOPPER, 'F', Blocks.FURNACE, 'P', ANY_PISTON);
                recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.stoneExtractor), " H ", "TFT", "TPT", 'T', Ic2Items.treeTap, 'H', Blocks.HOPPER, 'F', Blocks.FURNACE, 'P', ANY_PISTON);
                recipes.addRecipe(Ic2Items.compressor.copy(), "III", "IMI", "ICI", 'I', MATERIAL_STEELS, 'M', GTCXBlocks.stoneCompressor, 'C',
                        CIRCUIT_BASIC);
                recipes.addRecipe(Ic2Items.extractor.copy(), "III", "IMI", "ICI", 'I', MATERIAL_STEELS, 'M', GTCXBlocks.stoneExtractor, 'C',
                        CIRCUIT_BASIC);
            }
        }

    }

    public static ItemStack cableWithInsulationTag(ItemStack stack, int insulation){
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        nbt.setInteger(GTCXBlockWire.NBT_INSULATION, insulation);
        return stack;
    }

    static void initShapelessRecipes(){
        recipes.addShapelessRecipe(GTMaterialGen.get(Items.GUNPOWDER, 3), "dustCoal", "dustSulfur", "dustSaltpeter", "dustSaltpeter");
        recipes.addShapelessRecipe(GTMaterialGen.get(Items.GUNPOWDER, 2), "dustCharcoal", "dustSulfur", "dustSaltpeter", "dustSaltpeter");
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 3), Ic2Items.fertilizer, "dustSulfur", GTMaterialGen.getTube(GTMaterial.Calcium, 1));
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 2), Ic2Items.fertilizer, "dustAshes", "dustAshes", "dustAshes");
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 2), Ic2Items.fertilizer, "dustDarkAshes");

        recipes.addShapelessRecipe(Ic2Items.reactorPlatingExplosive, Ic2Items.reactorPlating, LEAD);
        recipes.addShapelessRecipe(GTMaterialGen.get(GTCXItems.fireClayBall, 2), Items.CLAY_BALL, "sand", "dustFlint", GTMaterialGen.getFluidStack("water", 1000));
        IRecipeInput ashes = combineRecipeObjects( "dustAshes", "dustAsh");
        recipes.addShapelessRecipe(new ItemStack(Items.IRON_INGOT, 1), "ingotRefinedIron", ashes);
        recipes.addShapelessRecipe(GTMaterialGen.get(GTCXItems.magicDye), "dyeCyan", "dyeMagenta", "dyeYellow", "dyeBlack");
        recipes.addShapelessRecipe(GTMaterialGen.get(GTCXItems.coalCoke, 9), "blockCoke");
    }

    static void initUranaiumRodOverrides(){
        overrideGTRecipe("shaped_item.gtclassic.rod_thorium_double_-1641330943", GTMaterialGen.get(GTItems.rodThorium2), "RCR", gtcxTooltip(),
        'R', GTItems.rodThorium1, 'C', COPPER);
        overrideGTRecipe("shaped_item.gtclassic.rod_thorium_quad_1590232849", GTMaterialGen.get(GTItems.rodThorium4), " R ","CCC", gtcxTooltip(), " R ", 'R', GTItems.rodThorium2, 'C', COPPER);
        overrideGTRecipe("shaped_item.gtclassic.rod_thorium_quad_1727480257", GTMaterialGen.get(GTItems.rodThorium4), "RCR", "CCC", gtcxTooltip(), "RCR", 'R', GTItems.rodThorium1, 'C', COPPER);
        overrideGTRecipe("shaped_item.gtclassic.rod_plutonium_double_-695190114", GTMaterialGen.get(GTItems.rodPlutonium2), "RCR", gtcxTooltip(), 'R', GTItems.rodPlutonium1, 'C', COPPER);
        overrideGTRecipe("shaped_item.gtclassic.rod_plutonium_quad_1932443438", GTMaterialGen.get(GTItems.rodPlutonium4), " R ","CCC", " R ", gtcxTooltip(), 'R', GTItems.rodPlutonium2, 'C', COPPER);
        overrideGTRecipe("shaped_item.gtclassic.rod_plutonium_quad_1460535640", GTMaterialGen.get(GTItems.rodPlutonium4), "RCR", "CCC", "RCR", gtcxTooltip(), 'R', GTItems.rodPlutonium1, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumdual_-755015514", Ic2Items.reactorUraniumRodDual, "RCR", 'R', Ic2Items.reactorUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumquad_1936822515", Ic2Items.reactorUraniumRodQuad, " R ","CCC", " R ", 'R', Ic2Items.reactorUraniumRodDual, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumquad_545094699", Ic2Items.reactorUraniumRodQuad, "RCR", "CCC", "RCR", 'R', Ic2Items.reactorUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumredstonedual_-752241975", Ic2Items.reactorRedstoneUraniumRodDual, "RCR", 'R', Ic2Items.reactorRedstoneUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumredstonequad_304369238", Ic2Items.reactorRedstoneUraniumRodQuad, " R ","CCC", " R ", 'R', Ic2Items.reactorRedstoneUraniumRodDual, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumredstonequad_-1675414636", Ic2Items.reactorRedstoneUraniumRodQuad, "RCR", "CCC", "RCR", 'R', Ic2Items.reactorRedstoneUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumblazedual_-749468436", Ic2Items.reactorBlazeUraniumRodDual, "RCR", 'R', Ic2Items.reactorBlazeUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumblazequad_-1328084039", Ic2Items.reactorBlazeUraniumRodQuad, " R ","CCC", " R ", 'R', Ic2Items.reactorBlazeUraniumRodDual, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumblazequad_399043325", Ic2Items.reactorBlazeUraniumRodQuad, "RCR", "CCC", "RCR", 'R', Ic2Items.reactorBlazeUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumenderpearldual_-746694897", Ic2Items.reactorEnderPearlUraniumRodDual, "RCR", 'R', Ic2Items.reactorEnderPearlUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumenderpearlquad_1334429980", Ic2Items.reactorEnderPearlUraniumRodQuad, " R ","CCC", " R ", 'R', Ic2Items.reactorEnderPearlUraniumRodDual, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumenderpearlquad_-1821466010", Ic2Items.reactorEnderPearlUraniumRodQuad, "RCR", "CCC", "RCR", 'R', Ic2Items.reactorEnderPearlUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumnetherstardual_-743921358", Ic2Items.reactorNetherStarUraniumRodDual, "RCR", 'R', Ic2Items.reactorNetherStarUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumnetherstarquad_-298023297", Ic2Items.reactorNetherStarUraniumRodQuad, " R ","CCC", " R ", 'R', Ic2Items.reactorNetherStarUraniumRodDual, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumnetherstarquad_252991951", Ic2Items.reactorNetherStarUraniumRodQuad, "RCR", "CCC", "RCR", 'R', Ic2Items.reactorNetherStarUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumcharcoaldual_-741147819", Ic2Items.reactorCharcoalUraniumRodDual, "RCR", 'R', Ic2Items.reactorCharcoalUraniumRodSingle, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumcharcoalquad_-1930476574", Ic2Items.reactorCharcoalUraniumRodQuad, " R ","CCC", " R ", 'R', Ic2Items.reactorCharcoalUraniumRodDual, 'C', COPPER);
        recipes.overrideRecipe("shaped_item.reactoruraniumcharcoalquad_-1967517384", Ic2Items.reactorCharcoalUraniumRodQuad, "RCR", "CCC", "RCR", 'R', Ic2Items.reactorCharcoalUraniumRodSingle, 'C', COPPER);
    }

    static void initIc2(){
        initUranaiumRodOverrides();
        ItemStack cable = GTMaterialGen.get(GTCXBlocks.aluminiumCable, 1);
        recipes.addRecipe(Ic2Items.detectorCable.copy(), " C ", "RcR", " R ",  hasEnoughInsulation(cable.copy(), 3), 'C', CIRCUIT_BASIC, 'R', "dustRedstone", 'c', cableWithInsulationTag(cable.copy(), 3));
        recipes.addRecipe(Ic2Items.splitterCable.copy(), " R ", "cLc", " R ",  hasEnoughInsulation(cable.copy(), 3), 'L', Blocks.LEVER, 'R', "dustRedstone", 'c', cableWithInsulationTag(cable.copy(), 3));
        recipes.addRecipe(Ic2Items.transformerHV.copy(), " c ", "CTE", " c ",  hasEnoughInsulation(cable.copy(), 3), 'C', CIRCUIT_BASIC, 'T', Ic2Items.transformerMV, 'E', Ic2Items.energyCrystal, 'c', cableWithInsulationTag(cable.copy(), 3));
        recipes.overrideRecipe("shaped_item.itempartiridium_1100834802", GTMaterialGen.get(GTCXItems.iridiumAlloyIngot), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'A', Ic2Items.advancedAlloy, 'D', "dustDiamond");
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FFF", "TTT", "FFF", 'F', "dustFlint", 'T', Blocks.TNT);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FTF", "FTF", "FTF", 'F', "dustFlint", 'T', Blocks.TNT);
        if (IC2.config.getFlag("CraftingNuke")){
            recipes.overrideRecipe("shaped_tile.blocknuke_-814805840", Ic2Items.nuke, "UCU", "BAB", "UCU", 'U', Ic2Items.reactorReEnrichedUraniumRod, 'C', CIRCUIT_ADVANCED, 'B', "blockUranium", 'A', MACHINE_ADV);
        }
        if (GTCXConfiguration.general.plateCableRecipes && !GTCXConfiguration.general.gt2Mode){
            recipes.overrideRecipe("shaped_item.itemcable_-895690168", GTMaterialGen.getIc2(Ic2Items.copperCable, 2), "PW", 'P', "plateCopper", 'W', "craftingToolWireCutter");
            recipes.overrideRecipe("shaped_item.itemgoldcable_-121137345", GTMaterialGen.getIc2(Ic2Items.goldCable, 4), "PW", 'P', "plateGold", 'W', "craftingToolWireCutter");
            int recipeId = STEEL_MODE ? -1596711841 : 1314416875;
            recipes.overrideRecipe("shaped_item.itemironcable_" + recipeId, GTMaterialGen.getIc2(Ic2Items.ironCable, 4), "PW", 'P', getRefinedIronPlate(), 'W', "craftingToolWireCutter");
            recipes.overrideRecipe("shaped_item.itemtincable_1475909484", GTMaterialGen.getIc2(Ic2Items.tinCable, 3), "PW", 'P', "plateTin", 'W', "craftingToolWireCutter");
            recipes.overrideRecipe("shaped_item.itembronzecable_1006731162", GTMaterialGen.getIc2(Ic2Items.bronzeCable, 2), "PW", 'P', "plateBronze", 'W', "craftingToolWireCutter");
            removeRecipe("ic2", "shaped_item.itemcablei_1114825827");
            removeRecipe("ic2", "shaped_item.itemcablei_156664931");
            removeRecipe("ic2", "shaped_item.itemgoldcablei_605816287");
            removeRecipe("ic2", "shaped_item.itemgoldcableii_1066149022");
            recipeId = STEEL_MODE ? 1790240415 : 926773675;
            removeRecipe("ic2", "shaped_item.itemironcablei_" + recipeId);
            recipeId = STEEL_MODE ? 1131313310 : 268464298;
            removeRecipe("ic2", "shaped_item.itemironcableii_" + recipeId);
            removeRecipe("ic2", "shaped_item.itembronzecablei_-251775529");
            removeRecipe("ic2", "shaped_item.itembronzecablei_-1648233001");
            removeRecipe("ic2", "shaped_item.itembronzecableii_-1797458595");
        }
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.itemingotadviron_845672146");
        if (GTConfig.general.harderIC2Macerator) {
            int recipeId = STEEL_MODE ? 2144549784 : 127744036;
            recipes.overrideRecipe("shaped_tile.blockMacerator_" + recipeId, Ic2Items.macerator.copy(), "III", "IMI", "ICI", 'I', MATERIAL_STEELS, 'M', Ic2Items.stoneMacerator.copy(), 'C',
                    CIRCUIT_ADVANCED);
            recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), "FDF", "DMD", "FCF", 'D', GEM_DIAMOND, 'F', MATERIAL_STEELS, 'M', MACHINE_BASIC, 'C',
                    CIRCUIT_ADVANCED);
            //recipes.addRecipe(Ic2Items.macerator.copy(), "FGF", "CMC", "FCF", 'G', GRINDER, 'F', MATERIAL_STEELS, 'M', MACHINE_BASIC, 'C',
            //        CIRCUIT_BASIC);
            recipes.overrideRecipe("shaped_tile.blockrotary_-1598189826", Ic2Items.rotaryMacerator, "SGS", "GMG", "SAS", 'G', GRINDER, 'M', Ic2Items.macerator, 'A', MACHINE_ADV, 'S', STAINLESS_STEEL);
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockrotary_-1360333248");
        }
        int recipeId = STEEL_MODE ? -1329500063 : 241486317;
        recipes.overrideRecipe("shaped_item.itemtoolcutter_" + recipeId, GTMaterialGen.get(GTCXItems.cutter), "R R", " R ", "I I", 'R', MATERIAL_REFINED_IRON, 'I', "ingotIron");
        recipes.overrideRecipe("shaped_item.itemplasmacore_-1985082214", Ic2Items.plasmaCore, "CSC", "SPS", "CSC", 'C', GTBlocks.tileSuperconductorCableMAX, 'S', TUNGSTEN, 'P', Ic2Items.plasmaCell);
        recipes.overrideRecipe("shaped_item.itempesd_-912043277", Ic2Items.pesd, "CSC", "SPS", "CSC", 'C', GTItems.orbEnergy, 'S', TUNGSTEN, 'P', Ic2Items.plasmaCore);
        recipes.overrideRecipe("shaped_item.itemportableteleporter_-869928001", Ic2Items.portableTeleporter, "ADA", "ACA", "ETE", 'A', PLATINUM, 'D', CIRCUIT_DATA, 'C', CIRCUIT_MASTER, 'E', GTItems.orbEnergy, 'T', Ic2Items.teleporter);
        String technetium = GTCXConfiguration.general.usePlates ? "plateTechnetium" : "ingotTechnetium";
        recipes.overrideRecipe("shaped_item.quantumoverclockerupgrade_-1387578587", Ic2Items.quantumOverclockerUpgrade, "THT", "HOH", "TST", 'T', technetium, 'H', GTItems.heatStorageHelium6, 'O', Ic2Items.overClockerUpgrade, 'S', GTItems.superConductor);
        recipes.overrideRecipe("shaped_tile.blockpesu_281205134", Ic2Items.pesu, "SCS", "PPP", "SCS", 'S', TUNGSTEN, 'C', CIRCUIT_MASTER, 'P', Ic2Items.pesd);
        recipes.overrideRecipe("shaped_tile.blockTransformerIV_1876908464", Ic2Items.transformerIV.copy(), "XYX", "CVB", "XYX", 'X', TUNGSTEN, 'Y', CRAFTING_SUPERCONDUCTOR, 'C', CIRCUIT_MASTER, 'V',
                Ic2Items.transformerEV.copy(), 'B', Ic2Items.pesd);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("Lossless", true);
        ItemStack stack = Ic2Items.precisionWrench.copy();
        stack.setTagCompound(nbt);
        if (!Loader.isModLoaded(MOD_ID_IC2_EXTRAS) || !GTCXIc2cECompat.isOverridingLossyWrench()){
            recipes.overrideRecipe("shaped_item.precisionwrench_-1322002202", stack, "CRC", "SIS", "CWC", 'C', CIRCUIT_ADVANCED, 'R', GTItems.rockCutter, 'S', "rodTungsten", 'I', Ic2Items.iridiumPlate, 'W', Ic2Items.electricWrench);
        }
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.itemPlasmaCable_449044295");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.precisionwrench_-1943783685");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockiridiumstone_-48520064");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockwatergenerator_-2059790844");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockwindgenerator_1669945012");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockwindgenerator_-244136268");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shapeled_item.itempartcarbonfibre_794316583");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockgenerator_234578637");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockgenerator_183901657");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.itemglasscable_-410929364");
        recipes.overrideRecipe("shaped_item.itemglasscable_-542195504", GTMaterialGen.getIc2(Ic2Items.glassFiberCable, 4), "GGG", "RDR", "GGG", 'G', GTCXBlocks.pureGlass, 'R', PRE + "Silver", 'D', GTRecipeCraftingHandler.combineRecipeObjects("gemRuby", "gemSapphire"));
        ItemStack battery = Ic2Items.battery;
        String circuit = CIRCUIT_BASIC;
        String machineBlock = MACHINE_BASIC;
        recipes.overrideRecipe("shaped_tile.blockgenerator_-66857461", Ic2Items.generator, "B", "M", "F", 'B', battery, 'M', machineBlock, 'F', Blocks.FURNACE);
        if (!Loader.isModLoaded(MOD_ID_IC2_EXTRAS) || !GTCXIc2cECompat.isForcingLead()){
            recipes.overrideRecipe("shaped_tile.blockreactorchamber_1490756150", Ic2Items.reactorChamber, " C ", "CMC", " C ", 'C', Ic2Items.denseCopperPlate, 'M', machineBlock);
        }
        recipes.overrideRecipe("shaped_tile.blockextractor_-1404085260", Ic2Items.extractor, "TMT", "TCT", 'T', Ic2Items.treeTap, 'M', machineBlock, 'C', circuit);
        IRecipeInput stone = GTCXConfiguration.general.forcePreElectricMachines && !GTCXConfiguration.general.gt2Mode ? input(Ic2Items.reinforcedStone.copy()) : input("stone");
        recipes.overrideRecipe("shaped_tile.blockcompressor_-1019977500", Ic2Items.compressor, "S S", "SMS", "SCS", 'S', stone, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockcanner_-1437776888", Ic2Items.canner, "TCT", "TMT", "TTT", 'T', TIN, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockelectrolyzer_-502750552", Ic2Items.electrolyzer, "c c", "cCc", "tMt", 'c', Ic2Items.insulatedCopperCable, 't', Ic2Items.emptyCell, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockcropscanner_-1289883511", Ic2Items.cropAnalyzerBlock, " c ", "CMC", 'c', Ic2Items.cropAnalyzer, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockmagnetizer_-465205004", Ic2Items.magnetizer, "RFR", "RMR", "RFR", 'R', DUST_REDSTONE, 'F', Ic2Items.ironFence, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpump_-527344087", Ic2Items.pump, "cCc", "cMc", "PTP", 'c', Ic2Items.emptyCell, 'P', Ic2Items.miningPipe, 'T', Ic2Items.treeTap, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockminer_-59581574", Ic2Items.miner, "CMC", " p ", " p ", 'M', machineBlock, 'C', circuit, 'p', Ic2Items.miningPipe);
        recipes.overrideRecipe("shaped_tile.blockcropmatron_1348153838", Ic2Items.cropmatron, "CcC", "sMs", "sss", 'c', "chest", 's', Ic2Items.cropStick, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blocksoundbeacon_755381740", Ic2Items.soundBeacon, "FcF", "cMc", "BcB", 'F', Ic2Items.frequencyTransmitter, 'c', COPPER, 'M', machineBlock, 'B', battery);
        recipes.overrideRecipe("shaped_tile.blockcroplibrary_1883857081", Ic2Items.cropLibary, "sBs", "LNO", "CMC", 's', Ic2Items.cropStick, 'B', battery, 'L', Ic2Items.luminator, 'N', Ic2Items.carbonBox, 'O', Ic2Items.obscurator, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockmachinebuffer_-989169435", Ic2Items.machineBuffer, " b ", "CTC", " M ", 'b', Ic2Items.upgradeBase, 'T', Ic2Items.toolBox, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockindustrialworktable_2049276174", Ic2Items.industrialWorktable, "HCH", "NcN", "HMH", 'H', Blocks.HOPPER, 'C', input(circuit, 4), 'N', Ic2Items.carbonBox, 'c', new ItemStack(Blocks.CRAFTING_TABLE, 28), 'M', machineBlock);
        if (!GTCXConfiguration.general.overrideIc2cSawmill){
            recipes.overrideRecipe("shaped_tile.blocksawmill_-1444206344", Ic2Items.sawMill, "ABA", "bMb", "bCb", 'A', Items.STONE_AXE, 'B', Ic2Items.turbineBlade, 'b', BRONZE, 'M', machineBlock, 'C', circuit);
        }
        recipes.overrideRecipe("shaped_tile.blockadvmachine_1515831549", Ic2Items.advMachine, " C ", "AMA", " C ", 'C', Ic2Items.carbonPlate, 'A', Ic2Items.advancedAlloy, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockadvmachine_-1920290047", Ic2Items.advMachine, " A ", "CMC", " A ", 'C', Ic2Items.carbonPlate, 'A', Ic2Items.advancedAlloy, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blocktransformermv_-1785545281", Ic2Items.transformerMV, "C", "M", "C", 'C', Ic2Items.doubleInsulatedGoldCable, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blocktransformermv_-1775375979", Ic2Items.transformerMV, "C", "M", "C", 'C', GTMaterialGen.getIc2(Ic2Items.doubleInsulatedBronzeCable, 2), 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpersonaltrader_1344478433", Ic2Items.tradeOMat, "RRR", "cMc", 'R', DUST_REDSTONE, 'c', "chest", 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpersonaltraderfluid_2117013984", Ic2Items.fluidOMat,"GGG", "PMP", 'G', BLOCK_GLASS, 'P', PUMP, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpersonaltraderenergy_-949356331", Ic2Items.energyOMat,"RBR", "cMc", 'R', DUST_REDSTONE, 'B', battery, 'c', Ic2Items.insulatedCopperCable, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpersonalchest_-1405328861", Ic2Items.personalSafe, "C", "M", "I", 'C', circuit, 'M', machineBlock, 'I', "chest");
        recipes.overrideRecipe("shaped_tile.blockpersonalenergystoragebatbox_1253794578", Ic2Items.personalEnergyStorageBatBox, "C", "M", "I", 'C', circuit, 'M', machineBlock, 'I', Ic2Items.batBox);
        recipes.overrideRecipe("shaped_tile.blockpersonalenergystoragemfe_1253218358", Ic2Items.personalEnergyStorageMFE, "C", "M", "I", 'C', circuit, 'M', machineBlock, 'I', Ic2Items.mfe);
        recipes.overrideRecipe("shaped_tile.blockpersonalenergystoragemfsu_1245973306", Ic2Items.personalEnergyStorageMFSU, "C", "M", "I", 'C', circuit, 'M', machineBlock, 'I', Ic2Items.mfsu);
        recipes.overrideRecipe("shaped_item.mufflerupgrade_-77325382", Ic2Items.mufflerUpgrade, "WWW", "WMW", "WWW", 'W', Blocks.WOOL, 'M', machineBlock);
        recipes.overrideRecipe("shaped_item.expcollectorupgrade_-1525635881", Ic2Items.expCollectorUpgrade, "DMD", "CHC", "DBD", 'D', Ic2Items.denseCopperPlate, 'M', machineBlock, 'C', CIRCUIT_ADVANCED, 'H', Blocks.HOPPER, 'B', Items.GLASS_BOTTLE);
        recipes.overrideRecipe("shaped_item.sideaccessupgrade_-607927002", Ic2Items.sideAccessUpgrade, " T ", "TMT", " T ", 'T', "trapdoorWood", 'M', machineBlock);
        recipes.overrideRecipe("shaped_item.rotationdisablerupgrade_412194477", Ic2Items.rotationDissablerUpgrade, "C", "R", "M", 'C', Items.COMPASS, 'R', Blocks.REDSTONE_TORCH, 'M', machineBlock);
        recipes.overrideRecipe("shaped_item.itemarmorquantumlegs_-1246661396", Ic2Items.quantumLeggings, "MLM", "INI", "G G", 'M', machineBlock, 'L', Ic2Items.lapotronCrystal, 'I', Ic2Items.iridiumPlate, 'N', Ic2Items.nanoLeggings, 'G', DUST_GLOWSTONE);
        recipeId = STEEL_MODE ? 314434482 : 1752336958;
        recipes.overrideRecipe("shaped_tile.wavegenerator_" + recipeId, Ic2Items.waveGenerator, "SAC", "STW", "SAC", 'S', MATERIAL_STEELS, 'A', Ic2Items.advancedAlloy, 'C', CIRCUIT_BASIC, 'T', GTCXItems.magnaliumTurbineRotor, 'W', Ic2Items.waterMill);
        recipeId = STEEL_MODE ? -1390003848 : -1674978904;
        recipes.overrideRecipe("shaped_tile.oceangenerator_" + recipeId, Ic2Items.oceanGenerator, "SAS", "TWT", "SAS", 'S', MATERIAL_STEELS, 'A', Ic2Items.advancedAlloy, 'T', GTCXItems.osmiumTurbineRotor, 'W', Ic2Items.waterMill);
        recipeId = STEEL_MODE ? 1337979563 : 1053004507;
        recipes.overrideRecipe("shaped_tile.blockthermalgenerator_" + recipeId, Ic2Items.thermalGenerator, "PPP", "PAP", "CGC", 'P', MATERIAL_INVAR_ALUMINIUM, 'A', MACHINE_ADV, 'C', CIRCUIT_BASIC, 'G', Ic2Items.geothermalGenerator);

        recipeId = STEEL_MODE ? -342403874 : -1588477206;
        recipes.overrideRecipe("shaped_item.itemtoolddrill_1955483893", Ic2Items.diamondDrill, " D ", "DdD", "TCT", 'D', "dustDiamond", 'd', Ic2Items.electricDrill, 'T', TITANIUM, 'C', CIRCUIT_ADVANCED);
        recipes.overrideRecipe("shaped_item.itemtooldrill_" + recipeId, Ic2Items.electricDrill, " S ", "SCS", "SBS", 'S', MATERIAL_STEELS, 'C', circuit, 'B', battery);
        recipeId = STEEL_MODE ? 286640886 : -824616294;
        recipes.overrideRecipe("shaped_item.itemtoolchainsaw_" + recipeId, Ic2Items.chainSaw, " SS", "SCS", "BS ", 'S', MATERIAL_STEELS, 'C', circuit, 'B', battery);
        recipeId = STEEL_MODE ? 1723493281 : -137638623;
        recipes.overrideRecipe("shaped_item.itemtoolhoe_" + recipeId, Ic2Items.electricHoe, "SS ", " C ", " B ", 'S', MATERIAL_STEELS, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemtreetapelectric_-1455688385", Ic2Items.electricTreeTap, " B ", "SCS", "T  ", 'T', Ic2Items.treeTap, 'S', MATERIAL_STEELS, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.electricsprayer_-335930196", Ic2Items.electricCfSprayer, "sS ", "SC ", "  B", foamTransfer(StackUtil.copyWithWildCard(Ic2Items.cfSprayer)), 's', StackUtil.copyWithWildCard(Ic2Items.cfSprayer), 'S', MATERIAL_STEELS, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemnanosaber_644260803", Ic2Items.nanoSaber, "PI ", "PI ", "CEC", 'P', PLATINUM, 'I', Ic2Items.iridiumPlate, 'C', Ic2Items.carbonPlate, 'E', Ic2Items.energyCrystal);
        nbt = new NBTTagCompound();
        nbt.setBoolean("losslessMode", true);
        stack = Ic2Items.electricWrench.copy();
        stack.setTagCompound(nbt);
        recipes.overrideRecipe("shaped_item.itemtoolwrenchelectric_883008511", stack, "S S", "SCS", " B ",'S', MATERIAL_STEELS, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemtoolmininglaser_1732214669", Ic2Items.miningLaser,"RHE", "TTC", " AA", 'R', "gemRuby", 'H', GTItems.heatStorageHelium6, 'E', TIER_2_ENERGY, 'T', TITANIUM, 'C', CIRCUIT_ADVANCED, 'A', Ic2Items.advancedAlloy);
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.suBattery, 32), "C", "S", "L", 'C', Ic2Items.insulatedCopperCable, 'S', GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 1), 'L', "dustLead");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.suBattery, 32), "C", "L", "S", 'C', Ic2Items.insulatedCopperCable, 'S', GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 1), 'L', "dustLead");
        String tin = GTCXConfiguration.general.usePlates ? "plateTin" : "ingotTin";
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.battery, 2), " C ", "TST", "TLT", 'C', Ic2Items.copperCable, 'T', tin, 'S', GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 1), 'L', "dustLead");
        recipes.overrideRecipe("shaped_item.itembatre_2077392104", Ic2Items.battery, " C ", "TRT", "TRT", 'C', Ic2Items.copperCable, 'T', tin, 'R', DUST_REDSTONE);
        recipeId = STEEL_MODE ? 389795443 : -650149377;
        recipes.overrideRecipe("shaped_item.itemingotalloy_" + recipeId, GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 2), "TTT", "MMM", "BBB", 'T', combineRecipeObjects(MATERIAL_REFINED_IRON, INVAR), 'M', MATERIAL_BRASS_BRONZE, 'B', MATERIAL_TIN_ZINC);
        recipeId = STEEL_MODE ? -1060574968 : -795420664;
        recipes.overrideRecipe("shaped_item.reactorvent_" + recipeId, Ic2Items.reactorVent, "PBP", "B B", "PBP", 'P', ALUMINIUM, 'B', Blocks.IRON_BARS);
        recipes.overrideRecipe("shaped_item.reactorheatswitch_688109925", Ic2Items.reactorHeatSwitch, " C ", "TcT", " T ", 'C', CIRCUIT_BASIC, 'T', TIN, 'c', Ic2Items.denseCopperPlate);
        recipes.overrideRecipe("shaped_item.reactorheatswitchspread_94917204", Ic2Items.reactorHeatSwitchGold, " G ", "GRG", " G ", 'G', PRE + "Gold", 'R', Ic2Items.reactorHeatSwitch);
        recipes.overrideRecipe("shaped_item.reactorventspread_2144532227", Ic2Items.reactorVentSpread, "ITI", "TRT", "ITI", 'I', Blocks.IRON_BARS, 'T', TIN, 'R', Ic2Items.reactorVent);
        recipes.overrideRecipe("shaped_item.reactorventgold_2067100004", Ic2Items.reactorVentGold, "G", "R", "G", 'G', PRE + "Gold", 'R', Ic2Items.reactorVentCore);
        recipes.overrideShapelessRecipe("shapeless_item.itemreactorplating_1093967048", Ic2Items.reactorPlating, LEAD, Ic2Items.advancedAlloy);
        recipes.overrideRecipe("shaped_item.upgradekit.mfs_1186329581", Ic2Items.mfsuUpgradeKid, "BMB", "BBB", " B ", 'B', "ingotBronze", 'M', Ic2Items.mfsu);
        if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
            recipeId = STEEL_MODE ? 1913907474 : 1986006418;
            recipes.overrideRecipe("shaped_tile.blockfenceiron_" + recipeId, GTMaterialGen.getIc2(Ic2Items.ironFence, 6), "RRR", "RRR", " W ", 'R', "rodIron", 'W', "craftingToolWrench");
        } else {
            recipeId = STEEL_MODE ? 1913907474 : 1986006418;
            recipes.overrideRecipe("shaped_tile.blockfenceiron_" + recipeId, GTMaterialGen.getIc2(Ic2Items.ironFence, 6), "RRR", "RRR", 'R', "rodRefinedIron");
        }
        IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? input("craftingToolWrench") : null;
        recipeId = STEEL_MODE ? 480320652 : 527557260;
        String ingot = GTCXConfiguration.general.forceSteelCasings && !GTCXConfiguration.general.gt2Mode ? REFINED_IRON : MATERIAL_REFINED_IRON;
        recipes.overrideRecipe("shaped_tile.blockmachine_" + recipeId, Ic2Items.machine, "PPP", "PWP", "PPP", 'P', ingot, 'W', wrench);
        recipeId = STEEL_MODE ? 1329688411 : -1509341361;
        recipes.overrideRecipe("shaped_tile.blockironscaffold_" + recipeId, GTMaterialGen.getIc2(Ic2Items.ironScaffold, 32), "III", " F ", "F F", 'I', "ingotRefinedIron", 'F', "rodIron");
        recipes.overrideRecipe("shaped_item.reactorReflectorThick_-1313142365", Ic2Items.reactorReflectorThick.copy(), " P ", "PBP", " P ", 'P', Ic2Items.reactorReflector, 'B', PRE + "Beryllium");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.upgradekit.mfs_-1749227982");
        recipes.overrideRecipe("shaped_item.reactorcoolantsimple_-484238369", Ic2Items.reactorCoolantCellSimple, " T ", "TWT", " T ", 'T', TIN, 'W', GTMaterialGen.getFluidStack("water", 1000));
        recipes.overrideRecipe("shaped_item.reactorcoolanttriple_1052008285", Ic2Items.reactorCoolantCellTriple, "TTT", "WWW", "TTT", 'T', TIN, 'W', Ic2Items.reactorCoolantCellSimple);
        recipes.overrideRecipe("shaped_item.reactorcoolanttriple_-606011908", Ic2Items.reactorCoolantCellTriple, "TTT", "WWW", "TTT", 'T', input(TIN, 2), 'W', GTMaterialGen.getIc2(Ic2Items.waterCell, 2));
        recipes.overrideRecipe("shaped_item.reactorcoolantsix_-1912497898", Ic2Items.reactorCoolantCellSix, "TWT", "TCT", "TWT", 'T', TIN, 'W', Ic2Items.reactorCoolantCellTriple, 'C', Ic2Items.denseCopperPlate);
        recipes.overrideRecipe("shaped_item.itemthermometer_1507613392", Ic2Items.thermometer, " GT", "GMG", "GG ", 'G', "blockGlass", 'T', TIN, 'M', GTMaterialGen.getFluidStack(GTMaterial.Mercury, 1000));
        if (GTCXConfiguration.general.harderCircuits && !GTCXConfiguration.general.gt2Mode){
            recipeId = STEEL_MODE ? 1921363733 : 1058514721;
            recipes.overrideRecipe("shaped_item.itempartcircuit_" + recipeId, Ic2Items.electricCircuit, "CCC", "RER", "CCC", 'C', Ic2Items.insulatedCopperCable, 'R', "plateRedAlloy", 'E', PLATE_ELECTRIC);
            recipeId = STEEL_MODE ? -1911001323 : 1521116961;
            recipes.overrideRecipe("shaped_item.itempartcircuit_" + recipeId, Ic2Items.electricCircuit, "CRC", "CEC", "CRC", 'C', Ic2Items.insulatedCopperCable, 'R', "plateRedAlloy", 'E', PLATE_ELECTRIC);
            recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-1948043137", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RGR", "LCL", "RGR", 'R', "plateRedAlloy", 'G', DUST_GLOWSTONE, 'C', CIRCUIT_BASIC, 'L', INPUT_LAPIS_ANY);
            recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-205948801", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RLR", "GCG", "RLR", 'R', "plateRedAlloy", 'G', DUST_GLOWSTONE, 'C', CIRCUIT_BASIC, 'L', INPUT_LAPIS_ANY);
        } else {
            IRecipeInput ingotElectric = combineRecipeObjects(INGOT_REFINEDIRON, INGOT_SILICON,
                    "ingotAluminium","ingotSilver",
                    "ingotElectrum", "ingotPlatinum");
            if (GTConfig.general.addBasicCircuitRecipes) {
                recipeId = STEEL_MODE ? 1921363733 : 1058514721;
                recipes.overrideRecipe("shaped_item.itemPartCircuit_"
                        + recipeId, GTMaterialGen.getIc2(Ic2Items.electricCircuit, 1), "CCC", "RIR", "CCC", 'C', Ic2Items.insulatedCopperCable.copy(), 'R', DUST_REDSTONE, 'I', ingotElectric);
                recipeId = STEEL_MODE ? -1911001323 : 1521116961;
                recipes.overrideRecipe("shaped_item.itemPartCircuit_"
                        + recipeId, GTMaterialGen.getIc2(Ic2Items.electricCircuit, 1), "CRC", "CIC", "CRC", 'C', Ic2Items.insulatedCopperCable.copy(), 'R', DUST_REDSTONE, 'I', ingotElectric);
                recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.electricCircuit, 2), "CCC", "III", "CCC", 'C', Ic2Items.insulatedCopperCable.copy(), 'I', ingotElectric);
                recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.electricCircuit, 2), "CIC", "CIC", "CIC", 'C', Ic2Items.insulatedCopperCable.copy(), 'I', ingotElectric);
            }
            if (GTConfig.general.addAdvCircuitRecipes) {
                recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-1948043137", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RGR", "LCL", "RGR", 'R', DUST_REDSTONE, 'G', DUST_GLOWSTONE, 'C', CIRCUIT_BASIC, 'L', INPUT_LAPIS_ANY);
                recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-205948801", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RLR", "GCG", "RLR", 'R', DUST_REDSTONE, 'G', DUST_GLOWSTONE, 'C', CIRCUIT_BASIC, 'L', INPUT_LAPIS_ANY);
                recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 2), "RGR", "LCL", "RGR", 'R', INPUT_INGOT_SILVER, 'G', DUST_GLOWSTONE, 'C', INPUT_CIRCUIT_BASIC_X2, 'L', INPUT_LAPIS_ANY);
                recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 2), "RLR", "GCG", "RLR", 'R', INPUT_INGOT_SILVER, 'G', DUST_GLOWSTONE, 'C', INPUT_CIRCUIT_BASIC_X2, 'L', INPUT_LAPIS_ANY);
            }
        }

    }

    static void initOverrideGTClassic(){
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileQuantumChest), "DCD", "HTH", "DdD", 'D', GTItems.orbData, 'C', GTCXItems.computerMonitor, 'H', MACHINE_ELITE, 'T', Ic2Items.teleporter, 'd', GTBlocks.tileDigitalChest);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileDigitalChest), "III", "SDS", "ICI", 'I', PLATINUM, 'S', "circuitElite", 'D', GTItems.orbData, 'C', GTCXItems.computerMonitor);
        recipes.addRecipe( GTMaterialGen.get(GTBlocks.tileQuantumTank), "CIC", "IQI", "CIC", 'I', PLATINUM, 'C', CIRCUIT_MASTER, 'Q', GTBlocks.tileQuantumChest);
        recipes.addRecipe( GTMaterialGen.get(GTBlocks.tileComputer), "CMO", "MAM", "OMC", 'C', CIRCUIT_MASTER, 'M', GTCXItems.computerMonitor, 'O', CIRCUIT_ULTIMATE, 'A', MACHINE_ADV);
        recipes.addRecipe( GTMaterialGen.get(GTBlocks.casingFusion), "CSC", "NMN", "CRC", 'C', CIRCUIT_MASTER, 'S', CRAFTING_SUPERCONDUCTOR, 'N', GTCXItems.nichromeHeatingCoil, 'M',
                MACHINE_ELITE, 'R', Ic2Items.reactorReflectorIridium);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileTesseractMaster),"TCT", "CEC", "TcT", 'T', TITANIUM, 'C', CIRCUIT_MASTER, 'E', Blocks.ENDER_CHEST, 'c', GTBlocks.tileComputer);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileMagicEnergyConverter, 1), "CTC", "IBI", "CLC", 'C', GTValues.CIRCUIT_ADVANCED, 'B', Blocks.BEACON, 'L', Ic2Items.lapotronCrystal.copy(), 'I', GTRecipeCraftingHandler.combineRecipeObjects(PLATINUM, PRE + "Draconium", PRE + "Knightmetal", PRE + "Thaumium", PRE + "Manasteel"), 'T', Ic2Items.teleporter.copy());
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileTesseractSlave),"TCT", "CEC", "TcT", 'T', TITANIUM, 'C', "circuitElite", 'E', Blocks.ENDER_CHEST, 'c', Ic2Items.advMachine);
        IRecipeInput rod = combineRecipeObjects( "rodTitanium", "rodTungstensteel");
        IRecipeInput plate = combineRecipeObjects( TITANIUM, TUNGSTEN_STEEL);
        recipes.addRecipe(GTMaterialGen.get(GTItems.rockCutter), "DR ", "DT ", "DCB", new EnchantmentModifier(GTMaterialGen.get(GTItems.rockCutter), Enchantments.SILK_TOUCH).setUsesInput(), 'R', rod, 'T', plate, 'B', Ic2Items.battery, 'C', CIRCUIT_BASIC, 'D', "dustDiamond");
        recipes.addRecipe(GTMaterialGen.get(GTItems.jackHammer), "TJT", " D ", 'T', "rodTungstensteel", 'J', GTCXItems.steelJackhammer, 'C', CIRCUIT_BASIC, 'D', "dustDiamond");
        recipes.addRecipe(GTMaterialGen.get(GTItems.lithiumBattery), " C ", "ALA", "ALA", 'C', Ic2Items.goldCable, 'A', ALUMINIUM, 'L', "dustLithium");
        recipes.addRecipe(GTMaterialGen.get(GTItems.lithiumBatpack), "BCB", "BAB", "B B", 'B', GTItems.lithiumBattery, 'C', CIRCUIT_ADVANCED, 'A', ALUMINIUM);
        recipes.addRecipe(GTMaterialGen.get(GTItems.portableScanner), "AEA", "CcC", "ABA", 'A', ALUMINIUM, 'E', Ic2Items.euReader, 'C', CIRCUIT_ADVANCED, 'c', Ic2Items.cropAnalyzer, 'B', GTItems.lithiumBattery);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.casingHighlyAdvanced), "CTC", "TMT", "CTC", 'C', CHROME, 'T', TITANIUM, 'M', MACHINE_ADV);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.casingHighlyAdvanced), "TCT", "CMC", "TCT", 'C', CHROME, 'T', TITANIUM, 'M', MACHINE_ADV);
        recipes.addRecipe(GTMaterialGen.get(GTItems.forceField), "TCT", "CLC", "TCT", 'T', TUNGSTEN_STEEL, 'C', "circuitElite", 'L', GTItems.orbEnergy);
        IRecipeInput hammer = GTCXConfiguration.general.enableCraftingTools ? input("craftingToolForgeHammer") : null;
        recipes.addRecipe(GTMaterialGen.get(GTItems.springBoots), "RWR", "RBR", 'R', getRefinedIronRod(), 'B', Ic2Items.compositeBoots, 'W', hammer);
        recipes.addRecipe(GTMaterialGen.get(GTItems.destructoPack, 1), "CIC", "ILI", "CIC", 'L', FS_LAVA, 'C', CIRCUIT_BASIC, 'I', MATERIAL_REFINED_IRON);
        recipes.addRecipe(GTMaterialGen.get(GTItems.electroMagnet, 1), "M M", "WMW", "IBI", 'M', Ic2Items.magnet, 'B', Ic2Items.battery, 'I', getRefinedIronRod(), 'W', Ic2Items.copperCable);
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 3), "TTT", "MMM", "BBB", 'T', combineRecipeObjects(MATERIAL_REFINED_IRON, INVAR), 'M', MATERIAL_BRASS_BRONZE, 'B', MATERIAL_MIXED_METAL_1);
        recipes.addRecipe(GTMaterialGen.get(GTItems.cloakingDevice, 1), "IPI", "POP", "IPI", 'I', CHROME, 'P', PLATE_IRIDIUM_ALLOY, 'O', BATTERY_ULTIMATE);
        recipes.addRecipe(GTMaterialGen.get(GTItems.teslaStaff, 1), " SL", " PS", "P  ", 'L', BATTERY_ULTIMATE, 'S', CRAFTING_SUPERCONDUCTOR, 'P', "rodIridium");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 6), "TTT", "MMM", "BBB", 'T', MATERIAL_MIXED_METAL_2, 'M', MATERIAL_BRASS_BRONZE, 'B', MATERIAL_MIXED_METAL_1);
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 8), "TTT", "MMM", "BBB", 'T', TUNGSTEN_STEEL, 'M', MATERIAL_BRASS_BRONZE, 'B', MATERIAL_MIXED_METAL_1);
        recipes.addRecipe(GTMaterialGen.get(GTItems.circuitEnergy, 4), "CTC", "LIL", "CTC", 'C', CIRCUIT_ADVANCED, 'T', TUNGSTEN, 'L', Ic2Items.lapotronCrystal, 'I', Ic2Items.iridiumPlate);
        recipes.addRecipe(GTMaterialGen.get(GTItems.superConductor, 4), "CCC", "TIT", "ccc", 'C', INPUT_COOLANT_SUPERCONDUCTOTR, 'I', Ic2Items.iridiumPlate, 'T', TUNGSTEN, 'c', CIRCUIT_MASTER);
        IRecipeInput rodIrons = combineRecipeObjects("rodIron", "rodRefinedIron");
        recipes.addRecipe(GTMaterialGen.get(GTItems.magnifyingGlass), "R ", " G", 'R', rodIrons, 'G', "paneGlass");
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileTranslocator), "EWE", "CBC", "EME", 'E', ELECTRUM, 'W', Ic2Items.insulatedCopperCable, 'C', CIRCUIT_BASIC, 'B', MACHINE_CHEAP, 'M', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileBufferLarge), "EWE", "CBC", "EcE", 'E', ELECTRUM, 'W', Ic2Items.insulatedCopperCable, 'C', CIRCUIT_ADVANCED, 'B', MACHINE_CHEAP, 'c', CHEST_WOOD);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileBufferFluid), "EWE", "CBC", "EbE", 'E', ELECTRUM, 'W', Ic2Items.insulatedCopperCable, 'C', CIRCUIT_ADVANCED, 'B', MACHINE_CHEAP, 'b', Items.BUCKET);
        IRecipeInput aluiron = combineRecipeObjects( REFINED_IRON, ALUMINIUM);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileCabinet), "III", "CIC", "III", 'I', aluiron, 'C', CHEST_WOOD);
        IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? combineRecipeObjects( "craftingToolMonkeyWrench", "craftingToolWrench") : new RecipeInputOreDict("rodRefinedIron");
        rod = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? new RecipeInputOreDict("rodRefinedIron") : null;
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileDrum), "PWP", "PMP", "PRP", 'P', REFINED_IRON, 'W', wrench, 'M', rod, 'R', "rodRefinedIron");
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileWorktable), "ICI", "IMI", "IcI", 'I', aluiron, 'C', "workbench", 'c', CHEST_WOOD, 'M', MACHINE_CHEAP);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileAutocrafter), "EBE", "CcC", "EME", 'E', ELECTRUM, 'B', Ic2Items.battery, 'C', CIRCUIT_ADVANCED, 'c', "workbench", 'M', MACHINE_ADV);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileTranslocatorFluid), "EWE", "CBC", "EME", 'E', ELECTRUM, 'W', Ic2Items.insulatedCopperCable, 'C', CIRCUIT_BASIC, 'B', MACHINE_CHEAP, 'M', Ic2Items.basicFluidImportUpgrade);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileDisassembler, 1), "RAR", "ECE", "RWR", 'A', GTMaterialGen.getIc2(Ic2Items.extractor),
                'W', GTCXBlocks.assemblingMachine, 'R', MATERIAL_STEELS, 'E', Ic2Items.insulatedCopperCable.copy(), 'C', CIRCUIT_ADVANCED );
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileCharcoalPit), "RFR", "RFR", "RFR", 'R', REFINED_IRON, 'F', Items.FLINT);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileTypeFilter), "PPP", "TCB", "PPP", 'P', aluiron, 'T', GTBlocks.tileTranslocator, 'C', CIRCUIT_ADVANCED, 'B', GTBlocks.tileBufferLarge);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileItemFilter), "PPP", "TCB", "PPP", 'P', PRE + "Iron", 'T', GTBlocks.tileTranslocator, 'C', CIRCUIT_BASIC, 'B', GTBlocks.tileBufferLarge);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileDisplayScreen), "cGc", "CMC", "IRI", 'c', GTCXItems.computerMonitor, 'G', "paneGlass", 'C', CIRCUIT_BASIC, 'M', MACHINE_BASIC, 'I', combineRecipeObjects(PRE + "Iron", REFINED_IRON), 'R', Items.REDSTONE);
        /** Fusion Computer **/
        if (GTConfig.general.removeIC2Plasmafier) {
            recipes.overrideRecipe("shaped_tile.blockPlasmafier_679353211", GTMaterialGen.get(GTCXBlocks.fusionComputer, 1), "CCC", "cMc", "CCC", 'C', CIRCUIT_MASTER, 'c', GTBlocks.tileComputer, 'M',
                    GTBlocks.casingFusion);
        } else {
            recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fusionComputer, 1), "CCC", "cMc", "CCC", 'C', CIRCUIT_MASTER, 'c', GTBlocks.tileComputer, 'M',
                    GTBlocks.casingFusion);
        }
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileRockBreaker), "IDI", "CTC", "IMI", 'I', INVAR, 'C', CIRCUIT_BASIC, 'M', MACHINE_ADV, 'D', INPUT_DIAMOND_OR_TUNGSTEN, 'T', Ic2Items.electricDrill.copy());
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.centrifuge), "RCR", "MEM", "RCR", 'R', MATERIAL_STEELS_ALUMINIUM, 'C', CIRCUIT_ADVANCED, 'M', MACHINE_ADV, 'E', Ic2Items.extractor);
        recipes.addShapelessRecipe(GTMaterialGen.get(GTCXBlocks.centrifuge), GTBlocks.tileCentrifuge);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileBatteryLV), " C ", "TRT", "TMT", 'C', Ic2Items.copperCable, 'T', TIN, 'R', "blockRedstone", 'M', MACHINE_BASIC);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileLamp, 2), "IPI", "PRP", "IPI", 'P', "paneGlass", 'I', MATERIAL_MACHINE, 'R', Blocks.REDSTONE_LAMP);
        overrideGTRecipe("shaped_item.gtclassic.heatstorage_helium_single_1470527211", GTMaterialGen.get(GTItems.heatStorageHelium1), " T ", "THT", " T ", 'T', TIN, 'H', GTMaterialGen.getTube(GTMaterial.Helium, 1));
        overrideGTRecipe("shaped_item.gtclassic.heatstorage_helium_triple_-1980325134", GTMaterialGen.get(GTItems.heatStorageHelium3), "TTT", "HHH", "TTT", 'T', TIN, 'H', GTItems.heatStorageHelium1);
        overrideGTRecipe("shaped_item.gtclassic.heatstorage_helium_six_-1788930351", GTMaterialGen.get(GTItems.heatStorageHelium6), "THT", "TCT", "THT", 'T', TIN, 'C', Ic2Items.denseCopperPlate, 'H', GTItems.heatStorageHelium3);
    }

    public static void overrideGTRecipe(String recipeId, ItemStack output, Object... input) {
        GTRecipeCraftingHandler.overrideGTRecipe("gtclassic", recipeId, output, input);
    }

    public static void overrideShapelessGTRecipe(String recipeId, ItemStack output, Object... input){
        GTRecipeCraftingHandler.overrideShapelessGTRecipe("gtclassic", recipeId, output, input);
    }

    public void removeGTRecipe(String recipeId){
        GTRecipeCraftingHandler.removeRecipe("gtclassic", recipeId);
    }

    public static IRecipeInput input(String name){
        return input(name, 1);
    }

    public static IRecipeInput input(String name, int size){
        return GTTileBaseMachine.input(name, size);
    }

    public static IRecipeInput input(ItemStack stack){
        return GTTileBaseMachine.input(stack);
    }
}
