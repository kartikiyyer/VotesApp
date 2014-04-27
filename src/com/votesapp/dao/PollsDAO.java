package com.votesapp.dao;

import java.util.ArrayList;
import java.util.List;

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

				/*BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_creator", "4084553112");
				DBCursor cursor = table.find();
				while(cursor.hasNext()) {
					DBObject getdata=cursor.next();
					System.out.println("poll created and poll id is: "+getdata.get("_id"));				
				}*/

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


	public String showAllPolls(String user_name) throws Exception {
		JSONObject result= new JSONObject();
		System.out.println("in showAllPoll...");
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){


				DBCollection table = db.getCollection("voted_polls");
				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_voter_id", user_name);
				DBCursor cursor = table.find(whereQuery);
				DBObject getdata;
				JSONArray resultJson=new JSONArray();
				JSONObject jobj=new JSONObject();
				while(cursor.hasNext()){
					getdata=cursor.next();
					resultJson.put(getdata.get("poll_id"));
				}
				jobj.put("voted_id", resultJson);

				List<ObjectId> list = new ArrayList<ObjectId>();
				for(int i=0;i<resultJson.length();i++){
					list.add(new ObjectId(resultJson.get(i).toString()));
				}
				System.out.println("list: "+list);

				table = db.getCollection("polls");
				whereQuery = new BasicDBObject();

				whereQuery.put("_id", new BasicDBObject("$nin", list));
				DBCursor cursor1 = table.find(whereQuery);
				DBObject getdata1;
				System.out.println("were query: "+whereQuery);
				while(cursor1.hasNext()) {
					getdata1=cursor1.next();
					if(getdata1.get("poll_public").equals("yes")){
						result.append("All_Polls", new JSONObject(JSON.serialize(getdata1)));
					}

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


	public String showPollsByGroup(String userName) throws Exception{
		JSONObject result= new JSONObject();
		JSONObject cursorResult1;
		System.out.println("in showPollsByGroup...");
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("group");

				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("members", userName);
				DBCursor cursor = table.find(whereQuery);
				DBObject getdata;
				JSONArray cursorResult=new JSONArray();
				while(cursor.hasNext()) {
					getdata=cursor.next();

					cursorResult.put(getdata.get("_id"));

				}
				System.out.println("Groups assigned to "+userName+" are: "+cursorResult.toString());

				DBCollection table1 = db.getCollection("polls");
				BasicDBObject whereQuery1;
				JSONArray resultJson1=new JSONArray();

				for(int i=0;i<cursorResult.length();i++){
					whereQuery1 = new BasicDBObject();
					System.out.println("cursorResult: "+cursorResult.get(i).toString());
					whereQuery1.put("poll_groupid", cursorResult.get(i).toString());
					System.out.println("whereQuery1: "+whereQuery1);
					DBCursor cursor1 = table1.find(whereQuery1);
					DBObject getdata1;

					while(cursor1.hasNext()) {
						getdata1=cursor1.next();
						cursorResult1=new JSONObject();

						cursorResult1.put("poll_id",getdata1.get("_id"));
						cursorResult1.put("poll_question",getdata1.get("poll_question"));
						cursorResult1.put("poll_options",getdata1.get("poll_options"));
						cursorResult1.put("poll_create_date",getdata1.get("poll_create_date"));
						cursorResult1.put("poll_end_date",getdata1.get("poll_end_date"));
						cursorResult1.put("poll_creator",getdata1.get("poll_creator"));
						cursorResult1.put("poll_category",getdata1.get("poll_category"));
						cursorResult1.put("poll_groupid",getdata1.get("poll_groupid"));
						cursorResult1.put("poll_participants",getdata1.get("poll_participants"));

						System.out.println("cursorResult1 : "+cursorResult1);

						resultJson1.put(cursorResult1);
					}
				}
				result.put("My_Polls", resultJson1);
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
		System.out.println("show polls by group: "+result);
		return result.toString();
	}



	public String showAllPollsAssignedToMe(String userName) throws Exception{
		JSONObject result= new JSONObject();
		JSONObject cursorResult1;
		System.out.println("in showAllPollsAssignedToMe...");
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");


				DBCollection table = db.getCollection("voted_polls");
				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_voter_id", userName);
				DBCursor cursor2 = table.find(whereQuery);
				DBObject getdata2;
				JSONArray resultJson=new JSONArray();
				JSONObject jobj=new JSONObject();
				while(cursor2.hasNext()){
					getdata2=cursor2.next();
					resultJson.put(getdata2.get("poll_id"));
				}
				jobj.put("voted_id", resultJson);

				List<ObjectId> list = new ArrayList<ObjectId>();
				for(int i=0;i<resultJson.length();i++){
					list.add(new ObjectId(resultJson.get(i).toString()));
				}
				System.out.println("list: "+list);
				
				DBCollection table1 = db.getCollection("polls");
				BasicDBObject whereQuery1;
				JSONArray resultJson1=new JSONArray();

				BasicDBObject whereQueryn = new BasicDBObject();
				whereQueryn.put("_id", new BasicDBObject("$nin", list));
				whereQueryn.put("poll_participants",userName);
				System.out.println("whereQueryn: "+whereQueryn);
				DBCursor cursorn = table1.find(whereQueryn);
				DBObject getdatan;
				System.out.println(cursorn.count());
				while(cursorn.hasNext()) {
					getdatan=cursorn.next();
					cursorResult1=new JSONObject();

					cursorResult1.put("poll_id",getdatan.get("_id"));
					cursorResult1.put("poll_question",getdatan.get("poll_question"));
					cursorResult1.put("poll_options",getdatan.get("poll_options"));
					cursorResult1.put("poll_create_date",getdatan.get("poll_create_date"));
					cursorResult1.put("poll_end_date",getdatan.get("poll_end_date"));
					cursorResult1.put("poll_creator",getdatan.get("poll_creator"));
					cursorResult1.put("poll_category",getdatan.get("poll_category"));
					cursorResult1.put("poll_groupid",getdatan.get("poll_groupid"));
					cursorResult1.put("poll_participants",getdatan.get("poll_participants"));

					System.out.println("cursorResult1 : "+cursorResult1);

					resultJson1.put(cursorResult1);
				}


				//cursorResult will contain all the groupid in which this user is member
				table = db.getCollection("group");
				whereQuery = new BasicDBObject();
				whereQuery.put("members", userName);
				DBCursor cursor = table.find(whereQuery);
				DBObject getdata;
//				JSONArray cursorResult=new JSONArray();
				List<ObjectId> cursorResult=new ArrayList<ObjectId>();
				while(cursor.hasNext()) {
					getdata=cursor.next();
					cursorResult.add(new ObjectId(getdata.get("_id").toString()));
				}
				System.out.println("Groups assigned to "+userName+" are: "+cursorResult.toString());

				whereQuery1 = new BasicDBObject();
				whereQuery1.put("poll_groupid", new BasicDBObject("$in", cursorResult).toString());
				System.out.println("whereQuery1: "+whereQuery1);
				DBCursor cursor1 = table1.find(whereQuery1);
				DBObject getdata1;

				while(cursor1.hasNext()) {
					getdata1=cursor1.next();
					cursorResult1=new JSONObject();

					cursorResult1.put("poll_id",getdata1.get("_id"));
					cursorResult1.put("poll_question",getdata1.get("poll_question"));
					cursorResult1.put("poll_options",getdata1.get("poll_options"));
					cursorResult1.put("poll_create_date",getdata1.get("poll_create_date"));
					cursorResult1.put("poll_end_date",getdata1.get("poll_end_date"));
					cursorResult1.put("poll_creator",getdata1.get("poll_creator"));
					cursorResult1.put("poll_category",getdata1.get("poll_category"));
					cursorResult1.put("poll_groupid",getdata1.get("poll_groupid"));
					cursorResult1.put("poll_participants",getdata1.get("poll_participants"));

					System.out.println("cursorResult1 : "+cursorResult1);

					resultJson1.put(cursorResult1);
				}

				result.put("My_Polls", resultJson1);
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
		System.out.println("show polls by group: "+result);
		return result.toString();
	}



	public String showPollsByCategory(String categoryName,String user_name) throws Exception{
		JSONObject result= new JSONObject();
		JSONObject cursorResult;
		System.out.println("in showPoll by category...");
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");

				DBCollection table = db.getCollection("voted_polls");
				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("poll_voter_id", user_name);
				DBCursor cursor = table.find(whereQuery);
				DBObject getdata;
				JSONArray resultJson=new JSONArray();
				JSONObject jobj=new JSONObject();
				while(cursor.hasNext()){
					getdata=cursor.next();
					resultJson.put(getdata.get("poll_id"));
				}
				jobj.put("voted_id", resultJson);

				List list = new ArrayList();
				for(int i=0;i<resultJson.length();i++){
					list.add(resultJson.get(i));
				}


				table = db.getCollection("polls");
				whereQuery = new BasicDBObject();
				whereQuery.put("poll_id", new BasicDBObject("$nin", list));
				whereQuery.put("poll_category", categoryName);
				DBCursor cursor1 = table.find(whereQuery);
				DBObject getdata1;
				JSONArray resultJson1=new JSONArray();
				while(cursor1.hasNext()) {
					getdata1=cursor1.next();
					cursorResult=new JSONObject();

					cursorResult.put("poll_id",getdata1.get("_id"));
					cursorResult.put("poll_question",getdata1.get("poll_question"));
					cursorResult.put("poll_options",getdata1.get("poll_options"));
					cursorResult.put("poll_create_date",getdata1.get("poll_create_date"));
					cursorResult.put("poll_end_date",getdata1.get("poll_end_date"));
					cursorResult.put("poll_creator",getdata1.get("poll_creator"));
					cursorResult.put("poll_category",getdata1.get("poll_category"));
					cursorResult.put("poll_groupid",getdata1.get("poll_groupid"));
					cursorResult.put("poll_participants",getdata1.get("poll_participants"));

					resultJson1.put(cursorResult);
				}

				result.put("Category_Polls", resultJson1);
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


				table = db.getCollection("voted_polls");
				document = new BasicDBObject();
				//				document.putAll((DBObject)JSON.parse(pollResults.toString()));
				document.put("poll_id",pollResults.getString("poll_id"));
				document.put("poll_voter_id",pollResults.getString("poll_voter_id"));
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
		//		pd.showAllPolls("4084553614");
		//		pd.showPollsByCategory("Fun", "4084553614");
		//		pd.showMyPolls("4084553613");
		//		pd.getPollOptionCount("53536c827d071683b7368835");
//				pd.showPollsByGroup("(408) 429-4731");
		pd.showAllPollsAssignedToMe("15555215553");
	}
}
