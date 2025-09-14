package dev.behindthescenery.core.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.debug.GameTestDebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {

    @Shadow private boolean showChunkBorder;

    @Shadow @Final public DebugRenderer.Renderer chunkBorderDebugRenderer;

    @Shadow @Final public GameTestDebugRenderer gameTestDebugRenderer;

    @Shadow @Final public DebugRenderer.Renderer collisionDebugRenderer;

    /**
     * @author Sixik
     * @reason
     */
    @Overwrite
    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        if (this.showChunkBorder && !MinecraftClient.getInstance().hasReducedDebugInfo()) {
            this.collisionDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
//            this.chunkBorderDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        }
        this.gameTestDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
    }
}
