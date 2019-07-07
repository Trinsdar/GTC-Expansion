package gtc_expansion.block;

import gtc_expansion.GTBlocks2;
import gtc_expansion.tile.GTTileAlloySmelter;
import gtc_expansion.tile.GTTileElectrolyzer;
import gtclassic.GTBlocks;
import gtclassic.block.GTBlockMachine;
import ic2.core.block.base.tile.TileEntityBlock;
import net.minecraft.world.World;

public class GTBlockTile extends GTBlockMachine {
    public GTBlockTile(String name) {
        super(name);
        this.setUnlocalizedName("gtc_expansion." + name.toLowerCase());
    }

    public GTBlockTile(String name, int additionalInfo) {
        super(name, additionalInfo);
        this.setUnlocalizedName("gtc_expansion." + name.toLowerCase());
    }

    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
        if (this == GTBlocks2.electrolyzer) {
            return new GTTileElectrolyzer();
        }
        if (this == GTBlocks2.alloySmelter){
            return new GTTileAlloySmelter();
        }
        return new TileEntityBlock();
    }
}
