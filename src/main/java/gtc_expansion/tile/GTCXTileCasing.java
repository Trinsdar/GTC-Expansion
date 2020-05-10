package gtc_expansion.tile;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.block.GTCXBlockCasing;
import gtclassic.api.interfaces.IGTDebuggableTile;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public class GTCXTileCasing extends TileEntityBlock implements IGTDebuggableTile {
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

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put("Rotor: " + rotor, false);
        if (world.getBlockState(pos).getBlock() == GTCXBlocks.casingStandard){
            map.put("Blockstate Rotor: " + world.getBlockState(pos).getValue(GTCXBlockCasing.rotor), false);
        }
    }
}
