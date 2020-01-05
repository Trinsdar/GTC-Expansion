package gtc_expansion.recipes;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXItems;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.GTCXTileMicrowave;
import gtclassic.api.helpers.GTHelperMods;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTConfig;
import gtclassic.common.GTItems;
import gtclassic.common.recipe.GTRecipeProcessing;
import gtclassic.common.tile.GTTileCentrifuge;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.machine.low.TileEntityCompressor;
import ic2.core.block.machine.low.TileEntityExtractor;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
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

public class GTCXRecipeProcessing {
    public static void init(){
        initCentrifugeRecipes();
        initIc2Recipes();
        initFurnaceRecipes();
    }

    public static void initFurnaceRecipes(){
        Item clay = GTCXConfiguration.general.unfiredBricks ? GTCXItems.unfiredFireBrick : GTCXItems.fireClayBall;
        GameRegistry.addSmelting(GTMaterialGen.get(clay), GTMaterialGen.get(GTCXItems.fireBrick), 0.1F);
        if (GTCXConfiguration.general.unfiredBricks){
            GameRegistry.addSmelting(GTMaterialGen.get(GTCXItems.unfiredBrick), GTMaterialGen.get(Items.BRICK), 0.1F);
        }
        GameRegistry.addSmelting(GTCXBlocks.oreSheldonite, GTMaterialGen.getIngot(GTMaterial.Platinum, 1), 1.0F);
        GameRegistry.addSmelting(GTCXBlocks.oreCassiterite, GTMaterialGen.getIc2(Ic2Items.tinIngot, 2), 0.5F);
        GameRegistry.addSmelting(GTCXBlocks.oreTetrahedrite, Ic2Items.copperIngot, 0.5F);
        GameRegistry.addSmelting(GTMaterialGen.getDust(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 6), 0.5F);
    }

    public static void initIc2Recipes(){
        TileEntityMacerator.addRecipe("oreRedstone", 1, GTMaterialGen.get(Items.REDSTONE, 8));
        if (!Loader.isModLoaded(GTHelperMods.IC2_EXTRAS) || !GTConfig.modcompat.compatIc2Extras){
            GTRecipeProcessing.maceratorUtil("orePyrite", 1, GTMaterialGen.getDust(GTMaterial.Pyrite, 5));
            GTRecipeProcessing.maceratorUtil("oreCinnabar", 1, GTMaterialGen.getDust(GTCXMaterial.Cinnabar, 3));
            GTRecipeProcessing.maceratorUtil("oreSphalerite", 1, GTMaterialGen.getDust(GTCXMaterial.Sphalerite, 4));
            GTRecipeProcessing.maceratorUtil("oreTungstate", 1, GTMaterialGen.getDust(GTMaterial.Tungsten, 2));
        }
        GTRecipeProcessing.maceratorUtil("oreSodalite", 1, GTMaterialGen.getDust(GTMaterial.Sodalite, 12));
        TileEntityExtractor.addRecipe("oreOlivine", 1, GTMaterialGen.getGem(GTCXMaterial.Olivine, 3));
        if (GTCXConfiguration.general.usePlates && (!Loader.isModLoaded(GTHelperMods.IC2_EXTRAS) || !GTConfig.modcompat.compatIc2Extras)){
            TileEntityCompressor.addRecipe("plateCopper", 8, Ic2Items.denseCopperPlate);
        }
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
        TileEntityExtractor.addRecipe(GTCXItems.acidBattery.getEmpty(), GTMaterialGen.get(GTCXItems.batteryHull));
        TileEntityExtractor.addRecipe(GTCXItems.mercuryBattery.getEmpty(), GTMaterialGen.get(GTCXItems.batteryHull));
        TileEntityExtractor.addRecipe(GTCXItems.largeAcidBattery.getEmpty(), GTMaterialGen.get(GTCXItems.largeBatteryHull));
        TileEntityExtractor.addRecipe(GTCXItems.largeMercuryBattery.getEmpty(), GTMaterialGen.get(GTCXItems.largeBatteryHull));
    }

    public static void initCentrifugeRecipes(){
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.GOLDEN_APPLE, 1), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_INGOT, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_INGOT, 64), GTMaterialGen.getTube(GTMaterial.Methane, 2));
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.GOLDEN_CARROT, 1), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_NUGGET, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.SPECKLED_MELON, 8), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_NUGGET, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe("dustEndstone", 64, 2, totalCentrifugeEu(100000), GTMaterialGen.get(Blocks.SAND, 48), GTMaterialGen.getTube(GTMaterial.Helium3, 4), GTMaterialGen.getTube(GTMaterial.Helium, 4), GTMaterialGen.getDust(GTMaterial.Tungsten, 1));
        GTTileCentrifuge.addRecipe("dustRedGarnet", 16, 0, totalCentrifugeEu(15000), GTMaterialGen.getDust(GTCXMaterial.Pyrope, 3), GTMaterialGen.getDust(GTCXMaterial.Almandine, 5), GTMaterialGen.getDust(GTCXMaterial.Spessartine, 8));
        GTTileCentrifuge.addRecipe("dustYellowGarnet", 16, 0, totalCentrifugeEu(17500), GTMaterialGen.getDust(GTCXMaterial.Uvarovite, 3), GTMaterialGen.getDust(GTCXMaterial.Andradite, 5), GTMaterialGen.getDust(GTCXMaterial.Grossular, 8));
        GTTileCentrifuge.addRecipe("dustDarkAshes", 2, 0, totalCentrifugeEu(1250), GTMaterialGen.getDust(GTCXMaterial.Ashes, 1), GTMaterialGen.getDust(GTCXMaterial.Slag, 1));
        IRecipeInput ashes = new RecipeInputCombined(2, new RecipeInputOreDict("dustAshes", 2), new RecipeInputOreDict("dustAsh", 2));
        GTTileCentrifuge.addRecipe(new IRecipeInput[]{ashes}, totalCentrifugeEu(1250), GTMaterialGen.getDust(GTMaterial.Carbon, 1));
        GTTileCentrifuge.addRecipe("dustSlag", 16, 0, totalCentrifugeEu(4000), GTMaterialGen.get(Items.IRON_NUGGET, 3), GTMaterialGen.get(Items.GOLD_NUGGET, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 4), GTMaterialGen.getDust(GTMaterial.Phosphorus, 4));
        GTTileCentrifuge.addRecipe("dustRedRock", 16, 0, totalCentrifugeEu(2000), GTMaterialGen.getDust(GTMaterial.Calcite, 8), GTMaterialGen.getDust(GTMaterial.Flint, 4), GTMaterialGen.getIc2(Ic2Items.clayDust, 4));
        GTTileCentrifuge.addRecipe("dustMarble", 8, 0, totalCentrifugeEu(5275), GTMaterialGen.getDust(GTCXMaterial.Magnesium, 1), GTMaterialGen.getDust(GTMaterial.Calcite, 7));
        GTTileCentrifuge.addRecipe("dustBasalt", 16, 0, totalCentrifugeEu(24000), GTMaterialGen.getDust(GTCXMaterial.Olivine, 1), GTMaterialGen.getDust(GTMaterial.Calcite, 3), GTMaterialGen.getDust(GTMaterial.Flint, 8), GTMaterialGen.getDust(GTCXMaterial.DarkAshes, 4));
        GTTileCentrifuge.addRecipe("dustBrass", 4,0, totalCentrifugeEu(7500), GTMaterialGen.getDust(GTCXMaterial.Zinc, 1), GTMaterialGen.getIc2(Ic2Items.copperDust, 3));
        GTTileCentrifuge.addRecipe("dustInvar", 3,0, totalCentrifugeEu(7500), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTMaterialGen.getIc2(Ic2Items.ironDust, 2));
        GTTileCentrifuge.addRecipe("dustConstantan", 3,0, totalCentrifugeEu(7500), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTMaterialGen.getIc2(Ic2Items.copperDust, 2));
        GTTileCentrifuge.addRecipe("dustTetrahedrite", 8, 0, totalCentrifugeEu(18240), GTMaterialGen.getIc2(Ic2Items.copperDust, 3), GTMaterialGen.getDust(GTCXMaterial.Antimony, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 3));
        GTTileCentrifuge.addRecipe("dustBatteryAlloy", 5, 0, totalCentrifugeEu(37800), GTMaterialGen.getDust(GTCXMaterial.Antimony, 1), GTMaterialGen.getDust(GTCXMaterial.Lead, 4));
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
