package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileDieselGenerator;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import net.minecraftforge.fluids.Fluid;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

@ZenClass("mods.gtclassic.DieselGenerator")
@ZenRegister
public class GTCXDieselGeneratorSupport {

    @ZenMethod
    public static void addEntry(ILiquidStack input, int ticks, int euPerTick){
        GTCraftTweakerActions.apply(new DieselGeneratorRecipeAction(CraftTweakerMC.getLiquidStack(input).getFluid(), ticks, euPerTick));
    }

    private static final class DieselGeneratorRecipeAction implements IAction {

        private final Fluid input;
        private final int euPerTick;
        private final int ticks;

        DieselGeneratorRecipeAction(Fluid input, int ticks, int euPerTick) {
            this.input = input;
            this.ticks = ticks;
            this.euPerTick = euPerTick;
        }

        @Override
        public void apply() {
            if (euPerTick <= 0) {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Eu Per Tick must be greater then 0!!");
                return;
            }
            if (ticks <= 0) {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Tick duration must be greater then 0!!");
                return;
            }
            GTCXTileDieselGenerator.addRecipe(this.input, this.ticks, this.euPerTick, this.input.getUnlocalizedName() + "_ct");
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s for %s eu/tick for %s ticks] to %s", input, euPerTick, ticks, GTCXRecipeLists.DIESEL_GEN_RECIPE_LIST);
        }
    }
}
