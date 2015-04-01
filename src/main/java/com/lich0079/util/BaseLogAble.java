package com.lich0079.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseLogAble  implements ILogAble{

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void log(String msg) {
		LogUtils.log(logger, msg);
	}

	public void logDebug(String msg) {
		LogUtils.logDebug(logger, msg);
	}

	public void logError(String msg, Throwable t) {
		LogUtils.logError(logger, msg,t);
	}
}
