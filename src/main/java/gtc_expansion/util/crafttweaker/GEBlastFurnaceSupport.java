package gtc_expansion.util.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.tile.multi.GETileMultiIndustrialBlastFurnace;
import gtc_expansion.tile.multi.GETileMultiPrimitiveBlastFurnace;
import gtclassic.util.crafttweaker.GTCraftTweakerActions;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Locale;

@ZenClass("mods.gtclassic.BlastFurnace")
@ZenRegister
public class GEBlastFurnaceSupport {

	@ZenMethod
	public static void addPrimitiveRecipe(IItemStack[] output, IIngredient[] input1, boolean inherit, @Optional(valueLong = 12000L) int totalTicks) {
		GTCraftTweakerActions.apply(new PrimitiveBlastFurnaceRecipeAction(GTCraftTweakerActions.of(input1), totalTicks, CraftTweakerMC.getItemStacks(output)));
		if (inherit){
			GTCraftTweakerActions.apply(new IndustrialBlastFurnaceRecipeAction(GTCraftTweakerActions.of(input1), 1000, totalTicks, CraftTweakerMC.getItemStacks(output)));
		}
	}

	@ZenMethod
	public static void addIndustrialRecipe(IItemStack[] output, IIngredient[] input1, int requiredHeat, @Optional(valueLong = 12000L) int totalEu) {
		GTCraftTweakerActions.apply(new IndustrialBlastFurnaceRecipeAction(GTCraftTweakerActions.of(input1), requiredHeat, totalEu, CraftTweakerMC.getItemStacks(output)));
	}

	private static final class IndustrialBlastFurnaceRecipeAction implements IAction {

		private final IRecipeInput[] input;
		private final int totalEu;
		private final int requiredHeat;
		private final ItemStack[] output;

		IndustrialBlastFurnaceRecipeAction(IRecipeInput[] input, int requiredHeat, int totalEu, ItemStack... output) {
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
			return String.format(Locale.ENGLISH, "Add Recipe[%s, %s, %s, -> %s] to %s", Arrays.deepToString(this.input), requiredHeat, totalEu, Arrays.deepToString(this.output), GERecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST);
		}
	}

	private static final class PrimitiveBlastFurnaceRecipeAction implements IAction {

		private final IRecipeInput[] input;
		private final int totalTicks;
		private final ItemStack[] output;

		PrimitiveBlastFurnaceRecipeAction(IRecipeInput[] input, int totalTicks, ItemStack... output) {
			this.input = input;
			this.totalTicks = totalTicks;
			this.output = output;
		}

		@Override
		public void apply() {
			if (input.length > 4) {
				CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
						+ "Recipe can only have a max of four inputs!");
			} else if (totalTicks <= 0) {
				CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
						+ "Tick amount must be greater then 0!!");
			} else {
				GETileMultiPrimitiveBlastFurnace.addRecipe(input, totalTicks, output);
			}
		}

		@Override
		public String describe() {
			return String.format(Locale.ENGLISH, "Add Recipe[%s, %s -> %s] to %s", Arrays.deepToString(this.input), totalTicks, Arrays.deepToString(this.output), GERecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST);
		}
	}
}
