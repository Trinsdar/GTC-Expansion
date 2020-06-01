package gtc_expansion.container;

import gtc_expansion.tile.GTCXTileTrashBin;
import gtclassic.GTMod;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.slots.SlotCustom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXContainerTrashBin extends ContainerTileComponent<GTCXTileTrashBin> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(GTMod.MODID, "textures/gui/destructopack.png");

    public GTCXContainerTrashBin(GTCXTileTrashBin inv, InventoryPlayer player) {
        super(inv);
        this.addSlotToContainer(new SlotCustom(inv, 0, 80, 17, inv.filter));
        this.addPlayerInventory(player, 0, 0);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public int guiInventorySize() {
        return getGuiHolder().getSlotCount();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return getGuiHolder().canInteractWith(player);
    }
}
