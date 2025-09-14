package dev.behindthescenery.core.utils.serializers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Сихнхронизированный {@link Codec} используется для потокобезопасного выполнения с использованием блокировок.
 */
@Deprecated
public class SynchronizedOldCodec<A> implements Codec<A> {
    private final ReentrantLock lock = new ReentrantLock(false);
    private final Codec<A> delegate;

    public SynchronizedOldCodec(Codec<A> delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
        try {
            lock.lockInterruptibly();
            return this.delegate.decode(ops, input);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    @Override
    public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
        try {
            lock.lockInterruptibly();
            return this.delegate.encode(input, ops, prefix);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
    }
}
