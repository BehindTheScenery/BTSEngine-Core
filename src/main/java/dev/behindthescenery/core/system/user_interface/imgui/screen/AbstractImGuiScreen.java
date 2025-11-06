package dev.behindthescenery.core.system.user_interface.imgui.screen;

import dev.behindthescenery.core.system.user_interface.imgui.ImGuiBTSFlags;
import dev.behindthescenery.core.system.user_interface.imgui.ImGuiDrawElement;
import dev.behindthescenery.core.system.user_interface.imgui.ImGuiScreenWrapper;
import dev.behindthescenery.core.system.user_interface.imgui.init.ReworkImGuiMain;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class AbstractImGuiScreen implements ImGuiDrawElement {
    protected final MinecraftClient minecraft = MinecraftClient.getInstance();

    protected final String screenName;
    protected boolean showScreen;
    protected ImVec2 screenSize = new ImVec2(ImGui.getIO().getDisplaySizeX(), ImGui.getIO().getDisplaySizeY());
    protected ImVec2 position = new ImVec2(0, 0);
    protected boolean setPosition = false;
    protected boolean setSize = false;

    /**
     * {@link imgui.flag.ImGuiCond}
     */
    protected int imGuiCond = ImGuiCond.Always;

    /**
     * {@link ImGuiBTSFlags}
     */
    protected int imGuiFlags;

    public AbstractImGuiScreen(String screenName) {
        this(screenName, true);
    }

    /**
     *
     * @param screenName Имя окна будет участвовать при создании {@link ImGui#begin(String index)}, главное чтобы у нескольких открытых окон не было одинаковых ID
     * @param showScreen Если true, то окно будет отображать если false, то оно отображаться не будет.
     */
    public AbstractImGuiScreen(String screenName, boolean showScreen) {
        this.screenName = screenName;
        this.showScreen = showScreen;
    }

    /**
     * Устанавливает размер окна во весь экран
     */
    public AbstractImGuiScreen setFullScreen() {
        this.screenSize.set(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight());
        return this;
    }

    /**
     * Устанавливает размер окна
     */
    public AbstractImGuiScreen setScreenSize(int w, int h) {
        this.screenSize.set(w,h);
        return this;
    }

    /**
     * Добавляет Флаги для окна
     */
    public AbstractImGuiScreen addFlags(int imGuiFlags) {
        this.imGuiFlags |= imGuiFlags;
        return this;
    }

    /**
     * Устанавливает позицию окна
     */
    public AbstractImGuiScreen setPos(int x, int y) {
        this.position = new ImVec2(x, y);
        return this;
    }

    public String getNameOnImGui() {
        return screenName + "_imscreen";
    }

    public ImVec2 getScreenSize() {
        return screenSize;
    }

    public ImVec2 getPosition() {
        return position;
    }

    public String getScreenName() {
        return screenName;
    }

    public final void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        preInitImGuiScreen(this.imGuiFlags, guiGraphics, mouseX, mouseY, partialTick);
        initImGuiScreen(this.imGuiFlags, guiGraphics, mouseX, mouseY, partialTick);
        postInitImGuiScreen(this.imGuiFlags, guiGraphics, mouseX, mouseY, partialTick);
    }

    protected void preInitImGuiScreen(int imGuiFlags, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {}
    protected void postInitImGuiScreen(int imGuiFlags, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {}

    protected void initImGuiScreen(int imGuiFlags, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {

        if(showScreen) {
            ImBoolean close = new ImBoolean();

            if(!setPosition) {
                ImGui.setNextWindowPos(position.x, position.y, imGuiCond);
                setPosition = true;
            }
            if(!setSize) {
                ImGui.setNextWindowSize(screenSize.x, screenSize.y, imGuiCond);
                setSize = true;
            }

            if (ImGui.begin(getNameOnImGui(), close, imGuiFlags)) {
                render(imGuiFlags, guiGraphics, mouseX, mouseY, partialTick);
            }
            ImGui.end();

            onScreenClose(close);
        }
    }

    protected void onScreenClose(ImBoolean value) {
        if(value.get()) {
            if(MinecraftClient.getInstance().currentScreen instanceof ImGuiScreenWrapper screenWrapper) {
                screenWrapper.close();
            }
        }
    }

    public void onVanillaInitScreen() {
        ReworkImGuiMain.getInstance().setViewportsEnabled(false);
    }

    public abstract void render(int imGuiFlags, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick);
}
