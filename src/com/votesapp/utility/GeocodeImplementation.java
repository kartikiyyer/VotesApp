package com.votesapp.utility;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils; //(For this add "commons-io-1.3.1.jar" in the project.)
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class GeocodeImplementation {

	private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json"; 

	public static void main(String[] args)throws Exception{
		GeocodeImplementation gl=new GeocodeImplementation();
		String val=gl.getJSONByGoogle("57.3331002,-121.9116811");
		/*JSONObject job=new JSONObject(val);
		JSONArray jarr=(JSONArray) job.get("results");
		job=new JSONObject(jarr.get(0).toString());
		JSONArray jarr1=(JSONArray)job.get("address_components");
		job=new JSONObject(jarr1.get(3).toString());
		System.out.println(job.get("short_name"));*/
		System.out.println(val);
	}
	
	public String getJSONByGoogle(String fullAddress) throws MalformedURLException{
		ByteArrayOutputStream output=null;
		JSONObject job=null;
		String city=null;
		try{
			URL url = new URL(URL + "?latlng=" + URLEncoder.encode(fullAddress, "UTF-8")+ "&sensor=false");

			URLConnection conn = url.openConnection();

			output = new ByteArrayOutputStream(1024);

			IOUtils.copy(conn.getInputStream(), output);

			output.close();
			job=new JSONObject(output.toString());
			JSONArray jarr=(JSONArray) job.get("results");
			job=new JSONObject(jarr.get(0).toString());
			JSONArray jarr1=(JSONArray)job.get("address_components");
			job=new JSONObject(jarr1.get(3).toString());
//			System.out.println(job.get("short_name"));
			city=job.get("long_name").toString();

		}catch(Exception e){
			e.printStackTrace();
		}
		return city;
	}
}
