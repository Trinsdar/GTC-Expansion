package gtc_expansion.material;

import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;
import gtclassic.material.GTMaterialGen;
import net.minecraft.item.ItemStack;

import static gtclassic.material.GTMaterialGen.materialBlockUtil;
import static gtclassic.material.GTMaterialGen.materialItemUtil;

public class GTMaterialGen2 {
    public static void init() {
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GTMaterial2.hotIngot);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialBlockUtil(mat, GTMaterial2.casing);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GTMaterial2.stick);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GTMaterial2.plate);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GTMaterial2.nugget);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GTMaterial2.gear);
        }
        for (GTMaterial mat : GTMaterial.values()){
            materialItemUtil(mat, GTMaterial2.smalldust);
        }
        for (GTMaterial mat : GTMaterial.values()){
            if (!mat.hasFlag(GTMaterialFlag.RUBY) && !mat.hasFlag(GTMaterialFlag.SAPPHIRE)) {
                materialItemUtil(mat, GTMaterial2.gemGarnetShape);
            }
        }
    }

    public static ItemStack getSmallDust(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTMaterial2.smalldust, count);
    }

    public static ItemStack getNugget(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTMaterial2.nugget, count);
    }

    public static ItemStack getPlate(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTMaterial2.plate, count);
    }
}
