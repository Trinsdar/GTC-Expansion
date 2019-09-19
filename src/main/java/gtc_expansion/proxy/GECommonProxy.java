package gtc_expansion.proxy;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEConfiguration;
import gtc_expansion.GEItems;
import gtc_expansion.GEOreDict;
import gtc_expansion.recipes.GERecipe;
import net.minecraftforge.common.config.Configuration;
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
        GEItems.registerItems();
        GEBlocks.registerBlocks();
        GEBlocks.registerTiles();
        GEOreDict.init();
    }

    public void init(FMLInitializationEvent e) {
        GERecipe.init();
    }

    public void postInit(FMLPostInitializationEvent e) {
        GERecipe.postInit();
    }
}
