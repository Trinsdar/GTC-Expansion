package gtc_expansion.tile;

import gtc_expansion.GTCXMachineGui;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.base.GTCXTileBaseBurnableFluidGenerator;
import gtc_expansion.data.GTCXLang;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GTCXTileGasTurbine extends GTCXTileBaseBurnableFluidGenerator {

    public GTCXTileGasTurbine() {
        super(3);
    }

    @Override
    public GTRecipeMultiInputList getRecipeList(){
        return GTCXRecipeLists.GAS_TURBINE_RECIPE_LIST;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXGasTurbineGui.class;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.GAS_TURBINE;
    }

    public static void init(){
        addRecipe(GTMaterialGen.getFluid(GTMaterial.Hydrogen), 940, 16);
        addRecipe(GTMaterialGen.getFluid(GTMaterial.Methane), 2815, 16);
        addRecipe(GTMaterialGen.getFluid(GTCXMaterial.Propane), 2815, 16);
    }

    public static void addRecipe(Fluid fluid, int ticks, int euPerTick) {
        addRecipe(new RecipeInputFluid(new FluidStack(fluid, 1000)), ticks, euPerTick);
    }

    private static void addRecipe(IRecipeInput input, int ticks, int euPerTick) {
        List<IRecipeInput> inlist = new ArrayList<>();
        List<ItemStack> outlist = new ArrayList<>();
        NBTTagCompound mods = new NBTTagCompound();
        mods.setInteger(RECIPE_TICKS, ticks);
        mods.setInteger(RECIPE_EU, euPerTick);
        inlist.add(input);
        outlist.add(GTMaterialGen.get(Items.REDSTONE));
        addRecipe(inlist, new MachineOutput(mods, outlist), euPerTick);
    }

    private static void addRecipe(List<IRecipeInput> input, MachineOutput output, int euPerTick) {
        if (!input.isEmpty()) {
            GTCXRecipeLists.GAS_TURBINE_RECIPE_LIST.addRecipe(input, output, input.get(0).getInputs().get(0).getUnlocalizedName(), euPerTick                                                );
        }
    }
}
