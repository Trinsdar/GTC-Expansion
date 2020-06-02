package gtc_expansion;

import gtc_expansion.data.GTCXBlocks;
import twilightforest.world.feature.TFGenCaveStalactite;

public class GTCXWorldGenTwilightForest {
    public static void init(){
        if (GTCXConfiguration.generation.olivineGenerate) {
            TFGenCaveStalactite.addStalactite(2, GTCXBlocks.oreOlivineOverworld.getDefaultState(), 0.6F, 6, 1, 20);
        }
        if (GTCXConfiguration.generation.tetrahedriteGenerate) {
            TFGenCaveStalactite.addStalactite(1, GTCXBlocks.oreTetrahedrite.getDefaultState(), 0.7F, 9, 1, 24);
        }
        if (GTCXConfiguration.generation.cassiteriteGenerate) {
            TFGenCaveStalactite.addStalactite(1, GTCXBlocks.oreCassiterite.getDefaultState(), 0.7F, 12, 1, 16);
        }
        if (GTCXConfiguration.generation.galenaGenerate){
            TFGenCaveStalactite.addStalactite(1, GTCXBlocks.oreGalena.getDefaultState(), 0.5F, 10, 1, 12);
        }
    }
}
