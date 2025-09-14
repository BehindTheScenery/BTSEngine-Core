package dev.behindthescenery.core.system.rendering.model_render.optimization;

import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.BoundingBox;
import dev.behindthescenery.core.system.rendering.model_render.model_data.Mesh;
import dev.behindthescenery.core.system.rendering.model_render.model_data.MeshBone;
import dev.behindthescenery.core.system.rendering.model_render.model_data.MeshVertex;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.meshoptimizer.MeshOptimizer;

import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

public class BtsMeshOptimization {

    private static final int POSITION_STRIDE_BYTES = 12;

    public static Mesh optimizeModel(Mesh inputMesh, MeshOptimizationStruct struct) {
        Mesh copy = inputMesh.copy(false);

        final int numVertices = copy.vertices().length;
        final int[] indices = copy.indices().clone();

        if (struct.optimizeVertexCache) {
            IntBuffer indicesBuffer = createIndicesBuffer(indices);
            IntBuffer resultBuffer = MemoryUtil.memAllocInt(indicesBuffer.capacity());

            if (struct.optimizeVertexCacheFifo) {
                MeshOptimizer.meshopt_optimizeVertexCacheFifo(
                        resultBuffer, indicesBuffer, numVertices, struct.fifoCache);
            } else {
                MeshOptimizer.meshopt_optimizeVertexCache(
                        resultBuffer, indicesBuffer, numVertices);
            }

            resultBuffer.get(indices);
            MemoryUtil.memFree(resultBuffer);
            MemoryUtil.memFree(indicesBuffer);
        }

        if (struct.optimizeOverdraw) {
            IntBuffer inputIndicesBuffer = createIndicesBuffer(indices);
            IntBuffer resultIndicesBuffer = MemoryUtil.memAllocInt(inputIndicesBuffer.capacity());
            FloatBuffer vertexPositions = createPositionBuffer(copy);

            MeshOptimizer.meshopt_optimizeOverdraw(
                    resultIndicesBuffer, inputIndicesBuffer, vertexPositions,
                    numVertices, POSITION_STRIDE_BYTES, struct.optimizeOverdrawThreshold);

            resultIndicesBuffer.get(indices);

            MemoryUtil.memFree(resultIndicesBuffer);
            MemoryUtil.memFree(inputIndicesBuffer);
            MemoryUtil.memFree(vertexPositions);
        }

        if (struct.optimizeVertexFetch) {

            ByteBuffer resultBuffer = MemoryUtil.memAlloc(MeshVertex.BYTES * copy.vertices().length);
            ByteBuffer resultVerticesBuffer = MemoryUtil.memAlloc(MeshVertex.BYTES * copy.vertices().length);
            IntBuffer inputIndicesBuffer = createIndicesBuffer(indices);

            MeshOptimizer.meshopt_optimizeVertexFetch(resultBuffer, inputIndicesBuffer, resultVerticesBuffer, copy.vertices().length, MeshVertex.BYTES);

            inputIndicesBuffer.get(indices);

            MemoryUtil.memFree(resultBuffer);
            MemoryUtil.memFree(resultVerticesBuffer);
            MemoryUtil.memFree(inputIndicesBuffer);

        }

        return new Mesh(indices, copy.vertices().clone(), copy.bones().clone(), copy.material(), false);
    }

    public static BoundingBox calculateBoundBox(Mesh mesh) {
        BoundingBox boundingBox = new BoundingBox();
        for (int i = 0; i < mesh.vertices().length; i++) {
            final MeshVertex vertex = mesh.vertices()[i];
            Vector3f min = boundingBox.min;
            Vector3f max = boundingBox.max;

            min.x = Math.min(min.x, vertex.positionX());
            min.y = Math.min(min.y, vertex.positionY());
            min.z = Math.min(min.z, vertex.positionZ());
            max.x = Math.max(max.x, vertex.positionX());
            max.y = Math.max(max.y, vertex.positionY());
            max.z = Math.max(max.z, vertex.positionZ());
        }

        return boundingBox;
    }


    public static FloatBuffer createPositionBuffer(Mesh mesh) {
        final int numVertices = mesh.vertices().length;
        final FloatBuffer buffer = MemoryUtil.memAllocFloat(numVertices * 3);
        final MeshVertex[] vertices = mesh.vertices();
        for (int i = 0; i < numVertices; i++) {
            vertices[i].pushPosition(buffer);
        }
        return buffer.flip();
    }

    public static IntBuffer createIndicesBuffer(int[] indices) {
        final IntBuffer buffer = MemoryUtil.memAllocInt(indices.length);
        return buffer.put(indices).flip();
    }

    public static IntBuffer createIndicesArrayBuffer(Mesh mesh) {
        return createIndicesBuffer(mesh.indices());
    }

    public static FloatBuffer createVertexBuffer(Mesh mesh, boolean useBones) {
        final int numVertices = mesh.vertices().length;
        final int floatsPerVertex = 8 + (useBones ? 8 : 0);
        final FloatBuffer buffer = MemoryUtil.memAllocFloat(numVertices * floatsPerVertex);

        final MeshVertex[] vertices = mesh.vertices();
        for (int i = 0; i < numVertices; i++) {
            vertices[i].push(buffer);
        }

        if (useBones) {
            final MeshBone[] bones = mesh.bones();
            if (bones.length != numVertices) {
                throw new IllegalArgumentException("Bones must match vertices count");
            }
            for (int i = 0; i < numVertices; i++) {
                bones[i].push(buffer);
            }
        }

        return buffer.flip();
    }

    public static void writeToFile(Mesh mesh, Path path) {
        writeToFile(mesh.toString(), path);
    }

    private static void writeToFile(String value, Path path) {
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(value);
        } catch (Exception e) {
            BtsCore.LOGGER.error("Failed to write mesh to {}: {}", path, e.getMessage(), e);
        }
    }
}
