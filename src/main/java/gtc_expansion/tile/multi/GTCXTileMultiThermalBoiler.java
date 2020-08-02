package gtc_expansion.tile.multi;

import gtc_expansion.container.GTCXContainerThermalBoiler;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileInputHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes;
import gtc_expansion.tile.hatch.GTCXTileMachineControlHatch;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTMultiTileStatus;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.util.obj.ITickListener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

import static gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes.FLUID_ONLY;
import static gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes.ITEM_AND_FLUID;
import static gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch.OutputModes.ITEM_ONLY;

public class GTCXTileMultiThermalBoiler extends TileEntityMachine implements ITickListener, IHasGui, IGTMultiTileStatus, IGTOwnerTile {
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
    int ticker = 0;
    int obsidianTicker = 0;
    public static final IBlockState reinforcedCasingState = GTCXBlocks.casingReinforced.getDefaultState();
    public static final IBlockState inputHatchState = GTCXBlocks.inputHatch.getDefaultState();
    public static final IBlockState outputHatchState = GTCXBlocks.outputHatch.getDefaultState();
    public static final IBlockState machineControlHatchState = GTCXBlocks.machineControlHatch.getDefaultState();

    public GTCXTileMultiThermalBoiler() {
        super(1);
        this.addGuiFields("lastState");
        input1 = this.getPos();
        input2 = this.getPos();
        output1 = this.getPos();
        output2 = this.getPos();
    }


    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.lastState = nbt.getBoolean("lastState");
        this.disabled = nbt.getBoolean("disabled");
        this.ticker = nbt.getInteger("ticker");
        this.obsidianTicker = nbt.getInteger("obsidianTicker");
        this.input1 = readBlockPosFromNBT(nbt, "input1");
        this.input2 = readBlockPosFromNBT(nbt, "input2");
        this.output1 = readBlockPosFromNBT(nbt, "output1");
        this.output2 = readBlockPosFromNBT(nbt, "output2");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("lastState", this.lastState);
        nbt.setBoolean("disabled", disabled);
        nbt.setInteger("ticker", ticker);
        nbt.setInteger("obsidianTicker", obsidianTicker);
        writeBlockPosToNBT(nbt, "input1", input1);
        writeBlockPosToNBT(nbt, "input2", input2);
        writeBlockPosToNBT(nbt, "output1", output1);
        writeBlockPosToNBT(nbt, "output2", output2);
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
        if (this.world.getTotalWorldTime() % 256L == 0L || this.firstCheck) {
            this.lastState = this.checkStructure();
            this.firstCheck = false;
            this.getNetwork().updateTileGuiField(this, "lastState");
        }
        return this.lastState;
    }

    boolean output1Full = false;
    boolean output2Full = false;

    @Override
    public void onTick() {
        TileEntity tile = world.getTileEntity(input1);
        TileEntity tile2 = world.getTileEntity(input2);
        TileEntity oTile = world.getTileEntity(output1);
        TileEntity oTile2 = world.getTileEntity(output2);
        boolean canWork = canWork() && tile instanceof GTCXTileInputHatch && tile2 instanceof GTCXTileInputHatch && oTile instanceof GTCXTileOutputHatch;
        if (canWork){
            if (inputHatch1 != tile){
                inputHatch1 = (GTCXTileInputHatch) tile;
            }
            if (inputHatch2 != tile2){
                inputHatch2 = (GTCXTileInputHatch) tile2;
            }
            if (outputHatch1 != oTile){
                outputHatch1 = (GTCXTileOutputHatch) world.getTileEntity(output1);
            }
            if (oTile2 instanceof GTCXTileOutputHatch && outputHatch2 != oTile2){
                outputHatch2 = (GTCXTileOutputHatch) oTile2;
            }
            if (inputHatch1.getTank().getFluid() != null && inputHatch2.getTank().getFluid() != null && inputHatch1.getTank().getFluidAmount() > 0 && inputHatch2.getTank().getFluidAmount() > 0 && !disabled){
                boolean lava = false;
                boolean water = false;
                IC2Tank lavaTank = inputHatch1.getTank();
                IC2Tank waterTank = inputHatch2.getTank();
                if (inputHatch1.getTank().getFluid() != null && inputHatch2.getTank().getFluid() != null){
                    if (inputHatch1.getTank().getFluid().isFluidEqual(GTMaterialGen.getFluidStack("water", 1))){
                        water = true;
                        waterTank = inputHatch1.getTank();
                    } else if (inputHatch1.getTank().getFluid().isFluidEqual(GTMaterialGen.getFluidStack("lava", 1))){
                        lava = true;
                        lavaTank = inputHatch1.getTank();
                    }
                    if (inputHatch2.getTank().getFluid().isFluidEqual(GTMaterialGen.getFluidStack("water", 1)) && !water){
                        water = true;
                        waterTank = inputHatch2.getTank();
                    } else if (inputHatch2.getTank().getFluid().isFluidEqual(GTMaterialGen.getFluidStack("lava", 1)) && !lava){
                        lava = true;
                        lavaTank = inputHatch2.getTank();
                    }
                    if (water && lava && lavaTank.getFluidAmount() >= 500 && waterTank.getFluidAmount() >= 5){
                        OutputModes cycle1 = outputHatch1.getCycle();
                        IC2Tank outputTank1 = outputHatch1.getTank();
                        if (outputHatch2 != null){
                            OutputModes cycle2 = outputHatch2.getCycle();
                            IC2Tank outputTank2 = outputHatch2.getTank();
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
                }
            } else {
                if (this.getActive()){
                    this.setActive(false);
                }
            }
        } else {
            if (inputHatch1 != null) inputHatch1 = null;
            if (inputHatch2 != null) inputHatch2 = null;
            if (outputHatch1 != null) outputHatch1 = null;
            if (outputHatch2 != null) outputHatch2 = null;
            if (this.getActive()){
                this.setActive(false);
            }
        }
    }

    public boolean opposite(OutputModes mode1, OutputModes mode2){
        return (mode1 == FLUID_ONLY && mode2 == ITEM_ONLY) || (mode1 == ITEM_ONLY && mode2 == FLUID_ONLY);
    }

    public void addObsidian(boolean both){
        OutputModes cycle1 = outputHatch1.getCycle();

        ItemStack compare = this.getStackInSlot(0).getItem() == GTCXItems.lavaFilter ? getOutput() : obsidian;
        GTCXTileOutputHatch hatch = outputHatch1;
        if (both){
            OutputModes cycle2 = outputHatch2.getCycle();
            if (!cycle1.isItem() && !cycle2.isItem()){
                return;
            }

            if (cycle1.isItem() && !cycle2.isItem()){
                hatch = outputHatch1;
            } else if (!cycle1.isItem()){
                hatch = outputHatch2;
            } else {
                hatch = outputHatch1.getOutput().isEmpty() || (outputHatch1.getOutput().getItem() == compare.getItem() && outputHatch1.getOutput().getCount() < 64) ? outputHatch1 : outputHatch2;
            }
        } else {
            if (!cycle1.isItem()){
                return;
            }
        }
        ItemStack output = hatch.getOutput();

        if (output.getItem() == compare.getItem() && output.getCount() < 64){
            output1Full = false;
            hatch.skipTick();
            output.grow(1);
        } else if (output.isEmpty()){
            output1Full = false;
            hatch.skipTick();
            hatch.setStackInSlot(1, compare.copy());
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
            addObsidian(outputHatch2 != null);
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
        if (world.getBlockState(dir.down(1).asBlockPos()) != Blocks.AIR.getDefaultState()){
            return false;
        }
        if (!isHatch(dir.down(1))){
            return false;
        }
        if (inputs < 2 || outputs < 1){
            return false;
        }
        return true;
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
            } else if (world.getBlockState(input2) != inputHatchState){
                input2 = pos.asBlockPos();
            }
            inputs++;
            return true;
        }
        if (world.getBlockState(pos.asBlockPos()) == outputHatchState){
            if (world.getBlockState(output1) != outputHatchState){
                output1 = pos.asBlockPos();
            } else if (world.getBlockState(output2) != outputHatchState){
                output2 = pos.asBlockPos();
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
    public void setShouldCheckRecipe(boolean checkRecipe) {

    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;

    }
}
