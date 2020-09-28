package gtc_expansion.events;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.interfaces.IGTWrench;
import gtc_expansion.interfaces.IGTScrewdriver;
import gtc_expansion.interfaces.IGTTextureStorageTile;
import gtc_expansion.item.GTCXItemCover;
import gtc_expansion.item.GTCXItemDiamondChainsaw;
import gtc_expansion.item.tools.GTCXItemToolCrowbar;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.render.GTCXRenderer;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import gtc_expansion.tile.wiring.GTCXTileColoredCable;
import gtc_expansion.util.GTCXWrenchUtils;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTBlocks;
import ic2.api.classic.audio.PositionSpec;
import ic2.api.classic.event.FoamEvent;
import ic2.api.classic.event.RetextureEventClassic;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @SubscribeEvent
    public void onTickEvent(TickEvent event) {
        GTCExpansion.instance.counter++;
        if (GTCExpansion.instance.counter % 3 == 0) GTCExpansion.instance.wrenchMap.clear();
    }

    @SubscribeEvent
    public void onEvent(LivingEntityUseItemEvent event) {
        if (event.getItem().getItem() instanceof IGTWrench) event.setCanceled(true);
    }


    @SubscribeEvent
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!(event instanceof PlayerInteractEvent.LeftClickEmpty | event instanceof  PlayerInteractEvent.LeftClickBlock)) {
            ItemStack held = event.getEntityPlayer().getHeldItemMainhand();
            ItemStack offHeld = event.getEntityPlayer().getHeldItemOffhand();
            RayTraceResult lookingAt = GTCXWrenchUtils.getBlockLookingAtIgnoreBB(event.getEntityPlayer());
            EntityPlayer player = event.getEntityPlayer();
            if (lookingAt != null){
                TileEntity tile = event.getWorld().getTileEntity(lookingAt.getBlockPos());
                if (tile instanceof GTCXTileBasePipe) {
                    if (held.getItem() instanceof IGTWrench && !player.isSneaking()) {
                        if (((IGTWrench) held.getItem()).canBeUsed(held)) {
                            if (event.isCancelable()) event.setCanceled(true);
                            if (GTCXWrenchUtils.wrenchUse(lookingAt, (GTCXTileBasePipe)tile, player, event.getWorld(), false) && !GTCExpansion.instance.wrenchMap.contains(lookingAt.getBlockPos())){
                                GTCExpansion.instance.wrenchMap.add(lookingAt.getBlockPos());
                                ((IGTWrench) held.getItem()).damage(held, player);
                            }
                        }
                    } else if (held.getItem() instanceof IGTScrewdriver){
                        GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
                        EnumFacing sideToggled = GTCXWrenchUtils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
                        if (sideToggled != null && pipe.anchors.contains(sideToggled)){
                            if (((IGTScrewdriver)held.getItem()).canScrewdriverBeUsed(held)){
                                if (event.isCancelable()) event.setCanceled(true);
                                if (!GTCExpansion.instance.wrenchMap.contains(lookingAt.getBlockPos())){
                                    GTCExpansion.instance.wrenchMap.add(lookingAt.getBlockPos());
                                    if (pipe.storage.getCoverLogicMap().get(sideToggled).cycleMode(player)){
                                        if (!player.isCreative()) {
                                            ((IGTScrewdriver) held.getItem()).damage(held, player);
                                        }
                                        player.swingArm(EnumHand.MAIN_HAND);
                                        IC2.audioManager.playOnce(player, PositionSpec.Hand, Ic2Sounds.wrenchUse, true, IC2.audioManager.defaultVolume);
                                    }
                                }
                            }
                        }
                    } else if (offHeld.getItem() instanceof IGTWrench && !player.isSneaking()) {
                        if (((IGTWrench) offHeld.getItem()).canBeUsed(offHeld)) {
                            if (event.isCancelable()) event.setCanceled(true);
                            if (GTCXWrenchUtils.wrenchUse(lookingAt, (GTCXTileBasePipe)tile, player, event.getWorld(), true) && !GTCExpansion.instance.wrenchMap.contains(lookingAt.getBlockPos())){
                                GTCExpansion.instance.wrenchMap.add(lookingAt.getBlockPos());
                                ((IGTWrench) offHeld.getItem()).damage(offHeld, player);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null) {
            RayTraceResult lookingAt = GTCXWrenchUtils.getBlockLookingAtIgnoreBB(player);
            if (lookingAt != null) {
                BlockPos pos = lookingAt.getBlockPos();
                TileEntity tile = player.world.getTileEntity(pos);
                if (tile instanceof GTCXTileBasePipe) {
                    ItemStack stack = player.getHeldItemMainhand();
                    ItemStack offHeld = player.getHeldItemOffhand();
                    if (stack.getItem() instanceof IGTWrench) {
                        GTCXRenderer.renderOverlay(player, pos, lookingAt.sideHit, event.getPartialTicks(), ((GTCXTileBasePipe)tile).connection);
                    }else if (stack.getItem() instanceof GTCXItemToolCrowbar || stack.getItem() instanceof IGTScrewdriver || stack.getItem() instanceof GTCXItemCover || FluidUtil.getFluidContained(stack) != null || stack.getItem() == GTCXItems.dataOrbStorage) {
                        GTCXRenderer.renderOverlay(player, pos, lookingAt.sideHit, event.getPartialTicks(), ((GTCXTileBasePipe)tile).anchors);
                    } else if (offHeld.getItem() instanceof IGTWrench) {
                        GTCXRenderer.renderOverlay(player, pos, lookingAt.sideHit, event.getPartialTicks(), ((GTCXTileBasePipe)tile).connection);
                    }
                }
            }
        }
    }
}
