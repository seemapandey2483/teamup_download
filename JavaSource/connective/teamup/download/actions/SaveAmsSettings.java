package connective.teamup.download.actions;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;

/**
 * Action bean to save edited agency vendor system information from the carrier
 * admin pages.
 * 
 * @author Kyle McCreary
 */
public class SaveAmsSettings extends SortVendorSystems
{
	private static final Logger LOGGER = Logger.getLogger(SaveAmsSettings.class);
	/**
	 * Constructor for SaveAmsSettings.
	 */
	public SaveAmsSettings()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "menu.ams";
		
		try
		{
			// Parse the ams info from the page
			AmsInfo ams = null;
			String flag = req.getParameter("customSystem");
			boolean customSystem = (flag != null && flag.equals("Y"));
			String amsID = req.getParameter("amsid").trim().toUpperCase();
			String oldDefaultFilename = "";
			flag = req.getParameter("newsystem");
			boolean newSystem = (customSystem && flag != null && flag.equals("Y"));
			if (newSystem)
			{
				if (amsID.indexOf(' ') > 0)
				{
					StringBuffer buf = new StringBuffer();
					for (int i=0; i < amsID.length(); i++)
					{
						char c = amsID.charAt(i);
						if (c == ' ')
							buf.append('_');
						else
							buf.append(c);
					}
					amsID = buf.toString();
				}
				
				ams = op.createAmsInfo(amsID);
				ams.setCustomSystem(true);
			}
			else
			{
				ams = op.getAmsInfo(amsID);
				if (ams.getCompanyFilename() == null)
					ams.setCompanyFilename("");
				oldDefaultFilename = ams.getCompanyFilename();
			}
			
			String sortSeq = req.getParameter("sort_seq");
			if (sortSeq == null || sortSeq.equals(""))
				sortSeq = "0";
			ams.setSortSequence(Integer.parseInt(sortSeq));
			
			if (customSystem)
			{
				ams.setVendor(req.getParameter("vendor"));
				ams.setName(req.getParameter("amsname"));
				ams.setNote(req.getParameter("note"));
				ams.setRegistrationControlType(req.getParameter("control_type"));
				ams.setCompanyDir(req.getParameter("company_dir"));
				ams.setCompanyChangeDirectory(true);
				ams.setDefaultDir(ams.getCompanyDir());
				ams.setDirectoryNotes(req.getParameter("dir_notes"));
				ams.setCompanyFilename(req.getParameter("company_filename"));
				ams.setCompanyChangeFilename(true);
				ams.setDirectoryVariable(true);
				ams.setDefaultFilename(ams.getCompanyFilename());
				ams.setFilenameNotes(req.getParameter("filename_notes"));
				flag = req.getParameter("agent_change_flag");
				ams.setAgentChangeFilenameFlag(flag != null && flag.equals("Y"));
				ams.setFilenameIncrementType(req.getParameter("filename_incr_type"));
				ams.setAppendFlag(ams.getFilenameIncrementType() != null && ams.getFilenameIncrementType().equals("A"));
				flag = req.getParameter("batchfile_flag");
				ams.setBatchFileFlag(flag != null && flag.equals("Y"));
			}
			else
			{
				if (ams.isCompanyChangeDirectory())
					ams.setCompanyDir(req.getParameter("company_dir"));
				if (ams.isCompanyChangeFilename())
					ams.setCompanyFilename(req.getParameter("company_filename"));
			}
			ams.save();
			
			if (newSystem)
			{
				// Check to see if this system ID already exists in the database
				AmsInfo existingSystem = op.getAmsInfo(amsID);
				if (existingSystem == null || existingSystem.getId() == null || existingSystem.getId().equals(""))
				{
					// Duplicate system ID
					req.getSession().setAttribute("ams.info", ams);
					return "ams.edit.duplicate";
				}
			}
			else if (!ams.isAgentChangeFilenameFlag() && !ams.getCompanyFilename().equals(oldDefaultFilename))
			{
				// Update the default filename for all agents using this vendor system
				ArrayList badUpdates = new ArrayList();
				String filename = ams.getCompanyFilename();
				AgentInfo[] agent = op.getAgentsForAms(amsID);
				Exception saveExc = null;
				for (int i=0; i < agent.length; i++)
				{
					try
					{
						agent[i].setDefaultFilename(filename);
						agent[i].updateFilenameToDb();
					}
					catch (Exception e)
					{
						LOGGER.error(e);
						if (saveExc == null)
							saveExc = e;
						badUpdates.add(agent[i]);
					}
				}
				
				if (agent.length > 1 && agent.length == badUpdates.size())
				{
					System.out.println("Vendor sync error: error occurred for every agent for system " + ams.getDisplayName());
					if (saveExc != null)
					{
						System.out.println(saveExc.getMessage());
						saveExc.printStackTrace();
					}
					
					// Every agent update failed -- send an error alert email
					String to = CarrierInfo.getInstance().getErrorsEmail();
					String subject = CarrierInfo.getInstance().getShortName() + " TEAM-UP Download - Error saving vendor system info";
					String body = "TEAM-UP was unable to update the vendor system settings and archived files " + 
								  "for any of the agents currently configured to use " + ams.getDisplayName() + ".\n\n" +
								  "Please contact Ebix,Inc user support for assistance.";
					EmailService.getInstance().sendEMail(to, subject, body);
					
					// Send copy w/ stack trace to CTI tech support
					if (saveExc != null)
						EmailService.getInstance().sendTechSupportEmail(subject, body, saveExc);
					
					// Set error message and return to splash page
					serverInfo.setStatusMessage(req.getSession(), "Error occurred updating vendor system info - see email for details");
					return "splash";
				}
				else if (badUpdates.size() > 0)
				{
					// Retry all agent updates that failed the first time
					String agtList = null;
					int errorCount = 0;
					for (int i=0; i < badUpdates.size(); i++)
					{
						AgentInfo agentInfo = (AgentInfo) badUpdates.get(i);
						try
						{
							agentInfo.setDefaultFilename(filename);
							agentInfo.updateFilenameToDb();
						}
						catch (Exception e)
						{
							LOGGER.error(e);
							// Error occurred second time for agent
							System.out.println("Vendor sync error: error occurred twice for system " + ams.getDisplayName() + ", agent " + agentInfo.getAgentId());
							System.out.println(e.getMessage());
							e.printStackTrace();
							if (saveExc == null)
								saveExc = e;
							
							if (agtList == null)
								agtList = "";
							agtList += "  " + agentInfo.getAgentId() + " - " + agentInfo.getName() + "\n";
							errorCount++;
						}
					}
					
					if (agtList != null)
					{
						// Send an error alert email listing all agents that could not be updated
						String body = "The following ";
						if (errorCount == 1)
							body += "agent's";
						else
							body += "agents'";
						body += " vendor system settings and/or archived files could not be updated during the " +
								ams.getDisplayName() + " system update:\n\n" + agtList +
								"\nYou will need to update the vendor system defaults manually for the listed agent";
						if (errorCount > 1)
							body += "s";
						body += ".  Please contact Ebix,Inc user support if you have any questions.";
						
						String to = CarrierInfo.getInstance().getErrorsEmail();
						String subject = CarrierInfo.getInstance().getShortName() + " TEAM-UP Download - Error saving vendor system info";
						EmailService.getInstance().sendEMail(to, subject, body);
						
						// Send copy w/ stack trace to CTI tech support
						if (saveExc != null)
							EmailService.getInstance().sendTechSupportEmail(subject, body, saveExc);
						
						// Set error message and return to splash page
						serverInfo.setStatusMessage(req.getSession(), "Error occurred updating vendor system info - see email for details");
						return "splash";
					}
				}
			}
			
			String resortFlag = req.getParameter("sort_alpha");
			if (resortFlag != null && resortFlag.equals("Y"))
			{
				// Resort all existing vendor systems alphabetically by name
				sortByName(serverInfo, op);
			}
			
			// Check to see if user clicked 'Edit Help Text' button
			String helpFlag = req.getParameter("edit_help");
			if (helpFlag != null && helpFlag.equals("Y"))
				nextPage = "ams.help.edit";
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving vendor system info", e);
			throw new ActionException("Error saving vendor system info", e);
		}
		
		return nextPage;
	}

}
