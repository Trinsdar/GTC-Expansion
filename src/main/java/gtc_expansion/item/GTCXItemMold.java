package gtc_expansion.item;

import net.minecraft.item.ItemStack;

public class GTCXItemMold extends GTCXItemMisc {
    /**
     * Constructor for making a simple item with no action.
     *
     * @param type - String type of mold
     * @param x    - int column
     * @param y    - int row
     */
    public GTCXItemMold(String type, int x, int y) {
        super("mold_" + type, x, y);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack.copy();
    }
}
