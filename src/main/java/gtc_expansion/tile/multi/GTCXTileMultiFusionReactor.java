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
import gtclassic.api.recipe.GTRecipeMultiInputList;
import gtclassic.api.recipe.GTRecipeMultiInputList.MultiRecipe;
import gtclassic.api.tile.multi.GTTileMultiBaseMachine;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTLang;
import gtclassic.common.tile.multi.GTTileMultiFusionReactor;
import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.tile.IStackOutput;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.IFilter;
import ic2.core.platform.lang.components.base.LocaleComp;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class GTCXTileMultiFusionReactor extends GTTileMultiBaseMachine implements IGTOwnerTile {
    static final IBlockState COIL_STATE = GTBlocks.casingFusion.getDefaultState();
    static final IBlockState CASING_STATE = GTCXBlocks.casingAdvanced.getDefaultState();
    static final IBlockState MATERIAL_INJECTOR_STATE = GTCXBlocks.fusionMaterialInjector.getDefaultState();
    static final IBlockState MATERIAL_EXTRACTOR_STATE = GTCXBlocks.fusionMaterialExtractor.getDefaultState();
    static final IBlockState ENERGY_INJECTOR_STATE = GTCXBlocks.fusionEnergyInjector.getDefaultState();
    static final IBlockState ENERGY_EXTRACTOR_STATE = GTCXBlocks.fusionEnergyExtractor.getDefaultState();
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(GTCExpansion.MODID, "textures/gui/fusioncomputer.png");
    public String status;
    private BlockPos input1;
    private BlockPos input2;
    private BlockPos output;
    private BlockPos energyOutput;
    private GTCXTileFusionMaterialInjector inputHatch1 = null;
    private GTCXTileFusionMaterialInjector inputHatch2 = null;
    private GTCXTileFusionMaterialExtractor outputHatch = null;
    private GTCXTileFusionEnergyExtractor energyOutputHatch = null;

    public GTCXTileMultiFusionReactor() {
        super(0, 0, 8192, 8192);
        this.maxEnergy = 100000000;
        this.status = "No";
        input1 = this.getPos();
        input2 = this.getPos();
        output = this.getPos();
        energyOutput = this.getPos();
        this.addGuiFields("status");
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
        return GTTileMultiFusionReactor.RECIPE_LIST;
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
            this.inputHatch1 = (GTCXTileFusionMaterialInjector) input1;
            this.inputHatch2 = (GTCXTileFusionMaterialInjector) input2;
            this.outputHatch = (GTCXTileFusionMaterialExtractor) output;
            this.energyOutputHatch = (GTCXTileFusionEnergyExtractor) energyOutput;
            if (inputHatch1.getOwner() == null){
                inputHatch1.setOwner(this);
            }
            if (inputHatch2.getOwner() == null){
                inputHatch2.setOwner(this);
            }
            if (outputHatch.getOwner() == null){
                outputHatch.setOwner(this);
            }
            super.update();
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
        return null;
    }

    public void onRecipeComplete() {
        if (this.lastRecipe != null && this.lastRecipe != GTRecipeMultiInputList.INVALID_RECIPE) {
            int rTime = this.lastRecipe.getOutputs().getMetadata().getInteger("RecipeTime") + 100;
            if (rTime < 3000) {
                return;
            }

            int euOutput = rTime * 32000;
            energyOutputHatch.addEnergy(euOutput);

        }

    }

    @Override
    public boolean addToInventory() {
        if (outputs.isEmpty()) {
            return false;
        }
        for (Iterator<IStackOutput> iter = outputs.iterator(); iter.hasNext();) {
            IStackOutput output = iter.next();
            if (output.addToInventory(outputHatch)) {
                iter.remove();
            }
        }
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
        this.status = nbt.getString("status");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        writeBlockPosToNBT(nbt, "input1", input1);
        writeBlockPosToNBT(nbt, "input2", input2);
        writeBlockPosToNBT(nbt, "output", output);
        writeBlockPosToNBT(nbt, "energyOutput", energyOutput);
        nbt.setString("status", this.status);
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

    int numInputs = 0;
    int numOutputs = 0;
    int numEnergyInputs = 0;
    int numEnergyOutputs = 0;

    @Override
    public boolean checkStructure() {
        int3 dir = new int3(this.getPos(), this.getFacing());
        this.status = "No";
        this.getNetwork().updateTileGuiField(this, "status");
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
        if (!isEnergyOutputHatch(dir.forward(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isEnergyOutputHatch(dir.right(1))){
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
        if (!isEnergyOutputHatch(dir.right(1))){
            return false;
        }
        if (!isCoil(dir.back(1))){
            return false;
        }
        if (!isEnergyOutputHatch(dir.back(1))){
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
        if (!isEnergyOutputHatch(dir.back(1))){
            return false;
        }
        if (!isCoil(dir.right(1))){
            return false;
        }
        if (!isEnergyOutputHatch(dir.right(1))){
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
        if (!isEnergyOutputHatch(dir.back(1))){
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
        if (numInputs < 2 || numOutputs < 1 || numEnergyInputs < 4 || numEnergyOutputs < 1){
            return false;
        }
        this.status = "Yes";
        this.getNetwork().updateTileGuiField(this, "status");
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
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    public boolean isEnergyOutputHatch(int3 pos){
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
            numEnergyOutputs++;
            return true;
        }
        return world.getBlockState(pos.asBlockPos()) == CASING_STATE;
    }

    @Override
    public void setShouldCheckRecipe(boolean checkRecipe) {
        this.shouldCheckRecipe = checkRecipe;
    }
}
