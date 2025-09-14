package dev.behindthescenery.core.system.rendering.utils.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.buffers.RenderBuffer2D;
import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import dev.behindthescenery.core.system.rendering.utils.DrawDirection;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;

/**
 * Метод для отрисовки фигур
 */
public class ShapesRenderHelper {


    public static void drawQuad(final Matrix4f m, 
                                final net.minecraft.client.util.math.Vector2f pos, 
                                final net.minecraft.client.util.math.Vector2f size, 
                                final SimpleColor color) {
        drawQuad(m, new Vector2f(pos.getX(), pos.getY()), new Vector2f(size.getX(), size.getY()), color);
    }

    public static void drawQuad(final Matrix4f m, final Vector2f pos, final Vector2f size, final SimpleColor color) {
        final int r = color.getRedI();
        final int g = color.getGreenI();
        final int b = color.getBlueI();
        final int a = color.getAlphaI();
        drawQuad(m, pos, size, r, g, b, a);
    }

    public static void drawQuad(final Matrix4f m, final Vector2f pos, final Vector2f size,
                                final int r, final int g, final int b, final int a) {
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        RenderBuffer2D.addQuadToBuffer(m, buffer, pos, size, r, g, b, a);

        BtsRenderSystem.drawWithGlobalShader(buffer.end());
    }

    private static void drawFillRect(final DrawContext guiGraphics, final int x, final int y, final int width, 
                                     final int height, final SimpleColor color) {
        guiGraphics.fill(x, y, x + width, y + height, rgbaToInt(color.getColorsI()));
    }

    public static void drawTriangle(final Matrix4f m, 
                                    final net.minecraft.client.util.math.Vector2f pos,
                                    final net.minecraft.client.util.math.Vector2f size, 
                                    final DrawDirection direction, final SimpleColor color) {
        drawTriangle(m, new Vector2f(pos.getX(), pos.getY()), new Vector2f(size.getX(), size.getY()), direction, color);
    }

    public static void drawTriangle(Matrix4f m, Vector2f pos, Vector2f size, DrawDirection direction, SimpleColor color) {
        final int r = color.getRedI();
        final int g = color.getGreenI();
        final int b = color.getBlueI();
        final int a = color.getAlphaI();
        drawTriangle(m, pos, size, direction, r, g, b, a);

    }

    public static void drawTriangle(final Matrix4f m, 
                                    final net.minecraft.client.util.math.Vector2f pos, 
                                    final net.minecraft.client.util.math.Vector2f size, 
                                    final DrawDirection direction, 
                                    final int r, final int g, final int b, final int a) {
        drawTriangle(m, new Vector2f(pos.getX(), pos.getY()), new Vector2f(size.getX(), size.getY()), direction, r, g, b, a);
    }

    public static void drawTriangle(Matrix4f m, Vector2f pos, Vector2f size, DrawDirection direction, int r, int g, int b, int a) {
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

        RenderSystem.enableBlend();

        RenderBuffer2D.addTriangleToBuffer(m, buffer, pos, size, direction, r, g, b, a);

        BtsRenderSystem.drawWithGlobalShader(buffer.end());

        RenderSystem.disableBlend();
    }

    public static void drawCircle(final Matrix4f m, final net.minecraft.client.util.math.Vector2f pos, 
                                  final float size, final int segments, final SimpleColor color) {
        drawCircle(m, new Vector2f(pos.getX(), pos.getY()), size, segments, color);
    }

    public static void drawCircle(final Matrix4f m, final Vector2f pos, final float size, final int segments, final SimpleColor color) {
        final int r = color.getRedI();
        final int g = color.getGreenI();
        final int b = color.getBlueI();
        final int a = color.getAlphaI();
        drawCircle(m, pos, size, segments, r, g, b, a);
    }

    public static void drawCircle(final Matrix4f m, final Vector2f pos, final float size, final int segments, 
                                  final int r, final int g, final int b, final int a) {
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);


        RenderSystem.enableBlend();

        RenderBuffer2D.addCircleToBuffer(m, buffer, pos, size, segments, r, g, b, a);

        BtsRenderSystem.drawWithGlobalShader(buffer.end());

        RenderSystem.disableBlend();
    }

    public static void drawArc(final DrawContext guiGraphics, final Vector2f pos, 
                               final int radius, final int startAngle, final int endAngle, final SimpleColor color) {
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        RenderSystem.enableBlend();

        RenderBuffer2D.addArcToBuffer(guiGraphics.getMatrices().peek().getPositionMatrix(), bufferBuilder, (int) pos.x, (int) pos.y, radius, startAngle, endAngle, color);

        BtsRenderSystem.drawWithGlobalShader(bufferBuilder.end());

        RenderSystem.disableBlend();
    }

    public static void drawRoundedRect(final DrawContext guiGraphics, final int x, final int y, final int width, 
                                       final int height, final int radius, final SimpleColor color) {
        drawFillRect(guiGraphics, x, y + radius, width, height - radius * 2, color); // Верхняя и нижняя части
        drawFillRect(guiGraphics, x + radius, y, width - radius * 2, radius, color); // Левая и правая части
        drawFillRect(guiGraphics, x + radius, y + height - radius, width - radius * 2, radius, color); // Левая и правая части

        // Отрисовка закругленных углов (дуги)
        drawArc(guiGraphics, new Vector2f(x + radius, y + radius), radius, 270, 180, color); // Левый верхний угол
        drawArc(guiGraphics, new Vector2f(x + width - radius, y + radius), radius, 0, -90, color); // Правый верхний угол
        drawArc(guiGraphics, new Vector2f(x + radius, y + height - radius), radius, -180, -270, color); // Левый нижний угол
        drawArc(guiGraphics, new Vector2f(x + width - radius, y + height - radius), radius, 90, 0, color); // Правый нижний угол
    }

    public static void drawRoundedRect(final DrawContext guiGraphics, final int x, final int y,
                                       final int width, final int height,
                                       final int radius, final SimpleColor color, final DrawDirection direction) {
        switch (direction) {
            case UP -> {
                drawFillRect(guiGraphics, x, y + radius, width, height - radius, color);
                drawFillRect(guiGraphics, x + radius, y, width - radius * 2, radius, color);

                // Отрисовка закругленных углов (дуги)
                drawArc(guiGraphics, new Vector2f(x + radius,   y + radius), radius, 270, 180, color); // Левый верхний угол
                drawArc(guiGraphics, new Vector2f(x + width - radius, y + radius), radius, 0, -90, color); // Правый верхний угол
                return;
            }
            case DOWN -> {
                drawFillRect(guiGraphics, x, y, width, height - radius, color);
                drawFillRect(guiGraphics, x + radius, y + height - radius, width - radius * 2, radius, color);

                // Отрисовка закругленных углов (дуги)
                drawArc(guiGraphics, new Vector2f(x + radius, y + height - radius), radius, -180, -270, color); // Левый нижний угол
                drawArc(guiGraphics, new Vector2f(x + width - radius, y + height - radius), radius, 90, 0, color); // Правый нижний угол
                return;
            }
        }

        throw new UnsupportedOperationException("Not implemented yet for this operation '" + direction.name() + "'");
    }


    public static int rgbaToInt(final int r, final int g, final int b, final int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    
    public static int rgbaToInt(final int[] color) {
        return (color[3] << 24) | (color[0] << 16) | (color[1] << 8) | color[2];
    }
}