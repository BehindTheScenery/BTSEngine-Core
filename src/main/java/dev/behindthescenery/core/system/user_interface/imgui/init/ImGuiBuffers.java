package dev.behindthescenery.core.system.user_interface.imgui.init;

import dev.behindthescenery.core.system.user_interface.imgui.ImGuiDrawElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class ImGuiBuffers {
    private static final Map<Class<?>, List<ImGuiDrawElement>> SCREEN_RENDERS = new HashMap<>();
    private static SimpleFramebuffer imguiBuffer;

    public static void initialize() {
        if (imguiBuffer == null) {
            var window = MinecraftClient.getInstance().getWindow();
            imguiBuffer = new SimpleFramebuffer(window.getWidth(), window.getHeight(), true, MinecraftClient.IS_SYSTEM_MAC);
        }
    }

    public static void resize(int width, int height) {
        if (imguiBuffer != null) {
            imguiBuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
        }
    }

    public static void destroy() {
        if (imguiBuffer != null) {
            imguiBuffer.delete();
            imguiBuffer = null;
        }
    }

    public static SimpleFramebuffer getBuffer() {
        if (imguiBuffer == null) {
            initialize();
        }
        return imguiBuffer;
    }

    public static void addScreenRender(Class<?> cls, ImGuiDrawElement renderable) {
        List<ImGuiDrawElement> list = SCREEN_RENDERS.computeIfAbsent(cls, k -> new ArrayList<>());
        if (!list.contains(renderable)) {
            list.add(renderable);
        }
    }

    public static void removeScreenRender(Class<?> cls, ImGuiDrawElement renderable) {
        List<ImGuiDrawElement> list = SCREEN_RENDERS.getOrDefault(cls, new ArrayList<>());
        list.remove(renderable);
        if (list.isEmpty()) {
            SCREEN_RENDERS.remove(cls);
        } else {
            SCREEN_RENDERS.put(cls, list);
        }
    }

    public static Map<Class<?>, List<ImGuiDrawElement>> getScreenRenders() {
        return SCREEN_RENDERS;
    }
}