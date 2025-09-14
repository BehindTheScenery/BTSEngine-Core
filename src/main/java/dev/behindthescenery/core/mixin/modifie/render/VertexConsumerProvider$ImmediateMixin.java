package dev.behindthescenery.core.mixin.modifie.render;

import dev.behindthescenery.core.system.rendering.assimp.render.BTSAssimpRenderType;
import dev.behindthescenery.core.system.rendering.vertex.ParallelMeshBuilder;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VertexConsumerProvider.Immediate.class)
public class VertexConsumerProvider$ImmediateMixin {

    @Unique
    protected boolean isParallel = false;

    @Inject(method = "getBuffer", at = @At(value = "HEAD"))
    public void sdm$getBuffer(RenderLayer arg, CallbackInfoReturnable<VertexConsumer> cir) {
        isParallel = BTSAssimpRenderType.contains(arg);
    }

    @Redirect(method = "getBuffer", at = @At(value = "NEW", target = "(Lnet/minecraft/client/util/BufferAllocator;Lnet/minecraft/client/render/VertexFormat$DrawMode;Lnet/minecraft/client/render/VertexFormat;)Lnet/minecraft/client/render/BufferBuilder;"))
    public BufferBuilder sdm$getBuffer(BufferAllocator allocator, VertexFormat.DrawMode drawMode, VertexFormat format) {
        if(!isParallel) return new BufferBuilder(allocator, drawMode, format);

        return new ParallelMeshBuilder(allocator, drawMode, format);
    }
}
