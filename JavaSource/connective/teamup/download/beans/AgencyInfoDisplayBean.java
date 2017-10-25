package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DuplicateParticipantException;
import connective.teamup.download.db.ParticipantInfo;


/**
 * @author Kyle McCreary
 *
 * Display bean used for displaying agency/trading partner info to JSP
 * pages for both carrier and agency admin.
 */
public class AgencyInfoDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(AgencyInfoDisplayBean.class);
	
	private String agentId;
	private String agentName;
	private String amsId;
	private String amsName;
	private String amsVersion;
	private String remoteDirectory;
	private String importFile;
	private String importFilePrompt;
	private String contactName;
	private String contactEmail;
	private String contactPhone;
	private String downloadLink;
	private String loginLink = null;
	private String instructionFile;
	private String interactiveFlag;
	private String statusDesc;
	private String locationState;
	private String migrationGroup;
	private String rolloutGroup;
	private boolean agentActive = false;
	private boolean agentDisabled = false;
	private boolean agentRegistered = false;
	private boolean agentLive = false;
	private boolean newAgent = false;
	private boolean passwordBlank = false;
	private boolean idByFilename = false;
	private boolean agentChangeInfo = true;
	private boolean testAgent = false;
	private boolean clientAppRegistered = false;
	
	private boolean keyUpdated = false;
	private boolean filenameInvalid = false;
	private boolean duplicateAgent = false;
	private String duplicateMsg = null;
	
	private String newRegistration;
	private String tpSortOrder = "";
	private String originatingPage = "";
	private CarrierInfo carrierInfo = null;
	
	private List amsIds = new ArrayList();
	private List amsNames = new ArrayList();
	private List amsDefaultDirs = new ArrayList();
	private List amsDirectoryNotes = new ArrayList();
	private List amsFileNotes = new ArrayList();
	
	private List stateList = null;
	private List participants = null;
	private List agentGroups = null;
	private List groupMember = null;
	

	/**
	 * Constructor for AgencyInfoDisplayBean
	 */
	public AgencyInfoDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		this.newAgent = true;

		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			if (carrierInfo.getImportFileCreator().equalsIgnoreCase("KEYLINK"))
				importFilePrompt = "KeyLink File";
			else
				importFilePrompt = "Import Filename";
			idByFilename = (carrierInfo.getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME));
			agentChangeInfo = carrierInfo.isAgentInfoChangeAllowed();

/*			downloadTestFile = op.getPropertyValue(ServerInfo.PROP_TESTFILE);
			if (downloadTestFile != null && !downloadTestFile.equals(""))
			{
				if (downloadTestFile.lastIndexOf("\\") > 0)
				{
					int n = downloadTestFile.lastIndexOf("\\");
					downloadTestFile = downloadTestFile.substring(n+1);
				}
				else if (downloadTestFile.lastIndexOf("/") > 0)
				{
					int n = downloadTestFile.lastIndexOf("/");
					downloadTestFile = downloadTestFile.substring(n+1);
				}
			}
*/			
			// Parse the current registration status
			newRegistration = req.getParameter("newRegistration");
			if (newRegistration == null)
				newRegistration = "";
			
			AgentInfo agentBean = serverInfo.getAgentInfo(req.getSession(), op);
			if (agentBean == null)
			{
				// Initialize the agency info
				agentId = "";
				agentName = "";
				amsId = "";
				amsName = "";
				amsVersion = "";
				remoteDirectory = "";
				importFile = "";
				contactName = "";
				contactEmail = "";
				contactPhone = "";
				passwordBlank = true;
				interactiveFlag = "Y";
				statusDesc = "";
				locationState = "";
				agentActive = true;
				agentDisabled = false;
				agentRegistered = false;
				agentLive = false;
				testAgent = false;
				clientAppRegistered = false;
			}
			else
			{
				// Load the agency info from the info bean
				newAgent = (!agentBean.isActive() && !agentBean.isRegistered() && agentBean.getKeylinkFile().equals(""));
				
				// Load the agency info
				agentId = agentBean.getAgentId();
				agentName = agentBean.getName();
				amsId = agentBean.getAms().getId();
				amsName = agentBean.getAms().getDisplayName();
				amsVersion = agentBean.getAmsVer();
				remoteDirectory = agentBean.getRemoteDir();
				importFile = agentBean.getKeylinkFile();
				contactName = agentBean.getContactName();
				contactEmail = agentBean.getContactEmail();
				contactPhone = agentBean.getContactPhone();
				locationState = agentBean.getLocationState();
				passwordBlank = (agentBean.getPassword() == null || agentBean.getPassword().trim().equals(""));
				interactiveFlag = agentBean.getInteractiveFlag();
				agentActive = (agentBean.isActive() || newAgent);
				agentDisabled = !agentActive;
				agentRegistered = agentBean.isRegistered();
				agentLive = agentBean.isLive();
				statusDesc = agentBean.getStatusDescription();
				testAgent = agentBean.isTestAgent();
				clientAppRegistered = agentBean.isClientAppRegistered();
				
				if (agentRegistered)
				{
					// Build urls using info from the request
					String serverName = req.getServerName();
					int port = req.getServerPort();
					String servletPath = req.getServletPath();
					String contextPath = req.getContextPath();
					
					String key = serverInfo.getSecurityProvider().getSecurityKey(agentBean.getAgentId(), agentBean.getAgentId(), agentBean.getPassword());
					String keyName = "key";
						//keyName = serverInfo.getPropertyValue(serverInfo.PROP_SECURITY_PASSWORD);

					String baseLink = serverInfo.getRequestUrl(req, "/agency");
					downloadLink = baseLink + "?action=dlstart&amp;" + keyName + "=" + key;
				}
			}
			
				
			// Get the Trading Partner List sort order from the request (if applicable)
			tpSortOrder = req.getParameter("current_sort");
			if (tpSortOrder == null)
				tpSortOrder = "";
			
			// Check for any duplicate participant info
			DuplicateParticipantException dupEx = serverInfo.getDuplicateParticipantInfo(req.getSession());
			if (dupEx != null && dupEx.getMessageCount() > 0)
			{
				StringBuffer msg = new StringBuffer(dupEx.getMessage(0));
				for (int i=1; i < dupEx.getMessageCount(); i++)
				{
					msg.append("<BR>");
					msg.append(dupEx.getMessage(i));
				}
				duplicateMsg = msg.toString();
				serverInfo.setDuplicateParticipantInfo(req.getSession(), null);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error preparing agency info display bean", e);
			throw new DisplayBeanException("Error preparing agency info display bean", e);
		}
	}
	
	/**
	 * Loads the list of agency vendor systems from the database.
	 * @param serverInfo The server info bean containing the database connection info
	 */
	public void loadAmsTable(DatabaseOperation op) throws DisplayBeanException
	{
		try
		{
			AmsInfo[] amslist = op.getAmsInfoList();
			for (int i=0; i < amslist.length; i++)
			{
				amsIds.add(amslist[i].getId());
				amsNames.add(amslist[i].getDisplayName());
				amsDefaultDirs.add(escapeForHtml(amslist[i].getCompanyDir()));
				amsDirectoryNotes.add(escapeForHtml(amslist[i].getDirectoryNotes()));
				amsFileNotes.add(escapeForHtml(amslist[i].getFilenameNotes()));
			}
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			throw new DisplayBeanException("Error loading agency vendor system table", e);
		}
	}
	
	private String escapeForHtml(String data)
	{
		StringBuffer str = new StringBuffer("");
		for (int i=0; i < data.length(); i++)
		{
			if (data.charAt(i) == '\\')
				str.append("\\\\");
			else if (data.charAt(i) == '"')
				str.append("&quot;");
			else
				str.append(data.charAt(i));
		}
		
		return str.toString();
	}

	/**
	 * Returns the agent ID.
	 * @return String
	 */
	public String getAgentId()
	{
		return agentId;
	}

	/**
	 * Returns the agent name.
	 * @return String
	 */
	public String getAgentName()
	{
		if (agentName == null)
			return "";
		else
			return agentName;
	}
	
	/**
	 * Returns the number of ams systems defined.
	 * @return int
	 */
	public int getAmsCount()
	{
		return amsIds.size();
	}

	/**
	 * Returns the default download directory for the specified ams.
	 * @return String
	 */
	public String getAmsDefaultDir(int index)
	{
		if (index >= amsDefaultDirs.size())
			return "";
		else
			return (String) amsDefaultDirs.get(index);
	}

	/**
	 * Returns the vendor-specific notes regarding the download directory.
	 * @return String
	 */
	public String getAmsDirectoryNote(int index)
	{
		if (index >= amsDirectoryNotes.size())
			return "";
		else
			return (String) amsDirectoryNotes.get(index);
	}

	/**
	 * Returns the vendor-specific notes regarding the download filename.
	 * @return String
	 */
	public String getAmsFileNote(int index)
	{
		if (index >= amsFileNotes.size())
			return "";
		else
			return (String) amsFileNotes.get(index);
	}

	/**
	 * Returns the ams ID for the agent's system.
	 * @return String
	 */
	public String getAmsId()
	{
		if (amsId == null)
			return "";
		else
			return amsId;
	}

	/**
	 * Returns the unique identifier for the specified ams.
	 * @return String
	 */
	public String getAmsId(int index)
	{
		if (index >= amsIds.size())
			return "";
		
		return (String) amsIds.get(index);
	}

	/**
	 * Returns the name of the agency's ams system.
	 * @return String
	 */
	public String getAmsName()
	{
		if (amsName == null)
			return "";
		else
			return amsName;
	}

	/**
	 * Returns the system name for the specified ams.
	 * @return String
	 */
	public String getAmsName(int index)
	{
		if (index >= amsNames.size())
			return "";
		else
			return (String) amsNames.get(index);
	}

	/**
	 * Returns the current version of the agency's ams system.
	 * @return String
	 */
	public String getAmsVersion()
	{
		if (amsVersion == null)
			return "";
		else
			return amsVersion;
	}

	/**
	 * Returns the agency contact's email address.
	 * @return String
	 */
	public String getContactEmail()
	{
		if (contactEmail == null)
			return "";
		else
			return contactEmail;
	}

	/**
	 * Returns the agency contact's name.
	 * @return String
	 */
	public String getContactName()
	{
		if (contactName == null)
			return "";
		else
			return contactName;
	}

	/**
	 * Returns the agency contact's phone number (formatted).
	 * @return String
	 */
	public String getContactPhone()
	{
		if (contactPhone == null || contactPhone.trim().length() < 10)
			return "";
		
		String phone = "(" + getContactPhoneArea() + ") " +
					   getContactPhonePrefix() + "-" + getContactPhoneSuffix();
		
		if (getContactPhoneExt().length() > 0)
			phone += " x." + getContactPhoneExt();
		
		return phone;
	}
	
	/**
	 * Returns the agency contact's phone number area code.
	 * @return String
	 */
	public String getContactPhoneArea()
	{
		if (contactPhone == null || contactPhone.trim().length() < 3)
			return "";
		
		if (contactPhone.length() == 3)
			return contactPhone;
		else
			return contactPhone.substring(0, 3);
	}
	
	/**
	 * Returns the agency contact's phone number extension.
	 * @return String
	 */
	public String getContactPhoneExt()
	{
		if (contactPhone == null || contactPhone.trim().length() <= 10)
			return "";
		
		return contactPhone.trim().substring(10);
	}
	
	/**
	 * Returns the agency contact's phone number prefix.
	 * @return String
	 */
	public String getContactPhonePrefix()
	{
		if (contactPhone == null || contactPhone.trim().length() < 6)
			return "";
		
		if (contactPhone.length() == 6)
			return contactPhone.substring(3);
		else
			return contactPhone.substring(3, 6);
	}
	
	/**
	 * Returns the agency contact's phone number suffix.
	 * @return String
	 */
	public String getContactPhoneSuffix()
	{
		if (contactPhone == null || contactPhone.trim().length() < 10)
			return "";
		
		if (contactPhone.trim().length() == 10)
			return contactPhone.substring(6);
		else
			return contactPhone.substring(6, 10);
	}

	/**
	 * Returns the agency's download link.
	 * @return String
	 */
	public String getDownloadLink()
	{
		if (downloadLink == null)
			return "";
		else
			return downloadLink;
	}
	
	/**
	 * Returns the name of the download test file.
	 * @return String
	 */
/*	public String getDownloadTestFile()
	{
		if (downloadTestFile == null)
			return "";
		else
			return downloadTestFile;
	}
*/	
	/**
	 * Returns any current duplicate participant/filename messages.
	 * @return String
	 */
	public String getDuplicateMessage()
	{
		return duplicateMsg;
	}

	/**
	 * Returns the name of the instruction PDF file.
	 * @return String
	 */
	public String getInstructionFile()
	{
		if (instructionFile == null)
			return "";
		else
			return instructionFile;
	}
	
	/**
	 * Returns the interactive flag description.
	 * @return String
	 */
	public String getInteractiveDesc()
	{
		if (interactiveFlag == null)
			return "";
		else if (interactiveFlag.equalsIgnoreCase("Y"))
			return "Interactive";
		else
			return "Scheduled (non-interactive)";
	}
	
	/**
	 * Returns the interactive flag.
	 * @return String
	 */
	public String getInteractiveFlag()
	{
		if (interactiveFlag == null)
			return "";
		else
			return interactiveFlag;
	}
	
	/**
	 * Returns a randomly generated alpha-numeric password.
	 * @return String
	 */
	public String getRandomPassword()
	{
		String time = Long.toString(System.currentTimeMillis(), 36);
		StringBuffer ret = new StringBuffer("");
		
		for (int i=time.length()-1; i >= 0; i--)
		{
			char ch = time.charAt(i);
			if (ch == 'l')
				ret.append('1');
			else
				ret.append(ch);
		}
		
		return ret.toString();
	}

	/**
	 * Returns the agency's defined remote directory for download files.
	 * @return String
	 */
	public String getRemoteDirectory()
	{
		if (remoteDirectory == null)
			return "";
		else
			return remoteDirectory;
	}

	/**
	 * Returns the download directory escaped to use as an URL.
	 * @return String
	 */
	public String getRemoteUrl()
	{
		String temp = getRemoteDirectory();
		StringBuffer url = new StringBuffer("");
		
		for (int i=0; i < temp.length(); i++)
		{
			char c = temp.charAt(i);
			if (c == '\\')
				url.append("/");
			else
				url.append(c);
		}
		
		return url.toString();
	}

	/**
	 * Returns true if agent is set as disabled (inactive).
	 * @return boolean
	 */
	public boolean isAgentDisabled()
	{
		return agentDisabled;
	}

	/**
	 * Returns true if agent is set to receive "live" downloads via TEAMWork Download.
	 * @return boolean
	 */
	public boolean isAgentLive()
	{
		return agentLive;
	}

	/**
	 * Returns true if the agent has completed the download registration process.
	 * @return boolean
	 */
	public boolean isAgentRegistered()
	{
		return agentRegistered;
	}

	/**
	 * Returns true if this is a new agent, false for an existing agent.
	 * @return boolean
	 */
	public boolean isNewAgent()
	{
		return newAgent;
	}

	/**
	 * Returns the agentActive.
	 * @return boolean
	 */
	public boolean isAgentActive()
	{
		return agentActive;
	}

	/**
	 * Returns true if the password has not yet been set.
	 * @return boolean
	 */
	public boolean isPasswordBlank()
	{
		return passwordBlank;
	}

	/**
	 * Sets the agent active flag.
	 * @param agentActive The agentActive value to set
	 */
	public void setAgentActive(boolean agentActive)
	{
		this.agentActive = agentActive;
	}

	/**
	 * Returns true if the key has been updated.
	 * @return boolean
	 */
	public boolean isKeyUpdated()
	{
		return keyUpdated;
	}

	/**
	 * Sets the keyUpdated flag.
	 * @param keyUpdated The keyUpdated value to set
	 */
	public void setKeyUpdated(boolean keyUpdated)
	{
		this.keyUpdated = keyUpdated;
	}

	/**
	 * Returns the carrier's contact email address.
	 * @return String
	 */
	public String getCarrierEmail()
	{
		return carrierInfo.getContactEmail();
	}

	/**
	 * Returns the carrier's display name.
	 * @return String
	 */
	public String getCarrierName()
	{
		return carrierInfo.getName();
	}

	/**
	 * Returns the carrier's short name.
	 * @return String
	 */
	public String getCarrierShortName()
	{
		return carrierInfo.getShortName();
	}

	/**
	 * Returns the originating page.
	 * @return String
	 */
	public String getOriginatingPage()
	{
		return originatingPage;
	}

	/**
	 * Sets the originating page.
	 * @param originatingPage The page to set
	 */
	public void setOriginatingPage(String originatingPage)
	{
		if (originatingPage == null)
			this.originatingPage = "";
		else
			this.originatingPage = originatingPage;
	}

	/**
	 * Returns the import file name.
	 * @return String
	 */
	public String getImportFile()
	{
		return importFile;
	}

	/**
	 * Returns true if the download (Keylink) file is invalid (e.g., duplicate entry).
	 * @return boolean
	 */
	public boolean isFilenameInvalid()
	{
		return filenameInvalid;
	}
	
	/**
	 * Sets the "filename invalid" flag.
	 * @param flag The flag value to set
	 */
	public void setFilenameInvalid(boolean flag)
	{
		this.filenameInvalid = flag;
	}

	/**
	 * Returns the banner graphic filename.
	 * @return String
	 */
	public String getBannerGraphic()
	{
		return carrierInfo.getBannerGraphicFile();
	}

	/**
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}
	
	/**
	 * Sets the agent/trading partner info.
	 * @param agentBean The agency info bean
	 */
	public void setAgencyInfo(AgentInfo agentBean)
	{
		if (agentBean == null || agentBean.getName() == null || agentBean.getName().equals(""))
		{
			newAgent = true;
			
			if (agentBean != null)
				agentId = agentBean.getAgentId();
		}
		else
		{
			newAgent = (!agentBean.isActive() && !agentBean.isRegistered() && agentBean.getKeylinkFile().equals(""));
			
			// Load the agency info
			agentId = agentBean.getAgentId();
			agentName = agentBean.getName();
			amsId = agentBean.getAms().getId();
			amsName = agentBean.getAms().getDisplayName();
			amsVersion = agentBean.getAmsVer();
			remoteDirectory = agentBean.getRemoteDir();
			importFile = agentBean.getKeylinkFile();
			contactName = agentBean.getContactName();
			contactEmail = agentBean.getContactEmail();
			contactPhone = agentBean.getContactPhone();
			locationState = agentBean.getLocationState();
			passwordBlank = (agentBean.getPassword() == null || agentBean.getPassword().trim().equals(""));
			interactiveFlag = agentBean.getInteractiveFlag();
			agentActive = (agentBean.isActive() || newAgent);
			agentDisabled = !agentActive;
			agentRegistered = agentBean.isRegistered();
			agentLive = agentBean.isLive();
			statusDesc = agentBean.getStatusDescription();
			testAgent = agentBean.isTestAgent();
			clientAppRegistered = agentBean.isClientAppRegistered();
			
			// Load the participant info
			if (agentBean.getParticipantCount() > 0)
				participants = new ArrayList(agentBean.getParticipants());
		}
	}

	/**
	 * Sets the link to the instruction file.
	 * @param filename The filename to set
	 */
	public void setInstructionFile(HttpServletRequest req, String filename)
	{
		// Build urls using info from the request
		String serverName = req.getServerName();
		int port = req.getServerPort();
		String servletPath = req.getServletPath();
		String contextPath = req.getContextPath();
		
		if (filename != null && filename.trim().length() > 0)
			this.instructionFile = contextPath + servletPath + "/" + filename.trim();
	}
	
	/**
	 * Returns the 'newAgent' flag.
	 * @return String
	 */
	public String getNewAgentFlag()
	{
		return (newAgent ? "Y" : "N");
	}

	/**
	 * Sets the 'newAgent' flag.
	 * @param newAgent The newAgent to set
	 */
	public void setNewAgent(boolean newAgent)
	{
		this.newAgent = newAgent;
	}

	/**
	 * Returns true if this is a duplicate agent.
	 * @return boolean
	 */
	public boolean isDuplicateAgent()
	{
		return duplicateAgent;
	}

	/**
	 * Sets the 'duplicateAgent' flag.
	 * @param duplicateAgent The duplicateAgent to set
	 */
	public void setDuplicateAgent(boolean duplicateAgent)
	{
		this.duplicateAgent = duplicateAgent;
	}

	/**
	 * Returns the idByFilename.
	 * @return boolean
	 */
	public boolean isIdByFilename() {
		return idByFilename;
	}

	/**
	 * Returns the importFilePrompt.
	 * @return String
	 */
	public String getImportFilePrompt() {
		return importFilePrompt;
	}

	/**
	 * Returns the current Trading Partner List sort order.
	 * @return String
	 */
	public String getTpSortOrder()
	{
		return tpSortOrder;
	}

	/**
	 * Returns the newRegistration.
	 * @return String
	 */
	public String getNewRegistration() {
		return newRegistration;
	}
	
	/**
	 * Returns the indicated participant code.
	 * @param index The participant code to get
	 * @return String
	 */
	public String getParticipantCode(int index)
	{
		if (participants == null || index >= participants.size())
			return "";
		
		ParticipantInfo part = (ParticipantInfo) participants.get(index);
		return part.getParticipantCode();
	}
	
	/**
	 * Returns a text list of participant codes for this agent.
	 * @return String
	 */
	public String getParticipantCodeList()
	{
		StringBuffer list = new StringBuffer("");
		for (int i=0; i < getParticipantCount(); i++)
		{
			if (i > 0)
				list.append(", ");
			list.append(getParticipantCode(i));
		}
		
		return list.toString();
	}
	
	/**
	 * Returns the indicated participant filename.
	 * @param index The filename to get
	 * @return String
	 */
	public String getParticipantFilename(int index)
	{
		if (participants == null || index >= participants.size())
			return "";
		
		ParticipantInfo part = (ParticipantInfo) participants.get(index);
		return part.getFilename();
	}
	
	/**
	 * Returns the number of participants for this agency.
	 * @return int
	 */
	public int getParticipantCount()
	{
		if (participants == null)
			return 0;
		return participants.size();
	}
	
	/**
	 * Sets the list of participants for this agency.
	 * @param participants The vector of participant info beans
	 */
	public void setParticipants(Vector participants)
	{
		if (participants != null && participants.size() > 0)
			this.participants = new ArrayList(participants);
	}

	/**
	 * Returns the URL to the agency login page, with the agent's key included for auto-login.<p>
	 * Returns <b>null</b> if agent is not yet registered.
	 * @return String
	 */
	public String getLoginLink()
	{
		return loginLink;
	}


	/**
	 * Sets the loginLink.
	 * @param loginLink The loginLink to set
	 */
	public void setLoginLink(String loginLink)
	{
		this.loginLink = loginLink;
	}

	/**
	 * Returns true if agent is allowed to change their own contact info.
	 * @return
	 */
	public boolean canAgentChangeContactInfo()
	{
		return agentChangeInfo;
	}

	/**
	 * Adds an agency group to the list.
	 * @param groupInfo - The group info bean
	 * @param isMember - True if agent is a member of this group
	 */
	public void addAgentGroup(AgentGroupInfo groupInfo, boolean isMember)
	{
		if (groupInfo != null)
		{
			if (agentGroups == null || groupMember == null)
			{
				agentGroups = new ArrayList();
				groupMember = new ArrayList();
			}
			
			agentGroups.add(groupInfo);
			String flag = "N";
			if (isMember)
				flag = "Y";
			groupMember.add(flag);
		}
	}

	public int getGroupCount()
	{
		if (agentGroups == null)
			return 0;
		return agentGroups.size();
	}

	public String getGroupName(int index)
	{
		if (agentGroups == null || index >= agentGroups.size())
			return "";
		
		AgentGroupInfo groupInfo = (AgentGroupInfo) agentGroups.get(index);
		return groupInfo.getName();
	}

	public String getGroupDescription(int index)
	{
		if (agentGroups == null || index >= agentGroups.size())
			return "";
		
		AgentGroupInfo groupInfo = (AgentGroupInfo) agentGroups.get(index);
		return groupInfo.getDescription();
	}

	public boolean isGroupMember(int index)
	{
		if (groupMember == null || index >= groupMember.size())
			return false;
		
		String flag = (String) groupMember.get(index);
		return (flag != null && flag.equals("Y"));
	}

	/**
	 * @return
	 */
	public boolean isTestAgent()
	{
		return testAgent;
	}

	/**
	 * @return
	 */
	public boolean isClientAppRegistered()
	{
		return clientAppRegistered;
	}

	public String getClientAppFlag()
	{
		if (clientAppRegistered)
			return "Yes";
		return "No";
	}

	/**
	 * @return
	 */
	public String getStatusDesc()
	{
		return statusDesc;
	}

	public String getLocationState()
	{
		return locationState;
	}

	public String getLocationStateName()
	{
		return States.getStateName(locationState);
	}

	public int getStateCount()
	{
		if (stateList == null)
			stateList = States.getStateList();
		return stateList.size();
	}

	public String getStateAbbreviation(int index)
	{
		if (stateList == null)
			stateList = States.getStateList();
		if (index >= stateList.size())
			return "";
		return States.getStateAbbreviation((String) stateList.get(index));
	}

	public String getStateName(int index)
	{
		if (stateList == null)
			stateList = States.getStateList();
		if (index >= stateList.size())
			return "";
		return (String) stateList.get(index);
	}

	public String getStateSelected(int index)
	{
		if (stateList == null)
			stateList = States.getStateList();
		
		String selected = "";
		if (locationState != null && index < stateList.size() && locationState.equals(getStateAbbreviation(index)))
			selected = " selected";
		return selected;
	}

	/**
	 * @return
	 */
	public String getMigrationGroup()
	{
		return migrationGroup;
	}

	/**
	 * @param string
	 */
	public void setMigrationGroup(String string)
	{
		migrationGroup = string;
	}

	/**
	 * @return
	 */
	public String getRolloutGroup()
	{
		return rolloutGroup;
	}

	/**
	 * @param string
	 */
	public void setRolloutGroup(String string)
	{
		rolloutGroup = string;
	}

}
