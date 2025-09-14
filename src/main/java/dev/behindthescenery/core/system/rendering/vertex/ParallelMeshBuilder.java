package dev.behindthescenery.core.system.rendering.vertex;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.mixin.access.render.BufferAllocatorAccessor;
import dev.behindthescenery.core.mixin.access.render.CloseableBufferAccessor;
import dev.behindthescenery.core.system.rendering.vertex.patch.BufferBuilderPatch;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.util.BufferAllocator;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Deprecated
public class ParallelMeshBuilder extends BufferBuilder {
    private static final ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder()
            .setNameFormat("Bts ParallelMeshBuilder").build());

    private final BufferAllocatorPool allocatorPool = new BufferAllocatorPool(1024); // 4MB на поток


    protected BufferBuilderPatch thisBuilderPatch = (BufferBuilderPatch)this;

    private final BufferAllocator allocator;
    private final VertexFormat format;
    private final VertexFormat.DrawMode drawMode;

    // Пул BufferBuilder на поток
    private final ThreadLocal<BufferAllocatorPool.ThreadAllocatorContext> threadLocalContext = new ThreadLocal<>();
    private final ConcurrentLinkedQueue<RunnableBuilder> builders = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Runnable> endTasks = new ConcurrentLinkedQueue<>();

    public ParallelMeshBuilder(BufferAllocator allocator, VertexFormat.DrawMode drawMode, VertexFormat format) {
        super(allocator, drawMode, format);
        this.allocator = allocator;
        this.drawMode = drawMode;
        this.format = format;
    }

    public void add(RunnableBuilder builderTask) {
        builders.add(builderTask);
    }

    public BuiltBuffer buildParallel() {
        if (builders.isEmpty()) {
            return thisBuilderPatch.bts$build();
        }

        int tasksPerThread = Math.max(1, builders.size() / Runtime.getRuntime().availableProcessors());

        List<List<RunnableBuilder>> partitions = partition(builders, tasksPerThread);
        List<CompletableFuture<BuiltBuffer>> futures = new ArrayList<>();

        for (List<RunnableBuilder> partition : partitions) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                BufferAllocatorPool.ThreadAllocatorContext ctx = threadLocalContext.get();
                if (ctx == null) {
                    BufferAllocator allocatorLocal = allocatorPool.acquire();
                    BufferBuilder builder = new BufferBuilder(allocatorLocal, drawMode, format);
                    ctx = new BufferAllocatorPool.ThreadAllocatorContext(allocatorLocal, builder);
                    threadLocalContext.set(ctx);
                }

                for (RunnableBuilder builderTask : partition) {
                    builderTask.build(ctx.builder);
                }

                BuiltBuffer buffer = ctx.builder.end();

                BufferAllocatorPool.ThreadAllocatorContext finalCtx = ctx;
                endTasks.add(() -> allocatorPool.release(finalCtx.allocator));
                threadLocalContext.remove(); // очистить контекст

                return buffer;
            }, executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        List<BuiltBuffer> builtBuffers = new ArrayList<>();

        for (CompletableFuture<BuiltBuffer> future : futures) {
            try {
                builtBuffers.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                BtsCore.LOGGER.error(e.toString());
            }
        }

        builders.clear();

        return linkBuffers(builtBuffers);
    }

    private List<List<RunnableBuilder>> partition(Collection<RunnableBuilder> builders, int size) {
        List<List<RunnableBuilder>> partitions = new ArrayList<>();
        List<RunnableBuilder> current = new ArrayList<>(size);
        for (RunnableBuilder builder : builders) {
            current.add(builder);
            if (current.size() >= size) {
                partitions.add(current);
                current = new ArrayList<>(size);
            }
        }

        if (!current.isEmpty()) {
            partitions.add(current);
        }

        return partitions;
    }

    private BuiltBuffer linkBuffers(List<BuiltBuffer> buffers) {
        if (buffers.isEmpty()) {
            throw new IllegalStateException("No buffers to link");
        }

        int totalVertexCount = 0;
        for (BuiltBuffer buffer : buffers) {
            totalVertexCount += buffer.getDrawParameters().vertexCount();
        }

        int totalSizeBytes = totalVertexCount * format.getVertexSizeByte(); // Важно!
        long destPointer = allocator.allocate(totalSizeBytes);

        BufferAllocator.CloseableBuffer combinedBuffer = allocator.getAllocated();
        if (combinedBuffer == null) {
            throw new OutOfMemoryError("Failed to allocate combined buffer");
        }

        BufferAllocatorAccessor bufferAllocatorAccessor = (BufferAllocatorAccessor) allocator;
        CloseableBufferAccessor closeableBufferAccessor = (CloseableBufferAccessor) combinedBuffer;

        long currentDest = bufferAllocatorAccessor.getPointer() + closeableBufferAccessor.getOffset();

        // Теперь копируем данные из всех маленьких буферов
        for (BuiltBuffer buffer : buffers) {
            ByteBuffer src = buffer.getBuffer();
            long srcPointer = MemoryUtil.memAddress(src);
            long size = src.remaining();
            MemoryUtil.memCopy(srcPointer, currentDest, size);
            currentDest += size;
            MemoryUtil.memFree(src);
        }

        return new BuiltBuffer(
                combinedBuffer,
                new BuiltBuffer.DrawParameters(
                        format,
                        totalVertexCount,
                        drawMode.getIndexCount(totalVertexCount),
                        drawMode,
                        VertexFormat.IndexType.smallestFor(totalVertexCount)
                )
        );

    }

    private ByteBuffer linkVertexBuffers(List<ByteBuffer> buffers) {
        int totalSize = buffers.stream().mapToInt(ByteBuffer::remaining).sum();
        ByteBuffer linked = MemoryUtil.memAlloc(totalSize);

        for (ByteBuffer buffer : buffers) {
            linked.put(buffer.duplicate()); // Просто ставим все подряд
        }
        linked.flip();
        return linked;
    }

    @Nullable
    @Override
    public BuiltBuffer endNullable() {
        thisBuilderPatch.bts$ensureBuilding();
        thisBuilderPatch.bts$endVertex();
        BuiltBuffer builtBuffer = buildParallel();
        thisBuilderPatch.bts$isBuilding(false);
        thisBuilderPatch.bts$vertexPointer(-1L);
//        allocatorPool.destroy();

        Runnable runable;
        while ((runable = endTasks.poll()) != null) {
            runable.run();
        }

        return builtBuffer;
    }

    public interface RunnableBuilder {
        void build(BufferBuilder builder);
    }

}
