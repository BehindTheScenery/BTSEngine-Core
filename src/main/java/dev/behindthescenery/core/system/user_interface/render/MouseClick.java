package dev.behindthescenery.core.system.user_interface.render;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public enum MouseClick {
    LEFT,
    RIGHT,
    MIDDLE,
    NONE;

    public static MouseClick from(int buttonID) {
        return switch (buttonID) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT -> LEFT;
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> RIGHT;
            case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> MIDDLE;
            default -> NONE;
        };
    }

    public boolean isLeft() {
        return this == LEFT;
    }

    public boolean isRight() {
        return this == RIGHT;
    }

    public boolean isMiddle() {
        return this == MIDDLE;
    }

    public static MouseClick getMouseClick(){
        if(MinecraftClient.getInstance().mouse.wasLeftButtonClicked())
            return LEFT;
        else if(MinecraftClient.getInstance().mouse.wasRightButtonClicked())
            return RIGHT;
        else if(MinecraftClient.getInstance().mouse.wasMiddleButtonClicked())
            return MIDDLE;
        return NONE;
    }

    public boolean isPressed() {
        return this != NONE;
    }
}
