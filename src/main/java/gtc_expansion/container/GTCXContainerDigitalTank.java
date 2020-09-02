package gtc_expansion.container;

import gtc_expansion.GTCExpansion;
import gtc_expansion.tile.GTCXTileDigitalTank;
import gtclassic.api.gui.GTGuiCompFluidTank;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.slots.SlotBase;
import ic2.core.inventory.slots.SlotDisplay;
import ic2.core.inventory.slots.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXContainerDigitalTank extends ContainerTileComponent<GTCXTileDigitalTank> {

	public static ResourceLocation TEXTURE = new ResourceLocation(GTCExpansion.MODID, "textures/gui/digitaltank.png");

	public GTCXContainerDigitalTank(InventoryPlayer player, GTCXTileDigitalTank tile) {
		super(tile);
		this.addSlotToContainer(new SlotBase(tile, 0, 80, 17));
		this.addSlotToContainer(new SlotOutput(player.player, tile, 1, 80, 53));
		this.addSlotToContainer(new SlotDisplay(tile, 2, 59, 42));
		this.addComponent(new GTGuiCompFluidTank(tile.getTankInstance()));
		this.addPlayerInventory(player, 0, 0);
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
}
