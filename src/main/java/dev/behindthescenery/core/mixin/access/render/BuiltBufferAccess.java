package dev.behindthescenery.core.mixin.access.render;

import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.util.BufferAllocator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BuiltBuffer.class)
public interface BuiltBufferAccess {

    @Accessor
    BufferAllocator.CloseableBuffer getBuffer();

}
