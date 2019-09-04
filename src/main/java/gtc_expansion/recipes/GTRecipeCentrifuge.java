package gtc_expansion.recipes;

import gtc_expansion.material.GTMaterial2;
import gtc_expansion.material.GTMaterialGen2;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.GTTileCentrifuge;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GTRecipeCentrifuge {

    public static void init(){
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.GOLDEN_APPLE, 1), 0, totalEu(50000), GTMaterialGen.get(Items.GOLD_INGOT, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), 0, totalEu(50000), GTMaterialGen.get(Items.GOLD_INGOT, 64), GTMaterialGen.getTube(GTMaterial.Methane, 2));
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.GOLDEN_CARROT, 1), 0, totalEu(50000), GTMaterialGen.get(Items.GOLD_NUGGET, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.get(Items.SPECKLED_MELON, 8), 0, totalEu(50000), GTMaterialGen.get(Items.GOLD_NUGGET, 6), GTMaterialGen.getTube(GTMaterial.Methane, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.Endstone, 64), 2, totalEu(100000), GTMaterialGen.get(Blocks.SAND, 48), GTMaterialGen.getTube(GTMaterial.Helium3, 4), GTMaterialGen.getTube(GTMaterial.Helium, 4), GTMaterialGen.getDust(GTMaterial2.Tungsten, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.GarnetRed, 16), 0, totalEu(15000), GTMaterialGen.getDust(GTMaterial2.Pyrope, 3), GTMaterialGen.getDust(GTMaterial2.Almandine, 5), GTMaterialGen.getDust(GTMaterial2.Spessartine, 8));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.GarnetYellow, 16), 0, totalEu(17500), GTMaterialGen.getDust(GTMaterial2.Uvarovite, 3), GTMaterialGen.getDust(GTMaterial2.Andradite, 5), GTMaterialGen.getDust(GTMaterial2.Grossular, 8));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.DarkAshes, 2), 0, totalEu(1250), GTMaterialGen.getDust(GTMaterial2.Ashes, 1), GTMaterialGen.getDust(GTMaterial2.Slag, 1));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.RedRock, 16), 0, totalEu(2000), GTMaterialGen.getDust(GTMaterial2.Calcite, 8), GTMaterialGen.getDust(GTMaterial2.Flint, 4), GTMaterialGen.getIc2(Ic2Items.clayDust, 4));
        GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.Marble, 8), 0, totalEu(5275), GTMaterialGen.getDust(GTMaterial2.Magnesium, 1), GTMaterialGen.getDust(GTMaterial2.Calcite, 7));
        //GTTileCentrifuge.addRecipe(GTMaterialGen.getDust(GTMaterial2.Basalt, 16), 0, totalEu(10200), GTMaterialGen.getDust(GTMaterial2.Olivine, 1), GTMaterialGen.getDust(GTMaterial2.Calcite, 3), GTMaterialGen.getDust(GTMaterial2.Flint, 8), GTMaterialGen.getDust(GTMaterial2.DarkAshes, 4));
    }
    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return GTTileCentrifuge.totalEu(amount);
    }
}
