package gtc_expansion.container;

import gtc_expansion.tile.overrides.GTCXTileScanner;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.components.base.MachineChargeComp;
import ic2.core.inventory.gui.components.base.MachineProgressComp;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotDischarge;
import ic2.core.inventory.slots.SlotOutput;
import ic2.core.inventory.slots.SlotUpgrade;
import ic2.core.platform.registry.Ic2GuiComp;
import ic2.core.platform.registry.Ic2Resources;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GTCXContainerScanner extends ContainerTileComponent<GTCXTileScanner> {

    public GTCXContainerScanner(InventoryPlayer player, GTCXTileScanner tile) {
        super(tile);
        this.addSlotToContainer(new SlotDischarge(tile, 2147483647, 0, 56, 53));
        this.addSlotToContainer(new SlotCustom(tile, 1, 56, 17, tile.filter));
        this.addSlotToContainer(new SlotOutput(player.player, tile, 2, 116, 35));

        for(int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotUpgrade(tile, 3 + i, 152, 8 + i * 18));
        }

        this.addPlayerInventory(player);
        this.addComponent(new MachineChargeComp(tile, Ic2GuiComp.machineChargeBox, Ic2GuiComp.machineChargePos));
        this.addComponent(new MachineProgressComp(tile, Ic2GuiComp.machineProgressBox, Ic2GuiComp.machineProgressPos));
    }

    @Override
    public ResourceLocation getTexture() {
        return Ic2Resources.cropAnalyzerBlock;
    }

    @Override
    public int guiInventorySize() {
        return 7;
    }
}
