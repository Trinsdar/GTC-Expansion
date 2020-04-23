package gtc_expansion.tile;

import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.nbt.NBTTagCompound;

public class GTCXTileCasing extends TileEntityBlock {
    public int rotor;
    public GTCXTileCasing(){
        super();
        rotor = 0;
    }

    public void setRotor(int rotor) {
        this.rotor = rotor;
    }

    public int getRotor() {
        return rotor;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("rotor", rotor);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        rotor = nbt.getInteger("rotor");
    }
}
