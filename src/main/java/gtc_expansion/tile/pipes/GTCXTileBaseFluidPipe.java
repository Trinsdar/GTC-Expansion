package gtc_expansion.tile.pipes;

import ic2.core.fluid.IC2Tank;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class GTCXTileBaseFluidPipe extends GTCXTileBasePipe {
    IC2Tank tank = new IC2Tank(1);

    int transferRate;
    public GTCXTileBaseFluidPipe() {
        super(0);
        this.transferRate = 0;
    }

    public GTCXTileBaseFluidPipe setTransferRate(int transferRate){
        this.transferRate = transferRate;
        tank.setCapacity(transferRate);
        return this;
    }

    @Override
    public boolean canConnect(TileEntity tile, EnumFacing facing) {
        return tile instanceof GTCXTileBaseFluidPipe || tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
    }
}
