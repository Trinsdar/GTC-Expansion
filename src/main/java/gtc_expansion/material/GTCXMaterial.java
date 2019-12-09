package gtc_expansion.material;

import gtc_expansion.GTCExpansion;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialFlag;
import gtclassic.api.material.GTMaterialGen;

public class GTCXMaterial {
    static String modid = GTCExpansion.MODID;
    static String tex = modid + "_materials";


    static GTMaterialFlag particle = new GTMaterialFlag("_particle", tex,15, false, modid);
    static GTMaterialFlag fluid = GTMaterialFlag.FLUID;
    public static GTMaterialFlag molten = new GTMaterialFlag("molten", tex, 14, true, modid);
    static GTMaterialFlag gas = GTMaterialFlag.GAS;
    public static GTMaterialFlag smalldust = new GTMaterialFlag("_dustsmall", tex, 1, false, modid);
    public static GTMaterialFlag tinydust = new GTMaterialFlag("_dusttiny", tex, 13, false, modid);
    public static GTMaterialFlag crushedore = new GTMaterialFlag("_crushedore", tex, 10, true, modid);
    public static GTMaterialFlag crushedorePurified = new GTMaterialFlag("_crushedorepurified", tex, 12, false, modid);
    static GTMaterialFlag dust = GTMaterialFlag.DUST;
    static GTMaterialFlag gemRubyShape = GTMaterialFlag.RUBY;
    static GTMaterialFlag gemSapphireShape = GTMaterialFlag.SAPPHIRE;
    public static GTMaterialFlag gemGarnetShape = new GTMaterialFlag("_gem", tex, 0, false, modid);
    static GTMaterialFlag ingot = GTMaterialFlag.INGOT;
    public static GTMaterialFlag hotIngot = GTMaterialFlag.INGOTHOT;
    public static GTMaterialFlag nugget = new GTMaterialFlag("_nugget", tex, 4, false, modid);
    public static GTMaterialFlag plate = new GTMaterialFlag("_plate", tex, 5, false, modid);
    public static GTMaterialFlag gear = new GTMaterialFlag("_gear", tex, 6, false, modid);
    public static GTMaterialFlag stick = new GTMaterialFlag("_stick", tex, 7, false, modid);
    public static GTMaterialFlag hull = new GTMaterialFlag("_hull", tex, 8, true, modid);
    static GTMaterialFlag blockMetal = GTMaterialFlag.BLOCKMETAL;
    public static GTMaterialFlag blockGem = GTMaterialFlag.BLOCKGEM;
    static GTMaterialFlag[] dustAll = { smalldust, dust };
    static GTMaterialFlag[] crushedAll = { dust, smalldust, crushedore, crushedorePurified };
    static GTMaterialFlag[] gemAll1 = { smalldust, dust, gemRubyShape, blockGem };
    static GTMaterialFlag[] gemAll2 = { smalldust, dust, gemSapphireShape, blockGem };
    static GTMaterialFlag[] gemAll3 = { smalldust, dust, gemGarnetShape, blockGem };
    static GTMaterialFlag[] metalFull = { molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal };
    static GTMaterialFlag[] metalFullWHull = { molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, hull };
    static GTMaterialFlag[] metalFullHot = { molten, smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal };
    static GTMaterialFlag[] metalBase = { molten, smalldust, dust, nugget, ingot, plate, stick };
    public static final GTMaterial
    Almandine = new GTMaterial("Almandine", 255, 0, 0, dustAll),
    Aluminium = new GTMaterial(13,"Aluminium", 128, 200, 240, metalFullWHull),
    Andradite = new GTMaterial("Andradite", 150, 120, 0, dustAll),
    Antimony = new GTMaterial(51,"Antimony", 220, 220, 240, molten, smalldust, dust, tinydust, nugget, ingot, plate, stick, gear, blockMetal),
    Ashes = new GTMaterial("Ashes", 192, 192, 192, dustAll),
    Basalt = new GTMaterial("Basalt", 30, 20, 20, dustAll),
    Bauxite = new GTMaterial("Bauxite", 200, 100, 0, crushedAll),
    BatteryAlloy = new GTMaterial("BatteryAlloy", 95, 77, 126, dust, ingot, smalldust, nugget, plate),
    Brass = new GTMaterial("Brass", 255, 180, 0, metalFullWHull),
    Bronze = new GTMaterial("Bronze", 230, 83, 34, molten, smalldust, nugget, plate, stick, gear, hull),
    Calcite = new GTMaterial("Calcite", 250, 230, 220, dustAll),
    Carbon = new GTMaterial(6,"Carbon", 0, 0, 0,dustAll),
    Charcoal = new GTMaterial("Charcoal", 100, 70, 70, smalldust),
    Chrome = new GTMaterial(24,"Chrome", 240, 210, 230, false, metalFull),
    Cinnabar = new GTMaterial("Cinnabar", 150, 0, 0, crushedAll),
    Clay = new GTMaterial("Clay", 200, 200, 220, smalldust),
    Coal = new GTMaterial("Coal", 70, 70, 70, smalldust),
    Constantan = new GTMaterial("Constantan", 196, 116, 77, molten, smalldust, dust, nugget, ingot, plate, stick),
    Copper = new GTMaterial("Copper", 180, 113, 61, molten, smalldust, nugget, plate, stick, gear),
    DarkAshes = new GTMaterial("DarkAshes", 50, 50, 50, dustAll),
    Diamond = new GTMaterial("Diamond", 51, 235, 203, dustAll),
    Diesel = new GTMaterial("Diesel", 255, 255, 0, fluid),
    Electrum = new GTMaterial("Electrum", 255, 255, 100, metalFull),
    Emerald = new GTMaterial("Emerald", 80, 255, 80, dustAll),
    EnderEye = new GTMaterial("EnderEye", 160, 250, 230, dustAll),
    EnderPearl = new GTMaterial("EnderPearl", 108, 220, 200, dustAll),
    Endstone = new GTMaterial("Endstone", 250, 250, 198, dustAll),
    Flint = new GTMaterial("Flint", 0, 32, 64, dustAll),
    Galena = new GTMaterial("Galena", 100, 60, 100, smalldust, dust, crushedore, crushedorePurified),
    GarnetRed = new GTMaterial("RedGarnet", 200, 80, 80, gemAll3),
    GarnetYellow = new GTMaterial("YellowGarnet", 200, 200, 80, dust, smalldust, gemGarnetShape, blockGem, tinydust),
    Gasoline = new GTMaterial("Gasoline", 132, 114, 62, fluid),
    Glowstone = new GTMaterial("Glowstone", 255, 255, 0, smalldust),
    Glyceryl = new GTMaterial("Glyceryl",52, 157, 157, fluid),
    Gold = new GTMaterial("Gold", 255, 255, 30, molten, smalldust, plate, stick, gear),
    Granite = new GTMaterial("Granite", 165, 89, 39, dustAll),
    Grossular = new GTMaterial("Grossular", 200, 100, 0, dust, smalldust, tinydust),
    Gunpowder = new GTMaterial("Gunpowder", 128, 128, 128, smalldust),
    Iridium = new GTMaterial(77,"Iridium", 255, 255, 255, false, molten, smalldust, dust, nugget, ingot, hotIngot, gear, stick, blockMetal, tinydust, crushedore, crushedorePurified),
    Iron = new GTMaterial("Iron", 184, 184, 184, molten, smalldust, plate, stick, gear),
    Invar = new GTMaterial("Invar", 220, 220, 150, metalFull),
    Kanthal = new GTMaterial("Kanthal", 219, 191, 111, false, dust, smalldust, nugget, ingot, plate, hotIngot),
    Lazurite = new GTMaterial("Lazurite", 100, 120, 255, dustAll),
    Lead = new GTMaterial(82,"Lead", 140, 100, 140, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal),
    Lithium = new GTMaterial(3,"Lithium", 87, 150, 204, dustAll),
    Magnalium = new GTMaterial("Magnalium", 200, 190, 255, metalBase),
    Magnesium = new GTMaterial(12,"Magnesium", 255, 200, 200, dustAll),
    Manganese = new GTMaterial(25,"Manganese", 250, 235, 250, molten, smalldust, dust, tinydust, nugget, ingot, plate, blockMetal),
    Marble = new GTMaterial("Marble", 200, 200, 200, dustAll),
    Naphtha = new GTMaterial("Naphtha", 255, 255, 100, fluid),
    Netherrack = new GTMaterial("Netherrack", 200, 0, 0, smalldust),
    Nickel = new GTMaterial(28,"Nickel", 250, 250, 200, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, tinydust),
    Nichrome = new GTMaterial("Nichrome", 205, 206, 246, false, molten, smalldust, dust, nugget, ingot, plate, stick),
    NitroCarbon = new GTMaterial("NitroCarbon", 31, 94, 94, fluid),
    NitroCoalFuel = new GTMaterial("NitroCoalFuel", 0, 43, 43, fluid),
    NitroDiesel = new GTMaterial("NitroDiesel", 191, 255, 100, fluid),
    NitrogenDioxide = new GTMaterial("NitrogenDioxide", 109, 185, 185, fluid),
    Obsidian = new GTMaterial("Obsidian", 80, 50, 100, smalldust),
    OilCrude = new GTMaterial("Crude_Oil", 0, 0, 0, fluid),
    Olivine = new GTMaterial("Olivine", 150, 255, 150, gemAll1),
    Osmium = new GTMaterial(76,"Osmium", 50, 50, 255, false, molten, smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal, tinydust),
    Phosphorus = new GTMaterial(15,"Phosphorus", 190, 0, 0, dust, smalldust, tinydust),
    Platinum = new GTMaterial(78,"Platinum", 100, 180, 250, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, tinydust, crushedore, crushedorePurified),
    Plutonium = new GTMaterial(94,"Plutonium", 240, 50, 50, smalldust, dust, nugget, ingot, plate, blockMetal, molten),
    Propane = new GTMaterial("Propane", 81, 27, 27, gas),
    Pyrite = new GTMaterial("Pyrite", 150, 120, 40, crushedAll),
    Pyrope = new GTMaterial("Pyrope", 120, 50, 100, dustAll),
    RedAlloy = new GTMaterial("RedAlloy", 200, 0, 0, molten, smalldust, dust, ingot, plate, nugget, stick),
    RedRock = new GTMaterial("RedRock", 255, 80, 50, dustAll),
    Redstone = new GTMaterial("Redstone", 200, 0, 0, smalldust, tinydust),
    RefinedIron = new GTMaterial("RefinedIron", 220, 235, 235, stick, plate, gear, hull),
    Ruby = new GTMaterial("Ruby", 255, 100, 100, gemAll1),
    Saltpeter = new GTMaterial("Saltpeter", 230, 230, 230, dustAll),
    Sapphire = new GTMaterial("Sapphire", 100, 100, 200, gemAll2),
    Silicon = new GTMaterial(14,"Silicon", 60, 60, 80, fluid, smalldust, dust, nugget, ingot, plate),
    Silver = new GTMaterial("Silver", 215, 225, 230, molten, smalldust, nugget, plate, stick, gear),
    Slag = new GTMaterial("Slag", 64, 48, 0, dustAll),
    Sodalite = new GTMaterial("Sodalite", 20, 20, 255,dustAll),
    SodiumPersulfate = new GTMaterial("SodiumPersulfate", 0, 102, 70, fluid),
    SodiumSulfide = new GTMaterial("SodiumSulfide", 161, 168, 73, fluid),
    Spessartine = new GTMaterial("Spessartine", 255, 100, 100, dustAll),
    Sphalerite = new GTMaterial("Sphalerite", 200, 140, 40, crushedAll),
    StainlessSteel = new GTMaterial("StainlessSteel", 200, 200, 220, false, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, hull),
    Steel = new GTMaterial("Steel", 128, 128, 128, false, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, hull),
    Stone = new GTMaterial("Stone", 196, 196, 196, dustAll),
    Sulfur = new GTMaterial(16,"Sulfur", 200, 200, 0, smalldust, dust, tinydust),
    Tetrahedrite = new GTMaterial("Tetrahedrite", 200, 32, 0 , smalldust, dust, crushedore, crushedorePurified),
    Thorium = new GTMaterial(90,"Thorium", 0, 30, 0, false, smalldust, dust, nugget, ingot, blockMetal, molten),
    Tin = new GTMaterial("Tin", 220, 220, 220, molten, smalldust, nugget, plate, stick, gear),
    Titanium = new GTMaterial(22,"Titanium", 170, 143, 222, false, molten, smalldust, dust, tinydust, nugget, ingot, plate, stick, gear, blockMetal, hull),
    Tungsten = new GTMaterial(74,"Tungsten", 50, 50, 50, false, molten, smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal, crushedore, crushedorePurified),
    TungstenSteel = new GTMaterial("Tungstensteel", 100, 100, 160, false, molten, smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal, hull),
    Uranium = new GTMaterial("Uranium", 50, 240, 50, dust, smalldust, molten),
    Uvarovite = new GTMaterial("Uvarovite", 180, 255, 180, dustAll),
    Wood = new GTMaterial("Wood", 137, 103, 39, dustAll),
    Zinc = new GTMaterial(30,"Zinc", 250, 240, 240, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, tinydust);

    public static void initMaterials(){
        GTMaterialGen.addItemFlag(smalldust);
        GTMaterialGen.addItemFlag(tinydust);
        GTMaterialGen.addItemFlag(crushedore);
        GTMaterialGen.addItemFlag(crushedorePurified);
        GTMaterialGen.addItemFlag(gemGarnetShape);
        GTMaterialGen.addItemFlag(nugget);
        GTMaterialGen.addItemFlag(plate);
        GTMaterialGen.addItemFlag(gear);
        GTMaterialGen.addItemFlag(stick);
        GTMaterialGen.addItemFlag(hull);
        GTMaterialGen.addFluidFlag(molten);
    }

}
