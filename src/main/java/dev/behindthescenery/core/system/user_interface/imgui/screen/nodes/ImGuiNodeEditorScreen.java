package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes;

import com.mojang.datafixers.util.Pair;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.builder.ImNodeRenderBuilder;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.events.ImNodeSelectNodeEvent;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNode;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePin;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.utils.ImNodeUtils;
import dev.behindthescenery.core.system.user_interface.imgui.screen.rework.AbstractImGuiScreen;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.extension.nodeditor.NodeEditor;
import imgui.extension.nodeditor.NodeEditorConfig;
import imgui.extension.nodeditor.NodeEditorContext;
import imgui.type.ImLong;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ImGuiNodeEditorScreen<NODE extends ImNode<PIN>, PIN extends ImNodePin> extends AbstractImGuiScreen {

    public static final long NULL_SELECTED = -404;

    private final ImNodeVariableContainer variableContainer = new ImNodeVariableContainer();

    private final NodesData<NODE, PIN> nodesData;
    protected final NodeEditorConfig nodeEditorConfig;
    protected final NodeEditorContext nodeEditorContext;
    protected ImNodeRenderBuilder nodeRenderBuilder = new ImNodeRenderBuilder();
    protected ImNodeConstructorList<NODE, PIN> nodeConstructor = new ImNodeConstructorList<>(this);
    protected long selectedNode = NULL_SELECTED;

    protected ImGuiNodeEditorScreen(Builder builder) {
        this(builder, null, createDefaultConfig());
    }

    protected ImGuiNodeEditorScreen(Builder builder, @Nullable ImNodeConstructorList<NODE, PIN> nodeConstructor, NodeEditorConfig nodeEditorConfig) {
        super(builder);
        this.nodeEditorConfig = nodeEditorConfig;
        this.nodeEditorContext = NodeEditor.createEditor(nodeEditorConfig);
        this.nodesData = createNodeData();

        if(nodeConstructor != null)
            this.nodeConstructor = nodeConstructor;
        registerNodes();
    }

    public ImNodeConstructorList<NODE, PIN> getNodeConstructor() {
        return nodeConstructor;
    }

    protected NodesData<NODE, PIN> createNodeData() {
        return new NodesData<>(this);
    }

    public NodesData<NODE, PIN> getNodesData() {
        return nodesData;
    }

    public ImNodeVariableContainer getVariableContainer() {
        return variableContainer;
    }

    /**
     * Создаёт стандартный конфиг файл без каких либо настроек
     */
    public static NodeEditorConfig createDefaultConfig() {
        NodeEditorConfig config = new NodeEditorConfig();
        config.setSettingsFile(null);
        return config;
    }

    public void registerNodes() {}

    @Override
    protected final void renderContent(DrawContext context, int mouseX, int mouseY, float partialTick) {
        preRender(context, mouseX, mouseY, partialTick);

        NodeEditor.setCurrentEditor(nodeEditorContext);
        NodeEditor.begin("Node Editor");

        renderInNodeEditor(context, mouseX, mouseY, partialTick);

        NodeEditor.end();

        postRender(context, mouseX, mouseY, partialTick);
    }

    protected void preRender(DrawContext context, int mouseX, int mouseY, float partialTick) {}

    protected void renderInNodeEditor(DrawContext context, int mouseX, int mouseY, float partialTick) {
        renderNodes(context, mouseX, mouseY, partialTick);
        createConnection();
        renderNodeConnection();
        renderAllContexts();
    }

    protected void postRender(DrawContext context, int mouseX, int mouseY, float partialTick) {}

    protected void renderNodes(DrawContext context, int mouseX, int mouseY, float partialTick) {

        for (NODE node : getNodesData().getNodes()) {

            nodeRenderBuilder.begin(node.getNodeId());

            final boolean simple = node.isSimple();

            if(!simple && node.hasName()) {
                nodeRenderBuilder.header(node.getHeaderColor());
                ImGui.spring(0);
                node.renderHeader(context, mouseX, mouseY, partialTick);
                ImGui.spring(1);
                ImGui.dummy(new ImVec2(0, 28));
                ImGui.spring(0);
                nodeRenderBuilder.endHeader();
            }

            for (ImNodePin pin : node.getInputPins()) {
                nodeRenderBuilder.beginInput(pin.getPinId());

                drawPinIcon(pin, pin.hasConnectedNodes());

                ImGui.spring(0);
                pin.render(context, mouseX, mouseY, partialTick);
                ImGui.spring(0);
                nodeRenderBuilder.endInput();
            }

            if(simple) {
                nodeRenderBuilder.middle();
                ImGui.spring(1,0);
                ImGui.text(node.getName());
                ImGui.spring(1, 0);
            }

            for (ImNodePin pin : node.getOutputPins()) {
                nodeRenderBuilder.beginOutput(pin.getPinId());
                ImGui.spring(0);
                pin.render(context, mouseX, mouseY, partialTick);
                ImGui.spring(0);
                drawPinIcon(pin, pin.hasConnectedNodes());
                nodeRenderBuilder.endOutput();
            }

            nodeRenderBuilder.end();
        }
    }

    protected void createConnection() {
        if(NodeEditor.beginCreate()) {
            try {
                final ImLong from = new ImLong();
                final ImLong to = new ImLong();

                if (!NodeEditor.queryNewLink(from, to)) return;

                final NodesData<NODE, PIN> nodesData = getNodesData();

                @Nullable Pair<NODE, PIN> fromData = nodesData.findByPin(from.get());
                @Nullable Pair<NODE, PIN> toData = nodesData.findByPin(to.get());

                if (fromData == null || toData == null) return;

                NODE fNode = fromData.getFirst();
                PIN fPin = fromData.getSecond();
                NODE tNode = toData.getFirst();
                PIN tPin = toData.getSecond();

                if (!fNode.canConnecting(tNode, fPin, tPin)) return;

                if (!NodeEditor.acceptNewItem()) return;

                nodesData.addConnection(fNode, tNode, fPin, tPin);
            } finally {
                NodeEditor.endCreate();
            }
        }
    }

    protected void renderNodeConnection() {
        long linkId = 1;

        final NodesData<NODE, PIN> nodesData = getNodesData();
        final List<Pair<PIN, List<PIN>>> nodeConnections = nodesData.getNodesPinConnections();

        for (Pair<PIN, List<PIN>> nodeConnection : nodeConnections) {
            final PIN connectionFrom = nodeConnection.getFirst();

            for (PIN imNodePin : nodeConnection.getSecond()) {
                NodeEditor.link(linkId++, connectionFrom.getPinId(), imNodePin.getPinId(), imNodePin.getNodePinObject().getColorImVec4());
            }
        }
    }

    protected void renderAllContexts() {
        NodeEditor.suspend();
        renderNodeContext();
        renderNodePinContext();
        renderNodeLinkContext();
        renderNodeEditorContext();
        NodeEditor.resume();
    }

    protected void renderNodeContext() {
        final long objectId = NodeEditor.getNodeWithContextMenu();

        if(objectId != -1) {
            ImGui.openPopup("node_context");
            ImGui.getStateStorage().setInt(ImGui.getID("node_context_id"), (int) objectId);
        }

        if(ImGui.isPopupOpen("node_context")) {
            final int targetNode = ImGui.getStateStorage().getInt(ImGui.getID("node_context_id"));
            if(ImGui.beginPopup("node_context")) {
                if (onOpenNodeContext(targetNode)) {
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }
        }
    }

    protected void renderNodePinContext() {
        final long objectId = NodeEditor.getPinWithContextMenu();

        if(objectId != -1) {
            ImGui.openPopup("node_pin_context");
            ImGui.getStateStorage().setInt(ImGui.getID("node_pin_context_id"), (int) objectId);
        }

        if(ImGui.isPopupOpen("node_pin_context")) {
            final int targetNode = ImGui.getStateStorage().getInt(ImGui.getID("node_pin_context_id"));
            if (ImGui.beginPopup("node_pin_context")) {
                if (onOpenNodePinContext(targetNode)) {
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }
        }
    }

    protected void renderNodeLinkContext() {
        final long objectId = NodeEditor.getLinkWithContextMenu();

        if(objectId != -1) {
            ImGui.openPopup("node_link_context");
            ImGui.getStateStorage().setInt(ImGui.getID("node_link_context_id"), (int) objectId);
        }

        if(ImGui.isPopupOpen("node_link_context")) {
            final int targetNode = ImGui.getStateStorage().getInt(ImGui.getID("node_link_context_id"));
            if(ImGui.beginPopup("node_link_context")) {
                if (onOpenNodeLinkContext(targetNode)) {
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }
        }
    }

    protected void renderNodeEditorContext() {
        if(NodeEditor.showBackgroundContextMenu()) {
            ImGui.openPopup("node_editor_context");
        }

        if(ImGui.beginPopup("node_editor_context")) {
            if(onOpenNodeEditorContext()) {
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
    }

    protected boolean onOpenNodeEditorContext() {
        return nodeConstructor.renderConstructors();
    }

    protected boolean onOpenNodeContext(long nodeId) {

        if(ImGui.button("Delete")) {
            getNodesData().deleteNode(nodeId);
            return true;
        }

        return false;
    }

    protected boolean onOpenNodePinContext(long pinId) {
        return false;
    }

    protected boolean onOpenNodeLinkContext(long linkId) {

        if(ImGui.button("Delete")) {
            final ImLong startPin = new ImLong();
            final ImLong endPin = new ImLong();
            NodeEditor.getLinkPins(linkId, startPin, endPin);

            final var data = getNodesData();

            Pair<NODE, PIN> sD = data.findByPin(startPin.get());
            Pair<NODE, PIN> eD = data.findByPin(endPin.get());
            if(sD != null && eD != null) {
                getNodesData().removeConnection(sD.getFirst(), eD.getFirst(), sD.getSecond(), eD.getSecond());
                return true;
            }
        }

        return false;
    }

    public void selectNode(long nodeId) {
        this.selectNode(nodeId, false);
    }

    public void selectNode(long nodeId, boolean selectInEditor) {
        long b1 = nodeId;

        if(this.selectedNode == b1) {
            b1 = NULL_SELECTED;
        }

        final Optional<NODE> node =  getNodesData().getNode(b1);
        if(node.isEmpty()) {
            this.selectedNode = NULL_SELECTED;
            invokeEvent(new ImNodeSelectNodeEvent(null));
        } else {
            if(selectInEditor) NodeEditor.selectNode(nodeId);
            this.selectedNode = nodeId;
            invokeEvent(new ImNodeSelectNodeEvent(node.get()));
        }
    }

    protected void drawPinIcon(ImNodePin pin, boolean connected) {
        drawPinIcon(pin, connected, 255);
    }

    protected void drawPinIcon(ImNodePin pin, boolean connected, int alpha) {
       final ImVec4 color = pin.getNodePinObject().getColorImVec4();
//       color.w = (float) alpha / 255;
        ImNodeUtils.drawIcon(pin, new ImVec2(8,8), pin.getNodeIcon(), connected, color, new ImVec4(32, 32, 32, alpha));

    }
}
