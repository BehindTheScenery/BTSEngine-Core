package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes;

import com.mojang.datafixers.util.Pair;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNode;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePin;
import imgui.ImGui;
import imgui.extension.nodeditor.NodeEditor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NodesData<T extends ImNode<B>, B extends ImNodePin> {
    protected long nextNodeID = 1;
    protected long nextPinID = 50000;

    protected final List<T> nodes = new ArrayList<>();

    private final ImGuiNodeEditorScreen<T, B> nodeEditor;

    public NodesData(ImGuiNodeEditorScreen<T, B> nodeEditor) {
        this.nodeEditor = nodeEditor;
    }

    public ImGuiNodeEditorScreen<T, B> getNodeEditor() {
        return nodeEditor;
    }

    public List<Pair<B, List<B>>> getNodesPinConnections() {
        List<Pair<B, List<B>>> connections = new ArrayList<>();

        for (T node : getNodes()) {

            for (B outputPin : node.getOutputPins()) {
                List<B> connectedInputPins = new ArrayList<>();

                for (T otherNode : getNodes()) {
                    for (B inputPin : otherNode.getInputPins()) {

                        if (isConnected(outputPin, inputPin)) {
                            connectedInputPins.add(inputPin);
                        }
                    }
                }

                if (!connectedInputPins.isEmpty()) {
                    connections.add(new Pair<>(outputPin, connectedInputPins));
                }
            }
        }

        return connections;
    }

    public List<T> findByInputConnection(long pinId) {
        List<T> connections = new ArrayList<>();

        for (T node : getNodes()) {
            for (B inputPin : node.getInputPins()) {
                if (inputPin.isConnectedToPin(pinId)) {
                    connections.add(node);
                    break;
                }
            }
        }

        return connections;
    }

    public List<T> findByOutputConnection(long pinId) {
        List<T> connections = new ArrayList<>();

        for (T node : getNodes()) {
            for (B outputPin : node.getOutputPins()) {
                if (outputPin.isConnectedToPin(pinId)) {
                    connections.add(node);
                    break;
                }
            }
        }

        return connections;
    }

    @Nullable
    public Pair<T, B> findByInputPin(long pinID) {
        for (T node : getNodes()) {
            for (B pin : node.getAllPins()) {

                if(pin.getType().isInput() && pin.getPinId() == pinID)
                    return new Pair<>(node, pin);
            }
        }
        return null;
    }

    @Nullable
    public Pair<T, B> findByOutputPin(long pinID) {
        for (T node : getNodes()) {
            for (B pin : node.getAllPins()) {
                if(pin.getType().isOutput() && pin.getPinId() == pinID)
                    return new Pair<>(node, pin);
            }
        }
        return null;
    }

    @Nullable
    public Pair<T, B> findByPin(long pinID) {
        Pair<T, B> node = findByInputPin(pinID);
        if (node == null) {
            node = findByOutputPin(pinID);
        }
        return node;
    }


    public Optional<T> getNode(long nodeId) {
        return getNodes().stream().filter(s -> s.getNodeId() == nodeId).findFirst();
    }

    public List<T> getNodes() {
        return nodes;
    }

    public void deleteNode(long nodeId) {
        Optional<T> node = getNode(nodeId);
        if(node.isEmpty()) return;
        T n = node.get();
        if(!n.isCanDelete()) return;
        removeAllConnections(n);
        nodes.removeIf(s -> s.getNodeId() == nodeId);
    }

    public T createNode(T type) {
        return createNode(type, false);
    }

    public T createNode(T type, boolean setPosToMouse) {
        type.setNodeEditor(nodeEditor);
        type.setNodeId(nextNodeID++);
        type.getAllPins().forEach(pin -> {
            pin.setRootNode(type);
            pin.setPinId(nextPinID++);
        });

        getNodes().add(type);

        if(setPosToMouse) NodeEditor.setNodePosition(type.getNodeId(), NodeEditor.screenToCanvas(ImGui.getMousePos()));
        return type;
    }

    public void addConnection(T nodeFrom, T nodeTo, B pinFrom, B pinTo) {
        pinFrom.getConnectedPins().add(pinTo);
        pinFrom.getConnectedNodes().add(nodeTo);
        pinTo.getConnectedPins().add(pinFrom);
        pinTo.getConnectedNodes().add(nodeFrom);
    }

    public void removeConnection(T nodeFrom, T nodeTo, B pinFrom, B pinTo) {
        pinFrom.getConnectedPins().remove(pinTo);
        pinFrom.getConnectedNodes().remove(nodeTo);
        pinTo.getConnectedPins().remove(pinFrom);
        pinTo.getConnectedNodes().remove(nodeFrom);
    }

    public void removeAllConnections(T nodeFrom, T nodeTo) {
        for (B nodePin : nodeFrom.getAllPins()) {

            for (B pin : nodeTo.getAllPins()) {
                nodePin.getConnectedPins().remove(pin);
            }

            nodePin.getConnectedNodes().remove(nodeTo);
        }

        for (B nodePin : nodeTo.getAllPins()) {

            for (B pin : nodeFrom.getAllPins()) {
                nodePin.getConnectedPins().remove(pin);
            }

            nodePin.getConnectedNodes().remove(nodeFrom);
        }
    }

    public void removeAllConnections(T node) {
        for (B nodePin : node.getAllPins()) {

            for (ImNodePin connectedPin : nodePin.getConnectedPins()) {
                connectedPin.getConnectedNodes().removeIf(s -> s.getNodeId() == node.getNodeId());
                connectedPin.getConnectedPins().removeIf(s -> s.getPinId() == nodePin.getPinId());
            }

            nodePin.getConnectedNodes().clear();
            nodePin.getConnectedPins().clear();
        }
    }

    private boolean isConnected(B outputPin, B inputPin) {
        return outputPin.isConnectedToPin(inputPin.getPinId()) ||
                inputPin.isConnectedToPin(outputPin.getPinId());
    }
}
