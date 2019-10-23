package gtc_expansion.util.jei;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEConfiguration;
import gtc_expansion.GEItems;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.util.jei.category.GEJeiDistillationTowerCategory;
import gtc_expansion.util.jei.category.GEJeiIBFCategory;
import gtc_expansion.util.jei.wrapper.GEJeiDistillationTowerWrapper;
import gtc_expansion.util.jei.wrapper.GEJeiIBFWrapper;
import gtclassic.GTBlocks;
import gtclassic.GTConfig;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.util.GTValues;
import gtclassic.util.jei.GTJeiPlugin;
import gtclassic.util.recipe.GTRecipeMultiInputList;
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
public class GEJeiPlugin implements IModPlugin {
    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime arg0) {
        // empty method for construction
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (SubModul.load) {
            for (GEJeiRegistry entry : GEJeiRegistry.values()) {
                if (entry == GEJeiRegistry.DISTILLATIONTOWER){
                    wrapperUtil1(registry, entry.getRecipeList(), entry.getCatalyst(), entry.getGuiClass(), entry.getClickX(), entry.getClickY(), entry.getSizeX(), entry.getSizeY());
                } else if ( entry == GEJeiRegistry.INDUSTRIALBLASTFURNACE){
                    wrapperUtil2(registry, entry.getRecipeList(), entry.getCatalyst(), entry.getGuiClass(), entry.getClickX(), entry.getClickY(), entry.getSizeX(), entry.getSizeY());
                } else {
                    GTJeiPlugin.wrapperUtil(registry, entry.getRecipeList(), entry.getCatalyst(), entry.getGuiClass(), entry.getClickX(), entry.getClickY(), entry.getSizeX(), entry.getSizeY());
                }
            }
            registry.addRecipeCatalyst(new ItemStack(GEBlocks.alloyFurnace), "gt.alloysmelter");
            IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
            if (!Loader.isModLoaded(GTValues.IC2_EXTRAS) || !GTConfig.compatIc2Extras){
                for (GTMaterial mat : GTMaterial.values()){
                    if (mat.hasFlag(GEMaterial.crushedore)){
                        blacklist.addIngredientToBlacklist(GEMaterialGen.getCrushedOre(mat, 1));
                    }
                    if (mat.hasFlag(GEMaterial.crushedorePurified)){
                        blacklist.addIngredientToBlacklist(GEMaterialGen.getPurifiedCrushedOre(mat, 1));
                    }
                    if (mat.hasFlag(GEMaterial.tinydust)){
                        blacklist.addIngredientToBlacklist(GEMaterialGen.getTinyDust(mat, 1));
                    }
                }
            }
            if (!Loader.isModLoaded(GTValues.TFOREST) || !GTConfig.compatTwilightForest){
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GEBlocks.oreOlivineOverworld));
            }
            if (!GEConfiguration.unfiredBricks){
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GEItems.unfiredBrick));
                blacklist.addIngredientToBlacklist(GTMaterialGen.get(GEItems.unfiredFireBrick));
            }
            if (!GEConfiguration.gt2Mode){
                blacklist.addIngredientToBlacklist(Ic2Items.wrench);
            }
            //blacklist.addIngredientToBlacklist(GTMaterialGen.get(GTBlocks.tileFusionReactor));

        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        for (GEJeiRegistry entry : GEJeiRegistry.values()) {
            if (entry == GEJeiRegistry.DISTILLATIONTOWER){
                categoryUtil1(registry, entry.getRecipeList(), entry.getCatalyst());
            } else if (entry == GEJeiRegistry.INDUSTRIALBLASTFURNACE){
                categoryUtil2(registry, entry.getRecipeList(), entry.getCatalyst());
            } else {
                GTJeiPlugin.categoryUtil(registry, entry.getRecipeList(), entry.getCatalyst());
            }
        }

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void wrapperUtil1(@Nonnull IModRegistry registry, GTRecipeMultiInputList list, Block catalyst,
                                   Class gui, int clickX, int clickY, int sizeX, int sizeY) {
        String recipeList = list.getCategory();
        registry.handleRecipes(GTRecipeMultiInputList.MultiRecipe.class, GEJeiDistillationTowerWrapper::new, recipeList);
        registry.addRecipes(list.getRecipeList(), recipeList);
        registry.addRecipeCatalyst(new ItemStack(catalyst), recipeList);
        if (gui != null) {
            registry.addRecipeClickArea(gui, clickX, clickY, sizeX, sizeY, recipeList);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void wrapperUtil2(@Nonnull IModRegistry registry, GTRecipeMultiInputList list, Block catalyst,
                                    Class gui, int clickX, int clickY, int sizeX, int sizeY) {
        String recipeList = list.getCategory();
        registry.handleRecipes(GTRecipeMultiInputList.MultiRecipe.class, GEJeiIBFWrapper::new, recipeList);
        registry.addRecipes(list.getRecipeList(), recipeList);
        registry.addRecipeCatalyst(new ItemStack(catalyst), recipeList);
        if (gui != null) {
            registry.addRecipeClickArea(gui, clickX, clickY, sizeX, sizeY, recipeList);
        }
    }


    public static void categoryUtil1(IRecipeCategoryRegistration registry, GTRecipeMultiInputList list, Block catalyst) {
        registry.addRecipeCategories(new GEJeiDistillationTowerCategory(registry.getJeiHelpers().getGuiHelper(), list.getCategory(), catalyst));
    }

    public static void categoryUtil2(IRecipeCategoryRegistration registry, GTRecipeMultiInputList list, Block catalyst) {
        registry.addRecipeCategories(new GEJeiIBFCategory(registry.getJeiHelpers().getGuiHelper(), list.getCategory(), catalyst));
    }
}
