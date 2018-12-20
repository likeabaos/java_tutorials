package com.play.ground.HelloHelper;

public class LoggingHelper {
	private System.Logger logger;

	public LoggingHelper(String name) {
		this.logger = System.getLogger(name);
	}

	public void info(String msg, Throwable throwable) {
		this.logger.log(System.Logger.Level.INFO, msg, throwable);
	}

	public void info(String msg) {
		this.info(msg, null);
	}

	public void error(String msg, Throwable throwable) {
		this.logger.log(System.Logger.Level.ERROR, msg, throwable);
	}

	public void error(String msg) {
		this.error(msg, null);
	}

	public void debug(String msg, Throwable throwable) {
		this.logger.log(System.Logger.Level.DEBUG, msg, throwable);
	}

	public void debug(String msg) {
		this.debug(msg, null);
	}
}
