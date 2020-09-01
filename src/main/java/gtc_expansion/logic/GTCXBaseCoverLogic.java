package gtc_expansion.logic;

import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class GTCXBaseCoverLogic {
    GTCXTileBasePipe pipe;
    public GTCXBaseCoverLogic(GTCXTileBasePipe pipe){
        this.pipe = pipe;
    }
    public abstract void onTick();

    public abstract boolean cycleMode(EntityPlayer player);

    public void readFromNBT(NBTTagCompound nbt) {


    }

    public void writeToNBT(NBTTagCompound nbt) {

    }

    public void read(IInputBuffer buffer) {

    }

    public void write(IOutputBuffer buffer) {

    }
}
