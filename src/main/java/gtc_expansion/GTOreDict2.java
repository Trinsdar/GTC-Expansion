package gtc_expansion;

import gtc_expansion.material.GTMaterial2;
import gtclassic.material.GTMaterialGen;
import net.minecraftforge.oredict.OreDictionary;

public class GTOreDict2 {
    public static void init(){
        OreDictionary.registerOre("gemRedGarnet", GTMaterialGen.getGem(GTMaterial2.GarnetRed, 1));
        OreDictionary.registerOre("gemYellowGarnet", GTMaterialGen.getGem(GTMaterial2.GarnetYellow, 1));
    }
}
