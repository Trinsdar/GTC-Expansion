package gtc_expansion.logic;

import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import net.minecraft.entity.player.EntityPlayer;

public class GTCXPumpModuleLogic extends GTCXBaseCoverLogic {
    public GTCXPumpModuleLogic(GTCXTileBasePipe pipe) {
        super(pipe);
    }

    @Override
    public void onTick() {

    }

    @Override
    public boolean cycleMode(EntityPlayer player) {
        return false;
    }
}
