package gtc_expansion;

import gtc_expansion.material.GEMaterial;
import gtclassic.GTCrops;

public class GECrops {
    public static void init(){
        GTCrops.registerCrop(0, GTCExpansion.MODID + "_crops", GEMaterial.Olivine, "Olivia", "Trinsdar", 5, "Crystal", "Shiny", "Metal", "Olivine", "Leaves");
        GTCrops.registerCrop(1, GTCExpansion.MODID + "_crops", GEMaterial.Nickel, "Nickelback", "Trinsdar", 5, "Metal", "Fire", "Nickel", "Leaves");
        GTCrops.registerCrop(2, GTCExpansion.MODID + "_crops", GEMaterial.Zinc, "Galvania", "Trinsdar", 6, "Metal", "Bush", "Zinc", "Leaves");
    }
}
