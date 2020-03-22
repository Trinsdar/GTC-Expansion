package gtc_expansion.proxy;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.GTCXCrops;
import gtc_expansion.GTCXFluids;
import gtc_expansion.GTCXItems;
import gtc_expansion.GTCXOreDict;
import gtc_expansion.GTCXWorldGenTwilightForest;
import gtc_expansion.container.GTCXContainerFluidSmelter;
import gtc_expansion.item.tools.GTCXToolGen;
import gtc_expansion.recipes.GTCXRecipe;
import gtc_expansion.tile.GTCXTileFluidSmelter;
import gtc_expansion.util.GTCXBedrockOreHandler;
import gtclassic.api.helpers.GTValues;
import gtclassic.common.GTConfig;
import ic2.core.inventory.filters.BasicItemFilter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class GTCXCommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        GTCXFluids.registerFluids();
        GTCXBlocks.registerBlocks();
        GTCXItems.registerItems();
        GTCXToolGen.initTools();
        GTCXToolGen.initToolGen();
        GTCXBlocks.registerTiles();
        GTCXCrops.init();
        GTCXOreDict.init();
    }

    public void init(FMLInitializationEvent e) {
        GTCXRecipe.init();
        GTCXBedrockOreHandler.bedrockOresInit();
    }

    public void postInit(FMLPostInitializationEvent e) {
        GTCXRecipe.postInit();
        if (GTConfig.modcompat.compatTwilightForest && Loader.isModLoaded(GTValues.MOD_ID_TFOREST)) {
            GTCXWorldGenTwilightForest.init();
        }
        for (ItemStack stack : GTCXTileFluidSmelter.coilsSlotWhitelist.keySet()){
            GTCXContainerFluidSmelter.filters.add(new BasicItemFilter(stack));
        }
    }
}
