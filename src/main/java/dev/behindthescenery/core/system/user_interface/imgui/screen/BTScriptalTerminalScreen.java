package dev.behindthescenery.core.system.user_interface.imgui.screen;

import dev.behindthescenery.core.system.user_interface.imgui.modules.BTScriptalTerminal;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Function;

public class BTScriptalTerminalScreen extends AbstractImGuiScreen {

    public BTScriptalTerminal terminal;

    public BTScriptalTerminalScreen(Function<String, String> execCommant) {
        super("BTScriptal");
        this.terminal = new BTScriptalTerminal(execCommant);
    }

    @Override
    protected void initImGuiScreen(int imGuiFlags, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        setScreenSize(200, 350);
        super.initImGuiScreen(imGuiFlags, guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void render(int imGuiFlags, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        terminal.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
