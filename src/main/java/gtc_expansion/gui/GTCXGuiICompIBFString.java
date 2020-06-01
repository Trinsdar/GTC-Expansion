package gtc_expansion.gui;

import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.platform.registry.Ic2GuiComp;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class GTCXGuiICompIBFString extends GuiComponent {
    private GTCXTileMultiIndustrialBlastFurnace tile;
    public GTCXGuiICompIBFString(GTCXTileMultiIndustrialBlastFurnace tile) {
        super(Ic2GuiComp.nullBox);
        this.tile = tile;
    }

    @Override
    public List<ActionRequest> getNeededRequests() {
        return Collections.singletonList(ActionRequest.FrontgroundDraw);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawFrontground(GuiIC2 gui, int mouseX, int mouseY) {
        gui.drawString("Heat Capacity: " + tile.getCurrentHeat() + " K", 34, 63, Color.darkGray.hashCode());
    }
}
