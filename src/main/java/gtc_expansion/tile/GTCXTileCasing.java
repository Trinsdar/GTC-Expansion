package gtc_expansion.tile;

import gtclassic.api.interfaces.IGTDebuggableTile;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.Map;

public class GTCXTileCasing extends TileEntityBlock implements IGTDebuggableTile {
    @NetworkField(
            index = 3
    )
    public int rotor;

    @NetworkField(
            index = 4
    )
    public boolean up = false;
    @NetworkField(
            index = 5
    )
    public boolean down = false;
    @NetworkField(
            index = 6
    )
    public boolean north = false;
    @NetworkField(
            index = 7
    )
    public boolean south = false;
    @NetworkField(
            index = 8
    )
    public boolean east = false;
    @NetworkField(
            index = 9
    )
    public boolean west = false;
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
            switch (facing){
                case UP: up = hasBlock;
                case DOWN: down = hasBlock;
                case NORTH: north = hasBlock;
                case SOUTH: south = hasBlock;
                case EAST: east = hasBlock;
                case WEST: west = hasBlock;
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("rotor", rotor);
        nbt.setBoolean("up", up);
        nbt.setBoolean("down", down);
        nbt.setBoolean("north", north);
        nbt.setBoolean("south", south);
        nbt.setBoolean("east", east);
        nbt.setBoolean("west", west);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        rotor = nbt.getInteger("rotor");
        up = nbt.getBoolean("up");
        down = nbt.getBoolean("down");
        north = nbt.getBoolean("north");
        south = nbt.getBoolean("south");
        east = nbt.getBoolean("east");
        west = nbt.getBoolean("west");
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put("Rotor: " + rotor, true);
    }
}
