package dev.behindthescenery.core.system.concurrent;

public interface ConcurrentCASOperations {
    boolean compareAndSet(Object oldO, Object newO);
}
