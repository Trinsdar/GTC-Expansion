package gtc_expansion.tile;

import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.base.GTCXTileBaseLiquidFuelGenerator;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.util.math.Box2D;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXTileDieselGenerator extends GTCXTileBaseLiquidFuelGenerator {

    public GTCXTileDieselGenerator() {
        super(3);
    }
    @Override
    public ResourceLocation getTexture() {
        return null;
    }

    @Override
    public Box2D getEnergyBox() {
        return null;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList(){
        return GTCXRecipeLists.DIESEL_GEN_RECIPE_LIST;
    }

    @Override
    public Box2D getFuelBox() {
        return null;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return null;
    }
}
