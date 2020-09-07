package gtc_expansion.logic;

import gtc_expansion.data.GTCXLang;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class GTCXRedstoneControllerLogic extends GTCXBaseCoverLogic {
    Modes mode = Modes.NORMAL;
    public GTCXRedstoneControllerLogic(GTCXTileBasePipe pipe, EnumFacing facing) {
        super(pipe, facing);
    }

    @Override
    public void onTick() {
        int newLevel = pipe.getWorld().getRedstonePower(pipe.getPos().offset(this.facing), this.facing);
        if (this.mode != Modes.NO_WORK){
            boolean redstone = newLevel > 0;
            boolean invert = (this.mode == Modes.INVERT) != redstone;
            this.pipe.setRedstonePowered(invert);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        mode = Modes.values()[nbt.getInteger("mode")];
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("mode", mode.ordinal());
    }

    @Override
    public void read(IInputBuffer buffer) {
        mode = Modes.values()[buffer.readInt()];
    }

    @Override
    public void write(IOutputBuffer buffer) {
        buffer.writeInt(mode.ordinal());
    }

    @Override
    public boolean cycleMode(EntityPlayer player) {
        this.mode = mode.cycle(player);
        return true;
    }

    public enum Modes{
        NORMAL,
        INVERT,
        NO_WORK;
        Modes cycle(EntityPlayer player){
            if (this == NORMAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_1);
                return INVERT;
            } else if (this == INVERT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_2);
                return NO_WORK;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_0);
                return NORMAL;
            }
        }
    }
}
