package gtc_expansion.tile.multi;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.GTCXItems;
import gtc_expansion.container.GTCXContainerThermalBoiler;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileInputHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTMultiTileStatus;
import gtclassic.api.material.GTMaterialGen;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class GTCXTileMultiThermalBoiler extends TileEntityMachine implements ITickable, IHasGui, IGTMultiTileStatus {
    public boolean lastState;
    public boolean firstCheck = true;
    private BlockPos input1;
    private BlockPos input2;
    private BlockPos output1;
    private BlockPos output2;
    private BlockPos output3;
    int ticker = 0;
    public static final IBlockState reinforcedCasingState = GTCXBlocks.casingReinforced.getDefaultState();
    public static final IBlockState inputHatchState = GTCXBlocks.inputHatch.getDefaultState();
    public static final IBlockState outputHatchState = GTCXBlocks.outputHatch.getDefaultState();

    public GTCXTileMultiThermalBoiler() {
        super(1);
        this.addGuiFields("lastState");
        input1 = this.getPos();
        input2 = this.getPos();
        output1 = this.getPos();
        output2 = this.getPos();
        output3 = this.getPos();
    }


    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.lastState = nbt.getBoolean("lastState");
        this.ticker = nbt.getInteger("ticker");
        this.input1 = readBlockPosFromNBT(nbt, "input1");
        this.input2 = readBlockPosFromNBT(nbt, "input2");
        this.output1 = readBlockPosFromNBT(nbt, "output1");
        this.output2 = readBlockPosFromNBT(nbt, "output2");
        this.output3 = readBlockPosFromNBT(nbt, "output3");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("lastState", this.lastState);
        nbt.setInteger("ticker", ticker);
        writeBlockPosToNBT(nbt, "input1", input1);
        writeBlockPosToNBT(nbt, "input2", input2);
        writeBlockPosToNBT(nbt, "output1", output1);
        writeBlockPosToNBT(nbt, "output2", output2);
        writeBlockPosToNBT(nbt, "output3", output3);
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

    @Override
    public void update() {
        if (ticker < 80){
            ticker++;
        }
        boolean canWork = canWork() && world.getTileEntity(input1) instanceof GTCXTileInputHatch && world.getTileEntity(input2) instanceof GTCXTileInputHatch && world.getTileEntity(output1) instanceof GTCXTileOutputHatch;
        if (canWork && this.getStackInSlot(0).getItem() == GTCXItems.lavaFilter){
            GTCXTileInputHatch inputHatch1 = (GTCXTileInputHatch) world.getTileEntity(input1);
            GTCXTileInputHatch inputHatch2 = (GTCXTileInputHatch) world.getTileEntity(input2);
            GTCXTileOutputHatch outputHatch1 = (GTCXTileOutputHatch) world.getTileEntity(output1);
            if (inputHatch1.getTank().getFluid() != null && inputHatch2.getTank().getFluid() != null && inputHatch1.getTank().getFluidAmount() > 0 && inputHatch2.getTank().getFluidAmount() > 0){
                boolean lava = false;
                boolean water = false;
                IC2Tank lavaTank = inputHatch1.getTank();
                IC2Tank waterTank = inputHatch2.getTank();
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
                if (water && lava && lavaTank.getFluidAmount() >= 100){
                    if (outputHatch1.getTank().getFluidAmount() == 0 || (outputHatch1.getTank().getFluid().isFluidEqual(GTMaterialGen.getFluidStack("steam", 160)) && outputHatch1.getTank().getFluidAmount() + 160 <= outputHatch1.getTank().getCapacity())){
                        if (!this.getActive()){
                            this.setActive(true);
                        }
                        waterTank.drainInternal(1, true);
                        lavaTank.drainInternal(100, true);
                        outputHatch1.getTank().fill(GTMaterialGen.getFluidStack("steam", 160), true);
                        if (ticker >= 80){
                            this.getStackInSlot(0).attemptDamageItem(1, world.rand, null);
                            ticker = 0;
                        }
                    } else if (world.getTileEntity(output2) instanceof GTCXTileOutputHatch && (((GTCXTileOutputHatch)world.getTileEntity(output2)).getTank().getFluidAmount() == 0 || (((GTCXTileOutputHatch)world.getTileEntity(output2)).getTank().getFluid().isFluidEqual(GTMaterialGen.getFluidStack("steam", 160)) && ((GTCXTileOutputHatch)world.getTileEntity(output2)).getTank().getFluidAmount() + 160 <= ((GTCXTileOutputHatch)world.getTileEntity(output2)).getTank().getCapacity()))){
                        if (!this.getActive()){
                            this.setActive(true);
                        }
                        waterTank.drainInternal(1, true);
                        lavaTank.drainInternal(100, true);
                        ((GTCXTileOutputHatch)world.getTileEntity(output2)).getTank().fill(GTMaterialGen.getFluidStack("steam", 160), true);
                        if (ticker >= 80){
                            this.getStackInSlot(0).attemptDamageItem(1, world.rand, null);
                            ticker = 0;
                        }
                    } else if (world.getTileEntity(output3) instanceof GTCXTileOutputHatch && (((GTCXTileOutputHatch)world.getTileEntity(output3)).getTank().getFluidAmount() == 0 || (((GTCXTileOutputHatch)world.getTileEntity(output3)).getTank().getFluid().isFluidEqual(GTMaterialGen.getFluidStack("steam", 160)) && ((GTCXTileOutputHatch)world.getTileEntity(output3)).getTank().getFluidAmount() + 160 <= ((GTCXTileOutputHatch)world.getTileEntity(output3)).getTank().getCapacity()))){
                        if (!this.getActive()){
                            this.setActive(true);
                        }
                        waterTank.drainInternal(1, true);
                        lavaTank.drainInternal(100, true);
                        ((GTCXTileOutputHatch)world.getTileEntity(output3)).getTank().fill(GTMaterialGen.getFluidStack("steam", 160), true);
                        if (ticker >= 80){
                            this.getStackInSlot(0).attemptDamageItem(1, world.rand, null);
                            ticker = 0;
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
        } else {
            if (this.getActive()){
                this.setActive(false);
            }
        }
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
            } else if (world.getBlockState(output3) != outputHatchState){
                output3 = pos.asBlockPos();
            }
            outputs++;
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == reinforcedCasingState;
    }
}
