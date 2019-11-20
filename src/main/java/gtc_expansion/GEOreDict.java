package gtc_expansion;

import gtc_expansion.material.GEMaterialDict;
import gtc_expansion.material.GEMaterialGen;
import gtclassic.api.material.GTMaterial;
import gtclassic.common.GTItems;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.item.ItemStack;
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
        OreDictionary.registerOre("oreGalena", GEBlocks.oreGalena);
        OreDictionary.registerOre("oreCassiterite", GEBlocks.oreCassiterite);
        OreDictionary.registerOre("oreTetrahedrite", GEBlocks.oreTetrahedrite);
        OreDictionary.registerOre("craftingDiamondGrinder", GEItems.diamondGrinder);
        OreDictionary.registerOre("craftingTungstenGrinder", GEItems.wolframiumGrinder);

        // Registering Aluminium for idiots
        GTMaterial aluminium = GTMaterial.Aluminium;
        OreDictionary.registerOre("plateAluminum", (GEMaterialGen.getPlate(aluminium, 1)));
        OreDictionary.registerOre("rodAluminum", (GEMaterialGen.getRod(aluminium, 1)));
        OreDictionary.registerOre("gearAluminum", (GEMaterialGen.getGear(aluminium, 1)));
        OreDictionary.registerOre("batteryRegular", new ItemStack(GTItems.lithiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("batteryRegular", new ItemStack(GEItems.sodiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("energyCrystal", new ItemStack(GTItems.lithiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("energyCrystal", Ic2Items.energyCrystal);
        OreDictionary.registerOre("energyCrystal", new ItemStack(GEItems.sodiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("energyCrystal", new ItemStack(GEItems.largeLithiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("energyCrystal", new ItemStack(GEItems.largeSodiumBattery, 1, OreDictionary.WILDCARD_VALUE));
    }
}
