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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
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
		JSONObject jgroups = new JSONObject();

		// input --> validation
		if(null==phoneNumber || phoneNumber.isEmpty()){
			jgroups.put("Msg", "fail");
		}else{
//			phoneNumber=phoneNumber.replaceAll("[^0-9]", "").trim();

			JSONObject jphoneNumber = new JSONObject(phoneNumber);

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
				jgroups.put("Msg", "success");
				System.out.println(jgroups);
			} catch (Exception e) { 
				jgroups.put("Msg", "fail");
				e.printStackTrace(); 
			}
		}
		System.out.println("---------------Exiting showGroups()---------------");
		return jgroups.toString();
	}

	@POST
	@Path("/group")
	public String createGroup(@FormParam("group") String group) throws JSONException{
		System.out.println("-----------Inside createGroup()----------");

		JSONObject result=new JSONObject();

		JSONObject jgroup = new JSONObject(group);

		if(jgroup.get("name")==null ||
				jgroup.get("phone_number")==null ||
				jgroup.get("name").toString().isEmpty() ||
				jgroup.get("phone_number").toString().isEmpty()){
			result.put("Msg", "fail");
		}
		else{
			String flag="success";
			JSONArray members = (JSONArray) jgroup.get("members");
			JSONArray resp=new JSONArray();
			for(int i=0;i<members.length();i++){
				if(null==members.get(i) || members.get(i).toString().isEmpty()){
					result.put("Msg", "fail");
					flag="fail";
					break;
				}
				resp.put(members.get(i).toString().replaceAll("[^0-9]", "").trim());
			}
			if(flag.equals("success")){
				jgroup.put("members", resp);
				jgroup.put(("name"),jgroup.getString("name").trim());
				jgroup.put(("phone_number"),jgroup.getString("phone_number").replaceAll("[^0-9]", "").trim());

				System.out.println("Group: " + jgroup);
				try { 		
					System.out.println("Inserting data");
					DBCollection groupCollection = db.getCollection("group");
					BasicDBObject dbGroup = new BasicDBObject();
					dbGroup.putAll((DBObject)JSON.parse(jgroup.toString()));
					groupCollection.insert(dbGroup);
					System.out.println("Data inserted");
					result.put("Msg", "success");
				} catch (Exception e) { 
					result.put("Msg", "fail");
					e.printStackTrace(); 
				}
			}
		}
		System.out.println("-----------Exiting createGroup()-------------");

		return result.toString();
	}

	@PUT
	@Path("/group")
	public String editGroup(@FormParam("group") String group) throws JSONException{
		System.out.println("-----------Inside editGroup()----------");
		JSONObject jgroup = new JSONObject(group);
		//		System.out.println("Group: " + jgroup);
		JSONObject result=new JSONObject();

		if(jgroup.get("name")==null ||
				jgroup.get("phone_number")==null ||
				jgroup.get("name").toString().isEmpty() ||
				jgroup.get("phone_number").toString().isEmpty()){
			result.put("Msg", "fail");
		}else{
			String flag="success";
			JSONArray members = (JSONArray) jgroup.get("members");
			JSONArray resp=new JSONArray();
			for(int i=0;i<members.length();i++){
				if(null==members.get(i) || members.get(i).toString().isEmpty()){
					result.put("Msg", "fail");
					flag="fail";
					break;
				}
				resp.put(members.get(i).toString().replaceAll("[^0-9]", "").trim());
			}
			if(flag.equals("success")){
				jgroup.put("members", resp);
				jgroup.put(("name"),jgroup.getString("name").trim());
				jgroup.put(("phone_number"),jgroup.getString("phone_number").replaceAll("[^0-9]", "").trim());
				try { 		
					System.out.println("Updating data");
					DBCollection groupCollection = db.getCollection("group");
					BasicDBObject dbGroup = new BasicDBObject();
					BasicDBObject dbWhere = new BasicDBObject();
					dbGroup.putAll((DBObject)JSON.parse(jgroup.toString()));
					dbWhere.put("_id", new ObjectId(jgroup.getString("_id")));
					groupCollection.update(dbWhere, dbGroup);
					System.out.println("Data updated");
					result.put("Msg", "success");
				} catch (Exception e) { 
					result.put("Msg", "fail");
					e.printStackTrace(); 
				}
			}
		}
		System.out.println("-----------Exiting editGroup()-------------");
		return result.toString();
	}


	@DELETE
	@Path("/group")
	public String deleteGroup(@FormParam("id") String id) throws JSONException{
		System.out.println("-----------Inside deleteGroup()----------");
		//JSONObject jgroup = new JSONObject(group);
		//System.out.println("Group: " + jgroup);
		JSONObject result=new JSONObject();
		if(null==id || id.isEmpty()){
			result.put("Msg","fail");
		}else{

//			id=id.replaceAll("[^0-9]", "").trim();
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
				result.put("Msg","success");
			} catch (Exception e) { 
				result.put("Msg","fail");
				e.printStackTrace(); 
			}
		}
		System.out.println("-----------Exiting deleteGroup()-------------");
		return result.toString();
	}

	@GET
	@Path("/group")
	public String showGroupDetails(@QueryParam("id") String id) throws JSONException{
		System.out.println("-------------Inside showGroup()-------------");
		//		System.out.println("Group id: "+id);

		JSONObject result=new JSONObject();
		if(null==id || id.isEmpty()){
			result.put("Msg","fail");
		}else{
//			id=id.replaceAll("[^0-9]", "").trim();
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
				result.put("group", group);
				result.put("Msg", "success");
			} catch (Exception e) { 
				result.put("Msg", "fail");
				e.printStackTrace(); 
			}
		}
		System.out.println("---------------Exiting showGroup()----------------");
		return result.toString();
	}	
}
