package gtc_expansion.tile;

import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.base.GTCXTileBaseBurnableFluidGenerator;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.util.math.Box2D;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXTileDieselGenerator extends GTCXTileBaseBurnableFluidGenerator {

    public GTCXTileDieselGenerator() {
        super(3);
    }
    @Override
    public ResourceLocation getTexture() {
        return null;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList(){
        return GTCXRecipeLists.DIESEL_GEN_RECIPE_LIST;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return null;
    }
}
