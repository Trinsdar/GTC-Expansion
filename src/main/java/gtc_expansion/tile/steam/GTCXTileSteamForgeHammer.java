package gtc_expansion.tile.steam;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerSteamForgeHammer;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.recipes.GTCXRecipeProcessing;
import gtc_expansion.tile.base.GTCXTileBaseSteamMachine;
import gtc_expansion.util.GTCXSteamMachineFilter;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.recipe.GTRecipe;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static gtc_expansion.recipes.GTCXRecipeProcessing.getCrushedOrDust;

public class GTCXTileSteamForgeHammer extends GTCXTileBaseSteamMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/bronzehammer.png");
    public IFilter filter = new GTCXSteamMachineFilter(this);
    public GTCXTileSteamForgeHammer() {
        super(2, 100, 20);
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
        return GTCXRecipeLists.FORGE_HAMMER_RECIPE_LIST;
    }

    @Override
    public ResourceLocation getStartSoundFile() {
        return SoundEvents.BLOCK_ANVIL_USE.getSoundName();
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerSteamForgeHammer(entityPlayer.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GTCXMachineGui.GTCXForgeHammerGui.class;
    }

    public ResourceLocation getGuiLocation(){
        return GUI_LOCATION;
    }

    public static void init(){
        addRecipe("cobblestone", 1, 10, GTMaterialGen.get(Blocks.GRAVEL));
        addRecipe("gravel", 1, 10, GTMaterialGen.get(Blocks.SAND));
        addRecipe("oreIron", 1, 16, getCrushedOrDust(GTCXMaterial.Iron, 1));
        addRecipe("oreGold", 1, 16, getCrushedOrDust(GTCXMaterial.Gold, 1));
        addRecipe("oreCopper", 1, 16, getCrushedOrDust(GTCXMaterial.Copper, 1));
        addRecipe("oreTin", 1, 16, getCrushedOrDust(GTCXMaterial.Tin, 1));
        addRecipe("oreSilver", 1, 16, getCrushedOrDust(GTCXMaterial.Silver, 1));
        addRecipe("oreUranium", 1, 16, getCrushedOrDust(GTMaterial.Uranium, 1));
        addRecipe("oreGalena", 1, 16, getCrushedOrDust(GTCXMaterial.Galena, 1));
        addRecipe("oreTetrahedrite", 1, 16, getCrushedOrDust(GTCXMaterial.Tetrahedrite, 1));
        addRecipe("oreCassiterite", 1, 16, getCrushedOrDust(GTCXMaterial.Cassiterite, 2));
        addRecipe("orePyrite", 1, 16, getCrushedOrDust(GTMaterial.Pyrite, 2));
        addRecipe("oreCinnabar", 1, 16, getCrushedOrDust(GTCXMaterial.Cinnabar, 2));
        addRecipe("oreSphalerite", 1, 16, getCrushedOrDust(GTCXMaterial.Sphalerite, 1));
        addRecipe("orePlatinum", 1, 16, getCrushedOrDust(GTMaterial.Platinum, 1));
        addRecipe("oreTungstate", 1, 16, getCrushedOrDust(GTMaterial.Tungsten, 2));
        addRecipe("oreChromite", 1, 16, getCrushedOrDust(GTCXMaterial.Chromite, 1));
        addRecipe("oreBauxite", 1, 16, getCrushedOrDust(GTMaterial.Bauxite, 2));
        addRecipe("oreIridium", 1, 16, getCrushedOrDust(GTMaterial.Iridium, 1));
        addModOreRecipe("oreNickel", 1, 16, getCrushedOrDust(GTMaterial.Nickel, 1));
        addModOreRecipe("oreLead", 1, 16, getCrushedOrDust(GTCXMaterial.Lead, 1));
    }

    public static void addModOreRecipe(String input, int amount, int totalTime, ItemStack output){
        if (OreDictionary.doesOreNameExist(input)){
            addRecipe(GTTileBaseMachine.input(input, amount), totalTime, output);
        }
    }


    public static RecipeModifierHelpers.IRecipeModifier[] totalTime(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount) - 100) };
    }

    public static void addRecipe(ItemStack input, int totalTime, ItemStack output){
        addRecipe(GTTileBaseMachine.input(input), totalTime, output);
    }

    public static void addRecipe(String input, int amount, int totalTime, ItemStack output){
        addRecipe(GTTileBaseMachine.input(input, amount), totalTime, output);
    }

    public static void addRecipe(IRecipeInput input, int totalTime, ItemStack output){
        addRecipe(input, totalTime(totalTime), output, output.getUnlocalizedName());
    }

    public static void addRecipe(IRecipeInput input, RecipeModifierHelpers.IRecipeModifier[] modifiers, ItemStack output, String recipeId){
        List<IRecipeInput> inputList = new ArrayList<>(Collections.singletonList(input));
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        GTCXRecipeLists.FORGE_HAMMER_RECIPE_LIST.addRecipe(inputList, new MachineOutput(mods, output), recipeId, 0);
    }
}
