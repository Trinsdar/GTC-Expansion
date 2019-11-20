package gtc_expansion.item.tools;

import gtc_expansion.GTCExpansion;
import gtc_expansion.material.GTCXMaterial;
import gtclassic.api.material.GTMaterial;
import ic2.core.IC2;
import ic2.core.util.helpers.ToolHelper;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;

public class GTCXToolGen {

    public static LinkedHashMap<String, Item> itemMap = new LinkedHashMap();

    public static void initTools(){
        createVanillaSet(GTCXMaterial.Flint, GTCXToolMaterial.Flint);
        createCraftingSet(GTCXMaterial.Iron, ToolMaterial.IRON);
        createCraftingSet(GTCXMaterial.Bronze, ToolHelper.bronzeToolMaterial);
        createFullSet(GTCXMaterial.Steel, GTCXToolMaterial.Steel);
        createFullSet(GTCXMaterial.TungstenSteel, GTCXToolMaterial.TungstenSteel);
        createVanillaSet(GTCXMaterial.Ruby, GTCXToolMaterial.Ruby);
        createVanillaSet(GTCXMaterial.Sapphire, GTCXToolMaterial.Sapphire);
    }

    public static void initToolGen(){
        for (Item item : itemMap.values()){
            IC2.getInstance().createItem(item);
        }
        GTCExpansion.logger.info(getAxe(GTCXMaterial.Steel).getItem().getHasSubtypes());
    }

    public static void createFullSet(GTMaterial mat, ToolMaterial tmat){
        itemMap.put(mat.getName() + "_pickaxe", new GTCXItemToolPickaxe(mat, tmat));
        itemMap.put(mat.getName() + "_axe", new GTCXItemToolAxe(mat, tmat));
        itemMap.put(mat.getName() + "_shovel", new GTCXItemToolShovel(mat, tmat));
        itemMap.put(mat.getName() + "_sword", new GTCXItemToolSword(mat, tmat));
        itemMap.put(mat.getName() + "_hammer", new GTCXItemToolHammer(mat, tmat));
        itemMap.put(mat.getName() + "_file", new GTCXItemToolFile(mat, tmat));
        itemMap.put(mat.getName() + "_wrench", new GTCXItemToolWrench(mat, tmat));
    }

    public static void createVanillaSet(GTMaterial mat, ToolMaterial tmat){
        itemMap.put(mat.getName() + "_pickaxe", new GTCXItemToolPickaxe(mat, tmat));
        itemMap.put(mat.getName() + "_axe", new GTCXItemToolAxe(mat, tmat));
        itemMap.put(mat.getName() + "_shovel", new GTCXItemToolShovel(mat, tmat));
        itemMap.put(mat.getName() + "_sword", new GTCXItemToolSword(mat, tmat));
    }

    public static void createCraftingSet(GTMaterial mat, ToolMaterial tmat){
        itemMap.put(mat.getName() + "_hammer", new GTCXItemToolHammer(mat, tmat));
        itemMap.put(mat.getName() + "_file", new GTCXItemToolFile(mat, tmat));
        itemMap.put(mat.getName() + "_wrench", new GTCXItemToolWrench(mat, tmat));
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
