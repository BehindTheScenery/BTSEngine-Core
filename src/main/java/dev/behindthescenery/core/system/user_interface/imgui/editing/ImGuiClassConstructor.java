package dev.behindthescenery.core.system.user_interface.imgui.editing;

import java.util.List;

public interface ImGuiClassConstructor {

    List<Class<?>> getClassConstructorInput();

    Object createClassFromConstructor(List<Object> inputClasses);
}
