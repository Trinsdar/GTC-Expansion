package gtc_expansion.item;

import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import gtc_expansion.util.GTCXWrenchUtils;
import ic2.api.classic.audio.PositionSpec;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class GTCXItemCover extends GTCXItemMisc {
    protected int meta;
    /**
     * Constructor for making a simple item with no action.
     * Seperate class so I don't have to check the individual items for the overlay
     *
     * @param name - String name for the item
     * @param x    - int column
     * @param y    - int row
     * @param meta - int meta of cover blockstate
     */
    public GTCXItemCover(String name, int x, int y, int meta) {
        super(name, x, y);
        this.meta = meta;
    }

    public int getMeta() {
        return meta;
    }
}
