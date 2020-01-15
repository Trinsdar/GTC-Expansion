package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXBlocks;
import gtc_expansion.container.GTCXContainerDustbin;
import gtc_expansion.util.GTCXDustbinFilter;
import gtclassic.api.helpers.int3;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.GTTileBaseRecolorableTile;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.classic.tile.IStackOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.inventory.transport.IItemTransporter;
import ic2.core.inventory.transport.TransporterManager;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.item.recipe.entry.RecipeInputOreDict;
import ic2.core.util.math.MathUtil;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static gtclassic.api.tile.GTTileBaseMachine.MOVE_CONTAINER_TAG;

public class GTCXTileDustbin extends GTTileBaseRecolorableTile implements IHasGui, ITickable {
    public static ResourceLocation TEXTURE = new ResourceLocation(GTCExpansion.MODID, "textures/gui/dustbin.png");
    public static final GTRecipeMultiInputList DUSTBIN_RECIPE_LIST = new GTRecipeMultiInputList("gt.dustbin", 0);
    public IFilter filter = new GTCXDustbinFilter(this);
    private static final int[] slotInputs = MathUtil.fromTo(0, 16);
    private static final int[] slotOutputs = MathUtil.fromTo(16, 32);
    public MultiRecipe lastRecipe;
    protected LinkedList<IStackOutput> outputs = new LinkedList<>();

    public GTCXTileDustbin() {
        super(32);
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.UP.invert());
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInputs);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutputs);
        handler.registerDefaultSlotsForSide(RotationList.DOWN.invert(), slotInputs);
        handler.registerDefaultSlotsForSide(RotationList.UP.invert(), slotOutputs);
        handler.registerInputFilter(filter, slotInputs);
        handler.registerSlotType(SlotType.Input, slotInputs);
        handler.registerSlotType(SlotType.Output, slotOutputs);
    }

    public ResourceLocation getGuiTexture(){
        return TEXTURE;
    }

    @Override
    public Block getBlockDrop() {
        return GTCXBlocks.locker;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerDustbin(entityPlayer.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GuiComponentContainer.class;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return !this.isInvalid();
    }

    @Override
    public boolean hasGui(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void update() {
        tryImportItems();
        handleRedstone();
        boolean noRoom;
        for (int i = 0; i < 16; i++){
            if (inventory.get(i).isEmpty() || inventory.get(i).getCount() < 4){
                continue;
            }
            noRoom = addToInventory();
            lastRecipe = getRecipe(i);
            boolean operate = (!noRoom && lastRecipe != null && lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE);
            if (operate){
                process(lastRecipe, i);
            }
        }
        tryExportItems();
        updateComparators();
    }

    public BlockPos getImportTilePos() {
        int3 dir = new int3(getPos(), getFacing());
        return dir.up(1).asBlockPos();
    }

    public BlockPos getExportTilePos() {
        int3 dir = new int3(getPos(), getFacing());
        return dir.down(1).asBlockPos();
    }

    public void tryImportItems() {
        if (world.isBlockLoaded(getImportTilePos())) {
            IItemTransporter slave = TransporterManager.manager.getTransporter(world.getTileEntity(getImportTilePos()), true);
            if (slave != null) {
                IItemTransporter controller = TransporterManager.manager.getTransporter(this, true);
                int limit = controller.getSizeInventory(getFacing());
                for (int i = 0; i < limit; ++i) {
                    ItemStack stack = slave.removeItem(CommonFilters.Anything, getFacing(), 1, false);
                    if (stack.isEmpty()) {
                        break;
                    }
                    ItemStack added = controller.addItem(stack, getFacing().getOpposite(), true);
                    if (added.getCount() <= 0) {
                        break;
                    }
                    slave.removeItem(CommonFilters.Anything, getFacing(), 1, true);
                }
            }
        }
    }

    public void tryExportItems() {
        if (world.isBlockLoaded(getExportTilePos())) {
            IItemTransporter slave = TransporterManager.manager.getTransporter(world.getTileEntity(getExportTilePos()), false);
            if (slave != null) {
                IItemTransporter controller = TransporterManager.manager.getTransporter(this, true);
                int limit = controller.getSizeInventory(getFacing());
                for (int i = 0; i < limit; ++i) {
                    ItemStack stack = controller.removeItem(CommonFilters.Anything, getFacing(), 1, false);
                    if (stack.isEmpty()) {
                        break;
                    }
                    ItemStack added = slave.addItem(stack, getFacing().getOpposite(), true);
                    if (added.getCount() <= 0) {
                        break;
                    }
                    controller.removeItem(CommonFilters.Anything, getFacing(), 1, true);
                }
            }
        }
    }

    public void process(MultiRecipe recipe, int slot) {
        MachineOutput output = recipe.getOutputs().copy();
        for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
            outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
        }
        NBTTagCompound nbt = recipe.getOutputs().getMetadata();
        boolean shiftContainers = nbt != null && nbt.getBoolean(MOVE_CONTAINER_TAG);
        for (IRecipeInput key : recipe.getInputs()) {
            int count = key.getAmount();
            ItemStack input = inventory.get(slot);
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
    }

    public boolean addToInventory() {
        if (outputs.isEmpty()) {
            return false;
        }
        Iterator<IStackOutput> iter = outputs.iterator();
        //noinspection Java8CollectionRemoveIf
        while (iter.hasNext()) {
            IStackOutput output = iter.next();
            if (output.addToInventory(this)) iter.remove();
        }
        return !outputs.isEmpty();
    }

    public MultiRecipe getRecipe(int slot) {
        if (lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            return null;
        }
        // Check if previous recipe is valid
        // Check if previous recipe is valid
        ItemStack input = inventory.get(slot);
        if (lastRecipe != null) {
            lastRecipe = checkRecipe(lastRecipe, input.copy()) ? lastRecipe : null;
        }
        // If previous is not valid, find a new one
        if (lastRecipe == null) {
            lastRecipe = DUSTBIN_RECIPE_LIST.getPriorityRecipe(new Predicate<MultiRecipe>() {

                @Override
                public boolean test(MultiRecipe t) {
                    return checkRecipe(t, input.copy());
                }
            });
        }
        // If no recipe is found, return
        if (lastRecipe == null) {
            return null;
        }
        int empty = 0;
        int[] outputSlots = getOutputSlots();
        for (int slot1 : outputSlots) {
            if (getStackInSlot(slot1).isEmpty()) {
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

    public boolean checkRecipe(MultiRecipe entry, ItemStack inputItem) {
        IRecipeInput recipeInput = entry.getInput(0);
        int toFind = recipeInput.getAmount();
        return !inputItem.isEmpty() && recipeInput.matches(inputItem) && inputItem.getCount() >= toFind;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        super.setStackInSlot(slot, stack);
        if (isSimulating() && isRecipeSlot(slot) && lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            lastRecipe = null;
        }
    }

    public int[] getInputSlots(){
        return slotInputs;
    };

    public IFilter[] getInputFilters(int[] slots){
        return new IFilter[]{ filter };
    };

    public boolean isRecipeSlot(int slot){
        return slot < 16;
    };

    public int[] getOutputSlots(){
        return slotOutputs;
    };

    public LinkedList<IStackOutput> getOutputs() {
        return outputs;
    }

    public boolean isValidInput(ItemStack par1) {
        return DUSTBIN_RECIPE_LIST.isValidRecipeInput(par1);
    }

    public static IRecipeInput input(ItemStack stack) {
        return new RecipeInputItemStack(stack);
    }

    public static IRecipeInput input(String name, int amount) {
        return new RecipeInputOreDict(name, amount);
    }

    public static void addTinyDustRecipe(String name, GTMaterial output){
        addTinyDustRecipe(name, GTMaterialGen.getDust(output, 1));
    }

    public static void addTinyDustRecipe(String name, ItemStack output){
        addRecipe("dustTiny" + name, 9, output);
    }

    public static void addSmallDustRecipe(String name, GTMaterial output){
        addSmallDustRecipe(name, GTMaterialGen.getDust(output, 1));
    }

    public static void addSmallDustRecipe(String name, ItemStack output){
        addRecipe("dustSmall" + name, 4, output);
    }

    private static void addRecipe(String input1, int amount1, ItemStack output) {
        List<IRecipeInput> inputs = new ArrayList<>();
        inputs.add(new RecipeInputOreDict(input1, amount1));
        addRecipe(inputs, new MachineOutput(null, output));
    }

    static void addRecipe(List<IRecipeInput> input, MachineOutput output) {
        DUSTBIN_RECIPE_LIST.addRecipe(input, output, output.getAllOutputs().get(0).getUnlocalizedName(), 4);
    }
}
