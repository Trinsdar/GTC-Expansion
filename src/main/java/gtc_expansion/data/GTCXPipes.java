package gtc_expansion.data;

import gtc_expansion.block.GTCXBlockPipeFluid;
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
    }

    public static void createFluidPipes(GTMaterial material, int baseTransfer){
        blockMap.put("small_" + material.getName() + "_pipe", GTCXBlocks.registerBlock(new GTCXBlockPipeFluid("small_" + material.getName() + "_pipe", material, GTCXHelperPipe.GTPipeModel.SMALL, baseTransfer)));
        blockMap.put(material.getName() + "_pipe", GTCXBlocks.registerBlock(new GTCXBlockPipeFluid(material.getName() + "_pipe", material, GTCXHelperPipe.GTPipeModel.MED, baseTransfer * 3)));
        blockMap.put("large_" + material.getName() + "_pipe", GTCXBlocks.registerBlock(new GTCXBlockPipeFluid("large_" + material.getName() + "_pipe", material, GTCXHelperPipe.GTPipeModel.LARGE, (baseTransfer * 3) * 2)));
    }

    public static ItemStack getPipe(GTMaterial material,  GTCXHelperPipe.GTPipeModel model, int amount){
        return GTMaterialGen.get(blockMap.get(model.getSuffix() + material.getName() + "_pipe"), amount);
    }

    public static ItemStack getPipe(GTMaterial material,  GTCXHelperPipe.GTPipeModel model){
        return getPipe(material, model, 1);
    }
}
