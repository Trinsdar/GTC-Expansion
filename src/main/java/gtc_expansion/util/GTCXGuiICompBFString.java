package gtc_expansion.util;

import gtc_expansion.tile.multi.GTCXTileMultiIndustrialBlastFurnace;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.platform.registry.Ic2GuiComp;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GTCXGuiICompBFString extends GuiComponent {
    private GTCXTileMultiIndustrialBlastFurnace tile;
    public GTCXGuiICompBFString(GTCXTileMultiIndustrialBlastFurnace tile) {
        super(Ic2GuiComp.nullBox);
        this.tile = tile;
    }

    @Override
    public List<ActionRequest> getNeededRequests() {
        return Arrays.asList(ActionRequest.FrontgroundDraw);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawFrontground(GuiIC2 gui, int mouseX, int mouseY) {
        gui.drawString("Heat Capacity: " + tile.getCurrentHeat() + " K", 34, 63, Color.darkGray.hashCode());
    }
}
