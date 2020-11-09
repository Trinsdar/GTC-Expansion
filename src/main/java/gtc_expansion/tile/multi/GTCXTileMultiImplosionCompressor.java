package gtc_expansion.tile.multi;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerImplosionCompressor;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtclassic.api.helpers.int3;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.tile.multi.GTTileMultiBaseMachine;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.item.IMachineUpgradeItem.UpgradeType;
import ic2.api.classic.network.adv.NetworkField;
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
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GTCXTileMultiImplosionCompressor extends GTTileMultiBaseMachine {
    protected static final int[] slotInputs = { 0, 1 };
    protected static final int[] slotOutputs = { 2, 3 };
    protected static final int slotFuel = 4;
    public IFilter filter = new MachineFilter(this);
    public static final IBlockState casingStandardState = GTCXBlocks.casingStandard.getDefaultState();
    public static final IBlockState casingReinforcedState = GTCXBlocks.casingReinforced.getDefaultState();
    public static final IBlockState airState = Blocks.AIR.getDefaultState();
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/implosioncompressor.png");
    private static final int defaultEu = 32;
    @NetworkField(index = 13)
    int ticker = 0;

    public GTCXTileMultiImplosionCompressor() {
        super(5, 2, defaultEu, 32);
        maxEnergy = 10000;
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
    public void update() {
        this.handleRedstone();
        this.updateNeighbors();
        boolean noRoom = this.addToInventory();
        if (this.shouldCheckRecipe) {
            this.lastRecipe = this.getRecipe();
            this.shouldCheckRecipe = false;
        }

        boolean canWork = this.canWork() && !noRoom;
        boolean operate = canWork && this.lastRecipe != null && this.lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE;
        if (operate) {
            this.handleChargeSlot(this.maxEnergy);
        }

        if (operate && this.canContinue() && this.energy >= this.energyConsume) {
            if (!this.getActive()) {
                this.getNetwork().initiateTileEntityEvent(this, 0, false);
            }

            this.setActive(true);
            this.progress += this.progressPerTick;
            ticker++;
            if (ticker == 40){
                world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1.0F);
                ticker = 0;
            }

            this.useEnergy(this.recipeEnergy);
            if (this.progress >= (float)this.recipeOperation) {
                this.process(this.lastRecipe);
                this.progress = 0.0F;
                this.notifyNeighbors();
            }

            this.getNetwork().updateTileGuiField(this, "progress");
        } else {
            ticker = 0;
            if (this.getActive()) {
                if (this.progress != 0.0F) {
                    this.getNetwork().initiateTileEntityEvent(this, 1, false);
                } else {
                    this.getNetwork().initiateTileEntityEvent(this, 2, false);
                }
            }

            if (this.progress != 0.0F) {
                this.progress = 0.0F;
                this.getNetwork().updateTileGuiField(this, "progress");
            }

            this.setActive(false);
        }

        if (this.supportsUpgrades) {
            for(int i = 0; i < this.upgradeSlots; ++i) {
                ItemStack item = (ItemStack)this.inventory.get(i + this.inventory.size() - this.upgradeSlots);
                if (item.getItem() instanceof IMachineUpgradeItem) {
                    ((IMachineUpgradeItem)item.getItem()).onTick(item, this);
                }
            }
        }

        this.updateComparators();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        ticker = nbt.getInteger("ticker");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("ticker", ticker);
        return nbt;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.IMPLOSION_COMPRESSOR;
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
        return GTCXRecipeLists.IMPLOSION_COMPRESSOR_RECIPE_LIST;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXMachineGui.GTCXImplosionCompressorGui.class;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public Set<UpgradeType> getSupportedTypes() {
        return new LinkedHashSet<>(Arrays.asList(UpgradeType.ImportExport, UpgradeType.RedstoneControl, UpgradeType.Sounds, UpgradeType.MachineModifierA, UpgradeType.MachineModifierB));
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerImplosionCompressor(entityPlayer.inventory, this);
    }

    public static void init(){
        addRecipe("dustRuby", 1, 6, totalEu(2560), GTMaterialGen.getGem(GTMaterial.Ruby, 1));
        addRecipe("dustSapphire", 1, 6, totalEu(2560), GTMaterialGen.getGem(GTMaterial.Sapphire, 1));
        addRecipe("dustEmerald", 1, 6, totalEu(2560), GTMaterialGen.get(Items.EMERALD, 1));
        addRecipe("dustDiamond", 1, 8, totalEu(2560), GTMaterialGen.getIc2(Ic2Items.industrialDiamond, 1));
        addRecipe(Ic2Items.coalChunk, 8, totalEu(2560), GTMaterialGen.getIc2(Ic2Items.industrialDiamond, 1));
        addRecipe("dustOlivine", 1, 6, totalEu(2560), GTMaterialGen.getGem(GTCXMaterial.Olivine, 1));
        addRecipe("dustRedGarnet", 1, 4, totalEu(2560), GTMaterialGen.getGem(GTCXMaterial.GarnetRed, 1));
        addRecipe("dustYellowGarnet", 1, 4, totalEu(2560), GTMaterialGen.getGem(GTCXMaterial.GarnetYellow, 1));
        addRecipe(GTMaterialGen.get(GTCXItems.iridiumAlloyIngot), 8, totalEu(2560), Ic2Items.iridiumPlate, GTMaterialGen.getDust(GTCXMaterial.DarkAshes, 4));
    }

    public static void addRecipe(ItemStack stack, int tnt, RecipeModifierHelpers.IRecipeModifier[] modifiers, ItemStack... outputs) {
        if (tnt > 0) {
            addRecipe(new IRecipeInput[] { input(stack),
                    new RecipeInputItemStack(GTMaterialGen.getIc2(Ic2Items.industrialTNT, tnt)) }, modifiers, outputs);
        } else {
            addRecipe(new IRecipeInput[] { input(stack) }, modifiers, outputs);
        }
    }

    public static void addRecipe(String input, int amount, int tnt, RecipeModifierHelpers.IRecipeModifier[] modifiers,
                                 ItemStack... outputs) {
        if (tnt > 0) {
            addRecipe(new IRecipeInput[] { input(input, amount),
                    new RecipeInputItemStack(GTMaterialGen.getIc2(Ic2Items.industrialTNT, tnt)) }, modifiers, outputs);
        } else {
            addRecipe(new IRecipeInput[] { input(input, amount) }, modifiers, outputs);
        }
    }

    public static void addRecipe(IRecipeInput input, int tnt, int totalEu, String recipeId,
                                 ItemStack... outputs) {
        if (tnt > 0) {
            addRecipe(new IRecipeInput[] { input,
                    input(GTMaterialGen.getIc2(Ic2Items.industrialTNT, tnt)) }, totalEu(totalEu), recipeId, outputs);
        } else {
            addRecipe(new IRecipeInput[] { input }, totalEu(totalEu), recipeId, outputs);
        }
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalEu(int amount) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create((amount / defaultEu) - 100) };
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, ItemStack... outputs){
        addRecipe(inputs, modifiers, outputs[0].getUnlocalizedName(), outputs);
    }

    public static void addRecipe(IRecipeInput[] inputs, RecipeModifierHelpers.IRecipeModifier[] modifiers, String recipeID, ItemStack... outputs) {
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
        addRecipe(inlist, new MachineOutput(mods, outlist), recipeID);
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeID) {
        GTCXRecipeLists.IMPLOSION_COMPRESSOR_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), defaultEu);
    }

    /*@Override
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
    }*/

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
        if (!world.isAirBlock(dir.forward(1).asBlockPos())) {
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
