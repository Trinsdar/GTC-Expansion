package gtc_expansion.util;

import gtc_expansion.block.GTCXCropBlock;
import gtclassic.api.crops.GTCropType;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.classic.crops.ClassicCrops;
import ic2.api.crops.CropCard;

public class GTCXCropHandler {
    /** Public method for constructing basic crops easily **/
    public static void registerCrop(int id, String sprite, GTMaterial mat, String name, String discoverer, int tier,
                                    String... attributes) {
        ClassicCrops crop = ClassicCrops.instance;
        GTCropType type = new GTCropType(id, sprite, mat, name, discoverer, tier, attributes);
        CropCard card = new GTCXCropBlock(type);
        crop.registerCrop(card);
        crop.registerCropDisplayItem(card, GTMaterialGen.getDust(type.getMaterial(), 1));
    }
}
