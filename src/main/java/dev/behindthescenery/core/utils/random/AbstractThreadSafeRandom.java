package dev.behindthescenery.core.utils.random;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.*;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractThreadSafeRandom implements BaseRandom {
    public static final int INT_BITS = 48;
    public static final long SEED_MASK = 0xFFFFFFFFFFFFL;
    public static final long MULTIPLIER = 25214903917L;
    public static final long INCREMENT = 11L;

    public final AtomicLong seed = new AtomicLong();
    public final GaussianGenerator gaussianGenerator = new GaussianGenerator(this);

    public AbstractThreadSafeRandom(long seed) {
        this.setSeed(seed);
    }

    @Override
    public double nextGaussian() {
        return gaussianGenerator.next();
    }

    public static abstract class Splitter implements RandomSplitter {
        protected final long seed;

        public Splitter(long seed) {
            this.seed = seed;
        }

        public abstract Random split(int x, int y, int z);

        public abstract Random split(String seed);

        public abstract Random split(long seed);

        @VisibleForTesting
        public void addDebugInfo(StringBuilder info) {
            info.append("LegacyPositionalRandomFactory{").append(this.seed).append("}");
        }
    }

    public static abstract class LocalSplitter extends Splitter {
        public LocalSplitter(long seed) {
            super(seed);
        }

        @Override
        public Random split(int x, int y, int z) {
            long l = MathHelper.hashCode(x, y, z);
            long m = l ^ this.seed;
            return new LocalRandom(m);
        }

        @Override
        public Random split(String seed) {
            int i = seed.hashCode();
            return new LocalRandom((long)i ^ this.seed);
        }
    }
}
