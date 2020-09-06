package gtc_expansion.tile.pipes;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GTCXTileBaseFluidPipe extends GTCXTileBasePipe {
    @NetworkField(index = 7)
    IC2Tank tank = new IC2Tank(1);

    EnumFacing receivedFrom = null;

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
    }

    public void distribute() {
        transferredAmount = 0;
        List<Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing>> tAdjacentTanks = new ArrayList<>(), tAdjacentPipes = new ArrayList<>();
        IFluidHandler tTank;
        int amount = tank.getFluidAmount();
        if (amount <= 0) return;
        byte tPipeCount = 1;

        for (EnumFacing side : connection){
            if (anchors.notContains(side) || storage.getCoverLogicMap().get(side).allowsPipeOutput()) {
                TileEntity tile = world.getTileEntity(this.getPos().offset(side));
                if (tile instanceof GTCXTileBaseFluidPipe) {
                    IC2Tank target = ((GTCXTileBaseFluidPipe)tile).getTank();
                    if (target.getFluid() == null || (target.getFluid().isFluidEqual(this.tank.getFluid()) && target.getFluidAmount() < tank.getFluidAmount())){
                        amount += target.getFluidAmount();
                        tPipeCount++;
                        tAdjacentTanks.add(new Tuple<>(new Tuple<>(target, tile), side.getOpposite()));
                    }
                } else if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())){
                    if (tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).fill(tank.getFluid(), false) > 0){
                        tAdjacentTanks.add(new Tuple<>(new Tuple<>(tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()), tile), side.getOpposite()));
                    }
                }
            }
        }

        if (amount % tPipeCount == 0) {
            amount /= tPipeCount;
        } else {
            amount /= tPipeCount;
            amount++;
        }

        if (amount > 0) {
            for (int i = tAdjacentTanks.size(); i > 0 && tank.getFluidAmount() > 0;) {
                Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing> tuple = tAdjacentTanks.get(--i);
                tTank = tuple.getFirst().getFirst();
                if (tuple.getFirst().getSecond() instanceof GTCXTileBaseFluidPipe) {
                    tAdjacentTanks.remove(i);
                    if (!((GTCXTileBaseFluidPipe)tuple.getFirst().getSecond()).storage.getCoverLogicMap().get(tuple.getSecond()).allowsPipeInput()){
                        continue;
                    }
                    tAdjacentPipes.add(new Tuple<>(new Tuple<>(tTank, tuple.getFirst().getSecond()), tuple.getSecond()));
                    IC2Tank target = ((GTCXTileBaseFluidPipe)tuple.getFirst().getSecond()).getTank();

                    if (target.getFluid() == null || target.getFluid().isFluidEqual(tank.getFluid())) {
                        FluidStack fluid = tank.drain(target.fill(tank.getFluid(), true), true);
                        if (fluid != null){
                            transferredAmount += fluid.amount;
                        }
                    }
                }
            }
        }

        if (!tAdjacentTanks.isEmpty()) {
            amount = tank.getFluidAmount() / tAdjacentTanks.size();
            if (amount <= 0) {
                while (tank.getFluidAmount() > 0 && !tAdjacentTanks.isEmpty()) {
                    int i = random.nextInt(tAdjacentTanks.size());
                    tTank = tAdjacentTanks.get(i).getFirst().getFirst();
                    tAdjacentTanks.remove(tAdjacentTanks.get(i));
                    FluidStack fluid = tank.drain(tTank.fill(tank.getFluid(), true), true);
                    if (fluid != null){
                        transferredAmount += fluid.amount;
                    }
                }
            } else {
                for (Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing> tuple : tAdjacentTanks) {
                    IFluidHandler tTank2 = tuple.getFirst().getFirst();
                    FluidStack fluid = tank.drain(tTank2.fill(tank.getFluid(), true), true);
                    if (fluid != null){
                        transferredAmount += fluid.amount;
                    }
                }
            }
        }

        if (!tAdjacentPipes.isEmpty() && tank.getFluidAmount() > tank.getCapacity() / 2) {
            amount = (tank.getFluidAmount() - tank.getCapacity() / 2) / tAdjacentPipes.size();
            if (amount > 0) {
                for (Tuple<Tuple<IFluidHandler, TileEntity>, EnumFacing> tuple : tAdjacentPipes) {
                    IFluidHandler tPipe = tuple.getFirst().getFirst();
                    FluidStack fluid = tank.drain(tPipe.fill(tank.getFluid(), true), true);
                    if (fluid != null){
                        transferredAmount += fluid.amount;
                    }
                }
            }
        }
    }


    public GTCXTileBaseFluidPipe setTransferRate(int transferRate){
        this.transferRate = transferRate;
        tank.setCapacity(transferRate);
        return this;
    }

    public void setReceivedFrom(EnumFacing receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    @Override
    public boolean canConnectWithoutColor(TileEntity tile, EnumFacing facing) {
        return tile instanceof GTCXTileBaseFluidPipe || tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
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
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.tank.setCapacity(nbt.getInteger("TankCapacity"));
        this.tank.readFromNBT(nbt.getCompoundTag("tank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("TankCapacity", tank.getCapacity());
        this.tank.writeToNBT(this.getTag(nbt, "tank"));
        return nbt;
    }
}
