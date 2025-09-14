package dev.behindthescenery.core.utils;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import sun.misc.Unsafe;

@SuppressWarnings("unused")
public class MathUtils {

//    private static final Unsafe UNSAFE = Unsafe.getUnsafe();
//
//    private static final long DOUBLE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(double[].class);

    private static final float[] SIN = Util.make(new float[65536], (p_14077_) -> {
        for(int i = 0; i < p_14077_.length; ++i) {
            p_14077_[i] = (float)Math.sin((double)i * Math.PI * 2.0D / 65536.0D);
        }
    });

    private static final int[] SINE_TABLE_INT = new int[16384 + 1];
    private static final float SINE_TABLE_MIDPOINT;

    static {
        for (int i = 0; i < SINE_TABLE_INT.length; i++) {
            SINE_TABLE_INT[i] = Float.floatToRawIntBits(SIN[i]);
        }

        SINE_TABLE_MIDPOINT = SIN[SIN.length / 2];

        for (int i = 0; i < SIN.length; i++) {
            float expected = SIN[i];
            float value = lookup(i);

            if (expected != value) {
                throw new IllegalArgumentException(String.format("LUT error at index %d (expected: %s, found: %s)", i, expected, value));
            }
        }
    }

    public static Matrix4f lerpMatrix(Matrix4f matrix, Matrix4f other, float alpha) {
        return lerpMatrix(matrix, other, alpha, new Matrix4f());
    }

    public static Matrix4f lerpMatrix(Matrix4f matrix, Matrix4f other, float alpha, Matrix4f dist) {
        Vector3f translation = new Vector3f();
        Quaternionf rotation = new Quaternionf();
        Vector3f scale = new Vector3f();

        Vector3f otherTranslation = new Vector3f();
        Quaternionf otherRotation = new Quaternionf();
        Vector3f otherScale = new Vector3f();

        matrix.getTranslation(translation);
        matrix.getUnnormalizedRotation(rotation);
        matrix.getScale(scale);

        other.getTranslation(otherTranslation);
        other.getUnnormalizedRotation(otherRotation);
        other.getScale(otherScale);

        translation.lerp(otherTranslation, alpha);
        rotation.slerp(otherRotation, alpha);
        scale.lerp(otherScale, alpha);

        return dist.translationRotateScale(translation, rotation, scale);
    }

    public static Vector3f rotateVector(Vector3f target, Vector3f rotation) {
        return target.rotateZ(rotation.z).rotateY(rotation.y).rotateX(rotation.x);
    }

    public static MatrixStack rotateVector(MatrixStack stack, Vector3f rotation) {
        Quaternionf quaternionf = new Quaternionf().rotateZYX(rotation.z, rotation.y, rotation.x);
        stack.multiply(quaternionf);
        return stack;
    }

    public static Vector3f rotateAroundOrigin(Vector3f target, Vector3f origin, Vector3f rotation) {
        target.add(origin);
        rotateVector(target, rotation);
        return target.sub(origin);
    }

    public static MatrixStack rotateAroundOrigin(MatrixStack stack, Vector3f origin, Vector3f rotation) {
        stack.translate(origin.x, origin.y, origin.z);
        rotateVector(stack, rotation);
        stack.translate(-origin.x, -origin.y, -origin.z);
        return stack;
    }

    public static float sin(float f) {
        return lookup((int) (f * 10430.378f) & 0xFFFF);
    }

    public static float cos(float f) {
        return lookup((int) (f * 10430.378f + 16384.0f) & 0xFFFF);
    }

    private static float lookup(int index) {
        if (index == 32768) {
            return SINE_TABLE_MIDPOINT;
        }
        int neg = (index & 0x8000) << 16;
        int mask = (index << 17) >> 31;
        int pos = (0x8001 & mask) + (index ^ mask);
        pos &= 0x7fff;
        return Float.intBitsToFloat(SINE_TABLE_INT[pos] ^ neg);
    }

//    public static double abs(double a) {
//        double[] temp = new double[]{a};
//        long bits = UNSAFE.getLong(temp, DOUBLE_ARRAY_OFFSET);
//        bits &= 0x7FFFFFFFFFFFFFFFL;
//        UNSAFE.putLong(temp, DOUBLE_ARRAY_OFFSET, bits);
//        return temp[0];
//    }
}
