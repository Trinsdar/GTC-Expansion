package gtc_expansion.container;

import gtc_expansion.tile.GTCXTileAlloyFurnace;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.gui.components.base.MachineFuelComp;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXContainerAlloyFurnace extends ContainerTileComponent<GTCXTileAlloyFurnace> {

    public static Box2D machineProgressBox = new Box2D(78, 24, 20, 18); // the progress bar and size
    public static Vec2i machineProgressPos = new Vec2i(176, 0); // where the overlay is
    public static Box2D machineFuelBox = new Box2D(81, 47, 14, 14);
    public static Vec2i machineFuelPos = new Vec2i(176, 18);

    public GTCXContainerAlloyFurnace(InventoryPlayer player, GTCXTileAlloyFurnace tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 35, 25, tile.filter));// main slot
        this.addSlotToContainer(new SlotCustom(tile, 1, 53, 25, tile.filter)); // second slot
        this.addSlotToContainer(new SlotCustom(tile, 3, 80, 63, CommonFilters.IronFurnaceFuelWithLava)); // fuel
        this.addSlotToContainer(new SlotOutput(player.player, tile, 2, 107, 25)); // output
        this.addPlayerInventory(player);
        this.addComponent(new MachineProgressComp(tile, machineProgressBox, machineProgressPos));
        this.addComponent(new MachineFuelComp(tile, machineFuelBox, machineFuelPos));
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
