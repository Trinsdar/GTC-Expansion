package gtc_expansion.tile.pipes;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;

public class GTCXTileBaseItemPipe extends GTCXTileBasePipe {
    public GTCXTileBaseItemPipe() {
        super(1);
    }

    @Override
    public boolean canConnectWithoutColor(TileEntity tile, EnumFacing facing) {
        return tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing) || tile instanceof GTCXTileBaseItemPipe;
    }
}
