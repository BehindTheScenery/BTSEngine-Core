package dev.behindthescenery.core.utils;

import dev.behindthescenery.core.system.concurrent.ConcurrentCASOperations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

public class BtsConcurrentUtils {
    public static void tryLockCode(Runnable runtime, Lock lock) {
        try {
            while (!lock.tryLock()) {
                Thread.onSpinWait();
            }

            runtime.run();
        } finally {
            lock.unlock();
        }
    }

    public static <T> T tryLockCode(Supplier<T> runtime, Lock lock) {
        try {
            while (!lock.tryLock()) {
                Thread.onSpinWait();
            }

            return runtime.get();
        } finally {
            lock.unlock();
        }
    }

    public static <T> T lockCode(Supplier<T> runtime ,Lock lock) {
        lock.lock();
        try {
            return runtime.get();
        }  finally {
            lock.unlock();
        }
    }

    public static void lockCode(Runnable runnable ,Lock lock) {
        lock.lock();
        try {
            runnable.run();
        }  finally {
            lock.unlock();
        }
    }

    public static <T> T join(CompletableFuture<T> future) {
        while (!future.isDone()) {
            LockSupport.parkNanos("Waiting for future", 100000L);
        }
        return future.join();
    }

    public static <T> T[] fromReferenceToArray(AtomicReferenceArray<T> referenceArray) {
        int currentSize = referenceArray.length();
        Object[] toArray = new Object[currentSize];

        for (int i = 0; i < currentSize; i++) {
            toArray[i] = referenceArray.get(i);
        }

        return (T[]) toArray;
    }

    public static long[] fromReferenceToArray(AtomicLongArray referenceArray) {
        int currentSize = referenceArray.length();
        long[] toArray = new long[currentSize];

        for (int i = 0; i < currentSize; i++) {
            toArray[i] = referenceArray.get(i);
        }

        return toArray;
    }

    public static  <E> void fillReferenceArray(AtomicReferenceArray<E> atomicArray, E[] array) {
        for (int i = 0; i < array.length; i++) {
            atomicArray.set(i, array[i]);
        }
    }

    public static <E> E[] copyFromRefrerence(AtomicReferenceArray<E> array) {
        final int l = array.length();
        Object[] objects = new Object[l];
        for (int i = 0; i < l; i++) {
            objects[i] = array.get(i);
        }

        return (E[]) objects;
    }

    public static boolean compareAndSet(ConcurrentCASOperations concurrentCASOperations, Object oldO, Object newO, int tryConut) {
        int i = 0;
        while (i < tryConut) {
            if(concurrentCASOperations.compareAndSet(oldO, newO))
                return true;
            tryConut++;
        }
        return false;
    }

    public static int getThreadsForGenerator() {
       int processor = Runtime.getRuntime().availableProcessors();
       if(processor <= 2)
           return 1;

       if(processor >= 16)
           return processor - processor / 4;

       return processor / 2;
    }
}
