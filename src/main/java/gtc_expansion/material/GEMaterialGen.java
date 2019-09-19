package gtc_expansion.material;

import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;
import gtclassic.material.GTMaterialGen;
import net.minecraft.item.ItemStack;

import static gtclassic.material.GTMaterialGen.materialBlockUtil;
import static gtclassic.material.GTMaterialGen.materialItemUtil;

public class GEMaterialGen {
    public static void init() {
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GEMaterial.hotIngot);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GEMaterial.hull);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GEMaterial.stick);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GEMaterial.plate);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GEMaterial.nugget);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GEMaterial.gear);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GEMaterial.smalldust);
        }
        for (GTMaterial mat : GTMaterial.values()){
            if (!mat.hasFlag(GTMaterialFlag.RUBY) && !mat.hasFlag(GTMaterialFlag.SAPPHIRE)) {
                materialItemUtil(mat, GEMaterial.gemGarnetShape);
            }
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

    public static ItemStack getHull(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GEMaterial.hull, count);
    }
}
