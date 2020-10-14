package gtc_expansion.item.overrides;

import gtc_expansion.block.GTCXBlockPipe;
import gtc_expansion.interfaces.IGTWrench;
import gtc_expansion.util.GTCXBetterPipesCompat;
import gtc_expansion.util.GTCXIc2cECompat;
import gtclassic.api.helpers.GTValues;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.item.tool.electric.ItemElectricToolWrench;
import ic2.core.util.obj.ToolTipType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class GTCXItemElectricWrench extends ItemElectricToolWrench implements IGTWrench {
    boolean overrideLossChance = false;
    public GTCXItemElectricWrench(){
        super();
        this.setHarvestLevel("wrench", 1);
        if (Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS)){
            this.overrideLossChance = GTCXIc2cECompat.isOverridingLossyWrench();
        }
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        if (!ElectricItem.manager.canUse(stack, 50)){
            return -1;
        }
        return super.getHarvestLevel(stack, toolClass, player, blockState);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (state.getBlock() instanceof GTCXBlockPipe || (Loader.isModLoaded("betterpipes") && GTCXBetterPipesCompat.isAcceptableBlock(state))) {
            if (!ElectricItem.manager.canUse(stack, 50)){
                return 0.0F;
            }
            return ToolMaterial.IRON.getEfficiency();
        }
        return super.getDestroySpeed(stack, state);
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (!worldIn.isRemote && state.getBlock() instanceof GTCXBlockPipe)
        {
            ElectricItem.manager.use(stack, 50, entityLiving);
        }

        return state.getBlock() instanceof GTCXBlockPipe;
    }

    public boolean canBeUsed(ItemStack stack, EntityPlayer player) {
        return !player.isSneaking() && ElectricItem.manager.canUse(stack, 50);
    }

    @Override
    public boolean canBeUsed(ItemStack stack) {
        return ElectricItem.manager.canUse(stack, 50);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack empty = new ItemStack(this, 1, 0);
            ItemStack full = new ItemStack(this, 1, 0);
            ElectricItem.manager.discharge(empty, 2.147483647E9D, 2147483647, true, false, false);
            ElectricItem.manager.charge(full, 2.147483647E9D, 2147483647, true, false);
            items.add(empty);
            items.add(full);
        }
    }

    @Override
    public void damage(ItemStack stack, EntityPlayer player) {
        ElectricItem.manager.use(stack, 50, player);
    }

    /* Ic2c extras stuff */
    @Override
    public boolean canOverrideLossChance(ItemStack stack) {
        return overrideLossChance || super.canOverrideLossChance(stack);
    }

    @Override
    public boolean hasBigCost(ItemStack stack) {
        return !overrideLossChance;
    }

    @Override
    public void onSortedItemToolTip(ItemStack stack, EntityPlayer player, boolean debugTooltip, List<String> tooltip, Map<ToolTipType, List<String>> sortedTooltip) {
        super.onSortedItemToolTip(stack, player, debugTooltip, tooltip, sortedTooltip);
        if (overrideLossChance){
            tooltip.remove(1);
            // removing from same index twice since on the first remove index 2 becomes index 1
            tooltip.remove(1);
            sortedTooltip.get(ToolTipType.Ctrl).clear();
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (IC2.platform.isSimulating() && IC2.keyboard.isModeSwitchKeyDown(playerIn) && overrideLossChance) {
            return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
