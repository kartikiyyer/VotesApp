package com.votesapp.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

public class Test {
	public static void main(String args[]) throws Exception{
		JSONObject obj = new JSONObject();
		List<String> sList = new ArrayList<String>();
		sList.add("val1");
		sList.add("val2");

		obj.put("list", sList);
		System.out.println(obj);
	}
}
