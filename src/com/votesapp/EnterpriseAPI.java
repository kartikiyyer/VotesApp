package com.votesapp;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.codehaus.jettison.json.JSONObject;

import com.votesapp.dao.EnterpriseDAO;
import com.votesapp.dao.IEnterpriseDAO;

@Path("EnterpriseVotesapp")
public class EnterpriseAPI {
	
	@POST
	@Path("/follow")
	public String followEnterprise(String followValues) throws Exception{

		System.out.println("in follow...");
		JSONObject jsonFollowValues=new JSONObject(followValues);
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.followEnterprise(jsonFollowValues);
		
		return result;
	}
	
	@GET
	@Path("my_follow_list/{user_name}")
	public String showEnterpriseFollowList(@PathParam ("user_name") String user_name) throws Exception{
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.showEnterpriseFollowList(user_name);

		return result;
	}
	
	@GET
	@Path("enterprise_list/{user_name}/{category}")
	public String showEnterpriseList(@PathParam ("user_name") String user_name,@PathParam ("category") String category) throws Exception{
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.showEnterpriseList(user_name,category);

		return result;
	}
	
	@POST
	@Path("/enterprise_poll/myVote")
	public String voteOnPoll(String voteOnPollValues) throws Exception{

		JSONObject jsonVoteOnPollValues=new JSONObject(voteOnPollValues);
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.voteOnPoll(jsonVoteOnPollValues);

		return result;
	}
	
	@GET
	@Path("enterprise_poll/voted/{user_name}/{enterprise_name}")
	public String showEnterpriseVotedPolls(@PathParam ("user_name") String user_name,@PathParam ("enterprise_name") String enterprise_name) throws Exception{
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.showEnterpriseVotedPolls(user_name,enterprise_name);

		return result;
	}
	
	@GET
	@Path("enterprise_poll/unvoted/{user_name}/{enterprise_name}")
	public String showEnterpriseUnvotedPolls(@PathParam ("user_name") String user_name,@PathParam ("enterprise_name") String enterprise_name) throws Exception{
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.showEnterpriseUnvotedPolls(user_name,enterprise_name);

		return result;
	}
	
	@GET
	@Path("enterprise_poll/ById/{pollId}")
	public String showPollById(@PathParam ("pollId") String pollId) throws Exception{
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.showEnterprisePollByPollId(pollId);

		return result;
	}
	
	
	@GET
	@Path("enterprise_poll/ById/{username}/{pollId}")
	public String showEnterprisePollByPollIdVotedByMe(@PathParam ("username") String username,@PathParam ("pollId") String pollId) throws Exception{
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.showEnterprisePollByPollIdVotedByMe(username,pollId);
		return result;
	}
	
	
	@GET
	@Path("enterprise_poll/voteResult/{pollId}")
	public String showEnterpriseVoteResults(@PathParam ("pollId") String pollId) throws Exception{
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.showEnterpriseVoteResults(pollId);

		return result;
	}
	
	@GET
	@Path("enterprise_poll/voteResultGeo/{pollId}")
	public String showEnterpriseVoteResultsGeo(@PathParam ("pollId") String pollId) throws Exception{
		IEnterpriseDAO iEnterpriseDAO=new EnterpriseDAO();
		String result=iEnterpriseDAO.showEnterpriseVoteResultsGeo(pollId);

		return result;
	}
	
}
