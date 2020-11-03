package gtc_expansion.events;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.interfaces.IGTWrench;
import gtc_expansion.interfaces.IGTScrewdriver;
import gtc_expansion.interfaces.IGTTextureStorageTile;
import gtc_expansion.item.GTCXItemCover;
import gtc_expansion.item.GTCXItemDiamondChainsaw;
import gtc_expansion.item.tools.GTCXItemToolCrowbar;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.render.GTCXRenderer;
import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import gtc_expansion.tile.wiring.GTCXTileColoredCable;
import gtc_expansion.util.GTCXWrenchUtils;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTConfig;
import ic2.api.classic.audio.PositionSpec;
import ic2.api.classic.event.FoamEvent;
import ic2.api.classic.event.RetextureEventClassic;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

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
    public void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event){
        if (event.getSide() == Side.SERVER){
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            if (state.getBlock() == Blocks.CAULDRON && state.getValue(BlockCauldron.LEVEL) > 0){
                EntityPlayer player = event.getEntityPlayer();
                EnumHand hand = event.getHand();
                ItemStack stack = player.getHeldItem(hand);
                if (!stack.isEmpty()){
                    GTRecipeMultiInputList.MultiRecipe recipe = getRecipe(stack);
                    if (recipe != null){
                        if (!player.capabilities.isCreativeMode) {
                            player.getHeldItem(hand).shrink(recipe.getInput(0).getAmount());
                        }
                        Blocks.CAULDRON.setWaterLevel(event.getWorld(), event.getPos(), state, state.getValue(BlockCauldron.LEVEL) - 1);
                        for (ItemStack output : recipe.getOutputs().getAllOutputs()) {
                            ItemHandlerHelper.giveItemToPlayer(player, output);
                        }
                        event.getWorld().playSound(null, event.getPos(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F,
                                1.0F);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private GTRecipeMultiInputList.MultiRecipe getRecipe(ItemStack input){
        GTRecipeMultiInputList.MultiRecipe recipe = GTCXRecipeLists.CAULDRON_RECIPE_LIST.getPriorityRecipe(t -> checkRecipe(t, input));
        return recipe == GTRecipeMultiInputList.INVALID_RECIPE ? null : recipe;
    }

    public boolean checkRecipe(GTRecipeMultiInputList.MultiRecipe entry, ItemStack input) {
        IRecipeInput key = entry.getInputs().get(0);
        int toFind = key.getAmount();
        if (key.matches(input)) {
            return input.getCount() >= toFind;
        }
        return false;
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
                    if (held.getItem() instanceof IGTWrench && !player.isSneaking() && ((IGTWrench) held.getItem()).canBeUsed(held)) {
                        if (event.isCancelable()) event.setCanceled(true);
                        if (GTCXWrenchUtils.wrenchUse(lookingAt, (GTCXTileBasePipe)tile, player, event.getWorld(), false) && !GTCExpansion.instance.wrenchMap.contains(lookingAt.getBlockPos())){
                            GTCExpansion.instance.wrenchMap.add(lookingAt.getBlockPos());
                            ((IGTWrench) held.getItem()).damage(held, player);
                        }
                    } else if ((held.getItem() instanceof IGTScrewdriver && ((IGTScrewdriver) held.getItem()).canScrewdriverBeUsed(held))){
                        GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
                        EnumFacing sideToggled = GTCXWrenchUtils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
                        if (sideToggled != null && pipe.anchors.contains(sideToggled)){
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
                    } else if (held.getItem() instanceof GTCXItemCover){
                        int meta = ((GTCXItemCover)held.getItem()).getMeta();
                        GTCXTileBasePipe pipe = (GTCXTileBasePipe) tile;
                        EnumFacing sideToggled = GTCXWrenchUtils.getDirection(lookingAt.sideHit, lookingAt.hitVec);
                        if (event.isCancelable()) event.setCanceled(true);
                        if (sideToggled != null && pipe.anchors.notContains(sideToggled)){
                            pipe.addCover(sideToggled, GTCXBlocks.dummyCover.getStateFromMeta(meta));
                            if (!player.isCreative()) {
                                held.shrink(1);
                            }
                            player.swingArm(EnumHand.MAIN_HAND);
                            if (event.getWorld().isRemote){
                                IC2.audioManager.playOnce(player, PositionSpec.Hand, Ic2Sounds.wrenchUse, true, IC2.audioManager.defaultVolume);
                            }
                        }
                    } else if (offHeld.getItem() instanceof IGTWrench && !player.isSneaking() && ((IGTWrench) offHeld.getItem()).canBeUsed(offHeld)) {
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
                    if (stack.getItem() instanceof IGTWrench && ((IGTWrench) stack.getItem()).canBeUsed(stack)) {
                        GTCXRenderer.renderOverlay(player, pos, lookingAt.sideHit, event.getPartialTicks(), ((GTCXTileBasePipe)tile).connection);
                    }else if (stack.getItem() instanceof GTCXItemToolCrowbar || (stack.getItem() instanceof IGTScrewdriver && ((IGTScrewdriver) stack.getItem()).canScrewdriverBeUsed(stack)) || stack.getItem() instanceof GTCXItemCover || FluidUtil.getFluidContained(stack) != null || stack.getItem() == GTCXItems.dataOrbStorage) {
                        GTCXRenderer.renderOverlay(player, pos, lookingAt.sideHit, event.getPartialTicks(), ((GTCXTileBasePipe)tile).anchors);
                    } else if (offHeld.getItem() instanceof IGTWrench && ((IGTWrench) offHeld.getItem()).canBeUsed(offHeld)) {
                        GTCXRenderer.renderOverlay(player, pos, lookingAt.sideHit, event.getPartialTicks(), ((GTCXTileBasePipe)tile).connection);
                    }
                }
            }
        }
    }
}
