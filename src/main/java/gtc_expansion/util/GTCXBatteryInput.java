package gtc_expansion.util;

import gtc_expansion.GTCExpansion;
import gtc_expansion.item.GTCXItemBatterySingleUse;
import ic2.api.item.ElectricItem;
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
        return !this.item.isEmpty() && !subject.isEmpty() && this.item.getItem() == subject.getItem() && ElectricItem.manager.getCharge(subject) <= 0;
    }

    @Override
    public List<ItemStack> getInputs() {
        List<ItemStack> list = new ArrayList();
        if (this.item.getItem() instanceof GTCXItemBatterySingleUse){
            list.addAll(((GTCXItemBatterySingleUse)this.item.getItem()).getValidItemVariants());
        }
        return list;
    }
}
