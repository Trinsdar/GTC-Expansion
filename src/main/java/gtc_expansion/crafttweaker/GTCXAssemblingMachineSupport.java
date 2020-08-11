package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileAssemblingMachine;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

@ZenClass("mods.gtclassic.AssemblingMachine")
@ZenRegister
public class GTCXAssemblingMachineSupport {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input1, IIngredient input2, @Optional(valueLong = 1200L)int totalEu){
        GTCraftTweakerActions.apply(new AssemblingMachineRecipeAction(GTCraftTweakerActions.of(input1), GTCraftTweakerActions.of(input2), totalEu, CraftTweakerMC.getItemStack(output)));
    }

    private static final class AssemblingMachineRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final IRecipeInput input2;
        private final int totalEu;
        private final ItemStack output;

        AssemblingMachineRecipeAction(IRecipeInput input1, IRecipeInput input2, int totalEu, ItemStack output) {
            this.input1 = input1;
            this.input2 = input2;
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
            GTCXTileAssemblingMachine.addRecipe(new IRecipeInput[]{input1, input2}, GTCXTileAssemblingMachine.totalEu(totalEu), output.getUnlocalizedName() + "_ct", output);
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s] to %s", input1, input2, output, GTCXRecipeLists.ASSEMBLING_MACHINE_RECIPE_LIST);
        }
    }
}
