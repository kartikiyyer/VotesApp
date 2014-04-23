package com.votesapp.dao;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class PollsDAO implements IPollsDAO{
	private String ipaddress="ds053838.mongolab.com";

	public String createPoll(JSONObject jsonCreatePollValues) throws Exception{ 
		JSONObject result= new JSONObject();
		System.out.println("in createpoll...");
		try { 
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("polls");
				BasicDBObject document = new BasicDBObject();
				document.putAll((DBObject)JSON.parse(jsonCreatePollValues.toString()));
				table.insert(document);

				//use this to search by _id 
				/*BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("_id", new ObjectId("put your id here"));*/

				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_creator", "4084553112");
				DBCursor cursor = table.find();
				while(cursor.hasNext()) {
					DBObject getdata=cursor.next();
					System.out.println("poll created and poll id is: "+getdata.get("_id"));				
				}

				result.put("Msg", "success");
			}else{
				System.out.println("can not connect");
				result.put("Msg", "fail");
			}

		}catch (MongoException me) { 
			me.printStackTrace(); 
		} catch(Exception e){
			e.printStackTrace();
		}
		return(result.toString());
	}



	public String deletePoll(String jsonDeletePollId) throws Exception{ 
		JSONObject result= new JSONObject();
		System.out.println("in deletePoll...");

		try { 
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("polls");

				//to search by _id --> use this
				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("_id", new ObjectId(jsonDeletePollId.toString()));
				System.out.println("deleting poll for poll id : "+whereQuery.get("_id"));
				table.remove(whereQuery);
				System.out.println("deleted...");

				result.put("Msg", "success");
			}else{
				System.out.println("can not connect");
				result.put("Msg", "fail");
			}

		}catch (MongoException me) { 
			result.put("Msg", "fail");
			me.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}
		return(result.toString());
	}


	public String showAllPolls() throws Exception {
		JSONObject result= new JSONObject();
		JSONObject cursorResult;
		System.out.println("in showPoll...");
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("polls");

				DBCursor cursor=table.find();
				DBObject getdata;
//				JSONArray resultJson=new JSONArray();
				
				while(cursor.hasNext()) {
					getdata=cursor.next();
					result.append("All_Polls", new JSONObject(JSON.serialize(getdata)));
/*					cursorResult=new JSONObject();
					cursorResult.put("poll_id",getdata.get("_id"));
					cursorResult.put("poll_question",getdata.get("poll_question"));
//					cursorResult.put("poll_options",getdata.get("poll_options"));
					cursorResult.append("poll_options",new JSONObject(JSON.serialize(getdata.get("poll_options"))));
					cursorResult.put("poll_create_date",getdata.get("poll_create_date"));
					cursorResult.put("poll_end_date",getdata.get("poll_end_date"));
					cursorResult.put("poll_creator",getdata.get("poll_creator"));
					cursorResult.put("poll_category",getdata.get("poll_category"));
//					cursorResult.put("poll_groupid",getdata.get("poll_groupid"));
					cursorResult.append("poll_groupid",new JSONObject(JSON.serialize(getdata.get("poll_groupid"))));
//					cursorResult.put("poll_participants",getdata.get("poll_participants"));
					cursorResult.append("poll_participants",new JSONObject(JSON.serialize(getdata.get("poll_participants"))));

					resultJson.put(cursorResult);*/
				}

				//to fetch particular element in mongo object which is an array
				/*BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_question", jsonCreatePollValues.get("poll_question"));
				DBCursor cursor = table.find(whereQuery);
				while(cursor.hasNext()) {
					DBObject getdata=cursor.next();
					BasicDBList bdl=(BasicDBList)getdata.get("poll_options");
					System.out.println(bdl.get(1));
				}*/
//				result.put("All_Polls", resultJson);
				result.put("Msg", "success");


			}else{
				System.out.println("can not connect");
				result.put("Msg", "fail");
			}

		}catch (MongoException me) { 
			result.put("Msg", "fail");
			me.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}
		System.out.println(result);
		return result.toString();
	}


	public String showPollsByCategory(String categoryName) throws Exception{
		JSONObject result= new JSONObject();
		JSONObject cursorResult;
		System.out.println("in showPoll by category...");
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("polls");

				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_category", categoryName);
				DBCursor cursor = table.find(whereQuery);
				DBObject getdata;
				JSONArray resultJson=new JSONArray();
				while(cursor.hasNext()) {
					getdata=cursor.next();
					cursorResult=new JSONObject();
					
					cursorResult.put("poll_id",getdata.get("_id"));
					cursorResult.put("poll_question",getdata.get("poll_question"));
					cursorResult.put("poll_options",getdata.get("poll_options"));
					cursorResult.put("poll_create_date",getdata.get("poll_create_date"));
					cursorResult.put("poll_end_date",getdata.get("poll_end_date"));
					cursorResult.put("poll_creator",getdata.get("poll_creator"));
					cursorResult.put("poll_category",getdata.get("poll_category"));
					cursorResult.put("poll_groupid",getdata.get("poll_groupid"));
					cursorResult.put("poll_participants",getdata.get("poll_participants"));

					resultJson.put(cursorResult);
				}
				
				result.put("Category_Polls", resultJson);
				result.put("Msg", "success");

			}else{
				System.out.println("can not connect");
				result.put("Msg", "fail");
			}

		}catch (MongoException me) { 
			result.put("Msg", "fail");
			me.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}
		System.out.println(result);
		return result.toString();
	}

	
	public String showMyPolls(String userName) throws Exception{
		JSONObject result= new JSONObject();
		JSONObject cursorResult;
		System.out.println("in showMyPoll...");
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("polls");

				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_creator", userName);
				DBCursor cursor = table.find(whereQuery);
				DBObject getdata;
				JSONArray resultJson=new JSONArray();
				while(cursor.hasNext()) {
					getdata=cursor.next();
					cursorResult=new JSONObject();
					
					cursorResult.put("poll_id",getdata.get("_id"));
					cursorResult.put("poll_question",getdata.get("poll_question"));
					cursorResult.put("poll_options",getdata.get("poll_options"));
					cursorResult.put("poll_create_date",getdata.get("poll_create_date"));
					cursorResult.put("poll_end_date",getdata.get("poll_end_date"));
					cursorResult.put("poll_creator",getdata.get("poll_creator"));
					cursorResult.put("poll_category",getdata.get("poll_category"));
					cursorResult.put("poll_groupid",getdata.get("poll_groupid"));
					cursorResult.put("poll_participants",getdata.get("poll_participants"));

					resultJson.put(cursorResult);
				}
				
				result.put("My_Polls", resultJson);
				result.put("Msg", "success");

			}else{
				System.out.println("can not connect");
				result.put("Msg", "fail");
			}

		}catch (MongoException me) { 
			result.put("Msg", "fail");
			me.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}
		System.out.println(result);
		return result.toString();
	}
	
	public String voteOnPoll(JSONObject pollResults) throws Exception{
		
		JSONObject result= new JSONObject();
		System.out.println("in voteonpoll...");
		try { 
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("poll_results");
				BasicDBObject document = new BasicDBObject();
				document.putAll((DBObject)JSON.parse(pollResults.toString()));
				table.insert(document);

				//use this to search by _id 
				/*BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("_id", new ObjectId("put your id here"));*/

				result.put("Msg", "success");
			}else{
				System.out.println("can not connect");
				result.put("Msg", "fail");
			}

		}catch (MongoException me) { 
			me.printStackTrace(); 
		} catch(Exception e){
			e.printStackTrace();
		}
		return(result.toString());
	}
	
	
	public String getPollOptionCount(String pollId) throws Exception{
		JSONObject result= new JSONObject();
		JSONObject cursorResult=new JSONObject();
		System.out.println("in getpolloptioncount...");
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("poll_results");

				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_id", pollId);
				DBCursor cursor = table.find(whereQuery);
				DBObject getdata;
				int temp=1,noOfPollOptions=0;
				while(cursor.hasNext() && temp==1) {
					temp=2;
					getdata=cursor.next();
					getdata.get("poll_options");
					BasicDBList bdl=(BasicDBList)getdata.get("poll_options");
					System.out.println(bdl.size());
					noOfPollOptions=bdl.size();
					cursorResult.put("poll_question", getdata.get("poll_question"));
					cursorResult.put("poll_options",getdata.get("poll_options"));
				}

				whereQuery = new BasicDBObject();
				whereQuery.put("poll_id", pollId);
				for(int tempval=1;tempval<=noOfPollOptions;tempval++){
					whereQuery.put("poll_voter_option", tempval);
					result.put(Integer.toString(tempval), table.getCount(whereQuery)); 
				}
				
				cursorResult.put("TotalOptions", noOfPollOptions);
				cursorResult.put("OptionsVoteCount", result);
				cursorResult.put("Msg", "success");
				
			}else{
				System.out.println("can not connect");
				cursorResult.put("Msg", "fail");
			}

		}catch (MongoException me) { 
			cursorResult.put("Msg", "fail");
			me.printStackTrace(); 
		} catch(Exception e){
			cursorResult.put("Msg", "fail");
			e.printStackTrace();
		}
		System.out.println(cursorResult);
		return cursorResult.toString();
	}
	
	
	/*public String showPollResult(String pollId) throws Exception{
		JSONObject result= new JSONObject();
		JSONObject cursorResult;
		System.out.println("in showPollResult...");
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("polls");

				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_id", pollId);
				JSONObject jobj=new JSONObject();
				jobj=getPollById(pollId);
				jobj.get("poll_options");
				BasicDBObject whereQuery1 = new BasicDBObject();
				whereQuery1.put("poll_options", pollId);
				DBCursor cursor = table.find(whereQuery,whereQuery);
				DBObject getdata;
				JSONArray resultJson=new JSONArray();
				while(cursor.hasNext()) {
					getdata=cursor.next();
					cursorResult=new JSONObject();
					
					cursorResult.put("poll_id",getdata.get("_id"));
					cursorResult.put("poll_question",getdata.get("poll_question"));
					cursorResult.put("poll_options",getdata.get("poll_options"));
					cursorResult.put("poll_create_date",getdata.get("poll_create_date"));
					cursorResult.put("poll_end_date",getdata.get("poll_end_date"));
					cursorResult.put("poll_creator",getdata.get("poll_creator"));
					cursorResult.put("poll_category",getdata.get("poll_category"));
					cursorResult.put("poll_groupid",getdata.get("poll_groupid"));
					cursorResult.put("poll_participants",getdata.get("poll_participants"));

					resultJson.put(cursorResult);
				}
				
				result.put("My_Polls", resultJson);
				result.put("Msg", "success");

			}else{
				System.out.println("can not connect");
				result.put("Msg", "fail");
			}

		}catch (MongoException me) { 
			result.put("Msg", "fail");
			me.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}
		System.out.println(result);
		return result.toString();
	}*/

	public static void main(String[] args) throws Exception{
		PollsDAO pd=new PollsDAO();
//		pd.showAllPolls();
//		pd.showMyPolls("4084553613");
		pd.getPollOptionCount("53536c827d071683b7368835");
	}
}
