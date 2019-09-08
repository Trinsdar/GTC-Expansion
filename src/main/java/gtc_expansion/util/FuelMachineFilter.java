package gtc_expansion.util;

import gtc_expansion.tile.base.GETileFuelBaseMachine;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.item.ItemStack;

public class FuelMachineFilter implements IFilter {
    GETileFuelBaseMachine machine;

    public FuelMachineFilter(GETileFuelBaseMachine machine) {
        this.machine = machine;
    }

    public boolean matches(ItemStack stack) {
        return !stack.isEmpty() && this.machine.isValidInput(stack);
    }
}
