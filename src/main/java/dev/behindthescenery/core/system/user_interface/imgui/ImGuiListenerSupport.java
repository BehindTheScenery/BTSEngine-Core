package dev.behindthescenery.core.system.user_interface.imgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ImGuiListenerSupport {

    Map<Class<? extends ImGuiEvent>, List<ImGuiEventListener<? extends ImGuiEvent>>> getEventListenerMap();

    default void addEventListener(Class<? extends ImGuiEvent> event, ImGuiEventListener<? extends ImGuiEvent> listener)  {
        List<ImGuiEventListener<? extends ImGuiEvent>> list = getEventListenerMap().getOrDefault(event, new ArrayList<>());
        list.add(listener);
        getEventListenerMap().put(event, list);
    }

    default void removeEventListener(Class<? extends ImGuiEvent> event, ImGuiEventListener<? extends ImGuiEvent> listener) {
        List<ImGuiEventListener<? extends ImGuiEvent>> list = getEventListenerMap().getOrDefault(event, new ArrayList<>());
        list.remove(listener);
        getEventListenerMap().put(event, list);
    }

    default void invokeEvent(ImGuiEvent event) {
        for (ImGuiEventListener listener : getEventListenerMap().get(event.getClass())) {
            listener.onImGuiEvent(event);
        }
    }
}
