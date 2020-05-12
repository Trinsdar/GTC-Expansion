package gtc_expansion.tile;

import gtclassic.api.interfaces.IGTDebuggableTile;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.LinkedHashMap;
import java.util.Map;

public class GTCXTileCasing extends TileEntityBlock implements IGTDebuggableTile {
    @NetworkField(
            index = 3
    )
    public int rotor;

    @NetworkField(
            index = 4
    )
    public Map<EnumFacing, Boolean> neighborMap = new LinkedHashMap<>();
    public GTCXTileCasing(){
        super();
        this.addNetworkFields("rotor", "neighborMap");
        rotor = 0;
    }

    public void setRotor(int rotor) {
        this.rotor = rotor;
    }

    public int getRotor() {
        return rotor;
    }

    public void setNeighborMap(Block block){
        for (EnumFacing facing : EnumFacing.values()){
            boolean hasBlock = world.getBlockState(pos.offset(facing)).getBlock() == block;
            neighborMap.put(facing, hasBlock);
        }
    }

    public Map<EnumFacing, Boolean> getNeighborMap() {
        return neighborMap;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("rotor", rotor);
        NBTTagCompound map = new NBTTagCompound();
        for (EnumFacing facing : neighborMap.keySet()){
            map.setBoolean(facing.getName(), neighborMap.get(facing));
        }
        nbt.setTag("neighborMap", map);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        rotor = nbt.getInteger("rotor");
        NBTTagCompound map = nbt.getCompoundTag("neighborMap");
        for (String name : map.getKeySet()){
            neighborMap.put(EnumFacing.byName(name), map.getBoolean(name));
        }
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put("Rotor: " + rotor, true);
    }
}
