package gtc_expansion.tile.multi;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEItems;
import gtc_expansion.GEMachineGui.GEIndustrialBlastFurnaceGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerIndustrialBlastFurnace;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.util.GEIc2cECompat;
import gtc_expansion.util.GELang;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.multi.GTTileMultiBaseMachine;
import gtclassic.util.int3;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.api.classic.item.IMachineUpgradeItem.UpgradeType;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers.IRecipeModifier;
import ic2.api.classic.recipe.RecipeModifierHelpers.ModifierType;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.inventory.base.IHasInventory;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.MachineFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IClickable;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GETileMultiIndustrialBlastFurnace extends GTTileMultiBaseMachine implements IClickable {

	protected static final int[] slotInputs = { 0, 1, 2, 3 };
	protected static final int[] slotOutputs = { 4, 5, 6, 7 };
	public IFilter filter = new MachineFilter(this);
	public static final IBlockState casingReinforcedState = GEBlocks.casingReinforced.getDefaultState();
	public static final IBlockState casingStandardState = GEBlocks.casingStandard.getDefaultState();
	public static final IBlockState casingAdvancedState = GEBlocks.casingAdvanced.getDefaultState();
	public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialblastfurnace.png");
	private static final int defaultEu = 120;
	public static final int COST_MED = 128000;
	public static final int COST_HIGH = 256000;
	@NetworkField(index = 13)
	public int currentHeat = 0;
	public static final String neededHeat = "minHeat";

	private boolean kanthal = false;
	private boolean nichrome = false;

	public GETileMultiIndustrialBlastFurnace() {
		super(8, 2, defaultEu, 128);
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
		return GERecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST;
	}

	public ResourceLocation getGuiTexture() {
		return GUI_LOCATION;
	}

	@Override
	public ResourceLocation getStartSoundFile() {
		return Ic2Sounds.generatorLoop;
	}

	@Override
	public boolean checkRecipe(GTRecipeMultiInputList.MultiRecipe entry, List<ItemStack> inputs) {
		return super.checkRecipe(entry, inputs) && currentHeat >= getRequiredHeat(entry.getOutputs());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.currentHeat = nbt.getInteger("currentHeat");
		this.kanthal = nbt.getBoolean("Kanthal");
		this.nichrome = nbt.getBoolean("Nichrome");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("currentHeat", this.currentHeat);
		nbt.setBoolean("Kanthal", kanthal);
		nbt.setBoolean("Nichrome", nichrome);
		return nbt;
	}

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		ItemStack machine = GTMaterialGen.get(GEBlocks.industrialBlastFurnace);
		StackUtil.getOrCreateNbtData(machine).setBoolean("Kanthal", kanthal);
		StackUtil.getOrCreateNbtData(machine).setBoolean("Nichrome", nichrome);
		list.add(machine);

		for(int i = 0; i < this.inventory.size(); ++i) {
			ItemStack stack = this.inventory.get(i);
			if (!stack.isEmpty()) {
				list.add(stack);
			}
		}

		InventoryHandler handler = this.getHandler();
		if (handler != null) {
			IHasInventory inv = handler.getUpgradeSlots();

			for(int i = 0; i < inv.getSlotCount(); ++i) {
				ItemStack result = inv.getStackInSlot(i);
				if (result != null) {
					list.add(result);
				}
			}
		}
		return list;
	}

	public void setKanthal(boolean kanthal) {
		this.kanthal = kanthal;
	}

	public void setNichrome(boolean nichrome) {
		this.nichrome = nichrome;
	}

	public static void init() {
		/** Iron Processing **/
		addRecipe(new IRecipeInput[] { input("oreIron", 1),
				input("dustCalcite", 1) }, 1500, 12800, GTMaterialGen.getIc2(Ic2Items.refinedIronIngot, 3));
		addRecipe(new IRecipeInput[] { input("dustPyrite", 3),
				input("dustCalcite", 1) }, 1500, 12800, GTMaterialGen.getIc2(Ic2Items.refinedIronIngot, 2));
		/** Steel **/
		addRecipe(new IRecipeInput[] { input("dustSteel", 1) }, 1000, COST_MED, GTMaterialGen.getIngot(GEMaterial.Steel, 1));
		addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
				input("dustCoal", 2) }, 1000, COST_MED, GTMaterialGen.getIngot(GEMaterial.Steel, 1));
		addRecipe(new IRecipeInput[] { input("ingotRefinedIron", 1),
				input("dustCarbon", 1) }, 1000, COST_MED, GTMaterialGen.getIngot(GEMaterial.Steel, 1));
		/** Titanium **/
		addRecipe(new IRecipeInput[] {
				input("dustTitanium", 1) }, 1500, COST_MED, GTMaterialGen.getIngot(GTMaterial.Titanium, 1));
		/** Tungsten Steel **/
		addRecipe(new IRecipeInput[]{input("ingotTungsten", 1), input("ingotSteel", 1)}, 3000, COST_HIGH, GTMaterialGen.getStack(GEMaterial.TungstenSteel, GEMaterial.hotIngot, 2), GTMaterialGen.getDust(GEMaterial.DarkAshes, 4));
		/** Tungsten **/
		addRecipe(new IRecipeInput[] {
				input("dustTungsten", 1) }, 2500, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Tungsten, GEMaterial.hotIngot, 1));
		/** Iridium **/
		addRecipe(new IRecipeInput[] {
				input("dustIridium", 1) }, 2500, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Iridium, GEMaterial.hotIngot, 1));
		addRecipe(new IRecipeInput[] { input("oreIridium", 1) }, 2500, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Iridium, GEMaterial.hotIngot, 1));
		addRecipe(new IRecipeInput[] {
				input(GTMaterialGen.getIc2(Ic2Items.iridiumOre, 1)) }, 2500, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Iridium, GEMaterial.hotIngot, 1));
		/** Galena **/
		addRecipe(new IRecipeInput[] {
				input("dustGalena", 2) }, 1500, COST_MED, GTMaterialGen.getIngot(GEMaterial.Lead, 1), GTMaterialGen.getIc2(Ic2Items.silverIngot, 1));
		/** Osmium **/
		addRecipe(new IRecipeInput[] {
				input("dustOsmium", 1) }, 3000, COST_HIGH, GTMaterialGen.getStack(GEMaterial.Osmium, GEMaterial.hotIngot, 1));
		/** Thorium **/
		addRecipe(new IRecipeInput[] {
				input("dustThorium", 1) }, 1500, COST_HIGH, GTMaterialGen.getIngot(GEMaterial.Thorium, 1));
		/** Chrome **/
		addRecipe(new IRecipeInput[] {
				input("dustChrome", 1) }, 1700, COST_HIGH, GTMaterialGen.getIngot(GEMaterial.Chrome, 1));
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
		addRecipe(inlist, new MachineOutput(mods, outlist));
	}

	static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
		GERecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
	}

	public static void removeRecipe(String id) {
		GERecipeLists.INDUSTRIAL_BLAST_FURNACE_RECIPE_LIST.removeRecipe(id);
	}

	private Map<BlockPos, IBlockState> stateMap = new Object2ObjectLinkedOpenHashMap<>();

	@Override
	public Map<BlockPos, IBlockState> provideStructure() {
		Map<BlockPos, IBlockState> states = super.provideStructure();
		for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
			states.put(entry.getKey(), entry.getValue());
		}
		return states;
	}

	@Override
	public boolean checkStructure() {
		if (!world.isAreaLoaded(pos, 3)) {
			return false;
		}
		// resetting the heat value to avoid continous upcount
		currentHeat = nichrome ? 1000 : (kanthal ? 500 : 0);
		stateMap.clear();
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
		return world.getBlockState(pos.asBlockPos()) == Blocks.AIR.getDefaultState() || world.getBlockState(pos.asBlockPos()) == Blocks.FLOWING_LAVA.getDefaultState();
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
	public boolean hasRightClick() {
		return true;
	}

	@Override
	public boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing facing, Side side) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getCount() >= 4){
			if (stack.getItem() == GEItems.kanthalHeatingCoil && !kanthal){
				stack.shrink(4);
				kanthal = true;
				return true;
			}
			if (kanthal && stack.getItem() == GEItems.nichromeHeatingCoil && !nichrome){
				stack.shrink(4);
				nichrome = true;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasLeftClick() {
		return false;
	}

	@Override
	public void onLeftClick(EntityPlayer entityPlayer, Side side) {

	}
}