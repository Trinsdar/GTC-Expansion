package gtc_expansion.gui;

import gtc_expansion.tile.steam.GTCXTileCoalBoiler;
import gtclassic.api.helpers.GTHelperMath;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.util.math.Box2D;
import net.minecraft.client.resources.I18n;

import java.util.Arrays;
import java.util.List;

public class GTCXGuiCompCoalBoiler extends GuiComponent {
    GTCXTileCoalBoiler tile;
    public GTCXGuiCompCoalBoiler(GTCXTileCoalBoiler tile) {
        super(new Box2D(70, 25, 36, 54));
        this.tile = tile;
    }

    @Override
    public List<ActionRequest> getNeededRequests() {
        return Arrays.asList(ActionRequest.BackgroundDraw, ActionRequest.ToolTip);
    }

    @Override
    public void drawBackground(GuiIC2 gui, int mouseX, int mouseY, float particalTicks) {
        int x = gui.getXOffset();
        int y = gui.getYOffset();
        float heat = this.tile.getHeat();
        if (heat >= 1.0F) {
            float per = heat / this.tile.getMaxHeat();
            if (per > 1.0F) {
                per = 1.0F;
            }
            Box2D box = new Box2D(96, 25, 10, 54);
            int maxY = box.getHeight();
            int lvl = (int) (per * (float) maxY);
            if (lvl < 0) {
                return;
            }
            int y2 = (y + box.getY() + box.getHeight()) - lvl;
            int texY = (box.getHeight()) - lvl;
            gui.drawTexturedModalRect(x + box.getX(), y2, 214, texY, box.getLenght(), lvl);
        }
        float steam = this.tile.getSteam().getFluidAmount();
        if (steam >= 1.0F) {
            float per = steam / this.tile.getSteam().getCapacity();
            if (per > 1.0F) {
                per = 1.0F;
            }
            Box2D box = new Box2D(70, 25, 10, 54);
            int maxY = box.getHeight();
            int lvl = (int) (per * (float) maxY);
            if (lvl < 0) {
                return;
            }
            int y2 = (y + box.getY() + box.getHeight()) - lvl;
            int texY = (box.getHeight()) - lvl;
            gui.drawTexturedModalRect(x + box.getX(), y2, 194, texY, box.getLenght(), lvl);
        }
        float water = this.tile.getWater().getFluidAmount();
        if (water >= 1.0F) {
            float per = water / this.tile.getWater().getCapacity();
            if (per > 1.0F) {
                per = 1.0F;
            }
            Box2D box = new Box2D(83, 25, 10, 54);
            int maxY = box.getHeight();
            int lvl = (int) (per * (float) maxY);
            if (lvl < 0) {
                return;
            }
            int y2 = (y + box.getY() + box.getHeight()) - lvl;
            int texY = (box.getHeight()) - lvl;
            gui.drawTexturedModalRect(x + box.getX(), y2, 204, texY, box.getLenght(), lvl);
        }
    }

    @Override
    public void onToolTipCollecting(GuiIC2 gui, int mouseX, int mouseY, List<String> tooltips) {
        if (this.isMouseOver(mouseX, mouseY)){
            if (GTHelperMath.within(mouseX, 70, 80)){
                String fluid = tile.getSteam().getFluid() != null ? tile.getSteam().getFluidAmount() + " mb of " + tile.getSteam().getFluid().getLocalizedName() : "Empty";
                tooltips.add(fluid);
            } else if (GTHelperMath.within(mouseX, 83, 93)){
                String fluid = tile.getWater().getFluid() != null ? tile.getWater().getFluidAmount() + " mb of " + tile.getWater().getFluid().getLocalizedName() : "Empty";
                tooltips.add(fluid);
            } else if (mouseX >= 96){
                tooltips.add(tile.getHeat() + "K");
            }
        }
    }
}
