package gtc_expansion.util;

import gtc_expansion.model.GTCXModelPipe;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.api.classic.event.RetextureEventClassic;
import ic2.core.block.base.util.texture.TextureCopyEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CoverStorage {
    TextureCopyEntry[] entries;
    GTCXTileBasePipe owner;
    public CoverStorage(GTCXTileBasePipe owner){
        this.owner = owner;
        this.entries = TextureCopyEntry.createArray(6);
    }

    @SideOnly(Side.CLIENT)
    public GTCXModelPipe.QuadList getQuads() {
        GTCXModelPipe.QuadList list = new GTCXModelPipe.QuadList();

        for(int i = 0; i < 6; ++i) {
            list.addQuads(this.entries[i].getQuads(), EnumFacing.getFront(i));
        }

        return list;
    }

    public void setCover(IBlockState cover, EnumFacing facing){
        entries[facing.getIndex()].set(cover, cover, new int[]{-1}, new RetextureEventClassic.Rotation[]{RetextureEventClassic.Rotation.Rotation0}, facing);
    }

    public void removeCover(EnumFacing facing){
        entries[facing.getIndex()].clear();
    }
}
