package gtc_expansion.container;


import gtc_expansion.tile.GTCXTileChemicalReactor;
import gtclassic.api.gui.GTGuiCompDirectionalProgress;
import gtclassic.api.gui.GTGuiCompMachinePower;
import gtclassic.api.slot.GTSlotUpgrade;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotDischarge;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXContainerChemicalReactor extends ContainerTileComponent<GTCXTileChemicalReactor> {

    public static Box2D machineProgressBox = new Box2D(73, 34, 30, 10); // the progress bar and size
    public static Vec2i machineProgressPos = new Vec2i(176, 0); // where the overlay is

    public GTCXContainerChemicalReactor(InventoryPlayer player, GTCXTileChemicalReactor tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 60, 16, tile.filter));// main slot
        this.addSlotToContainer(new SlotCustom(tile, 1, 80, 16, tile.filter)); // second slot
        this.addSlotToContainer(new SlotCustom(tile, 2, 100, 16, tile.filter)); // third output
        this.addSlotToContainer(new SlotDischarge(tile, Integer.MAX_VALUE, 5, 8, 62)); // battery
        this.addSlotToContainer(new SlotOutput(player.player, tile, 3, 70, 46)); // output
        this.addSlotToContainer(new SlotOutput(player.player, tile, 4, 90, 46)); // second output
        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new GTSlotUpgrade(tile, 6 + i, 152, 26 + i * 18));
        }
        this.addComponent(new GTGuiCompMachinePower(tile));
        this.addPlayerInventory(player);
        this.addComponent(new GTGuiCompDirectionalProgress(tile, machineProgressBox, machineProgressPos, GTGuiCompDirectionalProgress.Direction.DOWN));
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
