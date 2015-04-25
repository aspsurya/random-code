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

public class EventServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		Service service = ExportSearch.init();
		List<Event> events = ExportSearch.executeExportSearchByStatus(service);
		
		Map<String, String> statusMap = new HashMap<String, String>();
		/*data: [{
            period: '2010 Q1',
            iphone: 2666,
            ipad: null,
            itouch: 2647
        }, {
            period: '2010 Q2',
            iphone: 2778,
            ipad: 2294,
            itouch: 2441
        }*/
		
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