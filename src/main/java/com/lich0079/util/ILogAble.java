package com.lich0079.util;

public interface ILogAble {

	void log(String msg);
	
	void logDebug(String msg);
	
	void logError(String msg, Throwable t);
}
