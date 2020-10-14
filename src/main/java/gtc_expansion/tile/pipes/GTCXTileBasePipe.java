package gtc_expansion.tile.pipes;

import gtc_expansion.data.GTCXPipes;
import gtc_expansion.events.GTCXServerTickEvent;
import gtc_expansion.logic.GTCXBaseCoverLogic;
import gtc_expansion.logic.GTCXRedstoneControllerLogic;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.util.CoverStorage;
import gtc_expansion.util.GTCXHelperPipe;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTRecolorableStorageTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GTCXTileBasePipe extends TileEntityMachine implements IGTDebuggableTile, IGTItemContainerTile, IGTRecolorableStorageTile {
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
    @NetworkField(index = 6)
    public int color;
    public boolean redstonePowered = false;
    private int prevColor = 0;
    private static String NBT_COLOR = "color";
    private GTMaterial material;
    private GTCXHelperPipe.GTPipeModel model;
    public GTCXTileBasePipe(int slots) {
        super(slots);
        this.color = 16383998;
        this.storage = new CoverStorage(this);
        this.connection = RotationList.EMPTY;
        this.anchors = RotationList.EMPTY;
        this.material = GTCXMaterial.Bronze;
        this.addNetworkFields("connection", "storage", "anchors", NBT_COLOR);
    }

    public void setMaterial(GTMaterial material) {
        this.material = material;
    }

    public void setModel(GTCXHelperPipe.GTPipeModel model) {
        this.model = model;
    }

    public void setRedstonePowered(boolean redstonePowered) {
        this.redstonePowered = redstonePowered;
    }

    @Override
    public boolean isRedstonePowered() {
        return redstonePowered;
    }

    public void onTick(){
        this.storage.onTick();
        this.setRedstonePowered(false);
        int poweredAmount = 0;
        int redstoneLogicAmount = 0;
        for (GTCXBaseCoverLogic logic : storage.getCoverLogicMap().values()){
            if (logic instanceof GTCXRedstoneControllerLogic){
                GTCXRedstoneControllerLogic redstoneControllerLogic = (GTCXRedstoneControllerLogic) logic;
                redstoneLogicAmount++;
                if (redstoneControllerLogic.isPowered()) {
                    poweredAmount++;
                }
            }
        }
        this.setRedstonePowered(poweredAmount > 0 && poweredAmount == redstoneLogicAmount);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        updateConnections();
        if (even()){
            GTCXServerTickEvent.SERVER_TICK_PRE.add(this);
        } else {
            GTCXServerTickEvent.SERVER_TICK_PR2.add(this);
        }
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        GTCXServerTickEvent.SERVER_TICK_PRE.remove(this);
        GTCXServerTickEvent.SERVER_TICK_PR2.remove(this);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.anchors = RotationList.ofNumber(nbt.getByte("Anchors"));
        this.connection = RotationList.ofNumber(nbt.getByte("Connection"));
        this.storage.readFromNBT(nbt.getCompoundTag("Storage"));
        if (nbt.hasKey(NBT_COLOR)) {
            this.color = nbt.getInteger(NBT_COLOR);
        } else {
            this.color = 16383998;
        }
        this.model = GTCXHelperPipe.GTPipeModel.values()[nbt.getInteger("model")];
        this.material = this.getMaterialFromInt(nbt.getInteger("material"));
        this.redstonePowered = nbt.getBoolean("redstonePowered");

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger(NBT_COLOR, this.color);
        nbt.setByte("Anchors", (byte)this.anchors.getCode());
        nbt.setByte("Connection", (byte)this.connection.getCode());
        this.storage.writeToNBT(this.getTag(nbt, "Storage"));
        nbt.setInteger("model", model.ordinal());
        nbt.setInteger("material", this.getIntFromMaterial(this.material));
        nbt.setBoolean("redstonePowered", redstonePowered);
        return nbt;
    }

    private GTMaterial getMaterialFromInt(int material){
        switch (material){
            case 1: return GTCXMaterial.Steel;
            case 2: return GTCXMaterial.StainlessSteel;
            case 3: return GTCXMaterial.TungstenSteel;
            case 4: return GTCXMaterial.Brass;
            case 5: return GTMaterial.Electrum;
            case 6: return GTMaterial.Platinum;
            case 7: return GTMaterial.Invar;
            default: return GTCXMaterial.Bronze;
        }
    }

    private int getIntFromMaterial(GTMaterial material){
        if (material.equals(GTCXMaterial.Bronze)){
            return 0;
        }
        if (material.equals(GTCXMaterial.Steel)){
            return 1;
        }
        if (material.equals(GTCXMaterial.StainlessSteel)){
            return 2;
        }
        if (material.equals(GTCXMaterial.TungstenSteel)){
            return 3;
        }
        if (material.equals(GTCXMaterial.Brass)){
            return 4;
        }
        if (material.equals(GTMaterial.Electrum)){
            return 5;
        }
        if (material.equals(GTMaterial.Platinum)){
            return 6;
        }
        if (material.equals(GTMaterial.Invar)){
            return 7;
        }
        return 0;
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (orString(field,"anchors", "connection", "storage", NBT_COLOR)) {
            this.prevColor = color;
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
        return player.isSneaking();
    }

    @Override
    public boolean supportsRotation() {
        return false;
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
    public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
        return false;
    }

    @Override
    public void onBlockUpdate(Block block) {
        super.onBlockUpdate(block);
        if (!this.isRendering()) {
            /*RotationList newList = RotationList.EMPTY;
            EnumFacing[] facings = EnumFacing.VALUES;
            int length = facings.length;
            for (int i = 0; i < length; ++i) {
                EnumFacing dir = facings[i];
                TileEntity tile = world.getTileEntity(this.getPos().offset(dir));
                if ((tile != null && this.canConnect(tile, dir))) {
                    newList = newList.add(dir);
                }
            }
            if (this.connection.getCode() != newList.getCode()) {
                this.connection = newList;
                this.getNetwork().updateTileEntityField(this, "connection");
            }*/
        }
    }

    public boolean even() {
        int[] coords = {this.pos.getX(), this.pos.getY(), this.pos.getZ()};
        int i = 0;
        for (int coord : coords) {
            if (coord % 2 == 0) {
                i++;
            }
        }
        return i % 2 == 0;
    }

    public abstract boolean canConnectWithoutColor(TileEntity tile, EnumFacing facing);

    public boolean canConnect(TileEntity tile, EnumFacing facing){
        if (tile == null){
            return false;
        }
        boolean sharesColor = true;
        if (tile instanceof GTCXTileBasePipe){
            GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
            sharesColor = !this.isColored() || this.color == pipe.color || !pipe.isColored();
        }
        return canConnectWithoutColor(tile, facing) && sharesColor;
    }

    public void addCover(EnumFacing facing, IBlockState coverState){
        this.storage.setCover(coverState, facing);
        this.anchors = this.anchors.add(facing);
        updateConnections();
        this.getNetwork().updateTileEntityField(this, "storage");
        this.getNetwork().updateTileEntityField(this, "anchors");
    }

    public void addConnection(EnumFacing facing){
        this.connection = connection.add(facing);
        updateConnections();
        this.getNetwork().updateTileEntityField(this, "connection");
    }

    public void removeConnection(EnumFacing facing){
        this.connection = connection.remove(facing);
        updateConnections();
        this.getNetwork().updateTileEntityField(this, "connection");
    }

    public void removeCover(EnumFacing facing){
        this.storage.removeCover(facing);
        this.anchors = anchors.remove(facing);
        updateConnections();
        this.getNetwork().updateTileEntityField(this, "storage");
        this.getNetwork().updateTileEntityField(this, "anchors");
    }

    @Override
    public void setTileColor(int color){
        this.color = color;
        if (color != this.prevColor) {
            this.getNetwork().updateTileEntityField(this, NBT_COLOR);
        }
        this.prevColor = color;
    }

    @Override
    public Color getTileColor() {
        return new Color(this.color);
    }

    @Override
    public boolean isColored() {
        return this.color != 16383998 && this.color != material.getColor().getRGB();
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<>();
        ItemStack block = GTCXPipes.getPipe(material, model);
        if (this.isColored() && this.color != material.getColor().getRGB()) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(block);
            nbt.setInteger("color", this.color);
        }
        list.add(block);
        list.addAll(this.getInventoryDrops());
        return list;
    }

    @Override
    public List<ItemStack> getInventoryDrops() {
        List<ItemStack> list = new ArrayList<>();
        for (EnumFacing facing : EnumFacing.values()){
            ItemStack stack = storage.getCoverDrop(facing);
            if (!stack.isEmpty()){
                list.add(stack);
            }
        }
        if (!getSlotStack().isEmpty()){
            list.add(getSlotStack());
        }
        return list;
    }

    public ItemStack getSlotStack(){
        return ItemStack.EMPTY;
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        //map.put("Model type: " + model.getPrefix(), true);
        //map.put("Material: " + material.getDisplayName(), true);
        for (EnumFacing facing : EnumFacing.VALUES){
            if (connection.contains(facing)){
                map.put("Connections has facing " + facing.getName(), true);
            }
        }
        for(int i = 0; i < 6; ++i) {
            if (storage.getEntries()[i].getModelState() != null && storage.getEntries()[i].getRenderState() != null){
                map.put("Cover at facing " + EnumFacing.getFront(i) + ": " + storage.getCoverDrop(EnumFacing.getFront(i)).getDisplayName(), true);
                storage.getCoverLogicMap().get(EnumFacing.getFront(i)).getData(map);
            }
        }
    }

    @Override
    public void onBlockBreak() {
        super.onBlockBreak();
        for (EnumFacing facing : EnumFacing.values()){
            BlockPos offset = this.getPos().offset(facing);
            TileEntity offsetTile = this.getWorld().getTileEntity(offset);
            if (offsetTile instanceof GTCXTileBasePipe && ((GTCXTileBasePipe)offsetTile).connection.contains(facing.getOpposite())){
                ((GTCXTileBasePipe)offsetTile).removeConnection(facing.getOpposite());
            }
        }
    }
}
