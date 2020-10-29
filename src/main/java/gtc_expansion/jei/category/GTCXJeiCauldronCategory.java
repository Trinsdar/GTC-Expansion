package gtc_expansion.jei.category;

import gtclassic.api.jei.GTJeiMultiRecipeCategory;
import mezz.jei.api.IGuiHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GTCXJeiCauldronCategory extends GTJeiMultiRecipeCategory {
    public GTCXJeiCauldronCategory(IGuiHelper helper, String name, Item item) {
        super(helper, name, Blocks.AIR);
        this.displayName = (new ItemStack(item)).getDisplayName().replace(" Controller", "");
    }
}
