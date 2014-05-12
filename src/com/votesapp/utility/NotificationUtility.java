package com.votesapp.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.google.android.gcm.demo.server.Datastore;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class NotificationUtility {
	private String ipaddress="ds053838.mongolab.com";

	  private static final int MULTICAST_SIZE = 1000;
	  
	  private Sender sender;
	  private static final Executor threadPool = Executors.newFixedThreadPool(5);
	  protected final Logger logger = Logger.getLogger(getClass().getName());

	public static void main(String[] args) {

	}

	public String sendNotification(JSONObject jsonCreatePollValues)throws Exception{
		JSONObject result= new JSONObject();
		jsonCreatePollValues.get("poll_groupid");
		jsonCreatePollValues.get("poll_participants");
		jsonCreatePollValues.get("poll_creator");
		try { 
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				
				JSONArray resultJson=new JSONArray();
				resultJson=(JSONArray)jsonCreatePollValues.get("poll_groupid");
				List list=new ArrayList();
				for(int i=0;i<resultJson.length();i++){
					list.add(resultJson.get(i));
				}
				DBCollection table = db.getCollection("group");
				BasicDBObject whereQuery = new BasicDBObject();
				
				whereQuery.put("name", new BasicDBObject("$in", list));
				DBCursor cursor=table.find(whereQuery);
				DBObject getdata;
				list=new ArrayList();
				BasicDBList bdl;
				while(cursor.hasNext()){
					getdata=cursor.next();
					bdl=(BasicDBList)getdata.get("members");
					System.out.println("bdl:  "+bdl);
					for(int j=0;j<bdl.size();j++){
						list.add(bdl.get(j));
					}
				}
				System.out.println("list after grp members: "+list);
				
				resultJson=new JSONArray();
				resultJson=(JSONArray)jsonCreatePollValues.get("poll_participants");
				for(int i=0;i<resultJson.length();i++){
					list.add(resultJson.get(i).toString());
				}
				System.out.println("list after participants: "+list); 
				
				result.put("poll_creator", jsonCreatePollValues.get("poll_creator"));
				result.put("poll_receivers", list);
				List<String> regIds=getGcmRegIdList(list);
				System.out.println("b4 assembleMessage...");
				try {
				    URL myURL = new URL("http://votesapp.elasticbeanstalk.com/sendAll?");
				    HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();
				    myURLConnection.setRequestMethod("POST");
				   // myURLConnection.connect();
				   //myURLConnection.setDoOutput(true);
				    String urlParameters = "senderNo="+jsonCreatePollValues.get("poll_creator").toString()+"&regIds="+regIds.toString();
				    
					// Send post request
				    myURLConnection.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(myURLConnection.getOutputStream());
					wr.writeBytes(urlParameters);
					wr.flush();
					wr.close();
				    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                            		myURLConnection.getInputStream()));
				} 
				catch (MalformedURLException e) { 
				    // new URL() failed
				    // ...
					e.printStackTrace();
				} 
				catch (IOException e) {   
				    // openConnection() failed
				    // ...
					e.printStackTrace();
				}
				//assembleMessage(jsonCreatePollValues.get("poll_creator").toString(), regIds);
				result.put("Msg", "success");
			}else{
				System.out.println("can not connect");
				result.put("Msg", "fail");
			}

		}

		catch (MongoException me) { 
			result.put("Msg", "fail");
			me.printStackTrace(); 
		}
		System.out.println("result--> "+result);
		return result.toString();
	}
	
	private List<String> getGcmRegIdList(List list)throws Exception{
		List<String> regList=new ArrayList<String>();
		try { 
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("gcm_entry_details");
				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("username", new BasicDBObject("$in", list));
				DBCursor cursor=table.find(whereQuery);
				DBObject getdata;
				while(cursor.hasNext()){
					getdata=cursor.next();
					regList.add(getdata.get("regid").toString());
				}
				
			}else{
				System.out.println("can not connect");
			}

		}
		catch (MongoException me) { 
			me.printStackTrace(); 
		}
		System.out.println("getGcmRegIdList regList: "+regList);
		return regList;
	}
	
	private void assembleMessage(String noOfSender, List<String> regIds){

	    List<String> devices = Datastore.getDevices();
	    String status;
	    if (devices.isEmpty()) {
	      status = "Message ignored as there is no device registered!";
	    } else {
	      // NOTE: check below is for demonstration purposes; a real application
	      // could always send a multicast, even for just one recipient
	      if (devices.size() == 1) {
	        // send a single message using plain post
	        String registrationId = devices.get(0);
	        Message message = new Message.Builder().addData("1","abc").build();
	        Result result = null;
	        try{
	         result = sender.send(message, registrationId, 5);}
	        catch (IOException e){
	        	e.printStackTrace();
	        }
	        if(result!=null)
	        status = "Sent message to one device: " + result;
	        else {
	        	status = "Result was null";
	        }
	      } else {
	        // send a multicast message using JSON
	        // must split in chunks of 1000 devices (GCM limit)
	        int total = devices.size();
	        List<String> partialDevices = new ArrayList<String>(total);
	        int counter = 0;
	        int tasks = 0;
	        for (String device : devices) {
	          counter++;
	          partialDevices.add(device);
	          int partialSize = partialDevices.size();
	          if (partialSize == MULTICAST_SIZE || counter == total) {
	            asyncSend(partialDevices);
	            partialDevices.clear();
	            tasks++;
	          }
	        }
	        status = "Asynchronously sending " + tasks + " multicast messages to " +
	            total + " devices";
	      }
	    }
	/*    req.setAttribute(HomeServlet.ATTRIBUTE_STATUS, status.toString());
	    getServletContext().getRequestDispatcher("/home").forward(req, resp);*/
	  
		
	}
	  private void asyncSend(List<String> partialDevices) {
		    // make a copy
		    final List<String> devices = new ArrayList<String>(partialDevices);
		    threadPool.execute(new Runnable() {

		      public void run() {
		        Message message = new Message.Builder().build();
		        MulticastResult multicastResult;
		        try {
		          multicastResult = sender.send(message, devices, 5);
		        } catch (IOException e) {
		          logger.log(Level.SEVERE, "Error posting messages", e);
		          return;
		        }
		        List<Result> results = multicastResult.getResults();
		        // analyze the results
		        for (int i = 0; i < devices.size(); i++) {
		          String regId = devices.get(i);
		          Result result = results.get(i);
		          String messageId = result.getMessageId();
		          if (messageId != null) {
		            logger.fine("Succesfully sent message to device: " + regId +
		                "; messageId = " + messageId);
		            String canonicalRegId = result.getCanonicalRegistrationId();
		            if (canonicalRegId != null) {
		              // same device has more than on registration id: update it
		              logger.info("canonicalRegId " + canonicalRegId);
		              Datastore.updateRegistration(regId, canonicalRegId);
		            }
		          } else {
		            String error = result.getErrorCodeName();
		            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
		              // application has been removed from device - unregister it
		              logger.info("Unregistered device: " + regId);
		              Datastore.unregister(regId);
		            } else {
		              logger.severe("Error sending message to " + regId + ": " + error);
		            }
		          }
		        }
		      }});
		  }
}
