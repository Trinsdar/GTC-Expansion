package gtc_expansion.material;

import gtc_expansion.GTCExpansion;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;

public class GEMaterial extends GTMaterial{
    static String tex = GTCExpansion.MODID + "_materials";

    static GTMaterialFlag particle = new GTMaterialFlag("_particle", tex,15, false);
    static GTMaterialFlag fluid = GTMaterialFlag.FLUID;
    static GTMaterialFlag gas = GTMaterialFlag.GAS;
    public static GTMaterialFlag smalldust = new GTMaterialFlag("_dustsmall", tex, 1, false);
    static GTMaterialFlag dust = GTMaterialFlag.DUST;
    static GTMaterialFlag gemRubyShape = GTMaterialFlag.RUBY;
    static GTMaterialFlag gemSapphireShape = GTMaterialFlag.SAPPHIRE;
    public static GTMaterialFlag gemGarnetShape = new GTMaterialFlag("_gem", tex, 0, false);
    static GTMaterialFlag ingot = GTMaterialFlag.INGOT;
    public static GTMaterialFlag hotIngot = new GTMaterialFlag("_hotingot", tex, 2, true);
    public static GTMaterialFlag nugget = new GTMaterialFlag("_nugget", tex, 4, false);
    public static GTMaterialFlag plate = new GTMaterialFlag("_plate", tex, 5, false);
    static GTMaterialFlag smallplate = new GTMaterialFlag("_smallplate", tex, 6, false);
    public static GTMaterialFlag stick = new GTMaterialFlag("_stick", tex, 7, false);
    static GTMaterialFlag magnetic = new GTMaterialFlag("_magneticstick", tex, 7, true);
    static GTMaterialFlag wire = new GTMaterialFlag("_finewire", tex, 11, false);
    public static GTMaterialFlag gear = new GTMaterialFlag("_gear", tex, 9, false);
    static GTMaterialFlag foil = new GTMaterialFlag("_foil", tex, 10, false);
    static GTMaterialFlag blockMetal = GTMaterialFlag.BLOCKMETAL;
    public static GTMaterialFlag blockGem = GTMaterialFlag.BLOCKGEM;
    static GTMaterialFlag hull = new GTMaterialFlag("_hull", tex, 50, false);
    static GTMaterialFlag coil = new GTMaterialFlag("_coil", tex, 52, false);
    static GTMaterialFlag[] dustAll = { smalldust, dust };
    static GTMaterialFlag[] gemAll1 = { smalldust, dust, gemRubyShape, blockGem };
    static GTMaterialFlag[] gemAll2 = { smalldust, dust, gemSapphireShape, blockGem };
    static GTMaterialFlag[] gemAll3 = { smalldust, dust, gemGarnetShape, blockGem };
    static GTMaterialFlag[] metalFull = { smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal };
    static GTMaterialFlag[] metalFullWHull = { smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, hull };
    static GTMaterialFlag[] metalFullHot = { smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal,  };
    static GTMaterialFlag[] metalSemiFull = { smalldust, nugget, plate, stick, gear };
    static GTMaterialFlag[] metalBase = { smalldust, dust, nugget, ingot, plate, stick };
    static GTMaterialFlag[] metalIc2 = { smalldust, nugget, plate, stick, gear };
    static GTMaterialFlag[] metalMC = { smalldust, plate, stick, gear };
    static GTMaterialFlag[] slurryBase = { smalldust, dust, fluid };

    public static final GEMaterial
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
    Thorium,
    Tin,
    Titanium,
    Tungsten,
    TungstenSteel,
    Uranium,
    Uvarovite,
    Wood,
    Zinc;

    private float speed;
    private int durability;
    private int level;

    public GEMaterial(String displayName, int r, int g, int b, boolean smeltable, float speed, int durability, int level, GTMaterialFlag... flags){
        super(displayName, r, g, b, smeltable, flags);
        this.speed = speed;
        this.durability = durability;
        this.level = level;
    }

    public GEMaterial(String displayName, int r, int g, int b, float speed, int durability, int level, GTMaterialFlag... flags){
        super(displayName, r, g, b, flags);
        this.speed = speed;
        this.durability = durability;
        this.level = level;
    }

    public GEMaterial(String displayName, int r, int g, int b, boolean smeltable, GTMaterialFlag... flags){
        super(displayName, r, g, b, smeltable, flags);
        this.speed = 1.0f;
        this.durability = 0;
        this.level = 1;
    }

    public GEMaterial(String displayName, int r, int g, int b, GTMaterialFlag... flags){
        super(displayName, r, g, b, flags);
        this.speed = 1.0f;
        this.durability = 0;
        this.level = 1;
    }

    public static GEMaterial replaceMaterial(GTMaterial oldMaterial, GEMaterial newMaterial){
        oldMaterial = newMaterial;
        return newMaterial;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDurability() {
        return durability;
    }

    public int getLevel() {
        return level;
    }

    static {
        Almandine = new GEMaterial("Almandine", 255, 0, 0, dustAll);
        Aluminium = replaceMaterial(GTMaterial.Aluminium, new GEMaterial("Aluminium", 128, 200, 240, 10.0F, 128, 2, smalldust, dust, nugget, ingot, plate, stick, wire, gear, blockMetal, hull, foil));
        Andradite = new GEMaterial("Andradite", 150, 120, 0, dustAll);
        Ashes = new GEMaterial("Ashes", 192, 192, 192, dustAll);
        Basalt = replaceMaterial(GTMaterial.Basalt, new GEMaterial("Basalt", 30, 20, 20, 5.0F, 0, 2, dustAll));
        Bauxite = replaceMaterial(GTMaterial.Bauxite, new GEMaterial("Bauxite", 200, 100, 0, 3.0F, 0, 1, dustAll));
        Brass = new GEMaterial("Brass", 255, 180, 0, 7.0F, 96, 1, metalFullWHull);
        Bronze = new GEMaterial("Bronze", 230, 83, 34, 6.0F, 192, 2, smalldust, nugget, plate, stick, gear, hull);
        Calcite = replaceMaterial(GTMaterial.Calcite, new GEMaterial("Calcite", 250, 230, 220, 3.0F, 0, 1, dustAll));
        Carbon = replaceMaterial(GTMaterial.Carbon, new GEMaterial("Carbon", 0, 0, 0,dustAll));
        Charcoal = new GEMaterial("Charcoal", 100, 70, 70, smalldust);
        Chrome = replaceMaterial(GTMaterial.Chrome, new GEMaterial("Chrome", 240, 210, 230, false, 11.0F, 256, 3, metalFull));
        Cinnabar = new GEMaterial("Cinnabar", 150, 0, 0, 3.0F, 0, 2, dustAll);
        Clay = new GEMaterial("Clay", 200, 200, 220, smalldust);
        Coal = new GEMaterial("Coal", 70, 70, 70, 3.0F, 0, 1, smalldust);
        Constantan = new GEMaterial("Constantan", 196, 116, 77, 8.0F, 128, 2, smalldust, dust, nugget, ingot, plate, stick, coil);
        Copper = new GEMaterial("Copper", 180, 113, 61, smalldust, nugget, plate, stick, wire, gear, coil, foil);
        DarkAshes = new GEMaterial("DarkAshes", 50, 50, 50, dustAll);
        Diamond = new GEMaterial("Diamond", 51, 235, 203, 8.0F, 1280, 3, dustAll);
        Electrum = replaceMaterial(GTMaterial.Electrum, new GEMaterial("Electrum", 255, 255, 100, 12.0F, 64, 2, metalFull));
        Emerald = replaceMaterial(GTMaterial.Emerald, new GEMaterial("Emerald", 80, 255, 80, dustAll));
        EnderEye = replaceMaterial(GTMaterial.EnderEye, new GEMaterial("EnderEye", 160, 250, 230, dustAll));
        EnderPearl = replaceMaterial(GTMaterial.EnderPearl, new GEMaterial("EnderPearl", 108, 220, 200, dustAll));
        Endstone = new GEMaterial("Endstone", 250, 250, 198, dustAll);
        Flint = replaceMaterial(GTMaterial.Flint, new GEMaterial("Flint", 0, 32, 64, 2.5F, 64, 1, dustAll));
        Galena = new GEMaterial("Galena", 100, 60, 100, 3.0F, 0, 1, dustAll);
        GarnetRed = new GEMaterial("RedGarnet", 200, 80, 80, 7.0F, 128, 2, gemAll3);
        GarnetYellow = new GEMaterial("YellowGarnet", 200, 200, 80, 7.0F, 128, 2, gemAll3);
        Glowstone = new GEMaterial("Glowstone", 255, 255, 0, smalldust);
        Gold = new GEMaterial("Gold", 255, 255, 30, 12.0F, 64, 2, smalldust, plate, stick, wire, gear);
        Granite = new GEMaterial("Granite", 165, 89, 39, dustAll);
        Grossular = new GEMaterial("Grossular", 200, 100, 0, dustAll);
        Gunpowder = new GEMaterial("Gunpowder", 128, 128, 128, smalldust);
        Iridium = replaceMaterial(GTMaterial.Iridium, new GEMaterial("Iridium", 255, 255, 255, false, 6.0F, 5120, 4, smalldust, dust, nugget, ingot, hotIngot, gear, stick, blockMetal));
        Iron = new GEMaterial("Iron", 184, 184, 184, 6.0F, 256, 2, smalldust, plate, stick, gear, magnetic);
        Invar = new GEMaterial("Invar", 220, 220, 150, 6.0F, 256, 2, metalFull);
        Lazurite = replaceMaterial(GTMaterial.Lazurite, new GEMaterial("Lazurite", 100, 120, 255, dustAll));
        Lead = new GEMaterial("Lead", 140, 100, 140, 8.0F, 64, 1, smalldust, dust, nugget, ingot, plate, stick, wire, gear, blockMetal);
        Lithium = replaceMaterial(GTMaterial.Lithium, new GEMaterial("Lithium", 87, 150, 204, dustAll));
        Magnalium = new GEMaterial("Magnalium", 200, 190, 255, 6.0F, 256, 2, metalBase);
        Magnesium = new GEMaterial("Magnesium", 255, 200, 200, dustAll);
        Manganese = new GEMaterial("Manganese", 250, 235, 250, smalldust, dust, nugget, ingot, plate, blockMetal);
        Marble = new GEMaterial("Marble", 200, 200, 200, dustAll);
        Netherrack = new GEMaterial("Netherrack", 200, 0, 0, smalldust);
        Nickel = new GEMaterial("Nickel", 200, 200, 250, 6.0F, 64, 2, smalldust, dust, nugget, ingot, plate, stick, wire, gear, blockMetal);
        Nichrome = new GEMaterial("Nichrome", 205, 206, 246, 10.0F, 256, 3, smalldust, dust, nugget, ingot, plate, stick, coil);
        Obsidian = new GEMaterial("Obsidian", 80, 50, 100, smalldust);
        OilCrude = new GEMaterial("Crude_Oil", 0, 0, 0, fluid);
        Olivine = new GEMaterial("Olivine", 150, 255, 150, 7.0F, 256, 2, gemAll1);
        Osmium = new GEMaterial("Osmium", 50, 50, 255, false, 16.0F, 1280, 4, metalFullHot);
        Phosphorus = new GEMaterial("Phosphorus", 190, 0, 0, dustAll);
        Platinum = replaceMaterial(GTMaterial.Platinum, new GEMaterial("Platinum", 255, 255, 200, false, 12.0F, 64, 2, smalldust, dust, nugget, ingot, plate, stick, wire, gear, blockMetal));
        Plutonium = replaceMaterial(GTMaterial.Plutonium, new GEMaterial("Plutonium", 240, 50, 50, 6.0F, 512, 3, smalldust, dust, nugget, ingot, plate));
        Pyrite = replaceMaterial(GTMaterial.Pyrite, new GEMaterial("Pyrite", 150, 120, 40, 3.0F, 0, 1, dustAll));
        Pyrope = new GEMaterial("Pyrope", 120, 50, 100, dustAll);
        RedAlloy = new GEMaterial("RedAlloy", 200, 0, 0, smalldust, dust, ingot, nugget, stick, wire);
        RedRock = new GEMaterial("RedRock", 255, 80, 50, dustAll);
        Redstone = new GEMaterial("Redstone", 200, 0, 0, smalldust);
        RefinedIron = new GEMaterial("RefinedIron", 220, 235, 235,6.0F, 384, 2, stick, plate, gear);
        Ruby = replaceMaterial(GTMaterial.Ruby, new GEMaterial("Ruby", 255, 100, 100, 7.0F, 256, 2, gemAll1));
        Saltpeter = new GEMaterial("Saltpeter", 230, 230, 230, 3.0F, 0, 1, dustAll);
        Sapphire = replaceMaterial(GTMaterial.Sapphire, new GEMaterial("Sapphire", 100, 100, 200, 7.0F, 256, 2, gemAll2));
        SapphireGreen = replaceMaterial(GTMaterial.SapphireGreen, new GEMaterial("GreenSapphire", 100, 200, 130, 7.0F, 256, 2, gemAll2));
        Silicon = replaceMaterial(GTMaterial.Silicon, new GEMaterial("Silicon", 60, 60, 80, 1.0F, 0, 1, fluid, smalldust, dust, nugget, ingot, plate, smallplate));
        Silver = new GEMaterial("Silver", 215, 225, 230, 10.0F, 64, 2, smalldust, nugget, plate, stick, wire, gear, foil);
        Slag = new GEMaterial("Slag", 64, 48, 0, dustAll);
        Sodalite = replaceMaterial(GTMaterial.Sodalite, new GEMaterial("Sodalite", 20, 20, 255, 3.0F, 0, 2,dustAll));
        Spessartine = new GEMaterial("Spessartine", 255, 100, 100, dustAll);
        Sphalerite = new GEMaterial("Sphalerite", 200, 140, 40, 2.0F, 0, 1, dustAll);
        StainlessSteel = new GEMaterial("StainlessSteel", 200, 200, 220, false, 7.0F, 480, 2, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal);
        Steel = replaceMaterial(GTMaterial.Steel, new GEMaterial("Steel", 128, 128, 128, 6.0F, 512, 2, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, hull, magnetic));
        Stone = new GEMaterial("Stone", 196, 196, 196, dustAll);
        Sulfur = new GEMaterial("Sulfur", 200, 200, 0, 2.0F, 0, 1, smalldust, dust);
        SulfuricAcid = new GEMaterial("SulfuricAcid", 255, 106, 0, fluid);
        Thorium = replaceMaterial(GTMaterial.Thorium, new GEMaterial("Thorium", 0, 30, 0, false, 6.0F, 512, 2, smalldust, dust, nugget, ingot));
        Tin = new GEMaterial("Tin", 220, 220, 220, smalldust, nugget, plate, stick, wire, gear);
        Titanium = replaceMaterial(GTMaterial.Titanium, new GEMaterial("Titanium", 170, 143, 222, 8.0F, 2560, 3, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, hull));
        Tungsten = replaceMaterial(GTMaterial.Tungsten, new GEMaterial("Tungsten", 50, 50, 50, 8.0F, 5120, 3, smalldust, dust, nugget, ingot, hotIngot, plate, stick, wire, gear, blockMetal));
        TungstenSteel = new GEMaterial("Tungstensteel", 100, 100, 160, 10.0F, 5120, 4, smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal);
        Uranium = replaceMaterial(GTMaterial.Uranium, new GEMaterial("Uranium", 50, 240, 50, 6.0F, 512, 3, dustAll));
        Uvarovite = new GEMaterial("Uvarovite", 180, 255, 180, dustAll);
        Wood = new GEMaterial("Wood", 137, 103, 39, dustAll);
        Zinc = new GEMaterial("Zinc", 250, 240, 240, metalFull);
    }
}
