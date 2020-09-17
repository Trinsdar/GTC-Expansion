package gtc_expansion;

import gtc_expansion.data.GTCXBlocks;
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

public class GTCXWorldGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        Biome biomegenbase = world.getBiome(new BlockPos(chunkX * 16 + 16, 128, chunkZ * 16 + 16));
        if (world.provider.getDimensionType().equals(DimensionType.THE_END)){
            GTOreGenerator.generateBasicVein(GTCXBlocks.oreTungstate, GTCXConfiguration.generation.tungstateGenerate, GTCXConfiguration.generation.tungstateSize, GTCXConfiguration.generation.tungstateWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GTCXBlocks.oreSheldonite, GTCXConfiguration.generation.sheldoniteGenerate, GTCXConfiguration.generation.sheldoniteSize, GTCXConfiguration.generation.sheldoniteWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GTCXBlocks.oreOlivine, GTCXConfiguration.generation.olivineGenerate, GTCXConfiguration.generation.olivineSize, GTCXConfiguration.generation.olivineWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GTCXBlocks.oreSodalite, GTCXConfiguration.generation.sodaliteGenerate, GTCXConfiguration.generation.sodaliteSize, GTCXConfiguration.generation.sodaliteWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GTCXBlocks.oreChromite, GTCXConfiguration.generation.chromiteGenerate, GTCXConfiguration.generation.chromiteSize, GTCXConfiguration.generation.chromiteWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
        }
        if (world.provider.getDimensionType().equals(DimensionType.OVERWORLD)){
            GTOreGenerator.generateBasicVein(GTCXBlocks.oreGalena, GTCXConfiguration.generation.galenaGenerate, GTCXConfiguration.generation.galenaSize, GTCXConfiguration.generation.galenaWeight, 0, 64, Blocks.STONE, world, random, chunkX, chunkZ);
            if (BiomeDictionary.hasType(biomegenbase, Type.MOUNTAIN)){
                GTOreGenerator.generateBasicVein(Blocks.EMERALD_ORE, GTCXConfiguration.generation.emeraldGenerate, GTCXConfiguration.generation.emeraldSize, GTCXConfiguration.generation.emeraldWeight, 0, 32, Blocks.STONE, world, random, chunkX, chunkZ);
            }
            if ((BiomeDictionary.hasType(biomegenbase, Type.COLD) || BiomeDictionary.hasType(biomegenbase, Type.CONIFEROUS) || BiomeDictionary.hasType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.hasType(biomegenbase, Type.MUSHROOM) && !BiomeDictionary.hasType(biomegenbase, Type.JUNGLE))){
                GTOreGenerator.generateBasicVein(GTCXBlocks.oreCassiterite, GTCXConfiguration.generation.cassiteriteGenerate, GTCXConfiguration.generation.cassiteriteSize, GTCXConfiguration.generation.cassiteriteWeight, 30, 80, Blocks.STONE, world, random, chunkX, chunkZ);
            }
            if ((BiomeDictionary.hasType(biomegenbase, Type.JUNGLE) || BiomeDictionary.hasType(biomegenbase, Type.SWAMP) || BiomeDictionary.hasType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.hasType(biomegenbase, Type.MUSHROOM)) && !BiomeDictionary.hasType(biomegenbase, Type.COLD)){
                GTOreGenerator.generateBasicVein(GTCXBlocks.oreTetrahedrite, GTCXConfiguration.generation.tetrahedriteGenerate, GTCXConfiguration.generation.tetrahedriteSize, GTCXConfiguration.generation.tetrahedriteWeight, 20, 70, Blocks.STONE, world, random, chunkX, chunkZ);
            }
        }
        if (world.provider.getDimensionType().equals(DimensionType.NETHER)){
            GTWorldGen worldGen = new GTWorldGen();
            GTOreGenerator.generateBasicVein(GTCXBlocks.orePyrite, GTCXConfiguration.generation.pyriteGenerate, GTCXConfiguration.generation.pyriteSize, GTCXConfiguration.generation.pyriteWeight, 0, 64, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GTCXBlocks.oreCinnabar, GTCXConfiguration.generation.cinnabarGenerate, GTCXConfiguration.generation.cinnabarSize, GTCXConfiguration.generation.cinnabarWeight, 64, 128, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            GTOreGenerator.generateBasicVein(GTCXBlocks.oreSphalerite, GTCXConfiguration.generation.sphaleriteGenerate, GTCXConfiguration.generation.sphaleriteSize, GTCXConfiguration.generation.sphaleriteWeight, 32, 96, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
        }
    }
}
