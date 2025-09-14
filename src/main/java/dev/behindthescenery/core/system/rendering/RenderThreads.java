package dev.behindthescenery.core.system.rendering;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class RenderThreads {


    public static ScheduledExecutorService PARTICLE_LOGIC_THREAD;
    public static ScheduledExecutorService PARTICLE_ANIMATION_THREAD;


    public static void init() {
        PARTICLE_LOGIC_THREAD = Executors.newSingleThreadScheduledExecutor();;
        PARTICLE_ANIMATION_THREAD = Executors.newSingleThreadScheduledExecutor();
    }

    public static void shutdown() {
        PARTICLE_LOGIC_THREAD.shutdownNow();
        PARTICLE_ANIMATION_THREAD.shutdownNow();
    }
}
