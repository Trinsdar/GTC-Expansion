package gtc_expansion.tile.hatch;

import gtc_expansion.container.GTCXContainerItemFluidHatch;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTUtility;
import gtclassic.api.interfaces.IGTItemContainerTile;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.fluid.IC2Tank;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.item.misc.ItemDisplayIcon;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public abstract class GTCXTileItemFluidHatches extends TileEntityMachine implements ITankListener, ITickable, IClickable, IGTItemContainerTile, IHasGui {
    boolean input;
    private IC2Tank tank;
    private static final int slotInput = 0;
    private static final int slotOutput = 1;
    private static final int slotDisplay = 2;
    public static final String NBT_TANK = "tank";
    public GTCXTileItemFluidHatches(boolean input) {
        super(3);
        this.input = input;
        this.tank = new IC2Tank(32000);
        this.tank.addListener(this);
        this.addGuiFields(NBT_TANK);
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
    public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
        return this.getFacing() != facing;
    }

    @Override
    public void onTankChanged(IFluidTank iFluidTank) {
        this.getNetwork().updateTileGuiField(this, NBT_TANK);
        this.inventory.set(slotDisplay, ItemDisplayIcon.createWithFluidStack(this.tank.getFluid()));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank)
                : super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.tank.readFromNBT(nbt.getCompoundTag(NBT_TANK));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.tank.writeToNBT(this.getTag(nbt, NBT_TANK));
        return nbt;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<>();
        //list.add(getBlockDrop());
        list.addAll(getInventoryDrops());
        return list;
    }

    @Override
    public List<ItemStack> getInventoryDrops() {
        List<ItemStack> list = new ArrayList<>();
        list.add(this.getStackInSlot(slotInput));
        list.add(this.getStackInSlot(slotOutput));
        return list;
    }

    protected abstract ItemStack getBlockDrop();

    @Override
    public void update() {
        if (input) {
            GTUtility.importFromSideIntoMachine(this, this.getFacing());
            importFluidFromMachineToSide(this, tank, this.getFacing(), 1000);
        } else {
            GTUtility.exportFromMachineToSide(this, this.getFacing(), slotOutput);
            GTUtility.exportFluidFromMachineToSide(this, tank, this.getFacing(), 1000);
        }
        GTHelperFluid.doFluidContainerThings(this, this.tank, slotInput, slotOutput);
    }

    /**
     * Export a FluidStack from a TileEntityMachine tank to another tile.
     *
     * @param machine - The TileEntityMachine which has the tank, provides World and
     *                BlockPos data.
     * @param tank    - the IC2Tank to try to import to.
     * @param side    - the EnumFacing to try to export fluids out of.
     * @param amount  - the amount of fluid to transfer
     */
    public static void importFluidFromMachineToSide(TileEntityMachine machine, IC2Tank tank, EnumFacing side,
                                                    int amount) {
        BlockPos importPos = machine.getPos().offset(side);
        if (!machine.getWorld().isBlockLoaded(importPos)) {
            return;
        }
        IFluidHandler fluidTile = FluidUtil.getFluidHandler(machine.getWorld(), importPos, side.getOpposite());
        boolean canImport = (tank.getFluidAmount() == 0 || tank.getFluid() != null) && fluidTile != null;
        if (canImport) {
            FluidUtil.tryFluidTransfer(tank, fluidTile, amount, true);
        }
    }

    @Override
    public boolean hasRightClick() {
        return true;
    }

    @Override
    public boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing enumFacing, Side side) {
        return input ? GTHelperFluid.doClickableFluidContainerEmptyThings(player, hand, world, pos, this.tank) : GTHelperFluid.doClickableFluidContainerFillThings(player, hand, world, pos, this.tank);
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

    public IC2Tank getTank() {
        return tank;
    }

    public ItemStack getOutput(){
        return this.getStackInSlot(slotOutput);
    }

    public ItemStack getInput(){
        return this.getStackInSlot(slotInput);
    }

    public static class GTCXTileImportHatch extends GTCXTileItemFluidHatches{

        public GTCXTileImportHatch() {
            super(true);
        }

        @Override
        protected ItemStack getBlockDrop() {
            return null;
        }
    }
}
