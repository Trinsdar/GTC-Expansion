package gtc_expansion.tile;

import gtc_expansion.GTCExpansion;
import gtc_expansion.container.GTCXContainerDigitalTank;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.data.GTCXLang;
import gtclassic.api.helpers.GTHelperFluid;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.interfaces.IGTItemContainerTile;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTItems;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IC2;
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
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IClickable;
import ic2.core.util.obj.ITankListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class GTCXTileDigitalTank extends TileEntityMachine implements IHasGui, INetworkClientTileEntityEventListener, ITankListener, ITickable, IGTItemContainerTile, IClickable {

	@NetworkField(index = 3)
	IC2Tank tank = new IC2Tank(256000);
	public static final String NBT_TANK = "tank";

	public GTCXTileDigitalTank() {
		super(4);
		this.addNetworkFields(NBT_TANK);
		this.addGuiFields(NBT_TANK);
		this.tank.addListener(this);
	}

	@Override
	public LocaleComp getBlockName() {
		return GTCXLang.DIGITAL_TANK;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Class<? extends GuiScreen> getGuiClass(EntityPlayer player) {
		return GuiComponentContainer.class;
	}

	@Override
	public ContainerIC2 getGuiContainer(EntityPlayer player) {
		return new GTCXContainerDigitalTank(player.inventory, this);
	}

	@Override
	protected void addSlots(InventoryHandler handler) {
		handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
		handler.registerDefaultSlotAccess(AccessRule.Import, 0);
		handler.registerDefaultSlotAccess(AccessRule.Export, 1);
		handler.registerDefaultSlotsForSide(RotationList.ALL, 0, 1);
		handler.registerSlotType(SlotType.Storage, 0, 1);
	}

	@Override
	public int getMaxStackSize(int slot) {
		return slot == 3 ? 1 : super.getMaxStackSize(slot);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !this.isInvalid();
	}

	@Override
	public void onGuiClosed(EntityPlayer entityPlayer) {
		// needed for construction
	}

	@Override
	public boolean hasGui(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
		return facing != getFacing() && facing.getAxis().isHorizontal();
	}

	@Override
	public boolean canRemoveBlock(EntityPlayer player) {
		return true;
	}

	public ItemStack dataSlot() {
		return this.getStackInSlot(3);
	}

	public void dataSlot(ItemStack stack) {
		this.setStackInSlot(3, stack);
	}

	public void tryReadOrbData(EntityPlayer player) {
		if (dataSlot().isEmpty()) {
			IC2.platform.messagePlayer(player, "No Data Orb present!");
			return;
		}
		if (StackUtil.isStackEqual(dataSlot(), GTMaterialGen.get(GTCXItems.dataOrbStorage), false, true)) {
			if (dataSlot().getCount() > 1) {
				IC2.platform.messagePlayer(player, "Read Failed: Too many orbs");
				return;
			}
			NBTTagCompound nbt = StackUtil.getNbtData(dataSlot());
			if (!nbt.hasKey("Fluid")) {
				IC2.platform.messagePlayer(player, "Read Failed: No data to read");
				return;
			}
			NBTTagCompound data = nbt.getCompoundTag("Fluid");
			FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(data);
			if (tank.getFluid() != null && !tank.getFluid().isFluidEqual(fluidStack)){
				IC2.platform.messagePlayer(player, "Read Failed: Tank is not empty");
				return;
			}
			if (fluidStack == null){
				IC2.platform.messagePlayer(player, "Read Failed: Fluid contained is null");
				return;
			}
			int room = this.tank.getCapacity() - this.tank.getFluidAmount();
			if (fluidStack.amount > room){
				GTCExpansion.logger.info(room);
				int fill = this.tank.fill(new FluidStack(fluidStack.getFluid(), room), true);
				fluidStack.amount -= fill;
				fluidStack.writeToNBT(data);
				nbt.setTag("Fluid", data);
				return;
			} else {
				this.tank.fill(fluidStack, true);
			}

			dataSlot(GTMaterialGen.get(GTItems.orbData));
		}
	}

	public void tryWriteOrbData(EntityPlayer player) {
		if (dataSlot().isEmpty()) {
			IC2.platform.messagePlayer(player, "No Data Orb present!");
			return;
		}
		if (this.tank.getFluid() == null){
			IC2.platform.messagePlayer(player, "Write Failed: fluid tank empty");
			return;
		}
		if (GTHelperStack.isEqual(dataSlot(), GTMaterialGen.get(GTItems.orbData))) {
			if (dataSlot().getCount() > 1) {
				IC2.platform.messagePlayer(player, "Write Failed: too many orbs");
				return;
			}
			dataSlot(GTMaterialGen.get(GTCXItems.dataOrbStorage));
			NBTTagCompound nbt = StackUtil.getOrCreateNbtData(dataSlot());
			NBTTagCompound data = new NBTTagCompound();
			this.tank.getFluid().writeToNBT(data);
			nbt.setTag("Fluid", data);
			this.tank.drain(this.tank.getCapacity(), true);
		}
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
	public void onNetworkEvent(EntityPlayer player, int event) {
		if (event == 0) {
			tryReadOrbData(player);
		}
		if (event == 1) {
			tryWriteOrbData(player);
		}
	}

	public IC2Tank getTankInstance() {
		return tank;
	}

	@Override
	public void onTankChanged(IFluidTank iFluidTank) {
		this.getNetwork().updateTileGuiField(this, "tank");
		this.setStackInSlot(2, ItemDisplayIcon.createWithFluidStack(this.tank.getFluid()));
	}

	@Override
	public List<ItemStack> getDrops() {
		return getInventoryDrops();
	}

	@Override
	public List<ItemStack> getInventoryDrops() {
		List<ItemStack> list = new ArrayList<>();
		list.add(this.getStackInSlot(0));
		list.add(this.getStackInSlot(1));
		list.add(dataSlot());
		return list;
	}

	@Override
	public void update() {
		GTHelperFluid.doFluidContainerThings(this, this.tank, 0, 1);
	}

	@Override
	public boolean hasRightClick() {
		return true;
	}

	@Override
	public boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing enumFacing, Side side) {
		return GTHelperFluid.doClickableFluidContainerThings(player, hand, world, pos, this.tank);
	}

	@Override
	public boolean hasLeftClick() {
		return false;
	}

	@Override
	public void onLeftClick(EntityPlayer entityPlayer, Side side) {

	}
}
