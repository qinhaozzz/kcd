package com.lim.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qinhao
 */
public class LogbackPattern {

    private static final Logger logger = LoggerFactory.getLogger(LogbackPattern.class);
    private static String msg = "Shutting down in response to management operation 'shutdown'";

    public static void main(String[] args) {
        logger.info(msg);
    }
}
