package com.votesapp;

import java.net.UnknownHostException;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

@Path("user")
public class User {
	
	DB db;
	
	public User() {
		MongoClient mongo;
		try {
			mongo = new MongoClient( "ds053838.mongolab.com" , 53838 );
			db = mongo.getDB("votesapp");
	        db.authenticate("admin", "password@123".toCharArray());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@GET
    @Path("/groups")
	public String showGroups(@QueryParam("phone_number") String phoneNumber) throws JSONException{
		System.out.println("-----------Inside showGroups()--------------");
		JSONObject jphoneNumber = new JSONObject(phoneNumber);
		JSONObject jgroups = new JSONObject();
		try { 		
		    System.out.println("Fetching data");
	        DBCollection groupCollection = db.getCollection("group");
	        DBObject dbGroup = new BasicDBObject();
	        System.out.println(jphoneNumber.get("phone_number"));
	        dbGroup.put("phone_number",jphoneNumber.get("phone_number"));
	        DBCursor dbCursor = groupCollection.find(dbGroup);
	        System.out.println("Data fetched");
	        
	        while(dbCursor.hasNext()) {
	        	DBObject dbRow = dbCursor.next();
	        	System.out.println(dbRow);
	        	jgroups.append("groups", new JSONObject(JSON.serialize(dbRow)));
	        }
	        System.out.println(jgroups);
	      } catch (Exception e) { 
	    	  e.printStackTrace(); 
	      }
		System.out.println("---------------Exiting showGroups()---------------");
		return jgroups.toString();
    }
	
	@POST
    @Path("/group")
	public void createGroup(@FormParam("group") String group) throws JSONException{
		System.out.println("-----------Inside createGroup()----------");
		JSONObject jgroup = new JSONObject(group);
		System.out.println("Group: " + jgroup);
		try { 		
		    System.out.println("Inserting data");
	        DBCollection groupCollection = db.getCollection("group");
	        BasicDBObject dbGroup = new BasicDBObject();
	        dbGroup.putAll((DBObject)JSON.parse(jgroup.toString()));
	        groupCollection.insert(dbGroup);
	        System.out.println("Data inserted");
	      } catch (Exception e) { 
	    	  e.printStackTrace(); 
	      }
		System.out.println("-----------Exiting createGroup()-------------");
    }
	
	@PUT
    @Path("/group")
	public void editGroup(@FormParam("group") String group) throws JSONException{
		System.out.println("-----------Inside editGroup()----------");
		JSONObject jgroup = new JSONObject(group);
		System.out.println("Group: " + jgroup);
		try { 		
		    System.out.println("Updating data");
	        DBCollection groupCollection = db.getCollection("group");
	        BasicDBObject dbGroup = new BasicDBObject();
	        BasicDBObject dbWhere = new BasicDBObject();
	        dbGroup.putAll((DBObject)JSON.parse(jgroup.toString()));
	        dbWhere.put("_id", new ObjectId(jgroup.getString("_id")));
	        groupCollection.update(dbWhere, dbGroup);
	        System.out.println("Data updated");
	      } catch (Exception e) { 
	    	  e.printStackTrace(); 
	      }
		System.out.println("-----------Exiting editGroup()-------------");
    }
	
	
	@DELETE
    @Path("/group")
	public void deleteGroup(@FormParam("id") String id) throws JSONException{
		System.out.println("-----------Inside deleteGroup()----------");
		//JSONObject jgroup = new JSONObject(group);
		//System.out.println("Group: " + jgroup);
		System.out.println("Id to delete: " + id);
		try { 		
		    System.out.println("Deleting data");
	        DBCollection groupCollection = db.getCollection("group");
	        BasicDBObject dbWhere = new BasicDBObject();
	        dbWhere.put("_id", new ObjectId(id));
	        //dbGroup.append("$set", new BasicDBObject().append("deleted", true));
	        //groupCollection.update(dbWhere, dbGroup);
	        //BasicDBObject dbGroup = new BasicDBObject();
	        //dbGroup.put("_id",new ObjectId(id));
	        groupCollection.remove(dbWhere);
	        System.out.println("Data deleted");
	    } catch (Exception e) { 
	    	  e.printStackTrace(); 
	      }
		System.out.println("-----------Exiting deleteGroup()-------------");
    }
	
	@GET
    @Path("/group")
	public String showGroupDetails(@QueryParam("id") String id) throws JSONException{
		System.out.println("-------------Inside showGroup()-------------");
		System.out.println("Group id: "+id);
		String group = null;
		try { 		
		    System.out.println("Fetching data");
	        DBCollection groupCollection = db.getCollection("group");
	        DBObject dbGroup = new BasicDBObject();
	        dbGroup.put("_id",new ObjectId(id));
	        DBCursor dbCursor = groupCollection.find(dbGroup);
	        System.out.println("Data fetched");
	        
	        if(dbCursor.hasNext()) {
	        	System.out.println("Data is present");
	        	DBObject dbRow = dbCursor.next();
	        	//System.out.println(dbRow);
	        	group = JSON.serialize(dbRow);
	        	//System.out.println(((BasicDBList)dbRow.get("members")).get(0));
	        }	       
	      } catch (Exception e) { 
	    	  e.printStackTrace(); 
	      }
		System.out.println("---------------Exiting showGroup()----------------");
		return group;
    }	
}
