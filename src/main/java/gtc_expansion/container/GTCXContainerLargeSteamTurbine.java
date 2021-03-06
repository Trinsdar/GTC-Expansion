package gtc_expansion.container;

import gtc_expansion.GTCExpansion;
import gtc_expansion.gui.GTCXGuiCompMultiblockProductionString;
import gtc_expansion.gui.GTCXGuiCompMultiblockStatusString;
import gtc_expansion.tile.multi.GTCXTileMultiLargeSteamTurbine;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.util.math.Box2D;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXContainerLargeSteamTurbine extends ContainerTileComponent<GTCXTileMultiLargeSteamTurbine> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(GTCExpansion.MODID, "textures/gui/multiblockdisplay.png");

    public GTCXContainerLargeSteamTurbine(InventoryPlayer player, GTCXTileMultiLargeSteamTurbine tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 152, 5, null));
        this.addComponent(new GTCXGuiCompMultiblockStatusString(tile, new Box2D(10, 7, 137, 15)));
        this.addComponent(new GTCXGuiCompMultiblockProductionString(tile, new Box2D(10, 24, 137, 15)));
        this.addPlayerInventory(player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onGuiLoaded(GuiIC2 gui) {
        gui.disableName();
        gui.dissableInvName();
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public int guiInventorySize() {
        return 1;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return getGuiHolder().canInteractWith(player);
    }
}
