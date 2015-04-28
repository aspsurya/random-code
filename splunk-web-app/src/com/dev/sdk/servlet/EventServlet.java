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

		String mySearch = "search index=access_idx status=4*";

		if ("email".equalsIgnoreCase(request.getParameter("index"))) {
			mySearch = "search index=syslog_idx status = bounced";
		}
		mySearch += " latest=now earliest=-1y | stats count by date_year,date_month,date_mday,date_hour,date_minute | eval str_mon=lower(date_month) | eval sort_month=case(str_mon==\"april\",4, str_mon==\"match\",3, str_mon==\"february\",2, str_mon==\"january\",1) | sort by -date_year,-sort_month,-date_mday,-date_hour,-date_minute";
		
		if(request.getParameter("limit") != null){
			mySearch += " | head " + request.getParameter("limit");
		}

		List<Event> events = ExportSearch.run(service, mySearch);

		int max = events.size();
		
		if(request.getParameter("limit") != null){
			max = Integer.parseInt(request.getParameter("limit"));
		}
		
		JSONObject json = null;
		Map<Integer, List<List<Integer>>> barMap = new HashMap<Integer, List<List<Integer>>>();
		
		List<List<Integer>> records = new ArrayList<List<Integer>>(max);
		for (Event event : events) {
			List<Integer> record = new ArrayList<Integer>();
			record.add(--max);
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

		response.setContentType("text/plain");
		response.getWriter().println(json);
	}

	public static void main(String[] args) {

	}

}