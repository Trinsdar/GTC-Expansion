package gtc_expansion.tile;

import gtc_expansion.GTCXMachineGui;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.base.GTCXTileBaseBurnableFluidGenerator;
import gtclassic.api.fluid.GTFluidHandler;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
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
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXDieselGeneratorGui.class;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.DIESEL_GENERATOR;
    }

    @Override
    public void doFluidContainerThings(){
        if (!GTHelperStack.isSlotEmpty(this, slotInput)) {
            ItemStack emptyStack = Ic2Items.emptyCell.copy();
            if (this.getStackInSlot(slotInput).isItemEqual(Ic2Items.coalFuelCell) || this.getStackInSlot(slotInput).isItemEqual(Ic2Items.bioFuelCell) || this.getStackInSlot(slotInput).isItemEqual(emptyStack)){
                if (!GTHelperFluid.isTankFull(tank) && !GTHelperStack.isSlotFull(this, slotOutput) && GTHelperStack.canOutputStack(this, emptyStack, slotOutput) && tank.getFluidAmount() + 1000 <= tank.getCapacity()) {
                    if (this.getStackInSlot(slotInput).isItemEqual(Ic2Items.coalFuelCell)){
                        if (tank.getFluid() == null || tank.getFluid().isFluidEqual(GTMaterialGen.getFluidStack(GTCXMaterial.CoalFuel))){
                            tank.fillInternal(GTMaterialGen.getFluidStack(GTCXMaterial.CoalFuel), true);
                            if (GTHelperStack.isSlotEmpty(this, slotOutput)) {
                                this.setStackInSlot(slotOutput, emptyStack);
                            } else {
                                this.getStackInSlot(slotOutput).grow(1);
                            }

                            this.getStackInSlot(slotInput).shrink(1);
                            return;
                        }
                    } else if (this.getStackInSlot(slotInput).isItemEqual(Ic2Items.bioFuelCell)){
                        if (tank.getFluid() == null || tank.getFluid().isFluidEqual(GTMaterialGen.getFluidStack(GTCXMaterial.BioFuel))){
                            tank.fillInternal(GTMaterialGen.getFluidStack(GTCXMaterial.BioFuel), true);
                            if (GTHelperStack.isSlotEmpty(this, slotOutput)) {
                                this.setStackInSlot(slotOutput, emptyStack);
                            } else {
                                this.getStackInSlot(slotOutput).grow(1);
                            }

                            this.getStackInSlot(slotInput).shrink(1);
                            return;
                        }
                    }
                }
                if (this.getStackInSlot(slotInput).isItemEqual(emptyStack) && tank.getFluid() != null && tank.getFluidAmount() >= 1000){
                    if (tank.getFluid().isFluidEqual(GTMaterialGen.getFluidStack(GTCXMaterial.CoalFuel)) && GTHelperStack.canOutputStack(this, Ic2Items.coalFuelCell.copy(), slotOutput)){
                        tank.drainInternal(1000, true);
                        if (GTHelperStack.isSlotEmpty(this, slotOutput)) {
                            this.setStackInSlot(slotOutput, Ic2Items.coalFuelCell.copy());
                        } else {
                            this.getStackInSlot(slotOutput).grow(1);
                        }

                        this.getStackInSlot(slotInput).shrink(1);
                        return;
                    }
                    if (tank.getFluid().isFluidEqual(GTMaterialGen.getFluidStack(GTCXMaterial.BioFuel)) && GTHelperStack.canOutputStack(this, Ic2Items.bioFuelCell.copy(), slotOutput)){
                        tank.drainInternal(1000, true);
                        if (GTHelperStack.isSlotEmpty(this, slotOutput)) {
                            this.setStackInSlot(slotOutput, Ic2Items.bioFuelCell.copy());
                        } else {
                            this.getStackInSlot(slotOutput).grow(1);
                        }

                        this.getStackInSlot(slotInput).shrink(1);
                        return;
                    }
                }
            }
        }
        GTHelperFluid.doFluidContainerThings(this, this.tank, slotInput, slotOutput);
    }

    public static void init(){
        addRecipe(GTMaterialGen.getFluid(GTCXMaterial.Diesel), 10667, 12);
        addRecipe(GTMaterialGen.getFluid(GTMaterial.Fuel), 10667, 12);
        addRecipe(GTMaterialGen.getFluid(GTCXMaterial.Gasoline), 10667, 12);
        addRecipe(GTMaterialGen.getFluid(GTCXMaterial.Naphtha), 10667, 12);
        addRecipe(GTMaterialGen.getFluid(GTCXMaterial.NitroDiesel), 16000, 24);
        addRecipe(GTMaterialGen.getFluid(GTCXMaterial.NitroCoalFuel), 4000, 12);
        addRecipe(GTMaterialGen.getFluid(GTCXMaterial.BioFuel), 500, 12);
        addRecipe(GTMaterialGen.getFluid(GTCXMaterial.CoalFuel), 1340, 12);
        addModFluidRecipe("bio.ethanol", 10667, 12);
    }

    public static void addModFluidRecipe(String fluidName, int ticks, int euPerTick) {
        try {
            Fluid fluid = GTMaterialGen.getFluidStack(fluidName, 1000).getFluid();
            addRecipe(fluid, ticks, euPerTick, fluid.getUnlocalizedName());
            if (!GTFluidHandler.getBurnableToolTipList().contains(fluidName)) {
                GTFluidHandler.addBurnableToolTip(fluidName);
            }
        } catch (Exception ignored){}
    }

    public static void addRecipe(Fluid fluid, int ticks, int euPerTick) {
        addRecipe(fluid, ticks, euPerTick, fluid.getUnlocalizedName());
        if (!GTFluidHandler.getBurnableToolTipList().contains(fluid.getName())) {
            GTFluidHandler.addBurnableToolTip(fluid);
        }
    }

    public static void addRecipe(Fluid fluid, int ticks, int euPerTick, String recipeId) {
        addRecipe(new RecipeInputFluid(new FluidStack(fluid, 1000)), ticks, euPerTick, recipeId);
        if (!GTFluidHandler.getBurnableToolTipList().contains(fluid.getName())) {
            GTFluidHandler.addBurnableToolTip(fluid);
        }
    }

    private static void addRecipe(IRecipeInput input, int ticks, int euPerTick, String recipeID) {
        List<IRecipeInput> inlist = new ArrayList<>();
        List<ItemStack> outlist = new ArrayList<>();
        NBTTagCompound mods = new NBTTagCompound();
        mods.setInteger(RECIPE_TICKS, ticks);
        mods.setInteger(RECIPE_EU, euPerTick);
        inlist.add(input);
        outlist.add(GTMaterialGen.get(Items.REDSTONE));
        addRecipe(inlist, new MachineOutput(mods, outlist), euPerTick, recipeID);
    }

    private static void addRecipe(List<IRecipeInput> input, MachineOutput output, int euPerTick, String recipeID) {
        if (!input.isEmpty()) {
            GTCXRecipeLists.DIESEL_GEN_RECIPE_LIST.addRecipe(input, output, recipeID, euPerTick                                                );
        }
    }

}
