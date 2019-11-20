package gtc_expansion.material;

import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialFlag;
import gtclassic.api.material.GTMaterialGen;
import net.minecraft.item.ItemStack;

public class GEMaterialGen {
    public static void init() {
        for (GTMaterial mat : GTMaterial.values()){
            materialHotItemUtil(mat, GEMaterial.hotIngot);
        }
    }

    public static void materialHotItemUtil(GTMaterial mat, GTMaterialFlag flag) {
        if (mat.hasFlag(flag)) {
            GTMaterialGen.itemMap.put(mat.getName() + "_" + flag.getSuffix(), new GEMaterialItemHot(mat, flag));
        }

    }

    public static ItemStack getSmallDust(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.smalldust, count);
    }

    public static ItemStack getNugget(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.nugget, count);
    }

    public static ItemStack getPlate(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.plate, count);
    }

    public static ItemStack getRod(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.stick, count);
    }

    public static ItemStack getGear(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.gear, count);
    }

    public static ItemStack getHull(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.hull, count);
    }

    public static ItemStack getCrushedOre(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.crushedore, count);
    }

    public static ItemStack getPurifiedCrushedOre(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.crushedorePurified, count);
    }

    public static ItemStack getTinyDust(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.tinydust, count);
    }
}
