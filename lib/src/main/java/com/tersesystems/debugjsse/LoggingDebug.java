package com.tersesystems.debugjsse;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a class that logs to the given logger at the given log level.
 */
public class LoggingDebug extends AbstractDebug {

    private final Logger logger;
    private final Level level;

    public LoggingDebug(Level level, Logger logger) {
        this.level = level;
        this.logger = logger;
    }

    @Override
    public void enter(String message) {
        logger.log(level, message);
    }

    @Override
    public void exit(String message) {
        logger.log(level, message);
    }

    @Override
    public void exception(String message, Exception e) {
        logger.log(level, message);
    }
}
