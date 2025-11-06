package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.pin_objects;

import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePinObject;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePinObjectColors;
import imgui.ImVec4;

public final class ImNodePinObjectImpl extends ImNodePinObject {

    private final ImVec4 color;

    public ImNodePinObjectImpl(Class<?> objClass) {
        super(objClass);
        this.color = ImNodePinObjectColors.getColorByClass(objClass);
    }

    @Override
    public boolean canConnect(ImNodePinObject pinObject) {
        return pinObject.getObjClass().equals(getObjClass());
    }

    @Override
    public ImVec4 getColorImVec4() {
        return color;
    }
}
