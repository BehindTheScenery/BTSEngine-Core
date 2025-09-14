package dev.behindthescenery.core.system.rendering.model_render.model_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4f;
import org.joml.Vector4i;

import java.nio.FloatBuffer;

public record MeshBone(int indicesX, int indicesY, int indicesZ, int indicesW,
                       float weightX, float weightY, float weightZ, float weightW) {

    public MeshBone(Vector4i indices, Vector4f weight) {
        this(indices.x, indices.y, indices.z, indices.w,
                weight.x, weight.y, weight.z, weight.w);
    }

    public static final Codec<MeshBone> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.INT.fieldOf("iX").forGetter(MeshBone::indicesX),
                    Codec.INT.fieldOf("iY").forGetter(MeshBone::indicesY),
                    Codec.INT.fieldOf("iZ").forGetter(MeshBone::indicesZ),
                    Codec.INT.fieldOf("iW").forGetter(MeshBone::indicesW),
                    Codec.FLOAT.fieldOf("wX").forGetter(MeshBone::weightX),
                    Codec.FLOAT.fieldOf("wY").forGetter(MeshBone::weightY),
                    Codec.FLOAT.fieldOf("wZ").forGetter(MeshBone::weightZ),
                    Codec.FLOAT.fieldOf("wW").forGetter(MeshBone::weightW)
            ).apply(builder, MeshBone::new)
    );

    public MeshBone copy() {
        return new MeshBone(indicesX, indicesY, indicesZ, indicesW, weightX, weightY, weightZ, weightW);
    }
    
    public MeshBone push(FloatBuffer buffer) {
        buffer.put(indicesX()).put(indicesY()).put(indicesZ()).put(indicesW());
        buffer.put(weightX()).put(weightY()).put(weightZ()).put(weightW());
        return this;
    }

    @Override
    public @NotNull String toString() {
        return "{" +
                "indicesX=" + indicesX +
                ", indicesY=" + indicesY +
                ", indicesZ=" + indicesZ +
                ", indicesW=" + indicesW +
                ", weightX=" + weightX +
                ", weightY=" + weightY +
                ", weightZ=" + weightZ +
                ", weightW=" + weightW +
                '}';
    }
}
