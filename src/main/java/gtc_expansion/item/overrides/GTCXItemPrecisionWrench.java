package gtc_expansion.item.overrides;

import flyingperson.BetterPipes.IBetterPipesWrench;
import gtc_expansion.block.GTCXBlockPipe;
import gtc_expansion.interfaces.IGTOverlayWrench;
import gtc_expansion.util.GTCXBetterPipesCompat;
import ic2.api.item.ElectricItem;
import ic2.core.item.tool.electric.ItemElectricToolPrecisionWrench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

@Optional.Interface(iface = "flyingperson.BetterPipes.IBetterPipesWrench", modid = "betterpipes")
public class GTCXItemPrecisionWrench extends ItemElectricToolPrecisionWrench implements IGTOverlayWrench, IBetterPipesWrench {
    public GTCXItemPrecisionWrench(){
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

    @Override
    public boolean canBeUsed(ItemStack stack, EntityPlayer player) {
        return !player.isSneaking() && ElectricItem.manager.canUse(stack, 100);
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
