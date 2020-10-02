package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GTCXContainerCentrifuge;
import gtc_expansion.material.GTCXMaterial;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTFluidMachineOutput;
import gtclassic.api.recipe.GTRecipeMachineHandler;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTLang;
import gtclassic.common.gui.GTGuiMachine.GTIndustrialCentrifugeGui;
import gtclassic.common.tile.GTTileCentrifuge;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.item.IMachineUpgradeItem.UpgradeType;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers.IRecipeModifier;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.fluid.IC2Tank;
import ic2.core.fluid.LayeredFluidTank;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.MachineFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static gtc_expansion.data.GTCXValues.EMPTY;

public class GTCXTileCentrifuge extends GTTileBaseMachine implements ITankListener, IClickable, IFluidHandler {

	public static final int SLOT_FUEL = 15;
	public static final int SLOT_TANK = 14;
	public static final String NBT_TANK_IN = "inputTank";
	public static final String NBT_TANK_OUT = "outputTank";
	protected static final int[] SLOT_INPUTS = { 0, 1 };
	protected static final int[] SLOT_OUTPUTS = { 2, 3, 4, 5, 6, 7 };
	public static final List<Fluid> VALID_FLUIDS = new ArrayList<>();
	@NetworkField(index = 13)
	private IC2Tank inputTank = new IC2Tank(32000){
		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return super.canFillFluidType(fluid) && VALID_FLUIDS.contains(fluid.getFluid());
		}
	};
	@NetworkField(index = 14)
	private final LayeredFluidTank outputTank = new LayeredFluidTank(96000);
	public IFilter filter;
	public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialcentrifuge.png");
	private static final int EU_TICK = 16;
	protected static final ItemStack BLOCK_RED_SAND = new ItemStack(Blocks.SAND, 32, 1);

	public GTCXTileCentrifuge() {
		super(16, 4, EU_TICK, 100, 32);
		setFuelSlot(SLOT_FUEL);
		this.inputTank.addListener(this);
		this.outputTank.addListener(this);
		this.addNetworkFields(NBT_TANK_IN);
		this.maxEnergy = 10000;
	}

	@Override
	protected void addSlots(InventoryHandler handler) {
		this.filter = new MachineFilter(this);
		handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
		handler.registerDefaultSlotAccess(AccessRule.Both, SLOT_FUEL);
		handler.registerDefaultSlotAccess(AccessRule.Import, SLOT_INPUTS);
		handler.registerDefaultSlotAccess(AccessRule.Export, SLOT_OUTPUTS);
		handler.registerDefaultSlotsForSide(RotationList.UP, 0);
		handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, 1);
		handler.registerDefaultSlotsForSide(RotationList.UP.invert(), SLOT_OUTPUTS);
		handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), SLOT_FUEL);
		handler.registerInputFilter(filter, SLOT_INPUTS);
		handler.registerOutputFilter(CommonFilters.NotDischargeEU, SLOT_FUEL);
		handler.registerSlotType(SlotType.Fuel, SLOT_FUEL);
		handler.registerSlotType(SlotType.Input, SLOT_INPUTS);
		handler.registerSlotType(SlotType.Output, SLOT_OUTPUTS);
	}

	@Override
	public LocaleComp getBlockName() {
		return GTLang.INDUSTRIAL_CENTRIFUGE;
	}

	@Override
	public void onTankChanged(IFluidTank tank) {
		this.getNetwork().updateTileGuiField(this, NBT_TANK_IN);
		this.setStackInSlot(SLOT_TANK, ItemDisplayIcon.createWithFluidStack(this.inputTank.getFluid()));
		for (int i = 0; i < 6; i++) {
			if (i < outputTank.getTankProperties().length) {
				this.setStackInSlot( 8 + i, ItemDisplayIcon.createWithFluidStack(this.outputTank.getTankProperties()[i].getContents()));
			} else {
				this.setStackInSlot(8 + i, ItemStack.EMPTY);
			}
		}
		this.getNetwork().updateTileGuiField(this, NBT_TANK_OUT);
		shouldCheckRecipe = true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.inputTank.readFromNBT(nbt.getCompoundTag(NBT_TANK_IN));
		this.outputTank.readFromNBT(nbt.getCompoundTag(NBT_TANK_OUT));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		this.inputTank.writeToNBT(this.getTag(nbt, NBT_TANK_IN));
		this.outputTank.writeToNBT(this.getTag(nbt, NBT_TANK_OUT));
		return nbt;
	}

	@Override
	public void process(MultiRecipe recipe) {
		MachineOutput output = recipe.getOutputs().copy();
		for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
			if (!(stack.getItem() instanceof ItemDisplayIcon)){
				outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
			}
			onRecipeComplete();
		}
		if (output instanceof GTFluidMachineOutput){
			GTFluidMachineOutput fluidOutput = (GTFluidMachineOutput) output;
			for (FluidStack fluid : fluidOutput.getFluids()){
				outputTank.fill(fluid, true);
			}
		}
		NBTTagCompound nbt = recipe.getOutputs().getMetadata();
		boolean shiftContainers = nbt != null && nbt.getBoolean(MOVE_CONTAINER_TAG);
		boolean fluidExtracted = false;
		List<ItemStack> inputs = getInputs();
		List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(recipe.getInputs());
		for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
			IRecipeInput key = keyIter.next();
			if (key instanceof RecipeInputFluid && !fluidExtracted) {
				inputTank.drainInternal(((RecipeInputFluid) key).fluid, true);
				fluidExtracted = true;
				keyIter.remove();
				continue;
			}
			int count = key.getAmount();
			for (Iterator<ItemStack> inputIter = inputs.iterator(); inputIter.hasNext();) {
				ItemStack input = inputIter.next();
				if (key.matches(input)) {
					if (input.getCount() >= count) {
						if (input.getItem().hasContainerItem(input)) {
							if (!shiftContainers) {
								continue;
							}
							ItemStack container = input.getItem().getContainerItem(input);
							if (!container.isEmpty()) {
								container.setCount(count);
								outputs.add(new MultiSlotOutput(container, getOutputSlots()));
							}
						}
						input.shrink(count);
						count = 0;
						if (input.isEmpty()) {
							inputIter.remove();
						}
						keyIter.remove();
						break;
					}
					if (input.getItem().hasContainerItem(input)) {
						if (!shiftContainers) {
							continue;
						}
						ItemStack container = input.getItem().getContainerItem(input);
						if (!container.isEmpty()) {
							container.setCount(input.getCount());
							outputs.add(new MultiSlotOutput(container, getOutputSlots()));
						}
					}
					count -= input.getCount();
					input.setCount(0);
					inputIter.remove();
				}
			}
		}
		addToInventory();
		if (supportsUpgrades) {
			for (int i = 0; i < upgradeSlots; i++) {
				ItemStack item = inventory.get(i + inventory.size() - upgradeSlots);
				if (item.getItem() instanceof IMachineUpgradeItem) {
					((IMachineUpgradeItem) item.getItem()).onProcessFinished(item, this);
				}
			}
		}
		shouldCheckRecipe = true;
	}

	@Override
	public MultiRecipe getRecipe() {
		if (lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
			return null;
		}
		// Check if previous recipe is valid
		List<ItemStack> inputs = getInputs();
		if (lastRecipe != null) {
			lastRecipe = checkRecipe(lastRecipe, StackUtil.copyList(inputs)) ? lastRecipe : null;
			if (lastRecipe == null) {
				progress = 0;
			}
		}
		// If previous is not valid, find a new one
		if (lastRecipe == null) {
			lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<MultiRecipe>() {

				@Override
				public boolean test(MultiRecipe t) {
					return checkRecipe(t, StackUtil.copyList(inputs));
				}
			});
		}
		// If no recipe is found, return
		if (lastRecipe == null) {
			return null;
		}
		applyRecipeEffect(lastRecipe.getOutputs());
		if (lastRecipe.getOutputs() instanceof GTFluidMachineOutput){
			GTFluidMachineOutput output = (GTFluidMachineOutput) lastRecipe.getOutputs();
			applyRecipeEffect(output);
			int empty = 0;
			int[] outputSlots = getOutputSlots();
			for (int slot : outputSlots) {
				if (getStackInSlot(slot).isEmpty()) {
					empty++;
				}
			}
			if (empty == outputSlots.length && outputTank.getFluidAmount() == 0){
				return lastRecipe;
			}
			int totalAmount = 0;
			for (FluidStack fluid : output.getFluids()){
				totalAmount += fluid.amount;
			}
			if (outputTank.getFluidAmount() + totalAmount <= outputTank.getCapacity()){
				for (ItemStack outputItem : lastRecipe.getOutputs().getAllOutputs()) {
					if (!(outputItem.getItem() instanceof ItemDisplayIcon)){
						for (int outputSlot : outputSlots) {
							if (inventory.get(outputSlot).isEmpty()) {
								return lastRecipe;
							}
							if (StackUtil.isStackEqual(inventory.get(outputSlot), outputItem, false, true)) {
								if (inventory.get(outputSlot).getCount()
										+ outputItem.getCount() <= inventory.get(outputSlot).getMaxStackSize()) {
									return lastRecipe;
								}
							}
						}
					} else {
						return lastRecipe;
					}
				}
			}
			return null;
		}
		int empty = 0;
		int[] outputSlots = getOutputSlots();
		for (int slot : outputSlots) {
			if (getStackInSlot(slot).isEmpty()) {
				empty++;
			}
		}
		if (empty == outputSlots.length) {
			return lastRecipe;
		}
		for (ItemStack output : lastRecipe.getOutputs().getAllOutputs()) {
			for (int outputSlot : outputSlots) {
				if (inventory.get(outputSlot).isEmpty()) {
					return lastRecipe;
				}
				if (StackUtil.isStackEqual(inventory.get(outputSlot), output, false, true)) {
					if (inventory.get(outputSlot).getCount()
							+ output.getCount() <= inventory.get(outputSlot).getMaxStackSize()) {
						return lastRecipe;
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean checkRecipe(MultiRecipe entry, List<ItemStack> inputs) {
		FluidStack fluid = inputTank.getFluid();
		boolean hasCheckedFluid = false;
		List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(entry.getInputs());
		for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
			IRecipeInput key = keyIter.next();
			if (key instanceof RecipeInputFluid) {
				if (!hasCheckedFluid) {
					hasCheckedFluid = true;
					if (fluid != null && fluid.containsFluid(((RecipeInputFluid) key).fluid)) {
						keyIter.remove();
					}
				}
				continue;
			}
			int toFind = key.getAmount();
			for (Iterator<ItemStack> inputIter = inputs.iterator(); inputIter.hasNext();) {
				ItemStack input = inputIter.next();
				if (key.matches(input)) {
					if (input.getCount() >= toFind) {
						input.shrink(toFind);
						keyIter.remove();
						if (input.isEmpty()) {
							inputIter.remove();
						}
						break;
					}
					toFind -= input.getCount();
					input.setCount(0);
					inputIter.remove();
				}
			}
		}
		return recipeKeys.isEmpty();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null
				? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this)
				: super.getCapability(capability, facing);
	}

	@Override
	public Set<UpgradeType> getSupportedTypes() {
		return new LinkedHashSet<>(Arrays.asList(UpgradeType.values()));
	}

	@Override
	public ContainerIC2 getGuiContainer(EntityPlayer player) {
		return new GTCXContainerCentrifuge(player.inventory, this);
	}

	@Override
	public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
		return GTIndustrialCentrifugeGui.class;
	}

	@Override
	public int[] getInputSlots() {
		return SLOT_INPUTS;
	}

	@Override
	public IFilter[] getInputFilters(int[] slots) {
		return new IFilter[] { filter };
	}

	@Override
	public boolean isRecipeSlot(int slot) {
		return slot <= 1;
	}

	@Override
	public int[] getOutputSlots() {
		return SLOT_OUTPUTS;
	}

	@Override
	public GTRecipeMultiInputList getRecipeList() {
		return GTTileCentrifuge.RECIPE_LIST;
	}

	public ResourceLocation getGuiTexture() {
		return GUI_LOCATION;
	}

	@Override
	public ResourceLocation getStartSoundFile() {
		return Ic2Sounds.extractorOp;
	}

	public static void init() {
		GTTileCentrifuge.RECIPE_LIST.startMassChange();
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.itemCellAir");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.itemCellEmpty");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.itemCellEmpty_1");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustCarbon");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustCarbon_1");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustAluminium");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustAluminium_1");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.itemDustIron");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.itemDustCoal");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustSilicon");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustSilicon_1");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustSilicon_2");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustLithium");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.redstone");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.itemCellEmpty_4");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("tile.sand.default_3");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustCarbon_2");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.itemDustIron_1");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustSilicon_3");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("tile.sand.default_4");
		GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.dustBeryllium");
		for (int i = 0; i < 43; i++){
			String id = i == 0 ? "" :  "_" + i;
			GTTileCentrifuge.RECIPE_LIST.removeRecipe("item.gtclassic.test_tube" + id);
		}
		GTTileCentrifuge.RECIPE_LIST.finishMassChange();

		addCustomRecipe(GTMaterialGen.getIc2(Ic2Items.emptyCell, 2), GTMaterialGen.getFluidStack(GTMaterial.Oxygen), totalEu(5000), new ItemStack[]{GTMaterialGen.getIc2(Ic2Items.airCell, 2)});
		addRecipe("logRubber", 16, totalEu(25000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Carbon, 8), GTMaterialGen.getIc2(Ic2Items.stickyResin, 8), GTMaterialGen.getIc2(Ic2Items.plantBall, 6)}, GTMaterialGen.getFluidStack(GTMaterial.Methane, 4000));
		addRecipe(GTMaterialGen.getFluidStack(GTMaterial.Hydrogen, 4000), totalEu(6000), EMPTY, GTMaterialGen.getFluidStack(GTMaterial.Deuterium));
		addRecipe(GTMaterialGen.getFluidStack(GTMaterial.Deuterium, 4000), totalEu(6000), EMPTY, GTMaterialGen.getFluidStack(GTMaterial.Tritium));
		addRecipe(GTMaterialGen.getFluidStack(GTMaterial.Helium, 16000), totalEu(18000), EMPTY, GTMaterialGen.getFluidStack(GTMaterial.Helium3));
		addRecipe("dustEnderEye", 16, totalEu(35000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.EnderPearl, 8), GTMaterialGen.get(Items.BLAZE_POWDER, 8)});
		addRecipe("dustGlowstone", 16, totalEu(25000), new ItemStack[]{GTMaterialGen.get(Items.REDSTONE, 8), GTMaterialGen.getIc2(Ic2Items.goldDust, 8)}, GTMaterialGen.getFluidStack(GTMaterial.Helium));
		addRecipe("dustRedstone", 10, totalEu(35000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Silicon, 1), GTMaterialGen.getDust(GTMaterial.Pyrite, 5), GTMaterialGen.getDust(GTMaterial.Ruby, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Mercury, 3000));

		/** New Recipes E adds that I override **/
		addRecipe("blockPrismarine", 16, totalEu(24000),new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Manganese, 1), GTMaterialGen.getDust(GTMaterial.Silicon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Potassium), GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 8000));
		addRecipe(GTMaterialGen.get(Blocks.MAGMA, 64), totalEu(3000), EMPTY, GTMaterialGen.getFluidStack("lava",64000));
		addRecipe(GTMaterialGen.get(Items.ROTTEN_FLESH, 16), totalEu(6000), new ItemStack[]{GTMaterialGen.get(Items.LEATHER, 4), GTMaterialGen.get(Items.SLIME_BALL, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Methane, 4000));
		addRecipe(GTMaterialGen.getFluidStack(GTMaterial.Oil, 3000), totalEu(96000), EMPTY, GTMaterialGen.getFluidStack(GTMaterial.Fuel, 2000), GTMaterialGen.getFluidStack(GTMaterial.Lubricant));
		/*
		 * Recipes solely focused on getting methane from various things
		 */
		addMethaneRecipe("listAllmeatraw", 12);
		addMethaneRecipe("listAllmeatcooked", 12);
		addMethaneRecipe("listAllfishraw", 12);
		addMethaneRecipe("listAllfishcooked", 12);
		addMethaneRecipe("listAllfruit", 32);
		addMethaneRecipe("listAllveggie", 16);
		addMethaneRecipe("listAllcookie", 64);
		addMethaneRecipe(GTMaterialGen.get(Items.MUSHROOM_STEW, 16));
		addMethaneRecipe(GTMaterialGen.get(Items.BREAD, 64));
		addMethaneRecipe(GTMaterialGen.get(Items.MELON, 64));
		addMethaneRecipe(GTMaterialGen.get(Items.SPIDER_EYE, 32));
		addMethaneRecipe(GTMaterialGen.get(Items.POISONOUS_POTATO, 12));
		addMethaneRecipe(GTMaterialGen.get(Items.BAKED_POTATO, 24));
		addMethaneRecipe(GTMaterialGen.get(Items.CAKE, 8));
		addMethaneRecipe(GTMaterialGen.get(Blocks.BROWN_MUSHROOM_BLOCK, 12));
		addMethaneRecipe(GTMaterialGen.get(Blocks.RED_MUSHROOM_BLOCK, 12));
		addMethaneRecipe(GTMaterialGen.get(Blocks.BROWN_MUSHROOM, 32));
		addMethaneRecipe(GTMaterialGen.get(Blocks.RED_MUSHROOM, 32));
		addMethaneRecipe(GTMaterialGen.get(Items.NETHER_WART, 32));
		addMethaneRecipe(GTMaterialGen.getIc2(Ic2Items.terraWart, 16));
		addMethaneRecipe(GTMaterialGen.getIc2(Ic2Items.plantBall, 6));
		addMethaneRecipe(GTMaterialGen.getIc2(Ic2Items.compressedPlantBall, 4));


		/** Recipes I add **/
		addRecipe(GTMaterialGen.get(Items.GOLDEN_APPLE, 1), totalEu(50000), new ItemStack[]{GTMaterialGen.get(Items.GOLD_INGOT, 6)}, GTMaterialGen.getFluidStack(GTMaterial.Methane));
		addRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), totalEu(50000), new ItemStack[]{GTMaterialGen.get(Items.GOLD_INGOT, 64)}, GTMaterialGen.getFluidStack(GTMaterial.Methane, 2000));
		addRecipe(GTMaterialGen.get(Items.GOLDEN_CARROT, 1), totalEu(50000), new ItemStack[]{GTMaterialGen.get(Items.GOLD_NUGGET, 6)}, GTMaterialGen.getFluidStack(GTMaterial.Methane));
		addRecipe(GTMaterialGen.get(Items.SPECKLED_MELON, 8), totalEu(50000), new ItemStack[]{GTMaterialGen.get(Items.GOLD_NUGGET, 6)}, GTMaterialGen.getFluidStack(GTMaterial.Methane));
		addRecipe("dustEndstone", 64, totalEu(307200), new ItemStack[]{GTMaterialGen.get(Blocks.SAND, 48),GTMaterialGen.getDust(GTMaterial.Tungsten, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Helium3, 4000), GTMaterialGen.getFluidStack(GTMaterial.Helium, 4000));
		addRecipe("dustRedGarnet", 16, totalEu(15000), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Pyrope, 3), GTMaterialGen.getDust(GTCXMaterial.Almandine, 5), GTMaterialGen.getDust(GTCXMaterial.Spessartine, 8)});
		addRecipe("dustYellowGarnet", 16, totalEu(17500), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Uvarovite, 3), GTMaterialGen.getDust(GTCXMaterial.Andradite, 5), GTMaterialGen.getDust(GTCXMaterial.Grossular, 8)});
		addRecipe("dustDarkAshes", 2, totalEu(1250), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Ashes, 1), GTMaterialGen.getDust(GTCXMaterial.Slag, 1)});
		IRecipeInput ashes = new RecipeInputCombined(2, new RecipeInputOreDict("dustAshes", 2), new RecipeInputOreDict("dustAsh", 2));
		addRecipe(new IRecipeInput[]{ashes}, totalEu(1250), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Carbon, 1)}, new FluidStack[]{});
		addRecipe("dustSlag", 16, totalEu(4000), new ItemStack[]{GTMaterialGen.get(Items.IRON_NUGGET, 3), GTMaterialGen.get(Items.GOLD_NUGGET, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 4), GTMaterialGen.getDust(GTMaterial.Phosphorus, 4)});
		addRecipe("dustRedRock", 16, totalEu(38400), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Calcite, 8), GTMaterialGen.getDust(GTMaterial.Flint, 4), GTMaterialGen.getIc2(Ic2Items.clayDust, 4)});
		addRecipe("dustMarble", 8, totalEu(5275), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Magnesium, 1), GTMaterialGen.getDust(GTMaterial.Calcite, 7)});
		addRecipe("dustBasalt", 16, totalEu(24000), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Olivine, 1), GTMaterialGen.getDust(GTMaterial.Calcite, 3), GTMaterialGen.getDust(GTMaterial.Flint, 8), GTMaterialGen.getDust(GTCXMaterial.DarkAshes, 4)});
		addRecipe("dustBrass", 4, totalEu(7500), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Zinc, 1), GTMaterialGen.getIc2(Ic2Items.copperDust, 3)});
		addRecipe("dustInvar", 3, totalEu(7500), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTMaterialGen.getIc2(Ic2Items.ironDust, 2)});
		addRecipe("dustConstantan", 3, totalEu(7500), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTMaterialGen.getIc2(Ic2Items.copperDust, 2)});
		addRecipe("dustTetrahedrite", 8, totalEu(58240), new ItemStack[]{GTMaterialGen.getIc2(Ic2Items.copperDust, 3), GTMaterialGen.getDust(GTCXMaterial.Antimony, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 3)});
		addRecipe("dustBatteryAlloy", 5, totalEu(37800), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Antimony, 1), GTMaterialGen.getDust(GTCXMaterial.Lead, 4)});
		addRecipe("dustMagnalium", 3, totalEu(9600), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTCXMaterial.Magnesium, 1)});
		addRecipe("dustKanthal", 3, totalEu(16640), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Aluminium, 1), GTMaterialGen.getDust(GTMaterial.Chrome, 1), Ic2Items.ironDust.copy()});
		addRecipe("dustTungstensteel", 2, totalEu(30400), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Steel, 1), GTMaterialGen.getDust(GTMaterial.Tungsten, 1)});
		addRecipe("dustNichrome", 5, totalEu(35840), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Nickel, 4), GTMaterialGen.getDust(GTMaterial.Chrome, 1)});
		addRecipe("dustCinnabar", 4, totalEu(58880), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Sulfur, 2)}, GTMaterialGen.getFluidStack(GTMaterial.Mercury, 2000));
		addRecipe("dustIridium", 9, totalEu(117760), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Osmium, 1)});
		addRecipe(GTMaterialGen.get(Blocks.SOUL_SAND, 16), totalEu(12000), new ItemStack[]{GTMaterialGen.get(Blocks.SAND), GTMaterialGen.getDust(GTCXMaterial.Saltpeter, 4), Ic2Items.coalDust}, GTMaterialGen.getFluidStack(GTMaterial.Oil));
		addRecipe(GTMaterialGen.getFluidStack(GTMaterial.Beryllium), totalEu(1000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Beryllium, 1)});
	}

	public static void addCustomRecipe(ItemStack stack, FluidStack fluid, IRecipeModifier[] modifiers,
			ItemStack[] outputs, FluidStack... fluidOutputs) {
		addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack),
				new RecipeInputFluid(fluid), }, modifiers, outputs, fluidOutputs);
	}

	public static void addCustomRecipe(String input, int amount, FluidStack fluid, IRecipeModifier[] modifiers,
			ItemStack[] outputs, FluidStack... fluidOutputs) {
		addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount),
				new RecipeInputFluid(fluid), }, modifiers, outputs, fluidOutputs);
	}

	public static void addCustomRecipe(ItemStack stack0, ItemStack stack1, IRecipeModifier[] modifiers,
									   ItemStack[] outputs, FluidStack... fluidOutputs) {
		addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack0),
				new RecipeInputItemStack(stack1), }, modifiers, outputs, fluidOutputs);
	}

	public static void addCustomRecipe(String input, int amount, ItemStack stack, IRecipeModifier[] modifiers,
									   ItemStack[] outputs, FluidStack... fluidOutputs) {
		addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount),
				new RecipeInputItemStack(stack), }, modifiers, outputs, fluidOutputs);
	}

	public static void addMethaneRecipe(ItemStack stack) {
		addRecipe(stack, totalEu(36000), EMPTY, GTMaterialGen.getFluidStack(GTMaterial.Methane));
	}

	public static void addMethaneRecipe(String input, int amount) {
		addRecipe(input, amount, totalEu(36000), EMPTY, GTMaterialGen.getFluidStack(GTMaterial.Methane));
	}

	public static void addRecipe(ItemStack stack, IRecipeModifier[] modifiers, ItemStack[] outputs, FluidStack... fluidOutputs) {
		addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack) }, modifiers, outputs, fluidOutputs);
	}

	public static void addRecipe(String input, int amount, IRecipeModifier[] modifiers,
			ItemStack[] outputs, FluidStack... fluidOutputs) {
		addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount) }, modifiers, outputs, fluidOutputs);
	}

	public static void addRecipe(FluidStack fluid, IRecipeModifier[] modifiers, ItemStack[] outputs, FluidStack... fluidOutputs) {
		addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid) }, modifiers, outputs, fluidOutputs);
	}

	public static IRecipeModifier[] totalEu(int total) {
		return GTRecipeMachineHandler.totalEu(GTTileCentrifuge.RECIPE_LIST, total);
	}

	public static void addRecipe(IRecipeInput[] inputs, IRecipeModifier[] modifiers, ItemStack[] outputs, FluidStack[] fluidOutputs) {
		List<IRecipeInput> inlist = new ArrayList<>();
		List<ItemStack> outlist = new ArrayList<>();
		List<FluidStack> fluidOutlist = new ArrayList<>();
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
		for (FluidStack output : fluidOutputs){
			fluidOutlist.add(output);
		}
		MachineOutput output = fluidOutputs.length > 0 ? outputs.length > 0 ? new GTFluidMachineOutput(mods, outlist, fluidOutlist) : new GTFluidMachineOutput(mods, fluidOutlist) : new MachineOutput(mods, outlist);
		addRecipe(inlist, output);
	}

	public static void addRecipe(IRecipeInput[] inputs, IRecipeModifier[] modifiers, ItemStack[] outputs, FluidStack[] fluidOutputs, String recipeId) {
		List<IRecipeInput> inlist = new ArrayList<>();
		List<ItemStack> outlist = new ArrayList<>();
		List<FluidStack> fluidOutlist = new ArrayList<>();
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
		for (FluidStack output : fluidOutputs){
			fluidOutlist.add(output);
		}
		MachineOutput output = fluidOutputs.length > 0 ? outputs.length > 0 ? new GTFluidMachineOutput(mods, outlist, fluidOutlist) : new GTFluidMachineOutput(mods, fluidOutlist) : new MachineOutput(mods, outlist);
		addRecipe(inlist, output, recipeId);
	}

	static void addRecipe(List<IRecipeInput> input, MachineOutput output){
		addRecipe(input, output, (output instanceof GTFluidMachineOutput ? ((GTFluidMachineOutput)output).getFluids().get(0).getUnlocalizedName() : output.getAllOutputs().get(0).getUnlocalizedName()));
	}

	static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeID) {
		for (IRecipeInput in : input){
			if (in instanceof RecipeInputFluid && !VALID_FLUIDS.contains(((RecipeInputFluid)in).fluid.getFluid())){
				VALID_FLUIDS.add(((RecipeInputFluid)in).fluid.getFluid());
			}
		}
		GTTileCentrifuge.RECIPE_LIST.addRecipe(input, output, (output instanceof GTFluidMachineOutput ? ((GTFluidMachineOutput)output).getFluids().get(0).getUnlocalizedName() : output.getAllOutputs().get(0).getUnlocalizedName()), EU_TICK);
	}

	@Override
	public boolean hasLeftClick() {
		return false;
	}

	@Override
	public boolean hasRightClick() {
		return true;
	}

	@Override
	public void onLeftClick(EntityPlayer var1, Side var2) {
	}

	@Override
	public boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing enumFacing, Side side) {
		boolean worked = GTHelperFluid.doClickableFluidContainerEmptyThings(player, hand, world, pos, this.inputTank) || doClickableFluidContainerFillThings(player, hand, this.getWorld(), this.getPos(), this.outputTank);
		if (worked) this.shouldCheckRecipe = true;
		return worked;
	}

	public static boolean doClickableFluidContainerFillThings(EntityPlayer player, EnumHand hand, World world, BlockPos pos, LayeredFluidTank tank) {
		ItemStack playerStack = player.getHeldItem(hand);
		if (!playerStack.isEmpty()) {
			FluidActionResult result = FluidUtil.tryFillContainer(playerStack, tank, tank.getCapacity(), player, true);
			if (result.isSuccess()) {
				playerStack.shrink(1);
				ItemStack resultStack = result.getResult();
				if (!resultStack.isEmpty() && !player.inventory.addItemStackToInventory(resultStack)) {
					player.dropItem(resultStack, false);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		List<IFluidTankProperties> combined = new ArrayList<>();
		Stream.of(inputTank.getTankProperties(), outputTank.getTankProperties()).flatMap(Stream::of).forEach(combined::add);
		return combined.toArray(new IFluidTankProperties[0]);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return inputTank.fill(resource, doFill);
	}

	@Nullable
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return outputTank.drain(resource, doDrain);
	}

	@Nullable
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return outputTank.drain(maxDrain, doDrain);
	}
}
