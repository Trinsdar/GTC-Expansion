package gtc_expansion.container;

import gtc_expansion.GTCExpansion;
import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import gtc_expansion.util.GTCXTank;
import gtclassic.api.gui.GTGuiCompFluidTank;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotDisplay;
import ic2.core.inventory.slots.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXContainerFusionReactorHatch extends ContainerTileComponent<GTCXTileMultiFusionReactor> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(GTCExpansion.MODID, "textures/gui/hatchitemfluid.png");

    public GTCXContainerFusionReactorHatch(InventoryPlayer player, GTCXTileMultiFusionReactor tile, boolean second, boolean input) {
        super(tile);
        int index = input ? second ? 1 : 0 : 2;
        this.addSlotToContainer(new SlotCustom(tile, index * 2, 80, 17, null));
        this.addSlotToContainer(new SlotOutput(player.player, tile, 1 + (index * 2), 80, 53));
        int display = input ? second ? 7 : 6 : 8;
        this.addSlotToContainer(new SlotDisplay(tile, display, 59, 42));
        GTCXTank tank = input ? second ? tile.getInputTank2() : tile.getInputTank1() : tile.getOutputTank();
        this.addComponent(new GTGuiCompFluidTank(tank));
        this.addPlayerInventory(player, 0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onGuiLoaded(GuiIC2 gui) {
        gui.disableName();
        gui.dissableInvName();
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return getGuiHolder().canInteractWith(player);
    }

    @Override
    public int guiInventorySize() {
        return 3;
    }
}
