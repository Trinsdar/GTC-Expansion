package gtc_expansion.container;

import gtc_expansion.tile.multi.GTCXTileMultiIndustrialSawmill;
import gtclassic.api.gui.GTGuiCompMachinePower;
import gtclassic.api.gui.GTGuiCompMultiTileStatus;
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

public class GTCXContainerIndustrialSawmill extends ContainerTileComponent<GTCXTileMultiIndustrialSawmill> {
    public static final Box2D machineProgressBox = new Box2D(58, 28, 20, 11);
    public static final Vec2i machineProgressPos = new Vec2i(176, 0);
    public GTCXContainerIndustrialSawmill(InventoryPlayer player, GTCXTileMultiIndustrialSawmill tile) {
        super(tile);
        this.addSlotToContainer(new SlotDisplay(tile, 0, 34, 34));
        this.addSlotToContainer(new SlotCustom(tile, 1, 34, 16, tile.filter));
        for (int x = 0; x < 3; ++x) {
            this.addSlotToContainer(new SlotOutput(player.player, tile, 2 + x, 86 + x * 18, 25));
        }
        this.addSlotToContainer(new SlotDischarge(tile, Integer.MAX_VALUE, 5, 8, 62));
        this.addPlayerInventory(player);
        for (int i = 0; i < 2; ++i) {
            this.addSlotToContainer(new GTSlotUpgrade(tile, 6 + i, 80 + (i * 18), 62));
        }
        this.addComponent(new GTGuiCompMachinePower(tile));
        this.addComponent(new GTGuiCompMultiTileStatus(tile,new Box2D(12, 21, 15, 24)));
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
        return this.getGuiHolder().getSlotCount();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.getGuiHolder().canInteractWith(playerIn);
    }
}
