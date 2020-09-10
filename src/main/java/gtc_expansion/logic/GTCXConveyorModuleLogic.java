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

public class GTCXConveyorModuleLogic extends GTCXBaseCoverLogic {
    Modes mode = Modes.IMPORT;
    public GTCXConveyorModuleLogic(GTCXTileBasePipe pipe, EnumFacing facing) {
        super(pipe, facing);
    }

    @Override
    public void onTick() {

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
    public boolean allowsPipeOutput(){
        return mode.ordinal() > 2;
    }

    @Override
    public boolean cycleMode(EntityPlayer player) {
        if (this.pipe.isSimulating()){
            mode = player.isSneaking() ? mode.cycleBack(player) : mode.cycle(player);
        }
        return true;
    }

    @Override
    public void getData(Map<String, Boolean> map){
        map.put(this.mode.toString(), true);
    }

    public enum Modes{
        IMPORT,
        IMPORT_CONDITIONAL,
        IMPORT_INVERSE_CONDITIONAL,
        IMPORT_EXPORT,
        IMPORT_EXPORT_CONDITIONAL,
        IMPORT_EXPORT_INVERSE_CONDITIONAL;
        Modes cycle(EntityPlayer player){
            if (this == IMPORT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_1);
                return IMPORT_CONDITIONAL;
            } else if (this == IMPORT_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_2);
                return IMPORT_INVERSE_CONDITIONAL;
            } else if (this == IMPORT_INVERSE_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_3);
                return IMPORT_EXPORT;
            } else if (this == IMPORT_EXPORT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_4);
                return IMPORT_EXPORT_CONDITIONAL;
            } else if (this == IMPORT_EXPORT_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_5);
                return IMPORT_EXPORT_INVERSE_CONDITIONAL;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_0);
                return IMPORT;
            }
        }

        Modes cycleBack(EntityPlayer player){
            if (this == IMPORT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_5);
                return IMPORT_EXPORT_INVERSE_CONDITIONAL;
            } else if (this == IMPORT_EXPORT_INVERSE_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_4);
                return IMPORT_EXPORT_CONDITIONAL;
            } else if (this == IMPORT_EXPORT_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_3);
                return IMPORT_EXPORT;
            } else if (this == IMPORT_EXPORT){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_2);
                return IMPORT_INVERSE_CONDITIONAL;
            } else if (this == IMPORT_INVERSE_CONDITIONAL){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_1);
                return IMPORT_CONDITIONAL;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_MODE_0);
                return IMPORT;
            }
        }
    }
}
