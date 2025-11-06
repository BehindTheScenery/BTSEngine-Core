package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node;

import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.ImPinType;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.NodeIconType;
import imgui.ImGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;

import java.util.ArrayList;
import java.util.List;

public class ImNodePin {

    protected String name;
    protected ImNode rootNode;
    protected final ImPinType type;
    protected long pinId;
    protected List<ImNode> connectedNodes;
    protected ArrayList<ImNodePin> connectedPins;
    protected ImNodePinObject nodePinObject;
    protected NodeIconType nodeIconType;

    public ImNodePin(String name, ImPinType type, ImNodePinObject nodePinObject) {
        this(0, name, type, nodePinObject, nodePinObject.getNodeIcon());
    }

    public ImNodePin(String name, ImPinType type, ImNodePinObject nodePinObject, NodeIconType nodeIconType) {
        this(0, name, type, nodePinObject, nodeIconType);
    }

    public ImNodePin(long pinId, String name, ImPinType type, ImNodePinObject nodePinObject) {
        this(0, name, type, nodePinObject, nodePinObject.getNodeIcon());
    }

    public ImNodePin(long pinId, String name, ImPinType type, ImNodePinObject nodePinObject, NodeIconType nodeIconType) {
        this.name = name;
        this.type = type;
        this.pinId = pinId;
        this.nodePinObject = nodePinObject;
        this.connectedNodes = new ArrayList<>();
        this.connectedPins = new ArrayList<>();
        this.nodeIconType = nodeIconType;
    }

    public void setRootNode(ImNode rootNode) {
        this.rootNode = rootNode;
    }

    public ImNode getRootNode() {
        return rootNode;
    }

    public ImNodePin changeNodePinObject(ImNodePinObject pinObject) {
        this.nodePinObject = pinObject;
        return this;
    }

    public ImNodePinObject getNodePinObject() {
        return nodePinObject;
    }

    public long getPinId() {
        return pinId;
    }

    public void setPinId(long pinId) {
        this.pinId = pinId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return !name.isEmpty();
    }

    public ImPinType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTranslation(Object... obj) {
        return I18n.translate(name, obj);
    }

    public List<ImNode> getConnectedNodes() {
        return connectedNodes;
    }

    public ArrayList<ImNodePin> getConnectedPins() {
        return connectedPins;
    }

    public boolean hasConnectedPins() {
        return !connectedPins.isEmpty();
    }

    public boolean hasConnectedNodes() {
        return !connectedNodes.isEmpty();
    }

    public boolean isConnectedToNode(ImNode node) {
        return this.connectedNodes.contains(node);
    }

    public boolean isConnectedToPin(ImNodePin pin) {
        return this.connectedPins.contains(pin);
    }

    public boolean isConnectedToNode(long nodeId) {
        return this.connectedNodes.stream().anyMatch(s -> s.nodeId == nodeId);
    }

    public boolean isConnectedToPin(long pinId) {
        return this.connectedPins.stream().anyMatch(s -> s.pinId == pinId);
    }

    public NodeIconType getNodeIcon() {
        return nodeIconType;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        ImGui.text(getName());
    }

}
