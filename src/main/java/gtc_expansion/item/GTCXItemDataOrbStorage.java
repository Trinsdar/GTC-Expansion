package gtc_expansion.item;

import gtclassic.GTMod;
import ic2.core.util.misc.StackUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GTCXItemDataOrbStorage extends GTCXItemMisc {
    public GTCXItemDataOrbStorage() {
        super("data_orb_storage", 11, 0, GTMod.MODID + "_items");
        this.setCreativeTab(null);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound nbt = StackUtil.getNbtData(stack);
        if (!nbt.hasKey("Fluid")){
            tooltip.add(I18n.format("No Data Stored... How did you get this?"));
            return;
        }
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("Fluid"));
        if (fluidStack == null) {
            tooltip.add(I18n.format("No Data Stored... How did you get this?"));
            return;
        }
        tooltip.add(I18n.format(fluidStack.amount + "mB of "+ fluidStack.getLocalizedName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
