package dev.behindthescenery.core.utils.random;

import net.minecraft.util.math.random.BaseRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;

import java.util.function.Supplier;

public class ThreadLocalRandom<T extends BaseRandom> extends AbstractThreadSafeRandom {

    private ThreadLocal<T> randomThreadLocal;

    public ThreadLocalRandom(Supplier<T> randomSupplier) {
        super(0);
        randomThreadLocal = ThreadLocal.withInitial(randomSupplier);
    }

    @Override
    public int next(int bits) {
        return randomThreadLocal.get().next(bits);
    }

    @Override
    public Random split() {
        return randomThreadLocal.get().split();
    }

    @Override
    public RandomSplitter nextSplitter() {
        return randomThreadLocal.get().nextSplitter();
    }

    @Override
    public void setSeed(long seed) {
        if(randomThreadLocal == null) return;
        randomThreadLocal.get().setSeed(seed);
    }
}
