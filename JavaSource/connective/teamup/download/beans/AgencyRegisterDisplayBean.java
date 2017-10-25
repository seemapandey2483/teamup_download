package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Display bean used to build the agency registration pages containing the
 * registration ActiveX controls.
 */
public class AgencyRegisterDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(AgencyRegisterDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private String agentId;
	private String agentName;
	private String agentDirectory;
	private String agentFilename;
	private String amsId;
	private String amsName;
	private String amsVersion;
	private String amsDirectory;
	private String amsDirectoryNote;
	private String amsFilename;
	private String vendorChanged;
	private boolean agentRegistered;
	private boolean agentChangeFilename;
	private boolean agentChangeInfo;
	
	private String controlType;
	private String classid;
	private String codebase;

	private List amsIds = new ArrayList();
	private List amsNames = new ArrayList();


	/**
	 * Constructor for AgencyRegisterDisplayBean.
	 */
	public AgencyRegisterDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			agentChangeInfo = carrierInfo.isAgentInfoChangeAllowed();
			
			// Load the agency info
			AgentInfo agentBean = serverInfo.getAgentInfo(req.getSession(), op);
			agentId = agentBean.getAgentId();
			agentName = agentBean.getName();
			agentDirectory = agentBean.getRemoteDir();
//			agentFilename = getSequentialFilename(agentBean.getCompanyFilename());
			agentFilename = agentBean.getDefaultFilename();
			agentRegistered = agentBean.isRegistered();
			
			// Load the agency vendor system info
			amsId = agentBean.getAms().getId();
			amsName = agentBean.getAms().getDisplayName();
			amsVersion = agentBean.getAmsVer();
			amsDirectory = agentBean.getAms().getCompanyDir();
			amsDirectoryNote = agentBean.getAms().getDirectoryNotes();
//			amsFilename = getSequentialFilename(agentBean.getAms().getCompanyFilename());
			amsFilename = agentBean.getAms().getCompanyFilename();
			agentChangeFilename = agentBean.getAms().isAgentChangeFilenameFlag();
			
			// Load the registration control attributes
			controlType = agentBean.getAms().getRegistrationControlType();
			this.classid = serverInfo.getRegControlClassid(controlType);
			this.codebase = serverInfo.getRegControlCodebase(controlType);
			
			// Pass the vendor changed flag through from the request
			vendorChanged = req.getParameter("vendor_changed");
			if (vendorChanged == null)
				vendorChanged = "";
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			throw new DisplayBeanException("Error building agency registration display bean", e);
		}
	}
	
	/**
	 * Returns a proper-looking filename, replacing sequential counter markers
	 * with '1', '01', etc.
	 * @param filename The original filename
	 */
	private String getSequentialFilename(String filename)
	{
		if (filename == null)
			return filename;
		
		boolean firstMarker = true;
		StringBuffer nameBuffer = new StringBuffer(filename);
		for (int i=nameBuffer.length()-1; i>= 0; i--)
		{
			if (nameBuffer.charAt(i) == '#')
			{
				if (firstMarker)
				{
					nameBuffer.setCharAt(i, '1');
					firstMarker = false;
				}
				else
				{
					nameBuffer.setCharAt(i, '0');
				}
			}
		}
		
		return nameBuffer.toString();
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
			}
		}
		catch (SQLException e)
		{
			LOGGER.error("Error loading agency vendor system table", e);
			throw new DisplayBeanException("Error loading agency vendor system table", e);
		}
	}

	/**
	 * Returns the remote download directory saved in the agency configuration.
	 * @return String
	 */
	public String getAgentDirectory()
	{
		return agentDirectory;
	}

	/**
	 * Returns the default download filename saved in the agency configuration.
	 * @return String
	 */
	public String getAgentFilename()
	{
		return agentFilename;
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
	 * Returns the agent's name.
	 * @return String
	 */
	public String getAgentName()
	 {
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
	 * Returns the default download directory for the agent's vendor system.
	 * @return String
	 */
	public String getAmsDirectory()
	{
		return amsDirectory;
	}

	/**
	 * Returns the default download directory notes for the agent's vendor system.
	 * @return String
	 */
	public String getAmsDirectoryNote()
	{
		if (amsDirectoryNote == null)
			return "";
		
		return amsDirectoryNote.trim();
	}

	/**
	 * Returns the default download filename for the agent's vendor system.
	 * @return String
	 */
	public String getAmsFilename()
	{
		return amsFilename;
	}

	/**
	 * Returns the amsId for the agent's vendor system.
	 * @return String
	 */
	public String getAmsId()
	{
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
	 * Returns the name of the agency's vendor system.
	 * @return String
	 */
	public String getAmsName()
	{
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
	 * Returns the software version for the agent's vendor system.
	 * @return String
	 */
	public String getAmsVersion()
	{
		return amsVersion;
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
	 * Returns the registration control classid.
	 * @return String
	 */
	public String getClassid()
	{
		return classid;
	}

	/**
	 * Returns the registration control codebase.
	 * @return String
	 */
	public String getCodebase()
	{
		return codebase;
	}
	
	/**
	 * Returns the registration control type for the selected vendor system.
	 * @return String
	 */
	public String getControlType()
	{
		return controlType;
	}

	/**
	 * Returns the 'agent registered' flag.
	 * @return boolean
	 */
	public boolean isAgentRegistered()
	{
		return agentRegistered;
	}

	/**
	 * Returns true if agent is allowed to change the default filename, otherwise false.
	 * @return boolean
	 */
	public boolean canAgentChangeFilename()
	{
		return agentChangeFilename;
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
	 * @return
	 */
	public String getVendorChanged()
	{
		return vendorChanged;
	}

}
