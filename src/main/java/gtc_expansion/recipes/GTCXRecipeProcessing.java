package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXValues;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.GTCXTileMicrowave;
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
        GameRegistry.addSmelting(Ic2Items.machine.copy(), GTMaterialGen.getIc2(Ic2Items.refinedIronIngot, 6), 0.0F);
        if (GTCXConfiguration.general.unfiredBricks){
            GameRegistry.addSmelting(GTMaterialGen.get(GTCXItems.unfiredBrick), GTMaterialGen.get(Items.BRICK), 0.1F);
        }
        GameRegistry.addSmelting(GTCXBlocks.oreSheldonite, GTMaterialGen.getIngot(GTMaterial.Platinum, 1), 1.0F);
        GameRegistry.addSmelting(GTCXBlocks.oreCassiterite, GTMaterialGen.getIc2(Ic2Items.tinIngot, 2), 0.5F);
        GameRegistry.addSmelting(GTCXBlocks.oreTetrahedrite, Ic2Items.copperIngot, 0.5F);
        GameRegistry.addSmelting(GTMaterialGen.getDust(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 6), 0.5F);
        GameRegistry.addSmelting(GTMaterialGen.getDust(GTCXMaterial.Cassiterite, 1), GTMaterialGen.getIc2(Ic2Items.tinIngot, 1), 0.5F);
        GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Cassiterite, 1), GTMaterialGen.getIc2(Ic2Items.tinIngot, 1), 0.5F);
        GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Cassiterite, 1), GTMaterialGen.getIc2(Ic2Items.tinIngot, 1), 0.5F);
    }

    public static void initIc2Recipes(){
        TileEntityExtractor.addRecipe("oreDiamond", 1, GTMaterialGen.get(Items.DIAMOND, 2));
        TileEntityExtractor.addRecipe("oreEmerald", 1, GTMaterialGen.get(Items.EMERALD, 2));
        TileEntityExtractor.addRecipe("oreCoal", 1, GTMaterialGen.get(Items.COAL, 2));
        TileEntityExtractor.addRecipe("oreQuartz", 1, GTMaterialGen.get(Items.QUARTZ, 2));
        TileEntityExtractor.addRecipe("oreRuby", 1, GTMaterialGen.getGem(GTMaterial.Ruby, 2));
        TileEntityExtractor.addRecipe("oreSapphire", 1, GTMaterialGen.getGem(GTMaterial.Sapphire, 2));
        TileEntityExtractor.addRecipe("oreOlivine", 1, GTMaterialGen.getGem(GTCXMaterial.Olivine, 2));
        TileEntityMacerator.addRecipe("oreRedstone", 1, GTMaterialGen.get(Items.REDSTONE, 8));
        if (!Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) || !GTConfig.modcompat.compatIc2Extras){
            GTRecipe.maceratorUtil("orePyrite", 1, GTMaterialGen.getDust(GTMaterial.Pyrite, 4));
            GTRecipe.maceratorUtil("oreCinnabar", 1, GTMaterialGen.getDust(GTCXMaterial.Cinnabar, 3));
            GTRecipe.maceratorUtil("oreSphalerite", 1, GTMaterialGen.getDust(GTCXMaterial.Sphalerite, 2));
            GTRecipe.maceratorUtil("oreTungstate", 1, GTMaterialGen.getDust(GTMaterial.Tungsten, 4));
        }
        if (OreDictionary.doesOreNameExist("stoneMarble")){
            GTRecipe.maceratorUtil("stoneMarble", 1, GTMaterialGen.getDust(GTCXMaterial.Marble, 1));
        }
        TileEntityMacerator.addRecipe(GTMaterialGen.get(GTCXItems.constantanHeatingCoil), GTMaterialGen.getDust(GTCXMaterial.Constantan, 3));
        TileEntityMacerator.addRecipe(GTMaterialGen.get(GTCXItems.kanthalHeatingCoil), GTMaterialGen.getDust(GTCXMaterial.Kanthal, 4));
        TileEntityMacerator.addRecipe(GTMaterialGen.get(GTCXItems.nichromeHeatingCoil), GTMaterialGen.getDust(GTCXMaterial.Nichrome, 4));
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTItems.sprayCanEmpty), GTCXRecipe.input("sand", 16), GTMaterialGen.get(GTCXItems.hardeningSpray));
        //TileEntityMacerator.addRecipe(GTRecipeCraftingHandler.combineRecipeObjects(GTMaterialGen.get(GTCXItems.mold), GTMaterialGen.get(GTCXItems.moldBlock), GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.get(GTCXItems.moldCell), GTMaterialGen.get(GTCXItems.moldGear), GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.get(GTCXItems.moldLargePipe), GTMaterialGen.get(GTCXItems.moldMediumPipe), GTMaterialGen.get(GTCXItems.moldNugget), GTMaterialGen.get(GTCXItems.moldPlate), GTMaterialGen.get(GTCXItems.moldRod), GTMaterialGen.get(GTCXItems.moldSmallPipe), GTMaterialGen.get(GTCXItems.moldWire)), GTMaterialGen.getDust(GTCXMaterial.Steel, 4));
        GTRecipe.maceratorUtil("oreSodalite", 1, GTMaterialGen.getDust(GTMaterial.Sodalite, 12));
        GTRecipe.maceratorUtil("gemDiamond", 1, GTMaterialGen.getDust(GTCXMaterial.Diamond, 1));
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
        TileEntityExtractor.addRecipe(GTCXItems.acidBattery.getEmpty(), GTMaterialGen.get(GTCXItems.batteryHull));
        TileEntityExtractor.addRecipe(GTCXItems.mercuryBattery.getEmpty(), GTMaterialGen.get(GTCXItems.batteryHull));
        TileEntityExtractor.addRecipe(GTCXItems.largeAcidBattery.getEmpty(), GTMaterialGen.get(GTCXItems.largeBatteryHull));
        TileEntityExtractor.addRecipe(GTCXItems.largeMercuryBattery.getEmpty(), GTMaterialGen.get(GTCXItems.largeBatteryHull));
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
