package com.dev.sdk.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropUtil {

	public String setProp(String key, String value) throws IOException {

		Properties props = new Properties();
		props.putAll(this.getAllProps());
		props.setProperty(key, value);

		FileOutputStream out = new FileOutputStream(
				"C:/hackathon/email.properties");
		props.store(out, null);
		out.close();
		return value;
	}

	public String getProp(String key) throws IOException {

		Properties props = new Properties();
		FileInputStream in = new FileInputStream(
				"C:/hackathon/email.properties");
		props.load(in);
		String value = props.getProperty(key);
		in.close();
		return value;
	}

	public Map<String, String> getAllProps() throws IOException {

		Map<String, String> propMap = new HashMap<String, String>();

		Properties props = new Properties();
		FileInputStream in = new FileInputStream(
				"C:/hackathon/email.properties");
		props.load(in);

		for (Object tmp : props.keySet()) {
			String k = (String) tmp;
			propMap.put(k, props.getProperty(k));
		}
		in.close();
		return propMap;
	}
	
	public static void setJobProp() throws IOException{
		Map<String, String> propMap = new HashMap<String, String>();
		Properties props = new Properties();
		FileInputStream in = new FileInputStream(
				"C:/hackathon/job.properties");
		props.load(in);

		for (Object tmp : props.keySet()) {
			String k = (String) tmp;
			propMap.put(k, props.getProperty(k));
		}
		in.close();
		
		FileOutputStream out = new FileOutputStream(
				"C:/hackathon/job.properties");
		props.putAll(propMap);
		props.setProperty("FLAG", "ON");
		
		props.store(out, null);
		out.close();
	}

}