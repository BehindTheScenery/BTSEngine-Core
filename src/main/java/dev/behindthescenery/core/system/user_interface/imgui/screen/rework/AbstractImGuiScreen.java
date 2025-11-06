package dev.behindthescenery.core.system.user_interface.imgui.screen.rework;

import dev.behindthescenery.core.system.user_interface.imgui.*;
import dev.behindthescenery.core.system.user_interface.imgui.init.ReworkImGuiMain;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractImGuiScreen implements ImGuiDrawElement, ImGuiListenerSupport {
    private final String screenName;
    protected ImVec2 position;
    protected ImVec2 size;
    private final int flags;
    private final int condition;
    private boolean isVisible;
    private final ImBoolean closeFlag;
    private boolean isInitialized;

    private final Map<Class<? extends ImGuiEvent>, List<ImGuiEventListener<? extends ImGuiEvent>>> eventListeners = new HashMap<>();

    protected AbstractImGuiScreen(String name) {
        this(new Builder(name));
    }

    protected AbstractImGuiScreen(Builder builder) {
        this.screenName = builder.screenName;
        this.position = new ImVec2(builder.positionX, builder.positionY);
        this.size = new ImVec2(builder.sizeX, builder.sizeY);
        this.flags = builder.flags;
        this.condition = builder.condition;
        this.isVisible = builder.isVisible;
        this.closeFlag = new ImBoolean();
        this.isInitialized = false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        if (!isVisible) {
            return;
        }

        // Установка позиции и размера окна только при первой инициализации
        if (!isInitialized) {
            ImGui.setNextWindowPos(position.x, position.y, condition);
            ImGui.setNextWindowSize(size.x, size.y, condition);
            isInitialized = true;
        }

        // Начало рендера ImGui окна
        if (ImGui.begin(screenName, closeFlag, flags)) {
            renderContent(context, mouseX, mouseY, partialTick);
        }

        ImGui.end();

        // Обработка закрытия окна
        if (closeFlag.get() && MinecraftClient.getInstance().currentScreen instanceof ImGuiScreenWrapper wrapper) {
            wrapper.close();
        }
    }

    /**
     * Реализация рендера содержимого окна.
     */
    protected abstract void renderContent(DrawContext context, int mouseX, int mouseY, float partialTick);

    /**
     * Инициализация ресурсов.
     */
    @Override
    public void init() {
        ReworkImGuiMain.getInstance().setViewportsEnabled(false);
        isInitialized = false; // Сбрасываем флаг для повторной установки позиции и размера
    }

    /**
     * Освобождение ресурсов.
     */
    @Override
    public void close() {
        isVisible = false;
    }

    /**
     * Устанавливает видимость окна.
     */
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Override
    public Map<Class<? extends ImGuiEvent>, List<ImGuiEventListener<?  extends ImGuiEvent>>> getEventListenerMap() {
        return eventListeners;
    }



    /**
     * Builder для создания экземпляров AbstractImGuiScreen.
     */
    public static class Builder {
        private final String screenName;
        private float positionX = 0;
        private float positionY = 0;
        private float sizeX;
        private float sizeY;
        private int flags = 0;
        private int condition = ImGuiCond.Always;
        private boolean isVisible = true;

        public Builder(String screenName) {
            this.screenName = screenName;
            // Устанавливаем размер по умолчанию как размер окна Minecraft
            MinecraftClient client = MinecraftClient.getInstance();
            this.sizeX = client.getWindow().getScaledWidth();
            this.sizeY = client.getWindow().getScaledHeight();
        }

        public Builder setPosition(float x, float y) {
            this.positionX = x;
            this.positionY = y;
            return this;
        }

        public Builder setSize(float width, float height) {
            this.sizeX = Math.max(100, width); // Минимальный размер для избежания некорректного рендера
            this.sizeY = Math.max(100, height);
            return this;
        }

        public Builder setFullScreen() {
            MinecraftClient client = MinecraftClient.getInstance();
            this.sizeX = client.getWindow().getWidth();
            this.sizeY = client.getWindow().getHeight();
            return this;
        }

        public Builder addFlags(int flags) {
            this.flags |= flags;
            return this;
        }

        public Builder setCondition(int condition) {
            this.condition = condition;
            return this;
        }

        public Builder setVisible(boolean visible) {
            this.isVisible = visible;
            return this;
        }

        public AbstractImGuiScreen build() {
            return new AbstractImGuiScreen(this) {
                @Override
                protected void renderContent(DrawContext context, int mouseX, int mouseY, float partialTick) {
                    // Пустая реализация, должна быть переопределена
                }
            };
        }
    }
}
