package gtc_expansion.tile.multi;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerIndustrialGrinder;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.recipes.GTCXRecipeProcessing;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.multi.GTTileMultiBaseMachine;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.item.IMachineUpgradeItem.UpgradeType;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
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
import ic2.core.item.recipe.entry.RecipeInputCombined;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class GTCXTileMultiIndustrialGrinder extends GTTileMultiBaseMachine implements ITankListener, IClickable, IGTDebuggableTile {
    protected static final int slotDisplayIn = 0;
    protected static final int[] slotInputs = { 1 };
    protected static final int[] slotOutputs = { 2, 3, 4, 5, 6, 7 };
    protected static final int slotFuel = 8;
    public static final List<Fluid> validFluids = new ArrayList<>();
    public IFilter filter = new MachineFilter(this);
    public static final IBlockState casingStandardState = GTCXBlocks.casingStandard.getDefaultState();
    public static final IBlockState casingReinforcedState = GTCXBlocks.casingReinforced.getDefaultState();
    public static final IBlockState waterState = Blocks.WATER.getDefaultState();
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialgrinder.png");
    private static final int defaultEu = 120;
    @NetworkField(index = 13)
    private final IC2Tank inputTank = new IC2Tank(16000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && validFluids.contains(fluid.getFluid());
        }
    };

    public GTCXTileMultiIndustrialGrinder() {
        super(9, 2, defaultEu, 128);
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
        return GTCXLang.INDUSTRIAL_GRINDER;
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
        return GTCXRecipeLists.INDUSTRIAL_GRINDER_RECIPE_LIST;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXIndustrialGrinderGui.class;
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
        return new LinkedHashSet<>(Arrays.asList(UpgradeType.ImportExport, UpgradeType.RedstoneControl, UpgradeType.Sounds, UpgradeType.MachineModifierA, UpgradeType.MachineModifierB));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerIndustrialGrinder(entityPlayer.inventory, this);
    }

    @Override
    public void process(MultiRecipe recipe) {
        MachineOutput output = recipe.getOutputs().copy();
        for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
            outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
            onRecipeComplete();
        }
        NBTTagCompound nbt = recipe.getOutputs().getMetadata();
        boolean shiftContainers = nbt != null && nbt.getBoolean(MOVE_CONTAINER_TAG);
        boolean fluidExtracted = false;
        List<ItemStack> inputs = getInputs();
        for (IRecipeInput key : recipe.getInputs()) {
            int count = key.getAmount();
            if (key instanceof RecipeInputFluid && !fluidExtracted){
                inputTank.drainInternal(((RecipeInputFluid)key).fluid, true);
                fluidExtracted = true;
                continue;
            }
            ItemStack input = inventory.get(1);
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
        int empty = 0;
        int[] outputSlots = getOutputSlots();
        for (int slot : outputSlots) {
            if (getStackInSlot(slot).isEmpty()) {
                empty++;
            }
        }
        if (empty == outputSlots.length) {
            return lastRecipe;
        }
        for (ItemStack output : lastRecipe.getOutputs().getAllOutputs()) {
            for (int outputSlot : outputSlots) {
                if (inventory.get(outputSlot).isEmpty()) {
                    return lastRecipe;
                }
                if (StackUtil.isStackEqual(inventory.get(outputSlot), output, false, true)) {
                    if (inventory.get(outputSlot).getCount()
                            + output.getCount() <= inventory.get(outputSlot).getMaxStackSize()) {
                        return lastRecipe;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean checkRecipe(GTRecipeMultiInputList.MultiRecipe entry, List<ItemStack> inputs) {
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(entry.getInputs());
        int index = 0;
        FluidStack fluidInput = inputTank.getFluid();
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
            int toFind = key.getAmount();
            if (key instanceof RecipeInputFluid){
                FluidStack fluidStack = ((RecipeInputFluid)key).fluid;
                if (fluidInput != null && fluidInput.isFluidEqual(fluidStack) && fluidInput.amount >= fluidStack.amount){
                    keyIter.remove();
                    continue;
                }
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
        addWaterRecipe("oreUranium", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Uranium, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Thorium, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 1));
        addWaterRecipe("oreSilver", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Silver, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 2));
        addWaterRecipe("oreIron", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Iron, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Tin, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
        addWaterRecipe("oreGold", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Gold, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Copper, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
        addWaterRecipe("oreCopper", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Copper, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Gold, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
        addWaterRecipe("oreTin", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Tin, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 1));
        addWaterRecipe("oreLead", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Lead, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 2));
        addWaterRecipe("oreCassiterite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Cassiterite, 4), GTCXMaterialGen.getSmallDust(GTCXMaterial.Tin, 2));
        addWaterRecipe("oreTetrahedrite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Tetrahedrite, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Antimony, 1));
        addWaterRecipe("oreGalena", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Galena, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Sulfur, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 1));
        addWaterRecipe("oreIridium", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Iridium, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Platinum, 2));
        addWaterRecipe("oreBauxite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Bauxite, 4), GTMaterialGen.getDust(GTMaterial.Aluminium, 1));
        addWaterRecipe("orePyrite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Pyrite, 4), GTMaterialGen.getDust(GTMaterial.Sulfur, 2));
        addWaterRecipe("oreCinnabar", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Cinnabar, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Redstone, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Glowstone, 1));
        addWaterRecipe("oreSphalerite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Sphalerite, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.GarnetYellow, 1));
        addWaterRecipe("oreTungsten", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Tungsten, 4), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Manganese, 3));
        addWaterRecipe("oreSheldonite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Platinum, 2), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Iridium, 2));
        addWaterRecipe("oreChromite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Chromite, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 1));
        addMercuryRecipe("oreIridium", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Iridium, 2), GTMaterialGen.getDust(GTMaterial.Platinum, 1));
        addMercuryRecipe("oreSheldonite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Platinum, 3), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTCXMaterialGen.getTinyDust(GTMaterial.Iridium, 2));
        addMercuryRecipe("oreGalena", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Galena, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Sulfur, 1), Ic2Items.silverDust);
        addMercuryRecipe("oreSilver", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Silver, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 2));
        addMercuryRecipe("oreGold", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Gold, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Copper, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
        addMercuryRecipe("oreCopper", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Copper, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1) , Ic2Items.goldDust);
        addMercuryRecipe("oreLead", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Lead, 2), Ic2Items.silverDust);
        addMercuryRecipe("oreUranium", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Uranium, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Thorium, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 2));
        addRecipe("oreBauxite", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid), totalEu(12720),GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Bauxite, 4), GTMaterialGen.getDust(GTMaterial.Aluminium, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Titanium, 1));
        addSodiumPersulfateRecipe("oreIron", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Iron, 2), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Tin, 1));
        addSodiumPersulfateRecipe("oreSphalerite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Sphalerite, 2), GTMaterialGen.getDust(GTCXMaterial.Zinc, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.GarnetYellow, 1));
        addSodiumPersulfateRecipe("oreTetrahedrite", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Tetrahedrite, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Antimony, 1));
        addSodiumPersulfateRecipe("oreTin", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Tin, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 1), GTMaterialGen.getDust(GTCXMaterial.Zinc, 1));
        addSodiumPersulfateRecipe("oreCopper", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Copper, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Gold, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
        addSodiumPersulfateRecipe("oreGold", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTCXMaterial.Gold, 2), Ic2Items.copperDust, GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
        addSodiumPersulfateRecipe("oreUranium", 1, totalEu(12800), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Uranium, 3), GTCXMaterialGen.getSmallDust(GTMaterial.Thorium, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 1));
        if (OreDictionary.doesOreNameExist("oreNickel")) {
            addWaterRecipe("oreNickel", 1, totalEu(12000), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Nickel, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Platinum, 1));
            addMercuryRecipe("oreNickel", 1, totalEu(12000), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Nickel, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 1), GTMaterialGen.getDust(GTMaterial.Platinum, 1));
            addSodiumPersulfateRecipe("oreNickel", 1, totalEu(12000), GTCXRecipeProcessing.getPurifiedOrDust(GTMaterial.Nickel, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Platinum, 1));
        }
        IRecipeInput hardenedClay = new RecipeInputCombined(1, new RecipeInputItemStack(GTMaterialGen.get(Blocks.HARDENED_CLAY, 1, OreDictionary.WILDCARD_VALUE)), new RecipeInputItemStack(GTMaterialGen.get(Blocks.STAINED_HARDENED_CLAY, 1, OreDictionary.WILDCARD_VALUE)));
        addRecipe(hardenedClay, GTMaterialGen.getFluidStack("water", 1000),12800, GTMaterialGen.getIc2(Ic2Items.clayDust, 2));
        addWaterRecipe(GTMaterialGen.get(Blocks.NETHERRACK, 16), totalEu(204800), GTMaterialGen.get(Items.GOLD_NUGGET), GTMaterialGen.getIc2(Ic2Items.netherrackDust, 16));
        addWaterRecipe("oreRuby", 1, totalEu(12800), GTMaterialGen.getGem(GTMaterial.Ruby, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.GarnetRed, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Chrome, 2));
        addRecipe("oreRuby", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid), totalEu(12800), GTMaterialGen.getGem(GTMaterial.Ruby, 3), GTCXMaterialGen.getSmallDust(GTMaterial.Chrome, 1));
        addWaterRecipe("oreSapphire", 1, totalEu(12800), GTMaterialGen.getGem(GTMaterial.Sapphire, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Sapphire, 6), GTCXMaterialGen.getSmallDust(GTMaterial.Aluminium, 2));
        addRecipe("oreSapphire", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid), totalEu(12800), GTMaterialGen.getGem(GTMaterial.Sapphire, 3), GTCXMaterialGen.getSmallDust(GTMaterial.Aluminium, 1));
        addWaterRecipe("oreCoal", 1, totalEu(12800), GTMaterialGen.get(Items.COAL), Ic2Items.coalDust, GTCXMaterialGen.getSmallDust(GTMaterial.Thorium, 1));
        addWaterRecipe("oreLapis", 1, totalEu(12800), new ItemStack(Items.DYE, 12, 4), GTMaterialGen.getDust(GTMaterial.Lazurite, 3));
        addWaterRecipe("oreRedstone", 1, totalEu(12800), GTMaterialGen.get(Items.REDSTONE, 10), GTCXMaterialGen.getSmallDust(GTCXMaterial.Glowstone, 2));
        addWaterRecipe("oreDiamond", 1, totalEu(12800), GTMaterialGen.get(Items.DIAMOND), GTCXMaterialGen.getSmallDust(GTCXMaterial.Diamond, 6), Ic2Items.hydratedCoalDust);
        addRecipe("oreDiamond", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid), totalEu(12800), GTMaterialGen.get(Items.DIAMOND, 3));
        addWaterRecipe("oreEmerald", 1, totalEu(12800), GTMaterialGen.get(Items.EMERALD), GTCXMaterialGen.getSmallDust(GTMaterial.Emerald, 6), GTCXMaterialGen.getSmallDust(GTMaterial.Aluminium, 2));
        addRecipe("oreEmerald", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid), totalEu(12800), GTMaterialGen.get(Items.EMERALD, 3), GTCXMaterialGen.getSmallDust(GTMaterial.Aluminium, 1));
        addWaterRecipe("oreOlivine", 1, totalEu(12800), GTMaterialGen.getGem(GTCXMaterial.Olivine, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Olivine, 6), GTCXMaterialGen.getSmallDust(GTCXMaterial.Pyrope, 2));
        addRecipe("oreOlivine", 1, GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid), totalEu(12800), GTMaterialGen.getGem(GTCXMaterial.Olivine, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Pyrope, 1));
        addWaterRecipe("oreQuartz", 1, totalEu(12800), GTMaterialGen.get(Items.QUARTZ, 4), GTCXMaterialGen.getSmallDust(GTCXMaterial.Netherrack, 2));
        addWaterRecipe("oreSodalite", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial.Sodalite, 12), GTMaterialGen.getDust(GTMaterial.Aluminium, 3));
        addMercuryRecipe(GTMaterialGen.get(Blocks.NETHERRACK, 8), totalEu(102400), GTMaterialGen.get(Items.GOLD_NUGGET), GTMaterialGen.getIc2(Ic2Items.netherrackDust, 8));
    }

    public static void addWaterRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                      ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputFluid(new FluidStack(FluidRegistry.WATER, 1000)) }, modifiers, outlist);
    }

    public static void addWaterRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputFluid(new FluidStack(FluidRegistry.WATER, 1000)) }, modifiers, outlist);
    }

    public static void addSodiumPersulfateRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                      ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputFluid(GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate)) }, modifiers, outlist);
    }

    public static void addRecipe(String input, int amount, FluidStack fluid, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {

        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputFluid(fluid) }, modifiers, outlist);
    }

    public static void addRecipe(ItemStack input, FluidStack fluid, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {

        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputFluid(fluid) }, modifiers, outlist);
    }

    public static void addMercuryRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputFluid(GTMaterialGen.getFluidStack(GTMaterial.Mercury)) }, modifiers, outlist);
    }

    public static void addMercuryRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputFluid(GTMaterialGen.getFluidStack(GTMaterial.Mercury)) }, modifiers, outlist);
    }

    public static void addRecipe(IRecipeInput input, FluidStack fluid, int totalEu,
                                 ItemStack... outputs) {
        addRecipe(input, fluid, totalEu, outputs[0].getUnlocalizedName(), outputs);
    }

    public static void addRecipe(IRecipeInput input, FluidStack fluid, int totalEu, String recipeID,
                                        ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { input, new RecipeInputFluid(fluid) }, totalEu(totalEu), outlist, recipeID);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, List<ItemStack> outputs) {
        addRecipe(inputs, modifiers, outputs, outputs.get(0).getUnlocalizedName());
    }

    static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, List<ItemStack> outputs, String recipeId) {
        List<IRecipeInput> inlist = new ArrayList<>();
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }

        addRecipe(inlist, new MachineOutput(mods, outputs), recipeId);
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeID) {
        for (IRecipeInput in : input){
            if (in instanceof RecipeInputFluid && !validFluids.contains(((RecipeInputFluid)in).fluid.getFluid())){
                validFluids.add(((RecipeInputFluid)in).fluid.getFluid());
            }
        }
        GTCXRecipeLists.INDUSTRIAL_GRINDER_RECIPE_LIST.addRecipe(input, output, recipeID, defaultEu);
    }

    /*@Override
    public Map<BlockPos, IBlockState> provideStructure() {
        Map<BlockPos, IBlockState> states = super.provideStructure();
        int3 dir = new int3(getPos(), getFacing());
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        states.put(dir.left(1).asBlockPos(), casingStandardState);
        states.put(dir.down(1).asBlockPos(), casingReinforcedState);
        states.put(dir.down(1).asBlockPos(), casingStandardState);
        states.put(dir.back(1).asBlockPos(), casingStandardState);
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        states.put(dir.right(1).asBlockPos(), casingStandardState);
        states.put(dir.down(1).asBlockPos(), waterState);
        states.put(dir.down(1).asBlockPos(), casingStandardState);
        states.put(dir.right(1).asBlockPos(), casingStandardState);
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        states.put(dir.back(1).asBlockPos(), casingStandardState);
        states.put(dir.down(1).asBlockPos(), casingReinforcedState);
        states.put(dir.down(1).asBlockPos(), casingStandardState);
        states.put(dir.left(1).asBlockPos(), casingStandardState);
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        states.put(dir.left(1).asBlockPos(), casingStandardState);
        states.put(dir.down(1).asBlockPos(), casingReinforcedState);
        states.put(dir.down(1).asBlockPos(), casingStandardState);
        states.put(dir.forward(2).right(2).asBlockPos(), casingStandardState);
        states.put(dir.up(1).asBlockPos(), casingReinforcedState);
        states.put(dir.up(1).asBlockPos(), casingStandardState);
        return states;
    }*/

    @Override
    public boolean checkStructure() {
        if (!world.isAreaLoaded(pos, 3)) {
            return false;
        }
        // we doing it "big math" style not block by block
        int3 dir = new int3(getPos(), getFacing());
        if (!isReinforcedCasing(dir.back(1))){
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!(isReinforcedCasing(dir.down(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.down(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.back(1))){
            return false;
        }
        if (!(isReinforcedCasing(dir.up(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.right(1))) {// right
            return false;
        }
        if (world.getBlockState(dir.down(1).asBlockPos()) != waterState) {
            return false;
        }
        if (!(isStandardCasing(dir.down(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.right(1))) {// right
            return false;
        }
        if (!(isReinforcedCasing(dir.up(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.back(1))) {// back
            return false;
        }
        if (!(isReinforcedCasing(dir.down(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.down(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!(isReinforcedCasing(dir.up(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.left(1))) {// left
            return false;
        }
        if (!(isReinforcedCasing(dir.down(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.down(1)))) {
            return false;
        }
        if (!isStandardCasing(dir.forward(2).right(1))) {//block underneath block behind controller
            return false;
        }
        if (!isStandardCasing(dir.right(1))) {
            return false;
        }
        if (!(isReinforcedCasing(dir.up(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.up(1)))) {
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
