package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerElectrolyzer;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTFluidMachineOutput;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTItems;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers;
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
import net.minecraft.item.Item;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GTCXTileElectrolyzer extends GTTileBaseMachine implements ITankListener, IClickable, IFluidHandler {

    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialelectrolyzer.png");
    public IFilter filter = new MachineFilter(this);
    public static final int slotFuel = 15;
    public static final int SLOT_TANK = 14;
    public static final String NBT_TANK_IN = "inputTank";
    public static final String NBT_TANK_OUT = "outputTank";
    public static final List<Fluid> validFluids = new ArrayList<>();
    protected static final int[] slotInputs = { 0, 1 };
    protected static final int[] slotOutputs = { 2, 3, 4, 5, 6, 7 };
    @NetworkField(index = 13)
    private final IC2Tank inputTank = new IC2Tank(32000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && validFluids.contains(fluid.getFluid());
        }
    };

    @NetworkField(index = 14)
    private final LayeredFluidTank outputTank = new LayeredFluidTank(96000);
    private static final int defaultEu = 64;

    public GTCXTileElectrolyzer() {
        super(16, 2, defaultEu, 100, 128);
        setFuelSlot(slotFuel);
        this.inputTank.addListener(this);
        this.outputTank.addListener(this);
        this.addGuiFields(NBT_TANK_IN, NBT_TANK_OUT);
        maxEnergy = 10000;
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.DOWN, slotFuel);
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
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.inputTank.readFromNBT(nbt.getCompoundTag(NBT_TANK_IN));
        this.outputTank.readFromNBT(nbt.getCompoundTag(NBT_TANK_OUT));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.inputTank.writeToNBT(this.getTag(nbt, NBT_TANK_IN));
        this.outputTank.writeToNBT(this.getTag(nbt,NBT_TANK_OUT));
        return nbt;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.INDUSTRIAL_ELECTROLYZER;
    }

    @Override
    public void onTankChanged(IFluidTank tank) {
        this.setStackInSlot(SLOT_TANK, ItemDisplayIcon.createWithFluidStack(this.inputTank.getFluid()));
        this.getNetwork().updateTileGuiField(this, NBT_TANK_IN);
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
    public void process(MultiRecipe recipe) {
        MachineOutput output = recipe.getOutputs().copy();
        for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
            if (!(stack.getItem() instanceof ItemDisplayIcon)){
                outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
            }
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
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing!= null) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (facing!= null && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerElectrolyzer(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXIndustrialElectrolyzerGui.class;
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
        for (int i : this.getInputSlots()) {
            if (slot <= i) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] getOutputSlots() {
        return slotOutputs;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GTCXRecipeLists.ELECTROLYZER_RECIPE_LIST;
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
        return GTCExpansion.getAprilFirstSound(Ic2Sounds.magnetizerOp);
    }

    public static ItemStack[] empty = new ItemStack[]{};

    public static void init() {
        Item tube = GTItems.testTube;
        /** Recipes from GT1 **/
        addRecipe("dustCoal", 4, 0, totalEu(7500), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Carbon, 8)});
        addRecipe("dustRuby", 9, 0, totalEu(25000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Chrome, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 3000));
        addRecipe("dustSapphire", 8, 0, totalEu(20000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Aluminium, 2)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 3000));
        addRecipe("dustEmerald", 29, 0, totalEu(30000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 6), GTMaterialGen.getDust(GTMaterial.Beryllium, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 9000));
        addRecipe("dustEnderPearl", 16, 0, totalEu(65000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Beryllium, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Chlorine, 6000), GTMaterialGen.getFluidStack(GTMaterial.Nitrogen, 5000), GTMaterialGen.getFluidStack(GTMaterial.Potassium, 4000));
        addRecipe("dustLazurite", 29, 0, totalEu(295000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Aluminium, 3), GTMaterialGen.getDust(GTMaterial.Silicon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Sodium, 4000), GTMaterialGen.getFluidStack(GTMaterial.Calcium, 4000));
        addRecipe("dustPyrite", 3, 0, totalEu(15000), new ItemStack[]{GTMaterialGen.getIc2(Ic2Items.ironDust, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 2)});
        addRecipe("dustCalcite", 10, 0, totalEu(50000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Carbon, 2)}, GTMaterialGen.getFluidStack(GTMaterial.Calcium, 2000), GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 3000));
        addRecipe("dustSodalite", 11, 0, totalEu(115000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Aluminium, 3), GTMaterialGen.getDust(GTMaterial.Silicon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Chlorine), GTMaterialGen.getFluidStack(GTMaterial.Sodium, 4000));
        addRecipe("dustBauxite", 24, 0, totalEu(250000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Aluminium, 16), GTMaterialGen.getDust(GTMaterial.Titanium, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 6000), GTMaterialGen.getFluidStack(GTMaterial.Hydrogen, 10000));
        addRecipe(GTMaterialGen.get(Items.BLAZE_POWDER, 8), 0, totalEu(15000), new ItemStack[]{GTMaterialGen.getIc2(Ic2Items.coalDust, 2), GTMaterialGen.get(Items.GUNPOWDER, 1)});
        addRecipe(GTMaterialGen.get(Blocks.SAND, 32, 0), 0, totalEu(50000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Silicon, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000));
        addRecipe(GTMaterialGen.get(Blocks.SAND, 32, 1), 0, totalEu(50000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Silicon, 1), Ic2Items.clayDust.copy()}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 1000));
        addRecipe("dustFlint", 8, 0, totalEu(5000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Silicon, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen));

        /** Recipes from GT2 **/
        addRecipe("dustClay", 7, 0, totalEu(20000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Lithium, 1), GTMaterialGen.getDust(GTMaterial.Silicon, 2), GTMaterialGen.getDust(GTMaterial.Aluminium, 2)}, GTMaterialGen.getFluidStack(GTMaterial.Sodium, 2000));
        addRecipe("dustSaltpeter", 10, 0, totalEu(5500), empty, GTMaterialGen.getFluidStack(GTMaterial.Potassium, 2000), GTMaterialGen.getFluidStack(GTMaterial.Nitrogen, 2000), GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 3000));
        addRecipe("dustSphalerite", 4, 0, totalEu(15000), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Zinc, 2), GTMaterialGen.getDust(GTMaterial.Sulfur, 2)});
        addRecipe("dustOlivine", 9, 0, totalEu(36000), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Magnesium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 1), GTMaterialGen.getIc2(Ic2Items.ironDust, 2)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000));
        addRecipe("dustGalena", 2, 0, totalEu(120000), new ItemStack[]{GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 3), GTCXMaterialGen.getSmallDust(GTMaterial.Sulfur, 2)});
        addRecipe("dustObsidian", 8, 0, totalEu(5000), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Magnesium, 1), Ic2Items.ironDust.copy(), GTMaterialGen.getDust(GTMaterial.Silicon, 2)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 4000));
        addRecipe("dustCharcoal", 4, 0, totalEu(7500), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Carbon, 4)});
        addRecipe("dustPyrope", 20, 0, totalEu(89500), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Magnesium, 3), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 6000));
        addRecipe("dustAlmandine", 20, 0, totalEu(82000), new ItemStack[]{GTMaterialGen.getIc2(Ic2Items.ironDust, 3), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 6000));
        addRecipe("dustSpessartine", 20, 0, totalEu(90500), new ItemStack[]{GTMaterialGen.getDust(GTCXMaterial.Manganese, 3), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 6000));
        addRecipe("dustAndradite", 20, 0, totalEu(64000), new ItemStack[]{GTMaterialGen.getIc2(Ic2Items.ironDust, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Calcium, 3000), GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 6000));
        addRecipe("dustGrossular", 20, 0, totalEu(102500), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Calcium, 3000), GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 6000));
        addRecipe("dustUvarovite", 20, 0, totalEu(110000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Chrome, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Calcium, 3000), GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 6000));

        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.Glyceryl, 20000), 0, totalEu(720000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Carbon, 3)}, GTMaterialGen.getFluidStack(GTMaterial.Hydrogen, 5000), GTMaterialGen.getFluidStack(GTMaterial.Nitrogen, 3000));
        addRecipe(GTMaterialGen.getIc2(Ic2Items.airCell, 16), 0, totalEu(100000), new ItemStack[]{GTMaterialGen.getIc2(Ic2Items.emptyCell, 16)}, GTMaterialGen.getFluidStack(GTMaterial.Nitrogen, 9000), GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 4000), GTMaterialGen.getFluidStack(GTMaterial.Helium, 1000), GTMaterialGen.getFluidStack(GTMaterial.Neon, 1000), GTMaterialGen.getFluidStack(GTMaterial.Argon, 1000));
        addRecipe("dustSteel", 50, 0, totalEu(83200), new ItemStack[]{GTMaterialGen.getIc2(Ic2Items.ironDust, 50), GTMaterialGen.getDust(GTMaterial.Carbon, 1)});
        addRecipe("dustStainlessSteel", 9, 0, totalEu(14080), new ItemStack[]{GTMaterialGen.getIc2(Ic2Items.ironDust, 6), GTMaterialGen.getDust(GTMaterial.Chrome, 1), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTMaterialGen.getDust(GTCXMaterial.Manganese, 1)});

        /** New Recipes I added **/
        addRecipe(GTMaterialGen.get(Items.QUARTZ, 1), 0, totalEu(8000), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Silicon, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000));
        addRecipe("dustChromite",7, 0, totalEu(512000), new ItemStack[]{Ic2Items.ironDust, GTMaterialGen.getDust(GTMaterial.Chrome, 2)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 4000));
        addRecipe("dustCassiterite",1, 0, totalEu(512000), new ItemStack[]{Ic2Items.tinDust}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000));

        /** Fluid recipes **/
        addRecipe(GTMaterialGen.getFluidStack("water", 6000), 0, totalEu(50000), empty, GTMaterialGen.getFluidStack(GTMaterial.Hydrogen, 4000), GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid, 7000), 0, totalEu(32580), new ItemStack[]{ GTMaterialGen.getDust(GTMaterial.Sulfur, 1) }, GTMaterialGen.getFluidStack(GTMaterial.Hydrogen, 2000), GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate, 6000), 0, totalEu(38880), new ItemStack[]{ GTMaterialGen.getDust(GTMaterial.Sulfur, 1) }, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000), GTMaterialGen.getFluidStack(GTMaterial.Sodium, 1000));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.NitroCarbon, 2000), 0, totalEu(5760), new ItemStack[]{ GTMaterialGen.getDust(GTMaterial.Carbon, 1) }, GTMaterialGen.getFluidStack(GTMaterial.Nitrogen));
        addRecipe(GTMaterialGen.getFluidStack(GTMaterial.Methane, 5000), 0, totalEu(5760), new ItemStack[]{ GTMaterialGen.getDust(GTMaterial.Carbon, 1) }, GTMaterialGen.getFluidStack(GTMaterial.Hydrogen, 4000));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.SodiumSulfide, 2000), 0, totalEu(5760), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Sulfur, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Sodium));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.CarbonDioxide, 3000), 0, totalEu(5760), new ItemStack[]{GTMaterialGen.getDust(GTMaterial.Carbon, 1)}, GTMaterialGen.getFluidStack(GTMaterial.Oxygen, 2000));
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


    public static void addRecipe(ItemStack stack, int cells, IRecipeModifier[] modifiers, ItemStack[] outputs, FluidStack... fluidOutputs) {
        if (cells > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack),
                    new RecipeInputItemStack(GTMaterialGen.get(GTItems.testTube, cells)) }, modifiers, outputs, fluidOutputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack) }, modifiers, outputs, fluidOutputs);
        }
    }

    public static void addRecipe(String input, int amount, int cells, IRecipeModifier[] modifiers,
                                 ItemStack[] outputs, FluidStack... fluidOutputs) {
        if (cells > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount),
                    new RecipeInputItemStack(GTMaterialGen.get(GTItems.testTube, cells)) }, modifiers, outputs, fluidOutputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount) }, modifiers, outputs, fluidOutputs);
        }
    }

    public static void addRecipe(FluidStack fluid, int cells, IRecipeModifier[] modifiers, ItemStack[] outputs, FluidStack... fluidOutputs) {
        if (cells > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid),
                    new RecipeInputItemStack(GTMaterialGen.get(GTItems.testTube, cells)) }, modifiers, outputs, fluidOutputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid) }, modifiers, outputs, fluidOutputs);
        }
    }

    public static IRecipeModifier[] totalEu(int amount) {
        return new IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
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
            if (in instanceof RecipeInputFluid && !validFluids.contains(((RecipeInputFluid)in).fluid.getFluid())){
                validFluids.add(((RecipeInputFluid)in).fluid.getFluid());
            }
        }
        GTCXRecipeLists.ELECTROLYZER_RECIPE_LIST.addRecipe(input, output, (output instanceof GTFluidMachineOutput ? ((GTFluidMachineOutput)output).getFluids().get(0).getUnlocalizedName() : output.getAllOutputs().get(0).getUnlocalizedName()), defaultEu);
    }

    public static void removeRecipe(String id) {
        GTCXRecipeLists.ELECTROLYZER_RECIPE_LIST.removeRecipe(id);
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
        return GTHelperFluid.doClickableFluidContainerEmptyThings(player, hand, world, pos, inputTank) || doClickableFluidContainerFillThings(player, hand, world, pos, outputTank);
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
