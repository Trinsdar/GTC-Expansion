package gtc_expansion.tile.pipes;

import gtc_expansion.util.CoverStorage;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public abstract class GTCXTileBasePipe extends TileEntityMachine {
    @NetworkField(index = 3)
    public RotationList connection;
    @NetworkField(
            index = 4
    )
    public RotationList anchors;
    @NetworkField(
            index = 5
    )
    public CoverStorage storage;
    public GTCXTileBasePipe(int slots) {
        super(slots);
        this.storage = new CoverStorage(this);
        this.connection = RotationList.EMPTY;
        this.anchors = RotationList.EMPTY;
        this.addNetworkFields("connection", "storage", "anchors");
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        updateConnections();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.anchors = RotationList.ofNumber(nbt.getByte("Anchors"));
        this.storage.readFromNBT(nbt.getCompoundTag("Storage"));

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("Anchors", (byte)this.anchors.getCode());
        this.storage.writeToNBT(this.getTag(nbt, "Storage"));
        return nbt;
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (orString(field,"anchors", "connection", "storage")) {
            this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
        }
    }

    public boolean orString(String compare, String... strings){
        for (String string : strings){
            if (compare.equals(string)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
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

    public Vec3i getConnections() {
        return new Vec3i(this.connection.getCode(), this.anchors.getCode(), this.connection.getCode() << 6 | this.anchors.getCode());
    }

    @Override
    public void onBlockUpdate(Block block) {
        super.onBlockUpdate(block);
        if (!this.isRendering()) {
            RotationList newList = RotationList.EMPTY;
            EnumFacing[] facings = EnumFacing.VALUES;
            int length = facings.length;
            for (int i = 0; i < length; ++i) {
                EnumFacing dir = facings[i];
                TileEntity tile = world.getTileEntity(this.getPos().offset(dir));
                if (tile != null && this.canConnectOnClient(tile, dir)) {
                    newList = newList.add(dir);
                }
            }
            if (this.connection.getCode() != newList.getCode()) {
                this.connection = newList;
                this.getNetwork().updateTileEntityField(this, "connection");
            }
        }
    }

    public abstract boolean canConnect(TileEntity tile, EnumFacing facing);

    public boolean canConnectOnClient(TileEntity tile, EnumFacing facing){
        return canConnect(tile, facing) || this.anchors.contains(facing);
    }

    public void addCover(EnumFacing facing, IBlockState coverState){
        this.storage.setCover(coverState, facing);
        this.anchors = this.anchors.add(facing);
        updateConnections();
        this.getNetwork().updateTileEntityField(this, "storage");
    }

    public void removeCover(EnumFacing facing){
        this.storage.removeCover(facing);
        this.anchors = anchors.remove(facing);
        updateConnections();
        this.getNetwork().updateTileEntityField(this, "storage");
    }
}
