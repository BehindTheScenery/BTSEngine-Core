package dev.behindthescenery.core.system.rendering.buffers;

import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import dev.behindthescenery.core.system.rendering.utils.DrawDirection;
import dev.behindthescenery.core.system.rendering.utils.QuadVector;
import dev.behindthescenery.core.system.rendering.utils.TriangleVector;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class RenderBuffer2D {
    
    /**
     * Добавляет квадрат в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param rgb Цвет фигуры
     */
    public static void addQuadToBuffer(final Matrix4f m, final VertexConsumer buffer,
                                       final net.minecraft.client.util.math.Vector2f pos,
                                       final net.minecraft.client.util.math.Vector2f size, SimpleColor rgb){
        addQuadToBuffer(m, buffer, new Vector2f(pos.getX(), pos.getY()), new Vector2f(size.getX(), size.getY()), rgb);
    }

    /**
     * Добавляет квадрат в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param color Цвет фигуры
     */
    public static void addQuadToBuffer(final Matrix4f m, final VertexConsumer buffer,
                                       final Vector2f pos, final Vector2f size, final SimpleColor color) {
        final int r = color.getRedI();
        final int g = color.getGreenI();
        final int b = color.getBlueI();
        final int a = color.getAlphaI();
        addQuadToBuffer(m, buffer, pos, size, r,g,b,a);
    }


    /**
     * Добавляет квадрат в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param r Красный Цвет
     * @param g Зелёный Цвет
     * @param b Синий Цвет
     * @param a Прозрачность
     */
    public static void addQuadToBuffer(final Matrix4f m, final VertexConsumer buffer,
                                       final Vector2f pos, final Vector2f size,
                                       final int r, final int g, final int b, final int a){
        final Vector2f pos1 = new Vector2f(pos.x, pos.y - size.y);
        final Vector2f pos2 = new Vector2f(pos.x, pos.y);
        final Vector2f pos3 = new Vector2f(pos.x + size.x, pos.y);
        final Vector2f pos4 = new Vector2f(pos.x + size.x, pos.y - size.y);

        buffer.vertex(m, pos1.x, pos1.y, 0.0F).color(r, g, b, a);
        buffer.vertex(m, pos2.x, pos2.y, 0.0F).color(r, g, b, a);
        buffer.vertex(m, pos3.x, pos3.y, 0.0F).color(r, g, b, a);
        buffer.vertex(m, pos4.x, pos4.y, 0.0F).color(r, g, b, a);
    }

    /**
     * Добавляет треугольник в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param rgb Цвет фигуры
     */
    public static void addTriangleToBuffer(final Matrix4f m, final VertexConsumer buffer,
                                           final net.minecraft.client.util.math.Vector2f pos,
                                           final net.minecraft.client.util.math.Vector2f size,
                                           final DrawDirection direction, final SimpleColor rgb){
        addTriangleToBuffer(m, buffer, new Vector2f(pos.getX(), pos.getY()), new Vector2f(size.getX(), size.getY()), direction, rgb);
    }

    /**
     * Добавляет треугольник в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param color Цвет фигуры
     */
    public static void addTriangleToBuffer(final Matrix4f m, final VertexConsumer buffer,
                                           final Vector2f pos, final Vector2f size,
                                           final DrawDirection direction, final SimpleColor color){
        final int r = color.getRedI();
        final int g = color.getGreenI();
        final int b = color.getBlueI();
        final int a = color.getAlphaI();
        addTriangleToBuffer(m, buffer, pos, size, direction, r,g,b,a);

    }

    /**
     * Добавляет треугольник в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param r Красный Цвет
     * @param g Зелёный Цвет
     * @param b Синий Цвет
     * @param a Прозрачность
     */
    public static void addTriangleToBuffer(final Matrix4f m, final VertexConsumer buffer,
                                           final net.minecraft.client.util.math.Vector2f pos,
                                           final net.minecraft.client.util.math.Vector2f size,
                                           final DrawDirection direction,
                                           final int r, final int g, final int b, final int a) {
        addTriangleToBuffer(m, buffer, new Vector2f(pos.getX(), pos.getY()), new Vector2f(size.getX(), size.getY()), direction, r, g, b, a);
    }


    /**
     * Добавляет треугольник в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param r Красный Цвет
     * @param g Зелёный Цвет
     * @param b Синий Цвет
     * @param a Прозрачность
     */
    public static void addTriangleToBuffer(final Matrix4f m, final VertexConsumer buffer, final Vector2f pos,
                                           final Vector2f size, final DrawDirection direction,
                                           final int r, final int g, final int b, final int a){
        TriangleVector triangle = null;

        switch (direction) {
            case UP -> triangle = TriangleVector.create(
                    new Vector2f(pos.x + (float) size.x / 2, pos.y - size.y),
                    new Vector2f(pos.x, pos.y),
                    new Vector2f(pos.x + size.x, pos.y)
            );
            case DOWN -> triangle = TriangleVector.create(
                    new Vector2f(pos.x, pos.y - size.y),
                    new Vector2f(pos.x + (float)size.x / 2, pos.y + size.y - size.y),
                    new Vector2f(pos.x + size.x, pos.y - size.y)
            );
            case LEFT -> triangle = TriangleVector.create(
                    new Vector2f(pos.x, pos.y - size.y / 2),
                    new Vector2f(pos.x + size.x, pos.y),
                    new Vector2f(pos.x + size.x, pos.y - size.y)
            );
            case RIGHT -> triangle = TriangleVector.create(
                    new Vector2f(pos.x, pos.y - size.y),
                    new Vector2f(pos.x, pos.y),
                    new Vector2f(pos.x + size.x, pos.y - size.y / 2)
            );
        }

        Vector2f pos1 = triangle.getPos1();
        Vector2f pos2 = triangle.getPos2();
        Vector2f pos3 = triangle.getPos3();

        buffer.vertex(m, pos1.x, pos1.y, 0.0F).color(r,g,b,a);
        buffer.vertex(m, pos2.x, pos2.y, 0.0F).color(r,g,b,a);
        buffer.vertex(m, pos3.x, pos3.y, 0.0F).color(r,g,b,a);
    }


    /**
     * Добавляет круг в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param segments Количество сегментов (углов)
     * @param rgb Цвет фигуры
     */
    public static void addCircleToBuffer(final Matrix4f m, final VertexConsumer buffer,
                                         final net.minecraft.client.util.math.Vector2f pos,
                                         final float size, final int segments, final SimpleColor rgb){
        addCircleToBuffer(m, buffer, new Vector2f(pos.getX(), pos.getY()), size, segments, rgb);
    }

     /**
     * Добавляет круг в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param segments Количество сегментов (углов)
     * @param color Цвет фигуры
     */
    public static void addCircleToBuffer(final Matrix4f m, final VertexConsumer buffer, final Vector2f pos, 
                                         final float size, final int segments, final SimpleColor color){
        final int r = color.getRedI();
        final int g = color.getGreenI();
        final int b = color.getBlueI();
        final int a = color.getAlphaI();
        addCircleToBuffer(m, buffer, pos, size, segments, r,g,b,a);
    }


    /**
     * Добавляет круг в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param pos Координаты для отрисовки
     * @param size Размер фигуры
     * @param segments Количество сегментов (углов)
     * @param r Красный Цвет
     * @param g Зелёный Цвет
     * @param b Синий Цвет
     * @param a Прозрачность
     */
    public static void addCircleToBuffer(final Matrix4f m, final VertexConsumer buffer, final Vector2f pos, 
                                         final float size, final int segments,
                                         final int r, final int g, final int b, final int a){
        buffer.vertex(m, pos.x, pos.y, 0).color(r, g, b, a);


        float angleStep = (float)(2 * Math.PI / segments);

        for (int i = segments; i >= 0; i--) {
            float angle = i * angleStep;
            float x = (float) (Math.cos(angle) * size) + pos.x;
            float y = (float) (Math.sin(angle) * size) + pos.y;
            buffer.vertex(m, x, y, 0).color(r, g, b, a);
        }

    }


    /**
     * Добавляет дугу в буффер
     * @param m Матрица рендера. Обычно это {@link MatrixStack.Entry#getPositionMatrix()}
     * @param buffer Буффер к которому будет добавлена фигура
     * @param cX Координаты для отрисовки
     * @param cY Координаты для отрисовки
     * @param radius Радиус дуги
     * @param startAngle Начальный угол
     * @param endAngle Конечный угол
     * @param color Цвет фигуры
     */
    public static void addArcToBuffer(final Matrix4f m, final VertexConsumer buffer, 
                                      final int cX, final int cY, final int radius,
                                      final int startAngle, final int endAngle, final SimpleColor color) {

        final int r = color.getRedI();
        final int g = color.getGreenI();
        final int b = color.getBlueI();
        final int a = color.getAlphaI();
        buffer.vertex(m, cX, cY, 0).color(r, g, b, a); // Центр дуги

        for (int i = startAngle; i >= endAngle; i -= 5) {
            final double angle = Math.toRadians(i);
            final float x = (float) (Math.cos(angle) * radius) + cX;
            final float y = (float) (Math.sin(angle) * radius) + cY;
            buffer.vertex(m, x, y, 0).color(r, g, b, a);
        }

    }


    public static void addRectToBufferWithUV(final Matrix4f m, final VertexConsumer buffer, final int x,
                                             final int y, final int w, final int h,
                                             final SimpleColor color, final float u0, final float v0,
                                             final float u1, final float v1) {
        if (w > 0 && h > 0) {
            final int r = color.getRedI();
            final int g = color.getGreenI();
            final int b = color.getBlueI();
            final int a = color.getAlphaI();
            buffer.vertex(m, (float)x, (float)(y + h), 0.0F).color(r, g, b, a).texture(u0, v1);
            buffer.vertex(m, (float)(x + w), (float)(y + h), 0.0F).color(r, g, b, a).texture(u1, v1);
            buffer.vertex(m, (float)(x + w), (float)y, 0.0F).color(r, g, b, a).texture(u1, v0);
            buffer.vertex(m, (float)x, (float)y, 0.0F).color(r, g, b, a).texture(u0, v0);
        }
    }
}
