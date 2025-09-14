package dev.behindthescenery.core.system.rendering.utils;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

public interface NbtSerializeble {

    NbtCompound serialize();

    void deserialize(NbtCompound nbt);

    default void serialize(NbtCompound nbt, String name, Vector3f var) {
        NbtCompound compound = new NbtCompound();
        compound.putFloat("x", var.x);
        compound.putFloat("y", var.y);
        compound.putFloat("z", var.z);
        nbt.put(name, nbt);
    }

    default void serialize(NbtCompound nbt, String name, Vector3d var) {
        NbtCompound compound = new NbtCompound();
        compound.putDouble("x", var.x);
        compound.putDouble("y", var.y);
        compound.putDouble("z", var.z);
        nbt.put(name, nbt);
    }

    default void serialize(NbtCompound nbt, String name, Vector4f var) {
        NbtCompound compound = new NbtCompound();
        compound.putFloat("x", var.x);
        compound.putFloat("y", var.y);
        compound.putFloat("z", var.z);
        compound.putFloat("w", var.w);
        nbt.put(name, nbt);
    }

    default void serialize(NbtCompound nbt, String name, Vector4d var) {
        NbtCompound compound = new NbtCompound();
        compound.putDouble("x", var.x);
        compound.putDouble("y", var.y);
        compound.putDouble("z", var.z);
        compound.putDouble("w", var.w);
        nbt.put(name, nbt);
    }

    default void serialize(NbtCompound nbt, String name, Matrix3f var) {
        NbtCompound compound = new NbtCompound();
        compound.putFloat("m00", var.m00);
        compound.putFloat("m01", var.m01);
        compound.putFloat("m02", var.m02);
        compound.putFloat("m10", var.m10);
        compound.putFloat("m11", var.m11);
        compound.putFloat("m12", var.m12);
        compound.putFloat("m20", var.m20);
        compound.putFloat("m21", var.m21);
        compound.putFloat("m22", var.m22);
        nbt.put(name, nbt);
    }

    default void serialize(NbtCompound nbt, String name, Matrix3d var) {
        NbtCompound compound = new NbtCompound();
        compound.putDouble("m00", var.m00);
        compound.putDouble("m01", var.m01);
        compound.putDouble("m02", var.m02);
        compound.putDouble("m10", var.m10);
        compound.putDouble("m11", var.m11);
        compound.putDouble("m12", var.m12);
        compound.putDouble("m20", var.m20);
        compound.putDouble("m21", var.m21);
        compound.putDouble("m22", var.m22);
        nbt.put(name, nbt);
    }

    default void serialize(NbtCompound nbt, String name, Matrix4f var) {
        NbtCompound compound = new NbtCompound();
        compound.putFloat("m00", var.m00());
        compound.putFloat("m01", var.m01());
        compound.putFloat("m02", var.m02());
        compound.putFloat("m03", var.m03());
        compound.putFloat("m10", var.m10());
        compound.putFloat("m11", var.m11());
        compound.putFloat("m12", var.m12());
        compound.putFloat("m13", var.m13());
        compound.putFloat("m20", var.m20());
        compound.putFloat("m21", var.m21());
        compound.putFloat("m22", var.m22());
        compound.putFloat("m23", var.m23());
        compound.putFloat("m30", var.m30());
        compound.putFloat("m31", var.m31());
        compound.putFloat("m32", var.m32());
        compound.putFloat("m33", var.m33());
        nbt.put(name, nbt);
    }

    default void serialize(NbtCompound nbt, String name, Matrix4d var) {
        NbtCompound compound = new NbtCompound();
        compound.putDouble("m00", var.m00());
        compound.putDouble("m01", var.m01());
        compound.putDouble("m02", var.m02());
        compound.putDouble("m03", var.m03());
        compound.putDouble("m10", var.m10());
        compound.putDouble("m11", var.m11());
        compound.putDouble("m12", var.m12());
        compound.putDouble("m13", var.m13());
        compound.putDouble("m20", var.m20());
        compound.putDouble("m21", var.m21());
        compound.putDouble("m22", var.m22());
        compound.putDouble("m23", var.m23());
        compound.putDouble("m30", var.m30());
        compound.putDouble("m31", var.m31());
        compound.putDouble("m32", var.m32());
        compound.putDouble("m33", var.m33());
        nbt.put(name, nbt);
    }

    /**
     * @return Return null if nbt didn't contains 'name'
     */
    @Nullable
    @SuppressWarnings("unchecked")
    default <OUT> OUT deserialize(NbtCompound nbt, String name, Type type) {
        if(!nbt.contains(name)) return null;

        NbtCompound compound = nbt.getCompound(name);

        switch (type) {
            case VECTOR_3F ->  {
                return (OUT) new Vector3f(compound.getFloat("x"), compound.getFloat("y"), compound.getFloat("z"));
            }
            case VECTOR_3D -> {
                return (OUT) new Vector3d(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z"));
            }
            case VECTOR_4F -> {
                return (OUT) new Vector4f(
                        compound.getFloat("x"),
                        compound.getFloat("y"),
                        compound.getFloat("z"),
                        compound.getFloat("W")
                );
            }
            case VECTOR_4D -> {
                return (OUT) new Vector4d(
                        compound.getDouble("x"),
                        compound.getDouble("y"),
                        compound.getDouble("z"),
                        compound.getDouble("W")
                );
            }
            case MATRIX_3F -> {
                return (OUT) new Matrix3f(
                        compound.getFloat("m00"),
                        compound.getFloat("m01"),
                        compound.getFloat("m02"),
                        compound.getFloat("m10"),
                        compound.getFloat("m11"),
                        compound.getFloat("m12"),
                        compound.getFloat("m20"),
                        compound.getFloat("m21"),
                        compound.getFloat("m22")
                );
            }
            case MATRIX_3D -> {
                return (OUT) new Matrix3d(
                        compound.getDouble("m00"),
                        compound.getDouble("m01"),
                        compound.getDouble("m02"),
                        compound.getDouble("m10"),
                        compound.getDouble("m11"),
                        compound.getDouble("m12"),
                        compound.getDouble("m20"),
                        compound.getDouble("m21"),
                        compound.getDouble("m22")
                );
            }
            case MATRIX_4F -> {
                return (OUT) new Matrix4f(
                        compound.getFloat("m00"),
                        compound.getFloat("m01"),
                        compound.getFloat("m02"),
                        compound.getFloat("m03"),
                        compound.getFloat("m10"),
                        compound.getFloat("m11"),
                        compound.getFloat("m12"),
                        compound.getFloat("m13"),
                        compound.getFloat("m20"),
                        compound.getFloat("m21"),
                        compound.getFloat("m22"),
                        compound.getFloat("m23"),
                        compound.getFloat("m30"),
                        compound.getFloat("m31"),
                        compound.getFloat("m32"),
                        compound.getFloat("m33")
                );
            }
            case MATRIX_4D -> {
                return (OUT) new Matrix4d(
                        compound.getDouble("m00"),
                        compound.getDouble("m01"),
                        compound.getDouble("m02"),
                        compound.getDouble("m03"),
                        compound.getDouble("m10"),
                        compound.getDouble("m11"),
                        compound.getDouble("m12"),
                        compound.getDouble("m13"),
                        compound.getDouble("m20"),
                        compound.getDouble("m21"),
                        compound.getDouble("m22"),
                        compound.getDouble("m23"),
                        compound.getDouble("m30"),
                        compound.getDouble("m31"),
                        compound.getDouble("m32"),
                        compound.getDouble("m33")
                );
            }
        }

        throw new RuntimeException("Try deserialize unsupported type!");
    }

    enum Type {
        VECTOR_3F,
        VECTOR_3D,
        VECTOR_4F,
        VECTOR_4D,
        MATRIX_3F,
        MATRIX_3D,
        MATRIX_4F,
        MATRIX_4D
    }
}
