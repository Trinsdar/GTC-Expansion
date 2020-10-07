package gtc_expansion.util;

import ic2.api.recipe.IRecipeInput;
import ic2.core.item.recipe.entry.RecipeInputElectricItem;
import ic2.core.util.misc.StackUtil;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GTCXBatteryInput extends RecipeInputElectricItem {
    public GTCXBatteryInput(ItemStack item) {
        super(item);
    }

    @Override
    public boolean matches(ItemStack subject) {
        return !this.item.isEmpty() && !subject.isEmpty() && this.item.getItem() == subject.getItem();
    }

    @Override
    public List<ItemStack> getInputs() {
        List<ItemStack> list = new ArrayList();
        list.add(this.item.copy());
        return list;
    }
}
