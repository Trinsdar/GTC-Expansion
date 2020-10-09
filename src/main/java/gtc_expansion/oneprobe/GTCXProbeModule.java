package gtc_expansion.oneprobe;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.jetbrains.annotations.Nullable;

public class GTCXProbeModule implements Function<ITheOneProbe, Void> {

    public static void init(){
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "gtc_expansion.oneprobe.GTCXProbeModule");
    }

    @Nullable
    @Override
    public Void apply(@Nullable ITheOneProbe input) {
        if (input != null){
            input.registerProvider(new GTCXProbeHandler());
        }
        return null;
    }
}
