package gtc_expansion.material;

import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;

import static gtclassic.material.GTMaterialGen.materialBlockUtil;
import static gtclassic.material.GTMaterialGen.materialItemUtil;

public class GTMaterialGen2 {
    public static void init() {
        for (GTMaterial mat : GTMaterial.values()) {
            materialBlockUtil(mat, GTMaterial2.casing);
            if (!mat.hasFlag(GTMaterialFlag.BLOCK)) {
                materialBlockUtil(mat, GTMaterial2.gemBlock);
            }
            materialItemUtil(mat, GTMaterial2.stick);
            materialItemUtil(mat, GTMaterial2.plate);
            materialItemUtil(mat, GTMaterial2.nugget);
            materialItemUtil(mat, GTMaterial2.gear);
            materialItemUtil(mat, GTMaterial2.smalldust);
            if (!mat.hasFlag(GTMaterialFlag.RUBY) && !mat.hasFlag(GTMaterialFlag.SAPPHIRE)) {
                materialItemUtil(mat, GTMaterial2.gemGarnetShape);
            }
        }
    }
}
