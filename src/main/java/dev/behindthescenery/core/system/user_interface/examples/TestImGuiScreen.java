package dev.behindthescenery.core.system.user_interface.examples;

import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.user_interface.imgui.ImGuiDrawElement;
import dev.behindthescenery.core.system.user_interface.imgui.extern.ImGuiExtension;
import imgui.ImGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Items;

/**
 * Чтобы открыть экран вызовите {@link ImGuiDrawElement#openScreenImGui()}
 */
public class TestImGuiScreen implements ImGuiDrawElement {

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        RGBA.of(0,255,0, 255).draw(guiGraphics,  100, 100, 100, 100);

        ImGui.begin("HelloTest");
        ImGuiExtension.drawItem("TestItem", Items.DIAMOND.getDefaultStack(), 128,128);
        ImGui.pushID("TestRender");

//        ImGuiExtension.reworkDrawMcRender(256,256, 0, RGBA.DEFAULT, (
//            (pos, clicked) -> {
//
////                ImGuiGLRenderHelper.renderItemStack(Items.DIAMOND.getDefaultStack(), guiGraphics.getMatrices(),
////                        (int) pos.x, (int) pos.y, 256, 256, 1f, 0);
//
//                guiGraphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, "Hello World", (int) pos.x, (int) pos.y, 100);
//                RGBA.of(255, 0, 0, 255).drawRoundFill(guiGraphics, (int) pos.x, (int) (pos.y + 40), 20, 20, 6);
//            }
//        ));
        ImGui.popID();

        ImGui.end();

    }
}
