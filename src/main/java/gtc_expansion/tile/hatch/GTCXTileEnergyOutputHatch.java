package gtc_expansion.tile.hatch;

import gtclassic.common.GTLang;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.core.block.base.tile.TileEntityElectricBlock;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class GTCXTileEnergyOutputHatch extends TileEntityElectricBlock {

    public GTCXTileEnergyOutputHatch(int tier, int maxEnergy, int output) {
        super(tier, (int) EnergyNet.instance.getPowerFromTier(tier), maxEnergy);
        this.output = output;
        this.addGuiFields("output");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.output = nbt.getInteger("output");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("output", this.output);
        return nbt;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return null;
    }

    @Override
    public boolean hasGui(EntityPlayer player) {
        return false;
    }

    @Override
    public int getProcessRate() {
        return 128;
    }

    @Override
    public double getWrenchDropRate() {
        return 1.0D;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTLang.AESU;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return false;
    }

    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        if (amount <= 0.0D) {
            return 0.0D;
        }
        energy = ((int) (energy + amount));
        int left = 0;
        if (energy >= maxEnergy) {
            left = energy - maxEnergy;
            energy = maxEnergy;
        }
        getNetwork().updateTileGuiField(this, "energy");
        return left;
    }

    public static class GTCXTileDynamoHatch extends GTCXTileEnergyOutputHatch{
        public GTCXTileDynamoHatch() {
            super(4, 10000, 2048);
        }
    }
}
