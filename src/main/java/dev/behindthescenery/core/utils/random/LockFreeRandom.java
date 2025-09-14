package dev.behindthescenery.core.utils.random;

import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;

public class LockFreeRandom extends AbstractThreadSafeRandom {
    public LockFreeRandom(long seed) {
        super(seed);
    }

    /**
     * o - Old data <br>
     * n - New Data
     */
    @Override
    public int next(int bits) {
        long o;
        long n;
        do {
            o = this.seed.get();
            n = o * AbstractThreadSafeRandom.MULTIPLIER + AbstractThreadSafeRandom.INCREMENT & AbstractThreadSafeRandom.SEED_MASK;
        } while(!this.seed.compareAndSet(o, n));

        return (int)(n >>> 48 - bits);
    }

    @Override
    public Random split() {
        return new LockFreeRandom(this.nextLong());
    }

    @Override
    public RandomSplitter nextSplitter() {
        return new Splitter(this.nextLong());
    }

    @Override
    public void setSeed(long seed) {
        long newSeed;
        long s;
        do {
            newSeed = (seed ^ AbstractThreadSafeRandom.MULTIPLIER) & AbstractThreadSafeRandom.SEED_MASK;
            s = this.seed.get();
        } while (!this.seed.compareAndSet(s, newSeed));
    }

    public static class Splitter extends LocalSplitter {

        public Splitter(long seed) {
            super(seed);
        }

        @Override
        public Random split(long seed) {
            return new LockFreeRandom(seed);
        }
    }
}
