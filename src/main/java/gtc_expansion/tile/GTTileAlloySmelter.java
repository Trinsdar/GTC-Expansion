package gtc_expansion.tile;

import gtc_expansion.GTGuiMachine2;
import gtc_expansion.GTMod2;
import gtc_expansion.container.GTContainerAlloySmelter;
import gtclassic.GTMod;
import gtclassic.tile.GTTileBaseMachine;
import gtclassic.util.int3;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GTTileAlloySmelter extends GTTileBaseMachine {

    public static final GTRecipeMultiInputList RECIPE_LIST = new GTRecipeMultiInputList("gt.alloysmelter");
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTMod2.MODID, "textures/gui/alloysmelter.png");
    public static final int slotInput0 = 0;
    public static final int slotInput1 = 1;
    public static final int slotOutput = 2;
    public static final int slotFuel = 3;
    protected static final int[] slotInputs = { slotInput0, slotInput1 };
    public IFilter filter = new MachineFilter(this);
    private static final int defaultEu = 16;

    public GTTileAlloySmelter() {
        super(9, 2, defaultEu, 100, 32);
        setFuelSlot(slotFuel);
        maxEnergy = 10000;
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutput);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutput);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerInputFilter(filter, slotInputs);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Input, slotInputs);
        handler.registerSlotType(SlotType.Output, slotOutput);
    }

    @Override
    public LocaleComp getBlockName() {
        return new LangComponentHolder.LocaleBlockComp(this.getBlockType().getUnlocalizedName());
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTContainerAlloySmelter(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTGuiMachine2.GTAlloySmelterGui.class;
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
        return new int[]{slotOutput};
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return RECIPE_LIST;
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
    /**
     * Recipes not handled by the iterator class
     */
    public static void init() {


    }

    /**
     * Simple utility to generate recipe variants for the Alloy Smelter
     */
    public static void addAlloyRecipe(String input1, int amount1, String input2, int amount2, ItemStack output) {
        addRecipe("ingot" + input1, amount1, "ingot" + input2, amount2, output);
        addRecipe("dust" + input1, amount1, "dust" + input2, amount2, output);
        addRecipe("dust" + input1, amount1, "ingot" + input2, amount2, output);
        addRecipe("ingot" + input1, amount1, "dust" + input2, amount2, output);
    }

    /**
     * Simple utility to generate mold recipe variants for the Alloy Smelter
     */
    public static void addMoldingRecipe(String input1, int amount1, ItemStack input2, ItemStack output) {
        addRecipe("ingot" + input1, amount1, input2, output);
        addRecipe("dust" + input1, amount1, input2, output);
    }

    public static void addRecipe(String input1, int amount1, ItemStack input2, ItemStack output) {
        List<IRecipeInput> inputs = new ArrayList<>();
        inputs.add((IRecipeInput) (new RecipeInputOreDict(input1, amount1)));
        inputs.add((IRecipeInput) (new RecipeInputItemStack(input2)));
        addRecipe(inputs, new MachineOutput(null, output));
    }

    public static void addRecipe(ItemStack input1, String input2, int amount2, ItemStack output) {
        List<IRecipeInput> inputs = new ArrayList<>();
        inputs.add((IRecipeInput) (new RecipeInputItemStack(input1)));
        inputs.add((IRecipeInput) (new RecipeInputOreDict(input2, amount2)));
        addRecipe(inputs, new MachineOutput(null, output));
    }

    public static void addRecipe(String input1, int amount1, String input2, int amount2, ItemStack output) {
        List<IRecipeInput> inputs = new ArrayList<>();
        inputs.add((IRecipeInput) (new RecipeInputOreDict(input1, amount1)));
        inputs.add((IRecipeInput) (new RecipeInputOreDict(input2, amount2)));
        addRecipe(inputs, new MachineOutput(null, output));
    }

    public static void addRecipe(ItemStack input1, ItemStack input2, ItemStack output) {
        List<IRecipeInput> inputs = new ArrayList<>();
        inputs.add((IRecipeInput) (new RecipeInputItemStack(input1)));
        inputs.add((IRecipeInput) (new RecipeInputItemStack(input2)));
        addRecipe(inputs, new MachineOutput(null, output));
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
        RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getDisplayName(), 16);
    }
}
