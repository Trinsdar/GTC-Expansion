package gtc_expansion.item.overrides;

import ic2.core.item.upgrades.ItemUpgradeModul;
import ic2.core.item.upgrades.subtypes.machine.IUpgradeMetaItem;
import net.minecraft.item.ItemStack;

public class GTCXItemUpgrade extends ItemUpgradeModul {
    public GTCXItemUpgrade(){
        super();
    }
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stack.getMetadata() == 12 ? 4 : super.getItemStackLimit(stack);
    }
}
