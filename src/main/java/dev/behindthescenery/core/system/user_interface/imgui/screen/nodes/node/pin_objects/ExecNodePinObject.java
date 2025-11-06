package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.pin_objects;

import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.NodeIconType;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePinObject;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePinObjectColors;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.structs.ImExec;
import imgui.ImVec4;

public class ExecNodePinObject extends ImNodePinObject {

    public ExecNodePinObject() {
        super(ImExec.class);
    }

    @Override
    public boolean canConnect(ImNodePinObject pinObject) {
        return pinObject.getObjClass().equals(getObjClass());
    }

    @Override
    public ImVec4 getColorImVec4() {
        return ImNodePinObjectColors.WHITE;
    }

    @Override
    public NodeIconType getNodeIcon() {
        return NodeIconType.Flow;
    }
}
