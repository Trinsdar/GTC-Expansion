package gtc_expansion.tile;

import gtc_expansion.data.GTCXLang;
import gtc_expansion.interfaces.IGTCapabilityTile;
import gtclassic.api.interfaces.IGTDebuggableTile;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Map;

public class GTCXTileBrick extends TileEntityBlock implements IGTDebuggableTile, IHasGui {
    IGTCapabilityTile owner = null;

    public void setOwner(IGTCapabilityTile owner) {
        this.owner = owner;
    }

    public IGTCapabilityTile getOwner() {
        return owner;
    }

    @Override
    public LocaleComp getBlockName() {
        return GTCXLang.FIRE_BRICK_BLOCK;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (this.owner != null){
            return owner.hasCapability(capability, facing);
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (this.owner != null){
            return owner.getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void getData(Map<String, Boolean> map) {
        map.put("Tile null? " + (this.owner == null), true);
    }

    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer entityPlayer) {
        if (this.owner != null){
            return owner.getGuiContainer(entityPlayer);
        }
        return null;
    }

    @Override
    public Class<? extends GuiScreen> getGuiClass(EntityPlayer entityPlayer) {
        if (owner != null){
            return owner.getGuiClass(entityPlayer);
        }
        return null;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return owner != null && !this.isInvalid();
    }

    @Override
    public boolean hasGui(EntityPlayer entityPlayer) {
        return owner != null;
    }
}
