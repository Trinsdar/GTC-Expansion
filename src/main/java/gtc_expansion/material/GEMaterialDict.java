package gtc_expansion.material;

import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import net.minecraftforge.oredict.OreDictionary;

public class GEMaterialDict {
    public static void init(){
        OreDictionary.registerOre("gemRedGarnet", GTMaterialGen.getGem(GEMaterial.GarnetRed, 1));
        OreDictionary.registerOre("gemYellowGarnet", GTMaterialGen.getGem(GEMaterial.GarnetYellow, 1));
        for (GTMaterial mat : GTMaterial.values()){
            if (mat.hasFlag(GEMaterial.smalldust)){
                OreDictionary.registerOre("dustSmall" + mat.getDisplayName(), GEMaterialGen.getSmallDust(mat, 1));
            }
            if (mat.hasFlag(GEMaterial.nugget)){
                OreDictionary.registerOre("nugget" + mat.getDisplayName(), GEMaterialGen.getNugget(mat, 1));
            }
            if (mat.hasFlag(GEMaterial.plate)){
                OreDictionary.registerOre("plate" + mat.getDisplayName(), GEMaterialGen.getPlate(mat, 1));
            }
            if (mat.hasFlag(GEMaterial.gear)){
                OreDictionary.registerOre("gear" + mat.getDisplayName(), GTMaterialGen.getStack(mat, GEMaterial.gear, 1));
            }
            if (mat.hasFlag(GEMaterial.stick)){
                OreDictionary.registerOre("rod" + mat.getDisplayName(), GTMaterialGen.getStack(mat, GEMaterial.stick, 1));
            }
        }
    }
}
