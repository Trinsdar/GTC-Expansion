package gtc_expansion.tile;

import gtc_expansion.data.GTCXLang;
import gtc_expansion.interfaces.IGTCapabilityTile;
import gtclassic.api.interfaces.IGTDebuggableTile;
import ic2.api.classic.tile.machine.IProgressMachine;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.base.util.info.ProgressInfo;
import ic2.core.inventory.base.IHasGui;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.util.obj.IClickable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

public class GTCXTileBrick extends TileEntityBlock implements IGTDebuggableTile, IHasGui {
    IGTCapabilityTile owner = null;

    public void setOwner(IGTCapabilityTile owner) {
        this.owner = owner;
        this.getComponents().clear();
        if (owner instanceof IProgressMachine){
            this.addInfos(new ProgressInfo((IProgressMachine) owner));
        }
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


    public boolean hasRightClick() {
        return true;
    }


    public boolean onRightClick(EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, Side side) {
        if (owner instanceof IClickable){
            ((IClickable)owner).onRightClick(entityPlayer, enumHand, enumFacing, side);
        }
        return false;
    }


    public boolean hasLeftClick() {
        return false;
    }


    public void onLeftClick(EntityPlayer entityPlayer, Side side) {

    }
}
