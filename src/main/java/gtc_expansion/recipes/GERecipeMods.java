package gtc_expansion.recipes;

import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.tile.multi.GETileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GETileMultiPrimitiveBlastFurnace;
import gtc_expansion.util.GEIc2cECompat;
import gtclassic.api.helpers.GTHelperMods;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTConfig;
import gtclassic.common.recipe.GTRecipeProcessing;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.machine.low.TileEntityMacerator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static gtclassic.common.recipe.GTRecipeMods.input;
import static gtclassic.common.recipe.GTRecipeMods.metal;

public class GERecipeMods {
    public static void init(){
        if (GTConfig.compatTwilightForest && Loader.isModLoaded(GTHelperMods.TFOREST)) {
            GETileMultiPrimitiveBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input(GTMaterialGen.getModItem(GTHelperMods.TFOREST, "liveroot")),
                    input("nuggetGold", 1) }, 400, GTMaterialGen.getModItem(GTHelperMods.TFOREST, "ironwood_ingot", 2));
        }

        if (GTConfig.compatEnderIO && Loader.isModLoaded(GTHelperMods.ENDERIO)) {
            // Dark steel upgrade
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] {
                    input(GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_dark_iron_bars", 1)), input("itemClay", 1),
                    input("string", 4) }, 500, 120000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "item_dark_steel_upgrade"));
            // Electric Steel
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1), input("dustCoal", 1),
                    input("itemSilicon", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 0, 1));
            // Energenic Alloy
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustRedstone", 1), metal("Gold", 1),
                    input("dustGlowstone", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 1, 1));
            // Vibrant Alloy
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("ingotEnergeticAlloy", 1),
                    input("enderpearl", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 2, 1));
            // Redstone Alloy
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustRedstone", 1),
                    input("itemSilicon", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 3, 1));
            // Conductive Iron
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustRedstone", 1),
                    metal("Iron", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 4, 1));
            // Pulsating Alloy
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input("enderpearl", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 5, 1));
            // Dark Steel
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Steel", 1),
                    input("dustObsidian", 1) }, 500, 80000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 6, 1));
            // End Steel
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("endstone", 1), input("ingotDarkSteel", 1),
                    input("obsidian", 1) }, 500, 80000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 8, 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input(GTMaterialGen.get(Blocks.SOUL_SAND, 1)),
                    metal("Gold", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 7, 1));
            // Fused Quartz - Custom Recipe!
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustNetherQuartz", 2),
                    input("blockGlass", 1) }, 500, 10000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_fused_quartz", 1));
            // Enlightened Fused Quartz
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("gemQuartz", 4),
                    input("dustGlowstone", 4) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_enlightened_fused_quartz", 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("blockQuartz", 4),
                    input("glowstone", 4) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_enlightened_fused_quartz", 1));
            // Enlightned Clear Glass
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("blockGlass", 1),
                    input("dustGlowstone", 4) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_enlightened_fused_glass", 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("blockGlass", 1),
                    input("glowstone", 1) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_enlightened_fused_glass", 1));
            // Dark Fused Quartz
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dyeBlack", 1),
                    input("gemQuartz", 4) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_dark_fused_quartz", 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dyeBlack", 1),
                    input("blockQuartz", 1) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_dark_fused_quartz", 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dyeBlack", 1),
                    input("blockGlassHardened", 1) }, 500, 10000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_dark_fused_quartz", 1));
            // Dyes
            //doEnderIOBlastFurnaceDyeThings("Green", 48);
            //doEnderIOBlastFurnaceDyeThings("Brown", 49);
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCoal", 6),
                    input("slimeball", 1) }, 500, 8000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 50, 2));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCoal", 3),
                    input("egg", 1) }, 500, 6000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 50, 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCharcoal", 6),
                    input("slimeball", 1) }, 500, 8000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 50, 2));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCharcoal", 3),
                    input("egg", 1) }, 500, 6000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 50, 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input(GTMaterialGen.get(Items.BEETROOT, 1)),
                    input("itemClay", 3), input("egg", 6) }, 500, 60000, new ItemStack(Items.DYE, 12, 1));
            // Machine Chassis
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("itemSimpleMachineChassi", 1),
                    input("dyeMachine", 1) }, 500, 14400, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 1, 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("itemEndSteelMachineChassi", 1),
                    input("dyeEnhancedMachine", 1) }, 500, 14400, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 54, 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("itemSimpleMachineChassi", 1),
                    input("dyeSoulMachine", 1) }, 500, 14400, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 53, 1));
            // Other Stuff
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustLapis", 1), input("blockWool", 1),
                    input("dustTin", 1) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_industrial_insulation", 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("ingotBrickNether", 1),
                    input("cropNetherWart", 4),
                    input("itemClay", 6) }, 500, 80000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 72, 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustBedrock", 1),
                    input("dustCoal", 1) }, 500, 20000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 75, 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustBedrock", 1),
                    input("dustCharcoal", 1) }, 500, 20000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 75, 1));
            GETileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustGlowstone", 1),
                    input("itemClay", 1) }, 500, 20000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 76, 2));
        }

        if (GTConfig.compatIc2Extras && Loader.isModLoaded(GTHelperMods.IC2_EXTRAS)){
            addCrushedOreRecipes(GEMaterial.Tetrahedrite, GEMaterialGen.getTinyDust(GEMaterial.Antimony, 1), GEMaterialGen.getTinyDust(GEMaterial.Zinc, 1));
            addCrushedOreRecipes(GEMaterial.Galena, GEMaterialGen.getTinyDust(GEMaterial.Sulfur, 2), GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "silvertinydust"));
            addCrushedOreRecipes(GEMaterial.Cinnabar, GEMaterialGen.getTinyDust(GEMaterial.Redstone, 1), GEMaterialGen.getTinyDust(GEMaterial.Sulfur, 1));
            addCrushedOreRecipes(GEMaterial.Sphalerite, GEMaterialGen.getTinyDust(GEMaterial.Zinc, 1), GEMaterialGen.getTinyDust(GEMaterial.GarnetYellow, 1));
            addCrushedOreRecipes(GEMaterial.Pyrite, GEMaterialGen.getTinyDust(GEMaterial.Sulfur, 1), GEMaterialGen.getTinyDust(GEMaterial.Phosphorus, 1));
            addCrushedOreRecipes(GEMaterial.Bauxite, GEMaterialGen.getTinyDust(GEMaterial.Grossular, 1), GEMaterialGen.getTinyDust(GEMaterial.Titanium, 1));
            addCrushedOreRecipes(GEMaterial.Platinum, GEMaterialGen.getTinyDust(GEMaterial.Sulfur, 2), GEMaterialGen.getTinyDust(GEMaterial.Nickel, 1));
            addCrushedOreRecipes(GEMaterial.Tungsten, GEMaterialGen.getTinyDust(GEMaterial.Manganese, 1), GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "silvertinydust"));
            addCrushedOreRecipes(GEMaterial.Iridium, GEMaterialGen.getTinyDust(GEMaterial.Platinum, 1), GEMaterialGen.getTinyDust(GEMaterial.Osmium, 1));
            GTRecipeProcessing.maceratorUtil("oreGalena", 1, GEMaterialGen.getCrushedOre(GEMaterial.Galena, 2));
            GTRecipeProcessing.maceratorUtil("oreTetrahedrite", 1, GEMaterialGen.getCrushedOre(GEMaterial.Tetrahedrite, 2));
            GTRecipeProcessing.maceratorUtil("oreCassiterite", 1, GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "tincrushedore", 4));
            GTRecipeProcessing.maceratorUtil("orePyrite", 1, GEMaterialGen.getCrushedOre(GEMaterial.Pyrite, 5));
            GTRecipeProcessing.maceratorUtil("oreCinnabar", 1, GEMaterialGen.getCrushedOre(GEMaterial.Cinnabar, 3));
            GTRecipeProcessing.maceratorUtil("oreSphalerite", 1, GEMaterialGen.getCrushedOre(GEMaterial.Sphalerite, 4));
            GTRecipeProcessing.maceratorUtil("orePlatinum", 1, GEMaterialGen.getCrushedOre(GEMaterial.Platinum, 2));
            GTRecipeProcessing.maceratorUtil("oreTungstate", 1, GEMaterialGen.getCrushedOre(GEMaterial.Tungsten, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreBauxite", 1));
            TileEntityMacerator.addRecipe("oreBauxite", 1, GEMaterialGen.getCrushedOre(GEMaterial.Bauxite, 4));
            ClassicRecipes.macerator.removeRecipe(input("oreIridium", 1));
            TileEntityMacerator.addRecipe("oreIridium", 1, GEMaterialGen.getCrushedOre(GEMaterial.Iridium, 2));
            GameRegistry.addSmelting(GEMaterialGen.getCrushedOre(GEMaterial.Tetrahedrite, 1), GEMaterialGen.getNugget(GEMaterial.Copper, 10), 0.5F);
            GameRegistry.addSmelting(GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Tetrahedrite, 1), GEMaterialGen.getNugget(GEMaterial.Copper, 10), 0.5F);
            GameRegistry.addSmelting(GEMaterialGen.getPurifiedCrushedOre(GEMaterial.Platinum, 1), GTMaterialGen.getIngot(GEMaterial.Platinum, 1), 1.0F);
            GameRegistry.addSmelting(GEMaterialGen.getCrushedOre(GEMaterial.Platinum, 1), GTMaterialGen.getIngot(GEMaterial.Platinum, 1), 1.0F);
        }
    }

    public static void addCrushedOreRecipes(GTMaterial main, ItemStack outputWashSide, ItemStack outputThermalSide){
        GEIc2cECompat.addOreWashingMachineRecipe("crushed" + main.getDisplayName(), 1, GEMaterialGen.getPurifiedCrushedOre(main, 1), outputWashSide, GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "stonedust"));
        GEIc2cECompat.addThermalCentrifugeRecipe("crushedPurified" + main.getDisplayName(), 1, 400, GTMaterialGen.getDust(main, 1), outputThermalSide);
        GEIc2cECompat.addThermalCentrifugeRecipe("crushed" + main.getDisplayName(), 1, 600, GTMaterialGen.getDust(main, 1), outputThermalSide, GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "stonedust"));
        TileEntityMacerator.addRecipe("crushed" + main.getDisplayName(), 1, GTMaterialGen.getDust(main, 1));
        TileEntityMacerator.addRecipe("crushedPurified" + main.getDisplayName(), 1, GTMaterialGen.getDust(main, 1));
    }

    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

}
