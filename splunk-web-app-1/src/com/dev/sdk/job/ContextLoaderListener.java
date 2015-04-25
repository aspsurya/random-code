package com.dev.sdk.job;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

	private static Logger logger = Logger.getLogger(ContextLoaderListener.class
			.getName());

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("ContextLoaderListener Initialized");
		init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("ContextLoaderListener Destroyed");
		uninit();
	}

	private void init() {
		//AlertMonitorTimer livenessMonitorTimer = new AlertMonitorTimer();
		//livenessMonitorTimer.createTimer();
		logger.info("AlertMonitorTimer started");
	}

	private void uninit() {
		logger.fine("AlertMonitorTimer ContextLoaderListener.uninit");
	}
}
