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

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        RayTraceResult lookingAt = GTCXWrenchUtils.getBlockLookingAtIgnoreBB(playerIn);
        if (lookingAt != null){
            TileEntity tile = worldIn.getTileEntity(lookingAt.getBlockPos());
            if (tile instanceof GTCXTileBasePipe){
                ItemStack stack = playerIn.getHeldItem(handIn);
                GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
                EnumFacing sideToggled = GTCXWrenchUtils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
                if (sideToggled != null && pipe.anchors.notContains(sideToggled)){
                    pipe.addCover(sideToggled, GTCXBlocks.dummyCover.getStateFromMeta(meta));
                    if (!playerIn.isCreative()) {
                        stack.shrink(1);
                    }
                    playerIn.swingArm(handIn);
                    if (worldIn.isRemote){
                        IC2.audioManager.playOnce(playerIn, PositionSpec.Hand, Ic2Sounds.wrenchUse, true, IC2.audioManager.defaultVolume);
                    }
                    return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
