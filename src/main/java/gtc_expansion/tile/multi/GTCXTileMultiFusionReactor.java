package gtc_expansion.tile.multi;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui.GTCXFusionComputerGui;
import gtc_expansion.container.GTCXContainerFusionReactor;
import gtc_expansion.container.GTCXContainerFusionReactorHatch;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.interfaces.IGTEnergySource;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch.GTCXTileFusionEnergyExtractor;
import gtc_expansion.tile.hatch.GTCXTileFusionEnergyInjector;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileFusionMaterialExtractor;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileFusionMaterialInjector;
import gtc_expansion.util.GTCXTank;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialElement;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTFluidMachineOutput;
import gtclassic.api.recipe.GTRecipeMachineHandler;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.multi.GTTileMultiBaseMachine;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTLang;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers.IRecipeModifier;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
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
import ic2.core.util.obj.ITankListener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static gtclassic.common.tile.multi.GTTileMultiFusionReactor.RECIPE_LIST;

public class GTCXTileMultiFusionReactor extends GTTileMultiBaseMachine implements IGTOwnerTile, IGTEnergySource, IMetaDelegate, ITankListener, INetworkClientTileEntityEventListener, IGTDebuggableTile {
    static final IBlockState COIL_STATE = GTBlocks.casingFusion.getDefaultState();
    static final IBlockState CASING_STATE = GTCXBlocks.casingAdvanced.getDefaultState();
    static final IBlockState MATERIAL_INJECTOR_STATE = GTCXBlocks.fusionMaterialInjector.getDefaultState();
    static final IBlockState MATERIAL_EXTRACTOR_STATE = GTCXBlocks.fusionMaterialExtractor.getDefaultState();
    static final IBlockState ENERGY_INJECTOR_STATE = GTCXBlocks.fusionEnergyInjector.getDefaultState();
    static final IBlockState ENERGY_EXTRACTOR_STATE = GTCXBlocks.fusionEnergyExtractor.getDefaultState();
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/fusioncomputer.png");
    public static final String START_EU = "startEu";
    public boolean usedStartEnergy = false;
    private BlockPos input1;
    private BlockPos input2;
    private BlockPos output;
    private BlockPos energyOutput;
    private List<BlockPos> energyInputList = new ArrayList<>();
    @NetworkField(index = 13)
    private GTCXTank inputTank1 = new GTCXTank(32000);
    @NetworkField(index = 14)
    private GTCXTank inputTank2 = new GTCXTank(32000);
    @NetworkField(index = 15)
    private GTCXTank outputTank = new GTCXTank(32000);
    private static int slotInput1 = 0;
    private static int slotFakeOutput1 = 1;
    private static int slotInput2 = 2;
    private static int slotFakeOutput2 = 3;
    private static int slotFakeInput = 4;
    private static int slotOutput = 5;
    private static int slotDisplayIn1 = 6;
    private static int slotDisplayIn2 = 7;
    private static int slotDisplayOut = 8;
    public int maxProducedEnergy = 1000000000;
    @NetworkField(
            index = 16
    )
    public int producedEnergy;
    //public boolean lastState;
    public boolean firstCheck = true;
    List<IEnergyTile> lastPositions = null;
    protected InventoryHandler secondHandler = new InventoryHandler(this);
    private int tickOffset = 0;
    private GTCXTileFusionMaterialInjector inputHatch1 = null;
    private GTCXTileFusionMaterialInjector inputHatch2 = null;
    private GTCXTileFusionMaterialExtractor outputHatch = null;
    private GTCXTileFusionEnergyExtractor energyOutputHatch = null;
    private int guiOverlay = 0;

    public GTCXTileMultiFusionReactor() {
        super(9, 0, 8192, 8192);
        this.maxEnergy = 1000000;
        input1 = this.getPos();
        input2 = this.getPos();
        output = this.getPos();
        energyOutput = this.getPos();
        this.inputTank1.addListener(this);
        this.inputTank2.addListener(this);
        this.outputTank.addListener(this);
        this.addSlots(secondHandler);
        secondHandler.validateSlots();
        this.addGuiFields("guiOverlay", "inputTank1", "inputTank2", "outputTank");
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        int slot = handler == secondHandler ? 1 : 0;
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Import, slot);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutput);
        handler.registerDefaultSlotsForSide(RotationList.UP, slot);
        handler.registerDefaultSlotsForSide(RotationList.DOWN, slotOutput);
        handler.registerSlotType(SlotType.Input, slot);
        handler.registerSlotType(SlotType.Output, slotOutput);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.isSimulating()){
            this.tickOffset = world.rand.nextInt(128);
        }
    }

    public void addMaxEnergy(int add){
        this.maxEnergy += add;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTLang.FUSION_REACTOR;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerFusionReactor(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXFusionComputerGui.class;
    }

    public int getGuiOverlay() {
        return guiOverlay;
    }

    @Override
    public int[] getInputSlots() {
        return new int[] {slotInput1, slotInput2};
    }

    @Override
    public IFilter[] getInputFilters(int[] slots) {
        return new IFilter[0];
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        return slot == slotInput1 || slot == slotInput2;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{ slotOutput };
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return null;
    }

    @Override
    public ResourceLocation getStartSoundFile() {
        return Ic2Sounds.compressorOp;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return RECIPE_LIST;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.getFacing().getAxis() == EnumFacing.Axis.Y){
                return false;
            }
            EnumFacing dir = facing == this.getFacing().rotateY() ? EnumFacing.UP : EnumFacing.DOWN;
            return this.getHandler() != null && facing != EnumFacing.DOWN && facing != EnumFacing.UP && facing != this.getFacing() && facing != this.getFacing().getOpposite() && super.hasCapability(capability, dir);
        }
        return super.hasCapability(capability, facing) || (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return null;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, GTCXTileItemFluidHatches hatch) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            if (hatch.isInput()){
                return hatch.isSecond() ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(inputTank2) : CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(inputTank1);
            } else {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(outputTank);
            }
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            InventoryHandler handler = this.getHandler();
            if (hatch.isSecond()){
                handler = this.secondHandler;
            }
            if (this.getFacing().getAxis() == EnumFacing.Axis.Y){
                return null;
            }
            EnumFacing dir = facing == this.getFacing().rotateY() ? EnumFacing.UP : EnumFacing.DOWN;
            return handler == null || facing == EnumFacing.DOWN || facing == EnumFacing.UP || facing == this.getFacing() || facing == this.getFacing().getOpposite() ? null : CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler.getInventory(dir));
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.shouldCheckRecipe = true;
        this.setStackInSlot(slotDisplayIn1, ItemDisplayIcon.createWithFluidStack(inputTank1.getFluid()));
        this.setStackInSlot(slotDisplayIn2, ItemDisplayIcon.createWithFluidStack(inputTank2.getFluid()));
        this.setStackInSlot(slotDisplayOut, ItemDisplayIcon.createWithFluidStack(outputTank.getFluid()));
        this.getNetwork().updateTileGuiField(this, "inputTank1");
        this.getNetwork().updateTileGuiField(this, "inputTank2");
        this.getNetwork().updateTileGuiField(this, "outputTank");
    }

    @Override
    public void update() {
        GTHelperFluid.doFluidContainerThings(this, this.inputTank1, slotInput1, slotFakeOutput1);
        GTHelperFluid.doFluidContainerThings(this, this.inputTank2, slotInput2, slotFakeOutput2);
        GTHelperFluid.doFluidContainerThings(this, this.outputTank, slotFakeInput, slotOutput);
        TileEntity input1 = world.getTileEntity(this.input1);
        TileEntity input2 = world.getTileEntity(this.input2);
        TileEntity output = world.getTileEntity(this.output);
        TileEntity energyOutput = world.getTileEntity(this.energyOutput);
        boolean hasHatches = this.canWork() && input1 instanceof GTCXTileFusionMaterialInjector && input2 instanceof GTCXTileFusionMaterialInjector && output instanceof GTCXTileFusionMaterialExtractor && energyOutput instanceof GTCXTileFusionEnergyExtractor;
        if (hasHatches){
            handleRedstone();
            updateNeighbors();
            boolean noRoom = addToInventory();
            if (shouldCheckRecipe) {
                lastRecipe = getRecipe();
                shouldCheckRecipe = false;
            }
            boolean operate = (!noRoom && lastRecipe != null && lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE);
            if (operate && canContinue() && energy >= energyConsume) {
                int neededStartEu = getStartEu(lastRecipe.getOutputs());
                if (usedStartEnergy || energy >= neededStartEu + energyConsume || neededStartEu == 0){
                    if (!usedStartEnergy){
                        this.useEnergy(neededStartEu);
                        usedStartEnergy = true;
                    }
                    if (!getActive()) {
                        getNetwork().initiateTileEntityEvent(this, 0, false);
                    }
                    setActive(true);
                    progress += progressPerTick;
                    useEnergy(recipeEnergy);
                    if (progress >= recipeOperation) {
                        process(lastRecipe);
                        progress = 0;
                        notifyNeighbors();
                    }
                    getNetwork().updateTileGuiField(this, "progress");
                }
            } else {
                if (getActive()) {
                    if (progress != 0) {
                        getNetwork().initiateTileEntityEvent(this, 1, false);
                    } else {
                        getNetwork().initiateTileEntityEvent(this, 2, false);
                    }
                }
                if (progress != 0) {
                    progress = 0;
                    usedStartEnergy = false;
                    getNetwork().updateTileGuiField(this, "progress");
                }
                setActive(false);
            }
            if (supportsUpgrades) {
                for (int i = 0; i < upgradeSlots; i++) {
                    ItemStack item = inventory.get(i + inventory.size() - upgradeSlots);
                    if (item.getItem() instanceof IMachineUpgradeItem) {
                        ((IMachineUpgradeItem) item.getItem()).onTick(item, this);
                    }
                }
            }
            updateComparators();
        }
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
                usedStartEnergy = false;
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
        if (outputs instanceof GTFluidMachineOutput){
            GTFluidMachineOutput fluidOutputs = (GTFluidMachineOutput) outputs;
            if (outputTank.getFluidAmount() == 0){
                return lastRecipe;
            }
            FluidStack fluid = outputTank.getFluid();
            for (FluidStack output : fluidOutputs.getFluids()){
                if (output.isFluidEqual(fluid) && outputTank.getFluidAmount() + 1000 <= outputTank.getCapacity()){
                    return lastRecipe;
                }
            }
        } else {
            ItemStack outputStack = this.getStackInSlot(2);
            if (outputStack.isEmpty()){
                return lastRecipe;
            }
            for (ItemStack output : lastRecipe.getOutputs().getAllOutputs()) {
                if (StackUtil.isStackEqual(outputStack, output, false, true)) {
                    if (outputStack.getCount()
                            + output.getCount() <= outputStack.getMaxStackSize()) {
                        return lastRecipe;
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
                if (fluidInput1 != null && fluidInput1.isFluidEqual(fluidStack) && fluidInput1.amount >= 1000){
                    keyIter.remove();
                    continue;
                }
                if (fluidInput2 != null && fluidInput2.isFluidEqual(fluidStack) && fluidInput2.amount >= 1000){
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
                outputs.add(new MultiSlotOutput(stack, slotOutput));
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
                    inputTank1.drainInternal(1000, true);
                    keyIter.remove();
                    continue;
                }
                if (inputTank2.getFluid() != null && inputTank2.getFluid().isFluidEqual(fluidStack)){
                    inputTank2.drainInternal(1000, true);
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
    public void onRecipeComplete() {
        if (this.lastRecipe != null && this.lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE) {
            int startEu = this.lastRecipe.getOutputs().getMetadata().getInteger(START_EU);
            int rTime = lastRecipe.getOutputs().getMetadata().getInteger("RecipeTime") + 100;
            if (startEu != 40000000 && startEu != 60000000) {
                int euOutput = rTime * 12000;
                producedEnergy += euOutput;
                return;
            }
            int euOutput = startEu == 40000000 ? rTime * 60000 : rTime * 62000;
            this.producedEnergy += euOutput;

        }

    }

    @Override
    public boolean canWork() {
        boolean superCall = !this.redstoneSensitive || this.isRedstonePowered();
        if (superCall){
            if (this.world.getTotalWorldTime() % (128 + this.tickOffset) == 0 || this.firstCheck) {
                boolean lastCheck = lastState;
                lastState = this.checkStructure();
                int newMaxEu = (energyInputList.size() * 10000000) + 1000000;
                if (energyInputList.size() > 0 && this.maxEnergy != newMaxEu){
                    this.maxEnergy = newMaxEu;
                    this.getNetwork().updateTileGuiField(this,"maxEnergy");
                } else if (energyInputList.size() == 0 && this.maxEnergy != 1000000){
                    this.maxEnergy = 1000000;
                    this.getNetwork().updateTileGuiField(this,"maxEnergy");
                }
                firstCheck = false;
                if(lastCheck != lastState){
                    if(addedToEnergyNet) {
                        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                    }
                    lastPositions = null;
                    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                    addedToEnergyNet = true;
                }
                this.getNetwork().updateTileGuiField(this, "lastState");
            }
            superCall = lastState;
        }
        return superCall;
    }

    public int getTickOffset() {
        return tickOffset;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.input1 = readBlockPosFromNBT(nbt, "input1");
        this.input2 = readBlockPosFromNBT(nbt, "input2");
        this.output = readBlockPosFromNBT(nbt, "output");
        this.energyOutput = readBlockPosFromNBT(nbt, "energyOutput");
        this.usedStartEnergy = nbt.getBoolean("usedStartEnergy");
        this.producedEnergy = nbt.getInteger("producedEnergy");
        this.guiOverlay = nbt.getInteger("guiOverlay");
        this.inputTank1.readFromNBT(nbt.getCompoundTag("inputTank1"));
        this.inputTank2.readFromNBT(nbt.getCompoundTag("inputTank2"));
        this.outputTank.readFromNBT(nbt.getCompoundTag("outputTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        writeBlockPosToNBT(nbt, "input1", input1);
        writeBlockPosToNBT(nbt, "input2", input2);
        writeBlockPosToNBT(nbt, "output", output);
        writeBlockPosToNBT(nbt, "energyOutput", energyOutput);
        nbt.setBoolean("usedStartEnergy", usedStartEnergy);
        nbt.setInteger("producedEnergy", producedEnergy);
        nbt.setInteger("guiOverlay", guiOverlay);
        this.inputTank1.writeToNBT(this.getTag(nbt, "inputTank1"));
        this.inputTank2.writeToNBT(this.getTag(nbt, "inputTank2"));
        this.outputTank.writeToNBT(this.getTag(nbt, "outputTank"));
        return nbt;
    }

    public BlockPos readBlockPosFromNBT(NBTTagCompound nbt, String id){
        NBTTagCompound compound = nbt.getCompoundTag(id);
        int x = compound.getInteger("X");
        int y = compound.getInteger("Y");
        int z = compound.getInteger("Z");
        return new BlockPos(x, y, z);
    }

    public void writeBlockPosToNBT(NBTTagCompound nbt, String id, BlockPos pos){
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("X", pos.getX());
        compound.setInteger("Y", pos.getY());
        compound.setInteger("Z", pos.getZ());
        nbt.setTag(id, compound);
    }

    public static void postInit() {
        RECIPE_LIST.startMassChange();
        for (MultiRecipe recipe : RECIPE_LIST.getRecipeList()){
            GTRecipeMachineHandler.removeRecipe(RECIPE_LIST, recipe.getRecipeID());
        }
        RECIPE_LIST.finishMassChange();
        /* Just regular recipes added manually **/
        addRecipe(input(GTMaterialGen.getFluidStack(GTMaterial.Deuterium)),
                input(GTMaterialGen.getFluidStack(GTMaterial.Tritium)), totalEu(2097152), 40000000, GTMaterialGen.getFluidStack(GTMaterial.Helium));
        addRecipe(input(GTMaterialGen.getFluidStack(GTMaterial.Deuterium)),
                input(GTMaterialGen.getFluidStack(GTMaterial.Helium3)), totalEu(2097152), 60000000, GTMaterialGen.getFluidStack(GTMaterial.Helium));
        /* This iterates the element objects to create all Fusion recipes **/
        Set<Integer> usedInputs = new HashSet<>();
        for (GTMaterialElement sum : GTMaterialElement.getElementList()) {
            for (GTMaterialElement input1 : GTMaterialElement.getElementList()) {
                for (GTMaterialElement input2 : GTMaterialElement.getElementList()) {
                    int hash = input1.hashCode() + input2.hashCode();
                    if ((input1.getNumber() + input2.getNumber() == sum.getNumber()) && input1 != input2
                            && !usedInputs.contains(hash)) {
                        float ratio = (sum.getNumber() / 100.0F) * 7000000.0F;
                        int startEu = sum.getNumber() == GTMaterial.Iridium.getElementNumber() ? 120000000 : 0;
                        IRecipeInput recipeInput1 = input1.getInput();
                        IRecipeInput recipeInput2 = input2.getInput();
                        if (sum.isFluid()){
                            addRecipe(recipeInput1,
                                    recipeInput2, totalEu(Math.round(ratio)), startEu, ((RecipeInputFluid)sum.getInput()).fluid);
                        } else {
                            addRecipe(recipeInput1,
                                    recipeInput2, totalEu(Math.round(ratio)), startEu, sum.getOutput());
                        }
                        usedInputs.add(hash);

                    }
                }
            }
        }
        addRecipe(input(GTMaterialGen.getIc2(Ic2Items.uuMatter, 10)),
                input(GTMaterialGen.getIc2(Ic2Items.emptyCell, 1)), totalEu(10000000),100000000, GTMaterialGen.getIc2(Ic2Items.plasmaCell, 1));
    }

    public static int getStartEu(MachineOutput output) {
        if (output == null || output.getMetadata() == null || !output.getMetadata().hasKey(START_EU)) {
            return 0;
        }
        return output.getMetadata().getInteger(START_EU);
    }

    public static IRecipeModifier[] totalEu(int total) {
        return GTRecipeMachineHandler.totalEu(RECIPE_LIST, total);
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, IRecipeModifier[] modifiers, int startEu, ItemStack output){
        addRecipe(input1, input2, modifiers, startEu, output, output.getUnlocalizedName());
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, IRecipeModifier[] modifiers, int startEu, ItemStack output, String recipeId){
        List<IRecipeInput> inlist = new ArrayList<>();
        if (startEu > 161000000){
            GTCExpansion.logger.info("Recipe: " +  output.getUnlocalizedName() + " has too high of a start eu amount");
            return;
        }
        inlist.add(input1);
        inlist.add(input2);
        NBTTagCompound mods = new NBTTagCompound();
        if (modifiers != null) {
            for (IRecipeModifier modifier : modifiers) {
                modifier.apply(mods);
            }
        }
        mods.setInteger(START_EU, startEu);
        RECIPE_LIST.addRecipe(inlist, new MachineOutput(mods, output), recipeId, 8192);
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, IRecipeModifier[] modifiers, int startEu, FluidStack output){
        addRecipe(input1, input2, modifiers, startEu, output, output.getUnlocalizedName());
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, IRecipeModifier[] modifiers, int startEu, FluidStack output, String recipeId){
        List<IRecipeInput> inlist = new ArrayList<>();
        List<FluidStack> outList = new ArrayList<>();
        if (startEu > 161000000){
            GTCExpansion.logger.info("Recipe: " +  output.getUnlocalizedName() + " has too high of a start eu amount");
            return;
        }
        inlist.add(input1);
        inlist.add(input2);
        outList.add(output);
        NBTTagCompound mods = new NBTTagCompound();
        if (modifiers != null) {
            for (IRecipeModifier modifier : modifiers) {
                modifier.apply(mods);
            }
        }
        mods.setInteger(START_EU, startEu);
        RECIPE_LIST.addRecipe(inlist, new GTFluidMachineOutput(mods, outList), output.getUnlocalizedName(), 8192);
    }

    public void removeTilesWithOwners(){
        if (inputHatch1 != null && inputHatch1.getOwner() == this){
            inputHatch1.setOwner(null);
        }
        if (inputHatch2 != null && inputHatch2.getOwner() == this){
            inputHatch2.setOwner(null);
        }
        if (outputHatch != null && outputHatch.getOwner() == this){
            outputHatch.setOwner(null);
        }
        if (energyOutputHatch != null && energyOutputHatch.getOwner() == this){
            energyOutputHatch.setOwner(null);
        }
        for (BlockPos blockPos : energyInputList){
            TileEntity tile = world.getTileEntity(blockPos);
            if (tile instanceof GTCXTileFusionEnergyInjector && ((GTCXTileFusionEnergyInjector)tile).getAccept() == this){
                ((GTCXTileFusionEnergyInjector)tile).setAccept(null);
            }
        }
    }

    int numInputs = 0;
    int numOutputs = 0;
    int numEnergyOutputs = 0;

    @Override
    public boolean checkStructure() {
        energyInputList.clear();
        numInputs = 0;
        numOutputs = 0;
        numEnergyOutputs = 0;
        input1 = this.getPos();
        input2 = this.getPos();
        output = this.getPos();
        energyOutput = this.getPos();
        int3 dir = new int3(this.getPos(), this.getFacing());
        //top section
        if (!isCasing(dir.up(1).back(1))){
            return false;
        }
        if (!isInputHatch(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isInputHatch(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isInputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isInputHatch(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isInputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isInputHatch(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isInputHatch(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isInputHatch(dir.back(1).right(1))){
            return false;
        }

        // middle section
        if (!isOutputHatch(dir.forward(1).down(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isOutputHatch(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }

        if (!isEnergyInputHatch(dir.left(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.left(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.right(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.forward(1).right(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.forward(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.back(1).right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1).right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isOutputHatch(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1).back(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isOutputHatch(dir.right(1).back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isOutputHatch(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.left(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isOutputHatch(dir.back(1).left(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }

        // bottom section
        if (!isCasing(dir.down(1).forward(1))){
            return false;
        }
        if (!isInputHatch(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isInputHatch(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isInputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isInputHatch(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isInputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isInputHatch(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isInputHatch(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isInputHatch(dir.back(1).right(1))){
            return false;
        }
        return numInputs >= 2 && numOutputs >= 1 && energyInputList.size() >= 4 && numEnergyOutputs >= 1;
    }

    public boolean isCasing(int3 pos){
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    public boolean isCoil(int3 pos) {
        return this.world.getBlockState(pos.asBlockPos()) == COIL_STATE;
    }

    public boolean isInputHatch(int3 pos) {
        if (world.getBlockState(pos.asBlockPos()) == MATERIAL_INJECTOR_STATE){
            if (world.getBlockState(input1) != MATERIAL_INJECTOR_STATE){
                input1 = pos.asBlockPos();
                TileEntity tile = world.getTileEntity(pos.asBlockPos());
                if (inputHatch1 != tile && tile instanceof GTCXTileFusionMaterialInjector) {
                    if (inputHatch1 != null){
                        inputHatch1.setOwner(null);
                    }
                    inputHatch1 = (GTCXTileFusionMaterialInjector) tile;
                    inputHatch1.setOwner(this);
                }
            } else if (world.getBlockState(input2) != MATERIAL_INJECTOR_STATE){
                input2 = pos.asBlockPos();
                TileEntity tile = world.getTileEntity(pos.asBlockPos());
                if (inputHatch2 != tile && tile instanceof GTCXTileFusionMaterialInjector) {
                    if (inputHatch2 != null){
                        inputHatch2.setOwner(null);
                    }
                    inputHatch2 = (GTCXTileFusionMaterialInjector) tile;
                    inputHatch2.setOwner(this);
                    inputHatch2.setSecond(true);
                }
            }
            numInputs++;
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    public boolean isOutputHatch(int3 pos){
        if (world.getBlockState(pos.asBlockPos()) == MATERIAL_EXTRACTOR_STATE){
            if (world.getBlockState(output) != MATERIAL_EXTRACTOR_STATE){
                output = pos.asBlockPos();
                TileEntity tile = world.getTileEntity(pos.asBlockPos());
                if (outputHatch != tile && tile instanceof GTCXTileFusionMaterialExtractor) {
                    if (outputHatch != null){
                        outputHatch.setOwner(null);
                    }
                    outputHatch = (GTCXTileFusionMaterialExtractor) tile;
                    outputHatch.setOwner(this);
                }
            }
            numOutputs++;
            return true;
        }
        if (world.getBlockState(pos.asBlockPos()) == ENERGY_EXTRACTOR_STATE){
            if (world.getBlockState(energyOutput) != ENERGY_EXTRACTOR_STATE){
                energyOutput = pos.asBlockPos();
                TileEntity tile = world.getTileEntity(pos.asBlockPos());
                if (energyOutputHatch != tile && tile instanceof GTCXTileFusionEnergyExtractor) {
                    if (energyOutputHatch != null){
                        energyOutputHatch.setOwner(null);
                    }
                    energyOutputHatch = (GTCXTileFusionEnergyExtractor) tile;
                    energyOutputHatch.setOwner(this);
                }
            }
            numEnergyOutputs++;
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    public boolean isEnergyInputHatch(int3 pos){
        TileEntity tile = world.getTileEntity(pos.asBlockPos());
        if (world.getBlockState(pos.asBlockPos()) == ENERGY_INJECTOR_STATE && tile instanceof GTCXTileFusionEnergyInjector){
            if (((GTCXTileFusionEnergyInjector)tile).getAccept() == null){
                ((GTCXTileFusionEnergyInjector)tile).setAccept(this);
            }
            energyInputList.add(pos.asBlockPos());
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    @Override
    public void setShouldCheckRecipe(boolean checkRecipe) {
        this.shouldCheckRecipe = checkRecipe;
    }

    @Override
    public void setDisabled(boolean disabled) {
        
    }

    @Override
    public int getOutputSlot(GTCXTileItemFluidHatches hatch) {
        return hatch.isInput() ? (hatch.isSecond() ? slotFakeOutput2 : slotFakeOutput1) : slotOutput;
    }

    @Override
    public int getInputSlot(GTCXTileItemFluidHatches hatch) {
        return hatch.isInput() ? (hatch.isSecond() ? slotInput2 : slotInput1) : slotFakeInput;
    }

    @Override
    public GTCXTank getTank(GTCXTileItemFluidHatches hatch) {
        return hatch.isInput() ? hatch.isSecond() ? this.inputTank2 : this.inputTank1 : this.outputTank;
    }

    public GTCXTank getInputTank1() {
        return this.inputTank1;
    }

    public GTCXTank getInputTank2() {
        return this.inputTank2;
    }

    public GTCXTank getOutputTank() {
        return this.outputTank;
    }

    @Override
    public void invalidateStructure() {
        if(addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnergyNet = false;
        }
        this.firstCheck = true;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer, GTCXTileItemFluidHatches hatch) {
        return new GTCXContainerFusionReactorHatch(entityPlayer.inventory, this, hatch.isSecond(), hatch.isInput());
    }

    @Override
    public int getStoredEnergy() {
        return producedEnergy;
    }

    @Override
    public int getMaxEnergy() {
        return maxProducedEnergy;
    }

    @Override
    public int getMaxSendingEnergy() {
        return 0;
    }

    @Override
    public double getOfferedEnergy() {
        return 0;
    }

    @Override
    public void drawEnergy(double v) {
        producedEnergy -= v;
    }

    @Override
    public int getSourceTier() {
        return 0;
    }

    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor iEnergyAcceptor, EnumFacing enumFacing) {
        return false;
    }

    @Override
    public List<IEnergyTile> getSubTiles() {
        if (lastPositions == null){
            lastPositions = new ArrayList<>();
            lastPositions.add(this);
            /*if (energyOutputHatch != null){
                lastPositions.add(energyOutputHatch);
            }*/
            for (BlockPos pos : energyInputList){
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof GTCXTileFusionEnergyInjector){
                    lastPositions.add(((GTCXTileFusionEnergyInjector)tile));
                }
            }
        }
        return lastPositions;
    }

    @Override
    public void onNetworkEvent(EntityPlayer entityPlayer, int i) {
        if (i < 3 && i >= 0 && this.guiOverlay != i){
            this.guiOverlay = i;
            this.getNetwork().updateTileGuiField(this, "guiOverlay");
        }
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put("Produced Energy: " + this.producedEnergy, true);
    }
}
