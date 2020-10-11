package gtc_expansion.oneprobe;

import gtc_expansion.GTCExpansion;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.util.obj.IWrenchableTile;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GTCXProbeHandler implements IProbeInfoProvider {
    @Override
    public String getID() {
        return GTCExpansion.MODID;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof IWrenchableTile){
            IWrenchableTile wrenchableTile = (IWrenchableTile) tile;
            if (wrenchableTile.canRemoveBlock(player) || tile instanceof GTCXTileBasePipe){
                ItemStack mainHand = player.getHeldItemMainhand();
                ItemStack offHand = player.getHeldItemOffhand();
                String sneak = tile instanceof GTCXTileBasePipe ? "Sneak right" : "Right";
                String sneak2 = tile instanceof GTCXTileBasePipe ? "sneak" : "";
                if (mainHand.getItem() instanceof ItemToolWrench || offHand.getItem() instanceof ItemToolWrench){
                    probeInfo.text(sneak + " click block with wrench to get.");
                } else {
                    probeInfo.text("Block require " + sneak2 + " right clicking with wrench to get.");
                }
            } else {
                probeInfo.text("Either block drops with pickaxe, or modder did not do the block correctly.");
            }
        }
    }
}
