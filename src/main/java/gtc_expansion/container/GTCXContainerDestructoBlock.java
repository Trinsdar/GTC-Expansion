package gtc_expansion.container;

import gtc_expansion.tile.GTCXTileDestructoBlock;
import gtclassic.GTMod;
import gtclassic.common.GTItems;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.filters.InvertedFilter;
import ic2.core.inventory.slots.SlotCustom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXContainerDestructoBlock extends ContainerTileComponent<GTCXTileDestructoBlock> {

    public static ResourceLocation TEXTURE = new ResourceLocation(GTMod.MODID, "textures/gui/destructopack.png");

    public GTCXContainerDestructoBlock(GTCXTileDestructoBlock inv, InventoryPlayer player) {
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
