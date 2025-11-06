package dev.behindthescenery.core.system.user_interface.imgui.editing.editors.basic;

import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassEditor;
import imgui.ImGui;
import imgui.type.ImDouble;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleClassEditor implements ImGuiClassEditor {

    @Override
    public Runnable createClassEditor(String name, Supplier<Object> input, Consumer<Object> onChange, float elementWidth, int flag) {
        return () -> {
            ImDouble imInt = new ImDouble(input.get() instanceof Double f ? f : 0);

            if(elementWidth > 0)
                ImGui.setNextItemWidth(elementWidth);

            if(ImGui.inputDouble(name, imInt)) {
                onChange.accept(imInt.get());
            }
        };
    }
}
