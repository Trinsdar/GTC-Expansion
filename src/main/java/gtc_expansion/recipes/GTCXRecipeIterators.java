package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXPipes;
import gtc_expansion.data.GTCXValues;
import gtc_expansion.item.tools.GTCXToolGen;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileDustbin;
import gtc_expansion.tile.GTCXTileExtruder;
import gtc_expansion.tile.GTCXTileFluidCaster;
import gtc_expansion.tile.GTCXTileFluidSmelter;
import gtc_expansion.tile.GTCXTileLathe;
import gtc_expansion.tile.GTCXTileMicrowave;
import gtc_expansion.tile.GTCXTilePlateBender;
import gtc_expansion.tile.GTCXTilePlateCutter;
import gtc_expansion.tile.steam.GTCXTileSteamForgeHammer;
import gtc_expansion.util.GTCXHelperPipe;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialFlag;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeCraftingHandler;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTConfig;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.machine.low.TileEntityCompressor;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gtc_expansion.recipes.GTCXRecipe.input;

public class GTCXRecipeIterators {
    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;
    public static final List<String> plateBenderBlacklist = new ArrayList<>();
    public static final List<String> fluidCasterBlacklist = new ArrayList<>();
    public static final List<String> latheBlacklist = new ArrayList<>();
    public static final List<String> gearBlacklist = new ArrayList<>();
    public static final List<String> metalList = new ArrayList<>();
    public static final List<String> dustBlacklist = new ArrayList<>();
    public static final List<String> tinyDustBlacklist = new ArrayList<>();

    public static void init(){
        for (GTMaterial mat : GTMaterial.values()){
            createSmallDustRecipe(mat);
            createPlateRecipe(mat);
            createRodRecipe(mat);
            createGearRecipe(mat);
            createNuggetRecipe(mat);
            createHullRecipe(mat);
            createFluidCastingRecipes(mat);
            createTurbineBlade(mat);
            createTinyDustRecipe(mat);
            createDustbinTinyDustRecipe(mat);
        }
        fluidCasterBlacklist.add(GTMaterial.Beryllium.getDisplayName());
        final ItemStack dustGlowstone = new ItemStack(Items.GLOWSTONE_DUST);
        final ItemStack dustGunpowder = new ItemStack(Items.GUNPOWDER);
        final ItemStack dustRedstone = new ItemStack(Items.REDSTONE);
        dustUtil(dustGlowstone, GTCXMaterial.Glowstone);
        dustUtil(dustGunpowder, GTCXMaterial.Gunpowder);
        dustUtil(Ic2Items.tinDust, GTCXMaterial.Tin);
        dustUtil(Ic2Items.obsidianDust, GTCXMaterial.Obsidian);
        dustUtil(Ic2Items.bronzeDust, GTCXMaterial.Bronze);
        dustUtil(Ic2Items.coalDust, GTCXMaterial.Coal);
        dustUtil(Ic2Items.silverDust, GTCXMaterial.Silver);
        dustUtil(dustRedstone, GTCXMaterial.Redstone);
        dustUtil(Ic2Items.clayDust, GTCXMaterial.Clay);
        dustUtil(Ic2Items.goldDust, GTCXMaterial.Gold);
        dustUtil(Ic2Items.copperDust, GTCXMaterial.Copper);
        dustUtil(Ic2Items.netherrackDust, GTCXMaterial.Netherrack);
        dustUtil(Ic2Items.ironDust, GTCXMaterial.Iron);
        dustUtil(Ic2Items.charcoalDust, GTCXMaterial.Charcoal);
        ingotUtil(Ic2Items.copperIngot, GTCXMaterial.Copper);
        ingotUtil(Ic2Items.tinIngot, GTCXMaterial.Tin);
        ingotUtil(Ic2Items.bronzeIngot, GTCXMaterial.Bronze);
        ingotUtil(Ic2Items.silverIngot, GTCXMaterial.Silver);
        createFullToolRecipes(GTCXMaterial.Steel, false);
        createFullToolRecipes(GTCXMaterial.TungstenSteel, false);
        createFullToolRecipes(GTMaterial.Ruby, true);
        createFullToolRecipes(GTMaterial.Sapphire, true);
        createPipeRecipes(GTCXMaterial.Bronze, true);
        createPipeRecipes(GTCXMaterial.Steel, true);
        createPipeRecipes(GTMaterial.Invar, true);
        createPipeRecipes(GTCXMaterial.StainlessSteel, true);
        createPipeRecipes(GTCXMaterial.TungstenSteel, true);
    }

    public static void createFluidCastingRecipes(GTMaterial mat){
        if (mat.hasFlag(GTCXMaterial.molten)){
            String orename = mat.getDisplayName();
            fluidCasterBlacklist.add(orename);
            int heat = GTCXMaterialGen.getMaterialHeatValue(mat);
            if (GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Copper) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Tin) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Iron) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Gold) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.RefinedIron) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Silver) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Bronze)){
                GTCXTileFluidSmelter.addRecipe("ingot" + orename, 1, heat, 12800, GTMaterialGen.getFluidStack(mat, 144));
                if (!GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.RefinedIron)){
                    GTCXTileFluidSmelter.addRecipe("dust" + orename, 1, heat, 12800, GTMaterialGen.getFluidStack(mat, 144));
                }
                if (GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Iron) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Gold)){
                    GTCXTileFluidSmelter.addRecipe("nugget" + orename, 1, heat, 1600, GTMaterialGen.getFluidStack(mat, 16));
                }
            }
            if (mat.hasFlag(GTMaterialFlag.INGOT)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.getFluidStack(mat, 144), false, 12800, GTMaterialGen.getIngot(mat, 1));
                GTCXTileFluidSmelter.addRecipe("ingot" + orename, 1, heat, 12800, GTMaterialGen.getFluidStack(mat, 144));
            }
            if (mat.hasFlag(GTCXMaterial.plate)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldPlate), GTMaterialGen.getFluidStack(mat, 144), false, 12800, GTCXMaterialGen.getPlate(mat, 1));
                GTCXTileFluidSmelter.addRecipe("plate" + orename, 1, heat, 12800, GTMaterialGen.getFluidStack(mat, 144));
            }
            if (mat.hasFlag(GTCXMaterial.nugget)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldNugget), GTMaterialGen.getFluidStack(mat, 16), false, 1600, GTCXMaterialGen.getNugget(mat, 1));
                GTCXTileFluidSmelter.addRecipe("nugget" + orename, 1, heat, 1600, GTMaterialGen.getFluidStack(mat, 16));
            }
            if (mat.hasFlag(GTCXMaterial.stick)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldRod), GTMaterialGen.getFluidStack(mat, 144), false, 12800, GTCXMaterialGen.getRod(mat, 2));
                GTCXTileFluidSmelter.addRecipe("rod" + orename, 1, heat, 6400, GTMaterialGen.getFluidStack(mat, 72));
            }
            if (mat.hasFlag(GTCXMaterial.gear)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldGear), GTMaterialGen.getFluidStack(mat, 576), false, 51200, GTCXMaterialGen.getGear(mat, 1));
                GTCXTileFluidSmelter.addRecipe("gear" + orename, 1, heat, 51200, GTMaterialGen.getFluidStack(mat, 576));
            }
            if (mat.hasFlag(GTMaterialFlag.BLOCKMETAL)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldBlock), GTMaterialGen.getFluidStack(mat, 1296), false, 115200, GTMaterialGen.getMaterialBlock(mat, 1));
                GTCXTileFluidSmelter.addRecipe("block" + orename, 1, heat, 115200, GTMaterialGen.getFluidStack(mat, 1296));
            }
            if (mat.getSmeltable()){
                if (mat.hasFlag(GTMaterialFlag.DUST)){
                    GTCXTileFluidSmelter.addRecipe("dust" + orename, 1, heat, 12800, GTMaterialGen.getFluidStack(mat, 144));
                }
                if (mat.hasFlag(GTCXMaterial.smalldust)){
                    GTCXTileFluidSmelter.addRecipe("dustsmall" + orename, 1, heat, 6400, GTMaterialGen.getFluidStack(mat, 36));
                }
                if (mat.hasFlag(GTCXMaterial.tinydust) && Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) && GTConfig.modcompat.compatIc2Extras){
                    GTCXTileFluidSmelter.addRecipe("dusttiny" + orename, 1, heat, 1600, GTMaterialGen.getFluidStack(mat, 16));
                }
            }

        }
    }

    public static void createPipeRecipes(GTMaterial mat, boolean fluid){
        String plate = GTCXValues.PRE + mat.getDisplayName();
        if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
            if (fluid){
                recipes.addRecipe(GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.SMALL, 6), "PWP", "P P", "PHP", 'P', plate, 'W', "craftingToolWrench", 'H', "craftingToolForgeHammer");
            }
            recipes.addRecipe(GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.MED, 2), "PPP", "W H", "PPP", 'P', plate, 'W', "craftingToolWrench", 'H', "craftingToolForgeHammer");
            recipes.addRecipe(GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.LARGE, 1), "PHP", "P P", "PWP", 'P', plate, 'W', "craftingToolWrench", 'H', "craftingToolForgeHammer");
            recipes.addRecipe(GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.HUGE, 1), "PPP", "H W", "PPP", 'P', GTTileBaseMachine.input(plate, 2), 'W', "craftingToolWrench", 'H', "craftingToolForgeHammer");
        }
        recipes.addRecipe(GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.QUAD), "PP", "PP", 'P', GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.MED));
        if (fluid) {
            GTCXTileExtruder.addRecipe("ingot" + mat.getDisplayName(), 1, GTMaterialGen.get(GTCXItems.moldSmallPipe), 1920, GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.SMALL));
            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldSmallPipe), GTMaterialGen.getFluidStack(mat, 144), false, 12800, GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.SMALL));
        }
        GTCXTileExtruder.addRecipe("ingot" + mat.getDisplayName(), 3, GTMaterialGen.get(GTCXItems.moldMediumPipe), 5760, GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.MED));
        GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldMediumPipe), GTMaterialGen.getFluidStack(mat, 432), false, 38400, GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.MED));
        GTCXTileExtruder.addRecipe("ingot" + mat.getDisplayName(), 6, GTMaterialGen.get(GTCXItems.moldLargePipe), 11520, GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.LARGE));
        GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldLargePipe), GTMaterialGen.getFluidStack(mat, 864), false, 76800, GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.LARGE));
        GTCXTileExtruder.addRecipe("ingot" + mat.getDisplayName(), 12, GTMaterialGen.get(GTCXItems.moldHugePipe), 23040, GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.HUGE));
        GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldHugePipe), GTMaterialGen.getFluidStack(mat, 1728), false, 153600, GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.HUGE));
        GTCXTileFluidSmelter.addRecipe(GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.SMALL), GTCXMaterialGen.getMaterialHeatValue(mat), 12800, GTMaterialGen.getFluidStack(mat, 144));
        GTCXTileFluidSmelter.addRecipe(GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.MED), GTCXMaterialGen.getMaterialHeatValue(mat), 38400, GTMaterialGen.getFluidStack(mat, 432));
        GTCXTileFluidSmelter.addRecipe(GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.LARGE), GTCXMaterialGen.getMaterialHeatValue(mat), 76800, GTMaterialGen.getFluidStack(mat, 864));
        GTCXTileFluidSmelter.addRecipe(GTRecipeCraftingHandler.combineRecipeObjects(GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.HUGE), GTCXPipes.getPipe(mat, GTCXHelperPipe.GTPipeModel.QUAD)), GTCXMaterialGen.getMaterialHeatValue(mat), 153600, GTMaterialGen.getFluidStack(mat, 1728));
    }

    public static void createTinyDustRecipe(GTMaterial mat) {
        String tinyDust = "dustTiny" + mat.getDisplayName();
        String dust = "dust" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterialFlag.DUST)) {
            if (mat.hasFlag(GTCXMaterial.tinydust)) {
                // Block crafting recipe
                recipes.addRecipe(getDust(mat, 1), "XXX", "XXX", "XXX", 'X',
                        tinyDust);
                TileEntityCompressor.addRecipe(tinyDust, 9, getDust(mat), 0.0F);
                recipes.addRecipe(GTMaterialGen.getStack(mat, GTCXMaterial.tinydust, 9), "D ", 'D', dust);
            }
        }
    }

    public static void createTurbineBlade(GTMaterial mat) {
        String plate = "plate" + mat.getDisplayName();
        if ((mat.hasFlag(GTCXMaterial.plate) || mat.equals(GTMaterial.Carbon)) && mat.hasFlag(GTCXMaterial.turbineBlade)) {
            IRecipeInput plateInput = mat.equals(GTMaterial.Carbon) ? input(Ic2Items.carbonPlate) : input(plate);
            recipes.addRecipe(GTMaterialGen.getStack(mat, GTCXMaterial.turbineBlade, 1), " H ", "PPP", " F ", 'H', "craftingToolForgeHammer", 'P', plateInput, 'F', "craftingToolFile");
        }
    }

    public static void createDustbinTinyDustRecipe(GTMaterial mat) {
        if (mat.hasFlag(GTMaterialFlag.DUST)) {
            if (mat.hasFlag(GTCXMaterial.tinydust)) {
                GTCXTileDustbin.addTinyDustRecipe(mat.getDisplayName(), getDust(mat));
                tinyDustBlacklist.add(mat.getDisplayName());
            }
        }
    }

    public static void createSmallDustRecipe(GTMaterial mat) {
        String smallDust = "dustSmall" + mat.getDisplayName();
        String dust = "dust" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterialFlag.DUST)) {
            if (mat.hasFlag(GTCXMaterial.smalldust)) {
                // Block crafting recipe
                recipes.addShapelessRecipe(getDust(mat, 1),
                        smallDust, smallDust, smallDust, smallDust);
                TileEntityCompressor.addRecipe(smallDust, 4, getDust(mat), 0.0F);
                // Inverse
                recipes.addRecipe(GTMaterialGen.getStack(mat, GTCXMaterial.smalldust, 4), " D", 'D', dust);
                GTCXTileDustbin.addSmallDustRecipe(mat.getDisplayName(), mat);
                dustBlacklist.add(mat.getDisplayName());
            }
        }
    }

    public static void createNuggetRecipe(GTMaterial mat) {
        if (!mat.hasFlag(GTMaterialFlag.INGOT) || !mat.hasFlag(GTCXMaterial.nugget)) {
            return;
        }

        String ingot = "ingot" + mat.getDisplayName();
        String nugget = "nugget" + mat.getDisplayName();

        recipes.addShapelessRecipe(GTCXMaterialGen.getNugget(mat, 9), ingot);
        recipes.addRecipe(GTMaterialGen.getIngot(mat, 1), "XXX", "XXX", "XXX", 'X', nugget);
    }

    public static void createPlateRecipe(GTMaterial mat) {
        String gt = mat.equals(GTCXMaterial.Osmium) ? "GT" : "";
        String ingot = "ingot" + mat.getDisplayName() + gt;
        String plate = "plate" + mat.getDisplayName() + gt;
        String block = "block" + mat.getDisplayName() + gt;
        if (mat.hasFlag(GTCXMaterial.plate)) {
            // Plate crafting recipe
            if (GTCXConfiguration.general.harderPlates) {
                recipes.addRecipe(GTCXMaterialGen.getPlate(mat, 1), "H", "X", "X", 'H',
                        "craftingToolForgeHammer", 'X', ingot);
                GTCXTileSteamForgeHammer.addRecipe(ingot, 3, 100, GTCXMaterialGen.getPlate(mat, 2));
            } else {
                recipes.addRecipe(GTCXMaterialGen.getPlate(mat, 1), "H", "X", 'H',
                        "craftingToolForgeHammer", 'X', ingot);
                GTCXTileSteamForgeHammer.addRecipe(ingot, 1, 100, GTCXMaterialGen.getPlate(mat, 1));
            }
            plateBenderBlacklist.add(ingot);
            GTCXTilePlateBender.addRecipe(ingot, 1, GTCXMaterialGen.getPlate(mat, 1));
            GTCXTileExtruder.addRecipe(ingot, 1, GTMaterialGen.get(GTCXItems.moldPlate), 1920, GTCXMaterialGen.getPlate(mat, 1));
            // If a dust is present create a maceration recipe
            if (mat.hasFlag(GTCXMaterial.smalldust)) {
                TileEntityMacerator.addRecipe(plate, 1, getDust(mat), 0.0F);
            }
            if (OreDictionary.doesOreNameExist(block)){
                GTCXTilePlateCutter.addRecipe(block, 1, GTCXMaterialGen.getPlate(mat, 9));
            }
        }
    }

    public static void createRodRecipe(GTMaterial mat) {
        String gt = mat.equals(GTCXMaterial.Osmium) ? "GT" : "";
        String ingot = "ingot" + mat.getDisplayName() + gt;
        String rod = "rod" + mat.getDisplayName() + gt;
        if (mat.hasFlag(GTCXMaterial.stick)) {
            // Rod crafting recipe
            if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
                recipes.addRecipe(GTCXMaterialGen.getRod(mat, 2), "XF", 'F',
                        "craftingToolFile", 'X', ingot);
            } else {
                recipes.addRecipe(GTCXMaterialGen.getRod(mat, 4), "X", "X", 'X', ingot);
            }
            latheBlacklist.add(ingot);
            GTCXTileLathe.addRecipe(ingot, 1, GTCXMaterialGen.getRod(mat, 2));
            GTCXTileExtruder.addRecipe(ingot, 1, GTMaterialGen.get(GTCXItems.moldRod), 1920, GTCXMaterialGen.getRod(mat, 2));
            // If a dust is present create a maceration recipe
            if (mat.hasFlag(GTCXMaterial.smalldust)) {
                TileEntityMacerator.addRecipe(rod, 2, getDust(mat), 0.0F);
            }
        }
    }

    public static void createGearRecipe(GTMaterial mat) {
        String gt = mat.equals(GTCXMaterial.Osmium) ? "GT" : "";
        String ingot = "ingot" + mat.getDisplayName() + gt;
        String plate = "plate" + mat.getDisplayName() + gt;
        String gear = "gear" + mat.getDisplayName() + gt;
        String rod = "rod" + mat.getDisplayName() + gt;
        if (mat.hasFlag(GTCXMaterial.gear)) {
            // Rod crafting recipe
            IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? new RecipeInputOreDict("craftingToolWrench") : null;
            recipes.addRecipe(GTCXMaterialGen.getGear(mat, 1), "RIR", "IWI", "RIR", 'R', rod,
                    'W', wrench, 'I', plate);
            gearBlacklist.add(ingot);
            GTCXTileExtruder.addRecipe(ingot, 4, GTMaterialGen.get(GTCXItems.moldGear), 7680, GTCXMaterialGen.getGear(mat, 1));
            // If a dust is present create a maceration recipe
            if (mat.hasFlag(GTCXMaterial.smalldust)) {
                TileEntityMacerator.addRecipe(gear, 1, getDust(mat, 4), 0.0F);
            }
        }
    }

    public static void createHullRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        String plate = "plate" + mat.getDisplayName();
        boolean refinedIron = true;
        if (mat.equals(GTCXMaterial.RefinedIron)){
            refinedIron = (!GTCXConfiguration.general.forceSteelCasings || GTCXConfiguration.general.gt2Mode) && GTCXValues.STEEL_MODE;
        }
        if (mat.equals(GTCXMaterial.Steel) && !GTCXConfiguration.general.forceSteelCasings || GTCXConfiguration.general.gt2Mode){
            refinedIron = !GTCXValues.STEEL_MODE;
        }
        if (mat.hasFlag(GTCXMaterial.hull) && mat.hasFlag(GTCXMaterial.plate) && refinedIron) {
            // Hull crafting recipe
            IRecipeInput wrench = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? new RecipeInputOreDict("craftingToolWrench") : null;
            String material = GTCXConfiguration.general.usePlates ? plate : ingot;
            recipes.addRecipe(GTCXMaterialGen.getHull(mat, 1), "PPP", "PWP", "PPP", 'P', material, 'W', wrench);
            //Ingots from hulls
            ItemStack ingotStack = mat.equals(GTCXMaterial.Bronze) ? GTMaterialGen.getIc2(Ic2Items.bronzeIngot, 6) : GTMaterialGen.getIngot(mat, 6);
            GameRegistry.addSmelting(GTCXMaterialGen.getHull(mat, 1), ingotStack,0F);
            //Cheaper recipes in assembler
            GTCXTileAssemblingMachine.addRecipe(plate, 6, GTMaterialGen.get(GTCXItems.machineParts), 3200, GTCXMaterialGen.getHull(mat, 1));
        }
    }

    public static void createFullToolRecipes(GTMaterial mat, boolean gemInput){
        String ingot = "ingot" + mat.getDisplayName();
        String plate = "plate" + mat.getDisplayName();
        String gem = "gem" + mat.getDisplayName();
        if (gemInput){
            ingot = gem;
            plate = gem;
        }
        String stick = mat.equals(GTCXMaterial.TungstenSteel) ? "rodSteel" : mat.equals(GTCXMaterial.Steel) ? "rodIron" : "stickWood";
        if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
            recipes.addRecipe(GTCXToolGen.getPickaxe(mat), "PII", "FSH", " S ", 'P', plate, 'I', ingot, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
            recipes.addRecipe(GTCXToolGen.getAxe(mat), "PIH", "PS ", "FS ", 'P', plate, 'I', ingot, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
            recipes.addRecipe(GTCXToolGen.getShovel(mat), "FPH", " S ", " S ", 'P', plate, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
            recipes.addRecipe(GTCXToolGen.getSword(mat), " P ", "FPH", " S ", 'P', plate, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
        } else {
            recipes.addRecipe(GTCXToolGen.getPickaxe(mat), "PII", " S ", " S ", 'P', plate, 'I', ingot, 'S', stick);
            recipes.addRecipe(GTCXToolGen.getAxe(mat), "PI", "PS", " S", 'P', plate, 'I', ingot, 'S', stick);
            recipes.addRecipe(GTCXToolGen.getShovel(mat), "P", "S", "S", 'P', plate, 'S', stick);
            recipes.addRecipe(GTCXToolGen.getSword(mat), "P", "P", "S", 'P', plate, 'S', stick);
        }
        if (!gemInput){
            if (GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode){
                recipes.addRecipe(GTCXToolGen.getFile(mat), "P", "P", "S", 'P', plate, 'S', stick);
                recipes.addRecipe(GTCXToolGen.getSaw(mat), "SSS", "PPS", "FH ", 'S', stick, 'P', plate, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer");
            }
            IRecipeInput hammer = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? input("craftingToolForgeHammer") : null;
            recipes.addRecipe(GTCXToolGen.getWrench(mat), "IHI", "III", " I ", 'I', plate, 'H', hammer);
            recipes.addRecipe(GTCXToolGen.getHammer(mat), "III", "III", " S ", 'I', ingot, 'S', stick);
            recipes.addRecipe(GTCXToolGen.getCrowbar(mat), " BR", "BRB", "RB ", 'B', "dyeBlue", 'R', "rod" + mat.getDisplayName());
            recipes.addRecipe(GTCXToolGen.getScrewdriver(mat), "R  ", " R ", "  S", 'R', "rod" + mat.getDisplayName(), 'S', stick);
            IRecipeInput file = GTCXConfiguration.general.enableCraftingTools && !GTCXConfiguration.general.gt2Mode ? input("craftingToolFile") : null;
            if (Loader.isModLoaded(GTValues.MOD_ID_FORESTRY)){
                recipes.addRecipe(GTCXToolGen.getBranchCutter(mat), "PFP", "P P", "RRR", 'P', plate, 'F', file, 'R', "rod" + mat.getDisplayName());
            }
        }
    }

    public static void dustUtil(ItemStack stack, GTMaterial material) {
        String smalldust = "dustSmall" + material.getDisplayName();
        String dust = "dust" + material.getDisplayName();
        recipes.addShapelessRecipe(stack, smalldust, smalldust, smalldust, smalldust);
        recipes.addRecipe(GTCXMaterialGen.getSmallDust(material, 4)," D", 'D', dust);
        TileEntityCompressor.addRecipe(smalldust, 4, GTMaterialGen.getIc2(stack, 1), 0.0F);
        GTCXTileDustbin.addSmallDustRecipe(material.getDisplayName(), stack);
    }

    public static void ingotUtil(ItemStack stack, GTMaterial material) {
        String nugget = "nugget" + material.getDisplayName();
        recipes.addRecipe(stack, "XXX", "XXX", "XXX", 'X', nugget);
    }

    public static ItemStack getDust(GTMaterial mat){
        return getDust(mat, 1);
    }

    public static ItemStack getDust(GTMaterial mat, int count){
        if (mat.equals(GTCXMaterial.Bronze)){
            return GTMaterialGen.getIc2(Ic2Items.bronzeDust, count);
        }
        if (mat.equals(GTCXMaterial.Silver)){
            return GTMaterialGen.getIc2(Ic2Items.silverDust, count);
        }
        if (mat.equals(GTCXMaterial.Gold)){
            return GTMaterialGen.getIc2(Ic2Items.goldDust, count);
        }
        if (mat.equals(GTCXMaterial.Copper)){
            return GTMaterialGen.getIc2(Ic2Items.copperDust, count);
        }
        if (mat.equals(GTCXMaterial.Tin)){
            return GTMaterialGen.getIc2(Ic2Items.tinDust, count);
        }
        if (mat.equals(GTCXMaterial.Iron) || mat.equals(GTCXMaterial.RefinedIron)){
            return GTMaterialGen.getIc2(Ic2Items.ironDust, count);
        }
        return GTMaterialGen.getDust(mat, count);
    }

    public static void initAutoOredictMachineRecipes(){
        Set<String> gemBlacklist = new HashSet();
        gemBlacklist.addAll(Arrays.asList("ingotDiamond", "ingotEmerald", "ingotQuartz", "ingotIridium", "ingotCoal", "ingotRedstone", "ingotIridiumAlloy", "ingotOsmium", "ingotWroughtIron"));
        String[] oreNames = OreDictionary.getOreNames();
        int length = oreNames.length;

        for(int i = 0; i < length; ++i) {
            String id = oreNames[i];
            NonNullList<ItemStack> listPlates;
            NonNullList<ItemStack> listIngots;
            NonNullList<ItemStack> listGears;
            NonNullList<ItemStack> listRods;
            NonNullList<ItemStack> listBlocks;
            NonNullList<ItemStack> listNuggets;
            NonNullList<ItemStack> listDusts;
            if (id.startsWith("ingot")){
                if (GTCXConfiguration.general.ingotsRequireBlastFurnace){
                    if (or(id, "ingotSteel", "ingotAluminium", "ingotAluminum", "ingotSilicon", "ingotThorium", "ingotIridium", "ingotOsmium", "ingotStainlessSteel", "ingotTungsten", "ingotChrome", "ingotTitanium")){
                        for (ItemStack stack : OreDictionary.getOres(id, false)){
                            GTHelperStack.removeSmelting(stack);
                        }
                    }
                }
                String oreName = id.substring(5);
                boolean moltenExist = FluidRegistry.isFluidRegistered(oreName.toLowerCase());
                String plate = "plate" + oreName;
                String gear = "gear" + oreName;
                String rod = "rod" + oreName;
                if (!gemBlacklist.contains(id)) {
                    if (!plateBenderBlacklist.contains(id) && OreDictionary.doesOreNameExist(plate)) {
                        listPlates = OreDictionary.getOres(plate, false);
                        if (!listPlates.isEmpty()) {
                            GTCXTilePlateBender.addRecipe(id, 1, listPlates.get(0));
                            GTCXTileExtruder.addRecipe(id, 1, GTMaterialGen.get(GTCXItems.moldPlate), 1920, listPlates.get(0));
                            if (!Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS)) {
                                if (GTCXConfiguration.general.harderPlates) {
                                    recipes.addRecipe(listPlates.get(0), "H", "I", "I", 'H', "craftingToolForgeHammer", 'I', id);
                                    GTCXTileSteamForgeHammer.addRecipe(id, 3, 100, GTMaterialGen.getIc2(listPlates.get(0), 2));
                                } else {
                                    recipes.addRecipe(listPlates.get(0), "H", "I", 'H', "craftingToolForgeHammer", 'I', id);
                                    GTCXTileSteamForgeHammer.addRecipe(id, 1, 100, listPlates.get(0));
                                }
                            }
                        }
                    }
                    if (!latheBlacklist.contains(id) && OreDictionary.doesOreNameExist(rod)){
                        listRods = OreDictionary.getOres(rod, false);
                        if (!listRods.isEmpty()){
                            GTCXTileLathe.addRecipe(id, 1, GTMaterialGen.getIc2(listRods.get(0), 2));
                            GTCXTileExtruder.addRecipe(id, 1, GTMaterialGen.get(GTCXItems.moldRod), 1920, GTMaterialGen.getIc2(listRods.get(0), 2));
                        }
                    }
                    if (!gemBlacklist.contains(id) && OreDictionary.doesOreNameExist(gear)){
                        listGears = OreDictionary.getOres(gear, false);
                        if (!listGears.isEmpty()){
                            GTCXTileExtruder.addRecipe(id, 4, GTMaterialGen.get(GTCXItems.moldGear), 7680, listGears.get(0));
                        }
                    }
                }
                if (moltenExist && !fluidCasterBlacklist.contains(oreName)){
                    Fluid fluid = FluidRegistry.getFluid(oreName.toLowerCase());
                    String block = "block" + oreName;
                    String nugget = "nugget" + oreName;
                    listIngots = OreDictionary.getOres(id, false);
                    if (!listIngots.isEmpty() && !gemBlacklist.contains(id)){
                        GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), new FluidStack(fluid, 144),false, 12800, listIngots.get(0));
                    }
                    if (OreDictionary.doesOreNameExist(nugget)) {
                        listNuggets = OreDictionary.getOres(nugget, false);
                        if (!listNuggets.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldNugget), new FluidStack(fluid, 16),false, 3200, listNuggets.get(0));
                        }
                    }
                    if (OreDictionary.doesOreNameExist(block)) {
                        listBlocks = OreDictionary.getOres(block, false);
                        if (!listBlocks.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldBlock), new FluidStack(fluid, 1296),false, 115200, listBlocks.get(0));
                        }
                    }
                    if (OreDictionary.doesOreNameExist(plate)) {
                        listPlates = OreDictionary.getOres(plate, false);
                        if (!listPlates.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldPlate), new FluidStack(fluid, 144),false, 12800, listPlates.get(0));
                        }
                    }
                    if (OreDictionary.doesOreNameExist(gear)) {
                        listGears = OreDictionary.getOres(gear, false);
                        if (!listGears.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldGear), new FluidStack(fluid, 576),false, 51200, listGears.get(0));
                        }
                    }
                    if (OreDictionary.doesOreNameExist(rod)) {
                        listRods = OreDictionary.getOres(rod, false);
                        if (!listRods.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldGear), new FluidStack(fluid, 144),false, 12800,  GTMaterialGen.getIc2(listRods.get(0), 2));
                        }
                    }
                }
            }
            if (id.startsWith("crushed")){
                GTCXTileMicrowave.explodeList.addAll(OreDictionary.getOres(id, false));
            }
            if (id.startsWith("dust") && !id.startsWith("dustSmall") && !id.startsWith("dustTiny")){
                String oreName = id.substring(4);
                GTCXTileMicrowave.explodeList.addAll(OreDictionary.getOres(id, false));
                String smallDust = "dustSmall" + oreName;
                String tinyDust = "dustTiny" + oreName;
                if (!dustBlacklist.contains(oreName) && OreDictionary.doesOreNameExist(smallDust)){
                    listDusts = OreDictionary.getOres(id, false);
                    if (!listDusts.isEmpty()) {
                        GTCXTileDustbin.addSmallDustRecipe(oreName, listDusts.get(0));
                    }
                }

                if (!tinyDustBlacklist.contains(oreName) && OreDictionary.doesOreNameExist(tinyDust)){
                    listDusts = OreDictionary.getOres(id, false);
                    if (!listDusts.isEmpty()) {
                        GTCXTileDustbin.addTinyDustRecipe(oreName, listDusts.get(0));
                    }
                }
            }
        }
    }

    public static boolean or(String compare, String... values){
        for (String string : values){
            if (string.equals(compare)){
                return true;
            }
        }
        return false;
    }
}
