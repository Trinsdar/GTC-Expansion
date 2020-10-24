package gtc_expansion.container;

import gtc_expansion.gui.GTCXGuiCompCoalBoiler;
import gtc_expansion.tile.steam.GTCXTileCoalBoiler;
import gtclassic.common.util.GTIFilters;
import ic2.core.block.base.util.info.FuelMachineInfo;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.base.MachineFuelComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXContainerCoalBoiler extends ContainerTileComponent<GTCXTileCoalBoiler> {
    public GTCXContainerCoalBoiler(InventoryPlayer player, GTCXTileCoalBoiler tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 44, 26, new GTIFilters.FluidItemFilter()));
        this.addSlotToContainer(new SlotOutput(player.player, tile, 1, 44, 62));
        this.addSlotToContainer(new SlotOutput(player.player, tile, 2, 116, 26));
        this.addSlotToContainer(new SlotCustom(tile, 3, 116, 62, CommonFilters.FurnaceFuel));
        this.addComponent(new MachineFuelComp(tile, new Box2D(115,43, 18, 18), new Vec2i(176, 0)));
        this.addComponent(new GTCXGuiCompCoalBoiler(tile));
        this.addPlayerInventory(player);
    }

    @Override
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
}
