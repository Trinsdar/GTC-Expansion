package gtc_expansion.tile.multi;

import gtc_expansion.GEBlocks;
import gtc_expansion.GEItems;
import gtc_expansion.GEMachineGui;
import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GEContainerImplosionCompressor;
import gtc_expansion.material.GEMaterial;
import gtc_expansion.recipes.GERecipeLists;
import gtclassic.GTBlocks;
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

public class GETileMultiImplosionCompressor extends GTTileMultiBaseMachine {
    protected static final int[] slotInputs = { 0, 1 };
    protected static final int[] slotOutputs = { 2, 3 };
    protected static final int slotFuel = 4;
    public IFilter filter = new MachineFilter(this);
    public static final IBlockState casingStandardState = GEBlocks.casingStandard.getDefaultState();
    public static final IBlockState casingReinforcedState = GTBlocks.casingReinforced.getDefaultState();
    public static final IBlockState airState = Blocks.AIR.getDefaultState();
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/implosioncompressor.png");
    private static final int defaultEu = 32;

    public GETileMultiImplosionCompressor() {
        super(5, 2, defaultEu, 32);
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
        return new IFilter[]{filter};
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
        return GERecipeLists.IMPLOSION_COMPRESSOR_RECIPE_LIST;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GEMachineGui.GEImplosionCompressorGui.class;
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
        return new GEContainerImplosionCompressor(entityPlayer.inventory, this);
    }

    public static void init(){
        addRecipe("dustRuby", 1, 24, totalEu(2560), GTMaterialGen.getGem(GEMaterial.Ruby, 1));
        addRecipe("dustSapphire", 1, 24, totalEu(2560), GTMaterialGen.getGem(GEMaterial.Sapphire, 1));
        addRecipe("dustGreenSapphire", 1, 24, totalEu(2560), GTMaterialGen.getGem(GEMaterial.SapphireGreen, 1));
        addRecipe("dustEmerald", 1, 24, totalEu(2560), GTMaterialGen.get(Items.EMERALD, 1));
        addRecipe("dustDiamond", 1, 32, totalEu(2560), GTMaterialGen.getIc2(Ic2Items.industrialDiamond, 1));
        addRecipe("dustOlivine", 1, 24, totalEu(2560), GTMaterialGen.getGem(GEMaterial.Olivine, 1));
        addRecipe("dustRedGarnet", 1, 16, totalEu(2560), GTMaterialGen.getGem(GEMaterial.GarnetRed, 1));
        addRecipe("dustYellowGarnet", 1, 16, totalEu(2560), GTMaterialGen.getGem(GEMaterial.GarnetYellow, 1));
        addRecipe(GTMaterialGen.get(GEItems.iridiumAlloyIngot), 8, totalEu(2560), Ic2Items.iridiumPlate, GTMaterialGen.getDust(GEMaterial.DarkAshes, 4));
    }

    public static void addRecipe(ItemStack stack, int tnt, RecipeModifierHelpers.IRecipeModifier[] modifiers, ItemStack... outputs) {
        if (tnt > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack),
                    new RecipeInputItemStack(GTMaterialGen.getIc2(Ic2Items.industrialTNT, tnt)) }, modifiers, outputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputItemStack(stack) }, modifiers, outputs);
        }
    }

    public static void addRecipe(String input, int amount, int tnt, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {
        if (tnt > 0) {
            addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount),
                    new RecipeInputItemStack(GTMaterialGen.getIc2(Ic2Items.industrialTNT, tnt)) }, modifiers, outputs);
        } else {
            addRecipe(new IRecipeInput[] { new RecipeInputOreDict(input, amount) }, modifiers, outputs);
        }
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
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
        GERecipeLists.IMPLOSION_COMPRESSOR_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
    }

    @Override
    public Map<BlockPos, IBlockState> provideStructure() {
        Map<BlockPos, IBlockState> states = super.provideStructure();
        int3 dir = new int3(getPos(), getFacing());
        //Top Layer
        states.put(dir.back(1).asBlockPos(), casingReinforcedState);
        states.put(dir.left(1).asBlockPos(), casingStandardState);
        states.put(dir.forward(1).asBlockPos(), casingReinforcedState);
        states.put(dir.forward(1).asBlockPos(), casingStandardState);
        states.put(dir.right(1).asBlockPos(), casingReinforcedState);
        states.put(dir.right(1).asBlockPos(), casingStandardState);
        states.put(dir.back(1).asBlockPos(), casingReinforcedState);
        states.put(dir.back(1).asBlockPos(), casingStandardState);
        //Middle Layer
        states.put(dir.down(1).asBlockPos(), casingReinforcedState);
        int i;
        for (i = 0; i < 2; i++){
            states.put(dir.forward(1).asBlockPos(), casingReinforcedState);
        }
        for (i = 0; i < 2; i++){
            states.put(dir.left(1).asBlockPos(), casingReinforcedState);
        }
        for (i = 0; i < 2; i++){
            states.put(dir.back(1).asBlockPos(), casingReinforcedState);
        }
        states.put(dir.right(1).asBlockPos(), casingReinforcedState);
        states.put(dir.forward(1).asBlockPos(), airState);
        //Bottom Layer
        states.put(dir.down(1).asBlockPos(), casingReinforcedState);
        states.put(dir.back(1).asBlockPos(), casingReinforcedState);
        states.put(dir.left(1).asBlockPos(), casingStandardState);
        states.put(dir.forward(1).asBlockPos(), casingReinforcedState);
        states.put(dir.forward(1).asBlockPos(), casingStandardState);
        states.put(dir.right(1).asBlockPos(), casingReinforcedState);
        states.put(dir.right(1).asBlockPos(), casingStandardState);
        states.put(dir.back(1).asBlockPos(), casingReinforcedState);
        states.put(dir.back(1).asBlockPos(), casingStandardState);
        return states;
    }

    @Override
    public boolean checkStructure() {
        if (!world.isAreaLoaded(pos, 3)) {
            return false;
        }
        int3 dir = new int3(getPos(), getFacing());
        //Top Layer
        if (!(isReinforcedCasing(dir.back(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.left(1)))) {
            return false;
        }
        if (!(isReinforcedCasing(dir.forward(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.forward(1)))) {
            return false;
        }
        if (!(isReinforcedCasing(dir.right(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.right(1)))){
            return false;
        }
        if (!(isReinforcedCasing(dir.back(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.back(1)))) {
            return false;
        }
        //Middle Layer
        if (!(isReinforcedCasing(dir.down(1)))) {
            return false;
        }
        int i;
        for (i = 0; i < 2; i++){
            if (!(isReinforcedCasing(dir.forward(1)))) {
                return false;
            }
        }
        for (i = 0; i < 2; i++){
            if (!(isReinforcedCasing(dir.left(1)))) {
                return false;
            }
        }
        for (i = 0; i < 2; i++){
            if (!(isReinforcedCasing(dir.back(1)))) {
                return false;
            }
        }
        if (!(isReinforcedCasing(dir.right(1)))) {
            return false;
        }
        if (world.getBlockState(dir.forward(1).asBlockPos()) != airState) {
            return false;
        }
        //Bottom Layer
        if (!(isReinforcedCasing(dir.down(1)))){
            return false;
        }
        if (!(isReinforcedCasing(dir.back(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.left(1)))) {
            return false;
        }
        if (!(isReinforcedCasing(dir.forward(1)))) {// left
            return false;
        }
        if (!(isStandardCasing(dir.forward(1)))) {
            return false;
        }
        if (!(isReinforcedCasing(dir.right(1)))) {
            return false;
        }
        if (!(isStandardCasing(dir.right(1)))){
            return false;
        }
        if (!(isReinforcedCasing(dir.back(1)))) {
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
}