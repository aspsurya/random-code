package com.dev.sdk.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dev.sdk.executor.AlertThreadController;

public class AlertMonitorTimer {
	private final String liveness_job_interval_length = "30000";
	private List<String> alertTypes = new ArrayList<String>();

	public AlertMonitorTimer() {
		alertTypes.add("ACCESS_LOG_ALERT");
		//alertTypes.add("SEC_LOG_ALERT");
	}

	Timer timer = null;

	public void createTimer() {
		try {
			Timer timer = new Timer();
			long delay = Long.parseLong(liveness_job_interval_length); // Every 30 Seconds
			long period = delay;

			timer.schedule(new TimerTask() {
				public void run() {
					for (String type : alertTypes) {
						Thread thread = new Thread(new AlertThreadController(type), "Thread_" + type);
						thread.start();
					}
				}
			}, delay, period);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception occurred in LivenessMonitorTimer : "
					+ ex.getMessage());
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, AddressException, MessagingException{
		long startTime = System.currentTimeMillis();
		List<String> alertTypes = new ArrayList<String>();
		alertTypes.add("ACCESS_LOG_ALERT");
		for (String type : alertTypes) {
			Thread thread = new Thread(new AlertThreadController(type), "Thread_" + type);
			thread.start();
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken to process in seconds = " + (endTime-startTime)/1000);
	}
}
