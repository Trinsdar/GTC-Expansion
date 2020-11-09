package gtc_expansion.tile.multi;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerIndustrialBlastFurnace;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.data.GTCXLang;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.multi.GTTileMultiBaseMachine;
import ic2.api.classic.item.IMachineUpgradeItem.UpgradeType;
import ic2.api.classic.network.adv.NetworkField;
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
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

public class GTCXTileMultiIndustrialBlastFurnace extends GTTileMultiBaseMachine implements IGTDebuggableTile {

	protected static final int[] slotInputs = { 0, 1, 2, 3 };
	protected static final int[] slotOutputs = { 4, 5, 6, 7 };
	public static final int slotCoil = 8;
	public IFilter filter = new MachineFilter(this);
	public static final IBlockState casingReinforcedState = GTCXBlocks.casingReinforced.getDefaultState();
	public static final IBlockState casingStandardState = GTCXBlocks.casingStandard.getDefaultState();
	public static final IBlockState casingAdvancedState = GTCXBlocks.casingAdvanced.getDefaultState();
	public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialblastfurnace.png");
	private static final int defaultEu = 120;
	public static final int COST_MED = 72000;
	public static final int COST_HIGH = 204000;
	public static final int COST_EXTREME = 1200000;
	@NetworkField(index = 13)
	public int currentHeat = 0;
	public int baseHeat = 0;
	public static final String neededHeat = "minHeat";

	public GTCXTileMultiIndustrialBlastFurnace() {
		super(9, 2, defaultEu, 128);
		maxEnergy = 8192;
		this.addGuiFields("currentHeat");
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
		return GTCXLang.INDUSTRIAL_BLAST_FURNACE;
	}

	@Override
	public Set<UpgradeType> getSupportedTypes() {
		return new LinkedHashSet<>(Arrays.asList(UpgradeType.ImportExport, UpgradeType.RedstoneControl, UpgradeType.Sounds, UpgradeType.MachineModifierA, UpgradeType.MachineModifierB));
	}

	@Override
	public ContainerIC2 getGuiContainer(EntityPlayer player) {
		return new GTCXContainerIndustrialBlastFurnace(player.inventory, this);
	}

	@Override
	public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
		return GTCXMachineGui.GTCXIndustrialBlastFurnaceGui.class;
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
	public void setStackInSlot(int slot, ItemStack stack) {
		super.setStackInSlot(slot, stack);
		if (slot == slotCoil){
			baseHeat = 0;
			if (!stack.isEmpty()){
				if (stack.getItem() == GTCXItems.kanthalHeatingCoil){
					baseHeat = 125 * stack.getCount();
				} else {
					baseHeat = 250 * stack.getCount();
				}
			}
		}
	}

	@Override
	public GTRecipeMultiInputList getRecipeList() {
		return GTCXRecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST;
	}

	public ResourceLocation getGuiTexture() {
		return GUI_LOCATION;
	}

	@Override
	public ResourceLocation getStartSoundFile() {
		return GTCExpansion.getAprilFirstSound(Ic2Sounds.generatorLoop);
	}

	@Override
	public boolean checkRecipe(GTRecipeMultiInputList.MultiRecipe entry, List<ItemStack> inputs) {
		return super.checkRecipe(entry, inputs) && currentHeat >= getRequiredHeat(entry.getOutputs());
	}

	@Override
	public int getMaxStackSize(int slot) {
		if (slot == slotCoil){
			return 4;
		}
		return super.getMaxStackSize(slot);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.currentHeat = nbt.getInteger("currentHeat");
		this.baseHeat = nbt.getInteger("baseHeat");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("currentHeat", this.currentHeat);
		nbt.setInteger("baseHeat", this.baseHeat);
		return nbt;
	}

	public int getCurrentHeat() {
		return currentHeat;
	}

	public static void init() {
		/* Iron Processing **/
		addRecipe(new IRecipeInput[] { input("oreIron", 1),
				input("dustCalcite", 1) }, 1500, 12800, GTMaterialGen.get(Items.IRON_INGOT, 3));
		addRecipe(new IRecipeInput[] { input("dustPyrite", 3),
				input("dustCalcite", 1) }, 1500, 12800, GTMaterialGen.get(Items.IRON_INGOT, 2));
		/* Steel **/
		addRecipe(new IRecipeInput[] { input("dustSteel", 1) }, 1000, COST_MED, GTMaterialGen.getIngot(GTCXMaterial.Steel, 1));
		addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
				input("dustCoal", 2) }, 1000, COST_MED, GTMaterialGen.getIngot(GTCXMaterial.Steel, 1));
		addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
				input("dustCarbon", 1) }, 1000, COST_MED, GTMaterialGen.getIngot(GTCXMaterial.Steel, 1));
		addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
				input("dustCoke", 1) }, 1000, COST_MED, GTMaterialGen.getIngot(GTCXMaterial.Steel, 1));
		/* Titanium **/
		addRecipe(new IRecipeInput[] {
				input("dustTitanium", 1) }, 1500, COST_HIGH, GTMaterialGen.getIngot(GTMaterial.Titanium, 1));
		/* Tungsten Steel **/
		addRecipe(new IRecipeInput[]{input("ingotTungsten", 1), input("ingotSteel", 1)}, 3000, COST_HIGH, GTMaterialGen.getHotIngot(GTCXMaterial.TungstenSteel, 2), GTMaterialGen.getDust(GTCXMaterial.DarkAshes, 4));
		addRecipe(new IRecipeInput[]{input("dustTungstensteel", 1)}, 3000, COST_HIGH, GTMaterialGen.getHotIngot(GTCXMaterial.TungstenSteel, 1));
		/* Tungsten **/
		addRecipe(new IRecipeInput[] {
				input("dustTungsten", 1) }, 2500, COST_EXTREME, GTMaterialGen.getHotIngot(GTMaterial.Tungsten, 1));
		/* Iridium **/
		addRecipe(new IRecipeInput[] {
				input("dustIridium", 1) }, 3000, COST_EXTREME, GTMaterialGen.getHotIngot(GTMaterial.Iridium, 1));
		addRecipe(new IRecipeInput[] { input("oreIridium", 1) }, 2500, COST_EXTREME, GTMaterialGen.getHotIngot(GTMaterial.Iridium, 1));
		addRecipe(new IRecipeInput[] {
				input(GTMaterialGen.getIc2(Ic2Items.iridiumOre, 1)) }, 2500, COST_EXTREME, GTMaterialGen.getHotIngot(GTMaterial.Iridium, 1));
		/* Galena **/
		addRecipe(new IRecipeInput[] {
				input("dustGalena", 2) }, 1500, COST_MED, GTMaterialGen.getIngot(GTCXMaterial.Lead, 1), GTMaterialGen.getIc2(Ic2Items.silverIngot, 1));
		/* Osmium **/
		addRecipe(new IRecipeInput[] {
				input("dustOsmiumGT", 1) }, 3000, COST_EXTREME, GTMaterialGen.getHotIngot(GTCXMaterial.Osmium, 1));
		/* Chrome **/
		addRecipe(new IRecipeInput[] {
				input("dustChrome", 1), input(GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1)) }, 1700, COST_HIGH, GTMaterialGen.getIngot(GTMaterial.Chrome, 1));

		/* Osmiridium **/
		addRecipe(new IRecipeInput[]{input("ingotOsmiumGT", 1), input("ingotIridium", 1)}, 3000, COST_EXTREME, GTMaterialGen.getHotIngot(GTCXMaterial.Osmiridium, 2));

		/* Stainless Steel **/
		addRecipe(new IRecipeInput[]{metal("Iron", 6), metal("Nickel", 1), metal("Chrome", 1), metal("Manganese", 1)}, 1700, COST_HIGH * 6, GTMaterialGen.getIngot(GTCXMaterial.StainlessSteel, 9));
		addRecipe(new IRecipeInput[]{input("dustStainlessSteel", 1)}, 1700, COST_HIGH, GTMaterialGen.getIngot(GTCXMaterial.StainlessSteel, 1));
		/* Kanthal **/
		addRecipe(new IRecipeInput[]{new RecipeInputCombined(1, input("ingotIron", 1), input("ingotRefinedIron", 1)), input("ingotAluminium", 1), input("ingotChrome", 1)}, 2200, COST_HIGH*3, GTMaterialGen.getHotIngot(GTCXMaterial.Kanthal, 3));
		addRecipe(new IRecipeInput[]{input("dustKanthal", 1)}, 2200, COST_HIGH, GTMaterialGen.getHotIngot(GTCXMaterial.Kanthal, 1));

		/* Nichrome **/
		addRecipe(new IRecipeInput[]{metal("Nickel", 4), metal("Chrome", 1), input(GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 2))}, 2500, COST_HIGH*6, GTMaterialGen.getHotIngot(GTCXMaterial.Nichrome, 5));
		addRecipe(new IRecipeInput[]{input("dustNichrome", 1)}, 2500, COST_HIGH*2, GTMaterialGen.getHotIngot(GTCXMaterial.Nichrome, 1));

		/* Aluminium **/
		IRecipeInput aluminium = new RecipeInputCombined(1, new RecipeInputOreDict("dustAluminum"), new RecipeInputOreDict("dustAluminium"));
		addRecipe(new IRecipeInput[]{aluminium}, 1000, COST_MED, GTMaterialGen.getIngot(GTMaterial.Aluminium, 1));

		/* Silicon **/
		addRecipe(new IRecipeInput[]{input("dustSilicon", 1)}, 1000, COST_MED, GTMaterialGen.getIngot(GTMaterial.Silicon, 1));
		addRecipe(new IRecipeInput[]{input("dustSiliconDioxide", 1)}, 2000, COST_MED, GTMaterialGen.get(GTCXBlocks.pureGlass, 1));
	}

	public static int getRequiredHeat(MachineOutput output) {
		if (output == null || output.getMetadata() == null) {
			return 1;
		}
		return output.getMetadata().getInteger(neededHeat);
	}

	public static IRecipeModifier[] totalEu(int total) {
		return new IRecipeModifier[] { ModifierType.RECIPE_LENGTH.create((total / defaultEu) - 100) };
	}

	public static void addRecipe(IRecipeInput[] inputs, int heat, int totalEu, ItemStack... outputs) {
		addRecipe(inputs, heat, totalEu, outputs[0].getUnlocalizedName(), outputs);
	}

	public static void addRecipe(IRecipeInput[] inputs, int heat, int totalEu, String recipeID, ItemStack... outputs) {
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
		mods.setInteger(neededHeat, heat);
		for (ItemStack output : outputs) {
			outlist.add(output);
		}
		addRecipe(inlist, new MachineOutput(mods, outlist), recipeID);
	}

	static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeId) {
		GTCXRecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST.addRecipe(input, output, recipeId, defaultEu);
	}

	public static void removeRecipe(String id) {
		GTCXRecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST.removeRecipe(id);
	}

	private Map<BlockPos, IBlockState> stateMap = new Object2ObjectLinkedOpenHashMap<>();

	/*@Override
	public Map<BlockPos, IBlockState> provideStructure() {
		Map<BlockPos, IBlockState> states = super.provideStructure();
		for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
			states.put(entry.getKey(), entry.getValue());
		}
		return states;
	}*/

	@Override
	public boolean checkStructure() {
		if (!world.isAreaLoaded(pos, 3)) {
			return false;
		}
		// resetting the heat value to avoid continous upcount
		currentHeat = baseHeat;
		this.getNetwork().updateTileGuiField(this, "currentHeat");
		stateMap.clear();
		// we doing it "big math" style not block by block
		int3 dir = new int3(getPos(), getFacing());
		if (!isCasing(dir.back(1))){
			return false;
		}
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
		if (!isAirOrLava(dir.down(1))){
			return false;
		}
		if (!isAirOrLava(dir.down(1))){
			return false;
		}
		if (!(isCasing(dir.down(1)))) {
			return false;
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

	public boolean isAirOrLava(int3 pos){
		if (world.getBlockState(pos.asBlockPos()) == Blocks.LAVA.getDefaultState()){
			currentHeat += 250;
			this.getNetwork().updateTileGuiField(this, "currentHeat");
			return true;
		}
		return world.isAirBlock(pos.asBlockPos()) || world.getBlockState(pos.asBlockPos()) == Blocks.FLOWING_LAVA.getDefaultState();
	}

	public boolean isCasing(int3 pos) {
		if (world.getBlockState(pos.asBlockPos()) == casingStandardState){
			currentHeat += 30;
			this.getNetwork().updateTileGuiField(this, "currentHeat");
			stateMap.put(pos.asBlockPos(), casingStandardState);
			return true;
		}
		if (world.getBlockState(pos.asBlockPos()) == casingReinforcedState){
			currentHeat += 50;
			this.getNetwork().updateTileGuiField(this, "currentHeat");
			stateMap.put(pos.asBlockPos(), casingReinforcedState);
			return true;
		}
		if (world.getBlockState(pos.asBlockPos()) == casingAdvancedState){
			currentHeat +=70;
			this.getNetwork().updateTileGuiField(this, "currentHeat");
			stateMap.put(pos.asBlockPos(), casingAdvancedState);
			return true;
		}
		return false;
	}

	@Override
	public void getData(Map<String, Boolean> map) {
		map.put("Heat: " + currentHeat + "K", true);
	}
}
