package gtc_expansion.recipes;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEConfiguration;
import gtc_expansion.GEItems;
import gtc_expansion.material.GEMaterial;
import gtclassic.GTConfig;
import gtclassic.GTItems;
import gtclassic.helpers.GTHelperStack;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.recipe.GTRecipeProcessing;
import gtclassic.tile.GTTileBaseMachine;
import gtclassic.tile.GTTileCentrifuge;
import gtclassic.util.GTValues;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.RecipeModifierHelpers;
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

public class GERecipeProcessing {
    public static void init(){
        initCentrifugeRecipes();
        initIc2Recipes();
        initFurnaceRecipes();
        initLiquidFuelRecipes();
    }

    public static void initLiquidFuelRecipes(){
        ClassicRecipes.fluidGenerator.addEntry(GTMaterialGen.getFluid(GEMaterial.Diesel), 4000, 30);
        ClassicRecipes.fluidGenerator.addEntry(GTMaterialGen.getFluid(GEMaterial.NitroDiesel), 8000, 30);
        ClassicRecipes.fluidGenerator.addEntry(GTMaterialGen.getFluid(GEMaterial.Gasoline), 4000, 30);
        ClassicRecipes.fluidGenerator.addEntry(GTMaterialGen.getFluid(GEMaterial.Naphtha), 4000, 30);
        ClassicRecipes.fluidGenerator.addEntry(GTMaterialGen.getFluid(GEMaterial.Propane), 3000, 16);
        ClassicRecipes.fluidGenerator.addEntry(GTMaterialGen.getFluid(GEMaterial.NitroCoalFuel), 4000, 12);
    }

    public static void initFurnaceRecipes(){
        Item clay = GEConfiguration.unfiredBricks ? GEItems.unfiredFireBrick : GEItems.fireClayBall;
        GameRegistry.addSmelting(GTMaterialGen.get(clay), GTMaterialGen.get(GEItems.fireBrick), 0.1F);
        if (GEConfiguration.unfiredBricks){
            GameRegistry.addSmelting(GTMaterialGen.get(GEItems.unfiredBrick), GTMaterialGen.get(Items.BRICK), 0.1F);
        }
        GameRegistry.addSmelting(GEBlocks.oreSheldonite, GTMaterialGen.getIngot(GEMaterial.Platinum, 1), 1.0F);
        GameRegistry.addSmelting(GEBlocks.oreCassiterite, GTMaterialGen.getIc2(Ic2Items.tinIngot, 2), 0.5F);
        GameRegistry.addSmelting(GEBlocks.oreTetrahedrite, Ic2Items.copperIngot, 0.5F);
        GameRegistry.addSmelting(GTMaterialGen.getDust(GEMaterial.Tetrahedrite, 1), Ic2Items.copperIngot, 0.5F);
    }

    public static void initIc2Recipes(){
        TileEntityMacerator.addRecipe("oreRedstone", 1, GTMaterialGen.get(Items.REDSTONE, 10));
        if (!Loader.isModLoaded(GTValues.IC2_EXTRAS) || !GTConfig.compatIc2Extras){
            GTRecipeProcessing.maceratorUtil("orePyrite", 1, GTMaterialGen.getDust(GEMaterial.Pyrite, 5));
            GTRecipeProcessing.maceratorUtil("oreCinnabar", 1, GTMaterialGen.getDust(GEMaterial.Cinnabar, 3));
            GTRecipeProcessing.maceratorUtil("oreSphalerite", 1, GTMaterialGen.getDust(GEMaterial.Sphalerite, 4));
            GTRecipeProcessing.maceratorUtil("oreTungstate", 1, GTMaterialGen.getDust(GEMaterial.Tungsten, 2));
        }
        GTRecipeProcessing.maceratorUtil("oreSodalite", 1, GTMaterialGen.getDust(GEMaterial.Sodalite, 12));
        TileEntityExtractor.addRecipe("oreOlivine", 1, GTMaterialGen.getGem(GEMaterial.Olivine, 3));
        if (GEConfiguration.usePlates && (!Loader.isModLoaded(GTValues.IC2_EXTRAS) || !GTConfig.compatIc2Extras)){
            TileEntityCompressor.addRecipe("plateCopper", 8, Ic2Items.denseCopperPlate);
        }
        ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTItems.testTube), GTTileBaseMachine.input(GTMaterialGen.get(GEItems.oilberry)), GTMaterialGen.getTube(GTMaterial.Oil, 1));
    }

    public static void initCentrifugeRecipes(){
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.GOLDEN_APPLE, 1), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_INGOT, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_INGOT, 64), GTMaterialGen.getTube(GTMaterial.Methane, 2));
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.GOLDEN_CARROT, 1), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_NUGGET, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.SPECKLED_MELON, 8), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_NUGGET, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GEMaterial.Endstone, 64), 2, totalCentrifugeEu(100000), GTMaterialGen.get(Blocks.SAND, 48), GTMaterialGen.getTube(GTMaterial.Helium3, 4), GTMaterialGen.getTube(GTMaterial.Helium, 4), GTMaterialGen.getDust(GEMaterial.Tungsten, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GEMaterial.GarnetRed, 16), 0, totalCentrifugeEu(15000), GTMaterialGen.getDust(GEMaterial.Pyrope, 3), GTMaterialGen.getDust(GEMaterial.Almandine, 5), GTMaterialGen.getDust(GEMaterial.Spessartine, 8));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GEMaterial.GarnetYellow, 16), 0, totalCentrifugeEu(17500), GTMaterialGen.getDust(GEMaterial.Uvarovite, 3), GTMaterialGen.getDust(GEMaterial.Andradite, 5), GTMaterialGen.getDust(GEMaterial.Grossular, 8));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GEMaterial.DarkAshes, 2), 0, totalCentrifugeEu(1250), GTMaterialGen.getDust(GEMaterial.Ashes, 1), GTMaterialGen.getDust(GEMaterial.Slag, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GEMaterial.RedRock, 16), 0, totalCentrifugeEu(2000), GTMaterialGen.getDust(GEMaterial.Calcite, 8), GTMaterialGen.getDust(GEMaterial.Flint, 4), GTMaterialGen.getIc2(Ic2Items.clayDust, 4));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GEMaterial.Marble, 8), 0, totalCentrifugeEu(5275), GTMaterialGen.getDust(GEMaterial.Magnesium, 1), GTMaterialGen.getDust(GEMaterial.Calcite, 7));
        //GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.Basalt, 16), 0, totalCentrifugeEu(10200), GTMaterialGen.getDust(GTMaterial2.Olivine, 1), GTMaterialGen.getDust(GTMaterial2.Calcite, 3), GTMaterialGen.getDust(GTMaterial2.Flint, 8), GTMaterialGen.getDust(GTMaterial2.DarkAshes, 4));
        GTTileCentrifuge.addRecipe("dustBrass", 4,0, totalCentrifugeEu(7500), GTMaterialGen.getDust(GEMaterial.Zinc, 1), GTMaterialGen.getIc2(Ic2Items.copperDust, 3));
        GTTileCentrifuge.addRecipe("dustInvar", 3,0, totalCentrifugeEu(7500), GTMaterialGen.getDust(GEMaterial.Nickel, 1), GTMaterialGen.getIc2(Ic2Items.ironDust, 2));
        GTTileCentrifuge.addRecipe("dustConstantan", 3,0, totalCentrifugeEu(7500), GTMaterialGen.getDust(GEMaterial.Nickel, 1), GTMaterialGen.getIc2(Ic2Items.copperDust, 2));
    }

    public static void removals() {
        // Remove smelting from mods who dont respect my authority
        if (GEConfiguration.ingotsRequireBlastFurnace) {
            for (Item item : Item.REGISTRY) {
                NonNullList<ItemStack> items = NonNullList.create();
                item.getSubItems(CreativeTabs.SEARCH, items);
                for (ItemStack stack : items) {
                    if (GTHelperStack.matchOreDict(stack,"ingotOsmium")
                            || GTHelperStack.matchOreDict(stack,"ingotSteel")
                            || GTHelperStack.matchOreDict(stack,"ingotStainlessSteel")
                            || GTHelperStack.matchOreDict(stack,"ingotThorium")
                            || GTHelperStack.matchOreDict(stack, "ingotIridium")
                            || GTHelperStack.matchOreDict(stack, "ingotTungsten")
                            || GTHelperStack.matchOreDict(stack, "ingotChrome")
                            || GTHelperStack.matchOreDict(stack, "ingotTitanium")) {
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
