package gtc_expansion;

import gtc_expansion.material.GEMaterial;
import gtclassic.GTWorldGen;
import gtclassic.material.GTMaterialGen;
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
                instance.generate(GEBlocks.oreTungstate, GEConfiguration.tungstateSize, GEConfiguration.tungstateWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.sheldoniteGenerate){
                instance.generate(GEBlocks.oreSheldonite, GEConfiguration.sheldoniteSize, GEConfiguration.sheldoniteWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.olivineGenerate){
                instance.generate(GEBlocks.oreOlivine, GEConfiguration.olivineSize, GEConfiguration.olivineWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.sodaliteGenerate){
                instance.generate(GEBlocks.oreSodalite, GEConfiguration.sodaliteSize, GEConfiguration.sodaliteWeight, 0, 80, Blocks.END_STONE, world, random, chunkX, chunkZ);
            }
        }
        if (world.provider.getDimensionType().equals(DimensionType.NETHER)){
            GTWorldGen worldGen = new GTWorldGen();
            worldGen.generateFluid(GTMaterialGen.getFluidBlock(GEMaterial.Nickel), 3, 1, 0, 64, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            worldGen.generateFluid(GTMaterialGen.getFluidBlock(GEMaterial.Invar), 3, 1, 0, 64, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            if (GEConfiguration.pyriteGenerate){
                instance.generate(GEBlocks.orePyrite, GEConfiguration.pyriteSize, GEConfiguration.pyriteWeight, 0, 64, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.cinnabarGenerate){
                instance.generate(GEBlocks.oreCinnabar, GEConfiguration.cinnabarSize, GEConfiguration.cinnabarWeight, 64, 128, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            }
            if (GEConfiguration.sphaleriteGenerate){
                instance.generate(GEBlocks.oreSphalerite, GEConfiguration.sphaleriteSize, GEConfiguration.sphaleriteWeight, 32, 96, Blocks.NETHERRACK, world, random, chunkX, chunkZ);
            }
        }

    }
}
