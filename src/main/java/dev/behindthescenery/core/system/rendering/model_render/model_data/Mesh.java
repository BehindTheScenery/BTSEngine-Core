package dev.behindthescenery.core.system.rendering.model_render.model_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.model_render.buffer.BasicMeshBuffer;
import dev.behindthescenery.core.system.rendering.model_render.buffer.GpuMeshBuffer;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Material;
import dev.behindthescenery.core.utils.CollectionsUtils;
import net.minecraft.client.render.VertexConsumer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class Mesh {

    public static final Codec<Mesh> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.INT.listOf().fieldOf("indices").forGetter(s -> CollectionsUtils.toList(s.indices)),
                    MeshVertex.CODEC.listOf().fieldOf("vertex").forGetter(s -> Arrays.asList(s.vertices)),
                    MeshBone.CODEC.listOf().fieldOf("bones").forGetter(s -> Arrays.asList(s.bones)),
                    Material.CODEC.fieldOf("material").forGetter(Mesh::material)
            ).apply(builder, Mesh::new));

    public static Mesh create(int[] indices, float[] vertices, float[] uvs,
                              float[] normals, int[] boneIndices, float[] boneWeights, Material material) {
        int count = vertices.length / 3;

        MeshVertex[] v1 = new MeshVertex[count];
        for (int i = 0; i < count; i++) {
            v1[i] = new MeshVertex(
                    new Vector3f(vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2]),
                    new Vector3f(normals[i * 3], normals[i * 3 + 1], normals[i * 3 + 2]),
                    new Vector2f(uvs[i * 2], uvs[i * 2 + 1])
            );
        }

//        MeshBone[] v2 = new MeshBone[count];
//        for (int i = 0; i < count; i++) {
//            v2[i] = new MeshBone(
//                    boneIndices[i * 4],
//                    boneIndices[i * 4 + 1],
//                    boneIndices[i * 4 + 2],
//                    boneIndices[i * 4 + 3],
//                    boneWeights[i * 4],
//                    boneWeights[i * 4 + 1],
//                    boneWeights[i * 4 + 2],
//                    boneWeights[i * 4 + 3]
//            );
//        }

        return new Mesh(indices, v1, new MeshBone[0], material);
    }

    private final int[] indices;
    private final MeshVertex[] vertices;
    private final MeshBone[] bones;
    private Material material;

    @OnlyIn(Dist.CLIENT)
    protected BasicMeshBuffer meshBuffer;

    public Mesh(int[] indices, MeshVertex[] vertices, Material material) {
        this(indices, vertices, new MeshBone[0], material);
    }

    public Mesh(int[] indices, MeshVertex[] vertices, MeshBone[] bones, Material material) {
        this(indices, vertices, bones, material, true);
    }

    public Mesh(int[] indices, MeshVertex[] vertices, MeshBone[] bones, Material material, boolean createRenderData) {
        if (indices.length == 0 || vertices.length == 0)
            throw new RuntimeException("Failed load mesh. [indices: " + indices.length + ", vertices: " + vertices.length + "]");
        this.indices = indices;
        this.vertices = vertices;
        this.bones = bones;
        this.material = material;

        if(createRenderData) {
            if (BtsRenderSystem.isOnRenderThread())
                meshBuffer = createMeshBuffer();
            else {
                BtsRenderSystem.recordRenderCall(() -> meshBuffer = createMeshBuffer());
            }
        }
    }

    protected BasicMeshBuffer createMeshBuffer() {
        return new GpuMeshBuffer(this);
    }

    public Mesh(List<Integer> integers, List<MeshVertex> meshVertices, List<MeshBone> meshBones, Material material) {
        this(CollectionsUtils.toArrayInt(integers),
                CollectionsUtils.toArrayObject(meshVertices, MeshVertex[]::new),
                CollectionsUtils.toArrayObject(meshBones, MeshBone[]::new),
                material
        );
    }

    public boolean hasBones() {
        return bones.length > 0;
    }

    public Mesh copy() {
        return copy(true);
    }

    public Mesh copy(boolean createBuffer) {
        return new Mesh(indices.clone(), vertices.clone(), bones.clone(), material, createBuffer);
    }

    @Override
    public @NotNull String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mesh[").append("\n");
        builder.append("Indices:").append(indices.length).append("\n");
        builder.append("Vertices[").append("\n");
        for (int i = 0; i < vertices.length; i++) {
            builder.append(vertices[i].toString()).append("\n");
        }
        builder.append("]\n").append("MeshBone[").append("\n");
        for (int i = 0; i < bones.length; i++) {
            builder.append(bones[i].toString()).append("\n");
        }
        builder.append("]\n]\n");
        return builder.toString();
    }

    public int[] indices() {
        return indices;
    }

    public MeshVertex[] vertices() {
        return vertices;
    }

    public MeshBone[] bones() {
        return bones;
    }

    public Material material() {
        return material;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Mesh) obj;
        return Objects.equals(this.indices, that.indices) &&
                Objects.equals(this.vertices, that.vertices) &&
                Objects.equals(this.bones, that.bones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indices, vertices, bones);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @OnlyIn(Dist.CLIENT)
    public BasicMeshBuffer getMeshBuffer() {
        return meshBuffer;
    }

    @OnlyIn(Dist.CLIENT)
    public void uploadToBuffer(VertexConsumer vertexConsumer) {
        uploadToBuffer(vertexConsumer, (s,s1) -> {});
    }

    @OnlyIn(Dist.CLIENT)
    public void uploadToBuffer(VertexConsumer vertexConsumer, BiConsumer<VertexConsumer, MeshVertex> consumer) {
        for (MeshVertex vertex : vertices) {
            vertexConsumer.vertex(vertex.positionX(), vertex.positionY(), vertex.positionZ());
            vertexConsumer.normal(vertex.normalX(), vertex.normalY(), vertex.normalZ());
            vertexConsumer.texture(vertex.textureX(), vertex.textureY());
            consumer.accept(vertexConsumer, vertex);
        }
    }
}
