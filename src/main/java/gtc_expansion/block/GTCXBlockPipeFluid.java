package gtc_expansion.block;

import gtc_expansion.tile.pipes.GTCXTileBaseFluidPipe;
import gtc_expansion.util.GTCXHelperPipe;
import gtclassic.api.material.GTMaterial;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.world.World;

public class GTCXBlockPipeFluid extends GTCXBlockPipe {
    int transferRate;
    public GTCXBlockPipeFluid(String name,  GTMaterial material, GTCXHelperPipe.GTPipeModel type, int transferRate) {
        super(name,  material, type);
        this.transferRate = transferRate;
    }

    @Override
    public TileEntityBlock createNewTileEntity(World world, int i) {
        return new GTCXTileBaseFluidPipe(transferRate);
    }
}
