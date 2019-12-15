package gtc_expansion.recipes;

import gtc_expansion.GTCXItems;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.util.GTCXIc2cECompat;
import gtclassic.api.helpers.GTHelperMods;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeCraftingHandler;
import gtclassic.common.GTConfig;
import gtclassic.common.GTItems;
import gtclassic.common.recipe.GTRecipeProcessing;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static gtclassic.common.recipe.GTRecipeMods.input;
import static gtclassic.common.recipe.GTRecipeMods.metal;

public class GTCXRecipeMods {
    public static void init(){
        if (GTConfig.modcompat.compatTwilightForest && Loader.isModLoaded(GTHelperMods.TFOREST)) {
            GTCXTileMultiPrimitiveBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input(GTMaterialGen.getModItem(GTHelperMods.TFOREST, "liveroot")),
                    input("nuggetGold", 1) }, 400, GTMaterialGen.getModItem(GTHelperMods.TFOREST, "ironwood_ingot", 2));
        }

        if (GTConfig.modcompat.compatEnderIO && Loader.isModLoaded(GTHelperMods.ENDERIO)) {
            // Dark steel upgrade
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] {
                    input(GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_dark_iron_bars", 1)), input("itemClay", 1),
                    input("string", 4) }, 500, 120000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "item_dark_steel_upgrade"));
            // Electric Steel
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1), input("dustCoal", 1),
                    input("itemSilicon", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 0, 1));
            // Energenic Alloy
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustRedstone", 1), metal("Gold", 1),
                    input("dustGlowstone", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 1, 1));
            // Vibrant Alloy
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("ingotEnergeticAlloy", 1),
                    input("enderpearl", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 2, 1));
            // Redstone Alloy
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustRedstone", 1),
                    input("itemSilicon", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 3, 1));
            // Conductive Iron
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustRedstone", 1),
                    metal("Iron", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 4, 1));
            // Pulsating Alloy
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input("enderpearl", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 5, 1));
            // Dark Steel
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Steel", 1),
                    input("dustObsidian", 1) }, 500, 80000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 6, 1));
            // End Steel
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("endstone", 1), input("ingotDarkSteel", 1),
                    input("obsidian", 1) }, 500, 80000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 8, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input(GTMaterialGen.get(Blocks.SOUL_SAND, 1)),
                    metal("Gold", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_alloy_ingot", 7, 1));
            // Fused Quartz - Custom Recipe!
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustNetherQuartz", 2),
                    input("blockGlass", 1) }, 500, 10000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_fused_quartz", 1));
            // Enlightened Fused Quartz
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("gemQuartz", 4),
                    input("dustGlowstone", 4) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_enlightened_fused_quartz", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("blockQuartz", 4),
                    input("glowstone", 4) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_enlightened_fused_quartz", 1));
            // Enlightned Clear Glass
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("blockGlass", 1),
                    input("dustGlowstone", 4) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_enlightened_fused_glass", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("blockGlass", 1),
                    input("glowstone", 1) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_enlightened_fused_glass", 1));
            // Dark Fused Quartz
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dyeBlack", 1),
                    input("gemQuartz", 4) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_dark_fused_quartz", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dyeBlack", 1),
                    input("blockQuartz", 1) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_dark_fused_quartz", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dyeBlack", 1),
                    input("blockGlassHardened", 1) }, 500, 10000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_dark_fused_quartz", 1));
            // Dyes
            //doEnderIOBlastFurnaceDyeThings("Green", 48);
            //doEnderIOBlastFurnaceDyeThings("Brown", 49);
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCoal", 6),
                    input("slimeball", 1) }, 500, 8000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 50, 2));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCoal", 3),
                    input("egg", 1) }, 500, 6000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 50, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCharcoal", 6),
                    input("slimeball", 1) }, 500, 8000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 50, 2));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCharcoal", 3),
                    input("egg", 1) }, 500, 6000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 50, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input(GTMaterialGen.get(Items.BEETROOT, 1)),
                    input("itemClay", 3), input("egg", 6) }, 500, 60000, new ItemStack(Items.DYE, 12, 1));
            // Machine Chassis
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("itemSimpleMachineChassi", 1),
                    input("dyeMachine", 1) }, 500, 14400, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 1, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("itemEndSteelMachineChassi", 1),
                    input("dyeEnhancedMachine", 1) }, 500, 14400, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 54, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("itemSimpleMachineChassi", 1),
                    input("dyeSoulMachine", 1) }, 500, 14400, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 53, 1));
            // Other Stuff
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustLapis", 1), input("blockWool", 1),
                    input("dustTin", 1) }, 500, 20000, GTMaterialGen.getModItem(GTHelperMods.ENDERIO, "block_industrial_insulation", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("ingotBrickNether", 1),
                    input("cropNetherWart", 4),
                    input("itemClay", 6) }, 500, 80000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 72, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustBedrock", 1),
                    input("dustCoal", 1) }, 500, 20000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 75, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustBedrock", 1),
                    input("dustCharcoal", 1) }, 500, 20000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 75, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustGlowstone", 1),
                    input("itemClay", 1) }, 500, 20000, GTMaterialGen.getModMetaItem(GTHelperMods.ENDERIO, "item_material", 76, 2));
        }

        if (GTConfig.modcompat.compatIc2Extras && Loader.isModLoaded(GTHelperMods.IC2_EXTRAS)){
            addCrushedOreRecipes(GTCXMaterial.Tetrahedrite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Antimony, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.Zinc, 1));
            addCrushedOreRecipes(GTCXMaterial.Galena, GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 2), GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "silvertinydust"));
            addCrushedOreRecipes(GTCXMaterial.Cinnabar, GTCXMaterialGen.getTinyDust(GTCXMaterial.Redstone, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 1));
            addCrushedOreRecipes(GTCXMaterial.Sphalerite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Zinc, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.GarnetYellow, 1));
            addCrushedOreRecipes(GTMaterial.Pyrite, GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Phosphorus, 1));
            addCrushedOreRecipes(GTMaterial.Bauxite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Grossular, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Titanium, 1));
            addCrushedOreRecipes(GTMaterial.Platinum, GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 2), GTCXMaterialGen.getTinyDust(GTMaterial.Nickel, 1));
            addCrushedOreRecipes(GTMaterial.Tungsten, GTCXMaterialGen.getTinyDust(GTCXMaterial.Manganese, 1), GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "silvertinydust"));
            addCrushedOreRecipes(GTMaterial.Iridium, GTCXMaterialGen.getTinyDust(GTMaterial.Platinum, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.Osmium, 1));
            GTRecipeProcessing.maceratorUtil("oreGalena", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Galena, 2));
            GTRecipeProcessing.maceratorUtil("oreTetrahedrite", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Tetrahedrite, 2));
            GTRecipeProcessing.maceratorUtil("oreCassiterite", 1, GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "tincrushedore", 4));
            GTRecipeProcessing.maceratorUtil("orePyrite", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Pyrite, 5));
            GTRecipeProcessing.maceratorUtil("oreCinnabar", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Cinnabar, 3));
            GTRecipeProcessing.maceratorUtil("oreSphalerite", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Sphalerite, 4));
            GTRecipeProcessing.maceratorUtil("orePlatinum", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Platinum, 2));
            GTRecipeProcessing.maceratorUtil("oreTungstate", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Tungsten, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreBauxite", 1));
            TileEntityMacerator.addRecipe("oreBauxite", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Bauxite, 4));
            ClassicRecipes.macerator.removeRecipe(input("oreIridium", 1));
            TileEntityMacerator.addRecipe("oreIridium", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Iridium, 2));
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 6), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 6), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Platinum, 1), GTMaterialGen.getIngot(GTMaterial.Platinum, 1), 1.0F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTMaterial.Platinum, 1), GTMaterialGen.getIngot(GTMaterial.Platinum, 1), 1.0F);
            GTRecipeCraftingHandler.removeRecipe(GTHelperMods.IC2_EXTRAS, "shapeless_item.itemdustbronze_-1753288283");
            String circuit = "circuitBasic";
            String machineBlock = "machineBlockBasic";
            int recipeId = IC2.config.getFlag("SteelRecipes") ? 42294514 : -997650306;
            GTRecipeCraftingHandler.overrideGTRecipe(GTHelperMods.IC2_EXTRAS, "shaped_tile.orewashingplant_" + recipeId, GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "orewashingplant"), "PPP", "BMB", "cCc", 'P', GTCXRecipe.materialRefinedIron, 'B', Items.BUCKET, 'M', machineBlock, 'c', Ic2Items.carbonMesh, 'C', circuit);
            GTRecipeCraftingHandler.overrideGTRecipe(GTHelperMods.IC2_EXTRAS, "shaped_tile.roller_-2064391190", GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "roller"), "CPC", "PMP", "cPc", 'C', circuit, 'P', Blocks.PISTON, 'M', machineBlock, 'c', GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "coil"));
            IRecipeInput casing = new RecipeInputCombined(1, new RecipeInputOreDict("casingSteel"), new RecipeInputOreDict("casingRefinedIron"), new RecipeInputOreDict("casingBronze"));
            GTRecipeCraftingHandler.overrideGTRecipe(GTHelperMods.IC2_EXTRAS, "shaped_tile.extruder_704871140", GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "extruder"), "SCS", "cMc", "SCS", 'C', circuit, 'S', casing, 'M', machineBlock, 'c', GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "coil"));
        }
        if (Loader.isModLoaded("gravisuit")){
            GTRecipeCraftingHandler.overrideGTRecipe("gravisuit", "shaped_item.advanceddiamondchainsaw_-416372460", GTMaterialGen.getModItem("gravisuit", "advancedchainsaw"), " SS", "SCS", "BS ", 'S', GTCXRecipe.tungstenSteel, 'C', GTCXItems.diamondChainsaw, 'B', GTItems.lithiumBattery);
            GTRecipeCraftingHandler.overrideGTRecipe("gravisuit", "shaped_item.advanceddrill_1408250051", GTMaterialGen.getModItem("gravisuit", "advanceddrill"), " S ", "SDS", "SBS", 'S', GTCXRecipe.tungstenSteel, 'D', Ic2Items.diamondDrill, 'B', GTItems.lithiumBattery);
        }
    }

    public static void addCrushedOreRecipes(GTMaterial main, ItemStack outputWashSide, ItemStack outputThermalSide){
        GTCXIc2cECompat.addOreWashingMachineRecipe("crushed" + main.getDisplayName(), 1, GTCXMaterialGen.getPurifiedCrushedOre(main, 1), outputWashSide, GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "stonedust"));
        GTCXIc2cECompat.addThermalCentrifugeRecipe("crushedPurified" + main.getDisplayName(), 1, 400, GTMaterialGen.getDust(main, 1), outputThermalSide);
        GTCXIc2cECompat.addThermalCentrifugeRecipe("crushed" + main.getDisplayName(), 1, 600, GTMaterialGen.getDust(main, 1), outputThermalSide, GTMaterialGen.getModItem(GTHelperMods.IC2_EXTRAS, "stonedust"));
        TileEntityMacerator.addRecipe("crushed" + main.getDisplayName(), 1, GTMaterialGen.getDust(main, 1));
        TileEntityMacerator.addRecipe("crushedPurified" + main.getDisplayName(), 1, GTMaterialGen.getDust(main, 1));
    }

    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

}
