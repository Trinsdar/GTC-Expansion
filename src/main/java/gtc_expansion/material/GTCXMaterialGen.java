package gtc_expansion.material;

import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import net.minecraft.item.ItemStack;

public class GTCXMaterialGen {
    public static ItemStack getSmallDust(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTCXMaterial.smalldust, count);
    }

    public static ItemStack getNugget(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTCXMaterial.nugget, count);
    }

    public static ItemStack getPlate(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTCXMaterial.plate, count);
    }

    public static ItemStack getRod(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTCXMaterial.stick, count);
    }

    public static ItemStack getGear(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTCXMaterial.gear, count);
    }

    public static ItemStack getHull(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTCXMaterial.hull, count);
    }

    public static ItemStack getCrushedOre(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTCXMaterial.crushedore, count);
    }

    public static ItemStack getPurifiedCrushedOre(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTCXMaterial.crushedorePurified, count);
    }

    public static ItemStack getTinyDust(GTMaterial mat, int count){
        return GTMaterialGen.getStack(mat, GTCXMaterial.tinydust, count);
    }

    public static int getMaterialHeatValue(GTMaterial material){
        return GTCXMaterial.materialHeatMap.getOrDefault(material, 0);
    }
}
