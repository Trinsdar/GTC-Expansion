package gtc_expansion.gui;

import gtc_expansion.GTCExpansion;
import gtc_expansion.tile.multi.GTCXTileMultiFusionReactor;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.util.math.Box2D;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class GTCXGuiCompFusionOverlay extends GuiComponent {
    private static final ResourceLocation TOP = new ResourceLocation(GTCExpansion.MODID, "textures/gui/fusioncomputertopbottomoverlay.png");
    private static final ResourceLocation MIDDLE = new ResourceLocation(GTCExpansion.MODID, "textures/gui/fusioncomputermiddleoverlay.png");
    GTCXTileMultiFusionReactor block;
    public GTCXGuiCompFusionOverlay(GTCXTileMultiFusionReactor reactor) {
        super(new Box2D(6, 6, 145, 145));
        this.block = reactor;
    }

    public List<ActionRequest> getNeededRequests() {
        return Arrays.asList(ActionRequest.BackgroundDraw);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(GuiIC2 gui, int mouseX, int mouseY, float particalTicks) {
        if (block.getGuiOverlay() == 1) {
            gui.mc.getTextureManager().bindTexture(MIDDLE);
            gui.drawTexturedModalRect(gui.getXOffset() + this.getPosition().getX(), gui.getYOffset() + this.getPosition().getY(), 0, 0, this.getPosition().getLenght(), this.getPosition().getHeight());
            gui.mc.getTextureManager().bindTexture(block.getGuiTexture());
        } else if (this.block.getGuiOverlay() == 2){
            gui.mc.getTextureManager().bindTexture(TOP);
            gui.drawTexturedModalRect(gui.getXOffset() + this.getPosition().getX(), gui.getYOffset() + this.getPosition().getY(), 0, 0, this.getPosition().getLenght(), this.getPosition().getHeight());
            gui.mc.getTextureManager().bindTexture(block.getGuiTexture());
        }

    }
}
