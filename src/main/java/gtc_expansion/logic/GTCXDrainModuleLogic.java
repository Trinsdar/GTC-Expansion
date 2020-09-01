package gtc_expansion.logic;

import gtc_expansion.GTCExpansion;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GTCXDrainModuleLogic extends GTCXBaseCoverLogic {
    int test = 0;
    public GTCXDrainModuleLogic(GTCXTileBasePipe pipe) {
        super(pipe);
    }

    @Override
    public void onTick() {
        GTCExpansion.logger.info("Drain test: " + test);
    }

    @Override
    public boolean cycleMode(EntityPlayer player) {
        if (test == 0){
            test = 1;
        }
        return false;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        test = nbt.getInteger("test");
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("test", test);
    }

    public void read(IInputBuffer buffer) {
        this.test = buffer.readInt();
    }

    public void write(IOutputBuffer buffer) {
        buffer.writeInt(test);
    }
}
