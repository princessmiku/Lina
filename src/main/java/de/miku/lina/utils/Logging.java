package de.miku.lina.utils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Logging {

    private static final Logger thisLogger = LoggerFactory.getLogger(Logging.class);
    private static final Map<Class<?>, Logger> loggers = new HashMap<>();
    private static final Map<String, Logger> loggersString = new HashMap<>();

    public static void info(String response) {
       thisLogger.info(response);
    }

    public static void info(String loggerName, String response) {
        loggerName = loggerName.toLowerCase();
        Logger logger = loggersString.get(loggerName);
        if (logger == null) {
            logger = LoggerFactory.getLogger(loggerName);
            loggersString.put(loggerName, logger);
        }
        logger.info(response);
    }

    public static void info(@NotNull Class<?> clazz, String response) {
        Logger logger = loggers.get(clazz);
        if (logger == null) {
            logger = LoggerFactory.getLogger(clazz);
            loggers.put(clazz, logger);
        }
        logger.info(response);
    }

    public static void debug(String response) {
       thisLogger.debug(response);
    }

    public static void debug(String loggerName, String response) {
        loggerName = loggerName.toLowerCase();
        Logger logger = loggersString.get(loggerName);
        if (logger == null) {
            logger = LoggerFactory.getLogger(loggerName);
            loggersString.put(loggerName, logger);
        }
        logger.debug(response);
    }

    public static void debug(@NotNull Class<?> clazz, String response) {
        Logger logger = loggers.get(clazz);
        if (logger == null) {
            logger = LoggerFactory.getLogger(clazz);
            loggers.put(clazz, logger);
        }
        logger.debug(response);
    }

    public static void warning(String response) {
       thisLogger.warn(response);
    }

    public static void warning(String loggerName, String response) {
        loggerName = loggerName.toLowerCase();
        Logger logger = loggersString.get(loggerName);
        if (logger == null) {
            logger = LoggerFactory.getLogger(loggerName);
            loggersString.put(loggerName, logger);
        }
        logger.warn(response);
    }

    public static void warning(@NotNull Class<?> clazz, String response) {
        Logger logger = loggers.get(clazz);
        if (logger == null) {
            logger = LoggerFactory.getLogger(clazz);
            loggers.put(clazz, logger);
        }
        logger.warn(response);
    }

    public static void error(String response) {
       thisLogger.error(response);
    }

    public static void error(String loggerName, String response) {
        loggerName = loggerName.toLowerCase();
        Logger logger = loggersString.get(loggerName);
        if (logger == null) {
            logger = LoggerFactory.getLogger(loggerName);
            loggersString.put(loggerName, logger);
        }
        logger.error(response);
    }

    public static void error(@NotNull Class<?> clazz, String response) {
        Logger logger = loggers.get(clazz);
        if (logger == null) {
            logger = LoggerFactory.getLogger(clazz);
            loggers.put(clazz, logger);
        }
        logger.error(response);
    }


}
