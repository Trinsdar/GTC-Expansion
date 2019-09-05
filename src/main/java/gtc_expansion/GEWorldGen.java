package gtc_expansion;

import gtclassic.GTWorldGen;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class GEWorldGen implements IWorldGenerator {
    static GTWorldGen instance = new GTWorldGen();
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimensionType().equals(DimensionType.THE_END)){
            if (GEConfiguration.tungstateGenerate){
                instance.generateOre(GEBlocks.oreTungstate.getDefaultState(), GEConfiguration.tungstateSize, GEConfiguration.tungstateWeight, 0, 80, BlockMatcher.forBlock(Blocks.END_STONE), world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.sheldoniteGenerate){
                instance.generateOre(GEBlocks.oreSheldonite.getDefaultState(), GEConfiguration.sheldoniteSize, GEConfiguration.sheldoniteWeight, 0, 80, BlockMatcher.forBlock(Blocks.END_STONE), world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.olivineGenerate){
                instance.generateOre(GEBlocks.oreOlivine.getDefaultState(), GEConfiguration.olivineSize, GEConfiguration.olivineWeight, 0, 80, BlockMatcher.forBlock(Blocks.END_STONE), world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.sodaliteGenerate){
                instance.generateOre(GEBlocks.oreSodalite.getDefaultState(), GEConfiguration.sodaliteSize, GEConfiguration.sodaliteWeight, 0, 80, BlockMatcher.forBlock(Blocks.END_STONE), world, random, chunkX, chunkZ);
            }
        }
        if (world.provider.getDimensionType().equals(DimensionType.NETHER)){
            if (GEConfiguration.pyriteGenerate){
                instance.generateOre(GEBlocks.orePyrite.getDefaultState(), GEConfiguration.pyriteSize, GEConfiguration.pyriteWeight, 0, 64, BlockMatcher.forBlock(Blocks.NETHERRACK), world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.cinnabarGenerate){
                instance.generateOre(GEBlocks.oreCinnabar.getDefaultState(), GEConfiguration.cinnabarSize, GEConfiguration.cinnabarWeight, 64, 128, BlockMatcher.forBlock(Blocks.NETHERRACK), world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.sphaleriteGenerate){
                instance.generateOre(GEBlocks.oreSphalerite.getDefaultState(), GEConfiguration.sphaleriteSize, GEConfiguration.sphaleriteWeight, 32, 96, BlockMatcher.forBlock(Blocks.NETHERRACK), world, random, chunkX, chunkZ);
            }
        }

    }
}
