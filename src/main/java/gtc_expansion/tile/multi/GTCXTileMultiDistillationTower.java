package gtc_expansion.tile.multi;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerDistillationTower;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTFluidMachineOutput;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.multi.GTTileMultiBaseMachine;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.item.IMachineUpgradeItem.UpgradeType;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.fluid.IC2Tank;
import ic2.core.fluid.LayeredFluidTank;
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
import ic2.core.inventory.transport.wrapper.RangedInventoryWrapper;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GTCXTileMultiDistillationTower extends GTTileMultiBaseMachine implements ITankListener, IClickable, IGTDebuggableTile, IFluidHandler {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/distillationtower.png");
    public static final List<Fluid> validFluids = new ArrayList<>();
    public static final IBlockState standardCasingState = GTCXBlocks.casingStandard.getDefaultState();
    public static final IBlockState advancedCasingState = GTCXBlocks.casingAdvanced.getDefaultState();
    public static final IBlockState airState = Blocks.AIR.getDefaultState();
    public static final int slotDisplayIn = 0;
    public static final int slotFuel = 1;
    public static final int slotDisplayOut1 = 2;
    public static final int slotDisplayOut2 = 3;
    public static final int slotDisplayOut3 = 4;
    public static final int slotDisplayOut4 = 5;
    public static final int slotDisplayOut5 = 6;
    public static final int slotDisplayOut6 = 7;
    public static final int[] slotOutputs = { 8, 9 };
    private static final int defaultEu = 64;
    @NetworkField(index = 13)
    private final IC2Tank inputTank = new IC2Tank(16000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && validFluids.contains(fluid.getFluid());
        }
    };
    @NetworkField(index = 14)
    private final IC2Tank outputTank1 = new IC2Tank(16000);
    @NetworkField(index = 15)
    private final IC2Tank outputTank2 = new IC2Tank(16000);
    @NetworkField(index = 16)
    private final IC2Tank outputTank3 = new IC2Tank(16000);
    @NetworkField(index = 17)
    private final IC2Tank outputTank4 = new IC2Tank(16000);
    @NetworkField(index = 18)
    private final IC2Tank outputTank5 = new IC2Tank(16000);
    @NetworkField(index = 19)
    private final IC2Tank outputTank6 = new IC2Tank(16000);
    @NetworkField(index = 20)
    private final LayeredFluidTank outputTank = new LayeredFluidTank(96000);

    public GTCXTileMultiDistillationTower() {
        super(10, 2, defaultEu, 128);
        setFuelSlot(slotFuel);
        maxEnergy = 10000;
        this.inputTank.addListener(this);
        this.outputTank1.addListener(this);
        this.outputTank2.addListener(this);
        this.outputTank3.addListener(this);
        this.outputTank4.addListener(this);
        this.outputTank5.addListener(this);
        this.outputTank6.addListener(this);
        this.outputTank.addListener(this);
        this.addGuiFields("inputTank", "outputTank1", "outputTank2", "outputTank3", "outputTank4", "outputTank5", "outputTank6", "outputTank");
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutputs);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Output, slotOutputs);
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.DISTILLATION_TOWER;
    }

    @Override
    public void onTankChanged(IFluidTank tank) {
        this.inventory.set(slotDisplayIn, ItemDisplayIcon.createWithFluidStack(this.inputTank.getFluid()));
        this.getNetwork().updateTileGuiField(this, "inputTank");
        for (int i = 0; i < 6; i++) {
            if (this.getOutputTank(i + 1).getFluidAmount() > 0){
                this.setStackInSlot(2 + i, ItemDisplayIcon.createWithFluidStack(this.getOutputTank(i + 1).getFluid()));
                this.getNetwork().updateTileGuiField(this, "outputTank" + (i + 1));
            } else if (i < outputTank.getTankProperties().length) {
                this.setStackInSlot( 2 + i, ItemDisplayIcon.createWithFluidStack(this.outputTank.getTankProperties()[i].getContents()));
            } else {
                this.setStackInSlot(2 + i, ItemStack.EMPTY);
            }
        }
        this.getNetwork().updateTileGuiField(this, "outputTank");
        shouldCheckRecipe = true;
    }

    private IC2Tank getOutputTank(int index){
        switch (index){
            case 2: return outputTank2;
            case 3: return outputTank3;
            case 4: return outputTank4;
            case 5: return outputTank5;
            case 6: return outputTank6;
            default: return outputTank1;
        }
    }

    @Override
    public Set<UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(UpgradeType.ImportExport, UpgradeType.RedstoneControl, UpgradeType.Sounds, UpgradeType.MachineModifierA, UpgradeType.MachineModifierB));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerDistillationTower(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXDistillationTowerGui.class;
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{};
    }

    @Override
    public IFilter[] getInputFilters(int[] slots) {
        return new IFilter[] { new MachineFilter(this) };
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
        return GTCXRecipeLists.DISTILLATION_TOWER_RECIPE_LIST;
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
        return GTCExpansion.getAprilFirstSound(Ic2Sounds.electrolyzerOp);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.inputTank.readFromNBT(nbt.getCompoundTag("inputTank"));
        this.outputTank1.readFromNBT(nbt.getCompoundTag("outputTank1"));
        this.outputTank2.readFromNBT(nbt.getCompoundTag("outputTank2"));
        this.outputTank3.readFromNBT(nbt.getCompoundTag("outputTank3"));
        this.outputTank4.readFromNBT(nbt.getCompoundTag("outputTank4"));
        this.outputTank5.readFromNBT(nbt.getCompoundTag("outputTank5"));
        this.outputTank6.readFromNBT(nbt.getCompoundTag("outputTank6"));
        this.outputTank.readFromNBT(nbt.getCompoundTag("outputTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.inputTank.writeToNBT(this.getTag(nbt, "inputTank"));
        this.outputTank1.writeToNBT(this.getTag(nbt, "outputTank1"));
        this.outputTank2.writeToNBT(this.getTag(nbt, "outputTank2"));
        this.outputTank3.writeToNBT(this.getTag(nbt, "outputTank3"));
        this.outputTank4.writeToNBT(this.getTag(nbt, "outputTank4"));
        this.outputTank5.writeToNBT(this.getTag(nbt, "outputTank5"));
        this.outputTank6.writeToNBT(this.getTag(nbt, "outputTank6"));
        this.outputTank.writeToNBT(this.getTag(nbt, "outputTank"));
        return nbt;
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
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void process(GTRecipeMultiInputList.MultiRecipe recipe) {
        GTFluidMachineOutput output;
        IRecipeInput input = recipe.getInput(0);
        if (recipe.getOutputs() instanceof GTFluidMachineOutput && input instanceof RecipeInputFluid){
            output = (GTFluidMachineOutput) recipe.getOutputs();
            for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
                if (!(stack.getItem() instanceof ItemDisplayIcon)){
                    outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
                }
            }
            inputTank.drain(((RecipeInputFluid) input).fluid, true);
            NBTTagCompound nbt = recipe.getOutputs().getMetadata();
            for (FluidStack fluid : output.getFluids()){
                outputTank.fill(fluid, true);
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
    public GTRecipeMultiInputList.MultiRecipe getRecipe() {
        if (lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            return null;
        }
        // Check if previous recipe is valid
        FluidStack input = inputTank.getFluid();
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, input) ? lastRecipe : null;
            if (lastRecipe == null) {
                progress = 0;
            }
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<GTRecipeMultiInputList.MultiRecipe>() {

                @Override
                public boolean test(GTRecipeMultiInputList.MultiRecipe t) {
                    return checkRecipe(t, input);
                }
            });
        }
        // If no recipe is found, return
        if (lastRecipe == null) {
            return null;
        }
        if (!(lastRecipe.getOutputs() instanceof GTFluidMachineOutput)){
            return null;
        }
        GTFluidMachineOutput output = (GTFluidMachineOutput) lastRecipe.getOutputs();
        applyRecipeEffect(output);
        int empty = 0;
        int[] outputSlots = getOutputSlots();
        for (int slot : outputSlots) {
            if (getStackInSlot(slot).isEmpty()) {
                empty++;
            }
        }
        if (empty == outputSlots.length && outputTank.getFluidAmount() == 0) {
            return lastRecipe;
        }
        int totalAmount = 0;
        for (FluidStack fluid : output.getFluids()){
            totalAmount += fluid.amount;
        }
        if (outputTank.getFluidAmount() + totalAmount <= outputTank.getCapacity()){
            for (ItemStack outputItem : lastRecipe.getOutputs().getAllOutputs()) {
                if (!(outputItem.getItem() instanceof ItemDisplayIcon)){
                    for (int outputSlot : outputSlots) {
                        if (inventory.get(outputSlot).isEmpty()) {
                            return lastRecipe;
                        }
                        if (StackUtil.isStackEqual(inventory.get(outputSlot), outputItem, false, true)) {
                            if (inventory.get(outputSlot).getCount()
                                    + outputItem.getCount() <= inventory.get(outputSlot).getMaxStackSize()) {
                                return lastRecipe;
                            }
                        }
                    }
                } else {
                    return lastRecipe;
                }
            }
        }
        return null;
    }

    public boolean checkRecipe(GTRecipeMultiInputList.MultiRecipe entry, FluidStack input) {
        IRecipeInput recipeInput = entry.getInput(0);
        if (recipeInput instanceof RecipeInputFluid){
            return input != null && input.containsFluid(((RecipeInputFluid)recipeInput).fluid);
        }
        return false;
    }

    @Override
    public IHasInventory getInputInventory() {
        int[] input = getInputSlots();
        RangedInventoryWrapper result = new RangedInventoryWrapper(this, input).addFilters(getInputFilters(input));
        return result;
    }

    public static void init(){
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.OilCrude, 8000), 256000, GTMaterialGen.getFluidStack(GTCXMaterial.Diesel, 4000), GTMaterialGen.getFluidStack(GTCXMaterial.Glyceryl, 500), GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid, 4000), GTMaterialGen.getFluidStack(GTCXMaterial.Naphtha, 4000), GTMaterialGen.getFluidStack(GTMaterial.Lubricant, 2000));
        addRecipe(GTMaterialGen.getFluidStack(GTMaterial.Oil, 8000), 256000, GTMaterialGen.getFluidStack(GTCXMaterial.Diesel, 4000), GTMaterialGen.getFluidStack(GTCXMaterial.Glyceryl, 500), GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid, 4000), GTMaterialGen.getFluidStack(GTCXMaterial.Naphtha, 4000), GTMaterialGen.getFluidStack(GTMaterial.Lubricant, 2000));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.Naphtha, 4000), 64000, new FluidStack[]{GTMaterialGen.getFluidStack(GTCXMaterial.Gasoline, 4000), GTMaterialGen.getFluidStack(GTCXMaterial.Propane, 4000), GTMaterialGen.getFluidStack(GTMaterial.Methane,3500), GTMaterialGen.getFluidStack(GTMaterial.Fuel, 4000)}, GTMaterialGen.getDust(GTMaterial.Carbon, 1));
        if (Loader.isModLoaded("forestry")){
            addRecipe(GTMaterialGen.getFluidStack("biomass", 3000), 32000, new FluidStack[]{GTMaterialGen.getFluidStack("bio.ethanol", 1000), GTMaterialGen.getFluidStack("water", 1000)}, Ic2Items.fertilizer.copy());
        }
    }

    public static void addRecipe(FluidStack input, int totalEu, FluidStack[] outputFluid, ItemStack... outputItem){
        addRecipe(input, totalEu, outputFluid[0].getUnlocalizedName(), outputFluid, outputItem);
    }

    public static void addRecipe(FluidStack input, int totalEu, FluidStack... outputFluid){
        addRecipe(input, totalEu, outputFluid[0].getUnlocalizedName(), outputFluid);
    }

    public static void addRecipe(FluidStack input, int totalEu, String recipeId, FluidStack[] outputFluid, ItemStack... outputItem){
        if (outputFluid.length > 6){
            GTCExpansion.logger.info("There can only be up to 6 fluid outputs");
            return;
        }
        validFluids.add(input.getFluid());
        List<ItemStack> outListItem = new ArrayList<>();
        for (ItemStack item : outputItem){
            outListItem.add(item);
        }
        List<FluidStack> outListFluid = new ArrayList<>();
        for (FluidStack fluid : outputFluid){
            outListFluid.add(fluid);
        }

        addRecipe(new IRecipeInput[]{new RecipeInputFluid(input)}, totalEu(totalEu), outListFluid, outListItem, recipeId);
    }

    public static void addRecipe(FluidStack input, int totalEu, String recipeId, FluidStack... outputFluid){
        if (outputFluid.length > 6){
            GTCExpansion.logger.info("There can only be up to 6 fluid outputs");
            return;
        }
        validFluids.add(input.getFluid());
        List<ItemStack> outListItem = new ArrayList<>();
        outListItem.add(ItemDisplayIcon.createWithFluidStack(new FluidStack(FluidRegistry.WATER, 1000)));
        List<FluidStack> outListFluid = new ArrayList<>();
        for (FluidStack fluid : outputFluid){
            outListFluid.add(fluid);
        }

        addRecipe(new IRecipeInput[]{new RecipeInputFluid(input)}, totalEu(totalEu), outListFluid, outListItem, recipeId);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    private static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, List<FluidStack> fluidOutputs, List<ItemStack> outputs, String recipeID) {
        List<IRecipeInput> inlist = new ArrayList<>();
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }

        addRecipe(inlist, new GTFluidMachineOutput(mods, outputs, fluidOutputs), recipeID);
    }

    private static void addRecipe(List<IRecipeInput> input, GTFluidMachineOutput output, String recipeId) {
        GTCXRecipeLists.DISTILLATION_TOWER_RECIPE_LIST.addRecipe(input, output, recipeId, defaultEu);
    }

    /*@Override
    public Map<BlockPos, IBlockState> provideStructure() {
        Map<BlockPos, IBlockState> states = super.provideStructure();
        int3 dir = new int3(getPos(), getFacing());
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);

        states.put(dir.left(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        BlockPos down = dir.down(1).asBlockPos();
        if (world.getBlockState(down) != GTBlocks.tileBufferFluid.getDefaultState()){
            states.put(down, standardCasingState);
        }

        states.put(dir.back(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);

        states.put(dir.right(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), airState);
        states.put(dir.down(1).asBlockPos(), airState);
        states.put(dir.down(1).asBlockPos(), airState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);

        states.put(dir.right(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);

        states.put(dir.back(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);

        states.put(dir.left(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);

        states.put(dir.left(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);
        states.put(dir.down(1).asBlockPos(), advancedCasingState);
        states.put(dir.down(1).asBlockPos(), standardCasingState);

        states.put(dir.forward(2).right(2).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        states.put(dir.up(1).asBlockPos(), advancedCasingState);
        states.put(dir.up(1).asBlockPos(), standardCasingState);
        return states;
    }*/

    @Override
    public boolean checkStructure() {
        if (!world.isAreaLoaded(pos, 3)) {
            return false;
        }
        // we doing it "big math" style not block by block
        int3 dir = new int3(getPos(), getFacing());
        if (!isStandardCasing(dir.back(1))){
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }

        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }

        if (!isStandardCasing(dir.back(1))) {// back
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }

        if (!isStandardCasing(dir.right(1))) {// right
            return false;
        }
        if (!isAir(dir.down(1))) {
            return false;
        }
        if (!isAir(dir.down(1))) {
            return false;
        }
        if (!isAir(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }

        if (!isStandardCasing(dir.right(1))) {// right
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }

        if (!isStandardCasing(dir.back(1))) {// back
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }

        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }

        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.down(1))) {
            return false;
        }
        if (!isStandardCasing(dir.down(1))) {
            return false;
        }

        if (!isStandardCasing(dir.forward(2).right(2))) {// missing front right column
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        if (!isAdvancedCasing(dir.up(1))) {
            return false;
        }
        if (!isStandardCasing(dir.up(1))) {
            return false;
        }
        return true;
    }

    public boolean isStandardCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == standardCasingState;
    }

    public boolean isAdvancedCasing(int3 pos) {
        return world.getBlockState(pos.asBlockPos()) == advancedCasingState;
    }

    public boolean isAir(int3 pos) {
        return world.isAirBlock(pos.asBlockPos());
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
        return GTHelperFluid.doClickableFluidContainerEmptyThings(player, hand, world, pos, inputTank) || GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, outputTank1) || GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, outputTank2) || GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, outputTank3) || GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, outputTank4) || GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, outputTank5) || GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, outputTank6);
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        FluidStack fluid = this.inputTank.getFluid();
        map.put("Input Tank: " + (fluid != null ? fluid.amount + "mb of " + fluid.getLocalizedName() : "Empty"), false);
        for (int i = 0; i < outputTank.getTankProperties().length; i++) {
            fluid = this.outputTank.getTankProperties()[i].getContents();
            map.put("Output Tank Fluid " + (i + 1) + " : " + (fluid != null ? fluid.amount + "mb of " + fluid.getLocalizedName() : "Empty"), false);
        }
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        List<IFluidTankProperties> combined = new ArrayList<>();
        Stream.of(inputTank.getTankProperties(), outputTank.getTankProperties()).flatMap(Stream::of).forEach(combined::add);
        return combined.toArray(new IFluidTankProperties[0]);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return inputTank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (outputTank1.getFluidAmount() > 0){
            return outputTank1.drain(resource, doDrain);
        }
        if (outputTank2.getFluidAmount() > 0){
            return outputTank2.drain(resource, doDrain);
        }
        if (outputTank3.getFluidAmount() > 0){
            return outputTank3.drain(resource, doDrain);
        }
        if (outputTank4.getFluidAmount() > 0){
            return outputTank4.drain(resource, doDrain);
        }
        if (outputTank5.getFluidAmount() > 0){
            return outputTank5.drain(resource, doDrain);
        }
        if (outputTank6.getFluidAmount() > 0){
            return outputTank6.drain(resource, doDrain);
        }
        return outputTank.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (outputTank1.getFluidAmount() > 0){
            return outputTank1.drain(maxDrain, doDrain);
        }
        if (outputTank2.getFluidAmount() > 0){
            return outputTank2.drain(maxDrain, doDrain);
        }
        if (outputTank3.getFluidAmount() > 0){
            return outputTank3.drain(maxDrain, doDrain);
        }
        if (outputTank4.getFluidAmount() > 0){
            return outputTank4.drain(maxDrain, doDrain);
        }
        if (outputTank5.getFluidAmount() > 0){
            return outputTank5.drain(maxDrain, doDrain);
        }
        if (outputTank6.getFluidAmount() > 0){
            return outputTank6.drain(maxDrain, doDrain);
        }
        return outputTank.drain(maxDrain, doDrain);
    }
}
