package gtc_expansion;

import gtc_expansion.material.GEMaterial;
import gtclassic.GTWorldGen;
import gtclassic.material.GTMaterialGen;
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
    static GTWorldGen instance = new GTWorldGen();
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        Biome biomegenbase = world.getBiome(new BlockPos(chunkX * 16 + 16, 128, chunkZ * 16 + 16));
        if (world.provider.getDimensionType().equals(DimensionType.THE_END)){
            instance.generate(GEBlocks.oreTungstate, GEConfiguration.tungstateGenerate, GEConfiguration.tungstateSize, GEConfiguration.tungstateWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            instance.generate(GEBlocks.oreSheldonite, GEConfiguration.sheldoniteGenerate, GEConfiguration.sheldoniteSize, GEConfiguration.sheldoniteWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            instance.generate(GEBlocks.oreOlivine, GEConfiguration.olivineGenerate, GEConfiguration.olivineSize, GEConfiguration.olivineWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            instance.generate(GEBlocks.oreSodalite, GEConfiguration.sodaliteGenerate, GEConfiguration.sodaliteSize, GEConfiguration.sodaliteWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
        }
        if (world.provider.getDimensionType().equals(DimensionType.OVERWORLD)){
            instance.generate(GEBlocks.oreGalena, GEConfiguration.galenaGenerate, GEConfiguration.galenaSize, GEConfiguration.galenaWeight, 0, 64, Blocks.STONE, world, random, chunkX, chunkZ);
            if ((BiomeDictionary.hasType(biomegenbase, Type.COLD) || BiomeDictionary.hasType(biomegenbase, Type.CONIFEROUS) || BiomeDictionary.hasType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.hasType(biomegenbase, Type.MUSHROOM) && !BiomeDictionary.hasType(biomegenbase, Type.JUNGLE))){
                instance.generate(GEBlocks.oreCassiterite, GEConfiguration.cassiteriteGenerate, GEConfiguration.cassiteriteSize, GEConfiguration.cassiteriteWeight, 30, 80, Blocks.STONE, world, random, chunkX, chunkZ);
            }
            if ((BiomeDictionary.hasType(biomegenbase, Type.JUNGLE) || BiomeDictionary.hasType(biomegenbase, Type.SWAMP) || BiomeDictionary.hasType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.hasType(biomegenbase, Type.MUSHROOM)) && !BiomeDictionary.hasType(biomegenbase, Type.COLD)){
                instance.generate(GEBlocks.oreTetrahedrite, GEConfiguration.tetrahedriteGenerate, GEConfiguration.tetrahedriteSize, GEConfiguration.tetrahedriteWeight, 20, 70, Blocks.STONE, world, random, chunkX, chunkZ);
            }
        }
        if (world.provider.getDimensionType().equals(DimensionType.NETHER)){
            GTWorldGen worldGen = new GTWorldGen();
            instance.generate(GEBlocks.orePyrite, GEConfiguration.pyriteGenerate, GEConfiguration.pyriteSize, GEConfiguration.pyriteWeight, 0, 64, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            instance.generate(GEBlocks.oreCinnabar, GEConfiguration.cinnabarGenerate, GEConfiguration.cinnabarSize, GEConfiguration.cinnabarWeight, 64, 128, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            instance.generate(GEBlocks.oreSphalerite, GEConfiguration.sphaleriteGenerate, GEConfiguration.sphaleriteSize, GEConfiguration.sphaleriteWeight, 32, 96, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
        }
    }
}
