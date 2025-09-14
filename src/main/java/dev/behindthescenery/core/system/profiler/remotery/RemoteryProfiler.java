package dev.behindthescenery.core.system.profiler.remotery;

import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.remotery.Remotery;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.util.remotery.Remotery.*;
import static org.lwjgl.util.remotery.RemoteryGL.*;

public class RemoteryProfiler {
    public static RemoteryProfiler.Init start() {
        var prof = new RemoteryProfiler.Init();
        prof.initProfiler();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(RemoteryProfiler.Init.Instance == null) return;
            RemoteryProfiler.Init.Instance.shutdownProfiler();
        }));

        return prof;
    }

    public enum Category {
        CPU,
        GPU,
        ALL,
        NONE
    }

    public static class None implements Profiler {
        @Override
        public void close() {}
    }

    public static class BindGl implements AutoCloseable {
        protected static boolean isBind;

        public BindGl() {
            if(isBind) return;
            rmt_BindOpenGL();
            isBind = true;
        }

        @Override
        public void close() {
            if(!isBind) return;
            rmt_UnbindOpenGL();
            isBind = false;
        }
    }

    public static class Multi implements Profiler {
        private static final Map<ByteBuffer, IntBuffer> hashCacheMap = new ConcurrentHashMap<>();

        private ByteBuffer nameBufferCpu;
        private ByteBuffer nameBufferGpu;
        protected boolean isStatic = false;

        public Multi(String name) {
            this(name, 0);
        }

        public Multi(String name, int cpu_flags) {
            if(!isEnabled()) return;

            this.nameBufferCpu = MemoryUtil.memUTF8(name, true);
            IntBuffer hashCache = hashCacheMap.computeIfAbsent(nameBufferCpu, key -> MemoryUtil.memCallocInt(1));
            rmt_BeginCPUSample(nameBufferCpu, cpu_flags, hashCache);

            if(!BindGl.isBind) {
                BtsCore.LOGGER.error("Need bind GL! try(var ignored = new RemoteryProfiler.BindGl())");
                return;
            }
            this.nameBufferGpu = MemoryUtil.memUTF8(name, true);
            hashCache = hashCacheMap.computeIfAbsent(nameBufferGpu, key -> MemoryUtil.memCallocInt(1));
            rmt_BeginOpenGLSample(nameBufferGpu, hashCache);
        }

        public Multi(ByteBuffer buffer) {
            this(buffer, 0);
        }

        public Multi(ByteBuffer buffer, int cpu_flags) {
            if(!isEnabled()) return;

            this.nameBufferCpu = buffer;
            IntBuffer hashCache = hashCacheMap.computeIfAbsent(nameBufferCpu, key -> MemoryUtil.memCallocInt(1));
            rmt_BeginCPUSample(nameBufferCpu, cpu_flags, hashCache);

            if(!BindGl.isBind) {
                BtsCore.LOGGER.error("Need bind GL! try(var ignored = new RemoteryProfiler.BindGl())");
                return;
            }
            this.nameBufferGpu = buffer;
            hashCache = hashCacheMap.computeIfAbsent(nameBufferGpu, key -> MemoryUtil.memCallocInt(1));
            rmt_BeginOpenGLSample(nameBufferGpu, hashCache);
            isStatic = true;
        }

        @Override
        public void close() {
            if(!isEnabled()) return;
            rmt_EndCPUSample();

            if(!BindGl.isBind)
                return;

            rmt_EndOpenGLSample();
        }
    }

    public static class Gpu implements Profiler {
        private static final Map<ByteBuffer, IntBuffer> hashCacheMap = new ConcurrentHashMap<>();

        private final ByteBuffer nameBuffer;
        protected boolean isStatic = false;

        public Gpu(String name) {
            if(!isEnabled()) {
                nameBuffer = null;
                return;
            }
            if(!BindGl.isBind) {
                BtsCore.LOGGER.error("Need bind GL! try(var ignored = new RemoteryProfiler.BindGl())");
                nameBuffer = null;
                return;
            }

            this.nameBuffer = MemoryUtil.memUTF8(name, true);
            IntBuffer hashCache = hashCacheMap.computeIfAbsent(nameBuffer, key -> MemoryUtil.memCallocInt(1));
            rmt_BeginOpenGLSample(name, hashCache);
        }

        public Gpu(ByteBuffer buffer) {
            if(!isEnabled()) {
                nameBuffer = null;
                return;
            }
            if(!BindGl.isBind) {
                nameBuffer = null;
                BtsCore.LOGGER.error("Need bind GL! try(var ignored = new RemoteryProfiler.BindGl())");
                return;
            }

            this.nameBuffer = buffer;
            IntBuffer hashCache = hashCacheMap.computeIfAbsent(nameBuffer, key -> MemoryUtil.memCallocInt(1));
            rmt_BeginOpenGLSample(nameBuffer, hashCache);
            isStatic = true;
        }

        public Gpu setStatic(boolean value) {
            this.isStatic = value;
            return this;
        }

        @Override
        public void close() {
            if(!isEnabled()) return;

            if(!BindGl.isBind)
                return;


            rmt_EndOpenGLSample();
        }
    }

    public static class Cpu implements Profiler {
        private static final Map<ByteBuffer, IntBuffer> hashCacheMap = new ConcurrentHashMap<>();

        private static final int CPU_FLAGS = 0;
        private ByteBuffer nameBuffer;
        protected boolean isStatic = false;

        public Cpu(String name) {
            this(name, CPU_FLAGS);
        }

        public Cpu(String name, int flags) {
            if(!BtsCore.LoadProfiler) return;
            this.nameBuffer = MemoryUtil.memUTF8(name, true);
            IntBuffer hashCache = hashCacheMap.computeIfAbsent(nameBuffer, key -> MemoryUtil.memCallocInt(1));
            rmt_BeginCPUSample(nameBuffer, flags, hashCache);
        }

        public Cpu(ByteBuffer buffer) {
            this(buffer, CPU_FLAGS);
        }

        public Cpu(ByteBuffer buffer, int flags) {
            if(!isEnabled()) return;
            this.nameBuffer = buffer;
            IntBuffer hashCache = hashCacheMap.computeIfAbsent(nameBuffer, key -> MemoryUtil.memCallocInt(1));
            rmt_BeginCPUSample(nameBuffer, flags, hashCache);
            isStatic = true;
        }

        public Cpu setStatic(boolean value) {
            this.isStatic = value;
            return this;
        }

        @Override
        public void close() {
            if(!isEnabled()) return;
            rmt_EndCPUSample();
            if(!isStatic && nameBuffer != null)
                MemoryUtil.memFree(nameBuffer);
        }

        public static void cleanup() {
            for (IntBuffer buffer : hashCacheMap.values()) {
                MemoryUtil.memFree(buffer);
            }
            hashCacheMap.clear();
        }
    }

    public static class Init {
        public static Init Instance;
        protected boolean isEnabled = false;

        private long rmtContext;
        protected BindGl bindGl;


        public void initProfiler() {
            try (MemoryStack stack = MemoryStack.stackPush()) {

                BtsCore.LOGGER.info("Remotery Initializing!");
                var pContext = stack.mallocPointer(1);
                int result = rmt_CreateGlobalInstance(pContext);
                if (result != RMT_ERROR_NONE) {
                    throw new IllegalStateException("Failed to initialize Remotery: " + result);
                }
                rmtContext = pContext.get(0);

                Instance = this;
                isEnabled = true;
                BtsCore.LOGGER.info("Remotery Initialized! {}", Info.connectionMessage());

                BtsRenderSystem.exec(() -> bindGl = new BindGl());
            }
        }

        public void shutdownProfiler() {
            if (rmtContext != 0L) {
                rmt_DestroyGlobalInstance(rmtContext);
                rmtContext = 0L;
                isEnabled = false;
                BtsCore.LOGGER.info("Remotery Shutdown!");

                BtsRenderSystem.exec(() -> bindGl.close());
            }
        }
    }

    public static class Info {
        public static String connectionMessage() {

            if(!isEnabled())
                return "Remotery not initialized!";

            short port = Remotery.rmt_Settings().port();
            return "http://127.0.0.1:" + port + "/";
        }
    }

    public static boolean isEnabled() {
        return BtsCore.LoadProfiler && Init.Instance != null && Init.Instance.isEnabled;
    }

    public interface Profiler extends AutoCloseable {
        @Override
        default void close() {}
    }
}
