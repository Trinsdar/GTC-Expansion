package gtc_expansion.proxy;

import gtc_expansion.GTBlocks2;
import gtc_expansion.GTOreDict2;
import gtc_expansion.recipes.GTRecipe2;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class GTProxyCommon2 {

    public void preInit(FMLPreInitializationEvent e) {
        GTBlocks2.registerBlocks();
        GTBlocks2.registerTiles();
    }

    public void init(FMLInitializationEvent e) {
        GTOreDict2.init();
        GTRecipe2.init();
    }

    public void postInit(FMLPostInitializationEvent e) {
        GTRecipe2.postInit();
    }
}
