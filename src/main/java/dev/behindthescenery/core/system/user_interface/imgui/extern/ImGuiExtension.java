package dev.behindthescenery.core.system.user_interface.imgui.extern;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import dev.behindthescenery.core.system.user_interface.imgui.ImGuiBTSFlags;
import dev.behindthescenery.core.system.user_interface.imgui.ImGuiGLRenderHelper;
import dev.behindthescenery.core.system.user_interface.imgui.init.ImGuiBuffers;
import dev.behindthescenery.core.system.user_interface.imgui.init.ReworkImGuiBuffer;
import dev.behindthescenery.core.system.user_interface.imgui.struct.ImGuiStructs;
import imgui.ImColor;
import imgui.ImDrawList;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import oshi.util.tuples.Pair;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class ImGuiExtension {

    public static void textWithFilled(String text, float width, boolean centered) {
        textWithFilled(text, ImColor.rgba(imgui.ImGui.getStyle().getColors()[ImGuiCol.HeaderActive]), width, centered);
    }

    public static void textWithFilled(String text, int color, float width, boolean centered) {
        imgui.ImGui.getWindowDrawList().addRectFilled(
                imgui.ImGui.getCursorScreenPos(),
                imgui.ImGui.getCursorScreenPos().plus(new ImVec2(width, imgui.ImGui.getTextLineHeight())),
                color
        );
        imgui.ImGui.spacing();
        imgui.ImGui.sameLine();
        if(centered) textCentered(text);
        else imgui.ImGui.text(text);
    }

    public static void textCentered(String text) {
        float windowWidth = imgui.ImGui.getWindowSize().x;
        float textWidth = imgui.ImGui.calcTextSize(text).x;

        imgui.ImGui.setCursorPosX((windowWidth - textWidth) * 0.5f);
        imgui.ImGui.text(text);
    }

    public static void createMenuItem(String name, final ImBoolean selected) {
        createMenuItem(name, selected, true);
    }

    public static void createMenuItem(String name, final ImBoolean selected, final boolean enabled) {
        if(ImGui.menuItem(name, selected.get(), enabled)) {
            selected.set(!selected.get());
        }
    }

    public static void createID(String id, Runnable runnable) {
        ImGui.pushID(id);
        runnable.run();
        ImGui.popID();
    }


    /**
     * {@link imgui.flag.ImGuiComboFlags}
     */

    public static void comboBoxList(String id, int currentValue, Collection<String> array, Consumer<ImGuiStructs.ComboBox> onSelected) {
        comboBoxList(id, 0, currentValue, array, onSelected);
    }

    public static void comboBoxList(String id, int imGuiComboFlags, int currentValue, Collection<String> array, Consumer<ImGuiStructs.ComboBox> onSelected) {
        comboBoxList(id, currentValue, imGuiComboFlags, array.toArray(new String[0]), onSelected);
    }

    public static void comboBoxList(String id, int currentValue, String[] array, Consumer<ImGuiStructs.ComboBox> onSelected) {
        comboBoxList(id, 0, currentValue, array, onSelected);
    }

    public static void comboBoxList(String id, int imGuiComboFlags, int currentValue, String[] array, Consumer<ImGuiStructs.ComboBox> onSelected) {
        String d = "NULL";
        int currentItem = -1;

        if (currentValue >= 0 && currentValue < array.length) {
            d = array[currentValue];
            currentItem = currentValue;
        }


        if (ImGui.beginCombo(id, d, imGuiComboFlags)) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    continue;
                }

                boolean s = currentItem == i;
                if (ImGui.selectable(array[i], s)) {
                    currentItem = i;
                    if (onSelected != null) {
                        onSelected.accept(new ImGuiStructs.ComboBox(array[i], i));
                    }
                }

                if (s) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endCombo();
        }
    }

    public static void comboBoxListWithTooltip(String id, String currentKey, Map<String, Runnable> valueMap, Consumer<ImGuiStructs.ComboBox> onSelected) {
        comboBoxListWithTooltip(id, 0, currentKey, valueMap, onSelected);
    }

    public static void comboBoxListWithTooltip(String id, int imGuiComboFlags, String currentKey, Map<String, Runnable> valueMap, Consumer<ImGuiStructs.ComboBox> onSelected) {
        String d = "NULL";
        int currentItem = -1;

        Runnable[] tooltips = valueMap.values().toArray(new Runnable[0]);
        String[] array = valueMap.keySet().toArray(new String[0]);

        for (int i = 0; i < array.length; i++) {
            if(currentKey.equals(array[i])) {
                d = array[i];
                currentItem = i;
                break;
            }
        }

        if (ImGui.beginCombo(id, d, imGuiComboFlags)) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    continue;
                }

                boolean s = currentItem == i;
                if (ImGui.selectable(array[i], s)) {
                    currentItem = i;
                    if (onSelected != null) {
                        onSelected.accept(new ImGuiStructs.ComboBox(array[i], i));
                    }
                }

                if(tooltips[i] != null && ImGui.isItemHovered()) {
                    tooltips[i].run();
                }

                if (s) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endCombo();
        }
    }

    /**
     * Создаёт контейнер для Панели
     * @param id ID Панели
     */
    public static void toolBar(String id, Runnable codeBlock) {
        ImGui.pushID(id);
        codeBlock.run();
        ImGui.popID();
    }

    /**
     * Добавляет элемент кода к Панели
     */
    public static void toolBarItem(Runnable codeBlock) {
        ImGui.beginGroup();
        codeBlock.run();
        ImGui.endGroup();
    }

    /**
     * Добавляет элемент кода к Панели и располагает на одной линии (Вызывает {@link ImGui#sameLine()} перед вызовом {@link Runnable}
     */
    public static void toolBarItemLine(Runnable codeBlock) {
        ImGui.sameLine();
        ImGui.beginGroup();
        codeBlock.run();
        ImGui.endGroup();
    }

    /**
     * Создаёт группу с элементами
     * @param id ID группы
     * @param codeBlock Фрагмент кода который вызывается при открытой группы
     */
    public static void collapseGroup(String id, Runnable codeBlock) {
        collapseGroup(id, 0, codeBlock);
    }

    /**
     * Создаёт группу с элементами
     * @param id ID группы
     * @param imGuiTreeNodeFlags Флаги {@link ImGuiTreeNodeFlags}
     * @param codeBlock Фрагмент кода который вызывается при открытой группы
     */
    public static void collapseGroup(String id, int imGuiTreeNodeFlags, Runnable codeBlock) {
        if (ImGui.collapsingHeader(id, imGuiTreeNodeFlags)) {
            codeBlock.run();
        }
    }

    /**
     * Создаёт группу с элементами
     * @param id ID группы
     * @param codeBlock Фрагмент кода который вызывается при открытой группы
     * @param collapsed Закрыта или открыта группа
     */
    public static void collapseGroup(String id, ImBoolean collapsed, Runnable codeBlock) {
        collapseGroup(id, collapsed, 0, codeBlock);
    }

    /**
     * Создаёт группу с элементами
     * @param id ID группы
     * @param imGuiTreeNodeFlags Флаги {@link ImGuiTreeNodeFlags}
     * @param codeBlock Фрагмент кода который вызывается при открытой группы
     * @param collapsed Закрыта или открыта группа
     */
    public static void collapseGroup(String id, ImBoolean collapsed, int imGuiTreeNodeFlags, Runnable codeBlock) {
        if (ImGui.collapsingHeader(id, new ImBoolean(collapsed), imGuiTreeNodeFlags)) {
            codeBlock.run();
        }
    }

    /**
     * Добавляет простой Tooltip для элемента при наведении. Учтите что элемент должен поддерживать {@link ImGui#isItemHovered()} иначе применение будет к последнему элементу который поддерживает функцию
     * <pre>{@code
     * simpleTooltip("someTooltip", () -> {
     *    ImGui.text("Element 1");
     * });
     * }</pre>
     * @param tooltip Текст тултипа
     * @param toAdd Блок кода к которому будет применяться тултип
     */
    public static void simpleTooltip(String tooltip, Runnable toAdd) {
        toAdd.run();
        if(ImGui.isItemHovered()) {
            ImGui.setTooltip(tooltip);
        }
    }

    /**
     * Позволяет рендерить тултип со сложной логикой
     * <pre>{@code
     * advancedTooltip(() -> {
     *     ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 10, 10);
     *     ImGui.text("Detailed Information:");
     *     ImGui.separator();
     *     ImGui.text("Name: John Doe");
     *     ImGui.text("Age: 30");
     *     ImGui.text("Occupation: Developer");
     *     ImGui.popStyleVar();
     * }, () -> {
     *     ImGui.text("Element 1");
     * });
     * }</pre>
     * @param codeBlock Блок кода к тултипа (К коду уже будет применён {@link ImGui#beginTooltip()} и {@link ImGui#endTooltip()}
     * @param toAdd Блок кода к которому будет применяться тултип
     */
    public static void advancedTooltip(Runnable codeBlock, Runnable toAdd) {
        toAdd.run();
        if(ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            codeBlock.run();
            ImGui.endTooltip();
        }
    }

    public static void advancedTooltip(Runnable codeBlock) {
        if(ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            codeBlock.run();
            ImGui.endTooltip();
        }
    }

    public static void advancedTooltipWithoutCheck(Runnable codeBlock) {
        if(ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            codeBlock.run();
            ImGui.endTooltip();
        }
    }

    /**
     * Позволяет рендерить предметы в контексте ImGui
     * @param name Имя контейнера
     * @param item Предмет
     * @param width Ширина рендера предмета
     * @param height Высота рендера предмета
     * @return Если предмет нажат
     */
    public static boolean drawItem(String name, ItemStack item, float width, float height) {
        return drawItem(name, item, width, height, 0);
    }


    /**
     * Позволяет рендерить предметы в контексте ImGui
     * @param name Имя контейнера
     * @param item Предмет
     * @param width Ширина рендера предмета
     * @param height Высота рендера предмета
     * @param imGuiBTSFlags Флаги рендера {@link ImGuiBTSFlags}
     * @return Если предмет нажат
     */
    public static boolean drawItem(String name, ItemStack item, float width, float height, int imGuiBTSFlags) {
        return drawItem(name, item, width, height, 1, 0, imGuiBTSFlags, SimpleColor.DEFAULT);
    }

    /**
     * Позволяет рендерить предметы в контексте ImGui
     * @param name Имя контейнера
     * @param item Предмет
     * @param width Ширина контейнера
     * @param height Высота контейнера
     * @param imGuiBTSFlags Флаги рендера {@link ImGuiBTSFlags}
     * @param scale Размер предмета
     * @param rotation Вращение предмета
     * @param color Цвета рендера {@link SimpleColor}, работает так же как и {@link RenderSystem#setShaderColor(float r, float g, float b, float a)}
     * @return Если предмет нажат
     */
    public static boolean drawItem(String name, ItemStack item, float width, float height, float scale, float rotation, int imGuiBTSFlags, SimpleColor color) {
        ImGui.pushID(name);
        ImVec2 cPos = ImGui.getCursorPos();
        boolean clicked = reworkDrawMcRender(width, height, imGuiBTSFlags, color, (
            (cursor, hovered) -> {
                MatrixStack stack = new MatrixStack();
                if((imGuiBTSFlags & ImGuiBTSFlags.ALWAYS_ON_TOP) != 0)
                    stack.translate(0f, 0f, 200f);


                stack.push();
                ImGuiGLRenderHelper.renderItemStack(item, stack,
                        cursor.x, cursor.y, width, height,
                        scale, rotation
                 );
                stack.pop();

                ImGuiGLRenderHelper.renderItemDecorations(item, stack, (int) cursor.x, (int) cursor.y, width, height);
            }
        ));

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) return false;

        ImVec2 pos = ImGui.getCursorScreenPos();

//        if (ImGui.isMouseHoveringRect(
//                pos.x,
//                pos.y,
//                pos.x + width,
//                pos.y + height
//        ) && !item.isEmpty() && ((imGuiBTSFlags & ImGuiBTSFlags.RENDER_TOOLTIP) == 0)
//        ) {
//            ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0f, 0f);
//            ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0f);
//            ImGui.pushStyleVar(ImGuiStyleVar.PopupBorderSize, 0f);
//            ImGui.pushStyleColor(ImGuiCol.Border, 1f, 1f, 1f, 1f);
//            ImGui.pushStyleColor(ImGuiCol.PopupBg, 0f, 0f, 0f, 0f);
//
//            /*
//            tooltip(() -> {
//                float borderSize = 5f;
//                ImVec2 min = ImGui.getWindowPos();
//                ImVec2 max = min.clone().plus(ImGui.getWindowSize());
//                int top = ImGui.colorConvertFloat4ToU32(0.19215688f, 0.09607843f, 0.45882353f, 1f);
//                int bottom = ImGui.colorConvertFloat4ToU32(0.13725491f, 0.07058824f, 0.23921569f, 1f);
//
//                ImGui.getForegroundDrawList().addRectFilled(min.x, min.y, max.x, min.y + borderSize, top);
//                ImGui.getForegroundDrawList()
//                        .addRectFilledMultiColor(min.x, min.y, min.x + borderSize, max.y, top, top, bottom, bottom);
//                ImGui.getForegroundDrawList().addRectFilled(min.x, max.y - borderSize, max.x, max.y, bottom);
//                ImGui.getForegroundDrawList()
//                        .addRectFilledMultiColor(max.x - borderSize, min.y, max.x, max.y, top, top, bottom, bottom);
//
//                top = ImGui.colorConvertFloat4ToU32(0.06f, 0.06f, 0.06f, 0.75f);
//                bottom = ImGui.colorConvertFloat4ToU32(0.12f, 0.12f, 0.12f, 0.4f);
//                ImGui.getWindowDrawList().addRectFilledMultiColor(
//                        min.x + borderSize, min.y + borderSize, max.x - borderSize, max.y - borderSize,
//                        top, top, top, top
//                );
//
//                ImGui.dummy(0f, borderSize / 2);
//                item.getTooltipLines(
//                        Item.TooltipContext.of(player.level()), player, TooltipFlag.Default.NORMAL
//                ).forEach((s) -> {
//                    ImGui.setCursorPosX(ImGui.getCursorPosX() + borderSize * 2);
////                    text(s);
//                });
//                ImGui.dummy(borderSize, borderSize);
//            });
//
//
//             */
//            ImGui.popStyleColor(2);
//            ImGui.popStyleVar(3);
//        }


        if ((imGuiBTSFlags & ImGuiBTSFlags.ALWAYS_ON_TOP) == 0) {
            ImGui.setCursorPos(cPos.x, cPos.y);
            ImGui.dummy(width, height);
        }
        ImGui.popID();

        return clicked;
    }

    /**
     * Позволяет вызывать методы из Minecraft Render
     * @param width Ширина контейнера
     * @param height Высота контейнера
     * @param imGuiBTSFlags Флаги рендера {@link ImGuiBTSFlags}
     * @param color Цвета рендера {@link SimpleColor}, работает так же как и {@link RenderSystem#setShaderColor(float r, float g, float b, float a)}
     * @param render BiConsumer который вызывается для рендера
     * @return Если контекст нажат
     */
    public static boolean reworkDrawMcRender(float width, float height, int imGuiBTSFlags, SimpleColor color, BiConsumer<ImVec2, Boolean> render) {
        final MinecraftClient client = MinecraftClient.getInstance();
        final Framebuffer mcBuffer = client.getFramebuffer();

        final Framebuffer imGuiBuffer = ReworkImGuiBuffer.getMainRenderBuffer();

        final boolean isMac = MinecraftClient.IS_SYSTEM_MAC;

        final int imGuiBufferWidth = imGuiBuffer.textureWidth;
        final int imGuiBufferHeight = imGuiBuffer.textureHeight;

        mcBuffer.endWrite();
        imGuiBuffer.beginWrite(true);

        RenderSystem.clearColor(0f, 0f, 0f, 0f);
        RenderSystem.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT, isMac);

        RenderSystem.backupProjectionMatrix();

        RenderSystem.setProjectionMatrix(
                new Matrix4f().setOrtho(
                        0f,
                        imGuiBufferWidth,
                        imGuiBufferHeight,
                        0f,
                        1000f,
                        3000f
                ),
                VertexSorter.BY_Z
        );

        final Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushMatrix();
        matrixStack.translate(0f,0f, -2000f);
        RenderSystem.applyModelViewMatrix();

        ImVec2 cursorPos = ImGui.getCursorScreenPos();

        final boolean isHovered = ImGui.isMouseHoveringRect(cursorPos.x, cursorPos.y,
                cursorPos.x + width, cursorPos.y + height);
        final boolean isClicked = isHovered && ImGui.isMouseClicked(0);

        final boolean useScissor = (imGuiBTSFlags & ImGuiBTSFlags.ENABLE_SCISSOR) != 0;

        if(useScissor) {
            final int scissorX = (int) cursorPos.x;
            final int scissorY = (int) (imGuiBufferHeight - cursorPos.y - height);
            RenderSystem.enableScissor(
                    scissorX, scissorY,
                    (int) width, (int) height
            );
        }

        RenderSystem.enableDepthTest();

        render.accept(cursorPos, isHovered);

        if(useScissor) {
            RenderSystem.disableScissor();
        }

        RenderSystem.restoreProjectionMatrix();
        matrixStack.popMatrix();
        RenderSystem.applyModelViewMatrix();

        imGuiBuffer.endWrite();
        mcBuffer.beginWrite(true);

        final float u0 =  cursorPos.x / imGuiBufferWidth;
        final float u1 =  (cursorPos.x + width) / imGuiBufferWidth;
        final float v0 =  1f - cursorPos.y / imGuiBufferHeight;
        final float v1 =  1f - (cursorPos.y + height) / imGuiBufferHeight;

        final boolean alwaysOnTop = (imGuiBTSFlags & ImGuiBTSFlags.ALWAYS_ON_TOP) != 0;
        final boolean useBorder = (imGuiBTSFlags & ImGuiBTSFlags.BORDER) != 0;

        final ImDrawList list = alwaysOnTop ? ImGui.getForegroundDrawList() : ImGui.getWindowDrawList();

        cursorPos = ImGui.getCursorScreenPos();

        list.addImage(
                imGuiBuffer.getColorAttachment(), cursorPos.x, cursorPos.y,
                cursorPos.x + width, cursorPos.y + height,
                u0, v0, u1, v1,
                color.argbI()
        );

        if(useBorder) {
            list.addRect(
                    cursorPos.x, cursorPos.y,
                    cursorPos.x + width, cursorPos.y + height, -1
            );
        }

        return isClicked;
    }

    /**
         * Позволяет вызывать методы из Minecraft Render
         * @param width Ширина контейнера
         * @param height Высота контейнера
         * @param imGuiBTSFlags Флаги рендера {@link ImGuiBTSFlags}
         * @param color Цвета рендера {@link SimpleColor}, работает так же как и {@link RenderSystem#setShaderColor(float r, float g, float b, float a)}
         * @param render BiConsumer который вызывается для рендера
         * @return Если контекст нажат
         */
    public static boolean drawMcRender(float width, float height, int imGuiBTSFlags, SimpleColor color, BiConsumer<ImVec2, Boolean> render) {

        Framebuffer mcBuffer = MinecraftClient.getInstance().getFramebuffer();
        mcBuffer.endWrite();

        SimpleFramebuffer buffer = ImGuiBuffers.getBuffer();
        buffer.beginWrite(true);

        ImVec2 cursorPos = ImGui.getCursorScreenPos();

        Window window = MinecraftClient.getInstance().getWindow();

        boolean isHovered = ImGui.isMouseHoveringRect(cursorPos.x, cursorPos.y, cursorPos.x + width, cursorPos.y + height);
        boolean isClicked = isHovered && ImGui.isMouseClicked(0);
        RenderSystem.backupProjectionMatrix();

        if((imGuiBTSFlags & ImGuiBTSFlags.USE_MINECRAFT_MATRIX) != 0) {
            RenderSystem.setProjectionMatrix(
                    new Matrix4f().setOrtho(
                            0.0F,
                            (float) ((double) window.getWidth() / window.getScaleFactor()),
                            (float) ((double) window.getHeight() / window.getScaleFactor()),
                            0.0F,
                            1000.0F,
                            3000.0F
                    ), VertexSorter.BY_Z
            );
        } else {
            RenderSystem.setProjectionMatrix(
                    new Matrix4f().setOrtho(
                            0.0F,
                            buffer.textureWidth,
                            buffer.textureHeight,
                            0.0F,
                            1000.0F,
                            3000.0F
                    ), VertexSorter.BY_Z
            );
        }

        Matrix4fStack matrix4fstack = RenderSystem.getModelViewStack();
        matrix4fstack.pushMatrix();
        matrix4fstack.translation(0.0f, 0.0f, -2000.0f);
        RenderSystem.applyModelViewMatrix();

        if ((imGuiBTSFlags & ImGuiBTSFlags.ENABLE_SCISSOR) != 0) RenderSystem.enableScissor(
                (int) cursorPos.x, (int) (buffer.textureHeight - cursorPos.y - height),
                (int) width, (int) height
        );
        RenderSystem.enableDepthTest();

        render.accept(cursorPos, isHovered);

        if ((imGuiBTSFlags & ImGuiBTSFlags.ENABLE_SCISSOR) != 0) RenderSystem.disableScissor();
        RenderSystem.restoreProjectionMatrix();

        matrix4fstack.popMatrix();
        RenderSystem.applyModelViewMatrix();

        buffer.endWrite();
        mcBuffer.beginWrite(true);

        float u0 = cursorPos.x / buffer.textureWidth;
        float u1 = (cursorPos.x + width) / buffer.textureWidth;
        float v0 = 1f - cursorPos.y / buffer.textureHeight;
        float v1 = 1f - (cursorPos.y + height) / buffer.textureHeight;

        ImDrawList list = (imGuiBTSFlags & ImGuiBTSFlags.ALWAYS_ON_TOP) != 0 ? ImGui.getForegroundDrawList() : ImGui.getWindowDrawList();

        ImVec2 cursor = ImGui.getCursorScreenPos();

        pushStyle(List.of(
                new Pair<>(ImGuiCol.Button, ImGui.colorConvertFloat4ToU32(0f, 0f, 0f, 0f)),
                new Pair<>(ImGuiCol.ButtonActive, ImGui.colorConvertFloat4ToU32(0f, 0f, 0f, 0f)),
                new Pair<>(ImGuiCol.ButtonHovered, ImGui.colorConvertFloat4ToU32(0f, 0f, 0f, 0f))
        ),
            () -> {
                list.addImage(
                        buffer.getColorAttachment(), cursor.x, cursor.y,
                        cursor.x + width, cursor.y + height, u0, v0, u1, v1,
                        color.argbI()
                );
                if ((imGuiBTSFlags & ImGuiBTSFlags.BORDER) != 0) list.addRect(
                        cursor.x, cursor.y,
                        cursor.x + width, cursor.y + height, -1
                );
            }
        );

        return isClicked;
    }

    private static void tooltip(Runnable runnable) {
        ImGui.beginTooltip();
        runnable.run();
        ImGui.endTooltip();
    }

//    private static void text(Component text, float alpha, boolean shadow) {
//        TextColor color = text.getStyle().getColor();
//        if (color != null) {
//            int value = color.getValue();
//            int r = FastColor.ARGB32.red(value);
//            int g = FastColor.ARGB32.green(value);
//            int b = FastColor.ARGB32.blue(value);
//
////            pushColorStyle(ImGuiCol.Text, ImGui.colorConvertFloat4ToU32(r / 255f, g / 255f, b / 255f, alpha)) {
////                drawText(text, alpha, shadow)
////            }
//        } else {
//            drawText(text, alpha, shadow);
//        }
//    }
//
//    private static void drawText(Component text, float alpha, boolean shadow) {
//    }

    private static void pushStyle(Collection<Pair<Integer, Integer>> styles, Runnable runnable) {
        styles.forEach(style -> ImGui.pushStyleColor(style.getA(), style.getB()));
        runnable.run();
        ImGui.popStyleColor(styles.size());
    }


    public static double getGuiScale() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        Window window = minecraft.getWindow();
//

//        double frame = ((WindowAccessor)window).getFramebufferWidth();
//        double guiScale = minecraft.getWindow().getGuiScale();
//        int i = (int)((double)minecraft.framebufferWidth / scaleFactor);
        return window.getScaleFactor();
    }


    /**
     * Пока не придумал как заставить это работать
     */
    @Deprecated
    public static boolean drawOpenGL(float width, float height, int imGuiBTSFlags, SimpleColor color, BiConsumer<ImVec2, Boolean> render) {

        Framebuffer mcBuffer = MinecraftClient.getInstance().getFramebuffer();
        mcBuffer.endWrite();

        // Создаем и настраиваем FBO (Framebuffer Object)
        int framebuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);

        // Создаем текстуру для буфера цвета
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, (int) width, (int) height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        // Привязываем текстуру к FBO
        GL30.glFramebufferTexture2D(GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);

        // Создаем буфер глубины
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, (int) width, (int) height);
        GL30.glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);

        // Проверяем, что FBO корректен
        if (GL30.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Framebuffer is not complete!");
        }

        // Сохраняем текущий контекст OpenGL
        GL30.glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        GL11.glViewport(0, 0, (int) width, (int) height);

        // Сохраняем состояние проекции и матрицы
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, width, height, 0.0, -1000.0, 3000.0);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);

        // Включаем Scissor Test, если нужно
        if ((imGuiBTSFlags & ImGuiBTSFlags.ENABLE_SCISSOR) != 0) {
            glEnable(GL_SCISSOR_TEST);
            glScissor(0, 0, (int) width, (int) height);
        }

        // Выполняем пользовательский рендер
        ImVec2 cursorPos = ImGui.getCursorScreenPos();
        boolean isHovered = ImGui.isMouseHoveringRect(cursorPos.x, cursorPos.y, cursorPos.x + width, cursorPos.y + height);
        boolean isClicked = isHovered && ImGui.isMouseClicked(0);
        render.accept(cursorPos, isHovered);

        // Отключаем Scissor Test
        if ((imGuiBTSFlags & ImGuiBTSFlags.ENABLE_SCISSOR) != 0) {
            glDisable(GL_SCISSOR_TEST);
        }

        // Восстанавливаем матрицы
        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPopMatrix();

        // Восстанавливаем исходный буфер кадра
        GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);

        // Рисуем результат FBO как текстуру
        ImDrawList drawList = ImGui.getWindowDrawList();
        drawList.addImage(texture, cursorPos.x, cursorPos.y, cursorPos.x + width, cursorPos.y + height, 0, 0, 1, 1, color.argbI());

        // Удаляем FBO и связанные ресурсы
        glDeleteFramebuffers(framebuffer);
        glDeleteTextures(texture);
        glDeleteRenderbuffers(depthBuffer);

        return isClicked;
    }
}
