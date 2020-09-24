package gtc_expansion;

import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.material.GTCXMaterialDict;
import gtc_expansion.material.GTCXMaterialGen;
import gtclassic.api.material.GTMaterial;
import gtclassic.common.GTItems;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GTCXOreDict {
    public static void init(){
        GTCXMaterialDict.init();
        OreDictionary.registerOre("orePyrite", GTCXBlocks.orePyrite);
        OreDictionary.registerOre("oreCinnabar", GTCXBlocks.oreCinnabar);
        OreDictionary.registerOre("oreSphalerite", GTCXBlocks.oreSphalerite);
        OreDictionary.registerOre("oreTungstate", GTCXBlocks.oreTungstate);
        OreDictionary.registerOre("oreTungsten", GTCXBlocks.oreTungstate);
        OreDictionary.registerOre("oreSheldonite", GTCXBlocks.oreSheldonite);
        OreDictionary.registerOre("orePlatinum", GTCXBlocks.oreSheldonite);
        OreDictionary.registerOre("oreOlivine", GTCXBlocks.oreOlivine);
        OreDictionary.registerOre("oreSodalite", GTCXBlocks.oreSodalite);
        OreDictionary.registerOre("oreOlivine", GTCXBlocks.oreOlivineOverworld);
        OreDictionary.registerOre("oreGalena", GTCXBlocks.oreGalena);
        OreDictionary.registerOre("oreCassiterite", GTCXBlocks.oreCassiterite);
        OreDictionary.registerOre("oreTetrahedrite", GTCXBlocks.oreTetrahedrite);
        OreDictionary.registerOre("oreChromite", GTCXBlocks.oreChromite);
        OreDictionary.registerOre("craftingDiamondGrinder", GTCXItems.diamondGrinder);
        OreDictionary.registerOre("craftingTungstenGrinder", GTCXItems.wolframiumGrinder);
        OreDictionary.registerOre("craftingToolWireCutter", new ItemStack(GTCXItems.cutter, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("blockCoke", GTCXBlocks.coalCokeBlock);

        // Registering Aluminium for idiots
        GTMaterial aluminium = GTMaterial.Aluminium;
        OreDictionary.registerOre("plateAluminum", (GTCXMaterialGen.getPlate(aluminium, 1)));
        OreDictionary.registerOre("rodAluminum", (GTCXMaterialGen.getRod(aluminium, 1)));
        OreDictionary.registerOre("gearAluminum", (GTCXMaterialGen.getGear(aluminium, 1)));
        OreDictionary.registerOre("dustSmallAluminum", (GTCXMaterialGen.getSmallDust(aluminium, 1)));
        OreDictionary.registerOre("batteryRegular", new ItemStack(GTItems.lithiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("batteryRegular", new ItemStack(GTCXItems.sodiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("energyCrystal", new ItemStack(GTItems.lithiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("energyCrystal", Ic2Items.energyCrystal);
        OreDictionary.registerOre("energyCrystal", new ItemStack(GTCXItems.sodiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("energyCrystal", new ItemStack(GTCXItems.largeLithiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("energyCrystal", new ItemStack(GTCXItems.largeSodiumBattery, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("plateWood", GTCXItems.woodPlate);
        OreDictionary.registerOre("plankWood", GTCXItems.woodPlate);
        OreDictionary.registerOre("blockGlass", GTCXBlocks.pureGlass);
        OreDictionary.registerOre("fuelCoke", GTCXItems.coalCoke);
    }
}
