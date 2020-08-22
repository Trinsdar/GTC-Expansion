package gtc_expansion.interfaces;

import net.minecraft.item.ItemStack;

public interface IGTOverlayWrench {
    boolean canBeUsed(ItemStack stack);

    void damage(ItemStack stack);
}
