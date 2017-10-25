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
import connective.teamup.download.db.AmsClaimInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

public class AmsClaimInfoDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(AmsClaimInfoDisplayBean.class);
	
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

	
	//New Variables defined for Claim and Policy XML download 
	private boolean claimSupported =false;
	private boolean policyXMLSupported =false;
	private String defaultClaimDirectory = null;
	private boolean directoryClaimVariable = false;
	private boolean companyClaimChangeDirectory = false;
	private String regClaimControlType = null;
	private String deafultClaimFileName = null; 
	private String filenameClaimIncrementType = null; 
	private boolean companyChangeClaimFilename = false;
	private boolean agentChangeClaimFilename = false;


	private String defaultPolicyDirectory = null;
	private boolean directoryPolicyVariable = false;
	private boolean companyPolicyChangeDirectory = false;
	private String regPolicyControlType = null;
	private String deafultPolicyFileName = null; 
	private String filenamePolicyIncrementType = null; 
	private boolean companyChangePolicyFilename = false;
	private boolean agentChangePolicyFilename = false;
	
	private String companyClaimDir = null;
	private String companyPolicyDir = null;
	private String companyPolicyFilename = null;
	private String companyClaimFilename = null;

	/**
	 * Constructor for AmsInfoDisplayBean.
	 */
	public AmsClaimInfoDisplayBean()
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
				AmsClaimInfo amsClaimInfo = op.getAmsClaimInfo(amsID.trim().toUpperCase());
				AmsInfo amsInfo = op.getAmsInfo(amsID.trim().toUpperCase());
				if (amsClaimInfo != null)
				{
					// Load the vendor system info
					id = amsClaimInfo.getId();
					vendor = amsClaimInfo.getVendor();
					name = amsClaimInfo.getName();
					note = amsClaimInfo.getNote();
					displayName = amsClaimInfo.getDisplayName();
					sortSequence = amsClaimInfo.getSortSequence();
					registrationControlType = amsClaimInfo.getRegistrationControlType();
					batchFileFlag = amsClaimInfo.isBatchFileFlag();
					customSystem = amsClaimInfo.isCustomSystem();
					
					defaultDirectory = amsClaimInfo.getDefaultDir();
					companyDirectory = amsClaimInfo.getCompanyDir();
					changeDirectory = amsClaimInfo.isCompanyChangeDirectory();
					directoryNotes = amsClaimInfo.getDirectoryNotes();
					
					defaultFilename = amsClaimInfo.getDefaultFilename();
					companyFilename = amsClaimInfo.getCompanyFilename();
					changeFilename = amsClaimInfo.isCompanyChangeFilename();
					agentChangeFilename = amsClaimInfo.isAgentChangeFilenameFlag();
					filenameIncrType = amsClaimInfo.getFilenameIncrementType();
					appendFlag = amsClaimInfo.isAppendFlag();
					filenameNotes = amsClaimInfo.getFilenameNotes();
					
					claimSupported =amsClaimInfo.isClaimSupported();
					policyXMLSupported =amsClaimInfo.isPolicyXMLSupported();
					defaultClaimDirectory = amsClaimInfo.getDefaultClaimDirectory();
					directoryClaimVariable = amsClaimInfo.isDirectoryClaimVariable();
					companyClaimChangeDirectory = amsClaimInfo.isCompanyClaimChangeDirectory();
					regClaimControlType = amsClaimInfo.getRegClaimControlType();
					deafultClaimFileName = amsClaimInfo.getDeafultClaimFileName(); 
					filenameClaimIncrementType = amsClaimInfo.getFilenameClaimIncrementType(); 
					companyChangeClaimFilename = amsClaimInfo.isCompanyChangeClaimFilename();
					agentChangeClaimFilename = amsClaimInfo.isAgentChangeClaimFilename();
					
					defaultPolicyDirectory = amsClaimInfo.getDefaultPolicyDirectory();
					directoryPolicyVariable = amsClaimInfo.isDirectoryPolicyVariable();
					companyPolicyChangeDirectory = amsClaimInfo.isCompanyPolicyChangeDirectory();
					regPolicyControlType = amsClaimInfo.getRegPolicyControlType();
					deafultPolicyFileName = amsClaimInfo.getDeafultPolicyFileName(); 
					filenamePolicyIncrementType = amsClaimInfo.getFilenamePolicyIncrementType(); 
					companyChangePolicyFilename = amsClaimInfo.isCompanyChangePolicyFilename();
					agentChangePolicyFilename = amsClaimInfo.isAgentChangePolicyFilename();
					
					companyPolicyFilename = amsClaimInfo.getCompanyPolicyFilename();
					companyClaimFilename = amsClaimInfo.getCompanyClaimFilename();
					companyClaimDir = amsClaimInfo.getCompanyClaimDir();
					companyPolicyDir = amsClaimInfo.getCompanyPolicyDir();
					
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
	public void setAmsInfo(AmsClaimInfo amsClaimInfo)
	{
		if (amsClaimInfo != null)
		{
			// Load the vendor system info
			id = amsClaimInfo.getId();
			vendor = amsClaimInfo.getVendor();
			name = amsClaimInfo.getName();
			note = amsClaimInfo.getNote();
			displayName = amsClaimInfo.getDisplayName();
			sortSequence = amsClaimInfo.getSortSequence();
			defaultDirectory = amsClaimInfo.getDefaultDir();
			directoryNotes = amsClaimInfo.getDirectoryNotes();
			defaultFilename = amsClaimInfo.getDefaultFilename();
			filenameNotes = amsClaimInfo.getFilenameNotes();
			appendFlag = amsClaimInfo.isAppendFlag();
			agentChangeFilename = amsClaimInfo.isAgentChangeFilenameFlag();
			registrationControlType = amsClaimInfo.getRegistrationControlType();
			batchFileFlag = amsClaimInfo.isBatchFileFlag();
			companyDirectory = amsClaimInfo.getCompanyDir();
			changeDirectory = amsClaimInfo.isCompanyChangeDirectory();
			companyFilename = amsClaimInfo.getCompanyFilename();
			changeFilename = amsClaimInfo.isCompanyChangeFilename();
			filenameIncrType = amsClaimInfo.getFilenameIncrementType();
			customSystem = amsClaimInfo.isCustomSystem();
			claimSupported =amsClaimInfo.isClaimSupported();
			policyXMLSupported =amsClaimInfo.isPolicyXMLSupported();
			defaultClaimDirectory = amsClaimInfo.getDefaultClaimDirectory();
			directoryClaimVariable = amsClaimInfo.isDirectoryClaimVariable();
			companyClaimChangeDirectory = amsClaimInfo.isCompanyClaimChangeDirectory();
			regClaimControlType = amsClaimInfo.getRegClaimControlType();
			deafultClaimFileName = amsClaimInfo.getDeafultClaimFileName(); 
			filenameClaimIncrementType = amsClaimInfo.getFilenameClaimIncrementType(); 
			companyChangeClaimFilename = amsClaimInfo.isCompanyChangeClaimFilename();
			agentChangeClaimFilename = amsClaimInfo.isAgentChangeClaimFilename();
			
			defaultPolicyDirectory = amsClaimInfo.getDefaultPolicyDirectory();
			directoryPolicyVariable = amsClaimInfo.isDirectoryPolicyVariable();
			companyPolicyChangeDirectory = amsClaimInfo.isCompanyPolicyChangeDirectory();
			regPolicyControlType = amsClaimInfo.getRegPolicyControlType();
			deafultPolicyFileName = amsClaimInfo.getDeafultPolicyFileName(); 
			filenamePolicyIncrementType = amsClaimInfo.getFilenamePolicyIncrementType(); 
			companyChangePolicyFilename = amsClaimInfo.isCompanyChangePolicyFilename();
			agentChangePolicyFilename = amsClaimInfo.isAgentChangePolicyFilename();
			
			companyPolicyFilename = amsClaimInfo.getCompanyPolicyFilename();
			companyClaimFilename = amsClaimInfo.getCompanyClaimFilename();
			companyClaimDir = amsClaimInfo.getCompanyClaimDir();
			companyPolicyDir = amsClaimInfo.getCompanyPolicyDir();
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

	public boolean isPolicyXMLSupported() {
		return policyXMLSupported;
	}

	public void setPolicyXMLSupported(boolean policyXMLSupported) {
		this.policyXMLSupported = policyXMLSupported;
	}

	public String getDefaultClaimDirectory() {
		return defaultClaimDirectory;
	}

	public void setDefaultClaimDirectory(String defaultClaimDirectory) {
		this.defaultClaimDirectory = defaultClaimDirectory;
	}

	public boolean isDirectoryClaimVariable() {
		return directoryClaimVariable;
	}

	public void setDirectoryClaimVariable(boolean directoryClaimVariable) {
		this.directoryClaimVariable = directoryClaimVariable;
	}

	public boolean isCompanyClaimChangeDirectory() {
		return companyClaimChangeDirectory;
	}

	public void setCompanyClaimChangeDirectory(boolean companyClaimChangeDirectory) {
		this.companyClaimChangeDirectory = companyClaimChangeDirectory;
	}

	public String getRegClaimControlType() {
		return regClaimControlType;
	}

	public void setRegClaimControlType(String regClaimControlType) {
		this.regClaimControlType = regClaimControlType;
	}

	public String getDeafultClaimFileName() {
		return deafultClaimFileName;
	}

	public void setDeafultClaimFileName(String deafultClaimFileName) {
		this.deafultClaimFileName = deafultClaimFileName;
	}

	public String getFilenameClaimIncrementType() {
		return filenameClaimIncrementType;
	}

	public void setFilenameClaimIncrementType(String filenameClaimIncrementType) {
		this.filenameClaimIncrementType = filenameClaimIncrementType;
	}

	public boolean isCompanyChangeClaimFilename() {
		return companyChangeClaimFilename;
	}

	public void setCompanyChangeClaimFilename(boolean companyChangeClaimFilename) {
		this.companyChangeClaimFilename = companyChangeClaimFilename;
	}

	public boolean isAgentChangeClaimFilename() {
		return agentChangeClaimFilename;
	}

	public void setAgentChangeClaimFilename(boolean agentChangeClaimFilename) {
		this.agentChangeClaimFilename = agentChangeClaimFilename;
	}

	public String getDefaultPolicyDirectory() {
		return defaultPolicyDirectory;
	}

	public void setDefaultPolicyDirectory(String defaultPolicyDirectory) {
		this.defaultPolicyDirectory = defaultPolicyDirectory;
	}

	public boolean isCompanyPolicyChangeDirectory() {
		return companyPolicyChangeDirectory;
	}

	public void setCompanyPolicyChangeDirectory(boolean companyPolicyChangeDirectory) {
		this.companyPolicyChangeDirectory = companyPolicyChangeDirectory;
	}

	public String getRegPolicyControlType() {
		return regPolicyControlType;
	}

	public void setRegPolicyControlType(String regPolicyControlType) {
		this.regPolicyControlType = regPolicyControlType;
	}

	public String getDeafultPolicyFileName() {
		return deafultPolicyFileName;
	}

	public void setDeafultPolicyFileName(String deafultPolicyFileName) {
		this.deafultPolicyFileName = deafultPolicyFileName;
	}

	public String getFilenamePolicyIncrementType() {
		return filenamePolicyIncrementType;
	}

	public void setFilenamePolicyIncrementType(String filenamePolicyIncrementType) {
		this.filenamePolicyIncrementType = filenamePolicyIncrementType;
	}

	public boolean isCompanyChangePolicyFilename() {
		return companyChangePolicyFilename;
	}

	public void setCompanyChangePolicyFilename(boolean companyChangePolicyFilename) {
		this.companyChangePolicyFilename = companyChangePolicyFilename;
	}

	public boolean isAgentChangePolicyFilename() {
		return agentChangePolicyFilename;
	}

	public void setAgentChangePolicyFilename(boolean agentChangePolicyFilename) {
		this.agentChangePolicyFilename = agentChangePolicyFilename;
	}

	public boolean isDirectoryPolicyVariable() {
		return directoryPolicyVariable;
	}

	public void setDirectoryPolicyVariable(boolean directoryPolicyVariable) {
		this.directoryPolicyVariable = directoryPolicyVariable;
	}

	public boolean isClaimSupported() {
		return claimSupported;
	}

	public void setClaimSupported(boolean claimSupported) {
		this.claimSupported = claimSupported;
	}

	public String getCompanyClaimDir() {
		return companyClaimDir;
	}

	public void setCompanyClaimDir(String companyClaimDir) {
		this.companyClaimDir = companyClaimDir;
	}

	public String getCompanyPolicyDir() {
		return companyPolicyDir;
	}

	public void setCompanyPolicyDir(String companyPolicyDir) {
		this.companyPolicyDir = companyPolicyDir;
	}

	public String getCompanyPolicyFilename() {
		return companyPolicyFilename;
	}

	public void setCompanyPolicyFilename(String companyPolicyFilename) {
		this.companyPolicyFilename = companyPolicyFilename;
	}

	public String getCompanyClaimFilename() {
		return companyClaimFilename;
	}

	public void setCompanyClaimFilename(String companyClaimFilename) {
		this.companyClaimFilename = companyClaimFilename;
	}

}
