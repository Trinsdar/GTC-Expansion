package gtc_expansion.block;

import gtc_expansion.tile.pipes.GTCXTileBaseItemPipe;
import gtc_expansion.util.GTCXHelperPipe;
import gtclassic.api.material.GTMaterial;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.world.World;

public class GTCXBlockPipeItem extends GTCXBlockPipe {
    public GTCXBlockPipeItem(GTMaterial material, GTCXHelperPipe.GTPipeModel type) {
        super(material, type);
    }

    @Override
    public TileEntityBlock createNewTileEntity(World world, int i) {
        return new GTCXTileBaseItemPipe();
    }
}
