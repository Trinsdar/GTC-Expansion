package gtc_expansion.tile.pipes;

import ic2.api.classic.network.adv.NetworkField;
import ic2.core.fluid.IC2Tank;
import net.minecraft.nbt.NBTTagCompound;

public class GTCXTileQuadFluidPipe extends GTCXTileBaseFluidPipe {
    @NetworkField(index = 8)
    IC2Tank tank2 = new PipeTank(1);
    @NetworkField(index = 9)
    IC2Tank tank3 = new PipeTank(1);
    @NetworkField(index = 10)
    IC2Tank tank4 = new PipeTank(1);

    public GTCXTileQuadFluidPipe(){
        super();
        this.tank2.setCanDrain(false);
        this.tank3.setCanDrain(false);
        this.tank4.setCanDrain(false);
        this.addNetworkFields("tank2", "tank3", "tank4");
    }

    @Override
    public IC2Tank[] getTanks() {
        return new IC2Tank[]{this.tank, this.tank2, this.tank3, this.tank4};
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.tank2.readFromNBT(nbt.getCompoundTag("tank2"));
        this.tank3.readFromNBT(nbt.getCompoundTag("tank3"));
        this.tank4.readFromNBT(nbt.getCompoundTag("tank4"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.tank2.writeToNBT(this.getTag(nbt, "tank2"));
        this.tank3.writeToNBT(this.getTag(nbt, "tank3"));
        this.tank4.writeToNBT(this.getTag(nbt, "tank4"));
        return nbt;
    }
}
