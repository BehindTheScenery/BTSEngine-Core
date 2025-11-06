package dev.behindthescenery.core.system.user_interface.imgui;

import dev.behindthescenery.core.system.user_interface.imgui.init.ReworkImGuiMain;
import dev.behindthescenery.core.system.user_interface.imgui.screen.rework.AbstractImGuiScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class ImGuiScreenWrapper extends Screen {

    private final ImGuiDrawElement imGuiDrawElement;

    public ImGuiScreenWrapper(ImGuiDrawElement imGuiDrawElement) {
        super(Text.empty());
        this.imGuiDrawElement = imGuiDrawElement;
    }

    @Override
    public void init() {
        if(imGuiDrawElement instanceof AbstractImGuiScreen screen) {
            screen.init();
        } else if (imGuiDrawElement instanceof dev.behindthescenery.core.system.user_interface.imgui.screen.AbstractImGuiScreen screen) {
            screen.onVanillaInitScreen();
        }
    }

    @Override
    public void render(@NotNull DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        ReworkImGuiMain.getInstance().addRender(() -> imGuiDrawElement.render(guiGraphics, mouseX, mouseY, partialTick));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }


}