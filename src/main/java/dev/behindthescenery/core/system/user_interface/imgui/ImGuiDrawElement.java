package dev.behindthescenery.core.system.user_interface.imgui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public interface ImGuiDrawElement {
    /**
     * Рендерит элементы ImGui в контексте Minecraft.
     * @param context Контекст рендеринга Minecraft.
     * @param mouseX Координата X мыши.
     * @param mouseY Координата Y мыши.
     * @param partialTick Дробная часть тика для интерполяции.
     */
    void render(DrawContext context, int mouseX, int mouseY, float partialTick);

    /**
     * Инициализация элемента перед открытием экрана.
     */
    default void init() {}

    /**
     * Очистка ресурсов при закрытии экрана.
     */
    default void close() {}

    /**
     * Открывает экран с рендерингом ImGui.
     */
    default void openScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> client.setScreen(new ImGuiScreenWrapper(this)));
        } else {
            client.setScreen(new ImGuiScreenWrapper(this));
        }
    }

    /**
     * Добавляет элемент в оверлей.
     * @param id Уникальный идентификатор оверлея.
     */
    default void addToOverlay(String id) {
        ImGuiOverlayWrapper.addOverlayElement(id, this);
    }

    /**
     * Удаляет элемент из оверлея.
     * @param id Уникальный идентификатор оверлея.
     */
    default void removeFromOverlay(String id) {
        ImGuiOverlayWrapper.removeOverlayElement(id);
    }
}
