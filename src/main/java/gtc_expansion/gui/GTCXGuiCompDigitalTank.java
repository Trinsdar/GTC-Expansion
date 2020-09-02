package gtc_expansion.gui;

import gtc_expansion.data.GTCXLang;
import gtclassic.api.gui.GTGuiButton;
import gtclassic.api.helpers.GTHelperMath;
import gtclassic.common.GTLang;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.util.math.Box2D;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class GTCXGuiCompDigitalTank extends GuiComponent {

    TileEntityMachine block;
    private static final Box2D BOX = new Box2D(7, 63, 36, 18);

    public GTCXGuiCompDigitalTank(TileEntityMachine tile) {
        super(BOX);
        this.block = tile;
    }

    @Override
    public List<ActionRequest> getNeededRequests() {
        return Arrays.asList(ActionRequest.GuiInit, ActionRequest.ButtonNotify, ActionRequest.ToolTip);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onGuiInit(GuiIC2 gui) {
        gui.registerButton((new GTGuiButton(0, bX(gui, 7), bY(gui, 63), 18, 20)));
        gui.registerButton((new GTGuiButton(1, bX(gui, 25), bY(gui, 63), 18, 20)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onButtonClick(GuiIC2 gui, GuiButton button) {
        if (button.id == 0) {
            this.block.getNetwork().initiateClientTileEntityEvent(this.block, 0);
        }
        if (button.id == 1) {
            this.block.getNetwork().initiateClientTileEntityEvent(this.block, 1);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onToolTipCollecting(GuiIC2 gui, int mouseX, int mouseY, List<String> tooltips) {
        if (this.isMouseOver(mouseX, mouseY)) {
            if (mouseX < 25) {
                tooltips.add(I18n.format(GTCXLang.BUTTON_DIGITALTANK_UPLOAD));
            }
            if (GTHelperMath.within(mouseX, 25, 42)) {
                tooltips.add(I18n.format(GTLang.BUTTON_DIGITALCHEST_DOWNLOAD));
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
