package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerFluidCaster;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.data.GTCXValues;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTUtility;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTConfig;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.energy.EnergyNet;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityBasicElectricMachine;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class GTCXTileFluidCaster extends GTTileBaseMachine implements ITankListener, IGTDebuggableTile, IClickable {
    protected static final int slotDisplayIn = 0;
    protected static final int slotDisplayWater = 1;
    protected static final int slotInput = 2;
    protected static final int slotOutput = 3;
    protected static final int slotFuel = 4;
    public static final List<Fluid> validFluids = new ArrayList<>();
    public IFilter filter = new MachineFilter(this);
    float oldProgressPerTick;
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/fluidcaster.png");
    private static final int defaultEu = 64;
    @NetworkField(index = 13)
    private final IC2Tank inputTank = new IC2Tank(16000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && fluid.getFluid() != FluidRegistry.WATER && validFluids.contains(fluid.getFluid());
        }
    };

    @NetworkField(index = 14)
    private final IC2Tank waterTank = new IC2Tank(16000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && fluid.getFluid() == FluidRegistry.WATER;
        }
    };

    public static final String CONSUME_PRESS = "consumePress";

    public GTCXTileFluidCaster() {
        super(5, 2, defaultEu, 100,128);
        maxEnergy = 10000;
        this.inputTank.addListener(this);
        this.waterTank.addListener(this);
        oldProgressPerTick = 1.0F;
        this.addGuiFields("inputTank", "waterTank");
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
    public ResourceLocation getStartSoundFile() {
        return GTCExpansion.getAprilFirstSound(super.getStartSoundFile());
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.getNetwork().updateTileGuiField(this, "inputTank");
        this.inventory.set(slotDisplayIn, ItemDisplayIcon.createWithFluidStack(this.inputTank.getFluid()));
        this.getNetwork().updateTileGuiField(this, "waterTank");
        this.inventory.set(slotDisplayWater, ItemDisplayIcon.createWithFluidStack(this.waterTank.getFluid()));
        if (this.waterTank.getFluidAmount() > 1000){
            progressPerTick *= 1.5F;
        } else {
            progressPerTick = oldProgressPerTick;
        }
        shouldCheckRecipe = true;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        super.setStackInSlot(slot, stack);
        if (this.waterTank.getFluidAmount() > 1000){
            progressPerTick *= 1.5F;
        } else {
            progressPerTick = oldProgressPerTick;
        }
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
    public void update() {
        super.update();
        GTUtility.importFluidFromSideToMachine(this, inputTank, left(), 1000);
    }

    @Override
    public void process(MultiRecipe recipe) {
        MachineOutput output = recipe.getOutputs().copy();
        for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
            outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
            onRecipeComplete();
        }
        NBTTagCompound nbt = recipe.getOutputs().getMetadata();
        boolean shiftContainers = nbt != null && nbt.getBoolean(MOVE_CONTAINER_TAG);
        boolean fluidExtracted = false;
        List<ItemStack> inputs = getInputs();
        for (IRecipeInput key : recipe.getInputs()) {
            int count = key.getAmount();
            if (key instanceof RecipeInputFluid && !fluidExtracted){
                inputTank.drain(((RecipeInputFluid)key).fluid, true);
                fluidExtracted = true;
                continue;
            }
            ItemStack input = inventory.get(slotInput);
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
        if (waterTank.getCapacity() >= 1000){
            waterTank.drainInternal(1000, true);
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
        ItemStack input = inventory.get(slotInput);
        FluidStack fluid = inputTank.getFluid();
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, fluid, input.copy()) ? lastRecipe : null;
            if (lastRecipe == null) {
                progress = 0;
            }
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<MultiRecipe>() {

                @Override
                public boolean test(MultiRecipe t) {
                    return checkRecipe(t, fluid, input.copy());
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
    public void setOverclockRates() {
        if (!supportsUpgrades) {
            return;
        }
        lastRecipe = null;
        shouldCheckRecipe = true;
        int extraProcessSpeed = 0;
        double processingSpeedMultiplier = 1.0D;
        int extraProcessTime = 0;
        double processTimeMultiplier = 1.0D;
        int extraEnergyDemand = 0;
        double energyDemandMultiplier = 1.0D;
        int extraEnergyStorage = 0;
        double energyStorageMultiplier = 1.0D;
        int extraTier = 0;
        float soundModfier = 1.0F;
        boolean redstonePowered = false;
        redstoneSensitive = defaultSensitive;
        for (int i = 0; i < 4; i++) {
            ItemStack item = inventory.get(i + inventory.size() - 4);
            if (item.getItem() instanceof IMachineUpgradeItem) {
                IMachineUpgradeItem upgrade = (IMachineUpgradeItem) item.getItem();
                upgrade.onInstalling(item, this);
                extraProcessSpeed += upgrade.getExtraProcessSpeed(item, this) * item.getCount();
                processingSpeedMultiplier *= Math.pow(upgrade.getProcessSpeedMultiplier(item, this), item.getCount());
                extraProcessTime += upgrade.getExtraProcessTime(item, this) * item.getCount();
                processTimeMultiplier *= Math.pow(upgrade.getProcessTimeMultiplier(item, this), item.getCount());
                extraEnergyDemand += upgrade.getExtraEnergyDemand(item, this) * item.getCount();
                energyDemandMultiplier *= Math.pow(upgrade.getEnergyDemandMultiplier(item, this), item.getCount());
                extraEnergyStorage += upgrade.getExtraEnergyStorage(item, this) * item.getCount();
                energyStorageMultiplier *= Math.pow(upgrade.getEnergyStorageMultiplier(item, this), item.getCount());
                soundModfier *= Math.pow(upgrade.getSoundVolumeMultiplier(item, this), item.getCount());
                extraTier += upgrade.getExtraTier(item, this) * item.getCount();
                if (upgrade.useRedstoneInverter(item, this)) {
                    redstonePowered = true;
                }
            }
        }
        redstoneInverted = redstonePowered;
        oldProgressPerTick = TileEntityBasicElectricMachine.applyFloatModifier(1, extraProcessSpeed, processingSpeedMultiplier);
        energyConsume = TileEntityBasicElectricMachine.applyModifier(defaultEnergyConsume, extraEnergyDemand, energyDemandMultiplier);
        operationLength = TileEntityBasicElectricMachine.applyModifier(defaultOperationLength, extraProcessTime, processTimeMultiplier);
        setMaxEnergy(TileEntityBasicElectricMachine.applyModifier(defaultEnergyStorage, extraEnergyStorage, energyStorageMultiplier));
        tier = baseTier + extraTier;
        if (tier > 13) {
            tier = 13;
        }
        maxInput = (int) EnergyNet.instance.getPowerFromTier(tier);
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
        soundLevel = soundModfier;
        if (this.waterTank.getFluidAmount() > 1000){
            progressPerTick *= 1.5F;
        } else {
            progressPerTick = oldProgressPerTick;
        }
        if (progressPerTick < 0.01F) {
            progressPerTick = 0.01F;
        }
        if (operationLength < 1) {
            operationLength = 1;
        }
        if (energyConsume < 1) {
            energyConsume = 1;
        }
        if (lastRecipe == null || lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            applyRecipeEffect(null);
        } else {
            applyRecipeEffect(lastRecipe.getOutputs());
        }
        getNetwork().updateTileEntityField(this, "redstoneInverted");
        getNetwork().updateTileEntityField(this, "redstoneSensitive");
        getNetwork().updateTileEntityField(this, "soundLevel");
        getNetwork().updateTileGuiField(this, "maxInput");
        getNetwork().updateTileGuiField(this, "energy");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.inputTank.readFromNBT(nbt.getCompoundTag("inputTank"));
        this.waterTank.readFromNBT(nbt.getCompoundTag("waterTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.inputTank.writeToNBT(this.getTag(nbt, "inputTank"));
        this.waterTank.writeToNBT(this.getTag(nbt,"waterTank"));
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

    public EnumFacing left(){
        try { // for load cases
            return this.getFacing().rotateY();
        } catch (IllegalStateException e){
            return this.getFacing();
        }
    }

    public EnumFacing right(){
        try { // for load cases
            return this.getFacing().rotateYCCW();
        } catch (IllegalStateException e){
            return this.getFacing();
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing!= null) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null){
            if (facing == EnumFacing.UP || facing == left() || facing == this.getFacing().getOpposite()){
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.inputTank);
            } else
            if (facing == EnumFacing.DOWN || facing == right()){
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.waterTank);
            } else
            return super.getCapability(capability, facing);
        } else
        return super.getCapability(capability, facing);
    }

    public static void init(){
        addRecipe(GTMaterialGen.get(GTCXItems.moldBlock), new FluidStack(FluidRegistry.LAVA,1000), true,64000, GTMaterialGen.get(Blocks.OBSIDIAN));
        addRecipe(GTMaterialGen.get(GTCXItems.moldCell), GTMaterialGen.getFluidStack(GTCXMaterial.Tin, 144), true, 12800, GTMaterialGen.getIc2(Ic2Items.emptyCell, 4));
        addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.getFluidStack(GTCXMaterial.RefinedIron, 144), true, 12800, Ic2Items.refinedIronIngot);
        addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.getFluidStack(GTCXMaterial.Copper, 144), true, 12800, Ic2Items.copperIngot);
        addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.getFluidStack(GTCXMaterial.Tin, 144), true, 12800, Ic2Items.tinIngot);
        addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.getFluidStack(GTCXMaterial.Silver, 144), true, 12800, Ic2Items.silverIngot);
        addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.getFluidStack(GTCXMaterial.Bronze, 144), true, 12800, Ic2Items.bronzeIngot);
        addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.getFluidStack(GTCXMaterial.Iron, 144), true, 12800, GTMaterialGen.get(Items.IRON_INGOT));
        addRecipe(GTMaterialGen.get(GTCXItems.moldIngot), GTMaterialGen.getFluidStack(GTCXMaterial.Gold, 144), true, 12800, GTMaterialGen.get(Items.GOLD_INGOT));
        addRecipe(GTMaterialGen.get(GTCXItems.moldNugget), GTMaterialGen.getFluidStack(GTCXMaterial.Iron, 16), true, 1600, GTMaterialGen.get(Items.IRON_NUGGET));
        addRecipe(GTMaterialGen.get(GTCXItems.moldNugget), GTMaterialGen.getFluidStack(GTCXMaterial.Gold, 16), true, 1600, GTMaterialGen.get(Items.GOLD_NUGGET));
        addRecipe(GTMaterialGen.get(GTCXItems.moldWire), GTMaterialGen.getFluidStack(GTCXMaterial.Gold, 144), true, 12800, GTMaterialGen.getIc2(Ic2Items.goldCable, 6));
        addRecipe(GTMaterialGen.get(GTCXItems.moldWire), GTMaterialGen.getFluidStack(GTCXMaterial.Copper, 144), true, 12800, GTMaterialGen.getIc2(Ic2Items.copperCable, 3));
        addRecipe(GTMaterialGen.get(GTCXItems.moldWire), GTMaterialGen.getFluidStack(GTCXMaterial.Tin, 144), true, 12800, GTMaterialGen.getIc2(Ic2Items.tinCable, 4));
        addRecipe(GTMaterialGen.get(GTCXItems.moldWire), GTMaterialGen.getFluidStack(GTCXMaterial.Bronze, 144), true, 12800, GTMaterialGen.getIc2(Ic2Items.bronzeCable, 3));
        GTMaterial mat = GTCXValues.STEEL_MODE ? GTCXMaterial.Steel : GTCXMaterial.RefinedIron;
        addRecipe(GTMaterialGen.get(GTCXItems.moldWire), GTMaterialGen.getFluidStack(mat, 144), true, 12800, GTMaterialGen.getIc2(Ic2Items.ironCable, 6));
        addRecipe(GTMaterialGen.get(GTCXItems.moldWire), GTMaterialGen.getFluidStack(GTMaterial.Electrum, 144), true, 12800, GTMaterialGen.get(GTCXBlocks.electrumCable, 6));
        addRecipe(GTMaterialGen.get(GTCXItems.moldWire), GTMaterialGen.getFluidStack(GTMaterial.Aluminium, 144), true, 12800, GTMaterialGen.get(GTCXBlocks.aluminiumCable, 6));

        if (Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) && GTConfig.modcompat.compatIc2Extras){
            addRecipe(GTMaterialGen.get(GTCXItems.moldBlock), GTMaterialGen.getFluidStack(GTCXMaterial.RefinedIron, 1296), false, 115200, GTMaterialGen.getModBlock(GTValues.MOD_ID_IC2_EXTRAS, "refinedironblock"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCell), GTMaterialGen.getFluidStack(GTCXMaterial.Iron, 144), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "emptyfuelrod"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.getFluidStack(GTCXMaterial.Copper, 72), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "coppercasing"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.getFluidStack(GTCXMaterial.Tin, 72), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "tincasing"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.getFluidStack(GTCXMaterial.Silver, 72), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "silvercasing"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.getFluidStack(GTCXMaterial.Lead, 72), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "leadcasing"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.getFluidStack(GTCXMaterial.Iron, 72), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "ironcasing"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.getFluidStack(GTCXMaterial.Gold, 72), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "goldcasing"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.getFluidStack(GTCXMaterial.RefinedIron, 72), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "refinedironcasing"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.getFluidStack(GTCXMaterial.Steel, 72), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "steelcasing"));
            addRecipe(GTMaterialGen.get(GTCXItems.moldCasing), GTMaterialGen.getFluidStack(GTCXMaterial.Bronze, 72), true, 6400, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "bronzecasing"));
        }
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int total) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((total / defaultEu) - 100) };
    }

    public static boolean canConsumePress(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            return true;
        }
        return output.getMetadata().getBoolean(CONSUME_PRESS);
    }

    public static void addRecipe(String input, int amount, FluidStack fluid, boolean press, int totalEu,
                                 ItemStack output) {
        if (fluid.getFluid() == FluidRegistry.WATER){
            GTCExpansion.logger.info("Fluid in fluid casting recipes can't be water.");
            return;
        }
        addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid), new RecipeInputOreDict(input, amount) }, press, totalEu, output);
    }

    public static void addRecipe(ItemStack input, FluidStack fluid, boolean press, int totalEu,
                                 ItemStack output) {
        if (fluid.getFluid() == FluidRegistry.WATER){
            GTCExpansion.logger.info("Fluid in fluid casting recipes can't be water.");
            return;
        }
        addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid), new RecipeInputItemStack(input) }, press, totalEu, output);
    }

    public static void addRecipe(IRecipeInput input, FluidStack fluid, boolean press, int totalEu, String recipeId,
                                 ItemStack output) {
        if (fluid.getFluid() == FluidRegistry.WATER){
            GTCExpansion.logger.info("Fluid in fluid casting recipes can't be water.");
            return;
        }
        addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid), input }, press, totalEu, recipeId, output);
    }

    private static void addRecipe(IRecipeInput[] inputs, boolean press, int totalEu, ItemStack... outputs) {
        addRecipe(inputs, press, totalEu, outputs[0].getUnlocalizedName(), outputs);
    }

    private static void addRecipe(IRecipeInput[] inputs, boolean press, int totalEu, String recipeId, ItemStack... outputs) {
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
        mods.setBoolean(CONSUME_PRESS, press);
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(inlist, new MachineOutput(mods, outlist), recipeId);
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeId) {
        for (IRecipeInput in : input){
            if (in instanceof RecipeInputFluid && !validFluids.contains(((RecipeInputFluid)in).fluid.getFluid())){
                validFluids.add(((RecipeInputFluid)in).fluid.getFluid());
            }
        }
        GTCXRecipeLists.FLUID_CASTER_RECIPE_LIST.addRecipe(input, output, recipeId, defaultEu);
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        FluidStack fluid = this.inputTank.getFluid();
        map.put("Input Tank: " + (fluid != null ? fluid.amount + "mb of " + fluid.getLocalizedName() : "Empty"), false);
        fluid = this.waterTank.getFluid();
        map.put("Water Tank: " + (fluid != null ? fluid.amount + "mb of " + fluid.getLocalizedName() : "Empty"), false);
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
        return GTHelperFluid.doClickableFluidContainerEmptyThings(entityPlayer, enumHand, this.getMachineWorld(), this.getMachinePos(), inputTank) || GTHelperFluid.doClickableFluidContainerEmptyThings(entityPlayer, enumHand, this.getMachineWorld(), this.getMachinePos(), waterTank);
    }

    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }
}
