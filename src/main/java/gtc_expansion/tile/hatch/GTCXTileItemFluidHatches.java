package gtc_expansion.tile.hatch;

import gtc_expansion.block.GTCXBlockHatch;
import gtc_expansion.container.GTCXContainerItemFluidHatch;
import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.data.GTCXLang;
import gtc_expansion.interfaces.IGTCasingBackgroundBlock;
import gtc_expansion.interfaces.IGTOwnerTile;
import gtc_expansion.interfaces.IGTScrewdriver;
import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import gtc_expansion.tile.multi.GTCXTileMultiThermalBoiler;
import gtc_expansion.util.GTCXTank;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTUtility;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.api.interfaces.IGTItemContainerTile;
import ic2.api.classic.audio.PositionSpec;
import ic2.api.classic.network.adv.NetworkField;
import ic2.core.IC2;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.inventory.transport.IItemTransporter;
import ic2.core.inventory.transport.TransporterManager;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GTCXTileItemFluidHatches extends TileEntityMachine implements ITankListener, ITickable, IClickable, IGTItemContainerTile, IHasGui, IGTCasingBackgroundBlock, IGTDebuggableTile, IFluidHandler {
    boolean input;
    @NetworkField(index = 3)
    protected final GTCXTank tank;
    private static final int slotInput = 0;
    private static final int slotOutput = 1;
    private static final int slotDisplay = 2;
    public static final String NBT_TANK = "tank";
    @NetworkField(
            index = 4
    )
    public int casing = 0;
    private int prevCasing = 0;

    @NetworkField(
            index = 5
    )
    public int config = 0;
    private int prevConfig = 0;
    public IGTOwnerTile owner = null;
    protected boolean second = false;
    public GTCXTileItemFluidHatches(boolean input) {
        super(3);
        this.input = input;
        this.tank = new GTCXTank(32000);
        this.tank.addListener(this);
        this.addGuiFields(NBT_TANK, "owner");
        this.addNetworkFields("casing", "config");
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInput);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutput);
        handler.registerDefaultSlotsForSide(RotationList.ALL, slotInput);
        handler.registerDefaultSlotsForSide(RotationList.ALL, slotOutput);
        handler.registerSlotType(SlotType.Input, slotInput);
        handler.registerSlotType(SlotType.Output, slotOutput);
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("casing") || field.equals("config")) {
            this.prevCasing = this.casing;
            this.prevConfig = this.config;
            this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
        }
    }

    @Override
    public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
        return this.getFacing() != facing;
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.getNetwork().updateTileGuiField(this, NBT_TANK);
        this.getNetwork().updateTileGuiField(this, "owner");
        this.inventory.set(slotDisplay, ItemDisplayIcon.createWithFluidStack(this.getTank().getFluid()));
        
    }

    @Override
    public void onBlockBreak() {
        if (this.owner != null){
            owner.invalidateStructure();
            owner.getTank(this).drainInternal(32000, true);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (owner != null){
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.owner instanceof GTCXTileMultiFusionReactor && owner.getFacing().getAxis() != EnumFacing.Axis.Y) {
                return owner.hasCapability(capability, input ? owner.getFacing().rotateY() : owner.getFacing().rotateYCCW());
            }
            return owner.hasCapability(capability, input ? EnumFacing.UP : EnumFacing.DOWN);
        } else {
            return super.hasCapability(capability, facing) || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (owner != null){
            return owner.getCapability(capability, facing, this);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.tank.readFromNBT(nbt.getCompoundTag(NBT_TANK));
        casing = nbt.getInteger("casing");
        config = nbt.getInteger("config");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.tank.writeToNBT(this.getTag(nbt, NBT_TANK));
        nbt.setInteger("casing", casing);
        nbt.setInteger("config", config);
        return nbt;
    }

    public void setSecond(boolean second) {
        this.second = second;
    }

    public boolean isSecond() {
        return second;
    }

    public boolean isInput() {
        return input;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<>();
        list.addAll(getInventoryDrops());
        return list;
    }

    @Override
    public List<ItemStack> getInventoryDrops() {
        List<ItemStack> list = new ArrayList<>();
        if (this.owner != null){
            int slot = owner.getInputSlot(this);
            list.add(owner.getStackInSlot(slot).copy());
            owner.setStackInSlot(slot, ItemStack.EMPTY);
            slot = owner.getOutputSlot(this);
            list.add(owner.getStackInSlot(slot).copy());
            owner.setStackInSlot(slot, ItemStack.EMPTY);
        }
        list.add(this.getStackInSlot(slotInput));
        list.add(this.getStackInSlot(slotOutput));
        return list;
    }

    @Override
    public boolean canRemoveBlock(EntityPlayer player) {
        return true;
    }

    @Override
    public void update() {
        inputOutputFromFacing();
        // commenting out for now as it won't work with the new system as is at least
        //GTHelperFluid.doFluidContainerThings(this, this.tank, slotInput, slotOutput);
    }

    public void inputOutputFromFacing(){
        if (input) {
            GTUtility.importFromSideIntoMachine(this, this.getFacing());
            GTUtility.importFluidFromSideToMachine(this, this.getTank(), this.getFacing(), 1000);
        } else {
            GTUtility.exportFromMachineToSide(this, this.getFacing(), slotOutput);
            GTUtility.exportFluidFromMachineToSide(this, this.getTank(), this.getFacing(), 1000);
        }
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing enumFacing, Side side) {
        return input ? GTHelperFluid.doClickableFluidContainerEmptyThings(player, hand, world, pos, this.getTank()) : GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, this.getTank());
    }

    @Override
    public boolean hasLeftClick() {
        return false;
    }

    @Override
    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        if (owner != null){
            return owner.getGuiContainer(entityPlayer, this);
        }
        return new GTCXContainerItemFluidHatch(entityPlayer.inventory, this);
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

    public GTCXTank getTank() {
        if (owner != null){
            return owner.getTank(this);
        }
        return tank;
    }

    public ItemStack getOutput(){
        return this.getStackInSlot(slotOutput);
    }

    public ItemStack getInput(){
        return this.getStackInSlot(slotInput);
    }

    public void setOwner(IGTOwnerTile tile){
        this.owner = tile;
    }

    public IGTOwnerTile getOwner(){
        return owner;
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        Block block = fromCasing(casing);
        map.put("Casing: " + (block == Blocks.AIR ? "None" : block.getLocalizedName()), true);
        map.put("Config: "+ config, true);
        boolean hasOwner = owner != null;
        map.put("Has owner: " + hasOwner, true);
        map.put("Is Second : " + second, true);
        map.put("Tank: " + getTank().toString(), true);
        map.put("Fluid in tank: " + (getTank().getFluid() != null ? getTank().getFluid().getLocalizedName() : "Empty"), true);
    }

    public Block fromCasing(int casing){
        switch (casing){
            case 1: return GTCXBlocks.casingStandard;
            case 2: return GTCXBlocks.casingReinforced;
            case 3: return GTCXBlocks.casingAdvanced;
            default: return Blocks.AIR;
        }
    }

    @Override
    public int getCasing(){
        return casing;
    }

    @Override
    public void setCasing(){
        int standard = 0;
        int reinforced = 0;
        int advanced = 0;
        int hatches = 0;
        for (EnumFacing facing : EnumFacing.VALUES){
            BlockPos offset = this.getPos().offset(facing);
            Block block = world.getBlockState(offset).getBlock();
            if (block == GTCXBlocks.casingStandard){
                standard++;
            } else if (block == GTCXBlocks.casingReinforced){
                reinforced++;
            } else if (block == GTCXBlocks.casingAdvanced){
                advanced++;
            } else if (block instanceof GTCXBlockHatch){
                hatches++;
            }
        }
        if (standard > 3 || (standard == 3 && hatches == 1)){
            casing = 1;
        } else if (reinforced > 3 || (reinforced == 3 && hatches == 1)){
            casing = 2;
        } else if (advanced > 3 || (advanced == 3 && hatches == 1)){
            casing = 3;
        } else {
            casing = 0;
        }
        if (casing != this.prevCasing) {
            world.notifyNeighborsOfStateChange(pos, world.getBlockState(this.getPos()).getBlock(), true);
            this.getNetwork().updateTileEntityField(this, "casing");
        }
        this.prevCasing = casing;
    }

    @Override
    public int getConfig(){
        return config;
    }

    @Override
    public void setConfig(){
        Block block = fromCasing(casing);
        config = 0;
        for (EnumFacing facing : EnumFacing.values()){
            boolean hasBlock = (world.getBlockState(pos.offset(facing)).getBlock() == block || isHatchWithCasing(pos.offset(facing))) && block != Blocks.AIR;
            if (hasBlock){
                config += 1 << facing.getIndex();
            }
        }
        if (config != this.prevConfig) {
            this.getNetwork().updateTileEntityField(this, "config");
        }

        this.prevConfig = config;
    }

    @Override
    public void onBlockUpdate(Block block) {
        super.onBlockUpdate(block);
        this.setConfig();
    }

    public boolean isHatchWithCasing(BlockPos pos){
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IGTCasingBackgroundBlock){
            return ((IGTCasingBackgroundBlock)tile).getCasing() == casing;
        }
        return false;
    }

    @Override
    public EnumFacing getFacing(){
        return super.getFacing();
    }

    @Override
    public boolean getActive(){
        return super.getActive();
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return this.getTank().getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return this.getTank().fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return this.getTank().drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return this.getTank().drain(maxDrain, doDrain);
    }

    public static class GTCXTileInputHatch extends GTCXTileItemFluidHatches{

        public GTCXTileInputHatch() {
            super(true);
        }
    }

    public static class GTCXTileOutputHatch extends GTCXTileItemFluidHatches implements IGTDebuggableTile{
        OutputModes cycle = OutputModes.ITEM_AND_FLUID;

        public GTCXTileOutputHatch() {
            super(false);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            super.writeToNBT(nbt);
            nbt.setBoolean("SupportsFluidOutput", cycle.isFluid());
            nbt.setBoolean("SupportsItemOutput", cycle.isItem());
            return nbt;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            super.readFromNBT(nbt);
            boolean fluid = nbt.getBoolean("SupportsFluidOutput");
            boolean item = nbt.getBoolean("SupportsItemOutput");
            cycle = cycle.fromBool(item, fluid);
        }

        @Override
        public void inputOutputFromFacing(){
            if (cycle.isFluid()){
                GTUtility.exportFluidFromMachineToSide(this, this.getTank(), this.getFacing(), 1000);
            }
            if (cycle.isItem()){
                if (this.owner != null && (owner instanceof GTCXTileMultiThermalBoiler || owner instanceof GTCXTileMultiFusionReactor)){
                    int slot = owner.getOutputSlot(this);
                    exportFromMachineToSide((TileEntityMachine) owner, this.getPos(), this.getFacing(), slot);
                } else {
                    if (!this.getStackInSlot(slotOutput).isEmpty()){
                        GTUtility.exportFromMachineToSide(this, this.getFacing(), slotOutput);
                    }
                }

            }
        }

        public static void exportFromMachineToSide(TileEntityMachine machine, BlockPos original, EnumFacing side, int... slots) {
            BlockPos exportPos = original.offset(side);
            if (machine.getWorld().isBlockLoaded(exportPos)) {
                IItemTransporter slave = TransporterManager.manager.getTransporter(machine.getWorld().getTileEntity(exportPos), false);
                if (slave != null) {
                    for (int i : slots) {
                        int added = slave.addItem(machine.getStackInSlot(i).copy(), side.getOpposite(), true).getCount();
                        if (added > 0) {
                            machine.getStackInSlot(i).shrink(added);
                            break;
                        }
                    }
                }

            }
        }

        public OutputModes getCycle() {
            return cycle;
        }

        public void cycleModes(EntityPlayer player){
            this.cycle = cycle.cycle(player);
            if (this.owner != null){
                this.owner.setOutputModes(second, cycle);
            }
        }

        public enum OutputModes{
            ITEM_AND_FLUID(true, true),
            ITEM_ONLY(true, false),
            FLUID_ONLY(false, true),
            NEITHER(false, false);

            boolean item;
            boolean fluid;
            OutputModes(boolean item, boolean fluid){
                this.item = item;
                this.fluid = fluid;
            }

            OutputModes fromBool(boolean item, boolean fluid){
                if (item && fluid){
                    return ITEM_AND_FLUID;
                } else if (item){
                    return ITEM_ONLY;
                } else if (fluid){
                    return FLUID_ONLY;
                } else {
                    return NEITHER;
                }
            }

            OutputModes cycle(EntityPlayer player){
                if (this == ITEM_AND_FLUID){
                    IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_0);
                    return ITEM_ONLY;
                } else if (this == ITEM_ONLY){
                    IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_1);
                    return FLUID_ONLY;
                } else if (this == FLUID_ONLY){
                    IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_2);
                    return NEITHER;
                } else {
                    IC2.platform.messagePlayer(player, GTCXLang.MESSAGE_HATCH_MODE_3);
                    return ITEM_AND_FLUID;
                }
            }

            public boolean isFluid() {
                return fluid;
            }

            public boolean isItem() {
                return item;
            }
        }

        @Override
        public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
            ItemStack stack = entityPlayer.getHeldItem(enumHand);
            if (stack.getItem() instanceof IGTScrewdriver && side.isServer()){
                this.cycleModes(entityPlayer);
                ((IGTScrewdriver)stack.getItem()).damage(stack, entityPlayer);
                IC2.audioManager.playOnce(entityPlayer, PositionSpec.Hand, Ic2Sounds.wrenchUse, true, IC2.audioManager.defaultVolume);
                return true;
            }
            return super.onRightClick(entityPlayer, enumHand, enumFacing, side);
        }

        @Override
        public void getData(Map<String, Boolean> data){
            data.put(cycle.toString(), false);
            super.getData(data);
        }
    }

    public static class GTCXTileFusionMaterialInjector extends GTCXTileInputHatch{

        public GTCXTileFusionMaterialInjector() {
            super();
        }

        @Override
        public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
            return this.getFacing() != facing && facing.getAxis().isHorizontal();
        }
    }

    public static class GTCXTileFusionMaterialExtractor extends GTCXTileOutputHatch{

        public GTCXTileFusionMaterialExtractor() {
            super();
        }

        @Override
        public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
            return this.getFacing() != facing && facing.getAxis().isHorizontal();
        }
    }
}
