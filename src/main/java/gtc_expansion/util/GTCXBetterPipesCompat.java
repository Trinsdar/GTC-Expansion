package gtc_expansion.util;

import flyingperson.BetterPipes.BetterPipes;
import flyingperson.BetterPipes.compat.ICompatBase;
import net.minecraft.block.state.IBlockState;

public class GTCXBetterPipesCompat {
    public static boolean isAcceptableBlock(IBlockState state){
        for (ICompatBase compat : BetterPipes.instance.COMPAT_LIST) {
            if (compat.getAcceptedBlocks().contains(state.getBlock())) {
                return true;
            }
        }
        return false;
    }
}
