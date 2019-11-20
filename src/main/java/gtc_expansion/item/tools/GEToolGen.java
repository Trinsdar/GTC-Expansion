package gtc_expansion.item.tools;

import gtc_expansion.GTCExpansion;
import gtc_expansion.material.GEMaterial;
import gtclassic.api.material.GTMaterial;
import ic2.core.IC2;
import ic2.core.util.helpers.ToolHelper;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;

public class GEToolGen {

    public static LinkedHashMap<String, Item> itemMap = new LinkedHashMap();

    public static void initTools(){
        createVanillaSet(GEMaterial.Flint, GEToolMaterial.Flint);
        createCraftingSet(GEMaterial.Iron, ToolMaterial.IRON);
        createCraftingSet(GEMaterial.Bronze, ToolHelper.bronzeToolMaterial);
        createFullSet(GEMaterial.Steel, GEToolMaterial.Steel);
        createFullSet(GEMaterial.TungstenSteel, GEToolMaterial.TungstenSteel);
        createVanillaSet(GEMaterial.Ruby, GEToolMaterial.Ruby);
        createVanillaSet(GEMaterial.Sapphire, GEToolMaterial.Sapphire);
    }

    public static void initToolGen(){
        for (Item item : itemMap.values()){
            IC2.getInstance().createItem(item);
        }
        GTCExpansion.logger.info(getAxe(GEMaterial.Steel).getItem().getHasSubtypes());
    }

    public static void createFullSet(GTMaterial mat, ToolMaterial tmat){
        itemMap.put(mat.getName() + "_pickaxe", new GEItemToolPickaxe(mat, tmat));
        itemMap.put(mat.getName() + "_axe", new GEItemToolAxe(mat, tmat));
        itemMap.put(mat.getName() + "_shovel", new GEItemToolShovel(mat, tmat));
        itemMap.put(mat.getName() + "_sword", new GEItemToolSword(mat, tmat));
        itemMap.put(mat.getName() + "_hammer", new GEItemToolHammer(mat, tmat));
        itemMap.put(mat.getName() + "_file", new GEItemToolFile(mat, tmat));
        itemMap.put(mat.getName() + "_wrench", new GEItemToolWrench(mat, tmat));
    }

    public static void createVanillaSet(GTMaterial mat, ToolMaterial tmat){
        itemMap.put(mat.getName() + "_pickaxe", new GEItemToolPickaxe(mat, tmat));
        itemMap.put(mat.getName() + "_axe", new GEItemToolAxe(mat, tmat));
        itemMap.put(mat.getName() + "_shovel", new GEItemToolShovel(mat, tmat));
        itemMap.put(mat.getName() + "_sword", new GEItemToolSword(mat, tmat));
    }

    public static void createCraftingSet(GTMaterial mat, ToolMaterial tmat){
        itemMap.put(mat.getName() + "_hammer", new GEItemToolHammer(mat, tmat));
        itemMap.put(mat.getName() + "_file", new GEItemToolFile(mat, tmat));
        itemMap.put(mat.getName() + "_wrench", new GEItemToolWrench(mat, tmat));
    }

    public static ItemStack getPickaxe(GTMaterial material){
        return new ItemStack(itemMap.get(material.getName() + "_pickaxe"));
    }

    public static ItemStack getAxe(GTMaterial material){
        return new ItemStack(itemMap.get(material.getName() + "_axe"));
    }

    public static ItemStack getShovel(GTMaterial material){
        return new ItemStack(itemMap.get(material.getName() + "_shovel"));
    }

    public static ItemStack getSword(GTMaterial material){
        return new ItemStack(itemMap.get(material.getName() + "_sword"));
    }

    public static ItemStack getHammer(GTMaterial material){
        return new ItemStack(itemMap.get(material.getName() + "_hammer"));
    }

    public static ItemStack getFile(GTMaterial material){
        return new ItemStack(itemMap.get(material.getName() + "_file"));
    }

    public static ItemStack getWrench(GTMaterial material){
        return new ItemStack(itemMap.get(material.getName() + "_wrench"));
    }
}
