package gtc_expansion;

import gtc_expansion.proxy.GECommonProxy;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class GEConfiguration {
    private static final String CATEGORY_CONFIG = "config";
    private static final String CATEGORY_GENERATION = "generation";

    public static boolean usePlates = false;
    public static boolean harderTools = true;

    // generation
    public static boolean pyriteGenerate = true;
    public static int pyriteSize = 32;
    public static int pyriteWeight = 8;
    public static boolean cinnabarGenerate = true;
    public static int cinnabarSize = 16;
    public static int cinnabarWeight = 4;
    public static boolean sphaleriteGenerate = true;
    public static int sphaleriteSize = 32;
    public static int sphaleriteWeight = 8;

    public static boolean tungstateGenerate = true;
    public static int tungstateSize = 16;
    public static int tungstateWeight = 2;
    public static boolean sheldoniteGenerate = true;
    public static int sheldoniteSize = 4;
    public static int sheldoniteWeight = 2;
    public static boolean olivineGenerate = true;
    public static int olivineSize = 8;
    public static int olivineWeight = 5;
    public static boolean sodaliteGenerate = true;
    public static int sodaliteSize = 16;
    public static int sodaliteWeight = 6;
    // mod compat options

    public static void readConfig() {
        Configuration cfg = GECommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
            initGenerationConfig(cfg);
        } catch (Exception e1) {
            GTCExpansion.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_CONFIG, "Configuration");
        usePlates = cfg.getBoolean("usePlates", CATEGORY_CONFIG, usePlates, "Enables quite a few of GT recipes using plates instead of ingots.");
        harderTools = cfg.getBoolean("harderTools", CATEGORY_CONFIG, harderTools, "Enables harder recipes for some ic2 and gtclassic electric tools.");
    }

    private static void initGenerationConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERATION, "Generation configuration");
        // pyrite nether ore
        pyriteGenerate = cfg.getBoolean("pyriteGenerate", CATEGORY_GENERATION, pyriteGenerate, "Generate Pyrite ore in the nether");
        pyriteSize = cfg.getInt("pyriteSize", CATEGORY_GENERATION, pyriteSize, 1, 64, "Max size of Pyrite veins");
        pyriteWeight = cfg.getInt("pyriteWeight", CATEGORY_GENERATION, pyriteWeight, 1, 32, "Chance of Pyrite veins to spawn");

        // cinnabar nether ore
        cinnabarGenerate = cfg.getBoolean("cinnabarGenerate", CATEGORY_GENERATION, cinnabarGenerate, "Generate Cinnabar ore in the nether");
        cinnabarSize = cfg.getInt("cinnabarSize", CATEGORY_GENERATION, cinnabarSize, 1, 32, "Max size of Cinnabar veins");
        cinnabarWeight = cfg.getInt("cinnabarWeight", CATEGORY_GENERATION, cinnabarWeight, 1, 32, "Chance of Cinnabar veins to spawn");
        // sphalerite nether ore
        sphaleriteGenerate = cfg.getBoolean("sphaleriteGenerate", CATEGORY_GENERATION, sphaleriteGenerate, "Generate Sphalerite ore in the nether");
        sphaleriteSize = cfg.getInt("sphaleriteSize", CATEGORY_GENERATION, sphaleriteSize, 1, 64, "Max size of Sphalerite veins");
        sphaleriteWeight = cfg.getInt("sphaleriteWeight", CATEGORY_GENERATION, sphaleriteWeight, 1, 32, "Chance of Sphalerite veins to spawn");

        // tungstate end ore
        tungstateGenerate = cfg.getBoolean("tungstateGenerate", CATEGORY_GENERATION, tungstateGenerate, "Generate Tungstate ore in the end");
        tungstateSize = cfg.getInt("tungstateSize", CATEGORY_GENERATION, tungstateSize, 1, 32, "Max size of Tungstate veins");
        tungstateWeight = cfg.getInt("tungstateWeight", CATEGORY_GENERATION, tungstateWeight, 1, 32, "Chance of Tungstate veins to spawn");
        // sheldonite end ore
        sheldoniteGenerate = cfg.getBoolean("sheldoniteGenerate", CATEGORY_GENERATION, sheldoniteGenerate, "Generate Sheldonite ore in the end");
        sheldoniteSize = cfg.getInt("sheldoniteSize", CATEGORY_GENERATION, sheldoniteSize, 1, 32, "Max size of Sheldonite veins");
        sheldoniteWeight = cfg.getInt("sheldoniteWeight", CATEGORY_GENERATION, sheldoniteWeight, 1, 32, "Chance of Sheldonite veins to spawn");
        // olivine end ore
        olivineGenerate = cfg.getBoolean("olivineGenerate", CATEGORY_GENERATION, olivineGenerate, "Generate Olivine ore in the end");
        olivineSize = cfg.getInt("olivineSize", CATEGORY_GENERATION, olivineSize, 1, 32, "Max size of Olivine veins");
        olivineWeight = cfg.getInt("olivineWeight", CATEGORY_GENERATION, olivineWeight, 1, 32, "Chance of Olivine veins to spawn");
        // sodalite end ore
        sodaliteGenerate = cfg.getBoolean("sodaliteGenerate", CATEGORY_GENERATION, sodaliteGenerate, "Generate Sodalite ore in the end");
        sodaliteSize = cfg.getInt("sodaliteSize", CATEGORY_GENERATION, sodaliteSize, 1, 32, "Max size of Sodalite veins");
        sodaliteWeight = cfg.getInt("sodaliteWeight", CATEGORY_GENERATION, sodaliteWeight, 1, 32, "Chance of Sodalite veins to spawn");
    }
}
