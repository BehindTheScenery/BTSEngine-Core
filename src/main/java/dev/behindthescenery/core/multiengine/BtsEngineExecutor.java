package dev.behindthescenery.core.multiengine;

import dev.behindthescenery.multiengine.EngineExecutor;
import dev.behindthescenery.multiengine.EngineLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BtsEngineExecutor implements EngineExecutor {

    private int bits;
    private final BtsEngineLogger logger;

    public BtsEngineExecutor(Logger logger) {
        this.logger = new BtsEngineLogger(logger);
    }

    @Override
    public EngineLogger getLogger() {
        return logger;
    }

    @Override
    public EngineLogger createLogger(String s) {
        return new BtsEngineLogger(LoggerFactory.getLogger(s));
    }

    @Override
    public String getName() {
        return "Bts Engine";
    }

    @Override
    public int getActiveModules() {
        return bits;
    }

    public void setBits(int bits) {
        this.bits |= bits;
    }
}
