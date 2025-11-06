package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.events;

import dev.behindthescenery.core.system.user_interface.imgui.ImGuiEvent;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNode;
import org.jetbrains.annotations.Nullable;

public class ImNodeSelectNodeEvent implements ImGuiEvent {

    @Nullable
    protected final ImNode selectedNode;

    public ImNodeSelectNodeEvent(@Nullable ImNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    @Nullable
    public ImNode getSelectedNode() {
        return selectedNode;
    }
}
