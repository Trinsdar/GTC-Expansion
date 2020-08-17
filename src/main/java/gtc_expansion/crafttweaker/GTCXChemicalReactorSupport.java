package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileChemicalReactor;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Locale;

@ZenClass("mods.gtclassic.ChemicalReactor")
@ZenRegister
public class GTCXChemicalReactorSupport {
    @ZenMethod
    public static void addRecipe(IItemStack[] output, IIngredient[] input, @Optional(valueLong = 3200L) int totalEu) {
        GTCraftTweakerActions.apply(new ChemicalReactorRecipeAction(GTCraftTweakerActions.of(input), totalEu, CraftTweakerMC.getItemStacks(output)));
    }

    @ZenMethod
    public static void addRecipe(IItemStack[] output, ILiquidStack fluidOutput, IIngredient[] input, @Optional(valueLong = 3200L) int totalEu) {
        GTCraftTweakerActions.apply(new ChemicalReactorRecipeAction(GTCraftTweakerActions.of(input), totalEu, CraftTweakerMC.getLiquidStack(fluidOutput), CraftTweakerMC.getItemStacks(output)));
    }

    private static final class ChemicalReactorRecipeAction implements IAction {

        private final IRecipeInput[] inputs;
        private final int totalEu;
        private final FluidStack fluidOutput;
        private final ItemStack[] output;

        ChemicalReactorRecipeAction(IRecipeInput[] inputs, int totalEu, ItemStack... output) {
            this.inputs = inputs;
            this.totalEu = totalEu;
            this.output = output;
            this.fluidOutput = null;
        }

        ChemicalReactorRecipeAction(IRecipeInput[] inputs, int totalEu, FluidStack fluidOutput, ItemStack... output) {
            this.inputs = inputs;
            this.totalEu = totalEu;
            this.output = output;
            this.fluidOutput = fluidOutput;
        }

        @Override
        public void apply() {
            if (totalEu > 0) {
                if (fluidOutput != null){
                    GTCXTileChemicalReactor.addRecipe(inputs, GTCXTileChemicalReactor.totalEu(totalEu), fluidOutput, fluidOutput.getUnlocalizedName() + "_ct", output);
                } else {
                    GTCXTileChemicalReactor.addRecipe(inputs, GTCXTileChemicalReactor.totalEu(totalEu), output[0].getUnlocalizedName() + "_ct", output);
                }
            } else {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Eu amount must be greater then 0!!");
            }
        }

        @Override
        public String describe() {
            if (fluidOutput != null){
                return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s, %s] to %s", Arrays.deepToString(inputs), this.totalEu, this.fluidOutput, Arrays.deepToString(this.output), GTCXRecipeLists.CHEMICAL_REACTOR_RECIPE_LIST);
            }
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s] to %s", Arrays.deepToString(inputs), this.totalEu, Arrays.deepToString(this.output), GTCXRecipeLists.CHEMICAL_REACTOR_RECIPE_LIST);
        }
    }
}
