package gtc_expansion.util;

import gtc_expansion.model.GTCXModelPipe;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.core.block.base.util.texture.TextureCopyEntry;
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
        GTCXModelPipe.QuadList list = new GTCXModelPipe.QuadList(owner.getConnections());

        for(int i = 0; i < 6; ++i) {
            list.addQuads(this.entries[i].getQuads(), EnumFacing.getFront(i));
        }

        return list;
    }
}
