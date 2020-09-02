package gtc_expansion.tile.pipes;

import ic2.api.classic.network.adv.NetworkField;
import ic2.core.fluid.IC2Tank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.Map;

public class GTCXTileBaseFluidPipe extends GTCXTileBasePipe {
    @NetworkField(index = 7)
    IC2Tank tank = new IC2Tank(1);

    public byte mLastReceivedFrom = 0, oLastReceivedFrom = 0;

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
            mLastReceivedFrom &= 63;
            if (mLastReceivedFrom == 63) {
                mLastReceivedFrom = 0;
            }

            if (mLastReceivedFrom == oLastReceivedFrom) {
                for (EnumFacing facing : connection){
                    if (anchors.notContains(facing) || storage.getCoverLogicMap().get(facing).allowsInput()){

                    }
                }
                /*HashMap<IFluidHandler, ForgeDirection> tTanks = new HashMap<IFluidHandler, ForgeDirection>();

                mConnections = 0;

                for (byte i = 0; i < 6; i++) {
                    IFluidHandler tTileEntity = getBaseMetaTileEntity().getITankContainerAtSide(i);
                    if (tTileEntity != null) {
                        if (tTileEntity instanceof IGregTechTileEntity) {
                            if (getBaseMetaTileEntity().getColorization() >= 0) {
                                byte tColor = ((IGregTechTileEntity)tTileEntity).getColorization();
                                if (tColor >= 0 && tColor != getBaseMetaTileEntity().getColorization()) {
                                    continue;
                                }
                            }
                        }
                        FluidTankInfo[] tInfo = tTileEntity.getTankInfo(ForgeDirection.getOrientation(i).getOpposite());
                        if (tInfo != null && tInfo.length > 0) {
                            if (getBaseMetaTileEntity().getCoverBehaviorAtSide(i).letsLiquidIn(i, getBaseMetaTileEntity().getCoverIDAtSide(i), getBaseMetaTileEntity().getCoverDataAtSide(i), getBaseMetaTileEntity())) {
                                mConnections |= (1<<i);
                            }
                            if (getBaseMetaTileEntity().getCoverBehaviorAtSide(i).letsLiquidOut(i, getBaseMetaTileEntity().getCoverIDAtSide(i), getBaseMetaTileEntity().getCoverDataAtSide(i), getBaseMetaTileEntity())) {
                                mConnections |= (1<<i);
                                if (((1<<i) & mLastReceivedFrom) == 0) tTanks.put(tTileEntity, ForgeDirection.getOrientation(i).getOpposite());
                            }
                        }
                    }
                }

                int tAmount = Math.min(getFluidCapacityPerTick()*20, mFluid==null?0:mFluid.amount) / 2;

                if (tTanks.size() > 0) {
                    if (tAmount >= tTanks.size()) {
                        for (IFluidHandler tTileEntity : tTanks.keySet()) {
                            int tFilledAmount = tTileEntity.fill(tTanks.get(tTileEntity), drain(tAmount / tTanks.size(), false), false);
                            if (tFilledAmount > 0) {
                                tTileEntity.fill(tTanks.get(tTileEntity), drain(tFilledAmount, true), true);
                            }
                        }
                    } else {
                        if (mFluid != null && mFluid.amount > 0) {
                            for (IFluidHandler tTileEntity : tTanks.keySet()) {
                                int tFilledAmount = tTileEntity.fill(tTanks.get(tTileEntity), drain(mFluid.amount, false), false);
                                if (tFilledAmount > 0) {
                                    tTileEntity.fill(tTanks.get(tTileEntity), drain(tFilledAmount, true), true);
                                }
                                if (mFluid == null || mFluid.amount <= 0) break;
                            }
                        }
                    }
                }*/

                mLastReceivedFrom = 0;
            }

            oLastReceivedFrom = mLastReceivedFrom;
        }
    }


    public GTCXTileBaseFluidPipe setTransferRate(int transferRate){
        this.transferRate = transferRate;
        tank.setCapacity(transferRate);
        return this;
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
        mLastReceivedFrom = nbt.getByte("mLastReceivedFrom");
        this.tank.readFromNBT(nbt.getCompoundTag("tank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("mLastReceivedFrom", mLastReceivedFrom);
        this.tank.writeToNBT(this.getTag(nbt, "tank"));
        return nbt;
    }
}
