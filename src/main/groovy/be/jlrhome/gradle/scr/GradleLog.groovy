package be.jlrhome.gradle.scr

import org.apache.felix.scrplugin.Log
import org.gradle.api.Project
import org.gradle.api.logging.Logger

class GradleLog implements Log {

    Logger logger;

    GradleLog(Logger logger) {
        this.logger = logger;
    }


    boolean isDebugEnabled() {
        return logger.isDebugEnabled()
    }

    void debug(String content) {
        logger.debug(content)
    }

    void debug(String content, Throwable error) {
        logger.debug(content, error)
    }

    void debug(Throwable error) {
        logger.debug(error.toString())
    }


    boolean isInfoEnabled() {
        return logger.isInfoEnabled()
    }


    void info(String content) {
        logger.info(content)
    }


    void info( String content, Throwable error ) {
        logger.info(content, error)
    }


    void info(Throwable error) {
        logger.info(error.toString())
    }


    boolean isWarnEnabled() {
        return logger.isWarnEnabled()
    }


    void warn(String content) {
        logger.warn(content)
    }


    void warn(String content, String location, int lineNumber) {
        logger.warn("{} [{},{}]", content, location, lineNumber)
    }

    void warn(String content, String location, int lineNumber, int columNumber) {
    	logger.warn("{} [{},{}:{}]", content, location, lineNumber , columNumber)
    }


    void warn(String content, Throwable error) {
        logger.warn(content, error)
    }


    void warn(Throwable error) {
        logger.warn(error.toString())
    }


    boolean isErrorEnabled() {
        logger.isErrorEnabled()
    }


    void error(String content) {
        logger.error(content)
    }


    void error(String content, String location, int lineNumber) {
        logger.error("{} [{},}{}]", content, location, lineNumber)
    }

    void error( String content, String location, int lineNumber, int columnNumber ) {
    	logger.error("{} [{},{}:{}]", content, location, lineNumber, columnNumber)
    }


    void error(String content, Throwable error) {
        logger.error(content, error)
    }


    public void error(Throwable error) {
        logger.error(error.toString())
    }
}