package gtc_expansion.tile.wiring;

import gtclassic.api.interfaces.IGTRecolorableStorageTile;
import gtclassic.common.tile.wiring.GTTileSuperconductorCable;
import ic2.api.classic.energy.tile.IAnchorConductor;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.network.adv.NetworkField.BitLevel;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.base.util.texture.TextureCopyStorage;
import ic2.core.block.wiring.tile.TileEntityCable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import java.awt.Color;

public class GTCXTileElectrumCable extends TileEntityBlock implements IEnergyConductor, IGTRecolorableStorageTile {

    @NetworkField(index = 8)
    public RotationList connection;
    protected boolean addedToEnergyNet;
    @NetworkField(index = 9)
    public int color;
    @NetworkField(
            index = 10
    )
    public byte foamed;
    @NetworkField(
            index = 11
    )
    public TextureCopyStorage storage;
    @NetworkField(
            index = 12,
            compression = BitLevel.Bit8
    )
    public int insulation;
    private static final String NBT_COLOR = "color";

    public GTCXTileElectrumCable() {
        this.color = 16383998;
        this.insulation = 0;
        this.foamed = 0;
        this.connection = RotationList.EMPTY;
        this.addNetworkFields("connection", NBT_COLOR, "foamed", "storage", "insulation");
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        updateConnections();
        if (!this.addedToEnergyNet && this.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    @Override
    public void onUnloaded() {
        if (this.addedToEnergyNet && this.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
        super.onUnloaded();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey(NBT_COLOR)) {
            this.color = nbt.getInteger(NBT_COLOR);
        } else {
            this.color = 16383998;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger(NBT_COLOR, this.color);
        return nbt;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return this.connection.contains(side);
    }

    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return this.connection.contains(side);
    }

    @Override
    public double getConductionLoss() {
        if (insulation == 1){
            return 0.04D;
        }
        if (insulation == 2){
            return 0.035D;
        }
        if (insulation == 3){
            return 0.03D;
        }
        return 0.045D;
    }

    @Override
    public double getInsulationEnergyAbsorption() {
        return 0;
    }

    @Override
    public double getInsulationBreakdownEnergy() {
        return 0;
    }

    @Override
    public double getConductorBreakdownEnergy() {
        return 0;
    }

    @Override
    public void removeInsulation() {

    }

    @Override
    public void removeConductor() {

    }

    public void updateConnections() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos sidedPos = pos.offset(facing);
            if (world.isBlockLoaded(sidedPos)) {
                world.neighborChanged(sidedPos, Blocks.AIR, pos);
            }
        }
        if (world.isBlockLoaded(pos)) {
            world.neighborChanged(pos, Blocks.AIR, pos);
        }
    }

    @Override
    public void onBlockUpdate(Block block) {
        super.onBlockUpdate(block);
        if (!this.isRendering()) {
            RotationList newList = RotationList.EMPTY;
            EnumFacing[] var3 = EnumFacing.VALUES;
            int var4 = var3.length;
            for (int var5 = 0; var5 < var4; ++var5) {
                EnumFacing dir = var3[var5];
                IEnergyTile tile = EnergyNet.instance.getSubTile(this.getWorld(), this.getPos().offset(dir));
                if (tile == null || !(tile instanceof IEnergyAcceptor) && !(tile instanceof IEnergyEmitter)) {
                    tile = EnergyNet.instance.getTile(this.getWorld(), this.getPos().offset(dir));
                }
                if (tile != null && this.canConnect(tile, dir)) {
                    newList = newList.add(dir);
                }
            }
            if (this.connection.getCode() != newList.getCode()) {
                this.connection = newList;
                this.getNetwork().updateTileEntityField(this, "connection");
            }
        }
    }

    public boolean canConnect(IEnergyTile tile, EnumFacing side) {
        if (tile instanceof TileEntityCable) {
            return true;
        } else if (tile instanceof GTTileSuperconductorCable) {
            return true;
        } else if (tile instanceof IAnchorConductor && ((IAnchorConductor) tile).hasAnchor(side.getOpposite())) {
            return false;
        } else if (tile instanceof IEnergyAcceptor && !(tile instanceof IEnergyEmitter)) {
            return ((IEnergyAcceptor) tile).acceptsEnergyFrom(this, side.getOpposite());
        } else if (tile instanceof IEnergyEmitter && !(tile instanceof IEnergyAcceptor)) {
            return ((IEnergyEmitter) tile).emitsEnergyTo(this, side.getOpposite());
        } else if (tile instanceof IEnergyAcceptor && tile instanceof IEnergyEmitter) {
            return ((IEnergyEmitter) tile).emitsEnergyTo(this, side.getOpposite())
                    || ((IEnergyAcceptor) tile).acceptsEnergyFrom(this, side.getOpposite());
        } else {
            return tile instanceof IMetaDelegate || tile instanceof INetworkTileEntityEventListener;
        }
    }

    @Override
    public void onNetworkUpdate(String field) {
        if (field.equals("connection")) {
            this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
        }
        super.onNetworkUpdate(field);
    }

    @Override
    public void setTileColor(int color) {
        this.color = color;
    }

    @Override
    public Color getTileColor() {
        return new Color(this.color);
    }

    @Override
    public boolean isColored() {
        return this.color != 16383998;
    }
}
