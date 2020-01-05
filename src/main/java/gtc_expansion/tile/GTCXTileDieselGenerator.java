package gtc_expansion.tile;

import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.base.GTCXTileBaseBurnableFluidGenerator;
import gtc_expansion.util.GTCXLang;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GTCXTileDieselGenerator extends GTCXTileBaseBurnableFluidGenerator {

    public GTCXTileDieselGenerator() {
        super(3);
    }

    @Override
    public GTRecipeMultiInputList getRecipeList(){
        return GTCXRecipeLists.DIESEL_GEN_RECIPE_LIST;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.DIESEL_GENERATOR;
    }

    public static void init(){
        addRecipe(GTMaterialGen.getFluid(GTCXMaterial.Diesel), 2000, 32);
    }

    public static void addRecipe(Fluid fluid, int ticks, int euPerTick) {
        addRecipe(new RecipeInputFluid(new FluidStack(fluid, 1000)), ticks, euPerTick);
    }

    private static void addRecipe(IRecipeInput input, int ticks, int euPerTick) {
        List<IRecipeInput> inlist = new ArrayList<>();
        List<ItemStack> outlist = new ArrayList<>();
        NBTTagCompound mods = new NBTTagCompound();
        mods.setInteger(recipeTicks, ticks);
        mods.setInteger(recipeEu, euPerTick);
        inlist.add(input);
        outlist.add(GTMaterialGen.get(Items.REDSTONE));
        addRecipe(inlist, new MachineOutput(mods, outlist), euPerTick);
    }

    private static void addRecipe(List<IRecipeInput> input, MachineOutput output, int euPerTick) {
        if (!input.isEmpty()) {
            GTCXRecipeLists.DIESEL_GEN_RECIPE_LIST.addRecipe(input, output, input.get(0).getInputs().get(0).getUnlocalizedName(), euPerTick                                                );
        }
    }

}
