package com.dev.sdk.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dev.sdk.executor.AlertThreadController;

public class AlertMonitorTimer {
	private List<String> alertTypes = new ArrayList<String>();
	private Properties properties = new Properties();
	private final String pathname = "C:/hackathon/job.properties";

	public AlertMonitorTimer() {
		alertTypes.add("ACCESS_LOG_ALERT");
		alertTypes.add("SYS_LOG_ALERT");
	}

	Timer timer = null;

	public void createTimer(String job_interval_length) {
		try {
			Timer timer = new Timer();
			long delay = Long.parseLong(job_interval_length); // In seconds
			long period = delay;

			timer.schedule(new TimerTask() {
				public void run() {

					FileInputStream inStream;
					try {
						inStream = new FileInputStream(new File(pathname));
						properties.load(inStream);
						inStream.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("###########  " + properties.getProperty("FLAG")  + " ###########");
					if ("ON".equalsIgnoreCase(properties.getProperty("FLAG"))) {
						for (String type : alertTypes) {
							Thread thread = new Thread(
									new AlertThreadController(type), "Thread_"
											+ type);
							thread.start();
						}
					}
					
					properties.clear();
				}
			}, delay, period);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception occurred in LivenessMonitorTimer : "
					+ ex.getMessage());
		}
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, ExecutionException, AddressException,
			MessagingException {
		List<String> alertTypes = new ArrayList<String>();
		alertTypes.add("ACCESS_LOG_ALERT");
		alertTypes.add("SYS_LOG_ALERT");
		for (String type : alertTypes) {
			Thread thread = new Thread(new AlertThreadController(type),
					"Thread_" + type);
			thread.start();
		}
	}
}
