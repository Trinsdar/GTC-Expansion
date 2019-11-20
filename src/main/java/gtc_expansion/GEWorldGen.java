package gtc_expansion;

import gtclassic.api.world.GTOreGenerator;
import gtclassic.common.GTWorldGen;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class GEWorldGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        Biome biomegenbase = world.getBiome(new BlockPos(chunkX * 16 + 16, 128, chunkZ * 16 + 16));
        if (world.provider.getDimensionType().equals(DimensionType.THE_END)){
            GTOreGenerator.generateBasicVein(GEBlocks.oreTungstate, GEConfiguration.generation.tungstateGenerate, GEConfiguration.generation.tungstateSize, GEConfiguration.generation.tungstateWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GEBlocks.oreSheldonite, GEConfiguration.generation.sheldoniteGenerate, GEConfiguration.generation.sheldoniteSize, GEConfiguration.generation.sheldoniteWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GEBlocks.oreOlivine, GEConfiguration.generation.olivineGenerate, GEConfiguration.generation.olivineSize, GEConfiguration.generation.olivineWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GEBlocks.oreSodalite, GEConfiguration.generation.sodaliteGenerate, GEConfiguration.generation.sodaliteSize, GEConfiguration.generation.sodaliteWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
        }
        if (world.provider.getDimensionType().equals(DimensionType.OVERWORLD)){
            GTOreGenerator.generateBasicVein(GEBlocks.oreGalena, GEConfiguration.generation.galenaGenerate, GEConfiguration.generation.galenaSize, GEConfiguration.generation.galenaWeight, 0, 64, Blocks.STONE, world, random, chunkX, chunkZ);
            if ((BiomeDictionary.hasType(biomegenbase, Type.COLD) || BiomeDictionary.hasType(biomegenbase, Type.CONIFEROUS) || BiomeDictionary.hasType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.hasType(biomegenbase, Type.MUSHROOM) && !BiomeDictionary.hasType(biomegenbase, Type.JUNGLE))){
                GTOreGenerator.generateBasicVein(GEBlocks.oreCassiterite, GEConfiguration.generation.cassiteriteGenerate, GEConfiguration.generation.cassiteriteSize, GEConfiguration.generation.cassiteriteWeight, 30, 80, Blocks.STONE, world, random, chunkX, chunkZ);
            }
            if ((BiomeDictionary.hasType(biomegenbase, Type.JUNGLE) || BiomeDictionary.hasType(biomegenbase, Type.SWAMP) || BiomeDictionary.hasType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.hasType(biomegenbase, Type.MUSHROOM)) && !BiomeDictionary.hasType(biomegenbase, Type.COLD)){
                GTOreGenerator.generateBasicVein(GEBlocks.oreTetrahedrite, GEConfiguration.generation.tetrahedriteGenerate, GEConfiguration.generation.tetrahedriteSize, GEConfiguration.generation.tetrahedriteWeight, 20, 70, Blocks.STONE, world, random, chunkX, chunkZ);
            }
        }
        if (world.provider.getDimensionType().equals(DimensionType.NETHER)){
            GTWorldGen worldGen = new GTWorldGen();
            GTOreGenerator.generateBasicVein(GEBlocks.orePyrite, GEConfiguration.generation.pyriteGenerate, GEConfiguration.generation.pyriteSize, GEConfiguration.generation.pyriteWeight, 0, 64, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GEBlocks.oreCinnabar, GEConfiguration.generation.cinnabarGenerate, GEConfiguration.generation.cinnabarSize, GEConfiguration.generation.cinnabarWeight, 64, 128, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GEBlocks.oreSphalerite, GEConfiguration.generation.sphaleriteGenerate, GEConfiguration.generation.sphaleriteSize, GEConfiguration.generation.sphaleriteWeight, 32, 96, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
        }
    }
}
