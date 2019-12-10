package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerFluidCaster;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.util.GTCXLang;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBaseMachine;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasInventory;
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
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.ITankListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class GTCXTileFluidCaster extends GTTileBaseMachine implements ITankListener {
    protected static final int slotDisplayIn = 0;
    protected static final int slotInput = 1;
    protected static final int slotOutput = 2;
    protected static final int slotFuel = 3;
    public IFilter filter = new MachineFilter(this);
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/fluidcaster.png");
    private static final int defaultEu = 64;
    private IC2Tank inputTank = new IC2Tank(16000);

    public static final String consumePress = "consumePress";

    public GTCXTileFluidCaster() {
        super(4, 2, defaultEu, 100,128);
        maxEnergy = 10000;
        this.inputTank.addListener(this);
        this.addGuiFields("inputTank");
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInput);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutput);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInput);
        handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotInput);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutput);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerInputFilter(filter, slotInput);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Input, slotInput);
        handler.registerSlotType(SlotType.Output, slotOutput);
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.FLUID_CASTER;
    }

    @Override
    public int[] getInputSlots() {
        return new int[] {slotInput};
    }

    @Override
    public IFilter[] getInputFilters(int[] ints) {
        return new IFilter[] { filter };
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        for (int i : this.getInputSlots()) {
            if (slot <= i) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[] {slotOutput};
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GTCXRecipeLists.FLUID_CASTER_RECIPE_LIST;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXFluidCasterGui.class;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.getNetwork().updateTileGuiField(this, "inputTank");
        this.inventory.set(slotDisplayIn, ItemDisplayIcon.createWithFluidStack(this.inputTank.getFluid()));
        shouldCheckRecipe = true;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerFluidCaster(entityPlayer.inventory, this);
    }

    @Override
    public void process(MultiRecipe recipe) {
        MachineOutput output = recipe.getOutputs().copy();
        for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
            outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
            onRecipeComplete();
        }
        NBTTagCompound nbt = recipe.getOutputs().getMetadata();
        boolean shiftContainers = nbt == null ? false : nbt.getBoolean(MOVE_CONTAINER_TAG);
        boolean fluidExtracted = false;
        List<ItemStack> inputs = getInputs();
        for (IRecipeInput key : recipe.getInputs()) {
            int count = key.getAmount();
            if (key instanceof RecipeInputFluid && !fluidExtracted){
                inputTank.drain(((RecipeInputFluid)key).fluid, true);
                fluidExtracted = true;
                continue;
            }
            ItemStack input = inventory.get(1);
            if (key.matches(input) && canConsumePress(recipe.getOutputs())) {
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
                    continue;
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
    public List<ItemStack> getInputs() {
        ArrayList<ItemStack> inputs = new ArrayList<>();
        for (int i : getInputSlots()) {
            if (inventory.get(i).isEmpty()) {
                continue;
            }
            inputs.add(inventory.get(i));
        }
        return inputs;
    }

    @Override
    public MultiRecipe getRecipe() {
        if (lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            return null;
        }
        // Check if previous recipe is valid
        ItemStack input = inventory.get(1);
        FluidStack fluid = inputTank.getFluid();
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, fluid, input) ? lastRecipe : null;
            if (lastRecipe == null) {
                progress = 0;
            }
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<MultiRecipe>() {

                @Override
                public boolean test(MultiRecipe t) {
                    return checkRecipe(t, fluid, input);
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

    public boolean checkRecipe(MultiRecipe entry, FluidStack input, ItemStack inputItem) {
        IRecipeInput recipeInput = entry.getInput(0);
        IRecipeInput recipeInput1 = entry.getInput(1);
        boolean hasFluid = false;
        boolean hasItem = false;
        if (recipeInput instanceof RecipeInputFluid){
            hasFluid = input != null && input.containsFluid(((RecipeInputFluid)recipeInput).fluid);
        } else {
            hasItem = !inputItem.isEmpty() && recipeInput.matches(inputItem);
        }
        if (recipeInput1 instanceof RecipeInputFluid){
            if (hasFluid) {
                hasFluid = false;
            } else {
                hasFluid = input != null && input.containsFluid(((RecipeInputFluid)recipeInput1).fluid);
            }

        } else {
            if (hasItem){
                hasItem = false;
            } else {
                hasItem = !inputItem.isEmpty() && recipeInput1.matches(inputItem);
            }
        }
        return hasFluid && hasItem;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.inputTank.readFromNBT(nbt.getCompoundTag("inputTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.inputTank.writeToNBT(this.getTag(nbt, "inputTank"));
        return nbt;
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
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.inputTank);
        }
        return super.getCapability(capability, facing);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int total) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((total / defaultEu) - 100) };
    }

    public static boolean canConsumePress(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            return true;
        }
        return output.getMetadata().getBoolean(consumePress);
    }

    public static void addRecipe(String input, int amount, FluidStack fluid, boolean press, int totalEu,
                                 ItemStack... outputs) {

        addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid), new RecipeInputOreDict(input, amount) }, press, totalEu, outputs);
    }

    public static void addRecipe(ItemStack input, FluidStack fluid, boolean press, int totalEu,
                                 ItemStack... outputs) {

        addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid), new RecipeInputItemStack(input) }, press, totalEu, outputs);
    }

    public static void addRecipe(IRecipeInput input, FluidStack fluid, boolean press, int totalEu,
                                 ItemStack... outputs) {

        addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid), input }, press, totalEu, outputs);
    }

    private static void addRecipe(IRecipeInput[] inputs, boolean press, int totalEu, ItemStack... outputs) {
        List<IRecipeInput> inlist = new ArrayList<>();
        List<ItemStack> outlist = new ArrayList<>();
        RecipeModifierHelpers.IRecipeModifier[] modifiers = totalEu(totalEu);
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        mods.setBoolean(consumePress, press);
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(inlist, new MachineOutput(mods, outlist));
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
        GTCXRecipeLists.FLUID_CASTER_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
    }
}
