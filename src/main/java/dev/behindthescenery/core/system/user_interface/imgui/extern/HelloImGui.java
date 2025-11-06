package dev.behindthescenery.core.system.user_interface.imgui.extern;

import imgui.ImGui;
import imgui.ImVec2;

public class HelloImGui extends ImGui {

    public static float emSize() {
        return getFontSize();
    }

    public static float emSize(float nbLines) {
        return emSize() * nbLines;
    }

    public static ImVec2 emToVec2(ImVec2 vec2) {
        return new ImVec2(vec2.x, vec2.y);
    }

    public static ImVec2 emToVec2(float x, float y) {
        float k = getFontSize();
        return new ImVec2(x * k, y * k);
    }

    public static ImVec2 pixelsToEm(ImVec2 pixels) {
        float k = getFontSize();
        return new ImVec2(pixels.x / k, pixels.y / k);
    }

    public static float pixelsToEm(float pixels) {
        float k = getFontSize();
        return pixels / k;
    }

    public static void beginGroupColumn() {
        ImGui.beginGroup();
    }

    public static void endGroupColumn() {
        ImGui.endGroup();
        ImGui.sameLine();
    }
}
