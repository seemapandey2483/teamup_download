package connective.teamup.download.beans;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Display bean used for displaying a new or existing agency vendor system.
 */
public class AmsInfoDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(AmsInfoDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private boolean newAms = false;
	private boolean duplicateSystem = false;
	private int agentCount = 0;
	
	private String id = "";
	private String vendor = "";
	private String name = "";
	private String note = "";
	private String displayName = "";
	private int sortSequence = 0;
	private String registrationControlType = "";
	private boolean batchFileFlag = false;
	private boolean customSystem = false;
	
	private String defaultDirectory = "";
	private String companyDirectory = "";
	private boolean changeDirectory = false;
	private String directoryNotes = "";
	
	private String defaultFilename = "";
	private String companyFilename = "";
	private boolean changeFilename = false;
	private boolean agentChangeFilename = false;
	private String filenameIncrType = "";
	private boolean appendFlag = false;
	private String filenameNotes = "";

	private String setupNotes = null;
	private String runtimeNotes = null;


	/**
	 * Constructor for AmsInfoDisplayBean.
	 */
	public AmsInfoDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		String amsID = req.getParameter("amsid");
		try
		{
			// Load carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			
			if (amsID != null && !amsID.equals(""))
			{
				// Parse the amsID from the request
				AmsInfo amsInfo = op.getAmsInfo(amsID.trim().toUpperCase());
				if (amsInfo != null)
				{
					// Load the vendor system info
					id = amsInfo.getId();
					vendor = amsInfo.getVendor();
					name = amsInfo.getName();
					note = amsInfo.getNote();
					displayName = amsInfo.getDisplayName();
					sortSequence = amsInfo.getSortSequence();
					registrationControlType = amsInfo.getRegistrationControlType();
					batchFileFlag = amsInfo.isBatchFileFlag();
					customSystem = amsInfo.isCustomSystem();
					
					defaultDirectory = amsInfo.getDefaultDir();
					companyDirectory = amsInfo.getCompanyDir();
					changeDirectory = amsInfo.isCompanyChangeDirectory();
					directoryNotes = amsInfo.getDirectoryNotes();
					
					defaultFilename = amsInfo.getDefaultFilename();
					companyFilename = amsInfo.getCompanyFilename();
					changeFilename = amsInfo.isCompanyChangeFilename();
					agentChangeFilename = amsInfo.isAgentChangeFilenameFlag();
					filenameIncrType = amsInfo.getFilenameIncrementType();
					appendFlag = amsInfo.isAppendFlag();
					filenameNotes = amsInfo.getFilenameNotes();
					
					if (customSystem)
					{
						// Get the number of agents currently configured for this vendor system
						AgentInfo[] agentList = op.getAgentsForAms(id);
						if (agentList != null)
							agentCount = agentList.length;
					}
					else
					{
						// Load the setup and runtime notes for display only
						CustomTextFactory runtimeFactory = new CustomTextFactory(CustomTextFactory.VENDOR_HELP_RUNTIME, amsInfo, serverInfo, op);
						runtimeNotes = runtimeFactory.getText();
						CustomTextFactory setupFactory = new CustomTextFactory(CustomTextFactory.VENDOR_HELP_SETUP, amsInfo, serverInfo, op);
						setupNotes = setupFactory.getText();
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred loading vendor system info", e);
			throw new DisplayBeanException("Error occurred loading vendor system info", e);
		}
		
		if (id == null || id.equals(""))
			newAms = true;
	}
	
	/**
	 * Sets the vendor system info.
	 * @param amsInfo The ams info bean
	 */
	public void setAmsInfo(AmsInfo amsInfo)
	{
		if (amsInfo != null)
		{
			// Load the vendor system info
			id = amsInfo.getId();
			vendor = amsInfo.getVendor();
			name = amsInfo.getName();
			note = amsInfo.getNote();
			displayName = amsInfo.getDisplayName();
			sortSequence = amsInfo.getSortSequence();
			defaultDirectory = amsInfo.getDefaultDir();
			directoryNotes = amsInfo.getDirectoryNotes();
			defaultFilename = amsInfo.getDefaultFilename();
			filenameNotes = amsInfo.getFilenameNotes();
			appendFlag = amsInfo.isAppendFlag();
			agentChangeFilename = amsInfo.isAgentChangeFilenameFlag();
			registrationControlType = amsInfo.getRegistrationControlType();
			batchFileFlag = amsInfo.isBatchFileFlag();
			companyDirectory = amsInfo.getCompanyDir();
			changeDirectory = amsInfo.isCompanyChangeDirectory();
			companyFilename = amsInfo.getCompanyFilename();
			changeFilename = amsInfo.isCompanyChangeFilename();
			filenameIncrType = amsInfo.getFilenameIncrementType();
			customSystem = amsInfo.isCustomSystem();
		}
	}

	/**
	 * Returns the append flag.
	 * @return boolean
	 */
	public boolean isAppendFlag()
	{
		return appendFlag;
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
	 * Returns the default download directory.
	 * @return String
	 */
	public String getDefaultDirectory()
	{
		return defaultDirectory;
	}

	/**
	 * Returns the default download directory, escaped for javascript.
	 * @return String
	 */
	public String getDefaultDirectoryEscaped()
	{
		if (defaultDirectory == null)
			return "";
		
		StringBuffer buf = new StringBuffer();
		for (int i=0; i < defaultDirectory.length(); i++)
		{
			char c = defaultDirectory.charAt(i);
			if (c == '\\')
				buf.append(c);		// add an extra back slash ('\')
			buf.append(c);
		}
		return buf.toString();
	}

	/**
	 * Returns the default download filename.
	 * @return String
	 */
	public String getDefaultFilename()
	{
		return defaultFilename;
	}

	/**
	 * Returns the directory notes.
	 * @return String
	 */
	public String getDirectoryNotes()
	{
		return directoryNotes;
	}

	/**
	 * Returns the filename notes.
	 * @return String
	 */
	public String getFilenameNotes()
	{
		return filenameNotes;
	}

	/**
	 * Returns the vendor system full display name.
	 * @return String
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * Returns the vendor system id.
	 * @return String
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Returns the system name.
	 * @return String
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns true if this is a new system.
	 * @return boolean
	 */
	public boolean isNewAms()
	{
		return newAms;
	}

	/**
	 * Returns the registration ActiveX control type.
	 * @return String
	 */
	public String getRegistrationControlType()
	{
		return registrationControlType;
	}

	/**
	 * Returns the sort sequence.
	 * @return int
	 */
	public int getSortSequence()
	 {
		return sortSequence;
	}

	/**
	 * Returns true if the agent is allowed to change the default download
	 * filename for this system.
	 * @return boolean
	 */
	public boolean isAgentChangeFilename()
	{
		return agentChangeFilename;
	}

	/**
	 * Sets the "new system" flag.
	 * @param newAms True to add a new system, false to edit an existing system
	 */
	public void setNewAms(boolean newAms)
	{
		this.newAms = newAms;
	}

	/**
	 * Returns true if this is a duplicate vendor system.
	 * @return boolean
	 */
	public boolean isDuplicateSystem()
	{
		return duplicateSystem;
	}

	/**
	 * Sets the 'duplicateSystem' flag.
	 * @param duplicateSystem The flag to set
	 */
	public void setDuplicateSystem(boolean duplicateSystem)
	{
		this.duplicateSystem = duplicateSystem;
	}

	/**
	 * Returns the note associated with the system name.
	 * @return String
	 */
	public String getNote()
	{
		if (note == null)
			return "";
		return note;
	}

	/**
	 * Returns the vendor name.
	 * @return String
	 */
	public String getVendor()
	{
		if (vendor == null)
			return "";
		return vendor;
	}
	
	/**
	 * Returns the cleanup batch file flag.
	 * @return boolean
	 */
	public boolean isBatchFileFlag()
	{
		return batchFileFlag;
	}

	/**
	 * @return
	 */
	public boolean isChangeDirectory()
	{
		return changeDirectory;
	}

	/**
	 * @return
	 */
	public boolean isChangeFilename()
	{
		return changeFilename;
	}

	/**
	 * @return
	 */
	public String getCompanyDirectory()
	{
		return companyDirectory;
	}

	/**
	 * @return
	 */
	public String getCompanyFilename()
	{
		return companyFilename;
	}

	/**
	 * @return
	 */
	public boolean isCustomSystem()
	{
		return customSystem;
	}

	/**
	 * @return
	 */
	public String getFilenameIncrType()
	{
		return filenameIncrType;
	}

	/**
	 * @return
	 */
	public String getRuntimeNotes()
	{
		return runtimeNotes;
	}

	/**
	 * @return
	 */
	public String getSetupNotes()
	{
		return setupNotes;
	}

	public void setRuntimeNotes(String str)
	{
		if (str == null || str.trim().equals(""))
			runtimeNotes = null;
		else
			runtimeNotes = str;
	}

	public void setSetupNotes(String str)
	{
		if (str == null || str.trim().equals(""))
			setupNotes = null;
		else
			setupNotes = str;
	}

	/**
	 * @return
	 */
	public int getAgentCount()
	{
		return agentCount;
	}

}
