package gtc_expansion.tile.base;

import gtc_expansion.GTCExpansion;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import ic2.api.classic.audio.PositionSpec;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.classic.tile.IStackOutput;
import ic2.api.classic.tile.machine.IProgressMachine;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.IRecipeInput;
import ic2.core.IC2;
import ic2.core.audio.AudioSource;
import ic2.core.block.base.tile.TileEntityBasicElectricMachine;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.block.base.util.comparator.ComparatorManager;
import ic2.core.block.base.util.comparator.comparators.ComparatorProgress;
import ic2.core.block.base.util.info.ProgressInfo;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.base.IHasInventory;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.transport.wrapper.RangedInventoryWrapper;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.ITankListener;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static gtclassic.api.tile.GTTileBaseMachine.MOVE_CONTAINER_TAG;

public abstract class GTCXTileBaseSteamMachine extends TileEntityMachine implements ITickable, ITankListener, IProgressMachine, IHasGui, INetworkTileEntityEventListener {


    @NetworkField(index = 3)
    public IC2Tank steam = new IC2Tank(2000){
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return super.canFillFluidType(fluid) && fluid.isFluidEqual(GTMaterialGen.getFluidStack("steam", 1));
        }
    };
    @NetworkField(index = 4)
    public int progress = 0;
    @NetworkField(index = 6)
    public int maxProgress;
    @NetworkField(index = 7)
    public float soundLevel = 1F;
    // Current Usage & Time
    @NetworkField(index = 8)
    public int recipeOperation;
    public int steamPerTick;
    public boolean backObstructed;
    public GTRecipeMultiInputList.MultiRecipe lastRecipe;
    public boolean shouldCheckRecipe;
    public AudioSource audioSource;
    LinkedList<IStackOutput> outputs = new LinkedList<>();


    public GTCXTileBaseSteamMachine(int slots, int maxProgress, int steamPerTick) {
        super(slots);
        this.maxProgress = maxProgress;
        this.steamPerTick = steamPerTick;
        this.steam.setCanDrain(false);
        this.steam.addListener(this);
        shouldCheckRecipe = true;
        addNetworkFields("soundLevel", "steam");
        addGuiFields("recipeOperation", "progress", "steam", "maxProgress");
        addInfos(new ProgressInfo(this));
    }

    @Override
    public float getProgress() {
        return progress;
    }

    @Override
    public float getMaxProgress() {
        return recipeOperation;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

    @Override
    public boolean hasGui(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.getNetwork().updateTileGuiField(this, "steam");
    }

    @Override
    public void onBlockUpdate(Block block) {
        super.onBlockUpdate(block);
        this.backObstructed = !world.isAirBlock(this.pos.offset(this.getFacing().getOpposite()));
    }

    @Override
    public void update() {
        handleRedstone();
        boolean noRoom = addToInventory();
        if (shouldCheckRecipe) {
            lastRecipe = getRecipe();
            shouldCheckRecipe = false;
        }
        boolean canWork = this.canWork() && !noRoom;
        boolean operate = (canWork && lastRecipe != null && lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE);
        if (operate && this.steam.getFluidAmount() >= this.steamPerTick && !backObstructed) {
            if (!getActive()) {
                getNetwork().initiateTileEntityEvent(this, 0, false);
            }
            setActive(true);
            progress += 1;
            this.steam.drainInternal(this.steamPerTick, true);
            if (progress >= recipeOperation) {
                process(lastRecipe);
                getNetwork().initiateTileEntityEvent(this, 3, false);
                for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.offset(this.getFacing().getOpposite())))){
                    player.attackEntityFrom(DamageSource.GENERIC, 1);
                }
                progress = 0;
            }
            getNetwork().updateTileGuiField(this, "progress");
        } else {
            if (getActive()) {
                if (progress != 0) {
                    getNetwork().initiateTileEntityEvent(this, 1, false);
                } else {
                    getNetwork().initiateTileEntityEvent(this, 2, false);
                }
                this.setActive(false);
            }
            if (progress != 0) {
                progress = 0;
                getNetwork().updateTileGuiField(this, "progress");
            }
        }
        updateComparators();
    }

    public void process(GTRecipeMultiInputList.MultiRecipe recipe) {
        MachineOutput output = recipe.getOutputs().copy();
        for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
            outputs.add(new MultiSlotOutput(stack, getOutputSlots()));
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

    public boolean addToInventory() {
        if (outputs.isEmpty()) {
            return false;
        }
        for (Iterator<IStackOutput> iter = outputs.iterator(); iter.hasNext();) {
            IStackOutput output = iter.next();
            if (output.addToInventory(this)) {
                iter.remove();
            }
        }
        return !outputs.isEmpty();
    }

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

    public GTRecipeMultiInputList.MultiRecipe getRecipe() {
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
            lastRecipe = getRecipeList().getPriorityRecipe(new Predicate<GTRecipeMultiInputList.MultiRecipe>() {

                @Override
                public boolean test(GTRecipeMultiInputList.MultiRecipe t) {
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

    public boolean checkRecipe(GTRecipeMultiInputList.MultiRecipe entry, List<ItemStack> inputs) {
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(entry.getInputs());
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
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
    public void setStackInSlot(int slot, ItemStack stack) {
        super.setStackInSlot(slot, stack);
        shouldCheckRecipe = true;
        if (isSimulating() && isRecipeSlot(slot) && lastRecipe == GTRecipeMultiInputList.INVALID_RECIPE) {
            lastRecipe = null;
        }
    }

    public void applyRecipeEffect(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            if (recipeOperation != maxProgress) {
                recipeOperation = maxProgress;
                if (recipeOperation < 1) {
                    recipeOperation = 1;
                }
                getNetwork().updateTileGuiField(this, "recipeOperation");
            }
            return;
        }
        NBTTagCompound nbt = output.getMetadata();
        double progMod = nbt.hasKey("RecipeTimeModifier") ? nbt.getDouble("RecipeTimeModifier") : 1F;
        int newProgress = TileEntityBasicElectricMachine.applyModifier(maxProgress, nbt.getInteger("RecipeTime"), progMod);
        if (newProgress != recipeOperation) {
            recipeOperation = newProgress * 2;
            if (recipeOperation < 1) {
                recipeOperation = 1;
            }
            getNetwork().updateTileGuiField(this, "recipeOperation");
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        shouldCheckRecipe = true;
    }

    public void spawnBackSteam(){
        if (this.isActive){
            float f;
            float f3;
            float f4;
            float f5;
            int facing = this.getFacing().getIndex();
            for (int i = 0; i < 8; i++){
                f = (float)pos.getX();
                float f2 = (float)pos.getY() + 0.0F + world.rand.nextFloat() * 6.0F / 16.0F;
                f3 = (float)pos.getZ();
                f4 = 0.52F;
                f5 = world.rand.nextFloat() * 0.6F - 0.3F;
                double x = 0.0D;
                double y = 0.0D;
                double z = 0.0D;
                boolean spawn = false;
                if (facing == 2) {
                    x = f + f5;
                    y = f2;
                    z = f3 - f4;
                    spawn = true;
                } else if (facing == 3) {
                    x = f + f5;
                    y = f2;
                    z = f3 + f4;
                    spawn = true;
                } else if (facing == 4) {
                    x = f - f4;
                    y = f2;
                    z = f3 + f5;
                    spawn = true;
                } else if (facing == 5) {
                    x = f + f4;
                    y = f2;
                    z = f3 + f5;
                    spawn = true;
                }

                if (spawn) {
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y, z, 0.0D, 0.1D, 0.0D);
                }
            }
        }
    }

    public abstract int[] getInputSlots();

    public abstract IFilter[] getInputFilters(int[] slots);

    public abstract boolean isRecipeSlot(int slot);

    public abstract int[] getOutputSlots();

    public abstract GTRecipeMultiInputList getRecipeList();

    public boolean isValidInput(ItemStack par1) {
        return getRecipeList().isValidRecipeInput(par1);
    }

    public boolean canWork() {
        return true;
    }

    public IHasInventory getOutputInventory() {
        return new RangedInventoryWrapper(this, getOutputSlots());
    }

    public IHasInventory getInputInventory() {
        int[] input = getInputSlots();
        RangedInventoryWrapper result = new RangedInventoryWrapper(this, input).addFilters(getInputFilters(input));
        return result;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !isInvalid();
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    @Override
    public void onUnloaded() {
        if (this.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource.remove();
            this.audioSource = null;
        }
        super.onUnloaded();
    }

    public abstract ResourceLocation getStartSoundFile();

    public ResourceLocation getInterruptSoundFile() {
        return Ic2Sounds.interruptingSound;
    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource != null && this.audioSource.isRemoved()) {
            this.audioSource = null;
        }
        if (this.audioSource == null && this.getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, this.getStartSoundFile(), true, false, IC2.audioManager.defaultVolume
                    * this.soundLevel);
        }
        if (event == 0) {
            if (this.audioSource != null) {
                this.audioSource.play();
            }
        } else if (event == 1) {
            if (this.audioSource != null) {
                this.audioSource.stop();
                if (this.getInterruptSoundFile() != null) {
                    IC2.audioManager.playOnce(this, PositionSpec.Center, this.getInterruptSoundFile(), false, IC2.audioManager.defaultVolume
                            * this.soundLevel);
                }
            }
        } else if (event == 3){
            if (this.audioSource != null) {
                IC2.audioManager.playOnce(this, PositionSpec.Center, SoundEvents.BLOCK_FIRE_EXTINGUISH.getSoundName(), false, IC2.audioManager.defaultVolume
                        * this.soundLevel);
            }
            if (this.isRendering()){
                spawnBackSteam();
            }
        } else if (event == 2 && this.audioSource != null) {
            this.audioSource.stop();
        }
    }

    public int getSteamPerTick() {
        return steamPerTick;
    }

    public IC2Tank getSteam() {
        return steam;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.progress = nbt.getInteger("Progress");
        this.steam.readFromNBT(nbt.getCompoundTag("steam"));
        this.backObstructed = nbt.getBoolean("backObstructed");
        this.outputs.clear();
        NBTTagList list = nbt.getTagList("Results", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            this.outputs.add(new MultiSlotOutput(new ItemStack(data), this.getOutputSlots()));
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Progress", this.progress);
        this.steam.writeToNBT(this.getTag(nbt, "steam"));
        nbt.setBoolean("backObstructed", backObstructed);
        NBTTagList list = new NBTTagList();
        Iterator<IStackOutput> var3 = this.outputs.iterator();
        while (var3.hasNext()) {
            ItemStack item = var3.next().getStack();
            NBTTagCompound data = new NBTTagCompound();
            item.writeToNBT(data);
            list.appendTag(data);
        }
        nbt.setTag("Results", list);
        return nbt;
    }

    @Override
    protected void addComparators(ComparatorManager manager) {
        super.addComparators(manager);
        manager.addComparatorMode(new ComparatorProgress(this));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return super.hasCapability(capability, facing) || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.steam);
        }
        return super.getCapability(capability, facing);
    }
}
