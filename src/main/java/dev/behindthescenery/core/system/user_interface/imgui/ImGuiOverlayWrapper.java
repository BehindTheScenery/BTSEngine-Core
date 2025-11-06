package dev.behindthescenery.core.system.user_interface.imgui;

import dev.behindthescenery.core.system.user_interface.imgui.init.ImGuiBuffers;
import dev.behindthescenery.core.system.user_interface.imgui.init.ReworkImGuiMain;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;

import java.util.HashMap;
import java.util.Map;

public class ImGuiOverlayWrapper {

    private static Map<String, ImGuiDrawElement> OVERLAY_ELEMENTS = new HashMap<>();


    public static void addOverlayElement(String id, ImGuiDrawElement element) {
        final var window = MinecraftClient.getInstance().getWindow();
        final SimpleFramebuffer framebuffer = ImGuiBuffers.getBuffer();
        if(framebuffer.textureWidth != window.getWidth() || framebuffer.textureHeight != window.getHeight()) {
            framebuffer.resize(window.getWidth(), window.getHeight(), MinecraftClient.IS_SYSTEM_MAC);
        }
        OVERLAY_ELEMENTS.put(id, element);
    }

    public static void removeOverlayElement(String id) {
        OVERLAY_ELEMENTS.remove(id);
    }

    public static ImGuiDrawElement getByID(String id) {
        return OVERLAY_ELEMENTS.get(id);
    }

    public static void renderAll(DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (ImGuiDrawElement value : OVERLAY_ELEMENTS.values()) {
            ReworkImGuiMain.getInstance().addRender(() -> value.render(guiGraphics, mouseX, mouseY, partialTick));
        }
    }

    private static final int IMGUI_FLAG = ImGuiWindowFlags.NoMouseInputs | ImGuiWindowFlags.NoInputs;

}
