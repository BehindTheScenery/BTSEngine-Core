package dev.behindthescenery.core.system.user_interface.imgui.init;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.user_interface.imgui.ImGuiDrawElement;
import dev.behindthescenery.core.utils.ResourcesUtils;
import imgui.*;
import imgui.extension.imnodes.ImNodes;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;

@Deprecated
public class ImguiMain {
    private static final ImguiMain INSTANCE = new ImguiMain();

    private final ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiImplGl3 = new ImGuiImplGl3();
    private long windowHandle = 0;
    private boolean isInitialized = false;
    private boolean callbackInput = false;

    private ImguiMain() {}

    public static ImguiMain getInstance() {
        return INSTANCE;
    }

    public void initialize(long handle) {
        if (isInitialized) {
            BtsCore.LOGGER.warn("ImGui is already initialized");
            return;
        }

        try {
            BtsCore.LOGGER.info("Initializing ImGui with window handle: {}", handle);
            windowHandle = handle;
            initializeImGui();
            imGuiImplGlfw.init(handle, true);
            imGuiImplGl3.init(MinecraftClient.IS_SYSTEM_MAC ? "#version 120" : "#version 430");
            ImNodes.createContext();
            ImGui.styleColorsDark();
            isInitialized = true;
            BtsCore.LOGGER.info("ImGui initialization completed");
        } catch (Exception e) {
            BtsCore.LOGGER.error("Failed to initialize ImGui", e);
            throw new RuntimeException("ImGui initialization failed", e);
        }
    }

    private void initializeImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.addBackendFlags(ImGuiBackendFlags.HasSetMousePos);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard | ImGuiConfigFlags.DockingEnable | ImGuiConfigFlags.ViewportsEnable);

        initFonts(io);

        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            ImGuiStyle style = ImGui.getStyle();
            style.setWindowRounding(0.0f);
            style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1f));
        }
    }

    private void initFonts(ImGuiIO io) {
        ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesCyrillic());
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesJapanese());
        rangesBuilder.addRanges(FontAwesomeIcons._IconRange);

        ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setOversampleH(4);
        fontConfig.setOversampleV(4);
        fontConfig.setPixelSnapH(true);

        try {
            short[] glyphRanges = rangesBuilder.buildRanges();
            io.getFonts().addFontFromMemoryTTF(
                    ResourcesUtils.readResourceBytes(Identifier.of(BtsCore.MOD_ID, "fonts/perfectdosvga437.ttf")),
                    18,
                    fontConfig,
                    glyphRanges
            );
            io.getFonts().build();
        } finally {
            fontConfig.destroy();
        }
    }

    public void setCallback(boolean value) {
//        if (!isInitialized) return;
//
//        if (value && !callbackInput) {
//            imGuiImplGlfw.installCallbacks(windowHandle);
//            callbackInput = true;
//        } else if (!value && callbackInput) {
//            imGuiImplGlfw.restoreCallbacks(windowHandle);
//            callbackInput = false;
//        }
    }

    public void drawFrame(int imGuiFlags, ImGuiDrawElement renderable, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isInitialized) return;

//        ImGuiBuffers.setBuffer(ImGuiBuffers.Type.SCREEN);
        SimpleFramebuffer buffer = ImGuiBuffers.getBuffer();
        buffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);

        RenderSystem.recordRenderCall(() -> {
            imGuiImplGlfw.newFrame();
            ImGui.newFrame();
            ImGui.setNextWindowViewport(ImGui.getMainViewport().getID());
            endFrame();
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        });
    }

    public void drawFrames(int imGuiFlags, Collection<ImGuiDrawElement> renderables, DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isInitialized) return;

//        ImGuiBuffers.setBuffer(ImGuiBuffers.Type.SCREEN);
        SimpleFramebuffer buffer = ImGuiBuffers.getBuffer();
        buffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);

        RenderSystem.recordRenderCall(() -> {
            imGuiImplGlfw.newFrame();
            ImGui.newFrame();
            ImGui.setNextWindowViewport(ImGui.getMainViewport().getID());
            renderables.forEach(r -> r.render(guiGraphics, mouseX, mouseY, partialTick));
            endFrame();
        });
    }

    private void endFrame() {
        ImGui.render();
        imGuiImplGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    public void destroy() {
        if (!isInitialized) return;

        try {
            imGuiImplGl3.shutdown();
            imGuiImplGlfw.shutdown();
            ImNodes.destroyContext();
            ImGui.destroyContext();
            isInitialized = false;
            BtsCore.LOGGER.info("ImGui context destroyed");
        } catch (Exception e) {
            BtsCore.LOGGER.error("Failed to destroy ImGui context", e);
        }
    }
}