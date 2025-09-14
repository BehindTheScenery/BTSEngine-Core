package dev.behindthescenery.core.system.rendering.vertex;

import dev.behindthescenery.core.mixin.access.render.BufferAllocatorAccessor;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.BufferAllocator;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BufferAllocatorPool {

    private final int allocatorSizeBytes;
    private final ConcurrentLinkedQueue<BufferAllocator> pool = new ConcurrentLinkedQueue<>();

    public BufferAllocatorPool(int allocatorSizeBytes) {
        this.allocatorSizeBytes = allocatorSizeBytes;
    }

    public BufferAllocator acquire() {
        BufferAllocator allocator = pool.poll();
        if (allocator == null) {
            allocator = new BufferAllocator(allocatorSizeBytes);
        }
        return allocator;
    }

    public void release(BufferAllocator allocator) {
        if(((BufferAllocatorAccessor)allocator).getPointer() != 0)
            allocator.reset();
        if (pool.size() < 100) {
            pool.add(allocator);
        }
    }

    public void destroy() {
        BufferAllocator allocator;
        while ((allocator = pool.poll()) != null) {
            allocator.close();
        }
    }

    public static class ThreadAllocatorContext {
        final BufferAllocator allocator;
        final BufferBuilder builder;

        ThreadAllocatorContext(BufferAllocator allocator, BufferBuilder builder) {
            this.allocator = allocator;
            this.builder = builder;
        }
    }
}
