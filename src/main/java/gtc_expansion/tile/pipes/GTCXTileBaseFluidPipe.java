package gtc_expansion.tile.pipes;

import ic2.api.classic.network.adv.NetworkField;
import ic2.core.fluid.IC2Tank;
import net.minecraft.block.BlockCauldron;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Map;

public class GTCXTileBaseFluidPipe extends GTCXTileBasePipe {
    @NetworkField(index = 7)
    IC2Tank tank = new IC2Tank(1);

    EnumFacing receivedFrom = null;

    int transferRate;
    public GTCXTileBaseFluidPipe() {
        super(0);
        this.transferRate = 0;
        this.addNetworkFields("tank");
    }

    @Override
    public void update() {
        super.update();
        if (this.isSimulating()) {

            int pipes = 0;
            int others;
            for (EnumFacing facing : connection) {
                if (anchors.notContains(facing) || storage.getCoverLogicMap().get(facing).allowsInput()) {
                    TileEntity tile = world.getTileEntity(this.getPos().offset(facing));
                    if (tile instanceof GTCXTileBaseFluidPipe) {
                        pipes++;
                    }
                }
            }
        }
    }

    public void distribute(DelegatorTileEntity<IFluidHandler>[] aAdjacentTanks) {
        ArrayListNoNulls<DelegatorTileEntity<IFluidHandler>> tAdjacentTanks = new ArrayListNoNulls<>(), tAdjacentPipes = new ArrayListNoNulls<>();
        DelegatorTileEntity<IFluidHandler> tTank;
        int amount = tank.getFluidAmount();
        if (amount <= 0) return;
        byte tPipeCount = 1;

        for (byte side : ALL_SIDES_VALID) {
            if (aAdjacentTanks[side] != null && !FACE_CONNECTED[side][mLastReceivedFrom] && (!hasCovers() || mCovers.mBehaviours[side] == null || !mCovers.mBehaviours[side].interceptFluidDrain(side, mCovers, side, tank.get()))) {
                tTank = aAdjacentTanks[side];
                if (tTank.mTileEntity == null) {
                    if (tTank.getBlock() instanceof BlockCauldron && tank.amount() >= 334 && FL.water(tank.get())) {
                        switch(tTank.getMetaData()) {
                            case 0:
                                if (tank.drainAll(1000)) {tTank.setMetaData(3); break;}
                                if (tank.drainAll( 667)) {tTank.setMetaData(2); break;}
                                if (tank.drainAll( 334)) {tTank.setMetaData(1); break;}
                                break;
                            case 1:
                                if (tank.drainAll( 667)) {tTank.setMetaData(3); break;}
                                if (tank.drainAll( 334)) {tTank.setMetaData(2); break;}
                                break;
                            case 2:
                                if (tank.drainAll( 334)) {tTank.setMetaData(3); break;}
                                break;
                        }
                    }
                } else {
                    if (tTank.mTileEntity instanceof MultiTileEntityPipeFluid) {
                        FluidTankGT tTarget = (FluidTankGT)((MultiTileEntityPipeFluid)tTank.mTileEntity).getFluidTankFillable2(tTank.mSideOfTileEntity, tank.get());
                        if (tTarget != null && tTarget.amount() < tank.amount()) {
                            amount += tTarget.amount();
                            tPipeCount++;
                            tAdjacentTanks.add(tTank);
                        }
                    } else if (FL.fill_(tTank, tank.get(), F) > 0) {
                        tAdjacentTanks.add(tTank);
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

        if (amount > 0) for (int i = tAdjacentTanks.size(); i > 0 && tank.amount() > 0;) {
            tTank = tAdjacentTanks.get(--i);
            if (tTank.mTileEntity instanceof MultiTileEntityPipeFluid) {
                tAdjacentTanks.remove(i);
                if (!((MultiTileEntityPipeFluid)tTank.mTileEntity).hasCovers() || ((MultiTileEntityPipeFluid)tTank.mTileEntity).mCovers.mBehaviours[tTank.mSideOfTileEntity] == null || !((MultiTileEntityPipeFluid)tTank.mTileEntity).mCovers.mBehaviours[tTank.mSideOfTileEntity].interceptFluidFill(tTank.mSideOfTileEntity, ((MultiTileEntityPipeFluid)tTank.mTileEntity).mCovers, tTank.mSideOfTileEntity, tank.get())) {
                    tAdjacentPipes.add(tTank);
                    FluidTankGT tTarget = (FluidTankGT)((MultiTileEntityPipeFluid)tTank.mTileEntity).getFluidTankFillable2(tTank.mSideOfTileEntity, tank.get());
                    if (tTarget != null) {
                        mTransferredAmount += tank.remove(FL.fill_(tTank, tank.get(amount-tTarget.amount()), T));
                    }
                }
            }
        }

        if (!tAdjacentTanks.isEmpty()) {
            amount = tank.amount() / tAdjacentTanks.size();
            if (amount <= 0) {
                while (tank.amount() > 0 && !tAdjacentTanks.isEmpty()) {
                    tAdjacentTanks.remove(tTank = tAdjacentTanks.get(rng(tAdjacentTanks.size())));
                    mTransferredAmount += tank.remove(FL.fill_(tTank, tank.get(1), T));
                }
            } else {
                for (DelegatorTileEntity<IFluidHandler> tTank2 : tAdjacentTanks) {
                    mTransferredAmount += tank.remove(FL.fill_(tTank2, tank.get(amount), T));
                }
            }
        }

        if (!tAdjacentPipes.isEmpty() && tank.amount() > mCapacity / 2) {
            amount = (tank.amount() - mCapacity / 2) / tAdjacentPipes.size();
            if (amount > 0) {
                for (DelegatorTileEntity<IFluidHandler> tPipe : tAdjacentPipes) {
                    mTransferredAmount += tank.remove(FL.fill_(tPipe, tank.get(amount), T));
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
