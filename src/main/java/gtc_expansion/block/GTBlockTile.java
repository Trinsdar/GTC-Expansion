package gtc_expansion.block;

import gtc_expansion.GTBlocks2;
import gtc_expansion.tile.GTTileAlloySmelter;
import gtc_expansion.tile.GTTileElectrolyzer;
import gtc_expansion.tile.multi.GTTileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GTTileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GTTileMultiVacuumFreezer;
import gtclassic.GTBlocks;
import gtclassic.block.GTBlockMachine;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.world.World;

public class GTBlockTile extends GTBlockMachine {
    public GTBlockTile(String name, LocaleComp comp) {
        this(name, comp, 0);
    }

    public GTBlockTile(String name, LocaleComp comp, int additionalInfo) {
        super(name, comp, additionalInfo);
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
        if (this == GTBlocks2.industrialGrinder){
            return new GTTileMultiIndustrialGrinder();
        }
        if (this == GTBlocks2.implosionCompressor){
            return new GTTileMultiImplosionCompressor();
        }
        if (this == GTBlocks2.vacuumFreezer){
            return new GTTileMultiVacuumFreezer();
        }
        return new TileEntityBlock();
    }
}
