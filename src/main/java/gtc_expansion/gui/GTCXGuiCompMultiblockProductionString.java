package gtc_expansion.gui;

import gtc_expansion.interfaces.IGTMultiTileProduction;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.util.math.Box2D;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class GTCXGuiCompMultiblockProductionString extends GuiComponent {
    IGTMultiTileProduction tile;
    Box2D box;

    public GTCXGuiCompMultiblockProductionString(IGTMultiTileProduction tile, Box2D box) {
        super(box);
        this.tile = tile;
        this.box = box;
    }

    @Override
    public List<ActionRequest> getNeededRequests() {
        return Collections.singletonList(ActionRequest.FrontgroundDraw);
    }

    @Override
    public void drawFrontground(GuiIC2 gui, int mouseX, int mouseY) {
        gui.drawString("Current Production:", box.getX(), box.getY(), Color.cyan.hashCode());
        gui.drawString(tile.getProduction() + " EU/Tick", box.getX(), box.getY() + 8, Color.cyan.hashCode());
    }
}
