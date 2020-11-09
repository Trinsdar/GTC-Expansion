package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXValues;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.GTCXTileBath;
import gtc_expansion.tile.GTCXTileCentrifuge;
import gtc_expansion.tile.GTCXTileMicrowave;
import gtc_expansion.util.GTCXBatteryInput;
import gtc_expansion.util.GTCXIc2cECompat;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTConfig;
import gtclassic.common.GTItems;
import gtclassic.common.recipe.GTRecipe;
import gtclassic.common.tile.GTTileCentrifuge;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.core.block.machine.low.TileEntityCompressor;
import ic2.core.block.machine.low.TileEntityExtractor;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;

import static gtclassic.common.recipe.GTRecipeMods.input;

public class GTCXRecipeProcessing {
    public static void init(){
        initCentrifugeRecipes();
        initIc2Recipes();
        initFurnaceRecipes();
        initOreProcessing();
    }

    public static void initFurnaceRecipes(){
        Item clay = GTCXConfiguration.general.unfiredBricks ? GTCXItems.unfiredFireBrick : GTCXItems.fireClayBall;
        GameRegistry.addSmelting(GTMaterialGen.get(clay), GTMaterialGen.get(GTCXItems.fireBrick), 0.1F);
        GameRegistry.addSmelting(Ic2Items.machine.copy(), GTMaterialGen.getIc2(Ic2Items.refinedIronIngot, 6), 0.0F);
        if (GTCXConfiguration.general.unfiredBricks){
            GameRegistry.addSmelting(GTMaterialGen.get(GTCXItems.unfiredBrick), GTMaterialGen.get(Items.BRICK), 0.1F);
        }
        GameRegistry.addSmelting(GTCXBlocks.oreSheldonite, GTMaterialGen.getIngot(GTMaterial.Platinum, 1), 1.0F);
        GameRegistry.addSmelting(GTCXBlocks.oreCassiterite, GTMaterialGen.getIc2(Ic2Items.tinIngot, 2), 0.5F);
        GameRegistry.addSmelting(GTCXBlocks.oreTetrahedrite, Ic2Items.copperIngot, 0.5F);
        GameRegistry.addSmelting(GTMaterialGen.getDust(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 6), 0.5F);
        GameRegistry.addSmelting(GTMaterialGen.getDust(GTCXMaterial.Cassiterite, 1), GTMaterialGen.getIc2(Ic2Items.tinIngot, 1), 0.5F);
    }

    public static void initIc2Recipes(){
        ClassicRecipes.earthExtractor.registerValue(5.85f, GTMaterialGen.getDust(GTCXMaterial.Stone, 1));
        TileEntityExtractor.addRecipe("oreDiamond", 1, GTMaterialGen.get(Items.DIAMOND, 2));
        TileEntityExtractor.addRecipe("oreEmerald", 1, GTMaterialGen.get(Items.EMERALD, 2));
        TileEntityExtractor.addRecipe("oreCoal", 1, GTMaterialGen.get(Items.COAL, 2));
        TileEntityExtractor.addRecipe("oreQuartz", 1, GTMaterialGen.get(Items.QUARTZ, 2));
        TileEntityExtractor.addRecipe("oreRuby", 1, GTMaterialGen.getGem(GTMaterial.Ruby, 2));
        TileEntityExtractor.addRecipe("oreSapphire", 1, GTMaterialGen.getGem(GTMaterial.Sapphire, 2));
        TileEntityExtractor.addRecipe("oreOlivine", 1, GTMaterialGen.getGem(GTCXMaterial.Olivine, 2));
        TileEntityMacerator.addRecipe("oreRedstone", 1, GTMaterialGen.get(Items.REDSTONE, 8));
        if (OreDictionary.doesOreNameExist("stoneMarble")){
            GTRecipe.maceratorUtil("stoneMarble", 1, GTMaterialGen.getDust(GTCXMaterial.Marble, 1));
        }
        TileEntityMacerator.addRecipe(GTMaterialGen.get(Blocks.END_STONE), GTMaterialGen.getDust(GTCXMaterial.Endstone, 1));
        GTRecipe.maceratorUtil("fuelCoke", 1, GTMaterialGen.getDust(GTCXMaterial.Coke, 1));
        TileEntityCompressor.addRecipe("dustIridium", 1, Ic2Items.iridiumOre.copy());
        TileEntityMacerator.addRecipe(GTMaterialGen.get(GTCXItems.constantanHeatingCoil), GTMaterialGen.getDust(GTCXMaterial.Constantan, 3));
        TileEntityMacerator.addRecipe(GTMaterialGen.get(GTCXItems.kanthalHeatingCoil), GTMaterialGen.getDust(GTCXMaterial.Kanthal, 4));
        TileEntityMacerator.addRecipe(GTMaterialGen.get(GTCXItems.nichromeHeatingCoil), GTMaterialGen.getDust(GTCXMaterial.Nichrome, 4));
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTItems.sprayCanEmpty), GTCXRecipe.input("sand", 16), GTMaterialGen.get(GTCXItems.hardeningSpray));
        //TileEntityMacerator.addRecipe(GTRecipeCraftingHandler.combineRecipeObjects(GTMaterialGen.get(GTCXItems.mold), GTMaterialGen.get(GTCXItems.moldBlock), GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.get(GTCXItems.moldCell), GTMaterialGen.get(GTCXItems.moldGear), GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.get(GTCXItems.moldLargePipe), GTMaterialGen.get(GTCXItems.moldMediumPipe), GTMaterialGen.get(GTCXItems.moldNugget), GTMaterialGen.get(GTCXItems.moldPlate), GTMaterialGen.get(GTCXItems.moldRod), GTMaterialGen.get(GTCXItems.moldSmallPipe), GTMaterialGen.get(GTCXItems.moldWire)), GTMaterialGen.getDust(GTCXMaterial.Steel, 4));
        GTRecipe.maceratorUtil("oreSodalite", 1, GTMaterialGen.getDust(GTMaterial.Sodalite, 12));
        GTRecipe.maceratorUtil("gemDiamond", 1, GTMaterialGen.getDust(GTCXMaterial.Diamond, 1));
        GTRecipe.maceratorUtil("blockGlass", 1, GTMaterialGen.getDust(GTCXMaterial.SiliconDioxide, 1));
        if (GTCXConfiguration.general.usePlates && (!Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) || !GTConfig.modcompat.compatIc2Extras)){
            TileEntityCompressor.addRecipe("plateCopper", 8, Ic2Items.denseCopperPlate);
        }
        if (GTCXValues.STEEL_MODE){
            TileEntityCompressor.addRecipe(GTCXMaterialGen.getHull(GTCXMaterial.Steel, 1), GTMaterialGen.getIc2(Ic2Items.miningPipe, 10));
        }
        TileEntityCompressor.addRecipe("pulpWood", 4,GTMaterialGen.get(GTCXItems.woodPlate));
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTItems.testTube), GTTileBaseMachine.input(GTMaterialGen.get(GTCXItems.oilberry)), GTMaterialGen.getTube(GTMaterial.Oil, 1));
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTCXItems.batteryHull), GTTileBaseMachine.input(GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 2)), GTCXItems.acidBattery.getFull());
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTCXItems.batteryHull), GTTileBaseMachine.input(GTMaterialGen.getTube(GTMaterial.Mercury, 2)), GTCXItems.mercuryBattery.getFull());
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTCXItems.batteryHull), GTTileBaseMachine.input("dustLithium", 2), GTMaterialGen.get(GTItems.lithiumBattery));
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTCXItems.batteryHull), GTTileBaseMachine.input(GTMaterialGen.getTube(GTMaterial.Sodium, 2)), GTMaterialGen.get(GTCXItems.sodiumBattery));
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTCXItems.batteryHull), GTTileBaseMachine.input(GTMaterialGen.get(Items.REDSTONE, 2)), Ic2Items.battery);
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTCXItems.largeBatteryHull), GTTileBaseMachine.input(GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 6)), GTCXItems.largeAcidBattery.getFull());
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTCXItems.largeBatteryHull), GTTileBaseMachine.input(GTMaterialGen.getTube(GTMaterial.Mercury, 6)), GTCXItems.largeMercuryBattery.getFull());
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTCXItems.largeBatteryHull), GTTileBaseMachine.input("dustLithium", 6), GTMaterialGen.get(GTCXItems.largeLithiumBattery));
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTCXItems.largeBatteryHull), GTTileBaseMachine.input(GTMaterialGen.getTube(GTMaterial.Sodium, 6)), GTMaterialGen.get(GTCXItems.largeSodiumBattery));
        TileEntityExtractor.addRecipe(new GTCXBatteryInput(GTCXItems.acidBattery.getEmpty()), GTMaterialGen.get(GTCXItems.batteryHull));
        TileEntityExtractor.addRecipe(new GTCXBatteryInput(GTCXItems.mercuryBattery.getEmpty()), GTMaterialGen.get(GTCXItems.batteryHull));
        TileEntityExtractor.addRecipe(GTCXItems.largeAcidBattery.getEmpty(), GTMaterialGen.get(GTCXItems.largeBatteryHull));
        TileEntityExtractor.addRecipe(GTCXItems.largeMercuryBattery.getEmpty(), GTMaterialGen.get(GTCXItems.largeBatteryHull));
    }

    public static void initOreProcessing(){
        GTRecipe.maceratorUtil("oreGalena", 1, getCrushedOrDust(GTCXMaterial.Galena, 2));
        GTRecipe.maceratorUtil("oreTetrahedrite", 1, getCrushedOrDust(GTCXMaterial.Tetrahedrite, 2));
        GTRecipe.maceratorUtil("oreCassiterite", 1, getCrushedOrDust(GTCXMaterial.Cassiterite, 4));
        GTRecipe.maceratorUtil("orePyrite", 1, getCrushedOrDust(GTMaterial.Pyrite, 4));
        GTRecipe.maceratorUtil("oreCinnabar", 1, getCrushedOrDust(GTCXMaterial.Cinnabar, 3));
        GTRecipe.maceratorUtil("oreSphalerite", 1, getCrushedOrDust(GTCXMaterial.Sphalerite, 2));
        GTRecipe.maceratorUtil("orePlatinum", 1, getCrushedOrDust(GTMaterial.Platinum, 2));
        GTRecipe.maceratorUtil("oreTungstate", 1, getCrushedOrDust(GTMaterial.Tungsten, 4));
        GTRecipe.maceratorUtil("oreChromite", 1, getCrushedOrDust(GTCXMaterial.Chromite, 2));
        if (GTCXConfiguration.general.crushedOres && !GTCXConfiguration.general.gt2Mode){
            addCrushedOreRecipes(GTCXMaterial.Tetrahedrite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Antimony, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.Zinc, 1));
            addCrushedOreRecipes(GTCXMaterial.Galena, GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 2), GTCXMaterialGen.getTinyDust(GTCXMaterial.Silver, 1));
            addCrushedOreRecipes(GTCXMaterial.Cinnabar, GTCXMaterialGen.getTinyDust(GTCXMaterial.Redstone, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 1));
            addCrushedOreRecipes(GTCXMaterial.Sphalerite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Zinc, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.GarnetYellow, 1));
            addCrushedOreRecipes(GTMaterial.Pyrite, GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Phosphorus, 1));
            addCrushedOreRecipes(GTMaterial.Bauxite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Grossular, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Titanium, 1));
            addCrushedOreRecipes(GTMaterial.Platinum, GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 2), GTCXMaterialGen.getTinyDust(GTMaterial.Nickel, 1));
            addCrushedOreRecipes(GTMaterial.Tungsten, GTCXMaterialGen.getTinyDust(GTCXMaterial.Manganese, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.Silver, 1));
            addCrushedOreRecipes(GTMaterial.Iridium, GTCXMaterialGen.getTinyDust(GTMaterial.Platinum, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.Osmium, 1));
            addCrushedOreRecipes(GTCXMaterial.Chromite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Iron, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Chrome, 1));
            addCrushedOreRecipes(GTCXMaterial.Cassiterite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Tin, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.Tin, 1));
            addCrushedOreRecipesWithRemove(GTCXMaterial.Iron, GTCXMaterialGen.getTinyDust(GTCXMaterial.Iron, 2), GTCXMaterialGen.getTinyDust(GTCXMaterial.Gold, 1));
            addCrushedOreRecipesWithRemove(GTCXMaterial.Gold, GTCXMaterialGen.getTinyDust(GTCXMaterial.Gold, 2), GTCXMaterialGen.getTinyDust(GTCXMaterial.Silver, 1));
            addCrushedOreRecipesWithRemove(GTCXMaterial.Copper, GTCXMaterialGen.getTinyDust(GTCXMaterial.Copper, 2), GTCXMaterialGen.getTinyDust(GTCXMaterial.Tin, 1));
            addCrushedOreRecipesWithRemove(GTCXMaterial.Tin, GTCXMaterialGen.getTinyDust(GTCXMaterial.Tin, 2), GTCXMaterialGen.getTinyDust(GTCXMaterial.Iron, 1));
            addCrushedOreRecipesWithRemove(GTCXMaterial.Lead, GTCXMaterialGen.getTinyDust(GTCXMaterial.Lead, 3), GTCXMaterialGen.getTinyDust(GTCXMaterial.Lead, 1));
            addCrushedOreRecipesWithRemove(GTCXMaterial.Silver, GTCXMaterialGen.getTinyDust(GTCXMaterial.Silver, 2), GTCXMaterialGen.getTinyDust(GTCXMaterial.Copper, 1));
            addCrushedOreRecipesWithRemove(GTMaterial.Uranium, GTCXMaterialGen.getTinyDust(GTCXMaterial.Lead, 2), GTCXMaterialGen.getTinyDust(GTMaterial.Thorium, 1));
            addCrushedOreRecipes(GTMaterial.Nickel, GTCXMaterialGen.getTinyDust(GTMaterial.Nickel, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Platinum, 2));
            if (OreDictionary.doesOreNameExist("oreNickel")) {
                ClassicRecipes.macerator.removeRecipe(input("oreNickel", 1));
                GTRecipe.maceratorUtil("oreNickel", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Nickel, 2));
            }
            ClassicRecipes.macerator.removeRecipe(input("oreBauxite", 1));
            TileEntityMacerator.addRecipe("oreBauxite", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Bauxite, 4));
            ClassicRecipes.macerator.removeRecipe(input("oreIridium", 1));
            TileEntityMacerator.addRecipe("oreIridium", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Iridium, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreIron", 1));
            GTRecipe.maceratorUtil("oreIron", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Iron, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreGold", 1));
            GTRecipe.maceratorUtil("oreGold", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Gold, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreCopper", 1));
            GTRecipe.maceratorUtil("oreCopper", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Copper, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreTin", 1));
            GTRecipe.maceratorUtil("oreTin", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Tin, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreSilver", 1));
            GTRecipe.maceratorUtil("oreSilver", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Silver, 2));
            if (OreDictionary.doesOreNameExist("oreLead")){
                ClassicRecipes.macerator.removeRecipe(input("oreLead", 1));
                GTRecipe.maceratorUtil("oreLead", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Lead, 2));
            }
            ClassicRecipes.macerator.removeRecipe(input("oreUranium", 1));
            GTRecipe.maceratorUtil("oreUranium", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Uranium, 2));
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 6), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 6), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Platinum, 1), GTMaterialGen.getIngot(GTMaterial.Platinum, 1), 1.0F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTMaterial.Platinum, 1), GTMaterialGen.getIngot(GTMaterial.Platinum, 1), 1.0F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Cassiterite, 1), Ic2Items.tinIngot.copy(), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Cassiterite, 1), Ic2Items.tinIngot.copy(), 0.5F);

            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Iron, 1), GTMaterialGen.get(Items.IRON_INGOT), 0.7F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Gold, 1), GTMaterialGen.get(Items.GOLD_INGOT), 1.0F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Copper, 1), Ic2Items.copperIngot, 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Tin, 1), Ic2Items.tinIngot.copy(), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Silver, 1), Ic2Items.silverIngot.copy(), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Lead, 1), GTMaterialGen.getIngot(GTCXMaterial.Lead, 1), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Iron, 1), GTMaterialGen.get(Items.IRON_INGOT), 0.7F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Iron, 1), GTMaterialGen.get(Items.IRON_INGOT), 1.0F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Gold, 1), GTMaterialGen.get(Items.GOLD_INGOT), 1.0F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Copper, 1), Ic2Items.copperIngot, 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Tin, 1), Ic2Items.tinIngot.copy(), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Silver, 1), Ic2Items.silverIngot.copy(), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Lead, 1), GTMaterialGen.getIngot(GTCXMaterial.Lead, 1), 0.5F);
        }
    }

    public static void addCrushedOreRecipesWithRemove(GTMaterial main, ItemStack outputWashSide, ItemStack outputThermalSide){
        if (Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS)){
            GTCXIc2cECompat.removeOreWashingRecipe("crushed" + main.getDisplayName());
            GTCXIc2cECompat.removeThermalCentrifugeRecipe("crushedPurified" + main.getDisplayName());
        }
        addCrushedOreRecipes(main, outputWashSide, outputThermalSide);
    }

    public static void addCrushedOreRecipes(GTMaterial main, ItemStack outputWashSide, ItemStack outputThermalSide){
        ItemStack uranium = Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) && main.equals(GTMaterial.Uranium) ? GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "refineduraniumore", 1) : GTCXRecipeIterators.getDust(main, 1);
        if (Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS)){
            GTCXIc2cECompat.addOreWashingMachineRecipe("crushed" + main.getDisplayName(), 1, GTCXMaterialGen.getPurifiedCrushedOre(main, 1), outputWashSide, GTMaterialGen.getDust(GTCXMaterial.Stone, 1));
            GTCXIc2cECompat.addThermalCentrifugeRecipe("crushedPurified" + main.getDisplayName(), 1, 400, uranium, outputThermalSide);
        } else {
            GTCXTileBath.addRecipe("crushed" + main.getDisplayName(), 1, GTMaterialGen.getFluidStack("water", 1000), 800, GTCXMaterialGen.getPurifiedCrushedOre(main, 1), outputWashSide, GTMaterialGen.getDust(GTCXMaterial.Stone, 1));
            GTCXTileCentrifuge.addRecipe("crushedPurified" + main.getDisplayName(), 1, GTCXTileCentrifuge.totalEu(1280), new ItemStack[]{ GTCXRecipeIterators.getDust(main, 1), outputThermalSide });
        }
        if (GTCXConfiguration.general.cauldronOreWashing){
            GTCXRecipeLists.CAULDRON_RECIPE_LIST.addRecipe(Collections.singletonList(input("crushed" + main.getDisplayName(), 1)), new MachineOutput(null, GTCXMaterialGen.getPurifiedCrushedOre(main, 1), outputWashSide, GTMaterialGen.getDust(GTCXMaterial.Stone, 1)), GTCXMaterialGen.getCrushedOre(main, 1).getUnlocalizedName(), 0);
        }
        TileEntityMacerator.addRecipe("crushed" + main.getDisplayName(), 1, GTCXRecipeIterators.getDust(main, 1));
        TileEntityMacerator.addRecipe("crushedPurified" + main.getDisplayName(), 1, GTCXRecipeIterators.getDust(main, 1));
    }

    public static ItemStack getPurifiedOrDust(GTMaterial material, int amount){
        if (GTCXConfiguration.general.crushedOres && !GTCXConfiguration.general.gt2Mode){
            return GTCXMaterialGen.getPurifiedCrushedOre(material, amount);
        } else {
            return GTCXRecipeIterators.getDust(material, amount);
        }
    }

    public static ItemStack getCrushedOrDust(GTMaterial material, int amount){
        if (GTCXConfiguration.general.crushedOres && !GTCXConfiguration.general.gt2Mode){
            return GTCXMaterialGen.getCrushedOre(material, amount);
        } else {
            return GTCXRecipeIterators.getDust(material, amount);
        }
    }

    public static void initCentrifugeRecipes(){

    }

    public static void removals() {
        // Remove smelting from mods who dont respect my authority
        if (GTCXConfiguration.general.ingotsRequireBlastFurnace) {
            for (Item item : Item.REGISTRY) {
                NonNullList<ItemStack> items = NonNullList.create();
                item.getSubItems(CreativeTabs.SEARCH, items);
                for (ItemStack stack : items) {
                    if (!stack.isEmpty() && OreDictionary.getOreIDs(stack).length > 0) {
                        for(int i = 0; i < OreDictionary.getOreIDs(stack).length; ++i) {
                            if (OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[i]).startsWith("dust") || OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[i]).startsWith("crushed") || OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[i]).startsWith("crushedPurified")) {
                                GTCXTileMicrowave.explodeList.add(stack);
                                break;
                            }
                        }
                    }
                    if (GTHelperStack.matchOreDict(stack,"ingotOsmium")
                            || GTHelperStack.matchOreDict(stack,"ingotSteel")
                            || GTHelperStack.matchOreDict(stack,"ingotStainlessSteel")
                            || GTHelperStack.matchOreDict(stack,"ingotThorium")
                            || GTHelperStack.matchOreDict(stack, "ingotIridium")
                            || GTHelperStack.matchOreDict(stack, "ingotTungsten")
                            || GTHelperStack.matchOreDict(stack, "ingotChrome")
                            || GTHelperStack.matchOreDict(stack, "ingotTitanium")
                            || GTHelperStack.matchOreDict(stack, "ingotAluminium")
                            || GTHelperStack.matchOreDict(stack, "ingotAluminum")
                            || GTHelperStack.matchOreDict(stack, "ingotSilicon")
                            || GTHelperStack.matchOreDict(stack, "itemSilicon")) {
                        GTHelperStack.removeSmelting(stack);
                    }
                }
            }
        }
    }



    public static RecipeModifierHelpers.IRecipeModifier[] totalCentrifugeEu(int amount) {
        return GTTileCentrifuge.totalEu(amount);
    }
}
