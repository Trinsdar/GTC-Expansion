package gtc_expansion.util;

import net.minecraft.util.EnumFacing;

public interface IGTCasingBackgroundBlock {
    int getCasing();

    void setCasing();

    int getConfig();

    void setConfig();

    EnumFacing getFacing();

    boolean getActive();
}
