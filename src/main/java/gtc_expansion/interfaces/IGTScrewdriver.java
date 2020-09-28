package gtc_expansion.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IGTScrewdriver {
    boolean canScrewdriverBeUsed(ItemStack stack);

    void damage(ItemStack stack, EntityPlayer player);
}
