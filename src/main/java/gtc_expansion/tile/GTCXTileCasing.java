package gtc_expansion.tile;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch;
import gtc_expansion.tile.multi.GTCXTileMultiLargeSteamTurbine;
import gtc_expansion.util.IGTCasingBackgroundBlock;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class GTCXTileCasing extends TileEntityBlock implements IGTDebuggableTile {
    @NetworkField(
            index = 3
    )
    public int rotor;
    private int prevRotor = 0;

    @NetworkField(
            index = 4
    )
    public int config = 0;
    private int prevConfig = 0;

    public GTCXTileCasing(){
        super();
        this.addNetworkFields("rotor", "config");
        rotor = 0;
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("rotor") || field.equals("config")) {
            this.prevRotor = this.rotor;
            this.prevConfig = this.config;
            this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
        }
    }

    public void setRotor(int rotor) {
        this.rotor = rotor;
        if (rotor != this.prevRotor) {
            this.getNetwork().updateTileEntityField(this, "rotor");
        }

        this.prevRotor = rotor;
    }

    public int getRotor() {
        return rotor;
    }

    public void setNeighborMap(Block block){
        config = 0;
        for (EnumFacing facing : EnumFacing.values()){
            boolean hasBlock = world.getBlockState(pos.offset(facing)).getBlock() == block || isHatchWithCasing(pos.offset(facing), block);
            if (hasBlock){
                config += 1 << facing.getIndex();
            }
        }
        if (config != this.prevConfig) {
            this.getNetwork().updateTileEntityField(this, "config");
        }

        this.prevConfig = config;
    }

    public boolean isHatchWithCasing(BlockPos pos, Block block){
        if (world.getTileEntity(pos) instanceof IGTCasingBackgroundBlock){
            return GTCXTileEnergyOutputHatch.fromCasing(((IGTCasingBackgroundBlock)world.getTileEntity(pos)).getCasing()) == block;
        }
        return false;
    }

    public void setRotor(Block block){
        if (block == GTCXBlocks.casingStandard){
            int3 original = new int3(getPos(), getFacing());
            int3 dir = new int3(getPos(), getFacing());
            if (world.getTileEntity(dir.up(1).left(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 8;
            } else if (world.getTileEntity(dir.set(original).up(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 7;
            } else if (world.getTileEntity(dir.set(original).up(1).right(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 6;
            } else if (world.getTileEntity(dir.set(original).left(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 5;
            } else if (world.getTileEntity(dir.set(original).right(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 4;
            } else if (world.getTileEntity(dir.set(original).down(1).left(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 3;
            } else if (world.getTileEntity(dir.set(original).down(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 2;
            } else if (world.getTileEntity(dir.set(original).down(1).right(1).asBlockPos()) instanceof GTCXTileMultiLargeSteamTurbine){
                rotor = 1;
            } else {
                rotor = 0;
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("rotor", rotor);
        nbt.setInteger("config", config);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        rotor = nbt.getInteger("rotor");
        config = nbt.getInteger("config");
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put("Rotor: " + rotor, true);
        map.put("Config: "+ config, true);
        for (EnumFacing facing : EnumFacing.VALUES){
            map.put("Index with facing " + facing.getName() + ": " + getIndexes(facing), true);
        }
    }

    public int getIndexes(EnumFacing textureFacing)
    {
        int con = config;
        RotationList list = RotationList.ofNumber(con).remove(textureFacing).remove(textureFacing.getOpposite());
        if(list.size() == 0 || list.size() == 4)
        {
            return list.size() == 4 ? 6 : 7;
        }
        if (list.size() == 1){
            return containsAxis(list, EnumFacing.Axis.Y) ? 1 : 0;
        }
        if (list.size() == 2){
            if (!containsAxis(list, EnumFacing.Axis.Y)){
                return 0;
            }
            if (!containsAxis(list, EnumFacing.Axis.Z) && !containsAxis(list, EnumFacing.Axis.X)){
                return  1;
            }
        }
        int index = 0;
        int result = 0;
        if (textureFacing.getAxis() == EnumFacing.Axis.Y){
            EnumFacing blockFacing = this.getFacing();
        }
        for(EnumFacing facing : list) {
            if (list.size() == 2){
                result += 1;
            }
            result += (1 << (index++ * 2)) + convert(textureFacing, facing) & 3;
        }

        return result;
    }

    protected boolean containsAxis(RotationList list, EnumFacing.Axis axis){
        switch (axis){
            case X: return list.contains(EnumFacing.EAST) || list.contains(EnumFacing.WEST);
            case Z: return list.contains(EnumFacing.NORTH) || list.contains(EnumFacing.SOUTH);
            case Y: return list.contains(EnumFacing.UP) || list.contains(EnumFacing.DOWN);
            default: return false;
        }
    }

    protected int add(EnumFacing side, EnumFacing index)
    {
        switch(side.getAxis())
        {
            case X:
                switch(index)
                {
                    case DOWN: return 0;
                    case UP: return 1;
                    case NORTH: return 2;
                    case SOUTH: return 3;
                    default: return -1;
                }
            case Y:
                switch(index)
                {
                    case WEST: return 0;
                    case EAST: return 1;
                    case NORTH: return 2;
                    case SOUTH: return 3;
                    default: return -1;
                }
            case Z:
                switch(index)
                {
                    case DOWN: return 0;
                    case UP: return 1;
                    case WEST: return 2;
                    case EAST: return 3;
                    default: return -1;
                }
        }
        return 0;
    }

    protected int convert(EnumFacing side, EnumFacing index)
    {
        switch(side.getAxis())
        {
            case X:
                switch(index)
                {
                    case DOWN: return 0;
                    case UP: return 1;
                    case NORTH: return 2;
                    case SOUTH: return 3;
                    default: return -1;
                }
            case Y:
                switch(index)
                {
                    case WEST: return 0;
                    case EAST: return 1;
                    case NORTH: return 2;
                    case SOUTH: return 3;
                    default: return -1;
                }
            case Z:
                switch(index)
                {
                    case DOWN: return 0;
                    case UP: return 1;
                    case WEST: return 2;
                    case EAST: return 3;
                    default: return -1;
                }
        }
        return 0;
    }

    public int getConfig() {
        return config;
    }
}
