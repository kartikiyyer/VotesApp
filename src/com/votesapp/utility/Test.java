package com.votesapp.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class Test {
	public static void main(String[] args) throws Exception{/*
		JSONObject result= new JSONObject();
		JSONObject jsonCreatePollValues= new JSONObject();
		jsonCreatePollValues.put("poll_question","this is poll by Aditya \t\t\t\t");
		jsonCreatePollValues.put("poll_options","[u,me,hum,tum,aap]");
		jsonCreatePollValues.put("poll_create_date","19-04-2014");
		jsonCreatePollValues.put("poll_end_date","21-04-2014");
		jsonCreatePollValues.put("poll_creator","  (4  08)4 ...@@#$%^&*()~`55- == 3613  ");
		jsonCreatePollValues.put("poll_category","timepass");
		jsonCreatePollValues.put("poll_groupid","[\"roomies\"]");
		List lst=new ArrayList();
		lst.add("");
		lst.add("");
		lst.add("");
		jsonCreatePollValues.put("poll_participants",lst);
		if(jsonCreatePollValues.get("poll_participants").toString().isEmpty()){
			System.out.println("ITS EMPTY!!!!!!!!!!");
		}
//		String newval=jsonCreatePollValues.getString("poll_creator").replaceAll("[\\)\\-\\( ]", "");
		jsonCreatePollValues.put(("poll_creator"),jsonCreatePollValues.getString("poll_creator").replaceAll("[\\p{P}\\p{S} ]", ""));
//		jsonCreatePollValues.put("poll_participants",jsonCreatePollValues.getString("poll_participants").toString().replaceAll("[\\)\\-\\( ]", ""));
//				System.out.println(newval);
		jsonCreatePollValues.put(("poll_question"),jsonCreatePollValues.getString("poll_question").replaceAll("[\\t]", ""));
		JSONArray partici = (JSONArray) jsonCreatePollValues.get("poll_participants");
		JSONArray res=new JSONArray();
		for(int i=0;i<partici.length();i++){
//			System.out.println(partici.get(i).toString().replaceAll("[\\p{P}\\p{S} ]", ""));
			res.put(partici.get(i).toString().replaceAll("[^0-9]", ""));
		}
		jsonCreatePollValues.put("poll_participants", res);
		
		
		System.out.println(jsonCreatePollValues);
	*/
		
	
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
	
	}
}
