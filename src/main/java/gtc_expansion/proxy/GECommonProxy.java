package gtc_expansion.proxy;

import gtc_expansion.GEBlocks;
import gtc_expansion.GECrops;
import gtc_expansion.GEFluids;
import gtc_expansion.GEItems;
import gtc_expansion.GEOreDict;
import gtc_expansion.GEWorldGenTwilightForest;
import gtc_expansion.item.tools.GEToolGen;
import gtc_expansion.material.GEMaterialElement;
import gtc_expansion.recipes.GERecipe;
import gtclassic.api.helpers.GTHelperMods;
import gtclassic.common.GTConfig;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class GECommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        GEFluids.registerFluids();
        GEBlocks.registerBlocks();
        GEItems.registerItems();
        GEMaterialElement.init();
        GEToolGen.initTools();
        GEToolGen.initToolGen();
        GEBlocks.registerTiles();
        GECrops.init();
        GEOreDict.init();
    }

    public void init(FMLInitializationEvent e) {
        GERecipe.init();
    }

    public void postInit(FMLPostInitializationEvent e) {
        GERecipe.postInit();
        if (GTConfig.compatTwilightForest && Loader.isModLoaded(GTHelperMods.TFOREST)) {
            GEWorldGenTwilightForest.init();
        }
    }
}
