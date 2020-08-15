package gtc_expansion.block;

import gtc_expansion.tile.pipes.GTCXTileBaseFluidPipe;
import gtc_expansion.util.GTCXHelperPipe;
import gtclassic.api.material.GTMaterial;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GTCXBlockPipeFluid extends GTCXBlockPipe {
    int transferRate;
    public GTCXBlockPipeFluid(String name,  GTMaterial material, GTCXHelperPipe.GTPipeModel type, int transferRate) {
        super(name,  material, type);
        this.transferRate = transferRate;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
                                ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof GTCXTileBaseFluidPipe){
            GTCXTileBaseFluidPipe pipe = (GTCXTileBaseFluidPipe) tile;
            pipe.setTransferRate(transferRate);
        }
    }

    @Override
    public TileEntityBlock createNewTileEntity(World world, int i) {
        return new GTCXTileBaseFluidPipe();
    }
}
