package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtclassic.api.helpers.GTHelperMods;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.recipe.GTRecipeMachineHandler;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTConfig;
import gtclassic.common.tile.GTTileCentrifuge;
import ic2.api.classic.recipe.ClassicRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class GTCXRecipeRemove {

    public static void init(){
        initCentrifugeRemoval();
        initIc2Removals();
        if (GTCXConfiguration.general.unfiredBricks){
            GTHelperStack.removeSmelting(new ItemStack(Items.BRICK));
        }
        removeFurnaceRecipes();
    }

    public static void initIc2Removals(){
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustRuby", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustSapphire", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustGreenSapphire", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustOlivine", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustRedGarnet", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustYellowGarnet", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustCarbon", 8));

        ClassicRecipes.macerator.removeRecipe(GTTileBaseMachine.input("oreRedstone", 1));
        if (GTCXConfiguration.general.usePlates && (!Loader.isModLoaded(GTHelperMods.IC2_EXTRAS) || !GTConfig.modcompat.compatIc2Extras)){
            ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("ingotCopper", 8));
        }
    }

    public static void initCentrifugeRemoval(){
        GTTileCentrifuge.RECIPE_LIST.startMassChange();
        removeCentrifugeRecipe("item.gtclassic.test_tube");
        removeCentrifugeRecipe("item.itemCellEmpty");
        removeCentrifugeRecipe("item.itemCellEmpty_1");
        removeCentrifugeRecipe("item.gtclassic.dustCarbon");
        removeCentrifugeRecipe("item.gtclassic.dustAluminium");
        removeCentrifugeRecipe("item.gtclassic.dustAluminium_1");
        removeCentrifugeRecipe("item.gtclassic.dustSapphire");
        removeCentrifugeRecipe("item.gtclassic.test_tube_4");
        removeCentrifugeRecipe("item.gtclassic.test_tube_5");
        removeCentrifugeRecipe("item.gtclassic.test_tube_6");
        removeCentrifugeRecipe("item.itemDustIron");
        removeCentrifugeRecipe("item.gtclassic.test_tube_7");
        removeCentrifugeRecipe("item.gtclassic.test_tube_8");
        removeCentrifugeRecipe("item.gtclassic.test_tube_9");
        removeCentrifugeRecipe("item.itemDustCoal");
        removeCentrifugeRecipe("item.gtclassic.dustSilicon");
        removeCentrifugeRecipe("item.gtclassic.dustSilicon_1");
        removeCentrifugeRecipe("item.gtclassic.dustLithium");
        removeCentrifugeRecipe("item.gtclassic.dustSilicon_2");
        removeCentrifugeRecipe("item.gtclassic.dustSodalite_1");
        GTTileCentrifuge.RECIPE_LIST.finishMassChange();
    }

    public static void removeFurnaceRecipes(){
        if (GTCXConfiguration.general.removeVanillaCharcoalRecipe) {
            GTHelperStack.removeSmelting(new ItemStack(Items.COAL, 1, 1));

        }
    }

    public static void removeCentrifugeRecipe(String id){
        GTRecipeMachineHandler.removeRecipe(GTTileCentrifuge.RECIPE_LIST, id);
    }
}
