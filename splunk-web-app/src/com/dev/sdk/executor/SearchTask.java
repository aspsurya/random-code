package com.dev.sdk.executor;

import java.util.List;
import java.util.concurrent.Callable;

import com.dev.sdk.splunk.ExportSearch;
import com.splunk.Event;
import com.splunk.Service;


public class SearchTask implements Callable<String> {
	
	private String alertType;
	private String alertKeyword;
	private Service service;

	public SearchTask(Service service,String searchType,String alertKeyword) {
		this.service = service;
		this.alertType = searchType;
		this.alertKeyword = alertKeyword;
	}
	
	public String getAlertType() {
		return this.alertType;
	}

	@Override
	public String call() throws Exception {
		String status = null;
		try {
			if("ACCESS_LOG_ALERT".equalsIgnoreCase(this.alertType)) {
				List<Event> events = ExportSearch.executeExportSearch(this.service,this.alertType,this.alertKeyword);
				AccesslogAlertExecutor.setEvents(events);
				//System.out.println("\nEvents size = " + events.size());
			}else if("SYS_LOG_ALERT".equalsIgnoreCase(this.alertType)) {
				List<Event> events = ExportSearch.executeExportSearchSyslog(this.service,this.alertType,this.alertKeyword);
				SyslogAlertExecutor.setEvents(events);
				//System.out.println("\nEvents size = " + events.size());
			}
			status = "SUCCESS";
		} catch (Exception e) {
			status = "FAILED";
			e.printStackTrace();
		}
		
		return status;
	}
	
	
	private void displayEvents(List<Event> events) {
		int counter = 0; // count the number of events
		for (Event event : events) {
			System.out.println("\n***** Event " + counter++ + " *****");
			for (String key : event.keySet()) {
				System.out.println("   " + key + " :  " + event.get(key));
			}
		}
	}
	
}