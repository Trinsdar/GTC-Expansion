package gtc_expansion.events;

import gtc_expansion.interfaces.IGTTextureStorageTile;
import gtc_expansion.item.GTCXItemDiamondChainsaw;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.tile.wiring.GTCXTileColoredCable;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTBlocks;
import ic2.api.classic.event.FoamEvent;
import ic2.api.classic.event.RetextureEventClassic;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;
import java.util.Set;

public class GTCXOtherEvents {


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHarvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
        IBlockState block = event.getState();
        Random random = event.getWorld().rand;
        if (block == GTBlocks.oreRuby.getDefaultState()){
            if (event.isSilkTouching()){
                return;
            }
            if (random.nextInt(Math.max(1, 32/(event.getFortuneLevel() + 1))) == 0){
                event.getDrops().add(GTMaterialGen.getGem(GTCXMaterial.GarnetRed, 1));
            }

        }
    }

    @SubscribeEvent
    public void onRetextureEvent(RetextureEventClassic event){
        if (!event.isApplied() && !event.isCanceled()){
            TileEntity tile = event.getTargetTile();
            if (tile instanceof IGTTextureStorageTile){
                IGTTextureStorageTile storage = (IGTTextureStorageTile) tile;
                if (storage.setStorage(event.getTargetSide(), event.getModelState(), event.getRenderState(), event.getColorMultipliers(), event.getRotations(), event.getRefSide())){
                    event.setApplied(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFoamCheckEvent(FoamEvent.Check event) {
        if (!event.hasCustomTarget()){
            TileEntity tile = event.getTileEntity();
            if (tile instanceof GTCXTileColoredCable) {
                GTCXTileColoredCable cable = (GTCXTileColoredCable)tile;
                if (cable.foamed == 0) {
                    event.setResult(FoamEvent.FoamResult.Cable);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFoamPlaceEvent(FoamEvent.Place event){

        TileEntity tile = event.getTileEntity();
        if (tile instanceof GTCXTileColoredCable) {
            GTCXTileColoredCable cable = (GTCXTileColoredCable)tile;
            if (cable.changeFoam((byte)1)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onBreakSpeedEvent(PlayerEvent.BreakSpeed event){
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() instanceof GTCXItemDiamondChainsaw){
            NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
            GTCXItemDiamondChainsaw item = (GTCXItemDiamondChainsaw) stack.getItem();
            Set<BlockPos> positions = item.getTargetBlocks(player.world, event.getPos(), player);
            if (!positions.isEmpty()){
                int logCount = 1;
                for (BlockPos ignored : positions) {
                    logCount++;
                }
                tag.setInteger("logCount", logCount);
            } else {
                tag.setInteger("logCount", 0);
            }
        }
    }

}
