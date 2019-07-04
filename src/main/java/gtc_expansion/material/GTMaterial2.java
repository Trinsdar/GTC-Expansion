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
    Almandine = new GTMaterial2(new GTMaterial("Almandine", 255, 0, 0, dustAll), 1759, 1.0F, 0, 1),
    Alumina = new GTMaterial2(new GTMaterial("Alumina", 200, 232, 255, dustAll), 2345, 1.0F, 0, 1),
    Aluminium = new GTMaterial2(new GTMaterial("Aluminium", 128, 200, 240, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing, foil), 933, 10.0F, 128, 2),
    Andradite = new GTMaterial2(new GTMaterial("Andradite", 150, 120, 0, dustAll), 1258, 1.0F, 0, 1),
    Antimony = new GTMaterial2(new GTMaterial("Antimony", 220, 220, 240, dustAll), 903, 1.0F, 0, 1),
    Ashes = new GTMaterial2(new GTMaterial("Ashes", 192, 192, 192, dustAll), 1000, 1.0F, 0, 1),
    Aquaregia = new GTMaterial2(new GTMaterial("AquaRegia", 255, 185, 0, fluid), 200, 1.0F, 0, 1),
    Basalt = new GTMaterial2(new GTMaterial("Basalt", 30, 20, 20, dustAll), 1672, 5.0F, 0, 2),
    Bauxite = new GTMaterial2(new GTMaterial("Bauxite", 200, 100, 0, dustAll), 2800, 3.0F, 0, 1),
    BauxiteTailings = new GTMaterial2(new GTMaterial("BauxiteTailings", 172, 0, 0, slurryBase), 1000, 1.0F, 0, 1),
    Beryllium = new GTMaterial2(new GTMaterial("Beryllium", 30, 80, 50, fluid), 1560, 1.0F, 0, 1),
    Bismuth = new GTMaterial2(new GTMaterial("Bismuth", 100, 160, 160, metalFull), 1837, 6.0F, 96, 2),
    BismuthBronze = new GTMaterial2(new GTMaterial("BismuthBronze", 100, 125, 125, smalldust, dust, nugget, ingot, plate), 1036, 6.0F, 128, 2),
    Bismuthtine = new GTMaterial2(new GTMaterial("Bismuthtine", 75, 135, 135, dustAll), 737, 3.0F, 0, 1),
    Brass = new GTMaterial2(new GTMaterial("Brass", 255, 180, 0, metalFull), 1160, 7.0F, 96, 1),
    Brine = new GTMaterial2(new GTMaterial("Brine", 0, 255, 221, fluid, dust, smalldust), 300, 1.0F, 0, 1),
    Calcite = new GTMaterial2(new GTMaterial("Calcite", 250, 230, 220, dustAll), 1612, 3.0F, 0, 1),
    Calcium = new GTMaterial2(new GTMaterial("Calcium", 155, 96, 80, fluid), 1115, 1.0F, 0, 1),
    Carbon = new GTMaterial2(new GTMaterial("Carbon", 0, 0, 0, dustAll), 3800, 1.0F, 0, 1),
    CarbonDioxide = new GTMaterial2(new GTMaterial("CarbonDioxide", 48, 48, 48, gas), 1929, 1.0F, 0, 1),
    Cassiterite = new GTMaterial2(new GTMaterial("Cassiterite", 220, 220, 220, dustAll), 757, 3.0F, 0, 1),
    Charcoal = new GTMaterial2(new GTMaterial("Charcoal", 100, 70, 70, smalldust), 3800, 1.0F, 0, 1),
    Chlorine = new GTMaterial2(new GTMaterial("Chlorine", 50, 150, 150, fluid), 171, 1.0F, 0, 1),
    Chlorinatedpolyvinyl = new GTMaterial2(new GTMaterial("Chlorinatedpolyvinyl", 70, 170, 170, fluid), 233, 1.0F, 0, 1),
    Chloroplatinicacid = new GTMaterial2(new GTMaterial("Chloroplatinicacid", 255, 0, 0, fluid), 14, 1.0F, 0, 1),
    Chrome = new GTMaterial2(new GTMaterial("Chrome", 255, 230, 230, metalFull), 2180, 11.0F, 256, 3),
    Chromite = new GTMaterial2(new GTMaterial("Chromite", 35, 20, 15, dustAll), 912, 5.0F, 0, 3),
    Cinnabar = new GTMaterial2(new GTMaterial("Cinnabar", 150, 0, 0, dustAll), 311, 3.0F, 0, 2),
    Clay = new GTMaterial2(new GTMaterial("Clay", 200, 200, 220, smalldust), 2000, 1.0F, 0, 1),
    Coal = new GTMaterial2(new GTMaterial("Coal", 70, 70, 70, smalldust), 3800, 3.0F, 0, 1),
    Cobalt = new GTMaterial2(new GTMaterial("Cobalt", 80, 80, 250, metalBase), 1768, 8.0F, 512, 4),
    Constantan = new GTMaterial2(new GTMaterial("Constantan", 196, 116, 77, smalldust, dust, nugget, ingot, plate, stick, coil), 1542, 8.0F, 128, 2),
    Copper = new GTMaterial2(new GTMaterial("Copper", 180, 113, 61, smalldust, nugget, plate, stick, wire, gear, casing, coil, foil), 1357, 1.0F, 0, 1),
    Cryolite = new GTMaterial2(new GTMaterial("Cryolite", 255, 255, 255, fluid, smalldust, dust), 1285, 3.0F, 0, 1),
    DarkAshes = new GTMaterial2(new GTMaterial("DarkAshes", 50, 50, 50, dustAll), 1000, 1.0F, 0, 1),
    Diamond = new GTMaterial2(new GTMaterial("Diamond", 51, 235, 203, dustAll), 3800, 8.0F, 1280, 3),
    DirtyResin = new GTMaterial2(new GTMaterial("DirtyResin", 170, 124, 49, dustAll), 300, 1.0F, 0, 1),
    Deuterium = new GTMaterial2(new GTMaterial("Deuterium", 255, 255, 0, gas), 14, 1.0F, 0, 1),
    Electrum = new GTMaterial2(new GTMaterial("Electrum", 255, 255, 100, metalFull), 1285, 12.0F, 64, 2),
    Emerald = new GTMaterial2(new GTMaterial("Emerald", 80, 255, 80, dustAll), 1803, 7.0F, 256, 3),
    EnderEye = new GTMaterial2(new GTMaterial("EnderEye", 160, 250, 230, dustAll), 3447, 1.0F, 0, 1),
    EnderPearl = new GTMaterial2(new GTMaterial("Enderpearl", 108, 220, 200, dustAll), 2723, 1.0F, 0, 1),
    Endstone = new GTMaterial2(new GTMaterial("Endstone", 250, 250, 198, dustAll), 1200, 1.0F, 0, 1),
    Flint = new GTMaterial2(new GTMaterial("Flint", 0, 32, 64, dustAll), 1986, 2.5F, 64, 1),
    Galena = new GTMaterial2(new GTMaterial("Galena", 100, 60, 100, dustAll), 784, 3.0F, 0, 1),
    GarnetRed = new GTMaterial2(new GTMaterial("RedGarnet", 200, 80, 80, dustAll), 1574, 7.0F, 128, 2),
    GarnetYellow = new GTMaterial2(new GTMaterial("YellowGarnet", 200, 200, 80, dustAll), 1574, 7.0F, 128, 2),
    Garnierite = new GTMaterial2(new GTMaterial("Garnierite", 50, 200, 70, dustAll), 891, 3.0F, 0, 1),
    Germanium = new GTMaterial2(new GTMaterial("Germanium", 250, 250, 250, smalldust, dust, nugget, ingot, plate, smallplate, stick, gear, block, casing), 1211, 8.0F, 64, 1),
    Glowstone = new GTMaterial2(new GTMaterial("Glowstone", 255, 255, 0, smalldust), 987, 1.0F, 0, 1),
    Gold = new GTMaterial2(new GTMaterial("Gold", 255, 255, 30, smalldust, plate, stick, wire, gear, casing), 1337, 12.0F, 64, 2),
    Granite = new GTMaterial2(new GTMaterial("Granite", 165, 89, 39, dustAll), 1811, 1.0F, 0, 1),
    Graphite = new GTMaterial2(new GTMaterial("Graphite", 96, 96, 96, smalldust, dust, nugget, ingot, stick, plate, stick, coil), 2000, 5.0F, 32, 2),
    Grossular = new GTMaterial2(new GTMaterial("Grossular", 200, 100, 0, dustAll), 1655, 1.0F, 0, 1),
    Gunpowder = new GTMaterial2(new GTMaterial("Gunpowder", 128, 128, 128, smalldust), 2026, 1.0F, 0, 1),
    Helium = new GTMaterial2(new GTMaterial("Helium", 255, 255, 0, gas), 1, 1.0F, 0, 1),
    Helium3 = new GTMaterial2(new GTMaterial("Helium3", 255, 255, 0, gas), 1, 1.0F, 0, 1),
    Hydrochloricacid = new GTMaterial2(new GTMaterial("Hydrochloricacid", 127, 255, 142, fluid), 100, 1.0F, 0, 1),
    Hydrogen = new GTMaterial2(new GTMaterial("Hydrogen", 0, 38, 255, gas), 14, 1.0F, 0, 1),
    Iridium = new GTMaterial2(new GTMaterial("Iridium", 255, 255, 255, smalldust, dust, nugget, ingot, gear, stick, casing, block), 2719, 6.0F, 5120, 4),
    Iron = new GTMaterial2(new GTMaterial("Iron", 184, 184, 184, smalldust, plate, stick, gear, casing, magnetic), 1811, 6.0F, 256, 2),
    Invar = new GTMaterial2(new GTMaterial("Invar", 220, 220, 150, metalFull), 1916, 6.0F, 256, 2),
    Lazurite = new GTMaterial2(new GTMaterial("Lazurite", 100, 120, 255, dustAll), 1352, 1.0F, 0, 1),
    Lead = new GTMaterial2(new GTMaterial("Lead", 140, 100, 140, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing), 600, 8.0F, 64, 1),
    Limonite = new GTMaterial2(new GTMaterial("Limonite", 200, 100, 0, dustAll), 1207, 3.0F, 0, 1),
    Lithium = new GTMaterial2(new GTMaterial("Lithium", 87, 150, 204, dustAll), 453, 1.0F, 0, 1),
    LithiumCarbonate = new GTMaterial2(new GTMaterial("LithiumCarbonate", 87, 150, 204, fluid), 453, 1.0F, 0, 1),
    LithiumChloride = new GTMaterial2(new GTMaterial("LithiumChloride", 85, 204, 204, dustAll), 453, 1.0F, 0, 1),
    Malachite = new GTMaterial2(new GTMaterial("Malachite", 5, 95, 5, dustAll), 754, 3.0F, 0, 1),
    Maganlium = new GTMaterial2(new GTMaterial("Magnalium", 200, 190, 255, metalBase), 929, 6.0F, 256, 2),
    MagnesiaCarbon = new GTMaterial2(new GTMaterial("MagnesiaCarbon", 0, 0, 0, dustAll), 3000, 1.0F, 0, 1),
    Magnesium = new GTMaterial2(new GTMaterial("Magnesium", 255, 200, 200, dustAll), 923, 1.0F, 0, 1),
    Magnetite = new GTMaterial2(new GTMaterial("Magnetite", 0, 0, 0, dustAll), 1811, 3.0F, 0, 1),
    Manganese = new GTMaterial2(new GTMaterial("Manganese", 250, 235, 250, smalldust, dust, nugget, ingot, plate, block), 1519, 1.0F, 0, 1),
    Marble = new GTMaterial2(new GTMaterial("Marble", 200, 200, 200, dustAll), 1525, 1.0F, 0, 1),
    Methane = new GTMaterial2(new GTMaterial("Methane", 255, 50, 130, gas), 100, 1.0F, 0, 1),
    Mercury = new GTMaterial2(new GTMaterial("Mercury", 250, 250, 250, fluid), 234, 1.0F, 0, 1),
    Molybdenite = new GTMaterial2(new GTMaterial("Molybdenite", 35, 20, 15, dustAll), 1224, 5.0F, 0, 3),
    Molybdenum = new GTMaterial2(new GTMaterial("Molybdenum", 180, 180, 220, metalBase), 2896, 1.0F, 0, 1),
    Neodymium = new GTMaterial2(new GTMaterial("Neodymium", 100, 100, 100, smalldust, dust, nugget, ingot, plate, stick, magnetic), 1297, 7.0F, 3347, 3),
    Netherrack = new GTMaterial2(new GTMaterial("Netherrack", 200, 0, 0, smalldust), 1500, 1.0F, 0, 1),
    Nichrome = new GTMaterial2(new GTMaterial("Nichrome", 205, 206, 246, smalldust, dust, nugget, ingot, plate, stick, coil), 1818, 10.0F, 256, 3),
    Nickel = new GTMaterial2(new GTMaterial("Nickel", 200, 200, 250, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing), 1728, 6.0F, 64, 2),
    Niobium = new GTMaterial2(new GTMaterial("Niobium", 200, 200, 200, smalldust, dust, nugget, ingot, plate, stick, foil), 2750, 1.0F, 0, 1),
    NiobiumTitanium = new GTMaterial2(new GTMaterial("NiobiumTitanium", 29, 29, 41, smalldust, dust, nugget, ingot, plate, stick, wire, coil), 2345, 1.0F, 0, 1),
    NitricAcid = new GTMaterial2(new GTMaterial("NitricAcid", 34, 185, 55, fluid), 255, 1.0F, 0, 1),
    Nitrocellulose = new GTMaterial2(new GTMaterial("Nitrocellulose", 160, 255, 120, fluid), 370, 3.0F, 0, 1),
    Nitrogen = new GTMaterial2(new GTMaterial("Nitrogen", 0, 190, 190, gas), 63, 1.0F, 0, 1),
    Obsidian = new GTMaterial2(new GTMaterial("Obsidian", 80, 50, 100, smalldust), 1300, 1.0F, 0, 1),
    Oil = new GTMaterial2(new GTMaterial("Oil", 0, 0, 0, fluid), 100, 1.0F, 0, 1),
    OilCrude = new GTMaterial2(new GTMaterial("Crude_Oil", 0, 0, 0, fluid), 100, 1.0F, 0, 1),
    Olivine = new GTMaterial2(new GTMaterial("Olivine", 150, 255, 150), 1525, 7.0F, 256, 2, gemAll1),
    Osmium = new GTMaterial2(new GTMaterial("Osmium", 50, 50, 255), 3306, 16.0F, 1280, 4, metalFull),
    Oxygen = new GTMaterial2(new GTMaterial("Oxygen", 100, 160, 220), 54, 1.0F, 0, 1, gas),
    Phosphorus = new GTMaterial2(new GTMaterial("Phosphorus", 190, 0, 0), 317, 1.0F, 0, 1, dustAll),
    Plastic = new GTMaterial2(new GTMaterial("Plastic", 235, 235, 235), 423, 1.0F, 0, 1, dust, smalldust, plate),
    Platinum = new GTMaterial2(new GTMaterial("Platinum", 100, 180, 250), 2041, 12.0F, 64, 2, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing),
    PlatinumGroupSludge = new GTMaterial2(new GTMaterial("PlatinumGroupSludge", 50, 50, 80), 317, 1.0F, 0, 1, dustAll),
    Plutonium = new GTMaterial2(new GTMaterial("Plutonium", 240, 50, 50), 912, 6.0F, 512, 3, smalldust, dust, nugget, ingot, plate),
    Potassium = new GTMaterial2(new GTMaterial("Potassium", 250, 250, 250), 336, 1.0F, 0, 1, fluid),
    Pyrite = new GTMaterial2(new GTMaterial("Pyrite", 150, 120, 40), 862, 3.0F, 0, 1, dustAll),
    Pyrolusite = new GTMaterial2(new GTMaterial("Pyrolusite", 70), 70, 90, 808, 5.0F, 0, 2, dustAll),
    Pyrope = new GTMaterial2(new GTMaterial("Pyrope", 120, 50, 100), 1626, 1.0F, 0, 1, dustAll),
    RedAlloy = new GTMaterial2(new GTMaterial("RedAlloy", 200, 0, 0), 1400, 1.0F, 0, 1, smalldust, dust, ingot, nugget, stick, wire),
    RedRock = new GTMaterial2(new GTMaterial("RedRock", 255, 80, 50), 1802, 1.0F, 0, 1, dustAll),
    Redstone = new GTMaterial2(new GTMaterial("Redstone", 200, 0, 0), 500, 1.0F, 0, 1, smalldust),
    Resin = new GTMaterial2(new GTMaterial("Resin", 233, 194, 70), 300, 1.0F, 0, 1, dustAll),
    Ruby = new GTMaterial2(new GTMaterial("Ruby", 255, 100, 100), 2317, 7.0F, 256, 2, gemAll1),
    Salt = new GTMaterial2(new GTMaterial("Salt", 160, 190, 200), 1074, 4.0F, 0, 1, dustAll),
    Saltpeter = new GTMaterial2(new GTMaterial("Saltpeter", 230, 230, 230), 119, 3.0F, 0, 1, dustAll),
    Sapphire = new GTMaterial2(new GTMaterial("Sapphire", 100, 100, 200), 2345, 7.0F, 256, 2, gemAll2),
    SapphireGreen = new GTMaterial2(new GTMaterial("GreenSapphire", 100, 200, 130), 2108, 7.0F, 256, 2, gemAll2),
    Sheldonite = new GTMaterial2(new GTMaterial("Sheldonite", 215, 212, 137), 1677, 3.5F, 0, 3, dustAll),
    Silicon = new GTMaterial2(new GTMaterial("Silicon", 60, 60, 80), 1687, 1.0F, 0, 1, fluid, smalldust, dust, nugget, ingot, plate, smallplate),
    Silver = new GTMaterial2(new GTMaterial("Silver", 215, 225, 230), 1234, 10.0F, 64, 2, smalldust, nugget, plate, stick, wire, gear, casing, foil),
    Slag = new GTMaterial2(new GTMaterial("Slag", 64, 48, 0), 1000, 1.0F, 0, 1, dustAll),
    Sodalite = new GTMaterial2(new GTMaterial("Sodalite", 20, 20, 255), 1331, 3.0F, 0, 2, dustAll),
    Sodium = new GTMaterial2(new GTMaterial("Sodium", 0, 38, 255), 370, 1.0F, 0, 1, fluid),
    SodiumCarbonate = new GTMaterial2(new GTMaterial("SodiumCarbonate", 255, 255, 255), 591, 1.0F, 0, 1, fluid),
    SodiumHydroxide = new GTMaterial2(new GTMaterial("SodiumHydroxide", 255, 255, 255), 591, 1.0F, 0, 1, fluid),
    Spessartine = new GTMaterial2(new GTMaterial("Spessartine", 255, 100, 100), 1715, 1.0F, 0, 1, dustAll),
    Sphalerite = new GTMaterial2(new GTMaterial("Sphalerite", 200, 140, 40), 540, 2.0F, 0, 1, dustAll),
    StainlessSteel = new GTMaterial2(new GTMaterial("StainlessSteel", 200, 200, 220), 2032, 7.0F, 480, 2, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing),
    Steel = new GTMaterial2(new GTMaterial("Steel", 128, 128, 128), 2046, 6.0F, 512, 2, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing, magnetic),
    Stone = new GTMaterial2(new GTMaterial("Stone", 196, 196, 196), 1100, 1.0F, 0, 1, dustAll),
    Sulfur = new GTMaterial2(new GTMaterial("Sulfur", 200, 200, 0), 100, 2.0F, 0, 1, smalldust, dust),
    SulfurDioxide = new GTMaterial2(new GTMaterial("SulfurDioxide", 200, 200, 0), 100, 1.0F, 0, 1, gas),
    SulfuricAcid = new GTMaterial2(new GTMaterial("SulfuricAcid", 255, 106, 0), 200, 1.0F, 0, 1, fluid),
    Tantalite = new GTMaterial2(new GTMaterial("Tantalite", 145, 80, 40), 935, 5.0F, 0, 2, dustAll),
    Tantalum = new GTMaterial2(new GTMaterial("Tantalum", 96, 96, 96), 3290, 8.0F, 5120, 3, dust, smalldust, nugget, ingot, stick, plate, block, foil),
    Tetrahedrite = new GTMaterial2(new GTMaterial("Tetrahedrite", 200, 32, 0), 993, 3.0F, 0, 1, dustAll),
    Thorium = new GTMaterial2(new GTMaterial("Thorium", 0, 30, 0), 2115, 6.0F, 512, 2, smalldust, dust, nugget, ingot),
    Tin = new GTMaterial2(new GTMaterial("Tin", 220, 220, 220), 505, 1.0F, 0, 1, smalldust, nugget, plate, stick, wire, gear, casing),
    Titanium = new GTMaterial2(new GTMaterial("Titanium", 170, 143, 222), 1941, 8.0F, 2560, 3, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing),
    Tritium = new GTMaterial2(new GTMaterial("Tritium", 255, 0, 0), 14, 1.0F, 0, 1, gas),
    Tungstate = new GTMaterial2(new GTMaterial("Tungstate", 60, 60, 60), 688, 4.0F, 0, 3, dustAll),
    Tungsten = new GTMaterial2(new GTMaterial("Tungsten", 50, 50, 50), 3695, 8.0F, 5120, 3, smalldust, dust, nugget, ingot, plate, stick, wire, gear, block, casing),
    TungstenSteel = new GTMaterial2(new GTMaterial("Tungstensteel", 100, 100, 160), 3000, 10.0F, 5120, 4, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing),
    TungsticAcid = new GTMaterial2(new GTMaterial("TungsticAcid", 188, 200, 0), 1746, 1.0F, 0, 1, dustAll),
    Ultimet = new GTMaterial2(new GTMaterial("Ultimet", 180, 180, 230), 1980, 9.0F, 2048, 4, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing),
    Uranium = new GTMaterial2(new GTMaterial("Uranium", 50, 240, 50), 1405, 6.0F, 512, 3, dustAll),
    Uvarovite = new GTMaterial2(new GTMaterial("Uvarovite", 180, 255, 180), 1295, 1.0F, 0, 1, dustAll),
    Vibranium = new GTMaterial2(new GTMaterial("Vibranium", 178, 0, 255), 4852, 1000.0F, 512, 4, gemAll1),
    VitriolBlue = new GTMaterial2(new GTMaterial("BlueVitriol", 0, 20, 200), 255, 1.0F, 0, 1, fluid),
    VitriolCyan = new GTMaterial2(new GTMaterial("CyanVitriol", 0, 150, 150), 255, 1.0F, 0, 1, fluid),
    VitriolGreen = new GTMaterial2(new GTMaterial("GreenVitriol", 34, 185, 55), 255, 1.0F, 0, 1, fluid),
    VitriolRed = new GTMaterial2(new GTMaterial("RedVitriol", 196, 0, 0), 255, 1.0F, 0, 1, fluid),
    Wood = new GTMaterial2(new GTMaterial("Wood", 137, 103, 39), 400, 1.0F, 0, 1, dustAll),
    Zinc = new GTMaterial2(new GTMaterial("Zinc", 250, 240, 240), 692, 1.0F, 0, 1, metalFull),
    Zircaloy = new GTMaterial2(new GTMaterial("Zircaloy", 190, 190, 175), 2032, 9.0F, 512, 2, smalldust, dust, nugget, ingot, plate, stick, gear, block, casing),
    Zirconium = new GTMaterial2(new GTMaterial("Zirconium", 180, 210, 210), 2128, 1.0F, 0, 1, dustAll);

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
