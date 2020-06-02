package gtc_expansion.util;

import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.world.GTBedrockOreHandler;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTConfig;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class GTCXBedrockOreHandler {

    public static void bedrockOresInit() {
        String modid = GTValues.MOD_ID_IC2_EXTRAS;
        if (Loader.isModLoaded(modid) && GTConfig.modcompat.compatIc2Extras){
            // vanilla
            addBedrockOre(GTBlocks.oreBedrockGold, GTMaterialGen.getModItem(modid, "goldcrushedore", 1));
            addBedrockOre(GTBlocks.oreBedrockIron, GTMaterialGen.getModItem(modid, "ironcrushedore", 1));
            // ic2
            addBedrockOre(GTBlocks.oreBedrockCopper, GTMaterialGen.getModItem(modid, "coppercrushedore", 1));
            addBedrockOre(GTBlocks.oreBedrockTin, GTMaterialGen.getModItem(modid, "tincrushedore", 1));
            addBedrockOre(GTBlocks.oreBedrockUranium, GTMaterialGen.getModItem(modid, "uraniumcrushedore", 1));
            addBedrockOre(GTBlocks.oreBedrockSilver, GTMaterialGen.getModItem(modid, "silvercrushedore", 1));
            // gtc
            addBedrockOre(GTBlocks.oreBedrockSheldonite, GTCXMaterialGen.getCrushedOre(GTMaterial.Platinum, 1));
            addBedrockOre(GTBlocks.oreBedrockBauxite, GTCXMaterialGen.getCrushedOre(GTMaterial.Bauxite, 1));
            // gtcx
            addBedrockOre(GTCXBlocks.oreBedrockPyrite, GTCXMaterialGen.getCrushedOre(GTMaterial.Pyrite, 1));
            addBedrockOre(GTCXBlocks.oreBedrockCinnabar, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Cinnabar, 1));
            addBedrockOre(GTCXBlocks.oreBedrockSphalerite, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Sphalerite, 1));
            addBedrockOre(GTCXBlocks.oreBedrockTungstate, GTCXMaterialGen.getCrushedOre(GTMaterial.Tungsten, 1));
            addBedrockOre(GTCXBlocks.oreBedrockCassiterite, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Cassiterite, 2));
            addBedrockOre(GTCXBlocks.oreBedrockTetrahedrite, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Tetrahedrite, 1));
            addBedrockOre(GTCXBlocks.oreBedrockGalena, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Galena, 1));
        } else {
            // gtcx
            addBedrockOre(GTCXBlocks.oreBedrockPyrite, GTMaterialGen.getDust(GTMaterial.Pyrite, 1));
            addBedrockOre(GTCXBlocks.oreBedrockCinnabar, GTMaterialGen.getDust(GTCXMaterial.Cinnabar, 1));
            addBedrockOre(GTCXBlocks.oreBedrockSphalerite, GTMaterialGen.getDust(GTCXMaterial.Sphalerite, 1));
            addBedrockOre(GTCXBlocks.oreBedrockTungstate, GTMaterialGen.getDust(GTMaterial.Tungsten, 1));
            addBedrockOre(GTCXBlocks.oreBedrockCassiterite, GTMaterialGen.getDust(GTCXMaterial.Cassiterite, 2));
            addBedrockOre(GTCXBlocks.oreBedrockTetrahedrite, GTMaterialGen.getDust(GTCXMaterial.Tetrahedrite, 1));
            addBedrockOre(GTCXBlocks.oreBedrockGalena, GTMaterialGen.getDust(GTCXMaterial.Galena, 1));
        }
        addBedrockOre(GTCXBlocks.oreBedrockOlivine, GTMaterialGen.getDust(GTCXMaterial.Olivine, 1));
        addBedrockOre(GTCXBlocks.oreBedrockSodalite, GTMaterialGen.getDust(GTMaterial.Sodalite, 1));
    }

    public static void addBedrockOre(Block ore, ItemStack output) {
        GTBedrockOreHandler.addBedrockOre(ore, output);
    }
}
