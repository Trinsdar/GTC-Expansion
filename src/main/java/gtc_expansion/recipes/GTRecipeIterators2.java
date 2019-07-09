package gtc_expansion.recipes;

import gtc_expansion.material.GTMaterial2;
import gtc_expansion.material.GTMaterialGen2;
import gtclassic.GTConfig;
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

public class GTRecipeIterators2 {
    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    public static void init(){
        createGemRecipe(GTMaterial2.GarnetRed);
        createGemRecipe(GTMaterial2.GarnetYellow);
        for (GTMaterial mat : GTMaterial.values()){
            createSmallDustRecipe(mat);
            createPlateRecipe(mat);
            createNuggetRecipe(mat);
        }
        final ItemStack dustGlowstone = new ItemStack(Items.GLOWSTONE_DUST);
        final ItemStack dustGunpowder = new ItemStack(Items.GUNPOWDER);
        final ItemStack dustRedstone = new ItemStack(Items.REDSTONE);
        dustUtil(dustGlowstone, GTMaterial2.Glowstone);
        dustUtil(dustGunpowder, GTMaterial2.Gunpowder);
        dustUtil(Ic2Items.tinDust, GTMaterial2.Tin);
        dustUtil(Ic2Items.obsidianDust, GTMaterial2.Obsidian);
        dustUtil(Ic2Items.bronzeDust, GTMaterial2.Bronze);
        dustUtil(Ic2Items.coalDust, GTMaterial2.Coal);
        dustUtil(Ic2Items.silverDust, GTMaterial2.Silver);
        dustUtil(dustRedstone, GTMaterial2.Redstone);
        dustUtil(Ic2Items.clayDust, GTMaterial2.Clay);
        dustUtil(Ic2Items.goldDust, GTMaterial2.Gold);
        dustUtil(Ic2Items.copperDust, GTMaterial2.Copper);
        dustUtil(Ic2Items.netherrackDust, GTMaterial2.Netherrack);
        dustUtil(Ic2Items.ironDust, GTMaterial2.Iron);
        dustUtil(Ic2Items.charcoalDust, GTMaterial2.Charcoal);
        ingotUtil(Ic2Items.copperIngot, GTMaterial2.Copper);
        ingotUtil(Ic2Items.tinIngot, GTMaterial2.Tin);
        ingotUtil(Ic2Items.bronzeIngot, GTMaterial2.Bronze);
        ingotUtil(Ic2Items.silverIngot, GTMaterial2.Silver);
    }

    public static void createGemRecipe(GTMaterial mat) {
        String dust = "dust" + mat.getDisplayName();
        String gem = "gem" + mat.getDisplayName();
        String block = "block" + mat.getDisplayName();
        // Dust to gem
        TileEntityCompressor.addRecipe(dust, 1, GTMaterialGen.getGem(mat, 1), 0.0F);
        // Inverse
        TileEntityMacerator.addRecipe(gem, 1, GTMaterialGen.getDust(mat, 1), 0.0F);
        if (mat.hasFlag(GTMaterialFlag.GEMBLOCK)) {
            // Block and gem related logic
            recipes.addShapelessRecipe(GTMaterialGen.getGem(mat, 9), new Object[]{block});
            TileEntityCompressor.addRecipe(gem, 9, GTMaterialGen.getMaterialBlock(mat, 1), 0.0F);
            TileEntityMacerator.addRecipe(block, 1, GTMaterialGen.getDust(mat, 9), 0.0F);
            recipes.addRecipe(GTMaterialGen.getMaterialBlock(mat, 1), new Object[]{"XXX", "XXX", "XXX", 'X',
                    gem});
        }
    }

    public static void createSmallDustRecipe(GTMaterial mat) {
        String smallDust = "dustSmall" + mat.getDisplayName();
        String dust = "dust" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterialFlag.DUST)) {
            if (mat.hasFlag(GTMaterial2.smalldust)) {
                // Block crafting recipe
                recipes.addRecipe(GTMaterialGen.getDust(mat, 1), new Object[] { "XX", "XX", 'X',
                        smallDust });
                TileEntityCompressor.addRecipe(smallDust, 4, GTMaterialGen.getDust(mat, 1), 0.0F);
                // Inverse
                recipes.addShapelessRecipe(GTMaterialGen.getStack(mat, GTMaterial2.smalldust, 4), dust);
            }
        }
    }

    public static void createNuggetRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterial2.nugget)) {
            recipes.addShapelessRecipe(GTMaterialGen2.getNugget(mat, 9), new Object[] { ingot });
        }
    }

    //Tempurary measure to get rid of compile errors till I add a config system.
    public static boolean harderPlates = false;

    public static void createPlateRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        String plate = "plate" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterial2.plate) && mat != GTMaterial2.Silicon) {
            // Plate crafting recipe
            if (harderPlates) {
                recipes.addRecipe(GTMaterialGen2.getPlate(mat, 1), new Object[] { "H", "X", "X", 'H',
                        "craftingToolForgeHammer", 'X', ingot });
            } else {
                recipes.addRecipe(GTMaterialGen2.getPlate(mat, 1), new Object[] { "H", "X", 'H',
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
