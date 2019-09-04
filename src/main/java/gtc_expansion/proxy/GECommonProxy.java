package gtc_expansion.proxy;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEItems;
import gtc_expansion.GEOreDict;
import gtc_expansion.recipes.GERecipe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class GECommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        GEBlocks.registerBlocks();
        GEBlocks.registerTiles();
        GEItems.registerItems();
    }

    public void init(FMLInitializationEvent e) {
        GEOreDict.init();
        GERecipe.init();
    }

    public void postInit(FMLPostInitializationEvent e) {
        GERecipe.postInit();
    }
}
