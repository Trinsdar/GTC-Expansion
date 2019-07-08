package gtc_expansion.container;

import gtc_expansion.tile.multi.GTTileMultiVacuumFreezer;
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

public class GTContainerVacuumFreezer extends ContainerTileComponent<GTTileMultiVacuumFreezer> {
    public static final Box2D machineProgressBox = new Box2D(78, 28, 20, 11);
    public static final Vec2i machineProgressPos = new Vec2i(176, 0);
    public GTContainerVacuumFreezer(InventoryPlayer player, GTTileMultiVacuumFreezer tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 53, 25, null));// main slot
        this.addSlotToContainer(new SlotDischarge(tile, Integer.MAX_VALUE, 2, 80, 63)); // battery
        this.addSlotToContainer(new SlotOutput(player.player, tile, 1, 107, 25)); // output
        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new GTSlotUpgrade(tile, 4 + i, 152, 26 + i * 18));
        }
        this.addComponent(new MachineProgressComp(tile, machineProgressBox, machineProgressPos));
    }

    @Override
    public ResourceLocation getTexture() {
        return this.getGuiHolder().getGuiTexture();
    }

    @Override
    public int guiInventorySize() {
        return this.getGuiHolder().getSlotCount();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.getGuiHolder().canInteractWith(playerIn);
    }
}