package gtc_expansion.tile;

import gtc_expansion.data.GTCXBlocks;
import gtc_expansion.container.GTCXContainerTrashBin;
import gtc_expansion.data.GTCXLang;
import gtclassic.api.tile.GTTileBaseRecolorableTile;
import gtclassic.common.GTItems;
import ic2.core.RotationList;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.InvertedFilter;
import ic2.core.inventory.gui.GuiComponentContainer;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GTCXTileTrashBin extends GTTileBaseRecolorableTile implements IHasGui {
    public IFilter filter = new InvertedFilter(new ArrayFilter(new BasicItemFilter(GTItems.destructoPack), new BasicItemFilter(GTItems.debugScanner), new BasicItemFilter(GTItems.portableScanner)));
    public GTCXTileTrashBin() {
        super(1);
    }

    @Override
    protected void addSlots(InventoryHandler handler) {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.UP.invert());
        handler.registerDefaultSlotAccess(AccessRule.Import, 0);
        handler.registerDefaultSlotsForSide(RotationList.DOWN.invert(), 0);
        handler.registerInputFilter(filter, 0);
        handler.registerSlotType(SlotType.Input, 0);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        // empty so items get deleted
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        return new GTCXContainerTrashBin(this, entityPlayer.inventory);
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        return GuiComponentContainer.class;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return !this.isInvalid();
    }

    @Override
    public boolean hasGui(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public Block getBlockDrop() {
        return GTCXBlocks.trashBin;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.TRASH_BIN;
    }
}
