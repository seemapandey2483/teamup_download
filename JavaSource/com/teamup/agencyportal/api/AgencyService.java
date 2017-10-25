package com.teamup.agencyportal.api;

import java.util.List;
import javax.servlet.ServletContext;
import com.ebix.api.dao.AgencyAPIDAO;
import com.ebix.api.model.AgentConfigInfo;
import com.ebix.api.model.MonthlyBill;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.services.EmailService;

public class AgencyService {
	private static AgencyAPIDAO agencyAPIDAO;
	
	static
	{
		if (agencyAPIDAO == null) {
			agencyAPIDAO = new AgencyAPIDAO();
		}
	}

	public AgentConfigInfo getAgentByAgentId(String agentId) throws Exception {
		return agencyAPIDAO.getAgentByAgentId(agentId);
	}

	public boolean validate(String agentId, String password) throws Exception {

		
		return agencyAPIDAO.validate(agentId, password);
	}

	public List<MonthlyBill> listMonthlyBill(Long date) throws Exception  {
		return agencyAPIDAO.listMonthlyBill(date);
	}
	public MonthlyBill getTransLog(Long date) throws Exception  {
		return agencyAPIDAO.getTransLog(date);
	}
	public List<MonthlyBill> getListTransLog(Long date) throws Exception  {
		return agencyAPIDAO.getListTransLog(date);
	}
	
	public static boolean disableAgent(String agentId, String flag) throws Exception  {
		
		return agencyAPIDAO.disableAgent(agentId, flag);
	}
	
	public static boolean createAgent(AgentConfigInfo agent,ServletContext context) throws Exception  {
		agencyAPIDAO.manageAgent(agent);
		try{
			ServerInfo serverInfo = new ServerInfo(context, "Download Config App");
			CustomTextFactory factory = new CustomTextFactory(CustomTextFactory.TEXT_NEW_AGENT_CLIENT_APP, CustomTextFactory.TYPE_EMAIL, serverInfo, new  AgentInfo(), null,true);
			AgentConfigInfo  agentConfigInfo= agencyAPIDAO.getAgentByAgentId(agent.getAgentId());
			String message = factory.getText();
			message = message.replace("Agent ID:  null", "Agent ID:  "+agentConfigInfo.getAgentId());
			message = message.replace("Password:  null", "Password:  "+agentConfigInfo.getPassword());
			String subject = factory.getEmailSubject();
			String email = agentConfigInfo.getEmail();
			String htmlMsg = factory.getHtml();
			htmlMsg = StringUtil.replaceWithPattern(htmlMsg,agentConfigInfo.getAgentId(),"null"); 
			htmlMsg = StringUtil.replaceWithPattern(htmlMsg,agentConfigInfo.getAgentId(),"null"); 
			htmlMsg = StringUtil.replaceWithPattern(htmlMsg,agentConfigInfo.getPassword(),"null"); 
			EmailService.getInstance().sendEMail(email, subject, message, htmlMsg);
			}catch(Exception e){e.printStackTrace();
				return false;
			}
		
		return true;
	}
	
}