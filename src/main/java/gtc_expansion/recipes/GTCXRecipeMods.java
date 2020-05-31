package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXItems;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.GTCXTileAlloySmelter;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileWiremill;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.util.GTCXIc2cECompat;
import gtc_expansion.util.GTCXValues;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeCraftingHandler;
import gtclassic.common.GTConfig;
import gtclassic.common.GTItems;
import gtclassic.common.recipe.GTRecipe;
import gtclassic.common.tile.GTTileCentrifuge;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
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
        if (GTConfig.modcompat.compatTwilightForest && Loader.isModLoaded(GTValues.MOD_ID_TFOREST)) {
            GTCXTileMultiPrimitiveBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input(GTMaterialGen.getModItem(GTValues.MOD_ID_TFOREST, "liveroot")),
                    input("nuggetGold", 1) }, 400, GTMaterialGen.getModItem(GTValues.MOD_ID_TFOREST, "ironwood_ingot", 2));
        }

        if (GTConfig.modcompat.compatEnderIO && Loader.isModLoaded(GTValues.MOD_ID_ENDERIO)) {
            // Dark steel upgrade
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] {
                    input(GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_dark_iron_bars", 1)), input("itemClay", 1),
                    input("string", 4) }, 500, 120000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "item_dark_steel_upgrade"));
            // Electric Steel
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1), input("dustCoal", 1),
                    input("itemSilicon", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_alloy_ingot", 0, 1));
            // Energenic Alloy
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustRedstone", 1), metal("Gold", 1),
                    input("dustGlowstone", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_alloy_ingot", 1, 1));
            // Vibrant Alloy
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("ingotEnergeticAlloy", 1),
                    input("enderpearl", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_alloy_ingot", 2, 1));
            // Redstone Alloy
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustRedstone", 1),
                    input("itemSilicon", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_alloy_ingot", 3, 1));
            // Conductive Iron
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustRedstone", 1),
                    metal("Iron", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_alloy_ingot", 4, 1));
            // Pulsating Alloy
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input("enderpearl", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_alloy_ingot", 5, 1));
            // Dark Steel
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Steel", 1),
                    input("dustObsidian", 1) }, 500, 80000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_alloy_ingot", 6, 1));
            // End Steel
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("endstone", 1), input("ingotDarkSteel", 1),
                    input("obsidian", 1) }, 500, 80000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_alloy_ingot", 8, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input(GTMaterialGen.get(Blocks.SOUL_SAND, 1)),
                    metal("Gold", 1) }, 500, 40000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_alloy_ingot", 7, 1));
            // Fused Quartz - Custom Recipe!
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustNetherQuartz", 2),
                    input("blockGlass", 1) }, 500, 10000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_fused_quartz", 1));
            // Enlightened Fused Quartz
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("gemQuartz", 4),
                    input("dustGlowstone", 4) }, 500, 20000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_enlightened_fused_quartz", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("blockQuartz", 4),
                    input("glowstone", 4) }, 500, 20000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_enlightened_fused_quartz", 1));
            // Enlightned Clear Glass
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("blockGlass", 1),
                    input("dustGlowstone", 4) }, 500, 20000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_enlightened_fused_glass", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("blockGlass", 1),
                    input("glowstone", 1) }, 500, 20000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_enlightened_fused_glass", 1));
            // Dark Fused Quartz
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dyeBlack", 1),
                    input("gemQuartz", 4) }, 500, 20000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_dark_fused_quartz", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dyeBlack", 1),
                    input("blockQuartz", 1) }, 500, 20000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_dark_fused_quartz", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dyeBlack", 1),
                    input("blockGlassHardened", 1) }, 500, 10000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_dark_fused_quartz", 1));
            // Dyes
            //doEnderIOBlastFurnaceDyeThings("Green", 48);
            //doEnderIOBlastFurnaceDyeThings("Brown", 49);
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCoal", 6),
                    input("slimeball", 1) }, 500, 8000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 50, 2));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCoal", 3),
                    input("egg", 1) }, 500, 6000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 50, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCharcoal", 6),
                    input("slimeball", 1) }, 500, 8000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 50, 2));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustCharcoal", 3),
                    input("egg", 1) }, 500, 6000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 50, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input(GTMaterialGen.get(Items.BEETROOT, 1)),
                    input("itemClay", 3), input("egg", 6) }, 500, 60000, new ItemStack(Items.DYE, 12, 1));
            // Machine Chassis
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("itemSimpleMachineChassi", 1),
                    input("dyeMachine", 1) }, 500, 14400, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 1, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("itemEndSteelMachineChassi", 1),
                    input("dyeEnhancedMachine", 1) }, 500, 14400, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 54, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("itemSimpleMachineChassi", 1),
                    input("dyeSoulMachine", 1) }, 500, 14400, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 53, 1));
            // Other Stuff
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustLapis", 1), input("blockWool", 1),
                    input("dustTin", 1) }, 500, 20000, GTMaterialGen.getModItem(GTValues.MOD_ID_ENDERIO, "block_industrial_insulation", 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("ingotBrickNether", 1),
                    input("cropNetherWart", 4),
                    input("itemClay", 6) }, 500, 80000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 72, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustBedrock", 1),
                    input("dustCoal", 1) }, 500, 20000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 75, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustBedrock", 1),
                    input("dustCharcoal", 1) }, 500, 20000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 75, 1));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { input("dustGlowstone", 1),
                    input("itemClay", 1) }, 500, 20000, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_ENDERIO, "item_material", 76, 2));
        }

        if (GTConfig.modcompat.compatIc2Extras && Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS)){
            addCrushedOreRecipes(GTCXMaterial.Tetrahedrite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Antimony, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.Zinc, 1));
            addCrushedOreRecipes(GTCXMaterial.Galena, GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 2), GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "silvertinydust"));
            addCrushedOreRecipes(GTCXMaterial.Cinnabar, GTCXMaterialGen.getTinyDust(GTCXMaterial.Redstone, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 1));
            addCrushedOreRecipes(GTCXMaterial.Sphalerite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Zinc, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.GarnetYellow, 1));
            addCrushedOreRecipes(GTMaterial.Pyrite, GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Phosphorus, 1));
            addCrushedOreRecipes(GTMaterial.Bauxite, GTCXMaterialGen.getTinyDust(GTCXMaterial.Grossular, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Titanium, 1));
            addCrushedOreRecipes(GTMaterial.Platinum, GTCXMaterialGen.getTinyDust(GTMaterial.Sulfur, 2), GTCXMaterialGen.getTinyDust(GTMaterial.Nickel, 1));
            addCrushedOreRecipes(GTMaterial.Tungsten, GTCXMaterialGen.getTinyDust(GTCXMaterial.Manganese, 1), GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "silvertinydust"));
            addCrushedOreRecipes(GTMaterial.Iridium, GTCXMaterialGen.getTinyDust(GTMaterial.Platinum, 1), GTCXMaterialGen.getTinyDust(GTCXMaterial.Osmium, 1));
            addCrushedOreRecipes(GTCXMaterial.Chromite, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "irontinydust"), GTCXMaterialGen.getTinyDust(GTMaterial.Chrome, 1));
            addCrushedOreRecipes(GTCXMaterial.Cassiterite, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "tintinydust"), GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "tintinydust"));
            GTRecipe.maceratorUtil("oreGalena", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Galena, 2));
            GTRecipe.maceratorUtil("oreTetrahedrite", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Tetrahedrite, 2));
            GTRecipe.maceratorUtil("oreCassiterite", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Cassiterite, 4));
            GTRecipe.maceratorUtil("orePyrite", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Pyrite, 4));
            GTRecipe.maceratorUtil("oreCinnabar", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Cinnabar, 3));
            GTRecipe.maceratorUtil("oreSphalerite", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Sphalerite, 2));
            GTRecipe.maceratorUtil("orePlatinum", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Platinum, 2));
            GTRecipe.maceratorUtil("oreTungstate", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Tungsten, 4));
            GTRecipe.maceratorUtil("oreChromite", 1, GTCXMaterialGen.getCrushedOre(GTCXMaterial.Chromite, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreBauxite", 1));
            TileEntityMacerator.addRecipe("oreBauxite", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Bauxite, 2));
            ClassicRecipes.macerator.removeRecipe(input("oreIridium", 1));
            TileEntityMacerator.addRecipe("oreIridium", 1, GTCXMaterialGen.getCrushedOre(GTMaterial.Iridium, 2));
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 6), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 6), 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Platinum, 1), GTMaterialGen.getIngot(GTMaterial.Platinum, 1), 1.0F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTMaterial.Platinum, 1), GTMaterialGen.getIngot(GTMaterial.Platinum, 1), 1.0F);
            GameRegistry.addSmelting(GTCXMaterialGen.getCrushedOre(GTCXMaterial.Cassiterite, 1), Ic2Items.tinIngot, 0.5F);
            GameRegistry.addSmelting(GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Cassiterite, 1), Ic2Items.tinIngot, 0.5F);
            GTRecipeCraftingHandler.removeRecipe(GTValues.MOD_ID_IC2_EXTRAS, "shapeless_item.itemdustbronze_-1753288283");
            String circuit = "circuitBasic";
            String machineBlock = "machineBlockBasic";
            int recipeId = IC2.config.getFlag("SteelRecipes") ? 42294514 : -997650306;
            GTRecipeCraftingHandler.overrideGTRecipe(GTValues.MOD_ID_IC2_EXTRAS, "shaped_tile.orewashingplant_" + recipeId, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "orewashingplant"), "PPP", "BMB", "cCc", 'P', GTCXValues.MATERIAL_REFINED_IRON, 'B', Items.BUCKET, 'M', machineBlock, 'c', Ic2Items.carbonMesh, 'C', circuit);
            GTRecipeCraftingHandler.overrideGTRecipe(GTValues.MOD_ID_IC2_EXTRAS, "shaped_tile.roller_-2064391190", GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "roller"), "CPC", "PMP", "cPc", 'C', circuit, 'P', Blocks.PISTON, 'M', machineBlock, 'c', GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "coil"));
        }
        if (Loader.isModLoaded(GTValues.MOD_ID_THERMAL) && GTConfig.modcompat.compatThermal){
            GTCXTileAlloySmelter.addRecipe("dustCopper", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass", 0, 2));
            GTCXTileAlloySmelter.addRecipe("dustTin", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass", 1, 2));
            GTCXTileAlloySmelter.addRecipe("dustSilver", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass", 2, 2));
            GTCXTileAlloySmelter.addRecipe("dustLead", 1, "dustObsidian", 4, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass", 3, 2));
            GTCXTileAlloySmelter.addRecipe("dustAluminium", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass", 4, 2));
            GTCXTileAlloySmelter.addRecipe("dustNickel", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass", 5, 2));
            GTCXTileAlloySmelter.addRecipe("dustPlatinum", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass", 6, 2));
            GTCXTileAlloySmelter.addRecipe("dustIridium", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass", 7, 2));
            GTCXTileAlloySmelter.addRecipe("dustMithril", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass", 8, 2));
            GTCXTileAlloySmelter.addRecipe("dustSteel", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass_alloy", 0, 2));
            GTCXTileAlloySmelter.addRecipe("dustElectrum", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass_alloy", 1, 2));
            GTCXTileAlloySmelter.addRecipe("dustInvar", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass_alloy", 2, 2));
            GTCXTileAlloySmelter.addRecipe("dustBronze", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass_alloy", 3, 2));
            GTCXTileAlloySmelter.addRecipe("dustConstantan", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass_alloy", 4, 2));
            GTCXTileAlloySmelter.addRecipe("dustSignalum", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass_alloy", 5, 2));
            GTCXTileAlloySmelter.addRecipe("dustLumium", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass_alloy", 6, 2));
            GTCXTileAlloySmelter.addRecipe("dustEnderium", 1, "blockGlassHardened", 2, GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "glass_alloy", 7, 2));
            GTTileCentrifuge.addRecipe("clathrateRedstone", 1, 0, GTTileCentrifuge.totalEu(4000), GTMaterialGen.get(Items.REDSTONE, 2));
        }
        if (Loader.isModLoaded("gravisuit") && GTCXConfiguration.modcompat.compatGravisuit){
            GTRecipeCraftingHandler.overrideGTRecipe("gravisuit", "shaped_item.advanceddiamondchainsaw_-416372460", GTMaterialGen.getModItem("gravisuit", "advancedchainsaw"), " SS", "SCS", "BS ", 'S', GTCXValues.TUNGSTEN_STEEL, 'C', GTCXItems.diamondChainsaw, 'B', GTItems.lithiumBattery);
            GTRecipeCraftingHandler.overrideGTRecipe("gravisuit", "shaped_item.advanceddrill_1408250051", GTMaterialGen.getModItem("gravisuit", "advanceddrill"), " S ", "SDS", "SBS", 'S', GTCXValues.TUNGSTEN_STEEL, 'D', Ic2Items.diamondDrill, 'B', GTItems.lithiumBattery);
        }
        if (Loader.isModLoaded("railcraft") && GTCXConfiguration.modcompat.compatRailcraft){
            GTCXTileAssemblingMachine.addRecipe(new IRecipeInput[]{new RecipeInputCombined(12, input("rodIron", 12), input("rodBronze", 12)), input(GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1))}, GTCXTileAssemblingMachine.totalEu(400), GTMaterialGen.getModMetaItem("railcraft", "rail", 0, 8));
            GTCXTileAssemblingMachine.addRecipe("rodInvar", 12, GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1), 400, GTMaterialGen.getModMetaItem("railcraft", "rail", 0, 12));
            GTCXTileAssemblingMachine.addRecipe("rodSteel", 12, GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1), 400, GTMaterialGen.getModMetaItem("railcraft", "rail", 0, 16));
            GTCXTileAssemblingMachine.addRecipe(new IRecipeInput[]{new RecipeInputCombined(12, input("rodTitanium", 12), input("rodTungsten", 12)), new RecipeInputItemStack(GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1))}, GTCXTileAssemblingMachine.totalEu(400), GTMaterialGen.getModMetaItem("railcraft", "rail", 0, 32));
            GTCXTileAssemblingMachine.addRecipe("rodTungstensteel", 12,  GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1), 400, GTMaterialGen.getModMetaItem("railcraft", "rail", 0, 48));
            GTCXTileAssemblingMachine.addRecipe("rodSteel", 12, "dustObsidian", 3, 400, GTMaterialGen.getModMetaItem("railcraft", "rail", 4, 8));
            GTCXTileAssemblingMachine.addRecipe("rodInvar", 12, "dustObsidian", 3, 400, GTMaterialGen.getModMetaItem("railcraft", "rail", 4, 4));
            GTCXTileAssemblingMachine.addRecipe("rodTungstensteel", 12, "dustObsidian", 3, 400, GTMaterialGen.getModMetaItem("railcraft", "rail", 4, 16));
            GTCXTileAssemblingMachine.addRecipe("rodGold", 12, "dustRedstone", 3, 400, GTMaterialGen.getModMetaItem("railcraft", "rail", 1, 8));
            GTCXTileAssemblingMachine.addRecipe("rodCopper", 12, GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1), 400, GTMaterialGen.getModMetaItem("railcraft", "rail", 5, 6));
            GTCXTileAssemblingMachine.addRecipe("rodSteel", 12, "rodCopper", 6, 400, GTMaterialGen.getModMetaItem("railcraft", "rail", 5, 12));
            GTCXTileWiremill.addRecipe(new RecipeInputCombined(6, input("rodIron", 6), input("rodBronze", 6)), 400, GTMaterialGen.getModItem("railcraft", "rebar", 4));
            GTCXTileWiremill.addRecipe(new RecipeInputCombined(6, input("rodRefinedIron", 6), input("rodInvar", 6)), 400, GTMaterialGen.getModItem("railcraft", "rebar", 6));
            GTCXTileWiremill.addRecipe(new RecipeInputCombined(6, input("rodTungsten", 6), input("rodTitanium", 6)), 400, GTMaterialGen.getModItem("railcraft", "rebar", 16));
            GTCXTileWiremill.addRecipe("rodSteel", 6, GTMaterialGen.getModItem("railcraft", "rebar", 8));
            GTCXTileWiremill.addRecipe("nuggetBronze", 3, GTMaterialGen.getModItem("railcraft", "track_parts"));
            GTCXTileWiremill.addRecipe("nuggetIron", 2, GTMaterialGen.getModItem("railcraft", "track_parts"));
            GTCXTileWiremill.addRecipe("nuggetSteel", 1, GTMaterialGen.getModItem("railcraft", "track_parts"));
            GTCXTileWiremill.addRecipe(GTRecipeCraftingHandler.combineRecipeObjects("nuggetTungsten", "nuggetTitanium"), GTMaterialGen.getModItem("railcraft", "track_parts", 2));
            GTCXTileWiremill.addRecipe("nuggetTungstensteel", 1, GTMaterialGen.getModItem("railcraft", "track_parts", 3));
        }
    }

    public static void addCrushedOreRecipes(GTMaterial main, ItemStack outputWashSide, ItemStack outputThermalSide){
        GTCXIc2cECompat.addOreWashingMachineRecipe("crushed" + main.getDisplayName(), 1, GTCXMaterialGen.getPurifiedCrushedOre(main, 1), outputWashSide, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "stonedust"));
        GTCXIc2cECompat.addThermalCentrifugeRecipe("crushedPurified" + main.getDisplayName(), 1, 400, GTMaterialGen.getDust(main, 1), outputThermalSide);
        GTCXIc2cECompat.addThermalCentrifugeRecipe("crushed" + main.getDisplayName(), 1, 600, GTMaterialGen.getDust(main, 1), outputThermalSide, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "stonedust"));
        TileEntityMacerator.addRecipe("crushed" + main.getDisplayName(), 1, GTMaterialGen.getDust(main, 1));
        TileEntityMacerator.addRecipe("crushedPurified" + main.getDisplayName(), 1, GTMaterialGen.getDust(main, 1));
    }

    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

}
