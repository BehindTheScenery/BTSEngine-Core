package dev.behindthescenery.core.system.rendering.utils;

import org.joml.Vector2d;

public class MathLib {

    public static final double DEG_TO_RAD = Math.PI / 180.0;
    public static final double RAD_TO_DEG = 180.0 / Math.PI;

    public static double toRadians(double degrees) {
        return degrees * DEG_TO_RAD;
    }

    public static double toDegrees(double radians) {
        return radians * RAD_TO_DEG;
    }

    public static double distance(Vector2d pos1, Vector2d pos2) {
        return distance(pos1.x, pos1.y, pos2.x, pos2.y);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static Vector2d rotatePoint(Vector2d pos, Vector2d originalPos, double angle) {
        return rotatePoint(pos.x, pos.y, originalPos.x, originalPos.y, angle);
    }

    public static Vector2d rotatePoint(double px, double py, double originX, double originY, double angle) {
        double radians = toRadians(angle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double translatedX = px - originX;
        double translatedY = py - originY;

        double rotatedX = translatedX * cos - translatedY * sin;
        double rotatedY = translatedX * sin + translatedY * cos;

        return new Vector2d(rotatedX + originX, rotatedY + originY);
    }

    public static boolean isPointInRect(Vector2d pos, Vector2d rectPos, double rectWidth, double rectHeight) {
        return isPointInRect(pos.x, pos.y, rectPos.x, rectPos.y, rectWidth, rectHeight);
    }

    public static boolean isPointInRectWithRotation(Vector2d p, Vector2d rectPos, Vector2d rectSize, float rotation) {
        return isPointInRectWithRotation(p.x, p.y, rectPos.x, rectPos.y, rectSize.x, rectSize.y, rotation);
    }

    public static boolean isPointInRect(double px, double py, double rectX, double rectY, double rectWidth, double rectHeight) {
        return px >= rectX && px <= rectX + rectWidth && py >= rectY && py <= rectY + rectHeight;
    }

    public static boolean isPointInRectWithRotation(double px, double py, double rectX, double rectY, double rectWidth, double rectHeight, float rotation) {
        if(rotation % 90 == 0)
            return px >=rectX && px < rectX + rectWidth && py >= rectY && py < rectY + rectHeight;

        double centerX = rectX + rectWidth / 2.0;
        double centerY = rectY + rectHeight / 2.0;

        double localX = px - centerX;
        double localY = py - centerY;

        double radians = Math.toRadians(-rotation);

        double rotatedX = localX * Math.cos(radians) - localY * Math.sin(radians);
        double rotatedY = localX * Math.sin(radians) + localY * Math.cos(radians);

        double widgetX = rotatedX + centerX;
        double widgetY = rotatedY + centerY;

        return widgetX >= rectX && widgetX < rectX + rectWidth && widgetY >= rectY && widgetY < rectY + rectHeight;
    }

    public static double angleBetweenPoints(Vector2d pos1, Vector2d pos2) {
        return angleBetweenPoints(pos1.x, pos1.y, pos2.x, pos2.y);
    }

    public static double angleBetweenPoints(double x1, double y1, double x2, double y2) {
        return toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }

    public static Vector2d reflectVector(Vector2d v, Vector2d n) {
        return reflectVector(v.x, v.y, n.x, n.y);
    }

    public static Vector2d reflectVector(double vx, double vy, double nx, double ny) {
        double dotProduct = vx * nx + vy * ny;
        double reflectedX = vx - 2 * dotProduct * nx;
        double reflectedY = vy - 2 * dotProduct * ny;
        return new Vector2d(reflectedX, reflectedY);
    }

    public static Vector2d normalizeVector(double vx, double vy) {
        double length = Math.sqrt(vx * vx + vy * vy);
        if (length == 0) {
            return new Vector2d(0,0);
        }

        return new Vector2d(vx / length, vy / length);
    }

    /**
     * Преобразует цвет из формата float4 (RGBA) в 32-битное целое число (U32).
     *
     * @param r Красный компонент (0.0 - 1.0)
     * @param g Зеленый компонент (0.0 - 1.0)
     * @param b Синий компонент (0.0 - 1.0)
     * @param a Альфа-канал (0.0 - 1.0)
     * @return Цвет в формате 32-битного целого числа (RGBA)
     */
    public static int colorConvertFloat4ToU32(float r, float g, float b, float a) {
        int r8 = (int) (r * 255.0f) & 0xFF;
        int g8 = (int) (g * 255.0f) & 0xFF;
        int b8 = (int) (b * 255.0f) & 0xFF;
        int a8 = (int) (a * 255.0f) & 0xFF;

        return (a8 << 24) | (b8 << 16) | (g8 << 8) | r8;
    }

    /**
     * Преобразует 32-битное целое число (U32) в цвет в формате float4 (RGBA).
     *
     * @param color Цвет в формате 32-битного целого числа (RGBA)
     * @return Массив из четырех float: [r, g, b, a]
     */
    public static float[] colorConvertU32ToFloat4(int color) {
        int a = (color >> 24) & 0xFF;
        int b = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int r = color & 0xFF;

        float rFloat = r / 255.0f;
        float gFloat = g / 255.0f;
        float bFloat = b / 255.0f;
        float aFloat = a / 255.0f;

        return new float[]{rFloat, gFloat, bFloat, aFloat};
    }

    public static boolean isAngleBetween(float a, float start, float end) {
        float twoPI = (float)(Math.PI*2);
        float normEnd = (end - start + twoPI) % twoPI;
        float normA   = (a   - start + twoPI) % twoPI;
        return normA <= normEnd;
    }

    public static float angleDiff(float a0, float a1, boolean ccw) {
        float twoPI = (float)(Math.PI*2);
        float diff = (a1 - a0 + twoPI) % twoPI;
        if (!ccw) diff -= twoPI;
        return diff;
    }
}
