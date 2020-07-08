package gtc_expansion.tile.multi;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXMachineGui.GTCXFusionComputerGui;
import gtc_expansion.container.GTCXContainerFusionReactor;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtc_expansion.tile.hatch.GTCXTileEnergyOutputHatch.GTCXTileFusionEnergyExtractor;
import gtc_expansion.tile.hatch.GTCXTileFusionEnergyInjector;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileFusionMaterialExtractor;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches.GTCXTileFusionMaterialInjector;
import gtclassic.api.helpers.int3;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialElement;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.api.recipe.GTFluidMachineOutput;
import gtclassic.api.recipe.GTRecipeMachineHandler;
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.multi.GTTileMultiBaseMachine;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTLang;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.recipe.RecipeModifierHelpers.IRecipeModifier;
import ic2.api.classic.recipe.crafting.RecipeInputFluid;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.base.util.output.MultiSlotOutput;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.misc.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static gtclassic.common.tile.multi.GTTileMultiFusionReactor.RECIPE_LIST;

public class GTCXTileMultiFusionReactor extends GTTileMultiBaseMachine implements IGTOwnerTile {
    static final IBlockState COIL_STATE = GTBlocks.casingFusion.getDefaultState();
    static final IBlockState CASING_STATE = GTCXBlocks.casingAdvanced.getDefaultState();
    static final IBlockState MATERIAL_INJECTOR_STATE = GTCXBlocks.fusionMaterialInjector.getDefaultState();
    static final IBlockState MATERIAL_EXTRACTOR_STATE = GTCXBlocks.fusionMaterialExtractor.getDefaultState();
    static final IBlockState ENERGY_INJECTOR_STATE = GTCXBlocks.fusionEnergyInjector.getDefaultState();
    static final IBlockState ENERGY_EXTRACTOR_STATE = GTCXBlocks.fusionEnergyExtractor.getDefaultState();
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/fusioncomputer.png");
    public static final String START_EU = "startEu";
    public boolean usedStartEnergy = false;
    private BlockPos input1;
    private BlockPos input2;
    private BlockPos output;
    private BlockPos energyOutput;
    private List<BlockPos> energyInputList = new ArrayList<>();
    private GTCXTileFusionMaterialInjector inputHatch1 = null;
    private GTCXTileFusionMaterialInjector inputHatch2 = null;
    private GTCXTileFusionMaterialExtractor outputHatch = null;
    private GTCXTileFusionEnergyExtractor energyOutputHatch = null;

    public GTCXTileMultiFusionReactor() {
        super(0, 0, 8192, 8192);
        this.maxEnergy = 1000000;
        input1 = this.getPos();
        input2 = this.getPos();
        output = this.getPos();
        energyOutput = this.getPos();
    }

    public void addMaxEnergy(int add){
        this.maxEnergy += add;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTLang.FUSION_REACTOR;
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new GTCXContainerFusionReactor(player.inventory, this);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
        return GTCXFusionComputerGui.class;
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public IFilter[] getInputFilters(int[] slots) {
        return new IFilter[0];
    }

    @Override
    public boolean isRecipeSlot(int slot) {
        return false;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return null;
    }

    @Override
    public ResourceLocation getStartSoundFile() {
        return Ic2Sounds.compressorOp;
    }

    @Override
    public GTRecipeMultiInputList getRecipeList() {
        return RECIPE_LIST;
    }

    public ResourceLocation getGuiTexture() {
        return GUI_LOCATION;
    }

    @Override
    public void update() {
        TileEntity input1 = world.getTileEntity(this.input1);
        TileEntity input2 = world.getTileEntity(this.input2);
        TileEntity output = world.getTileEntity(this.output);
        TileEntity energyOutput = world.getTileEntity(this.energyOutput);
        boolean hasHatches = this.canWork() && input1 instanceof GTCXTileFusionMaterialInjector && input2 instanceof GTCXTileFusionMaterialInjector && output instanceof GTCXTileFusionMaterialExtractor && energyOutput instanceof GTCXTileFusionEnergyExtractor;
        if (hasHatches){
            if (this.inputHatch1 != input1){
                this.inputHatch1 = (GTCXTileFusionMaterialInjector) input1;
            }
            if (this.inputHatch2 != input2){
                this.inputHatch2 = (GTCXTileFusionMaterialInjector) input2;
            }
            if (this.outputHatch != output){
                this.outputHatch = (GTCXTileFusionMaterialExtractor) output;
            }
            if (this.energyOutputHatch !=  energyOutput){
                this.energyOutputHatch = (GTCXTileFusionEnergyExtractor) energyOutput;
            }
            if (inputHatch1.getOwner() == null){
                inputHatch1.setOwner(this);
            }
            if (inputHatch2.getOwner() == null){
                inputHatch2.setOwner(this);
            }
            if (outputHatch.getOwner() == null){
                outputHatch.setOwner(this);
            }
            handleRedstone();
            updateNeighbors();
            boolean noRoom = addToInventory();
            if (shouldCheckRecipe) {
                lastRecipe = getRecipe();
                shouldCheckRecipe = false;
            }
            boolean operate = (!noRoom && lastRecipe != null && lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE);
            if (operate && canContinue() && energy >= energyConsume) {
                int neededStartEu = getStartEu(lastRecipe.getOutputs());
                if (usedStartEnergy || energy >= neededStartEu + energyConsume || neededStartEu == 0){
                    if (!usedStartEnergy){
                        this.useEnergy(neededStartEu);
                        usedStartEnergy = true;
                    }
                    if (!getActive()) {
                        getNetwork().initiateTileEntityEvent(this, 0, false);
                    }
                    setActive(true);
                    progress += progressPerTick;
                    useEnergy(recipeEnergy);
                    if (progress >= recipeOperation) {
                        process(lastRecipe);
                        progress = 0;
                        notifyNeighbors();
                    }
                    getNetwork().updateTileGuiField(this, "progress");
                }
            } else {
                if (getActive()) {
                    if (progress != 0) {
                        getNetwork().initiateTileEntityEvent(this, 1, false);
                    } else {
                        getNetwork().initiateTileEntityEvent(this, 2, false);
                    }
                }
                if (progress != 0) {
                    progress = 0;
                    usedStartEnergy = false;
                    getNetwork().updateTileGuiField(this, "progress");
                }
                setActive(false);
            }
            if (supportsUpgrades) {
                for (int i = 0; i < upgradeSlots; i++) {
                    ItemStack item = inventory.get(i + inventory.size() - upgradeSlots);
                    if (item.getItem() instanceof IMachineUpgradeItem) {
                        ((IMachineUpgradeItem) item.getItem()).onTick(item, this);
                    }
                }
            }
            updateComparators();
        } else {
            if (!(input1 instanceof GTCXTileFusionMaterialInjector)){
                this.inputHatch1 = null;
            }
            if (!(input2 instanceof GTCXTileFusionMaterialInjector)){
                this.inputHatch2 = null;
            }
            if (!(output instanceof GTCXTileFusionMaterialExtractor)){
                this.outputHatch = null;
            }
            if (!(energyOutput instanceof GTCXTileFusionEnergyExtractor)){
                this.energyOutputHatch = null;
            }
        }
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
                usedStartEnergy = false;
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
        if (outputs instanceof GTFluidMachineOutput){
            GTFluidMachineOutput fluidOutputs = (GTFluidMachineOutput) outputs;
            if (outputHatch.getTank().getFluidAmount() == 0){
                return lastRecipe;
            }
            FluidStack fluid = outputHatch.getTank().getFluid();
            for (FluidStack output : fluidOutputs.getFluids()){
                if (output.isFluidEqual(fluid) && outputHatch.getTank().getFluidAmount() + 1000 <= outputHatch.getTank().getCapacity()){
                    return lastRecipe;
                }
            }
        } else {
            ItemStack outputStack = outputHatch.getOutput();
            if (outputStack.isEmpty()){
                return lastRecipe;
            }
            for (ItemStack output : lastRecipe.getOutputs().getAllOutputs()) {
                if (StackUtil.isStackEqual(outputStack, output, false, true)) {
                    if (outputStack.getCount()
                            + output.getCount() <= outputStack.getMaxStackSize()) {
                        return lastRecipe;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean checkRecipe(MultiRecipe entry, List<ItemStack> inputs) {
        FluidStack fluidInput1 = inputHatch1.getTank().getFluid();
        FluidStack fluidInput2 = inputHatch2.getTank().getFluid();
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(entry.getInputs());
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
            if (key instanceof RecipeInputFluid){
                FluidStack fluidStack = ((RecipeInputFluid)key).fluid;
                if (fluidInput1 != null && fluidInput1.isFluidEqual(fluidStack) && fluidInput1.amount >= 1000){
                    keyIter.remove();
                    continue;
                }
                if (fluidInput2 != null && fluidInput2.isFluidEqual(fluidStack) && fluidInput2.amount >= 1000){
                    keyIter.remove();
                    continue;
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
    public void process(MultiRecipe recipe) {
        MachineOutput output = recipe.getOutputs().copy();
        inputHatch1.skip5Ticks();
        inputHatch2.skip5Ticks();
        outputHatch.skip5Ticks();
        if (output instanceof GTFluidMachineOutput){
            for (FluidStack fluid : ((GTFluidMachineOutput)output).getFluids()){
                outputHatch.getTank().fillInternal(fluid, true);
                onRecipeComplete();
            }
        } else {
            for (ItemStack stack : output.getRecipeOutput(getWorld().rand, getTileData())) {
                outputs.add(new MultiSlotOutput(stack, 1));
                onRecipeComplete();
            }
        }

        NBTTagCompound nbt = recipe.getOutputs().getMetadata();
        boolean shiftContainers = nbt != null && nbt.getBoolean(MOVE_CONTAINER_TAG);
        List<ItemStack> inputs = getInputs();
        List<IRecipeInput> recipeKeys = new LinkedList<IRecipeInput>(recipe.getInputs());
        for (Iterator<IRecipeInput> keyIter = recipeKeys.iterator(); keyIter.hasNext();) {
            IRecipeInput key = keyIter.next();
            if (key instanceof RecipeInputFluid){
                FluidStack fluidStack = ((RecipeInputFluid)key).fluid;
                if (inputHatch1.getTank().getFluid() != null && inputHatch1.getTank().getFluid().isFluidEqual(fluidStack)){
                    inputHatch1.getTank().drainInternal(1000, true);
                    keyIter.remove();
                    continue;
                }
                if (inputHatch2.getTank().getFluid() != null && inputHatch2.getTank().getFluid().isFluidEqual(fluidStack)){
                    inputHatch2.getTank().drainInternal(1000, true);
                    keyIter.remove();
                    continue;
                }
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
        shouldCheckRecipe = true;
    }

    @Override
    public void onRecipeComplete() {
        if (this.lastRecipe != null && this.lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE) {
            int startEu = this.lastRecipe.getOutputs().getMetadata().getInteger(START_EU);
            if (startEu != 40000000 && startEu != 60000000) {
                return;
            }
            int rTime = lastRecipe.getOutputs().getMetadata().getInteger("RecipeTime") + 100;
            int euOutput = startEu == 40000000 ? rTime * 60000 : rTime * 62000;
            energyOutputHatch.addEnergy(euOutput);

        }

    }

    @Override
    public boolean addToInventory() {
        if (outputs.isEmpty()) {
            return false;
        }
        outputs.removeIf(output -> output.addToInventory(outputHatch));
        return !outputs.isEmpty();
    }

    public List<ItemStack> getInputs() {
        ArrayList<ItemStack> inputs = new ArrayList<>();
        if (!inputHatch1.getInput().isEmpty()){
            inputs.add(inputHatch1.getInput());
        }
        if (!inputHatch2.getInput().isEmpty()){
            inputs.add(inputHatch2.getInput());
        }
        return inputs;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.input1 = readBlockPosFromNBT(nbt, "input1");
        this.input2 = readBlockPosFromNBT(nbt, "input2");
        this.output = readBlockPosFromNBT(nbt, "output");
        this.energyOutput = readBlockPosFromNBT(nbt, "energyOutput");
        this.usedStartEnergy = nbt.getBoolean("usedStartEnergy");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        writeBlockPosToNBT(nbt, "input1", input1);
        writeBlockPosToNBT(nbt, "input2", input2);
        writeBlockPosToNBT(nbt, "output", output);
        writeBlockPosToNBT(nbt, "energyOutput", energyOutput);
        nbt.setBoolean("usedStartEnergy", usedStartEnergy);
        return nbt;
    }

    public BlockPos readBlockPosFromNBT(NBTTagCompound nbt, String id){
        NBTTagCompound compound = nbt.getCompoundTag(id);
        int x = compound.getInteger("X");
        int y = compound.getInteger("Y");
        int z = compound.getInteger("Z");
        return new BlockPos(x, y, z);
    }

    public void writeBlockPosToNBT(NBTTagCompound nbt, String id, BlockPos pos){
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("X", pos.getX());
        compound.setInteger("Y", pos.getY());
        compound.setInteger("Z", pos.getZ());
        nbt.setTag(id, compound);
    }

    public static void postInit() {
        RECIPE_LIST.startMassChange();
        for (MultiRecipe recipe : RECIPE_LIST.getRecipeList()){
            GTRecipeMachineHandler.removeRecipe(RECIPE_LIST, recipe.getRecipeID());
        }
        RECIPE_LIST.finishMassChange();
        /** Just regular recipes added manually **/
        addRecipe(new RecipeInputFluid(GTMaterialGen.getFluidStack(GTMaterial.Deuterium)),
                new RecipeInputFluid(GTMaterialGen.getFluidStack(GTMaterial.Tritium)), totalEu(2097152), 40000000, GTMaterialGen.getFluidStack(GTMaterial.Helium));
        addRecipe(new RecipeInputFluid(GTMaterialGen.getFluidStack(GTMaterial.Deuterium)),
                new RecipeInputFluid(GTMaterialGen.getFluidStack(GTMaterial.Helium3)), totalEu(2097152), 60000000, GTMaterialGen.getFluidStack(GTMaterial.Helium));
        /** This iterates the element objects to create all Fusion recipes **/
        Set<Integer> usedInputs = new HashSet<>();
        for (GTMaterialElement sum : GTMaterialElement.getElementList()) {
            for (GTMaterialElement input1 : GTMaterialElement.getElementList()) {
                for (GTMaterialElement input2 : GTMaterialElement.getElementList()) {
                    int hash = input1.hashCode() + input2.hashCode();
                    if ((input1.getNumber() + input2.getNumber() == sum.getNumber()) && input1 != input2
                            && !usedInputs.contains(hash)) {
                        float ratio = (sum.getNumber() / 100.0F) * 7000000.0F;
                        int startEu = sum.getNumber() == GTMaterial.Iridium.getElementNumber() ? 120000000 : 0;
                        IRecipeInput recipeInput1 = input1.getInput();
                        IRecipeInput recipeInput2 = input2.getInput();
                        if (sum.isFluid()){
                            addRecipe(recipeInput1,
                                    recipeInput2, totalEu(Math.round(ratio)), startEu, ((RecipeInputFluid)sum.getInput()).fluid);
                        } else {
                            addRecipe(recipeInput1,
                                    recipeInput2, totalEu(Math.round(ratio)), startEu, sum.getOutput());
                        }
                        usedInputs.add(hash);

                    }
                }
            }
        }
        addRecipe(input(GTMaterialGen.getIc2(Ic2Items.uuMatter, 10)),
                input(GTMaterialGen.getIc2(Ic2Items.emptyCell, 1)), totalEu(10000000),100000000, GTMaterialGen.getIc2(Ic2Items.plasmaCell, 1));
    }

    public static int getStartEu(MachineOutput output) {
        if (output == null || output.getMetadata() == null || !output.getMetadata().hasKey(START_EU)) {
            return 0;
        }
        return output.getMetadata().getInteger(START_EU);
    }

    public static IRecipeModifier[] totalEu(int total) {
        return GTRecipeMachineHandler.totalEu(RECIPE_LIST, total);
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, IRecipeModifier[] modifiers, int startEu, ItemStack output){
        List<IRecipeInput> inlist = new ArrayList<>();
        inlist.add(input1);
        inlist.add(input2);
        NBTTagCompound mods = new NBTTagCompound();
        if (modifiers != null) {
            for (IRecipeModifier modifier : modifiers) {
                modifier.apply(mods);
            }
        }
        mods.setInteger(START_EU, startEu);
        RECIPE_LIST.addRecipe(inlist, new MachineOutput(mods, output), output.getUnlocalizedName(), 8192);
    }

    public static void addRecipe(IRecipeInput input1, IRecipeInput input2, IRecipeModifier[] modifiers, int startEu, FluidStack output){
        List<IRecipeInput> inlist = new ArrayList<>();
        List<FluidStack> outList = new ArrayList<>();
        inlist.add(input1);
        inlist.add(input2);
        outList.add(output);
        NBTTagCompound mods = new NBTTagCompound();
        if (modifiers != null) {
            for (IRecipeModifier modifier : modifiers) {
                modifier.apply(mods);
            }
        }
        mods.setInteger(START_EU, startEu);
        RECIPE_LIST.addRecipe(inlist, new GTFluidMachineOutput(mods, outList), output.getUnlocalizedName(), 8192);
    }

    public void removeTilesWithOwners(){
        if (inputHatch1 != null && inputHatch1.getOwner() == this){
            inputHatch1.setOwner(null);
        }
        if (inputHatch2 != null && inputHatch2.getOwner() == this){
            inputHatch2.setOwner(null);
        }
        if (outputHatch != null && outputHatch.getOwner() == this){
            outputHatch.setOwner(null);
        }
        for (BlockPos blockPos : energyInputList){
            TileEntity tile = world.getTileEntity(blockPos);
            if (tile instanceof GTCXTileFusionEnergyInjector && ((GTCXTileFusionEnergyInjector)tile).getAccept() == this){
                ((GTCXTileFusionEnergyInjector)tile).setAccept(null);
            }
        }
    }

    int numInputs = 0;
    int numOutputs = 0;
    int numEnergyOutputs = 0;

    @Override
    public boolean checkStructure() {
        energyInputList.clear();
        numInputs = 0;
        numOutputs = 0;
        numEnergyOutputs = 0;
        input1 = this.getPos();
        input2 = this.getPos();
        output = this.getPos();
        energyOutput = this.getPos();
        int3 dir = new int3(this.getPos(), this.getFacing());
        //top section
        if (!isCasing(dir.up(1).back(1))){
            return false;
        }
        if (!isInputHatch(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isInputHatch(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isInputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isInputHatch(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isInputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isInputHatch(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isInputHatch(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isInputHatch(dir.back(1).right(1))){
            return false;
        }

        // middle section
        if (!isOutputHatch(dir.forward(1).down(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isOutputHatch(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }

        if (!isEnergyInputHatch(dir.left(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.left(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.right(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.forward(1).right(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.forward(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.back(1).right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1).right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isOutputHatch(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1).back(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isOutputHatch(dir.right(1).back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isOutputHatch(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isOutputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.left(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.left(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.right(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.back(1).left(1))){
            return false;
        }
        if (!isCasing(dir.back(1).left(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isEnergyInputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isOutputHatch(dir.back(1).left(1))){
            return false;
        }
        if (!isCoil(dir.forward(1))){
            return false;
        }
        if (!isOutputHatch(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }

        // bottom section
        if (!isCasing(dir.down(1).forward(1))){
            return false;
        }
        if (!isInputHatch(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isInputHatch(dir.right(1).forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isInputHatch(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isInputHatch(dir.forward(1).left(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isInputHatch(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.left(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isInputHatch(dir.left(1).back(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isInputHatch(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.back(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.back(1).right(1))){
            return false;
        }
        if (!isCasing(dir.right(1))){
            return false;
        }
        if (!isInputHatch(dir.back(1).right(1))){
            return false;
        }
        if (numInputs < 2 || numOutputs < 1 || energyInputList.size() < 4 || numEnergyOutputs < 1){
            return false;
        }
        return true;
    }

    public boolean isCasing(int3 pos){
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    public boolean isCoil(int3 pos) {
        return this.world.getBlockState(pos.asBlockPos()) == COIL_STATE;
    }

    public boolean isInputHatch(int3 pos) {
        if (world.getBlockState(pos.asBlockPos()) == MATERIAL_INJECTOR_STATE){
            if (world.getBlockState(input1) != MATERIAL_INJECTOR_STATE){
                input1 = pos.asBlockPos();
            } else if (world.getBlockState(input2) != MATERIAL_INJECTOR_STATE){
                input2 = pos.asBlockPos();
            }
            numInputs++;
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    public boolean isOutputHatch(int3 pos){
        if (world.getBlockState(pos.asBlockPos()) == MATERIAL_EXTRACTOR_STATE){
            if (world.getBlockState(output) != MATERIAL_EXTRACTOR_STATE){
                output = pos.asBlockPos();
            }
            numOutputs++;
            return true;
        }
        if (world.getBlockState(pos.asBlockPos()) == ENERGY_EXTRACTOR_STATE){
            if (world.getBlockState(energyOutput) != ENERGY_EXTRACTOR_STATE){
                energyOutput = pos.asBlockPos();
            }
            numEnergyOutputs++;
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    public boolean isEnergyInputHatch(int3 pos){
        TileEntity tile = world.getTileEntity(pos.asBlockPos());
        if (world.getBlockState(pos.asBlockPos()) == ENERGY_INJECTOR_STATE && tile instanceof GTCXTileFusionEnergyInjector){
            if (((GTCXTileFusionEnergyInjector)tile).getAccept() == null){
                ((GTCXTileFusionEnergyInjector)tile).setAccept(this);
            }
            energyInputList.add(pos.asBlockPos());
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    @Override
    public void setShouldCheckRecipe(boolean checkRecipe) {
        this.shouldCheckRecipe = checkRecipe;
    }
}
