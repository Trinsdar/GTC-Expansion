package gtc_expansion.recipes;

import gtc_expansion.tile.multi.GETileMultiPrimitiveBlastFurnace;
import gtclassic.GTConfig;
import gtclassic.material.GTMaterialGen;
import gtclassic.util.GTValues;
import ic2.api.recipe.IRecipeInput;
import net.minecraftforge.fml.common.Loader;

import static gtclassic.recipe.GTRecipeMods.input;
import static gtclassic.recipe.GTRecipeMods.metal;

public class GERecipeMods {
    public static void init(){
        if (GTConfig.compatTwilightForest && Loader.isModLoaded(GTValues.TFOREST)) {
            GETileMultiPrimitiveBlastFurnace.addRecipe(new IRecipeInput[] { metal("Iron", 1),
                    input(GTMaterialGen.getModItem(GTValues.TFOREST, "liveroot")),
                    input("nuggetGold", 1) }, 400, GTMaterialGen.getModItem(GTValues.TFOREST, "ironwood_ingot", 2));
        }
    }
}
