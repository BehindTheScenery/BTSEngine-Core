package dev.behindthescenery.core.system.rendering.model_render.buffer;

import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.model_render.model_data.Mesh;
import dev.behindthescenery.core.system.rendering.model_render.model_data.MeshBone;
import dev.behindthescenery.core.system.rendering.model_render.model_data.MeshVertex;
import dev.behindthescenery.core.system.rendering.model_render.optimization.BtsMeshOptimization;
import dev.behindthescenery.core.system.rendering.model_render.optimization.MeshOptimizationStruct;
import dev.behindthescenery.core.system.rendering.shader.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

@OnlyIn(Dist.CLIENT)
public class GpuMeshBuffer implements BasicMeshBuffer {

    protected int vao, vbo, ebo;
    protected int indexCount;
    protected int vertexCount;

    protected boolean hasBones;

    private String materialName;

    private static final int FLOAT_BYTES = 4;
    private static final int POS_SIZE = 3;
    private static final int UV_SIZE = 2;
    private static final int NORM_SIZE = 3;
    private static final int BONE_INDICES_SIZE = 4;
    private static final int BONE_WEIGHTS_SIZE = 4;

    private static final int STRIDE_WITHOUT_BONES = (POS_SIZE + UV_SIZE + NORM_SIZE) * FLOAT_BYTES;  // 32 байта
    private static final int STRIDE_WITH_BONES = (POS_SIZE + UV_SIZE + NORM_SIZE + BONE_INDICES_SIZE + BONE_WEIGHTS_SIZE) * FLOAT_BYTES;  // 64 байта

    private static final long POS_OFFSET = 0;
    private static final long UV_OFFSET = POS_SIZE * FLOAT_BYTES;  // 12
    private static final long NORM_OFFSET = (POS_SIZE + UV_SIZE) * FLOAT_BYTES;  // 20
    private static final long BONE_INDICES_OFFSET = (POS_SIZE + UV_SIZE + NORM_SIZE) * FLOAT_BYTES;  // 32
    private static final long BONE_WEIGHTS_OFFSET = (POS_SIZE + UV_SIZE + NORM_SIZE + BONE_INDICES_SIZE) * FLOAT_BYTES;  // 48

    public GpuMeshBuffer(Mesh mesh) {
        createRenderData(mesh);
    }


    protected void createRenderData(Mesh m) {
        Mesh mesh = BtsMeshOptimization.optimizeModel(m.copy(false), new MeshOptimizationStruct());  // Или optimized

        MeshVertex[] vertices = mesh.vertices();
        MeshBone[] bones = mesh.bones();

        hasBones = bones.length != 0;

        this.vertexCount = vertices.length;
        this.indexCount = mesh.indices().length;
        this.materialName = mesh.material().getMaterialName();
        this.hasBones = mesh.hasBones() && bones.length == vertexCount;

        if (vertexCount == 0 || indexCount == 0 || indexCount % 3 != 0) {
            BtsCore.LOGGER.error("Invalid mesh: vertices={}, indices={}", vertexCount, indexCount);
            return;
        }

        if (hasBones && bones.length != vertexCount) {
            BtsCore.LOGGER.warn("Bones count ({}) != vertices ({}), disabling bones", bones.length, vertexCount);
            hasBones = false;
        }

        final int floatsPerVertex = (POS_SIZE + UV_SIZE + NORM_SIZE) + (hasBones ? (BONE_INDICES_SIZE + BONE_WEIGHTS_SIZE) : 0);
        final int vboSize = vertexCount * floatsPerVertex;
        final long strideBytes = (long) floatsPerVertex * FLOAT_BYTES;

        FloatBuffer vertexBuffer = null;
        IntBuffer indexBuffer = null;
        try {
            vertexBuffer = MemoryUtil.memAllocFloat(vboSize);
            indexBuffer = MemoryUtil.memAllocInt(indexCount);

            for (int i = 0; i < vertexCount; i++) {
                MeshVertex vertex = vertices[i];
                vertex.pushPosition(vertexBuffer);  // pos 0-2
                vertex.pushUv(vertexBuffer);        // uv 3-4
                vertex.pushNormal(vertexBuffer);    // norm 5-7

                if (hasBones) {
                    MeshBone bone = bones[i];

                    vertexBuffer.put(bone.indicesX()).put(bone.indicesY())
                            .put(bone.indicesZ()).put(bone.indicesW());  // 8-11
                    bone.push(vertexBuffer);
                }
            }
            vertexBuffer.flip();

            indexBuffer.put(mesh.indices()).flip();

            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

            setupAttrib(0, POS_SIZE, GL_FLOAT, false, strideBytes, POS_OFFSET);     // Position
            setupAttrib(1, UV_SIZE, GL_FLOAT, false, strideBytes, UV_OFFSET);       // UV
            setupAttrib(2, NORM_SIZE, GL_FLOAT, false, strideBytes, NORM_OFFSET);   // Normal

            if (hasBones) {
                glEnableVertexAttribArray(3);
                glVertexAttribIPointer(3, BONE_INDICES_SIZE, GL_INT, (int) strideBytes, (long) BONE_INDICES_OFFSET);  // Int для indices
                setupAttrib(4, BONE_WEIGHTS_SIZE, GL_FLOAT, false, strideBytes, BONE_WEIGHTS_OFFSET);
            }

            ebo = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

            int error = glGetError();
            if (error != GL11.GL_NO_ERROR) {
                BtsCore.LOGGER.error("OpenGL error in createRenderData: 0x{}", Integer.toHexString(error));
            }

        } finally {
            if (vertexBuffer != null) MemoryUtil.memFree(vertexBuffer);
            if (indexBuffer != null) MemoryUtil.memFree(indexBuffer);
        }
    }

    private void setupAttrib(int index, int size, int type, boolean normalized, long stride, long offset) {
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, size, type, normalized, (int) stride, (long) offset);
    }

    public int getEbo() { return ebo; }
    public int getVao() { return vao; }
    public int getVbo() { return vbo; }
    public String getMaterialName() { return materialName; }
    public int getIndexCount() { return indexCount; }
    public int getVertexCount() { return vertexCount; }
    public boolean hasBones() { return hasBones; }

    @Override
    public void drawWithGlobalShader() {
        ShaderProgram shader = BtsRenderSystem.getGlobalShader();
        if (shader == null) {
            draw();
        } else {
            shader.apply();
            draw();
            shader.stop();
        }
    }

    @Override
    public void drawWithGlobalShader(MatrixStack poseStack) {
        drawWithGlobalShader();
    }

    @Override
    public void draw(MatrixStack poseStack) {
        this.draw(poseStack, BtsRenderSystem.getGlobalShader());
    }

    @Override
    public void draw(MatrixStack poseStack, @Nullable ShaderProgram shaderProgram) {
        if (shaderProgram == null) {
            draw();
        } else {
            shaderProgram.apply();
            shaderProgram.setUniformValue("model", poseStack.peek().getPositionMatrix());
            shaderProgram.setUniformValue("useInstancing", false);
            draw();
            shaderProgram.stop();
        }
    }

    @Override
    public void draw() {
        if (vao == 0 || indexCount == 0) {
            BtsCore.LOGGER.warn("Invalid MeshBuffer: vao={}, indices={}", vao, indexCount);
            return;
        }


        glBindVertexArray(vao);
        try {
            glDrawElements(GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0L);  // 0L для offset
        } finally {
            glBindVertexArray(0);
        }

//        int error = glGetError();
//        if (error != GL11.GL_NO_ERROR) {
//            BtsCore.LOGGER.error("OpenGL error in draw: 0x{}", Integer.toHexString(error));
//        }
    }

    public void cleanup() {
        if (vao > 0) glDeleteVertexArrays(vao);
        if (vbo > 0) glDeleteBuffers(vbo);
        if (ebo > 0) glDeleteBuffers(ebo);
        vao = vbo = ebo = 0;  // Nullify для safety
    }
}
