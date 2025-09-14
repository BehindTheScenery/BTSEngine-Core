package dev.behindthescenery.core.system.profiler;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleProfiler {
    protected static Queue<PData> QUEN = new LinkedList<>();

    private static AtomicInteger drawCalls = new AtomicInteger();

    public static void pushTime(String name) {
        QUEN.add(new PData(name, System.nanoTime()));
    }

    public static float popTime() {
        PData time = QUEN.poll();
        if(time == null) {
            return -1;
        }

        return time.getEndTime();
    }

    public static String popTimeString() {
        PData time = QUEN.poll();
        if(time == null) {
            return "NaN";
        }

        return time.getString();
    }

    public static void popAndPrint() {
        System.out.println(popTimeString());
    }

    public static void beginDrawCalls() {
        drawCalls.set(0);
    }

    public static int pushDrawCalls() {
        return drawCalls.incrementAndGet();
    }

    public static int popDrawClass() {
        return drawCalls.get();
    }

    private record PData(String name, Long time) {
        public float getEndTime() {
            return (System.nanoTime() - time)/1_000_000f;
        }

        public String getString() {
            return String.format("[%s]: %s", name, getEndTime());
        }
    }
}
