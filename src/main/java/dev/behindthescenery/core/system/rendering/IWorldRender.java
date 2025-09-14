package dev.behindthescenery.core.system.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IWorldRender {

    default void renderInWorld(MatrixStack matrixStack) {
        renderInWorld(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers());
    }

    void renderInWorld(MatrixStack matrixStack, VertexConsumerProvider.Immediate consumerProvider);
}
