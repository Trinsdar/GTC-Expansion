package gtc_expansion.recipes;

import gtc_expansion.material.GTMaterial2;
import gtclassic.GTConfig;
import gtclassic.helpers.GTHelperStack;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.recipe.GTRecipeProcessing;
import gtclassic.tile.multi.GTTileMultiBlastFurnace;
import gtclassic.util.GTValues;
import ic2.api.recipe.IRecipeInput;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import static gtclassic.tile.GTTileBaseMachine.input;
import static gtclassic.tile.multi.GTTileMultiBlastFurnace.addRecipe;

public class GTRecipeBlastFurnace {
    public static void init(){
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
                        GTRecipeProcessing.removeSmelting(stack);
                    }
                }
            }
        }
    }
}
