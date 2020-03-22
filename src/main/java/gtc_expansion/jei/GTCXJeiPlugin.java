package gtc_expansion.jei;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXItems;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.item.tools.GTCXToolGen;
import gtc_expansion.jei.category.GTCXJeiBurnableFluidCategory;
import gtc_expansion.jei.category.GTCXJeiCustomCategory;
import gtc_expansion.jei.wrapper.GTCXJeiBurnableFluidWrapper;
import gtc_expansion.jei.wrapper.GTCXJeiCasterWrapper;
import gtc_expansion.jei.wrapper.GTCXJeiHeatWrapper;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.common.GTConfig;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2Items;
import ic2.jeiIntigration.SubModul;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

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
            registry.addRecipeCatalyst(new ItemStack(GTCXBlocks.alloyFurnace), "gt.alloysmelter");
            IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
            if (!Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) || !GTConfig.modcompat.compatIc2Extras){
                for (GTMaterial mat : GTMaterial.values()){
                    if (mat.hasFlag(GTCXMaterial.crushedore)){
                        blacklist.addIngredientToBlacklist(GTCXMaterialGen.getCrushedOre(mat, 1));
                    }
                    if (mat.hasFlag(GTCXMaterial.crushedorePurified)){
                        blacklist.addIngredientToBlacklist(GTCXMaterialGen.getPurifiedCrushedOre(mat, 1));
                    }
                    if (mat.hasFlag(GTCXMaterial.tinydust)){
                        blacklist.addIngredientToBlacklist(GTCXMaterialGen.getTinyDust(mat, 1));
                    }
                }
            }
            if (!Loader.isModLoaded(GTValues.MOD_ID_TFOREST) || !GTConfig.modcompat.compatTwilightForest){
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXBlocks.oreOlivineOverworld));
            }
            if (!GTCXConfiguration.general.unfiredBricks){
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXItems.unfiredBrick));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTCXItems.unfiredFireBrick));
            }
            if (GTCXConfiguration.general.enableCraftingTools){
                blacklist.addIngredientToBlacklist(Ic2Items.wrench);
            }
            if (IC2.config.getFlag("SteelRecipes")){
                blacklist.addIngredientToBlacklist(GTCXMaterialGen.getHull(GTCXMaterial.Steel, 1));
            } else {
                blacklist.addIngredientToBlacklist(GTCXMaterialGen.getHull(GTCXMaterial.RefinedIron, 1));
            }
            blacklist.addIngredientToBlacklist(Ic2Items.cutter);
            if (!GTCXConfiguration.general.enableCraftingTools){
                blacklist.addIngredientToBlacklist(GTCXToolGen.getWrench(GTCXMaterial.Bronze));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getWrench(GTCXMaterial.Iron));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getWrench(GTCXMaterial.Steel));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getWrench(GTCXMaterial.TungstenSteel));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getFile(GTCXMaterial.Bronze));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getFile(GTCXMaterial.Iron));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getFile(GTCXMaterial.Steel));
                blacklist.addIngredientToBlacklist(GTCXToolGen.getFile(GTCXMaterial.TungstenSteel));
            }
            //blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTBlocks.tileFusionReactor));

        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        categoryUtil(registry, GTCXRecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST, GTCXBlocks.industrialBlastFurnace);
        categoryUtil(registry, GTCXRecipeLists.FLUID_SMELTER_RECIPE_LIST, GTCXBlocks.fluidSmelter);
        categoryUtil(registry, GTCXRecipeLists.FLUID_CASTER_RECIPE_LIST, GTCXBlocks.fluidCaster);
        categoryUtil2(registry, GTCXRecipeLists.DIESEL_GEN_RECIPE_LIST, GTCXBlocks.dieselGenerator);
        categoryUtil2(registry, GTCXRecipeLists.GAS_TURBINE_RECIPE_LIST, GTCXBlocks.gasTurbine);

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

    private static void categoryUtil(IRecipeCategoryRegistration registry, GTRecipeMultiInputList list, Block catalyst) {
        registry.addRecipeCategories(new GTCXJeiCustomCategory(registry.getJeiHelpers().getGuiHelper(), list.getCategory(), catalyst));
    }

    private static void categoryUtil2(IRecipeCategoryRegistration registry, GTRecipeMultiInputList list, Block catalyst) {
        registry.addRecipeCategories(new GTCXJeiBurnableFluidCategory(registry.getJeiHelpers().getGuiHelper(), list.getCategory(), catalyst));
    }
}
