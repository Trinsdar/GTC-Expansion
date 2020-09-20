package gtc_expansion.logic;

import gtc_expansion.data.GTCXLang;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.Map;

public class GTCXRedstoneControllerLogic extends GTCXBaseCoverLogic {
    Modes mode = Modes.NORMAL;
    boolean powered = false;
    public GTCXRedstoneControllerLogic(GTCXTileBasePipe pipe, EnumFacing facing) {
        super(pipe, facing);
    }

    @Override
    public void onTick() {
        int newLevel = pipe.getWorld().getRedstonePower(pipe.getPos().offset(this.facing), this.facing);
        if (this.mode != Modes.NO_WORK){
            boolean redstone = newLevel > 0;
            boolean invert = (this.mode == Modes.INVERT) != redstone;
            if (powered != invert){
                this.powered = invert;
            }
        }
    }

    public boolean isPowered() {
        return powered;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        mode = Modes.values()[nbt.getInteger("mode")];
        powered = nbt.getBoolean("powered");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("mode", mode.ordinal());
        nbt.setBoolean("powered", powered);
    }

    @Override
    public void read(IInputBuffer buffer) {
        mode = Modes.values()[buffer.readInt()];
        powered = buffer.readBoolean();
    }

    @Override
    public void write(IOutputBuffer buffer) {
        buffer.writeInt(mode.ordinal());
        buffer.writeBoolean(powered);
    }

    @Override
    public boolean cycleMode(EntityPlayer player) {
        if (this.pipe.isSimulating()){
            mode = player.isSneaking() ? mode.cycleBack(player) : mode.cycle(player);
        }
        return true;
    }

    @Override
    public boolean allowsPipeOutput(){
        return true;
    }

    @Override
    public void getData(Map<String, Boolean> map){
        map.put(this.mode.toString(), true);
    }

    public enum Modes{
        NORMAL,
        INVERT,
        NO_WORK;
        Modes cycle(EntityPlayer player){
            if (this == NORMAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_REDSTONE_MODE_1);
                return INVERT;
            } else if (this == INVERT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_REDSTONE_MODE_2);
                return NO_WORK;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_REDSTONE_MODE_0);
                return NORMAL;
            }
        }

        Modes cycleBack(EntityPlayer player){
            if (this == NORMAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_REDSTONE_MODE_2);
                return NO_WORK;
            } else if (this == NO_WORK){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_REDSTONE_MODE_1);
                return INVERT;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_REDSTONE_MODE_0);
                return NORMAL;
            }
        }
    }
}
