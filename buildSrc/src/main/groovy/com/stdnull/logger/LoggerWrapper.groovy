package com.stdnull.logger

import com.android.annotations.NonNull
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger

/**
 * Created by chen on 2017/11/5.
 */
class LoggerWrapper {
    private final Logger logger
    private final int logLevel

    LoggerWrapper(@NonNull Logger logger) {
        this(logger, LoggerLevel.INFO)
    }

    LoggerWrapper(@NonNull Logger logger, int logLevel) {
        this.logger = logger
        this.logLevel = logLevel
    }

    void debug(@NonNull String s, Object... params) {
        if(LoggerLevel.DEBUG < logLevel){
            return
        }
        logger.debug(s, params)
    }

    void info(@NonNull String s, Object... params) {
        if(LoggerLevel.INFO < logLevel){
            return
        }
        logger.info(s, params)
    }

    void lifeCycle(@NonNull String s, Object... params) {
        if(LoggerLevel.LIFECYCLE < logLevel){
            return
        }
        logger.lifecycle(s, params)
    }

    void warning(@NonNull String s, Object... params) {
        if(LoggerLevel.WARN < logLevel){
            return
        }
        logger.warn(s, params)
    }

    void quiet(@NonNull String s, Object... params) {
        if(LoggerLevel.QUIET < logLevel){
            return
        }
        logger.quiet(s, params)
    }

    void error(@NonNull String s, Object... params) {
        if(LoggerLevel.ERROR < logLevel){
            return
        }
        logger.error(s, params)
    }
}
