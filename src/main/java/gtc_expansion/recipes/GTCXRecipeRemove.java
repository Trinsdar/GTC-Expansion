package gtc_expansion.recipes;

import gtc_expansion.GTCXConfiguration;
import gtc_expansion.data.GTCXValues;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMachineHandler;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTConfig;
import gtclassic.common.tile.GTTileCentrifuge;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class GTCXRecipeRemove {

    public static void init(){
        //initCentrifugeRemoval();
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
        ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input(Ic2Items.coalChunk));
        if (GTCXValues.STEEL_MODE){
            ClassicRecipes.compressor.removeRecipe(Ic2Items.machine);
        }

        ClassicRecipes.macerator.removeRecipe(GTTileBaseMachine.input("oreRedstone", 1));
        if (GTCXConfiguration.general.usePlates && (!Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) || !GTConfig.modcompat.compatIc2Extras)){
            ClassicRecipes.compressor.removeRecipe(GTTileBaseMachine.input("ingotCopper", 8));
        }

        ClassicRecipes.fluidGenerator.getBurnMap().remove(GTMaterialGen.getFluid(GTMaterial.Methane));
        ClassicRecipes.fluidGenerator.getBurnMap().remove(GTMaterialGen.getFluid(GTMaterial.Hydrogen));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreLapis"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreCoal"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreDiamond"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreEmerald"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreQuartz"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreRuby"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreSapphire"));
    }

    public static void initCentrifugeRemoval(){
        GTTileCentrifuge.RECIPE_LIST.startMassChange();
        removeCentrifugeRecipe("item.gtclassic.test_tube");
        removeCentrifugeRecipe("item.gtclassic.test_tube_1");
        removeCentrifugeRecipe("item.itemCellEmpty");
        removeCentrifugeRecipe("item.itemCellEmpty_1");
        removeCentrifugeRecipe("item.gtclassic.dustCarbon");
        removeCentrifugeRecipe("item.gtclassic.dustAluminium");
        removeCentrifugeRecipe("item.gtclassic.dustAluminium_1");
        removeCentrifugeRecipe("item.itemDustIron");
        removeCentrifugeRecipe("item.itemDustIron_1");
        removeCentrifugeRecipe("item.gtclassic.test_tube_8");
        removeCentrifugeRecipe("item.gtclassic.test_tube_9");
        removeCentrifugeRecipe("item.gtclassic.test_tube_10");
        removeCentrifugeRecipe("item.gtclassic.test_tube_11");
        removeCentrifugeRecipe("item.gtclassic.test_tube_12");
        removeCentrifugeRecipe("item.gtclassic.test_tube_13");
        removeCentrifugeRecipe("item.itemDustCoal");
        removeCentrifugeRecipe("item.gtclassic.dustSilicon");
        removeCentrifugeRecipe("item.gtclassic.dustSilicon_1");
        removeCentrifugeRecipe("item.gtclassic.dustSilicon_2");
        removeCentrifugeRecipe("item.gtclassic.dustSilicon_3");
        removeCentrifugeRecipe("item.gtclassic.dustLithium");
        removeCentrifugeRecipe("tile.sand.default_4");
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
