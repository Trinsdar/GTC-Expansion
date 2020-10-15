package gtc_expansion.tile.hatch;

import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.base.util.info.MaxInputInfo;
import ic2.core.block.base.util.info.SinkTierInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public class GTCXTileFusionEnergyInjector extends TileEntityBlock implements IEnergySink {
    GTCXTileMultiFusionReactor accept;
    public boolean addedToEnergyNet;

    public GTCXTileFusionEnergyInjector() {
        super();
        accept = null;
        this.addInfos(new SinkTierInfo(this), new MaxInputInfo(this));
    }

    public void setAccept(GTCXTileMultiFusionReactor accept){
        /*if (accept == null){
            this.accept.addMaxEnergy(-10000000);
        } else {
            accept.addMaxEnergy(10000000);
        }*/
        this.accept = accept;
        this.updateNeighbors(true);
    }

    public GTCXTileMultiFusionReactor getAccept(){
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

    public void onLoaded() {
        super.onLoaded();
    }

    public void updateNeighbors(boolean needSelf) {
        if (needSelf) {
            this.world.neighborChanged(this.getPos(), this.getBlockType(), this.getPos());
        }

        EnumFacing[] var2 = EnumFacing.VALUES;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            EnumFacing side = var2[var4];
            BlockPos newPos = this.getPos().offset(side);
            if (this.world.isBlockLoaded(newPos)) {
                this.world.neighborChanged(newPos, this.getBlockType(), this.getPos());
            }
        }

    }

    public void onUnloaded() {
        super.onUnloaded();
    }

    public void onBlockRemoved(){
        if (accept != null){
            //this.accept.addMaxEnergy(-10000000);
            accept.invalidateStructure();
        }
    }

    @Override
    public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
        return this.getFacing() != facing && facing.getAxis().isHorizontal();
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    @Override
    public double getWrenchDropRate() {
        return 1.0D;
    }
}
