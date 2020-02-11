package gtc_expansion.tile;

import gtc_expansion.GTCXItems;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GTCXContainerChemicalReactor;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.util.GTCXLang;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTItems;
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
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GTCXTileChemicalReactor extends GTTileBaseMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/chemicalreactor.png");
    protected static final int[] slotInputs = { 0, 1, 2 };
    public static final int[] slotOutputs = { 3, 4 };
    public static final int slotFuel = 5;
    public IFilter filter = new MachineFilter(this);
    private static final int defaultEu = 16;

    public GTCXTileChemicalReactor() {
        super(6, 2, defaultEu, 100, 32);
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
        return GTCXLang.CHEMICAL_REACTOR;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerChemicalReactor(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXChemicalReactorGui.class;
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
        return GTCXRecipeLists.CHEMICAL_REACTOR_RECIPE_LIST;
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
        return Ic2Sounds.extractorOp;
    }

    public static void init() {
        addRecipe(input(GTMaterialGen.getTube(GTMaterial.Calcium, 1)), input("dustCarbon", 1), 7500, GTMaterialGen.getDust(GTMaterial.Calcite, 2), GTMaterialGen.get(GTItems.testTube));
        addRecipe(input("dustCarbon", 1), input(GTMaterialGen.getTube(GTMaterial.Hydrogen, 4)), 1, 105000, GTMaterialGen.getTube(GTMaterial.Methane, 5));
        addRecipe(input("dustCarbon", 1), input(GTMaterialGen.getTube(GTMaterial.Nitrogen, 1)), 1, 45000, GTMaterialGen.getTube(GTCXMaterial.NitroCarbon, 2));
        addRecipe(input("dustCarbon", 1), input(GTMaterialGen.getTube(GTMaterial.Sodium, 1)), 1, 3000, GTMaterialGen.getTube(GTCXMaterial.SodiumSulfide, 2));
        addRecipe(input(GTMaterialGen.getTube(GTCXMaterial.NitroCarbon, 1)), input(GTMaterialGen.getModdedTube("water", 1)), 17490, GTMaterialGen.getTube(GTCXMaterial.Glyceryl, 2));
        addRecipe(input(GTMaterialGen.getTube(GTCXMaterial.SodiumSulfide, 1)), input(GTMaterialGen.getTube(GTMaterial.Oxygen, 1)), 60000, GTMaterialGen.getTube(GTCXMaterial.SodiumPersulfate, 2));
        addRecipe(input(GTMaterialGen.getTube(GTMaterial.Nitrogen, 1)), input(GTMaterialGen.getTube(GTMaterial.Oxygen, 1)), 37500, GTMaterialGen.getTube(GTCXMaterial.NitrogenDioxide, 2));
        addRecipe(input(GTMaterialGen.getTube(GTCXMaterial.Glyceryl, 1)), input(GTMaterialGen.getTube(GTCXMaterial.Diesel, 4)), 30000, GTMaterialGen.getTube(GTCXMaterial.NitroDiesel, 5));
        addRecipe(input(GTMaterialGen.getTube(GTCXMaterial.Glyceryl, 1)), input(GTMaterialGen.getIc2(Ic2Items.coalFuelCell, 4)), 4, 30000, GTMaterialGen.getTube(GTCXMaterial.NitroCoalFuel, 5), GTMaterialGen.getIc2(Ic2Items.emptyCell, 4));
        addRecipe(input(GTMaterialGen.getTube(GTCXMaterial.Glyceryl, 1)), input(GTMaterialGen.getTube(GTCXMaterial.CoalFuel, 4)), 30000, GTMaterialGen.getTube(GTCXMaterial.NitroCoalFuel, 5));
        addRecipe(input(GTMaterialGen.getDust(GTMaterial.Sulfur, 1)), input(GTMaterialGen.getModdedTube("water", 2)), 1, 34500, GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 3));
        addRecipe(input(GTMaterialGen.getTube(GTMaterial.Hydrogen, 4)), input(GTMaterialGen.getTube(GTMaterial.Oxygen, 2)), 300, GTMaterialGen.getModdedTube("water", 6));
        addRecipe(input(GTMaterialGen.getTube(GTCXMaterial.NitrogenDioxide, 5)), input(GTMaterialGen.getTube(GTMaterial.Sodium, 2)), 1000, GTMaterialGen.get(Items.GUNPOWDER, 5), GTMaterialGen.get(GTItems.testTube, 7));
        addRecipe(new IRecipeInput[]{input(GTMaterialGen.get(GTCXItems.magicDye)), input(GTMaterialGen.get(Items.BLAZE_POWDER)), input(GTMaterialGen.getTube(GTMaterial.Chlorine, 1))}, totalEu(5000), GTMaterialGen.getTube(GTMaterial.MagicDye, 1));
        addRecipe(new IRecipeInput[]{input("pulpWood", 4), input("dustSulfur", 1), new RecipeInputCombined(1, input("dustLithium", 1), input(GTMaterialGen.getTube(GTMaterial.Sodium, 1)))}, totalEu(6400), GTMaterialGen.get(GTItems.fuelBinder, 6));
        addRecipe(new IRecipeInput[]{input(GTMaterialGen.get(GTItems.fuelBinder)), input(GTMaterialGen.getTube(GTMaterial.Mercury, 1)), new RecipeInputCombined(1, input("dustEnderEye", 1), input(GTMaterialGen.get(Items.BLAZE_POWDER)))}, totalEu(6400), GTMaterialGen.get(GTItems.fuelBinderMagic, 3));
        addRecipe(input(GTMaterialGen.getTube(GTCXMaterial.NitricAcid, 1)), input(GTMaterialGen.getTube(GTMaterial.Potassium, 1)), 600, GTMaterialGen.getDust(GTCXMaterial.Saltpeter, 1), GTMaterialGen.get(GTItems.testTube, 2));
        addRecipe(input(GTMaterialGen.getTube(GTCXMaterial.NitrogenDioxide, 3)), input(GTMaterialGen.getWater(1)), 600, GTMaterialGen.getTube(GTCXMaterial.NitricAcid, 2), GTMaterialGen.get(GTItems.testTube, 2));
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, int totalEu, ItemStack... output) {
        addRecipe(input1, input2, 0, totalEu, output);
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, int cells, int totalEu, ItemStack... output) {
        IRecipeInput[] inputs = new IRecipeInput[]{input1, input2};
        if (cells > 0){
            inputs = new IRecipeInput[]{input1, input2, input(new ItemStack(GTItems.testTube, cells))};
        }
        addRecipe(inputs, totalEu(totalEu), output);
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
        GTCXRecipeLists.CHEMICAL_REACTOR_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), 16);
    }
}
