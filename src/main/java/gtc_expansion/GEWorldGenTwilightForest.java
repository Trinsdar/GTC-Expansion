package gtc_expansion;

import twilightforest.world.feature.TFGenCaveStalactite;

public class GEWorldGenTwilightForest {
    public static void init(){
        if (GEConfiguration.olivineGenerate) {
            TFGenCaveStalactite.addStalactite(2, GEBlocks.oreOlivineOverworld.getDefaultState(), 0.6F, 6, 1, 20);
        }
        if (GEConfiguration.tetrahedriteGenerate) {
            TFGenCaveStalactite.addStalactite(1, GEBlocks.oreTetrahedrite.getDefaultState(), 0.7F, 9, 1, 24);
        }
        if (GEConfiguration.cassiteriteGenerate) {
            TFGenCaveStalactite.addStalactite(1, GEBlocks.oreCassiterite.getDefaultState(), 0.7F, 12, 1, 16);
        }
        if (GEConfiguration.galenaGenerate){
            TFGenCaveStalactite.addStalactite(1, GEBlocks.oreGalena.getDefaultState(), 0.5F, 10, 1, 12);
        }
    }
}
