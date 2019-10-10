package gtc_expansion.tile.multi;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEMachineGui.GEIndustrialBlastFurnaceGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerIndustrialBlastFurnace;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.util.GELang;
import gtclassic.GTBlocks;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.multi.GTTileMultiBaseMachine;
import gtclassic.util.int3;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.api.classic.item.IMachineUpgradeItem.UpgradeType;
import ic2.api.classic.recipe.RecipeModifierHelpers.IRecipeModifier;
import ic2.api.classic.recipe.RecipeModifierHelpers.ModifierType;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.MachineFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GETileMultiIndustrialBlastFurnace extends GTTileMultiBaseMachine {

	protected static final int[] slotInputs = { 0, 1, 2, 3 };
	protected static final int[] slotOutputs = { 4, 5, 6, 7 };
	public IFilter filter = new MachineFilter(this);
	public static final IBlockState casingState = GEBlocks.casingReinforced.getDefaultState();
	public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialblastfurnace.png");
	private static final int defaultEu = 120;
	public static final int COST_MED = 128000;
	public static final int COST_HIGH = 256000;

	public GETileMultiIndustrialBlastFurnace() {
		super(8, 2, defaultEu, 128);
		maxEnergy = 8192;
	}

	@Override
	protected void addSlots(InventoryHandler handler) {
		handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
		handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
		handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
		handler.registerDefaultSlotsForSide(RotationList.DOWN.invert(), slotInputs);
		handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutputs);
		handler.registerInputFilter(filter, slotInputs);
		handler.registerSlotType(SlotType.Input, slotInputs);
		handler.registerSlotType(SlotType.Output, slotOutputs);
	}

	@Override
	public LocaleComp getBlockName() {
		return GELang.INDUSTRIAL_BLAST_FURNACE;
	}

	@Override
	public Set<UpgradeType> getSupportedTypes() {
		return new LinkedHashSet<>(Arrays.asList(UpgradeType.values()));
	}

	@Override
	public ContainerIC2 getGuiContainer(EntityPlayer player) {
		return new GEContainerIndustrialBlastFurnace(player.inventory, this);
	}

	@Override
	public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
		return GEIndustrialBlastFurnaceGui.class;
	}

	@Override
	public int[] getInputSlots() {
		return slotInputs;
	}

	@Override
	public IFilter[] getInputFilters(int[] slots) {
		return new IFilter[] { filter };
	}

	@Override
	public boolean isRecipeSlot(int slot) {
		return slot <= 3;
	}

	@Override
	public int[] getOutputSlots() {
		return slotOutputs;
	}

	@Override
	public GTRecipeMultiInputList getRecipeList() {
		return GERecipeLists.INDUSTRIAL_BLAST_FURNACE;
	}

	public ResourceLocation getGuiTexture() {
		return GUI_LOCATION;
	}

	@Override
	public ResourceLocation getStartSoundFile() {
		return Ic2Sounds.generatorLoop;
	}

	public static void init() {
		/** Iron Processing **/
		addRecipe(new IRecipeInput[] { input("oreIron", 1),
				input("dustCalcite", 1) }, 12800, GTMaterialGen.getIc2(Ic2Items.refinedIronIngot, 3));
		addRecipe(new IRecipeInput[] { input("dustPyrite", 3),
				input("dustCalcite", 1) }, 12800, GTMaterialGen.getIc2(Ic2Items.refinedIronIngot, 2));
		/** Steel **/
		addRecipe(new IRecipeInput[] { input("dustSteel", 1) }, COST_MED, GTMaterialGen.getIngot(GEMaterial.Steel, 1));
		addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
				input("dustCoal", 2) }, COST_MED, GTMaterialGen.getIngot(GEMaterial.Steel, 1));
		addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
				input("dustCarbon", 1) }, COST_MED, GTMaterialGen.getIngot(GEMaterial.Steel, 1));
		/** Titanium **/
		addRecipe(new IRecipeInput[] {
				input("dustTitanium", 1) }, COST_MED, GTMaterialGen.getIngot(GTMaterial.Titanium, 1));
		/** Tungsten Steel **/
		addRecipe(new IRecipeInput[]{input("ingotTungsten", 1), input("ingotSteel", 1)}, COST_HIGH, GTMaterialGen.getStack(GEMaterial.TungstenSteel, GEMaterial.hotIngot, 2), GTMaterialGen.getDust(GEMaterial.DarkAshes, 4));
		/** Tungsten **/
		addRecipe(new IRecipeInput[] {
				input("dustTungsten", 1) }, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Tungsten, GEMaterial.hotIngot, 1));
		/** Iridium **/
		addRecipe(new IRecipeInput[] {
				input("dustIridium", 1) }, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Iridium, GEMaterial.hotIngot, 1));
		addRecipe(new IRecipeInput[] { input("oreIridium", 1) }, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Iridium, GEMaterial.hotIngot, 1));
		addRecipe(new IRecipeInput[] {
				input(GTMaterialGen.getIc2(Ic2Items.iridiumOre, 1)) }, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Iridium, GEMaterial.hotIngot, 1));
		/** Galena **/
		addRecipe(new IRecipeInput[] {
				input("dustGalena", 2) }, COST_MED, GTMaterialGen.getIngot(GEMaterial.Lead, 1), GTMaterialGen.getIc2(Ic2Items.silverIngot, 1));
		/** Osmium **/
		addRecipe(new IRecipeInput[] {
				input("dustOsmium", 1) }, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Osmium, GEMaterial.hotIngot, 1));
		/** Thorium **/
		addRecipe(new IRecipeInput[] {
				input("dustThorium", 1) }, COST_HIGH, GTMaterialGen.getIngot(GEMaterial.Thorium, 1));
	}

	public static IRecipeModifier[] totalEu(int total) {
		return new IRecipeModifier[] { ModifierType.RECIPE_LENGTH.create((total / defaultEu) - 100) };
	}

	public static void addRecipe(IRecipeInput[] inputs, int totalEu, ItemStack... outputs) {
		List<IRecipeInput> inlist = new ArrayList<>();
		List<ItemStack> outlist = new ArrayList<>();
		IRecipeModifier[] modifiers = totalEu(totalEu);
		for (IRecipeInput input : inputs) {
			inlist.add(input);
		}
		NBTTagCompound mods = new NBTTagCompound();
		for (IRecipeModifier modifier : modifiers) {
			modifier.apply(mods);
		}
		for (ItemStack output : outputs) {
			outlist.add(output);
		}
		addRecipe(inlist, new MachineOutput(mods, outlist));
	}

	static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
		GERecipeLists.INDUSTRIAL_BLAST_FURNACE.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
	}

	public static void removeRecipe(String id) {
		GERecipeLists.INDUSTRIAL_BLAST_FURNACE.removeRecipe(id);
	}

	@Override
	public Map<BlockPos, IBlockState> provideStructure() {
		Map<BlockPos, IBlockState> states = super.provideStructure();
		int3 dir = new int3(getPos(), getFacing());
		for (int i = 0; i < 3; i++) {// above tile
			states.put(dir.up(1).asBlockPos(), casingState);
		}
		states.put(dir.left(1).asBlockPos(), casingState);
		for (int i = 0; i < 3; i++) {
			states.put(dir.down(1).asBlockPos(), casingState);
		}
		states.put(dir.back(1).asBlockPos(), casingState);
		for (int i = 0; i < 3; i++) {
			states.put(dir.up(1).asBlockPos(), casingState);
		}
		states.put(dir.right(1).asBlockPos(), casingState);
		for (int i = 0; i < 3; i++) {
			states.put(dir.down(1).asBlockPos(), casingState);
		}
		states.put(dir.right(1).asBlockPos(), casingState);
		for (int i = 0; i < 3; i++) {
			states.put(dir.up(1).asBlockPos(), casingState);
		}
		states.put(dir.back(1).asBlockPos(), casingState);
		for (int i = 0; i < 3; i++) {
			states.put(dir.down(1).asBlockPos(), casingState);
		}
		states.put(dir.left(1).asBlockPos(), casingState);
		for (int i = 0; i < 3; i++) {
			states.put(dir.up(1).asBlockPos(), casingState);
		}
		states.put(dir.left(1).asBlockPos(), casingState);
		for (int i = 0; i < 3; i++) {
			states.put(dir.down(1).asBlockPos(), casingState);
		}
		states.put(dir.forward(2).right(2).asBlockPos(), casingState);
		for (int i = 0; i < 3; i++) {
			states.put(dir.up(1).asBlockPos(), casingState);
		}
		return states;
	}

	@Override
	public boolean checkStructure() {
		if (!world.isAreaLoaded(pos, 3)) {
			return false;
		}
		// we doing it "big math" style not block by block
		int3 dir = new int3(getPos(), getFacing());
		for (int i = 0; i < 3; i++) {// above tile
			if (!(isCasing(dir.up(1)))) {
				return false;
			}
		}
		if (!isCasing(dir.left(1))) {// left
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (!(isCasing(dir.down(1)))) {
				return false;
			}
		}
		if (!isCasing(dir.back(1))) {// back
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (!(isCasing(dir.up(1)))) {
				return false;
			}
		}
		if (!isCasing(dir.right(1))) {// right
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (!(isCasing(dir.down(1)))) {
				return false;
			}
		}
		if (!isCasing(dir.right(1))) {// right
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (!(isCasing(dir.up(1)))) {
				return false;
			}
		}
		if (!isCasing(dir.back(1))) {// back
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (!(isCasing(dir.down(1)))) {
				return false;
			}
		}
		if (!isCasing(dir.left(1))) {// left
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (!(isCasing(dir.up(1)))) {
				return false;
			}
		}
		if (!isCasing(dir.left(1))) {// left
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (!(isCasing(dir.down(1)))) {
				return false;
			}
		}
		if (!isCasing(dir.forward(2).right(2))) {// missing front right column
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (!(isCasing(dir.up(1)))) {
				return false;
			}
		}
		return true;
	}

	public boolean isCasing(int3 pos) {
		return world.getBlockState(pos.asBlockPos()) == casingState;
	}
}