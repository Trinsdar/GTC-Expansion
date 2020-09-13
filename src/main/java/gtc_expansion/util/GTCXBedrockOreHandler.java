package gtc_expansion.util;

import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeProcessing;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.world.GTBedrockOreHandler;
import gtclassic.common.GTBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class GTCXBedrockOreHandler {

    public static void bedrockOresInit() {
        // vanilla
        addBedrockOre(GTBlocks.oreBedrockGold, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Gold, 1));
        addBedrockOre(GTBlocks.oreBedrockIron, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Iron, 1));
        // ic2
        addBedrockOre(GTBlocks.oreBedrockCopper, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Copper, 1));
        addBedrockOre(GTBlocks.oreBedrockTin, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Tin, 1));
        addBedrockOre(GTBlocks.oreBedrockUranium, GTCXRecipeProcessing.getCrushedOrDust(GTMaterial.Uranium, 1));
        addBedrockOre(GTBlocks.oreBedrockSilver, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Silver, 1));
        // gtc
        addBedrockOre(GTBlocks.oreBedrockSheldonite, GTCXRecipeProcessing.getCrushedOrDust(GTMaterial.Platinum, 1));
        addBedrockOre(GTBlocks.oreBedrockBauxite, GTCXRecipeProcessing.getCrushedOrDust(GTMaterial.Bauxite, 1));
        // gtcx
        addBedrockOre(GTCXBlocks.oreBedrockPyrite, GTCXRecipeProcessing.getCrushedOrDust(GTMaterial.Pyrite, 1));
        addBedrockOre(GTCXBlocks.oreBedrockCinnabar, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Cinnabar, 1));
        addBedrockOre(GTCXBlocks.oreBedrockSphalerite, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Sphalerite, 1));
        addBedrockOre(GTCXBlocks.oreBedrockTungstate, GTCXRecipeProcessing.getCrushedOrDust(GTMaterial.Tungsten, 1));
        addBedrockOre(GTCXBlocks.oreBedrockCassiterite, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Cassiterite, 2));
        addBedrockOre(GTCXBlocks.oreBedrockTetrahedrite, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Tetrahedrite, 1));
        addBedrockOre(GTCXBlocks.oreBedrockGalena, GTCXRecipeProcessing.getCrushedOrDust(GTCXMaterial.Galena, 1));
        addBedrockOre(GTCXBlocks.oreBedrockOlivine, GTMaterialGen.getDust(GTCXMaterial.Olivine, 1));
        addBedrockOre(GTCXBlocks.oreBedrockSodalite, GTMaterialGen.getDust(GTMaterial.Sodalite, 1));
    }

    public static void addBedrockOre(Block ore, ItemStack output) {
        GTBedrockOreHandler.addBedrockOre(ore, output);
    }
}
