package gtc_expansion.recipes;

import gtc_expansion.GEConfiguration;
import gtclassic.GTConfig;
import gtclassic.GTMod;
import gtclassic.material.GTMaterialGen;
import gtclassic.recipe.GTRecipeProcessing;
import gtclassic.tile.GTTileBaseMachine;
import gtclassic.tile.GTTileCentrifuge;
import gtclassic.tile.multi.GTTileMultiBlastFurnace;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class GERecipeRemove {

    public static void init(){
        initCentrifugeRemoval();
        initIc2Removals();
        initBlastFurnaceRemoval();
        if (GEConfiguration.unfiredBricks){
            GTRecipeProcessing.removeSmelting(new ItemStack(Items.BRICK));
        }
    }

    public static void initIc2Removals(){
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustRuby", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustSapphire", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustGreenSapphire", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustOlivine", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustRedGarnet", 1));
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("dustYellowGarnet", 1));

        ClassicRecipes.macerator.removeRecipe(GTTileBaseMachine.input("oreRedstone", 1));
        if (GEConfiguration.usePlates){
            ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("ingotCopper", 8));
        }
    }

    public static void initCentrifugeRemoval(){
        GTTileCentrifuge.RECIPE_LIST.startMassChange();
        GTTileCentrifuge.removeRecipe("item.gtclassic.test_tube");
        GTTileCentrifuge.removeRecipe("item.itemCellEmpty");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustCarbon");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustAluminium");
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustAluminium_1");
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
        GTTileCentrifuge.removeRecipe("item.gtclassic.dustSodalite_1");
        GTTileCentrifuge.RECIPE_LIST.finishMassChange();
    }

    public static void initBlastFurnaceRemoval(){
        GTTileMultiBlastFurnace.RECIPE_LIST.startMassChange();
        GTTileMultiBlastFurnace.removeRecipe("item.gtclassic.ingotIridium");
        GTTileMultiBlastFurnace.removeRecipe("item.gtclassic.ingotIridium_1");
        GTTileMultiBlastFurnace.removeRecipe("item.gtclassic.ingotIridium_2");
        GTTileMultiBlastFurnace.removeRecipe("item.gtclassic.ingotTungsten");
        GTTileMultiBlastFurnace.removeRecipe("item.itemIngotBronze");
        GTTileMultiBlastFurnace.removeRecipe("item.gtclassic.ingotElectrum");
        if (GTConfig.compatIE && Loader.isModLoaded("immersiveengineering") && (!Loader.isModLoaded("thermalfoundation") || !GTConfig.compatThermal)) {
            GTTileMultiBlastFurnace.removeRecipe("item.immersiveengineering.metal.ingot_constantan");

        }
        GTTileMultiBlastFurnace.RECIPE_LIST.finishMassChange();
    }
}
