package dev.behindthescenery.core.system.rendering.assimp.resource.buffers;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.BoundingBox;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

@OnlyIn(Dist.CLIENT)
public class BoundBoxBuffer {

    private final int bbVaoId;
    private final int bbVboId;
    private final int bbEboId;
    private final int bbIndexCount;

    public BoundBoxBuffer(BoundingBox box) {
        bbVaoId = glGenVertexArrays();
        glBindVertexArray(bbVaoId);

        bbVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bbVboId);

        float[] bbVertices = new float[]{
                box.min.x, box.min.y, box.min.z,
                box.max.x, box.min.y, box.min.z,
                box.max.x, box.max.y, box.min.z,
                box.min.x, box.max.y, box.min.z,
                box.min.x, box.min.y, box.max.z,
                box.max.x, box.min.y, box.max.z,
                box.max.x, box.max.y, box.max.z,
                box.min.x, box.max.y, box.max.z };


        FloatBuffer bbVertexBuffer = MemoryUtil.memAllocFloat(bbVertices.length);
        bbVertexBuffer.put(bbVertices).flip();
        glBufferData(GL_ARRAY_BUFFER, bbVertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        MemoryUtil.memFree(bbVertexBuffer);

        bbEboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bbEboId);

        int[] bbIndices = new int[]{
                0, 1, 1, 2, 2, 3, 3, 0, // Передняя грань
                4, 5, 5, 6, 6, 7, 7, 4, // Задняя грань
                0, 4, 1, 5, 2, 6, 3, 7  // Соединяющие рёбра
        };

        IntBuffer bbIndexBuffer = MemoryUtil.memAllocInt(bbIndices.length);
        bbIndexBuffer.put(bbIndices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bbIndexBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(bbIndexBuffer);
        bbIndexCount = bbIndices.length;

        glBindVertexArray(0);
    }

    public BoundBoxBuffer(Box box) {
        bbVaoId = glGenVertexArrays();
        glBindVertexArray(bbVaoId);

        bbVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bbVboId);

        float[] bbVertices = new float[]{
                (float) box.minX, (float) box.minY, (float) box.minZ,
                (float) box.maxX, (float) box.minY, (float) box.minZ,
                (float) box.maxX, (float) box.maxY, (float) box.minZ,
                (float) box.minX, (float) box.maxY, (float) box.minZ,
                (float) box.minX, (float) box.minY, (float) box.maxZ,
                (float) box.maxX, (float) box.minY, (float) box.maxZ,
                (float) box.maxX, (float) box.maxY, (float) box.maxZ,
                (float) box.minX, (float) box.maxY, (float) box.maxZ };



        FloatBuffer bbVertexBuffer = MemoryUtil.memAllocFloat(bbVertices.length);
        bbVertexBuffer.put(bbVertices).flip();
        glBufferData(GL_ARRAY_BUFFER, bbVertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        MemoryUtil.memFree(bbVertexBuffer);

        bbEboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bbEboId);

        int[] bbIndices = new int[]{
                0, 1, 1, 2, 2, 3, 3, 0, // Передняя грань
                4, 5, 5, 6, 6, 7, 7, 4, // Задняя грань
                0, 4, 1, 5, 2, 6, 3, 7  // Соединяющие рёбра
        };

        IntBuffer bbIndexBuffer = MemoryUtil.memAllocInt(bbIndices.length);
        bbIndexBuffer.put(bbIndices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bbIndexBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(bbIndexBuffer);
        bbIndexCount = bbIndices.length;

        glBindVertexArray(0);
    }

    public void draw(MatrixStack poseStack) {
        var shader = BtsRenderSystem.getGlobalShader();
        if(shader == null) {
            draw();
        } else {
            shader.apply();
            shader.setUniformValue("model", poseStack.peek().getPositionMatrix());
            draw();
            shader.stop();
        }
    }

    public void draw() {
        glBindVertexArray(bbVaoId);
        GlStateManager._drawElements(GL_TRIANGLES, bbIndexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDeleteVertexArrays(bbVaoId);
        glDeleteBuffers(bbVboId);
        glDeleteBuffers(bbEboId);
    }
}
