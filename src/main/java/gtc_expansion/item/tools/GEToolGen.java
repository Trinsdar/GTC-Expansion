package gtc_expansion.item.tools;

import gtc_expansion.material.GEMaterial;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;

public class GEToolGen {

    public static void init(){
        createFullSet(GEMaterial.Flint, GEToolMaterial.Flint);
        createCraftingSet(GEMaterial.Iron, ToolMaterial.IRON);
        createCraftingSet(GEMaterial.Diamond, ToolMaterial.DIAMOND);
        createFullSet(GEMaterial.Steel, GEToolMaterial.Steel);
        createFullSet(GEMaterial.TungstenSteel, GEToolMaterial.TungstenSteel);
        createFullSet(GEMaterial.Ruby, GEToolMaterial.Ruby);
        createFullSet(GEMaterial.Sapphire, GEToolMaterial.Sapphire);
    }

    public static void createFullSet(GTMaterial mat, ToolMaterial tmat){
        GTMaterialGen.itemMap.put(mat.getName() + "_pickaxe", new GEItemToolPickaxe(mat, tmat));
        GTMaterialGen.itemMap.put(mat.getName() + "_axe", new GEItemToolAxe(mat, tmat));
        GTMaterialGen.itemMap.put(mat.getName() + "_shovel", new GEItemToolShovel(mat, tmat));
        GTMaterialGen.itemMap.put(mat.getName() + "_sword", new GEItemToolSword(mat, tmat));
        if (!mat.equals(GEMaterial.Flint)){
            GTMaterialGen.itemMap.put(mat.getName() + "_hammer", new GEItemToolHammer(mat, tmat));
            GTMaterialGen.itemMap.put(mat.getName() + "_file", new GEItemToolFile(mat, tmat));
        }
    }

    public static void createCraftingSet(GTMaterial mat, ToolMaterial tmat){
        GTMaterialGen.itemMap.put(mat.getName() + "_hammer", new GEItemToolHammer(mat, tmat));
        GTMaterialGen.itemMap.put(mat.getName() + "_file", new GEItemToolFile(mat, tmat));
    }

    public static ItemStack getPickaxe(GTMaterial material){
        return new ItemStack(GTMaterialGen.itemMap.get(material.getName() + "_pickaxe"));
    }

    public static ItemStack getAxe(GTMaterial material){
        return new ItemStack(GTMaterialGen.itemMap.get(material.getName() + "_axe"));
    }

    public static ItemStack getShovel(GTMaterial material){
        return new ItemStack(GTMaterialGen.itemMap.get(material.getName() + "_shovel"));
    }

    public static ItemStack getSword(GTMaterial material){
        return new ItemStack(GTMaterialGen.itemMap.get(material.getName() + "_sword"));
    }

    public static ItemStack getHammer(GTMaterial material){
        return new ItemStack(GTMaterialGen.itemMap.get(material.getName() + "_hammer"));
    }

    public static ItemStack getFile(GTMaterial material){
        return new ItemStack(GTMaterialGen.itemMap.get(material.getName() + "_file"));
    }
}
