package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXConfiguration;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerAssemblingMachine;
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
import gtclassic.common.GTBlocks;
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
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
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
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static gtc_expansion.data.GTCXValues.MATERIAL_REFINED_IRON;
import static gtc_expansion.data.GTCXValues.REFINED_IRON;
import static gtclassic.api.recipe.GTRecipeCraftingHandler.combineRecipeObjects;

public class GTCXTileAssemblingMachine extends GTTileBaseMachine {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/assemblingmachine.png");
    public static final int[] slotInputs = {0, 1};
    public static final int[] slotOutputs = {2, 3};
    public static final int slotFuel = 4;
    public IFilter filter = new MachineFilter(this);
    private static final int defaultEu = 16;

    public GTCXTileAssemblingMachine() {
        super(5, 4, defaultEu, 100, 32);
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
        return GTCXLang.ASSEMBLING_MACHINE;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerAssemblingMachine(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXAssemblingMachineGui.class;
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
        return GTCXRecipeLists.ASSEMBLING_MACHINE_RECIPE_LIST;
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
        return GTCExpansion.getAprilFirstSound(Ic2Sounds.extractorOp);
    }

    public static void init() {
        addRecipe(input("stickWood", 1), new RecipeInputCombined(1, input(new ItemStack(Items.COAL, 1, 0)), input(new ItemStack(Items.COAL, 1, 1))), 400, GTMaterialGen.get(Blocks.TORCH, 4));
        addRecipe("slimeball", 1, "string", 1, 400, GTMaterialGen.get(Items.LEAD, 2));
        addRecipe(GTMaterialGen.getIc2(Ic2Items.coalBall, 8), GTMaterialGen.get(Blocks.BRICK_BLOCK), 1600, Ic2Items.coalChunk);
        addRecipe(GTMaterialGen.get(GTCXItems.advancedCircuitBoard), GTMaterialGen.get(GTCXItems.advancedCircuitParts, 2), 3200, Ic2Items.advancedCircuit);
        addRecipe(GTMaterialGen.get(GTCXItems.processorCircuitBoard), GTMaterialGen.get(GTItems.chipData), 12800, GTMaterialGen.get(GTItems.circuitData));
        addRecipe(GTMaterialGen.get(GTCXItems.processorCircuitBoard), Ic2Items.lapotronCrystal, 12800, GTMaterialGen.get(GTItems.circuitEnergy));
        addRecipe(GTMaterialGen.get(GTItems.circuitData), GTMaterialGen.get(GTItems.chipData, 8), 204800, GTMaterialGen.get(GTItems.orbData, 4));
        addRecipe(GTMaterialGen.get(GTBlocks.tileEchotron), GTMaterialGen.get(GTItems.chipData, 4), 51200, GTMaterialGen.get(GTItems.echotron));
        addRecipe("plateElectrum", 2, Ic2Items.electricCircuit, 12800, GTMaterialGen.get(GTCXItems.advancedCircuitBoard));
        addRecipe("plateElectrum", 4, "plateSilicon", 1, 12800, GTMaterialGen.get(GTCXItems.advancedCircuitBoard, 2));
        addRecipe("plateAluminium", 2, Ic2Items.electricCircuit, 12800, GTMaterialGen.get(GTCXItems.machineParts, 3));
        IRecipeInput pump = Loader.isModLoaded("buildcraftfactory") ? combineRecipeObjects(Ic2Items.pump.copy(), GTMaterialGen.getModBlock("buildcraftfactory", "pump")) : input(Ic2Items.pump.copy());
        addRecipe(GTCXValues.MATERIAL_MACHINE, pump, 12800, GTMaterialGen.get(GTCXItems.pumpModule));
        if (GTCXConfiguration.general.harderCircuits){
            addRecipe(GTRecipeCraftingHandler.combineRecipeObjects("plateSteel", "plateAluminium", "plateSilver"), input("plateRedAlloy", 2), 800, GTMaterialGen.get(GTCXItems.basicCircuitBoard));
        }
        addRecipe(GTRecipeCraftingHandler.combineRecipeObjects("plateSteel", "plateAluminium"), input("plateElectrum", 2), 800, GTMaterialGen.get(GTCXItems.basicCircuitBoard, 2));
        addRecipe(GTCXValues.getRefinedIronPlate(), 2, Ic2Items.electricCircuit, 12800, GTMaterialGen.get(GTCXItems.machineParts, 4));
        addRecipe("platePlatinum", 1, Ic2Items.advancedCircuit, 51200, GTMaterialGen.get(GTCXItems.processorCircuitBoard));
        addRecipe(new RecipeInputCombined(8, input("gemEmerald", 8), input("gemOlivine", 8)), new RecipeInputItemStack(Ic2Items.advancedCircuit), 51200, GTMaterialGen.get(GTItems.chipData, 4));
        String ingot = GTCXConfiguration.general.forceSteelCasings && !GTCXConfiguration.general.gt2Mode ? REFINED_IRON : MATERIAL_REFINED_IRON;
        addRecipe(ingot, 6, GTMaterialGen.get(GTCXItems.machineParts), 3200, Ic2Items.machine);
        addRecipe("dustFlint", 5, GTMaterialGen.get(Blocks.TNT, 3), 1600, GTMaterialGen.getIc2(Ic2Items.industrialTNT, 5));
        addRecipe("dustGunpowder", 4, GTMaterialGen.get(Blocks.SAND), 400, GTMaterialGen.get(Blocks.TNT));
        addRecipe(input("dustGlowstone", 1), GTValues.INPUT_LAPIS_ANY, 1600, GTMaterialGen.get(GTCXItems.advancedCircuitParts, 2));
        addRecipe(GTMaterialGen.get(GTItems.lithiumBattery), Ic2Items.cropAnalyzer, 20480, GTMaterialGen.get(GTItems.portableScanner));
        ItemStack cable = GTCXConfiguration.general.harderCircuits ? Ic2Items.copperCable : Ic2Items.insulatedCopperCable;
        addRecipe(GTMaterialGen.get(GTCXItems.basicCircuitBoard), GTMaterialGen.getIc2(cable, 3), 800, Ic2Items.electricCircuit);
        addRecipe("plateIridiumAlloy", 1, Ic2Items.reinforcedStone, 1600, Ic2Items.iridiumStone);
        addRecipe("plateAluminium", 4, Ic2Items.generator, 51200, GTMaterialGen.getIc2(Ic2Items.waterMill, 2));
        addRecipe("plateMagnalium", 2, Ic2Items.generator, 51200, Ic2Items.windMill);
        addRecipe(GTMaterialGen.getIc2(Ic2Items.carbonPlate, 4), Ic2Items.generator, 51200, Ic2Items.windMill);
//        addRecipe(GTMaterialGen.getFluidPipeSmall(GTCXMaterial.TungstenSteel, 1), Ic2Items.fluidExportUpgrade, 51200, GTMaterialGen.getFluidPipeSmall(GTMaterial.HighPressure, 1));
//        addRecipe(GTMaterialGen.getFluidPipe(GTCXMaterial.TungstenSteel, 1), Ic2Items.fluidExportUpgrade, 51200, GTMaterialGen.getFluidPipe(GTMaterial.HighPressure, 1));
//        addRecipe(GTMaterialGen.getFluidPipeLarge(GTCXMaterial.TungstenSteel, 1), Ic2Items.fluidExportUpgrade, 51200, GTMaterialGen.getFluidPipeLarge(GTMaterial.HighPressure, 1));
        addRecipe("plankWood", 4, 3200, new ItemStack(Blocks.CRAFTING_TABLE));
        addRecipe("cobblestone", 8, 3200, new ItemStack(Blocks.FURNACE));
        addRecipe(Ic2Items.emptyCell, "dustRedstone", 1, 1600, GTMaterialGen.get(GTItems.sprayCanEmpty));
        addRecipe("plateTungstensteel", 1, Ic2Items.iridiumStone, 1600, GTMaterialGen.get(GTCXBlocks.iridiumTungstensteelBlock));
        addRecipe(GTMaterialGen.get(GTCXBlocks.tungstensteelReinforcedStone), Ic2Items.iridiumPlate, 1600, GTMaterialGen.get(GTCXBlocks.iridiumTungstensteelBlock));
        addRecipe("plateTungstensteel", 1, Ic2Items.reinforcedStone, 1600, GTMaterialGen.get(GTCXBlocks.tungstensteelReinforcedStone));
        addRecipe(Ic2Items.memoryStick, Ic2Items.frequencyTransmitter, 3000, GTMaterialGen.get(GTItems.sensorStick));
        addRecipe(GTMaterialGen.getIc2(Ic2Items.carbonMesh, 16), GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 1), 3000, GTMaterialGen.get(GTCXItems.lavaFilter));
        addRecipe("dustPhosphorus", 1, "stickWood", 1, 1600, GTMaterialGen.get(GTCXItems.match, 4));
        addRecipe(GTMaterialGen.get(GTCXItems.match, 64), GTMaterialGen.get(Items.PAPER, 2), 1600, GTMaterialGen.get(GTCXItems.matchBox, 1));
        addRecipe("rodIron", 6, GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 2), 1600, GTMaterialGen.getIc2(Ic2Items.ironFence, 6));
        addRecipe("rodIron", 6, GTMaterialGen.get(GTCXItems.integratedCircuit, 1, 3), 1600, GTMaterialGen.get(Blocks.IRON_BARS, 8));
        addRecipe(GTMaterialGen.get(GTCXItems.conveyorModule), GTMaterialGen.get(GTCXItems.pumpModule), 51200, GTMaterialGen.get(GTCXItems.itemTransportValve));
        addRecipe(new RecipeInputCombined(2, input(GTCXValues.REFINED_IRON, 2), input(GTCXValues.PRE + "Aluminium", 2), input(GTCXValues.PRE + "Aluminum", 2)), input(GTMaterialGen.get(Blocks.IRON_BARS, 2)), 12800, GTMaterialGen.get(GTCXItems.drain));
        addRecipe(new RecipeInputCombined(2, input(GTCXValues.REFINED_IRON, 2), input(GTCXValues.PRE + "Aluminium", 2), input(GTCXValues.PRE + "Aluminum", 2)), input(GTMaterialGen.get(Items.IRON_DOOR)), 12800, GTMaterialGen.get(GTCXItems.shutter, 2));
        addRecipe(GTCXValues.MATERIAL_MACHINE, input(GTMaterialGen.get(Blocks.LEVER)), 12800, GTMaterialGen.get(GTCXItems.redstoneController));
        addRecipe(GTMaterialGen.get(GTCXItems.shutter), GTValues.CIRCUIT_ADVANCED, 2, 12000, GTMaterialGen.get(GTCXItems.fluidFilter));
        addRecipe(GTCXValues.PRE + "Iron", 5, "chestWood", 1, 1600, GTMaterialGen.get(Blocks.HOPPER));
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
        addRecipe(inlist, new MachineOutput(mods, outlist));
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
        GTCXRecipeLists.ASSEMBLING_MACHINE_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), 16);
    }
}
