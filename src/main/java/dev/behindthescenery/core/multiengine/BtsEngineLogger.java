package dev.behindthescenery.core.multiengine;

import dev.behindthescenery.multiengine.EngineLogger;
import org.slf4j.Logger;

public class BtsEngineLogger implements EngineLogger {

    private final Logger logger;

    public BtsEngineLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        logger.info(s);
    }

    @Override
    public void info(String s, Object... objects) {
        logger.info(s, objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        logger.info(s, throwable);
    }

    @Override
    public void error(String s) {
        logger.error(s);
    }

    @Override
    public void error(String s, Object... objects) {
        logger.error(s, objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        logger.error(s, throwable);
    }

    @Override
    public void warn(String s) {
        logger.warn(s);
    }

    @Override
    public void warn(String s, Object... objects) {
        logger.warn(s, objects);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        logger.warn(s, throwable);
    }

    @Override
    public void debug(String s) {
        logger.debug(s);
    }

    @Override
    public void debug(String s, Object... objects) {
        logger.debug(s, objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        logger.debug(s, throwable);
    }
}
