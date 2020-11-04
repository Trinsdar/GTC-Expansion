package gtc_expansion.material;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.data.GTCXValues;
import gtc_expansion.item.tools.GTCXToolGen;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialFlag;
import gtclassic.api.material.GTMaterialGen;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class GTCXMaterialDict {
    public static void init(){
        OreDictionary.registerOre("gemRedGarnet", GTMaterialGen.getGem(GTCXMaterial.GarnetRed, 1));
        OreDictionary.registerOre("gemYellowGarnet", GTMaterialGen.getGem(GTCXMaterial.GarnetYellow, 1));
        for (GTMaterial mat : GTMaterial.values()){
            if (mat.hasFlag(GTCXMaterial.smalldust)){
                OreDictionary.registerOre("dustSmall" + mat.getDisplayName(), GTCXMaterialGen.getSmallDust(mat, 1));
            }
            if (mat.hasFlag(GTMaterialFlag.INGOTHOT)){
                OreDictionary.registerOre("ingotHot" + mat.getDisplayName(), GTMaterialGen.getHotIngot(mat, 1));
            }
            if (mat.hasFlag(GTCXMaterial.nugget)){
                OreDictionary.registerOre("nugget" + mat.getDisplayName(), GTCXMaterialGen.getNugget(mat, 1));
            }
            if (mat.hasFlag(GTCXMaterial.plate)){
                OreDictionary.registerOre("plate" + mat.getDisplayName(), GTCXMaterialGen.getPlate(mat, 1));
            }
            if (mat.hasFlag(GTCXMaterial.gear)){
                OreDictionary.registerOre("gear" + mat.getDisplayName(), GTMaterialGen.getStack(mat, GTCXMaterial.gear, 1));
            }
            if (mat.hasFlag(GTCXMaterial.stick)){
                OreDictionary.registerOre("rod" + mat.getDisplayName(), GTMaterialGen.getStack(mat, GTCXMaterial.stick, 1));
                OreDictionary.registerOre("stick" + mat.getDisplayName(), GTMaterialGen.getStack(mat, GTCXMaterial.stick, 1));
            }
            if (mat.hasFlag(GTCXMaterial.tinydust)){
                OreDictionary.registerOre("dustTiny" + mat.getDisplayName(), GTCXMaterialGen.getTinyDust(mat, 1));
            }
            if (mat.hasFlag(GTCXMaterial.crushedore)){
                OreDictionary.registerOre("crushed" + mat.getDisplayName(), GTCXMaterialGen.getCrushedOre(mat, 1));
            }
            if (mat.hasFlag(GTCXMaterial.crushedorePurified)){
                OreDictionary.registerOre("crushedPurified" + mat.getDisplayName(), GTCXMaterialGen.getPurifiedCrushedOre(mat, 1));
            }
        }
        registerToolDicts(GTCXMaterial.Iron);
        registerToolDicts(GTCXMaterial.Bronze);
        registerToolDictsWithAxe(GTCXMaterial.Steel);
        registerToolDictsWithAxe(GTCXMaterial.TungstenSteel);
        registerAxeDict(GTMaterial.Ruby);
        registerAxeDict(GTMaterial.Sapphire);
        registerAxeDict(GTMaterial.Flint);
        OreDictionary.registerOre("machineBlockCheap", GTCXMaterialGen.getHull(GTCXMaterial.Bronze, 1));
        OreDictionary.registerOre("machineBlockCheap", GTCXMaterialGen.getHull(GTCXMaterial.Brass, 1));
        if (!GTCXConfiguration.general.forceSteelCasings || GTCXConfiguration.general.gt2Mode){
            if (IC2.config.getFlag("SteelRecipes")){
                OreDictionary.registerOre("machineBlockBasic", GTCXMaterialGen.getHull(GTCXMaterial.RefinedIron, 1));
            }
            OreDictionary.registerOre("machineBlockBasic", Ic2Items.machine);
        } else {
            OreDictionary.registerOre("machineBlockCheap", Ic2Items.machine);
        }
        OreDictionary.registerOre("machineBlockBasic", GTCXMaterialGen.getHull(GTCXMaterial.Steel, 1));
        OreDictionary.registerOre("machineBlockBasic", GTCXMaterialGen.getHull(GTMaterial.Aluminium, 1));
        OreDictionary.registerOre("machineBlockBasic", GTCXMaterialGen.getHull(GTCXMaterial.StainlessSteel, 1));
        OreDictionary.registerOre("machineBlockAdvanced", GTCXMaterialGen.getHull(GTMaterial.Titanium, 1));
        OreDictionary.registerOre("machineBlockAdvanced", GTCXMaterialGen.getHull(GTCXMaterial.TungstenSteel, 1));
        OreDictionary.registerOre("machineBlockVeryAdvanced", GTCXMaterialGen.getHull(GTMaterial.Titanium, 1));
        OreDictionary.registerOre("machineBlockVeryAdvanced", GTCXMaterialGen.getHull(GTCXMaterial.TungstenSteel, 1));
        OreDictionary.registerOre("plateWroughtIron", GTCXMaterialGen.getPlate(GTCXMaterial.RefinedIron, 1));
        OreDictionary.registerOre("rodWroughtIron", GTCXMaterialGen.getRod(GTCXMaterial.RefinedIron, 1));
        OreDictionary.registerOre("stickWroughtIron", GTCXMaterialGen.getRod(GTCXMaterial.RefinedIron, 1));
        OreDictionary.registerOre("gearWroughtIron", GTCXMaterialGen.getGear(GTCXMaterial.RefinedIron, 1));
        if (Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS)){
            OreDictionary.registerOre("casingWroughtIron", GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "refinedironcasing"));
            OreDictionary.registerOre("plateWroughtIron", GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "refinedironplate"));
            OreDictionary.registerOre("blockWroughtIron", GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "refinedironblock"));
        }

        GTMaterial mat = GTCXMaterial.Osmium;
        OreDictionary.registerOre("dustSmall" + mat.getDisplayName() + "GT", GTCXMaterialGen.getSmallDust(mat, 1));
        OreDictionary.registerOre("block" + mat.getDisplayName() + "GT", GTMaterialGen.getMaterialBlock(mat, 1));
        OreDictionary.registerOre("dust" + mat.getDisplayName() + "GT", GTMaterialGen.getDust(mat, 1));
        OreDictionary.registerOre("ingot" + mat.getDisplayName() + "GT", GTMaterialGen.getIngot(mat, 1));
        OreDictionary.registerOre("ingotHot" + mat.getDisplayName() + "GT", GTMaterialGen.getHotIngot(mat, 1));
        OreDictionary.registerOre("nugget" + mat.getDisplayName() + "GT", GTCXMaterialGen.getNugget(mat, 1));
        OreDictionary.registerOre("plate" + mat.getDisplayName() + "GT", GTCXMaterialGen.getPlate(mat, 1));
        OreDictionary.registerOre("gear" + mat.getDisplayName() + "GT", GTMaterialGen.getStack(mat, GTCXMaterial.gear, 1));
        OreDictionary.registerOre("rod" + mat.getDisplayName() + "GT", GTMaterialGen.getStack(mat, GTCXMaterial.stick, 1));
        OreDictionary.registerOre("stick" + mat.getDisplayName() + "GT", GTMaterialGen.getStack(mat, GTCXMaterial.stick, 1));
        OreDictionary.registerOre("dustTiny" + mat.getDisplayName() + "GT", GTCXMaterialGen.getTinyDust(mat, 1));
    }

    public static void registerToolDicts(GTMaterial mat){
        OreDictionary.registerOre("craftingToolForgeHammer", new ItemStack(GTCXToolGen.getHammer(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolFile", new ItemStack(GTCXToolGen.getFile(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolWrench", new ItemStack(GTCXToolGen.getWrench(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolSaw", new ItemStack(GTCXToolGen.getSaw(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerToolDictsWithAxe(GTMaterial mat){
        OreDictionary.registerOre("craftingToolForgeHammer", new ItemStack(GTCXToolGen.getHammer(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolFile", new ItemStack(GTCXToolGen.getFile(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolWrench", new ItemStack(GTCXToolGen.getWrench(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolSaw", new ItemStack(GTCXToolGen.getSaw(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolAxe", new ItemStack(GTCXToolGen.getAxe(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerAxeDict(GTMaterial mat){
        OreDictionary.registerOre("toolAxe", new ItemStack(GTCXToolGen.getAxe(mat).getItem(), 1, OreDictionary.WILDCARD_VALUE));
    }
}
