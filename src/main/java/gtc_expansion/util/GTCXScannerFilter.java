package gtc_expansion.util;

import ic2.core.inventory.filters.IFilter;
import net.minecraft.item.ItemStack;

public class GTCXScannerFilter implements IFilter {
    @Override
    public boolean matches(ItemStack itemStack) {
        return false;
    }
}
