package gtc_expansion.container;

import gtc_expansion.tile.GTCXTileDustbin;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXContainerDustbin extends ContainerTileComponent<GTCXTileDustbin> {

    public GTCXContainerDustbin(InventoryPlayer player, GTCXTileDustbin tile) {
        super(tile);
        int i;
        int j;
        for (i = 0; i < 4; ++i) {
            for (j = 0; j < 4; ++j) {
                this.addSlotToContainer(new SlotCustom(tile, (i + j * 4), 8 + j * 18, 8 + i * 18, tile.filter));
            }
        }
        for (i = 0; i < 4; ++i) {
            for (j = 0; j < 4; ++j) {
                this.addSlotToContainer(new SlotOutput(player.player, tile, (i + j * 4) + 16, 98 + j * 18, 8 + i * 18));
            }
        }
        this.addPlayerInventory(player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onGuiLoaded(GuiIC2 gui) {
        gui.dissableInvName();
    }

    @Override
    public ResourceLocation getTexture() {
        return this.getGuiHolder().getGuiTexture();
    }

    @Override
    public int guiInventorySize() {
        return this.getGuiHolder().slotCount;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.getGuiHolder().canInteractWith(playerIn);
    }
}
