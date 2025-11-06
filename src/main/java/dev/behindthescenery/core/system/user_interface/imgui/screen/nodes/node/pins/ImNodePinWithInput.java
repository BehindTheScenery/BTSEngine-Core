package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.pins;

import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.ImPinType;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePin;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePinObject;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.utils.ImNodeUtils;
import imgui.ImGui;
import net.minecraft.client.gui.DrawContext;

public class ImNodePinWithInput<T> extends ImNodePin {
    
    protected T defaultValue;
    protected int elementFlags = 0;
    protected float elementWidth = 45;

    protected Runnable editTask = () -> {};

    public ImNodePinWithInput(String name, ImPinType type, ImNodePinObject nodePinObject, T defaultValue) {
        super(name, type, nodePinObject);
        this.defaultValue = defaultValue;
    }

    public ImNodePinWithInput(long pinId, String name, ImPinType type, ImNodePinObject nodePinObject, T defaultValue) {
        super(pinId, name, type, nodePinObject);
        this.defaultValue = defaultValue;
    }
    
    @Override
    public void setPinId(long pinId) {
        super.setPinId(pinId);
        createEditField();
    }

    public void setValue(T value) {
        getRootNode().getNodeEditor().getVariableContainer().setValue(getPinId(), value);
    }

    public T getValue() {
        return getRootNode().getNodeEditor().getVariableContainer().getOrCreate(getPinId(), defaultValue);
    }

    public boolean hasFlag(int flags) {
        return (elementFlags & flags) != 0;
    }

    public void addFlags(int flags) {
        this.elementFlags |= flags;
    }

    public void removeFlags(int flags) {
        this.elementFlags &= flags;
    }

    protected String getImGuiId() {
        return "##input_" + pinId;
    }

    protected String getImGuiId(int id) {
        return "##input_" + pinId + "_" + id;
    }

    public void setElementWidth(float elementWidth) {
        this.elementWidth = elementWidth;
    }

    public float getElementWidth() {
        return elementWidth;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        final boolean isInput = getType().isInput();

        if(isInput) {
            ImGui.text(getName());
            if(hasConnectedNodes()) return;
        }

        renderInput(context, mouseX, mouseY, partialTick);

        if(!isInput) {
            ImGui.text(getName());
        }
    }

    protected void renderInput(DrawContext context, int mouseX, int mouseY, float partialTick) {
        editTask.run();
    }

    protected void createEditField() {
        editTask = ImNodeUtils.createEditFieldForClass(this,
                nodePinObject.getObjClass(),
                defaultValue,
                getRootNode().getNodeEditor().getVariableContainer(),
                elementWidth, elementFlags);
    }
}
