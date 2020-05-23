package gtc_expansion.tile;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch;
import gtc_expansion.tile.multi.GTCXTileMultiLargeSteamTurbine;
import gtc_expansion.util.IGTCasingBackgroundBlock;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IGTCasingBackgroundBlock){
            return GTCXTileEnergyOutputHatch.fromCasing(((IGTCasingBackgroundBlock)tile).getCasing()) == block;
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
    }

    public int getConfig() {
        return config;
    }
}
