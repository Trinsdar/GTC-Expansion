package gtc_expansion.gui;

import gtclassic.api.interfaces.IGTMultiTileStatus;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.util.math.Box2D;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class GTCXGuiCompMultiblockStatusString extends GuiComponent {
    IGTMultiTileStatus tile;
    Box2D box;

    public GTCXGuiCompMultiblockStatusString(IGTMultiTileStatus tile, Box2D box) {
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
        String structureValid = tile.getStructureValid() ? "Structure is valid" : "Structure is invalid";
        gui.drawString(structureValid, box.getX(), box.getY(), Color.cyan.hashCode());
    }
}
