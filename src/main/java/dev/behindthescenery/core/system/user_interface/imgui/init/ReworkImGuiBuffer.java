package dev.behindthescenery.core.system.user_interface.imgui.init;

import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.util.Window;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class ReworkImGuiBuffer {

    protected static final List<SimpleFramebuffer> RESIZE_LISTENERS = new ArrayList<>();

    private static SimpleFramebuffer MAIN_RENDER_BUFFER;
    private static SimpleFramebuffer FULL_SCREENS_RENDER_BUFFER;

    public static SimpleFramebuffer createBuffer(int wight, int height, boolean resizable) {
        final SimpleFramebuffer buffer = new SimpleFramebuffer(wight, height,
                false, MinecraftClient.IS_SYSTEM_MAC);

        if(resizable) {
            RESIZE_LISTENERS.add(buffer);
        }

        return buffer;
    }

    public static SimpleFramebuffer createBufferWithDepth(int wight, int height, boolean resizable) {
        final SimpleFramebuffer buffer = new SimpleFramebuffer(wight, height,
                true, MinecraftClient.IS_SYSTEM_MAC);

        if(resizable) {
            RESIZE_LISTENERS.add(buffer);
        }

        return buffer;
    }

    public static void resizeBuffers() {
        final Window window = MinecraftClient.getInstance().getWindow();
        resizeBuffers(window.getFramebufferWidth(), window.getFramebufferHeight());
    }

    public static void resizeBuffers(int wight, int height) {
        synchronized (RESIZE_LISTENERS) {
            for (SimpleFramebuffer resizeListener : RESIZE_LISTENERS) {
                resizeListener.resize(wight, height, MinecraftClient.IS_SYSTEM_MAC);
            }
        }
    }

    public static void destroyBuffers() {
        synchronized (RESIZE_LISTENERS) {
            for (SimpleFramebuffer resizeListener : RESIZE_LISTENERS) {
                resizeListener.delete();
            }
        }
    }

    public static SimpleFramebuffer getMainRenderBuffer() {
        if(MAIN_RENDER_BUFFER == null) {
            final Window window = MinecraftClient.getInstance().getWindow();
            MAIN_RENDER_BUFFER = createBufferWithDepth(window.getWidth(), window.getHeight(), true);
        }

        return MAIN_RENDER_BUFFER;
    }

    public static void destroyMainBuffer() {
        if(MAIN_RENDER_BUFFER == null) return;

        synchronized (RESIZE_LISTENERS) {
            RESIZE_LISTENERS.remove(MAIN_RENDER_BUFFER);
        }

        MAIN_RENDER_BUFFER.delete();
        MAIN_RENDER_BUFFER = null;
    }

    public static SimpleFramebuffer getFullScreensRenderBuffer() {
        if(FULL_SCREENS_RENDER_BUFFER == null) {
            final Vector2i size = BtsRenderSystem.getFrameBufferSize();
            FULL_SCREENS_RENDER_BUFFER = createBufferWithDepth(size.x, size.y, false);
        }

        return FULL_SCREENS_RENDER_BUFFER;
    }

    public static void destroyFullScreensRenderBuffer() {
        if(FULL_SCREENS_RENDER_BUFFER == null) return;
        FULL_SCREENS_RENDER_BUFFER.delete();
        FULL_SCREENS_RENDER_BUFFER = null;
    }
}
