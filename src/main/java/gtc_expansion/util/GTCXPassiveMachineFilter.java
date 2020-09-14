package gtc_expansion.util;

import gtclassic.api.tile.GTTileBasePassiveMachine;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.item.ItemStack;

public class GTCXPassiveMachineFilter implements IFilter {
    GTTileBasePassiveMachine machine;

    public GTCXPassiveMachineFilter(GTTileBasePassiveMachine machine) {
        this.machine = machine;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return !stack.isEmpty() && this.machine.isValidInput(stack);
    }
}
