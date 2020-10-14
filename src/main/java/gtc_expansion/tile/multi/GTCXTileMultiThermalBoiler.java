package gtc_expansion.tile.multi;

import gtc_expansion.container.GTCXContainerThermalBoiler;
import gtc_expansion.container.GTCXContainerThermalBoilerHatch;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileInputHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes;
import gtc_expansion.tile.hatch.GTCXTileMachineControlHatch;
import gtc_expansion.util.GTCXTank;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.interfaces.IGTMultiTileStatus;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.util.obj.ITankListener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Map;
import java.util.Random;

import static gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes.FLUID_ONLY;
import static gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes.ITEM_AND_FLUID;
import static gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes.ITEM_ONLY;

public class GTCXTileMultiThermalBoiler extends TileEntityMachine implements ITickable, IHasGui, ITankListener, IGTMultiTileStatus, IGTOwnerTile, IGTDebuggableTile {
    public boolean lastState;
    public boolean firstCheck = true;
    private BlockPos input1;
    private BlockPos input2;
    private BlockPos output1;
    private BlockPos output2;
    private GTCXTileInputHatch inputHatch1 = null;
    private GTCXTileInputHatch inputHatch2 = null;
    private GTCXTileOutputHatch outputHatch1 = null;
    private GTCXTileOutputHatch outputHatch2 = null;
    private GTCXTileMachineControlHatch controlHatch = null;
    private boolean disabled = false;
    private final FluidStack steam = GTMaterialGen.getFluidStack("steam", 800);
    private final ItemStack obsidian = GTMaterialGen.get(Blocks.OBSIDIAN, 1);
    @NetworkField(index = 3)
    private GTCXTank inputTank1 = new GTCXTank(32000);
    @NetworkField(index = 4)
    private GTCXTank inputTank2 = new GTCXTank(32000);
    @NetworkField(index = 5)
    private GTCXTank outputTank1 = new GTCXTank(32000);
    @NetworkField(index = 6)
    private GTCXTank outputTank2 = new GTCXTank(32000);
    private int tickOffset = 0;
    public static int slotFakeIn1 = 1;
    private static int slotOutput1 = 2;
    public static int slotFakeIn2 = 3;
    private static int slotOutput2 = 4;
    public static int slotFakeIn3 = 5;
    public static int slotFakeOut1 = 6;
    public static int slotFakeIn4 = 7;
    public static int slotFakeOut2 = 8;
    private static int slotDisplayIn1 = 9;
    private static int slotDisplayIn2 = 10;
    private static int slotDisplayOut1 = 11;
    private static int slotDisplayOut2 = 12;
    public static int slotNothing = 7;
    int ticker = 0;
    int obsidianTicker = 0;
    protected OutputModes outputMode1;
    protected OutputModes outputMode2;
    private boolean hasBothOutputs = false;
    public static final IBlockState reinforcedCasingState = GTCXBlocks.casingReinforced.getDefaultState();
    public static final IBlockState inputHatchState = GTCXBlocks.inputHatch.getDefaultState();
    public static final IBlockState outputHatchState = GTCXBlocks.outputHatch.getDefaultState();
    public static final IBlockState machineControlHatchState = GTCXBlocks.machineControlHatch.getDefaultState();
    protected InventoryHandler secondHandler = new InventoryHandler(this);

    public GTCXTileMultiThermalBoiler() {
        super(13);
        this.addGuiFields("lastState", "inputTank1", "inputTank2", "outputTank1", "outputTank2");
        this.addNetworkFields("inputTank1", "inputTank2", "outputTank1", "outputTank2");
        outputMode1 = ITEM_AND_FLUID;
        outputMode2 = ITEM_AND_FLUID;
        input1 = this.getPos();
        input2 = this.getPos();
        output1 = this.getPos();
        output2 = this.getPos();
        inputTank1.addListener(this);
        inputTank2.addListener(this);
        outputTank1.addListener(this);
        outputTank2.addListener(this);
        addSlots(secondHandler);
        secondHandler.validateSlots();
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        int slot = handler == secondHandler ? slotOutput2 : slotOutput1;
        handler.registerDefaultSlotAccess(AccessRule.Export, slot);
        handler.registerDefaultSlotsForSide(RotationList.DOWN, slot);
        handler.registerSlotType(SlotType.Output, slot);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.isSimulating()) {
            this.tickOffset = world.rand.nextInt(128);
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.lastState = nbt.getBoolean("lastState");
        this.disabled = nbt.getBoolean("disabled");
        this.ticker = nbt.getInteger("ticker");
        this.obsidianTicker = nbt.getInteger("obsidianTicker");
        this.outputMode1 = OutputModes.values()[nbt.getInteger("outputMode1")];
        this.outputMode2 = OutputModes.values()[nbt.getInteger("outputMode2")];
        this.input1 = readBlockPosFromNBT(nbt, "input1");
        this.input2 = readBlockPosFromNBT(nbt, "input2");
        this.output1 = readBlockPosFromNBT(nbt, "output1");
        this.output2 = readBlockPosFromNBT(nbt, "output2");
        this.inputTank1.readFromNBT(nbt.getCompoundTag("inputTank1"));
        this.inputTank2.readFromNBT(nbt.getCompoundTag("inputTank2"));
        this.outputTank1.readFromNBT(nbt.getCompoundTag("outputTank1"));
        this.outputTank2.readFromNBT(nbt.getCompoundTag("outputTank2"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("lastState", this.lastState);
        nbt.setBoolean("disabled", disabled);
        nbt.setInteger("ticker", ticker);
        nbt.setInteger("obsidianTicker", obsidianTicker);
        nbt.setInteger("outputMode1", outputMode1.ordinal());
        nbt.setInteger("outputMode2", outputMode2.ordinal());
        writeBlockPosToNBT(nbt, "input1", input1);
        writeBlockPosToNBT(nbt, "input2", input2);
        writeBlockPosToNBT(nbt, "output1", output1);
        writeBlockPosToNBT(nbt, "output2", output2);
        this.inputTank1.writeToNBT(this.getTag(nbt, "inputTank1"));
        this.inputTank2.writeToNBT(this.getTag(nbt, "inputTank2"));
        this.outputTank1.writeToNBT(this.getTag(nbt, "outputTank1"));
        this.outputTank2.writeToNBT(this.getTag(nbt, "outputTank2"));
        return nbt;
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
            this.firstCheck = false;
            this.getNetwork().updateTileGuiField(this, "lastState");
        }
        return this.lastState;
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.setStackInSlot(slotDisplayIn1, ItemDisplayIcon.createWithFluidStack(inputTank1.getFluid()));
        this.setStackInSlot(slotDisplayIn2, ItemDisplayIcon.createWithFluidStack(inputTank2.getFluid()));
        this.setStackInSlot(slotDisplayOut1, ItemDisplayIcon.createWithFluidStack(outputTank1.getFluid()));
        this.setStackInSlot(slotDisplayOut2, ItemDisplayIcon.createWithFluidStack(outputTank2.getFluid()));
        this.getNetwork().updateTileGuiField(this, "inputTank1");
        this.getNetwork().updateTileGuiField(this, "inputTank2");
        this.getNetwork().updateTileGuiField(this, "outputTank1");
        this.getNetwork().updateTileGuiField(this, "outputTank2");
    }

    boolean output1Full = false;

    @Override
    public void update() {
        GTHelperFluid.doFluidContainerThings(this, this.inputTank1, slotFakeIn3, slotFakeOut1);
        GTHelperFluid.doFluidContainerThings(this, this.inputTank2, slotFakeIn4, slotFakeOut2);
        GTHelperFluid.doFluidContainerThings(this, this.outputTank1, slotFakeIn1, slotOutput1);
        GTHelperFluid.doFluidContainerThings(this, this.outputTank2, slotFakeIn2, slotOutput2);
        TileEntity tile = world.getTileEntity(input1);
        TileEntity tile2 = world.getTileEntity(input2);
        TileEntity oTile = world.getTileEntity(output1);
        boolean canWork = canWork() && tile instanceof GTCXTileInputHatch && tile2 instanceof GTCXTileInputHatch && oTile instanceof GTCXTileOutputHatch;
        if (canWork){
            if (outputHatch1 != null){
                this.outputMode1 = outputHatch1.getCycle();
            }
            if (outputHatch2 != null){
                this.outputMode2 = outputHatch2.getCycle();
            }
            if (inputTank1.getFluid() != null && inputTank2.getFluid() != null && inputTank1.getFluidAmount() > 0 && inputTank2.getFluidAmount() > 0 && !disabled){
                boolean lava = false;
                boolean water = false;
                IC2Tank lavaTank = inputTank1;
                IC2Tank waterTank = inputTank2;
                if (inputTank1.getFluid().isFluidEqual(GTMaterialGen.getFluidStack("water", 1))){
                    water = true;
                    waterTank = inputTank1;
                } else if (inputTank1.getFluid().isFluidEqual(GTMaterialGen.getFluidStack("lava", 1))){
                    lava = true;
                    lavaTank = inputTank1;
                }
                if (inputTank2.getFluid().isFluidEqual(GTMaterialGen.getFluidStack("water", 1)) && !water){
                    water = true;
                    waterTank = inputTank2;
                } else if (inputTank2.getFluid().isFluidEqual(GTMaterialGen.getFluidStack("lava", 1)) && !lava){
                    lava = true;
                    lavaTank = inputTank2;
                }
                if (water && lava && lavaTank.getFluidAmount() >= 500 && waterTank.getFluidAmount() >= 5){
                    OutputModes cycle1 = outputMode1;
                    if (hasBothOutputs){
                        OutputModes cycle2 = outputMode2;
                        if ((cycle1 == ITEM_AND_FLUID && cycle2 == ITEM_AND_FLUID) || (cycle1 == ITEM_AND_FLUID && cycle2 == FLUID_ONLY) || (cycle1 == FLUID_ONLY && cycle2 == ITEM_AND_FLUID)){
                            //noinspection ConstantConditions
                            if (outputTank1.getFluidAmount() == 0 || (outputTank1.getFluid().isFluidEqual(steam) && outputTank1.getFluidAmount() + 800 <= outputTank1.getCapacity())){
                                if (!this.getActive()){
                                    this.setActive(true);
                                }
                                fillSteam(waterTank, lavaTank, outputTank1);
                            } else if (outputTank1.getFluidAmount() < outputTank1.getCapacity() && outputTank1.getFluid().isFluidEqual(steam)){
                                int amount = outputTank1.getCapacity() - outputTank1.getFluidAmount();
                                int remaining = 800 - amount;
                                //noinspection ConstantConditions
                                if (outputTank2.getFluidAmount() == 0 || (outputTank2.getFluid().isFluidEqual(steam) && outputTank2.getFluidAmount() + remaining <= outputTank2.getCapacity())){
                                    if (!this.getActive()){
                                        this.setActive(true);
                                    }
                                    waterTank.drainInternal(5, true);
                                    lavaTank.drainInternal(500, true);
                                    if (obsidianTicker >= 1){
                                        addObsidian(true);
                                    } else {
                                        obsidianTicker++;
                                    }

                                    outputTank1.fill(GTMaterialGen.getFluidStack("steam", amount), true);
                                    outputTank2.fill(GTMaterialGen.getFluidStack("steam", remaining), true);
                                }
                            } else if (outputTank2.getFluidAmount() == 0 || (outputTank2.getFluid().isFluidEqual(steam) && outputTank2.getFluidAmount() + 800 <= outputTank2.getCapacity())){
                                if (!this.getActive()){
                                    this.setActive(true);
                                }
                                fillSteam(waterTank, lavaTank, outputTank2);
                            } else {
                                if (this.getActive()){
                                    this.setActive(false);
                                }
                            }
                        } else if (opposite(cycle1, cycle2) || (cycle1 == ITEM_AND_FLUID && cycle2 == ITEM_ONLY) || (cycle2 == ITEM_AND_FLUID && cycle1 == ITEM_ONLY)){
                            if (cycle1.isFluid()){
                                //noinspection ConstantConditions
                                if (outputTank1.getFluidAmount() == 0 || (outputTank1.getFluid().isFluidEqual(steam) && outputTank1.getFluidAmount() + 800 <= outputTank1.getCapacity())){
                                    if (!this.getActive()){
                                        this.setActive(true);
                                    }
                                    fillSteam(waterTank, lavaTank, outputTank1);
                                } else {
                                    if (this.getActive()){
                                        this.setActive(false);
                                    }
                                }
                            } else {
                                //noinspection ConstantConditions
                                if (outputTank2.getFluidAmount() == 0 || (outputTank2.getFluid().isFluidEqual(steam) && outputTank2.getFluidAmount() + 800 <= outputTank2.getCapacity())){
                                    if (!this.getActive()){
                                        this.setActive(true);
                                    }
                                    fillSteam(waterTank, lavaTank, outputTank2);
                                } else {
                                    if (this.getActive()){
                                        this.setActive(false);
                                    }
                                }
                            }
                        } else {
                            if (this.getActive()){
                                this.setActive(false);
                            }
                        }
                    } else {
                        if (cycle1.isFluid()){
                            //noinspection ConstantConditions
                            if (outputTank1.getFluidAmount() == 0 || (outputTank1.getFluid().isFluidEqual(steam) && outputTank1.getFluidAmount() + 800 <= outputTank1.getCapacity())){
                                if (!this.getActive()){
                                    this.setActive(true);
                                }
                                fillSteam(waterTank, lavaTank, outputTank1);
                            } else {
                                if (this.getActive()){
                                    this.setActive(false);
                                }
                            }
                        }else {
                            if (this.getActive()){
                                this.setActive(false);
                            }
                        }
                    }
                } else {
                    if (this.getActive()){
                        this.setActive(false);
                    }
                }
            } else {
                if (this.getActive()){
                    this.setActive(false);
                }
            }
        } else {
            if (this.getActive()){
                this.setActive(false);
            }
        }
    }

    public boolean opposite(OutputModes mode1, OutputModes mode2){
        return (mode1 == FLUID_ONLY && mode2 == ITEM_ONLY) || (mode1 == ITEM_ONLY && mode2 == FLUID_ONLY);
    }

    public void addObsidian(boolean both){
        OutputModes cycle1 = outputMode1;

        ItemStack compare = this.getStackInSlot(0).getItem() == GTCXItems.lavaFilter ? getOutput() : obsidian;
        int slot = slotOutput1;
        if (both){
            OutputModes cycle2 = outputMode2;
            if (!cycle1.isItem() && !cycle2.isItem()){
                return;
            }

            if (cycle1.isItem() && !cycle2.isItem()){
                slot = slotOutput1;
            } else if (!cycle1.isItem()){
                slot = slotOutput2;
            } else {
                slot = this.getStackInSlot(slotOutput1).isEmpty() || (this.getStackInSlot(slotOutput1).getItem() == compare.getItem() && this.getStackInSlot(slotOutput1).getCount() < 64) ? slotOutput1 : slotOutput2;
            }
        } else {
            if (!cycle1.isItem()){
                return;
            }
        }
        ItemStack output = this.getStackInSlot(slot);

        if (output.getItem() == compare.getItem() && output.getCount() < 64){
            output1Full = false;
            output.grow(1);
        } else if (output.isEmpty()){
            output1Full = false;
            this.setStackInSlot(slot, compare.copy());
        } else {
            output1Full = true;
        }
        if (!output1Full){
            obsidianTicker = 0;
        }
        if (this.getStackInSlot(0).getItem() == GTCXItems.lavaFilter){
            if (ticker < 80){
                ticker++;
            } else {
                if (this.getStackInSlot(0).attemptDamageItem(1, world.rand, null)){
                    this.getStackInSlot(0).shrink(1);
                }
                ticker = 0;
            }
        }


    }

    public ItemStack getOutput(){
        Random random = world.rand;
        int randomNumber = random.nextInt(100);
        if (randomNumber < 90){
            return obsidian;
        } else if (randomNumber < 95){
            return GTCXMaterialGen.getNugget(GTCXMaterial.Tin, 1);
        } else if (randomNumber < 99){
            return GTCXMaterialGen.getNugget(GTCXMaterial.Copper, 1);
        } else {
            return GTCXMaterialGen.getNugget(GTMaterial.Electrum, 1);
        }
    }

    public void fillSteam(IC2Tank water, IC2Tank lava, IC2Tank output){
        water.drainInternal(5, true);
        lava.drainInternal(500, true);
        if (obsidianTicker < 1){
            obsidianTicker++;
        } else {
            addObsidian(hasBothOutputs);
        }
        output.fill(steam, true);
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerThermalBoiler(entityPlayer.inventory, this);
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
        this.output1 = this.getPos();
        this.output2 = this.getPos();
        this.input1 = this.getPos();
        this.input2 = this.getPos();
        int3 dir = new int3(getPos(), getFacing());
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.right(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.up(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.up(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.left(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.left(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.back(1))){
            return false;
        }
        if (!isHatch(dir.up(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.up(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.back(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.right(1))){
            return false;
        }
        if (!isHatch(dir.up(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.up(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.right(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.forward(1))){
            return false;
        }
        if (!isHatch(dir.up(1))){
            return false;
        }
        if (!isReinforcedCasing(dir.up(1))){
            return false;
        }
        if (!isHatch(dir.left(1))){
            return false;
        }
        if (!world.isAirBlock(dir.down(1).asBlockPos())){
            return false;
        }
        if (!isHatch(dir.down(1))){
            return false;
        }
        hasBothOutputs = outputs > 1;
        return inputs >= 2 && outputs >= 2;
    }

    public boolean isReinforcedCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == reinforcedCasingState;
    }

    public boolean isHatch(int3 pos) {
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
                }
            }
            inputs++;
            return true;
        }
        if (world.getBlockState(pos.asBlockPos()) == outputHatchState){
            if (world.getBlockState(output1) != outputHatchState){
                output1 = pos.asBlockPos();
                TileEntity tile = world.getTileEntity(pos.asBlockPos());
                if (outputHatch1 != tile && tile instanceof GTCXTileOutputHatch){
                    if (outputHatch1 != null){
                        outputHatch1.setOwner(null);
                    }
                    outputHatch1 = (GTCXTileOutputHatch) tile;
                    outputHatch1.setOwner(this);
                }
            } else if (world.getBlockState(output2) != outputHatchState){
                output2 = pos.asBlockPos();
                hasBothOutputs = true;
                TileEntity tile = world.getTileEntity(pos.asBlockPos());
                if (outputHatch2 != tile && tile instanceof GTCXTileOutputHatch){
                    if (outputHatch2 != null){
                        outputHatch2.setOwner(null);
                    }
                    outputHatch2 = (GTCXTileOutputHatch) tile;
                    outputHatch2.setOwner(this);
                    outputHatch2.setSecond(true);
                }
            }
            outputs++;
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == reinforcedCasingState;
    }

    @Override
    public void onBlockBreak() {
        if (controlHatch != null){
            controlHatch.setOwner(null);
        }
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
                return hatch.isSecond() ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(outputTank2) : CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(outputTank1);
            }
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.DOWN){
            if (hatch.isSecond()){
                return this.secondHandler == null ? null : CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.secondHandler.getInventory(facing));
            }
            return this.getHandler() == null ? null : CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.handler.getInventory(facing));
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public GTCXTank getTank(GTCXTileItemFluidHatches hatch) {
        return hatch.isInput() ? (hatch.isSecond() ? this.inputTank2 : this.inputTank1) : (hatch.isSecond() ? this.outputTank2 : this.outputTank1);
    }

    public GTCXTank getInputTank1() {
        return this.inputTank1;
    }

    public GTCXTank getInputTank2() {
        return this.inputTank2;
    }

    public GTCXTank getOutputTank1() {
        return this.outputTank1;
    }

    public GTCXTank getOutputTank2() {
        return this.outputTank2;
    }

    @Override
    public void setShouldCheckRecipe(boolean checkRecipe) {

    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;

    }

    @Override
    public int getOutputSlot(GTCXTileItemFluidHatches hatch) {
        int index = hatch.isInput() ? (hatch.isSecond() ? 3 : 2) : (hatch.isSecond() ? 1 : 0);
        return 2 + (2 * index);
    }

    @Override
    public int getInputSlot(GTCXTileItemFluidHatches hatch) {
        int index = hatch.isInput() ? (hatch.isSecond() ? 3 : 2) : (hatch.isSecond() ? 1 : 0);
        return 1 + (2 * index);
    }

    @Override
    public void setOutputModes(boolean second, OutputModes mode) {
        if (second) {
            this.outputMode2 = mode;
        } else {
            this.outputMode1 = mode;
        }
    }

    @Override
    public void invalidateStructure() {
        this.firstCheck = true;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer, GTCXTileItemFluidHatches hatch) {
        return new GTCXContainerThermalBoilerHatch(entityPlayer.inventory, this, hatch.isSecond(), hatch.isInput());
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put("Output Mode 1: " + outputMode1.toString(), true);
        map.put("Output Mode 2: " + outputMode2.toString(), true);
        map.put("Input hatch 1 pos: " + (inputHatch1 != null ? inputHatch1.getPos() : "null"), true);
        map.put("Input hatch 2 pos: " + (inputHatch2 != null ? inputHatch2.getPos() : "null"), true);
        map.put("Output hatch 1 pos: " + (outputHatch1 != null ? outputHatch1.getPos() : "null"), true);
        map.put("Output hatch 2 pos: " + (outputHatch2 != null ? outputHatch2.getPos() : "null"), true);
    }
}
