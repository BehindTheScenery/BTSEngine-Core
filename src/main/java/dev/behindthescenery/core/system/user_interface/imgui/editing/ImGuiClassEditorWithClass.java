package dev.behindthescenery.core.system.user_interface.imgui.editing;

import java.util.HashMap;
import java.util.Map;

public interface ImGuiClassEditorWithClass extends ImGuiClassEditor {

    Class<?>[] getSupportedClasses();

    default Map<Class<?>, ImGuiClassConstructor> getConstructor() {
        return new HashMap<>();
    }

}
