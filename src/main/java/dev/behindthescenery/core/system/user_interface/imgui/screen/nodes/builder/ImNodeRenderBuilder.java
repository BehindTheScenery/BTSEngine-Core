package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.builder;

import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.color.Texture;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.ImPinType;
import imgui.*;
import imgui.extension.nodeditor.NodeEditor;
import imgui.extension.nodeditor.flag.NodeEditorStyleVar;
import imgui.flag.ImDrawFlags;

import java.util.concurrent.CompletableFuture;

public class ImNodeRenderBuilder {

    protected int HeaderTextureWidth;
    protected int HeaderTextureHeight;

    protected long CurrentNodeId;
    protected BuilderStage CurrentStage;
    protected ImVec4 HeaderColor;
    protected ImVec2 NodeMin;
    protected ImVec2 NodeMax;
    protected ImVec2 HeaderMin;
    protected ImVec2 HeaderMax;
    protected ImVec2 ContentMin;
    protected ImVec2 ContentMax;
    protected boolean HasHeader;

    protected int headerTexture;

    public static ImNodeRenderBuilder fromTexture(Texture texture) {
        CompletableFuture<ImNodeRenderBuilder> builderCompletable = new CompletableFuture<>();

        final int textureId = texture.getTexture().getGlId();
        final ImNodeRenderBuilder[] builder = new ImNodeRenderBuilder[1];

        if(!BtsRenderSystem.isOnRenderThread()) {
            BtsRenderSystem.recordRenderCall(() -> {
                final int[] array = BtsRenderSystem.getTextureSize(textureId);
                builder[0] = new ImNodeRenderBuilder(textureId, array[0], array[1]);
                builderCompletable.complete(builder[0]);
            });
        } else {
            final int[] array = BtsRenderSystem.getTextureSize(textureId);
            builder[0] = new ImNodeRenderBuilder(textureId, array[0], array[1]);
            builderCompletable.complete(builder[0]);
        }
        return builderCompletable.join();
    }

    public ImNodeRenderBuilder() {
        this(-1, 0, 0);
    }

    public ImNodeRenderBuilder(int headerTexture) {
        this(headerTexture, 0, 0);
    }

    public ImNodeRenderBuilder(int headerTexture, int headerTextureWidth, int headerTextureHeight) {
        CurrentNodeId = 0;
        CurrentStage = BuilderStage.Invalid;
        setStage(BuilderStage.Invalid);
        HasHeader = false;
        this.headerTexture = headerTexture;
        this.HeaderTextureWidth = headerTextureWidth;
        this.HeaderTextureHeight = headerTextureHeight;
    }

    public void begin(long nodeId) {
        HasHeader = false;
        HeaderMin = HeaderMax = new ImVec2();

        NodeEditor.pushStyleVar(NodeEditorStyleVar.NodePadding, new ImVec4(8, 4, 8, 8));
        NodeEditor.beginNode(nodeId);

        ImGui.pushID(nodeId);
        CurrentNodeId = nodeId;

        setStage(BuilderStage.Begin);
    }

    public void end() {
        setStage(BuilderStage.End);

        NodeEditor.endNode();

        if(headerTexture != -1 && ImGui.isItemVisible()) {
            ImDrawList drawList = NodeEditor.getNodeBackgroundDrawList(CurrentNodeId);
            final float halfBorderWidth = NodeEditor.getStyle().getNodeBorderWidth() * 0.5f;

            if ((HeaderMax.x > HeaderMin.x) && (HeaderMax.y > HeaderMin.y)) {
                final var uv = new ImVec2(
                        (HeaderMax.x - HeaderMin.x) / (4.0f * HeaderTextureWidth),
                        (HeaderMax.y - HeaderMin.y) / (4.0f * HeaderTextureHeight));

                drawList.addImageRounded(headerTexture,
                        HeaderMin.minus(new ImVec2(8 - halfBorderWidth, 4 - halfBorderWidth)),
                        HeaderMax.plus(new ImVec2(8 - halfBorderWidth, 0)),
                        new ImVec2(0.0f, 0.0f), uv,
                        ImColor.rgba(HeaderColor),
                        NodeEditor.getStyle().getNodeRounding(), ImDrawFlags.RoundCornersTop);
            }
        }

        CurrentNodeId = 0;
        ImGui.popID();

        NodeEditor.popStyleVar();
        setStage(BuilderStage.Invalid);
    }

    public void header(ImVec4 color)
    {
        HeaderColor = color;
        setStage(BuilderStage.Header);
    }

    public void endHeader()
    {
        setStage(BuilderStage.Content);
    }

    public void beginInput(long pinId)
    {
        if (CurrentStage == BuilderStage.Begin)
            setStage(BuilderStage.Content);

        final boolean applyPadding = (CurrentStage == BuilderStage.Input);

        setStage(BuilderStage.Input);

        if (applyPadding)
            ImGui.spring(0);

        pin(pinId, ImPinType.Input);

        ImGui.beginHorizontal(Math.toIntExact(pinId), new ImVec2(0,0), -1f);
    }

    public void endInput()
    {
        ImGui.endHorizontal();

        endPin();
    }

    public void middle() {
        if (CurrentStage == BuilderStage.Begin)
            setStage(BuilderStage.Content);

        setStage(BuilderStage.Middle);
    }

    public void beginOutput(long pinId) {

        if (CurrentStage == BuilderStage.Begin)
            setStage(BuilderStage.Content);

        final boolean applyPadding = (CurrentStage == BuilderStage.Output);

        setStage(BuilderStage.Output);

        if (applyPadding)
            ImGui.spring(0);

        pin(pinId, ImPinType.Output);

        ImGui.beginHorizontal(Math.toIntExact(pinId), new ImVec2(0,0), -1f);
    }

    public void endOutput() {
        ImGui.endHorizontal();
        endPin();
    }

    protected boolean setStage(BuilderStage stage) {
        if(stage == CurrentStage)
            return false;

        final BuilderStage oldStage = CurrentStage;
        CurrentStage = stage;

        switch (oldStage) {
            case Begin, Content, End, Invalid -> {
                break;
            }
            case Header -> {
                ImGui.endHorizontal();
                HeaderMin = ImGui.getItemRectMin();
                HeaderMax = ImGui.getItemRectMax();

                ImGui.spring(0, ImGui.getStyle().getItemSpacing().y * 2.0f);

                break;
            }
            case Input, Output -> {
                NodeEditor.popStyleVar(2);

                ImGui.spring(1, 0);
                ImGui.endVertical();

                break;
            }

            case Middle -> {
                ImGui.endVertical();
                break;
            }
        }

        switch (stage) {
            case Begin -> {
                ImGui.beginVertical("node");
                break;
            }
            case Header -> {
                HasHeader = true;
                ImGui.beginHorizontal("header", new ImVec2(0,0), -1f);
                break;
            }
            case Content -> {
                if (oldStage == BuilderStage.Begin)
                    ImGui.spring(0);

                ImGui.beginHorizontal("content", new ImVec2(0,0), -1f);
                ImGui.spring(0, 0);
                break;
            }
            case Input -> {
                ImGui.beginVertical("inputs", new ImVec2(0, 0), 0.0f);

                NodeEditor.pushStyleVar(NodeEditorStyleVar.PivotAlignment, new ImVec2(0, 0.5f));
                NodeEditor.pushStyleVar(NodeEditorStyleVar.PivotSize, new ImVec2(0, 0));

                if (!HasHeader)
                    ImGui.spring(1, 0);
                break;
            }
            case Middle -> {
                ImGui.spring(1);
                ImGui.beginVertical("middle", new ImVec2(0, 0), 1.0f);
                break;
            }
            case Output -> {
                if (oldStage == BuilderStage.Middle || oldStage == BuilderStage.Input)
                    ImGui.spring(1);
                else
                    ImGui.spring(1, 0);

                ImGui.beginVertical ("outputs", new ImVec2(0, 0), 1.0f);

                NodeEditor.pushStyleVar(NodeEditorStyleVar.PivotAlignment, new ImVec2(1.0f, 0.5f));
                NodeEditor.pushStyleVar(NodeEditorStyleVar.PivotSize, new ImVec2(0, 0));

                if (!HasHeader)
                    ImGui.spring (1, 0);
                break;
            }
            case End -> {
                if (oldStage == BuilderStage.Input)
                    ImGui.spring(1, 0);
                if (oldStage != BuilderStage.Begin)
                    ImGui.endHorizontal();
                ContentMin = ImGui.getItemRectMin();
                ContentMax = ImGui.getItemRectMax();

                //ImGui::Spring(0);
                ImGui.endVertical();
                NodeMin = ImGui.getItemRectMin();
                NodeMax = ImGui.getItemRectMax();
                break;
            }
            case Invalid -> {
                break;
            }
        }

        return true;
    }

    protected void pin(long id, ImPinType pinType) {
        NodeEditor.beginPin(id, pinType.getId());
    }

    protected void endPin() {
        NodeEditor.endPin();
    }
}


