package com.votesapp.dao;

import java.util.ArrayList;
import java.util.List;

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
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import com.votesapp.utility.GeocodeImplementation;

public class EnterpriseDAO implements IEnterpriseDAO {
	private String ipaddress="ds053838.mongolab.com";

	public String followEnterprise(JSONObject jsonFollowValues) throws Exception{
		JSONObject result= new JSONObject();
		System.out.println("in followEnterprise...");

		try { 
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				System.out.println("connection successfull");
				DBCollection table = db.getCollection("enterprise_follower");
				BasicDBObject document = new BasicDBObject();
				document.putAll((DBObject)JSON.parse(jsonFollowValues.toString()));
				table.insert(document);
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
		return result.toString();
	}

	public String showEnterpriseFollowList(String userName) throws Exception{
		JSONObject result= new JSONObject();
		System.out.println("in showEnterpriseFollowList...");

		try{
			//removing extra chars from the  input --> validation
			if(null==userName || userName.isEmpty()){
				result.put("Msg", "fail");
			}
			else{
				userName=userName.replaceAll("[^0-9]", "").trim();

				try{
					MongoClient mongo = new MongoClient( ipaddress , 53838 );
					DB db = mongo.getDB("votesapp");
					boolean auth = db.authenticate("admin", "password@123".toCharArray());
					if(auth){
						System.out.println("connection successfull");
						DBCollection table = db.getCollection("enterprise_follower");

						BasicDBObject whereQuery = new BasicDBObject();
						whereQuery.put("follower", userName);

						System.out.println("whereQuery:: "+whereQuery);
						DBCursor cursor = table.find(whereQuery);
						DBObject getdata;

						JSONArray resultJson=new JSONArray();
						while(cursor.hasNext()){
							getdata=cursor.next();
							resultJson.put(getdata.get("enterprise_name"));
						}

						List list = new ArrayList();
						for(int i=0;i<resultJson.length();i++){
							list.add(resultJson.get(i));
						}
						System.out.println("list: "+list);

						table = db.getCollection("enterprise_details");
						whereQuery = new BasicDBObject();
						whereQuery.put("enterprise_name", new BasicDBObject("$in", list));
						System.out.println("where query: "+whereQuery);

						DBCursor cursor1 = table.find(whereQuery);
						DBObject getdata1;
						JSONArray jarray=new JSONArray();
						while(cursor1.hasNext()) {
							getdata1=cursor1.next();
							jarray.put(new JSONObject(JSON.serialize(getdata1)));
						}
						result.put("following_list", jarray);

						result.put("Msg", "success");

					}else{
						System.out.println("can not connect");
						result.put("Msg", "fail");
					}

				}catch (MongoException me) { 
					result.put("Msg", "fail");
					me.printStackTrace(); 
				}
			}
		}catch(Exception e){

		}
		System.out.println("result:  "+result);
		return result.toString();
	}

	public String showEnterpriseList(String userName,String category) throws Exception{
		JSONObject result= new JSONObject();
		System.out.println("in showEnterpriseList...");

		try{
			//removing extra chars from the  input --> validation
			if(null==userName || userName.isEmpty()){
				result.put("Msg", "fail");
			}
			else{
				userName=userName.replaceAll("[^0-9]", "").trim();

				try{
					MongoClient mongo = new MongoClient( ipaddress , 53838 );
					DB db = mongo.getDB("votesapp");
					boolean auth = db.authenticate("admin", "password@123".toCharArray());
					if(auth){
						System.out.println("connection successfull");
						DBCollection table = db.getCollection("enterprise_follower");

						BasicDBObject whereQuery = new BasicDBObject();
						whereQuery.put("follower", userName);

						System.out.println("whereQuery:: "+whereQuery);
						DBCursor cursor = table.find(whereQuery);
						DBObject getdata;

						JSONArray resultJson=new JSONArray();
						while(cursor.hasNext()){
							getdata=cursor.next();
							resultJson.put(getdata.get("enterprise_name"));
						}

						List list = new ArrayList();
						for(int i=0;i<resultJson.length();i++){
							list.add(resultJson.get(i));
						}
						System.out.println("list: "+list);

						table = db.getCollection("enterprise_details");
						whereQuery = new BasicDBObject();
						whereQuery.put("enterprise_name", new BasicDBObject("$nin", list));
						whereQuery.put("enterprise_category", category);
						System.out.println("where query: "+whereQuery);

						DBCursor cursor1 = table.find(whereQuery);
						DBObject getdata1;
						JSONArray jarray=new JSONArray();
						while(cursor1.hasNext()) {
							getdata1=cursor1.next();
							jarray.put(new JSONObject(JSON.serialize(getdata1)));
						}
						result.put("enterprise_details", jarray);

						result.put("Msg", "success");

					}else{
						System.out.println("can not connect");
						result.put("Msg", "fail");
					}

				}catch (MongoException me) { 
					result.put("Msg", "fail");
					me.printStackTrace(); 
				}
			}
		}catch(Exception e){

		}
		System.out.println("result:  "+result);
		return result.toString();
	}

	public String showEnterpriseVotedPolls(String user_name,String enterprise_name) throws Exception {
		JSONObject result= new JSONObject();
		System.out.println("in showEnterpriseVotedPolls...");

		try{
			//removing extra chars from the  input --> validation
			if(null==user_name || user_name.isEmpty()){
				result.put("Msg", "fail");
			}
			else{
				user_name=user_name.replaceAll("[^0-9]", "").trim();

				try{
					MongoClient mongo = new MongoClient( ipaddress , 53838 );
					DB db = mongo.getDB("votesapp");
					boolean auth = db.authenticate("admin", "password@123".toCharArray());
					if(auth){

						DBCollection table = db.getCollection("enterprise_voted_polls");
						BasicDBObject whereQuery = new BasicDBObject();
						whereQuery.put("poll_voter_id", user_name);
						DBCursor cursor = table.find(whereQuery);
						DBObject getdata;
						JSONArray resultJson=new JSONArray();
						JSONObject jobj=new JSONObject();
						while(cursor.hasNext()){
							getdata=cursor.next();
							resultJson.put(getdata.get("enterprise_poll_id"));
						}
						jobj.put("voted_id", resultJson);

						List list = new ArrayList();
						for(int i=0;i<resultJson.length();i++){
							list.add(resultJson.get(i).toString());
						}
						System.out.println("list: "+list);

						table = db.getCollection("enterprise_poll_results");
						whereQuery = new BasicDBObject();

						whereQuery.put("enterprise_poll_id", new BasicDBObject("$in", list));
						whereQuery.put("enterprise_name", enterprise_name);
						whereQuery.put("poll_voter_id", user_name);
						DBCursor cursor1 = table.find(whereQuery);
						DBObject getdata1;
						System.out.println("where query: "+whereQuery);
						JSONArray jarray=new JSONArray();
						while(cursor1.hasNext()) {
							getdata1=cursor1.next();
							jarray.put(new JSONObject(JSON.serialize(getdata1)));
						}
						result.put("Voted_Polls", jarray);
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
			}
		}catch (JSONException je) { 
			result.put("Msg", "fail");
			je.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}

		System.out.println(result);
		return result.toString();
	}


	public String showEnterpriseUnvotedPolls(String user_name,String enterprise_name) throws Exception {
		JSONObject result= new JSONObject();
		System.out.println("in showEnterpriseUnvotedPolls...");

		try{
			//removing extra chars from the  input --> validation
			if(null==user_name || user_name.isEmpty()){
				result.put("Msg", "fail");
			}
			else{
				user_name=user_name.replaceAll("[^0-9]", "").trim();

				try{
					MongoClient mongo = new MongoClient( ipaddress , 53838 );
					DB db = mongo.getDB("votesapp");
					boolean auth = db.authenticate("admin", "password@123".toCharArray());
					if(auth){

						DBCollection table = db.getCollection("enterprise_voted_polls");
						BasicDBObject whereQuery = new BasicDBObject();
						whereQuery.put("poll_voter_id", user_name);
						DBCursor cursor = table.find(whereQuery);
						DBObject getdata;
						JSONArray resultJson=new JSONArray();
						JSONObject jobj=new JSONObject();
						while(cursor.hasNext()){
							getdata=cursor.next();
							resultJson.put(getdata.get("enterprise_poll_id"));
						}
						jobj.put("voted_id", resultJson);

						List<ObjectId> list = new ArrayList<ObjectId>();
						for(int i=0;i<resultJson.length();i++){
							list.add(new ObjectId(resultJson.get(i).toString()));
						}
						System.out.println("list: "+list);

						table = db.getCollection("enterprise_polls");
						whereQuery = new BasicDBObject();

						whereQuery.put("_id", new BasicDBObject("$nin", list));
						whereQuery.put("enterprise_name", enterprise_name);

						DBCursor cursor1 = table.find(whereQuery);
						DBObject getdata1;
						System.out.println("where query: "+whereQuery);
						JSONArray jarray=new JSONArray();
						while(cursor1.hasNext()) {
							getdata1=cursor1.next();
							jarray.put(new JSONObject(JSON.serialize(getdata1)));
						}
						result.put("Unvoted_Polls", jarray);
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
			}
		}catch (JSONException je) { 
			result.put("Msg", "fail");
			je.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}

		System.out.println(result);
		return result.toString();
	}

	public String showEnterprisePollByPollId(String pollId) throws Exception{
		JSONObject result= new JSONObject();
		System.out.println("in showEnterprisePollByPollId...");

		try{
			//removing extra chars from the  input --> validation
			if(null==pollId || pollId.isEmpty()){
				result.put("Msg", "fail");
			}else{
				pollId=pollId.replaceAll("[\\p{P}\\p{S} ]", "").trim();

				try{
					MongoClient mongo = new MongoClient( ipaddress , 53838 );
					DB db = mongo.getDB("votesapp");
					boolean auth = db.authenticate("admin", "password@123".toCharArray());
					if(auth){
						System.out.println("connection successfull");
						DBCollection table = db.getCollection("enterprise_polls");
						DBObject searchById = new BasicDBObject("_id", new ObjectId(pollId));
						DBObject getdata = table.findOne(searchById);
						System.out.println("searchById: "+searchById);
						result.put("This_Poll", new JSONObject(JSON.serialize(getdata)));
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
			}
		}catch (JSONException je) { 
			result.put("Msg", "fail");
			je.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}
		System.out.println(result);
		return result.toString();
	}


	
	public String showEnterprisePollByPollIdVotedByMe(String username,String pollId) throws Exception{
		JSONObject result= new JSONObject();
		System.out.println("in showEnterprisePollByPollIdVotedByMe...");

		try{
			//removing extra chars from the  input --> validation
			if(null==pollId || pollId.isEmpty()){
				result.put("Msg", "fail");
			}else{
				pollId=pollId.replaceAll("[\\p{P}\\p{S} ]", "").trim();

				try{
					MongoClient mongo = new MongoClient( ipaddress , 53838 );
					DB db = mongo.getDB("votesapp");
					boolean auth = db.authenticate("admin", "password@123".toCharArray());
					if(auth){
						System.out.println("connection successfull");
						DBCollection table = db.getCollection("enterprise_poll_results");
						BasicDBObject whereQuery=new BasicDBObject();
						whereQuery.put("enterprise_poll_id",pollId);
						whereQuery.put("poll_voter_id", username);
						DBObject getdata = table.findOne(whereQuery);
						System.out.println("searchById: "+whereQuery);
						result.put("This_Poll", new JSONObject(JSON.serialize(getdata)));
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
			}
		}catch (JSONException je) { 
			result.put("Msg", "fail");
			je.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}
		System.out.println(result);
		return result.toString();
	}
	
	
	
	public String voteOnPoll(JSONObject jsonVoteOnPollValues) throws Exception{

		JSONObject result= new JSONObject();
		System.out.println("in voteonpoll...");

		try{

			String latlng,lat,lng;
			JSONObject jsonObjectGeo=new JSONObject(jsonVoteOnPollValues.getString("poll_voter_location"));
			lat=jsonObjectGeo.getString("latitude");
			lng=jsonObjectGeo.getString("longitude");
			String geoResultSet=null;
			if(null!=lat && null!=lng){
				if(!lat.isEmpty() && !lng.isEmpty()){
					latlng=lat+","+lng;
					GeocodeImplementation geocodeImplementation=new GeocodeImplementation();
					geoResultSet=geocodeImplementation.getJSONByGoogle(latlng);
					if(null!=geoResultSet){
						jsonVoteOnPollValues.put("poll_voter_city", geoResultSet);
					}else{
						jsonVoteOnPollValues.put("poll_voter_city", "unknown");
					}
				}else{
					jsonVoteOnPollValues.put("poll_voter_city", "unknown");
				}
			}else{
				jsonVoteOnPollValues.put("poll_voter_city", "unknown");
			}


			try { 
				MongoClient mongo = new MongoClient( ipaddress , 53838 );
				DB db = mongo.getDB("votesapp");
				boolean auth = db.authenticate("admin", "password@123".toCharArray());
				if(auth){
					System.out.println("connection successfull");
					DBCollection table = db.getCollection("enterprise_poll_results");
					BasicDBObject document = new BasicDBObject();
					document.putAll((DBObject)JSON.parse(jsonVoteOnPollValues.toString()));
					table.insert(document);

					table = db.getCollection("enterprise_voted_polls");
					document = new BasicDBObject();
					document.put("enterprise_poll_id",jsonVoteOnPollValues.getString("enterprise_poll_id"));
					document.put("poll_voter_id",jsonVoteOnPollValues.getString("poll_voter_id"));
					table.insert(document);

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

		}catch (JSONException je) { 
			result.put("Msg", "fail");
			je.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}
		return(result.toString());
	}


	public String showEnterpriseVoteResults(String pollId) throws Exception{
		JSONObject result= new JSONObject();
		JSONObject resultTemp= new JSONObject();
		JSONObject cursorResult=new JSONObject();
		System.out.println("in showEnterpriseVoteResults...");

		try{
			//removing extra chars from the  input --> validation
			if(null==pollId || pollId.isEmpty()){
				System.out.println("is empty");
				cursorResult.put("Msg", "fail");
			}else{
				pollId=pollId.replaceAll("[\\p{P}\\p{S} ]", "").trim();

				try{
					MongoClient mongo = new MongoClient( ipaddress , 53838 );
					DB db = mongo.getDB("votesapp");
					boolean auth = db.authenticate("admin", "password@123".toCharArray());
					if(auth){
						System.out.println("connection successfull");
						DBCollection table = db.getCollection("enterprise_poll_results");

						BasicDBObject whereQuery = new BasicDBObject();
						whereQuery.put("enterprise_poll_id", pollId);
						DBCursor cursor = table.find(whereQuery);
						System.out.println("whereQuery: "+whereQuery);
						if(cursor.size()==0)
						{
							System.out.println("cursor count is ZERO!!");
							result.put("Msg", "no_votes");
						}else{

							DBObject getdata;

							System.out.println("cursor has next!!");

							JSONArray cursorArray=new JSONArray();
							getdata=cursor.next();
							for(int temp=0;temp<Integer.parseInt(getdata.get("enterprise_poll_question_count").toString());temp++){
								int noOfPollOptions=0;
								String jobjectstr=getdata.get("enterprise_poll_questions").toString();
								JSONObject jnew=new JSONObject(jobjectstr);
								JSONArray jarray=(JSONArray)jnew.get("polls");
								JSONObject tempJObject=new JSONObject();
								tempJObject=(JSONObject) jarray.get(temp);

								noOfPollOptions=Integer.parseInt(tempJObject.get("poll_option_count").toString());
								cursorResult=new JSONObject();
								cursorResult.put("poll_question", tempJObject.get("poll_question"));
								cursorResult.put("poll_options",tempJObject.get("poll_options"));

								resultTemp= new JSONObject();
								for(int tempval=1;tempval<=noOfPollOptions;tempval++){
									whereQuery = new BasicDBObject();
									whereQuery.put("poll_voter_option", tempval);
									whereQuery.put("poll_question", tempJObject.get("poll_question"));
									DBObject fields = new BasicDBObject("$elemMatch", whereQuery);
									DBObject query = new BasicDBObject("enterprise_poll_questions.polls",fields);
									query.put("enterprise_poll_id",pollId);
									System.out.println("query: "+query);
									resultTemp.put(Integer.toString(tempval), table.getCount(query)); 
								}

								cursorResult.put("TotalOptions", noOfPollOptions);
								cursorResult.put("OptionsVoteCount", resultTemp);
								cursorArray.put(cursorResult);
							}
							result= new JSONObject();
							result.put("VoteCountResult", cursorArray);
							result.put("Msg", "success");
						}
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
			}
		}catch (JSONException je) { 
			result.put("Msg", "fail");
			je.printStackTrace(); 
		} catch(Exception e){
			result.put("Msg", "fail");
			e.printStackTrace();
		}
		System.out.println(result);
		return result.toString();
	}

	public String showEnterpriseVoteResultsGeo(String pollId) throws Exception{
		JSONObject result= new JSONObject();
		JSONObject cursorResult=new JSONObject();
		System.out.println("in showEnterpriseVoteResultsGeo...");

		try{
			//removing extra chars from the  input --> validation
			if(null==pollId || pollId.isEmpty()){
				cursorResult.put("Msg", "fail");
			}else{
				pollId=pollId.replaceAll("[\\p{P}\\p{S} ]", "").trim();

				try{
					MongoClient mongo = new MongoClient( ipaddress , 53838 );
					DB db = mongo.getDB("votesapp");
					boolean auth = db.authenticate("admin", "password@123".toCharArray());
					if(auth){
						System.out.println("connection successfull");
						DBCollection table = db.getCollection("enterprise_poll_results");

						BasicDBObject whereQuery = new BasicDBObject();
						whereQuery.put("enterprise_poll_id", pollId);
						DBCursor cursor = table.find(whereQuery);

						System.out.println("whereQuery: "+whereQuery);
						if(cursor.size()==0)
						{
							System.out.println("cursor count is ZERO!!");
							cursorResult.put("Msg", "no_votes");
						}else{
							DBObject getdata=null;
							System.out.println("cursor has next!!");
							
							while(cursor.hasNext()){
								getdata=cursor.next();
								whereQuery = new BasicDBObject();
								whereQuery.put("enterprise_poll_id", pollId);
								if(!result.toString().contains(getdata.get("poll_voter_city").toString())){
									whereQuery.put("poll_voter_city", getdata.get("poll_voter_city"));
									result.put(getdata.get("poll_voter_city").toString(), Long.toString(table.getCount(whereQuery)));
								}
							}
							cursorResult.put("city_count", result);
							cursorResult.put("Msg", "success");
						}
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
			}
		}catch (JSONException je) { 
			cursorResult.put("Msg", "fail");
			je.printStackTrace(); 
		} catch(Exception e){
			cursorResult.put("Msg", "fail");
			e.printStackTrace();
		}
		System.out.println(cursorResult);
		return cursorResult.toString();
	}
	
	

	public static void main(String[] args) throws Exception{
		EnterpriseDAO edao=new EnterpriseDAO();
//		edao.showEnterpriseList("4084553112","education");
//		edao.showEnterpriseFollowList("4084555566");
//		edao.showEnterpriseVotedPolls("4084294731","SJSU");
//		edao.showEnterpriseUnvotedPolls("4084555566","SJSU");
//		edao.showEnterprisePollByPollId("536dd2b9e4b0e36cd2b8e6d1");
		edao.showEnterprisePollByPollIdVotedByMe("4084294731", "536dd2b9e4b0e36cd2b8e6d1");
//		edao.showEnterpriseVoteResults("536dd2b9e4b0e36cd2b8e6d1");
//		edao.showEnterpriseVoteResultsGeo("536dd2b9e4b0e36cd2b8e6d1");
	}

}
