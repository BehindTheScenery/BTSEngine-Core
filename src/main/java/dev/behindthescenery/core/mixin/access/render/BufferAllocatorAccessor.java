package dev.behindthescenery.core.mixin.access.render;

import net.minecraft.client.util.BufferAllocator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BufferAllocator.class)
public interface BufferAllocatorAccessor {

    @Accessor
    long getPointer();

    @Accessor
    int getSize();

    @Accessor
    int getOffset();

    @Accessor
    int getPrevOffset();

    @Accessor
    int getRefCount();

    @Accessor
    int getClearCount();
}
