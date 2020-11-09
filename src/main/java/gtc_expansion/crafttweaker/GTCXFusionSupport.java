package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import gtclassic.common.tile.multi.GTTileMultiFusionReactor;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenMethodStatic;

import java.util.Locale;

@ZenRegister
@ZenExpansion("mods.gtclassic.FusionReactor")
public class GTCXFusionSupport {
    @ZenMethodStatic
    public static void addRecipe(IItemStack output, IIngredient input1, IIngredient input2, int totalEu, @Optional int startEu) {
        GTCraftTweakerActions.apply(new FusionReactorRecipeAction(GTCraftTweakerActions.of(input1), GTCraftTweakerActions.of(input2), totalEu, startEu, CraftTweakerMC.getItemStack(output)));
    }

    @ZenMethodStatic
    public static void addRecipe(ILiquidStack output, IIngredient input1, IIngredient input2, int totalEu, @Optional int startEu) {
        GTCraftTweakerActions.apply(new FusionReactorFluidRecipeAction(GTCraftTweakerActions.of(input1), GTCraftTweakerActions.of(input2), totalEu, startEu, CraftTweakerMC.getLiquidStack(output)));
    }

    private static final class FusionReactorRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final IRecipeInput input2;
        private final int totalEu;
        private final int startEu;
        private final ItemStack output;

        FusionReactorRecipeAction(IRecipeInput input1, IRecipeInput input2, int totalEu, int startEu, ItemStack output) {
            this.input1 = input1;
            this.input2 = input2;
            this.totalEu = totalEu;
            this.startEu = startEu;
            this.output = output;
        }

        @Override
        public void apply() {
            if (totalEu > 0) {
                GTCXTileMultiFusionReactor.addRecipe(input1, input2, GTTileMultiFusionReactor.totalEu(totalEu), startEu, output, output.getUnlocalizedName()+ "_ct");
            } else {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Eu amount must be greater then 0!!");
            }
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s] to %s", this.input1, this.input2, this.output, GTTileMultiFusionReactor.RECIPE_LIST);
        }
    }

    private static final class FusionReactorFluidRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final IRecipeInput input2;
        private final int totalEu;
        private final int startEu;
        private final FluidStack output;

        FusionReactorFluidRecipeAction(IRecipeInput input1, IRecipeInput input2, int totalEu, int startEu, FluidStack output) {
            this.input1 = input1;
            this.input2 = input2;
            this.totalEu = totalEu;
            this.startEu = startEu;
            this.output = output;
        }

        @Override
        public void apply() {
            if (totalEu > 0) {
                GTCXTileMultiFusionReactor.addRecipe(input1, input2, GTTileMultiFusionReactor.totalEu(totalEu), startEu, output, output.getUnlocalizedName() + "_ct");
            } else {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Eu amount must be greater then 0!!");
            }
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s] to %s", this.input1, this.input2, this.output, GTTileMultiFusionReactor.RECIPE_LIST);
        }
    }
}
