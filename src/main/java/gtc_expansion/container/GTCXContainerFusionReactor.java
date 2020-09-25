package gtc_expansion.container;

import gtc_expansion.gui.GTCXGuiCompFusionButtons;
import gtc_expansion.gui.GTCXGuiCompFusionOverlay;
import gtc_expansion.gui.GTCXGuiCompMultiblockStatusString;
import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import gtclassic.api.gui.GTGuiCompEnergyStorageBar;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.util.math.Box2D;
import ic2.core.util.math.Vec2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXContainerFusionReactor extends ContainerTileComponent<GTCXTileMultiFusionReactor> {
    public static Box2D machineProgressBox = new Box2D(4, 162, 149, 16);
    public static Vec2i machineProgressPos = new Vec2i(0, 235);
    public static Box2D statusBox = new Box2D(7, 101, 121, 15);
    public static Box2D machineChargeBox = new Box2D(5, 156, 147, 5);
    public static Vec2i machineChargePos = new Vec2i(0, 251);

    public GTCXContainerFusionReactor(InventoryPlayer player, GTCXTileMultiFusionReactor tile) {
        super(tile);
        this.addComponent(new MachineProgressComp(tile, machineProgressBox, machineProgressPos));
        this.addComponent(new GTGuiCompEnergyStorageBar(tile, machineChargeBox, machineChargePos));
        this.addComponent(new GTCXGuiCompMultiblockStatusString(tile, statusBox));
        this.addComponent(new GTCXGuiCompFusionButtons(tile));
        this.addComponent(new GTCXGuiCompFusionOverlay(tile));
    }

    @SideOnly(Side.CLIENT)
    public void onGuiLoaded(GuiIC2 gui) {
        gui.disableName();
        gui.dissableInvName();
        gui.setMaxGuiY(182);
    }

    public ResourceLocation getTexture() {
        return this.getGuiHolder().getGuiTexture();
    }

    public int guiInventorySize() {
        return this.getGuiHolder().slotCount;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.getGuiHolder().canInteractWith(playerIn);
    }
}
