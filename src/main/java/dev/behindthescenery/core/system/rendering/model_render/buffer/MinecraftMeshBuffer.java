package dev.behindthescenery.core.system.rendering.model_render.buffer;

import dev.behindthescenery.core.system.rendering.assimp.resource.model.WorldModel;
import dev.behindthescenery.core.system.rendering.model_render.model_data.Mesh;
import dev.behindthescenery.core.system.rendering.model_render.model_data.MeshVertex;
import dev.behindthescenery.core.system.rendering.shader.ShaderProgram;
import dev.behindthescenery.core.utils.WorldRenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MinecraftMeshBuffer implements BasicMeshBuffer {

    protected Mesh mesh;

    public MinecraftMeshBuffer(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public void setupRenderWorldModel(MatrixStack poseStack, @Nullable WorldModel worldModel, VertexConsumer vertexConsumer) {
        var pos = worldModel.getPosition();
        var i = WorldRenderUtils.getLightColor(MinecraftClient.getInstance().world, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
        for (MeshVertex vertex : mesh.vertices()) {
            vertexConsumer
                    .vertex(poseStack.peek(), vertex.positionX(), vertex.positionY(), vertex.positionZ())
                    .color(1f, 1f, 1f, 1f)
                    .texture(vertex.textureX(), vertex.textureY())
                    .normal(vertex.normalX(), vertex.normalY(), vertex.normalZ())
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .light(i);
        }
    }

    @Override
    public void drawWithGlobalShader() {}

    @Override
    public void drawWithGlobalShader(MatrixStack poseStack) {}

    @Override
    public void draw() {}

    @Override
    public void draw(MatrixStack poseStack) {}

    @Override
    public void draw(MatrixStack poseStack, @Nullable ShaderProgram shaderProgram) {}

    @Override
    public BufferRenderType getRenderType() {
        return BufferRenderType.VANILLA;
    }
}
