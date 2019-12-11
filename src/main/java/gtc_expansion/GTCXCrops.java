package gtc_expansion;

import gtc_expansion.block.GTCXCropOilberry;
import gtc_expansion.material.GTCXMaterial;
import gtclassic.api.crops.GTCropHandler;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.classic.crops.ClassicCrops;
import ic2.api.crops.CropCard;

public class GTCXCrops {
    public static void init(){
        GTCropHandler.registerCrop(0, GTCExpansion.MODID + "_crops", GTCXMaterial.Olivine, "Olivia", "Trinsdar", 5, "Crystal", "Shiny", "Metal", "Olivine", "Leaves");
        GTCropHandler.registerCrop(1, GTCExpansion.MODID + "_crops", GTMaterial.Nickel, "Nickelback", "Trinsdar", 5, "Metal", "Fire", "Nickel", "Leaves");
        GTCropHandler.registerCrop(2, GTCExpansion.MODID + "_crops", GTCXMaterial.Zinc, "Galvania", "Trinsdar", 6, "Metal", "Bush", "Zinc", "Leaves");
        ClassicCrops crop = ClassicCrops.instance;
        CropCard card = new GTCXCropOilberry();
        crop.registerCrop(card);
        crop.registerCropDisplayItem(card, GTMaterialGen.get(GTCXItems.oilberry));
    }
}
