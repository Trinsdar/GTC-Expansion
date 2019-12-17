package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXItems;
import gtc_expansion.item.tools.GTCXToolGen;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtc_expansion.tile.GTCXTileFluidCaster;
import gtc_expansion.tile.GTCXTileFluidSmelter;
import gtc_expansion.tile.GTCXTileLathe;
import gtc_expansion.tile.GTCXTilePlateBender;
import gtc_expansion.util.GTCXIc2cECompat;
import gtclassic.api.helpers.GTHelperMods;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialFlag;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTConfig;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.crafting.ICraftingRecipeList;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.block.machine.low.TileEntityCompressor;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GTCXRecipeIterators {
    public static ICraftingRecipeList recipes = ClassicRecipes.advCrafting;
    public static final List<String> plateBenderBlacklist = new ArrayList<>();
    public static final List<String> fluidCasterBlacklist = new ArrayList<>();
    public static final List<String> metalList = new ArrayList<>();

    public static void init(){
        for (GTMaterial mat : GTMaterial.values()){
            createSmallDustRecipe(mat);
            createPlateRecipe(mat);
            createRodRecipe(mat);
            createGearRecipe(mat);
            createNuggetRecipe(mat);
            createHullRecipe(mat);
            createFluidCastingRecipes(mat);
            createPipeRecipe(mat);
            if (Loader.isModLoaded(GTHelperMods.IC2_EXTRAS) && GTConfig.modcompat.compatIc2Extras){
                createTinyDustRecipe(mat);
            }
        }
        final ItemStack dustGlowstone = new ItemStack(Items.GLOWSTONE_DUST);
        final ItemStack dustGunpowder = new ItemStack(Items.GUNPOWDER);
        final ItemStack dustRedstone = new ItemStack(Items.REDSTONE);
        dustUtil(dustGlowstone, GTCXMaterial.Glowstone);
        dustUtil(dustGunpowder, GTCXMaterial.Gunpowder);
        dustUtil(Ic2Items.tinDust, GTCXMaterial.Tin);
        dustUtil(Ic2Items.obsidianDust, GTCXMaterial.Obsidian);
        dustUtil(Ic2Items.bronzeDust, GTMaterial.Bronze);
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
        ingotUtil(Ic2Items.bronzeIngot, GTMaterial.Bronze);
        ingotUtil(Ic2Items.silverIngot, GTCXMaterial.Silver);
        createFullToolRecipes(GTCXMaterial.Steel, false);
        createFullToolRecipes(GTCXMaterial.TungstenSteel, false);
        createFullToolRecipes(GTMaterial.Ruby, true);
        createFullToolRecipes(GTMaterial.Sapphire, true);
    }

    public static void createFluidCastingRecipes(GTMaterial mat){
        if (mat.hasFlag(GTCXMaterial.molten)){
            String orename = mat.getDisplayName();
            fluidCasterBlacklist.add(orename);
            int tier = mat.getTier() + 1;
            if (GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Copper) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Tin) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Iron) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Gold) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.RefinedIron) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Silver) || GTMaterialGen.isMaterialEqual(mat, GTMaterial.Bronze)){
                GTCXTileFluidSmelter.addRecipe("ingot" + orename, 1, 750 * tier, 12800, GTMaterialGen.getFluidStack(mat, 144));
                if (GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Iron) || GTMaterialGen.isMaterialEqual(mat, GTCXMaterial.Gold)){
                    GTCXTileFluidSmelter.addRecipe("nugget" + orename, 1, 750 * tier, 1600, GTMaterialGen.getFluidStack(mat, 16));
                }
            }
            if (mat.hasFlag(GTMaterialFlag.INGOT)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.getFluidStack(mat, 144), false, 12800, GTMaterialGen.getIngot(mat, 1));
                GTCXTileFluidSmelter.addRecipe("ingot" + orename, 1, 750 * tier, 12800, GTMaterialGen.getFluidStack(mat, 144));
            }
            if (mat.hasFlag(GTCXMaterial.plate)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldPlate), GTMaterialGen.getFluidStack(mat, 144), false, 12800, GTCXMaterialGen.getPlate(mat, 1));
                GTCXTileFluidSmelter.addRecipe("plate" + orename, 1, 750 * tier, 12800, GTMaterialGen.getFluidStack(mat, 144));
            }
            if (mat.hasFlag(GTCXMaterial.nugget)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldNugget), GTMaterialGen.getFluidStack(mat, 16), false, 1600, GTCXMaterialGen.getNugget(mat, 1));
                GTCXTileFluidSmelter.addRecipe("nugget" + orename, 1, 750 * tier, 1600, GTMaterialGen.getFluidStack(mat, 16));
            }
            if (mat.hasFlag(GTCXMaterial.stick)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldRod), GTMaterialGen.getFluidStack(mat, 144), false, 12800, GTCXMaterialGen.getRod(mat, 2));
                GTCXTileFluidSmelter.addRecipe("rod" + orename, 1, 750 * tier, 6400, GTMaterialGen.getFluidStack(mat, 72));
            }
            if (mat.hasFlag(GTCXMaterial.gear)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldGear), GTMaterialGen.getFluidStack(mat, 576), false, 51200, GTCXMaterialGen.getGear(mat, 1));
                GTCXTileFluidSmelter.addRecipe("gear" + orename, 1, 750 * tier, 51200, GTMaterialGen.getFluidStack(mat, 576));
            }
            if (mat.hasFlag(GTMaterialFlag.BLOCKMETAL)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldBlock), GTMaterialGen.getFluidStack(mat, 1296), false, 115200, GTMaterialGen.getMaterialBlock(mat, 1));
                GTCXTileFluidSmelter.addRecipe("block" + orename, 1, 750 * tier, 115200, GTMaterialGen.getFluidStack(mat, 1296));
            }
            if (mat.hasFlag(GTMaterialFlag.PIPEFLUID)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldSmallPipe), GTMaterialGen.getFluidStack(mat, 144), false, 12800, GTMaterialGen.getFluidPipeSmall(mat, 1));
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldMediumPipe), GTMaterialGen.getFluidStack(mat, 432), false, 38400, GTMaterialGen.getFluidPipe(mat, 1));
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldLargePipe), GTMaterialGen.getFluidStack(mat, 864), false, 76800, GTMaterialGen.getFluidPipeLarge(mat, 1));
                GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getFluidPipeSmall(mat, 1), 750 * tier, 12800, GTMaterialGen.getFluidStack(mat, 144));
                GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getFluidPipe(mat, 1), 750 * tier, 38400, GTMaterialGen.getFluidStack(mat, 432));
                GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getFluidPipeLarge(mat, 1), 750 * tier, 76800, GTMaterialGen.getFluidStack(mat, 864));
            }
            if (mat.hasFlag(GTMaterialFlag.PIPEITEM)){
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldMediumPipe), GTMaterialGen.getFluidStack(mat, 432), false, 38400, GTMaterialGen.getItemPipe(mat, 1));
                GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldLargePipe), GTMaterialGen.getFluidStack(mat, 864), false, 76800, GTMaterialGen.getItemPipeLarge(mat, 1));
                GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getItemPipe(mat, 1), 750 * tier, 38400, GTMaterialGen.getFluidStack(mat, 432));
                GTCXTileFluidSmelter.addRecipe(GTMaterialGen.getItemPipeLarge(mat, 1), 750 * tier, 76800, GTMaterialGen.getFluidStack(mat, 864));
            }
            if (mat.getSmeltable()){
                if (mat.hasFlag(GTMaterialFlag.DUST)){
                    GTCXTileFluidSmelter.addRecipe("dust" + orename, 1, 750 * tier, 12800, GTMaterialGen.getFluidStack(mat, 144));
                }
                if (mat.hasFlag(GTCXMaterial.smalldust)){
                    GTCXTileFluidSmelter.addRecipe("dustsmall" + orename, 1, 750 * tier, 6400, GTMaterialGen.getFluidStack(mat, 36));
                }
                if (mat.hasFlag(GTCXMaterial.tinydust) && Loader.isModLoaded(GTHelperMods.IC2_EXTRAS) && GTConfig.modcompat.compatIc2Extras){
                    GTCXTileFluidSmelter.addRecipe("dusttiny" + orename, 1, 750 * tier, 1600, GTMaterialGen.getFluidStack(mat, 16));
                }
            }

        }
    }

    public static void createPipeRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        String plate = "plate" + mat.getDisplayName();
        String material = GTCXConfiguration.general.usePlates ? plate : ingot;
        String hammer = "craftingToolForgeHammer";
        IRecipeInput wrench = new RecipeInputCombined(1, new RecipeInputOreDict("craftingToolMonkeyWrench"), new RecipeInputOreDict("craftingToolWrench"));
        if (mat.hasFlag(GTMaterialFlag.PIPEITEM)) {
            recipes.addRecipe(GTMaterialGen.getItemPipe(mat, 2), "III", "W H", "III", 'I', material, 'W', wrench, 'H', hammer);
            recipes.addRecipe(GTMaterialGen.getItemPipeLarge(mat, 1), "IHI", "I I", "IWI", 'I', material, 'W', wrench, 'H', hammer);
        }
        if (mat.hasFlag(GTMaterialFlag.PIPEFLUID) && mat != GTMaterial.HighPressure) {
            recipes.addRecipe(GTMaterialGen.getFluidPipeSmall(mat, 6), "IWI", "I I", "IHI", 'I', material, 'W', wrench, 'H', hammer);
            recipes.addRecipe(GTMaterialGen.getFluidPipe(mat, 2), "III", "W H", "III", 'I', material, 'W', wrench, 'H', hammer);
            recipes.addRecipe(GTMaterialGen.getFluidPipeLarge(mat, 1), "IHI", "I I", "IWI", 'I', material, 'W', wrench, 'H', hammer);
        }
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

    public static void createSmallDustRecipe(GTMaterial mat) {
        String smallDust = "dustSmall" + mat.getDisplayName();
        String dust = "dust" + mat.getDisplayName();
        if (mat.hasFlag(GTMaterialFlag.DUST)) {
            if (mat.hasFlag(GTCXMaterial.smalldust)) {
                // Block crafting recipe
                recipes.addRecipe(getDust(mat, 1), "XX", "XX", 'X',
                        smallDust);
                TileEntityCompressor.addRecipe(smallDust, 4, getDust(mat), 0.0F);
                // Inverse
                recipes.addRecipe(GTMaterialGen.getStack(mat, GTCXMaterial.smalldust, 4), " D", 'D', dust);
            }
        }
    }

    public static void createNuggetRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        if (mat.hasFlag(GTCXMaterial.nugget)) {
            recipes.addShapelessRecipe(GTCXMaterialGen.getNugget(mat, 9), ingot);
        }
    }

    public static void createPlateRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        String plate = "plate" + mat.getDisplayName();
        if (mat.hasFlag(GTCXMaterial.plate)) {
            // Plate crafting recipe
            if (GTCXConfiguration.general.harderPlates) {
                recipes.addRecipe(GTCXMaterialGen.getPlate(mat, 1), "H", "X", "X", 'H',
                        "craftingToolForgeHammer", 'X', ingot);
            } else {
                recipes.addRecipe(GTCXMaterialGen.getPlate(mat, 1), "H", "X", 'H',
                        "craftingToolForgeHammer", 'X', ingot);
            }
            if (Loader.isModLoaded("ic2c_extras")){
                // Add to auto add blacklist first
                GTCXIc2cECompat.addToRollerIngotBlacklist(mat.getDisplayName());
                if (GTConfig.modcompat.compatIc2Extras){
                    GTCXIc2cECompat.addRollerRecipe(ingot, 1, GTCXMaterialGen.getPlate(mat, 1));
                }
            }
            plateBenderBlacklist.add(ingot);
            GTCXTilePlateBender.addRecipe(ingot, 1, GTCXMaterialGen.getPlate(mat, 1));
            // If a dust is present create a maceration recipe
            if (mat.hasFlag(GTCXMaterial.smalldust)) {
                TileEntityMacerator.addRecipe(plate, 1, getDust(mat), 0.0F);
            }
        }
    }

    public static void createRodRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        String rod = "rod" + mat.getDisplayName();
        if (mat.hasFlag(GTCXMaterial.stick)) {
            // Rod crafting recipe
            recipes.addRecipe(GTCXMaterialGen.getRod(mat, 2), "XF", 'F',
                    "craftingToolFile", 'X', ingot);
            GTCXTileLathe.addRecipe(ingot, 1, GTCXMaterialGen.getRod(mat, 2));
            // If a dust is present create a maceration recipe
            if (mat.hasFlag(GTCXMaterial.smalldust)) {
                TileEntityMacerator.addRecipe(rod, 2, getDust(mat), 0.0F);
            }
        }
    }

    public static void createGearRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        String gear = "gear" + mat.getDisplayName();
        String rod = "rod" + mat.getDisplayName();
        if (mat.hasFlag(GTCXMaterial.gear)) {
            // Rod crafting recipe
            recipes.addRecipe(GTCXMaterialGen.getGear(mat, 1), "RIR", "IWI", "RIR", 'R', rod,
                    'W', "craftingToolWrench", 'I', ingot);
            // If a dust is present create a maceration recipe
            if (mat.hasFlag(GTCXMaterial.smalldust)) {
                TileEntityMacerator.addRecipe(gear, 1, getDust(mat, 6), 0.0F);
            }
        }
    }

    public static void createHullRecipe(GTMaterial mat) {
        String ingot = "ingot" + mat.getDisplayName();
        String plate = "plate" + mat.getDisplayName();
        boolean steel = false;
        boolean refinedIron = true;
        if (mat.equals(GTCXMaterial.Steel)){
            steel = IC2.config.getFlag("SteelRecipes");
        }
        if (mat.equals(GTCXMaterial.RefinedIron)){
            refinedIron = IC2.config.getFlag("SteelRecipes");
        }
        if (mat.hasFlag(GTCXMaterial.hull) && mat.hasFlag(GTCXMaterial.plate) && !steel && refinedIron) {
            // Hull crafting recipe
            if (!GTCXConfiguration.general.harderProgression){
                recipes.addRecipe(GTCXMaterialGen.getHull(mat, 1), "PPP", "P P", "PPP", 'P', ingot);
            } else {
                recipes.addRecipe(GTCXMaterialGen.getHull(mat, 1), "PPP", "PWP", "PPP", 'P', plate, 'W', "craftingToolWrench");
            }
            //Ingots from hulls
            recipes.addShapelessRecipe(GTMaterialGen.getIngot(mat, 8), GTCXMaterialGen.getHull(mat, 1));
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
        String stick = "stickWood";
        GTCXToolGen G = new GTCXToolGen();
        recipes.addRecipe(G.getPickaxe(mat), "PII", "FSH", " S ", 'P', plate, 'I', ingot, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
        recipes.addRecipe(G.getAxe(mat), "PIH", "PS ", "FS ", 'P', plate, 'I', ingot, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
        recipes.addRecipe(G.getShovel(mat), "FPH", " S ", " S ", 'P', plate, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
        recipes.addRecipe(G.getSword(mat), " P ", "FPH", " S ", 'P', plate, 'F', "craftingToolFile", 'H', "craftingToolForgeHammer", 'S', stick);
        if (!gemInput){
            recipes.addRecipe(G.getFile(mat), "P", "P", "S", 'P', plate, 'S', stick);
            recipes.addRecipe(G.getHammer(mat), "III", "III", " S ", 'I', ingot, 'S', stick);
            recipes.addRecipe(G.getWrench(mat), "I I", "III", " I ", 'I', ingot);
        }
    }

    public static void dustUtil(ItemStack stack, GTMaterial material) {
        String smalldust = "dustSmall" + material.getDisplayName();
        recipes.addShapelessRecipe(stack, smalldust, smalldust, smalldust, smalldust);
        TileEntityCompressor.addRecipe(smalldust, 4, GTMaterialGen.getIc2(stack, 1), 0.0F);
    }

    public static void ingotUtil(ItemStack stack, GTMaterial material) {
        String nugget = "nugget" + material.getDisplayName();
        recipes.addRecipe(stack, "XXX", "XXX", "XXX", 'X', nugget);
    }

    public static ItemStack getDust(GTMaterial mat){
        return getDust(mat, 1);
    }

    public static ItemStack getDust(GTMaterial mat, int count){
        if (mat.equals(GTMaterial.Bronze)){
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
        gemBlacklist.addAll(Arrays.asList("ingotDiamond", "ingotEmerald", "ingotQuartz", "ingotIridium", "ingotCoal", "ingotRedstone"));
        String[] var2 = OreDictionary.getOreNames();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String id = var2[var4];
            String plate;
            String gear;
            String rod;
            String block;
            String nugget;
            NonNullList<ItemStack> listPlates;
            NonNullList<ItemStack> listIngots;
            NonNullList<ItemStack> listGears;
            NonNullList<ItemStack> listRods;
            NonNullList<ItemStack> listBlocks;
            NonNullList<ItemStack> listNuggets;
            if (id.startsWith("ingot")){
                String oreName = id.substring(5);
                boolean moltenExist = FluidRegistry.isFluidRegistered(oreName.toLowerCase());
                plate = "plate" + oreName;
                if (!plateBenderBlacklist.contains(id) && !gemBlacklist.contains(id)){
                    if (OreDictionary.doesOreNameExist(plate)) {
                        listPlates = OreDictionary.getOres(plate, false);
                        if (!listPlates.isEmpty()) {
                            GTCXTilePlateBender.addRecipe(id, 1, listPlates.get(0));
                            if (!Loader.isModLoaded(GTHelperMods.IC2_EXTRAS)){
                                if (GTCXConfiguration.general.harderPlates){
                                    recipes.addRecipe(listPlates.get(0), "H", "I", "I", 'H', "craftingToolForgeHammer", 'I', id );
                                }else {
                                    recipes.addRecipe(listPlates.get(0), "H", "I", 'H', "craftingToolForgeHammer", 'I', id );
                                }
                            }
                        }
                    }
                }
                if (moltenExist && !fluidCasterBlacklist.contains(oreName)){
                    Fluid fluid = FluidRegistry.getFluid(oreName.toLowerCase());
                    gear = "gear" + oreName;
                    rod = "rod" + oreName;
                    block = "block" + oreName;
                    nugget = "nugget" + oreName;
                    listIngots = OreDictionary.getOres(id, false);
                    if (!listIngots.isEmpty() && !gemBlacklist.contains(id)){
                        GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), new FluidStack(fluid, 144),true, 12800, listIngots.get(0));
                    }
                    if (OreDictionary.doesOreNameExist(nugget)) {
                        listNuggets = OreDictionary.getOres(nugget, false);
                        if (!listNuggets.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldNugget), new FluidStack(fluid, 16),true, 3200, listNuggets.get(0));
                        }
                    }
                    if (OreDictionary.doesOreNameExist(block)) {
                        listBlocks = OreDictionary.getOres(block, false);
                        if (!listBlocks.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldBlock), new FluidStack(fluid, 1296),true, 115200, listBlocks.get(0));
                        }
                    }
                    if (OreDictionary.doesOreNameExist(plate)) {
                        listPlates = OreDictionary.getOres(plate, false);
                        if (!listPlates.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldPlate), new FluidStack(fluid, 144),true, 12800, listPlates.get(0));
                        }
                    }
                    if (OreDictionary.doesOreNameExist(gear)) {
                        listGears = OreDictionary.getOres(gear, false);
                        if (!listGears.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldGear), new FluidStack(fluid, 576),true, 51200, listGears.get(0));
                        }
                    }
                    if (OreDictionary.doesOreNameExist(rod)) {
                        listRods = OreDictionary.getOres(rod, false);
                        if (!listRods.isEmpty()) {
                            GTCXTileFluidCaster.addRecipe(GTMaterialGen.get(GTCXItems.moldGear), new FluidStack(fluid, 144),true, 12800,  GTMaterialGen.getIc2(listRods.get(0), 2));
                        }
                    }
                }
            }
        }
    }
}
