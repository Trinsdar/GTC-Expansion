package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileElectrolyzer;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Locale;

@ZenClass("mods.gtclassic.Electrolyzer")
@ZenRegister
public class GTCXElectrolyzerSupport {
    @ZenMethod
    public static void addRecipe(IItemStack[] output, IIngredient[] input,
                                 @Optional(valueLong = 3200L) int totalEu) {
        GTCraftTweakerActions.apply(new IndustrialElectrolyzerRecipeAction(GTCraftTweakerActions.of(input), totalEu, CraftTweakerMC.getItemStacks(output)));
    }

    @ZenMethod
    public static void addRecipe(IItemStack[] output, ILiquidStack[] fluidOutput, IIngredient[] input, @Optional(valueLong = 3200L) int totalEu) {
        GTCraftTweakerActions.apply(new IndustrialElectrolyzerRecipeAction(GTCraftTweakerActions.of(input), totalEu, CraftTweakerMC.getItemStacks(output), CraftTweakerMC.getLiquidStacks(fluidOutput)));
    }

    private static final class IndustrialElectrolyzerRecipeAction implements IAction {

        private final IRecipeInput[] input;
        private final int totalEu;
        private final ItemStack[] output;
        private final FluidStack[] fluidOutput;

        IndustrialElectrolyzerRecipeAction(IRecipeInput[] input, int totalEu, ItemStack[] output, FluidStack[] fluidOutput) {
            this.input = input;
            this.totalEu = totalEu;
            this.output = output;
            this.fluidOutput = fluidOutput;
        }

        IndustrialElectrolyzerRecipeAction(IRecipeInput[] input, int totalEu, ItemStack[] output) {
            this.input = input;
            this.totalEu = totalEu;
            this.output = output;
            this.fluidOutput = new FluidStack[]{};
        }

        @Override
        public void apply() {
            if (totalEu > 0) {
                String recipeId = fluidOutput.length == 0 ? output[0].getUnlocalizedName() : fluidOutput[0].getUnlocalizedName();
                GTCXTileElectrolyzer.addRecipe(input, GTCXTileElectrolyzer.totalEu(totalEu), output, fluidOutput, recipeId + "_ct");
            } else {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Eu amount must be greater then 0!!");
            }
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s, %s] to %s", Arrays.deepToString(this.input), this.totalEu, Arrays.deepToString(this.output), Arrays.deepToString(fluidOutput), GTCXRecipeLists.ELECTROLYZER_RECIPE_LIST);
        }
    }
}
