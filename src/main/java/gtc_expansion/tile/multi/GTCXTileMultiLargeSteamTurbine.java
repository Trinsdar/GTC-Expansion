package gtc_expansion.tile.multi;

import gtc_expansion.container.GTCXContainerLargeSteamTurbine;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.interfaces.IGTMultiTileProduction;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtc_expansion.tile.GTCXTileCasing;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch.GTCXTileDynamoHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileInputHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileOutputHatch;
import gtc_expansion.tile.hatch.GTCXTileMachineControlHatch;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.interfaces.IGTMultiTileStatus;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public class GTCXTileMultiLargeSteamTurbine extends TileEntityMachine implements ITickable, IHasGui, IGTMultiTileStatus, IGTMultiTileProduction, INetworkClientTileEntityEventListener, INetworkTileEntityEventListener, IGTOwnerTile, IGTDebuggableTile {
    public boolean lastState;
    public boolean firstCheck = true;
    private BlockPos input1;
    private BlockPos input2;
    private BlockPos dynamo;
    private BlockPos output;
    private GTCXTileInputHatch inputHatch1 = null;
    private GTCXTileInputHatch inputHatch2 = null;
    private GTCXTileDynamoHatch dynamoHatch = null;
    private GTCXTileOutputHatch outputHatch = null;
    private GTCXTileMachineControlHatch controlHatch = null;
    private boolean disabled = false;
    int production;
    int ticker = 0;
    final FluidStack water = GTMaterialGen.getFluidStack("water", 10);
    public static final IBlockState standardCasingState = GTCXBlocks.casingStandard.getDefaultState();
    public static final IBlockState inputHatchState = GTCXBlocks.inputHatch.getDefaultState();
    public static final IBlockState outputHatchState = GTCXBlocks.outputHatch.getDefaultState();
    public static final IBlockState dynamoHatchState = GTCXBlocks.dynamoHatch.getDefaultState();
    public static final IBlockState machineControlHatchState = GTCXBlocks.machineControlHatch.getDefaultState();

    public GTCXTileMultiLargeSteamTurbine() {
        super(1);
        this.addGuiFields("lastState", "production");
        input1 = this.getPos();
        input2 = this.getPos();
        dynamo = this.getPos();
        output = this.getPos();
        production = 0;
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.lastState = nbt.getBoolean("lastState");
        this.disabled = nbt.getBoolean("disabled");
        this.ticker = nbt.getInteger("ticker");
        this.input1 = readBlockPosFromNBT(nbt, "input1");
        this.input2 = readBlockPosFromNBT(nbt, "input2");
        this.dynamo = readBlockPosFromNBT(nbt, "dynamo");
        this.output = readBlockPosFromNBT(nbt, "output");
        this.production = nbt.getInteger("production");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("lastState", this.lastState);
        nbt.setBoolean("disabled", this.disabled);
        nbt.setInteger("ticker", ticker);
        writeBlockPosToNBT(nbt, "input1", input1);
        writeBlockPosToNBT(nbt, "input2", input2);
        writeBlockPosToNBT(nbt, "dynamo", dynamo);
        writeBlockPosToNBT(nbt, "output", output);
        nbt.setInteger("production", production);
        return nbt;
    }

    public void onBlockRemoved() {
        removeRing(new int3(getPos(), getFacing()));
        if (controlHatch != null){
            controlHatch.setOwner(null);
        }
    }

    public void onBlockPlaced(){
        getNetwork().initiateClientTileEntityEvent(this, 1);
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
            if (this.firstCheck){
                this.firstCheck = false;
            }
            this.getNetwork().updateTileGuiField(this, "lastState");
        }
        return this.lastState;
    }

    public boolean isTurbineRotor(ItemStack stack){
        return stack.getItem() == GTCXItems.bronzeTurbineRotor || stack.getItem() == GTCXItems.steelTurbineRotor || stack.getItem() == GTCXItems.magnaliumTurbineRotor || stack.getItem() == GTCXItems.tungstensteelTurbineRotor || stack.getItem() == GTCXItems.carbonTurbineRotor;
    }


    @Override
    public void update() {
        if (ticker < 80){
            ticker++;
        }
        TileEntity tile = world.getTileEntity(input1);
        TileEntity tile2 = world.getTileEntity(input2);
        TileEntity oTile = world.getTileEntity(output);
        TileEntity dTile2 = world.getTileEntity(dynamo);
        boolean canWork = canWork() && tile instanceof GTCXTileInputHatch && dTile2 instanceof GTCXTileDynamoHatch;
        if (canWork && isTurbineRotor(this.getStackInSlot(0))){
            if (inputHatch1 != tile){
                inputHatch1 = (GTCXTileInputHatch) tile;
            }
            if (dynamoHatch != dTile2){
                dynamoHatch = (GTCXTileDynamoHatch) dTile2;
            }
            if (tile2 instanceof GTCXTileInputHatch) {
                if (inputHatch2 != tile2) {
                    inputHatch2 = (GTCXTileInputHatch) tile2;
                }
            } else {
                if (inputHatch2 != null) inputHatch2 = null;
            }
            if (oTile instanceof GTCXTileOutputHatch) {
                if (outputHatch != oTile) {
                    outputHatch = (GTCXTileOutputHatch) oTile;
                }
            } else {
                if (outputHatch != null) outputHatch = null;
            }
            production = (int)(800 * getRotorEfficiency(this.getStackInSlot(0)));
            int fluidAmount = 1600;
            FluidStack compare = GTMaterialGen.getFluidStack("steam", fluidAmount);
            if (dynamoHatch.getStoredEnergy() + production <= dynamoHatch.getMaxEnergyStorage() && !disabled){
                FluidStack inputFluid1 = inputHatch1.getTank().getFluid();
                if (inputFluid1 != null && inputFluid1.isFluidEqual(compare) && inputFluid1.amount >= fluidAmount){
                    if (!this.getActive()){
                        this.setActive(true);
                        this.setRingActive(true);
                    }
                    inputHatch1.getTank().drainInternal(1600, true);
                    if (outputHatch != null && outputHatch.getCycle().isFluid()){
                        //noinspection ConstantConditions
                        if (outputHatch.getTank().getFluidAmount() == 0 || (outputHatch.getTank().getFluid().isFluidEqual(water) && outputHatch.getTank().getFluidAmount() + 10 <= outputHatch.getTank().getCapacity())){
                            outputHatch.getTank().fillInternal(water, true);
                        }
                    }
                    dynamoHatch.addEnergy(production);
                    if (ticker >= 80){
                        if (this.getStackInSlot(0).attemptDamageItem(1, world.rand, null)){
                            this.getStackInSlot(0).shrink(1);
                        }
                        ticker = 0;
                    }
                } else if (inputHatch2 != null){
                    FluidStack inputFluid2 = inputHatch2.getTank().getFluid();
                    if (inputFluid1 != null && inputFluid1.isFluidEqual(compare) && inputFluid2 != null && inputFluid2.isFluidEqual(compare) && inputHatch1.getTank().getFluidAmount() > 0){
                        int amount = inputHatch1.getTank().getFluidAmount();
                        int remaining = fluidAmount - amount;
                        if (inputHatch2.getTank().getFluidAmount() >= remaining) {
                            if (!this.getActive()) {
                                this.setActive(true);
                                this.setRingActive(true);
                            }
                            inputHatch1.getTank().drainInternal(amount, true);
                            inputHatch2.getTank().drainInternal(remaining, true);
                            if (outputHatch != null && outputHatch.getCycle().isFluid()){
                                //noinspection ConstantConditions
                                if (outputHatch.getTank().getFluidAmount() == 0 || (outputHatch.getTank().getFluid().isFluidEqual(water) && outputHatch.getTank().getFluidAmount() + 10 <= outputHatch.getTank().getCapacity())){
                                    outputHatch.getTank().fillInternal(water, true);
                                }
                            }
                            dynamoHatch.addEnergy(production);
                            if (ticker >= 80) {
                                if (this.getStackInSlot(0).attemptDamageItem(1, world.rand, null)) {
                                    this.getStackInSlot(0).shrink(1);
                                }
                                ticker = 0;
                            }
                        }
                    } else if (inputFluid2 != null && inputFluid2.isFluidEqual(compare) && inputHatch2.getTank().getFluidAmount() >= fluidAmount){
                        if (!this.getActive()){
                            this.setActive(true);
                            this.setRingActive(true);
                        }
                        inputHatch2.getTank().drainInternal(fluidAmount, true);
                        if (outputHatch != null && outputHatch.getCycle().isFluid()){
                            //noinspection ConstantConditions
                            if (outputHatch.getTank().getFluidAmount() == 0 || (outputHatch.getTank().getFluid().isFluidEqual(water) && outputHatch.getTank().getFluidAmount() + 10 <= outputHatch.getTank().getCapacity())){
                                outputHatch.getTank().fillInternal(water, true);
                            }
                        }
                        dynamoHatch.addEnergy(production);
                        if (ticker >= 80){
                            if (this.getStackInSlot(0).attemptDamageItem(1, world.rand, null)){
                                this.getStackInSlot(0).shrink(1);
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
            } else {
                if (this.getActive()){
                    this.setActive(false);
                    this.setRingActive(false);
                }
            }
        } else {
            if (inputHatch1 != null) inputHatch1 = null;
            if (inputHatch2 != null) inputHatch2 = null;
            if (dynamoHatch != null) dynamoHatch = null;
            if (outputHatch != null) outputHatch = null;
            production = 0;
            if (this.getActive()){
                this.setActive(false);
                this.setRingActive(false);
            }
        }
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
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerLargeSteamTurbine(entityPlayer.inventory, this);
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
        this.input1 = this.getPos();
        this.input2 = this.getPos();
        this.dynamo = this.getPos();
        this.output = this.getPos();
        int3 dir = new int3(getPos(), getFacing());
        if (!isStandardCasingWithSpecial(dir.up(1), 2)){
            return false;
        }
        if (!isStandardCasingWithSpecial(dir.right(1), 3)){
            return false;
        }
        if (!isStandardCasingWithSpecial(dir.down(1), 5)){
            return false;
        }
        if (!isStandardCasingWithSpecial(dir.down(1), 8)){
            return false;
        }
        if (!isStandardCasingWithSpecial(dir.left(1), 7)){
            return false;
        }
        if (!isStandardCasingWithSpecial(dir.left(1), 6)){
            return false;
        }
        if (!isStandardCasingWithSpecial(dir.up(1), 4)){
            return false;
        }
        if (!isStandardCasingWithSpecial(dir.up(1), 1)){
            return false;
        }

        int i;
        for (i = 0; i < 3; i++){
            if (!isStandardCasing(dir.back(1))){
                return false;
            }
        }
        if (!isStandardCasing(dir.right(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isInputOutputHatch(dir.forward(1))){
                return false;
            }
        }
        if (!isStandardCasing(dir.right(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isStandardCasing(dir.back(1))){
                return false;
            }
        }
        if (!isStandardCasing(dir.down(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isInputOutputHatch(dir.forward(1))){
                return false;
            }
        }
        if (world.getBlockState(dir.left(1).asBlockPos()) != Blocks.AIR.getDefaultState()){
            return false;
        }
        if (world.getBlockState(dir.back(1).asBlockPos()) != Blocks.AIR.getDefaultState()){
            return false;
        }
        if (!isDynamoHatch(dir.back(1))){
            return false;
        }
        if (!isStandardCasing(dir.left(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isInputOutputHatch(dir.forward(1))){
                return false;
            }
        }
        if (!isStandardCasing(dir.down(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isStandardCasing(dir.back(1))){
                return false;
            }
        }
        if (!isStandardCasing(dir.right(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isInputOutputHatch(dir.forward(1))){
                return false;
            }
        }
        if (!isStandardCasing(dir.right(1))){
            return false;
        }
        for (i = 0; i < 2; i++){
            if (!isStandardCasing(dir.back(1))){
                return false;
            }
        }

        if (inputs < 1){
            return false;
        }
        return true;
    }

    public void removeRing(int3 dir){
        removeStandardCasingWithSpecial(dir.up(1));
        removeStandardCasingWithSpecial(dir.right(1));
        removeStandardCasingWithSpecial(dir.down(1));
        removeStandardCasingWithSpecial(dir.down(1));
        removeStandardCasingWithSpecial(dir.left(1));
        removeStandardCasingWithSpecial(dir.left(1));
        removeStandardCasingWithSpecial(dir.up(1));
        removeStandardCasingWithSpecial(dir.up(1));
    }

    public void addRing(){
        int3 dir = new int3(this.pos, this.getFacing());
        setStandardCasingWithSpecial(dir.up(1), 2);
        setStandardCasingWithSpecial(dir.right(1), 3);
        setStandardCasingWithSpecial(dir.down(1), 5);
        setStandardCasingWithSpecial(dir.down(1), 8);
        setStandardCasingWithSpecial(dir.left(1), 7);
        setStandardCasingWithSpecial(dir.left(1), 6);
        setStandardCasingWithSpecial(dir.up(1), 4);
        setStandardCasingWithSpecial(dir.up(1), 1);
    }

    public boolean isStandardCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == standardCasingState;
    }

    public boolean isStandardCasingWithSpecial(int3 pos, int position) {
        IBlockState state = world.getBlockState(pos.asBlockPos());
        if (state == standardCasingState){
            TileEntity tile = world.getTileEntity(pos.asBlockPos());
            if (tile instanceof GTCXTileCasing){
                GTCXTileCasing  casing = (GTCXTileCasing) tile;
                casing.setFacing(this.getFacing());
                casing.setRotor(position);
            }
            return true;
        }
        return false;
    }

    public void setStandardCasingWithSpecial(int3 pos, int position) {
        IBlockState state = world.getBlockState(pos.asBlockPos());
        if (state == standardCasingState){
            TileEntity tile = world.getTileEntity(pos.asBlockPos());
            if (tile instanceof GTCXTileCasing){
                GTCXTileCasing  casing = (GTCXTileCasing) tile;
                casing.setFacing(this.getFacing());
                casing.setRotor(position);
            }
        }
    }

    public void removeStandardCasingWithSpecial(int3 pos) {
        IBlockState state = world.getBlockState(pos.asBlockPos());
        if (state == standardCasingState){
            TileEntity tile = world.getTileEntity(pos.asBlockPos());
            if (tile instanceof GTCXTileCasing){
                ((GTCXTileCasing) tile).setRotor(0);
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
            } else if (world.getBlockState(input2) != inputHatchState){
                input2 = pos.asBlockPos();
            }
            inputs++;
            return true;
        }
        if (world.getBlockState(pos.asBlockPos()) == outputHatchState){
            if (world.getBlockState(output) != outputHatchState){
                output = pos.asBlockPos();
            }
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == standardCasingState;
    }
    public boolean isDynamoHatch(int3 pos) {
        if (world.getBlockState(pos.asBlockPos()) == dynamoHatchState){
            dynamo = pos.asBlockPos();
            return true;
        }
        return false;
    }

    @Override
    public void onBlockBreak() {
        if (controlHatch != null){
            controlHatch.setOwner(null);
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
        return this.isActive ? production : 0;
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    @Override
    public void setShouldCheckRecipe(boolean checkRecipe) {

    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        boolean enoughSteam = inputHatch1 != null && ((inputHatch2 != null && inputHatch1.getTank().getFluidAmount() + inputHatch2.getTank().getFluidAmount() >= 1600) || (inputHatch1.getTank().getFluidAmount() >= 1600));
        map.put("Has Enough Steam: " + enoughSteam, true);
        map.put("Dynamo Hatch has enough room: " + (dynamoHatch != null && dynamoHatch.getStoredEnergy() + production <= dynamoHatch.getMaxEnergyStorage()), true);
        map.put("Disabled: " + disabled, true);
        map.put("Input Hatch 1 amount: " + (inputHatch1 != null ? inputHatch1.getTank().getFluidAmount() : 0), true);
        map.put("Input Hatch 2 amount: " + (inputHatch2 != null ? inputHatch2.getTank().getFluidAmount() : 0), true);
    }
}
