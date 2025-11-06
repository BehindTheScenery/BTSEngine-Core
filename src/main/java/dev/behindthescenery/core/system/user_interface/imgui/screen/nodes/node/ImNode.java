package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node;

import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.ImGuiNodeEditorScreen;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.extension.nodeditor.NodeEditor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImNode<T extends ImNodePin> {

    protected String name = "";
    protected long nodeId;
    protected List<T> inputPins = new ArrayList<>();
    protected List<T> outputPins = new ArrayList<>();
    protected boolean canDelete = true;
    protected boolean simple;
    protected ImVec4 headerColor = new ImVec4(1,1,1,1);

    protected ImGuiNodeEditorScreen<? extends ImNode<T>, T> nodeEditor;

    public ImNode(String name) {
        this(0, name);
    }

    public ImNode(long nodeID, String name) {
        this.nodeId = nodeID;
        this.name = name;
    }

    public boolean isSimple() {
        return simple;
    }

    public ImVec4 getHeaderColor() {
        return headerColor;
    }

    public void setNodeEditor(ImGuiNodeEditorScreen<? extends ImNode<T>, T> nodeEditor) {
        this.nodeEditor = nodeEditor;
    }

    public ImGuiNodeEditorScreen<? extends ImNode<T>, T> getNodeEditor() {
        return nodeEditor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return !name.isEmpty();
    }

    public String getName() {
        return name;
    }

    public String getTranslation(Object... obj) {
        return I18n.translate(name, obj);
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public long getNodeId() {
        return nodeId;
    }

    public List<T> getInputPins() {
        return inputPins;
    }

    public List<T> getOutputPins() {
        return outputPins;
    }

    public List<T> getAllPins() {
        List<T> imNodePins = new ArrayList<>();
        imNodePins.addAll(getInputPins());
        imNodePins.addAll(getOutputPins());
        return imNodePins;
    }

    public Optional<T> containsInputPin(long pinId) {
        return inputPins.stream().filter(pin -> pin.type.isInput() && pin.pinId == pinId).findFirst();
    }

    public Optional<T> containsOutPin(long pinId) {
        return outputPins.stream().filter(pin -> pin.type.isOutput() && pin.pinId == pinId).findFirst();
    }

    public Optional<T> getPin(long pinId) {
        List<T> allPins = new ArrayList<>();
        allPins.addAll(inputPins);
        allPins.addAll(outputPins);
        return allPins.stream().filter(s -> s.pinId == pinId).findFirst();
    }

    public <T extends ImNode> boolean canConnecting(T node, ImNodePin from, ImNodePin to) {
        return from.type.canConnect(to.type) &&
                !from.isConnectedToPin(to) &&
                !to.isConnectedToNode(this) &&
                from.getNodePinObject().canConnect(to.getNodePinObject());
    }



    public ImNode<T> addPin(T pin) {
        pin.setRootNode(this);
        if(pin.type.isInput()) {
            inputPins.add(pin);
        } else {
            outputPins.add(pin);
        }
        return this;
    }

    public T createPin(T pin) {
        addPin(pin);
        return pin;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {

        renderHeader(context, mouseX, mouseY, partialTick);
        ImGui.dummy(0, 8);

        ImGui.beginGroup();
        renderInputNodes(context, mouseX, mouseY, partialTick);

        ImGui.endGroup();
        ImGui.sameLine();

        ImGui.beginGroup();
        ImGui.dummy(30, 0);
        ImGui.endGroup();

        ImGui.sameLine();
        ImGui.beginGroup();

        renderOutputNodes(context, mouseX, mouseY, partialTick);

        ImGui.endGroup();
    }

    public void renderHeader(DrawContext context, int mouseX, int mouseY, float partialTick) {
        ImGui.text(getName());
    }

    public void renderHeaderBackground(DrawContext context, int mouseX, int mouseY, float partialTick) {

    }

    public void renderInputNodes(DrawContext context, int mouseX, int mouseY, float partialTick) {
        for (final ImNodePin pin : getInputPins()) {

            NodeEditor.beginPin(pin.getPinId(), pin.getType().getId());

            final ImVec2 cursor = ImGui.getCursorPos();
            final boolean hovered = ImGui.isMouseHoveringRect(cursor.x, cursor.y, cursor.x + 8, cursor.y + 8);
            final ImVec4 pinColor = pin.getNodePinObject().getColorImVec4();
            final int color = hovered ? ImGui.colorConvertFloat4ToU32(pinColor.x, pinColor.y, pinColor.z, pinColor.w) :
                    ImGui.colorConvertFloat4ToU32(pinColor.x * 0.8f, pinColor.y * 0.8f, pinColor.z * 0.8f, pinColor.w);

            if (pin.hasConnectedPins()) {

                ImGui.getWindowDrawList().addCircleFilled(
                        cursor.x + 8,
                        cursor.y + 8,
                        8,
                        color,
                        24
                );
            }
            else {

                ImGui.getWindowDrawList().addCircle(
                        cursor.x + 9, cursor.y + 8, 8, color, 24
                );
            }

            ImGui.dummy(16, 16);
            NodeEditor.pinPivotAlignment(.1f, .5f);
            NodeEditor.endPin();

            ImGui.sameLine();

            pin.render(context, mouseX, mouseY, partialTick);
        }
    }

    public void renderOutputNodes(DrawContext context, int mouseX, int mouseY, float partialTick) {

        for (ImNodePin pin : getOutputPins()) {

            pin.render(context, mouseX, mouseY, partialTick);

            ImGui.sameLine();
            NodeEditor.beginPin(pin.getPinId(), pin.getType().getId());

            final ImVec2 cursor = ImGui.getCursorPos();
            final boolean hovered = ImGui.isMouseHoveringRect(cursor.x, cursor.y, cursor.x + 8, cursor.y + 8);
            final ImVec4 pinColor = pin.getNodePinObject().getColorImVec4();
            final int color = hovered ? ImGui.colorConvertFloat4ToU32(pinColor.x, pinColor.y, pinColor.z, pinColor.w) :
                    ImGui.colorConvertFloat4ToU32(pinColor.x * 0.8f, pinColor.y * 0.8f, pinColor.z * 0.8f, pinColor.w);

            if(pin.hasConnectedPins()) {

                ImGui.getWindowDrawList().addCircleFilled(
                        cursor.x + 8,
                        cursor.y + 8,
                        8,
                        color,
                        24
                );
            } else {

                ImGui.getWindowDrawList().addCircle(
                        cursor.x + 9, cursor.y + 8, 8, color, 24
                );
            }

            ImGui.dummy(16,16);

            NodeEditor.pinPivotAlignment(.5f, .5f);

            NodeEditor.endPin();
        }
    }

    protected void renderTooltip(DrawContext context, int mouseX, int mouseY, float partialTick) {

    }
}
