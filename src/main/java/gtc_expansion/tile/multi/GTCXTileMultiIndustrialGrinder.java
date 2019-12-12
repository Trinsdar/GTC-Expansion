package gtc_expansion.tile.multi;

import gtc_expansion.GTCXBlocks;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GTCXContainerIndustrialGrinder;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.util.GTCXLang;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTHelperMods;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.multi.GTTileMultiBaseMachine;
import gtclassic.common.GTConfig;
import ic2.api.classic.item.IMachineUpgradeItem;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class GTCXTileMultiIndustrialGrinder extends GTTileMultiBaseMachine implements ITankListener, IClickable, IGTDebuggableTile {
    protected static final int slotDisplayIn = 0;
    protected static final int[] slotInputs = { 1 };
    protected static final int[] slotOutputs = { 2, 3, 4, 5, 6, 7 };
    protected static final int slotFuel = 8;
    public IFilter filter = new MachineFilter(this);
    public static final IBlockState casingStandardState = GTCXBlocks.casingStandard.getDefaultState();
    public static final IBlockState casingReinforcedState = GTCXBlocks.casingReinforced.getDefaultState();
    public static final IBlockState waterState = Blocks.WATER.getDefaultState();
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialgrinder.png");
    private static final int defaultEu = 120;
    private IC2Tank inputTank = new IC2Tank(16000);

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
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
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
        boolean shiftContainers = nbt == null ? false : nbt.getBoolean(MOVE_CONTAINER_TAG);
        boolean fluidExtracted = false;
        List<ItemStack> inputs = getInputs();
        for (IRecipeInput key : recipe.getInputs()) {
            int count = key.getAmount();
            if (key instanceof RecipeInputFluid && !fluidExtracted){
                inputTank.drain(((RecipeInputFluid)key).fluid, true);
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
        ItemStack input = inventory.get(1);
        FluidStack fluid = inputTank.getFluid();
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, fluid, input) ? lastRecipe : null;
            if (lastRecipe == null) {
                progress = 0;
            }
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<MultiRecipe>() {

                @Override
                public boolean test(MultiRecipe t) {
                    return checkRecipe(t, fluid, input);
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

    public boolean checkRecipe(MultiRecipe entry, FluidStack input, ItemStack inputItem) {
        IRecipeInput recipeInput = entry.getInput(0);
        IRecipeInput recipeInput1 = entry.getInput(1);
        boolean hasFluid = false;
        boolean hasItem = false;
        if (recipeInput instanceof RecipeInputFluid){
            hasFluid = input != null && input.containsFluid(((RecipeInputFluid)recipeInput).fluid);
        } else {
            hasItem = !inputItem.isEmpty() && recipeInput.matches(inputItem);
        }
        if (recipeInput1 instanceof RecipeInputFluid){
            if (hasFluid) {
                hasFluid = false;
            } else {
                hasFluid = input != null && input.containsFluid(((RecipeInputFluid)recipeInput1).fluid);
            }

        } else {
            if (hasItem){
                hasItem = false;
            } else {
                hasItem = !inputItem.isEmpty() && recipeInput1.matches(inputItem);
            }
        }
        return hasFluid && hasItem;
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
        String modid = GTHelperMods.IC2_EXTRAS;
        if (Loader.isModLoaded(GTHelperMods.IC2_EXTRAS) && GTConfig.modcompat.compatIc2Extras){
            addWaterRecipe("oreUranium", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "uraniumpurifiedcrushedore", 2), GTCXMaterialGen.getSmallDust(GTMaterial.Plutonium, 1));
            addWaterRecipe("oreSilver", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "silverpurifiedcrushedore", 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 2));
            addWaterRecipe("oreIron", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "ironpurifiedcrushedore", 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Tin, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
            addWaterRecipe("oreGold", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "goldpurifiedcrushedore", 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Copper, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
            addWaterRecipe("oreCopper", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "copperpurifiedcrushedore", 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Gold, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
            addWaterRecipe("oreTin", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "tinpurifiedcrushedore", 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 1));
            addWaterRecipe("oreLead", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "leadpurifiedcrushedore", 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 2));
            addWaterRecipe("oreCassiterite", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "tinpurifiedcrushedore", 4), GTCXMaterialGen.getSmallDust(GTMaterial.Carbon, 1));
            addWaterRecipe("oreTetrahedrite", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Tetrahedrite, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Antimony, 1));
            addWaterRecipe("oreGalena", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Galena, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Sulfur, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 1));
            addWaterRecipe("oreIridium", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Iridium, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Platinum, 2));
            addWaterRecipe("oreBauxite", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Bauxite, 4), GTMaterialGen.getDust(GTMaterial.Aluminium, 1));
            addWaterRecipe("orePyrite", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Pyrite, 5), GTMaterialGen.getDust(GTMaterial.Sulfur, 2));
            addWaterRecipe("oreCinnabar", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Cinnabar, 5), GTCXMaterialGen.getSmallDust(GTCXMaterial.Redstone, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Glowstone, 1));
            addWaterRecipe("oreSphalerite", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Sphalerite, 5), GTMaterialGen.getDust(GTCXMaterial.Zinc, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.GarnetYellow, 1));
            addWaterRecipe("oreTungstate", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Tungsten, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Manganese, 3));
            addWaterRecipe("oreSheldonite", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Platinum, 2), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTCXMaterialGen.getNugget(GTMaterial.Iridium, 2));
            addMercuryRecipe("oreIridium", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Iridium, 2), GTMaterialGen.getDust(GTMaterial.Platinum, 1));
            addMercuryRecipe("oreSheldonite", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTMaterial.Platinum, 3), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTCXMaterialGen.getNugget(GTMaterial.Iridium, 2));
            addMercuryRecipe("oreGalena", 1, totalEu(12800), GTCXMaterialGen.getPurifiedCrushedOre(GTCXMaterial.Galena, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Sulfur, 1), Ic2Items.silverDust);
            addMercuryRecipe("oreSilver", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "silverpurifiedcrushedore", 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 2));
            addMercuryRecipe("oreGold", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "goldpurifiedcrushedore", 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Copper, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
            addMercuryRecipe("oreCopper", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "copperpurifiedcrushedore", 2), Ic2Items.goldDust);
            addMercuryRecipe("oreLead", 1, totalEu(12800), GTMaterialGen.getModItem(modid, "leadpurifiedcrushedore", 2), Ic2Items.silverDust);
        } else {
            addWaterRecipe("oreUranium", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial.Uranium, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Plutonium, 1));
            addWaterRecipe("oreSilver", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.silverDust, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 2));
            addWaterRecipe("oreIron", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.ironDust, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Tin, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
            addWaterRecipe("oreGold", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.goldDust, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Copper, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
            addWaterRecipe("oreCopper", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.copperDust, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Gold, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
            addWaterRecipe("oreTin", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.tinDust, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 1));
            addWaterRecipe("oreLead", 1, totalEu(12800), GTMaterialGen.getDust(GTCXMaterial.Lead, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 2));
            addWaterRecipe("oreCassiterite", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.tinDust, 4), GTCXMaterialGen.getSmallDust(GTMaterial.Carbon, 1));
            addWaterRecipe("oreTetrahedrite", 1, totalEu(12800), GTMaterialGen.getDust(GTCXMaterial.Tetrahedrite, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Zinc, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Antimony, 1));
            addWaterRecipe("oreGalena", 1, totalEu(12800), GTMaterialGen.getDust(GTCXMaterial.Galena, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Sulfur, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 1));
            addWaterRecipe("oreIridium", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial.Iridium, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Platinum, 2));
            addWaterRecipe("oreBauxite", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial.Bauxite, 4), GTMaterialGen.getDust(GTMaterial.Aluminium, 1));
            addWaterRecipe("orePyrite", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial.Pyrite, 5), GTMaterialGen.getDust(GTMaterial.Sulfur, 2));
            addWaterRecipe("oreCinnabar", 1, totalEu(12800), GTMaterialGen.getDust(GTCXMaterial.Cinnabar, 5), GTCXMaterialGen.getSmallDust(GTCXMaterial.Redstone, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Glowstone, 1));
            addWaterRecipe("oreSphalerite", 1, totalEu(12800), GTMaterialGen.getDust(GTCXMaterial.Sphalerite, 5), GTMaterialGen.getDust(GTCXMaterial.Zinc, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.GarnetYellow, 1));
            addWaterRecipe("oreTungstate", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial.Tungsten, 2), GTCXMaterialGen.getSmallDust(GTCXMaterial.Iron, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Manganese, 3));
            addWaterRecipe("oreSheldonite", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial.Platinum, 2), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTCXMaterialGen.getNugget(GTMaterial.Iridium, 2));
            addMercuryRecipe("oreIridium", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial.Iridium, 2), GTMaterialGen.getDust(GTMaterial.Platinum, 1));
            addMercuryRecipe("oreSheldonite", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial.Platinum, 3), GTMaterialGen.getDust(GTMaterial.Nickel, 1), GTCXMaterialGen.getNugget(GTMaterial.Iridium, 2));
            addMercuryRecipe("oreGalena", 1, totalEu(12800), GTMaterialGen.getDust(GTCXMaterial.Galena, 2), GTCXMaterialGen.getSmallDust(GTMaterial.Sulfur, 1), Ic2Items.silverDust);
            addMercuryRecipe("oreSilver", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.silverDust, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 2));
            addMercuryRecipe("oreGold", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.goldDust, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Copper, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Nickel, 1));
            addMercuryRecipe("oreCopper", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.copperDust, 2), Ic2Items.goldDust);
            addMercuryRecipe("oreLead", 1, totalEu(12800), GTMaterialGen.getDust(GTCXMaterial.Lead, 2), Ic2Items.silverDust);
        }
        addWaterRecipe(GTMaterialGen.get(Blocks.NETHERRACK, 16), totalEu(204800), GTMaterialGen.get(Items.GOLD_NUGGET), GTMaterialGen.getIc2(Ic2Items.netherrackDust, 16));
        addWaterRecipe("oreRuby", 1, totalEu(12800), GTMaterialGen.getGem(GTMaterial.Ruby, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Ruby, 6), GTCXMaterialGen.getSmallDust(GTCXMaterial.GarnetRed, 2));
        addWaterRecipe("oreSapphire", 1, totalEu(12800), GTMaterialGen.getGem(GTMaterial.Sapphire, 1), GTCXMaterialGen.getSmallDust(GTMaterial.Sapphire, 6), GTCXMaterialGen.getSmallDust(GTMaterial.Aluminium, 2));
        //addWaterRecipe("oreTungsten", 1, totalEu(12800), GTMaterialGen.getDust(GEMaterial.Tungsten, 2), GEMaterialGen.getSmallDust(GEMaterial.Iron, 3), GEMaterialGen.getSmallDust(GEMaterial.Manganese, 3));
        addWaterRecipe("oreCoal", 1, totalEu(12800), GTMaterialGen.get(Items.COAL), Ic2Items.coalDust, GTCXMaterialGen.getSmallDust(GTMaterial.Thorium, 1));
        addWaterRecipe("oreLapis", 1, totalEu(12800), new ItemStack(Items.DYE, 12, 4), GTMaterialGen.getDust(GTMaterial.Lazurite, 3));
        addWaterRecipe("oreRedstone", 1, totalEu(12800), GTMaterialGen.get(Items.REDSTONE, 10), GTCXMaterialGen.getSmallDust(GTCXMaterial.Glowstone, 2));
        addWaterRecipe("oreDiamond", 1, totalEu(12800), GTMaterialGen.get(Items.DIAMOND), GTCXMaterialGen.getSmallDust(GTCXMaterial.Diamond, 8), Ic2Items.hydratedCoalDust);
        addWaterRecipe("oreEmerald", 1, totalEu(12800), GTMaterialGen.get(Items.EMERALD), GTCXMaterialGen.getSmallDust(GTMaterial.Emerald, 6), GTCXMaterialGen.getSmallDust(GTCXMaterial.Olivine, 2));
        addWaterRecipe("oreOlivine", 1, totalEu(12800), GTMaterialGen.getGem(GTCXMaterial.Olivine, 1), GTCXMaterialGen.getSmallDust(GTCXMaterial.Olivine, 6), GTCXMaterialGen.getSmallDust(GTMaterial.Emerald, 2));
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
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { input, new RecipeInputFluid(fluid) }, totalEu(totalEu), outlist);
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, List<ItemStack> outputs) {
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
        GTCXRecipeLists.INDUSTRIAL_GRINDER_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
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
