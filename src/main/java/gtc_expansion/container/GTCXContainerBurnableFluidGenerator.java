package gtc_expansion.container;

import gtc_expansion.tile.base.GTCXTileBaseBurnableFluidGenerator;
import gtclassic.api.gui.GTGuiCompFluidTank;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.components.base.MachineFuelComp;
import ic2.core.inventory.slots.SlotBase;
import ic2.core.inventory.slots.SlotDisplay;
import ic2.core.inventory.slots.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXContainerBurnableFluidGenerator extends ContainerTileComponent<GTCXTileBaseBurnableFluidGenerator> {

    public GTCXContainerBurnableFluidGenerator(InventoryPlayer player, GTCXTileBaseBurnableFluidGenerator tile) {
        super(tile);
        this.addSlotToContainer(new SlotBase(tile, 0, 80, 17));
        this.addSlotToContainer(new SlotOutput(player.player, tile, 1, 80, 53));
        this.addSlotToContainer(new SlotDisplay(tile, 2, 59, 42));
        this.addComponent(new GTGuiCompFluidTank(tile.getTankInstance()));
        this.addComponent(new MachineFuelComp(tile, tile.getFuelBox(), tile.getFuelPos()));
        this.addPlayerInventory(player, 0, 0);
    }

    @Override
    public ResourceLocation getTexture() {
        return this.getGuiHolder().getTexture();
    }

    @Override
    public int guiInventorySize() {
        return 2;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return getGuiHolder().canInteractWith(player);
    }
}
