package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileFluidCaster;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

@ZenClass("mods.gtclassic.FluidCaster")
@ZenRegister
public class GTCXFluidCasterSupport {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input1, ILiquidStack inputFluid, boolean consumePress, @Optional(valueLong = 2560L)int totalEu){
        GTCraftTweakerActions.apply(new FluidCasterRecipeAction(GTCraftTweakerActions.of(input1), CraftTweakerMC.getLiquidStack(inputFluid), consumePress, totalEu, CraftTweakerMC.getItemStack(output)));
    }

    private static final class FluidCasterRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final FluidStack inputFluid;
        private final int totalEu;
        private final boolean consumePress;
        private final ItemStack output;

        FluidCasterRecipeAction(IRecipeInput input1, FluidStack inputFluid, boolean consumePress, int totalEu, ItemStack output) {
            this.input1 = input1;
            this.inputFluid = inputFluid;
            this.consumePress = consumePress;
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
            GTCXTileFluidCaster.addRecipe(input1, inputFluid, consumePress, totalEu, output);
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s, %s, %s -> %s] to %s", input1, inputFluid, consumePress, totalEu, output, GTCXRecipeLists.FLUID_CASTER_RECIPE_LIST);
        }
    }
}
