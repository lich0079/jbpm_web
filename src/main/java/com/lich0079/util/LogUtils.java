package com.lich0079.util;

import org.slf4j.Logger;


public class LogUtils {

	public static void log(Logger logger, String msg) {
		logger.info(msg);
	}

	public static void logDebug(Logger logger, String msg) {
		logger.debug(msg);
	}

	public static void logError(Logger logger, String msg, Throwable t) {
		logger.error(msg, t);
//		t.printStackTrace();
	}
}
