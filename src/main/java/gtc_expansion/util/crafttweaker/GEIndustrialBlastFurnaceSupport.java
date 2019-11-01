package gtc_expansion.util.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.tile.multi.GETileMultiIndustrialBlastFurnace;
import gtclassic.util.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Locale;

@ZenClass("mods.gtclassic.IndustrialBlastFurnace")
@ZenRegister
public class GEIndustrialBlastFurnaceSupport {

	@ZenMethod
	public static void addRecipe(IItemStack[] output, IIngredient[] input1, int requiredHeat, @Optional(valueLong = 12000L) int totalEu) {
		GTCraftTweakerActions.apply(new BlastFurnaceRecipeAction(GTCraftTweakerActions.of(input1), requiredHeat, totalEu, CraftTweakerMC.getItemStacks(output)));
	}

	private static final class BlastFurnaceRecipeAction implements IAction {

		private final IRecipeInput[] input;
		private final int totalEu;
		private final int requiredHeat;
		private final ItemStack[] output;

		BlastFurnaceRecipeAction(IRecipeInput[] input, int requiredHeat, int totalEu, ItemStack... output) {
			this.input = input;
			this.requiredHeat = requiredHeat;
			this.totalEu = totalEu;
			this.output = output;
		}

		@Override
		public void apply() {
			if (input.length > 4) {
				CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
						+ "Recipe can only have a max of four inputs!");
			} else if (totalEu <= 0) {
				CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
						+ "Eu amount must be greater then 0!!");
			} else if (requiredHeat > 3880){
				CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine()+ " > "
						+ "Required heat cannot be greater then the max heat of the ibf!!!");
			}else {
				GETileMultiIndustrialBlastFurnace.addRecipe(input, requiredHeat, totalEu, output);
			}
		}

		@Override
		public String describe() {
			return String.format(Locale.ENGLISH, "Add Recipe[%s -> %s] to %s", Arrays.deepToString(this.input), Arrays.deepToString(this.output), GERecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST);
		}
	}
}
