package gtc_expansion.util;

import gtc_expansion.logic.GTCXBaseCoverLogic;
import gtc_expansion.logic.GTCXConveyorModuleLogic;
import gtc_expansion.logic.GTCXDrainModuleLogic;
import gtc_expansion.logic.GTCXFluidFilterLogic;
import gtc_expansion.logic.GTCXItemValveModuleLogic;
import gtc_expansion.logic.GTCXNullLogic;
import gtc_expansion.logic.GTCXPumpModuleLogic;
import gtc_expansion.logic.GTCXRedstoneControllerLogic;
import gtc_expansion.logic.GTCXShutterLogic;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.event.RetextureEventClassic;
import ic2.api.classic.network.INetworkFieldData;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import ic2.core.block.base.util.texture.TextureCopyEntry;
import ic2.core.block.base.util.texture.TextureCopyStorage.QuadList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedHashMap;
import java.util.Map;

public class CoverStorage implements INetworkFieldData {
    TextureCopyEntry[] entries;
    Map<EnumFacing, GTCXBaseCoverLogic> coverLogicMap = new LinkedHashMap<>();
    GTCXTileBasePipe owner;
    public CoverStorage(GTCXTileBasePipe owner){
        this.owner = owner;
        this.entries = TextureCopyEntry.createArray(6);
        for (int i = 0; i < 6; i++){
            coverLogicMap.put(EnumFacing.getFront(i), new GTCXNullLogic(owner, EnumFacing.getFront(i)));
        }
    }

    public void onTick(){
        for (GTCXBaseCoverLogic logic : coverLogicMap.values()){
            if (!(logic instanceof GTCXNullLogic)) {
                logic.onTick();
            }
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.entries = TextureCopyEntry.createArray(6);
        NBTTagList list = nbt.getTagList("Data", 10);

        for(int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            int slot = data.getInteger("Slot");
            if (slot >= 0 && slot < 6) {
                this.entries[slot].readFromNBT(data);
            }
        }
        coverLogicMap = new LinkedHashMap<>();
        NBTTagList logic = nbt.getTagList("Logic", 10);
        for (int i = 0; i < 6; i++){
            NBTTagCompound data = logic.getCompoundTagAt(i);
            this.coverLogicMap.put(EnumFacing.getFront(i), logicFromInt(data.getInteger("Type"), EnumFacing.getFront(i)));
            this.coverLogicMap.get(EnumFacing.getFront(i)).readFromNBT(data);
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for(int i = 0; i < 6; ++i) {
            NBTTagCompound data = new NBTTagCompound();
            this.entries[i].writeFromNBT(data);
            data.setByte("Slot", (byte)i);
            list.appendTag(data);
        }
        nbt.setTag("Data", list);
        NBTTagList logic = new NBTTagList();
        for (int i = 0; i < 6; i++){
            NBTTagCompound data = new NBTTagCompound();
            data.setInteger("Type", intFromLogic(coverLogicMap.get(EnumFacing.getFront(i))));
            this.coverLogicMap.get(EnumFacing.getFront(i)).writeToNBT(data);
            logic.appendTag(data);
        }
        nbt.setTag("Logic", logic);
    }

    @SideOnly(Side.CLIENT)
    public QuadList getQuads() {
        QuadList list = new QuadList();

        for(int i = 0; i < 6; ++i) {
            if (this.entries[i].getModelState() != null && this.entries[i].getRenderState() != null){
                list.addQuads(this.entries[i].getQuads());
            }
        }

        return list;
    }

    @SideOnly(Side.CLIENT)
    public QuadList[] getQuadList() {
        QuadList[] list = new QuadList[6];

        for(int i = 0; i < 6; ++i) {
            list[i] = this.getQuads(EnumFacing.getFront(i));
        }

        return list;
    }

    @SideOnly(Side.CLIENT)
    public QuadList getQuads(EnumFacing facing) {
        QuadList list = new QuadList();

        if (facing == null){
            return this.getQuads();
        }
        if (this.entries[facing.getIndex()].getModelState() != null && this.entries[facing.getIndex()].getRenderState() != null){
            list.addQuads(this.entries[facing.getIndex()].getQuads());
        }
        return list;
    }

    public void setCover(IBlockState cover, EnumFacing facing){
        entries[facing.getIndex()].set(cover, cover, new int[]{-1}, new RetextureEventClassic.Rotation[]{RetextureEventClassic.Rotation.Rotation0}, facing);
        coverLogicMap.put(facing, this.logicFromInt(cover.getBlock().getMetaFromState(cover), facing));
    }

    public void removeCover(EnumFacing facing){
        entries[facing.getIndex()].clear();
        coverLogicMap.put(facing, this.logicFromInt(0, facing));
    }

    public ItemStack getCoverDrop(EnumFacing facing){
        if (this.entries[facing.getIndex()].getRenderState() != null && this.entries[facing.getIndex()].getModelState() != null){
            IBlockState state = this.entries[facing.getIndex()].getModelState();
            return new ItemStack(state.getBlock().getItemDropped(state, owner.getWorld().rand, 0));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void read(IInputBuffer buffer) {
        this.entries = TextureCopyEntry.createArray(6);

        for(int i = 0; i < 6; ++i) {
            int slot = buffer.readByte();
            this.entries[slot].read(buffer);
        }
        coverLogicMap = new LinkedHashMap<>();
        for (int i = 0; i < 6; i++){
            this.coverLogicMap.put(EnumFacing.getFront(i), logicFromInt(buffer.readInt(), EnumFacing.getFront(i)));
            this.coverLogicMap.get(EnumFacing.getFront(i)).read(buffer);
        }
    }

    @Override
    public void write(IOutputBuffer buffer) {
        for(int i = 0; i < 6; ++i) {
            buffer.writeByte((byte)i);
            this.entries[i].write(buffer);
        }
        for (int i = 0; i < 6; i++){
            buffer.writeInt(intFromLogic(coverLogicMap.get(EnumFacing.getFront(i))));
            this.coverLogicMap.get(EnumFacing.getFront(i)).write(buffer);
        }
    }

    public TextureCopyEntry[] getEntries() {
        return entries;
    }

    public Map<EnumFacing, GTCXBaseCoverLogic> getCoverLogicMap() {
        return coverLogicMap;
    }

    protected GTCXBaseCoverLogic logicFromInt(int value, EnumFacing facing){
        switch (value){
            case 1: return new GTCXConveyorModuleLogic(owner, facing);
            case 2: return new GTCXDrainModuleLogic(owner, facing);
            case 3: return new GTCXItemValveModuleLogic(owner, facing);
            case 4: return new GTCXPumpModuleLogic(owner, facing);
            case 5: return new GTCXShutterLogic(owner, facing);
            case 6: return new GTCXRedstoneControllerLogic(owner, facing);
            case 7: return new GTCXFluidFilterLogic(owner, facing);
            default: return new GTCXNullLogic(owner, facing);
        }
    }

    protected int intFromLogic(GTCXBaseCoverLogic logic){
        if (logic instanceof GTCXConveyorModuleLogic){
            return 1;
        }
        if (logic instanceof GTCXDrainModuleLogic){
            return 2;
        }
        if (logic instanceof GTCXItemValveModuleLogic){
            return 3;
        }
        if (logic instanceof GTCXPumpModuleLogic){
            return 4;
        }
        if (logic instanceof GTCXShutterLogic){
            return 5;
        }
        if (logic instanceof GTCXRedstoneControllerLogic){
            return 6;
        }
        if (logic instanceof GTCXFluidFilterLogic){
            return 7;
        }
        return 0;
    }
}
