package gtc_expansion.tile.pipes;

import gtc_expansion.util.CoverStorage;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3i;

public class GTCXTileBasePipe extends TileEntityMachine {
    @NetworkField(index = 8)
    public RotationList connection;
    @NetworkField(
            index = 9
    )
    public RotationList anchors;
    @NetworkField(
            index = 10
    )
    public CoverStorage storage;
    public GTCXTileBasePipe(int slots) {
        super(slots);
        this.storage = new CoverStorage(this);
        this.connection = RotationList.EMPTY;
        this.anchors = RotationList.EMPTY;
        this.addNetworkFields("connection", "storage", "anchors");
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    public Vec3i getConnections() {
        return new Vec3i(this.connection.getCode(), this.anchors.getCode(), this.connection.getCode() << 6 | this.anchors.getCode());
    }
}
