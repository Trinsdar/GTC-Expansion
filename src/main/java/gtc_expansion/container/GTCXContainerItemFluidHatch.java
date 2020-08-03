package gtc_expansion.container;

import gtc_expansion.GTCExpansion;
import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches;
import gtclassic.api.gui.GTGuiCompFluidTank;
import ic2.core.inventory.container.ContainerTileComponent;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.slots.SlotCustom;
import ic2.core.inventory.slots.SlotDisplay;
import ic2.core.inventory.slots.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXContainerItemFluidHatch extends ContainerTileComponent<GTCXTileItemFluidHatches> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(GTCExpansion.MODID, "textures/gui/hatchitemfluid.png");

    public GTCXContainerItemFluidHatch(InventoryPlayer player, GTCXTileItemFluidHatches tile) {
        super(tile);
        this.addSlotToContainer(new SlotCustom(tile, 0, 80, 17, new GTCXFilterHatch(tile)));
        this.addSlotToContainer(new SlotOutput(player.player, tile, 1, 80, 53));
        this.addSlotToContainer(new SlotDisplay(tile, 2, 59, 42));
        this.addComponent(new GTGuiCompFluidTank(tile.getTank()));
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

    public static class GTCXFilterHatch implements IFilter {
        GTCXTileItemFluidHatches hatch;
        public GTCXFilterHatch(GTCXTileItemFluidHatches hatch){
            this.hatch = hatch;
        }

        @Override
        public boolean matches(ItemStack itemStack) {
            return hatch.getOwner() != null;
        }
    }
}
