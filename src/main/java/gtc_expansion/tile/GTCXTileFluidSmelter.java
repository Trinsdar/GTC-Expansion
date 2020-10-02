package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerFluidSmelter;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.helpers.GTUtility;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTFluidMachineOutput;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTConfig;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
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
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class GTCXTileFluidSmelter extends GTTileBaseMachine implements ITankListener, IClickable {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/fluidsmelter.png");
    public static final int slotInput = 0;
    public static final int slotOutputDisplay = 1;
    public static final int slotFuel = 2;
    public static final int slotCoil = 3;
    protected static final int[] slotInputs = { slotInput };
    public IFilter filter = new MachineFilter(this);
    private static final int defaultEu = 64;
    @NetworkField(index = 13)
    private final IC2Tank outputTank = new IC2Tank(16000);
    public int maxHeat;
    public int heat;
    public static final String neededHeat = "minHeat";
    public static final Map<ItemStack, Integer> coilsSlotWhitelist = new LinkedHashMap<>();
    private boolean reachedMaxHeat = false;

    public GTCXTileFluidSmelter() {
        super(4, 2, defaultEu, 100, 128);
        setFuelSlot(slotFuel);
        this.outputTank.addListener(this);
        this.outputTank.setCanFill(false);
        maxEnergy = 10000;
        this.addGuiFields("outputTank", "maxHeat", "heat");
        maxHeat = 750;
        heat = 0;
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.HORIZONTAL, slotInputs);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerInputFilter(filter, slotInputs);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Input, slotInputs);
    }

    @Override
    public int getMaxStackSize(int slot) {
        if (slot == slotCoil){
            return 6;
        }
        return super.getMaxStackSize(slot);
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.FLUID_SMELTER;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerFluidSmelter(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXFluidSmelterGui.class;
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
        return slot != slotFuel && slot != slotCoil;
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.inventory.set(slotOutputDisplay, ItemDisplayIcon.createWithFluidStack(this.outputTank.getFluid()));
        this.getNetwork().updateTileGuiField(this, "outputTank");
        shouldCheckRecipe = true;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{};
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GTCXRecipeLists.FLUID_SMELTER_RECIPE_LIST;
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

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        super.setStackInSlot(slot, stack);
        maxHeat = 750;
        for (Map.Entry<ItemStack, Integer> entry : coilsSlotWhitelist.entrySet()){
            if (inventory.get(slotCoil).isEmpty()){
                break;
            }
            if (GTHelperStack.isEqual(inventory.get(slotCoil), entry.getKey())){
                int increase = entry.getValue() * inventory.get(slotCoil).getCount();
                maxHeat += increase;
                break;
            }
        }
        this.getNetwork().updateTileGuiField(this, "maxHeat");
    }

    @Override
    public void update() {
        super.update();
        if (((lastRecipe != null && !this.inventory.get(slotInput).isEmpty()) || this.redstone) && this.energy > 0) {
            if (this.heat < maxHeat) {
                ++this.heat;
                this.getNetwork().updateTileGuiField(this, "heat");
            }
            if (this.heat == maxHeat && !reachedMaxHeat){
                shouldCheckRecipe = true;
                reachedMaxHeat = true;
            }
            if (this.heat > maxHeat){
                this.heat = maxHeat;
                this.shouldCheckRecipe = true;
                this.getNetwork().updateTileGuiField(this, "heat");
            }

            if (!this.isActive){
                this.setActive(true);
            }
            this.useEnergy(1);
        } else if (this.heat > 0) {
            this.heat -= Math.min(this.heat, 4);
            if (this.reachedMaxHeat){
                reachedMaxHeat = false;
            }
            this.getNetwork().updateTileGuiField(this, "heat");
            if (this.isActive){
                this.setActive(false);
            }
        }
        GTUtility.exportFluidFromMachineToSide(this, outputTank, right(), 1000);
    }

    public EnumFacing left(){
        try { // for load cases
            return this.getFacing().rotateY();
        } catch (IllegalStateException e){
            return this.getFacing();
        }
    }

    public EnumFacing right(){
        try { // for load cases
            return this.getFacing().rotateYCCW();
        } catch (IllegalStateException e){
            return this.getFacing();
        }
    }

    @Override
    public void process(MultiRecipe recipe) {
        MachineOutput output = recipe.getOutputs().copy();
        if (output instanceof GTFluidMachineOutput){
            GTFluidMachineOutput fluidOutput = (GTFluidMachineOutput)output;
            for (FluidStack fluid : fluidOutput.getFluids()){
                if (outputTank.getFluidAmount() == 0 || outputTank.getFluid().isFluidEqual(fluid)){
                    outputTank.fillInternal(fluid, true);
                }
            }
        }
        onRecipeComplete();
        NBTTagCompound nbt = recipe.getOutputs().getMetadata();
        boolean shiftContainers = nbt != null && nbt.getBoolean(MOVE_CONTAINER_TAG);
        List<ItemStack> inputs = getInputs();
        for (IRecipeInput key : recipe.getInputs()) {
            int count = key.getAmount();
            ItemStack input = inventory.get(slotInput);
            if (key.matches(input)) {
                if (input.getCount() >= count) {
                    if (input.getItem().hasContainerItem(input)) {
                        if (!shiftContainers) {
                            continue;
                        }
                        ItemStack container = input.getItem().getContainerItem(input);
                        if (!container.isEmpty()) {
                            container.setCount(count);
                            outputs.add(new MultiSlotOutput(container, getOutputSlots()));
                        }
                    }
                    input.shrink(count);
                    count = 0;
                    continue;
                }
                if (input.getItem().hasContainerItem(input)) {
                    if (!shiftContainers) {
                        continue;
                    }
                    ItemStack container = input.getItem().getContainerItem(input);
                    if (!container.isEmpty()) {
                        container.setCount(input.getCount());
                        outputs.add(new MultiSlotOutput(container, getOutputSlots()));
                    }
                }
                count -= input.getCount();
                input.setCount(0);
            }
        }
        addToInventory();
        if (supportsUpgrades) {
            for (int i = 0; i < upgradeSlots; i++) {
                ItemStack item = inventory.get(i + inventory.size() - upgradeSlots);
                if (item.getItem() instanceof IMachineUpgradeItem) {
                    ((IMachineUpgradeItem) item.getItem()).onProcessFinished(item, this);
                }
            }
        }
        shouldCheckRecipe = true;
    }

    @Override
    public MultiRecipe getRecipe() {
        if (lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            return null;
        }
        // Check if previous recipe is valid
        List<ItemStack> inputs = getInputs();
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, StackUtil.copyList(inputs)) ? lastRecipe : null;
            if (lastRecipe == null) {
                progress = 0;
            }
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<MultiRecipe>() {

                @Override
                public boolean test(MultiRecipe t) {
                    return checkRecipe(t, StackUtil.copyList(inputs));
                }
            });
        }
        // If no recipe is found, return
        if (lastRecipe == null) {
            return null;
        }
        applyRecipeEffect(lastRecipe.getOutputs());
        if (outputTank.getFluidAmount() == 0) {
            return lastRecipe;
        }
        if (lastRecipe.getOutputs() instanceof GTFluidMachineOutput){
            GTFluidMachineOutput output = (GTFluidMachineOutput)lastRecipe.getOutputs();
            int fluidListSize = output.getFluids().size();
            if (fluidListSize == 1){
                for (FluidStack fluid : output.getFluids()){
                    if ((fluid.isFluidEqual(outputTank.getFluid()) && outputTank.getFluidAmount() + fluid.amount <= outputTank.getCapacity()) || outputTank.getFluidAmount() == 0){
                        return lastRecipe;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean canContinue() {
        return heat >= getRequiredHeat(lastRecipe.getOutputs());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.outputTank.readFromNBT(nbt.getCompoundTag("outputTank"));
        maxHeat = nbt.getInteger("maxHeat");
        heat = nbt.getInteger("heat");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.outputTank.writeToNBT(this.getTag(nbt, "outputTank"));
        nbt.setInteger("maxHeat", maxHeat);
        nbt.setInteger("heat", heat);
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
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing!= null) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing!= null){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.outputTank);
        }
        return super.getCapability(capability, facing);
    }

    public static void init(){
        coilsSlotWhitelist.put(GTMaterialGen.get(GTCXItems.constantanHeatingCoil), 250);
        coilsSlotWhitelist.put(GTMaterialGen.get(GTCXItems.kanthalHeatingCoil), 500);
        coilsSlotWhitelist.put(GTMaterialGen.get(GTCXItems.nichromeHeatingCoil), 750);
        if (Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS) && GTConfig.modcompat.compatIc2Extras){
            addRecipe("blockRefinedIron", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.RefinedIron), 115200, GTMaterialGen.getFluidStack(GTCXMaterial.RefinedIron, 1296));
            addRecipe("casingCopper", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.Copper), 64000, GTMaterialGen.getFluidStack(GTCXMaterial.Copper, 72));
            addRecipe("casingTin", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.Tin), 64000, GTMaterialGen.getFluidStack(GTCXMaterial.Tin, 72));
            addRecipe("casingSilver", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.Silver), 64000, GTMaterialGen.getFluidStack(GTCXMaterial.Silver, 72));
            addRecipe("casingLead", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.Lead), 64000, GTMaterialGen.getFluidStack(GTCXMaterial.Lead, 72));
            addRecipe("casingIron", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.Iron), 64000, GTMaterialGen.getFluidStack(GTCXMaterial.Iron, 72));
            addRecipe("casingGold", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.Gold), 64000, GTMaterialGen.getFluidStack(GTCXMaterial.Gold, 72));
            addRecipe("casingRefinedIron", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.RefinedIron), 64000, GTMaterialGen.getFluidStack(GTCXMaterial.RefinedIron, 72));
            addRecipe("casingSteel", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.Steel), 64000, GTMaterialGen.getFluidStack(GTCXMaterial.Steel, 72));
            addRecipe("casingBronze", 1, GTCXMaterialGen.getMaterialHeatValue(GTCXMaterial.Bronze), 64000, GTMaterialGen.getFluidStack(GTCXMaterial.Bronze, 72));
        }
    }

    public static int getRequiredHeat(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            return 1;
        }
        return output.getMetadata().getInteger(neededHeat);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int total) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((total / defaultEu) - 100) };
    }

    public static void addRecipe(ItemStack input, int heat, int totalEu, FluidStack output) {
        addRecipe(new RecipeInputItemStack(input), heat, totalEu, output);
    }

    public static void addRecipe(String input, int amount, int heat, int totalEu, FluidStack output) {
        addRecipe(new RecipeInputOreDict(input, amount), heat, totalEu, output);
    }

    public static void addRecipe(IRecipeInput input, int heat, int totalEu, FluidStack output, String recipeID) {
        if (heat > 5250){
            GTCExpansion.logger.info("Max recipe heat cannot be more then 5000!");
            return;
        }
        List<IRecipeInput> inlist = new ArrayList<>();
        List<FluidStack> outlist = new ArrayList<>();
        RecipeModifierHelpers.IRecipeModifier[] modifiers = totalEu(totalEu);
        inlist.add(input);
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        mods.setInteger(neededHeat, heat);
        outlist.add(output);
        addRecipe(inlist, new GTFluidMachineOutput(mods, outlist), recipeID);
    }

    public static void addRecipe(IRecipeInput input, int heat, int totalEu, FluidStack output) {
        addRecipe(input, heat, totalEu, output, output.getUnlocalizedName());
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeId) {
        GTCXRecipeLists.FLUID_SMELTER_RECIPE_LIST.addRecipe(input, output, recipeId, defaultEu);
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
        return GTHelperFluid.doClickableFluidContainerFillThings(entityPlayer, enumHand, world, pos, outputTank);
    }

    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }
}
