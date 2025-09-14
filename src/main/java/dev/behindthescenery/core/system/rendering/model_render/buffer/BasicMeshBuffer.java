package dev.behindthescenery.core.system.rendering.model_render.buffer;

import dev.behindthescenery.core.system.rendering.assimp.resource.model.WorldModel;
import dev.behindthescenery.core.system.rendering.shader.ShaderProgram;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BasicMeshBuffer {

    default void setupRenderWorldModel(MatrixStack poseStack, @NotNull WorldModel worldModel, @Nullable VertexConsumer vertexConsumer) {}

    void drawWithGlobalShader();

    void drawWithGlobalShader(MatrixStack poseStack);

    void draw();

    void draw(MatrixStack poseStack);

    void draw(MatrixStack poseStack, @Nullable ShaderProgram shaderProgram);

    default BufferRenderType getRenderType() {
        return BufferRenderType.BTS;
    }
}
