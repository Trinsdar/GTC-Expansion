package gtc_expansion.material;

import gtc_expansion.GEItems;
import gtc_expansion.item.tools.GEToolGen;
import gtclassic.GTConfig;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.util.GTValues;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
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
            if (Loader.isModLoaded(GTValues.IC2_EXTRAS) && GTConfig.compatIc2Extras){
                if (mat.hasFlag(GEMaterial.crushedore)){
                    OreDictionary.registerOre("crushed" + mat.getDisplayName(), GEMaterialGen.getCrushedOre(mat, 1));
                }
                if (mat.hasFlag(GEMaterial.crushedorePurified)){
                    OreDictionary.registerOre("crushedPurified" + mat.getDisplayName(), GEMaterialGen.getPurifiedCrushedOre(mat, 1));
                }
                if (mat.hasFlag(GEMaterial.tinydust)){
                    OreDictionary.registerOre("dustTiny" + mat.getDisplayName(), GEMaterialGen.getTinyDust(mat, 1));
                }
            }
        }
        registerToolDicts(GEMaterial.Iron);
        registerToolDicts(GEMaterial.Bronze);
        registerToolDictsWithAxe(GEMaterial.Steel);
        registerToolDictsWithAxe(GEMaterial.TungstenSteel);
        registerAxeDict(GEMaterial.Ruby);
        registerAxeDict(GEMaterial.Sapphire);
        OreDictionary.registerOre("toolAxe", new ItemStack(GEToolGen.getAxe(GEMaterial.Flint).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("machineBlockBasic", GEMaterialGen.getHull(GEMaterial.Aluminium, 1));
        OreDictionary.registerOre("machineBlockBasic", GEMaterialGen.getHull(GEMaterial.Bronze, 1));
        OreDictionary.registerOre("machineBlockAdvanced", GEMaterialGen.getHull(GEMaterial.Steel, 1));
        OreDictionary.registerOre("machineBlockAdvanced", GEMaterialGen.getHull(GEMaterial.Titanium, 1));

    }

    public static void registerToolDicts(GTMaterial mat){
        OreDictionary.registerOre("craftingToolForgeHammer", new ItemStack(GEToolGen.getHammer(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolFile", new ItemStack(GEToolGen.getFile(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolWrench", new ItemStack(GEToolGen.getWrench(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerToolDictsWithAxe(GTMaterial mat){
        OreDictionary.registerOre("craftingToolForgeHammer", new ItemStack(GEToolGen.getHammer(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolFile", new ItemStack(GEToolGen.getFile(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolWrench", new ItemStack(GEToolGen.getWrench(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolAxe", new ItemStack(GEToolGen.getAxe(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerAxeDict(GTMaterial mat){
        OreDictionary.registerOre("toolAxe", new ItemStack(GEToolGen.getAxe(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
    }
}
