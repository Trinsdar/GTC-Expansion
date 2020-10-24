package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerMicrowave;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.GTTileBaseMachine;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.IMachineRecipeList.RecipeEntry;
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
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GTCXTileMicrowave extends GTTileBaseMachine {

    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/microwave.png");
    public static final int slotInput = 0;
    public static final int slotOutput = 1;
    public static final int slotFuel = 2;
    protected static final int[] slotInputs = { slotInput };
    public IFilter filter = new MachineFilter(this);
    private static final int defaultEu = 4;
    public static final List<ItemStack> explodeList = new ArrayList<>();

    public GTCXTileMicrowave() {
        super(3, 4, defaultEu, 20, 32);
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
        return GTCXLang.MICROWAVE;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerMicrowave(player.inventory, this);
    }

    @Override
    public void update() {
        if (this.hasEnergy(10)){
            if (inventory.get(slotInput).getItem() instanceof ItemEgg){
                world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true);
                this.world.setBlockToAir(this.getPos());
            }
            for (ItemStack stack : explodeList){
                if (GTHelperStack.isEqual(stack, inventory.get(slotInput))){
                    world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true);
                    this.world.setBlockToAir(this.getPos());
                    break;
                }
            }
        }
        super.update();
    }

    @Override
    public boolean isValidInput(ItemStack stack) {
        if (stack.getItem() instanceof ItemEgg){
            return true;
        }
        for (ItemStack stack1 : explodeList){
            if (GTHelperStack.isEqual(stack1, stack)){
                return true;
            }
        }
        return super.isValidInput(stack);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXMicrowaveGui.class;
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
        return GTCXRecipeLists.MICROWAVE_RECIPE_LIST;
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
        List<ItemStack> stacks = new ArrayList<>();
        for (RecipeEntry entry : ClassicRecipes.furnace.getRecipeMap()) {
            if (entry.getOutput().getAllOutputs().get(0).getItem() instanceof ItemFood) {
                for (ItemStack input : entry.getInput().getInputs()){
                    for (ItemStack stack : explodeList){
                        if (GTHelperStack.isEqual(stack, input)){
                            stacks.add(stack);
                        }
                    }
                }
                addRecipe(entry.getInput(), entry.getOutput().getAllOutputs().get(0));
            }
        }
        explodeList.removeAll(stacks);
    }

    public static void addRecipe(ItemStack input, ItemStack output) {
        List<IRecipeInput> inputs = new ArrayList<>();
        inputs.add(new RecipeInputItemStack(input));
        addRecipe(input(input), output);
    }

    public static void addRecipe(String input, int amount, ItemStack output) {
        addRecipe(input(input, amount), output);
    }

    public static void addRecipe(IRecipeInput input1, ItemStack output) {
        List<IRecipeInput> inputs = new ArrayList<>();
        inputs.add(input1);
        addRecipe(inputs, new MachineOutput(null, output), output.getUnlocalizedName());
    }

    public static void addRecipe(IRecipeInput input1, ItemStack output, String recipeId) {
        List<IRecipeInput> inputs = new ArrayList<>();
        inputs.add(input1);
        addRecipe(inputs, new MachineOutput(null, output), recipeId);
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
        GTCXRecipeLists.MICROWAVE_RECIPE_LIST.addRecipe(input, output, recipeId, 4);
    }
}
