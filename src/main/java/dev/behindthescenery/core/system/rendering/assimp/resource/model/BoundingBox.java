package dev.behindthescenery.core.system.rendering.assimp.resource.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.core.utils.ExternCodecs;
import net.minecraft.util.math.Box;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIVector3D;

public class BoundingBox {

    public static final Codec<BoundingBox> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExternCodecs.VECTOR_3_F_CODEC.fieldOf("min").forGetter(s -> s.min),
                    ExternCodecs.VECTOR_3_F_CODEC.fieldOf("max").forGetter(s -> s.max)
            ).apply(instance, BoundingBox::new));

    public Vector3f min;
    public Vector3f max;

    public BoundingBox(Box box) {
        this.min = new Vector3f((float) box.minX, (float) box.minY, (float) box.minZ);
        this.max = new Vector3f((float) box.maxX, (float) box.maxY, (float) box.maxZ);
    }

    public BoundingBox() {
        min = new Vector3f(-1, -1, -1);
        max = new Vector3f(1, 1, 1);
    }

    protected BoundingBox(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
    }

    public BoundingBox(float x, float y, float z, float x1, float y1, float z1) {
        this.min = new Vector3f(x,y,z);
        this.max = new Vector3f(x1, y1, z1);
    }

    public void update(AIVector3D vertex) {
        min.x = Math.min(min.x, vertex.x());
        min.y = Math.min(min.y, vertex.y());
        min.z = Math.min(min.z, vertex.z());
        max.x = Math.max(max.x, vertex.x());
        max.y = Math.max(max.y, vertex.y());
        max.z = Math.max(max.z, vertex.z());
    }

    public Vector3f getCenter() {
        return new Vector3f(
                (min.x + max.x) / 2.0f,
                (min.y + max.y) / 2.0f,
                (min.z + max.z) / 2.0f
        );
    }

    public Vector3f getSize() {
        return new Vector3f(
                max.x - min.x,
                max.y - min.y,
                max.z - min.z
        );
    }

    public Box toMinecraftBox() {
        return new Box(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public BoundingBox move(Vector3f position) {
        min = min.add(position);
        max = max.add(position);
        return this;
    }

    public BoundingBox scale(Vector3f scale) {
        if (scale.x <= 0 || scale.y <= 0 || scale.z <= 0) {
            min.set(-1, -1, -1);
            max.set(1, 1, 1);
            return this;
        }

        Vector3f center = getCenter();
        Vector3f halfSize = getSize().mul(0.5f);

        halfSize.mul(scale);

        min.set(center).sub(halfSize);
        max.set(center).add(halfSize);

        return this;
    }

    public BoundingBox scale(float factor) {
        if (factor <= 0) {
            min.set(-1, -1, -1);
            max.set(1, 1, 1);
            return this;
        }

        Vector3f center = getCenter();
        Vector3f halfSize = getSize().mul(0.5f * factor);  // Inline mul и /2

        min.set(center).sub(halfSize);
        max.set(center).add(halfSize);
        return this;
    }

    @Override
    public String toString() {
        return "BoundingBox{min=" + min + ", max=" + max + "}";
    }
}
