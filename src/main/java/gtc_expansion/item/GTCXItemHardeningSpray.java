package gtc_expansion.item;

import gtc_expansion.tile.wiring.GTCXTileColoredCable;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTItems;
import ic2.api.item.IBoxable;
import ic2.core.IC2;
import ic2.core.block.wiring.tile.TileEntityCable;
import ic2.core.block.wiring.tile.TileEntityMultipartLuminator;
import ic2.core.platform.registry.Ic2States;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GTCXItemHardeningSpray extends GTCXItemDamageable implements IBoxable {

    public GTCXItemHardeningSpray() {
        super("hardening_spray", 0, 3, 256);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }


    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tile = world.getTileEntity(pos);
        IBlockState state = world.getBlockState(pos);
        ItemStack stack = player.getHeldItem(hand);
        boolean setFoam = false;
        if (state == Ic2States.constructionFoam){
            world.setBlockState(pos, Ic2States.cfWallWhite);
            setFoam = true;
        }
        if (state == Ic2States.constructionFoamReinforced){
            world.setBlockState(pos, Ic2States.reinforcedStone);
            setFoam = true;
        }
        if (tile instanceof TileEntityCable){
            TileEntityCable cable = (TileEntityCable) tile;
            if (cable.foamed == 1){
                cable.changeFoam((byte) 2);
                setFoam = true;
            }
        }
        if (tile instanceof GTCXTileColoredCable){
            GTCXTileColoredCable cable = (GTCXTileColoredCable) tile;
            if (cable.foamed == 1){
                cable.changeFoam((byte) 2);
                setFoam = true;
            }
        }
        if (tile instanceof TileEntityMultipartLuminator){
            TileEntityMultipartLuminator cable = (TileEntityMultipartLuminator) tile;

        }
        if (setFoam){
            if (stack.getItemDamage() < stack.getMaxDamage()) {
                stack.damageItem(1, player);
            } else {
                player.setHeldItem(hand, GTMaterialGen.get(GTItems.sprayCanEmpty));
                if (!IC2.platform.isSimulating()) {
                    player.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemStack) {
        return true;
    }
}
