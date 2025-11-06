package dev.behindthescenery.core.system.rendering;

import com.mojang.blaze3d.systems.RenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.mixin.access.render.FrustumAccess;
import dev.behindthescenery.core.mixin.access.render.TessellatorAccessor;
import dev.behindthescenery.core.system.rendering.textures.TextureParams;
import dev.behindthescenery.core.system.rendering.vertex.BtsVertexConsumer;
import dev.behindthescenery.core.system.rendering.vertex.ParallelMeshBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL15;

import java.nio.IntBuffer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class BtsRenderSystem extends RenderSystem {

    @Nullable
    private static dev.behindthescenery.core.system.rendering.shader.ShaderProgram globalShader;
    private static TextureParams textureParams;

    public static <T extends dev.behindthescenery.core.system.rendering.shader.ShaderProgram> T setGlobalShader(@Nullable T globalShader) {
        if(!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                if(globalShader == BtsRenderSystem.globalShader)
                    return;

                BtsRenderSystem.globalShader = globalShader;
            });
        } else {
            if(globalShader != BtsRenderSystem.globalShader)
                BtsRenderSystem.globalShader = globalShader;
        }

        return globalShader;
    }

    public static void clearGlobalShader() {
        setGlobalShader(null);
    }

    public static @Nullable dev.behindthescenery.core.system.rendering.shader.ShaderProgram getGlobalShader() {
        return globalShader;
    }

    public static void setCreatingTextureParams(@Nullable Runnable runnable) {
        if(runnable == null) {
            exec(() -> textureParams = null);
        } else {
            exec(() -> textureParams = new TextureParams(runnable));
        }
    }

    public static void applyTextureParams() {
        if(textureParams != null)
            textureParams.apply();
    }

    public static Camera getCamera() {
        return MinecraftClient.getInstance().gameRenderer.getCamera();
    }

    public static void applyCameraOffset(MatrixStack matrixStack, Position pos) {
       final Position cameraPosition = getCamera().getPos();
       matrixStack.translate(
                pos.getX() - cameraPosition.getX(),
                pos.getY()- cameraPosition.getY(),
                pos.getZ()- cameraPosition.getZ());
    }

    public static void applyCameraOffset(MatrixStack matrixStack, Vector3f pos) {
       final Position cameraPosition = getCamera().getPos();
       matrixStack.translate(
                pos.x - cameraPosition.getX(),
                pos.y - cameraPosition.getY(),
                pos.z - cameraPosition.getZ());
    }

    public static void applyCameraRotation(MatrixStack matrixStack) {
        matrixStack.multiply(getCamera().getRotation());
    }

    public static Frustum getFrustum() {
       return MinecraftClient.getInstance().worldRenderer.getFrustum();
    }

    public static Vec3d getFrustumPosition() {
        final FrustumAccess access = getFrustumAccess();
        if(access == null) return Vec3d.ZERO;
        return new Vec3d(access.getX(), access.getY(), access.getZ());
    }

    @Nullable
    public static FrustumAccess getFrustumAccess() {
        if(MinecraftClient.getInstance().worldRenderer.getFrustum() == null) return null;

        return (FrustumAccess) MinecraftClient.getInstance().worldRenderer.getFrustum();
    }

    public static boolean isFrustum(Position position) {
        final Vector3f fPos = BtsRenderSystem.getFrustumPosition().toVector3f();
        final FrustumIntersection frustum = Objects.requireNonNull(BtsRenderSystem.getFrustumAccess()).getFrustumIntersection();

        return frustum.testPoint((float) (position.getX() - fPos.x), (float) (position.getY() - fPos.y), (float) (position.getZ() - fPos.z));
    }

    public static void exec(RenderCall renderCall) {
        if(!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(renderCall);
        } else renderCall.execute();
    }

    public static void drawWithShader(VertexFormat.DrawMode drawMode, ShaderProgram shaderProgram, RenderCall renderCall) {
        shaderProgram.initializeUniforms(drawMode, new Matrix4f(RenderSystem.getModelViewMatrix()), new Matrix4f(RenderSystem.getProjectionMatrix()), MinecraftClient.getInstance().getWindow());
        shaderProgram.bind();
        renderCall.execute();
        shaderProgram.unbind();
    }

    public static void drawWithShader(VertexFormat.DrawMode drawMode, ShaderProgram shaderProgram, RenderCall renderCall, Matrix4f view) {
        shaderProgram.initializeUniforms(drawMode, new Matrix4f(RenderSystem.getModelViewMatrix()), new Matrix4f(RenderSystem.getProjectionMatrix()), MinecraftClient.getInstance().getWindow());
        shaderProgram.bind();
        renderCall.execute();
        shaderProgram.unbind();
    }

    public static BufferAllocator getMainAllocator() {
        return ((TessellatorAccessor) Tessellator.getInstance()).getAllocator();
    }

    public static ParallelMeshBuilder createParallelBuilder(VertexFormat.DrawMode drawMode, VertexFormat format) {
        return new ParallelMeshBuilder(new BufferAllocator(786432), drawMode, format);
    }

    public static ParallelMeshBuilder createParallelBuilder(BufferAllocator allocator, VertexFormat.DrawMode drawMode, VertexFormat format) {
        return new ParallelMeshBuilder(allocator, drawMode, format);
    }

    public static void execParallelRender(VertexConsumer vertexConsumer, Consumer<VertexConsumer> consumer) {
//        consumer.accept(vertexConsumer);

        if(vertexConsumer instanceof BtsVertexConsumer<?> consumer1 && consumer1.getDelegate() instanceof ParallelMeshBuilder meshBuilder) {
            meshBuilder.add(consumer::accept);
        } else if (vertexConsumer instanceof ParallelMeshBuilder meshBuilder) {
            meshBuilder.add(consumer::accept);
        } else {
            consumer.accept(vertexConsumer);
        }

    }

    public static int decideCull(MatrixStack.Entry pose, MatrixStack matrixStack) {
        return decideCull(pose, matrixStack, 10.0f, 3.0f);
    }

    // Определяет, какие грани видны
    public static int decideCull(MatrixStack.Entry pose, MatrixStack matrixStack, float distanceCulling, float extraCulling) {
        final int extraCull = 0b1000000;
        int faces = 0b111111; // По умолчанию рендерим все грани (6 направлений)

        // Проверяем, рендерим ли в GUI или в мире
        float zTranslation = matrixStack.peek().getPositionMatrix().m32();
        if (zTranslation != 0) { // В GUI
            // Для GUI можно рендерить только определенные грани в зависимости от ориентации
            Matrix4f poseMatrix = pose.getPositionMatrix();
            if (poseMatrix.m20() == 0 && poseMatrix.m21() == 0) { // Без поворотов
                return 1 << Direction.SOUTH.ordinal(); // Рендерим только южную грань
            }
            // Дополнительная логика для блоков с поворотами (как в ItemRendererMixin)
            // Можно добавить проверки на основе трансформаций модели
            return faces;
        } else { // В мире
            if (zTranslation < -distanceCulling) { // Далеко от камеры
                faces &= ((1 << Direction.NORTH.ordinal()) | (1 << Direction.SOUTH.ordinal()));
            }
            if (zTranslation < -extraCulling) { // Добавить дополнительное отсечение
                faces |= extraCull;
            }
        }
        return faces;
    }

    // Находит ближайшее направление для нормали
    public static Direction getClosestDirection(Vector3f normal) {
        float maxDot = -Float.MAX_VALUE;
        Direction closestDirection = Direction.UP;

        for (Direction direction : Direction.values()) {
            Vector3f dirVec = new Vector3f(direction.getVector().getX(), direction.getVector().getY(), direction.getVector().getZ());
            float dot = normal.dot(dirVec);
            if (dot > maxDot) {
                maxDot = dot;
                closestDirection = direction;
            }
        }
        return closestDirection;
    }

    public static void drawWithGlobalShader(BuiltBuffer buffer) {
        drawWithShader(buffer, globalShader);
    }

    public static void drawWithShader(BuiltBuffer buffer, @Nullable dev.behindthescenery.core.system.rendering.shader.ShaderProgram shaderProgram) {
        if(!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> drawWithShaderInternal(buffer, shaderProgram));
        } else {
            drawWithShaderInternal(buffer, shaderProgram);
        }
    }

    private static void drawWithShaderInternal(BuiltBuffer buffer, @Nullable dev.behindthescenery.core.system.rendering.shader.ShaderProgram shaderProgram) {
        if(shaderProgram != null) {
            shaderProgram.apply();
            BufferRenderer.draw(buffer);
            shaderProgram.stop();
        } else {
            BufferRenderer.drawWithGlobalProgram(buffer);
        }
    }

    public static Vector3f minecraftVectorToJOML(Vec3d vec3d) {
        return new Vector3f((float) vec3d.x, (float) vec3d.y, (float) vec3d.z);
    }

    public static Vector2i getFrameBufferSize() {
        AtomicReference<Vector2i> pos = new AtomicReference<>(new Vector2i());

        if(isOnRenderThread()) {
            pos.set(getFrameBufferSizeSafe());
        } else {
            recordRenderCall(() -> pos.set(getFrameBufferSizeSafe()));
        }
        return pos.get();
    }

    private static Vector2i getFrameBufferSizeSafe() {
        final PointerBuffer pointerBuffer =  GLFW.glfwGetMonitors();



        if(pointerBuffer == null) {
            BtsCore.LOGGER.error("Can't get monitors data !");
            final Window window = MinecraftClient.getInstance().getWindow();

            return new Vector2i(window.getWidth(), window.getHeight());
        }

        Vector2i size = new Vector2i();
        for (long i = 0; i < pointerBuffer.get(); i++) {
            int[] w = new int[1];
            int[] h = new int[1];
            GLFW.glfwGetMonitorWorkarea(i, new int[1], new int[1], w, h);

            size = size.add(new Vector2i(w[0], h[0]));
        }

        return size;
    }

    public static int[] getTextureSize(int textureID) {
        GL15.glBindTexture(GL15.GL_TEXTURE_2D, textureID);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);

        GL15.glGetTexLevelParameteriv(GL15.GL_TEXTURE_2D, 0, GL15.GL_TEXTURE_WIDTH, width);
        GL15.glGetTexLevelParameteriv(GL15.GL_TEXTURE_2D, 0, GL15.GL_TEXTURE_HEIGHT, height);

        return new int[]{width.get(0), height.get(0)};
    }
}
