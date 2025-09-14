package dev.behindthescenery.core.utils.random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;

public class SynchronizedRandom extends AbstractThreadSafeRandom {
    public SynchronizedRandom(long seed) {
        super(seed);
    }

    @Override
    public synchronized int next(int bits) {
        long l = this.seed.get();
        long m = l * AbstractThreadSafeRandom.MULTIPLIER + AbstractThreadSafeRandom.INCREMENT & AbstractThreadSafeRandom.SEED_MASK;
        seed.set(m);
        return (int) (m >> AbstractThreadSafeRandom.INT_BITS - bits);
    }

    @Override
    public Random split() {
        return new SynchronizedRandom(this.nextLong());
    }

    @Override
    public RandomSplitter nextSplitter() {
        return new Splitter(this.nextLong());
    }

    @Override
    public synchronized void setSeed(long seed) {
        this.seed.set((seed ^ AbstractThreadSafeRandom.MULTIPLIER) & AbstractThreadSafeRandom.SEED_MASK);
    }

    public static class Splitter extends AbstractThreadSafeRandom.Splitter {

        public Splitter(long seed) {
            super(seed);
        }

        @Override
        public Random split(int x, int y, int z) {
            long l = MathHelper.hashCode(x, y, z);
            long m = l ^ this.seed;
            return new SynchronizedRandom(m);
        }

        @Override
        public Random split(String seed) {
            int i = seed.hashCode();
            return new SynchronizedRandom((long)i ^ this.seed);
        }

        @Override
        public Random split(long seed) {
            return new SynchronizedRandom(seed);
        }
    }
}
