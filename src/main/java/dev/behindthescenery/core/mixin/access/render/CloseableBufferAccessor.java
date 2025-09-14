package dev.behindthescenery.core.mixin.access.render;

import net.minecraft.client.util.BufferAllocator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BufferAllocator.CloseableBuffer.class)
public interface CloseableBufferAccessor {

    @Accessor
    int getOffset();

    @Accessor
    int getSize();

    @Accessor
    int getClearCount();

    @Accessor
    boolean getClosed();
}
