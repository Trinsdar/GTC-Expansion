package gtc_expansion.data;

import gtc_expansion.GTCExpansion;
import gtc_expansion.block.GTCXBlockPipeFluid;
import gtc_expansion.block.GTCXBlockPipeItem;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.util.GTCXHelperPipe;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;

public class GTCXPipes {

    static LinkedHashMap<String, Block> blockMap = new LinkedHashMap();

    public static void init(){
        createFluidPipes(GTCXMaterial.Bronze, 100);
        createFluidPipes(GTCXMaterial.Steel, 200);
        createFluidPipes(GTMaterial.Invar, 250);
        createFluidPipes(GTCXMaterial.StainlessSteel, 300);
        createFluidPipes(GTCXMaterial.TungstenSteel, 400);
        createItemPipes(GTCXMaterial.Brass);
        for (Block block : blockMap.values()){
            GTCXBlocks.createBlock(block);
        }
    }

    public static void createFluidPipes(GTMaterial material, int baseTransfer){
        blockMap.put("small_" + material.getName() + "_pipe", new GTCXBlockPipeFluid(material, GTCXHelperPipe.GTPipeModel.SMALL, baseTransfer));
        blockMap.put(material.getName() + "_pipe", new GTCXBlockPipeFluid(material, GTCXHelperPipe.GTPipeModel.MED, baseTransfer * 3));
        blockMap.put("large_" + material.getName() + "_pipe", new GTCXBlockPipeFluid(material, GTCXHelperPipe.GTPipeModel.LARGE, (baseTransfer * 3) * 2));
        blockMap.put("huge_" + material.getName() + "_pipe", new GTCXBlockPipeFluid(material, GTCXHelperPipe.GTPipeModel.HUGE, ((baseTransfer * 3) * 2) * 2));
        blockMap.put("quad_" + material.getName() + "_pipe", new GTCXBlockPipeFluid(material, GTCXHelperPipe.GTPipeModel.QUAD, baseTransfer * 3));
    }

    public static void createItemPipes(GTMaterial material){
        blockMap.put(material.getName() + "_pipe", new GTCXBlockPipeItem(material, GTCXHelperPipe.GTPipeModel.MED));
        blockMap.put("large_" + material.getName() + "_pipe", new GTCXBlockPipeItem(material, GTCXHelperPipe.GTPipeModel.LARGE));
    }

    public static ItemStack getPipe(GTMaterial material,  GTCXHelperPipe.GTPipeModel model, int amount){
        GTCExpansion.logger.info("Name: " + model.getSuffix() + material.getName() + "_pipe");
        return GTMaterialGen.get(blockMap.get(model.getSuffix() + material.getName() + "_pipe"), amount);
    }

    public static ItemStack getPipe(GTMaterial material,  GTCXHelperPipe.GTPipeModel model){
        return getPipe(material, model, 1);
    }
}
