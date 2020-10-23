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
        initIc2Removals();
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
        ClassicRecipes.fluidGenerator.getBurnMap().remove(GTMaterialGen.getFluid(GTMaterial.Fuel));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreLapis"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreCoal"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreDiamond"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreEmerald"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreQuartz"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreRuby"));
        ClassicRecipes.extractor.removeRecipe(new RecipeInputOreDict("oreSapphire"));
        ClassicRecipes.macerator.removeRecipe(new RecipeInputOreDict("stoneMarble"));
    }

    public static void postInit(){
        removeModFluidLiquidGenRecipe("bio.ethanol");
    }

    public static void removeModFluidLiquidGenRecipe(String fluidName){
        try {
            ClassicRecipes.fluidGenerator.getBurnMap().remove(GTMaterialGen.getFluidStack(fluidName, 1000).getFluid());
        } catch (Exception ignored){
            ignored.printStackTrace();
        }
    }

    public static void removeFurnaceRecipes(){
        if (GTCXConfiguration.general.unfiredBricks){
            GTHelperStack.removeSmelting(new ItemStack(Items.BRICK));
        }
        if (GTCXConfiguration.general.forcePreElectricMachines && !GTCXConfiguration.general.gt2Mode){
            GTHelperStack.removeSmelting(Ic2Items.rubber);
        }
        if (GTCXConfiguration.general.removeVanillaCharcoalRecipe) {
            GTHelperStack.removeSmelting(new ItemStack(Items.COAL, 1, 1));
        }
    }

    public static void removeCentrifugeRecipe(String id){
        GTRecipeMachineHandler.removeRecipe(GTTileCentrifuge.RECIPE_LIST, id);
    }
}
