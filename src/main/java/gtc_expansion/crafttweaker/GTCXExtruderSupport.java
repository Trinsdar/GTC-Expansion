package gtc_expansion.crafttweaker;

import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileExtruder;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

@ZenClass("mods.gtclassic.Extruder")
@ZenRegister
public class GTCXExtruderSupport {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input1, IIngredient input2, @Optional(valueLong = 1920L)int totalEu){
        GTCraftTweakerActions.apply(new ExtruderRecipeAction(GTCraftTweakerActions.of(input1), GTCraftTweakerActions.of(input2), CraftTweakerMC.getItemStack(output), totalEu));
    }

    private static final class ExtruderRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final IRecipeInput input2;
        private final ItemStack output;
        private final int totalEu;

        ExtruderRecipeAction(IRecipeInput input1, IRecipeInput input2, ItemStack output, int totalEu) {
            this.input1 = input1;
            this.input2 = input2;
            this.output = output;
            this.totalEu = totalEu;
        }

        @Override
        public void apply() {
            GTCXTileExtruder.addRecipe(input1, input2, totalEu, output, output.getUnlocalizedName() + "_ct");
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s, %s -> %s] to %s", input1, input2, totalEu, output, GTCXRecipeLists.EXTRUDER_RECIPE_LIST);
        }
    }
}
