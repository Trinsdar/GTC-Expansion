package gtc_expansion.proxy;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEConfiguration;
import gtc_expansion.GECrops;
import gtc_expansion.GEFluids;
import gtc_expansion.GEItems;
import gtc_expansion.GEOreDict;
import gtc_expansion.GEWorldGenTwilightForest;
import gtc_expansion.item.tools.GEToolGen;
import gtc_expansion.material.GEMaterialElement;
import gtc_expansion.recipes.GERecipe;
import gtclassic.GTConfig;
import gtclassic.util.GTValues;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class GECommonProxy {

    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "ic2/gtc_expansion.cfg"));
        GEConfiguration.readConfig();
        config.save();
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
        if (GTConfig.compatTwilightForest && Loader.isModLoaded(GTValues.TFOREST)) {
            GEWorldGenTwilightForest.init();
        }
    }
}
