package gtc_expansion.tile.multi;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui;
import gtc_expansion.container.GTCXContainerCokeOven;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.interfaces.IGTCapabilityTile;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.recipes.GTCXRecipeLists;
import gtc_expansion.tile.GTCXTileBrick;
import gtc_expansion.util.GTCXPassiveMachineFilter;
import gtc_expansion.util.MultiBlockHelper;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.interfaces.IGTMultiTileStatus;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTFluidMachineOutput;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBasePassiveMachine;
import gtclassic.common.GTBlocks;
import ic2.api.classic.recipe.RecipeModifierHelpers;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static gtclassic.api.tile.GTTileBaseMachine.MOVE_CONTAINER_TAG;

public class GTCXTileMultiCokeOven extends GTTileBasePassiveMachine implements ITankListener, IGTMultiTileStatus, IGTCapabilityTile, IGTItemContainerTile, IClickable {
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/cokeoven.png");
    IC2Tank tank = new IC2Tank(16000);
    public IFilter filter;
    public boolean lastState;
    public boolean firstCheck = true;
    private int tickOffset = 0;
    public static final IBlockState brickState = GTCXBlocks.fireBrickBlock.getDefaultState();
    public GTCXTileMultiCokeOven() {
        super(3, 100);
        this.tank.addListener(this);
        this.addGuiFields("lastState");
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        filter = new GTCXPassiveMachineFilter(this);
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Import, 0);
        handler.registerDefaultSlotAccess(AccessRule.Export, 1);
        handler.registerDefaultSlotsForSide(RotationList.DOWN.invert(), 0);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), 1);
        handler.registerInputFilter(filter, 0);
        handler.registerSlotType(SlotType.Input, 0);
        handler.registerSlotType(SlotType.Output, 1);
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    public IFilter[] getInputFilters(int[] ints) {
        return new IFilter[]{filter};
    }

    @Override
    public boolean isRecipeSlot(int i) {
        return i == 0;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{1};
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.COKE_OVEN;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return GTCXRecipeLists.COKE_OVEN_RECIPE_LIST;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerCokeOven(entityPlayer.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GTCXMachineGui.GTCXCokeOvenGui.class;
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.setStackInSlot(2, ItemDisplayIcon.createWithFluidStack(this.tank.getFluid()));
        this.shouldCheckRecipe = true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return super.hasCapability(capability, facing) || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
           return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank);
        }
        return super.getCapability(capability, facing);
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
        MachineOutput outputs = lastRecipe.getOutputs();
        int empty = 0;
        int[] outputSlots = getOutputSlots();
        for (int slot : outputSlots) {
            if (getStackInSlot(slot).isEmpty()) {
                empty++;
            }
        }
        if (outputs instanceof GTFluidMachineOutput){
            GTFluidMachineOutput fluidOutputs = (GTFluidMachineOutput) outputs;
            if (tank.getFluidAmount() == 0 && empty == outputSlots.length){
                return lastRecipe;
            }
            FluidStack fluid = tank.getFluid();
            boolean fluidFits = false;
            for (FluidStack output : fluidOutputs.getFluids()){
                if (output.isFluidEqual(fluid) && tank.getFluidAmount() + output.amount <= tank.getCapacity()){
                    fluidFits = true;
                    break;
                }
            }
            if (fluidFits && empty == outputSlots.length){
                return lastRecipe;
            }
            if (fluidFits || tank.getFluidAmount() == 0){
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
        } else {
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
        }
        return null;
    }

    @Override
    public void process(MultiRecipe recipe) {
        MachineOutput output = recipe.getOutputs().copy();
        if (output instanceof GTFluidMachineOutput){
            for (FluidStack fluid : ((GTFluidMachineOutput)output).getFluids()){
                tank.fillInternal(fluid, true);
            }
            for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
                if (!(stack.getItem() instanceof ItemDisplayIcon)){
                    outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
                }
            }
        } else {
            for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
                outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
            }
        }

        NBTTagCompound nbt = recipe.getOutputs().getMetadata();
        boolean shiftContainers = nbt != null && nbt.getBoolean(MOVE_CONTAINER_TAG);
        List<ItemStack> inputs = getInputs();
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(recipe.getInputs());
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
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
        shouldCheckRecipe = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.lastState = nbt.getBoolean("lastState");
        this.tank.readFromNBT(nbt.getCompoundTag("tank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("lastState", lastState);
        this.tank.writeToNBT(this.getTag(nbt, "tank"));
        return nbt;
    }

    @Override
    public boolean isValidInput(ItemStack par1) {
        return super.isValidInput(par1) || GTHelperStack.matchOreDict(par1, "logWood");
    }

    public static void init(){
        addRecipe(input(GTMaterialGen.get(Items.COAL, 1, 0)), 720, GTMaterialGen.get(GTCXItems.coalCoke), GTMaterialGen.getFluidStack(GTCXMaterial.Creosote, 500));
        addRecipe(input(GTMaterialGen.get(Blocks.COAL_BLOCK, 1, 0)), 1620, GTMaterialGen.get(GTCXBlocks.coalCokeBlock), GTMaterialGen.getFluidStack(GTCXMaterial.Creosote, 4500));
        addRecipe(input("logWood", 1), 720, GTMaterialGen.get(GTBlocks.brittleCharcoal), GTMaterialGen.getFluidStack(GTCXMaterial.Creosote, 250));
    }

    public static RecipeModifierHelpers.IRecipeModifier[] totalTime(int total) {
        return new RecipeModifierHelpers.IRecipeModifier[] { RecipeModifierHelpers.ModifierType.RECIPE_LENGTH.create(total - 100) };
    }

    public static void addRecipe(IRecipeInput input, int totalTime, ItemStack output, FluidStack outputFluid){
        addRecipe(new IRecipeInput[]{input}, totalTime, outputFluid, output);
    }

    public static void addRecipe(IRecipeInput[] inputs, int totalTime,  ItemStack... outputs) {
        addRecipe(inputs, totalTime, outputs[0].getUnlocalizedName(), outputs);
    }

    public static void addRecipe(IRecipeInput[] inputs, int totalTime, String recipeId, ItemStack... outputs) {
        RecipeModifierHelpers.IRecipeModifier[] modifiers = totalTime(totalTime);
        List<IRecipeInput> inlist = new ArrayList<>(Arrays.asList(inputs));
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        List<ItemStack> outlist = new ArrayList<>(Arrays.asList(outputs));
        addRecipe(inlist, new MachineOutput(mods, outlist), recipeId);
    }

    public static void addRecipe(IRecipeInput[] inputs, int totalTime, FluidStack fluidOutput,  ItemStack... outputs) {
        addRecipe(inputs, totalTime, outputs[0].getUnlocalizedName(), fluidOutput, outputs);
    }

    public static void addRecipe(IRecipeInput[] inputs, int totalTime, String recipeId, FluidStack fluidOutput, ItemStack... outputs) {
        List<FluidStack> fluidStacks = new ArrayList<>();
        fluidStacks.add(fluidOutput);
        RecipeModifierHelpers.IRecipeModifier[] modifiers = totalTime(totalTime);
        List<IRecipeInput> inlist = new ArrayList<>(Arrays.asList(inputs));
        NBTTagCompound mods = new NBTTagCompound();
        for (RecipeModifierHelpers.IRecipeModifier modifier : modifiers) {
            modifier.apply(mods);
        }
        List<ItemStack> outlist = new ArrayList<>(Arrays.asList(outputs));
        addRecipe(inlist, new GTFluidMachineOutput(mods, outlist, fluidStacks), recipeId);
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output, String recipeId) {
        GTCXRecipeLists.COKE_OVEN_RECIPE_LIST.addRecipe(input, output, recipeId, 0);
    }

    @Override
    public boolean canWork() {
        if (world.getTotalWorldTime() % (128 + this.tickOffset) == 0 || firstCheck) {
            boolean lastCheck = this.lastState;
            lastState = checkStructure();
            firstCheck = false;
            this.getNetwork().updateTileGuiField(this, "lastState");
            /*if (lastCheck != this.lastState) {
                MultiBlockHelper.INSTANCE.removeCore(this.getWorld(), this.getPos());
                if (this.lastState) {
                    MultiBlockHelper.INSTANCE.addCore(this.getWorld(), this.getPos(), new ArrayList<>(this.provideStructure().keySet()));
                }
            }*/
        }
        return lastState;
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        this.firstCheck = true;
        if (this.isSimulating()) {
            this.tickOffset = world.rand.nextInt(128);
        }
    }

    @Override
    public void onUnloaded() {
        MultiBlockHelper.INSTANCE.removeCore(getWorld(), getPos());
        super.onUnloaded();
    }

    public Map<BlockPos, IBlockState> provideStructure() {
        Map<BlockPos, IBlockState> states = new Object2ObjectLinkedOpenHashMap<>();
        int3 dir = new int3(this.getPos(), this.getFacing());

        int i;
        states.put(dir.up(1).asBlockPos(), brickState);


        states.put(dir.left(1).asBlockPos(), brickState);

        for(i = 0; i < 2; ++i) {
            states.put(dir.down(1).asBlockPos(), brickState);
        }

        states.put(dir.back(1).asBlockPos(), brickState);

        for(i = 0; i < 2; ++i) {
            states.put(dir.up(1).asBlockPos(), brickState);
        }

        states.put(dir.right(1).asBlockPos(), brickState);

        states.put(dir.down(2).asBlockPos(), brickState);

        states.put(dir.right(1).asBlockPos(), brickState);

        for(i = 0; i < 2; ++i) {
            states.put(dir.up(1).asBlockPos(), brickState);
        }

        states.put(dir.back(1).asBlockPos(), brickState);

        for(i = 0; i < 2; ++i) {
            states.put(dir.down(1).asBlockPos(), brickState);
        }

        states.put(dir.left(1).asBlockPos(), brickState);

        for(i = 0; i < 2; ++i) {
            states.put(dir.up(1).asBlockPos(), brickState);
        }

        states.put(dir.left(1).asBlockPos(), brickState);

        for(i = 0; i < 2; ++i) {
            states.put(dir.down(1).asBlockPos(), brickState);
        }

        states.put(dir.forward(2).right(1).asBlockPos(), brickState);
        states.put(dir.right(1).asBlockPos(), brickState);

        for(i = 0; i < 2; ++i) {
            states.put(dir.up(1).asBlockPos(), brickState);
        }

        return states;
    }

    public boolean checkStructure() {
        if (!world.isAreaLoaded(pos, 3)) {
            return false;
        }
        // we doing it "big math" style not block by block
        int3 dir = new int3(getPos(), getFacing());
        if (!(isBrick(dir.up(1)))) {
            return false;
        }
        if (!isBrick(dir.left(1))) {// left
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (!(isBrick(dir.down(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.back(1))) {// back
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (!(isBrick(dir.up(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.right(1))) {// right
            return false;
        }
        if (!world.isAirBlock(dir.down(1).asBlockPos())) {
            return false;
        }
        if (!(isBrick(dir.down(1)))) {
            return false;
        }
        if (!isBrick(dir.right(1))) {// right
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (!(isBrick(dir.up(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.back(1))) {// back
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (!(isBrick(dir.down(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.left(1))) {// left
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (!(isBrick(dir.up(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.left(1))) {// left
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (!(isBrick(dir.down(1)))) {
                return false;
            }
        }
        if (!isBrick(dir.forward(2).right(1))) {//missing block under controller
            return false;
        }
        if (!isBrick(dir.right(1))){// missing front right column
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (!(isBrick(dir.up(1)))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBlockBreak() {
        super.onBlockBreak();
        for (BlockPos pos : this.provideStructure().keySet()) {
            this.removeAllBricks(pos);
        }
    }

    public void removeAllBricks(BlockPos pos){
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof GTCXTileBrick){
            GTCXTileBrick brick = (GTCXTileBrick) tile;
            brick.setOwner(null);
        }
    }

    public boolean isBrick(int3 pos) {
        if (world.getBlockState(pos.asBlockPos()) == brickState){
            TileEntity tile = world.getTileEntity(pos.asBlockPos());
            if (tile instanceof GTCXTileBrick){
                GTCXTileBrick brick = (GTCXTileBrick) tile;
                if (brick.getOwner() == null || brick.getOwner() != this){
                    brick.setOwner(this);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean getStructureValid() {
        return lastState;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<ItemStack>();
        ItemStack machine = GTMaterialGen.get(GTCXBlocks.cokeOven);
        list.add(machine);

        list.addAll(getInventoryDrops());

        return list;
    }

    @Override
    public List<ItemStack> getInventoryDrops() {
        List<ItemStack> list = new ArrayList<>();
        for(int i = 0; i < 2; ++i) {
            ItemStack stack = this.inventory.get(i);
            if (!stack.isEmpty()) {
                list.add(stack);
            }
        }
        return list;
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
        return GTHelperFluid.doClickableFluidContainerThings(entityPlayer, enumHand, world, pos, this.tank);
    }

    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }
}
