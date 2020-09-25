package gtc_expansion.render;

import gtc_expansion.data.GTCXLang;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;

public class GTCXGuiFirstLoad extends GuiScreen {
    protected String title;
    protected final GuiScreen parent;

    public GuiButton backButton;

    public GTCXGuiFirstLoad(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonList.clear();
        title = I18n.format("title.gtc_expansion.first_load");

        int x = width / 2 - 100;
        int y = height / 6;

        buttonList.add(backButton = new GuiButton(0, x, y + 167, 200, 20, I18n.format(GTCXLang.BUTTON_BACK_TO_MAINMENU)));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, TextFormatting.BOLD + title, width / 2, 15, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);

        int x = width / 2;
        int y = height / 6;
        for(int i = 0; i < 9; i++) {
            String s = I18n.format("info.gtc_expansion.first_load" + i);

            drawCenteredString(fontRenderer, s, x, y, 0xFFFFFF);

            y += 10;
            if(i == 6 || i == 8)
                y += 8;
        }
        drawCenteredString(fontRenderer, "Trinsdar", x, y, 0xFFFFFF);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) // Esc
            returnToParent();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if(backButton != null && button == backButton)
            returnToParent();
    }

    void returnToParent() {
        mc.displayGuiScreen(parent);

        if(mc.currentScreen == null)
            mc.setIngameFocus();
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if(id == 0) {
            mc.displayGuiScreen(this);
        }
    }
}
