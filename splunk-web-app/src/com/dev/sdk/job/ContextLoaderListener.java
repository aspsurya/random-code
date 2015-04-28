package com.dev.sdk.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

	private static Logger logger = Logger.getLogger(ContextLoaderListener.class
			.getName());

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("ContextLoaderListener Initialized");
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("ContextLoaderListener Destroyed");
		uninit();
	}

	private void init() throws IOException {
		Properties properties = new Properties();
		String pathname = "C:/hackathon/job.properties";
		FileInputStream inStream = new FileInputStream( new File(pathname));
		properties.load(inStream);
		
		//if("ON".equalsIgnoreCase(properties.getProperty("FLAG"))){
			AlertMonitorTimer livenessMonitorTimer = new AlertMonitorTimer();
			String job_interval_length = properties.getProperty("job_interval_length");
			livenessMonitorTimer.createTimer(job_interval_length);
			logger.info("AlertMonitorTimer started");
		//}
	}

	private void uninit() {
		logger.fine("AlertMonitorTimer ContextLoaderListener.uninit");
	}
}
