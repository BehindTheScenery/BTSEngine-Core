package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.events;

import dev.behindthescenery.core.system.user_interface.imgui.ImGuiEvent;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNode;

public class ImNodeCreateNodeEvent implements ImGuiEvent {

    protected final ImNode node;

    public ImNodeCreateNodeEvent(ImNode node) {
        this.node = node;
    }

    public ImNode getNode() {
        return node;
    }
}
