package gtc_expansion.material;

import gtc_expansion.GTCExpansion;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;

public class GEMaterial {
    static String tex = GTCExpansion.MODID + "_materials";

    static GTMaterialFlag particle = new GTMaterialFlag("_particle", tex,15, false);
    static GTMaterialFlag fluid = GTMaterialFlag.FLUID;
    static GTMaterialFlag molten = GTMaterialFlag.MOLTEN;
    static GTMaterialFlag gas = GTMaterialFlag.GAS;
    public static GTMaterialFlag smalldust = new GTMaterialFlag("_dustsmall", tex, 1, false);
    public static GTMaterialFlag tinydust = new GTMaterialFlag("_dusttiny", tex, 12, false);
    public static GTMaterialFlag crushedore = new GTMaterialFlag("_crushedore", tex, 9, true);
    public static GTMaterialFlag crushedorePurified = new GTMaterialFlag("_crushedorepurified", tex, 11, false);
    static GTMaterialFlag dust = GTMaterialFlag.DUST;
    static GTMaterialFlag gemRubyShape = GTMaterialFlag.RUBY;
    static GTMaterialFlag gemSapphireShape = GTMaterialFlag.SAPPHIRE;
    public static GTMaterialFlag gemGarnetShape = new GTMaterialFlag("_gem", tex, 0, false);
    static GTMaterialFlag ingot = GTMaterialFlag.INGOT;
    public static GTMaterialFlag hotIngot = new GTMaterialFlag("_hotingot", tex, 2, true);
    public static GTMaterialFlag nugget = new GTMaterialFlag("_nugget", tex, 4, false);
    public static GTMaterialFlag plate = new GTMaterialFlag("_plate", tex, 5, false);
    public static GTMaterialFlag gear = new GTMaterialFlag("_gear", tex, 6, false);
    public static GTMaterialFlag stick = new GTMaterialFlag("_stick", tex, 7, false);
    public static GTMaterialFlag hull = new GTMaterialFlag("_hull", tex, 8, false);
    static GTMaterialFlag blockMetal = GTMaterialFlag.BLOCKMETAL;
    public static GTMaterialFlag blockGem = GTMaterialFlag.BLOCKGEM;
    static GTMaterialFlag[] dustAll = { smalldust, dust };
    static GTMaterialFlag[] gemAll1 = { smalldust, dust, gemRubyShape, blockGem };
    static GTMaterialFlag[] gemAll2 = { smalldust, dust, gemSapphireShape, blockGem };
    static GTMaterialFlag[] gemAll3 = { smalldust, dust, gemGarnetShape, blockGem };
    static GTMaterialFlag[] metalFull = { molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal };
    static GTMaterialFlag[] metalFullWHull = { molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, hull };
    static GTMaterialFlag[] metalFullHot = { molten, smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal,  };
    static GTMaterialFlag[] metalSemiFull = { smalldust, nugget, plate, stick, gear };
    static GTMaterialFlag[] metalBase = { molten, smalldust, dust, nugget, ingot, plate, stick };
    static GTMaterialFlag[] metalIc2 = { smalldust, nugget, plate, stick, gear };
    static GTMaterialFlag[] metalMC = { smalldust, plate, stick, gear };
    static GTMaterialFlag[] slurryBase = { smalldust, dust, fluid };

    public static final GTMaterial
    Almandine,
    Aluminium,
    Andradite,
    Ashes,
    Basalt,
    Bauxite,
    Brass,
    Bronze,
    Calcite,
    Carbon,
    Charcoal,
    Chrome,
    Cinnabar,
    Clay,
    Coal,
    Constantan,
    Copper,
    DarkAshes,
    Diamond,
    Electrum,
    Emerald,
    EnderEye,
    EnderPearl,
    Endstone,
    Flint,
    Galena,
    GarnetRed,
    GarnetYellow,
    Glowstone,
    Gold,
    Granite,
    Grossular,
    Gunpowder,
    Iridium,
    Iron,
    Invar,
    Lazurite,
    Lead,
    Lithium,
    Magnalium,
    Magnesium,
    Manganese,
    Marble,
    Netherrack,
    Nickel,
    Nichrome,
    Obsidian,
    OilCrude,
    Olivine,
    Osmium,
    Phosphorus,
    Platinum,
    Plutonium,
    Pyrite,
    Pyrope,
    RedAlloy,
    RedRock,
    Redstone,
    RefinedIron,
    Ruby,
    Saltpeter,
    Sapphire,
    SapphireGreen,
    Silicon,
    Silver,
    Slag,
    Sodalite,
    Spessartine,
    Sphalerite,
    StainlessSteel,
    Steel,
    Stone,
    Sulfur,
    SulfuricAcid,
    Tetrahedrite,
    Thorium,
    Tin,
    Titanium,
    Tungsten,
    TungstenSteel,
    Uranium,
    Uvarovite,
    Wood,
    Zinc;

    public static void initMaterials(){}

    static {
        Almandine = new GTMaterial("Almandine", 255, 0, 0, dustAll);
        Aluminium = new GTMaterial("Aluminium", 128, 200, 240, metalFullWHull);
        Andradite = new GTMaterial("Andradite", 150, 120, 0, dustAll);
        Ashes = new GTMaterial("Ashes", 192, 192, 192, dustAll);
        Basalt = new GTMaterial("Basalt", 30, 20, 20, dustAll);
        Bauxite = new GTMaterial("Bauxite", 200, 100, 0, dustAll);
        Brass = new GTMaterial("Brass", 255, 180, 0, metalFullWHull);
        Bronze = new GTMaterial("Bronze", 230, 83, 34, molten, smalldust, nugget, plate, stick, gear, hull);
        Calcite = new GTMaterial("Calcite", 250, 230, 220, dustAll);
        Carbon = new GTMaterial("Carbon", 0, 0, 0,dustAll);
        Charcoal = new GTMaterial("Charcoal", 100, 70, 70, smalldust);
        Chrome = new GTMaterial("Chrome", 240, 210, 230, false, metalFull);
        Cinnabar = new GTMaterial("Cinnabar", 150, 0, 0, dustAll);
        Clay = new GTMaterial("Clay", 200, 200, 220, smalldust);
        Coal = new GTMaterial("Coal", 70, 70, 70, smalldust);
        Constantan = new GTMaterial("Constantan", 196, 116, 77, molten, smalldust, dust, nugget, ingot, plate, stick);
        Copper = new GTMaterial("Copper", 180, 113, 61, molten, smalldust, nugget, plate, stick, gear);
        DarkAshes = new GTMaterial("DarkAshes", 50, 50, 50, dustAll);
        Diamond = new GTMaterial("Diamond", 51, 235, 203, dustAll);
        Electrum = new GTMaterial("Electrum", 255, 255, 100, metalFull);
        Emerald = new GTMaterial("Emerald", 80, 255, 80, dustAll);
        EnderEye = new GTMaterial("EnderEye", 160, 250, 230, dustAll);
        EnderPearl = new GTMaterial("EnderPearl", 108, 220, 200, dustAll);
        Endstone = new GTMaterial("Endstone", 250, 250, 198, dustAll);
        Flint = new GTMaterial("Flint", 0, 32, 64, dustAll);
        Galena = new GTMaterial("Galena", 100, 60, 100, smalldust, dust, crushedore, crushedorePurified);
        GarnetRed = new GTMaterial("RedGarnet", 200, 80, 80, gemAll3);
        GarnetYellow = new GTMaterial("YellowGarnet", 200, 200, 80, gemAll3);
        Glowstone = new GTMaterial("Glowstone", 255, 255, 0, smalldust);
        Gold = new GTMaterial("Gold", 255, 255, 30, molten, smalldust, plate, stick, gear);
        Granite = new GTMaterial("Granite", 165, 89, 39, dustAll);
        Grossular = new GTMaterial("Grossular", 200, 100, 0, dustAll);
        Gunpowder = new GTMaterial("Gunpowder", 128, 128, 128, smalldust);
        Iridium = new GTMaterial("Iridium", 255, 255, 255, false, molten, smalldust, dust, nugget, ingot, hotIngot, gear, stick, blockMetal);
        Iron = new GTMaterial("Iron", 184, 184, 184, molten, smalldust, plate, stick, gear);
        Invar = new GTMaterial("Invar", 220, 220, 150, metalFull);
        Lazurite = new GTMaterial("Lazurite", 100, 120, 255, dustAll);
        Lead = new GTMaterial("Lead", 140, 100, 140, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal);
        Lithium = new GTMaterial("Lithium", 87, 150, 204, dustAll);
        Magnalium = new GTMaterial("Magnalium", 200, 190, 255, metalBase);
        Magnesium = new GTMaterial("Magnesium", 255, 200, 200, dustAll);
        Manganese = new GTMaterial("Manganese", 250, 235, 250, molten, smalldust, dust, nugget, ingot, plate, blockMetal);
        Marble = new GTMaterial("Marble", 200, 200, 200, dustAll);
        Netherrack = new GTMaterial("Netherrack", 200, 0, 0, smalldust);
        Nickel = new GTMaterial("Nickel", 200, 200, 250, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal);
        Nichrome = new GTMaterial("Nichrome", 205, 206, 246, molten, smalldust, dust, nugget, ingot, plate, stick);
        Obsidian = new GTMaterial("Obsidian", 80, 50, 100, smalldust);
        OilCrude = new GTMaterial("Crude_Oil", 0, 0, 0, fluid);
        Olivine = new GTMaterial("Olivine", 150, 255, 150, gemAll1);
        Osmium = new GTMaterial("Osmium", 50, 50, 255, false, metalFullHot);
        Phosphorus = new GTMaterial("Phosphorus", 190, 0, 0, dustAll);
        Platinum = new GTMaterial("Platinum", 255, 255, 200, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal);
        Plutonium = new GTMaterial("Plutonium", 240, 50, 50, smalldust, dust, nugget, ingot, plate, blockMetal, fluid);
        Pyrite = new GTMaterial("Pyrite", 150, 120, 40, dustAll);
        Pyrope = new GTMaterial("Pyrope", 120, 50, 100, dustAll);
        RedAlloy = new GTMaterial("RedAlloy", 200, 0, 0, molten, smalldust, dust, ingot, nugget, stick);
        RedRock = new GTMaterial("RedRock", 255, 80, 50, dustAll);
        Redstone = new GTMaterial("Redstone", 200, 0, 0, smalldust);
        RefinedIron = new GTMaterial("RefinedIron", 220, 235, 235, stick, plate, gear);
        Ruby = new GTMaterial("Ruby", 255, 100, 100, gemAll1);
        Saltpeter = new GTMaterial("Saltpeter", 230, 230, 230, dustAll);
        Sapphire = new GTMaterial("Sapphire", 100, 100, 200, gemAll2);
        SapphireGreen = new GTMaterial("GreenSapphire", 100, 200, 130, gemAll2);
        Silicon = new GTMaterial("Silicon", 60, 60, 80, fluid, smalldust, dust, nugget, ingot, plate);
        Silver = new GTMaterial("Silver", 215, 225, 230, molten, smalldust, nugget, plate, stick, gear);
        Slag = new GTMaterial("Slag", 64, 48, 0, dustAll);
        Sodalite = new GTMaterial("Sodalite", 20, 20, 255,dustAll);
        Spessartine = new GTMaterial("Spessartine", 255, 100, 100, dustAll);
        Sphalerite = new GTMaterial("Sphalerite", 200, 140, 40, dustAll);
        StainlessSteel = new GTMaterial("StainlessSteel", 200, 200, 220, false, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal);
        Steel = new GTMaterial("Steel", 128, 128, 128, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, hull);
        Stone = new GTMaterial("Stone", 196, 196, 196, dustAll);
        Sulfur = new GTMaterial("Sulfur", 200, 200, 0, smalldust, dust, tinydust);
        SulfuricAcid = new GTMaterial("SulfuricAcid", 255, 106, 0, fluid);
        Tetrahedrite = new GTMaterial("Tetrahedrite", 200, 32, 0 , smalldust, dust, crushedore, crushedorePurified);
        Thorium = new GTMaterial("Thorium", 0, 30, 0, false, smalldust, dust, nugget, ingot, blockMetal, fluid);
        Tin = new GTMaterial("Tin", 220, 220, 220, molten, smalldust, nugget, plate, stick, gear);
        Titanium = new GTMaterial("Titanium", 170, 143, 222, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, hull);
        Tungsten = new GTMaterial("Tungsten", 50, 50, 50, molten, smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal);
        TungstenSteel = new GTMaterial("Tungstensteel", 100, 100, 160, molten, smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal);
        Uranium = new GTMaterial("Uranium", 50, 240, 50, dust, smalldust, molten);
        Uvarovite = new GTMaterial("Uvarovite", 180, 255, 180, dustAll);
        Wood = new GTMaterial("Wood", 137, 103, 39, dustAll);
        Zinc = new GTMaterial("Zinc", 250, 240, 240, molten, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, tinydust);
    }
}
