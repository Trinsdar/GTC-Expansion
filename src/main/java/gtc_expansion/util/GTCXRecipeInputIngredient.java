package gtc_expansion.util;

import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;

public class GTCXRecipeInputIngredient implements IRecipeInput {
    Ingredient input;
    public GTCXRecipeInputIngredient(Ingredient input){
        this.input = input;
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return input.apply(itemStack);
    }

    @Override
    public int getAmount() {
        return input.getMatchingStacks().length;
    }

    @Override
    public List<ItemStack> getInputs() {
        return Arrays.asList(input.getMatchingStacks());
    }

    @Override
    public Ingredient getIngredient() {
        return input;
    }
}
