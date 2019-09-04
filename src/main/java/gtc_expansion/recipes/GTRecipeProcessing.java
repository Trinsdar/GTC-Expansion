package gtc_expansion.recipes;

import gtc_expansion.material.GTMaterial2;
import gtclassic.GTConfig;
import gtclassic.helpers.GTHelperStack;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.GTTileBaseMachine;
import gtclassic.tile.GTTileCentrifuge;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import static gtclassic.tile.GTTileBaseMachine.input;
import static gtclassic.tile.multi.GTTileMultiBlastFurnace.addRecipe;

public class GTRecipeProcessing {
    public static void init(){
        initCentrifugeRecipes();
        initBlastFurnaceRecipes();
    }

    public static void initIc2Recipes(){
        TileEntityMacerator.addRecipe("oreRedstone", 1, GTMaterialGen.get(Items.REDSTONE, 10));
    }

    public static void initCentrifugeRecipes(){
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.GOLDEN_APPLE, 1), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_INGOT, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_INGOT, 64), GTMaterialGen.getTube(GTMaterial.Methane, 2));
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.GOLDEN_CARROT, 1), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_NUGGET, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.SPECKLED_MELON, 8), 0, totalCentrifugeEu(50000), GTMaterialGen.get(Items.GOLD_NUGGET, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.Endstone, 64), 2, totalCentrifugeEu(100000), GTMaterialGen.get(Blocks.SAND, 48), GTMaterialGen.getTube(GTMaterial.Helium3, 4), GTMaterialGen.getTube(GTMaterial.Helium, 4), GTMaterialGen.getDust(GTMaterial2.Tungsten, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.GarnetRed, 16), 0, totalCentrifugeEu(15000), GTMaterialGen.getDust(GTMaterial2.Pyrope, 3), GTMaterialGen.getDust(GTMaterial2.Almandine, 5), GTMaterialGen.getDust(GTMaterial2.Spessartine, 8));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.GarnetYellow, 16), 0, totalCentrifugeEu(17500), GTMaterialGen.getDust(GTMaterial2.Uvarovite, 3), GTMaterialGen.getDust(GTMaterial2.Andradite, 5), GTMaterialGen.getDust(GTMaterial2.Grossular, 8));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.DarkAshes, 2), 0, totalCentrifugeEu(1250), GTMaterialGen.getDust(GTMaterial2.Ashes, 1), GTMaterialGen.getDust(GTMaterial2.Slag, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.RedRock, 16), 0, totalCentrifugeEu(2000), GTMaterialGen.getDust(GTMaterial2.Calcite, 8), GTMaterialGen.getDust(GTMaterial2.Flint, 4), GTMaterialGen.getIc2(Ic2Items.clayDust, 4));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.Marble, 8), 0, totalCentrifugeEu(5275), GTMaterialGen.getDust(GTMaterial2.Magnesium, 1), GTMaterialGen.getDust(GTMaterial2.Calcite, 7));
        //GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.Basalt, 16), 0, totalCentrifugeEu(10200), GTMaterialGen.getDust(GTMaterial2.Olivine, 1), GTMaterialGen.getDust(GTMaterial2.Calcite, 3), GTMaterialGen.getDust(GTMaterial2.Flint, 8), GTMaterialGen.getDust(GTMaterial2.DarkAshes, 4));
    }

    public static void initBlastFurnaceRecipes(){
        addRecipe(new IRecipeInput[]{input("ingotTungsten", 1), input("ingotSteel", 1)}, 256000, GTMaterialGen.getStack(GTMaterial2.TungstenSteel, GTMaterial2.hotIngot, 2), GTMaterialGen.getDust(GTMaterial2.DarkAshes, 4));
        addRecipe(new IRecipeInput[] {
                input("dustTungsten", 1) }, 256000, GTMaterialGen.getStack(GTMaterial2.Tungsten, GTMaterial2.hotIngot, 1));
        addRecipe(new IRecipeInput[] {
                input("dustIridium", 1) }, 256000, GTMaterialGen.getStack(GTMaterial2.Iridium, GTMaterial2.hotIngot, 1));
        addRecipe(new IRecipeInput[] { input("oreIridium", 1) }, 256000, GTMaterialGen.getStack(GTMaterial2.Iridium, GTMaterial2.hotIngot, 1));
        addRecipe(new IRecipeInput[] {
                input(GTMaterialGen.getIc2(Ic2Items.iridiumOre, 1)) }, 256000, GTMaterialGen.getStack(GTMaterial2.Iridium, GTMaterial2.hotIngot, 1));
        addRecipe(new IRecipeInput[] {
                input("dustGalena", 2) }, 256000, GTMaterialGen.getIngot(GTMaterial2.Lead, 1), GTMaterialGen.getIc2(Ic2Items.silverIngot, 1));
        addRecipe(new IRecipeInput[] {
                input("dustOsmium", 1) }, 256000, GTMaterialGen.getStack(GTMaterial2.Osmium, GTMaterial2.hotIngot, 1));
        addRecipe(new IRecipeInput[] {
                input("dustPlatinum", 1) }, 256000, GTMaterialGen.getIngot(GTMaterial2.Platinum, 1));
        addRecipe(new IRecipeInput[] {
                input("dustThorium", 1) }, 256000, GTMaterialGen.getIngot(GTMaterial2.Thorium, 1));
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
