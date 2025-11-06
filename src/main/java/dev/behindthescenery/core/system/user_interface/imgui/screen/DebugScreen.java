package dev.behindthescenery.core.system.user_interface.imgui.screen;

import net.minecraft.client.gui.DrawContext;

public class DebugScreen extends AbstractImGuiScreen{


    public DebugScreen() {
        super("debug_screen");
    }

    @Override
    protected void initImGuiScreen(int imGuiFlags, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
//        ImGui.getStyle().setItemSpacing(new ImVec2(6,4));
//        super.initImGuiScreen(imGuiFlags, guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void render(int imGuiFlags, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {

    }
}
