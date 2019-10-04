package gtc_expansion.tile;

import gtc_expansion.GEMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerElectrolyzer;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.recipes.GERecipeLists;
import gtclassic.GTItems;
import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.GTTileBaseMachine;
import gtclassic.util.recipe.GTRecipeMultiInputList;
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
import ic2.core.platform.lang.components.base.LangComponentHolder;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GETileElectrolyzer extends GTTileBaseMachine {

    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialelectrolyzer.png");
    IFilter filter = new MachineFilter(this);
    public static final int slotFuel = 8;
    protected static final int[] slotInputs = { 0, 1 };
    protected static final int[] slotOutputs = { 2, 3, 4, 5, 6, 7 };
    private static final int defaultEu = 32;

    public GETileElectrolyzer() {
        super(9, 2, defaultEu, 100, 128);
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
        handler.registerDefaultSlotsForSide(RotationList.DOWN, slotFuel);
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
        return new LangComponentHolder.LocaleBlockComp(this.getBlockType().getUnlocalizedName());
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<IMachineUpgradeItem.UpgradeType>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GEContainerElectrolyzer(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GEMachineGui.GEIndustrialElectrolyzerGui.class;
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
        for (int i : this.getInputSlots()) {
            if (slot <= i) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] getOutputSlots() {
        return slotOutputs;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GERecipeLists.ELECTROLYZER_RECIPE_LIST;
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
        return Ic2Sounds.magnetizerOp;
    }

    public static void init() {
        Item tube = GTItems.testTube;
        /** Recipes from GT1 **/
        addRecipe(GTMaterialGen.getWater(6), 0, totalEu(3000), GTMaterialGen.getTube(GTMaterial.Hydrogen, 4), GTMaterialGen.getTube(GTMaterial.Oxygen, 2));
        addRecipe(GTMaterialGen.getIc2(Ic2Items.waterCell, 6), 6, totalEu(3000), GTMaterialGen.getIc2(Ic2Items.emptyCell, 6), GTMaterialGen.getTube(GTMaterial.Hydrogen, 4), GTMaterialGen.getTube(GTMaterial.Oxygen, 2));
        addRecipe("dustCoal", 4, 0, totalEu(7500), GTMaterialGen.getDust(GTMaterial.Carbon, 8));
        addRecipe("dustRuby", 9, 3, totalEu(25000), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Chrome, 1), GTMaterialGen.getTube(GTMaterial.Oxygen, 3));
        addRecipe("dustSapphire", 8, 3, totalEu(20000), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 3));
        addRecipe("dustGreenSapphire", 4, 0, totalEu(15000), GTMaterialGen.getDust(GTMaterial.Sapphire, 4));
        addRecipe("dustEmerald", 29, 12, totalEu(30000), GTMaterialGen.getTube(GTMaterial.Oxygen, 9), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getTube(GTMaterial.Beryllium, 3), GTMaterialGen.getDust(GTMaterial.Silicon, 6));
        addRecipe("dustEnderPearl", 16, 16, totalEu(65000), GTMaterialGen.getTube(GTMaterial.Chlorine, 6), GTMaterialGen.getTube(GTMaterial.Nitrogen, 5), GTMaterialGen.getTube(GTMaterial.Beryllium, 1), GTMaterialGen.getTube(GTMaterial.Potassium, 4));
        addRecipe("dustLazurite", 29, 8, totalEu(295000), GTMaterialGen.getTube(GTMaterial.Sodium, 4), GTMaterialGen.getDust(GTMaterial.Aluminium, 3), GTMaterialGen.getDust(GTMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Calcium, 4));
        addRecipe("dustPyrite", 3, 0, totalEu(15000), GTMaterialGen.getIc2(Ic2Items.ironDust, 1), GTMaterialGen.getDust(GEMaterial.Sulfur, 2));
        addRecipe("dustCalcite", 10, 5, totalEu(50000), GTMaterialGen.getTube(GTMaterial.Calcium, 2), GTMaterialGen.getDust(GTMaterial.Carbon, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 3));
        addRecipe("dustSodalite", 11, 5, totalEu(115000), GTMaterialGen.getTube(GTMaterial.Chlorine, 1), GTMaterialGen.getTube(GTMaterial.Sodium, 4), GTMaterialGen.getDust(GTMaterial.Aluminium, 3), GTMaterialGen.getDust(GTMaterial.Silicon, 3));
        addRecipe("dustBauxite", 24, 16, totalEu(250000), GTMaterialGen.getTube(GTMaterial.Oxygen, 6), GTMaterialGen.getDust(GTMaterial.Aluminium, 16), GTMaterialGen.getDust(GTMaterial.Titanium, 1), GTMaterialGen.getTube(GTMaterial.Hydrogen, 10));
        addRecipe(GTMaterialGen.get(Items.BLAZE_POWDER, 8), 0, totalEu(15000), GTMaterialGen.getIc2(Ic2Items.coalDust, 2), GTMaterialGen.get(Items.GUNPOWDER, 1));
        addRecipe("sand", 32, 1, totalEu(50000), GTMaterialGen.getDust(GTMaterial.Silicon, 1), GTMaterialGen.getTube(GTMaterial.Oxygen, 1));
        addRecipe("dustFlint", 8, 1, totalEu(5000), GTMaterialGen.getDust(GTMaterial.Silicon, 1), GTMaterialGen.getTube(GTMaterial.Oxygen, 1));

        /** Recipes from GT2 **/
        addRecipe("dustClay", 7, 2, totalEu(20000), GTMaterialGen.getDust(GTMaterial.Lithium, 1), GTMaterialGen.getDust(GTMaterial.Silicon, 2), GTMaterialGen.getDust(GEMaterial.Aluminium, 2), GTMaterialGen.getTube(GTMaterial.Sodium, 2));
        addRecipe("dustSaltpeter", 10, 7, totalEu(5500), GTMaterialGen.getTube(GTMaterial.Potassium, 2), GTMaterialGen.getTube(GTMaterial.Nitrogen, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 3));
        addRecipe("dustCinnabar", 4, 2, totalEu(12800), GTMaterialGen.getTube(GTMaterial.Mercury, 2), GTMaterialGen.getDust(GEMaterial.Sulfur, 2));
        addRecipe("dustSphalerite", 4, 0, totalEu(15000), GTMaterialGen.getDust(GEMaterial.Zinc, 2), GTMaterialGen.getDust(GEMaterial.Sulfur, 2));
        addRecipe("dustOlivine", 9, 2, totalEu(36000), GTMaterialGen.getDust(GEMaterial.Magnesium, 2), GTMaterialGen.getDust(GEMaterial.Silicon, 1), GTMaterialGen.getIc2(Ic2Items.ironDust, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 2));
        addRecipe("dustGalena", 2, 0, totalEu(120000), GEMaterialGen.getSmallDust(GEMaterial.Silver, 3), GEMaterialGen.getSmallDust(GEMaterial.Lead, 3), GEMaterialGen.getSmallDust(GEMaterial.Sulfur, 2));
        addRecipe("dustObsidian", 8, 4, totalEu(5000), GTMaterialGen.getDust(GEMaterial.Magnesium, 1), Ic2Items.ironDust.copy(), GTMaterialGen.getDust(GEMaterial.Silicon, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 4));
        addRecipe("dustCharcoal", 4, 0, totalEu(7500), GTMaterialGen.getDust(GTMaterial.Carbon, 4));
        addRecipe("dustPyrope", 20, 6, totalEu(89500), GTMaterialGen.getDust(GEMaterial.Magnesium, 3), GTMaterialGen.getDust(GEMaterial.Aluminium, 2), GTMaterialGen.getDust(GEMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustAlmandine", 20, 6, totalEu(82000), GTMaterialGen.getIc2(Ic2Items.ironDust, 3), GTMaterialGen.getDust(GEMaterial.Aluminium, 2), GTMaterialGen.getDust(GEMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustSpessartine", 20, 6, totalEu(90500), GTMaterialGen.getDust(GEMaterial.Manganese, 3), GTMaterialGen.getDust(GEMaterial.Aluminium, 2), GTMaterialGen.getDust(GEMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustAndradite", 20, 9, totalEu(64000), GTMaterialGen.getTube(GTMaterial.Calcium, 3), GTMaterialGen.getIc2(Ic2Items.ironDust, 2), GTMaterialGen.getDust(GEMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustGrossular", 20, 9, totalEu(102500), GTMaterialGen.getTube(GTMaterial.Calcium, 3), GTMaterialGen.getDust(GEMaterial.Aluminium, 2), GTMaterialGen.getDust(GEMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustUvarovite", 20, 9, totalEu(110000), GTMaterialGen.getTube(GTMaterial.Calcium, 3), GTMaterialGen.getDust(GEMaterial.Chrome, 2), GTMaterialGen.getDust(GEMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));




        /** New Recipes I added **/
        addRecipe(GTMaterialGen.get(Items.QUARTZ, 1), 2, totalEu(8000), GTMaterialGen.getDust(GTMaterial.Silicon, 1), GTMaterialGen.getTube(GTMaterial.Oxygen, 2));
    }

    public static void addCustomRecipe(ItemStack stack0, ItemStack stack1, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                       ItemStack... outputs) {
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack0),
                new RecipeInputItemStack(stack1), }, modifiers, outputs);
    }

    public static void addCustomRecipe(String input, int amount, ItemStack stack, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                       ItemStack... outputs) {
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount),
                new RecipeInputItemStack(stack), }, modifiers, outputs);
    }


    public static void addRecipe(ItemStack stack, int cells, RecipeModifierHelpers.IRecipeModifier[] modifiers, ItemStack... outputs) {
        if (cells > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack),
                    new RecipeInputItemStack(GTMaterialGen.get(GTItems.testTube, cells)) }, modifiers, outputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack) }, modifiers, outputs);
        }
    }

    public static void addRecipe(String input, int amount, int cells, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {
        if (cells > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount),
                    new RecipeInputItemStack(GTMaterialGen.get(GTItems.testTube, cells)) }, modifiers, outputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount) }, modifiers, outputs);
        }
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, ItemStack... outputs) {
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
        addRecipe(inlist, new MachineOutput(mods, outlist));
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
        GERecipeLists.ELECTROLYZER_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
    }

    public static void removeRecipe(String id) {
        GERecipeLists.ELECTROLYZER_RECIPE_LIST.removeRecipe(id);
    }
}
