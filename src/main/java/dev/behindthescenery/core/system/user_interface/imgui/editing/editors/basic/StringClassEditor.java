package dev.behindthescenery.core.system.user_interface.imgui.editing.editors.basic;

import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassEditor;
import imgui.ImGui;
import imgui.type.ImString;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringClassEditor implements ImGuiClassEditor {

    @Override
    public Runnable createClassEditor(String name, Supplier<Object> input, Consumer<Object> onChange, float elementWidth, int flag) {
        return () -> {
            ImString imInt = new ImString(input.get() instanceof String f ? f : "");

            if(elementWidth > 0)
                ImGui.setNextItemWidth(elementWidth);

            if(ImGui.inputText(name, imInt, flag)) {
                onChange.accept(imInt.get());
            }
        };
    }
}
