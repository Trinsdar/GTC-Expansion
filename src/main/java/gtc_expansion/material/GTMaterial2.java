package gtc_expansion.material;

import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;

public class GTMaterial2 {
    static GTMaterialFlag particle = new GTMaterialFlag("_particle", 15, false);
    static GTMaterialFlag fluid = GTMaterialFlag.FLUID;
    static GTMaterialFlag gas = GTMaterialFlag.GAS;
    static GTMaterialFlag smalldust = new GTMaterialFlag("_dustsmall", 1, false);
    static GTMaterialFlag dust = GTMaterialFlag.DUST;
    static GTMaterialFlag gemRubyShape = GTMaterialFlag.RUBY;
    static GTMaterialFlag gemSapphireShape = GTMaterialFlag.SAPPHIRE;
    static GTMaterialFlag gemGarnetShape = new GTMaterialFlag("_gem", 7, false);
    static GTMaterialFlag ingot = GTMaterialFlag.INGOT;
    static GTMaterialFlag nugget = new GTMaterialFlag("_nugget", 5, false);
    static GTMaterialFlag plate = new GTMaterialFlag("_plate", 6, false);
    static GTMaterialFlag smallplate = new GTMaterialFlag("_smallplate", 7, false);
    static GTMaterialFlag stick = new GTMaterialFlag("_stick", 8, false);
    static GTMaterialFlag magnetic = new GTMaterialFlag("_magneticstick", 8, true);
    static GTMaterialFlag wire = new GTMaterialFlag("_finewire", 12, false);
    static GTMaterialFlag gear = new GTMaterialFlag("_gear", 10, false);
    static GTMaterialFlag foil = new GTMaterialFlag("_foil", 11, false);
    static GTMaterialFlag block = GTMaterialFlag.BLOCK;
    static GTMaterialFlag casing = new GTMaterialFlag("_casing", 50, false);
    static GTMaterialFlag coil = new GTMaterialFlag("_coil", 52, false);
    static GTMaterialFlag[] dustAll = { smalldust, dust };
    static GTMaterialFlag[] gemAll1 = { smalldust, dust, gemRubyShape, block };
    static GTMaterialFlag[] gemAll2 = { smalldust, dust, gemSapphireShape, block };
    static GTMaterialFlag[] gemAll3 = { smalldust, dust, gemGarnetShape, block };
    static GTMaterialFlag[] metalFull = { smalldust, dust, nugget, ingot, plate, stick, gear, block, casing };
    static GTMaterialFlag[] metalSemiFull = { smalldust, nugget, plate, stick, gear, casing };
    static GTMaterialFlag[] metalBase = { smalldust, dust, nugget, ingot, plate, stick };
    static GTMaterialFlag[] metalIc2 = { smalldust, nugget, plate, stick, gear, casing };
    static GTMaterialFlag[] metalMC = { smalldust, plate, stick, gear, casing };
    static GTMaterialFlag[] slurryBase = { smalldust, dust, fluid };

    public static GTMaterial
    Almandine = new GTMaterial("Almandine", 255, 0, 0, dustAll),
    Aluminium = new GTMaterial(GTMaterial.Aluminium.getDisplayName(), GTMaterial.Aluminium.getColor(), GTMaterial.Aluminium.getSmeltable(), smalldust, nugget, plate, stick, wire, gear, casing, foil),
    Andradite = new GTMaterial("Andradite", 150, 120, 0, dustAll),
    Ashes = new GTMaterial("Ashes", 192, 192, 192, dustAll),
    Basalt = new GTMaterial("Basalt", 30, 20, 20, dustAll),
    Bauxite = new GTMaterial("Bauxite", 200, 100, 0, smalldust),
    Bismuthtine = new GTMaterial("Bismuthtine", 75, 135, 135, dustAll),
    Calcite = new GTMaterial("Calcite", 250, 230, 220, smalldust),
    Carbon = new GTMaterial("Carbon", 0, 0, 0, smalldust),
    Cassiterite = new GTMaterial("Cassiterite", 220, 220, 220, dustAll),
    Charcoal = new GTMaterial("Charcoal", 100, 70, 70, smalldust),
    Chrome = new GTMaterial(GTMaterial.Chrome.getDisplayName(), GTMaterial.Chrome.getColor(), GTMaterial.Chrome.getSmeltable(), metalFull),
    Cinnabar = new GTMaterial("Cinnabar", 150, 0, 0, dustAll),
    Clay = new GTMaterial("Clay", 200, 200, 220, smalldust),
    Coal = new GTMaterial("Coal", 70, 70, 70, smalldust),
    Copper = new GTMaterial("Copper", 180, 113, 61, smalldust, nugget, plate, stick, wire, gear, casing, coil, foil),
    DarkAshes = new GTMaterial("DarkAshes", 50, 50, 50, dustAll),
    Diamond = new GTMaterial("Diamond", 51, 235, 203, dustAll),
    Electrum = new GTMaterial(GTMaterial.Electrum.getDisplayName(), GTMaterial.Electrum.getColor(), GTMaterial.Electrum.getSmeltable(), metalSemiFull),
    Emerald = new GTMaterial("Emerald", 80, 255, 80, smalldust),
    EnderEye = new GTMaterial("EnderEye", 160, 250, 230, smalldust),
    EnderPearl = new GTMaterial("Enderpearl", 108, 220, 200, smalldust),
    Endstone = new GTMaterial("Endstone", 250, 250, 198, dustAll),
    Flint = new GTMaterial("Flint", 0, 32, 64, smalldust),
    Galena = new GTMaterial("Galena", 100, 60, 100, dustAll),
    GarnetRed = new GTMaterial("RedGarnet", 200, 80, 80, gemAll3),
    GarnetYellow = new GTMaterial("YellowGarnet", 200, 200, 80, gemAll3),
    Glowstone = new GTMaterial("Glowstone", 255, 255, 0, smalldust),
    Gold = new GTMaterial("Gold", 255, 255, 30, smalldust, plate, stick, wire, gear, casing),
    Granite = new GTMaterial("Granite", 165, 89, 39, dustAll),
    Grossular = new GTMaterial("Gunpowder", 128, 128, 128, smalldust),
    Gunpowder = new GTMaterial("Gunpowder", 128, 128, 128, smalldust),
    Iridium = new GTMaterial("Iridium", 255, 255, 255, smalldust, nugget, gear, stick, casing),
    Lazurite = new GTMaterial("Lazurite", 100, 120, 255, dustAll),
    Limonite = new GTMaterial("Limonite", 200, 100, 0, dustAll),
    Lithium = new GTMaterial("Lithium", 87, 150, 204, dustAll);


    public static GTMaterial2
    AluminiumTool = new GTMaterial2(GTMaterial.Aluminium, 10.0F, 128, 2),
    Bismuth = new GTMaterial2(new GTMaterial("Bismuth", 100, 160, 160, metalFull), 6.0F, 96, 2),
    BismuthBronze = new GTMaterial2(new GTMaterial("BismuthBronze", 100, 125, 125, smalldust, dust, nugget, ingot, plate), 6.0F, 128, 2),
    Brass = new GTMaterial2(new GTMaterial("Brass", 255, 180, 0, metalFull), 7.0F, 96, 1),
    Bronze = new GTMaterial2(new GTMaterial("Bronze", 230, 83, 34, metalIc2),6.0F, 192, 2),
    ChromeTool = new GTMaterial2(GTMaterial.Chrome, 11.0F, 256, 3),
    Cobalt = new GTMaterial2(new GTMaterial("Cobalt", 80, 80, 250, metalBase), 8.0F, 512, 4),
    Constantan = new GTMaterial2(new GTMaterial("Constantan", 196, 116, 77, smalldust, dust, nugget, ingot, plate, stick, coil), 8.0F, 128, 2),
    ElectrumTool = new GTMaterial2(new GTMaterial("Electrum", 255, 255, 100, metalFull), 12.0F, 64, 2),
    FlintTool = new GTMaterial2(new GTMaterial("Flint", 0, 32, 64, dustAll), 2.5F, 64, 1),
    IridiumTool = new GTMaterial2(GTMaterial.Iridium, 6.0F, 5120, 4),
    Iron = new GTMaterial2(new GTMaterial("Iron", 184, 184, 184, smalldust, plate, stick, gear, casing, magnetic), 6.0F, 256, 2),
    Invar = new GTMaterial2(new GTMaterial("Invar", 220, 220, 150, metalFull), 6.0F, 256, 2),
    Lead = new GTMaterial2(new GTMaterial("Lead", 140, 100, 140, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing), 8.0F, 64, 1),
    Malachite = new GTMaterial2(new GTMaterial("Malachite", 5, 95, 5, dustAll), 3.0F, 0, 1),
    Maganlium = new GTMaterial2(new GTMaterial("Magnalium", 200, 190, 255, metalBase), 6.0F, 256, 2),
    Magnesium = new GTMaterial2(new GTMaterial("Magnesium", 255, 200, 200, dustAll), 1.0F, 0, 1),
    Magnetite = new GTMaterial2(new GTMaterial("Magnetite", 0, 0, 0, dustAll), 3.0F, 0, 1),
    Manganese = new GTMaterial2(new GTMaterial("Manganese", 250, 235, 250, smalldust, dust, nugget, ingot, plate, block), 1.0F, 0, 1),
    Marble = new GTMaterial2(new GTMaterial("Marble", 200, 200, 200, dustAll), 1.0F, 0, 1),
    Neodymium = new GTMaterial2(new GTMaterial("Neodymium", 100, 100, 100, smalldust, dust, nugget, ingot, plate, stick, magnetic), 7.0F, 3347, 3),
    Netherrack = new GTMaterial2(new GTMaterial("Netherrack", 200, 0, 0, smalldust), 1.0F, 0, 1),
    Nichrome = new GTMaterial2(new GTMaterial("Nichrome", 205, 206, 246, smalldust, dust, nugget, ingot, plate, stick, coil), 10.0F, 256, 3),
    Obsidian = new GTMaterial2(new GTMaterial("Obsidian", 80, 50, 100, smalldust), 1.0F, 0, 1),
    OilCrude = new GTMaterial2(new GTMaterial("Crude_Oil", 0, 0, 0, fluid), 1.0F, 0, 1),
    Olivine = new GTMaterial2(new GTMaterial("Olivine", 150, 255, 150, gemAll1), 7.0F, 256, 2),
    Osmium = new GTMaterial2(new GTMaterial("Osmium", 50, 50, 255, metalFull), 16.0F, 1280, 4),
    Phosphorus = new GTMaterial2(new GTMaterial("Phosphorus", 190, 0, 0, dustAll), 1.0F, 0, 1),
    Platinum = new GTMaterial2(new GTMaterial("Platinum", 100, 180, 250, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing), 12.0F, 64, 2),
    Plutonium = new GTMaterial2(new GTMaterial("Plutonium", 240, 50, 50, smalldust, dust, nugget, ingot, plate), 6.0F, 512, 3),
    Pyrite = new GTMaterial2(new GTMaterial("Pyrite", 150, 120, 40, dustAll), 3.0F, 0, 1),
    Pyrolusite = new GTMaterial2(new GTMaterial("Pyrolusite", 70, 70, 90, dustAll), 5.0F, 0, 2),
    Pyrope = new GTMaterial2(new GTMaterial("Pyrope", 120, 50, 100, dustAll), 1.0F, 0, 1),
    RedAlloy = new GTMaterial2(new GTMaterial("RedAlloy", 200, 0, 0, smalldust, dust, ingot, nugget, stick, wire), 1.0F, 0, 1),
    RedRock = new GTMaterial2(new GTMaterial("RedRock", 255, 80, 50, dustAll), 1.0F, 0, 1),
    Redstone = new GTMaterial2(new GTMaterial("Redstone", 200, 0, 0, smalldust), 1.0F, 0, 1),
    RefinedIron = new GTMaterial2(new GTMaterial("RefinedIron", 220, 235, 235, stick, plate, gear, casing),6.0F, 384, 2),
    Ruby = new GTMaterial2(new GTMaterial("Ruby", 255, 100, 100, gemAll1), 7.0F, 256, 2),
    Saltpeter = new GTMaterial2(new GTMaterial("Saltpeter", 230, 230, 230, dustAll), 3.0F, 0, 1),
    Sapphire = new GTMaterial2(new GTMaterial("Sapphire", 100, 100, 200, gemAll2), 7.0F, 256, 2),
    SapphireGreen = new GTMaterial2(new GTMaterial("GreenSapphire", 100, 200, 130, gemAll2), 7.0F, 256, 2),
    Sheldonite = new GTMaterial2(new GTMaterial("Sheldonite", 215, 212, 137, dustAll), 3.5F, 0, 3),
    Silicon = new GTMaterial2(new GTMaterial("Silicon", 60, 60, 80, fluid, smalldust, dust, nugget, ingot, plate, smallplate), 1.0F, 0, 1),
    Silver = new GTMaterial2(new GTMaterial("Silver", 215, 225, 230, smalldust, nugget, plate, stick, wire, gear, casing, foil), 10.0F, 64, 2),
    Slag = new GTMaterial2(new GTMaterial("Slag", 64, 48, 0, dustAll), 1.0F, 0, 1),
    Sodalite = new GTMaterial2(new GTMaterial("Sodalite", 20, 20, 255, dustAll), 3.0F, 0, 2),
    Spessartine = new GTMaterial2(new GTMaterial("Spessartine", 255, 100, 100, dustAll), 1.0F, 0, 1),
    Sphalerite = new GTMaterial2(new GTMaterial("Sphalerite", 200, 140, 40, dustAll), 2.0F, 0, 1),
    StainlessSteel = new GTMaterial2(new GTMaterial("StainlessSteel", 200, 200, 220, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing), 7.0F, 480, 2),
    Steel = new GTMaterial2(new GTMaterial("Steel", 128, 128, 128, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing, magnetic), 6.0F, 512, 2),
    Stone = new GTMaterial2(new GTMaterial("Stone", 196, 196, 196, dustAll), 1.0F, 0, 1),
    Sulfur = new GTMaterial2(new GTMaterial("Sulfur", 200, 200, 0, smalldust, dust), 2.0F, 0, 1),
    SulfuricAcid = new GTMaterial2(new GTMaterial("SulfuricAcid", 255, 106, 0, fluid), 1.0F, 0, 1),
    Tetrahedrite = new GTMaterial2(new GTMaterial("Tetrahedrite", 200, 32, 0, dustAll), 3.0F, 0, 1),
    Thorium = new GTMaterial2(new GTMaterial("Thorium", 0, 30, 0, smalldust, dust, nugget, ingot), 6.0F, 512, 2),
    Tin = new GTMaterial2(new GTMaterial("Tin", 220, 220, 220, smalldust, nugget, plate, stick, wire, gear, casing), 1.0F, 0, 1),
    Titanium = new GTMaterial2(new GTMaterial("Titanium", 170, 143, 222, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing), 8.0F, 2560, 3),
    Tungsten = new GTMaterial2(new GTMaterial("Tungsten", 50, 50, 50, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing), 8.0F, 5120, 3),
    TungstenSteel = new GTMaterial2(new GTMaterial("Tungstensteel", 100, 100, 160, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing), 10.0F, 5120, 4),
    Uranium = new GTMaterial2(new GTMaterial("Uranium", 50, 240, 50, dustAll), 6.0F, 512, 3),
    Uvarovite = new GTMaterial2(new GTMaterial("Uvarovite", 180, 255, 180, dustAll), 1.0F, 0, 1),
    Wood = new GTMaterial2(new GTMaterial("Wood", 137, 103, 39, dustAll), 1.0F, 0, 1),
    Zinc = new GTMaterial2(new GTMaterial("Zinc", 250, 240, 240, metalFull), 1.0F, 0, 1);

    private GTMaterial material;
    private float speed;
    private int durability;
    private int level;
    public GTMaterial2(GTMaterial material, float speed, int durability, int level){
        this.material = material;
        this.speed = speed;
        this.durability = durability;
        this.level = level;
    }

    public GTMaterial replaceMaterial(GTMaterial oldMaterial, GTMaterial newMaterial){
        oldMaterial = newMaterial;
        return newMaterial;
    }

    public GTMaterial getMaterial() {
        return material;
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
}
