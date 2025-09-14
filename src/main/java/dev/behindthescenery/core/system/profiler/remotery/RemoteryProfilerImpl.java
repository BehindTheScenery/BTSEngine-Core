package dev.behindthescenery.core.system.profiler.remotery;

import java.nio.ByteBuffer;

import static org.lwjgl.util.remotery.RemoteryGL.rmt_BindOpenGL;
import static org.lwjgl.util.remotery.RemoteryGL.rmt_UnbindOpenGL;

public class RemoteryProfilerImpl extends RemoteryProfiler {
    public static Profiler pushDynamic(String name, Category category) {
        switch (category) {
            case ALL -> {
                return new Multi(name);
            }
            case CPU -> {
                return new Cpu(name);
            }
            case GPU -> {
                return new Gpu(name);
            }
        }

        return new None();
    }

    public static Profiler pushStatic(ByteBuffer buffer, Category category) {
        switch (category) {
            case ALL -> {
                return new Multi(buffer);
            }
            case CPU -> {
                return new Cpu(buffer);
            }
            case GPU -> {
                return new Gpu(buffer);
            }
        }

        return new None();
    }

    public static void bind() {
        rmt_BindOpenGL();
    }

    public static void unbind() {
        rmt_UnbindOpenGL();
    }
}
