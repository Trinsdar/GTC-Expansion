package gtc_expansion.item;

import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import gtc_expansion.util.GTCXWrenchUtils;
import ic2.api.classic.audio.PositionSpec;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class GTCXItemCover extends GTCXItemMisc {
    /**
     * Constructor for making a simple item with no action.
     * Seperate class so I don't have to check the individual items for the overlay
     *
     * @param name - String name for the item
     * @param x    - int column
     * @param y    - int row
     */
    public GTCXItemCover(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (this == GTCXItems.conveyorModule || this == GTCXItems.pumpModule || this == GTCXItems.itemTransportValve || this == GTCXItems.drain){
            TileEntity tile = world.getTileEntity(pos);
            RayTraceResult lookingAt = GTCXWrenchUtils.getBlockLookingAtIgnoreBB(player);
            if (tile instanceof GTCXTileBasePipe && lookingAt != null){
                ItemStack stack = player.getHeldItem(hand);
                GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
                EnumFacing sideToggled = GTCXWrenchUtils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
                int meta = this == GTCXItems.conveyorModule ? 1 : this == GTCXItems.drain ? 2 : this == GTCXItems.itemTransportValve ? 3 : 4;
                if (sideToggled != null && pipe.anchors.notContains(sideToggled)){
                    pipe.addCover(sideToggled, GTCXBlocks.dummyCover.getStateFromMeta(meta));
                    stack.shrink(1);
                    if (world.isRemote){
                        IC2.audioManager.playOnce(player, PositionSpec.Hand, Ic2Sounds.wrenchUse, true, IC2.audioManager.defaultVolume);
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
