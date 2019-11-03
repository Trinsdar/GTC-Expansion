package gtc_expansion.util.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiVacuumFreezer;
import gtclassic.util.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Locale;

@ZenClass("mods.gtclassic.VacuumFreezer")
@ZenRegister
public class GEVacuumFreezerSupport {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input1, @Optional(valueLong = 2560L)int totalEu){
        GTCraftTweakerActions.apply(new VacuumFreezerRecipeAction(GTCraftTweakerActions.of(input1), totalEu, CraftTweakerMC.getItemStack(output)));
    }

    private static final class VacuumFreezerRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final int totalEu;
        private final ItemStack output;

        VacuumFreezerRecipeAction(IRecipeInput input1, int totalEu, ItemStack output) {
            this.input1 = input1;
            this.totalEu = totalEu;
            this.output = output;
        }

        @Override
        public void apply() {
            if (totalEu <= 0) {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Eu amount must be greater then 0!!");
                return;
            }
            GETileMultiVacuumFreezer.addRecipe(input1, GETileMultiVacuumFreezer.totalEu(totalEu), output);
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s] to %s", input1, totalEu, output, GERecipeLists.IMPLOSION_COMPRESSOR_RECIPE_LIST);
        }
    }
}
