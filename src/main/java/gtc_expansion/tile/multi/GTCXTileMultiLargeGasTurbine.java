package gtc_expansion.tile.multi;

import gtc_expansion.container.GTCXContainerLargeGasTurbine;
import gtc_expansion.container.GTCXContainerLargeGasTurbineHatch;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.interfaces.IGTEnergySource;
import gtc_expansion.interfaces.IGTMultiTileProduction;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtc_expansion.item.GTCXItemTurbineRotor;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileCasing;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch.GTCXTileDynamoHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileInputHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch;
import gtc_expansion.tile.hatch.GTCXTileMachineControlHatch;
import gtc_expansion.util.GTCXTank;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTMultiTileStatus;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.util.obj.ITankListener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static gtc_expansion.tile.multi.GTCXTileMultiLargeSteamTurbine.outputHatchState;

public class GTCXTileMultiLargeGasTurbine extends TileEntityMachine implements ITickable, IHasGui, ITankListener, IGTMultiTileStatus, IGTMultiTileProduction, IGTOwnerTile, INetworkClientTileEntityEventListener, INetworkTileEntityEventListener, IGTEnergySource {
    public boolean lastState;
    public boolean firstCheck = true;
    public boolean addedToEnergyNet = false;
    List<IEnergyTile> lastPositions = null;
    private BlockPos input1;
    private BlockPos input2;
    private BlockPos output;
    private BlockPos dynamo;
    private GTCXTileInputHatch inputHatch1 = null;
    private GTCXTileInputHatch inputHatch2 = null;
    private GTCXTileOutputHatch outputHatch = null;
    private GTCXTileDynamoHatch dynamoHatch = null;
    private GTCXTileMachineControlHatch controlHatch = null;
    private boolean disabled = false;
    int production;
    protected MultiRecipe lastRecipe;
    int ticker = 0;
    @NetworkField(index = 3)
    private GTCXTank inputTank1 = new GTCXTank(32000);
    @NetworkField(index = 4)
    private GTCXTank inputTank2 = new GTCXTank(32000);
    @NetworkField(index = 5)
    private GTCXTank outputTank = new GTCXTank(32000);
    private static int slotDisplayIn1 = 1;
    private static int slotDisplayIn2 = 2;
    private static int slotDisplayOut = 3;
    public static int slotIn1 = 4;
    public static int slotOut1 = 5;
    public static int slotIn2 = 6;
    public static int slotOut2 = 7;
    public static int slotIn3 = 8;
    public static int slotOut3 = 9;
    public int maxEnergy = 100000;
    @NetworkField(
            index = 6
    )
    public int energy;
    private int tickOffset = 0;
    GTCXTileOutputHatch.OutputModes outputMode = GTCXTileOutputHatch.OutputModes.ITEM_AND_FLUID;
    boolean hasOutput = false;
    boolean hasSecondInput = false;
    final FluidStack co2 = GTMaterialGen.getFluidStack(GTCXMaterial.CarbonDioxide, 10);
    protected boolean shouldCheckRecipe;
    public static final String RECIPE_TICKS = "recipeTicks";
    public static final String RECIPE_EU = "recipeEu";
    public static final IBlockState reinforcedCasingState = GTCXBlocks.casingReinforced.getDefaultState();
    public static final IBlockState inputHatchState = GTCXBlocks.inputHatch.getDefaultState();
    public static final IBlockState dynamoHatchState = GTCXBlocks.dynamoHatch.getDefaultState();
    public static final IBlockState machineControlHatchState = GTCXBlocks.machineControlHatch.getDefaultState();

    public GTCXTileMultiLargeGasTurbine() {
        super(10);
        this.addGuiFields("lastState", "production", "inputTank1", "inputTank2", "outputTank");
        this.addNetworkFields("energy", "inputTank1", "inputTank2", "outputTank");
        this.inputTank1.addListener(this);
        this.inputTank2.addListener(this);
        this.outputTank.addListener(this);
        shouldCheckRecipe = true;
        input1 = this.getPos();
        input2 = this.getPos();
        output = this.getPos();
        dynamo = this.getPos();
        production = 0;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.isSimulating()) {
            this.tickOffset = world.rand.nextInt(128);
        }
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.lastState = nbt.getBoolean("lastState");
        this.disabled = nbt.getBoolean("disabled");
        this.energy = nbt.getInteger("energy");
        this.ticker = nbt.getInteger("ticker");
        this.input1 = readBlockPosFromNBT(nbt, "input1");
        this.input2 = readBlockPosFromNBT(nbt, "input2");
        this.dynamo = readBlockPosFromNBT(nbt, "dynamo");
        this.production = nbt.getInteger("production");
        this.inputTank1.readFromNBT(nbt.getCompoundTag("inputTank1"));
        this.inputTank2.readFromNBT(nbt.getCompoundTag("inputTank2"));
        this.outputTank.readFromNBT(nbt.getCompoundTag("outputTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("lastState", this.lastState);
        nbt.setBoolean("disabled", this.disabled);
        nbt.setInteger("energy", this.energy);
        nbt.setInteger("ticker", ticker);
        writeBlockPosToNBT(nbt, "input1", input1);
        writeBlockPosToNBT(nbt, "input2", input2);
        writeBlockPosToNBT(nbt, "dynamo", dynamo);
        nbt.setInteger("production", production);
        this.inputTank1.writeToNBT(this.getTag(nbt, "inputTank1"));
        this.inputTank2.writeToNBT(this.getTag(nbt, "inputTank2"));
        this.outputTank.writeToNBT(this.getTag(nbt, "outputTank"));
        return nbt;
    }

    public void onBlockPlaced(){
        getNetwork().initiateClientTileEntityEvent(this, 1);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        shouldCheckRecipe = true;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        super.setStackInSlot(slot, stack);
        shouldCheckRecipe = true;
    }

    @Override
    public int getOutputSlot(GTCXTileItemFluidHatches hatch) {
        int index = hatch.isInput() ? hatch.isSecond() ? 1 : 0 : 2;
        return 5 + (2 * index);
    }

    @Override
    public int getInputSlot(GTCXTileItemFluidHatches hatch) {
        int index = hatch.isInput() ? hatch.isSecond() ? 1 : 0 : 2;
        return 4 + (2 * index);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return super.hasCapability(capability, facing) || (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null);
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
        return super.getCapability(capability, facing);
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
    public void invalidateStructure() {
        this.firstCheck = true;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer, GTCXTileItemFluidHatches hatch) {
        return new GTCXContainerLargeGasTurbineHatch(entityPlayer.inventory, this, hatch.isSecond(), hatch.isInput());
    }

    public void writeBlockPosToNBT(NBTTagCompound nbt, String id, BlockPos pos){
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("X", pos.getX());
        compound.setInteger("Y", pos.getY());
        compound.setInteger("Z", pos.getZ());
        nbt.setTag(id, compound);
    }

    public BlockPos readBlockPosFromNBT(NBTTagCompound nbt, String id){
        NBTTagCompound compound = nbt.getCompoundTag(id);
        int x = compound.getInteger("X");
        int y = compound.getInteger("Y");
        int z = compound.getInteger("Z");
        return new BlockPos(x, y, z);
    }

    public boolean canWork() {
        if (this.world.getTotalWorldTime() % (128 + this.tickOffset) == 0 || this.firstCheck) {
            this.lastState = this.checkStructure();
            boolean lastCheck = lastState;
            lastState = checkStructure();
            firstCheck = false;
            /*if(lastCheck != lastState){
                if(addedToEnergyNet) {
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                }
                lastPositions = null;
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                addedToEnergyNet = true;
            }*/
            this.getNetwork().updateTileGuiField(this, "lastState");
        }
        return this.lastState;
    }

    public boolean isTurbineRotor(ItemStack stack){
        return stack.getItem() == GTCXItems.bronzeTurbineRotor || stack.getItem() == GTCXItems.steelTurbineRotor || stack.getItem() == GTCXItems.magnaliumTurbineRotor || stack.getItem() == GTCXItems.tungstensteelTurbineRotor || stack.getItem() == GTCXItems.carbonTurbineRotor || stack.getItem() == GTCXItems.osmiumTurbineRotor || stack.getItem() == GTCXItems.osmiridiumTurbineRotor;
    }


    @Override
    public void update() {
        if (ticker < 80){
            ticker++;
        }
        GTHelperFluid.doFluidContainerThings(this, this.inputTank1, slotIn1, slotOut1);
        GTHelperFluid.doFluidContainerThings(this, this.inputTank2, slotIn2, slotOut2);
        GTHelperFluid.doFluidContainerThings(this, this.outputTank, slotIn3, slotOut3);
        TileEntity tile = world.getTileEntity(input1);
        TileEntity dTile2 = world.getTileEntity(dynamo);
        boolean canWork = canWork() && tile instanceof GTCXTileInputHatch && dTile2 instanceof GTCXTileDynamoHatch;
        if (canWork && isTurbineRotor(this.getStackInSlot(0))){
            if (this.shouldCheckRecipe) {
                this.lastRecipe = this.getRecipe();
                this.shouldCheckRecipe = false;
            }
            if (this.outputHatch != null){
                this.outputMode = outputHatch.getCycle();
            }
            boolean operate = !disabled && this.lastRecipe != null && this.lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE;
            if (operate){
                double baseGeneration = getBaseGeneration();
                production = (int)(baseGeneration * getRotorEfficiency(this.getStackInSlot(0)));
                int fluidAmount = 1000 / getBaseDivider();
                if (energy + production <= maxEnergy){
                    if (inputTank1.getFluidAmount() >= fluidAmount){
                        if (!this.getActive()){
                            this.setActive(true);
                            this.setRingActive(true);
                        }
                        inputTank1.drainInternal(fluidAmount, true);
                        if (hasOutput && outputMode.isFluid()){
                            //noinspection ConstantConditions
                            if (outputTank.getFluidAmount() == 0 || (outputTank.getFluid().isFluidEqual(co2) && outputTank.getFluidAmount() + 10 <= outputTank.getCapacity())){
                                outputTank.fillInternal(co2, true);
                            }
                        }
                        energy += production;
                        if (ticker >= 80){
                            ItemStack slotStack = this.getStackInSlot(0);
                            if (slotStack.attemptDamageItem(1, world.rand, null)) {
                                if (slotStack.getItem() instanceof GTCXItemTurbineRotor){
                                    this.setStackInSlot(0, ((GTCXItemTurbineRotor)slotStack.getItem()).getBroken());
                                } else {
                                    slotStack.shrink(1);
                                }
                                if (world.isAnyPlayerWithinRangeAt(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 5)){
                                    for (EntityPlayer player : world.playerEntities){
                                        if (EntitySelectors.NOT_SPECTATING.apply(player) && !player.isCreative()) {
                                            double d0 = player.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
                                            if (d0 < 5 * 5) {
                                                player.attackEntityFrom(DamageSource.GENERIC, 8);
                                            }
                                        }
                                    }
                                }
                            }
                            ticker = 0;
                        }
                    } else if (hasSecondInput){
                        if (inputTank1.getFluidAmount() > 0){
                            int amount = inputHatch1.getTank().getFluidAmount();
                            int remaining = fluidAmount - amount;
                            if (inputTank2.getFluidAmount() >= remaining){
                                if (!this.getActive()){
                                    this.setActive(true);
                                    this.setRingActive(true);
                                }
                                inputTank1.drainInternal(amount, true);
                                inputTank2.drainInternal(remaining, true);
                                if (hasOutput && outputMode.isFluid()){
                                    //noinspection ConstantConditions
                                    if (outputTank.getFluidAmount() == 0 || (outputTank.getFluid().isFluidEqual(co2) && outputTank.getFluidAmount() + 10 <= outputTank.getCapacity())){
                                        outputTank.fillInternal(co2, true);
                                    }
                                }
                                energy += production;
                                if (ticker >= 80){
                                    ItemStack slotStack = this.getStackInSlot(0);
                                    if (slotStack.attemptDamageItem(1, world.rand, null)) {
                                        if (slotStack.getItem() instanceof GTCXItemTurbineRotor){
                                            this.setStackInSlot(0, ((GTCXItemTurbineRotor)slotStack.getItem()).getBroken());
                                        } else {
                                            slotStack.shrink(1);
                                        }
                                        if (world.isAnyPlayerWithinRangeAt(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 5)){
                                            for (EntityPlayer player : world.playerEntities){
                                                if (EntitySelectors.NOT_SPECTATING.apply(player) && !player.isCreative()) {
                                                    double d0 = player.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
                                                    if (d0 < 5 * 5) {
                                                        player.attackEntityFrom(DamageSource.GENERIC, 8);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    ticker = 0;
                                }
                            }
                        } else if (inputTank2.getFluidAmount() >= fluidAmount){
                            if (!this.getActive()){
                                this.setActive(true);
                                this.setRingActive(true);
                            }
                            inputTank2.drainInternal(fluidAmount, true);
                            if (hasOutput && outputMode.isFluid()){
                                //noinspection ConstantConditions
                                if (outputTank.getFluidAmount() == 0 || (outputTank.getFluid().isFluidEqual(co2) && outputTank.getFluidAmount() + 10 <= outputTank.getCapacity())){
                                    outputTank.fillInternal(co2, true);
                                }
                            }
                            energy += production;
                            if (ticker >= 80){
                                ItemStack slotStack = this.getStackInSlot(0);
                                if (slotStack.attemptDamageItem(1, world.rand, null)) {
                                    if (slotStack.getItem() instanceof GTCXItemTurbineRotor){
                                        this.setStackInSlot(0, ((GTCXItemTurbineRotor)slotStack.getItem()).getBroken());
                                    } else {
                                        slotStack.shrink(1);
                                    }
                                    if (world.isAnyPlayerWithinRangeAt(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 5)){
                                        for (EntityPlayer player : world.playerEntities){
                                            if (EntitySelectors.NOT_SPECTATING.apply(player) && !player.isCreative()) {
                                                double d0 = player.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
                                                if (d0 < 5 * 5) {
                                                    player.attackEntityFrom(DamageSource.GENERIC, 8);
                                                }
                                            }
                                        }
                                    }
                                }
                                ticker = 0;
                            }
                        } else {
                            if (this.getActive()){
                                this.setActive(false);
                                this.setRingActive(false);
                            }
                        }
                    } else {
                        if (this.getActive()){
                            this.setActive(false);
                            this.setRingActive(false);
                        }
                    }
                }else {
                    if (this.getActive()){
                        this.setActive(false);
                        this.setRingActive(false);
                    }
                }
            } else {
                if (production != 0){
                    production = 0;
                }
                if (this.getActive()){
                    this.setActive(false);
                    this.setRingActive(false);
                }
            }
        } else {
            if (production != 0){
                production = 0;
            }
            if (this.getActive()){
                this.setActive(false);
                this.setRingActive(false);
            }
        }
    }

    public MultiRecipe getRecipe(){
        if (lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            return null;
        }
        // Check if previous recipe is valid
        FluidStack input1 = inputTank1.getFluid();
        FluidStack input2 = hasSecondInput ? inputTank2.getFluid() : null;
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, input1, input2) ? lastRecipe : null;
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<MultiRecipe>() {

                @Override
                public boolean test(MultiRecipe t) {
                    return checkRecipe(t, input1, input2);
                }
            });
        }
        // If no recipe is found, return
        if (lastRecipe == null) {
            return null;
        }
        return lastRecipe;
    }

    public boolean checkRecipe(MultiRecipe entry, FluidStack input, FluidStack input2) {
        IRecipeInput recipeInput = entry.getInput(0);

        if (recipeInput instanceof RecipeInputFluid){
            RecipeInputFluid recipeInputFluid = (RecipeInputFluid) recipeInput;
            if (input2 == null){
                return input != null && input.isFluidEqual((recipeInputFluid).fluid);
            } else {
               int recipeAmount = 1000 / getBaseDivider(entry);
               if (input.amount >= recipeAmount){
                   return input.isFluidEqual(recipeInputFluid.fluid);
               } else {
                   if (input.amount > 0){
                       return input.isFluidEqual(recipeInputFluid.fluid) && input2.isFluidEqual(recipeInputFluid.fluid);
                   } else if (input2.amount >= recipeInputFluid.fluid.amount){
                       return input2.isFluidEqual(recipeInputFluid.fluid);
                   }
               }
            }
        }
        return false;
    }

    public GTRecipeMultiInputList getRecipeList(){
        return GTCXRecipeLists.GAS_TURBINE_RECIPE_LIST;
    }

    public int getBaseDivider(){
        return getTotalRecipeEu(lastRecipe) > 40960 ? 25 : 20;
    }

    public int getBaseDivider(MultiRecipe recipe){
        return getTotalRecipeEu(recipe) > 40960 ? 25 : 20;
    }

    public int getTotalRecipeEu(MultiRecipe recipe){
        return getRecipeEu(recipe.getOutputs()) * getRecipeTicks(recipe.getOutputs());
    }

    public double getBaseGeneration(){
        return (double) getTotalRecipeEu(lastRecipe) / getBaseDivider();
    }

    public void setRingActive(boolean active){
        int3 dir = new int3(getPos(), getFacing());
        setCasingActive(dir.up(1), active);
        setCasingActive(dir.right(1), active);
        setCasingActive(dir.down(1), active);
        setCasingActive(dir.down(1), active);
        setCasingActive(dir.left(1), active);
        setCasingActive(dir.left(1), active);
        setCasingActive(dir.up(1), active);
        setCasingActive(dir.up(1), active);
    }

    public void setCasingActive(int3 dir, boolean active){
        TileEntity tile = world.getTileEntity(dir.asBlockPos());
        if (tile instanceof GTCXTileCasing){
            GTCXTileCasing casing = (GTCXTileCasing) tile;
            if (casing.getActive() != active){
                casing.setActive(active);
            }
        }
    }

    public float getRotorEfficiency(ItemStack stack){
        if (stack.getItem() == GTCXItems.bronzeTurbineRotor){
            return 0.6F;
        }
        if (stack.getItem() == GTCXItems.steelTurbineRotor){
            return 0.8F;
        }
        if (stack.getItem() == GTCXItems.magnaliumTurbineRotor){
            return 1.0F;
        }
        if (stack.getItem() == GTCXItems.tungstensteelTurbineRotor){
            return 0.9F;
        }
        if (stack.getItem() == GTCXItems.carbonTurbineRotor){
            return 1.25F;
        }
        if (stack.getItem() == GTCXItems.osmiumTurbineRotor){
            return 1.75F;
        }
        if (stack.getItem() == GTCXItems.osmiridiumTurbineRotor){
            return 1.5F;
        }
        return 0.0F;
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerLargeGasTurbine(entityPlayer.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GuiComponentContainer.class;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return !this.isInvalid();
    }

    @Override
    public boolean hasGui(EntityPlayer entityPlayer) {
        return true;
    }

    public static int getRecipeTicks(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            return 0;
        }
        return output.getMetadata().getInteger(RECIPE_TICKS);
    }

    public static int getRecipeEu(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            return 0;
        }
        return output.getMetadata().getInteger(RECIPE_EU);
    }

    @Override
    public boolean getStructureValid() {
        return lastState;
    }

    int inputs = 0;
    int outputs = 0;
    public boolean checkStructure() {
        if (!this.world.isAreaLoaded(this.pos, 3)){
            return false;
        }
        inputs = 0;
        outputs = 0;
        this.input1 = this.getPos();
        this.input2 = this.getPos();
        this.output = this.getPos();
        this.dynamo = this.getPos();
        int3 dir = new int3(getPos(), getFacing());
        if (!isReinforcedCasingWithSpecial(dir.up(1), 2)){
            return false;
        }
        if (!isReinforcedCasingWithSpecial(dir.right(1), 3)){
            return false;
        }
        if (!isReinforcedCasingWithSpecial(dir.down(1), 5)){
            return false;
        }
        if (!isReinforcedCasingWithSpecial(dir.down(1), 8)){
            return false;
        }
        if (!isReinforcedCasingWithSpecial(dir.left(1), 7)){
            return false;
        }
        if (!isReinforcedCasingWithSpecial(dir.left(1), 6)){
            return false;
        }
        if (!isReinforcedCasingWithSpecial(dir.up(1), 4)){
            return false;
        }
        if (!isReinforcedCasingWithSpecial(dir.up(1), 1)){
            return false;
        }

        int i;
        for (i = 0; i < 3; i++){
            if (!isReinforcedCasing(dir.back(1))){
                return false;
            }
        }
        if (!isReinforcedCasing(dir.right(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isInputOutputHatch(dir.forward(1))){
                return false;
            }
        }
        if (!isReinforcedCasing(dir.right(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isReinforcedCasing(dir.back(1))){
                return false;
            }
        }
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isInputOutputHatch(dir.forward(1))){
                return false;
            }
        }
        if (!world.isAirBlock(dir.left(1).asBlockPos())){
            return false;
        }
        if (!world.isAirBlock(dir.back(1).asBlockPos())){
            return false;
        }
        if (!isDynamoHatch(dir.back(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.left(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isInputOutputHatch(dir.forward(1))){
                return false;
            }
        }
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isReinforcedCasing(dir.back(1))){
                return false;
            }
        }
        if (!isReinforcedCasing(dir.right(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isInputOutputHatch(dir.forward(1))){
                return false;
            }
        }
        if (!isReinforcedCasing(dir.right(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isReinforcedCasing(dir.back(1))){
                return false;
            }
        }
        hasSecondInput = inputs > 1;
        hasOutput = outputs > 0;
        return inputs >= 1;
    }

    public void removeRing(int3 dir){
        TileEntity tile = world.getTileEntity(input1);
        if (tile instanceof GTCXTileInputHatch && ((GTCXTileInputHatch)tile).getOwner() == this){
            ((GTCXTileInputHatch)tile).setOwner(null);
        }
        tile = world.getTileEntity(input2);
        if (tile instanceof GTCXTileInputHatch && ((GTCXTileInputHatch)tile).getOwner() == this){
            ((GTCXTileInputHatch)tile).setOwner(null);
        }
    }

    public void addRing(){
        int3 dir = new int3(this.pos, this.getFacing());
        setReinforcedCasingWithSpecial(dir.up(1), 2);
        setReinforcedCasingWithSpecial(dir.right(1), 3);
        setReinforcedCasingWithSpecial(dir.down(1), 5);
        setReinforcedCasingWithSpecial(dir.down(1), 8);
        setReinforcedCasingWithSpecial(dir.left(1), 7);
        setReinforcedCasingWithSpecial(dir.left(1), 6);
        setReinforcedCasingWithSpecial(dir.up(1), 4);
        setReinforcedCasingWithSpecial(dir.up(1), 1);
    }

    public boolean isReinforcedCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == reinforcedCasingState;
    }

    public boolean isReinforcedCasingWithSpecial(int3 pos, int position) {
        IBlockState state = world.getBlockState(pos.asBlockPos());
        if (state == reinforcedCasingState){
            TileEntity tile = world.getTileEntity(pos.asBlockPos());
            if (tile instanceof GTCXTileCasing){
                GTCXTileCasing  casing = (GTCXTileCasing) tile;
                casing.setFacing(this.getFacing());
            }
            return true;
        }
        return false;
    }

    public void setReinforcedCasingWithSpecial(int3 pos, int position) {
        IBlockState state = world.getBlockState(pos.asBlockPos());
        if (state == reinforcedCasingState){
            TileEntity tile = world.getTileEntity(pos.asBlockPos());
            if (tile instanceof GTCXTileCasing){
                GTCXTileCasing  casing = (GTCXTileCasing) tile;
                casing.setFacing(this.getFacing());
            }
        }
    }

    public boolean isInputOutputHatch(int3 pos) {
        if (world.getBlockState(pos.asBlockPos()) == machineControlHatchState){
            TileEntity tile = world.getTileEntity(pos.asBlockPos());
            if (controlHatch != tile && tile instanceof GTCXTileMachineControlHatch){
                if (controlHatch != null){
                    controlHatch.setOwner(null);
                }
                controlHatch = (GTCXTileMachineControlHatch) tile;
                controlHatch.setOwner(this);
            }
            return true;
        }
        if (world.getBlockState(pos.asBlockPos()) == inputHatchState){
            if (world.getBlockState(input1) != inputHatchState){
                input1 = pos.asBlockPos();
                TileEntity tile = world.getTileEntity(pos.asBlockPos());
                if (inputHatch1 != tile && tile instanceof GTCXTileInputHatch){
                    if (inputHatch1 != null){
                        inputHatch1.setOwner(null);
                    }
                    inputHatch1 = (GTCXTileInputHatch) tile;
                    inputHatch1.setOwner(this);
                }
            } else if (world.getBlockState(input2) != inputHatchState){
                input2 = pos.asBlockPos();
                TileEntity tile = world.getTileEntity(pos.asBlockPos());
                if (inputHatch2 != tile && tile instanceof GTCXTileInputHatch){
                    if (inputHatch2 != null){
                        inputHatch2.setOwner(null);
                    }
                    inputHatch2 = (GTCXTileInputHatch) tile;
                    inputHatch2.setOwner(this);
                    inputHatch2.setSecond(true);
                    this.hasSecondInput = true;
                }
            }
            inputs++;
            return true;
        }
        if (world.getBlockState(pos.asBlockPos()) == outputHatchState){
            if (world.getBlockState(output) != outputHatchState){
                output = pos.asBlockPos();
                TileEntity tile = world.getTileEntity(pos.asBlockPos());
                if (outputHatch != tile && tile instanceof GTCXTileOutputHatch){
                    if (outputHatch != null){
                        outputHatch.setOwner(null);
                    }
                    outputHatch = (GTCXTileOutputHatch) tile;
                    outputHatch.setOwner(this);
                }
            }
            outputs++;
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == reinforcedCasingState;
    }
    public boolean isDynamoHatch(int3 pos) {
        if (world.getBlockState(pos.asBlockPos()) == dynamoHatchState){
            dynamo = pos.asBlockPos();
            TileEntity tile = world.getTileEntity(pos.asBlockPos());
            if (dynamoHatch != tile && tile instanceof GTCXTileDynamoHatch){
                if (dynamoHatch != null){
                    dynamoHatch.setOwner(null);
                }
                dynamoHatch = (GTCXTileDynamoHatch) tile;
                dynamoHatch.setOwner(this);
            }
            return true;
        }
        if (dynamoHatch != null){
            dynamoHatch = null;
        }
        return false;
    }

    @Override
    public void onBlockBreak() {
        removeRing(new int3(getPos(), getFacing()));
        if (controlHatch != null){
            controlHatch.setOwner(null);
        }
        if (inputHatch1 != null){
            inputHatch1.setOwner(null);
        }
        if (inputHatch2 != null){
            inputHatch2.setOwner(null);
        }
        if (outputHatch != null){
            outputHatch.setOwner(null);
        }
        if (dynamoHatch != null){
            dynamoHatch.setOwner(null);
        }
    }

    @Override
    public void onNetworkEvent(int i) {
        if (i < 6){
            getNetwork().initiateClientTileEntityEvent(this, i);
        }
    }

    @Override
    public void onNetworkEvent(EntityPlayer entityPlayer, int i) {
        if (i < 5){
            getNetwork().initiateTileEntityEvent(this, i + 1, false);
        }
        if (i == 5){
            addRing();
        }
    }

    @Override
    public int getProduction() {
        return production;
    }

    @Override
    public void setShouldCheckRecipe(boolean checkRecipe) {
        shouldCheckRecipe = checkRecipe;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public void setOutputModes(boolean second, GTCXTileOutputHatch.OutputModes mode) {
        if (!second) {
            this.outputMode = mode;
        }
    }

    @Override
    public int getStoredEnergy() {
        return this.energy;
    }

    @Override
    public int getMaxEnergy() {
        return this.maxEnergy;
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
        this.energy -= v;
    }

    @Override
    public int getSourceTier() {
        return 0;
    }

    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor iEnergyAcceptor, EnumFacing enumFacing) {
        return false;
    }

    public List<IEnergyTile> getSubTiles() {
        if (lastPositions == null){
            lastPositions = new ArrayList<>();
            lastPositions.add(this);
            if (dynamoHatch != null){
                lastPositions.add(dynamoHatch);
            }
        }
        return lastPositions;
    }
}
