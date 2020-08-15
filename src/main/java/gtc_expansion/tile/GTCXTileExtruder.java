package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerExtruder;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.data.GTCXValues;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeCraftingHandler;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTConfig;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.MachineFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GTCXTileExtruder extends GTTileBaseMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/extruder.png");
    public static final int[] slotInputs = {0, 1};
    public static final int[] slotOutputs = {2, 3};
    public static final int slotFuel = 4;
    public IFilter filter = new MachineFilter(this);
    private static final int defaultEu = 96;

    public GTCXTileExtruder() {
        super(5, 2, defaultEu, 100, 128);
        setFuelSlot(slotFuel);
        maxEnergy = 10000;
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutputs);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerInputFilter(filter, slotInputs);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Input, slotInputs);
        handler.registerSlotType(SlotType.Output, slotOutputs);
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.EXTRUDER;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerExtruder(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXExtruderGui.class;
    }

    @Override
    public int[] getInputSlots() {
        return slotInputs;
    }

    @Override
    public IFilter[] getInputFilters(int[] slots) {
        return new IFilter[] { filter };
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        return slot != slotFuel;
    }

    @Override
    public int[] getOutputSlots() {
        return slotOutputs;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GTCXRecipeLists.EXTRUDER_RECIPE_LIST;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public boolean hasGui(EntityPlayer player) {
        return true;
    }

    @Override
    public ResourceLocation getStartSoundFile() {
        return GTCExpansion.getAprilFirstSound(Ic2Sounds.electricFurnaceLoop);
    }

    public static void init() {
        addRecipe("ingotTin", 1, GTMaterialGen.get(GTCXItems.moldCell), 1920, GTMaterialGen.getIc2(Ic2Items.emptyCell, 4));
        addRecipe("ingotGold", 1, GTMaterialGen.get(GTCXItems.moldWire), 1920, GTMaterialGen.getIc2(Ic2Items.goldCable, 6));
        addRecipe("ingotCopper", 1, GTMaterialGen.get(GTCXItems.moldWire), 1920, GTMaterialGen.getIc2(Ic2Items.copperCable, 3));
        addRecipe("ingotTin", 1, GTMaterialGen.get(GTCXItems.moldWire), 1920, GTMaterialGen.getIc2(Ic2Items.tinCable, 4));
        addRecipe("ingotBronze", 1, GTMaterialGen.get(GTCXItems.moldWire), 1920, GTMaterialGen.getIc2(Ic2Items.bronzeCable, 3));
        String steel = GTCXValues.STEEL_MODE ? "Steel" : "RefinedIron";
        addRecipe("ingot" + steel, 1, GTMaterialGen.get(GTCXItems.moldWire), 1920, GTMaterialGen.getIc2(Ic2Items.ironCable, 6));
        addRecipe("ingotElectrum", 1, GTMaterialGen.get(GTCXItems.moldWire), 1920, GTMaterialGen.get(GTCXBlocks.electrumCable, 6));
        addRecipe(GTRecipeCraftingHandler.combineRecipeObjects("ingotAluminium", "ingotAluminum"), input(GTMaterialGen.get(GTCXItems.moldWire)), 1920, GTMaterialGen.get(GTCXBlocks.aluminiumCable, 6));

        if (Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) && GTConfig.modcompat.compatIc2Extras){
            addRecipe("ingotIron", 1, GTMaterialGen.get(GTCXItems.moldCell), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "emptyfuelrod"));
            addRecipe("ingotCopper", 1, GTMaterialGen.get(GTCXItems.moldCasing), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "coppercasing", 2));
            addRecipe("ingotTin", 1, GTMaterialGen.get(GTCXItems.moldCasing), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "tincasing", 2));
            addRecipe("ingotSilver", 1, GTMaterialGen.get(GTCXItems.moldCasing), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "silvercasing", 2));
            addRecipe("ingotLead", 1, GTMaterialGen.get(GTCXItems.moldCasing), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "leadcasing", 2));
            addRecipe("ingotIron", 1, GTMaterialGen.get(GTCXItems.moldCasing), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "ironcasing", 2));
            addRecipe("ingotGold", 1, GTMaterialGen.get(GTCXItems.moldCasing), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "goldcasing", 2));
            addRecipe("ingotRefinedIron", 1, GTMaterialGen.get(GTCXItems.moldCasing), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "refinedironcasing", 2));
            addRecipe("ingotSteel", 1, GTMaterialGen.get(GTCXItems.moldCasing), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "steelcasing", 2));
            addRecipe("ingotBronze", 1, GTMaterialGen.get(GTCXItems.moldCasing), 1920, GTMaterialGen.getModItem(GTValues.MOD_ID_IC2_EXTRAS, "bronzecasing", 2));
        }
    }

    public static void addRecipe(String input1, int amount1, int totalEu, ItemStack output) {
        addRecipe(new IRecipeInput[]{new RecipeInputOreDict(input1, amount1)}, totalEu(totalEu), output);
    }

    public static void addRecipe(String input1, int amount1, ItemStack input2, int totalEu, ItemStack output) {
        addRecipe(new IRecipeInput[]{new RecipeInputOreDict(input1, amount1), new RecipeInputItemStack(input2)}, totalEu(totalEu), output);
    }

    public static void addRecipe(ItemStack input1, String input2, int amount2, int totalEu, ItemStack output) {
        addRecipe(new IRecipeInput[]{new RecipeInputItemStack(input1), new RecipeInputOreDict(input2, amount2)}, totalEu(totalEu), output);
    }

    public static void addRecipe(String input1, int amount1, String input2, int amount2, int totalEu, ItemStack output) {
        addRecipe(new IRecipeInput[]{new RecipeInputOreDict(input1, amount1), new RecipeInputOreDict(input2, amount2)}, totalEu(totalEu), output);
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, int totalEu, ItemStack output) {
        addRecipe(new IRecipeInput[]{input1, input2}, totalEu(totalEu), output);
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, int totalEu, ItemStack output, String recipeId) {
        addRecipe(new IRecipeInput[]{input1, input2}, totalEu(totalEu), recipeId, output);
    }

    public static void addRecipe(ItemStack input1, ItemStack input2, int totalEu, ItemStack output) {
        addRecipe(new IRecipeInput[]{new RecipeInputItemStack(input1), new RecipeInputItemStack(input2)}, totalEu(totalEu), output);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, ItemStack... outputs){
        addRecipe(inputs, modifiers, outputs[0].getUnlocalizedName(), outputs);
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, String recipeId, ItemStack... outputs) {
        List<IRecipeInput> inlist = new ArrayList<>();
        List<ItemStack> outlist = new ArrayList<>();
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(inlist, new MachineOutput(mods, outlist), recipeId);
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeId) {
        GTCXRecipeLists.EXTRUDER_RECIPE_LIST.addRecipe(input, output, recipeId, 16);
    }
}
