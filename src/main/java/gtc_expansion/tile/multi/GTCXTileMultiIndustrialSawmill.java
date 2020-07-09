package gtc_expansion.tile.multi;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerIndustrialSawmill;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.multi.GTTileMultiBaseMachine;
import ic2.api.classic.item.IMachineUpgradeItem.UpgradeType;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.IMachineRecipeList;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasInventory;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.MachineFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GTCXTileMultiIndustrialSawmill extends GTTileMultiBaseMachine implements ITankListener, IClickable, IGTDebuggableTile {
    protected static final int slotDisplayIn = 0;
    protected static final int[] slotInputs = { 1 };
    protected static final int[] slotOutputs = { 2, 3, 4 };
    protected static final int slotFuel = 5;
    public IFilter filter = new MachineFilter(this);
    public static final IBlockState casingStandardState = GTCXBlocks.casingStandard.getDefaultState();
    public static final IBlockState casingReinforcedState = GTCXBlocks.casingReinforced.getDefaultState();
    public static final IBlockState waterState = Blocks.WATER.getDefaultState();
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialsawmill.png");
    private static final int defaultEu = 30;
    @NetworkField(index = 13)
    private final IC2Tank inputTank = new IC2Tank(16000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && fluid.isFluidEqual(GTMaterialGen.getFluidStack("water", 1));
        }
    };

    public GTCXTileMultiIndustrialSawmill() {
        super(6, 2, defaultEu, 128);
        maxEnergy = 10000;
        this.inputTank.addListener(this);
        this.addGuiFields("inputTank");
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
    public ResourceLocation getStartSoundFile() {
        return GTCExpansion.getAprilFirstSound(super.getStartSoundFile());
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.INDUSTRIAL_SAWMILL;
    }

    @Override
    public int[] getInputSlots() {
        return slotInputs;
    }

    @Override
    public IFilter[] getInputFilters(int[] ints) {
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
        return GTCXRecipeLists.INDUSTRIAL_SAWMILL_RECIPE_LIST;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXIndustrialSawmillGui.class;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.getNetwork().updateTileGuiField(this, "inputTank");
        this.inventory.set(slotDisplayIn, ItemDisplayIcon.createWithFluidStack(this.inputTank.getFluid()));
        shouldCheckRecipe = true;
    }

    @Override
    public Set<UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(UpgradeType.ImportExport, UpgradeType.RedstoneControl, UpgradeType.Sounds));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerIndustrialSawmill(entityPlayer.inventory, this);
    }

    @Override
    public boolean canContinue() {
        return inputTank.getFluidAmount() >= 1000;
    }

    @Override
    public void onRecipeComplete() {
        inputTank.drainInternal(1000, true);
    }

    public boolean checkRecipe(GTRecipeMultiInputList.MultiRecipe entry, List<ItemStack> inputs) {
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(entry.getInputs());
        int index = 0;
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
            int toFind = key.getAmount();
            if (index > 0){
                keyIter.remove();
            }
            for (Iterator<ItemStack> inputIter = inputs.iterator(); inputIter.hasNext();) {
                ItemStack input = inputIter.next();
                if (key.matches(input)) {
                    if (input.getCount() >= toFind) {
                        input.shrink(toFind);
                        keyIter.remove();
                        if (input.isEmpty()) {
                            inputIter.remove();
                        }
                        break;
                    }
                    toFind -= input.getCount();
                    input.setCount(0);
                    inputIter.remove();
                }
            }
            index++;
        }
        return recipeKeys.isEmpty();
    }

    @Override
    public List<ItemStack> getInputs() {
        ArrayList<ItemStack> inputs = new ArrayList<>();
        for (int i : getInputSlots()) {
            if (inventory.get(i).isEmpty()) {
                continue;
            }
            inputs.add(inventory.get(i));
        }
        return inputs;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.inputTank.readFromNBT(nbt.getCompoundTag("inputTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.inputTank.writeToNBT(this.getTag(nbt, "inputTank"));
        return nbt;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<>();

        for(int i = 0; i < this.inventory.size(); ++i) {
            ItemStack stack = this.inventory.get(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemDisplayIcon){
                    continue;
                }
                list.add(stack);
            }
        }

        InventoryHandler handler = this.getHandler();
        if (handler != null) {
            IHasInventory inv = handler.getUpgradeSlots();

            for(int i = 0; i < inv.getSlotCount(); ++i) {
                ItemStack result = inv.getStackInSlot(i);
                if (result != null) {
                    list.add(result);
                }
            }
        }

        return list;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.inputTank);
        }
        return super.getCapability(capability, facing);
    }


    public static void init(){
        for(IMachineRecipeList.RecipeEntry  entry : ClassicRecipes.sawMill.getRecipeMap()){
            IRecipeInput input = entry.getInput();
            if (GTHelperStack.isEqual(input.getInputs().get(0), Ic2Items.rubberWood)){
                continue;
            }
            if (GTHelperStack.matchOreDict(input.getInputs().get(0), "logWood")){
                addRecipe(input, 6000, entry.getOutput().getAllOutputs().get(0), GTMaterialGen.getDust(GTMaterial.Wood, 1));
            } else {
                addRecipe(input, 6000, entry.getOutput().getAllOutputs().get(0));
            }
        }
        addRecipe(input("logRubber", 1), 6000, Ic2Items.stickyResin.copy(), GTMaterialGen.getDust(GTMaterial.Wood, 8), GTMaterialGen.get(Blocks.PLANKS, 9, 3));
    }

    public static void addRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {

        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputFluid(GTMaterialGen.getFluidStack("water", 1000)) }, modifiers, outlist);
    }

    public static void addRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {

        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputFluid(GTMaterialGen.getFluidStack("water", 1000)) }, modifiers, outlist);
    }

    public static void addRecipe(IRecipeInput input, int totalEu,
                                        ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { input, new RecipeInputFluid(GTMaterialGen.getFluidStack("water", 1000))}, totalEu(totalEu), outlist);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, List<ItemStack> outputs) {
        List<IRecipeInput> inlist = new ArrayList<>();
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }

        addRecipe(inlist, new MachineOutput(mods, outputs));
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
        GTCXRecipeLists.INDUSTRIAL_SAWMILL_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
    }

    @Override
    public boolean checkStructure() {
        if (!world.isAreaLoaded(pos, 3)) {
            return false;
        }
        int3 dir = new int3(getPos(), getFacing());
        //Top Layer
        if (!isReinforcedCasing(dir.down(1))){
            return false;
        }
        if (!(isStandardCasing(dir.back(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.left(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.forward(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.forward(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.right(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.right(1)))){
            return false;
        }
        if (!(isStandardCasing(dir.back(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.back(1)))) {
            return false;
        }
        return true;
    }

    public boolean isStandardCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == casingStandardState;
    }

    public boolean isReinforcedCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == casingReinforcedState;
    }


    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public void onLeftClick(EntityPlayer var1, Side var2) {
    }

    @Override
    public boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing enumFacing, Side side) {
        return GTHelperFluid.doClickableFluidContainerThings(player, hand, world, pos, this.inputTank);
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        FluidStack fluid = this.inputTank.getFluid();
        map.put("Input Tank: " + (fluid != null ? fluid.amount + "mb of " + fluid.getLocalizedName() : "Empty"), false);
    }
}
