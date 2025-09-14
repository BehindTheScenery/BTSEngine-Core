package dev.behindthescenery.core.mixin;

import com.google.common.collect.ImmutableList;
import dev.behindthescenery.core.system.rendering.managers.LevelRenderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.debug.CollisionDebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(CollisionDebugRenderer.class)
public class CollisionDebugRendererMixin {

    @Shadow private double lastUpdateTime;

    @Shadow @Final private MinecraftClient client;

    @Shadow private List<VoxelShape> collisions;

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        double d = (double) Util.getMeasuringTimeNano();
        if (d - this.lastUpdateTime > (double)1.0E8F) {
            this.lastUpdateTime = d;
            Entity entity = this.client.gameRenderer.getCamera().getFocusedEntity();
//            this.collisions = ImmutableList.copyOf(entity.getWorld().getCollisions(entity, entity.getBoundingBox().expand((double)6.0F)));
            this.collisions = LevelRenderManager.INSTANCE.getCollisions(null);
        }

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());

        for(VoxelShape voxelShape : this.collisions) {
            WorldRenderer.drawShapeOutline(matrices, vertexConsumer, voxelShape, -cameraX, -cameraY, -cameraZ, 1.0F, 1.0F, 1.0F, 1.0F, true);
        }

    }
}
