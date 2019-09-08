package gtc_expansion.material;

import gtc_expansion.GEItems;
import gtc_expansion.item.tools.GEToolGen;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GEMaterialDict {
    public static void init(){
        OreDictionary.registerOre("gemRedGarnet", GTMaterialGen.getGem(GEMaterial.GarnetRed, 1));
        OreDictionary.registerOre("gemYellowGarnet", GTMaterialGen.getGem(GEMaterial.GarnetYellow, 1));
        for (GTMaterial mat : GTMaterial.values()){
            if (mat.hasFlag(GEMaterial.smalldust)){
                OreDictionary.registerOre("dustSmall" + mat.getDisplayName(), GEMaterialGen.getSmallDust(mat, 1));
            }
            if (mat.hasFlag(GEMaterial.nugget)){
                OreDictionary.registerOre("nugget" + mat.getDisplayName(), GEMaterialGen.getNugget(mat, 1));
            }
            if (mat.hasFlag(GEMaterial.plate)){
                OreDictionary.registerOre("plate" + mat.getDisplayName(), GEMaterialGen.getPlate(mat, 1));
            }
            if (mat.hasFlag(GEMaterial.gear)){
                OreDictionary.registerOre("gear" + mat.getDisplayName(), GTMaterialGen.getStack(mat, GEMaterial.gear, 1));
            }
            if (mat.hasFlag(GEMaterial.stick)){
                OreDictionary.registerOre("rod" + mat.getDisplayName(), GTMaterialGen.getStack(mat, GEMaterial.stick, 1));
            }
        }
        registerToolDicts(GEMaterial.Iron);
        registerToolDicts(GEMaterial.Diamond);
        registerToolDicts(GEMaterial.Steel);
        registerToolDicts(GEMaterial.TungstenSteel);
        registerToolDicts(GEMaterial.Ruby);
        registerToolDicts(GEMaterial.Sapphire);
        OreDictionary.registerOre("craftingToolForgeHammer", new ItemStack(GEItems.bronzeHammer, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolFile", new ItemStack(GEItems.bronzeFile, 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerToolDicts(GTMaterial mat){
        OreDictionary.registerOre("craftingToolForgeHammer", new ItemStack(GEToolGen.getHammer(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolFile", new ItemStack(GEToolGen.getFile(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
    }
}
