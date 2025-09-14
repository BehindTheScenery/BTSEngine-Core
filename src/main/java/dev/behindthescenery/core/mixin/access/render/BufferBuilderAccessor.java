package dev.behindthescenery.core.mixin.access.render;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.BufferAllocator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BufferBuilder.class)
public interface BufferBuilderAccessor {

    @Accessor
    BufferAllocator getAllocator();
}
