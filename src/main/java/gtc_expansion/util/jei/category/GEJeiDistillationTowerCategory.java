package gtc_expansion.util.jei.category;

import gtc_expansion.util.GTFluidMachineOutput;
import gtc_expansion.util.jei.wrapper.GEJeiDistillationTowerWrapper;
import gtclassic.GTConfig;
import gtclassic.GTMod;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.item.misc.ItemDisplayIcon;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GEJeiDistillationTowerCategory implements IRecipeCategory<GEJeiDistillationTowerWrapper> {

    protected String name, displayName;
    protected ResourceLocation backgroundTexture;
    private IDrawable background, progress;

    public GEJeiDistillationTowerCategory(IGuiHelper helper, String name, Block block) {
        this.name = name;
        displayName = new ItemStack(block).getDisplayName().replace(" Controller", "");
        backgroundTexture = new ResourceLocation(GTMod.MODID, "textures/gui/default.png");
        background = helper.createDrawable(backgroundTexture, 16, 16, 144, getHeight());
        IDrawableStatic progressPic = helper.createDrawable(backgroundTexture, 176, 0, 20, 18);
        progress = helper.createAnimatedDrawable(progressPic, 150, IDrawableAnimated.StartDirection.LEFT, false);
    }

    private int getHeight() {
        return GTConfig.debugMode ? 100 : 90;
    }

    @Override
    public String getUid() {
        return name;
    }

    @Override
    public String getTitle() {
        return displayName;
    }

    @Override
    public String getModName() {
        return GTMod.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        progress.draw(minecraft, 62, 9);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, GEJeiDistillationTowerWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup itemGroup = layout.getItemStacks();
        IGuiFluidStackGroup fluidGroup = layout.getFluidStacks();
        int index = 0;
        int actualIndex = 0;
        for (IRecipeInput list : wrapper.getMultiRecipe().getInputs()) {
            if (list instanceof RecipeInputFluid){
                int x = index % 3;
                int y = index / 3;
                fluidGroup.init(actualIndex, true, (18 * x), (18 * y), 16, 16, ((RecipeInputFluid)list).fluid.amount, true, null);
                fluidGroup.set(actualIndex, ((RecipeInputFluid)list).fluid);
                index++;
                actualIndex++;
                if (index >= 6) {
                    break;
                }
            }
        }
        index = 0;
        MachineOutput output = wrapper.getMultiRecipe().getOutputs();

        if (output instanceof GTFluidMachineOutput){
            for (FluidStack stack : ((GTFluidMachineOutput)output).getFluids()) {
                int x = index % 3;
                int y = index / 3;
                fluidGroup.init(actualIndex, false, 90 + (18 * x), (18 * y), 16, 16, stack.amount, true, null);
                fluidGroup.set(actualIndex, stack);
                index++;
                actualIndex++;
                if (index >= 6) {
                    break;
                }
            }
            for (ItemStack stack : output.getAllOutputs()) {
                if (index >= 6) {
                    break;
                }
                if (stack.getItem() instanceof ItemDisplayIcon){
                    continue;
                }
                int x = index % 3;
                int y = index / 3;
                itemGroup.init(actualIndex, false, 90 + (18 * x), (18 * y));
                itemGroup.set(actualIndex, stack);
                index++;
                actualIndex++;
                if (index == 6) {
                    break;
                }
            }
        }
    }
}
