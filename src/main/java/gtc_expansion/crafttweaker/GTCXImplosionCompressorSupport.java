package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.multi.GTCXTileMultiImplosionCompressor;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Locale;

@ZenClass("mods.gtclassic.ImplosionCompressor")
@ZenRegister
public class GTCXImplosionCompressorSupport {
    @ZenMethod
    public static void addRecipe(IItemStack[] output, IIngredient input1, int tnt, @Optional(valueLong = 2560L)int totalEu){
        GTCraftTweakerActions.apply(new ImplosionCompressorRecipeAction(GTCraftTweakerActions.of(input1), tnt, totalEu, CraftTweakerMC.getItemStacks(output)));
    }

    private static final class ImplosionCompressorRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final int tnt;
        private final int totalEu;
        private final ItemStack[] output;

        ImplosionCompressorRecipeAction(IRecipeInput input1, int tnt, int totalEu, ItemStack... output) {
            this.input1 = input1;
            this.tnt = tnt;
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
            GTCXTileMultiImplosionCompressor.addRecipe(input1, tnt, totalEu, output[0].getUnlocalizedName() + "_ct", output);
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s, %s -> %s] to %s", input1, tnt, totalEu, Arrays.deepToString(output), GTCXRecipeLists.IMPLOSION_COMPRESSOR_RECIPE_LIST);
        }
    }
}
