package dev.behindthescenery.core.system.logger;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class GlobalThreadData<T> {
    private final Map<Thread, WeakReference<T>> values = new ConcurrentHashMap<>();
    private final ThreadLocal<T> threadLocal;

    public GlobalThreadData() {
        threadLocal = new LThreadLocal();
    }

    public GlobalThreadData(Supplier<T> supplier) {
        threadLocal = new LSThreadLocal(supplier);
    }

    public T get() {
        return threadLocal.get();
    }

    public void set(T value) {
        threadLocal.set(value);
    }

    public void remove() {
        threadLocal.remove();
    }

    public Map<Thread, Optional<T>> getAllThreadValues() {
        Map<Thread, Optional<T>> result = new HashMap<>();

        for (Map.Entry<Thread, WeakReference<T>> entry : values.entrySet()) {
            result.put(entry.getKey(), Optional.ofNullable(entry.getValue().get()));
        }

        return result;
    }

    protected class LThreadLocal extends ThreadLocal<T> {
        @Override
        protected T initialValue() {
            return null;
        }

        @Override
        public void set(T value) {
            super.set(value);
            values.put(Thread.currentThread(), new WeakReference<>(value));
        }

        @Override
        public void remove() {
            values.remove(Thread.currentThread());
            super.remove();
        }
    }

    protected class LSThreadLocal extends ThreadLocal<T> {
        private final Supplier<T> supplier;

        protected LSThreadLocal(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        protected T initialValue() {
            var v =supplier.get();
            values.put(Thread.currentThread(), new WeakReference<>(v));
            return v;
        }

        @Override
        public void set(T value) {
            super.set(value);
            values.put(Thread.currentThread(), new WeakReference<>(value));
        }

        @Override
        public void remove() {
            values.remove(Thread.currentThread());
            super.remove();
        }
    }
}
