package gtc_expansion.material;

import gtc_expansion.GTMod2;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;

public class GTMaterial2 extends GTMaterial{
    static String tex = GTMod2.MODID + "_materials";

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
    static GTMaterialFlag casing = new GTMaterialFlag("_casing", tex, 50, false);
    static GTMaterialFlag coil = new GTMaterialFlag("_coil", tex, 52, false);
    static GTMaterialFlag[] dustAll = { smalldust, dust };
    static GTMaterialFlag[] gemAll1 = { smalldust, dust, gemRubyShape, blockGem };
    static GTMaterialFlag[] gemAll2 = { smalldust, dust, gemSapphireShape, blockGem };
    static GTMaterialFlag[] gemAll3 = { smalldust, dust, gemGarnetShape, blockGem };
    static GTMaterialFlag[] metalFull = { smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, casing };
    static GTMaterialFlag[] metalFullHot = { smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal, casing };
    static GTMaterialFlag[] metalSemiFull = { smalldust, nugget, plate, stick, gear, casing };
    static GTMaterialFlag[] metalBase = { smalldust, dust, nugget, ingot, plate, stick };
    static GTMaterialFlag[] metalIc2 = { smalldust, nugget, plate, stick, gear, casing };
    static GTMaterialFlag[] metalMC = { smalldust, plate, stick, gear, casing };
    static GTMaterialFlag[] slurryBase = { smalldust, dust, fluid };

    public static final GTMaterial2
    Almandine,
    Aluminium,
    Andradite,
    Ashes,
    Basalt,
    Bauxite,
    Bismuth,
    BismuthBronze,
    Bismuthtine,
    Brass,
    Bronze,
    Calcite,
    Carbon,
    Cassiterite,
    Charcoal,
    Chrome,
    Cinnabar,
    Clay,
    Coal,
    Cobalt,
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
    Limonite,
    Lithium,
    Malachite,
    Magnalium,
    Magnesium,
    Magnetite,
    Manganese,
    Marble,
    Neodymium,
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
    Pyrolusite,
    Pyrope,
    RedAlloy,
    RedRock,
    Redstone,
    RefinedIron,
    Ruby,
    Saltpeter,
    Sapphire,
    SapphireGreen,
    Sheldonite,
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

    private float speed;
    private int durability;
    private int level;

    public GTMaterial2(String displayName, int r, int g, int b, boolean smeltable, float speed, int durability, int level, GTMaterialFlag... flags){
        super(displayName, r, g, b, smeltable, flags);
        this.speed = speed;
        this.durability = durability;
        this.level = level;
    }

    public GTMaterial2(String displayName, int r, int g, int b,  float speed, int durability, int level, GTMaterialFlag... flags){
        super(displayName, r, g, b, flags);
        this.speed = speed;
        this.durability = durability;
        this.level = level;
    }

    public GTMaterial2(String displayName, int r, int g, int b, boolean smeltable, GTMaterialFlag... flags){
        super(displayName, r, g, b, smeltable, flags);
        this.speed = 1.0f;
        this.durability = 0;
        this.level = 1;
    }

    public GTMaterial2(String displayName, int r, int g, int b, GTMaterialFlag... flags){
        super(displayName, r, g, b, flags);
        this.speed = 1.0f;
        this.durability = 0;
        this.level = 1;
    }

    public static GTMaterial2 replaceMaterial(GTMaterial oldMaterial, GTMaterial2 newMaterial){
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
        Almandine = new GTMaterial2("Almandine", 255, 0, 0, dustAll);
        Aluminium = replaceMaterial(GTMaterial.Aluminium, new GTMaterial2("Aluminium", 128, 200, 240, 10.0F, 128, 2, smalldust, dust, nugget, ingot, plate, stick, wire, gear, blockMetal, casing, foil));
        Andradite = new GTMaterial2("Andradite", 150, 120, 0, dustAll);
        Ashes = new GTMaterial2("Ashes", 192, 192, 192, dustAll);
        Basalt = new GTMaterial2("Basalt", 30, 20, 20, 5.0F, 0, 2, dustAll);
        Bauxite = replaceMaterial(GTMaterial.Bauxite, new GTMaterial2("Bauxite", 200, 100, 0, 3.0F, 0, 1, dustAll));
        Bismuth = new GTMaterial2("Bismuth", 100, 160, 160, 6.0F, 96, 2, metalFull);
        BismuthBronze = new GTMaterial2("BismuthBronze", 100, 125, 125, 6.0F, 128, 2, smalldust, dust, nugget, ingot, plate);
        Bismuthtine = new GTMaterial2("Bismuthtine", 75, 135, 135, 3.0F, 0, 1, dustAll);
        Brass = new GTMaterial2("Brass", 255, 180, 0, 7.0F, 96, 1, metalFull);
        Bronze = new GTMaterial2("Bronze", 230, 83, 34, 6.0F, 192, 2, metalIc2);
        Calcite = replaceMaterial(GTMaterial.Calcite, new GTMaterial2("Calcite", 250, 230, 220, 3.0F, 0, 1, dustAll));
        Carbon = replaceMaterial(GTMaterial.Carbon, new GTMaterial2("Carbon", 0, 0, 0,dustAll));
        Cassiterite = new GTMaterial2("Cassiterite", 220, 220, 220, 3.0F, 0, 1, dustAll);
        Charcoal = new GTMaterial2("Charcoal", 100, 70, 70, smalldust);
        Chrome = replaceMaterial(GTMaterial.Chrome, new GTMaterial2("Chrome", 240, 210, 230, false, 11.0F, 256, 3, metalFull));
        Cinnabar = new GTMaterial2("Cinnabar", 150, 0, 0, 3.0F, 0, 2, dustAll);
        Clay = new GTMaterial2("Clay", 200, 200, 220, smalldust);
        Coal = new GTMaterial2("Coal", 70, 70, 70, 3.0F, 0, 1, smalldust);
        Cobalt = new GTMaterial2("Cobalt", 80, 80, 250, false, 8.0F, 512, 4, metalBase);
        Constantan = new GTMaterial2("Constantan", 196, 116, 77, 8.0F, 128, 2, smalldust, dust, nugget, ingot, plate, stick, coil);
        Copper = new GTMaterial2("Copper", 180, 113, 61, smalldust, nugget, plate, stick, wire, gear, casing, coil, foil);
        DarkAshes = new GTMaterial2("DarkAshes", 50, 50, 50, dustAll);
        Diamond = new GTMaterial2("Diamond", 51, 235, 203, 8.0F, 1280, 3, dustAll);
        Electrum = replaceMaterial(GTMaterial.Electrum, new GTMaterial2("Electrum", 255, 255, 100, 12.0F, 64, 2, metalFull));
        Emerald = replaceMaterial(GTMaterial.Emerald, new GTMaterial2("Emerald", 80, 255, 80, dustAll));
        EnderEye = replaceMaterial(GTMaterial.EnderEye, new GTMaterial2("EnderEye", 160, 250, 230, dustAll));
        EnderPearl = replaceMaterial(GTMaterial.EnderPearl, new GTMaterial2("EnderPearl", 108, 220, 200, dustAll));
        Endstone = new GTMaterial2("Endstone", 250, 250, 198, dustAll);
        Flint = replaceMaterial(GTMaterial.Flint, new GTMaterial2("Flint", 0, 32, 64, 2.5F, 64, 1, dustAll));
        Galena = new GTMaterial2("Galena", 100, 60, 100, 3.0F, 0, 1, dustAll);
        GarnetRed = new GTMaterial2("RedGarnet", 200, 80, 80, 7.0F, 128, 2, gemAll3);
        GarnetYellow = new GTMaterial2("YellowGarnet", 200, 200, 80, 7.0F, 128, 2, gemAll3);
        Glowstone = new GTMaterial2("Glowstone", 255, 255, 0, smalldust);
        Gold = new GTMaterial2("Gold", 255, 255, 30, 12.0F, 64, 2, smalldust, plate, stick, wire, gear, casing);
        Granite = new GTMaterial2("Granite", 165, 89, 39, dustAll);
        Grossular = new GTMaterial2("Grossular", 200, 100, 0, dustAll);
        Gunpowder = new GTMaterial2("Gunpowder", 128, 128, 128, smalldust);
        Iridium = replaceMaterial(GTMaterial.Iridium, new GTMaterial2("Iridium", 255, 255, 255, false, 6.0F, 5120, 4, smalldust, dust, nugget, ingot, hotIngot, gear, stick, blockMetal, casing));
        Iron = new GTMaterial2("Iron", 184, 184, 184, 6.0F, 256, 2, smalldust, plate, stick, gear, casing, magnetic);
        Invar = new GTMaterial2("Invar", 220, 220, 150, 6.0F, 256, 2, metalFull);
        Lazurite = replaceMaterial(GTMaterial.Lazurite, new GTMaterial2("Lazurite", 100, 120, 255, dustAll));
        Lead = new GTMaterial2("Lead", 140, 100, 140, 8.0F, 64, 1, smalldust, dust, nugget, ingot, plate, stick, wire, gear, blockMetal, casing);
        Limonite = new GTMaterial2("Limonite", 200, 100, 0, 3.0F, 0, 1, dustAll);
        Lithium = replaceMaterial(GTMaterial.Lithium, new GTMaterial2("Lithium", 87, 150, 204, dustAll));
        Malachite = new GTMaterial2("Malachite", 5, 95, 5, 3.0F, 0, 1, dustAll);
        Magnalium = new GTMaterial2("Magnalium", 200, 190, 255, 6.0F, 256, 2, metalBase);
        Magnesium = new GTMaterial2("Magnesium", 255, 200, 200, dustAll);
        Magnetite = new GTMaterial2("Magnetite", 0, 0, 0, 3.0F, 0, 1, dustAll);
        Manganese = new GTMaterial2("Manganese", 250, 235, 250, smalldust, dust, nugget, ingot, plate, blockMetal);
        Marble = new GTMaterial2("Marble", 200, 200, 200, dustAll);
        Neodymium = new GTMaterial2("Neodymium", 100, 100, 100, 7.0F, 3347, 3, smalldust, dust, nugget, ingot, plate, stick, magnetic);
        Netherrack = new GTMaterial2("Netherrack", 200, 0, 0, smalldust);
        Nickel = new GTMaterial2("Nickel", 200, 200, 250, 6.0F, 64, 2, smalldust, dust, nugget, ingot, plate, stick, wire, gear, blockMetal, casing);
        Nichrome = new GTMaterial2("Nichrome", 205, 206, 246, 10.0F, 256, 3, smalldust, dust, nugget, ingot, plate, stick, coil);
        Obsidian = new GTMaterial2("Obsidian", 80, 50, 100, smalldust);
        OilCrude = new GTMaterial2("Crude_Oil", 0, 0, 0, fluid);
        Olivine = new GTMaterial2("Olivine", 150, 255, 150, 7.0F, 256, 2, gemAll1);
        Osmium = new GTMaterial2("Osmium", 50, 50, 255, false, 16.0F, 1280, 4, metalFullHot);
        Phosphorus = new GTMaterial2("Phosphorus", 190, 0, 0, dustAll);
        Platinum = new GTMaterial2("Platinum", 100, 180, 250, false, 12.0F, 64, 2, smalldust, dust, nugget, ingot, plate, stick, wire, gear, blockMetal, casing);
        Plutonium = new GTMaterial2("Plutonium", 240, 50, 50, 6.0F, 512, 3, smalldust, dust, nugget, ingot, plate);
        Pyrite = replaceMaterial(GTMaterial.Pyrite, new GTMaterial2("Pyrite", 150, 120, 40, 3.0F, 0, 1, dustAll));
        Pyrolusite = new GTMaterial2("Pyrolusite", 70, 70, 90, 5.0F, 0, 2, dustAll);
        Pyrope = new GTMaterial2("Pyrope", 120, 50, 100, dustAll);
        RedAlloy = new GTMaterial2("RedAlloy", 200, 0, 0, smalldust, dust, ingot, nugget, stick, wire);
        RedRock = new GTMaterial2("RedRock", 255, 80, 50, dustAll);
        Redstone = new GTMaterial2("Redstone", 200, 0, 0, smalldust);
        RefinedIron = new GTMaterial2("RefinedIron", 220, 235, 235,6.0F, 384, 2, stick, plate, gear, casing);
        Ruby = replaceMaterial(GTMaterial.Ruby, new GTMaterial2("Ruby", 255, 100, 100, 7.0F, 256, 2, gemAll1));
        Saltpeter = new GTMaterial2("Saltpeter", 230, 230, 230, 3.0F, 0, 1, dustAll);
        Sapphire = replaceMaterial(GTMaterial.Sapphire, new GTMaterial2("Sapphire", 100, 100, 200, 7.0F, 256, 2, gemAll2));
        SapphireGreen = replaceMaterial(GTMaterial.SapphireGreen, new GTMaterial2("GreenSapphire", 100, 200, 130, 7.0F, 256, 2, gemAll2));
        Sheldonite = new GTMaterial2("Sheldonite", 215, 212, 137, 3.5F, 0, 3, dustAll);
        Silicon = replaceMaterial(GTMaterial.Silicon, new GTMaterial2("Silicon", 60, 60, 80, 1.0F, 0, 1, fluid, smalldust, dust, nugget, ingot, plate, smallplate));
        Silver = new GTMaterial2("Silver", 215, 225, 230, 10.0F, 64, 2, smalldust, nugget, plate, stick, wire, gear, casing, foil);
        Slag = new GTMaterial2("Slag", 64, 48, 0, dustAll);
        Sodalite = replaceMaterial(GTMaterial.Sodalite, new GTMaterial2("Sodalite", 20, 20, 255, 3.0F, 0, 2,dustAll));
        Spessartine = new GTMaterial2("Spessartine", 255, 100, 100, dustAll);
        Sphalerite = new GTMaterial2("Sphalerite", 200, 140, 40, 2.0F, 0, 1, dustAll);
        StainlessSteel = new GTMaterial2("StainlessSteel", 200, 200, 220, false, 7.0F, 480, 2, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, casing);
        Steel = replaceMaterial(GTMaterial.Steel, new GTMaterial2("Steel", 128, 128, 128, 6.0F, 512, 2, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, casing, magnetic));
        Stone = new GTMaterial2("Stone", 196, 196, 196, dustAll);
        Sulfur = new GTMaterial2("Sulfur", 200, 200, 0, 2.0F, 0, 1, smalldust, dust);
        SulfuricAcid = new GTMaterial2("SulfuricAcid", 255, 106, 0, fluid);
        Tetrahedrite = new GTMaterial2("Tetrahedrite", 200, 32, 0, 3.0F, 0, 1, dustAll);
        Thorium = new GTMaterial2("Thorium", 0, 30, 0, false, 6.0F, 512, 2, smalldust, dust, nugget, ingot);
        Tin = new GTMaterial2("Tin", 220, 220, 220, smalldust, nugget, plate, stick, wire, gear, casing);
        Titanium = replaceMaterial(GTMaterial.Titanium, new GTMaterial2("Titanium", 170, 143, 222, 8.0F, 2560, 3, smalldust, dust, nugget, ingot, plate, stick, gear, blockMetal, casing));
        Tungsten = replaceMaterial(GTMaterial.Tungsten, new GTMaterial2("Tungsten", 50, 50, 50, 8.0F, 5120, 3, smalldust, dust, nugget, ingot, hotIngot, plate, stick, wire, gear, blockMetal, casing));
        TungstenSteel = new GTMaterial2("Tungstensteel", 100, 100, 160, 10.0F, 5120, 4, smalldust, dust, nugget, ingot, hotIngot, plate, stick, gear, blockMetal, casing);
        Uranium = replaceMaterial(GTMaterial.Uranium, new GTMaterial2("Uranium", 50, 240, 50, 6.0F, 512, 3, dustAll));
        Uvarovite = new GTMaterial2("Uvarovite", 180, 255, 180, dustAll);
        Wood = new GTMaterial2("Wood", 137, 103, 39, dustAll);
        Zinc = new GTMaterial2("Zinc", 250, 240, 240, metalFull);
    }
}
