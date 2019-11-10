package gtc_expansion.tile;

import gtc_expansion.GEItems;
import gtc_expansion.GEMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerAssemblingMachine;
import gtc_expansion.item.GEItemMisc;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.material.GEMaterialGen;
import gtc_expansion.recipes.GERecipe;
import gtc_expansion.recipes.GERecipeLists;
import gtc_expansion.util.GELang;
import gtclassic.GTBlocks;
import gtclassic.GTItems;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.GTTileBaseMachine;
import gtclassic.util.recipe.GTRecipeMultiInputList;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
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
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.lang.components.base.LangComponentHolder;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GETileAssemblingMachine extends GTTileBaseMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/assemblingmachine.png");
    public static final int[] slotInputs = {0, 1};
    public static final int[] slotOutputs = {2, 3};
    public static final int slotFuel = 4;
    public IFilter filter = new MachineFilter(this);
    private static final int defaultEu = 16;

    public GETileAssemblingMachine() {
        super(5, 2, defaultEu, 100, 32);
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
        return GELang.ASSEMBLING_MACHINE;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GEContainerAssemblingMachine(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GEMachineGui.GEAssemblingMachineGui.class;
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
        return GERecipeLists.ASSEMBLING_MACHINE_RECIPE_LIST;
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
        addRecipe(input("stickWood", 1), new RecipeInputCombined(1, input(new ItemStack(Items.COAL, 1, 0)), input(new ItemStack(Items.COAL, 1, 1))), 400, GTMaterialGen.get(Blocks.TORCH, 4));
        addRecipe("slimeball", 1, "string", 1, 400, GTMaterialGen.get(Items.LEAD, 2));
        addRecipe(GTMaterialGen.getIc2(Ic2Items.coalBall, 8), GTMaterialGen.get(Blocks.BRICK_BLOCK), 1600, Ic2Items.coalChunk);
        addRecipe(GTMaterialGen.get(GEItems.advancedCircuitBoard), GTMaterialGen.get(GEItems.advancedCircuitParts, 2), 3200, Ic2Items.advancedCircuit);
        addRecipe(GTMaterialGen.get(GEItems.processorCircuitBoard), GTMaterialGen.get(GTItems.chipData), 12800, GTMaterialGen.get(GTItems.circuitData));
        addRecipe(GTMaterialGen.get(GEItems.processorCircuitBoard), Ic2Items.lapotronCrystal, 12800, GTMaterialGen.get(GTItems.circuitEnergy));
        addRecipe(GTMaterialGen.get(GTItems.circuitData), GTMaterialGen.get(GTItems.chipData, 8), 204800, GTMaterialGen.get(GTItems.orbData, 4));
        addRecipe(GTMaterialGen.get(GTBlocks.tileEchotron), GTMaterialGen.get(GTItems.chipData, 4), 51200, GTMaterialGen.get(GTItems.echotron));
        addRecipe("plateAluminium", 2, Ic2Items.electricCircuit, 3200, GTMaterialGen.get(GEItems.advancedCircuitBoard));
        addRecipe("plateAluminium", 4, "plateSilicon", 1, 3200, GTMaterialGen.get(GEItems.advancedCircuitBoard, 2));
        addRecipe("plateAluminium", 2, Ic2Items.electricCircuit, 12800, GTMaterialGen.get(GEItems.machineParts, 3));
        addRecipe(GERecipe.plateMachine, input("plateRedAlloy", 2), 800, GTMaterialGen.get(GEItems.basicCircuitBoard));
        addRecipe(GERecipe.plateMachine, input("plateElectrum", 2), 800, GTMaterialGen.get(GEItems.basicCircuitBoard, 2));
        addRecipe(GERecipe.getRefinedIronPlate(), 2, Ic2Items.electricCircuit, 12800, GTMaterialGen.get(GEItems.machineParts, 4));
        addRecipe("platePlatinum", 1, Ic2Items.advancedCircuit, 12800, GTMaterialGen.get(GEItems.processorCircuitBoard));
        addRecipe(new RecipeInputCombined(1, input("gemEmerald", 8), input("gemOlivine", 8)), new RecipeInputItemStack(Ic2Items.advancedCircuit), 51200, GTMaterialGen.get(GTItems.chipData, 4));
        addRecipe("plateAluminium", 6, GTMaterialGen.get(GEItems.machineParts), 3200, GEMaterialGen.getHull(GEMaterial.Aluminium, 1));
        addRecipe("plateBronze", 6, GTMaterialGen.get(GEItems.machineParts), 3200, GEMaterialGen.getHull(GEMaterial.Bronze, 1));
        addRecipe("plateBrass", 6, GTMaterialGen.get(GEItems.machineParts), 3200, GEMaterialGen.getHull(GEMaterial.Brass, 1));
        if (!IC2.config.getFlag("SteelRecipes")) addRecipe("plateSteel", 6, GTMaterialGen.get(GEItems.machineParts), 3200, GEMaterialGen.getHull(GEMaterial.Steel, 1));
        addRecipe("plateTitanium", 6, GTMaterialGen.get(GEItems.machineParts), 3200, GEMaterialGen.getHull(GEMaterial.Titanium, 1));
        addRecipe(GERecipe.getRefinedIronPlate(), 6, GTMaterialGen.get(GEItems.machineParts), 3200, Ic2Items.machine);
        addRecipe("plateTungstensteel", 6, GTMaterialGen.get(GEItems.machineParts), 3200, GEMaterialGen.getHull(GEMaterial.TungstenSteel, 1));
        addRecipe("plateStainlessSteel", 6, GTMaterialGen.get(GEItems.machineParts), 3200, GEMaterialGen.getHull(GEMaterial.StainlessSteel, 1));
        addRecipe("dustFlint", 5, GTMaterialGen.get(Blocks.TNT), 1600, GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5));
        addRecipe("dustGunpowder", 4, GTMaterialGen.get(Blocks.SAND), 400, GTMaterialGen.get(Blocks.TNT));
        addRecipe(input("dustGlowstone", 1), GERecipe.anyLapis, 1600, GTMaterialGen.get(GEItems.advancedCircuitParts, 2));
        addRecipe(GTMaterialGen.get(GTItems.lithiumBattery), Ic2Items.cropAnalyzer, 204800, GTMaterialGen.get(GTItems.portableScanner));
        addRecipe(GTMaterialGen.get(GEItems.basicCircuitBoard), GTMaterialGen.getIc2(Ic2Items.copperCable, 3), 800, Ic2Items.electricCircuit);
        addRecipe("plateIridiumAlloy", 1, Ic2Items.reinforcedStone, 1600, Ic2Items.iridiumStone);
        addRecipe("plateAluminium", 4, Ic2Items.generator, 51200, GTMaterialGen.getIc2(Ic2Items.waterMill, 2));
        addRecipe("plateAluminium", 2, Ic2Items.generator, 51200, Ic2Items.windMill);
        addRecipe(GTMaterialGen.getIc2(Ic2Items.carbonPlate, 4), Ic2Items.generator, 51200, Ic2Items.windMill);
        addRecipe("plankWood", 4, 3200, new ItemStack(Blocks.CRAFTING_TABLE));
        addRecipe("cobblestone", 8, 3200, new ItemStack(Blocks.FURNACE));
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

    public static void addRecipe(ItemStack input1, ItemStack input2, int totalEu, ItemStack output) {
        addRecipe(new IRecipeInput[]{new RecipeInputItemStack(input1), new RecipeInputItemStack(input2)}, totalEu(totalEu), output);
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
        GERecipeLists.ASSEMBLING_MACHINE_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), 16);
    }
}
