package gtc_expansion.util;

import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.event.RetextureEventClassic;
import ic2.api.classic.network.INetworkFieldData;
import ic2.api.classic.network.adv.IInputBuffer;
import ic2.api.classic.network.adv.IOutputBuffer;
import ic2.core.block.base.util.texture.TextureCopyEntry;
import ic2.core.block.base.util.texture.TextureCopyStorage.QuadList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CoverStorage implements INetworkFieldData {
    TextureCopyEntry[] entries;
    GTCXTileBasePipe owner;
    public CoverStorage(GTCXTileBasePipe owner){
        this.owner = owner;
        this.entries = TextureCopyEntry.createArray(6);
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

    public void setCover(IBlockState cover, EnumFacing facing){
        entries[facing.getIndex()].set(cover, cover, new int[]{-1}, new RetextureEventClassic.Rotation[]{RetextureEventClassic.Rotation.Rotation0}, facing);
    }

    public void removeCover(EnumFacing facing){
        entries[facing.getIndex()].clear();
    }

    @Override
    public void read(IInputBuffer buffer) {
        this.entries = TextureCopyEntry.createArray(6);

        for(int i = 0; i < 6; ++i) {
            int slot = buffer.readByte();
            this.entries[slot].read(buffer);
        }
    }

    @Override
    public void write(IOutputBuffer buffer) {
        for(int i = 0; i < 6; ++i) {
            buffer.writeByte((byte)i);
            this.entries[i].write(buffer);
        }
    }

    /*@SideOnly(Side.CLIENT)
    public static class QuadList {
        List<BakedQuad>[] quads = createList();

        public QuadList() {
        }

        public void addQuads(List<BakedQuad> list, EnumFacing facing) {
            this.quads[facing.getIndex()].addAll(list);
        }

        public List<BakedQuad> getQuads(EnumFacing facing) {
            return this.quads[facing.getIndex()];
        }

        protected List<BakedQuad>[] createList() {
            List<BakedQuad>[] quads = new List[6];

            for(int i = 0; i < 6; ++i) {
                quads[i] = new ArrayList<>();
            }

            return quads;
        }
    }*/
}
