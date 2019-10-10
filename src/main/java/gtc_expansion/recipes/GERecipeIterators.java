package gtc_expansion.recipes;

import gtc_expansion.GEConfiguration;
import gtc_expansion.item.tools.GEToolGen;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.util.GEIc2cECompat;
import gtclassic.GTConfig;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;
import gtclassic.material.GTMaterialGen;
import gtclassic.util.GTValues;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.core.IC2;
import ic2.core.block.machine.low.TileEntityCompressor;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class GERecipeIterators {
    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    public static void init(){
        for (GTMaterial mat : GTMaterial.values()){
            createSmallDustRecipe(mat);
            createPlateRecipe(mat);
            createNuggetRecipe(mat);
            createHullRecipe(mat);
            if (Loader.isModLoaded(GTValues.IC2_EXTRAS) && GTConfig.compatIc2Extras){
                createTinyDustRecipe(mat);
            }
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
        createFullToolRecipes(GEMaterial.Steel, false);
        createFullToolRecipes(GEMaterial.TungstenSteel, false);
        createFullToolRecipes(GEMaterial.Ruby, true);
        createFullToolRecipes(GEMaterial.Sapphire, true);
    }

    public static void createTinyDustRecipe(GTMaterial mat) {
        String tinyDust = "dustTiny" + mat.getDisplayName();
        String dust = "dust" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterialFlag.DUST)) {
            if (mat.hasFlag(GEMaterial.tinydust)) {
                // Block crafting recipe
                recipes.addRecipe(GTMaterialGen.getDust(mat, 1), "XXX", "XXX", "XXX", 'X',
                        tinyDust);
                TileEntityCompressor.addRecipe(tinyDust, 9, GTMaterialGen.getDust(mat, 1), 0.0F);
                recipes.addRecipe(GTMaterialGen.getStack(mat, GEMaterial.tinydust, 9), "D ", 'D', dust);
            }
        }
    }

    public static void createSmallDustRecipe(GTMaterial mat) {
        String smallDust = "dustSmall" + mat.getDisplayName();
        String dust = "dust" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterialFlag.DUST)) {
            if (mat.hasFlag(GEMaterial.smalldust)) {
                // Block crafting recipe
                recipes.addRecipe(GTMaterialGen.getDust(mat, 1), "XX", "XX", 'X',
                        smallDust);
                TileEntityCompressor.addRecipe(smallDust, 4, GTMaterialGen.getDust(mat, 1), 0.0F);
                // Inverse
                recipes.addRecipe(GTMaterialGen.getStack(mat, GEMaterial.smalldust, 4), " D", 'D', dust);
            }
        }
    }

    public static void createNuggetRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        if (mat.hasFlag(GEMaterial.nugget)) {
            recipes.addShapelessRecipe(GEMaterialGen.getNugget(mat, 9), ingot);
        }
    }

    public static void createPlateRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        String plate = "plate" + mat.getDisplayName();
        if (mat.hasFlag(GEMaterial.plate)) {
            // Plate crafting recipe
            if (GEConfiguration.harderPlates) {
                recipes.addRecipe(GEMaterialGen.getPlate(mat, 1), "H", "X", "X", 'H',
                        "craftingToolForgeHammer", 'X', ingot);
            } else {
                recipes.addRecipe(GEMaterialGen.getPlate(mat, 1), "H", "X", 'H',
                        "craftingToolForgeHammer", 'X', ingot);
            }
            if (Loader.isModLoaded("ic2c_extras") && GTConfig.compatIc2Extras){
                GEIc2cECompat.addRollerRecipe(ingot, 1, GEMaterialGen.getPlate(mat, 1));
            }
            // If a dust is present create a maceration recipe
            if (mat.hasFlag(GTMaterialFlag.DUST)) {
                TileEntityMacerator.addRecipe(plate, 1, GTMaterialGen.getDust(mat, 1), 0.0F);
            }
        }
    }

    public static void createHullRecipe(GTMaterial mat) {
        String plate = "plate" + mat.getDisplayName();
        boolean steel = false;
        if (mat.equals(GEMaterial.Steel)){
            steel = IC2.config.getFlag("SteelRecipes");
        }
        if (mat.hasFlag(GEMaterial.hull) && mat.hasFlag(GEMaterial.plate) && !steel) {
            // Hull crafting recipe
            recipes.addRecipe(GEMaterialGen.getHull(mat, 1), "PPP", "PWP", "PPP", 'P', plate, 'W', "craftingToolWrench");
            //Ingots from hulls
            recipes.addShapelessRecipe(GTMaterialGen.getIngot(mat, 8), GEMaterialGen.getHull(mat, 1));
        }
    }

    public static void createFullToolRecipes(GTMaterial mat, boolean gemInput){
        String ingot = "ingot" + mat.getDisplayName();
        String plate = "plate" + mat.getDisplayName();
        String gem = "gem" + mat.getDisplayName();
        if (gemInput){
            ingot = gem;
            plate = gem;
        }
        String stick = "stickWood";
        GEToolGen G = new GEToolGen();
        recipes.addRecipe(G.getPickaxe(mat), "PII", "FSH", " S ", 'P', plate, 'I', ingot, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
        recipes.addRecipe(G.getAxe(mat), "PIH", "PS ", "FS ", 'P', plate, 'I', ingot, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
        recipes.addRecipe(G.getShovel(mat), "FPH", " S ", " S ", 'P', plate, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
        recipes.addRecipe(G.getSword(mat), " P ", "FPH", " S ", 'P', plate, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
        if (!gemInput){
            recipes.addRecipe(G.getFile(mat), "P", "P", "S", 'P', plate, 'S', stick);
            recipes.addRecipe(G.getHammer(mat), "III", "III", " S ", 'I', ingot, 'S', stick);
            recipes.addRecipe(G.getWrench(mat), "I I", "III", " I ", 'I', ingot);
        }
    }

    public static void dustUtil(ItemStack stack, GTMaterial material) {
        String smalldust = "dustSmall" + material.getDisplayName();
        recipes.addShapelessRecipe(stack, smalldust, smalldust, smalldust, smalldust);
        TileEntityCompressor.addRecipe(smalldust, 4, GTMaterialGen.getIc2(stack, 1), 0.0F);
    }

    public static void ingotUtil(ItemStack stack, GTMaterial material) {
        String nugget = "nugget" + material.getDisplayName();
        recipes.addRecipe(stack, "XXX", "XXX", "XXX", 'X', nugget);
    }
}
