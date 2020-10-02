package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerBath;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.util.GTCXPassiveMachineFilter;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMachineHandler;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBasePassiveMachine;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.RecipeModifierHelpers.IRecipeModifier;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasInventory;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.item.misc.ItemDisplayIcon;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class GTCXTileBath extends GTTileBasePassiveMachine implements ITankListener, IClickable {

	protected static final int[] slotInputs = { 0, 1, 2, 3, 4, 5 };
	protected static final int[] slotOutputs = { 6, 7, 8, 9, 10, 11 };
	public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/bath.png");
	public static final String NBT_TANK = "tank";
	public static final int SLOT_TANK = 12;
	public static final List<Fluid> VALID_FLUIDS = new ArrayList<>();
	public IFilter filter = new GTCXPassiveMachineFilter(this);
	@NetworkField(index = 13)
	private IC2Tank tank = new IC2Tank(16000){
		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return super.canFillFluidType(fluid) && VALID_FLUIDS.contains(fluid.getFluid());
		}
	};

	public GTCXTileBath() {
		super(13, 100);
		this.tank.addListener(this);
		this.addNetworkFields(NBT_TANK);
	}

	@Override
	protected void addSlots(InventoryHandler handler) {
		handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
		handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
		handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
		handler.registerDefaultSlotsForSide(RotationList.UP, slotInputs);
		handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotInputs);
		handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotOutputs);
		handler.registerSlotType(SlotType.Input, slotInputs);
		handler.registerSlotType(SlotType.Output, slotOutputs);
	}

	@Override
	public LocaleComp getBlockName() {
		return GTCXLang.BATH;
	}

	@Override
	public ContainerIC2 getGuiContainer(EntityPlayer player) {
		return new GTCXContainerBath(player.inventory, this);
	}

	@Override
	public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
		return GTCXMachineGui.GTCXBathGui.class;
	}

	@Override
	public void onTankChanged(IFluidTank tank) {
		this.getNetwork().updateTileGuiField(this, NBT_TANK);
		this.setStackInSlot(SLOT_TANK, ItemDisplayIcon.createWithFluidStack(this.tank.getFluid()));
		shouldCheckRecipe = true;
	}

	@Override
	public void process(MultiRecipe recipe) {
		MachineOutput output = recipe.getOutputs().copy();
		for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
			outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
		}
		NBTTagCompound nbt = recipe.getOutputs().getMetadata();
		boolean shiftContainers = nbt != null && nbt.getBoolean("move_container");
		boolean fluidExtracted = false;
		List<ItemStack> inputs = getInputs();
		for (IRecipeInput key : recipe.getInputs()) {
			int count = key.getAmount();
			if (key instanceof RecipeInputFluid && !fluidExtracted){
				tank.drainInternal(((RecipeInputFluid)key).fluid, true);
				fluidExtracted = true;
				continue;
			}
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
		FluidStack fluid = tank.getFluid();
		boolean hasCheckedFluid = false;
		List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(entry.getInputs());
		for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
			IRecipeInput key = keyIter.next();
			if (key instanceof RecipeInputFluid) {
				if (!hasCheckedFluid) {
					hasCheckedFluid = true;
					if (fluid != null && fluid.containsFluid(((RecipeInputFluid) key).fluid)) {
						keyIter.remove();
						continue;
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
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<>();

		for(int i = 0; i < this.inventory.size(); ++i) {
			ItemStack stack = this.inventory.get(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ItemDisplayIcon){
					continue;
				}
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

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tank.readFromNBT(nbt.getCompoundTag(NBT_TANK));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		this.tank.writeToNBT(this.getTag(nbt, NBT_TANK));
		return nbt;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank)
				: super.getCapability(capability, facing);
	}

	@Override
	public int[] getInputSlots() {
		return slotInputs;
	}

	@Override
	public IFilter[] getInputFilters(int[] slots) {
		return null;
	}

	@Override
	public int[] getOutputSlots() {
		return slotOutputs;
	}

	@Override
	public boolean isRecipeSlot(int slot) {
		return slot <= 5;
	}

	@Override
	public GTRecipeMultiInputList getRecipeList() {
		return GTCXRecipeLists.BATH_RECIPE_LIST;
	}

	public ResourceLocation getGuiTexture() {
		return GUI_LOCATION;
	}

	@Override
	public boolean hasGui(EntityPlayer player) {
		return true;
	}

	@Override
	public ResourceLocation getStartSoundFile() {
		return Ic2Sounds.watermillLoop;
	}

	@Override
	public boolean canRemoveBlock(EntityPlayer player) {
		return true;
	}

	public static void init() {
		if (GTCXConfiguration.general.crushedOres){
			ItemStack stoneDust = GTMaterialGen.getDust(GTCXMaterial.Stone, 1);
			addRecipe("crushedCopper", 1, GTMaterialGen.getFluidStack(GTMaterial.Mercury), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Copper, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Gold, 3), stoneDust);
			addRecipe("crushedGalena", 1, GTMaterialGen.getFluidStack(GTMaterial.Mercury), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Galena, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 3), stoneDust);
			addRecipe("crushedIridium", 1, GTMaterialGen.getFluidStack(GTMaterial.Mercury), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Iridium, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Platinum, 3), stoneDust);
			addRecipe("crushedSilver", 1, GTMaterialGen.getFluidStack(GTMaterial.Mercury), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Silver, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 3), stoneDust);
			addRecipe("crushedTungsten", 1, GTMaterialGen.getFluidStack(GTMaterial.Mercury), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Tungsten, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 3), stoneDust);
			addRecipe("crushedGold", 1, GTMaterialGen.getFluidStack(GTMaterial.Mercury), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Gold, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Gold, 3), stoneDust);
			addRecipe("crushedPlatinum", 1, GTMaterialGen.getFluidStack(GTMaterial.Mercury), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Platinum, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Platinum, 3), stoneDust);
			addRecipe("crushedLead", 1, GTMaterialGen.getFluidStack(GTMaterial.Mercury), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Lead, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 3), stoneDust);
			addRecipe("crushedNickel", 1, GTMaterialGen.getFluidStack(GTMaterial.Mercury), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Nickel, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Platinum, 3), stoneDust);
			addRecipe("crushedCopper", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Copper, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Copper, 3), stoneDust);
			addRecipe("crushedPlatinum", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Platinum, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 3), stoneDust);
			addRecipe("crushedIron", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Iron, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 3), stoneDust);
			addRecipe("crushedSphalerite", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Sphalerite, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 3), stoneDust);
			addRecipe("crushedTetrahedrite", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Tetrahedrite, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Tetrahedrite, 3), stoneDust);
			addRecipe("crushedGold", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Gold, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Copper, 3), stoneDust);
			addRecipe("crushedNickel", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Nickel, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 3), stoneDust);
			addRecipe("crushedTin", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate), 800, GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Tin, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 3), stoneDust);
		}
		addRecipe("pulpWood", 1, GTMaterialGen.getFluidStack("water", 100), 200, GTMaterialGen.get(Items.PAPER, 1));
		addRecipe(GTMaterialGen.get(Items.REEDS), GTMaterialGen.getFluidStack("water", 100), 100, GTMaterialGen.get(Items.PAPER, 1));
		addRecipe("dustCoal", 1, GTMaterialGen.getFluidStack("water", 125), 12, Ic2Items.hydratedCoalDust.copy());
		addRecipe("dustCharcoal", 1, GTMaterialGen.getFluidStack("water", 125), 12, Ic2Items.hydratedCharCoalDust.copy());
		addRecipe(GTMaterialGen.get(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE), GTMaterialGen.getFluidStack(GTMaterial.Chlorine, 125), 12, GTMaterialGen.get(Blocks.WOOL, 1, 0));
	}

	public static IRecipeModifier[] totalTime(int total) {
		return GTRecipeMachineHandler.totalTime(total);
	}

	public static void addRecipe(String name, int amount, FluidStack fluid, int totalTime, ItemStack... outputs){
		addRecipe(new IRecipeInput[]{input(name, amount), input(fluid)}, totalTime(totalTime), outputs);
	}

	public static void addRecipe(ItemStack input, FluidStack fluid, int totalTime, ItemStack... outputs){
		addRecipe(new IRecipeInput[]{input(input), input(fluid)}, totalTime(totalTime), outputs);
	}

	public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, ItemStack... outputs) {
		addRecipe(inputs, modifiers, outputs[0].getUnlocalizedName(), outputs);
	}

	public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, String recipeId, ItemStack... outputs) {
		List<IRecipeInput> inlist = new ArrayList<>();
		inlist.addAll(Arrays.asList(inputs));
		NBTTagCompound mods = new NBTTagCompound();
		for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
			modifier.apply(mods);
		}

		addRecipe(inlist, new MachineOutput(mods, outputs), recipeId);
	}

	static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeID) {
		for (IRecipeInput in : input){
			if (in instanceof RecipeInputFluid && !VALID_FLUIDS.contains(((RecipeInputFluid)in).fluid.getFluid())){
				VALID_FLUIDS.add(((RecipeInputFluid)in).fluid.getFluid());
			}
		}
		GTCXRecipeLists.BATH_RECIPE_LIST.addRecipe(input, output, recipeID, 0);
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
		boolean fill = GTHelperFluid.doClickableFluidContainerThings(player, hand, world, pos, this.tank);
		if (fill) this.shouldCheckRecipe = true;
		return fill;
	}
}
