package gtc_expansion.container;

import gtc_expansion.tile.GETileAssemblingMachine;
import gtclassic.util.GTSlotUpgrade;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotDischarge;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GEContainerAssemblingMachine extends ContainerTileComponent<GETileAssemblingMachine> {

    public static Box2D machineProgressBox = new Box2D(78, 24, 20, 18); // the progress bar and size
    public static Vec2i machineProgressPos = new Vec2i(176, 0); // where the overlay is

    public GEContainerAssemblingMachine(InventoryPlayer player, GETileAssemblingMachine tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 35, 25, tile.filter));// main slot
        this.addSlotToContainer(new SlotCustom(tile, 1, 53, 25, tile.filter)); // second slot
        this.addSlotToContainer(new SlotDischarge(tile, Integer.MAX_VALUE, 4, 80, 63)); // battery
        this.addSlotToContainer(new SlotOutput(player.player, tile, 2, 107, 25)); // output
        this.addSlotToContainer(new SlotOutput(player.player, tile, 3, 125, 25)); // second output
        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new GTSlotUpgrade(tile, 5 + i, 152, 26 + i * 18));
        }
        this.addPlayerInventory(player);
        this.addComponent(new MachineProgressComp(tile, machineProgressBox, machineProgressPos));
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
