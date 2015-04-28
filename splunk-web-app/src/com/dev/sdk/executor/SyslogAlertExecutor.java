package com.dev.sdk.executor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dev.sdk.splunk.ExportSearch;
import com.dev.sdk.util.EmailSender;
import com.splunk.Event;
import com.splunk.Service;


public class SyslogAlertExecutor {
	
	private static List<Event> events = new ArrayList<Event>(); 
	
	public static void execute() throws IOException, InterruptedException, ExecutionException, AddressException, MessagingException{
		Service service = ExportSearch.init();
		long startTime = System.currentTimeMillis();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		SearchTask searchTask = new SearchTask(service,"SYS_LOG_ALERT","deffered");
		System.out.println("A new task has been added for : " + searchTask.getAlertType());
		Future<String> searchFuture  = executor.submit(searchTask);

		while (true) {
            try {
                if(searchFuture.isDone()){
                	System.out.println("Tasks got completed");
                	System.out.println("EmailAlertExecutor - Terminating ThreadPoolExecutor");
                    executor.shutdown();
                    break;
                }
                 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		long endTime = System.currentTimeMillis();
		
		
		if(events != null && events.size() > 0){
			String file = writeToDisk(events);
			String subject = "Syslog alert notification";
			String message = "You have some incidents in syslog";

			// attachments
			String[] attachFiles = new String[1];
			attachFiles[0] = file;
			EmailSender.sendEmailWithAttachments(subject, message, attachFiles);
			System.out.println("Total time taken in seconds = " + (endTime-startTime)/1000 + " Events count=" + events.size());
			events.clear();
		}
		
	}
	
	public static String writeToDisk(List<Event> results) throws IOException{
		String part = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()); 
		String fileName = "C:/hackathon/results/sys_log_alert-" + part + ".txt";
		System.out.println("Filename=" + fileName);
		Writer output;
		String start = "<v xml:space=\"preserve\" trunc=\"0\">";
		String end = "</v>";
		String line = null;
		output = new BufferedWriter(new FileWriter(fileName,true));
		int cnt = 0;
		for(Event event : results ) {
			line = event.getSegmentedRaw().substring(event.getSegmentedRaw().indexOf(start)+start.length(), event.getSegmentedRaw().indexOf(end));
			//line = event.getSegmentedRaw().substring(event.getSegmentedRaw().lastIndexOf(start), event.getSegmentedRaw().indexOf(end));
			output.write(line + "\r\n" );
			cnt++;
			
			if(cnt == 20){
				break;
			}
		}
		
		output.close();
		System.out.println("Results written into file - " + fileName);
		return fileName;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, AddressException, MessagingException{
		long startTime = System.currentTimeMillis();
		SyslogAlertExecutor.execute();
		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken to process in seconds = " + (endTime-startTime)/1000);
	}

	public static void setEvents(List<Event> events) {
		SyslogAlertExecutor.events = events;
	}

}
