package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.multi.GTCXTileMultiDistillationTower;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Locale;

@ZenClass("mods.gtclassic.DistillationTower")
@ZenRegister
public class GTCXDistillationTowerSupport {
    @ZenMethod
    public static void addRecipe(IItemStack outputItem[], ILiquidStack[] output, int totalEu , ILiquidStack input){
        GTCraftTweakerActions.apply(new DistillationTowerRecipeAction(CraftTweakerMC.getLiquidStack(input), totalEu, CraftTweakerMC.getLiquidStacks(output), CraftTweakerMC.getItemStacks(outputItem)));
    }

    @ZenMethod
    public static void addRecipe(ILiquidStack[] output, int totalEu , ILiquidStack input){
        GTCraftTweakerActions.apply(new DistillationTowerRecipeAction(CraftTweakerMC.getLiquidStack(input), totalEu, CraftTweakerMC.getLiquidStacks(output)));
    }

    private static final class DistillationTowerRecipeAction implements IAction {

        private final FluidStack input;
        private final FluidStack[] output;
        private final ItemStack[] outputItem;
        private final int totalEu;

        DistillationTowerRecipeAction(FluidStack input, int totalEu, FluidStack[] output, ItemStack[] outputItem) {
            this.input = input;
            this.outputItem = outputItem;
            this.output = output;
            this.totalEu = totalEu;
        }

        DistillationTowerRecipeAction(FluidStack input, int totalEu, FluidStack[] output) {
            this.input = input;
            this.outputItem = null;
            this.output = output;
            this.totalEu = totalEu;
        }

        @Override
        public void apply() {
            if (totalEu <= 0) {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Eu amount must be greater then 0!!");
                return;
            }
            if (outputItem == null){
                GTCXTileMultiDistillationTower.addRecipe(input, totalEu,output[0].getUnlocalizedName() + "_ct", output);
            } else {
                GTCXTileMultiDistillationTower.addRecipe(input, totalEu,output[0].getUnlocalizedName() + "_ct", output, outputItem);
            }
        }

        @Override
        public String describe() {
            if (outputItem == null){
                return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s] to %s", input, totalEu, Arrays.deepToString(output), GTCXRecipeLists.DISTILLATION_TOWER_RECIPE_LIST);
            }
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s, %s] to %s", input, totalEu, Arrays.deepToString(output), Arrays.deepToString(outputItem), GTCXRecipeLists.DISTILLATION_TOWER_RECIPE_LIST);
        }
    }
}
