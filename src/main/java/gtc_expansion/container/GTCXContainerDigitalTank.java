package gtc_expansion.container;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.gui.GTCXGuiCompDigitalTank;
import gtc_expansion.tile.GTCXTileDigitalTank;
import gtclassic.api.gui.GTGuiCompFluidTank;
import gtclassic.api.helpers.GTHelperStack;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTItems;
import gtclassic.common.util.GTIFilters;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotDisplay;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.misc.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class GTCXContainerDigitalTank extends ContainerTileComponent<GTCXTileDigitalTank> {

	public static ResourceLocation TEXTURE = new ResourceLocation(GTCExpansion.MODID, "textures/gui/digitaltank.png");

	public GTCXContainerDigitalTank(InventoryPlayer player, GTCXTileDigitalTank tile) {
		super(tile);
		this.addSlotToContainer(new SlotCustom(tile, 0, 80, 17, new ArrayFilter(new DataOrbBlacklist(false), new GTIFilters.FluidItemFilter())));
		this.addSlotToContainer(new SlotOutput(player.player, tile, 1, 80, 53));
		this.addSlotToContainer(new SlotDisplay(tile, 2, 59, 42));
		this.addSlotToContainer(new SlotCustom(tile, 3, 61, 64, new DataOrbBlacklist(true)));
		this.addComponent(new GTGuiCompFluidTank(tile.getTankInstance()));
		this.addComponent(new GTCXGuiCompDigitalTank(tile));
		this.addPlayerInventory(player, 0, 1);
	}

	@Override
	public void onGuiLoaded(GuiIC2 gui) {
		gui.dissableInvName();
	}

	@Override
	public ResourceLocation getTexture() {
		return TEXTURE;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return getGuiHolder().canInteractWith(player);
	}

	@Override
	public int guiInventorySize() {
		return 4;
	}

	public static class DataOrbBlacklist implements IFilter {
		boolean inverse;
		public DataOrbBlacklist(boolean inverse){
			this.inverse = inverse;
		}

		@Override
		public boolean matches(ItemStack itemStack) {
			if (StackUtil.isStackEqual(itemStack, GTMaterialGen.get(GTCXItems.dataOrbStorage)) || GTHelperStack.isEqual(itemStack, GTMaterialGen.get(GTItems.orbData))) {
				return inverse;
			}
			return !inverse;
		}
	}
}
