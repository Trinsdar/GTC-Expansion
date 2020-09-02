package gtc_expansion.logic;

import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

public class GTCXNullLogic extends GTCXBaseCoverLogic {
    public GTCXNullLogic(GTCXTileBasePipe pipe, EnumFacing facing) {
        super(pipe, facing);
    }

    @Override
    public void onTick() {

    }

    @Override
    public boolean cycleMode(EntityPlayer player) {
        return false;
    }
}
