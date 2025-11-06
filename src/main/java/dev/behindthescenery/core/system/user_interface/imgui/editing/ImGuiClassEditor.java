package dev.behindthescenery.core.system.user_interface.imgui.editing;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ImGuiClassEditor {

    default Runnable createClassEditor(final String name, final Supplier<Object> input, final Consumer<Object> onChange) {
        return createClassEditor(name, input, onChange, 0, 0);
    }

    Runnable createClassEditor(final String name, final Supplier<Object> input, final Consumer<Object> onChange, float elementWidth, int flag);
}
