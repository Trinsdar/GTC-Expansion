package gtc_expansion.item.overrides;

import gtc_expansion.block.GTCXBlockPipe;
import gtc_expansion.interfaces.IGTWrench;
import gtc_expansion.util.GTCXBetterPipesCompat;
import ic2.api.item.ElectricItem;
import ic2.core.item.tool.electric.ItemElectricToolPrecisionWrench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import org.jetbrains.annotations.Nullable;

public class GTCXItemPrecisionWrench extends ItemElectricToolPrecisionWrench implements IGTWrench {
    public GTCXItemPrecisionWrench(){
        super();
        this.setHarvestLevel("wrench", 1);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        if (!ElectricItem.manager.canUse(stack, 100)){
            return -1;
        }
        return super.getHarvestLevel(stack, toolClass, player, blockState);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (state.getBlock() instanceof GTCXBlockPipe || (Loader.isModLoaded("betterpipes") && GTCXBetterPipesCompat.isAcceptableBlock(state))) {
            if (!ElectricItem.manager.canUse(stack, 100)){
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
            ElectricItem.manager.use(stack, 100, entityLiving);
        }

        return state.getBlock() instanceof GTCXBlockPipe;
    }

    public boolean canBeUsed(ItemStack stack, EntityPlayer player) {
        return !player.isSneaking() && ElectricItem.manager.canUse(stack, 100);
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
    public boolean canBeUsed(ItemStack stack) {
        return ElectricItem.manager.canUse(stack, 100);
    }

    @Override
    public void damage(ItemStack stack, EntityPlayer player) {
        ElectricItem.manager.use(stack, 100, player);
    }
}
