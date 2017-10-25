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
 * Display bean used for displaying vendor system-specific setup and runtime
 * info to JSP pages for the agency admin app.
 */
public class VendorSetupDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(VendorSetupDisplayBean.class);
	
	private String agentId = null;
	private String amsId = null;
	private String amsName = null;
	private String setupNotes = null;
	private String runtimeNotes = null;
	private String interactiveNotes = null;
	private String remoteDirectory = null;
	private String importFile = null;
	private String batchFileNotes = null;
	private boolean interactive = false;
	private boolean scheduled = false;
	private boolean customSystem = false;
	
	private CarrierInfo carrierInfo = null;
	

	/**
	 * Constructor for AgencyInfoDisplayBean
	 */
	public VendorSetupDisplayBean()
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

			// Load the agent info
			AmsInfo ams = null;
			AgentInfo agentBean = null;
			if (serverInfo != null)
				agentBean = serverInfo.getAgentInfo(req.getSession(), op);
			if (agentBean == null)
			{
				agentId = req.getParameter("agentId");
				if (agentId != null)
					agentBean = op.getAgentInfo(agentId);
			}
			if (agentBean != null)
			{
				importFile = agentBean.getDefaultFilename();
				interactive = agentBean.isInteractive();
				remoteDirectory = agentBean.getRemoteDir();
				scheduled = !agentBean.isInteractive();
				if (agentBean != null)
				{
					agentId = agentBean.getAgentId();
					ams = agentBean.getAms();
					if (ams != null)
						amsId = ams.getId();
				}
			}
			
			// Load the vendor system info
			boolean batchFileChecked = false;
			amsId = req.getParameter("amsid");
			if (amsId != null && !amsId.equals(""))
				ams = op.getAmsInfo(amsId);
			if (ams != null)
			{
				amsName = ams.getDisplayName();
				if (importFile == null)
					importFile = ams.getCompanyFilename();
				if (remoteDirectory == null)
					remoteDirectory = ams.getCompanyDir();
				
				batchFileChecked = ams.isBatchFileFlag();
				customSystem = ams.isCustomSystem();
				
				// Retrieve the custom help text for this vendor system
				CustomTextFactory runtimeFactory = new CustomTextFactory(CustomTextFactory.VENDOR_HELP_RUNTIME, ams, serverInfo, op);
				runtimeNotes = runtimeFactory.getText();
				CustomTextFactory setupFactory = new CustomTextFactory(CustomTextFactory.VENDOR_HELP_SETUP, ams, serverInfo, op);
				setupNotes = setupFactory.getText();
			}
			
			if (interactive)
			{
				interactiveNotes = "From your Windows desktop, double-click on the icon titled '";
				if (agentBean == null)
					interactiveNotes += "&lt;agent_id&gt;";
				else
					interactiveNotes += agentBean.getAgentId();
				interactiveNotes += " Download from " + carrierInfo.getShortName() + 
									".'  This will open a browser and automatically download any new files to your system.";
			}
			
			if (batchFileChecked)
			{
				String batchFileUrl;
				if (agentBean == null || agentBean.getAgentId() == null || agentBean.getAgentId().equals(""))
				{
					// Carrier app -- show generic message
					batchFileUrl = "javascript:show_batchfile_msg()";
				}
				else
				{
					// Agency app -- point to the FileGeneratorServlet
					batchFileUrl = serverInfo.getRequestUrl(req, "/dlclean.bat");
					batchFileUrl += "?agent=" + agentBean.getAgentId();
				}
				
				batchFileNotes = "A clean-up utility can be copied to your desktop to remove any old download files.  " +
						"<A href=\"" + batchFileUrl + "\">Click here</A> to download this file and save it to your Windows desktop.  " +
						"Then run the \"dlclean.bat\" icon from your desktop each day after successfully importing your download files into your agency management system.";
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving the vendor system setup or help files", e);
			throw new DisplayBeanException("Error occurred retrieving the vendor system setup or help files", e);
		}
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
	 * Returns the carrier name.
	 * @return String
	 */
	public String getCarrierName()
	{
		return carrierInfo.getName();
	}

	/**
	 * Returns the vendor system ID.
	 * @return String
	 */
	public String getAmsId()
	{
		if (amsId == null)
			return "";
		return amsId;
	}

	/**
	 * Returns the vendor system name.
	 * @return String
	 */
	public String getAmsName()
	{
		if (amsName == null)
			return "";
		return amsName;
	}

	/**
	 * Returns the clean-up batch file notes (if applicable).
	 * @return String
	 */
	public String getBatchFileNotes()
	{
		if (batchFileNotes == null)
			return "";
		return batchFileNotes;
	}

	/**
	 * Returns the import filename.
	 * @return String
	 */
	public String getImportFile()
	{
		if (importFile == null)
			return "";
		return importFile;
	}

	/**
	 * Returns true if agent is set for interactive (non-scheduled) download.
	 * @return boolean
	 */
	public boolean isInteractive()
	{
		return interactive;
	}

	/**
	 * Returns the interactive notes.
	 * @return String
	 */
	public String getInteractiveNotes()
	{
		if (interactiveNotes == null)
			return "";
		return interactiveNotes;
	}

	/**
	 * Returns the interactive notes, escaped for HTML.
	 * @return String
	 */
	public String getInteractiveNotesEditable()
	{
		if (interactiveNotes == null)
			return "";
		return escapeForHtml(interactiveNotes);
	}

	/**
	 * Returns the download directory.
	 * @return String
	 */
	public String getRemoteDirectory()
	{
		if (remoteDirectory == null)
			return "";
		return remoteDirectory;
	}

	/**
	 * Returns the runtime notes.
	 * @return String
	 */
	public String getRuntimeNotes()
	{
		if (runtimeNotes == null)
			return "";
		return runtimeNotes;
	}

	/**
	 * Returns the runtime notes, escaped for HTML.
	 * @return String
	 */
	public String getRuntimeNotesEditable()
	{
		if (runtimeNotes == null)
			return "";
		return escapeForHtml(runtimeNotes);
	}

	/**
	 * Returns true if agent is set for scheduled download.
	 * @return boolean
	 */
	public boolean isScheduled()
	{
		return scheduled;
	}

	/**
	 * Returns the setup notes.
	 * @return String
	 */
	public String getSetupNotes()
	{
		if (setupNotes == null)
			return "";
		return setupNotes;
	}

	/**
	 * Returns the setup notes, escaped for HTML.
	 * @return String
	 */
	public String getSetupNotesEditable()
	{
		if (setupNotes == null)
			return "";
		return escapeForHtml(setupNotes);
	}

	/**
	 * Sets the interactive notes.
	 * @param interactiveNotes The notes to set
	 */
	public void setInteractiveNotes(String interactiveNotes)
	{
		this.interactiveNotes = interactiveNotes;
	}

	/**
	 * Returns the agent ID.
	 * @return String
	 */
	public String getAgentId()
	{
		if (agentId == null)
			return "";
		return agentId;
	}

	/**
	 * Returns true if vendor system is a carrier-created system, false if supported and maintained
	 * by CTI in the central registration admin app.
	 * @return boolean
	 */
	public boolean isCustomSystem()
	{
		return customSystem;
	}

	private String escapeForHtml(String data)
	{
		StringBuffer str = new StringBuffer("");
		for (int i=0; i < data.length(); i++)
		{
			char c = data.charAt(i);
			
			if (c == '<')
				str.append("&lt;");
			else if (c == '>')
				str.append("&gt;");
			else if (c == '&')
				str.append("&amp;");
			else
				str.append(c);
		}
		
		return str.toString();
	}

}
