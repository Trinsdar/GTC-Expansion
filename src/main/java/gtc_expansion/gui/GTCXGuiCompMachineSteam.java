package gtc_expansion.gui;

import gtc_expansion.tile.base.GTCXTileBaseSteamMachine;
import gtclassic.api.helpers.GTHelperMath;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.platform.registry.Ic2GuiComp;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class GTCXGuiCompMachineSteam extends GuiComponent {
    GTCXTileBaseSteamMachine block;
    int x;
    int y;
    int xPos;
    int yPos;

    public GTCXGuiCompMachineSteam(GTCXTileBaseSteamMachine tile) {
        this(tile, 80, 53);
    }

    public GTCXGuiCompMachineSteam(GTCXTileBaseSteamMachine tile, int x, int y) {
        this(tile, x, y, 176, 54);
    }

    public GTCXGuiCompMachineSteam(GTCXTileBaseSteamMachine tile, int x, int y, int xPos, int yPos) {
        super(Ic2GuiComp.nullBox);
        this.block = tile;
        this.x = x;
        this.y = y;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public List<ActionRequest> getNeededRequests() {
        return Arrays.asList(ActionRequest.BackgroundDraw, ActionRequest.ToolTip);
    }

    @SideOnly(Side.CLIENT)
    public void drawBackground(GuiIC2 gui, int mouseX, int mouseY, float particalTicks) {
        if (this.block.getSteam().getFluidAmount() < this.block.getSteamPerTick()) {
            gui.drawTexturedModalRect(gui.getXOffset() + this.x, gui.getYOffset() + this.y, this.xPos, this.yPos, 18, 18);
        }

    }

    @SideOnly(Side.CLIENT)
    public void onToolTipCollecting(GuiIC2 gui, int mouseX, int mouseY, List<String> tooltips) {
        if (GTHelperMath.within(mouseX, this.x, this.x + 18) && GTHelperMath.within(mouseY, this.y, this.y + 18) && this.block.getSteam().getFluidAmount() < this.block.getSteamPerTick()) {
            tooltips.add(I18n.format("button.gtc_expansion.nosteam", new Object[0]));
        }

    }
}
