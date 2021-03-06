package connective.teamup.download.actions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.Agent;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DuplicateParticipantException;
import connective.teamup.download.services.EmailService;

/**
 * @author Kyle McCreary
 *
 * Action bean to save edited trading partner information from the Carrier Admin pages.
 */
public class SaveTradingPartnerNew implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveTradingPartnerNew.class);
	private static final String PASSWORD_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	/**
	 * Constructor for SaveTradingPartner.
	 */
	public SaveTradingPartnerNew()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		String nextPage = TradingPartnerListView.getUserDefaultView(req, serverInfo);
		String jsonArray ="";
		Gson gson = new Gson();
		AgentInfo agent = null;
		CarrierInfo carrierInfo = null;
		boolean keyLinkFile = false;
		String pword ="";
		try
		{
			// Parse the agency info from the page
			String newAgentFlag = req.getParameter("newAgent");
			String agentID = req.getParameter("agentId");
			carrierInfo = serverInfo.getCarrierInfo();
			if (carrierInfo.getImportFileCreator().equalsIgnoreCase("KEYLINK"))
				keyLinkFile = true;
			if (agentID != null)
			{
				agentID = agentID.trim().toUpperCase();
				if (agentID.indexOf(' ') > 0)
				{
					StringBuffer buf = new StringBuffer();
					for (int i=0; i < agentID.length(); i++)
					{
						char c = agentID.charAt(i);
						if (c != ' ')
							buf.append(c);
					}
					agentID = buf.toString();
				}
			}
			boolean newAgent = (newAgentFlag != null && newAgentFlag.equals("Y"));
			if (newAgent)
			{
				// TODO - Need to verify that the agent (or a participant) does
				//        not already exist for this agentID
				agent = op.createAgentInfo(agentID);
			}
			else
			{
				agent = op.getAgentInfo(agentID);
				if (agent == null){
					agent = op.createAgentInfo(agentID);
				}
				else{
					agent.loadParticipantsFromDb();
					pword = agent.getPassword();
				}
			}
			agent.setName(req.getParameter("name"));
			agent.setAmsVer(req.getParameter("amsVer"));
			agent.setContactName(req.getParameter("contactName"));
			agent.setContactEmail(req.getParameter("contactEmail"));
			if(!"-1".equals(req.getParameter("locationState")))
				agent.setLocationState(req.getParameter("locationState"));
			else
				agent.setLocationState(null);
			
			agent.setCity(req.getParameter("city"));
			agent.setStateId(req.getParameter("agentState"));
			agent.setZip(req.getParameter("zip"));
			agent.setAgentDestAddress(req.getParameter("destAddress"));
			
			String dlDir = req.getParameter("remoteDir");
			if (dlDir == null)
				dlDir = "";
			agent.setRemoteDir(dlDir);
			
			dlDir = req.getParameter("remoteClaimDir");
			if (dlDir == null)
				dlDir = "";
			agent.setRemoteClaimDir(dlDir);
			
			dlDir = req.getParameter("remotePolicyDir");
			if (dlDir == null)
				dlDir = "";
			agent.setRemotePolicyDir(dlDir);
	
			// AMSID is a foreign key, so if not entered we need to set to NULL
			boolean amsChanged = false;
			String amsID = req.getParameter("amsId");
			if (amsID == null || amsID.equals(""))
			{
				agent.setAms(null);
			}
			else if (agent.getAms() == null || agent.getAms().getId() == null || 
				!agent.getAms().getId().equals(amsID))
			{
				AmsInfo	ams = op.getAmsInfo(amsID);
				agent.setAms(ams);
				amsChanged = true;
			}
			if("on".equals(req.getParameter("chkDefault")) && agent.getAms()!= null) {
				agent.setRemoteDir(agent.getAms().getDefaultDir());
				agent.setRemoteClaimDir(agent.getAms().getDefaultClaimDirectory());
				agent.setRemotePolicyDir(agent.getAms().getDefaultPolicyDirectory());
			}
			/*String phoneArea = req.getParameter("phone_area");
			String phonePrefix = req.getParameter("phone_prefix");
			String phoneSuffix = req.getParameter("phone_suffix");
			String phoneExt = req.getParameter("phone_ext");
			String contactPhone = phoneArea + phonePrefix + phoneSuffix;
			if (phoneExt != null)
				contactPhone += phoneExt.trim();*/
			String contactPhone = req.getParameter("contactPhone");
			if(contactPhone!= null && !"".equals(contactPhone)) {
				contactPhone = contactPhone.replaceAll("[^0-9]","");
				agent.setContactPhone(contactPhone);
			}
			
			//String pword = req.getParameter("Password");
			if(pword== null || pword.length()<1)
				pword = makePasswd();
			
			String resetPassword = req.getParameter("resetPassword");
			if("true".equals(resetPassword)) {
				if(req.getParameter("newPassword")!= null 
						&& !"".equals(req.getParameter("newPassword"))){
					pword  = req.getParameter("newPassword");
				}
				else{
					pword = makePasswd();					
				}

			}
			if (pword != null && !pword.trim().equals(""))
			{
				agent.setPassword(pword.trim());
				
				if (!newAgent && agent.isRegistered() && !agent.isClientAppRegistered())
				{
					// Send an email to the agent contact with their new download URL
					createPasswordChangedEmail(serverInfo, agent, req, op);
				}
			}
			
			if (newAgent){
				agent.setStatus(AgentInfo.STATUS_INACTIVE);
				agent.setInteractive("Y");
			}
			
			
			String activeFlag = req.getParameter("active");
			boolean active = (activeFlag != null && activeFlag.equals("true"));
			if(!active){
				agent.setActive(active);
				agent.setStatus(AgentInfo.STATUS_DISABLED);
				agent.setPassword(makePasswd());
			}else{
				agent.setActive(active);
			}
			
			
			String testFlag = req.getParameter("testAgent");
			agent.setTestAgent(testFlag != null && testFlag.equals("true"));
			
			if (newAgent)
			{
				AgentInfo existingAgent = op.getAgentInfo(req.getParameter("agentId").toUpperCase());
				if (existingAgent != null)
				{
					// This agent ID already exists in the database
					serverInfo.setAgentInfo(req.getSession(), agent);
					String msg = "Agent ID [" +agent.getAgentId() +"] already Exist";
					req.getSession().setAttribute("errorMessage", msg);
					JSONROOT.put("Result", "ERROR");
					JSONROOT.put("Message", "Agent ID [" +agent.getAgentId() +"] already Exist");
					jsonArray = gson.toJson(JSONROOT);
					//return "menu.addnew.duplicate";
					return jsonArray;
				}
			}
			
			String resetFlag = req.getParameter("resetTradingP");
			if("true".equals(resetFlag)) 
			{
				// Reset the registered flag to require the agent to go through
				// the registration process again -- 10/07/2003, kwm
				agent.setStatus(AgentInfo.STATUS_UNREGISTERED);
				agent.setRemoteDir("");
			}
			
			// Check if agent uses participant codes
			String partCodesFlag = req.getParameter("partcodes");
			if (partCodesFlag != null && partCodesFlag.equals("Y"))
			{
				if (serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME))
					nextPage = "tplist.participants.filenames";
				else
					nextPage = "tplist.participants.codes";
			}
			
			
			try
			{
				// Save new or updated agent info to the database
				agent.save();

				String filename = req.getParameter("filename");
				if (filename == null || filename.equals(""))
				{
					filename = agentID;
				}
				else
				{
					int n = filename.indexOf(".");
					if (n == 0)
						filename = "";
					else if (n > 0)
						filename = filename.substring(0, filename.indexOf("."));
				}
				agent.setKeylinkFile(filename.toUpperCase());
				
			
			}
			catch (DuplicateParticipantException dupEx)
			{
				LOGGER.error(dupEx);
				serverInfo.setDuplicateParticipantInfo(req.getSession(), dupEx);
				nextPage = "tplist.edit.invalid";
				agent.setStatus(AgentInfo.STATUS_DISABLED);
				agent.save();
			}
			
			if (!newAgent)
			{
				// Check for manual vendor settings update
				boolean updateAmsSettings = false;
				String updateFlag = req.getParameter("updateSystemSettings");
				if("true".equals(updateFlag))
					updateAmsSettings = true;
				
				// If the AMS system was changed, update the agent's default filename
				// to match the new system
				AmsInfo ams = agent.getAms();
				if (ams != null && ams.getCompanyFilename() != null && 
					(updateAmsSettings || agent.getDefaultFilename() == null || 
										  !ams.getCompanyFilename().equals(agent.getDefaultFilename())))
				{
					agent.setDefaultFilename(ams.getCompanyFilename());
					agent.updateFilenameToDb();
				}
			}
			
			// Update any group memberships
			AgentGroupInfo[] groups = op.getAllAgentGroups();
			if (groups != null)
			{
				Hashtable groupHash = new Hashtable();
				AgentGroupInfo[] existingGroups = agent.getAgentGroups(AgentGroupInfo.TYPE_AGENTS_MISC);
				if (existingGroups != null)
				{
					for (int i=0; i < existingGroups.length; i++)
						groupHash.put(existingGroups[i].getName(), existingGroups[i]);
				}
				
				String[] pageGroups = req.getParameterValues("groupMember");
				if (pageGroups != null && pageGroups.length > 0)
				{
					for (int i=0; i < pageGroups.length; i++)
					{
						if (pageGroups[i] != null && !pageGroups[i].equals(""))
						{
							if (groupHash.get(pageGroups[i]) == null)
							{
								// Add group membership
								op.addAgentToGroup(pageGroups[i], AgentGroupInfo.TYPE_AGENTS_MISC, agent.getAgentId());
							}
							else
							{
								// Remove the group from the hashtable
								groupHash.remove(pageGroups[i]);
							}
						}
					}
				}
				
				// Remove any existing group memberships that were unchecked
				Iterator it = groupHash.keySet().iterator();
				while (it.hasNext())
				{
					String groupName = (String) it.next();
					AgentGroupInfo groupInfo = (AgentGroupInfo) groupHash.get(groupName);
					groupInfo.removeMember(agent.getAgentId());
				}
			}
			
			// If indicated by user, send a registration email to the agent contact
			String sendEmail = req.getParameter("resendRegEmail");
			if (sendEmail != null && sendEmail.equals("true"))
			{
				createNewAgentEmail(serverInfo, agent, req, op);
			}
			JSONROOT.put("Result", "OK");
			Agent agentDisp = op.LoadAgent(agent.getAgentId(), keyLinkFile);
			JSONROOT.put("Record", agentDisp);
			jsonArray = gson.toJson(JSONROOT);

		}

		catch (SQLException e)
		{
			LOGGER.error(e);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", e.toString());
			jsonArray = gson.toJson(JSONROOT);
			//throw new ActionException("Error saving agent configuration", e);

		}finally{
			return jsonArray;

		}
	}
	public Agent converToDisplay(AgentInfo agent) {
		Agent agentDisp = new Agent();
		agentDisp.setAgentId(agent.getAgentId());
		//agentDisp.setActive(agent.get);
		//agentDisp.setAms(agent.getAms());
		return agentDisp;
		
	}
	/**
	 * Creates and sends an email to a newly entered agent
	 * 
	 * @param agent The agent information bean
	 * 
	 * @return True if email was successfully created, otherwise false.
	 */
	private boolean createNewAgentEmail(ServerInfo serverInfo, AgentInfo agent, HttpServletRequest req, DatabaseOperation op)
	{
		if (agent == null || agent.getContactEmail().equals(""))
			return false;
			
		boolean successful = true;
		
		try
		{
			CustomTextFactory factory = null;
			if (serverInfo.getCarrierInfo().isClientAppUsed())
				factory = new CustomTextFactory(CustomTextFactory.TEXT_NEW_AGENT_CLIENT_APP, CustomTextFactory.TYPE_EMAIL, serverInfo, agent, op);
			else
				factory = new CustomTextFactory(CustomTextFactory.TEXT_NEW_AGENT, CustomTextFactory.TYPE_EMAIL, serverInfo, agent, op);
			factory.setLoginUrl(serverInfo.getRequestUrl(req, "/agency"));
			String message = factory.getText();
			String subject = factory.getEmailSubject();
			
			String htmlMsg = null;
			if (serverInfo.getCarrierInfo().isEmailAsHtml())
				htmlMsg = factory.getHtml();
			
			EmailService.getInstance().sendEMail(agent.getContactEmail(), subject, message, htmlMsg);
			
			// Update the agent status to show that invitation email has been sent
			agent.setSentAgentInvitation();
			agent.save();
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			System.out.println("Error sending new agent welcome email:  " + e.getMessage());
			e.printStackTrace(System.out);
			successful = false;
		}
		
		return successful;
	}
	
	/**
	 * Creates and sends an email to an agent notifying them of a password/security key change.
	 * 
	 * @param agent The agent information bean
	 * 
	 * @return True if email was successfully created, otherwise false.
	 */
	private boolean createPasswordChangedEmail(ServerInfo serverInfo, AgentInfo agent, HttpServletRequest req, DatabaseOperation op)
	{
		if (agent == null || agent.getContactEmail().equals(""))
			return false;
			
		boolean successful = true;
		
		try
		{
			CarrierInfo carrier = serverInfo.getCarrierInfo();
			String subject = carrier.getName() + " download registration";
			
			// Build urls using info from the request
			String url = serverInfo.getRequestUrl(req, "/agency");
			String keyName = "key";
			
				//keyName = serverInfo.getPropertyValue(serverInfo.PROP_SECURITY_PASSWORD);
			String dlUrl = url + "?action=dlstart&" + keyName + "=";
			dlUrl += serverInfo.getSecurityProvider().getSecurityKey(agent.getAgentId(), agent.getAgentId(), agent.getPassword());
			
			CustomTextFactory factory = new CustomTextFactory(
				CustomTextFactory.TEXT_DL_URL_CHANGE, CustomTextFactory.TYPE_EMAIL,
				serverInfo, agent, op);
			factory.setLoginUrl(url);
			factory.setDownloadUrl(dlUrl);
			String message = factory.getText();
			
			String htmlMsg = null;
			if (serverInfo.getCarrierInfo().isEmailAsHtml())
				htmlMsg = factory.getHtml();
			
			EmailService.getInstance().sendEMail(agent.getContactEmail(), subject, message, htmlMsg);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			System.out.println(e.getMessage());
			successful = false;
		}
		
		return successful;
	}
	private static String makePasswd(){
		String password ="";
		   for( int i = 0; i < 8; i++ ) {
		   password+=PASSWORD_CHARS.charAt(((Double)(Math.floor(Math.random()*PASSWORD_CHARS.length()+1))).intValue());
		}
		   return password;
	}
}
