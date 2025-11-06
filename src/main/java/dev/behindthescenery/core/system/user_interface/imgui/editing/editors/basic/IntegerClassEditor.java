package dev.behindthescenery.core.system.user_interface.imgui.editing.editors.basic;

import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassEditor;
import imgui.ImGui;
import imgui.type.ImInt;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntegerClassEditor implements ImGuiClassEditor {

    @Override
    public Runnable createClassEditor(String name, Supplier<Object> input, Consumer<Object> onChange, float elementWidth, int flag) {
        return () -> {
            ImInt imInt = new ImInt(input.get() instanceof Integer f ? f : 0);

            if(elementWidth > 0)
                ImGui.setNextItemWidth(elementWidth);

            if(ImGui.inputInt(name, imInt, 0, 0)) {
                onChange.accept(imInt.get());
            }
        };
    }
}
