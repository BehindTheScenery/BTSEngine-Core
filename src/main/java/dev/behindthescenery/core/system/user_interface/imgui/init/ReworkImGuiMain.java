package dev.behindthescenery.core.system.user_interface.imgui.init;

import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.user_interface.imgui.ImGuiUiEditingContext;
import dev.behindthescenery.core.utils.ResourcesUtils;
import imgui.ImFontConfig;
import imgui.ImFontGlyphRangesBuilder;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiFreeTypeBuilderFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class ReworkImGuiMain {

    private static ReworkImGuiMain instance;

    public static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    public static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public boolean initDone = false;
    protected boolean viewportsEnabled = true;

    public final ImGuiUiEditingContext EDIT_CONTEXT = new ImGuiUiEditingContext();

    protected Queue<Runnable> renderables = new LinkedBlockingQueue<>();
  ;
    public ReworkImGuiMain() {
        instance = this;
        initImGui();
        initDone = true;
    }

    public static ReworkImGuiMain getInstance() {
        return instance;
    }

    public void setViewportsEnabled(boolean enabled) {
        if (!initDone) return;
        viewportsEnabled = enabled;
        ImGuiIO io = ImGui.getIO();
        if (enabled) {
            io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        } else {
            io.removeConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        }
    }

    private void initImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();

        initFonts(io);

        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable | ImGuiConfigFlags.DockingEnable);
        imGuiGlfw.init(MinecraftClient.getInstance().getWindow().getHandle(), true);
        imGuiGl3.init();
    }

    private void initFonts(ImGuiIO io){
        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesCyrillic());
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesJapanese());
        rangesBuilder.addRanges(FontAwesomeIcons._IconRange);

        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setOversampleH(4);
        fontConfig.setOversampleV(4);
        fontConfig.setPixelSnapH(true);
        fontConfig.addFontBuilderFlags(ImGuiFreeTypeBuilderFlags.Monochrome | ImGuiFreeTypeBuilderFlags.NoHinting);
        fontConfig.setFontDataOwnedByAtlas(true);

        final short[] glyphRanges = rangesBuilder.buildRanges();
        io.getFonts().addFontFromMemoryTTF(ResourcesUtils.readResourceBytes(Identifier.of(BtsCore.MOD_ID, "fonts/monocraft.ttf")), 18, fontConfig, glyphRanges);
        io.getFonts().build();
        fontConfig.destroy();
    }

    public void destroy() {
        if (!initDone) return;
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        ImGui.destroyContext();

        ReworkImGuiBuffer.destroyMainBuffer();
    }

    public void render() {
        if (!initDone) return;

//        ReworkImGuiBuffer.getMainRenderBuffer().clear(MinecraftClient.IS_SYSTEM_MAC);

        imGuiGlfw.newFrame();
        imGuiGl3.newFrame();
        ImGui.newFrame();

        while (!renderables.isEmpty()) {
            Runnable obj = renderables.poll();
            if(obj == null) continue;
            obj.run();
        }

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    public void addRender(Runnable runnable) {
        renderables.add(runnable);
    }

    public boolean isViewportsEnabled() {
        return viewportsEnabled;
    }
}
