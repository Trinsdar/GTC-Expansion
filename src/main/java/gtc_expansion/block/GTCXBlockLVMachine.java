package gtc_expansion.block;

import gtc_expansion.tile.overrides.GTCXTileScanner;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.machine.BlockLVMachine;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class GTCXBlockLVMachine extends BlockLVMachine {
    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
        if (Loader.isModLoaded("forestry") && meta == 9){
            return new GTCXTileScanner();
        }
        return super.createNewTileEntity(worldIn, meta);
    }
}
