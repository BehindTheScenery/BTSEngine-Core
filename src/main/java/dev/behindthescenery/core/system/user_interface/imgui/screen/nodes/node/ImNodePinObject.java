package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node;

import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.NodeIconType;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.pin_objects.ExecNodePinObject;
import imgui.ImVec4;

public abstract class ImNodePinObject {

    protected Class<?> objClass;

    public ImNodePinObject(Class<?> objClass) {
        this.objClass = objClass;
    }

    public Class<?> getObjClass() {
        return objClass;
    }

    public abstract boolean canConnect(ImNodePinObject pinObject);

    public abstract ImVec4 getColorImVec4();

    public final boolean isExec() {
        return this instanceof ExecNodePinObject;
    }

    public void renderTooltip() {

    }

    public NodeIconType getNodeIcon() {
        return NodeIconType.Circle;
    }

}
