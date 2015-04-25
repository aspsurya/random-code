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

public class EventsCountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		Service service = ExportSearch.init();
		
        //String mySearch = "search index=access_idx | stats count by date_year,date_month,date_mday,status";
		String cond = "";
		if("critical".equalsIgnoreCase(request.getParameter("type"))){
			cond  = "status=4* ";
		}
		
        String mySearch = "search index=access_idx " + cond + "latest=now earliest=-3d| stats count by date_year,date_month,date_mday | sort -date_year,-date_month, -date_mday";
		
		List<Event> events = ExportSearch.run(service, mySearch);
		
		JSONObject json = null;
		
		Map<Integer, List<Integer>> eventsMap = new HashMap<Integer, List<Integer>>();
		List<Integer> records = new ArrayList<Integer>();
		
		for (Event event : events) {
			records.add(Integer.parseInt(event.get("count")));
		}
		
		eventsMap.put(0, records);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String str = mapper.writeValueAsString(eventsMap);
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