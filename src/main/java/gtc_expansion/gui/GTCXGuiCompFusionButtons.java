package gtc_expansion.gui;

import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import gtclassic.api.gui.GTGuiButton;
import gtclassic.api.helpers.GTHelperMath;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.util.math.Box2D;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class GTCXGuiCompFusionButtons extends GuiComponent {
    GTCXTileMultiFusionReactor block;
    public GTCXGuiCompFusionButtons(GTCXTileMultiFusionReactor reactor) {
        super(new Box2D(154, 22, 18, 54));
        this.block = reactor;
    }

    @Override
    public List<ActionRequest> getNeededRequests() {
        return Arrays.asList(ActionRequest.BackgroundDraw, ActionRequest.ButtonNotify, ActionRequest.ToolTip, ActionRequest.GuiInit);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiIC2 gui, int mouseX, int mouseY, float particalTicks) {
        if (block.getGuiOverlay() == 0){
            gui.drawTexturedModalRect(gui.getXOffset() + this.getPosition().getX(), gui.getYOffset() + this.getPosition().getY(), 176, 0, 18, 18);
        } else if (block.getGuiOverlay() == 1) {
            gui.drawTexturedModalRect(gui.getXOffset() + this.getPosition().getX(), gui.getYOffset() + this.getPosition().getY() + 18, 176, 18, 18, 18);
        } else if (this.block.getGuiOverlay() == 2){
            gui.drawTexturedModalRect(gui.getXOffset() + this.getPosition().getX(), gui.getYOffset() + this.getPosition().getY() + 36, 176, 36, 18, 18);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onGuiInit(GuiIC2 gui) {
        gui.registerButton((new GTGuiButton(0, bX(gui, 154), bY(gui, 20), 18, 20)));
        gui.registerButton((new GTGuiButton(1, bX(gui, 154), bY(gui, 38), 18, 20)));
        gui.registerButton((new GTGuiButton(2, bX(gui, 154), bY(gui, 56), 18, 20)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onButtonClick(GuiIC2 gui, GuiButton button) {
        if (button.id >= 0 && button.id < 3) {
            this.block.getNetwork().initiateClientTileEntityEvent(this.block, button.id);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onToolTipCollecting(GuiIC2 gui, int mouseX, int mouseY, List<String> tooltips) {
        if (this.isMouseOver(mouseX, mouseY)) {
            if (mouseY < 40) {
                tooltips.add(I18n.format("button.gtc_expansion.full"));
            }
            if (GTHelperMath.within(mouseY, 40, 57)) {
                tooltips.add(I18n.format("button.gtc_expansion.middle"));
            }
            if (mouseY > 57){
                tooltips.add(I18n.format("button.gtc_expansion.topbottom"));
            }

        }
    }

    private int bX(GuiIC2 gui, int position) {
        return gui.getXOffset() + position;
    }

    private int bY(GuiIC2 gui, int position) {
        return gui.getYOffset() + position;
    }
}
