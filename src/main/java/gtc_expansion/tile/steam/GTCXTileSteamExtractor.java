package gtc_expansion.tile.steam;

import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GTCXContainerSteamExtractor;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.base.GTCXTileBaseSteamMachine;
import gtc_expansion.util.GTCXSteamMachineFilter;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.machine.IMachineRecipeList;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.gui.custom.MachineGui;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GTCXTileSteamExtractor extends GTCXTileBaseSteamMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/bronzeextractor.png");
    public IFilter filter = new GTCXSteamMachineFilter(this);
    public GTCXTileSteamExtractor() {
        super(2, 800, 4);
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    public IFilter[] getInputFilters(int[] slots) {
        return new IFilter[]{filter};
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        return slot != 1;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{1};
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GTCXRecipeLists.EXTRACTOR_RECIPE_LIST;
    }

    @Override
    public ResourceLocation getStartSoundFile() {
        return Ic2Sounds.extractorOp;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerSteamExtractor(entityPlayer.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return MachineGui.ExtractorGui.class;
    }

    public ResourceLocation getGuiLocation(){
        return GUI_LOCATION;
    }

    public static void init(){
        for (IMachineRecipeList.RecipeEntry entry : ClassicRecipes.extractor.getRecipeMap()){
            if (!GTHelperStack.oreDictStartsWith(entry.getInput().getInputs().get(0), "ore")){
                addRecipe(entry.getInput(), entry.getOutput());
            }
        }
    }

    static void addRecipe(IRecipeInput input, MachineOutput output) {
        List<IRecipeInput> inputs = new ArrayList<>();
        inputs.add(input);
        GTCXRecipeLists.EXTRACTOR_RECIPE_LIST.addRecipe(inputs, output, output.getAllOutputs().get(0).getUnlocalizedName(), 4);
    }
}
