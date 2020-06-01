package gtc_expansion.gui;

import gtc_expansion.tile.GTCXTileFluidSmelter;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.platform.registry.Ic2GuiComp;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class GTCXGuiICompFluidSmelterString extends GuiComponent {
    private GTCXTileFluidSmelter tile;
    public GTCXGuiICompFluidSmelterString(GTCXTileFluidSmelter tile) {
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
        gui.drawString("Heat: " + tile.heat + " K/" + tile.maxHeat + " K" , 52, 45, Color.darkGray.hashCode());
    }
}
