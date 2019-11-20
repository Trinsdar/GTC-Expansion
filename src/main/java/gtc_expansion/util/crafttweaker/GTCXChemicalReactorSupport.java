package gtc_expansion.util.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileChemicalReactor;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Locale;

@ZenClass("mods.gtclassic.ChemicalReactor")
@ZenRegister
public class GTCXChemicalReactorSupport {
    @ZenMethod
    public static void addRecipe(IItemStack[] output, IIngredient input1, IIngredient input2, int cells, @Optional(valueLong = 3200L) int totalEu) {
        GTCraftTweakerActions.apply(new ChemicalReactorRecipeAction(GTCraftTweakerActions.of(input1), GTCraftTweakerActions.of(input2), cells, totalEu, CraftTweakerMC.getItemStacks(output)));
    }

    @ZenMethod
    public static void addRecipe(IItemStack[] output, IIngredient input1, IIngredient input2, @Optional(valueLong = 3200L) int totalEu) {
        addRecipe(output, input1, input2, 0, totalEu);
    }

    private static final class ChemicalReactorRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final IRecipeInput input2;
        private final int totalEu;
        private final int cells;
        private final ItemStack[] output;

        ChemicalReactorRecipeAction(IRecipeInput input1, IRecipeInput input2, int cells, int totalEu, ItemStack... output) {
            this.input1 = input1;
            this.input2 = input2;
            this.cells = cells;
            this.totalEu = totalEu;
            this.output = output;
        }

        @Override
        public void apply() {
            if (totalEu > 0) {
                GTCXTileChemicalReactor.addRecipe(input1, input2, cells, totalEu, output);
            } else {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Eu amount must be greater then 0!!");
            }
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s, %s, %s -> %s] to %s", this.input1, this.input2, this.cells, this.totalEu, Arrays.deepToString(this.output), GTCXRecipeLists.CHEMICAL_REACTOR_RECIPE_LIST);
        }
    }
}
