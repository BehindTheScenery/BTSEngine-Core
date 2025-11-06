package dev.behindthescenery.core.system.user_interface.imgui.editing.editors.basic;

import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassEditor;
import imgui.ImGui;
import imgui.type.ImFloat;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatClassEditor implements ImGuiClassEditor {

    @Override
    public Runnable createClassEditor(String name, Supplier<Object> input, Consumer<Object> onChange, float elementWidth, int flag) {
        return () -> {
            ImFloat imInt = new ImFloat(input.get() instanceof Float f ? f : 0);

            if(elementWidth > 0)
                ImGui.setNextItemWidth(elementWidth);

            if(ImGui.inputFloat(name, imInt)) {
                onChange.accept(imInt.get());
            }
        };
    }
}
