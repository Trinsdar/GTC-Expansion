package gtc_expansion.jei;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.item.tools.GTCXToolGen;
import gtc_expansion.jei.category.GTCXJeiBurnableFluidCategory;
import gtc_expansion.jei.category.GTCXJeiCauldronCategory;
import gtc_expansion.jei.category.GTCXJeiCustomCategory;
import gtc_expansion.jei.category.GTCXJeiIntegratedCircuitCategory;
import gtc_expansion.jei.wrapper.GTCXJeiBurnableFluidWrapper;
import gtc_expansion.jei.wrapper.GTCXJeiCasterWrapper;
import gtc_expansion.jei.wrapper.GTCXJeiFusionWrapper;
import gtc_expansion.jei.wrapper.GTCXJeiHeatWrapper;
import gtc_expansion.jei.wrapper.GTCXJeiIntegratedCircuitWrapper;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.recipes.GTCXRecipe;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.jei.GTJeiMultiRecipeCategory;
import gtclassic.api.jei.GTJeiMultiRecipeWrapper;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTConfig;
import gtclassic.common.gui.GTGuiMachine;
import gtclassic.common.tile.multi.GTTileMultiFusionReactor;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.machine.IMachineRecipeList;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Resources;
import ic2.jeiIntigration.SubModul;
import ic2.jeiIntigration.core.JeiPlugin;
import ic2.jeiIntigration.core.machine.basic.MachineRecipe;
import ic2.jeiIntigration.core.machine.basic.MachineRecipeCategory;
import ic2.jeiIntigration.core.machine.basic.MachineRecipeWrapper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import trinsdar.ic2c_extras.jei.JeiSimpleMachineCategory;
import trinsdar.ic2c_extras.jei.JeiSimpleMachineWrapper;
import trinsdar.ic2c_extras.util.Registry;

import javax.annotation.Nonnull;

@JEIPlugin
public class GTCXJeiPlugin implements IModPlugin {
    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime arg0) {
        // empty method for construction
    }
    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (SubModul.load) {
            wrapperUtil(registry, GTCXRecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST, GTCXBlocks.industrialBlastFurnace, GTCXMachineGui.GTCXIndustrialBlastFurnaceGui.class, 78, 24, 20, 18);
            wrapperUtil(registry, GTCXRecipeLists.FLUID_SMELTER_RECIPE_LIST, GTCXBlocks.fluidSmelter, GTCXMachineGui.GTCXFluidSmelterGui.class, 78, 24, 20, 18);
            wrapperUtil2(registry, GTCXRecipeLists.FLUID_CASTER_RECIPE_LIST, GTCXBlocks.fluidCaster, GTCXMachineGui.GTCXFluidCasterGui.class, 78, 24, 20, 18);
            wrapperUtil3(registry, GTCXRecipeLists.DIESEL_GEN_RECIPE_LIST, GTCXBlocks.dieselGenerator, GTCXMachineGui.GTCXDieselGeneratorGui.class, 78, 35, 16, 17);
            wrapperUtil3(registry, GTCXRecipeLists.GAS_TURBINE_RECIPE_LIST, GTCXBlocks.gasTurbine, GTCXMachineGui.GTCXGasTurbineGui.class, 78, 35, 16, 17);
            wrapperUtil4(registry, GTTileMultiFusionReactor.RECIPE_LIST, GTCXBlocks.fusionComputer, GTCXMachineGui.GTCXFusionComputerGui.class, 155, 5, 16, 16);
            registry.addRecipeCatalyst(new ItemStack(GTCXBlocks.alloyFurnace), "gt.alloysmelter");
            registry.addRecipeCatalyst(new ItemStack(GTCXBlocks.steamAlloySmelter), "gt.alloysmelter");
            registry.addRecipeClickArea(GTCXMachineGui.GTCXSteamAlloySmelterGui.class, 80, 34, 20, 18, "gt.alloysmelter");
            registry.addRecipeCatalyst(new ItemStack(GTCXBlocks.centrifuge), "gt.centrifuge");
            if (!GTCXConfiguration.general.gt2Mode){
                registry.addRecipeCatalyst(GTMaterialGen.get(GTCXBlocks.stoneCompressor), "compressor");
                registry.addRecipeCatalyst(GTMaterialGen.get(GTCXBlocks.stoneExtractor), "extractor");
                registry.addRecipeCatalyst(GTMaterialGen.get(GTCXBlocks.steamCompressor), "compressor");
                registry.addRecipeCatalyst(GTMaterialGen.get(GTCXBlocks.steamExtractor), "extractor");
                registry.addRecipeCatalyst(GTMaterialGen.get(GTCXBlocks.steamMacerator), "macerator");
                registry.addRecipeCatalyst(GTMaterialGen.get(GTCXBlocks.steamFurnace), "minecraft.smelting");
            }
            //electric furnace
            registry.handleRecipes(IMachineRecipeList.RecipeEntry.class, MachineRecipeWrapper::new, "electricFurnace");
            registry.addRecipes(ClassicRecipes.furnace.getRecipeMap(), "electricFurnace");
            registry.addRecipeCatalyst(Ic2Items.electroFurnace, "electricFurnace");
            registry.addRecipeCatalyst(Ic2Items.inductionFurnace, "electricFurnace");

            registry.handleRecipes(GTRecipeMultiInputList.MultiRecipe.class, GTJeiMultiRecipeWrapper::new, "gt.cauldron");
            registry.addRecipes(GTCXRecipeLists.CAULDRON_RECIPE_LIST.getRecipeList(), "gt.cauldron");
            registry.addRecipeCatalyst(GTMaterialGen.get(Items.CAULDRON), "gt.cauldron");

            registry.addRecipeClickArea(GTGuiMachine.GTIndustrialCentrifugeGui.class, 69, 26, 20, 18, "gt.centrifuge");
            String recipeId = "gt.integratedcircuit";
            registry.handleRecipes(GTCXJeiIntegratedCircuitWrapper.IntegratedCircuitRecipe.class, GTCXJeiIntegratedCircuitWrapper::new, recipeId);
            registry.addRecipes(GTCXRecipe.integratedCircuitRecipes, recipeId);
            registry.addRecipeCatalyst(new ItemStack(GTCXItems.integratedCircuit, 1), recipeId);
            IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
            if (!GTCXConfiguration.general.crushedOres || GTCXConfiguration.general.gt2Mode){
                for (GTMaterial mat : GTMaterial.values()){
                    if (mat.hasFlag(GTCXMaterial.crushedore)){
                        blacklist.addIngredientToBlacklist(GTCXMaterialGen.getCrushedOre(mat, 1));
                    }
                    if (mat.hasFlag(GTCXMaterial.crushedorePurified)){
                        blacklist.addIngredientToBlacklist(GTCXMaterialGen.getPurifiedCrushedOre(mat, 1));
                    }
                }
            }
            if (GTCXConfiguration.general.gt2Mode || GTCXConfiguration.general.enableSteamMachines){
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.stoneCompressor));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.stoneExtractor));
            }
            if (GTCXConfiguration.general.gt2Mode){
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.steamExtractor));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.steamCompressor));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.steamMacerator));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.steamFurnace));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.steamAlloySmelter));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.steamForgeHammer));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.coalBoiler));
            }
            blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXItems.dataOrbStorage));
            blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.dummyCover));
            if (!Loader.isModLoaded(GTValues.MOD_ID_TFOREST) || !GTConfig.modcompat.compatTwilightForest){
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.oreOlivineOverworld));
            }
            if (!GTCXConfiguration.general.unfiredBricks){
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXItems.unfiredBrick));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXItems.unfiredFireBrick));
            }
            blacklist.addIngredientToBlacklist(Ic2Items.wrench);
            if (GTCXConfiguration.general.overrideIc2cSawmill){
                blacklist.addIngredientToBlacklist(Ic2Items.sawMill);
            }
            blacklist.addIngredientToBlacklist(GTCXMaterialGen.getHull(GTCXMaterial.RefinedIron, 1));
            blacklist.addIngredientToBlacklist(Ic2Items.cutter);
            if (!GTCXConfiguration.general.enableCraftingTools || GTCXConfiguration.general.gt2Mode){
                blacklist.addIngredientToBlacklist(GTCXToolGen.getFile(GTCXMaterial.Bronze));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getFile(GTCXMaterial.Iron));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getFile(GTCXMaterial.Steel));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getFile(GTCXMaterial.TungstenSteel));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getSaw(GTCXMaterial.Bronze));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getSaw(GTCXMaterial.Iron));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getSaw(GTCXMaterial.Steel));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getSaw(GTCXMaterial.TungstenSteel));
            }
            blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTBlocks.tileFusionReactor));
            blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTBlocks.tileCentrifuge));

        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new MachineRecipeCategory(registry.getJeiHelpers().getGuiHelper(), "electricFurnace", Ic2Items.electroFurnace, Ic2Resources.electricFurnace));
        registry.addRecipeCategories(new GTCXJeiCauldronCategory(registry.getJeiHelpers().getGuiHelper(), "gt.cauldron", Items.CAULDRON));
        categoryUtil(registry, GTCXRecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST, GTCXBlocks.industrialBlastFurnace);
        categoryUtil(registry, GTCXRecipeLists.FLUID_SMELTER_RECIPE_LIST, GTCXBlocks.fluidSmelter);
        categoryUtil(registry, GTCXRecipeLists.FLUID_CASTER_RECIPE_LIST, GTCXBlocks.fluidCaster);
        categoryUtil(registry, GTTileMultiFusionReactor.RECIPE_LIST, GTCXBlocks.fusionComputer);
        categoryUtil2(registry, GTCXRecipeLists.DIESEL_GEN_RECIPE_LIST, GTCXBlocks.dieselGenerator);
        categoryUtil2(registry, GTCXRecipeLists.GAS_TURBINE_RECIPE_LIST, GTCXBlocks.gasTurbine);
        registry.addRecipeCategories(new GTCXJeiIntegratedCircuitCategory(registry.getJeiHelpers().getGuiHelper()));

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void wrapperUtil(@Nonnull IModRegistry registry, GTRecipeMultiInputList list, Block catalyst,
                                     Class gui, int clickX, int clickY, int sizeX, int sizeY) {
        String recipeList = list.getCategory();
        registry.handleRecipes(GTRecipeMultiInputList.MultiRecipe.class, GTCXJeiHeatWrapper::new, recipeList);
        registry.addRecipes(list.getRecipeList(), recipeList);
        registry.addRecipeCatalyst(new ItemStack(catalyst), recipeList);
        if (gui != null) {
            registry.addRecipeClickArea(gui, clickX, clickY, sizeX, sizeY, recipeList);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void wrapperUtil2(@Nonnull IModRegistry registry, GTRecipeMultiInputList list, Block catalyst,
                                    Class gui, int clickX, int clickY, int sizeX, int sizeY) {
        String recipeList = list.getCategory();
        registry.handleRecipes(GTRecipeMultiInputList.MultiRecipe.class, GTCXJeiCasterWrapper::new, recipeList);
        registry.addRecipes(list.getRecipeList(), recipeList);
        registry.addRecipeCatalyst(new ItemStack(catalyst), recipeList);
        if (gui != null) {
            registry.addRecipeClickArea(gui, clickX, clickY, sizeX, sizeY, recipeList);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void wrapperUtil3(@Nonnull IModRegistry registry, GTRecipeMultiInputList list, Block catalyst,
                                     Class gui, int clickX, int clickY, int sizeX, int sizeY) {
        String recipeList = list.getCategory();
        registry.handleRecipes(GTRecipeMultiInputList.MultiRecipe.class, GTCXJeiBurnableFluidWrapper::new, recipeList);
        registry.addRecipes(list.getRecipeList(), recipeList);
        registry.addRecipeCatalyst(new ItemStack(catalyst), recipeList);
        if (gui != null) {
            registry.addRecipeClickArea(gui, clickX, clickY, sizeX, sizeY, recipeList);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void wrapperUtil4(@Nonnull IModRegistry registry, GTRecipeMultiInputList list, Block catalyst,
                                     Class gui, int clickX, int clickY, int sizeX, int sizeY) {
        String recipeList = list.getCategory();
        registry.handleRecipes(GTRecipeMultiInputList.MultiRecipe.class, GTCXJeiFusionWrapper::new, recipeList);
        registry.addRecipes(list.getRecipeList(), recipeList);
        registry.addRecipeCatalyst(new ItemStack(catalyst), recipeList);
        if (gui != null) {
            registry.addRecipeClickArea(gui, clickX, clickY, sizeX, sizeY, recipeList);
        }
    }

    private static void categoryUtil(IRecipeCategoryRegistration registry, GTRecipeMultiInputList list, Block catalyst) {
        registry.addRecipeCategories(new GTCXJeiCustomCategory(registry.getJeiHelpers().getGuiHelper(), list.getCategory(), catalyst));
    }

    private static void categoryUtil2(IRecipeCategoryRegistration registry, GTRecipeMultiInputList list, Block catalyst) {
        registry.addRecipeCategories(new GTCXJeiBurnableFluidCategory(registry.getJeiHelpers().getGuiHelper(), list.getCategory(), catalyst));
    }
}
