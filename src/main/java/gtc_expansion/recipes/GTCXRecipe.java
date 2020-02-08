package gtc_expansion.recipes;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXItems;
import gtc_expansion.item.tools.GTCXToolGen;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.GTCXTileAlloySmelter;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileChemicalReactor;
import gtc_expansion.tile.GTCXTileDieselGenerator;
import gtc_expansion.tile.GTCXTileElectrolyzer;
import gtc_expansion.tile.GTCXTileFluidCaster;
import gtc_expansion.tile.GTCXTileFluidSmelter;
import gtc_expansion.tile.GTCXTileGasTurbine;
import gtc_expansion.tile.GTCXTileMicrowave;
import gtc_expansion.tile.GTCXTilePlateBender;
import gtc_expansion.tile.GTCXTileWiremill;
import gtc_expansion.tile.multi.GTCXTileMultiDistillationTower;
import gtc_expansion.tile.multi.GTCXTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTCXTileMultiPrimitiveBlastFurnace;
import gtc_expansion.tile.multi.GTCXTileMultiVacuumFreezer;
import gtclassic.GTMod;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeCraftingHandler;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTConfig;
import gtclassic.common.GTItems;
import gtclassic.common.tile.GTTileUUMAssembler;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.item.recipe.upgrades.EnchantmentModifier;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class GTCXRecipe {
    static GTCXRecipe instance = new GTCXRecipe();
    static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;

    static String ingotRefinedIron = IC2.getRefinedIron();
    static IRecipeInput ingotMachine = new RecipeInputCombined(1, input(ingotRefinedIron), input("ingotAluminium"), input("ingotAluminum"));
    public static IRecipeInput plateMachine = new RecipeInputCombined(1, input(getRefinedIronPlate()), input("plateAluminium"), input("plateAluminum"));
    static IRecipeInput ingotSteels = new RecipeInputCombined(1, input("ingotSteel"), input("ingotStainlessSteel"));
    public static IRecipeInput plateSteels = new RecipeInputCombined(1, input("plateSteel"), input("plateStainlessSteel"));
    static IRecipeInput rodSteels = new RecipeInputCombined(1, input("rodSteel"), input("rodStainlessSteel"));
    static IRecipeInput ingotSteelsAluminium = new RecipeInputCombined(1, input("ingotSteel"), input("ingotStainlessSteel"), input("ingotAluminium"), input("ingotAluminum"));
    public static IRecipeInput plateSteelsAluminium = new RecipeInputCombined(1, input("plateSteel"), input("plateStainlessSteel"), input("plateAluminium"), input("plateAluminum"));
    static IRecipeInput ingotBrassBronze = new RecipeInputCombined(1, input("ingotBronze"), input("ingotBrass"));
    static IRecipeInput plateBrassBronze = new RecipeInputCombined(1, input("plateBronze"), input("plateBrass"));
    static IRecipeInput materialBrassBronze = GTCXConfiguration.general.usePlates ? plateBrassBronze : ingotBrassBronze;
    static IRecipeInput ingotTinZinc = new RecipeInputCombined(1, input("ingotZinc"), input("ingotTin"));
    static IRecipeInput plateTinZinc = new RecipeInputCombined(1, input("plateZinc"), input("plateTin"));
    static IRecipeInput materialTinZinc = GTCXConfiguration.general.usePlates ? plateTinZinc : ingotTinZinc;
    static IRecipeInput ingotInvarAluminium = new RecipeInputCombined(1, input("ingotInvar"), input("ingotAluminium"));
    static IRecipeInput plateInvarAluminium = new RecipeInputCombined(1, input("plateInvar"), input("plateAluminium"));
    static IRecipeInput materialInvarAluminium = GTCXConfiguration.general.usePlates ? plateInvarAluminium : ingotInvarAluminium;
    static IRecipeInput ingotMixedMetal1 = new RecipeInputCombined(1, input("ingotAluminium"), input("ingotSilver"), input("ingotElectrum"));
    static IRecipeInput plateMixedMetal1 = new RecipeInputCombined(1, input("plateAluminium"), input("plateSilver"), input("plateElectrum"));
    static IRecipeInput materialMixedMetal1 = GTCXConfiguration.general.usePlates ? plateMixedMetal1 : ingotMixedMetal1;
    static IRecipeInput ingotMixedMetal2 = new RecipeInputCombined(1, input("ingotTungsten"), input("ingotTitanium"));
    static IRecipeInput plateMixedMetal2 = new RecipeInputCombined(1, input("plateTungsten"), input("plateTitanium"));
    static IRecipeInput materialMixedMetal2 = GTCXConfiguration.general.usePlates ? plateMixedMetal2 : ingotMixedMetal2;
    static String materialRefinedIron = GTCXConfiguration.general.usePlates ? getRefinedIronPlate() : ingotRefinedIron;
    public static IRecipeInput materialMachine = GTCXConfiguration.general.usePlates ? plateMachine : ingotMachine;
    static IRecipeInput materialSteels = GTCXConfiguration.general.usePlates ? plateSteels : ingotSteels;
    static IRecipeInput materialSteelsAluminium = GTCXConfiguration.general.usePlates ? plateSteelsAluminium : ingotSteelsAluminium;
    static IRecipeInput anyPiston = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(Blocks.STICKY_PISTON)), new RecipeInputItemStack(GTMaterialGen.get(Blocks.PISTON)));

    static IRecipeInput plateElectric = new RecipeInputCombined(1, input(getRefinedIronPlate()), input("plateSilicon"),
            input("plateAluminium"), input("plateSilver"),
            input("plateElectrum"), input("platePlatinum"));
    public static IRecipeInput anyLapis = new RecipeInputCombined(1, input("gemLapis"),
            input("dustLazurite"), input("dustSodalite"));
    static IRecipeInput reinforcedGlass = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.reinforcedGlass), new RecipeInputItemStack(Ic2Items.reinforcedGlassClear));
    static IRecipeInput grinder = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(GTCXItems.diamondGrinder)), new RecipeInputItemStack(GTMaterialGen.get(GTCXItems.wolframiumGrinder)));
    static IRecipeInput tier2Energy = new RecipeInputCombined(1, new RecipeInputItemStack(Ic2Items.energyCrystal), new RecipeInputItemStack(GTMaterialGen.get(GTItems.lithiumBattery)));
    static IRecipeInput ingotDigital = new RecipeInputCombined(1, input("ingotChrome"), input("ingotTitanium"), input("ingotPlatinum"));
    static IRecipeInput plateDigital = new RecipeInputCombined(1, input("plateChrome"), input("plateTitanium"), input("platePlatinum"));
    static IRecipeInput materialDigital = GTCXConfiguration.general.usePlates ? plateDigital : ingotDigital;

    static String steel = GTCXConfiguration.general.usePlates ? "plateSteel" : "ingotSteel";
    static String stainlessSteel = GTCXConfiguration.general.usePlates ? "plateStainlessSteel" : "ingotStainlessSteel";
    static String tungsten = GTCXConfiguration.general.usePlates ? "plateTungsten" : "ingotTungsten";
    static String tungstenSteel = GTCXConfiguration.general.usePlates ? "plateTungstensteel" : "ingotTungstensteel";
    static IRecipeInput aluminium = GTCXConfiguration.general.usePlates ? new RecipeInputCombined(1, input("plateAluminium"), input("plateAluminum")) : new RecipeInputCombined(1, input("ingotAluminium"), input("ingotAluminum")) ;
    static String titanium = GTCXConfiguration.general.usePlates ? "plateTitanium" : "ingotTitanium";
    static String platinum = GTCXConfiguration.general.usePlates ? "platePlatinum" : "ingotPlatinum";
    static String electrum = GTCXConfiguration.general.usePlates ? "plateElectrum" : "ingotElectrum";
    static String chrome = GTCXConfiguration.general.usePlates ? "plateChrome" : "ingotChrome";
    static String tin = GTCXConfiguration.general.usePlates ? "plateTin" : "ingotTin";
    static String copper = GTCXConfiguration.general.usePlates ? "plateCopper" : "ingotCopper";
    static String bronze = GTCXConfiguration.general.usePlates ? "plateBronze" : "ingotBronze";
    static String brass = GTCXConfiguration.general.usePlates ? "plateBrass" : "ingotBrass";
    static String refinedIron = GTCXConfiguration.general.usePlates ? "plateRefinedIron" : "ingotRefinedIron";
    static String lead = GTCXConfiguration.general.usePlates ? "plateLead" : "ingotLead";

    static ICraftingRecipeList.IRecipeModifier colorTransfer(ItemStack input){
        return new ICraftingRecipeList.IRecipeModifier() {

            String id = "color";
            boolean tag = false;
            int color;

            @Override
            public void clear() {
                tag = false;
            }

            @Override
            public boolean isStackValid(ItemStack provided) {
                if (StackUtil.isStackEqual(input, provided)){
                    NBTTagCompound nbt = StackUtil.getNbtData(provided);
                    if (nbt.hasKey(id)){
                        color = nbt.getInteger(id);
                        tag = true;
                    }
                }

                return true;
            }

            @Override
            public ItemStack getOutput(ItemStack output, boolean forDisplay) {
                if (forDisplay) {
                    StackUtil.addToolTip(output, "Color gets transfered");
                } else {
                    if (tag){
                        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(output);
                        nbt.setInteger(id, color);
                    }
                }

                return output;
            }

            @Override
            public boolean isOutput(ItemStack possibleOutput) {
                return false;
            }
        };
    }

    public static String getRefinedIronPlate() {
        return IC2.config.getFlag("SteelRecipes") ? "plateSteel" : "plateRefinedIron";
    }

    public static void init(){
        GTCXRecipeRemove.init();
        GTCXRecipeProcessing.init();
        GTCXTileElectrolyzer.init();
        GTCXTileMultiVacuumFreezer.init();
        GTCXTileMultiIndustrialGrinder.init();
        GTCXTileMultiImplosionCompressor.init();
        GTCXTileMultiIndustrialBlastFurnace.init();
        GTCXTileMultiDistillationTower.init();
        GTCXTileAlloySmelter.init();
        GTCXTileAssemblingMachine.init();
        GTCXTileChemicalReactor.init();
        GTCXTileMultiPrimitiveBlastFurnace.init();
        GTCXTileFluidCaster.init();
        GTCXTileFluidSmelter.init();
        GTCXTilePlateBender.init();
        GTCXTileWiremill.init();
        GTCXTileDieselGenerator.init();
        GTCXTileGasTurbine.init();
        GTCXRecipeMods.init();
        initUURecipes();
        initIc2();
        initOverrideGTClassic();
        if (GTCXConfiguration.general.enableCraftingTools){
            initOVerrideVanillaRecipes();
        }
        initShapedItemRecipes();
        initShapedBlockRecipes();
        initRemainingToolRecipes();
        initShapelessRecipes();
        GTCXRecipeIterators.init();
    }

    public static void postInit(){
        GTCXRecipeProcessing.removals();
        GTCXRecipeIterators.initAutoOredictMachineRecipes();
        GTCXTileMicrowave.init();
    }

    public static void initShapedItemRecipes(){
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.computerMonitor), "IGI", "RPB", "IgI", 'I', aluminium, 'G', "dyeGreen", 'R', "dyeRed", 'P', "paneGlass", 'B', "dyeBlue", 'g', "dustGlowstone");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.conveyorModule), "GGG", "AAA", "CBC", 'G', "blockGlass", 'A', materialMachine, 'C', "circuitBasic", 'B', Ic2Items.battery.copy());
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.diamondGrinder, 2), "DSD", "SdS", "DSD", 'D', "dustDiamond", 'S', steel, 'd', "gemDiamond");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.wolframiumGrinder, 2), "TST", "SBS", "TST", 'T', tungsten, 'S', steel, 'B', "blockSteel");
//        recipes.addRecipe(GTMaterialGen.get(GTCXItems.constantanHeatingCoil), "RRR", "R R", "RRR", 'R', "rodConstantan");
//        recipes.addRecipe(GTMaterialGen.get(GTCXItems.kanthalHeatingCoil), " R ", "R R", "RRR", 'R', "rodKanthal");
//        recipes.addRecipe(GTMaterialGen.get(GTCXItems.nichromeHeatingCoil), "RRR", "RIR", " R ", 'R', "rodNichrome", 'I', "ingotNichrome");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.diamondChainsaw), " D ", "DdD", "TCT", 'D', "dustDiamond", 'd', Ic2Items.chainSaw, 'T', titanium, 'C', "circuitAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.steelJackhammer), "SBS", " C ", " s ", 'S', rodSteels, 'B', Ic2Items.battery, 'C', "circuitAdvanced", 's', ingotSteels);
        if (GTCXConfiguration.general.unfiredBricks){
            recipes.addRecipe(GTMaterialGen.get(GTCXItems.unfiredBrick, 2), "C", "C", 'C', Items.CLAY_BALL);
            recipes.addRecipe(GTMaterialGen.get(GTCXItems.unfiredFireBrick, 2), "C", "C", 'C', GTCXItems.fireClayBall);
        }
        recipes.addRecipe(GTMaterialGen.get(GTItems.lithiumBatpack), "BCB", " A ", 'B', GTCXItems.largeLithiumBattery, 'C', "circuitAdvanced", 'A', aluminium);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.batteryHull), "C", "B", "B", 'C', Ic2Items.copperCable, 'B', "plateBatteryAlloy");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.largeBatteryHull), "C C", "BBB", "BBB", 'C', Ic2Items.goldCable, 'B', "plateBatteryAlloy");
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.acidBattery), " C ", "LAL", "LAL", 'C', Ic2Items.copperCable, 'L', lead, 'A', GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 1));
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.mercuryBattery), " C ", "LAL", "LAL", 'C', Ic2Items.copperCable, 'L', lead, 'A', GTMaterialGen.getTube(GTMaterial.Mercury, 1));
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.sodiumBattery), " C ", "LAL", "LAL", 'C', Ic2Items.goldCable, 'L', aluminium, 'A', GTMaterialGen.getTube(GTMaterial.Sodium, 1));
        if (GTCXConfiguration.general.enableCraftingTools){
            recipes.addRecipe(GTMaterialGen.get(GTCXItems.mold), "HF", "SS", "SS", 'H', "craftingToolForgeHammer", 'F', "craftingToolFile", 'S', steel);
        } else {
            recipes.addRecipe(GTMaterialGen.get(GTCXItems.mold), "SS", "SS", 'S', steel);
        }
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldPlate), "WM", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldRod), "M ", " W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldCell), "W", "M", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), " W", "M ", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldWire), "M", "W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), "W ", " M", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldSmallPipe), "M  ", "  W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldMediumPipe), "M ", "  ", " W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldLargePipe), "M  ", "   ", "  W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldBlock), "M W", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
        recipes.addRecipe(GTMaterialGen.get(GTCXItems.moldGear), "W", " ", "M", 'W', "craftingToolWireCutter", 'M', GTCXItems.mold);
    }

    public static void initUURecipes(){
        if (GTConfig.general.gregtechUURecipes){
            if (!GTCXConfiguration.general.removeCraftingUURecipes){
                recipes.addRecipe(GTMaterialGen.getGem(GTCXMaterial.Olivine, 1), "UU ", "UUU", "UU ", 'U', Ic2Items.uuMatter, true);
                recipes.addRecipe(GTMaterialGen.getDust(GTCXMaterial.Zinc, 10), "   ", "U U", "U  ", 'U', Ic2Items.uuMatter, true);
                recipes.addRecipe(GTMaterialGen.getDust(GTMaterial.Nickel, 10), "U  ", "U U", "   ", 'U', Ic2Items.uuMatter, true);
                recipes.addRecipe(GTCXMaterialGen.getSmallDust(GTCXMaterial.Osmium, 1), "U U", "UUU", "U U", 'U', Ic2Items.uuMatter, true);
            }
            GTTileUUMAssembler.addUUMAssemblerValue(7, GTMaterialGen.getGem(GTCXMaterial.Olivine, 1));
            GTTileUUMAssembler.addUUMAssemblerValue(3, GTMaterialGen.getDust(GTCXMaterial.Zinc, 10));
            GTTileUUMAssembler.addUUMAssemblerValue(3, GTMaterialGen.getDust(GTMaterial.Nickel, 10));
            GTTileUUMAssembler.addUUMAssemblerValue(7, GTCXMaterialGen.getSmallDust(GTCXMaterial.Osmium, 1));
        }
        if (GTCXConfiguration.general.removeCraftingUURecipes){
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.stonebrick_-602048670");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.stone.stone_-217206169");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.glass_1510125347");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.grass_1277632969");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.oak_-1277881396");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.spruce_1770332581");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.birch_-1277881334");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.jungle_1730114793");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.acacia_569794864");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.log.big_oak_529577045");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.dirt.default_294096457");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.sponge.dry_-486870232");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.whitestone_1292746542");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.stonemoss_-71282124");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.sandstone.default_-222457682");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.redsandstone.default_1661771456");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.snow_-66319588");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.water_2131885151");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.lava_1530544253");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.oreiron_-329870047");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.oregold_-862634671");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.obsidian_-440375730");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.hellrock_-1150777333");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.netherstalkseeds_-1945039845");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.lightgem_-1357010811");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.cactus_1557334976");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.vine_-128181428");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.chorusfruit_-621965693");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.cloth.white_77283415");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.coal_-894344071");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.diamond_-839125610");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.redstone_609035693");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.netherquartz_-150174040");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.dyepowder.blue_-1943975137");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.feather_1656145116");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.leather_2089489131");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.snowball_-427886876");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.sulphur_-1833859045");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.enderpearl_2025630033");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.clay_916245029");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.blazerod_1662380818");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.prismarinecrystals_-886701636");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.prismarineshard_-2053819173");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.dyepowder.brown_1426380757");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.dyepowder.black_953740984");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.reeds_-459416326");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.flint_-1284138319");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.bone_-835878595");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.itemharz_2066627912");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.itemoreiridium_358070459");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.mycel_1348556715");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.stonebricksmooth.chiseled_1563944091");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockorecopper_-1838757257");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockoretin_1214808534");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockoresilver_1084210042");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.emerald_981588030");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.gemruby_891389810");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.gemsapphire_-674562982");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_tile.gtclassic.orebauxite_-377989822");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.dusttitanium_171358254");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.dustaluminium_575613706");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.dustplatinum_645900471");
            GTRecipeCraftingHandler.removeRecipe(GTMod.MODID, "shaped_item.gtclassic.dusttungsten_1935969503");
        }
    }

    public static void initRemainingToolRecipes(){
        String stick = "stickWood";
        recipes.addRecipe(GTCXToolGen.getPickaxe(GTMaterial.Flint), "FFF", " S ", " S ", new EnchantmentModifier(GTCXToolGen.getPickaxe(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GTCXToolGen.getAxe(GTMaterial.Flint), "FF", "FS", " S", new EnchantmentModifier(GTCXToolGen.getAxe(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GTCXToolGen.getShovel(GTMaterial.Flint), "F", "S", "S", new EnchantmentModifier(GTCXToolGen.getShovel(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        recipes.addRecipe(GTCXToolGen.getSword(GTMaterial.Flint), "F", "F", "S", new EnchantmentModifier(GTCXToolGen.getSword(GTMaterial.Flint), Enchantments.FIRE_ASPECT).setUsesInput(), 'F',
                Items.FLINT, 'S', stick);
        if (GTCXConfiguration.general.enableCraftingTools){
            recipes.addRecipe(GTCXToolGen.getFile(GTCXMaterial.Bronze), "P", "P", "S", 'P', "plateBronze", 'S', stick);
            recipes.overrideRecipe("shaped_item.itemtoolwrench_-354759652", GTCXToolGen.getWrench(GTCXMaterial.Bronze), "I I", "III", " I ", 'I', "ingotBronze");
            recipes.addRecipe(GTCXToolGen.getFile(GTCXMaterial.Iron), "P", "P", "S", 'P', "plateIron", 'S', stick);
            recipes.addRecipe(GTCXToolGen.getWrench(GTCXMaterial.Iron), "I I", "III", " I ", 'I', "ingotIron");
        }
        recipes.addRecipe(GTCXToolGen.getHammer(GTCXMaterial.Bronze), "PPP", "PPP", " S ", 'P', "ingotBronze", 'S', stick);
        recipes.addRecipe(GTCXToolGen.getHammer(GTCXMaterial.Iron), "PPP", "PPP", " S ", 'P', "ingotIron", 'S', stick);
    }

    public static void initOVerrideVanillaRecipes(){
        ((ForgeRegistry)ForgeRegistries.RECIPES).remove(new ResourceLocation("minecraft", "iron_bars"));
        ((ForgeRegistry)ForgeRegistries.RECIPES).remove(new ResourceLocation("minecraft", "hopper"));
        ((ForgeRegistry)ForgeRegistries.RECIPES).remove(new ResourceLocation("quark", "hopper"));
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.hopper_-82413824");
        recipes.addRecipe(GTMaterialGen.get(Blocks.IRON_BARS), "RRR", "RRR", " W ", 'R', "rodIron", 'W', "craftingToolWrench");
        String nickel = GTCXConfiguration.general.usePlates ? "plateNickel" : "ingotNickel";
        String silver = GTCXConfiguration.general.usePlates ? "plateSilver" : "ingotSilver";
        String iron = GTCXConfiguration.general.usePlates ? "plateIron" : "ingotIron";
        IRecipeInput material = new RecipeInputCombined(1, input(bronze), aluminium, input(electrum), input(platinum), input(nickel),  input(refinedIron), input(silver), input(iron));
        int recipeID = IC2.config.getFlag("SteelRecipes") ? -305222786 : -156474894;
        GTRecipeCraftingHandler.overrideGTRecipe("gtclassic", "shaped_tile.hopper_" + recipeID, GTMaterialGen.get(Blocks.HOPPER), "IWI", "ICI", " I ", 'I', material, 'W', "craftingToolWrench", 'C', "chestWood");
    }

    public static void initShapedBlockRecipes(){
        String invar = GTCXConfiguration.general.usePlates ? "plateInvar" : "ingotInvar";
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.assemblingMachine), "CPC", "SMS", "CSC", 'C', "circuitBasic", 'P', Blocks.PISTON, 'S', materialSteelsAluminium, 'M', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.chemicalReactor), "PMP", "CcC", "PEP", 'P', materialInvarAluminium, 'M', Ic2Items.magnetizer, 'C', "circuitAdvanced", 'c', Ic2Items.compressor, 'E', Ic2Items.extractor);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.distillationTower), "CEC", "PAP", "eEe", 'C', GTBlocks.tileCentrifuge, 'E', "circuitMaster", 'P', Ic2Items.pump.copy(), 'A', "machineBlockHighlyAdvanced", 'e', GTCXBlocks.electrolyzer);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.implosionCompressor), "AMA", "cCc", "AMA", 'A', Ic2Items.advancedAlloy.copy(), 'M', "machineBlockAdvanced", 'c', "circuitBasic", 'C', Ic2Items.compressor.copy());
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.electrolyzer), "IEI", "AeA", "IEI", 'I', materialSteelsAluminium, 'E', Ic2Items.extractor.copy(), 'A', "circuitAdvanced", 'e', Ic2Items.electrolyzer.copy());
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.vacuumFreezer), "IPI", "AGA", "IPI", 'I', stainlessSteel, 'P', Ic2Items.pump.copy(), 'A', "circuitAdvanced", 'G', reinforcedGlass);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.alloySmelter), "IHI", "CFC", "IMI", 'I', invar, 'H', GTCXItems.constantanHeatingCoil, 'C', "circuitBasic", 'F', Ic2Items.electroFurnace.copy(), 'M', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.industrialGrinder), "ECP", "GGG", "CMC", 'E', GTCXBlocks.electrolyzer, 'C', "circuitAdvanced", 'P', Ic2Items.pump.copy(), 'G', grinder, 'M', "machineBlockAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.industrialBlastFurnace), "CcC", "cMc", "FcF", 'C', "circuitBasic", 'c', GTCXItems.constantanHeatingCoil, 'M', "machineBlockAdvanced", 'F', Ic2Items.inductionFurnace);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.plateBender), "PCP", "cMc", "PCP", 'P', Blocks.PISTON, 'C', "circuitBasic", 'c', Ic2Items.compressor, 'M', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.wiremill), "PDP", "CMC", "PcP", 'P', brass, 'D', "gemDiamond", 'C', "circuitBasic", 'M', "machineBlockBasic", 'c', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.lathe), "PCP", "GMG", "PmP", 'P', materialSteels, 'C', "circuitAdvanced", 'G', "gearSteel", 'M', GTCXItems.conveyorModule, 'm', "machineBlockBasic");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.microwave), "AAA", "L M", "AAA", 'A', aluminium, 'L', lead, 'M', Ic2Items.magnetizer);
        IRecipeInput materialStainlessTitatium = new RecipeInputCombined(1, input(stainlessSteel), input(titanium));
        //IRecipeInput pipe = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.getFluidPipe(GTMaterial.Titanium, 1)), new RecipeInputItemStack(GTMaterialGen.getFluidPipe(GTCXMaterial.StainlessSteel, 1)));
        ItemStack pipe = new ItemStack(Items.BUCKET, 1);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fluidCaster), "IcI", "PMP", "ICI", 'I', materialStainlessTitatium, 'c', GTCXItems.mold, 'P', pipe, 'M', "machineBlockVeryAdvanced", 'C', "circuitElite");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fluidSmelter), "IbI", "PMP", "BCB", 'I', materialStainlessTitatium, 'c', GTCXBlocks.industrialBlastFurnace, 'P', pipe, 'M', "machineBlockVeryAdvanced", 'C', "circuitElite", 'B', Blocks.BRICK_BLOCK);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.primitiveBlastFurnace), "BBB", "BPB", "BBB", 'B', GTCXBlocks.fireBrickBlock, 'P', "plateIron");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.alloyFurnace), "CCC", "FHF", "CCC", 'C', Blocks.BRICK_BLOCK, 'F', Blocks.FURNACE, 'H', Blocks.HOPPER);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.casingStandard, 4), "III", "CBC", "III", 'I', refinedIron, 'C', "circuitBasic", 'B', "machineBlockCheap");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.casingReinforced, 4), "III", "CMC", "III", 'I', materialSteelsAluminium, 'C', "circuitAdvanced", 'M', "machineBlockAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.casingAdvanced, 4), "III", "CBC", "III", 'I', chrome, 'C', "circuitData", 'B', "machineBlockElite");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.fireBrickBlock), "BB", "BB", 'B', GTCXItems.fireBrick);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.dieselGenerator), "PPP", "P P", "CGC", 'P', materialMachine, 'C', "circuitBasic", 'G', Ic2Items.generator);
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.gasTurbine), "PCP", "WGW", "PCP", 'P', materialInvarAluminium, 'C', "circuitAdvanced", 'W', Ic2Items.windMill, 'G', reinforcedGlass);
        IRecipeInput aluiron = new RecipeInputCombined(1, input(refinedIron), aluminium);
        IRecipeInput rodAluiron = new RecipeInputCombined(1, input("rodAluminium"), input("rodAluminum"),  input("rodRefinedIron"));
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.locker), "RLR", "LCL", "PMP", colorTransfer(GTMaterialGen.get(GTBlocks.tileCabinet)), 'R', rodAluiron, 'L', Items.LEATHER, 'C', GTBlocks.tileCabinet, 'P', aluiron, 'M', "machineBlockCheap");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.electricLocker), "SLS", "SlS", "SCS", colorTransfer(GTMaterialGen.get(GTCXBlocks.locker)), 'S', materialSteels, 'L', Ic2Items.lapotronCrystal, 'l', GTCXBlocks.locker, 'C', "circuitAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.advancedWorktable), "EOE", "EWE", "ECE", colorTransfer(GTMaterialGen.get(GTBlocks.tileWorktable)), 'E', electrum, 'O', tier2Energy, 'W', GTBlocks.tileWorktable, 'C', "circuitAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GTCXBlocks.dustBin), "IHI", "IWI", "IHI", colorTransfer(GTMaterialGen.get(GTBlocks.tileWorktable)), 'I', materialRefinedIron, 'H', Blocks.HOPPER, 'W', GTBlocks.tileWorktable);
    }

    public static void initShapelessRecipes(){
        FluidStack water = new FluidStack(FluidRegistry.WATER, 1000);
        recipes.addShapelessRecipe(GTMaterialGen.get(Items.GUNPOWDER, 3), "dustCoal", "dustSulfur", "dustSaltpeter", "dustSaltpeter");
        recipes.addShapelessRecipe(GTMaterialGen.get(Items.GUNPOWDER, 2), "dustCharcoal", "dustSulfur", "dustSaltpeter", "dustSaltpeter");
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 3), Ic2Items.fertilizer, "dustSulfur", GTMaterialGen.getTube(GTMaterial.Calcium, 1));
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 2), Ic2Items.fertilizer, "dustAshes", "dustAshes", "dustAshes");
        recipes.addShapelessRecipe(GTMaterialGen.getIc2(Ic2Items.fertilizer, 2), Ic2Items.fertilizer, "dustDarkAshes");

        recipes.addShapelessRecipe(Ic2Items.reactorPlatingExplosive, Ic2Items.reactorPlating, lead);
        recipes.addShapelessRecipe(GTMaterialGen.get(GTCXItems.fireClayBall, 2), Items.CLAY_BALL, "sand", "dustFlint", water);
        IRecipeInput ashes = new RecipeInputCombined(1, input("dustAshes"), input("dustAsh"));
        recipes.addShapelessRecipe(new ItemStack(Items.IRON_INGOT, 1), "ingotRefinedIron", ashes);
        recipes.addShapelessRecipe(GTMaterialGen.get(GTCXItems.magicDye), "dyeCyan", "dyeMagenta", "dyeYellow", "dyeBlack");
    }

    public static void initIc2(){
        recipes.overrideRecipe("shaped_item.itempartiridium_1100834802", GTMaterialGen.get(GTCXItems.iridiumAlloyIngot), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'A', Ic2Items.advancedAlloy, 'D', "dustDiamond");
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FFF", "TTT", "FFF", 'F', "dustFlint", 'T', Blocks.TNT);
        //recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5), "FTF", "FTF", "FTF", 'F', "dustFlint", 'T', Blocks.TNT);
        if (IC2.config.getFlag("CraftingNuke")){
            recipes.overrideRecipe("shaped_tile.blocknuke_-814805840", Ic2Items.nuke, "UCU", "BAB", "UCU", 'U', Ic2Items.reactorReEnrichedUraniumRod, 'C', "circuitAdvanced", 'B', "blockUranium", 'A', "machineBlockAdvanced");
        }
        if (IC2.config.getFlag("SteelRecipes")){
            recipes.overrideRecipe("shaped_item.itemingotadviron_845672146", GTMaterialGen.getIngot(GTCXMaterial.Steel, 8), "M", 'M', Ic2Items.machine);
        }
        if (GTConfig.general.harderIC2Macerator) {
            recipes.overrideRecipe("shaped_tile.blockStoneMacerator_-130868445", Ic2Items.stoneMacerator.copy(), "FDF", "DPD", "FBF", 'D', "gemDiamond", 'F', Items.FLINT, 'P', Blocks.PISTON, 'B',
                    Blocks.FURNACE);
            recipes.overrideRecipe("shaped_tile.blockMacerator_127744036", Ic2Items.macerator.copy(), "III", "IMI", "ICI", 'I', GTCXRecipe.materialRefinedIron, 'M', Ic2Items.stoneMacerator.copy(), 'C',
                    "circuitAdvanced");
            recipes.overrideRecipe("shaped_tile.blockMacerator_2072794668", Ic2Items.macerator.copy(), "FDF", "DMD", "FCF", 'D', "gemDiamond", 'F', materialSteels, 'M', "machineBlockBasic", 'C',
                    "circuitAdvanced");
            recipes.addRecipe(Ic2Items.macerator.copy(), "FGF", "CMC", "FCF", 'G', grinder, 'F', materialSteels, 'M', "machineBlockBasic", 'C',
                    "circuitBasic");
            recipes.overrideRecipe("shaped_tile.blockrotary_-1598189826", Ic2Items.rotaryMacerator, "GGG", "GMG", "GAG", 'G', grinder, 'M', Ic2Items.macerator, 'A', "machineBlockAdvanced");
            GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockrotary_-1360333248");
        }
        int recipeId = IC2.config.getFlag("SteelRecipes") ? -1329500063 : 241486317;
        recipes.overrideRecipe("shaped_item.itemtoolcutter_" + recipeId, GTMaterialGen.get(GTCXItems.cutter), "R R", " R ", "I I", 'R', materialRefinedIron, 'I', "ingotIron");
        recipes.overrideRecipe("shaped_item.itemplasmacore_-1985082214", Ic2Items.plasmaCore, "CSC", "SPS", "CSC", 'C', GTBlocks.tileSuperconductorCableMAX, 'S', tungsten, 'P', Ic2Items.plasmaCell);
        recipes.overrideRecipe("shaped_item.itempesd_-912043277", Ic2Items.pesd, "CSC", "SPS", "CSC", 'C', GTItems.orbEnergy, 'S', tungsten, 'P', Ic2Items.plasmaCore);
        recipes.overrideRecipe("shaped_item.itemportableteleporter_-869928001", Ic2Items.portableTeleporter, "ADA", "ACA", "ETE", 'A', platinum, 'D', "circuitData", 'C', "circuitMaster", 'E', GTItems.orbEnergy, 'T', Ic2Items.teleporter);
        String technetium = GTCXConfiguration.general.usePlates ? "plateTechnetium" : "ingotTechnetium";
        recipes.overrideRecipe("shaped_item.quantumoverclockerupgrade_-1387578587", Ic2Items.quantumOverclockerUpgrade, "THT", "HOH", "TST", 'T', technetium, 'H', GTItems.heatStorageHelium6, 'O', Ic2Items.overClockerUpgrade, 'S', GTItems.superConductor);
        recipes.overrideRecipe("shaped_tile.blockpesu_281205134", Ic2Items.pesu, "SCS", "PPP", "SCS", 'S', tungsten, 'C', "circuitMaster", 'P', Ic2Items.pesd);
        recipes.overrideRecipe("shaped_tile.blockTransformerIV_1876908464", Ic2Items.transformerIV.copy(), "XYX", "CVB", "XYX", 'X', tungsten, 'Y', "craftingSuperconductor", 'C', "circuitMaster", 'V',
                Ic2Items.transformerEV.copy(), 'B', Ic2Items.pesd);
        recipes.addRecipe(Ic2Items.pesu, "SsS", "CTP", "SsS", 'S', tungsten, 's', GTItems.superConductor, 'C', "circuitMaster", 'T', Ic2Items.transformerEV, 'P', Ic2Items.pesd);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("Lossless", true);
        ItemStack stack = Ic2Items.precisionWrench.copy();
        stack.setTagCompound(nbt);
        recipes.overrideRecipe("shaped_item.precisionwrench_-1322002202", stack, "CRC", "SIS", "CWC", 'C', "circuitAdvanced", 'R', GTItems.rockCutter, 'S', "rodTungsten", 'I', Ic2Items.iridiumPlate, 'W', Ic2Items.electricWrench);
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.itemPlasmaCable_449044295");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.precisionwrench_-1943783685");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockiridiumstone_-48520064");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockwatergenerator_-2059790844");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockwindgenerator_1669945012");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockwindgenerator_-244136268");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shapeless_item.itemdustbronze_-364730307");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shapeled_item.itempartcarbonfibre_794316583");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockgenerator_234578637");
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_tile.blockgenerator_183901657");
        ItemStack battery = Ic2Items.battery;
        String circuit = "circuitBasic";
        String machineBlock = "machineBlockBasic";
        recipes.overrideRecipe("shaped_tile.blockgenerator_-66857461", Ic2Items.generator, "B", "M", "F", 'B', battery, 'M', machineBlock, 'F', Blocks.FURNACE);
        recipes.overrideRecipe("shaped_tile.blockreactorchamber_1490756150", Ic2Items.reactorChamber, " C ", "CMC", " C ", 'C', Ic2Items.denseCopperPlate, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockextractor_-1404085260", Ic2Items.extractor, "TMT", "TCT", 'T', Ic2Items.treeTap, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockcompressor_-1019977500", Ic2Items.compressor, "S S", "SMS", "SCS", 'S', "stone", 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockcanner_-1437776888", Ic2Items.canner, "TCT", "TMT", "TTT", 'T', tin, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockelectrolyzer_-502750552", Ic2Items.electrolyzer, "c c", "cCc", "tMt", 'c', Ic2Items.insulatedCopperCable, 't', Ic2Items.emptyCell, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockcropscanner_-1289883511", Ic2Items.cropAnalyzerBlock, " c ", "CMC", 'c', Ic2Items.cropAnalyzer, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockmagnetizer_-465205004", Ic2Items.magnetizer, "RFR", "RMR", "RFR", 'R', "dustRedstone", 'F', Ic2Items.ironFence, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpump_-527344087", Ic2Items.pump, "cCc", "cMc", "PTP", 'c', Ic2Items.emptyCell, 'P', Ic2Items.miningPipe, 'T', Ic2Items.treeTap, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockminer_-59581574", Ic2Items.miner, "CMC", " p ", " p ", 'M', machineBlock, 'C', circuit, 'p', Ic2Items.miningPipe);
        recipes.overrideRecipe("shaped_tile.blockcropmatron_1348153838", Ic2Items.cropmatron, "CcC", "sMs", "sss", 'c', "chest", 's', Ic2Items.cropStick, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blocksoundbeacon_755381740", Ic2Items.soundBeacon, "FcF", "cMc", "BcB", 'F', Ic2Items.frequencyTransmitter, 'c', copper, 'M', machineBlock, 'B', battery);
        recipes.overrideRecipe("shaped_tile.blockcroplibrary_1883857081", Ic2Items.cropLibary, "sBs", "LNO", "CMC", 's', Ic2Items.cropStick, 'B', battery, 'L', Ic2Items.luminator, 'N', Ic2Items.carbonBox, 'O', Ic2Items.obscurator, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockmachinebuffer_-989169435", Ic2Items.machineBuffer, " b ", "CTC", " M ", 'b', Ic2Items.upgradeBase, 'T', Ic2Items.toolBox, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockindustrialworktable_2049276174", Ic2Items.industrialWorktable, "HCH", "NcN", "HMH", 'H', Blocks.HOPPER, 'C', input(circuit, 4), 'N', Ic2Items.carbonBox, 'c', new ItemStack(Blocks.CRAFTING_TABLE, 28), 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blocksawmill_-1444206344", Ic2Items.sawMill, "ABA", "bMb", "bCb", 'A', Items.STONE_AXE, 'B', Ic2Items.turbineBlade, 'b', bronze, 'M', machineBlock, 'C', circuit);
        recipes.overrideRecipe("shaped_tile.blockadvmachine_1515831549", Ic2Items.advMachine, " C ", "AMA", " C ", 'C', Ic2Items.carbonPlate, 'A', Ic2Items.advancedAlloy, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockadvmachine_-1920290047", Ic2Items.advMachine, " A ", "CMC", " A ", 'C', Ic2Items.carbonPlate, 'A', Ic2Items.advancedAlloy, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blocktransformermv_-1785545281", Ic2Items.transformerMV, "C", "M", "C", 'C', Ic2Items.doubleInsulatedGoldCable, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blocktransformermv_-1775375979", Ic2Items.transformerMV, "C", "M", "C", 'C', GTMaterialGen.getIc2(Ic2Items.doubleInsulatedBronzeCable, 2), 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpersonaltrader_1344478433", Ic2Items.tradeOMat, "RRR", "cMc", 'R', "dustRedstone", 'c', "chest", 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpersonaltraderfluid_2117013984", Ic2Items.fluidOMat,"GGG", "PMP", 'G', "blockGlass", 'P', Ic2Items.pump, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpersonaltraderenergy_-949356331", Ic2Items.energyOMat,"RBR", "cMc", 'R', "dustRedstone", 'B', battery, 'c', Ic2Items.insulatedCopperCable, 'M', machineBlock);
        recipes.overrideRecipe("shaped_tile.blockpersonalchest_-1405328861", Ic2Items.personalSafe, "C", "M", "I", 'C', circuit, 'M', machineBlock, 'I', "chest");
        recipes.overrideRecipe("shaped_tile.blockpersonalenergystoragebatbox_1253794578", Ic2Items.personalEnergyStorageBatBox, "C", "M", "I", 'C', circuit, 'M', machineBlock, 'I', Ic2Items.batBox);
        recipes.overrideRecipe("shaped_tile.blockpersonalenergystoragemfe_1253218358", Ic2Items.personalEnergyStorageMFE, "C", "M", "I", 'C', circuit, 'M', machineBlock, 'I', Ic2Items.mfe);
        recipes.overrideRecipe("shaped_tile.blockpersonalenergystoragemfsu_1245973306", Ic2Items.personalEnergyStorageMFSU, "C", "M", "I", 'C', circuit, 'M', machineBlock, 'I', Ic2Items.mfsu);
        recipes.overrideRecipe("shaped_item.mufflerupgrade_-77325382", Ic2Items.mufflerUpgrade, "WWW", "WMW", "WWW", 'W', Blocks.WOOL, 'M', machineBlock);
        recipes.overrideRecipe("shaped_item.expcollectorupgrade_-1525635881", Ic2Items.expCollectorUpgrade, "DMD", "CHC", "DBD", 'D', Ic2Items.denseCopperPlate, 'M', machineBlock, 'C', "circuitAdvanced", 'H', Blocks.HOPPER, 'B', Items.GLASS_BOTTLE);
        recipes.overrideRecipe("shaped_item.sideaccessupgrade_-607927002", Ic2Items.sideAccessUpgrade, " T ", "TMT", " T ", 'T', "trapdoorWood", 'M', machineBlock);
        recipes.overrideRecipe("shaped_item.rotationdisablerupgrade_412194477", Ic2Items.rotationDissablerUpgrade, "C", "R", "M", 'C', Items.COMPASS, 'R', Blocks.REDSTONE_TORCH, 'M', machineBlock);
        recipes.overrideRecipe("shaped_item.itemarmorquantumlegs_-1246661396", Ic2Items.quantumLeggings, "MLM", "INI", "G G", 'M', machineBlock, 'L', Ic2Items.lapotronCrystal, 'I', Ic2Items.iridiumPlate, 'N', Ic2Items.nanoLeggings, 'G', "dustGlowstone");

        recipeId = IC2.config.getFlag("SteelRecipes") ? -342403874 : -1588477206;
        recipes.overrideRecipe("shaped_item.itemtoolddrill_1955483893", Ic2Items.diamondDrill, " D ", "DdD", "TCT", 'D', "dustDiamond", 'd', Ic2Items.electricDrill, 'T', titanium, 'C', "circuitAdvanced");
        recipes.overrideRecipe("shaped_item.itemtooldrill_" + recipeId, Ic2Items.electricDrill, " S ", "SCS", "SBS", 'S', materialSteels, 'C', circuit, 'B', battery);
        recipeId = IC2.config.getFlag("SteelRecipes") ? 286640886 : -824616294;
        recipes.overrideRecipe("shaped_item.itemtoolchainsaw_" + recipeId, Ic2Items.chainSaw, " SS", "SCS", "BS ", 'S', materialSteels, 'C', circuit, 'B', battery);
        recipeId = IC2.config.getFlag("SteelRecipes") ? 1723493281 : -137638623;
        recipes.overrideRecipe("shaped_item.itemtoolhoe_" + recipeId, Ic2Items.electricHoe, "SS ", " C ", " B ", 'S', materialSteels, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemtreetapelectric_-1455688385", Ic2Items.electricTreeTap, " B ", "SCS", "T  ", 'T', Ic2Items.treeTap, 'S', materialSteels, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.electricsprayer_-335930196", Ic2Items.electricCfSprayer, "sS ", "SC ", "  B", 's', Ic2Items.cfSprayer, 'S', materialSteels, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemnanosaber_644260803", Ic2Items.nanoSaber, "PI ", "PI ", "CEC", 'P', platinum, 'I', Ic2Items.iridiumPlate, 'C', Ic2Items.carbonPlate, 'E', Ic2Items.energyCrystal);
        IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools ? null : new RecipeInputItemStack(Ic2Items.wrench);
        nbt = new NBTTagCompound();
        nbt.setBoolean("losslessMode", true);
        stack = Ic2Items.electricWrench.copy();
        stack.setTagCompound(nbt);
        recipes.overrideRecipe("shaped_item.itemtoolwrenchelectric_883008511", stack, "SWS", "SCS", " B ",'S', materialSteels, 'W', wrench, 'C', circuit, 'B', battery);
        recipes.overrideRecipe("shaped_item.itemtoolmininglaser_1732214669", Ic2Items.miningLaser,"RHE", "TTC", " AA", 'R', "gemRuby", 'H', GTItems.heatStorageHelium6, 'E', tier2Energy, 'T', titanium, 'C', "circuitAdvanced", 'A', Ic2Items.advancedAlloy);
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.suBattery, 32), "C", "S", "L", 'C', Ic2Items.insulatedCopperCable, 'S', GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 1), 'L', "dustLead");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.suBattery, 32), "C", "L", "S", 'C', Ic2Items.insulatedCopperCable, 'S', GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 1), 'L', "dustLead");
        String tin = GTCXConfiguration.general.usePlates ? "plateTin" : "ingotTin";
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.battery, 2), " C ", "TST", "TLT", 'C', Ic2Items.copperCable, 'T', tin, 'S', GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 1), 'L', "dustLead");
        recipes.overrideRecipe("shaped_item.itembatre_2077392104", Ic2Items.battery, " C ", "TRT", "TRT", 'C', Ic2Items.copperCable, 'T', tin, 'R', "dustRedstone");
        recipeId = IC2.config.getFlag("SteelRecipes") ? 389795443 : -650149377;
        recipes.overrideRecipe("shaped_item.itemingotalloy_" + recipeId, GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 2), "TTT", "MMM", "BBB", 'T', materialRefinedIron, 'M', materialBrassBronze, 'B', materialTinZinc);
        if (GTCXConfiguration.general.usePlates){
            recipes.overrideRecipe("shaped_item.reactorvent_-795420664", Ic2Items.reactorVent, "PBP", "B B", "PBP", 'P', getRefinedIronPlate(), 'B', Blocks.IRON_BARS);
        }
        if (GTCXConfiguration.general.enableCraftingTools){
            recipeId = IC2.config.getFlag("SteelRecipes") ? 1913907474 : 1986006418;
            recipes.overrideRecipe("shaped_tile.blockfenceiron_" + recipeId, GTMaterialGen.getIc2(Ic2Items.ironFence, 6), "RRR", "RRR", " W ", 'R', "rodRefinedIron", 'W', "craftingToolWrench");
            recipeId = IC2.config.getFlag("SteelRecipes") ? 480320652 : 527557260;
            recipes.overrideRecipe("shaped_tile.blockmachine_" + recipeId, Ic2Items.machine, "PPP", "PWP", "PPP", 'P', getRefinedIronPlate(), 'W', "craftingToolWrench");
            recipes.overrideRecipe("shaped_item.upgradekit.mfs_1186329581", Ic2Items.mfsuUpgradeKid, "BMB", "BBB", " B ", 'B', "ingotBronze", 'M', Ic2Items.mfsu);
        } else {
            recipeId = IC2.config.getFlag("SteelRecipes") ? 1913907474 : 1986006418;
            recipes.overrideRecipe("shaped_tile.blockfenceiron_" + recipeId, GTMaterialGen.getIc2(Ic2Items.ironFence, 6), "RRR", "RRR", 'R', "rodRefinedIron");
        }
        GTRecipeCraftingHandler.removeRecipe("ic2", "shaped_item.upgradekit.mfs_-1749227982");
        if (GTCXConfiguration.general.harderCircuits){
            recipeId = IC2.config.getFlag("SteelRecipes") ? 1921363733 : 1058514721;
            recipes.overrideRecipe("shaped_item.itempartcircuit_" + recipeId, Ic2Items.electricCircuit, "CCC", "RER", "CCC", 'C', Ic2Items.copperCable, 'R', "plateRedAlloy", 'E', plateElectric);
            recipeId = IC2.config.getFlag("SteelRecipes") ? -1911001323 : 1521116961;
            recipes.overrideRecipe("shaped_item.itempartcircuit_" + recipeId, Ic2Items.electricCircuit, "CRC", "CEC", "CRC", 'C', Ic2Items.copperCable, 'R', "plateRedAlloy", 'E', plateElectric);
            recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-1948043137", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RGR", "LCL", "RGR", 'R', "plateRedAlloy", 'G', "dustGlowstone", 'C', "circuitBasic", 'L', anyLapis);
            recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-205948801", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RLR", "GCG", "RLR", 'R', "plateRedAlloy", 'G', "dustGlowstone", 'C', "circuitBasic", 'L', anyLapis);
        } else {
            IRecipeInput ingotElectric = new RecipeInputCombined(1, input(ingotRefinedIron), input("itemSilicon"),
                    input("ingotAluminium"), input("ingotSilver"),
                    input("ingotElectrum"), input("ingotPlatinum"));
            IRecipeInput ingotSilver = new RecipeInputCombined(1, input("ingotSilver"), input("ingotElectrum"));
            IRecipeInput circuitBasicX2 = input("circuitBasic", 2);
            if (GTConfig.general.addBasicCircuitRecipes) {
                recipeId = IC2.config.getFlag("SteelRecipes") ? 1921363733 : 1058514721;
                recipes.overrideRecipe("shaped_item.itemPartCircuit_"
                        + recipeId, GTMaterialGen.getIc2(Ic2Items.electricCircuit, 1), "CCC", "RIR", "CCC", 'C', Ic2Items.insulatedCopperCable.copy(), 'R', "dustRedstone", 'I', ingotElectric);
                recipeId = IC2.config.getFlag("SteelRecipes") ? -1911001323 : 1521116961;
                recipes.overrideRecipe("shaped_item.itemPartCircuit_"
                        + recipeId, GTMaterialGen.getIc2(Ic2Items.electricCircuit, 1), "CRC", "CIC", "CRC", 'C', Ic2Items.insulatedCopperCable.copy(), 'R', "dustRedstone", 'I', ingotElectric);
                recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.electricCircuit, 2), "CCC", "III", "CCC", 'C', Ic2Items.insulatedCopperCable.copy(), 'I', ingotElectric);
                recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.electricCircuit, 2), "CIC", "CIC", "CIC", 'C', Ic2Items.insulatedCopperCable.copy(), 'I', ingotElectric);
            }
            if (GTConfig.general.addAdvCircuitRecipes) {
                recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-1948043137", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RGR", "LCL", "RGR", 'R', "dustRedstone", 'G', "dustGlowstone", 'C', "circuitBasic", 'L', anyLapis);
                recipes.overrideRecipe("shaped_item.itemPartCircuitAdv_-205948801", GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 1), "RLR", "GCG", "RLR", 'R', "dustRedstone", 'G', "dustGlowstone", 'C', "circuitBasic", 'L', anyLapis);
                recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 2), "RGR", "LCL", "RGR", 'R', ingotSilver, 'G', "dustGlowstone", 'C', circuitBasicX2, 'L', anyLapis);
                recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.advancedCircuit, 2), "RLR", "GCG", "RLR", 'R', ingotSilver, 'G', "dustGlowstone", 'C', circuitBasicX2, 'L', anyLapis);
            }
        }

    }

    public static void initOverrideGTClassic(){
//        if (GTConfig.removeIC2Plasmafier){
//            recipes.overrideRecipe("shaped_tile.blockPlasmafier_679353211", GTMaterialGen.get(GEBlocks.fusionReactor, 1), "ESE", "LCL", "ESE", 'E', "circuitMaster", 'S', GTBlocks.tileSupercondensator, 'L',
//                    "batteryUltimate", 'C', GTBlocks.tileComputer);
//        } else {
//            instance.overrideGTRecipe("shaped_tile.gtclassic.fusionreactor_1659066354",GTMaterialGen.get(GEBlocks.fusionReactor, 1), "ESE", "LCL", "ESE", 'E', "circuitMaster", 'S', GTBlocks.tileSupercondensator, 'L',
//                    "batteryUltimate", 'C', GTBlocks.tileComputer);
//        }
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.dataCable, 6), "RRR", "CIC", "RRR", 'I', "plateSilicon", 'C', GTValues.CIRCUIT_DATA, 'R', "itemRubber");
        recipes.addRecipe(GTMaterialGen.get(GTItems.nodeBlank, 2), "II", "II", 'I', aluminium);
        recipes.addRecipe(GTMaterialGen.get(GTItems.nodeNetworkManager, 1), "BS", "CX", 'B', GTItems.nodeBlank, 'C', GTValues.CIRCUIT_ELITE, 'S', "plateSilicon", 'X', "paneGlass");
        recipes.addRecipe(GTMaterialGen.get(GTItems.nodeNetworkEnergizer, 1), "BS", "CX", 'B', GTItems.nodeBlank, 'C', GTValues.CIRCUIT_DATA, 'S', "plateSilicon", 'X', Ic2Items.glassFiberCable.copy());
        recipes.addRecipe(GTMaterialGen.get(GTItems.nodeItemDigitizer, 4), "BS", "CX", 'B', GTItems.nodeBlank, 'S', "plateSilicon", 'C', GTValues.CIRCUIT_DATA, 'X', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTItems.nodeFluidDigitizer, 4), "BS", "CX", 'B', GTItems.nodeBlank, 'S', "plateSilicon", 'C', GTValues.CIRCUIT_DATA, 'X', GTCXItems.pumpModule);
        recipes.addRecipe(GTMaterialGen.get(GTItems.nodeItemReconstructor, 4),"BS", "XC", 'B', GTItems.nodeBlank, 'S', "plateSilicon", 'C', GTValues.CIRCUIT_DATA, 'X', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTItems.nodeFluidReconstructor, 4),"BS", "XC", 'B', GTItems.nodeBlank, 'S', "plateSilicon", 'C', GTValues.CIRCUIT_DATA, 'X', GTCXItems.pumpModule);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileQuantumChest), "DCD", "HTH", "DdD", 'D', GTItems.orbData, 'C', GTCXItems.computerMonitor, 'H', "machineBlockElite", 'T', Ic2Items.teleporter, 'd', GTBlocks.tileDigitalChest);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileDigitalChest), "III", "SDS", "ICI", 'I', materialDigital, 'S', Items.SHULKER_SHELL, 'D', GTItems.orbData, 'C', GTBlocks.tileComputer);
        recipes.addRecipe( GTMaterialGen.get(GTBlocks.tileQuantumTank), "IBI", "CQC", "IBI", 'I', materialDigital, 'B', Items.BUCKET, 'C', "circuitMaster", 'Q', GTBlocks.tileQuantumChest);
        recipes.addRecipe( GTMaterialGen.get(GTBlocks.tileComputer), "CMO", "MAM", "OMC", 'C', "circuitMaster", 'M', GTCXItems.computerMonitor, 'O', "circuitUltimate", 'A', "machineBlockAdvanced");
        recipes.addRecipe( GTMaterialGen.get(GTBlocks.casingFusion), "CSC", "NMN", "CRC", 'C', "circuitMaster", 'S', "craftingSuperconductor", 'N', GTCXItems.nichromeHeatingCoil, 'M',
                "machineBlockElite", 'R', Ic2Items.reactorReflectorIridium);
        IRecipeInput rod = new RecipeInputCombined(1, input("rodTitanium", 1), input("rodTungstensteel", 1));
        IRecipeInput plate = new RecipeInputCombined(1, input(titanium, 1), input(tungstenSteel, 1));
        recipes.addRecipe(GTMaterialGen.get(GTItems.rockCutter), "DR ", "DT ", "DCB", new EnchantmentModifier(GTMaterialGen.get(GTItems.rockCutter), Enchantments.SILK_TOUCH).setUsesInput(), 'R', rod, 'T', plate, 'B', Ic2Items.battery, 'C', "circuitBasic", 'D', "dustDiamond");
        recipes.addRecipe(GTMaterialGen.get(GTItems.jackHammer), "TJT", " D ", 'T', "rodTungstensteel", 'J', GTCXItems.steelJackhammer, 'C', "circuitBasic", 'D', "dustDiamond");
        recipes.addRecipe(GTMaterialGen.get(GTItems.lithiumBattery), " C ", "ALA", "ALA", 'C', Ic2Items.goldCable, 'A', aluminium, 'L', "dustLithium");
        recipes.addRecipe(GTMaterialGen.get(GTItems.lithiumBatpack), "BCB", "BAB", "B B", 'B', GTItems.lithiumBattery, 'C', "circuitAdvanced", 'A', aluminium);
        recipes.addRecipe(GTMaterialGen.get(GTItems.portableScanner), "AEA", "CcC", "ABA", 'A', aluminium, 'E', Ic2Items.euReader, 'C', "circuitAdvanced", 'c', Ic2Items.cropAnalyzer, 'B', GTItems.lithiumBattery);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.casingHighlyAdvanced), "CTC", "TMT", "CTC", 'C', chrome, 'T', titanium, 'M', "machineBlockAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.casingHighlyAdvanced), "TCT", "CMC", "TCT", 'C', chrome, 'T', titanium, 'M', "machineBlockAdvanced");
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 3), "TTT", "MMM", "BBB", 'T', materialRefinedIron, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 6), "TTT", "MMM", "BBB", 'T', materialMixedMetal2, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        recipes.addRecipe(GTMaterialGen.getIc2(Ic2Items.mixedMetalIngot, 8), "TTT", "MMM", "BBB", 'T', tungstenSteel, 'M', materialBrassBronze, 'B', materialMixedMetal1);
        recipes.addRecipe(Ic2Items.reactorVent, "PBP", "B B", "PBP", 'P', aluminium, 'B', Blocks.IRON_BARS);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileTranslocator), "EWE", "CBC", "EME", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitBasic", 'B', "machineBlockCheap", 'M', GTCXItems.conveyorModule);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileBufferLarge), "EWE", "CBC", "EcE", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitAdvanced", 'B', "machineBlockCheap", 'c', "chestWood");
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileBufferFluid), "EWE", "CBC", "EbE", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitAdvanced", 'B', "machineBlockCheap", 'b', Items.BUCKET);
        IRecipeInput aluiron = new RecipeInputCombined(1, input(refinedIron), aluminium);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileCabinet), "III", "CIC", "III", 'I', aluiron, 'C', "chestWood");
        IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools ? new RecipeInputCombined(1, input("craftingToolMonkeyWrench"), input("craftingToolWrench")) : new RecipeInputOreDict("rodRefinedIron");
        rod = GTCXConfiguration.general.enableCraftingTools ? new RecipeInputOreDict("rodRefinedIron") : null;
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileDrum), "PWP", "PMP", "PRP", 'P', refinedIron, 'W', wrench, 'M', rod, 'R', "rodRefinedIron");
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileWorktable), "ICI", "III", "IcI", 'I', aluiron, 'C', "workbench", 'c', "chestWood");
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileAutocrafter), "EBE", "CcC", "EME", 'E', electrum, 'B', Ic2Items.battery, 'C', "circuitAdvanced", 'c', "workbench", 'M', "machineBlockAdvanced");
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileTranslocatorFluid), "EWE", "CBC", "EME", 'E', electrum, 'W', Ic2Items.insulatedCopperCable, 'C', "circuitBasic", 'B', "machineBlockCheap", 'M', Ic2Items.basicFluidImportUpgrade);
        ItemStack top = GTConfig.modcompat.compatTwilightForest && Loader.isModLoaded(GTValues.MOD_ID_TFOREST)
                ? GTMaterialGen.getModItem(GTValues.MOD_ID_TFOREST, "uncrafting_table")
                : GTMaterialGen.getIc2(Ic2Items.extractor);
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileDisassembler, 1), "RAR", "ECE", "RWR", 'A', top,
                'W', GTCXBlocks.assemblingMachine, 'R', materialRefinedIron, 'E', Ic2Items.insulatedCopperCable.copy(), 'C', "circuitAdvanced" );
        /** Fusion Computer **/ //since I'll be adding my own fusion in the future
        if (GTConfig.general.removeIC2Plasmafier) {
            recipes.overrideRecipe("shaped_tile.blockPlasmafier_679353211", GTMaterialGen.get(GTBlocks.tileFusionReactor, 1), "ESE", "LCL", "ESE", 'E', "circuitMaster", 'S', GTBlocks.tileSupercondensator, 'L',
                    "batteryUltimate", 'C', GTBlocks.tileComputer);
        } else {
            recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileFusionReactor, 1), "ESE", "LCL", "ESE", 'E',
                    "circuitMaster", 'S', "craftingSuperconductor", 'L', "batteryUltimate", 'C',
                    GTBlocks.tileComputer);
        }
        recipes.addRecipe(GTMaterialGen.get(GTBlocks.tileCentrifuge), "RCR", "MEM", "RCR", 'R', materialSteelsAluminium, 'C', "circuitAdvanced", 'M', "machineBlockAdvanced", 'E', Ic2Items.extractor);
    }

    public void overrideGTRecipe(String recipeId, ItemStack output, Object... input) {
        GTRecipeCraftingHandler.overrideGTRecipe("gtclassic", recipeId, output, input);
    }

    public void overrideShapelessGTRecipe(String recipeId, ItemStack output, Object... input){
        GTRecipeCraftingHandler.overrideShapelessGTRecipe("gtclassic", recipeId, output, input);
    }

    public void removeGTRecipe(String recipeId){
        GTRecipeCraftingHandler.removeRecipe("gtclassic", recipeId);
    }

    public static IRecipeInput input(String name){
        return input(name, 1);
    }

    public static IRecipeInput input(String name, int size){
        return GTTileBaseMachine.input(name, size);
    }

    public static IRecipeInput input(ItemStack stack){
        return GTTileBaseMachine.input(stack);
    }
}
