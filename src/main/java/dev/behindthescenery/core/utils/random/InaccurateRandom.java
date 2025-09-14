package dev.behindthescenery.core.utils.random;

import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;

public class InaccurateRandom extends AbstractThreadSafeRandom {
    private long seed;

    public InaccurateRandom(long seed) {
        super(seed);
    }

    @Override
    public Random split() {
        return new LocalRandom(this.nextLong());
    }

    @Override
    public net.minecraft.util.math.random.RandomSplitter nextSplitter() {
        return new Splitter(this.nextLong());
    }

    @Override
    public void setSeed(long l) {
        this.seed = (l ^ MULTIPLIER) & SEED_MASK;
    }

    @Override
    public double nextGaussian() {
        return 0;
    }

    @Override
    public int next(int bits) {
        long l = this.seed * MULTIPLIER + INCREMENT & SEED_MASK;
        this.seed = l;
        return (int)(l >> INT_BITS - bits);
    }

    public static class Splitter extends LocalSplitter {

        public Splitter(long seed) {
            super(seed);
        }

        @Override
        public Random split(long seed) {
            return new InaccurateRandom(seed);
        }
    }
}
