package dev.behindthescenery.core.system.user_interface.imgui;


import dev.behindthescenery.core.system.user_interface.imgui.extern.ImGuiExtension;

/**
 * Флаги для рендеринга с ImGui {@link ImGuiExtension}
 */
public class ImGuiBTSFlags {

    public static final int ALWAYS_ON_TOP = 1;
    public static final int BORDER = 2;
    public static final int ENABLE_SCISSOR = 3;
    public static final int DISABLE_RESIZE = 4;
    public static final int RENDER_TOOLTIP = 5;

    /**
     * Только для {@link ImGuiExtension#reworkDrawMcRender}
     */
    public static final int USE_MINECRAFT_MATRIX = 6;
}
