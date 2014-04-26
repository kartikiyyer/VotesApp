package com.votesapp;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


import com.votesapp.dao.*;

@Path("votesapp")
public class RestAPI {
	

	@GET
	@Path("/check/{values}")
	public String CheckService(@PathParam ("values") String values) throws JSONException{
		System.out.println("values: "+values);
		JSONObject jobj=new JSONObject();
		jobj.put("email", "rohan.tan@gmail.com");
		return jobj.toString();
	}

	@POST
	@Path("/poll")
	public String addPoll(String createPollValues) throws Exception{

		JSONObject jsonCreatePollValues=new JSONObject(createPollValues);
		IPollsDAO iPollsDAO=new PollsDAO();
		String result=iPollsDAO.createPoll(jsonCreatePollValues);

		return result;
	}
	
	@DELETE
	@Path("/poll")
	public String deletePoll(String pollId) throws Exception{

		IPollsDAO iPollsDAO=new PollsDAO();
		String result=iPollsDAO.deletePoll(pollId);

		return result;
	}
	
	@GET
	@Path("poll/All")
	public String showAllPolls() throws Exception{
		IPollsDAO iPollsDAO=new PollsDAO();
		String result=iPollsDAO.showAllPolls();

		return result;
	}
	
	@GET
	@Path("poll/ByCategory/{category_name}")
	public String showPollsByCategory(@PathParam ("category_name") String category_name) throws Exception{
		IPollsDAO iPollsDAO=new PollsDAO();
		String result=iPollsDAO.showPollsByCategory(category_name);

		return result;
	}
	
	@GET
	@Path("poll/myPolls/{user_name}")
	public String showMyPolls(@PathParam ("user_name") String user_name) throws Exception{
		IPollsDAO iPollsDAO=new PollsDAO();
		String result=iPollsDAO.showMyPolls(user_name);

		return result;
	}
	
	
	@GET
	@Path("poll/pollsAssigned/{user_name}")
	public String showpollsAssigned(@PathParam ("user_name") String user_name) throws Exception{
		IPollsDAO iPollsDAO=new PollsDAO();
		String result=iPollsDAO.showAllPollsAssignedToMe(user_name);

		return result;
	}
	
	@POST
	@Path("/poll/myVote")
	public String voteOnPoll(String voteOnPollValues) throws Exception{

		JSONObject jsonVoteOnPollValues=new JSONObject(voteOnPollValues);
		IPollsDAO iPollsDAO=new PollsDAO();
		String result=iPollsDAO.voteOnPoll(jsonVoteOnPollValues);

		return result;
	}
	
	@GET
	@Path("poll/voteResult/{pollId}")
	public String showVoteResults(@PathParam ("pollId") String pollId) throws Exception{
		IPollsDAO iPollsDAO=new PollsDAO();
		String result=iPollsDAO.getPollOptionCount(pollId);

		return result;
	}

}
