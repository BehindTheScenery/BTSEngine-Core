package dev.behindthescenery.core.system.user_interface.imgui.editing.editors.basic;

import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassEditor;
import imgui.ImGui;
import imgui.type.ImBoolean;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BooleanClassEditor implements ImGuiClassEditor {

    @Override
    public Runnable createClassEditor(String name, Supplier<Object> input, Consumer<Object> onChange, float elementWidth, int flag) {
        return () -> {
            ImBoolean imInt = new ImBoolean(input.get() instanceof Boolean f ? f : false);

            if(elementWidth > 0)
                ImGui.setNextItemWidth(elementWidth);

            if(ImGui.checkbox(name, imInt)) {
                onChange.accept(imInt.get());
            }
        };
    }
}
