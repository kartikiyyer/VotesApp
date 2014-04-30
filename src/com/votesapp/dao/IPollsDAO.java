package com.votesapp.dao;

import org.codehaus.jettison.json.JSONObject;

public interface IPollsDAO {
	public String createPoll(JSONObject jsonCreatePollValues) throws Exception;
	
	public String deletePoll(String jsonDeletePollId) throws Exception;
	
	public String showAllPolls(String user_name) throws Exception;
	
	public String showPollsByGroup(String userName) throws Exception;
	
	public String showAllPollsAssignedToMe(String userName) throws Exception;
	
	public String showPollsByCategory(String categoryName,String user_name) throws Exception;
	
	public String showMyPolls(String userName) throws Exception;
	
	public String voteOnPoll(JSONObject pollResults) throws Exception;
	
//	public String showPollResult(String pollResults) throws Exception;
	public String showPollByPollId(String pollId) throws Exception;
	
	public String getPollOptionCount(String pollId) throws Exception;
}
