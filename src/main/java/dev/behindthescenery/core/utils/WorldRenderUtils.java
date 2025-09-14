package dev.behindthescenery.core.utils;

import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class WorldRenderUtils {

    protected static RenderTickCounter TICK_COUNTER;

    public static int getLightColor(World level, BlockPos blockPos) {
        if (level != null) {
            return WorldRenderer.getLightmapCoordinates(level, blockPos);
        } else {
            return  0xF000F0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static Vector3f getSkyColor() {
        return BtsRenderSystem.minecraftVectorToJOML(
                MinecraftClient.getInstance()
                        .world
                        .getSkyColor(
                                BtsRenderSystem.getCamera().getPos(),
                                tickDelta()
                        )
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static Vector3f skyAngleToDirection() {
        return skyAngleToDirection(MinecraftClient.getInstance().world);
    }

    public static Vector3f skyAngleToDirection(World world) {
        final float skyAngle = getSkyAngle(world) * 2.0f * (float) Math.PI;
        final float sunX = MathHelper.cos(skyAngle);
        final float sunZ = MathHelper.sin(skyAngle);
        return new Vector3f(sunX, 0.0f, sunZ).normalize();
    }

    public static float getSkyAngle(World world) {
        return world.getSkyAngle(tickDelta());
    }

    public static float tickDelta() {
        return MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false);
    }
}
