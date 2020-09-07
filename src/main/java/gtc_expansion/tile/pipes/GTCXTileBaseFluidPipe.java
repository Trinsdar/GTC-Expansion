package gtc_expansion.tile.pipes;

import gtc_expansion.GTCExpansion;
import gtclassic.common.tile.GTTileTranslocatorFluid;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.fluid.IC2Tank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gtc_expansion.data.GTCXValues.FACE_CONNECTED;
import static gtc_expansion.data.GTCXValues.SBIT;

public class GTCXTileBaseFluidPipe extends GTCXTileBasePipe {
    @NetworkField(index = 7)
    IC2Tank tank = new IC2Tank(1);

    int receivedFrom = 0;

    int transferRate, transferredAmount = 0;
    public GTCXTileBaseFluidPipe() {
        super(0);
        this.transferRate = 0;
        this.addNetworkFields("tank");
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void onTick() {
        super.onTick();
        distribute();
        receivedFrom = 0;
    }

    public void distribute() {
        transferredAmount = 0;
        List<Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing>> adjacentTanks = new ArrayList<>(), adjacentPipes = new ArrayList<>();
        IFluidHandler tTank;
        int amount = tank.getFluidAmount();
        if (amount <= 0) return;
        byte pipeCount = 1;

        for (EnumFacing side : connection){
            if ((anchors.notContains(side) || storage.getCoverLogicMap().get(side).allowsPipeOutput()) && !FACE_CONNECTED[side.getIndex()][receivedFrom]) {
                TileEntity tile = world.getTileEntity(this.getPos().offset(side));
                if (tile instanceof GTCXTileBaseFluidPipe) {
                    IC2Tank target = ((GTCXTileBaseFluidPipe)tile).getTank();
                    if ((target.getFluid() != null && target.getFluid().isFluidEqual(this.tank.getFluid()) && target.getFluidAmount() < tank.getFluidAmount()) || target.getFluid() == null){
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
                    IC2Tank target = ((GTCXTileBaseFluidPipe)tuple.getFirst().getSecond()).getTank();

                    if (target.getFluid() == null || target.getFluid().isFluidEqual(tank.getFluid())) {
                        FluidStack fluid = tank.drain(tTank.fill(get(amount - target.getFluidAmount()), true), true);
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
                    FluidStack fluid = tank.drain(tTank.fill(get(1), true), true);
                    if (fluid != null){
                        transferredAmount += fluid.amount;
                    }
                }
            } else {
                for (Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing> tuple : adjacentTanks) {
                    IFluidHandler tTank2 = tuple.getFirst().getFirst();
                    FluidStack fluid = tank.drain(tTank2.fill(get(amount), true), true);
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
                    FluidStack fluid = tank.drain(tPipe.fill(get(amount), true), true);
                    if (fluid != null){
                        transferredAmount += fluid.amount;
                    }
                }
            }
        }
    }

    private FluidStack get(int amount){
        if (this.tank.getFluid() != null && amount > 0){
            return new FluidStack(this.tank.getFluid().getFluid(), (Math.min(this.tank.getFluidAmount(), amount)));
        }
        return null;
    }


    public GTCXTileBaseFluidPipe setTransferRate(int transferRate){
        this.transferRate = transferRate;
        tank.setCapacity(transferRate * 2);
        return this;
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

    @Override
    public void getData(Map<String, Boolean> map) {
        super.getData(map);
        map.put("Tank capacity: " + tank.getCapacity(), true);
        map.put("Fluid in Tank: " + (this.tank.getFluid() != null ? (this.tank.getFluidAmount() + " mb of " + this.tank.getFluid().getLocalizedName()) : "Empty"), false);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return super.hasCapability(capability, facing) || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            if (facing != null){
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FacingFillWrapper(facing, this));
            }
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.tank.setCapacity(nbt.getInteger("TankCapacity"));
        this.tank.readFromNBT(nbt.getCompoundTag("tank"));
        this.receivedFrom = nbt.getInteger("receivedFrom");
        this.transferRate = nbt.getInteger("transferRate");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("TankCapacity", tank.getCapacity());
        this.tank.writeToNBT(this.getTag(nbt, "tank"));
        nbt.setInteger("receivedFrom", this.receivedFrom);
        nbt.setInteger("transferRate", this.transferRate);
        return nbt;
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
            return this.tank.getTankProperties();
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (doFill) {
                pipe.receivedFrom |= SBIT[this.facing.getIndex()];
            }
            return this.tank.fill(resource, doFill);
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
}
