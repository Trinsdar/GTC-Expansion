package gtc_expansion.container;

import gtc_expansion.gui.GTCXGuiCompMachineSteam;
import gtc_expansion.tile.steam.GTCXTileSteamAlloySmelter;
import gtc_expansion.tile.steam.GTCXTileSteamFurnace;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXContainerSteamAlloySmelter extends ContainerTileComponent<GTCXTileSteamAlloySmelter> {

    public static Box2D machineProgressBox = new Box2D(80, 34, 20, 18); // the progress bar and size
    public static Vec2i machineProgressPos = new Vec2i(176, 0); // where the overlay is

    public GTCXContainerSteamAlloySmelter(InventoryPlayer player, GTCXTileSteamAlloySmelter tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 37, 35, tile.filter));// input slot
        this.addSlotToContainer(new SlotCustom(tile, 1, 55, 35, tile.filter));// input slot
        this.addSlotToContainer(new SlotOutput(player.player, tile, 2, 109, 35));
        this.addPlayerInventory(player);
        this.addComponent(new GTCXGuiCompMachineSteam(tile));
        this.addComponent(new MachineProgressComp(tile, machineProgressBox, machineProgressPos));
    }

    @Override
    public ResourceLocation getTexture() {
        return this.getGuiHolder().getGuiLocation();
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
