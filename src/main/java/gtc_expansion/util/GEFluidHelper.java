package gtc_expansion.util;

import ic2.core.fluid.IC2Tank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;

public class GEFluidHelper {

    public static boolean doClickableFluidContainerFillThings(EntityPlayer player, EnumHand hand, World world, BlockPos pos, IC2Tank tank) {
        ItemStack playerStack = player.getHeldItem(hand);
        if (!playerStack.isEmpty()) {
            FluidActionResult result2 = FluidUtil.tryFillContainer(playerStack, tank, tank.getCapacity(), player, true);
            if (result2.isSuccess()){
                playerStack.shrink(1);
                ItemStack resultStack = result2.getResult();
                if (!resultStack.isEmpty()) {
                    if (!player.inventory.addItemStackToInventory(resultStack)) {
                        player.dropItem(resultStack, false);
                    }
                }
                return true;
            }
        }

        return false;
    }
}
