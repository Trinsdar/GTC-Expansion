package gtc_expansion.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;

public interface IGTWrench {
    boolean canBeUsed(ItemStack stack);

    void damage(ItemStack stack, EntityPlayer player);
}
