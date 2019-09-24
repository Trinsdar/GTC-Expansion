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
            GEIc2cECompat.addOreWashingMachineRecipe("crushedTetrahedrite", 1, GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Tetrahedrite, 1), GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "coppertinydust", 2), GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "stonedust"));
            GEIc2cECompat.addOreWashingMachineRecipe("crushedGalena", 1, GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Galena, 1), GEMaterialGen.getSmallDust(GEMaterial.Sulfur, 1), GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "stonedust"));
            GEIc2cECompat.addThermalCentrifugeRecipe("crushedTetrahedrite", 1, 600, GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Tetrahedrite, 1), GEMaterialGen.getTinyDust(GEMaterial.Zinc, 1), GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "stonedust"));
            GEIc2cECompat.addThermalCentrifugeRecipe("crushedGalena", 1, 600, GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Galena, 1), GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "silvertinydust"), GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "stonedust"));
            GEIc2cECompat.addThermalCentrifugeRecipe("crushedPurifiedTetrahedrite", 1, 400, GTMaterialGen.getDust(GEMaterial.Tetrahedrite, 1), GEMaterialGen.getTinyDust(GEMaterial.Zinc, 1));
            GEIc2cECompat.addThermalCentrifugeRecipe("crushedPurifiedGalena", 1, 400, GTMaterialGen.getDust(GEMaterial.Galena, 1), GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "silvertinydust"));
            GTRecipeProcessing.maceratorUtil("oreGalena", 1, GEMaterialGen.getCrushedOre(GEMaterial.Galena, 2));
            GTRecipeProcessing.maceratorUtil("oreTetrahedrite", 1, GEMaterialGen.getCrushedOre(GEMaterial.Tetrahedrite, 2));
            GTRecipeProcessing.maceratorUtil("oreCassiterite", 1, GTMaterialGen.getModItem(GTValues.IC2_EXTRAS, "tincrushedore", 4));
            TileEntityMacerator.addRecipe("crushedPurifiedTetrahedrite", 1, GTMaterialGen.getDust(GEMaterial.Tetrahedrite, 1));
            TileEntityMacerator.addRecipe("crushedPurifiedGalena", 1, GTMaterialGen.getDust(GEMaterial.Galena, 1));
            GameRegistry.addSmelting(GEMaterialGen.getCrushedOre(GEMaterial.Tetrahedrite, 1), Ic2Items.copperIngot, 0.5F);
            GameRegistry.addSmelting(GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Tetrahedrite, 1), Ic2Items.copperIngot, 0.5F);
            createTinyDustRecipe(GEMaterial.Zinc);
            createTinyDustRecipe(GEMaterial.Sulfur);
        }
    }
    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;
    public static void createTinyDustRecipe(GTMaterial mat) {
        String tinyDust = "dustTiny" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterialFlag.DUST)) {
            if (mat.hasFlag(GEMaterial.smalldust)) {
                // Block crafting recipe
                recipes.addRecipe(GTMaterialGen.getDust(mat, 1), "XXX", "XXX", "XXX", 'X',
                        tinyDust);
                TileEntityCompressor.addRecipe(tinyDust, 4, GTMaterialGen.getDust(mat, 1), 0.0F);
            }
        }
    }
}
