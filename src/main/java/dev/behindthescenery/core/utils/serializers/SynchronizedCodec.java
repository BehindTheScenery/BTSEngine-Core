package dev.behindthescenery.core.utils.serializers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Сихнхронизированный {@link Codec} используется для потокобезопасного выполнения с использованием блокировок.
 */
public class SynchronizedCodec<A> implements Codec<A> {
    private final ReentrantLock lock = new ReentrantLock();
    private final Codec<A> delegate;

    public SynchronizedCodec(Codec<A> delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
        lock.lock();
        try {
            return this.delegate.decode(ops, input);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
        lock.lock();
        try {
            return this.delegate.encode(input, ops, prefix);
        } finally {
            lock.unlock();
        }
    }
}
