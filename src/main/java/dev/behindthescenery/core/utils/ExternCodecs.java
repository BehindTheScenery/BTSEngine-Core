package dev.behindthescenery.core.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.BoundingBox;
import net.minecraft.util.math.Box;
import org.joml.*;

public class ExternCodecs {

    public static final Codec<Box> BOX_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.DOUBLE.fieldOf("minX").forGetter(s -> s.minX),
                    Codec.DOUBLE.fieldOf("minY").forGetter(s -> s.minY),
                    Codec.DOUBLE.fieldOf("minZ").forGetter(s -> s.minZ),
                    Codec.DOUBLE.fieldOf("maxX").forGetter(s -> s.maxX),
                    Codec.DOUBLE.fieldOf("maxY").forGetter(s -> s.maxY),
                    Codec.DOUBLE.fieldOf("maxZ").forGetter(s -> s.maxZ)
            ).apply(instance, Box::new));

    public static final Codec<Vector3f> VECTOR_3_F_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(s -> s.x),
                    Codec.FLOAT.fieldOf("y").forGetter(s -> s.y),
                    Codec.FLOAT.fieldOf("z").forGetter(s -> s.z)
            ).apply(instance, Vector3f::new));

    public static final Codec<Vector3d> VECTOR_3_D_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.DOUBLE.fieldOf("x").forGetter(s -> s.x),
                    Codec.DOUBLE.fieldOf("y").forGetter(s -> s.y),
                    Codec.DOUBLE.fieldOf("z").forGetter(s -> s.z)
            ).apply(instance, Vector3d::new));

    public static final Codec<Vector4f> VECTOR_4_F_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(s -> s.x),
                    Codec.FLOAT.fieldOf("y").forGetter(s -> s.y),
                    Codec.FLOAT.fieldOf("z").forGetter(s -> s.z),
                    Codec.FLOAT.fieldOf("w").forGetter(s -> s.w)
            ).apply(instance, Vector4f::new));

    public static final Codec<Vector4d> VECTOR_4_D_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.DOUBLE.fieldOf("x").forGetter(s -> s.x),
                    Codec.DOUBLE.fieldOf("y").forGetter(s -> s.y),
                    Codec.DOUBLE.fieldOf("z").forGetter(s -> s.z),
                    Codec.DOUBLE.fieldOf("w").forGetter(s -> s.w)
            ).apply(instance, Vector4d::new));

    public static final Codec<Matrix4f> MATRIX_4_F_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("m00").forGetter(Matrix4f::m00),
                    Codec.FLOAT.fieldOf("m01").forGetter(Matrix4f::m01),
                    Codec.FLOAT.fieldOf("m02").forGetter(Matrix4f::m02),
                    Codec.FLOAT.fieldOf("m03").forGetter(Matrix4f::m03),
                    Codec.FLOAT.fieldOf("m10").forGetter(Matrix4f::m10),
                    Codec.FLOAT.fieldOf("m11").forGetter(Matrix4f::m11),
                    Codec.FLOAT.fieldOf("m12").forGetter(Matrix4f::m12),
                    Codec.FLOAT.fieldOf("m13").forGetter(Matrix4f::m13),
                    Codec.FLOAT.fieldOf("m20").forGetter(Matrix4f::m20),
                    Codec.FLOAT.fieldOf("m21").forGetter(Matrix4f::m21),
                    Codec.FLOAT.fieldOf("m22").forGetter(Matrix4f::m22),
                    Codec.FLOAT.fieldOf("m23").forGetter(Matrix4f::m23),
                    Codec.FLOAT.fieldOf("m30").forGetter(Matrix4f::m30),
                    Codec.FLOAT.fieldOf("m31").forGetter(Matrix4f::m31),
                    Codec.FLOAT.fieldOf("m32").forGetter(Matrix4f::m32),
                    Codec.FLOAT.fieldOf("m33").forGetter(Matrix4f::m33)
            ).apply(instance, Matrix4f::new));

    public static final Codec<Matrix4d> MATRIX_4_D_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.DOUBLE.fieldOf("m00").forGetter(Matrix4d::m00),
                    Codec.DOUBLE.fieldOf("m01").forGetter(Matrix4d::m01),
                    Codec.DOUBLE.fieldOf("m02").forGetter(Matrix4d::m02),
                    Codec.DOUBLE.fieldOf("m03").forGetter(Matrix4d::m03),
                    Codec.DOUBLE.fieldOf("m10").forGetter(Matrix4d::m10),
                    Codec.DOUBLE.fieldOf("m11").forGetter(Matrix4d::m11),
                    Codec.DOUBLE.fieldOf("m12").forGetter(Matrix4d::m12),
                    Codec.DOUBLE.fieldOf("m13").forGetter(Matrix4d::m13),
                    Codec.DOUBLE.fieldOf("m20").forGetter(Matrix4d::m20),
                    Codec.DOUBLE.fieldOf("m21").forGetter(Matrix4d::m21),
                    Codec.DOUBLE.fieldOf("m22").forGetter(Matrix4d::m22),
                    Codec.DOUBLE.fieldOf("m23").forGetter(Matrix4d::m23),
                    Codec.DOUBLE.fieldOf("m30").forGetter(Matrix4d::m30),
                    Codec.DOUBLE.fieldOf("m31").forGetter(Matrix4d::m31),
                    Codec.DOUBLE.fieldOf("m32").forGetter(Matrix4d::m32),
                    Codec.DOUBLE.fieldOf("m33").forGetter(Matrix4d::m33)
            ).apply(instance, Matrix4d::new));
}
