package gtc_expansion.proxy;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.GTCXCrops;
import gtc_expansion.GTCXFluids;
import gtc_expansion.GTCXItems;
import gtc_expansion.GTCXJei;
import gtc_expansion.GTCXOreDict;
import gtc_expansion.GTCXWorldGenTwilightForest;
import gtc_expansion.item.tools.GTCXToolGen;
import gtc_expansion.material.GTCXMaterialElement;
import gtc_expansion.recipes.GTCXRecipe;
import gtclassic.api.helpers.GTHelperMods;
import gtclassic.common.GTConfig;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class GTCXCommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        GTCXFluids.registerFluids();
        GTCXBlocks.registerBlocks();
        GTCXItems.registerItems();
        GTCXMaterialElement.init();
        GTCXToolGen.initTools();
        GTCXToolGen.initToolGen();
        GTCXBlocks.registerTiles();
        GTCXCrops.init();
        GTCXOreDict.init();
    }

    public void init(FMLInitializationEvent e) {
        GTCXRecipe.init();
        GTCXJei.initJei();
    }

    public void postInit(FMLPostInitializationEvent e) {
        GTCXRecipe.postInit();
        if (GTConfig.compatTwilightForest && Loader.isModLoaded(GTHelperMods.TFOREST)) {
            GTCXWorldGenTwilightForest.init();
        }
    }
}
