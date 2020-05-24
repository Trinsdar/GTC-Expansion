package gtc_expansion.tile.multi;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.GTCXItems;
import gtc_expansion.container.GTCXContainerLargeSteamTurbine;
import gtc_expansion.tile.GTCXTileCasing;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch.GTCXTileDynamoHatch;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileInputHatch;
import gtclassic.api.helpers.int3;
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

public class GTCXTileMultiLargeSteamTurbine extends TileEntityMachine implements ITickable, IHasGui, IGTMultiTileStatus, INetworkClientTileEntityEventListener, INetworkTileEntityEventListener {
    public boolean lastState;
    public boolean firstCheck = true;
    private BlockPos input1;
    private BlockPos input2;
    private BlockPos dynamo;
    int production;
    int ticker = 0;
    public static final IBlockState standardCasingState = GTCXBlocks.casingStandard.getDefaultState();
    public static final IBlockState inputHatchState = GTCXBlocks.inputHatch.getDefaultState();
    public static final IBlockState dynamoHatchState = GTCXBlocks.dynamoHatch.getDefaultState();

    public GTCXTileMultiLargeSteamTurbine() {
        super(1);
        this.addGuiFields("lastState");
        input1 = this.getPos();
        input2 = this.getPos();
        dynamo = this.getPos();
        production = 0;
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.lastState = nbt.getBoolean("lastState");
        this.ticker = nbt.getInteger("ticker");
        this.input1 = readBlockPosFromNBT(nbt, "input1");
        this.input2 = readBlockPosFromNBT(nbt, "input2");
        this.dynamo = readBlockPosFromNBT(nbt, "dynamo");
        this.production = nbt.getInteger("production");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("lastState", this.lastState);
        nbt.setInteger("ticker", ticker);
        writeBlockPosToNBT(nbt, "input1", input1);
        writeBlockPosToNBT(nbt, "input2", input2);
        writeBlockPosToNBT(nbt, "dynamo", dynamo);
        nbt.setInteger("production", production);
        return nbt;
    }

    public void onBlockRemoved() {
        removeRing(new int3(getPos(), getFacing()));
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
        boolean canWork = canWork() && world.getTileEntity(input1) instanceof GTCXTileInputHatch && world.getTileEntity(dynamo) instanceof GTCXTileDynamoHatch;
        if (canWork && isTurbineRotor(this.getStackInSlot(0))){
            GTCXTileInputHatch inputHatch = (GTCXTileInputHatch) world.getTileEntity(input1);
            GTCXTileDynamoHatch dynamoHatch = (GTCXTileDynamoHatch) world.getTileEntity(dynamo);
            production = (int)(800 * getRotorEfficiency(this.getStackInSlot(0)));
            if (inputHatch.getTank().getFluidAmount() >= 8000 && inputHatch.getTank().getFluid().isFluidEqual(GTMaterialGen.getFluidStack("steam", 8000))){
                if (!this.getActive()){
                    this.setActive(true);
                    this.setRingActive(true);
                }
                inputHatch.getTank().drainInternal(8000, true);
                dynamoHatch.addEnergy(production);
                if (ticker >= 80){
                    this.getStackInSlot(0).attemptDamageItem(1, world.rand, null);
                    ticker = 0;
                }
            } else if (world.getTileEntity(input2) instanceof GTCXTileInputHatch){
                GTCXTileInputHatch inputHatch2 = (GTCXTileInputHatch) world.getTileEntity(input2);
                if (inputHatch2.getTank().getFluidAmount() >= 8000 && inputHatch2.getTank().getFluid().isFluidEqual(GTMaterialGen.getFluidStack("steam", 8000))){
                    if (!this.getActive()){
                        this.setActive(true);
                        this.setRingActive(true);
                    }
                    inputHatch2.getTank().drainInternal(8000, true);
                    dynamoHatch.addEnergy(production);
                    if (ticker >= 80){
                        this.getStackInSlot(0).attemptDamageItem(1, world.rand, null);
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
            if (!isStandardCasing(dir.forward(1))){
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
            if (!isInputHatch(dir.forward(1))){
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
            if (!isInputHatch(dir.forward(1))){
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
            if (!isInputHatch(dir.forward(1))){
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

    public boolean isInputHatch(int3 pos) {
        if (world.getBlockState(pos.asBlockPos()) == inputHatchState){
            if (world.getBlockState(input1) != inputHatchState){
                input1 = pos.asBlockPos();
            } else if (world.getBlockState(input2) != inputHatchState){
                input2 = pos.asBlockPos();
            }
            inputs++;
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
}
