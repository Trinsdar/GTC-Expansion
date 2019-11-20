package gtc_expansion.util;

import gtc_expansion.tile.base.GTCXTileFuelBaseMachine;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.item.ItemStack;

public class FuelMachineFilter implements IFilter {
    GTCXTileFuelBaseMachine machine;

    public FuelMachineFilter(GTCXTileFuelBaseMachine machine) {
        this.machine = machine;
    }

    public boolean matches(ItemStack stack) {
        return !stack.isEmpty() && this.machine.isValidInput(stack);
    }
}
