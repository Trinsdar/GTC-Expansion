package gtc_expansion;

import gtc_expansion.material.GEMaterialDict;
import net.minecraftforge.oredict.OreDictionary;

public class GEOreDict {
    public static void init(){
        GEMaterialDict.init();
        OreDictionary.registerOre("orePyrite", GEBlocks.orePyrite);
        OreDictionary.registerOre("oreCinnabar", GEBlocks.oreCinnabar);
        OreDictionary.registerOre("oreSphalerite", GEBlocks.oreSphalerite);
        OreDictionary.registerOre("oreTungstate", GEBlocks.oreTungstate);
        OreDictionary.registerOre("oreSheldonite", GEBlocks.oreSheldonite);
        OreDictionary.registerOre("orePlatinum", GEBlocks.oreSheldonite);
        OreDictionary.registerOre("oreOlivine", GEBlocks.oreOlivine);
        OreDictionary.registerOre("oreSodalite", GEBlocks.oreSodalite);
        OreDictionary.registerOre("oreOlivine", GEBlocks.oreOlivineOverworld);
        OreDictionary.registerOre("ingotIridiumAlloy", GEItems.iridiumAlloyIngot);
        OreDictionary.registerOre("craftingDiamondGrinder", GEItems.diamondGrinder);
        OreDictionary.registerOre("craftingTungstenGrinder", GEItems.wolframiumGrinder);
    }
}
