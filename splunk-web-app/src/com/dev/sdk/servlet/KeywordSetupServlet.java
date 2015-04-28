package com.dev.sdk.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.dev.sdk.splunk.ExportSearch;
import com.dev.sdk.util.PropUtil;

public class KeywordSetupServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String pathname = "C:/hackathon/job.properties";

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		JSONObject json = null;
		Map<String, String> respMap = new HashMap<String, String>();

		String action = request.getParameter("action");

		if ("get".equalsIgnoreCase(action)) {

			String query1 = request.getParameter("type1");
			String query2 = request.getParameter("type2");
			System.out.println("param1 => " + query1);
			System.out.println("param2 => " + query2);

			if (query1 != null) {
				respMap.put(query1, ExportSearch.getKeywords(query1));
			}

			if (query2 != null) {
				respMap.put(query2, ExportSearch.getKeywords(query2));
			}
			
			
			Properties prop = new Properties();

			FileInputStream inStream = new FileInputStream(new File(pathname));
			prop.load(inStream);
			
			respMap.put("alert", prop.getProperty("FLAG"));
			inStream.close();
			
			json = new JSONObject(respMap);

		} else if ("post".equalsIgnoreCase(action)) {
			/*
			String logType = request.getParameter("logType");

			String key = request.getParameter("key");
			String value = request.getParameter("value");

			System.out.println("logType= " + logType + "  key=" + key
					+ " value=" + value);
			Properties properties = new Properties();

			FileInputStream inStream = new FileInputStream(new File(pathname));
			properties.load(inStream);

			if ("accesslog".equalsIgnoreCase(logType)) {
				properties.setProperty(key, value); // accesslog_keyword=400,403,404
			} else if ("syslog".equalsIgnoreCase(logType)) {
				properties.setProperty(key, value); // syslog_keyword=deferred,bounced
			} else {

			}

			FileOutputStream out = new FileOutputStream(pathname);
			properties.store(out, null);
			out.close();
			*/
			PropUtil.setJobProp();

			json = new JSONObject(respMap);
		}

		response.setContentType("text/plain");
		response.getWriter().println(json);

	}
}
