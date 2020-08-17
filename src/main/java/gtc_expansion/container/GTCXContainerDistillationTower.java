package gtc_expansion.container;

import gtc_expansion.tile.multi.GTCXTileMultiDistillationTower;
import gtclassic.api.gui.GTGuiCompDirectionalProgress;
import gtclassic.api.gui.GTGuiCompMachinePower;
import gtclassic.api.gui.GTGuiCompMultiTileStatus;
import gtclassic.api.gui.GTSlotUpgrade;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.slots.SlotDischarge;
import ic2.core.inventory.slots.SlotDisplay;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXContainerDistillationTower extends ContainerTileComponent<GTCXTileMultiDistillationTower> {

    public static Box2D machineProgressBox = new Box2D(80, 4, 16, 72); // the progress bar and size
    public static Vec2i machineProgressPos = new Vec2i(176, 0); // where the overlay is

    public GTCXContainerDistillationTower(InventoryPlayer player, GTCXTileMultiDistillationTower tile) {
        super(tile);
        this.addSlotToContainer(new SlotDisplay(tile, 0, 62, 41));
        this.addSlotToContainer(new SlotDischarge(tile, Integer.MAX_VALUE, 1, 62, 59)); // battery
        this.addSlotToContainer(new SlotDisplay(tile, 2, 98, 5));
        this.addSlotToContainer(new SlotDisplay(tile, 3, 98, 23));
        this.addSlotToContainer(new SlotDisplay(tile, 4, 98, 41));
        this.addSlotToContainer(new SlotDisplay(tile, 5, 98, 59));
        this.addSlotToContainer(new SlotDisplay(tile, 6, 116, 5));
        this.addSlotToContainer(new SlotDisplay(tile, 7, 116, 23));
        this.addSlotToContainer(new SlotOutput(player.player, tile, 8, 116, 41)); // output
        this.addSlotToContainer(new SlotOutput(player.player, tile, 9, 116, 59)); // second output
        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new GTSlotUpgrade(tile, 10 + i, 152, 26 + i * 18));
        }
        this.addComponent(new GTGuiCompMachinePower(tile, 61, 21, 72, 176));
        this.addPlayerInventory(player);
        this.addComponent(new GTGuiCompMultiTileStatus(tile,new Box2D(12, 6, 12, 63)));
        this.addComponent(new GTGuiCompDirectionalProgress(tile, machineProgressBox, machineProgressPos, GTGuiCompDirectionalProgress.Direction.UP));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onGuiLoaded(GuiIC2 gui) {
        gui.dissableInvName();
        gui.disableName();
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
