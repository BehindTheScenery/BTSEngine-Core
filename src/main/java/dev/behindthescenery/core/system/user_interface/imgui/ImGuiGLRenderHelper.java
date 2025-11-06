package dev.behindthescenery.core.system.user_interface.imgui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * Методы для интеграции Minecraft Render в контексте ImGui
 */
public class ImGuiGLRenderHelper {

    public static void renderItemStack(ItemStack itemStack, MatrixStack stack, float x, float y, float width, float height, float scale, float rotation) {
        float xOffset = x + width / 2;
        float yOffset = y + height / 2;
        stack.translate(xOffset, yOffset, 0f);
        float newScale = Math.min(width, height) * 0.95f * scale;
        stack.scale(newScale, -newScale, newScale);
        stack.multiply(new Quaternionf().rotateZ(rotation * MathHelper.RADIANS_PER_DEGREE));

        VertexConsumerProvider.Immediate src = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(itemStack, MinecraftClient.getInstance().world, null, 0);
        boolean flat = !model.isSideLit();

        if (flat) DiffuseLighting.disableGuiDepthLighting();
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                itemStack, ModelTransformationMode.GUI, false, stack, src, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, model
        );

        src.draw();
        if (flat) DiffuseLighting.enableGuiDepthLighting();
    }

    public static void renderItemDecorations(ItemStack stack, MatrixStack poseStack, int x, int y, float width, float height) {
        if (stack.isItemBarVisible()) {
            float i = stack.getItemBarStep() / 16f;
            int j = stack.getItemBarColor();
            float k = (x + width * 0.125f);
            float l = (y + height * 0.8125f);
            fill(
                    poseStack,
                    RenderLayer.getGuiOverlay(),
                    (int) k,
                    (int) l,
                    (int) (k + width * 0.8125f),
                    (int) (l + height * 0.125f),
                    0,
                    -16777216
            );
            fill(
                    poseStack, RenderLayer.getGuiOverlay(), (int) k, (int) l, (int) (k + i * width),
                    (int) (l + height * 0.0625f), 10, ColorHelper.Abgr.toOpaque(j)
            );
        }
        float f = MinecraftClient.getInstance().player.getItemCooldownManager().getCooldownProgress(
                stack.getItem(), MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true)
        );

        if (f > 0) {
            float k = y + width * (MathHelper.floor(16.0f * (1.0f - f)) / 16f);
            float l = k + height * MathHelper.ceil(16.0f * f) / 16f;
            fill(poseStack, RenderLayer.getGuiOverlay(), x, (int) k, (int) (x + width), (int) l, 0, Integer.MAX_VALUE);
        }
    }

    public static void fill(MatrixStack stack, RenderLayer renderType, int minX, int minY, int maxX, int maxY, int z, int color) {

        int i;
        Matrix4f matrix4f = stack.peek().getPositionMatrix();
        if (minX < maxX) {
            i = minX;
            minX = maxX;
            maxX = i;
        }
        if (minY < maxY) {
            i = minY;
            minY = maxY;
            maxY = i;
        }
        VertexConsumerProvider.Immediate src = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer vertexConsumer = src.getBuffer(renderType);
        vertexConsumer.vertex(matrix4f, minX, minY, z).color(color);
        vertexConsumer.vertex(matrix4f, minX, maxY, z).color(color);
        vertexConsumer.vertex(matrix4f, maxX, maxY, z).color(color);
        vertexConsumer.vertex(matrix4f, maxX, minY, z).color(color);
        RenderSystem.disableDepthTest();
        src.draw();
        RenderSystem.enableDepthTest();
    }
}
