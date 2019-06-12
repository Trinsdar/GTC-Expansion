package gtc_expansion.block;

import gtclassic.block.GTBlockTile;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.world.World;

public class GTBlockTile2 extends GTBlockTile {
    TileEntityBlock tile;
    public GTBlockTile2(String name, TileEntityBlock tile) {
        super(name);
        this.tile = tile;
        this.setRegistryName(name.toLowerCase());
        this.setUnlocalizedName("gtc_expansion." + name.toLowerCase());
    }

    public GTBlockTile2(String name, TileEntityBlock tile, int additionalInfo) {
        super(name, additionalInfo);
        this.tile = tile;
        this.setRegistryName(name.toLowerCase());
        this.setUnlocalizedName("gtc_expansion." + name.toLowerCase());
    }

    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
        return tile;
    }
}
