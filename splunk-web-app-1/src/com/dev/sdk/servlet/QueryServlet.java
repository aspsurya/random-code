package com.dev.sdk.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.dev.sdk.splunk.ExportSearch;
import com.dev.sdk.util.ErrorCodes;
import com.splunk.Event;
import com.splunk.Service;

public class QueryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		Service service = ExportSearch.init();
		List<Event> events = ExportSearch.executeExportSearchByStatus(service);
		
		Map<String, String> statusMap = new HashMap<String, String>();
		
		
		for (Event event : events) {
			for (String key : event.keySet()) {
				statusMap.put(ErrorCodes.getCodeText(Integer.parseInt(event.get("status"))), event.get("count"));
			}
		}

		JSONObject json = new JSONObject(statusMap);
		response.setContentType("text/plain");
		response.getWriter().println(json);
	}

}