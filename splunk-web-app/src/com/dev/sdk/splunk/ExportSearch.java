package com.dev.sdk.splunk;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.dev.sdk.util.PropUtil;
import com.splunk.Event;
import com.splunk.JobExportArgs;
import com.splunk.JobResultsPreviewArgs;
import com.splunk.MultiResultsReaderJson;
import com.splunk.MultiResultsReaderXml;
import com.splunk.SearchResults;
import com.splunk.Service;
import com.splunk.ServiceArgs;

/**
 * An export search is the most reliable way to return a large set of results
 * because exporting returns results in a stream, rather than as a search job
 * that is saved on the server. So the server-side limitations to the number of
 * results that can be returned don't apply to export searches. You can run an
 * export search in normal and real-time modes, and running the search is
 * similar to running a regular search. However, displaying the results of an
 * export search is a little tricky because they require different parsing
 **/
public class ExportSearch {

	public static void main(String[] args) throws IOException,
			InterruptedException {

		ServiceArgs loginArgs = new ServiceArgs();
		loginArgs.setUsername("admin");
		loginArgs.setPassword("snapdev123");
		loginArgs.setHost("113.128.162.95");
		loginArgs.setPort(8089);

		// Create a Service instance and log in with the argument map
		Service service = Service.connect(loginArgs);
		System.out.println("Connected to Splunk - Host:" + service.getHost()
				+ " Port:" + service.getPort());
		// ExportSearch.runNormalExportSearchWithPreview(service); //Working
		// ExportSearch.runNormalExportSearchWithPreviewJavaStream(service);
		// ExportSearch.executeExportSearch(service,"ACCESS_LOG_ALERT","400");
		// ExportSearch.executeExportSearchByStatus(service);
		ExportSearch.debug(service);

	}

	public static Service init() {
		ServiceArgs loginArgs = new ServiceArgs();
		loginArgs.setUsername("admin");
		loginArgs.setPassword("snapdev123");
		loginArgs.setHost("113.128.162.95");
		loginArgs.setPort(8089);

		// Create a Service instance and log in with the argument map
		Service service = Service.connect(loginArgs);
		// System.out.println("\nConnected to Hunk - Host:" + service.getHost()
		// + " Port:" + service.getPort());
		return service;
	}

	/**
	 * Run a normal-mode export search and display previews from a normal export
	 * search using MultiResultsReaderXml class We can also index over the last
	 * hour and display the output - using setEarliestTime and setLatestTime
	 * This search is useful when you are performing a reporting (transforming)
	 * search on a large data set and you want to view previews while the search
	 * runs.
	 * 
	 * @param service
	 * @throws IOException
	 */
	private static void runNormalExportSearchWithPreview(Service service)
			throws IOException {
		// Create an argument map for the export arguments
		JobExportArgs exportArgs = new JobExportArgs();
		exportArgs.setSearchMode(JobExportArgs.SearchMode.NORMAL);
		exportArgs.setOutputMode(JobExportArgs.OutputMode.XML);

		JobResultsPreviewArgs previewargs = new JobResultsPreviewArgs();
		previewargs.setCount(1000); // Get 500 previews at a time

		exportArgs.setEarliestTime("-3years"); // Working -720h, -3years
		exportArgs.setLatestTime("now"); // Working

		// String mySearch = "search index=main FAIL*";
		String mySearch = "search index=access_idx sourcetype=access_common";
		// String mySearch =
		// "search index=access_idx sourcetype=access_common | stats count by date_year,date_month,date_mday,date_hour,date_minute,status";
		// String mySearch =
		// "search index=access_idx | stats count by date_year,date_month,date_mday,date_hour,date_minute,status";
		InputStream exportSearch = service.export(mySearch, exportArgs);
		displayResultWithMultiResultsReaderXml(exportSearch);
		// displayResultWithMultiResultsReaderJson(exportSearch);
	}

	private static void displayResultWithMultiResultsReaderXml(
			InputStream exportSearch) throws IOException {
		// Display results using the SDK's multi-results reader for XML
		MultiResultsReaderXml multiResultsReader = new MultiResultsReaderXml(
				exportSearch);

		int counterSet = 0;
		for (SearchResults searchResults : multiResultsReader) {
			String resultSetType = searchResults.isPreview() ? "Preview"
					: "Final";
			System.out.println("\n******** ResultSetType=" + resultSetType
					+ " counterSet=" + counterSet++ + " ********");
			int counter = 1; // count the number of events
			for (Event event : searchResults) {
				System.out.println("\n***** Event " + counter++ + " *****"
						+ event.getSegmentedRaw());
				for (String key : event.keySet()) {
					System.out.println("   " + key + ":  " + event.get(key));
				}
			}
		}

		multiResultsReader.close();
	}

	private static void displayResultWithMultiResultsReaderJson(
			InputStream exportSearch) throws IOException {
		// Display results using the SDK's multi-results reader for XML
		MultiResultsReaderJson multiResultsReader = new MultiResultsReaderJson(
				exportSearch);

		int counterSet = 0;
		for (SearchResults searchResults : multiResultsReader) {
			String resultSetType = searchResults.isPreview() ? "Preview"
					: "Final";
			System.out.println("\n******** ResultSetType=" + resultSetType
					+ " counterSet=" + counterSet++ + " ********");
			int counter = 1; // count the number of events
			for (Event event : searchResults) {
				System.out.println("\n***** Event " + counter++
						+ "  counterSet=" + counterSet + " *****");
				for (String key : event.keySet()) {
					System.out.println("   " + key + ":  " + event.get(key));
				}
			}
		}

		multiResultsReader.close();
	}

	private static void displayEvents(List<Event> events) {
		int counter = 0; // count the number of events
		for (Event event : events) {
			System.out.println("\n***** Event " + counter++ + " *****");
			for (String key : event.keySet()) {
				System.out.println("   " + key + " :  " + event.get(key));
			}
		}
	}

	private static void generateJSON(List<Event> events) {
		/*
		 * Map<String,String> statusMap = new HashMap<String,String>(); for
		 * (Event event : events) { for (String key : event.keySet()) {
		 * System.out.println("   " + key + " :  " + event.get(key));
		 * statusMap.put(event.get("status"),event.get("count")); } }
		 * 
		 * JSONObject json=new JSONObject(statusMap); System.out.println(json);
		 */

		/*
		 * JSONObject json = null; Map<Integer, List<Map<String, String>>>
		 * barMap = new HashMap<Integer, List<Map<String, String>>>();
		 * List<Map<String, String>> records = new ArrayList<Map<String,
		 * String>>(); for (Event event : events) { Map<String, String> record =
		 * new HashMap<String, String>(); record.put("period",
		 * event.get("date_year") + " " + event.get("date_month") + " " +
		 * event.get("date_mday")); record.put("events", event.get("count"));
		 * records.add(record); }
		 * 
		 * barMap.put(0, records); ObjectMapper mapper = new ObjectMapper(); try
		 * { String str = mapper.writeValueAsString(barMap); json = new
		 * JSONObject(str); System.out.println(json); } catch
		 * (JsonGenerationException e) { e.printStackTrace(); } catch
		 * (JsonMappingException e) { e.printStackTrace(); } catch (IOException
		 * e) { e.printStackTrace(); } catch (ParseException e) {
		 * e.printStackTrace(); }
		 */

		JSONObject json = null;
		Map<Integer, List<List<Integer>>> barMap = new HashMap<Integer, List<List<Integer>>>();
		List<List<Integer>> records = new ArrayList<List<Integer>>();
		int idx = 0;
		for (Event event : events) {
			List<Integer> record = new ArrayList<Integer>();
			record.add(idx++);
			record.add(Integer.parseInt(event.get("count")));
			records.add(record);
		}

		barMap.put(0, records);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String str = mapper.writeValueAsString(barMap);
			json = new JSONObject(str);
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static List<Event> executeExportSearchByStatus(Service service)
			throws IOException {
		List<Event> events = new ArrayList<Event>();
		JobExportArgs exportArgs = new JobExportArgs();
		exportArgs.setSearchMode(JobExportArgs.SearchMode.NORMAL);
		exportArgs.setOutputMode(JobExportArgs.OutputMode.XML);

		JobResultsPreviewArgs previewargs = new JobResultsPreviewArgs();
		previewargs.setCount(1000);

		exportArgs.setEarliestTime("-3years"); // Working -720h, -3years
		exportArgs.setLatestTime("now");

		String mySearch = "search index=access_idx sourcetype=access_common | stats count by status";
		InputStream exportSearch = service.export(mySearch, exportArgs);

		MultiResultsReaderXml multiResultsReader = new MultiResultsReaderXml(
				exportSearch);
		for (SearchResults searchResults : multiResultsReader) {
			for (Event event : searchResults) {
				events.add(event);
			}
		}

		multiResultsReader.close();
		System.out.println(events.size());

		// generateJSON(events);
		return events;
	}

	public static List<Event> executeExportSearchByStatusPerDay(Service service)
			throws IOException {
		List<Event> events = new ArrayList<Event>();
		JobExportArgs exportArgs = new JobExportArgs();
		exportArgs.setSearchMode(JobExportArgs.SearchMode.NORMAL);
		exportArgs.setOutputMode(JobExportArgs.OutputMode.XML);

		JobResultsPreviewArgs previewargs = new JobResultsPreviewArgs();
		previewargs.setCount(1000);

		exportArgs.setEarliestTime("-3years"); // Working -720h, -3years
		exportArgs.setLatestTime("now");

		String mySearch = "search index=access_idx | stats count by date_year,date_month,date_mday,status";
		// String mySearch =
		// "search index=access_idx status=4* | stats count by date_year,date_month,date_mday,date_hour,date_minute";
		// String mySearch =
		// "search index=syslog_idx status=bounced latest=now earliest=-30d | stats count by date_year,date_month,date_mday,date_hour,date_minute";
		// String mySearch =
		// "search index=access_idx status=4* latest=now earliest=-30d | stats count by date_year,date_month,date_mday,date_hour,date_minute";

		InputStream exportSearch = service.export(mySearch, exportArgs);

		MultiResultsReaderXml multiResultsReader = new MultiResultsReaderXml(
				exportSearch);
		for (SearchResults searchResults : multiResultsReader) {
			for (Event event : searchResults) {
				events.add(event);
			}
		}

		multiResultsReader.close();
		System.out.println(events.size());
		// generateJSON(events);
		// displayEvents(events);
		return events;
	}

	public static List<Event> run(Service service, String mySearch)
			throws IOException {
		List<Event> events = new ArrayList<Event>();
		JobExportArgs exportArgs = new JobExportArgs();
		exportArgs.setSearchMode(JobExportArgs.SearchMode.NORMAL);
		exportArgs.setOutputMode(JobExportArgs.OutputMode.XML);

		JobResultsPreviewArgs previewargs = new JobResultsPreviewArgs();
		previewargs.setCount(1000);

		exportArgs.setEarliestTime("-3years"); // Working -720h, -3years
		exportArgs.setLatestTime("now");

		InputStream exportSearch = service.export(mySearch, exportArgs);

		MultiResultsReaderXml multiResultsReader = new MultiResultsReaderXml(
				exportSearch);
		for (SearchResults searchResults : multiResultsReader) {
			for (Event event : searchResults) {
				events.add(event);
			}
		}

		multiResultsReader.close();
		System.out.println("Query events => " + events.size());

		// generateJSON(events);
		// displayEvents(events);
		return events;
	}

	public static List<Event> debug(Service service) throws IOException {
		
		
		String key = "access";
		String value = "400,404";
		
		String res = "";
		
		PropUtil pUtil = new PropUtil();
		try {
			if ("access".equalsIgnoreCase(key)) {
				res = pUtil.setProp("accesslog_keyword", value);
			} else if ("email".equalsIgnoreCase(key)) {
				res = pUtil.setProp("syslog_keyword", value);
			}
		} catch (Exception e) {
		}
		
		System.out.print("res " +res);
		System.exit(1);
		List<Event> events = new ArrayList<Event>();
		JobExportArgs exportArgs = new JobExportArgs();
		exportArgs.setSearchMode(JobExportArgs.SearchMode.NORMAL);
		exportArgs.setOutputMode(JobExportArgs.OutputMode.XML);

		JobResultsPreviewArgs previewargs = new JobResultsPreviewArgs();
		previewargs.setCount(1000);

		exportArgs.setEarliestTime("-3years"); // Working -720h, -3years
		exportArgs.setLatestTime("now");

		// String mySearch =
		// "search index=access_idx | stats count by date_year,date_month,date_mday,status";
		// String mySearch =
		// "search index=access_idx status=4* | stats count by date_year,date_month,date_mday,date_hour,date_minute";
		String mySearch = "search index=syslog_idx status=bounced latest=now earliest=-30d | stats count by date_year,date_month,date_mday,date_hour,date_minute";
		// String mySearch =
		// "search index=access_idx status=4* latest=now earliest=-30d | stats count by date_year,date_month,date_mday,date_hour,date_minute";

		InputStream exportSearch = service.export(mySearch, exportArgs);

		MultiResultsReaderXml multiResultsReader = new MultiResultsReaderXml(
				exportSearch);
		for (SearchResults searchResults : multiResultsReader) {
			for (Event event : searchResults) {
				events.add(event);
			}
		}

		multiResultsReader.close();
		System.out.println("Query events => " + events.size());

		JSONObject json = null;
		Map<Integer, List<List<Integer>>> barMap = new HashMap<Integer, List<List<Integer>>>();
		List<List<Integer>> records = new ArrayList<List<Integer>>();
		int idx = 0;
		for (Event event : events) {
			List<Integer> record = new ArrayList<Integer>();
			record.add(idx++);
			record.add(Integer.parseInt(event.get("count")));
			records.add(record);
		}

		barMap.put(0, records);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String str = mapper.writeValueAsString(barMap);
			json = new JSONObject(str);
			// System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// generateJSON(events);
		// displayEvents(events);
		return events;
	}

	public static List<Event> executeExportSearch(Service service,
			String alertType, String keyword) throws IOException {
		List<Event> events = new ArrayList<Event>();
		JobExportArgs exportArgs = new JobExportArgs();
		exportArgs.setSearchMode(JobExportArgs.SearchMode.NORMAL);
		exportArgs.setOutputMode(JobExportArgs.OutputMode.XML);

		JobResultsPreviewArgs previewargs = new JobResultsPreviewArgs();
		previewargs.setCount(1000);

		exportArgs.setEarliestTime("-24h"); // Working -720h, -3years
		exportArgs.setLatestTime("now");

		String mySearch = "search index=access_idx sourcetype=access_common status=400 OR status=403 OR status=404";
		InputStream exportSearch = service.export(mySearch, exportArgs);

		MultiResultsReaderXml multiResultsReader = new MultiResultsReaderXml(
				exportSearch);
		for (SearchResults searchResults : multiResultsReader) {
			String resultSetType = searchResults.isPreview() ? "Preview"
					: "Final";
			for (Event event : searchResults) {
				events.add(event);
			}
		}

		multiResultsReader.close();
		System.out.println(events.size());
		return events;
	}

	public static List<Event> executeExportSearchSyslog(Service service,
			String alertType, String keyword) throws IOException {
		List<Event> events = new ArrayList<Event>();
		JobExportArgs exportArgs = new JobExportArgs();
		exportArgs.setSearchMode(JobExportArgs.SearchMode.NORMAL);
		exportArgs.setOutputMode(JobExportArgs.OutputMode.XML);

		JobResultsPreviewArgs previewargs = new JobResultsPreviewArgs();
		previewargs.setCount(1000);

		exportArgs.setEarliestTime("-24h"); // Last 30 days
		exportArgs.setLatestTime("now");

		String mySearch = "search index=syslog_idx status=deferred OR status=bounced";
		InputStream exportSearch = service.export(mySearch, exportArgs);

		MultiResultsReaderXml multiResultsReader = new MultiResultsReaderXml(
				exportSearch);
		for (SearchResults searchResults : multiResultsReader) {
			String resultSetType = searchResults.isPreview() ? "Preview"
					: "Final";
			for (Event event : searchResults) {
				events.add(event);
			}
		}

		multiResultsReader.close();
		System.out.println(events.size());
		return events;
	}

	public static String getKeywords(String key) {

		PropUtil pUtil = new PropUtil();
		try {
			if ("access".equalsIgnoreCase(key)) {
				return pUtil.getProp("accesslog_keywords");
			} else if ("email".equalsIgnoreCase(key)) {
				return pUtil.getProp("syslog_keywords");
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	public static String setKeywords(String key, String value) throws IOException {

		PropUtil pUtil = new PropUtil();
		try {
			if ("access".equalsIgnoreCase(key)) {
				return pUtil.setProp("accesslog_keywords", value);
			} else if ("email".equalsIgnoreCase(key)) {
				return pUtil.setProp("syslog_keywords", value);
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}
}
