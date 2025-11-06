package dev.behindthescenery.core.system.user_interface.render;


import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public enum CursorType {

    ARROW(221185),
    IBEAM(221186),
    CROSSHAIR(221187),
    HAND(221188),
    HRESIZE(221189),
    VRESIZE(221190);

    private final int shape;
    private long cursor = 0L;


    private CursorType(int c) {
        this.shape = c;
    }

    public static void set(@Nullable CursorType type) {
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        if (type == null) {
            GLFW.glfwSetCursor(window, 0L);
        } else {
            if (type.cursor == 0L) {
                type.cursor = GLFW.glfwCreateStandardCursor(type.shape);
            }

            GLFW.glfwSetCursor(window, type.cursor);
        }
    }
}
