
package com.google.android.gcm.demo.server;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public final class Datastore {

	private static final List<String> regIds = new ArrayList<String>();
	private static final Logger logger =
			Logger.getLogger(Datastore.class.getName());
	private static String ipaddress="ds053838.mongolab.com";

	private Datastore() {
		throw new UnsupportedOperationException();
	}

	public static void register(String username,String regId) {
		logger.info("Registering " + regId);
		try {
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("gcm_entry_details");
				BasicDBObject document = new BasicDBObject();
				document.put("username",username);
				document.put("regid",regId);
				java.util.Date date= new java.util.Date();
				document.put("timestamp",new Timestamp(date.getTime()));
				table.insert(document);
			}else{
				System.out.println("can not connect");
			}
		}
		catch(MongoException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}

		regIds.add(regId);

	}

	public static void unregister(String regId) {
		logger.info("Unregistering " + regId);
		try {
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("gcm_entry_details");
				BasicDBObject document = new BasicDBObject();
				document.put("regid",regId);
				table.remove(document);
			}else{
				System.out.println("can not connect");
			}
		}
		catch(MongoException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		synchronized (regIds) {
			regIds.remove(regId);
		}
	}

	public static void updateRegistration(String oldId, String newId) {
		logger.info("Updating " + oldId + " to " + newId);

		try {
			
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("gcm_entry_details");
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set", new BasicDBObject().append("regid", newId));
				BasicDBObject searchQuery = new BasicDBObject().append("regid", oldId);
				table.update(searchQuery, newDocument);
			}else{
				System.out.println("can not connect");
			}
		}
		catch(MongoException  se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		synchronized (regIds) {
			regIds.remove(oldId);
			regIds.add(newId);
		}
	}


	public static List<String> getDevices() {

		ArrayList<String> regIdList = new ArrayList<String>();
		try {
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("gcm_entry_details");
				DBCursor cursor=table.find();
				DBObject getdata;
				while(cursor.hasNext()){
					getdata=cursor.next();
					regIdList.add(getdata.get("regid").toString());
				}
				
			}else{
				System.out.println("can not connect");
			}
		}
		catch(MongoException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return regIdList;
	}

	
}


