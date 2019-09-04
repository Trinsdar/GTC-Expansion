package gtc_expansion.tile.multi;

import gtc_expansion.GTBlocks2;
import gtc_expansion.GTGuiMachine2;
import gtc_expansion.GTMod2;
import gtc_expansion.container.GTContainerIndustrialGrinder;
import gtc_expansion.material.GTMaterial2;
import gtc_expansion.material.GTMaterialGen2;
import gtclassic.GTBlocks;
import gtclassic.GTItems;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.multi.GTTileMultiBaseMachine;
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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GTTileMultiIndustrialGrinder extends GTTileMultiBaseMachine {
    protected static final int[] slotInputs = { 0, 1 };
    protected static final int[] slotOutputs = { 2, 3, 4, 5, 6, 7 };
    protected static final int slotFuel = 8;
    public IFilter filter = new MachineFilter(this);
    public static final IBlockState casingStandardState = GTBlocks2.casingStandard.getDefaultState();
    public static final IBlockState casingReinforcedState = GTBlocks.casingReinforced.getDefaultState();
    public static final IBlockState waterState = Blocks.WATER.getDefaultState();
    public static final GTRecipeMultiInputList RECIPE_LIST = new GTRecipeMultiInputList("gt.industrialgrinder");
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTMod2.MODID, "textures/gui/industrialgrinder.png");
    private static final int defaultEu = 120;

    public GTTileMultiIndustrialGrinder() {
        super(9, 2, defaultEu, 128);
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
        return new LangComponentHolder.LocaleBlockComp(this.getBlockType().getUnlocalizedName());
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
        return RECIPE_LIST;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTGuiMachine2.GTIndustrialGrinderGui.class;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(IMachineUpgradeItem.UpgradeType.values()));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTContainerIndustrialGrinder(entityPlayer.inventory, this);
    }

    public static void init(){
        addWaterRecipe(GTMaterialGen.get(Blocks.NETHERRACK, 16), totalEu(204800), GTMaterialGen.get(Items.GOLD_NUGGET), GTMaterialGen.getDust(GTMaterial2.Netherrack, 16));
        addWaterRecipe("oreUranium", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial2.Uranium, 2), GTMaterialGen2.getSmallDust(GTMaterial2.Plutonium, 1));
        addWaterRecipe("oreSilver", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.silverDust, 2), GTMaterialGen2.getSmallDust(GTMaterial2.Lead, 2));
        addWaterRecipe("oreIron", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.ironDust, 2), GTMaterialGen2.getSmallDust(GTMaterial2.Tin, 1), GTMaterialGen2.getSmallDust(GTMaterial2.Nickel, 1));
        addWaterRecipe("oreGold", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.goldDust, 2), GTMaterialGen2.getSmallDust(GTMaterial2.Copper, 1), GTMaterialGen2.getSmallDust(GTMaterial2.Nickel, 1));
        addWaterRecipe("oreCopper", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.copperDust, 2), GTMaterialGen2.getSmallDust(GTMaterial2.Gold, 1), GTMaterialGen2.getSmallDust(GTMaterial2.Nickel, 1));
        addWaterRecipe("oreTin", 1, totalEu(12800), GTMaterialGen.getIc2(Ic2Items.tinDust, 2), GTMaterialGen2.getSmallDust(GTMaterial2.Iron, 1), GTMaterialGen2.getSmallDust(GTMaterial2.Zinc, 1));
        addWaterRecipe("oreLead", 1, totalEu(12800), GTMaterialGen.getDust(GTMaterial2.Lead, 2), GTMaterialGen2.getSmallDust(GTMaterial2.Silver, 2));
        addWaterRecipe("oreRuby", 1, totalEu(12800), GTMaterialGen.getGem(GTMaterial2.Ruby, 1), GTMaterialGen2.getSmallDust(GTMaterial2.Ruby, 6), GTMaterialGen2.getSmallDust(GTMaterial2.GarnetRed, 2));
        //addWaterRecipe("oreGreenSapphire", 1, totalEu(12800), GTMaterialGen.getGem(GTMaterial2.SapphireGreen, 1), GTMaterialGen2.getSmallDust(GTMaterial2.SapphireGreen, 6), GTMaterialGen2.getSmallDust(GTMaterial2.Sapphire, 2));
        addWaterRecipe("oreSapphire", 1, totalEu(12800), GTMaterialGen.getGem(GTMaterial2.Sapphire, 1), GTMaterialGen2.getSmallDust(GTMaterial2.Sapphire, 6), GTMaterialGen2.getSmallDust(GTMaterial2.SapphireGreen, 2));
    }

    public static void addWaterCellRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(Ic2Items.emptyCell);
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputItemStack(Ic2Items.waterCell) }, modifiers, outlist);
    }

    public static void addWaterRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                      ItemStack... outputs) {
        addWaterCellRecipe(input, modifiers, outputs);
        addWaterTubeRecipe(input, modifiers, outputs);
        addWaterBucketRecipe(input, modifiers, outputs);
    }

    public static void addWaterTubeRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(GTItems.testTube));
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputItemStack(GTMaterialGen.getModdedTube("water", 1)) }, modifiers, outlist);
    }

    public static void addWaterBucketRecipe(ItemStack input, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                            ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(Items.BUCKET));
        addRecipe(new IRecipeInput[] { new RecipeInputItemStack(input), new RecipeInputItemStack(GTMaterialGen.get(Items.WATER_BUCKET)) }, modifiers, outlist);
    }

    public static void addWaterCellRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(Ic2Items.emptyCell);
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputItemStack(Ic2Items.waterCell) }, modifiers, outlist);
    }

    public static void addWaterRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        addWaterCellRecipe(input, amount, modifiers, outputs);
        addWaterTubeRecipe(input, amount, modifiers, outputs);
        addWaterBucketRecipe(input, amount, modifiers, outputs);
    }

    public static void addWaterTubeRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(GTItems.testTube));
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputItemStack(GTMaterialGen.getModdedTube("water", 1)) }, modifiers, outlist);
    }

    public static void addWaterBucketRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                          ItemStack... outputs) {
        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        outlist.add(GTMaterialGen.get(Items.BUCKET));
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount), new RecipeInputItemStack(GTMaterialGen.get(Items.WATER_BUCKET)) }, modifiers, outlist);
    }

    public static void addRecipe(String input, int amount, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                      ItemStack... outputs) {

        List<ItemStack> outlist = new ArrayList<>();
        for (ItemStack output : outputs) {
            outlist.add(output);
        }
        addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount) }, modifiers, outlist);
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
        RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
    }

    @Override
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
    }

    @Override
    public boolean checkStructure() {
        if (!world.isAreaLoaded(pos, 3)) {
            return false;
        }
        // we doing it "big math" style not block by block
        int3 dir = new int3(getPos(), getFacing());
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
        if (!isStandardCasing(dir.forward(2).right(2))) {// missing front right column
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
}
