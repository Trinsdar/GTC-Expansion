package gtc_expansion.block;

import gtclassic.block.GTBlockMachine;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.world.World;

public class GTBlockTile extends GTBlockMachine {
    TileEntityBlock tile;
    public GTBlockTile(String name, TileEntityBlock tile) {
        super(name);
        this.tile = tile;
        this.setUnlocalizedName("gtc_expansion." + name.toLowerCase());
    }

    public GTBlockTile(String name, TileEntityBlock tile, int additionalInfo) {
        super(name, additionalInfo);
        this.tile = tile;
        this.setUnlocalizedName("gtc_expansion." + name.toLowerCase());
    }

    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
        return tile;
    }
}
