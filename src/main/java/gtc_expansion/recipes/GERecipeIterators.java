package gtc_expansion.recipes;

import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;
import gtclassic.material.GTMaterialGen;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.core.block.machine.low.TileEntityCompressor;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GERecipeIterators {
    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    public static void init(){
        for (GTMaterial mat : GTMaterial.values()){
            createSmallDustRecipe(mat);
            createPlateRecipe(mat);
            createNuggetRecipe(mat);
        }
        final ItemStack dustGlowstone = new ItemStack(Items.GLOWSTONE_DUST);
        final ItemStack dustGunpowder = new ItemStack(Items.GUNPOWDER);
        final ItemStack dustRedstone = new ItemStack(Items.REDSTONE);
        dustUtil(dustGlowstone, GEMaterial.Glowstone);
        dustUtil(dustGunpowder, GEMaterial.Gunpowder);
        dustUtil(Ic2Items.tinDust, GEMaterial.Tin);
        dustUtil(Ic2Items.obsidianDust, GEMaterial.Obsidian);
        dustUtil(Ic2Items.bronzeDust, GEMaterial.Bronze);
        dustUtil(Ic2Items.coalDust, GEMaterial.Coal);
        dustUtil(Ic2Items.silverDust, GEMaterial.Silver);
        dustUtil(dustRedstone, GEMaterial.Redstone);
        dustUtil(Ic2Items.clayDust, GEMaterial.Clay);
        dustUtil(Ic2Items.goldDust, GEMaterial.Gold);
        dustUtil(Ic2Items.copperDust, GEMaterial.Copper);
        dustUtil(Ic2Items.netherrackDust, GEMaterial.Netherrack);
        dustUtil(Ic2Items.ironDust, GEMaterial.Iron);
        dustUtil(Ic2Items.charcoalDust, GEMaterial.Charcoal);
        ingotUtil(Ic2Items.copperIngot, GEMaterial.Copper);
        ingotUtil(Ic2Items.tinIngot, GEMaterial.Tin);
        ingotUtil(Ic2Items.bronzeIngot, GEMaterial.Bronze);
        ingotUtil(Ic2Items.silverIngot, GEMaterial.Silver);
    }

    public static void createSmallDustRecipe(GTMaterial mat) {
        String smallDust = "dustSmall" + mat.getDisplayName();
        String dust = "dust" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterialFlag.DUST)) {
            if (mat.hasFlag(GEMaterial.smalldust)) {
                // Block crafting recipe
                recipes.addRecipe(GTMaterialGen.getDust(mat, 1), new Object[] { "XX", "XX", 'X',
                        smallDust });
                TileEntityCompressor.addRecipe(smallDust, 4, GTMaterialGen.getDust(mat, 1), 0.0F);
                // Inverse
                recipes.addShapelessRecipe(GTMaterialGen.getStack(mat, GEMaterial.smalldust, 4), dust);
            }
        }
    }

    public static void createNuggetRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        if (mat.hasFlag(GEMaterial.nugget)) {
            recipes.addShapelessRecipe(GEMaterialGen.getNugget(mat, 9), new Object[] { ingot });
        }
    }

    //Tempurary measure to get rid of compile errors till I add a config system.
    public static boolean harderPlates = false;

    public static void createPlateRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        String plate = "plate" + mat.getDisplayName();
        if (mat.hasFlag(GEMaterial.plate) && mat != GEMaterial.Silicon) {
            // Plate crafting recipe
            if (harderPlates) {
                recipes.addRecipe(GEMaterialGen.getPlate(mat, 1), new Object[] { "H", "X", "X", 'H',
                        "craftingToolForgeHammer", 'X', ingot });
            } else {
                recipes.addRecipe(GEMaterialGen.getPlate(mat, 1), new Object[] { "H", "X", 'H',
                        "craftingToolForgeHammer", 'X', ingot });
            }
            // If a dust is present create a maceration recipe
            if (mat.hasFlag(GTMaterialFlag.DUST)) {
                TileEntityMacerator.addRecipe(plate, 1, GTMaterialGen.getDust(mat, 1), 0.0F);
            }
        }
    }

    public static void dustUtil(ItemStack stack, GTMaterial material) {
        String smalldust = "dustSmall" + material.getDisplayName();
        recipes.addShapelessRecipe(stack, new Object[] { smalldust, smalldust, smalldust, smalldust });
        TileEntityCompressor.addRecipe(smalldust, 4, GTMaterialGen.getIc2(stack, 1), 0.0F);
    }

    public static void ingotUtil(ItemStack stack, GTMaterial material) {
        String nugget = "nugget" + material.getDisplayName();
        recipes.addRecipe(stack, new Object[] { "XXX", "XXX", "XXX", 'X', nugget });
    }
}
