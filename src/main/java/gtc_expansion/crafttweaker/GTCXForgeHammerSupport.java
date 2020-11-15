package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileLathe;
import gtc_expansion.tile.steam.GTCXTileSteamForgeHammer;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

@ZenClass("mods.gtclassic.ForgeHammer")
@ZenRegister
public class GTCXForgeHammerSupport {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input, @Optional(valueLong = 20L) int totalTime){
        GTCraftTweakerActions.apply(new ForgeHammerRecipeAction(GTCraftTweakerActions.of(input), totalTime, CraftTweakerMC.getItemStack(output)));
    }

    private static final class ForgeHammerRecipeAction implements IAction {

        private final IRecipeInput input1;
        private final int totalTime;
        private final ItemStack output;

        ForgeHammerRecipeAction(IRecipeInput input1, int totalTime, ItemStack output) {
            this.input1 = input1;
            this.totalTime = totalTime;
            this.output = output;
        }

        @Override
        public void apply() {
            if (totalTime <= 0) {
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Amount of time must be greater then 0!!");
                return;
            }
            GTCXTileSteamForgeHammer.addRecipe(input1, GTCXTileSteamForgeHammer.totalTime(totalTime), output, output.getUnlocalizedName() + "_ct");
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s] to %s", input1, totalTime, output, GTCXRecipeLists.FORGE_HAMMER_RECIPE_LIST);
        }
    }
}
