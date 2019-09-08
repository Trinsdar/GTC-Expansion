package gtc_expansion.util.jei;

import gtc_expansion.GEBlocks;
import gtclassic.util.jei.GTJeiPlugin;
import ic2.jeiIntigration.SubModul;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

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
                GTJeiPlugin.wrapperUtil(registry, entry.getRecipeList(), entry.getCatalyst(), entry.getGuiClass(), entry.getClickX(), entry.getClickY(), entry.getSizeX(), entry.getSizeY());
            }
            registry.addRecipeCatalyst(new ItemStack(GEBlocks.alloyFurnace), "gt.alloysmelter");
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        for (GEJeiRegistry entry : GEJeiRegistry.values()) {
            GTJeiPlugin.categoryUtil(registry, entry.getRecipeList(), entry.getCatalyst());
        }

    }
}
