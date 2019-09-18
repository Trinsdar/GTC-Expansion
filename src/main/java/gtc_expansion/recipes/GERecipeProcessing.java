package gtc_expansion.recipes;

import gtc_expansion.GEConfiguration;
import gtc_expansion.GEItems;
import gtc_expansion.material.GEMaterial;
import gtclassic.GTConfig;
import gtclassic.helpers.GTHelperStack;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.recipe.GTRecipeProcessing;
import gtclassic.tile.GTTileCentrifuge;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.recipe.IRecipeInput;
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
import net.minecraftforge.fml.common.registry.GameRegistry;

import static gtclassic.tile.GTTileBaseMachine.input;
import static gtclassic.tile.multi.GTTileMultiBlastFurnace.addRecipe;

public class GERecipeProcessing {
    public static void init(){
        initCentrifugeRecipes();
        initBlastFurnaceRecipes();
        initIc2Recipes();
        initFurnaceRecipes();
    }

    public static void initFurnaceRecipes(){
        GameRegistry.addSmelting(GTMaterialGen.get(GEItems.unfiredFireBrick), GTMaterialGen.get(GEItems.fireBrick), 0.1F);
        GameRegistry.addSmelting(GTMaterialGen.get(GEItems.unfiredBrick), GTMaterialGen.get(Items.BRICK), 0.1F);
    }

    public static void initIc2Recipes(){
        TileEntityMacerator.addRecipe("oreRedstone", 1, GTMaterialGen.get(Items.REDSTONE, 10));
        GTRecipeProcessing.maceratorUtil("orePyrite", 1, GTMaterialGen.getDust(GEMaterial.Pyrite, 5));
        GTRecipeProcessing.maceratorUtil("oreCinnabar", 1, GTMaterialGen.getDust(GEMaterial.Cinnabar, 3));
        GTRecipeProcessing.maceratorUtil("oreSphalerite", 1, GTMaterialGen.getDust(GEMaterial.Sphalerite, 4));
        GTRecipeProcessing.maceratorUtil("oreSheldonite", 1, GTMaterialGen.getDust(GEMaterial.Platinum, 2));
        GTRecipeProcessing.maceratorUtil("oreSodalite", 1, GTMaterialGen.getDust(GEMaterial.Sodalite, 12));
        TileEntityExtractor.addRecipe("oreOlivine", 1, GTMaterialGen.getGem(GEMaterial.Olivine, 3));
        if (GEConfiguration.usePlates){
            TileEntityCompressor.addRecipe("plateCopper", 8, Ic2Items.denseCopperPlate);
        }
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

    public static void initBlastFurnaceRecipes(){
        addRecipe(new IRecipeInput[]{input("ingotTungsten", 1), input("ingotSteel", 1)}, 256000, GTMaterialGen.getStack(GEMaterial.TungstenSteel, GEMaterial.hotIngot, 2), GTMaterialGen.getDust(GEMaterial.DarkAshes, 4));
        addRecipe(new IRecipeInput[] {
                input("dustTungsten", 1) }, 256000, GTMaterialGen.getStack(GEMaterial.Tungsten, GEMaterial.hotIngot, 1));
        addRecipe(new IRecipeInput[] {
                input("dustIridium", 1) }, 256000, GTMaterialGen.getStack(GEMaterial.Iridium, GEMaterial.hotIngot, 1));
        addRecipe(new IRecipeInput[] { input("oreIridium", 1) }, 256000, GTMaterialGen.getStack(GEMaterial.Iridium, GEMaterial.hotIngot, 1));
        addRecipe(new IRecipeInput[] {
                input(GTMaterialGen.getIc2(Ic2Items.iridiumOre, 1)) }, 256000, GTMaterialGen.getStack(GEMaterial.Iridium, GEMaterial.hotIngot, 1));
        addRecipe(new IRecipeInput[] {
                input("dustGalena", 2) }, 256000, GTMaterialGen.getIngot(GEMaterial.Lead, 1), GTMaterialGen.getIc2(Ic2Items.silverIngot, 1));
        addRecipe(new IRecipeInput[] {
                input("dustOsmium", 1) }, 256000, GTMaterialGen.getStack(GEMaterial.Osmium, GEMaterial.hotIngot, 1));
        addRecipe(new IRecipeInput[] {
                input("dustPlatinum", 1) }, 256000, GTMaterialGen.getIngot(GEMaterial.Platinum, 1));
        addRecipe(new IRecipeInput[] {
                input("dustThorium", 1) }, 256000, GTMaterialGen.getIngot(GEMaterial.Thorium, 1));
    }

    public static void removals() {
        // Remove smelting from mods who dont respect my authority
        if (GTConfig.ingotsRequireBlastFurnace) {
            for (Item item : Item.REGISTRY) {
                NonNullList<ItemStack> items = NonNullList.create();
                item.getSubItems(CreativeTabs.SEARCH, items);
                for (ItemStack stack : items) {
                    if (GTHelperStack.matchOreDict(stack,"ingotOsmium") || GTHelperStack.matchOreDict(stack,"ingotPlatinum")
                            || GTHelperStack.matchOreDict(stack,"ingotStainlessSteel") || GTHelperStack.matchOreDict(stack,"ingotThorium")) {
                        gtclassic.recipe.GTRecipeProcessing.removeSmelting(stack);
                    }
                }
            }
        }
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalCentrifugeEu(int amount) {
        return GTTileCentrifuge.totalEu(amount);
    }
}
