package gtc_expansion.recipes;

import gtc_expansion.GEBlocks;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.tile.multi.GETileMultiPrimitiveBlastFurnace;
import gtc_expansion.util.GEIc2cECompat;
import gtclassic.GTConfig;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;
import gtclassic.material.GTMaterialGen;
import gtclassic.recipe.GTRecipe;
import gtclassic.recipe.GTRecipeProcessing;
import gtclassic.util.GTValues;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.machine.low.TileEntityCompressor;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static gtclassic.recipe.GTRecipeMods.input;
import static gtclassic.recipe.GTRecipeMods.metal;

public class GERecipeMods {
    public static void init(){
        if (GTConfig.compatTwilightForest && Loader.isModLoaded(GTValues.TFOREST)) {
            GETileMultiPrimitiveBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input(GTMaterialGen.getModItem(GTValues.TFOREST, "liveroot")),
                    input("nuggetGold", 1) }, 400, GTMaterialGen.getModItem(GTValues.TFOREST, "ironwood_ingot", 2));
        }

        if (GTConfig.compatIc2Extras && Loader.isModLoaded(GTValues.IC2_EXTRAS)){
            addCrushedOreRecipes(GEMaterial.Tetrahedrite, GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "coppertinydust", 2), GEMaterialGen.getTinyDust(GEMaterial.Zinc, 1));
            addCrushedOreRecipes(GEMaterial.Galena, GEMaterialGen.getTinyDust(GEMaterial.Sulfur, 2), GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "silvertinydust"));
            addCrushedOreRecipes(GEMaterial.Cinnabar, GEMaterialGen.getTinyDust(GEMaterial.Redstone, 1), GEMaterialGen.getTinyDust(GEMaterial.Sulfur, 1));
            addCrushedOreRecipes(GEMaterial.Sphalerite, GEMaterialGen.getTinyDust(GEMaterial.Zinc, 1), GEMaterialGen.getTinyDust(GEMaterial.GarnetYellow, 1));
            addCrushedOreRecipes(GEMaterial.Pyrite, GEMaterialGen.getTinyDust(GEMaterial.Sulfur, 1), GEMaterialGen.getTinyDust(GEMaterial.Phosphorus, 1));
            addCrushedOreRecipes(GEMaterial.Bauxite, GEMaterialGen.getTinyDust(GEMaterial.Grossular, 1), GEMaterialGen.getTinyDust(GEMaterial.Titanium, 1));
            addCrushedOreRecipes(GEMaterial.Platinum, GEMaterialGen.getTinyDust(GEMaterial.Sulfur, 2), GEMaterialGen.getTinyDust(GEMaterial.Nickel, 1));
            addCrushedOreRecipes(GEMaterial.Tungsten, GEMaterialGen.getTinyDust(GEMaterial.Manganese, 1), GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "silvertinydust"));
            addCrushedOreRecipes(GEMaterial.Iridium, GEMaterialGen.getTinyDust(GEMaterial.Platinum, 1), GEMaterialGen.getTinyDust(GEMaterial.Osmium, 1));
            GTRecipeProcessing.maceratorUtil("oreGalena", 1, GEMaterialGen.getCrushedOre(GEMaterial.Galena, 2));
            GTRecipeProcessing.maceratorUtil("oreTetrahedrite", 1, GEMaterialGen.getCrushedOre(GEMaterial.Tetrahedrite, 2));
            GTRecipeProcessing.maceratorUtil("oreCassiterite", 1, GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "tincrushedore", 4));
            GTRecipeProcessing.maceratorUtil("orePyrite", 1, GEMaterialGen.getCrushedOre(GEMaterial.Pyrite, 5));
            GTRecipeProcessing.maceratorUtil("oreCinnabar", 1, GEMaterialGen.getCrushedOre(GEMaterial.Cinnabar, 3));
            GTRecipeProcessing.maceratorUtil("oreSphalerite", 1, GEMaterialGen.getCrushedOre(GEMaterial.Sphalerite, 4));
            GTRecipeProcessing.maceratorUtil("orePlatinum", 1, GEMaterialGen.getCrushedOre(GEMaterial.Platinum, 2));
            GTRecipeProcessing.maceratorUtil("oreTungstate", 1, GEMaterialGen.getCrushedOre(GEMaterial.Tungsten, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreBauxite", 1));
            TileEntityMacerator.addRecipe("oreBauxite", 1, GEMaterialGen.getCrushedOre(GEMaterial.Bauxite, 4));
            ClassicRecipes.macerator.removeRecipe(input("oreIridium", 1));
            GTRecipeProcessing.maceratorUtil("oreIridium", 1, GEMaterialGen.getCrushedOre(GEMaterial.Iridium, 2));
            GameRegistry.addSmelting(GEMaterialGen.getCrushedOre(GEMaterial.Tetrahedrite, 1), Ic2Items.copperIngot, 0.5F);
            GameRegistry.addSmelting(GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Tetrahedrite, 1), Ic2Items.copperIngot, 0.5F);
            GameRegistry.addSmelting(GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Platinum, 1), GTMaterialGen.getIngot(GEMaterial.Platinum, 1), 1.0F);
            GameRegistry.addSmelting(GEMaterialGen.getCrushedOre(GEMaterial.Platinum, 1), GTMaterialGen.getIngot(GEMaterial.Platinum, 1), 1.0F);
        }
    }

    public static void addCrushedOreRecipes(GTMaterial main, ItemStack outputWashSide, ItemStack outputThermalSide){
        GEIc2cECompat.addOreWashingMachineRecipe("crushed" + main.getDisplayName(), 1, GEMaterialGen.getPurifiedCrushedOre(main, 1), outputWashSide, GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "stonedust"));
        GEIc2cECompat.addThermalCentrifugeRecipe("crushedPurified" + main.getDisplayName(), 1, 400, GTMaterialGen.getDust(main, 1), outputThermalSide);
        GEIc2cECompat.addThermalCentrifugeRecipe("crushed" + main.getDisplayName(), 1, 600, GTMaterialGen.getDust(main, 1), outputThermalSide, GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "stonedust"));
        TileEntityMacerator.addRecipe("crushed" + main.getDisplayName(), 1, GTMaterialGen.getDust(main, 1));
        TileEntityMacerator.addRecipe("crushedPurified" + main.getDisplayName(), 1, GTMaterialGen.getDust(main, 1));
    }

    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

}
