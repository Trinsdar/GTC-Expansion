package gtc_expansion.container;

import gtc_expansion.gui.GTCXGuiCompMachineSteam;
import gtc_expansion.tile.steam.GTCXTileSteamForgeHammer;
import gtc_expansion.tile.steam.GTCXTileSteamFurnace;
import gtclassic.api.gui.GTGuiCompDirectionalProgress;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXContainerSteamForgeHammer extends ContainerTileComponent<GTCXTileSteamForgeHammer> {

    public static Box2D machineProgressBox = new Box2D(80, 34, 20, 18); // the progress bar and size
    public static Vec2i machineProgressPos = new Vec2i(176, 0); // where the overlay is

    public GTCXContainerSteamForgeHammer(InventoryPlayer player, GTCXTileSteamForgeHammer tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 55, 35, tile.filter));// input slot
        this.addSlotToContainer(new SlotOutput(player.player, tile, 1, 109, 35));
        this.addPlayerInventory(player);
        this.addComponent(new GTCXGuiCompMachineSteam(tile, 80, 58));
        this.addComponent(new GTGuiCompDirectionalProgress(tile, machineProgressBox, machineProgressPos, GTGuiCompDirectionalProgress.Direction.UP));
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
