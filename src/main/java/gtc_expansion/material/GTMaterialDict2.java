package gtc_expansion.material;

import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import net.minecraftforge.oredict.OreDictionary;

public class GTMaterialDict2 {
    public static void init(){
        OreDictionary.registerOre("gemRedGarnet", GTMaterialGen.getGem(GTMaterial2.GarnetRed, 1));
        OreDictionary.registerOre("gemYellowGarnet", GTMaterialGen.getGem(GTMaterial2.GarnetYellow, 1));
        for (GTMaterial mat : GTMaterial.values()){
            if (mat.hasFlag(GTMaterial2.smalldust)){
                OreDictionary.registerOre("dustSmall" + mat.getDisplayName(), GTMaterialGen2.getSmallDust(mat, 1));
            }
            if (mat.hasFlag(GTMaterial2.nugget)){
                OreDictionary.registerOre("nugget" + mat.getDisplayName(), GTMaterialGen2.getNugget(mat, 1));
            }
            if (mat.hasFlag(GTMaterial2.plate)){
                OreDictionary.registerOre("plate" + mat.getDisplayName(), GTMaterialGen2.getPlate(mat, 1));
            }
            if (mat.hasFlag(GTMaterial2.gear)){
                OreDictionary.registerOre("gear" + mat.getDisplayName(), GTMaterialGen.getStack(mat, GTMaterial2.gear, 1));
            }
            if (mat.hasFlag(GTMaterial2.stick)){
                OreDictionary.registerOre("rod" + mat.getDisplayName(), GTMaterialGen.getStack(mat, GTMaterial2.stick, 1));
            }
        }
    }
}
