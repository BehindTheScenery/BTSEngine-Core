package dev.behindthescenery.core.system.rendering.utils.helpers;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * Методы для взаимодействия с {@link MatrixStack}, вращения, масштабирования, перемещения
 */
public class GLRenderHelper {

    /**
     * Метод добавляет резчик
     * @param pos Координаты
     * @param size Размер
     */
    public static void enableScissor(DrawContext guiGraphics, Vector2i pos, Vector2i size) {
        guiGraphics.enableScissor(pos.x, pos.y, pos.x + size.x, pos.y + size.y);
    }

    /**
     * Метод добавляет резчик для блока кода
     * @param pos Координаты
     * @param size Размер
     * @param runnable Блок кода
     */
    public static void enableScissorFor(DrawContext guiGraphics, Vector2i pos, Vector2i size, Runnable runnable) {
        enableScissor(guiGraphics, pos, size);
        runnable.run();
        disableScissor(guiGraphics);
    }

    /**
     * Метод добавляет резчик
     * @param posX Координаты X
     * @param posY Координаты Y
     * @param weight Размер Ширина
     * @param height Размер Высота
     */
    public static void enableScissor(DrawContext guiGraphics, int posX, int posY, int weight, int height) {
        guiGraphics.enableScissor(posX, posY, posX + weight, posY + height);
    }


    /**
     * Метод добавляет резчик для блока кода
     * @param posX Координаты X
     * @param posY Координаты Y
     * @param weight Размер Ширина
     * @param height Размер Высота
     * @param runnable Блок кода
     */
    public static void enableScissorFor(DrawContext guiGraphics, int posX, int posY, int weight, int height, Runnable runnable) {
        enableScissor(guiGraphics, posX, posY, weight, height);
        runnable.run();
        disableScissor(guiGraphics);
    }

    /**
     * Метод отключает резчик
     */
    public static void disableScissor(DrawContext guiGraphics) {
        guiGraphics.disableScissor();
    }

    /**
     * Метод масштабирования
     * @param scale Уровень масштабирования
     * @param pos Координаты от которых происходит масштабирования (Обычно это координаты виджета)
     */
    public static void setScale(DrawContext guiGraphics, float scale, Vector2i pos) {
        setScale(guiGraphics, scale, pos.x, pos.y);
    }

    /**
     * Метод масштабирования для блока кода
     * @param scale Уровень масштабирования
     * @param pos Координаты от которых происходит масштабирования (Обычно это координаты виджета)
     * @param runnable Блок кода
     */
    public static void setScale(DrawContext guiGraphics, float scale, Vector2f pos, Runnable runnable) {
        setScale(guiGraphics,scale,pos.x, pos.y, runnable);
    }

    /**
     * Метод масштабирования
     * @param scale Уровень масштабирования
     * @param centerX Координаты X от которых происходит масштабирования (Обычно это координаты виджета)
     * @param centerY Координаты Y от которых происходит масштабирования (Обычно это координаты виджета)
     */
    public static void setScale(DrawContext guiGraphics, float scale, float centerX, float centerY) {
        MatrixStack poseStack = guiGraphics.getMatrices();

        poseStack.translate(centerX, centerY, 0);
        poseStack.scale(scale, scale, 1);

        poseStack.translate(-centerX, -centerY, 0);

    }

    /**
     * Метод масштабирования для блока кода
     * @param scale Уровень масштабирования
     * @param centerX Координаты X от которых происходит масштабирования (Обычно это координаты виджета)
     * @param centerY Координаты Y от которых происходит масштабирования (Обычно это координаты виджета)
     * @param runnable Блок кода
     */
    public static void setScale(DrawContext guiGraphics, float scale, float centerX, float centerY, Runnable runnable) {
        MatrixStack poseStack = guiGraphics.getMatrices();
        poseStack.push();

        poseStack.translate(centerX, centerY, 0);
        poseStack.scale(scale, scale, 1);
        poseStack.translate(-centerX, -centerY, 0);

        runnable.run();

        poseStack.pop();
    }

    /**
     * Метод применяет масштабирования на основе экрана для блока кода
     * @param scale Уровень масштабирования
     * @param runnable Блок кода
     */
    public static void setScaleByScreen(DrawContext guiGraphics, float scale, Runnable runnable) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        Window window = minecraft.getWindow();
        MatrixStack poseStack = guiGraphics.getMatrices();

        int w = window.getScaledWidth();
        int h = window.getScaledHeight();

        poseStack.translate((float) w / 2, (float) h / 2, 0);
        poseStack.scale(scale, scale, 1);
        poseStack.translate(-(float) w / 2, -(float) h / 2, 0);
        runnable.run();


    }

    /**
     * Метод для вращения
     * @param rotation Угол поворота
     * @param pos Координаты от которых происходит вращения (Обычно это координаты виджета)
     */
    public static void setRotation(DrawContext guiGraphics, float rotation, Vector2f pos) {
        setRotation(guiGraphics, rotation, pos.x, pos.y);
    }

    /**
     * Метод для вращения для блока кода
     * @param rotation Угол поворота
     * @param pos Координаты от которых происходит вращения (Обычно это координаты виджета)
     * @param runnable Блок Кода
     */
    public static void setRotation(DrawContext guiGraphics, float rotation, Vector2f pos, Runnable runnable) {
        setRotation(guiGraphics, rotation, pos.x, pos.y, runnable);
    }

    /**
     * Метод для вращения для блока кода
     * @param rotation Угол поворота
     * @param centerX Координаты X от которых происходит масштабирования (Обычно это координаты виджета)
     * @param centerY Координаты Y от которых происходит масштабирования (Обычно это координаты виджета)
     */
    public static void setRotation(DrawContext guiGraphics, float rotation, float centerX, float centerY) {
        MatrixStack poseStack = guiGraphics.getMatrices();

        poseStack.translate(centerX, centerY, 0);
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        poseStack.translate(-centerX, -centerY, 0);
    }

    /**
     * Метод для вращения для блока кода
     * @param rotation Угол поворота
     * @param centerX Координаты X от которых происходит масштабирования (Обычно это координаты виджета)
     * @param centerY Координаты Y от которых происходит масштабирования (Обычно это координаты виджета)
     * @param runnable Блок Кода
     */
    public static void setRotation(DrawContext guiGraphics, float rotation, float centerX, float centerY, Runnable runnable) {
        MatrixStack poseStack = guiGraphics.getMatrices();
        poseStack.push();

        poseStack.translate(centerX, centerY, 0);
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        poseStack.translate(-centerX, -centerY, 0);

        runnable.run();

        poseStack.pop();
    }

    /**
     * Метод, который реализует {@link #setRotation(DrawContext, float, Vector2f)} {@link #setScale(DrawContext, float, Vector2f)}
     */
    public static void setTransform(DrawContext guiGraphics, Vector2i pos, float rotation, float scale) {
        setTransform(guiGraphics, pos.x, pos.y, rotation, scale);
    }

    /**
     * Метод, который реализует {@link #setRotation(DrawContext, float, Vector2f)} {@link #setScale(DrawContext, float, Vector2f)}
     */
    public static void setTransform(DrawContext guiGraphics, Vector2i pos, float rotation, float scale, Runnable runnable) {
        setTransform(guiGraphics, pos.x, pos.y, rotation, scale, runnable);
    }

    /**
     * Метод, который реализует {@link #setRotation(DrawContext, float, Vector2f)} {@link #setScale(DrawContext, float, Vector2f)}
     */
    public static void setTransform(DrawContext guiGraphics, float centerX, float centerY, float rotation, float scale) {
        MatrixStack poseStack = guiGraphics.getMatrices();

        poseStack.translate(centerX, centerY, 0);
        poseStack.scale(scale, scale, 1);
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        poseStack.translate(-centerX, -centerY, 0);

    }

    /**
     * Метод, который реализует {@link #setRotation(DrawContext, float, Vector2f)} {@link #setScale(DrawContext, float, Vector2f)}
     */
    public static void setTransform(DrawContext guiGraphics, float centerX, float centerY, float rotation, float scale, Runnable runnable) {
        MatrixStack poseStack = guiGraphics.getMatrices();

        poseStack.push();

        poseStack.translate(centerX, centerY, 0);
        poseStack.scale(scale, scale, 1);
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        poseStack.translate(-centerX, -centerY, 0);

        runnable.run();

        poseStack.pop();

    }

    /**
     * Метод для установки глубины
     * @param depth Глубина
     */
    public static void setDepth(DrawContext guiGraphics, int depth) {
        guiGraphics.getMatrices().translate(0,0,depth);
    }

    /**
     * Метод для установки глубины для блока кода
     * @param depth Глубина
     * @param runnable Блок Кода
     */
    public static void setDepth(DrawContext guiGraphics, int depth, Runnable runnable) {
        guiGraphics.getMatrices().push();
        guiGraphics.getMatrices().translate(0,0,depth);
        runnable.run();
        guiGraphics.getMatrices().pop();
    }

    /**
     * Позволяет вызывать методы OpenGL на прямую
     */
    public static void glRender(Runnable runnable) {
        if(RenderSystem.isOnRenderThread())
            runnable.run();
        else RenderSystem.recordRenderCall(runnable::run);
    }

}
