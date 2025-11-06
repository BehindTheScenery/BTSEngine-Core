package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.utils;

import dev.behindthescenery.core.system.user_interface.imgui.editing.DefaultImGuiClassEditorContainer;
import dev.behindthescenery.core.system.user_interface.imgui.editing.ImClassEditorStruct;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.ImNodeVariableContainer;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.NodeIconType;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePin;
import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node.ImNodePinObjectColors;
import imgui.*;
import imgui.flag.ImDrawFlags;

import java.util.Map;
import java.util.Optional;

public class ImNodeUtils {

    public static final Map<Class<?>, ImVec4> COLOR_BY_CLASS = createColorByClass();

    public static void drawIcon(ImNodePin pin, ImVec2 size, NodeIconType type, boolean filled, ImVec4 color, ImVec4 innerColor) {
        if(ImGui.isRectVisible(size)) {
            ImVec2 cursorPos = ImGui.getCursorScreenPos();
            ImDrawList drawList  = ImGui.getWindowDrawList();

            final ImVec2 position = pin.getType().isInput() ?
                    cursorPos.plus(new ImVec2(size.x - 1, size.y / 2)) :
                    cursorPos.plus(new ImVec2(0, size.y / 2));

            drawShape(drawList, size, position, type, filled, ImColor.rgba(color), ImColor.rgba(innerColor));
        }

        ImGui.dummy(size);
    }

    public static void drawIcon(ImVec2 size, NodeIconType type, boolean filled, ImVec4 color, ImVec4 innerColor) {
        if(ImGui.isRectVisible(size)) {
            ImVec2 cursorPos = ImGui.getCursorScreenPos();
            ImDrawList drawList  = ImGui.getWindowDrawList();
            drawShape(drawList, size, cursorPos.plus(size), type, filled, ImColor.rgba(color), ImColor.rgba(innerColor));
        }

        ImGui.dummy(size);
    }

    public static void drawShape(ImDrawList drawList, final ImVec2 size, final ImVec2 pos, NodeIconType type, boolean filled, int color, int innerColor) {
        switch (type) {
            case Circle:
                drawCircle(drawList, size, pos, filled, color, innerColor);
                break;
            case Square:
                drawSquare(drawList, size, pos, filled, color, innerColor);
                break;
            case RoundSquare:
                drawRoundSquare(drawList, size, pos, filled, color, innerColor);
                break;
            case Diamond:
                drawDiamond(drawList, size, pos, filled, color, innerColor);
                break;
            case Grid:
                drawGrid(drawList, size, pos, filled, color, innerColor);
                break;
            case Flow:
                drawFlow(drawList, size, pos, filled, color, innerColor);
                break;
            default:
                throw new IllegalArgumentException("Unsupported shape type: " + type);
        }
    }

    private static void drawCircle(ImDrawList drawList, ImVec2 size, ImVec2 pos, boolean filled, int color, int innerColor) {
        if (filled) {
            drawList.addCircleFilled(pos.x, pos.y, size.x, color, 32); // 32 сегмента для гладкости
        } else {
            if((innerColor & 0xFF000000) != 0)
                drawList.addCircleFilled(pos.x, pos.y, size.x, innerColor, 32); // Толщина линии 2 пикселя
            drawList.addCircle(pos.x, pos.y, size.x, color, 32, 2.0f); // Толщина линии 2 пикселя
        }
    }

    private static void drawSquare(ImDrawList drawList, ImVec2 size, ImVec2 pos, boolean filled, int color, int innerColor) {
        ImVec2 p1 = new ImVec2(pos.x - size.x, pos.y - size.y);
        ImVec2 p2 = new ImVec2(pos.x + size.x, pos.y + size.y);
        if (filled) {
            drawList.addRectFilled(p1.x, p1.y, p2.x, p2.y, color);
        } else {
            if((innerColor & 0xFF000000) != 0)
                drawList.addRectFilled(p1.x, p1.y, p2.x, p2.y, innerColor);
            drawList.addRect(p1.x, p1.y, p2.x, p2.y, color, 0.0f, 0, 2.0f);
        }
    }

    private static void drawRoundSquare(ImDrawList drawList, ImVec2 size, ImVec2 pos, boolean filled, int color, int innerColor) {
        ImVec2 p1 = new ImVec2(pos.x - size.x, pos.y - size.y);
        ImVec2 p2 = new ImVec2(pos.x + size.x, pos.y + size.y);
        float rounding = Math.min(size.x, size.y) * 0.1f;
        if (filled) {
            drawList.addRectFilled(p1.x, p1.y, p2.x, p2.y, color, rounding);
        } else {
            if((innerColor & 0xFF000000) != 0)
                drawList.addRectFilled(p1.x, p1.y, p2.x, p2.y, innerColor, rounding);
            drawList.addRect(p1.x, p1.y, p2.x, p2.y, color, rounding, 0, 2.0f);
        }
    }

    private static void drawDiamond(ImDrawList drawList, ImVec2 size, ImVec2 pos, boolean filled, int color, int innerColor) {
        final float dividedSizeW = size.x / 4;
        final float dividedSizeH = size.y / 4;

        ImVec2[] points = {
                new ImVec2(pos.x, pos.y - (size.y - dividedSizeH)),         // Верх
                new ImVec2(pos.x + (size.x - dividedSizeW), pos.y),         // Право
                new ImVec2(pos.x, pos.y + (size.y - dividedSizeH)),         // Низ
                new ImVec2(pos.x - (size.x - dividedSizeW), pos.y)          // Лево
        };
        if (filled) {
            drawList.addConvexPolyFilled(points, points.length, color);
        } else {
            if((innerColor & 0xFF000000) != 0)
                drawList.addConvexPolyFilled(points, points.length, innerColor);
            drawList.addPolyline(points, points.length, color, ImDrawFlags.Closed, 2.0f);
        }
    }

    private static void drawGrid(ImDrawList drawList, ImVec2 _size, ImVec2 pos, boolean filled, int color, int innerColor) {
        int gridSize = 4;
        final ImVec2 size = new ImVec2(_size.x * 2, _size.y * 2);

        float cellWidth = size.x / gridSize;
        float cellHeight = size.y / gridSize;
        float halfWidth = size.x / 2;
        float halfHeight = size.y / 2;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                float x = pos.x - halfWidth + i * cellWidth;
                float y = pos.y - halfHeight + j * cellHeight;
                if (filled && (i + j) % 2 == 0) {
                    drawList.addRectFilled(x, y, x + cellWidth, y + cellHeight, color);
                } else {
                    drawList.addRectFilled(x, y, x + cellWidth, y + cellHeight, innerColor);
                }
            }
        }
    }

    private static void drawFlow(ImDrawList drawList, ImVec2 size, ImVec2 pos, boolean _filled, int color, int innerColor) {
        final float dividedSizeW = size.x / 2;
        final float ddividedSizeW = size.x / 4;
        final float right        = pos.x + size.x;
        final float top          = pos.y - (size.x - ddividedSizeW);
        final float bottom       = pos.y + (size.x - ddividedSizeW);
        final ImVec2 leftTop     = new ImVec2(pos.x - dividedSizeW, top);
        final ImVec2 leftBottom  = new ImVec2(pos.x - dividedSizeW, bottom);

        final ImVec2 tipTop    = new ImVec2(pos.x + ddividedSizeW, pos.y - (size.x - ddividedSizeW));
        final ImVec2 tipBottom = new ImVec2(pos.x + ddividedSizeW, pos.y + (size.x - ddividedSizeW));
        final ImVec2 tipRight = new ImVec2(right - ddividedSizeW, pos.y);

        ImVec2[] points = {
                leftTop, leftBottom, tipBottom, tipRight, tipTop
        };

        if (_filled) {
            drawList.addConvexPolyFilled(points, points.length, color);
        } else {
            if((innerColor & 0xFF000000) != 0) {
                drawList.addConvexPolyFilled(points, points.length, innerColor);
            }

            drawList.pathClear();
            for (ImVec2 point : points) {
                drawList.pathLineTo(point);
            }

            drawList.pathStroke(color, 1, 1f);
        }
    }

    public static ImVec4 getColorByClass(Class<?> classObj) {
        if (classObj == null) {
            return getDefaultColor();
        }

        return COLOR_BY_CLASS.getOrDefault(classObj, getDefaultColor());
    }

    private static Map<Class<?>, ImVec4> createColorByClass() {
        return Map.of(
                Boolean.class, ImNodePinObjectColors.BOOLEAN,
                String.class, ImNodePinObjectColors.STRING,
                Byte.class, ImNodePinObjectColors.BYTE,
                Integer.class, ImNodePinObjectColors.INTEGER,
                Long.class, ImNodePinObjectColors.INTEGER,
                Float.class, ImNodePinObjectColors.FLOAT,
                Double.class, ImNodePinObjectColors.FLOAT
        );
    }

    private static ImVec4 getDefaultColor() {
        return ImNodePinObjectColors.OBJECT;
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = Map.of(
            int.class, Integer.class,
            float.class, Float.class,
            double.class, Double.class,
            boolean.class, Boolean.class
    );

    public static Runnable createEditFieldForClass(ImNodePin pin, Class<?> classObj, Object defaultValue, ImNodeVariableContainer variableContainer) {
        return createEditFieldForClass(pin, classObj, defaultValue, variableContainer, 0, 0);
    }

    public static Runnable createEditFieldForClass(ImNodePin pin, Class<?> classObj, Object defaultValue, ImNodeVariableContainer variableContainer, float elementWidth, int elementFlags) {
        final Class<?> _class = PRIMITIVE_TO_WRAPPER.getOrDefault(classObj, classObj);
        Optional<ImClassEditorStruct> constructor = DefaultImGuiClassEditorContainer.Instance.getEditor(_class);
        if(constructor.isEmpty()) return () -> {};

        final long pinId = pin.getPinId();

        var editor = constructor.get().getClassEditor();
        if(editor == null) return () -> {};

        return editor.createClassEditor("##" + pin.getName(),
                () -> variableContainer.getOrCreate(pinId, defaultValue),
                (newValue) -> variableContainer.setValue(pinId, newValue), elementWidth, elementFlags
        );
    }
}
