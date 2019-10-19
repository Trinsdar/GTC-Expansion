package gtc_expansion.tile.multi;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerDistillationTower;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.util.GELang;
import gtc_expansion.util.GTFluidMachineOutput;
import gtclassic.GTBlocks;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.multi.GTTileMultiBaseMachine;
import gtclassic.util.int3;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
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
import ic2.core.inventory.transport.wrapper.RangedInventoryWrapper;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.ITankListener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class GETileMultiDistillationTower extends GTTileMultiBaseMachine implements ITankListener {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/distillationtower.png");
    public static final IBlockState standardCasingState = GEBlocks.casingStandard.getDefaultState();
    public static final IBlockState advancedCasingState = GEBlocks.casingAdvanced.getDefaultState();
    public static final IBlockState airState = Blocks.AIR.getDefaultState();
    public static final int slotDisplayIn = 0;
    public static final int slotFuel = 1;
    public static final int slotDisplayOut1 = 2;
    public static final int slotDisplayOut2 = 3;
    public static final int slotDisplayOut3 = 4;
    public static final int slotDisplayOut4 = 5;
    public static final int[] slotOutputs = { 6, 7 };
    private static final int defaultEu = 64;
    private IC2Tank inputTank = new IC2Tank(16000);
    private IC2Tank outputTank1 = new IC2Tank(16000);
    private IC2Tank outputTank2 = new IC2Tank(16000);
    private IC2Tank outputTank3 = new IC2Tank(16000);
    private IC2Tank outputTank4 = new IC2Tank(16000);

    public GETileMultiDistillationTower() {
        super(8, 2, defaultEu, 128);
        setFuelSlot(slotFuel);
        maxEnergy = 10000;
        this.inputTank.addListener(this);
        this.addGuiFields("inputTank");
        this.outputTank1.addListener(this);
        this.addGuiFields("outputTank1");
        this.outputTank2.addListener(this);
        this.addGuiFields("outputTank2");
        this.outputTank3.addListener(this);
        this.addGuiFields("outputTank3");
        this.outputTank4.addListener(this);
        this.addGuiFields("outputTank4");
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutputs);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Output, slotOutputs);
    }

    @Override
    public LocaleComp getBlockName() {
        return GELang.DISTILLATION_TOWER;
    }

    @Override
    public void onTankChanged(IFluidTank tank) {
        this.getNetwork().updateTileGuiField(this, "inputTank");
        this.inventory.set(slotDisplayIn, ItemDisplayIcon.createWithFluidStack(this.inputTank.getFluid()));
        this.getNetwork().updateTileGuiField(this, "outputTank1");
        this.inventory.set(slotDisplayOut1, ItemDisplayIcon.createWithFluidStack(this.outputTank1.getFluid()));
        this.getNetwork().updateTileGuiField(this, "outputTank2");
        this.inventory.set(slotDisplayOut2, ItemDisplayIcon.createWithFluidStack(this.outputTank2.getFluid()));
        this.getNetwork().updateTileGuiField(this, "outputTank3");
        this.inventory.set(slotDisplayOut3, ItemDisplayIcon.createWithFluidStack(this.outputTank3.getFluid()));
        this.getNetwork().updateTileGuiField(this, "outputTank4");
        this.inventory.set(slotDisplayOut4, ItemDisplayIcon.createWithFluidStack(this.outputTank4.getFluid()));
        shouldCheckRecipe = true;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GEContainerDistillationTower(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GEMachineGui.GEDistillationTowerGui.class;
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{};
    }

    @Override
    public IFilter[] getInputFilters(int[] slots) {
        return new IFilter[] { new MachineFilter(this) };
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
        return GERecipeLists.DISTILLATION_TOWER_RECIPE_LIST;
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
        return Ic2Sounds.electrolyzerOp;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.inputTank.readFromNBT(nbt.getCompoundTag("inputTank"));
        this.outputTank1.readFromNBT(nbt.getCompoundTag("outputTank1"));
        this.outputTank2.readFromNBT(nbt.getCompoundTag("outputTank2"));
        this.outputTank3.readFromNBT(nbt.getCompoundTag("outputTank3"));
        this.outputTank4.readFromNBT(nbt.getCompoundTag("outputTank4"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.inputTank.writeToNBT(this.getTag(nbt, "inputTank"));
        this.outputTank1.writeToNBT(this.getTag(nbt, "outputTank1"));
        this.outputTank2.writeToNBT(this.getTag(nbt, "outputTank2"));
        this.outputTank3.writeToNBT(this.getTag(nbt, "outputTank3"));
        this.outputTank4.writeToNBT(this.getTag(nbt, "outputTank4"));
        return nbt;
    }

    public EnumFacing left(){
        if (this.getFacing() == EnumFacing.NORTH){
            return EnumFacing.EAST;
        }
        if (this.getFacing() == EnumFacing.WEST){
            return EnumFacing.NORTH;
        }
        if (this.getFacing() == EnumFacing.SOUTH){
            return EnumFacing.WEST;
        }
        if (this.getFacing() == EnumFacing.EAST){
            return EnumFacing.SOUTH;
        }
        return this.getFacing();
    }

    public EnumFacing right(){
        if (this.getFacing() == EnumFacing.NORTH){
            return EnumFacing.WEST;
        }
        if (this.getFacing() == EnumFacing.WEST){
            return EnumFacing.SOUTH;
        }
        if (this.getFacing() == EnumFacing.SOUTH){
            return EnumFacing.EAST;
        }
        if (this.getFacing() == EnumFacing.EAST){
            return EnumFacing.NORTH;
        }
        return this.getFacing();
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
            if (facing == EnumFacing.UP || facing == left()){
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.inputTank);
            }else if (facing == EnumFacing.DOWN || facing == right()){
                if (outputTank1.getFluidAmount() > 0){
                    return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.outputTank1);
                }
                if (outputTank2.getFluidAmount() > 0){
                    return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.outputTank2);
                }
                if (outputTank3.getFluidAmount() > 0){
                    return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.outputTank3);
                }
                if (outputTank4.getFluidAmount() > 0){
                    return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.outputTank4);
                }
            }else {
                return super.getCapability(capability, facing);
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void process(GTRecipeMultiInputList.MultiRecipe recipe) {
        GTFluidMachineOutput output;
        IRecipeInput input = recipe.getInput(0);
        if (recipe.getOutputs() instanceof GTFluidMachineOutput && input instanceof RecipeInputFluid){
            output = (GTFluidMachineOutput) recipe.getOutputs();
            for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
                if (!(stack.getItem() instanceof ItemDisplayIcon)){
                    // outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
                }
            }
            inputTank.drain(((RecipeInputFluid) input).fluid, true);
            NBTTagCompound nbt = recipe.getOutputs().getMetadata();
            for (FluidStack fluid : output.getFluids()){
                if (outputTank1.getFluidAmount() == 0 || outputTank1.getFluid().isFluidEqual(fluid)){
                    outputTank1.fill(fluid, true);
                } else if (outputTank2.getFluidAmount() == 0 || outputTank2.getFluid().isFluidEqual(fluid)){
                    outputTank2.fill(fluid, true);
                } else if (outputTank3.getFluidAmount() == 0 || outputTank3.getFluid().isFluidEqual(fluid)){
                    outputTank3.fill(fluid, true);
                } else if (outputTank4.getFluidAmount() == 0 || outputTank4.getFluid().isFluidEqual(fluid)){
                    outputTank4.fill(fluid, true);
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
        } else {
            GTCExpansion.logger.info("Recipe: " + recipe.getRecipeID() + " not using methods in tile! Only use the methods in GETileMultiDistillationTower!");
        }
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
    public GTRecipeMultiInputList.MultiRecipe getRecipe() {
        if (lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            return null;
        }
        // Check if previous recipe is valid
        FluidStack input = inputTank.getFluid();
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, input) ? lastRecipe : null;
            if (lastRecipe == null) {
                progress = 0;
            }
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<GTRecipeMultiInputList.MultiRecipe>() {

                @Override
                public boolean test(GTRecipeMultiInputList.MultiRecipe t) {
                    return checkRecipe(t, input);
                }
            });
        }
        // If no recipe is found, return
        if (lastRecipe == null) {
            return null;
        }
        if (!(lastRecipe.getOutputs() instanceof GTFluidMachineOutput)){
            return null;
        }
        GTFluidMachineOutput output = (GTFluidMachineOutput) lastRecipe.getOutputs();
        applyRecipeEffect(output);
        int empty = 0;
        int[] outputSlots = getOutputSlots();
        for (int slot : outputSlots) {
            if (getStackInSlot(slot).isEmpty()) {
                empty++;
            }
        }
        int emptyTanks = 0;
        if (outputTank1.getFluidAmount() == 0) emptyTanks++;
        if (outputTank2.getFluidAmount() == 0) emptyTanks++;
        if (outputTank3.getFluidAmount() == 0) emptyTanks++;
        if (outputTank4.getFluidAmount() == 0) emptyTanks++;
        if (empty == outputSlots.length && emptyTanks == 4) {
            return lastRecipe;
        }
        int fluidListSize = output.getFluids().size();
        int availableTanks = 0;
        for (FluidStack fluid : output.getFluids()){
            if ((fluid.isFluidEqual(outputTank1.getFluid()) && outputTank1.getFluidAmount() + fluid.amount <= outputTank1.getCapacity()) || outputTank1.getFluidAmount() == 0){
                availableTanks++;
            } else if ((fluid.isFluidEqual(outputTank2.getFluid()) && outputTank2.getFluidAmount() + fluid.amount <= outputTank2.getCapacity()) || outputTank2.getFluidAmount() == 0){
                availableTanks++;
            } else if ((fluid.isFluidEqual(outputTank3.getFluid()) && outputTank3.getFluidAmount() + fluid.amount <= outputTank3.getCapacity()) || outputTank3.getFluidAmount() == 0){
                availableTanks++;
            } else if ((fluid.isFluidEqual(outputTank4.getFluid()) && outputTank4.getFluidAmount() + fluid.amount <= outputTank4.getCapacity()) || outputTank4.getFluidAmount() == 0){
                availableTanks++;
            } else {
                availableTanks = 0;
            }
        }
        if (fluidListSize <= 4 && availableTanks == fluidListSize){
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

    public boolean checkRecipe(GTRecipeMultiInputList.MultiRecipe entry, FluidStack input) {
        IRecipeInput recipeInput = entry.getInput(0);
        if (recipeInput instanceof RecipeInputFluid){
            return input != null && input.containsFluid(((RecipeInputFluid)recipeInput).fluid);
        }
        return false;
    }

    @Override
    public IHasInventory getInputInventory() {
        int[] input = getInputSlots();
        RangedInventoryWrapper result = new RangedInventoryWrapper(this, input).addFilters(getInputFilters(input));
        return result;
    }

    public static void init(){
        addRecipe(GTMaterialGen.getFluidStack(GEMaterial.OilCrude, 8000), 256000, GTMaterialGen.getFluidStack(GEMaterial.Diesel, 4000), GTMaterialGen.getFluidStack(GEMaterial.Glyceryl, 500), GTMaterialGen.getFluidStack(GEMaterial.SulfuricAcid, 4000), GTMaterialGen.getFluidStack(GEMaterial.Naphtha, 4000));
        addRecipe(GTMaterialGen.getFluidStack(GTMaterial.Oil, 8000), 256000, GTMaterialGen.getFluidStack(GEMaterial.Diesel, 4000), GTMaterialGen.getFluidStack(GEMaterial.Glyceryl, 500), GTMaterialGen.getFluidStack(GEMaterial.SulfuricAcid, 4000), GTMaterialGen.getFluidStack(GEMaterial.Naphtha, 4000));
        addRecipe(GTMaterialGen.getFluidStack(GEMaterial.Naphtha, 4000), 64000, new FluidStack[]{GTMaterialGen.getFluidStack(GEMaterial.Gasoline, 4000), GTMaterialGen.getFluidStack(GEMaterial.Propane, 4000), GTMaterialGen.getFluidStack(GTMaterial.Methane,3500)}, GTMaterialGen.getDust(GEMaterial.Carbon, 1));
    }

    public static void addRecipe(FluidStack input, int totalEu, FluidStack[] outputFluid, ItemStack... outputItem){
        if (outputFluid.length > 4){
            GTCExpansion.logger.info("There can only be up to 4 fluid outputs");
            return;
        }
        List<ItemStack> outListItem = new ArrayList<>();
        for (ItemStack item : outputItem){
            outListItem.add(item);
        }
        List<FluidStack> outListFluid = new ArrayList<>();
        for (FluidStack fluid : outputFluid){
            outListFluid.add(fluid);
        }

        addRecipe(new IRecipeInput[]{new RecipeInputFluid(input)}, totalEu(totalEu), outListFluid, outListItem);
    }

    public static void addRecipe(FluidStack input, int totalEu, FluidStack... outputFluid){
        if (outputFluid.length > 4){
            GTCExpansion.logger.info("There can only be up to 4 fluid outputs");
            return;
        }
        List<ItemStack> outListItem = new ArrayList<>();
        outListItem.add(ItemDisplayIcon.createWithFluidStack(new FluidStack(FluidRegistry.WATER, 1000)));
        List<FluidStack> outListFluid = new ArrayList<>();
        for (FluidStack fluid : outputFluid){
            outListFluid.add(fluid);
        }

        addRecipe(new IRecipeInput[]{new RecipeInputFluid(input)}, totalEu(totalEu), outListFluid, outListItem);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    private static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, List<FluidStack> fluidOutputs, List<ItemStack> outputs) {
        List<IRecipeInput> inlist = new ArrayList<>();
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }

        addRecipe(inlist, new GTFluidMachineOutput(mods, outputs, fluidOutputs), fluidOutputs.get(0).getUnlocalizedName());
    }

    private static void addRecipe(List<IRecipeInput> input, GTFluidMachineOutput output, String recipeId) {
        GERecipeLists.DISTILLATION_TOWER_RECIPE_LIST.addRecipe(input, output, recipeId, defaultEu);
    }

    @Override
    public Map<BlockPos, IBlockState> provideStructure() {
        Map<BlockPos, IBlockState> states = super.provideStructure();
        int3 dir = new int3(getPos(), getFacing());
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);

        states.put(dir.left(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        BlockPos down = dir.down(1).asBlockPos();
        if (world.getBlockState(down) != GTBlocks.tileBufferFluid.getDefaultState()){
            states.put(down, standardCasingState);
        }

        states.put(dir.back(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);

        states.put(dir.right(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), airState);
        states.put(dir.down(1).asBlockPos(), airState);
        states.put(dir.down(1).asBlockPos(), airState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);

        states.put(dir.right(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);

        states.put(dir.back(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);

        states.put(dir.left(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);

        states.put(dir.left(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);

        states.put(dir.forward(2).right(2).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        return states;
    }

    @Override
    public boolean checkStructure() {
        if (!world.isAreaLoaded(pos, 3)) {
            return false;
        }
        // we doing it "big math" style not block by block
        int3 dir = new int3(getPos(), getFacing());
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }

        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        BlockPos down = dir.down(1).asBlockPos();
        if (world.getBlockState(down) != standardCasingState && world.getBlockState(down) != GTBlocks.tileBufferFluid.getDefaultState()) {
            return false;
        }

        if (!isStandardCasing(dir.back(1))) {// back
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }

        if (!isStandardCasing(dir.right(1))) {// right
            return false;
        }
        if (!isAir(dir.down(1))) {
            return false;
        }
        if (!isAir(dir.down(1))) {
            return false;
        }
        if (!isAir(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }

        if (!isStandardCasing(dir.right(1))) {// right
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }

        if (!isStandardCasing(dir.back(1))) {// back
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }

        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }

        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }

        if (!isStandardCasing(dir.forward(2).right(2))) {// missing front right column
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }

        return true;
    }

    public boolean isStandardCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == standardCasingState;
    }

    public boolean isAdvancedCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == advancedCasingState;
    }

    public boolean isAir(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == airState;
    }
}
