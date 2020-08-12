package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.multi.GTCXTileMultiIndustrialSawmill;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Locale;

@ZenClass("mods.gtclassic.IndustrialSawmill")
@ZenRegister
public class GTCXIndustrialSawmillSupport {
    @ZenMethod
    public static void addRecipe(IItemStack[] output, IIngredient input1, ILiquidStack inputFluid, @Optional(valueLong = 2560L)int totalEu){
        GTCraftTweakerActions.apply(new IndustrialSawmillRecipeAction(GTCraftTweakerActions.of(input1), CraftTweakerMC.getLiquidStack(inputFluid), totalEu, CraftTweakerMC.getItemStacks(output)));
    }

    private static final class IndustrialSawmillRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final FluidStack inputFluid;
        private final int totalEu;
        private final ItemStack[] output;

        IndustrialSawmillRecipeAction(IRecipeInput input1, FluidStack inputFluid, int totalEu, ItemStack... output) {
            this.input1 = input1;
            this.inputFluid = inputFluid;
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
            GTCXTileMultiIndustrialSawmill.addRecipe(input1, inputFluid, totalEu, output[0].getUnlocalizedName() + "_ct", output);
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s, %s -> %s] to %s", input1, inputFluid, totalEu, Arrays.deepToString(output), GTCXRecipeLists.INDUSTRIAL_SAWMILL_RECIPE_LIST);
        }
    }
}
