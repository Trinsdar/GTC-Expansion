package gtc_expansion.util;

import gtc_expansion.tile.GTCXTileDustbin;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.item.ItemStack;

public class GTCXDustbinFilter implements IFilter {

	GTCXTileDustbin machine;

	public GTCXDustbinFilter(GTCXTileDustbin machine) {
		this.machine = machine;
	}

	public boolean matches(ItemStack stack) {
		return !stack.isEmpty() && this.machine.isValidInput(stack);
	}
}
