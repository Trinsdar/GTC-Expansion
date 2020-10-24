package gtc_expansion.logic;

import gtc_expansion.data.GTCXLang;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.Map;

public class GTCXShutterLogic extends GTCXBaseCoverLogic {
    Modes mode = Modes.OPEN_ENABLED;
    public GTCXShutterLogic(GTCXTileBasePipe pipe, EnumFacing facing) {
        super(pipe, facing);
    }

    @Override
    public void onTick() {
        if (this.mode == Modes.OPEN_ENABLED || this.mode == Modes.OPEN_DISABLED){
            boolean redstone = (this.mode == Modes.OPEN_ENABLED) == pipe.isRedstonePowered();
            if (redstone){
                TileEntity tileEntity = pipe.getWorld().getTileEntity(pipe.getPos().offset(this.facing));
                if (pipe.connection.notContains(this.facing) && pipe.canConnect(tileEntity, facing)){
                    pipe.addConnection(this.facing);
                }
                if (tileEntity instanceof GTCXTileBasePipe){
                    GTCXTileBasePipe pipe1 = (GTCXTileBasePipe) tileEntity;
                    if (pipe1.connection.notContains(this.facing.getOpposite()) && pipe1.canConnect(this.pipe, this.facing.getOpposite())){
                        pipe1.addConnection(this.facing.getOpposite());
                    }
                }
            } else {
                if (pipe.connection.contains(this.facing)){
                    pipe.removeConnection(this.facing);
                }
                TileEntity tileEntity = pipe.getWorld().getTileEntity(pipe.getPos().offset(this.facing));
                if (tileEntity instanceof GTCXTileBasePipe){
                    GTCXTileBasePipe pipe1 = (GTCXTileBasePipe) tileEntity;
                    if (pipe1.connection.contains(this.facing.getOpposite())){
                        pipe1.removeConnection(this.facing.getOpposite());
                    }
                }
            }
        }
    }

    @Override
    public boolean allowsPipeInput() {
        return this.mode != Modes.OUTPUT_ONLY;
    }

    @Override
    public boolean allowsPipeOutput() {
        return this.mode != Modes.INPUT_ONLY;
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
        OPEN_ENABLED,
        OPEN_DISABLED,
        OUTPUT_ONLY,
        INPUT_ONLY;
        Modes cycle(EntityPlayer player){
            if (this == OPEN_ENABLED){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_SHUTTER_MODE_1);
                return OPEN_DISABLED;
            } else if (this == OPEN_DISABLED){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_SHUTTER_MODE_2);
                return OUTPUT_ONLY;
            } else if (this == OUTPUT_ONLY){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_SHUTTER_MODE_3);
                return INPUT_ONLY;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_SHUTTER_MODE_0);
                return OPEN_ENABLED;
            }
        }

        Modes cycleBack(EntityPlayer player){
            if (this == OPEN_ENABLED){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_SHUTTER_MODE_3);
                return INPUT_ONLY;
            } else if (this == INPUT_ONLY){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_SHUTTER_MODE_2);
                return OUTPUT_ONLY;
            } else if (this == OUTPUT_ONLY){
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_SHUTTER_MODE_1);
                return OPEN_DISABLED;
            } else {
                IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_COVER_SHUTTER_MODE_0);
                return OPEN_ENABLED;
            }
        }
    }
}
