package dev.behindthescenery.core.system.rendering.model_render.model_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

public record MeshVertex(float positionX, float positionY, float positionZ,
                         float normalX, float normalY, float normalZ,
                         float textureX, float textureY) {

    public static final byte BYTES = 32;

    public MeshVertex(Vector3f position, Vector3f normal, Vector2f uv) {
        this(position.x, position.y, position.z, normal.x, normal.y, normal.z, uv.x, uv.y);
    }

    public static final Codec<MeshVertex> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("pX").forGetter(MeshVertex::positionX),
                    Codec.FLOAT.fieldOf("pY").forGetter(MeshVertex::positionY),
                    Codec.FLOAT.fieldOf("pZ").forGetter(MeshVertex::positionZ),
                    Codec.FLOAT.fieldOf("nX").forGetter(MeshVertex::normalX),
                    Codec.FLOAT.fieldOf("nY").forGetter(MeshVertex::normalY),
                    Codec.FLOAT.fieldOf("nZ").forGetter(MeshVertex::normalZ),
                    Codec.FLOAT.fieldOf("tX").forGetter(MeshVertex::textureX),
                    Codec.FLOAT.fieldOf("tY").forGetter(MeshVertex::textureY)
            ).apply(builder, MeshVertex::new)
    );

    public MeshVertex copy() {
        return new MeshVertex(positionX, positionY, positionZ, normalX, normalY, normalZ, textureX, textureY);
    }

    @Override
    public @NotNull String toString() {
        return "{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", positionZ=" + positionZ +
                ", normalX=" + normalX +
                ", normalY=" + normalY +
                ", normalZ=" + normalZ +
                ", textureX=" + textureX +
                ", textureY=" + textureY +
                '}';
    }

    public MeshVertex push(FloatBuffer buffer) {
        return pushPosition(buffer).pushNormal(buffer).pushUv(buffer);
    }

    public MeshVertex pushPosition(FloatBuffer buffer) {
        buffer.put(positionX).put(positionY).put(positionZ);
        return this;
    }

    public MeshVertex pushNormal(FloatBuffer buffer) {
        buffer.put(normalX).put(normalY).put(normalZ);
        return this;
    }

    public MeshVertex pushUv(FloatBuffer buffer) {
        buffer.put(textureX).put(textureY);
        return this;
    }
}
