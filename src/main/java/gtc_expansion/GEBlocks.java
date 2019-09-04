package gtc_expansion;

import gtc_expansion.block.GEBlockCasing;
import gtc_expansion.block.GEBlockTile;
import gtc_expansion.tile.GETileAlloySmelter;
import gtc_expansion.tile.GETileElectrolyzer;
import gtc_expansion.tile.multi.GETileMultiImplosionCompressor;
import gtc_expansion.tile.multi.GETileMultiIndustrialGrinder;
import gtc_expansion.tile.multi.GETileMultiVacuumFreezer;
import gtc_expansion.util.GELang;
import gtclassic.color.GTColorBlockInterface;
import gtclassic.color.GTColorItemBlock;
import gtclassic.itemblock.GTItemBlockInterface;
import gtclassic.itemblock.GTItemBlockRare;
import ic2.core.IC2;
import ic2.core.item.block.ItemBlockRare;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GEBlocks {
    private GEBlocks() {
        throw new IllegalStateException("Utility class");
    }

    static final List<Block> toRegister = new ArrayList();
    public static GEBlockTile electrolyzer = registerBlock(new GEBlockTile("industrialelectrolyzer", GELang.INDUSTRIAL_ELECTROLYZER, 1));
    public static GEBlockTile alloySmelter = registerBlock(new GEBlockTile("alloysmelter", GELang.ALLOY_SMELTER));
    public static GEBlockTile industrialGrinder = registerBlock(new GEBlockTile("industrialgrinder", GELang.INDUSTRIAL_GRINDER));
    public static GEBlockTile implosionCompressor = registerBlock(new GEBlockTile("implosioncompressor", GELang.IMPLOSION_COMPRESSOR));
    public static GEBlockTile vacuumFreezer = registerBlock(new GEBlockTile("vacuumfreezer", GELang.VACUUM_FREEZER));

    public static GEBlockCasing casingStandard = registerBlock(new GEBlockCasing("standard", 2,100F));
    public static GEBlockCasing casingAdvanced = registerBlock(new GEBlockCasing("advanced", 1, 200F));
    protected static final String[] textureTileBasic = new String[]{"industrialelectrolyzer", "alloysmelter", "industrialgrinder", "vacuumfreezer", "implosioncompressor"};
    public static void registerBlocks() {
        Iterator var0 = toRegister.iterator();
        Block block;
        while(var0.hasNext()) {
            block = (Block)var0.next();
            createBlock(block);
        }

    }

    static <T extends Block> T registerBlock(T block) {
        toRegister.add(block);
        return block;
    }

    public static void createBlock(Block block) {
        IC2.getInstance().createBlock(block, getItemBlock(block));
    }

    static Class<? extends ItemBlockRare> getItemBlock(Block block) {
        if (block instanceof GTItemBlockInterface) {
            return ((GTItemBlockInterface)block).getCustomItemBlock();
        } else {
            return block instanceof GTColorBlockInterface ? GTColorItemBlock.class : GTItemBlockRare.class;
        }
    }

    public static void registerTiles() {
        registerUtil(GETileElectrolyzer.class, "IndustrialElectrolyzer");
        registerUtil(GETileAlloySmelter.class, "AlloySmelter");
        registerUtil(GETileMultiIndustrialGrinder.class, "IndustrialGrinder");
        registerUtil(GETileMultiImplosionCompressor.class, "ImplosionCompressor");
        registerUtil(GETileMultiVacuumFreezer.class, "VacuumFreezer");
    }

    public static void registerUtil(Class tile, String name) {
        GameRegistry.registerTileEntity(tile, new ResourceLocation("gtclassic", "tileEntity" + name));
    }
}
