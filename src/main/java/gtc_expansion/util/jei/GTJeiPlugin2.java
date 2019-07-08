package gtc_expansion.util.jei;

import gtclassic.GTBlocks;
import gtclassic.container.GTContainerWorktable;
import gtclassic.gui.GTGuiMachine;
import gtclassic.recipe.GTRecipeUUAmplifier;
import gtclassic.util.jei.GTJeiPlugin;
import gtclassic.util.jei.GTJeiRegistry;
import gtclassic.util.jei.category.GTJeiUUAmplifierCategory;
import gtclassic.util.jei.wrapper.GTJeiUUAmplifierWrapper;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.jeiIntigration.SubModul;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class GTJeiPlugin2 implements IModPlugin {
    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime arg0) {
        // empty method for construction
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (SubModul.load) {
            for (GTJeiRegistry entry : GTJeiRegistry.values()) {
                GTJeiPlugin.wrapperUtil(registry, entry.getRecipeList(), entry.getCatalyst(), entry.getGuiClass(), entry.getClickX(), entry.getClickY(), entry.getSizeX(), entry.getSizeY());
            }
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        for (GTJeiRegistry entry : GTJeiRegistry.values()) {
            GTJeiPlugin.categoryUtil(registry, entry.getRecipeList(), entry.getCatalyst());
        }

    }
}
