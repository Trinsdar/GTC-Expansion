package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerElectrolyzer;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.util.GTCXLang;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBaseMachine;
import gtclassic.common.GTItems;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.RecipeModifierHelpers.IRecipeModifier;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.fluid.IC2Tank;
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
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import java.util.Set;
import java.util.function.Predicate;

public class GTCXTileElectrolyzer extends GTTileBaseMachine implements ITankListener, IClickable {

    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/industrialelectrolyzer.png");
    public IFilter filter = new MachineFilter(this);
    public static final int slotFuel = 9;
    public static final int SLOT_TANK = 8;
    public static final String NBT_TANK = "tank";
    protected static final int[] slotInputs = { 0, 1 };
    protected static final int[] slotOutputs = { 2, 3, 4, 5, 6, 7 };
    @NetworkField(index = 13)
    private IC2Tank tank;
    private static final int defaultEu = 64;

    public GTCXTileElectrolyzer() {
        super(10, 2, defaultEu, 100, 128);
        setFuelSlot(slotFuel);
        this.tank = new IC2Tank(16000);
        this.tank.addListener(this);
        this.addGuiFields(NBT_TANK);
        maxEnergy = 10000;
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
        handler.registerDefaultSlotsForSide(RotationList.UP, slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.DOWN, slotFuel);
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
        return GTCXLang.INDUSTRIAL_ELECTROLYZER;
    }

    @Override
    public void onTankChanged(IFluidTank tank) {
        this.getNetwork().updateTileGuiField(this, NBT_TANK);
        this.setStackInSlot(SLOT_TANK, ItemDisplayIcon.createWithFluidStack(this.tank.getFluid()));
        shouldCheckRecipe = true;
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
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(recipe.getInputs());
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
            if (key instanceof RecipeInputFluid && !fluidExtracted) {
                tank.drainInternal(((RecipeInputFluid) key).fluid, true);
                fluidExtracted = true;
                keyIter.remove();
                continue;
            }
            int count = key.getAmount();
            for (Iterator<ItemStack> inputIter = inputs.iterator(); inputIter.hasNext();) {
                ItemStack input = inputIter.next();
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
                        if (input.isEmpty()) {
                            inputIter.remove();
                        }
                        keyIter.remove();
                        break;
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
                    inputIter.remove();
                }
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
        FluidStack fluid = tank.getFluid();
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, fluid, StackUtil.copyList(inputs)) ? lastRecipe : null;
            if (lastRecipe == null) {
                progress = 0;
            }
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<MultiRecipe>() {

                @Override
                public boolean test(MultiRecipe t) {
                    return checkRecipe(t, fluid, StackUtil.copyList(inputs));
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

    public boolean checkRecipe(MultiRecipe entry, FluidStack inputFluid, List<ItemStack> inputs) {
        boolean hasCheckedFluid = false;
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(entry.getInputs());
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
            if (key instanceof RecipeInputFluid) {
                if (!hasCheckedFluid) {
                    hasCheckedFluid = true;
                    if (inputFluid != null && inputFluid.containsFluid(((RecipeInputFluid) key).fluid)) {
                        keyIter.remove();
                    }
                }
            }
            int toFind = key.getAmount();
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
        }
        return recipeKeys.isEmpty();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing!= null) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null
                ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank)
                : super.getCapability(capability, facing);
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<IMachineUpgradeItem.UpgradeType>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerElectrolyzer(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXIndustrialElectrolyzerGui.class;
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
        return GTCXRecipeLists.ELECTROLYZER_RECIPE_LIST;
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
        return GTCExpansion.getAprilFirstSound(Ic2Sounds.magnetizerOp);
    }

    public static void init() {
        Item tube = GTItems.testTube;
        /** Recipes from GT1 **/
        addRecipe(GTMaterialGen.getWater(6), 0, totalEu(50000), GTMaterialGen.getTube(GTMaterial.Hydrogen, 4), GTMaterialGen.getTube(GTMaterial.Oxygen, 2));
        addRecipe(GTMaterialGen.getIc2(Ic2Items.waterCell, 6), 6, totalEu(50000), GTMaterialGen.getIc2(Ic2Items.emptyCell, 6), GTMaterialGen.getTube(GTMaterial.Hydrogen, 4), GTMaterialGen.getTube(GTMaterial.Oxygen, 2));
        addRecipe("dustCoal", 4, 0, totalEu(7500), GTMaterialGen.getDust(GTMaterial.Carbon, 8));
        addRecipe("dustRuby", 9, 3, totalEu(25000), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Chrome, 1), GTMaterialGen.getTube(GTMaterial.Oxygen, 3));
        addRecipe("dustSapphire", 8, 3, totalEu(20000), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 3));
        addRecipe("dustGreenSapphire", 4, 0, totalEu(15000), GTMaterialGen.getDust(GTMaterial.Sapphire, 4));
        addRecipe("dustEmerald", 29, 12, totalEu(30000), GTMaterialGen.getTube(GTMaterial.Oxygen, 9), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getTube(GTMaterial.Beryllium, 3), GTMaterialGen.getDust(GTMaterial.Silicon, 6));
        addRecipe("dustEnderPearl", 16, 16, totalEu(65000), GTMaterialGen.getTube(GTMaterial.Chlorine, 6), GTMaterialGen.getTube(GTMaterial.Nitrogen, 5), GTMaterialGen.getTube(GTMaterial.Beryllium, 1), GTMaterialGen.getTube(GTMaterial.Potassium, 4));
        addRecipe("dustLazurite", 29, 8, totalEu(295000), GTMaterialGen.getTube(GTMaterial.Sodium, 4), GTMaterialGen.getDust(GTMaterial.Aluminium, 3), GTMaterialGen.getDust(GTMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Calcium, 4));
        addRecipe("dustPyrite", 3, 0, totalEu(15000), GTMaterialGen.getIc2(Ic2Items.ironDust, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 2));
        addRecipe("dustCalcite", 10, 5, totalEu(50000), GTMaterialGen.getTube(GTMaterial.Calcium, 2), GTMaterialGen.getDust(GTMaterial.Carbon, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 3));
        addRecipe("dustSodalite", 11, 5, totalEu(115000), GTMaterialGen.getTube(GTMaterial.Chlorine, 1), GTMaterialGen.getTube(GTMaterial.Sodium, 4), GTMaterialGen.getDust(GTMaterial.Aluminium, 3), GTMaterialGen.getDust(GTMaterial.Silicon, 3));
        addRecipe("dustBauxite", 24, 16, totalEu(250000), GTMaterialGen.getTube(GTMaterial.Oxygen, 6), GTMaterialGen.getDust(GTMaterial.Aluminium, 16), GTMaterialGen.getDust(GTMaterial.Titanium, 1), GTMaterialGen.getTube(GTMaterial.Hydrogen, 10));
        addRecipe(GTMaterialGen.get(Items.BLAZE_POWDER, 8), 0, totalEu(15000), GTMaterialGen.getIc2(Ic2Items.coalDust, 2), GTMaterialGen.get(Items.GUNPOWDER, 1));
        IRecipeInput sand = new RecipeInputCombined(32, new RecipeInputItemStack(GTMaterialGen.get(Blocks.SAND, 32, 0)), new RecipeInputItemStack(GTMaterialGen.get(Blocks.SAND, 32, 1)));
        addRecipe(new IRecipeInput[] { sand, new RecipeInputItemStack(GTMaterialGen.get(GTItems.testTube, 1)) }, totalEu(50000), GTMaterialGen.getDust(GTMaterial.Silicon, 1), GTMaterialGen.getTube(GTMaterial.Oxygen, 1));
        addRecipe("dustFlint", 8, 1, totalEu(5000), GTMaterialGen.getDust(GTMaterial.Silicon, 1), GTMaterialGen.getTube(GTMaterial.Oxygen, 1));

        /** Recipes from GT2 **/
        addRecipe("dustClay", 7, 2, totalEu(20000), GTMaterialGen.getDust(GTMaterial.Lithium, 1), GTMaterialGen.getDust(GTMaterial.Silicon, 2), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getTube(GTMaterial.Sodium, 2));
        addRecipe("dustSaltpeter", 10, 7, totalEu(5500), GTMaterialGen.getTube(GTMaterial.Potassium, 2), GTMaterialGen.getTube(GTMaterial.Nitrogen, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 3));
        addRecipe("dustCinnabar", 4, 2, totalEu(117760), GTMaterialGen.getTube(GTMaterial.Mercury, 2), GTMaterialGen.getDust(GTMaterial.Sulfur, 2));
        addRecipe("dustSphalerite", 4, 0, totalEu(15000), GTMaterialGen.getDust(GTCXMaterial.Zinc, 2), GTMaterialGen.getDust(GTMaterial.Sulfur, 2));
        addRecipe("dustOlivine", 9, 2, totalEu(36000), GTMaterialGen.getDust(GTCXMaterial.Magnesium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 1), GTMaterialGen.getIc2(Ic2Items.ironDust, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 2));
        addRecipe("dustGalena", 2, 0, totalEu(120000), GTCXMaterialGen.getSmallDust(GTCXMaterial.Silver, 3), GTCXMaterialGen.getSmallDust(GTCXMaterial.Lead, 3), GTCXMaterialGen.getSmallDust(GTMaterial.Sulfur, 2));
        addRecipe("dustObsidian", 8, 4, totalEu(5000), GTMaterialGen.getDust(GTCXMaterial.Magnesium, 1), Ic2Items.ironDust.copy(), GTMaterialGen.getDust(GTMaterial.Silicon, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 4));
        addRecipe("dustCharcoal", 4, 0, totalEu(7500), GTMaterialGen.getDust(GTMaterial.Carbon, 4));
        addRecipe("dustPyrope", 20, 6, totalEu(89500), GTMaterialGen.getDust(GTCXMaterial.Magnesium, 3), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustAlmandine", 20, 6, totalEu(82000), GTMaterialGen.getIc2(Ic2Items.ironDust, 3), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustSpessartine", 20, 6, totalEu(90500), GTMaterialGen.getDust(GTCXMaterial.Manganese, 3), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustAndradite", 20, 9, totalEu(64000), GTMaterialGen.getTube(GTMaterial.Calcium, 3), GTMaterialGen.getIc2(Ic2Items.ironDust, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustGrossular", 20, 9, totalEu(102500), GTMaterialGen.getTube(GTMaterial.Calcium, 3), GTMaterialGen.getDust(GTMaterial.Aluminium, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));
        addRecipe("dustUvarovite", 20, 9, totalEu(110000), GTMaterialGen.getTube(GTMaterial.Calcium, 3), GTMaterialGen.getDust(GTMaterial.Chrome, 2), GTMaterialGen.getDust(GTMaterial.Silicon, 3), GTMaterialGen.getTube(GTMaterial.Oxygen, 6));

        addRecipe(GTMaterialGen.getTube(GTCXMaterial.Glyceryl, 20), 0, totalEu(720000), GTMaterialGen.getDust(GTMaterial.Carbon, 3), GTMaterialGen.getTube(GTMaterial.Hydrogen, 5), GTMaterialGen.getTube(GTMaterial.Nitrogen, 3), GTMaterialGen.get(GTItems.testTube, 12));
        addRecipe(GTMaterialGen.getTube(GTCXMaterial.SulfuricAcid, 7), 0, totalEu(35280), GTMaterialGen.getTube(GTMaterial.Hydrogen, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 2), GTMaterialGen.getDust(GTMaterial.Sulfur, 1), GTMaterialGen.get(GTItems.testTube, 3));
        addRecipe(GTMaterialGen.getTube(GTCXMaterial.SodiumPersulfate, 6), 0, totalEu(38880), GTMaterialGen.getTube(GTMaterial.Oxygen, 2), GTMaterialGen.getTube(GTMaterial.Sodium, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 1), GTMaterialGen.get(GTItems.testTube, 3));
        addRecipe(GTMaterialGen.getTube(GTCXMaterial.NitroCarbon, 2), 0, totalEu(5760), GTMaterialGen.getTube(GTMaterial.Nitrogen, 1), GTMaterialGen.getDust(GTMaterial.Carbon, 1), GTMaterialGen.get(GTItems.testTube));
        addRecipe(GTMaterialGen.getTube(GTMaterial.Methane, 5), 0, totalEu(5760), GTMaterialGen.getTube(GTMaterial.Hydrogen, 4), GTMaterialGen.getDust(GTMaterial.Carbon, 1), GTMaterialGen.get(GTItems.testTube));
        addRecipe(GTMaterialGen.getTube(GTCXMaterial.SodiumSulfide, 2), 0, totalEu(5760), GTMaterialGen.getTube(GTMaterial.Sodium, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 1), GTMaterialGen.get(GTItems.testTube));
        addRecipe(GTMaterialGen.getIc2(Ic2Items.airCell, 16), 16, totalEu(1000000), GTMaterialGen.getIc2(Ic2Items.emptyCell, 16), GTMaterialGen.getTube(GTMaterial.Nitrogen, 9), GTMaterialGen.getTube(GTMaterial.Oxygen, 4), GTMaterialGen.getTube(GTMaterial.Helium, 1), GTMaterialGen.getTube(GTMaterial.Neon, 1), GTMaterialGen.getTube(GTMaterial.Argon, 1));

        /** New Recipes I added **/
        addRecipe(GTMaterialGen.get(Items.QUARTZ, 1), 2, totalEu(8000), GTMaterialGen.getDust(GTMaterial.Silicon, 1), GTMaterialGen.getTube(GTMaterial.Oxygen, 2));
        addRecipe("dustChromite",7, 4, totalEu(512000), Ic2Items.ironDust, GTMaterialGen.getDust(GTMaterial.Chrome, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 4));
        addRecipe("dustCassiterite",1, 2, totalEu(512000), Ic2Items.tinDust, GTMaterialGen.getTube(GTMaterial.Oxygen, 2));

        /** Fluid recipes **/
        addRecipe(GTMaterialGen.getFluidStack("water", 6000), 6, totalEu(50000), GTMaterialGen.getTube(GTMaterial.Hydrogen, 4), GTMaterialGen.getTube(GTMaterial.Oxygen, 2));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.SulfuricAcid, 7000), 4, totalEu(32580), GTMaterialGen.getTube(GTMaterial.Hydrogen, 2), GTMaterialGen.getTube(GTMaterial.Oxygen, 2), GTMaterialGen.getDust(GTMaterial.Sulfur, 1));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.SodiumPersulfate, 6000), 3, totalEu(38880), GTMaterialGen.getTube(GTMaterial.Oxygen, 2), GTMaterialGen.getTube(GTMaterial.Sodium, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 1));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.NitroCarbon, 2000), 1, totalEu(5760), GTMaterialGen.getTube(GTMaterial.Nitrogen, 1), GTMaterialGen.getDust(GTMaterial.Carbon, 1));
        addRecipe(GTMaterialGen.getFluidStack(GTMaterial.Methane, 5000), 4, totalEu(5760), GTMaterialGen.getTube(GTMaterial.Hydrogen, 4), GTMaterialGen.getDust(GTMaterial.Carbon, 1));
        addRecipe(GTMaterialGen.getFluidStack(GTCXMaterial.SodiumSulfide, 2000), 1, totalEu(5760), GTMaterialGen.getTube(GTMaterial.Sodium, 1), GTMaterialGen.getDust(GTMaterial.Sulfur, 1));
    }

    public static void addCustomRecipe(ItemStack stack0, ItemStack stack1, IRecipeModifier[] modifiers,
                                       ItemStack... outputs) {
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack0),
                new RecipeInputItemStack(stack1), }, modifiers, outputs);
    }

    public static void addCustomRecipe(String input, int amount, ItemStack stack, IRecipeModifier[] modifiers,
                                       ItemStack... outputs) {
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount),
                new RecipeInputItemStack(stack), }, modifiers, outputs);
    }


    public static void addRecipe(ItemStack stack, int cells, IRecipeModifier[] modifiers, ItemStack... outputs) {
        if (cells > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack),
                    new RecipeInputItemStack(GTMaterialGen.get(GTItems.testTube, cells)) }, modifiers, outputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack) }, modifiers, outputs);
        }
    }

    public static void addRecipe(String input, int amount, int cells, IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {
        if (cells > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount),
                    new RecipeInputItemStack(GTMaterialGen.get(GTItems.testTube, cells)) }, modifiers, outputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount) }, modifiers, outputs);
        }
    }

    public static void addRecipe(FluidStack fluid, int cells, IRecipeModifier[] modifiers, ItemStack... outputs) {
        if (cells > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid),
                    new RecipeInputItemStack(GTMaterialGen.get(GTItems.testTube, cells)) }, modifiers, outputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputFluid(fluid) }, modifiers, outputs);
        }
    }

    public static IRecipeModifier[] totalEu(int amount) {
        return new IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    public static void addRecipe(IRecipeInput[] inputs, IRecipeModifier[] modifiers, ItemStack... outputs) {
        List<IRecipeInput> inlist = new ArrayList<>();
        List<ItemStack> outlist = new ArrayList<>();
        for (IRecipeInput input : inputs) {
            inlist.add(input);
        }
        NBTTagCompound mods = new NBTTagCompound();
        for (IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(inlist, new MachineOutput(mods, outlist));
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
        GTCXRecipeLists.ELECTROLYZER_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
    }

    public static void removeRecipe(String id) {
        GTCXRecipeLists.ELECTROLYZER_RECIPE_LIST.removeRecipe(id);
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
        return GTHelperFluid.doClickableFluidContainerThings(player, hand, world, pos, this.tank);
    }
}
