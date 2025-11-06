package dev.behindthescenery.core.system.user_interface.imgui;

public interface ImGuiEventListener<T extends ImGuiEvent> {

    void onImGuiEvent(T event);
}
