package gtc_expansion.oneprobe;

import ic2.api.classic.addon.IModul;
import ic2.core.IC2;
import ic2.topIntigration.SubModul;
import net.minecraftforge.fml.common.Loader;

public class GTCXOneProbePlugin {

    public static void init(){
        if (IC2.loader.getPlugin("top", IModul.class) != null){
            GTCXProbeModule.init();
        }
    }

}
