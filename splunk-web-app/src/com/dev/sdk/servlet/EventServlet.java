package com.dev.sdk.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.dev.sdk.splunk.ExportSearch;
import com.splunk.Event;
import com.splunk.Service;

public class EventServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		Service service = ExportSearch.init();
		List<Event> events = ExportSearch.executeExportSearchByStatusPerDay(service);

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
			//System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.setContentType("text/plain");
		response.getWriter().println(json);
	}
	
	
	public static void main(String[] args){
		
		
	}

}