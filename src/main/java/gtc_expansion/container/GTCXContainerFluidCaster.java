package gtc_expansion.container;

import gtc_expansion.tile.GTCXTileFluidCaster;
import gtclassic.api.gui.GTGuiCompMachinePower;
import gtclassic.api.gui.GTSlotUpgrade;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
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

public class GTCXContainerFluidCaster extends ContainerTileComponent<GTCXTileFluidCaster> {

    public static Box2D machineProgressBox = new Box2D(78, 24, 20, 18); // the progress bar and size
    public static Vec2i machineProgressPos = new Vec2i(176, 0); // where the overlay is

    public GTCXContainerFluidCaster(InventoryPlayer player, GTCXTileFluidCaster tile) {
        super(tile);
        this.addSlotToContainer(new SlotDisplay(tile, 0, 35, 25));// fluid display slot
        this.addSlotToContainer(new SlotDisplay(tile, 1, 17, 25));// water display slot
        this.addSlotToContainer(new SlotCustom(tile, 2, 53, 25, tile.filter)); // mold slot
        this.addSlotToContainer(new SlotDischarge(tile, Integer.MAX_VALUE, 4, 80, 63)); // battery
        this.addSlotToContainer(new SlotOutput(player.player, tile, 3, 107, 25)); // output
        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new GTSlotUpgrade(tile, 5 + i, 152, 25 + i * 18));
        }
        this.addComponent(new GTGuiCompMachinePower(tile));
        this.addPlayerInventory(player);
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
