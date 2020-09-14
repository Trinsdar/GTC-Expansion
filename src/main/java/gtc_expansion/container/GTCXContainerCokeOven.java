package gtc_expansion.container;

import gtc_expansion.tile.multi.GTCXTileMultiCokeOven;
import gtclassic.api.gui.GTGuiCompMultiTileStatus;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotDisplay;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXContainerCokeOven extends ContainerTileComponent<GTCXTileMultiCokeOven> {

    public static final Box2D machineProgressBox = new Box2D(78, 23, 20, 18);
    public static final Vec2i machineProgressPos = new Vec2i(176, 0);
    public static Box2D machineFuelBox = new Box2D(81, 47, 14, 14);
    public static Vec2i machineFuelPos = new Vec2i(176, 18);

    public GTCXContainerCokeOven(InventoryPlayer player, GTCXTileMultiCokeOven tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 53, 26, tile.filter));
        this.addSlotToContainer(new SlotOutput(player.player, tile, 1, 107, 26));
        this.addSlotToContainer(new SlotDisplay(tile, 2, 125, 26));
        this.addPlayerInventory(player);
        this.addComponent(new GTGuiCompMultiTileStatus(tile, new Box2D(12, 6, 12, 36)));
        this.addComponent(new MachineProgressComp(tile, machineProgressBox, machineProgressPos));
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
