package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXValues;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.GTCXTileAlloySmelter;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileCentrifuge;
import gtc_expansion.tile.GTCXTileElectrolyzer;
import gtc_expansion.tile.GTCXTileFluidSmelter;
import gtc_expansion.tile.GTCXTileWiremill;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiVacuumFreezer;
import gtc_expansion.util.GTCXIc2cECompat;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeCraftingHandler;
import gtclassic.common.GTConfig;
import gtclassic.common.GTItems;
import gtclassic.common.tile.GTTileCentrifuge;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

import static gtc_expansion.data.GTCXValues.EMPTY;
import static gtclassic.common.recipe.GTRecipeMods.input;
import static gtclassic.common.recipe.GTRecipeMods.metal;

public class GTCXRecipeMods {
    public static void init(){
        if (GTConfig.modcompat.compatTwilightForest && Loader.isModLoaded(GTValues.MOD_ID_TFOREST)) {
            GTCXTileMultiPrimitiveBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input(GTMaterialGen.getModItem(GTValues.MOD_ID_TFOREST, "liveroot")),
                    input("nuggetGold", 1) }, 400, GTMaterialGen.getModItem(GTValues.MOD_ID_TFOREST, "ironwood_ingot", 2));
            GTCXTileMultiIndustrialBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input(GTMaterialGen.getModItem(GTValues.MOD_ID_TFOREST, "liveroot")),
                    input("nuggetGold", 1) }, 1500, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_TFOREST, "ironwood_ingot", 2));
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
            TileEntityMacerator.addRecipe(GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "iridiumshard"), GTCXMaterialGen.getTinyDust(GTMaterial.Iridium, 1));
            GTRecipeCraftingHandler.removeRecipe(GTValues.MOD_ID_IC2_EXTRAS, "shapeless_item.itemdustbronze_-1753288283");
            String circuit = "circuitBasic";
            int recipeId = GTCXValues.STEEL_MODE ? 42294514 : -997650306;
            GTRecipeCraftingHandler.overrideGTRecipe(GTValues.MOD_ID_IC2_EXTRAS, "shaped_tile.orewashingplant_" + recipeId, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "orewashingplant"), "PPP", "BMB", "cCc", 'P', GTCXValues.MATERIAL_STEELS, 'B', Items.BUCKET, 'M', GTValues.MACHINE_BASIC, 'c', Ic2Items.carbonMesh, 'C', circuit);
            recipeId = GTCXValues.STEEL_MODE ? 47090041 : -929770887;
            GTRecipeCraftingHandler.overrideGTRecipe(GTValues.MOD_ID_IC2_EXTRAS, "shaped_tile.thermalcentrifuge_" + recipeId, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "thermalcentrifuge"), "CLC", "SAS", "SHS", 'C', GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "coil"), 'L', Ic2Items.miningLaser, 'S', GTCXValues.MATERIAL_STEELS, 'A', GTValues.MACHINE_ADV, 'H', GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "heatconductor"));
            ItemStack fullCan = GTMaterialGen.get(GTItems.sprayCan);
            NBTTagCompound nbt = StackUtil.getNbtData(fullCan);
            nbt.setInteger("color", 15);
            GTCXIc2cECompat.addFluidCanningMachineFillingRecipe(input(GTMaterialGen.get(GTItems.sprayCanEmpty)), GTMaterialGen.getFluidStack(GTMaterial.MagicDye, 1000), fullCan);
            GTCXIc2cECompat.addFluidCanningMachineFillingRecipe(input(GTMaterialGen.get(GTCXItems.batteryHull)), GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid, 2000), GTCXItems.acidBattery.getFull());
            GTCXIc2cECompat.addFluidCanningMachineFillingRecipe(input(GTMaterialGen.get(GTCXItems.batteryHull)), GTMaterialGen.getFluidStack(GTMaterial.Mercury, 2000), GTCXItems.mercuryBattery.getFull());
            GTCXIc2cECompat.addFluidCanningMachineFillingRecipe(input(GTMaterialGen.get(GTCXItems.batteryHull)), GTMaterialGen.getFluidStack(GTMaterial.Sodium, 2000), GTMaterialGen.get(GTCXItems.sodiumBattery));
            GTCXIc2cECompat.addFluidCanningMachineFillingRecipe(input(GTMaterialGen.get(GTCXItems.largeBatteryHull)), GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid, 6000), GTCXItems.largeAcidBattery.getFull());
            GTCXIc2cECompat.addFluidCanningMachineFillingRecipe(input(GTMaterialGen.get(GTCXItems.largeBatteryHull)), GTMaterialGen.getFluidStack(GTMaterial.Mercury, 6000), GTCXItems.largeMercuryBattery.getFull());
            GTCXIc2cECompat.addFluidCanningMachineFillingRecipe(input(GTMaterialGen.get(GTCXItems.largeBatteryHull)), GTMaterialGen.getFluidStack(GTMaterial.Sodium, 6000), GTMaterialGen.get(GTCXItems.largeSodiumBattery));
        }
        if (Loader.isModLoaded(GTValues.MOD_ID_THERMAL) && GTConfig.modcompat.compatThermal){
            // Oil sand stuff
            FluidStack oil = Loader.isModLoaded("thermalexpansion") ? GTMaterialGen.getFluidStack("crude_oil", 1000) : GTMaterialGen.getFluidStack(GTMaterial.Oil);
            GTTileCentrifuge.RECIPE_LIST.startMassChange();
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.test_tube");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.test_tube_1");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.test_tube_2");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.test_tube_3");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.test_tube_4");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.test_tube_5");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.test_tube_6");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.redstone");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.redstone_1");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.yellowDust");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.yellowDust_1");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustEnderPearl_1");
            GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustEnderPearl_2");
            GTTileCentrifuge.RECIPE_LIST.finishMassChange();
            GTCXTileCentrifuge.addRecipe("oreClathrateOilSand", 1, GTCXTileCentrifuge.totalEu(8000), EMPTY, oil);
            GTCXTileCentrifuge.addRecipe("oreClathrateOilShale", 1, GTCXTileCentrifuge.totalEu(8000), EMPTY, oil);
            GTCXTileCentrifuge.addRecipe(GTMaterialGen.getFluidStack("crude_oil", 3000), GTCXTileCentrifuge.totalEu(96000), EMPTY, GTMaterialGen.getFluidStack(GTMaterial.Fuel, 2000), GTMaterialGen.getFluidStack(GTMaterial.Lubricant));
            // Thermal fluids and ores
            GTCXTileCentrifuge.addRecipe("oreClathrateRedstone", 1, GTCXTileCentrifuge.totalEu(8000), EMPTY, GTMaterialGen.getFluidStack("redstone", 1000));
            GTCXTileCentrifuge.addRecipe("oreClathrateGlowstone", 1, GTCXTileCentrifuge.totalEu(8000), EMPTY, GTMaterialGen.getFluidStack("glowstone", 1000));
            GTCXTileCentrifuge.addRecipe("oreClathrateEnder", 1, GTCXTileCentrifuge.totalEu(8000), EMPTY, GTMaterialGen.getFluidStack("ender", 1000));
            GTCXTileCentrifuge.addRecipe(GTMaterialGen.getFluidStack("redstone", 1000), GTCXTileCentrifuge.totalEu(8000), new ItemStack[]{GTMaterialGen.get(Items.REDSTONE, 8)});
            GTCXTileCentrifuge.addRecipe(GTMaterialGen.getFluidStack("glowstone", 1000), GTCXTileCentrifuge.totalEu(8000), new ItemStack[]{GTMaterialGen.get(Items.GLOWSTONE_DUST, 3)});
            GTCXTileCentrifuge.addRecipe(GTMaterialGen.getFluidStack("ender", 1000), GTCXTileCentrifuge.totalEu(8000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.EnderPearl, 3)});
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
            GTCXTileMultiVacuumFreezer.addRecipe("clathrateRedstone", 1, 4000, GTMaterialGen.get(Items.REDSTONE, 2));
            GTCXTileMultiVacuumFreezer.addRecipe("clathrateGlowstone", 1, 4000, GTMaterialGen.get(Items.GLOWSTONE_DUST, 1));
            GTCXTileMultiVacuumFreezer.addRecipe("clathrateEnder", 1, 4000, GTMaterialGen.get(Items.ENDER_PEARL, 1));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "material", 1024, 1), 200, 12800, GTMaterialGen.getFluidStack("pyrotheum", 250));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "material", 1025, 1), 200, 12800, GTMaterialGen.getFluidStack("cryotheum", 250));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "material", 1026, 1), 200, 12800, GTMaterialGen.getFluidStack("aerotheum", 250));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "material", 1027, 1), 200, 12800, GTMaterialGen.getFluidStack("petrotheum", 250));
            GTCXTileFluidSmelter.addRecipe("dustCoal", 1, 200, 12800, GTMaterialGen.getFluidStack("coal", 100));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.get(Items.ENDER_PEARL), 200, 12800, GTMaterialGen.getFluidStack("ender", 250));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.get(Items.REDSTONE), 200, 12800, GTMaterialGen.getFluidStack("redstone", 250));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.get(Items.GLOWSTONE_DUST), 200, 12800, GTMaterialGen.getFluidStack("glowstone", 250));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "material", 892, 1), 200, 12800, GTMaterialGen.getFluidStack("crude_oil", 250));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "material", 893, 1), 200, 12800, GTMaterialGen.getFluidStack("redstone", 250));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "material", 894, 1), 200, 12800, GTMaterialGen.getFluidStack("glowstone", 250));
            GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getModMetaItem(GTValues.MOD_ID_THERMAL, "material", 895, 1), 200, 12800, GTMaterialGen.getFluidStack("ender", 250));
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
            GTCXTileWiremill.addRecipe("blockCopper", 1, GTMaterialGen.getModMetaItem("railcraft", "charge", 4, 1));
            GTCXTileAssemblingMachine.addRecipe(GTMaterialGen.getModMetaItem("railcraft", "rail", 5, 1), GTMaterialGen.get(Blocks.RAIL, 16), 800, GTMaterialGen.getModItem("railcraft", "track_flex_electric", 16));
            GTCXTileAssemblingMachine.addRecipe(GTMaterialGen.getModMetaItem("railcraft", "rail", 5, 1), GTMaterialGen.getModItem("railcraft", "track_flex_high_speed", 16), 800, GTMaterialGen.getModItem("railcraft", "track_flex_hs_electric", 16));
            GTCXTileAssemblingMachine.addRecipe("plateInvar", 3, GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1), 400, GTMaterialGen.getModMetaItem("railcraft", "charge", 6, 1));
            GTCXTileAssemblingMachine.addRecipe("plateIron", 3, GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1), 400, GTMaterialGen.getModMetaItem("railcraft", "charge", 7, 1));
            GTCXTileAssemblingMachine.addRecipe("plateZinc", 3, GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1), 400, GTMaterialGen.getModMetaItem("railcraft", "charge", 8, 1));
            GTCXTileAssemblingMachine.addRecipe("plateSilver", 3, GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1), 400, GTMaterialGen.getModMetaItem("railcraft", "charge", 10, 1));
        }
        if (Loader.isModLoaded("traverse")){
            TileEntityMacerator.addRecipe(GTMaterialGen.getModItem("traverse", "red_rock"), GTMaterialGen.getDust(GTCXMaterial.RedRock, 1));
        }
        if (Loader.isModLoaded("appliedenergistics2")){
            ClassicRecipes.electrolyzer.addChargeRecipe(GTMaterialGen.getModMetaItem("appliedenergistics2", "material", 0, 1), GTMaterialGen.getModMetaItem("appliedenergistics2", "material", 1, 1), 800, GTMaterialGen.getModMetaItem("appliedenergistics2", "material", 1, 1).getUnlocalizedName());
            GTCXTileElectrolyzer.addRecipe(GTMaterialGen.getModMetaItem("appliedenergistics2", "crystal_seed", 0, 1), 0,GTCXTileElectrolyzer.totalEu(327680), new ItemStack[]{GTMaterialGen.getModMetaItem("appliedenergistics2", "material", 10, 1)});
            GTCXTileElectrolyzer.addRecipe(GTMaterialGen.getModMetaItem("appliedenergistics2", "crystal_seed", 600, 1), 0,GTCXTileElectrolyzer.totalEu(327680), new ItemStack[]{GTMaterialGen.getModMetaItem("appliedenergistics2", "material", 11, 1)});
            GTCXTileElectrolyzer.addRecipe(GTMaterialGen.getModMetaItem("appliedenergistics2", "crystal_seed", 1200, 1), 0,GTCXTileElectrolyzer.totalEu(327680), new ItemStack[]{GTMaterialGen.getModMetaItem("appliedenergistics2", "material", 12, 1)});
            GTCXTileMultiIndustrialGrinder.addWaterRecipe("oreCertusQuartz", 1, GTCXTileMultiIndustrialGrinder.totalEu(12800), GTMaterialGen.getModMetaItem("appliedenergistics2", "material", 0, 4), GTMaterialGen.getModMetaItem("appliedenergistics2", "material", 3, 1));
        }
    }

    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

}
