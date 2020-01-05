package gtc_expansion.jei.category;

import gtclassic.api.jei.GTJeiMultiRecipeCategory;
import mezz.jei.api.IGuiHelper;
import net.minecraft.block.Block;

public class GTCXJeiCustomCategory extends GTJeiMultiRecipeCategory {
    public GTCXJeiCustomCategory(IGuiHelper helper, String name, Block block) {
        super(helper, name, block);
    }

    @Override
    protected int getHeight() {
        return super.getHeight() + 10;
    }
}
