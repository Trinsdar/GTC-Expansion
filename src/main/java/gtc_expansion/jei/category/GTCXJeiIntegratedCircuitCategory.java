package gtc_expansion.jei.category;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.jei.wrapper.GTCXJeiIntegratedCircuitWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GTCXJeiIntegratedCircuitCategory implements IRecipeCategory<GTCXJeiIntegratedCircuitWrapper> {
    protected ResourceLocation backgroundTexture;
    private IDrawable background;

    public GTCXJeiIntegratedCircuitCategory(IGuiHelper helper) {
        backgroundTexture = new ResourceLocation(GTCExpansion.MODID, "textures/gui/jeiintegratedcircuit.png");
        background = helper.createDrawable(backgroundTexture, 52, 25, 72, 50);
    }

    @Override
    public String getUid() {
        return "gt.integratedcircuit";
    }

    @Override
    public String getTitle() {
        return new ItemStack(GTCXItems.integratedCircuit).getDisplayName() + " Modes";
    }

    @Override
    public String getModName() {
        return GTCExpansion.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, GTCXJeiIntegratedCircuitWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup itemGroup = layout.getItemStacks();
        itemGroup.init(0, true,  0, 0);
        itemGroup.set(0, recipeWrapper.getRecipe().getInput().getInputs());
        itemGroup.init(1, false, 54, 0);
        itemGroup.set(1, recipeWrapper.getRecipe().getOutput().getInputs());
    }
}
