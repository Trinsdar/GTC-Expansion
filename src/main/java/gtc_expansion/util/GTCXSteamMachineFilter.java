package gtc_expansion.util;

import gtc_expansion.tile.base.GTCXTileBaseSteamMachine;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.item.ItemStack;

public class GTCXSteamMachineFilter implements IFilter {
    GTCXTileBaseSteamMachine machine;

    public GTCXSteamMachineFilter(GTCXTileBaseSteamMachine machine) {
        this.machine = machine;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return !stack.isEmpty() && this.machine.isValidInput(stack);
    }
}
