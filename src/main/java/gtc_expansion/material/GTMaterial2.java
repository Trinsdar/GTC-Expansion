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
    static GTMaterialFlag[] metalFull = { smalldust, dust, nugget, ingot, plate, stick, gear, block, casing };
    static GTMaterialFlag[] metalBase = { smalldust, dust, nugget, ingot, plate, stick };
    static GTMaterialFlag[] metalIc2 = { smalldust, nugget, plate, stick, gear, casing };
    static GTMaterialFlag[] metalMC = { smalldust, plate, stick, gear, casing };
    static GTMaterialFlag[] slurryBase = { smalldust, dust, fluid };

    public static GTMaterial2
    Bronze = new GTMaterial2(new GTMaterial("Bronze", 230, 83, 34, metalIc2),6.0F, 192, 2),
    RefinedIron = new GTMaterial2(new GTMaterial("RefinedIron", 220, 235, 235, stick, plate, gear, casing),6.0F, 384, 2),
    AnnealedCopper = new GTMaterial2(new GTMaterial("AnnealedCopper", 255, 120, 20, ingot, nugget, stick, wire), 1.0F, 0, 1),
    Almandine = new GTMaterial2(new GTMaterial("Almandine", 255, 0, 0, dustAll), 1.0F, 0, 1),
    Alumina = new GTMaterial2(new GTMaterial("Alumina", 200, 232, 255, dustAll), 1.0F, 0, 1),
    Aluminium = new GTMaterial2(new GTMaterial("Aluminium", 128, 200, 240, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing, foil), 10.0F, 128, 2),
    Andradite = new GTMaterial2(new GTMaterial("Andradite", 150, 120, 0, dustAll),  1.0F, 0, 1),
    Antimony = new GTMaterial2(new GTMaterial("Antimony", 220, 220, 240, dustAll), 1.0F, 0, 1),
    Ashes = new GTMaterial2(new GTMaterial("Ashes", 192, 192, 192, dustAll), 1.0F, 0, 1),
    Aquaregia = new GTMaterial2(new GTMaterial("AquaRegia", 255, 185, 0, fluid),  1.0F, 0, 1),
    Basalt = new GTMaterial2(new GTMaterial("Basalt", 30, 20, 20, dustAll),  5.0F, 0, 2),
    Bauxite = new GTMaterial2(new GTMaterial("Bauxite", 200, 100, 0, dustAll),  3.0F, 0, 1),
    BauxiteTailings = new GTMaterial2(new GTMaterial("BauxiteTailings", 172, 0, 0, slurryBase),  1.0F, 0, 1),
    Beryllium = new GTMaterial2(new GTMaterial("Beryllium", 30, 80, 50, fluid), 1.0F, 0, 1),
    Bismuth = new GTMaterial2(new GTMaterial("Bismuth", 100, 160, 160, metalFull), 6.0F, 96, 2),
    BismuthBronze = new GTMaterial2(new GTMaterial("BismuthBronze", 100, 125, 125, smalldust, dust, nugget, ingot, plate), 6.0F, 128, 2),
    Bismuthtine = new GTMaterial2(new GTMaterial("Bismuthtine", 75, 135, 135, dustAll), 3.0F, 0, 1),
    Brass = new GTMaterial2(new GTMaterial("Brass", 255, 180, 0, metalFull), 7.0F, 96, 1),
    Brine = new GTMaterial2(new GTMaterial("Brine", 0, 255, 221, fluid, dust, smalldust), 1.0F, 0, 1),
    Calcite = new GTMaterial2(new GTMaterial("Calcite", 250, 230, 220, dustAll), 3.0F, 0, 1),
    Calcium = new GTMaterial2(new GTMaterial("Calcium", 155, 96, 80, fluid), 1.0F, 0, 1),
    Carbon = new GTMaterial2(new GTMaterial("Carbon", 0, 0, 0, dustAll), 1.0F, 0, 1),
    CarbonDioxide = new GTMaterial2(new GTMaterial("CarbonDioxide", 48, 48, 48, gas), 1.0F, 0, 1),
    Cassiterite = new GTMaterial2(new GTMaterial("Cassiterite", 220, 220, 220, dustAll), 3.0F, 0, 1),
    Charcoal = new GTMaterial2(new GTMaterial("Charcoal", 100, 70, 70, smalldust), 1.0F, 0, 1),
    Chlorine = new GTMaterial2(new GTMaterial("Chlorine", 50, 150, 150, fluid), 1.0F, 0, 1),
    Chlorinatedpolyvinyl = new GTMaterial2(new GTMaterial("Chlorinatedpolyvinyl", 70, 170, 170, fluid), 1.0F, 0, 1),
    Chloroplatinicacid = new GTMaterial2(new GTMaterial("Chloroplatinicacid", 255, 0, 0, fluid), 1.0F, 0, 1),
    Chrome = new GTMaterial2(new GTMaterial("Chrome", 255, 230, 230, metalFull), 11.0F, 256, 3),
    Chromite = new GTMaterial2(new GTMaterial("Chromite", 35, 20, 15, dustAll), 5.0F, 0, 3),
    Cinnabar = new GTMaterial2(new GTMaterial("Cinnabar", 150, 0, 0, dustAll), 3.0F, 0, 2),
    Clay = new GTMaterial2(new GTMaterial("Clay", 200, 200, 220, smalldust), 1.0F, 0, 1),
    Coal = new GTMaterial2(new GTMaterial("Coal", 70, 70, 70, smalldust), 3.0F, 0, 1),
    Cobalt = new GTMaterial2(new GTMaterial("Cobalt", 80, 80, 250, metalBase), 8.0F, 512, 4),
    Constantan = new GTMaterial2(new GTMaterial("Constantan", 196, 116, 77, smalldust, dust, nugget, ingot, plate, stick, coil), 8.0F, 128, 2),
    Copper = new GTMaterial2(new GTMaterial("Copper", 180, 113, 61, smalldust, nugget, plate, stick, wire, gear, casing, coil, foil), 1.0F, 0, 1),
    Cryolite = new GTMaterial2(new GTMaterial("Cryolite", 255, 255, 255, fluid, smalldust, dust), 3.0F, 0, 1),
    DarkAshes = new GTMaterial2(new GTMaterial("DarkAshes", 50, 50, 50, dustAll), 1.0F, 0, 1),
    Diamond = new GTMaterial2(new GTMaterial("Diamond", 51, 235, 203, dustAll), 8.0F, 1280, 3),
    DirtyResin = new GTMaterial2(new GTMaterial("DirtyResin", 170, 124, 49, dustAll), 1.0F, 0, 1),
    Deuterium = new GTMaterial2(new GTMaterial("Deuterium", 255, 255, 0, gas), 1.0F, 0, 1),
    Electrum = new GTMaterial2(new GTMaterial("Electrum", 255, 255, 100, metalFull), 12.0F, 64, 2),
    Emerald = new GTMaterial2(new GTMaterial("Emerald", 80, 255, 80, dustAll), 7.0F, 256, 3),
    EnderEye = new GTMaterial2(new GTMaterial("EnderEye", 160, 250, 230, dustAll), 1.0F, 0, 1),
    EnderPearl = new GTMaterial2(new GTMaterial("Enderpearl", 108, 220, 200, dustAll), 1.0F, 0, 1),
    Endstone = new GTMaterial2(new GTMaterial("Endstone", 250, 250, 198, dustAll), 1.0F, 0, 1),
    Flint = new GTMaterial2(new GTMaterial("Flint", 0, 32, 64, dustAll), 2.5F, 64, 1),
    Galena = new GTMaterial2(new GTMaterial("Galena", 100, 60, 100, dustAll), 3.0F, 0, 1),
    GarnetRed = new GTMaterial2(new GTMaterial("RedGarnet", 200, 80, 80, dustAll), 7.0F, 128, 2),
    GarnetYellow = new GTMaterial2(new GTMaterial("YellowGarnet", 200, 200, 80, dustAll), 7.0F, 128, 2),
    Garnierite = new GTMaterial2(new GTMaterial("Garnierite", 50, 200, 70, dustAll), 3.0F, 0, 1),
    Germanium = new GTMaterial2(new GTMaterial("Germanium", 250, 250, 250, smalldust, dust, nugget, ingot, plate, smallplate, stick, gear, block, casing), 8.0F, 64, 1),
    Glowstone = new GTMaterial2(new GTMaterial("Glowstone", 255, 255, 0, smalldust), 1.0F, 0, 1),
    Gold = new GTMaterial2(new GTMaterial("Gold", 255, 255, 30, smalldust, plate, stick, wire, gear, casing), 12.0F, 64, 2),
    Granite = new GTMaterial2(new GTMaterial("Granite", 165, 89, 39, dustAll), 1.0F, 0, 1),
    Graphite = new GTMaterial2(new GTMaterial("Graphite", 96, 96, 96, smalldust, dust, nugget, ingot, stick, plate, stick, coil), 5.0F, 32, 2),
    Grossular = new GTMaterial2(new GTMaterial("Grossular", 200, 100, 0, dustAll), 1.0F, 0, 1),
    Gunpowder = new GTMaterial2(new GTMaterial("Gunpowder", 128, 128, 128, smalldust), 1.0F, 0, 1),
    Helium = new GTMaterial2(new GTMaterial("Helium", 255, 255, 0, gas),  1.0F, 0, 1),
    Helium3 = new GTMaterial2(new GTMaterial("Helium3", 255, 255, 0, gas),  1.0F, 0, 1),
    Hydrochloricacid = new GTMaterial2(new GTMaterial("Hydrochloricacid", 127, 255, 142, fluid), 1.0F, 0, 1),
    Hydrogen = new GTMaterial2(new GTMaterial("Hydrogen", 0, 38, 255, gas), 1.0F, 0, 1),
    Iridium = new GTMaterial2(new GTMaterial("Iridium", 255, 255, 255, smalldust, dust, nugget, ingot, gear, stick, casing, block), 6.0F, 5120, 4),
    Iron = new GTMaterial2(new GTMaterial("Iron", 184, 184, 184, smalldust, plate, stick, gear, casing, magnetic), 6.0F, 256, 2),
    Invar = new GTMaterial2(new GTMaterial("Invar", 220, 220, 150, metalFull), 6.0F, 256, 2),
    Lazurite = new GTMaterial2(new GTMaterial("Lazurite", 100, 120, 255, dustAll), 1.0F, 0, 1),
    Lead = new GTMaterial2(new GTMaterial("Lead", 140, 100, 140, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing), 8.0F, 64, 1),
    Limonite = new GTMaterial2(new GTMaterial("Limonite", 200, 100, 0, dustAll), 3.0F, 0, 1),
    Lithium = new GTMaterial2(new GTMaterial("Lithium", 87, 150, 204, dustAll), 1.0F, 0, 1),
    LithiumCarbonate = new GTMaterial2(new GTMaterial("LithiumCarbonate", 87, 150, 204, fluid), 1.0F, 0, 1),
    LithiumChloride = new GTMaterial2(new GTMaterial("LithiumChloride", 85, 204, 204, dustAll), 1.0F, 0, 1),
    Malachite = new GTMaterial2(new GTMaterial("Malachite", 5, 95, 5, dustAll), 3.0F, 0, 1),
    Maganlium = new GTMaterial2(new GTMaterial("Magnalium", 200, 190, 255, metalBase), 6.0F, 256, 2),
    MagnesiaCarbon = new GTMaterial2(new GTMaterial("MagnesiaCarbon", 0, 0, 0, dustAll), 1.0F, 0, 1),
    Magnesium = new GTMaterial2(new GTMaterial("Magnesium", 255, 200, 200, dustAll), 1.0F, 0, 1),
    Magnetite = new GTMaterial2(new GTMaterial("Magnetite", 0, 0, 0, dustAll), 3.0F, 0, 1),
    Manganese = new GTMaterial2(new GTMaterial("Manganese", 250, 235, 250, smalldust, dust, nugget, ingot, plate, block), 1.0F, 0, 1),
    Marble = new GTMaterial2(new GTMaterial("Marble", 200, 200, 200, dustAll), 1.0F, 0, 1),
    Methane = new GTMaterial2(new GTMaterial("Methane", 255, 50, 130, gas), 1.0F, 0, 1),
    Mercury = new GTMaterial2(new GTMaterial("Mercury", 250, 250, 250, fluid), 1.0F, 0, 1),
    Molybdenite = new GTMaterial2(new GTMaterial("Molybdenite", 35, 20, 15, dustAll), 5.0F, 0, 3),
    Molybdenum = new GTMaterial2(new GTMaterial("Molybdenum", 180, 180, 220, metalBase), 1.0F, 0, 1),
    Neodymium = new GTMaterial2(new GTMaterial("Neodymium", 100, 100, 100, smalldust, dust, nugget, ingot, plate, stick, magnetic), 7.0F, 3347, 3),
    Netherrack = new GTMaterial2(new GTMaterial("Netherrack", 200, 0, 0, smalldust), 1.0F, 0, 1),
    Nichrome = new GTMaterial2(new GTMaterial("Nichrome", 205, 206, 246, smalldust, dust, nugget, ingot, plate, stick, coil), 10.0F, 256, 3),
    Nickel = new GTMaterial2(new GTMaterial("Nickel", 200, 200, 250, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing), 6.0F, 64, 2),
    Niobium = new GTMaterial2(new GTMaterial("Niobium", 200, 200, 200, smalldust, dust, nugget, ingot, plate, stick, foil), 1.0F, 0, 1),
    NiobiumTitanium = new GTMaterial2(new GTMaterial("NiobiumTitanium", 29, 29, 41, smalldust, dust, nugget, ingot, plate, stick, wire, coil), 1.0F, 0, 1),
    NitricAcid = new GTMaterial2(new GTMaterial("NitricAcid", 34, 185, 55, fluid), 1.0F, 0, 1),
    Nitrocellulose = new GTMaterial2(new GTMaterial("Nitrocellulose", 160, 255, 120, fluid), 3.0F, 0, 1),
    Nitrogen = new GTMaterial2(new GTMaterial("Nitrogen", 0, 190, 190, gas), 1.0F, 0, 1),
    Obsidian = new GTMaterial2(new GTMaterial("Obsidian", 80, 50, 100, smalldust), 1.0F, 0, 1),
    Oil = new GTMaterial2(new GTMaterial("Oil", 0, 0, 0, fluid), 1.0F, 0, 1),
    OilCrude = new GTMaterial2(new GTMaterial("Crude_Oil", 0, 0, 0, fluid), 1.0F, 0, 1),
    Olivine = new GTMaterial2(new GTMaterial("Olivine", 150, 255, 150, gemAll1), 7.0F, 256, 2),
    Osmium = new GTMaterial2(new GTMaterial("Osmium", 50, 50, 255, metalFull), 16.0F, 1280, 4),
    Oxygen = new GTMaterial2(new GTMaterial("Oxygen", 100, 160, 220, gas), 1.0F, 0, 1),
    Phosphorus = new GTMaterial2(new GTMaterial("Phosphorus", 190, 0, 0, dustAll), 1.0F, 0, 1),
    Plastic = new GTMaterial2(new GTMaterial("Plastic", 235, 235, 235, dust, smalldust, plate), 1.0F, 0, 1),
    Platinum = new GTMaterial2(new GTMaterial("Platinum", 100, 180, 250, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing), 12.0F, 64, 2),
    PlatinumGroupSludge = new GTMaterial2(new GTMaterial("PlatinumGroupSludge", 50, 50, 80, dustAll), 1.0F, 0, 1),
    Plutonium = new GTMaterial2(new GTMaterial("Plutonium", 240, 50, 50, smalldust, dust, nugget, ingot, plate), 6.0F, 512, 3),
    Potassium = new GTMaterial2(new GTMaterial("Potassium", 250, 250, 250, fluid), 1.0F, 0, 1),
    Pyrite = new GTMaterial2(new GTMaterial("Pyrite", 150, 120, 40, dustAll), 3.0F, 0, 1),
    Pyrolusite = new GTMaterial2(new GTMaterial("Pyrolusite", 70, 70, 90, dustAll), 5.0F, 0, 2),
    Pyrope = new GTMaterial2(new GTMaterial("Pyrope", 120, 50, 100, dustAll), 1.0F, 0, 1),
    RedAlloy = new GTMaterial2(new GTMaterial("RedAlloy", 200, 0, 0, smalldust, dust, ingot, nugget, stick, wire), 1.0F, 0, 1),
    RedRock = new GTMaterial2(new GTMaterial("RedRock", 255, 80, 50, dustAll), 1.0F, 0, 1),
    Redstone = new GTMaterial2(new GTMaterial("Redstone", 200, 0, 0, smalldust), 1.0F, 0, 1),
    Resin = new GTMaterial2(new GTMaterial("Resin", 233, 194, 70, dustAll), 1.0F, 0, 1),
    Ruby = new GTMaterial2(new GTMaterial("Ruby", 255, 100, 100, gemAll1), 7.0F, 256, 2),
    Salt = new GTMaterial2(new GTMaterial("Salt", 160, 190, 200, dustAll), 4.0F, 0, 1),
    Saltpeter = new GTMaterial2(new GTMaterial("Saltpeter", 230, 230, 230, dustAll), 3.0F, 0, 1),
    Sapphire = new GTMaterial2(new GTMaterial("Sapphire", 100, 100, 200, gemAll2), 7.0F, 256, 2),
    SapphireGreen = new GTMaterial2(new GTMaterial("GreenSapphire", 100, 200, 130, gemAll2), 7.0F, 256, 2),
    Sheldonite = new GTMaterial2(new GTMaterial("Sheldonite", 215, 212, 137, dustAll), 3.5F, 0, 3),
    Silicon = new GTMaterial2(new GTMaterial("Silicon", 60, 60, 80, fluid, smalldust, dust, nugget, ingot, plate, smallplate), 1.0F, 0, 1),
    Silver = new GTMaterial2(new GTMaterial("Silver", 215, 225, 230, smalldust, nugget, plate, stick, wire, gear, casing, foil), 10.0F, 64, 2),
    Slag = new GTMaterial2(new GTMaterial("Slag", 64, 48, 0, dustAll), 1.0F, 0, 1),
    Sodalite = new GTMaterial2(new GTMaterial("Sodalite", 20, 20, 255, dustAll), 3.0F, 0, 2),
    Sodium = new GTMaterial2(new GTMaterial("Sodium", 0, 38, 255, fluid), 1.0F, 0, 1),
    SodiumCarbonate = new GTMaterial2(new GTMaterial("SodiumCarbonate", 255, 255, 255, fluid), 1.0F, 0, 1),
    SodiumHydroxide = new GTMaterial2(new GTMaterial("SodiumHydroxide", 255, 255, 255, fluid), 1.0F, 0, 1),
    Spessartine = new GTMaterial2(new GTMaterial("Spessartine", 255, 100, 100, dustAll), 1.0F, 0, 1),
    Sphalerite = new GTMaterial2(new GTMaterial("Sphalerite", 200, 140, 40, dustAll), 2.0F, 0, 1),
    StainlessSteel = new GTMaterial2(new GTMaterial("StainlessSteel", 200, 200, 220, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing), 7.0F, 480, 2),
    Steel = new GTMaterial2(new GTMaterial("Steel", 128, 128, 128, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing, magnetic), 6.0F, 512, 2),
    Stone = new GTMaterial2(new GTMaterial("Stone", 196, 196, 196, dustAll), 1.0F, 0, 1),
    Sulfur = new GTMaterial2(new GTMaterial("Sulfur", 200, 200, 0, smalldust, dust), 2.0F, 0, 1),
    SulfurDioxide = new GTMaterial2(new GTMaterial("SulfurDioxide", 200, 200, 0, gas), 1.0F, 0, 1),
    SulfuricAcid = new GTMaterial2(new GTMaterial("SulfuricAcid", 255, 106, 0, fluid), 1.0F, 0, 1),
    Tantalite = new GTMaterial2(new GTMaterial("Tantalite", 145, 80, 40, dustAll), 5.0F, 0, 2),
    Tantalum = new GTMaterial2(new GTMaterial("Tantalum", 96, 96, 96, dust, smalldust, nugget, ingot, stick, plate, block, foil), 8.0F, 5120, 3),
    Tetrahedrite = new GTMaterial2(new GTMaterial("Tetrahedrite", 200, 32, 0, dustAll), 3.0F, 0, 1),
    Thorium = new GTMaterial2(new GTMaterial("Thorium", 0, 30, 0, smalldust, dust, nugget, ingot), 6.0F, 512, 2),
    Tin = new GTMaterial2(new GTMaterial("Tin", 220, 220, 220, smalldust, nugget, plate, stick, wire, gear, casing), 1.0F, 0, 1),
    Titanium = new GTMaterial2(new GTMaterial("Titanium", 170, 143, 222, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing), 8.0F, 2560, 3),
    Tritium = new GTMaterial2(new GTMaterial("Tritium", 255, 0, 0, gas), 1.0F, 0, 1),
    Tungstate = new GTMaterial2(new GTMaterial("Tungstate", 60, 60, 60, dustAll), 4.0F, 0, 3),
    Tungsten = new GTMaterial2(new GTMaterial("Tungsten", 50, 50, 50, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing), 8.0F, 5120, 3),
    TungstenSteel = new GTMaterial2(new GTMaterial("Tungstensteel", 100, 100, 160, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing), 10.0F, 5120, 4),
    TungsticAcid = new GTMaterial2(new GTMaterial("TungsticAcid", 188, 200, 0, dustAll), 1.0F, 0, 1),
    Ultimet = new GTMaterial2(new GTMaterial("Ultimet", 180, 180, 230, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing), 9.0F, 2048, 4),
    Uranium = new GTMaterial2(new GTMaterial("Uranium", 50, 240, 50, dustAll), 6.0F, 512, 3),
    Uvarovite = new GTMaterial2(new GTMaterial("Uvarovite", 180, 255, 180, dustAll), 1.0F, 0, 1),
    Vibranium = new GTMaterial2(new GTMaterial("Vibranium", 178, 0, 255, gemAll1), 1000.0F, 512, 4),
    VitriolBlue = new GTMaterial2(new GTMaterial("BlueVitriol", 0, 20, 200, fluid), 1.0F, 0, 1),
    VitriolCyan = new GTMaterial2(new GTMaterial("CyanVitriol", 0, 150, 150, fluid), 1.0F, 0, 1),
    VitriolGreen = new GTMaterial2(new GTMaterial("GreenVitriol", 34, 185, 55, fluid), 1.0F, 0, 1),
    VitriolRed = new GTMaterial2(new GTMaterial("RedVitriol", 196, 0, 0, fluid), 1.0F, 0, 1),
    Wood = new GTMaterial2(new GTMaterial("Wood", 137, 103, 39, dustAll), 1.0F, 0, 1),
    Zinc = new GTMaterial2(new GTMaterial("Zinc", 250, 240, 240, metalFull), 1.0F, 0, 1),
    Zircaloy = new GTMaterial2(new GTMaterial("Zircaloy", 190, 190, 175, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing), 9.0F, 512, 2),
    Zirconium = new GTMaterial2(new GTMaterial("Zirconium", 180, 210, 210, dustAll), 1.0F, 0, 1);

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
