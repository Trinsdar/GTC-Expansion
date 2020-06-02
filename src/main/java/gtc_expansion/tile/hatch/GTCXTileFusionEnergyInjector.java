package gtc_expansion.tile.hatch;

import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.info.ILocatable;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GTCXTileFusionEnergyInjector extends TileEntityBlock implements IEnergySink, ILocatable {
    IEnergySink accept;

    public GTCXTileFusionEnergyInjector() {
        super();
        accept = null;
    }

    public void setAccept(IEnergySink accept){
        this.accept = accept;
    }

    public IEnergySink getAccept(){
        return accept;
    }

    @Override
    public double getDemandedEnergy() {
        return accept != null ? accept.getDemandedEnergy() : 0;
    }

    @Override
    public int getSinkTier() {
        return accept != null ? accept.getSinkTier() : 0;
    }

    @Override
    public double injectEnergy(EnumFacing enumFacing, double v, double v1) {
        return accept != null ? accept.injectEnergy(enumFacing, v, v1) : 0;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing enumFacing) {
        return accept != null;
    }

    @Override
    public BlockPos getPosition() {
        return this.getPos();
    }

    @Override
    public World getWorldObj() {
        return this.getWorld();
    }
}
