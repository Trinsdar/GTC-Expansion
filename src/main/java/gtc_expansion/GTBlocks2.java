package gtc_expansion;

import gtc_expansion.block.GTBlockTile2;
import gtc_expansion.tile.GTTileElectrolyzer;
import gtclassic.GTBlocks;
import gtclassic.color.GTColorBlockInterface;
import gtclassic.color.GTColorItemBlock;
import gtclassic.itemblock.GTItemBlockInterface;
import gtclassic.itemblock.GTItemBlockRare;
import gtclassic.material.GTMaterialGen;
import ic2.core.IC2;
import ic2.core.item.block.ItemBlockRare;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GTBlocks2 {
    static final List<Block> toRegister = new ArrayList();
    public static GTBlockTile2 electrolyzer = registerBlock(new GTBlockTile2("industrialElectrolyzer", new GTTileElectrolyzer()));
    protected static final String[] textureTileBasic = new String[]{"industrialelectrolyzer"};
    public static void registerBlocks() {
        Iterator var0 = GTMaterialGen.blockMap.values().iterator();

        Block block;
        while(var0.hasNext()) {
            block = (Block)var0.next();
            createBlock(block);
        }

        var0 = toRegister.iterator();

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
        registerUtil(GTTileElectrolyzer.class, "IndustrialElectrolyzer");
    }

    public static void registerUtil(Class tile, String name) {
        GameRegistry.registerTileEntity(tile, new ResourceLocation("gtclassic", "tileEntity" + name));
    }
}
