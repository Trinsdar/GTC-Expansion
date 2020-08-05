package gtc_expansion.gui;

import gtc_expansion.tile.hatch.GTCXTileItemFluidHatches;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.platform.registry.Ic2GuiComp;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GTCXGuiCompFluidTank extends GuiComponent {
    GTCXTileItemFluidHatches hatch;

    public GTCXGuiCompFluidTank(GTCXTileItemFluidHatches hatch) {
        super(Ic2GuiComp.nullBox);
        this.hatch = hatch;
    }

    public List<ActionRequest> getNeededRequests() {
        return Arrays.asList(ActionRequest.FrontgroundDraw);
    }

    @SideOnly(Side.CLIENT)
    public void drawFrontground(GuiIC2 gui, int mouseX, int mouseY) {
        gui.drawString("Fluid Amount:", 11, 20, Color.cyan.hashCode());
        boolean second = hatch.isSecond();
        boolean input = hatch.isInput();
        int stored = this.hatch.getOwner() != null ? (input ? (second ? hatch.getOwner().getInputTank2().getFluidAmount() : hatch.getOwner().getInputTank1().getFluidAmount()) : (second ? hatch.getOwner().getOutputTank2().getFluidAmount() : hatch.getOwner().getOutputTank1().getFluidAmount())) : 0;
        gui.drawString("" + NumberFormat.getNumberInstance(Locale.US).format((long) stored), 11, 30, Color.cyan.hashCode());
    }
}
