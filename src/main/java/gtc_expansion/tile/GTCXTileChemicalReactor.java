package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerChemicalReactor;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTFluidMachineOutput;
import gtclassic.api.recipe.GTRecipeCraftingHandler;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTItems;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.fluid.IC2Tank;
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
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GTCXTileChemicalReactor extends GTTileBaseMachine implements ITankListener, IClickable, IGTDebuggableTile, IFluidHandler {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/chemicalreactor.png");
    protected static final int[] slotInputs = { 0, 1, 2 };
    public static final int[] slotOutputs = { 3, 4 };
    public static final int slotFuel = 5;
    public static final int slotDisplayIn1 = 6;
    public static final int slotDisplayIn2 = 7;
    public static final int slotDisplayOut = 8;
    public static final List<Fluid> validFluids = new ArrayList<>();
    public IFilter filter = new MachineFilter(this);
    private static final int defaultEu = 16;
    @NetworkField(index = 13)
    private final IC2Tank inputTank1 = new IC2Tank(16000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && (inputTank2.getFluid() == null || !inputTank2.getFluid().isFluidEqual(fluid)) && validFluids.contains(fluid.getFluid());
        }
    };
    @NetworkField(index = 14)
    private final IC2Tank inputTank2 = new IC2Tank(16000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && (inputTank1.getFluid() == null || !inputTank1.getFluid().isFluidEqual(fluid)) && validFluids.contains(fluid.getFluid());
        }
    };
    @NetworkField(index = 15)
    private final IC2Tank outputTank = new IC2Tank(16000);

    public GTCXTileChemicalReactor() {
        super(9, 2, defaultEu, 100, 32);
        this.inputTank1.addListener(this);
        this.inputTank2.addListener(this);
        this.outputTank.addListener(this);
        this.outputTank.setCanFill(false);
        this.addGuiFields("inputTank1", "inputTank2", "outputTank");
        setFuelSlot(slotFuel);
        maxEnergy = 10000;
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutputs);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerInputFilter(filter, slotInputs);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Input, slotInputs);
        handler.registerSlotType(SlotType.Output, slotOutputs);
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.CHEMICAL_REACTOR;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerChemicalReactor(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXChemicalReactorGui.class;
    }

    @Override
    public void onTankChanged(IFluidTank tank) {
        this.inventory.set(slotDisplayIn1, ItemDisplayIcon.createWithFluidStack(this.inputTank1.getFluid()));
        this.getNetwork().updateTileGuiField(this, "inputTank1");
        this.inventory.set(slotDisplayIn2, ItemDisplayIcon.createWithFluidStack(this.inputTank2.getFluid()));
        this.getNetwork().updateTileGuiField(this, "inputTank2");
        this.inventory.set(slotDisplayOut, ItemDisplayIcon.createWithFluidStack(this.outputTank.getFluid()));
        this.getNetwork().updateTileGuiField(this, "outputTank");
        shouldCheckRecipe = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.inputTank1.readFromNBT(nbt.getCompoundTag("inputTank1"));
        this.inputTank2.readFromNBT(nbt.getCompoundTag("inputTank2"));
        this.outputTank.readFromNBT(nbt.getCompoundTag("outputTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.inputTank1.writeToNBT(this.getTag(nbt, "inputTank1"));
        this.inputTank2.writeToNBT(this.getTag(nbt, "inputTank2"));
        this.outputTank.writeToNBT(this.getTag(nbt, "outputTank"));
        return nbt;
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
        MachineOutput outputs = lastRecipe.getOutputs();
        int empty = 0;
        int[] outputSlots = getOutputSlots();
        for (int slot : outputSlots) {
            if (getStackInSlot(slot).isEmpty()) {
                empty++;
            }
        }
        if (outputs instanceof GTFluidMachineOutput){
            GTFluidMachineOutput fluidOutputs = (GTFluidMachineOutput) outputs;
            if (outputTank.getFluidAmount() == 0 && empty == outputSlots.length){
                return lastRecipe;
            }
            FluidStack fluid = outputTank.getFluid();
            boolean fluidFits = false;
            for (FluidStack output : fluidOutputs.getFluids()){
                if (output.isFluidEqual(fluid) && outputTank.getFluidAmount() + output.amount <= outputTank.getCapacity()){
                    fluidFits = true;
                    break;
                }
            }
            if (fluidFits && empty == outputSlots.length){
                return lastRecipe;
            }
            if (fluidFits || outputTank.getFluidAmount() == 0){
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
        } else {
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
        }
        return null;
    }

    @Override
    public boolean checkRecipe(MultiRecipe entry, List<ItemStack> inputs) {
        FluidStack fluidInput1 = inputTank1.getFluid();
        FluidStack fluidInput2 = inputTank2.getFluid();
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(entry.getInputs());
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
            if (key instanceof RecipeInputFluid){
                FluidStack fluidStack = ((RecipeInputFluid)key).fluid;
                if (fluidInput1 != null && fluidInput1.isFluidEqual(fluidStack) && fluidInput1.amount >= fluidStack.amount){
                    keyIter.remove();
                    continue;
                }
                if (fluidInput2 != null && fluidInput2.isFluidEqual(fluidStack) && fluidInput2.amount >= fluidStack.amount){
                    keyIter.remove();
                    continue;
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
    public void process(MultiRecipe recipe) {
        MachineOutput output = recipe.getOutputs().copy();
        if (output instanceof GTFluidMachineOutput){
            for (FluidStack fluid : ((GTFluidMachineOutput)output).getFluids()){
                outputTank.fillInternal(fluid, true);
                onRecipeComplete();
            }
        } else {
            for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
                outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
                onRecipeComplete();
            }
        }

        NBTTagCompound nbt = recipe.getOutputs().getMetadata();
        boolean shiftContainers = nbt != null && nbt.getBoolean(MOVE_CONTAINER_TAG);
        List<ItemStack> inputs = getInputs();
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(recipe.getInputs());
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
            if (key instanceof RecipeInputFluid){
                FluidStack fluidStack = ((RecipeInputFluid)key).fluid;
                if (inputTank1.getFluid() != null && inputTank1.getFluid().isFluidEqual(fluidStack)){
                    inputTank1.drainInternal(fluidStack.amount, true);
                    keyIter.remove();
                    continue;
                }
                if (inputTank2.getFluid() != null && inputTank2.getFluid().isFluidEqual(fluidStack)){
                    inputTank2.drainInternal(fluidStack.amount, true);
                    keyIter.remove();
                    continue;
                }
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
        shouldCheckRecipe = true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing!= null) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, facing);
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
    public int[] getInputSlots() {
        return slotInputs;
    }

    @Override
    public IFilter[] getInputFilters(int[] slots) {
        return new IFilter[] { filter };
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        return slot != slotFuel;
    }

    @Override
    public int[] getOutputSlots() {
        return slotOutputs;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GTCXRecipeLists.CHEMICAL_REACTOR_RECIPE_LIST;
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
        return GTCExpansion.getAprilFirstSound(Ic2Sounds.extractorOp);
    }

    public static void init() {
        addRecipe(input(GTMaterialGen.getFluidStack(GTMaterial.Calcium)), input("dustCarbon", 1), 7500, GTMaterialGen.getDust(GTMaterial.Calcite, 2));
        addRecipe(input("dustCarbon", 1), input(GTMaterialGen.getFluidStack(GTMaterial.Hydrogen, 4000)), 105000, GTMaterialGen.getFluidStack(GTMaterial.Methane, 5000));
        addRecipe(input("dustCarbon", 1), input(GTMaterialGen.getFluidStack(GTMaterial.Nitrogen)), 45000, GTMaterialGen.getFluidStack(GTCXMaterial.NitroCarbon, 2000));
        addRecipe(input("dustSulfur", 1), input(GTMaterialGen.getFluidStack(GTMaterial.Sodium)), 3000, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumSulfide, 2000));
        addRecipe(input(GTMaterialGen.getFluidStack(GTCXMaterial.NitroCarbon)), input(GTMaterialGen.getFluidStack("water", 1000)), 17490, GTMaterialGen.getFluidStack(GTCXMaterial.Glyceryl, 2000));
        addRecipe(input(GTMaterialGen.getFluidStack(GTCXMaterial.SodiumSulfide)), input(GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000)), 60000, GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate, 3000));
        addRecipe(input(GTMaterialGen.getFluidStack(GTMaterial.Nitrogen)), input(GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000)), 37500, GTMaterialGen.getFluidStack(GTCXMaterial.NitrogenDioxide, 3000));
        addRecipe(input(GTMaterialGen.getFluidStack(GTCXMaterial.Glyceryl)), input(GTMaterialGen.getFluidStack(GTCXMaterial.Diesel, 4000)), 30000, GTMaterialGen.getFluidStack(GTCXMaterial.NitroDiesel, 5000));
        addRecipe(input(GTMaterialGen.getFluidStack(GTCXMaterial.Glyceryl)), input(GTMaterialGen.getIc2(Ic2Items.coalFuelCell, 4)), 30000, GTMaterialGen.getFluidStack(GTCXMaterial.NitroCoalFuel, 5000), GTMaterialGen.getIc2(Ic2Items.emptyCell, 4));
        addRecipe(input(GTMaterialGen.getFluidStack(GTCXMaterial.Glyceryl)), input(GTMaterialGen.getFluidStack(GTCXMaterial.CoalFuel, 4000)), 30000, GTMaterialGen.getFluidStack(GTCXMaterial.NitroCoalFuel, 5000));
        addRecipe(input(GTMaterialGen.getDust(GTMaterial.Sulfur, 1)), input(GTMaterialGen.getFluidStack("water", 2000)), 34500, GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid, 3000));
        addRecipe(input(GTMaterialGen.getFluidStack(GTMaterial.Hydrogen, 4000)), input(GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000)), 300, GTMaterialGen.getFluidStack("water", 6000));
        addRecipe(input(GTMaterialGen.getFluidStack(GTCXMaterial.NitrogenDioxide, 5000)), input(GTMaterialGen.getFluidStack(GTMaterial.Sodium, 2000)), 1000, GTMaterialGen.get(Items.GUNPOWDER, 5));
        addRecipe(new IRecipeInput[]{input(GTMaterialGen.get(GTCXItems.magicDye)), input(GTMaterialGen.get(Items.BLAZE_POWDER)), input(GTMaterialGen.getFluidStack(GTMaterial.Chlorine))}, totalEu(5000), GTMaterialGen.getFluidStack(GTMaterial.MagicDye));
        addRecipe(new IRecipeInput[]{input("pulpWood", 4), input("dustSulfur", 1), input(GTMaterialGen.getFluidStack(GTMaterial.Sodium))}, totalEu(6400), GTMaterialGen.get(GTItems.fuelBinder, 6));
        addRecipe(new IRecipeInput[]{input("pulpWood", 4), input("dustSulfur", 1), input("dustLithium", 1)}, totalEu(6400), GTMaterialGen.get(GTItems.fuelBinder, 6));
        addRecipe(new IRecipeInput[]{input(GTMaterialGen.get(GTItems.fuelBinder)), input(GTMaterialGen.getFluidStack(GTMaterial.Mercury)), new RecipeInputCombined(1, input("dustEnderEye", 1), input(GTMaterialGen.get(Items.BLAZE_POWDER)))}, totalEu(6400), GTMaterialGen.get(GTItems.fuelBinderMagic, 3));
        addRecipe(input(GTMaterialGen.getFluidStack(GTCXMaterial.NitricAcid)), input(GTMaterialGen.getFluidStack(GTMaterial.Potassium)), 600, GTMaterialGen.getDust(GTCXMaterial.Saltpeter, 1));
        addRecipe(input(GTMaterialGen.getFluidStack(GTCXMaterial.NitrogenDioxide, 3000)), input(GTMaterialGen.getFluidStack("water", 1000)), 600, GTMaterialGen.getFluidStack(GTCXMaterial.NitricAcid, 2000));
        addRecipe(new IRecipeInput[]{input(GTValues.BLOCK_COAL, 1), input(GTMaterialGen.get(GTItems.fuelBinder, 2)), input(GTMaterialGen.getFluidStack(GTMaterial.Fuel))}, totalEu(6400), GTMaterialGen.get(GTBlocks.superFuel));
        IRecipeInput dusts = GTRecipeCraftingHandler.combineRecipeObjects("dustEmerald", "dustSapphire", "dustThorium");
        addRecipe(new IRecipeInput[]{input(GTMaterialGen.get(GTBlocks.superFuel)), input(GTMaterialGen.get(GTItems.fuelBinderMagic, 3))}, totalEu(6400), GTMaterialGen.get(GTBlocks.superFuelMagic));
        addRecipe(input("dustCalcite", 1), input("dustPhosphorus", 1), 3200, GTMaterialGen.getIc2(Ic2Items.fertilizer, 3));
        addRecipe(input("dustSilicon", 1), input(GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000)), 12800, GTMaterialGen.getDust(GTCXMaterial.SiliconDioxide, 3));
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, int totalEu, FluidStack fluidOutput, ItemStack... output) {
        IRecipeInput[] inputs = new IRecipeInput[]{input1, input2};
        addRecipe(inputs, totalEu(totalEu), fluidOutput, output);
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, int totalEu, ItemStack... output) {
        IRecipeInput[] inputs = new IRecipeInput[]{input1, input2};
        addRecipe(inputs, totalEu(totalEu), output);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, ItemStack... outputs) {
        addRecipe(inputs, modifiers, outputs[0].getUnlocalizedName(), outputs);
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, FluidStack fluidOutput, ItemStack... outputs) {
        addRecipe(inputs, modifiers, fluidOutput, fluidOutput.getUnlocalizedName(), outputs);
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, String recipeId, ItemStack... outputs) {
        List<IRecipeInput> inlist = new ArrayList<>();
        List<ItemStack> outlist = new ArrayList<>();
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(inlist, new MachineOutput(mods, outlist), recipeId);
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, FluidStack fluidOutput, String recipeId, ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        List<FluidStack> fluidOutlist = new ArrayList<>();
        fluidOutlist.add(fluidOutput);
        List<IRecipeInput> inlist = new ArrayList<>(Arrays.asList(inputs));
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        if (outputs.length > 0){
            outlist.addAll(Arrays.asList(outputs));
        }
        GTFluidMachineOutput output = outputs.length > 0 ? new GTFluidMachineOutput(mods, outlist, fluidOutlist) : new GTFluidMachineOutput(mods, fluidOutlist);
        addRecipe(inlist, output, recipeId);
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeId) {
        for (IRecipeInput in : input){
            if (in instanceof RecipeInputFluid && !validFluids.contains(((RecipeInputFluid)in).fluid.getFluid())){
                validFluids.add(((RecipeInputFluid)in).fluid.getFluid());
            }
        }
        GTCXRecipeLists.CHEMICAL_REACTOR_RECIPE_LIST.addRecipe(input, output, recipeId, 16);
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        FluidStack fluid = this.inputTank1.getFluid();
        map.put("Input Tank 1: " + (fluid != null ? fluid.amount + "mb of " + fluid.getLocalizedName() : "Empty"), false);
        fluid = this.inputTank2.getFluid();
        map.put("Input Tank 2: " + (fluid != null ? fluid.amount + "mb of " + fluid.getLocalizedName() : "Empty"), false);
        fluid = this.outputTank.getFluid();
        map.put("Output Tank: " + (fluid != null ? fluid.amount + "mb of " + fluid.getLocalizedName() : "Empty"), false);
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing enumFacing, Side side) {
        return GTHelperFluid.doClickableFluidContainerEmptyThings(player, hand, world, pos, inputTank1) || GTHelperFluid.doClickableFluidContainerEmptyThings(player, hand, world, pos, inputTank2) || GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, outputTank);
    }

    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        List<IFluidTankProperties> combined = new ArrayList<>();
        Stream.of(inputTank1.getTankProperties(), inputTank2.getTankProperties(), outputTank.getTankProperties()).flatMap(Stream::of).forEach(combined::add);
        return combined.toArray(new IFluidTankProperties[0]);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return inputTank1.getFluid() == null || inputTank1.getFluid().isFluidEqual(resource) ? inputTank1.fill(resource, doFill) : inputTank2.fill(resource, doFill);
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
