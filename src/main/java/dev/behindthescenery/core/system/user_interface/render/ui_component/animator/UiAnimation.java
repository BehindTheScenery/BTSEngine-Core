package dev.behindthescenery.core.system.user_interface.render.ui_component.animator;

import dev.behindthescenery.core.system.rendering.color.RGB;
import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.rendering.utils.MathLib;
import org.joml.Vector2f;

public class UiAnimation<T> {
    public interface Getter<T> { T get(); }
    public interface Setter<T> { void apply(T value); }
    public interface Interpolator<T> { T interpolate(T start, T end, float t); }
    public interface Easing { float ease(float t); }

    private Getter<T> getter;
    private Setter<T> setter;
    private T startValue;
    private T endValue;
    private long durationMs;
    private Easing easing;
    private Interpolator<T> interpolator;

    private float elapsedMs = 0f;
    private boolean finished = false;

    public UiAnimation(Getter<T> getter, Setter<T> setter, T endValue, long durationMs,
                       Interpolator<T> interpolator, Easing easing) {
        this.getter = getter;
        this.setter = setter;
        this.startValue = this.getter.get();
        this.endValue = endValue;
        this.durationMs = durationMs;
        this.interpolator = interpolator;
        this.easing = easing;
    }

    public boolean isFinished() { return finished; }

    public void update(long deltaMs) {
        if (finished) return;
        elapsedMs += deltaMs;
        float time = Math.min(elapsedMs / durationMs, 1f);
        setter.apply(interpolator.interpolate(startValue, endValue, easing.ease(time)));

        if (time >= 1f) finished = true;
    }

    public void reset() {
        this.elapsedMs = 0;
        this.finished = false;
    }

    public void reverse() {
        T temp = startValue;
        startValue = endValue;
        endValue = temp;
        elapsedMs = 0;
        finished = false;
    }

    public static class Easings {
        public static final Easing LINEAR = t -> t;
        public static final Easing EASE_IN = t -> t * t;
        public static final Easing EASE_OUT = t -> 1 - (1 - t) * (1 - t);
        public static final Easing EASE_IN_OUT = t -> t < 0.5f
                ? 2 * t * t
                : 1 - (float)Math.pow(-2 * t + 2, 2) / 2;
    }

    public static class Interpolators {

        public static final Interpolator<Double> DOUBLE = (start, end, t) ->
                start + (end - start) * t;

        public static final Interpolator<Float> FLOAT = (start, end, t) ->
                start + (end - start) * t;

        public static final Interpolator<Integer> INT = (start, end, t) ->
                (int)(start + (end - start) * t);

        public static final Interpolator<Vector2f> VECTOR_2_F = ((start, end, t) ->
                new Vector2f(
                        start.x + (end.x - start.x) * t,
                        start.y + (end.y - start.y) * t
                )
        );

        public static final Interpolator<RGB> COLOR_RGB = (start, end, t) -> {
            int r = (int)(start.getRedI()   + (end.getRedI()   - start.getRedI()) * t);
            int g = (int)(start.getGreenI() + (end.getGreenI() - start.getGreenI()) * t);
            int b = (int)(start.getBlueI()  + (end.getBlueI()  - start.getBlueI()) * t);
            return RGB.of(r, g, b);
        };

        public static final Interpolator<RGBA> COLOR_RGBA = (start, end, t) -> {
            int r = (int)(start.getRedI()   + (end.getRedI()   - start.getRedI()) * t);
            int g = (int)(start.getGreenI() + (end.getGreenI() - start.getGreenI()) * t);
            int b = (int)(start.getBlueI()  + (end.getBlueI()  - start.getBlueI()) * t);
            int a = (int)(start.getAlphaI() + (end.getAlphaI() - start.getAlphaI()) * t);
            return RGBA.of(r, g, b, a);
        };

        public static final Interpolator<Vector2f> ARC = (start, end, t) -> {
            float startAngle = start.x;
            float endAngle = end.x;
            float radius = start.y;
            float angle = startAngle + (endAngle - startAngle) * t;

            float cx = 0;
            float cy = 0;

            float px = cx + (float) (Math.cos(angle) * radius);
            float py = cy + (float) (Math.sin(angle) * radius);

            return new Vector2f(px, py);
        };

        public static UiAnimation.Interpolator<Vector2f> arcBySagitta(final float sagittaPx) {
            return (start, end, t) -> {
                float sx = start.x, sy = start.y;
                float ex = end.x,   ey = end.y;

                float dx = ex - sx;
                float dy = ey - sy;
                float L  = (float)Math.hypot(dx, dy);
                if (L < 1e-6f || Math.abs(sagittaPx) < 1e-6f) {
                    // деградация в обычный линейный LERP
                    float x = sx + dx * t;
                    float y = sy + dy * t;
                    return new Vector2f(x, y);
                }

                float h = Math.abs(sagittaPx);             // высота дуги (>=0)
                float R = (L*L) / (8f*h) + h/2f;           // радиус окружности
                float mx = (sx + ex) * 0.5f;               // середина хорды
                float my = (sy + ey) * 0.5f;

                // единичная нормаль к хорде (перпендикуляр). для экранной СК с Y вниз — всё ок.
                float nx = -dy / L;
                float ny =  dx / L;

                float sign = Math.signum(sagittaPx);       // сторона выпуклости
                float c = R - h;                            // смещение центра от середины хорды вдоль нормали
                float cx = mx + nx * c * sign;
                float cy = my + ny * c * sign;

                // углы от центра к точкам
                float a0 = (float)Math.atan2(sy - cy, sx - cx);
                float a1 = (float)Math.atan2(ey - cy, ex - cx);

                // идём по МЕНЬШЕЙ дуге (без скачков угла)
                float twoPI = (float)(Math.PI * 2.0);
                float ccw = a1 - a0;
                ccw = (ccw % twoPI + twoPI) % twoPI;   // нормализация в [0, 2π)
                float delta = (ccw > Math.PI) ? (ccw - twoPI) : ccw; // теперь |delta| <= π

                float a = a0 + delta * t;

                float x = cx + (float)Math.cos(a) * R;
                float y = cy + (float)Math.sin(a) * R;
                return new Vector2f(x, y);
            };
        }

        public static UiAnimation.Interpolator<Vector2f> arcThrough(Vector2f via) {
            return (start, end, t) -> {
                float sx = start.x, sy = start.y;
                float ex = end.x,   ey = end.y;
                float vx = via.x,   vy = via.y;

                // Формулы центра окружности через 3 точки
                float d = 2 * (sx*(ey-vy) + ex*(vy-sy) + vx*(sy-ey));
                if (Math.abs(d) < 1e-6f) {
                    // точки почти на одной линии → fallback в прямую
                    return new Vector2f(sx + (ex - sx) * t, sy + (ey - sy) * t);
                }

                float ux = ((sx*sx+sy*sy)*(ey-vy) + (ex*ex+ey*ey)*(vy-sy) + (vx*vx+vy*vy)*(sy-ey)) / d;
                float uy = ((sx*sx+sy*sy)*(vx-ex) + (ex*ex+ey*ey)*(sx-vx) + (vx*vx+vy*vy)*(ex-sx)) / d;

                float dx = sx - ux, dy = sy - uy;
                float r = (float)Math.sqrt(dx*dx + dy*dy);

                float a0 = (float)Math.atan2(sy - uy, sx - ux);
                float a1 = (float)Math.atan2(ey - uy, ex - ux);

                // выясняем направление дуги (через какую сторону идти)
                float av = (float)Math.atan2(vy - uy, vx - ux);
                boolean viaBetween = MathLib.isAngleBetween(av, a0, a1);

                float delta = MathLib.angleDiff(a0, a1, viaBetween);

                float a = a0 + delta * t;
                float x = ux + (float)Math.cos(a) * r;
                float y = uy + (float)Math.sin(a) * r;
                return new Vector2f(x, y);
            };
        }
    }
}
