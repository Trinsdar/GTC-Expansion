package gtc_expansion;

import gtc_expansion.block.GECropOilberry;
import gtc_expansion.material.GEMaterial;
import gtclassic.api.crops.GTCropHandler;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.classic.crops.ClassicCrops;
import ic2.api.crops.CropCard;

public class GECrops {
    public static void init(){
        GTCropHandler.registerCrop(0, GTCExpansion.MODID + "_crops", GEMaterial.Olivine, "Olivia", "Trinsdar", 5, "Crystal", "Shiny", "Metal", "Olivine", "Leaves");
        GTCropHandler.registerCrop(1, GTCExpansion.MODID + "_crops", GEMaterial.Nickel, "Nickelback", "Trinsdar", 5, "Metal", "Fire", "Nickel", "Leaves");
        GTCropHandler.registerCrop(2, GTCExpansion.MODID + "_crops", GEMaterial.Zinc, "Galvania", "Trinsdar", 6, "Metal", "Bush", "Zinc", "Leaves");
        ClassicCrops crop = ClassicCrops.instance;
        CropCard card = new GECropOilberry();
        crop.registerCrop(card);
        crop.registerCropDisplayItem(card, GTMaterialGen.get(GEItems.oilberry));
    }
}
