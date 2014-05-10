package com.votesapp.dao;

import org.codehaus.jettison.json.JSONObject;

public interface IEnterpriseDAO {
	public String followEnterprise(JSONObject jsonFollowValues) throws Exception;
	
	public String showEnterpriseList(String userName,String category) throws Exception;
	
	public String showEnterpriseVotedPolls(String user_name,String enterprise_name) throws Exception;
	
	public String showEnterpriseUnvotedPolls(String user_name,String enterprise_name) throws Exception;
	
	public String showEnterprisePollByPollId(String pollId) throws Exception;
	
	public String voteOnPoll(JSONObject jsonVoteOnPollValues) throws Exception;
	
	public String showEnterpriseVoteResults(String pollId) throws Exception;
	
	public String showEnterpriseVoteResultsGeo(String pollId) throws Exception;
	
}
