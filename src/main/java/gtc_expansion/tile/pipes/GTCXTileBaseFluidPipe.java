package gtc_expansion.tile.pipes;

import gtc_expansion.logic.GTCXFluidFilterLogic;
import gtc_expansion.logic.GTCXShutterLogic;
import gtc_expansion.util.GTCXWrenchUtils;
import gtclassic.common.tile.GTTileTranslocatorFluid;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.fluid.IC2Tank;
import ic2.core.util.obj.IClickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static gtc_expansion.data.GTCXValues.FACE_CONNECTED;
import static gtc_expansion.data.GTCXValues.SBIT;

public class GTCXTileBaseFluidPipe extends GTCXTileBasePipe implements IClickable {
    @NetworkField(index = 7)
    IC2Tank tank = new PipeTank(1);

    int receivedFrom = 0;

    int transferRate, transferredAmount = 0;
    public GTCXTileBaseFluidPipe() {
        super(0);
        this.tank.setCanDrain(false);
        this.transferRate = 0;
        this.addNetworkFields("tank");
    }

    @Override
    public void onTick() {
        super.onTick();
        distribute();
        receivedFrom = 0;
    }

    public void distribute(){
        transferredAmount = 0;
        for (IC2Tank tank : this.getTanks()){
            this.distribute(tank);
        }
    }

    public void distribute(IC2Tank tank) {
        List<Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing>> adjacentTanks = new ArrayList<>(), adjacentPipes = new ArrayList<>();
        IFluidHandler tTank;
        int amount = tank.getFluidAmount();
        if (amount <= 0) return;
        byte pipeCount = 1;

        for (EnumFacing side : connection){
            if ((anchors.notContains(side) || storage.getCoverLogicMap().get(side).allowsPipeOutput()) && !FACE_CONNECTED[side.getIndex()][receivedFrom]) {
                if (storage.getCoverLogicMap().get(side) instanceof GTCXFluidFilterLogic && !((GTCXFluidFilterLogic)storage.getCoverLogicMap().get(side)).allowsPipeOutput(tank.getFluid())){
                    continue;
                }
                TileEntity tile = world.getTileEntity(this.getPos().offset(side));
                if (tile instanceof GTCXTileBaseFluidPipe) {
                    GTCXTileBaseFluidPipe pipe = (GTCXTileBaseFluidPipe) tile;
                    if (pipe.storage.getCoverLogicMap().get(side.getOpposite()) instanceof GTCXFluidFilterLogic && !((GTCXFluidFilterLogic)pipe.storage.getCoverLogicMap().get(side.getOpposite())).matches(tank.getFluid())){
                        continue;
                    }
                    IC2Tank target = ((GTCXTileBaseFluidPipe)tile).getFluidTankFillable2(tank.getFluid());
                    if (target != null && target.getFluidAmount() < tank.getFluidAmount()){
                        amount += target.getFluidAmount();
                        pipeCount++;
                        adjacentTanks.add(new Tuple<>(new Tuple<>(new FacingFillWrapper(side.getOpposite(), (GTCXTileBaseFluidPipe)tile), tile), side.getOpposite()));
                    }
                } else if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())){
                    IFluidHandler cap = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
                    if (cap != null && cap.fill(tank.getFluid(), false) > 0){
                        adjacentTanks.add(new Tuple<>(new Tuple<>(cap, tile), side.getOpposite()));
                    }
                }
            }
        }

        if (amount % pipeCount == 0) {
            amount /= pipeCount;
        } else {
            amount /= pipeCount;
            amount++;
        }

        if (amount > 0) {
            for (int i = adjacentTanks.size(); i > 0 && tank.getFluidAmount() > 0;) {
                Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing> tuple = adjacentTanks.get(--i);
                tTank = tuple.getFirst().getFirst();
                if (tuple.getFirst().getSecond() instanceof GTCXTileBaseFluidPipe) {
                    adjacentTanks.remove(i);
                    if (!((GTCXTileBaseFluidPipe)tuple.getFirst().getSecond()).storage.getCoverLogicMap().get(tuple.getSecond()).allowsPipeInput()){
                        continue;
                    }
                    adjacentPipes.add(new Tuple<>(new Tuple<>(tTank, tuple.getFirst().getSecond()), tuple.getSecond()));
                    IC2Tank target = ((GTCXTileBaseFluidPipe)tuple.getFirst().getSecond()).getFluidTankFillable2(tank.getFluid());

                    if (target != null) {
                        FluidStack fluid = tank.drainInternal(tTank.fill(get(tank,amount - target.getFluidAmount()), true), true);
                        if (fluid != null){
                            transferredAmount += fluid.amount;
                        }
                    }
                }
            }
        }

        if (!adjacentTanks.isEmpty()) {
            amount = tank.getFluidAmount() / adjacentTanks.size();
            if (amount <= 0) {
                while (tank.getFluidAmount() > 0 && !adjacentTanks.isEmpty()) {
                    int i = random.nextInt(adjacentTanks.size());
                    tTank = adjacentTanks.get(i).getFirst().getFirst();
                    adjacentTanks.remove(adjacentTanks.get(i));
                    FluidStack fluid = tank.drainInternal(tTank.fill(get(tank,1), true), true);
                    if (fluid != null){
                        transferredAmount += fluid.amount;
                    }
                }
            } else {
                for (Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing> tuple : adjacentTanks) {
                    IFluidHandler tTank2 = tuple.getFirst().getFirst();
                    FluidStack fluid = tank.drainInternal(tTank2.fill(get(tank, amount), true), true);
                    if (fluid != null){
                        transferredAmount += fluid.amount;
                    }
                }
            }
        }

        if (!adjacentPipes.isEmpty() && tank.getFluidAmount() > tank.getCapacity() / 2) {
            amount = (tank.getFluidAmount() - tank.getCapacity() / 2) / adjacentPipes.size();
            if (amount > 0) {
                for (Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing> tuple : adjacentPipes) {
                    IFluidHandler tPipe = tuple.getFirst().getFirst();
                    FluidStack fluid = tank.drainInternal(tPipe.fill(get(tank, amount), true), true);
                    if (fluid != null){
                        transferredAmount += fluid.amount;
                    }
                }
            }
        }
    }

    protected FluidStack get(IC2Tank tank, int amount){
        if (tank.getFluid() != null && amount > 0){
            return new FluidStack(tank.getFluid().getFluid(), (Math.min(tank.getFluidAmount(), amount)));
        }
        return null;
    }


    public void setTransferRate(int transferRate){
        this.transferRate = transferRate;
        for (IC2Tank tank : this.getTanks()){
            tank.setCapacity(transferRate * 2);
        }
    }

    public void setReceivedFrom(EnumFacing receivedFrom) {
        this.receivedFrom |= SBIT[receivedFrom == null ? 6 : receivedFrom.getIndex()];
    }

    @Override
    public boolean canConnectWithoutColor(TileEntity tile, EnumFacing facing) {
        return tile instanceof GTCXTileBaseFluidPipe || tile instanceof GTTileTranslocatorFluid || tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
    }

    public IC2Tank getTank() {
        return tank;
    }

    public IC2Tank[] getTanks(){
        return new IC2Tank[]{getTank()};
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        super.getData(map);
        for (IC2Tank tank : this.getTanks()){
            map.put("Tank capacity: " + tank.getCapacity(), true);
            map.put("Fluid in Tank: " + (tank.getFluid() != null ? (tank.getFluidAmount() + " mb of " + tank.getFluid().getLocalizedName()) : "Empty"), false);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null){
            return connection.contains(facing);
        }
        return super.hasCapability(capability, facing) || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            if (facing != null){
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FacingFillWrapper(facing, this));
            }
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new TankPropertyWrapper(this));
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.tank.readFromNBT(nbt.getCompoundTag("tank"));
        if (nbt.hasKey("TankCapacity") && nbt.getInteger("TankCapacity") > 0) {
            this.tank.setCapacity(nbt.getInteger("TankCapacity"));
        }
        this.receivedFrom = nbt.getInteger("receivedFrom");
        this.transferRate = nbt.getInteger("transferRate");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.tank.writeToNBT(this.getTag(nbt, "tank"));
        nbt.setInteger("receivedFrom", this.receivedFrom);
        nbt.setInteger("transferRate", this.transferRate);
        return nbt;
    }

    public IC2Tank getFluidTankFillable2(FluidStack fluidStack) {
        if (fluidStack == null) return null;
        for (IC2Tank tank : this.getTanks()){
            if (tank.getFluid() != null && tank.getFluid().isFluidEqual(fluidStack)) return tank;
        }
        for (IC2Tank tank : this.getTanks()){
            if (tank.getFluidAmount() == 0) return tank;
        }
        return null;
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
        if (enumFacing != null){
            RayTraceResult lookingAt = GTCXWrenchUtils.getBlockLookingAtIgnoreBB(entityPlayer);
            if (lookingAt != null){
                EnumFacing sideToggled = GTCXWrenchUtils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
                if (sideToggled != null && this.anchors.contains(sideToggled)){
                    if (storage.getCoverLogicMap().get(sideToggled) instanceof GTCXFluidFilterLogic){
                        boolean click = ((GTCXFluidFilterLogic)storage.getCoverLogicMap().get(sideToggled)).onRightClick(entityPlayer, enumHand, sideToggled, side);
                        return click;
                    }

                }
            }

        }
        return false;
    }

    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }

    public static class FacingFillWrapper implements IFluidHandler{
        EnumFacing facing;
        IC2Tank tank;
        GTCXTileBaseFluidPipe pipe;
        public FacingFillWrapper(EnumFacing facing, GTCXTileBaseFluidPipe pipe){
            this.facing = facing;
            this.tank = pipe.tank;
            this.pipe = pipe;
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            List<IFluidTankProperties> list = new ArrayList<>();
            for (IC2Tank tank : this.pipe.getTanks()){
                list.addAll(Arrays.asList(tank.getTankProperties()));
            }
            return list.toArray(new IFluidTankProperties[0]);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            IC2Tank tank = pipe.getFluidTankFillable2(resource);
            if (tank == null || pipe.connection.notContains(this.facing)){
                return 0;
            }
            if (pipe.storage.getCoverLogicMap().get(this.facing) instanceof GTCXFluidFilterLogic){
                GTCXFluidFilterLogic filter = (GTCXFluidFilterLogic) pipe.storage.getCoverLogicMap().get(this.facing);
                if (!filter.matches(resource)){
                    return 0;
                }
            }

            if (pipe.storage.getCoverLogicMap().get(this.facing) instanceof GTCXShutterLogic){
                GTCXShutterLogic filter = (GTCXShutterLogic) pipe.storage.getCoverLogicMap().get(this.facing);
                if (!filter.allowsPipeInput()){
                    return 0;
                }
            }
            if (doFill) {
                pipe.receivedFrom |= SBIT[this.facing.getIndex()];
            }
            return tank.fill(resource, doFill);
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return this.tank.drain(resource, doDrain);
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return this.tank.drain(maxDrain, doDrain);
        }
    }

    public static class TankPropertyWrapper implements IFluidHandler{
        GTCXTileBaseFluidPipe pipe;
        public TankPropertyWrapper(GTCXTileBaseFluidPipe pipe){
            this.pipe = pipe;
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            List<IFluidTankProperties> list = new ArrayList<>();
            for (IC2Tank tank : this.pipe.getTanks()){
                list.addAll(Arrays.asList(tank.getTankProperties()));
            }
            return list.toArray(new IFluidTankProperties[0]);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            IC2Tank tank = pipe.getFluidTankFillable2(resource);
            if (tank == null){
                return 0;
            }
            return tank.fill(resource, doFill);
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }

    public static class PipeTank extends IC2Tank{
        public PipeTank(int capacity) {
            super(capacity);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            super.writeToNBT(nbt);
            nbt.setInteger("TankCapacity", this.capacity);
            return nbt;

        }

        @Override
        public FluidTank readFromNBT(NBTTagCompound nbt) {
            super.readFromNBT(nbt);
            this.capacity = nbt.getInteger("TankCapacity");
            return this;
        }
    }
}
