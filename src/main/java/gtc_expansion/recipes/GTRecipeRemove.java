package gtc_expansion.recipes;

import gtclassic.tile.GTTileCentrifuge;

public class GTRecipeRemove {
    public static void init(){
        initCentrifugeRemoval();
    }

    public static void initCentrifugeRemoval(){
        GTTileCentrifuge.RECIPE_LIST.startMassChange();
        GTTileCentrifuge.removeRecipe("item.gtclassic.test_tube");
        GTTileCentrifuge.removeRecipe("item.itemCellEmpty");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustCarbon");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustAluminum");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustAluminum_1");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustSapphire");
        GTTileCentrifuge.removeRecipe("item.gtclassic.test_tube_4");
        GTTileCentrifuge.removeRecipe("item.gtclassic.test_tube_5");
        GTTileCentrifuge.removeRecipe("item.gtclassic.test_tube_6");
        GTTileCentrifuge.removeRecipe("item.itemDustIron");
        GTTileCentrifuge.removeRecipe("item.gtclassic.test_tube_7");
        GTTileCentrifuge.removeRecipe("item.gtclassic.test_tube_8");
        GTTileCentrifuge.removeRecipe("item.gtclassic.test_tube_9");
        GTTileCentrifuge.removeRecipe("item.itemDustCoal");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustSilicon");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustSilicon_1");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustLithium");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustSilicon_2");
        GTTileCentrifuge.RECIPE_LIST.finishMassChange();
    }
}
