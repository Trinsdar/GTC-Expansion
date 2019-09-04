package gtc_expansion.block;

import gtc_expansion.GEBlocks;
import gtc_expansion.tile.GETileAlloySmelter;
import gtc_expansion.tile.GETileElectrolyzer;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GETileMultiVacuumFreezer;
import gtclassic.block.GTBlockMachine;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.world.World;

public class GEBlockTile extends GTBlockMachine {
    public GEBlockTile(String name, LocaleComp comp) {
        this(name, comp, 0);
    }

    public GEBlockTile(String name, LocaleComp comp, int additionalInfo) {
        super(name, comp, additionalInfo);
    }

    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
        if (this == GEBlocks.electrolyzer) {
            return new GETileElectrolyzer();
        }
        if (this == GEBlocks.alloySmelter){
            return new GETileAlloySmelter();
        }
        if (this == GEBlocks.industrialGrinder){
            return new GETileMultiIndustrialGrinder();
        }
        if (this == GEBlocks.implosionCompressor){
            return new GETileMultiImplosionCompressor();
        }
        if (this == GEBlocks.vacuumFreezer){
            return new GETileMultiVacuumFreezer();
        }
        return new TileEntityBlock();
    }
}
