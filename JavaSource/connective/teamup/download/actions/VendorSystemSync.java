/*
 * Created on Jul 27, 2005
 */
package connective.teamup.download.actions;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsClaimInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;
import connective.teamup.registration.ws.objects.GetClaimVendorSystemOutput;
import connective.teamup.registration.ws.objects.VendorClaimSystemOutput;
import connective.teamup.ws.client.TeamupWSClient;

/**
 * Action bean to retrieve the current set of supported agency vendor systems from the TEAM-UP 
 * central registration app and sync the carrier's system definitions.
 * 
 * @author Kyle McCreary
 */
public class VendorSystemSync extends SortVendorSystems
{
	private static final Logger LOGGER = Logger.getLogger(VendorSystemSync.class);
	
public static final String YES ="Y";
	/* (non-Javadoc)
	 * @see connective.teamup.download.actions.SortVendorSystems#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "menu.ams";
		
		try
		{
			// Create the web service
			String endpoint = serverInfo.getRegistrationUrl() + "/ws";
			TeamupWSClient ws = new TeamupWSClient(endpoint, "TUDL");
			GetClaimVendorSystemOutput outObject = (GetClaimVendorSystemOutput) ws.callService("GetClaimVendorSystem", null);
			
			// Retrieve the latest set of vendor systems from the TEAM-UP Registration/Admin site
			VendorClaimSystemOutput[] systems = outObject.getVendorSystems();
			boolean sort = false;
			if (systems != null && systems.length > 0)
			{
				// Get the existing list of vendor systems
				Hashtable hash = op.getAmsClaimHashtable();
				// Loop and update or add the supported (non-custom) systems
				for (int i=0; i < systems.length; i++)
				{
					AmsClaimInfo amsClaimInfo = (AmsClaimInfo) hash.get(systems[i].getId());
					
					boolean newSystem = false;
					if (amsClaimInfo == null)
					{
						// Add system to carrier's database
						newSystem = true;
						amsClaimInfo = op.createAmsClaimInfo(systems[i].getId());
						amsClaimInfo.setCompanyDir(systems[i].getDefaultDirectory());
						amsClaimInfo.setCompanyFilename(systems[i].getDefaultFilename());
						
						amsClaimInfo.setCompanyClaimDir(systems[i].getDefaultClaimDirectory());
						amsClaimInfo.setCompanyClaimFilename(systems[i].getDeafultClaimFileName());
						
						amsClaimInfo.setCompanyPolicyDir(systems[i].getDefaultPolicyDirectory());
						amsClaimInfo.setCompanyPolicyFilename(systems[i].getDeafultPolicyFileName());
					}
					else
					{
						// Remove system from hashtable to prevent duplicate processing
						hash.remove(systems[i].getId());
					}
					
					if (amsClaimInfo.isCustomSystem() || amsClaimInfo.getLastUpdated() < systems[i].getLastUpdated())
					{
						boolean fileChanged = false;
						
						// Update the system definition and save
						amsClaimInfo.setVendor(systems[i].getVendorName());
						amsClaimInfo.setName(systems[i].getSystemName());
						amsClaimInfo.setNote(systems[i].getSystemNameNote());
						amsClaimInfo.setDefaultDir(systems[i].getDefaultDirectory());
						amsClaimInfo.setCompanyChangeDirectory(YES.equals(systems[i].getCompanyChangeDirectory()));
						
						if (!amsClaimInfo.isCompanyChangeDirectory() || amsClaimInfo.getCompanyDir() == null || amsClaimInfo.getCompanyDir().equals(""))
							amsClaimInfo.setCompanyDir(systems[i].getDefaultDirectory());
						
						amsClaimInfo.setDirectoryVariable(YES.equals(systems[i].getDirectoryVariable()));
						amsClaimInfo.setDirectoryNotes(systems[i].getDirectoryNotes());
						amsClaimInfo.setCompanyChangeFilename(YES.equals(systems[i].getCompanyChangeFilename()));
						
						if (amsClaimInfo.getDefaultFilename() == null)
							fileChanged = true;
						else if (!amsClaimInfo.isCompanyChangeFilename() && !amsClaimInfo.getDefaultFilename().equals(systems[i].getDefaultFilename()))
							fileChanged = true;
						
						amsClaimInfo.setDefaultFilename(systems[i].getDefaultFilename());
						
						if (!amsClaimInfo.isCompanyChangeFilename() || amsClaimInfo.getCompanyFilename() == null || amsClaimInfo.getCompanyFilename().equals(""))
							amsClaimInfo.setCompanyFilename(systems[i].getDefaultFilename());
						
						amsClaimInfo.setAgentChangeFilenameFlag(YES.equals(systems[i].getAgentChangeFilename()));
						amsClaimInfo.setFilenameNotes(systems[i].getFilenameNotes());
						amsClaimInfo.setFilenameIncrementType(systems[i].getFilenameIncrementType());
						amsClaimInfo.setAppendFlag(YES.equals(systems[i].getAppendFlag()));
						amsClaimInfo.setRegistrationControlType(systems[i].getRegControlType());
						amsClaimInfo.setBatchFileFlag(YES.equals(systems[i].getBatchfile()));
						amsClaimInfo.setCustomSystem(false);
						amsClaimInfo.setLastUpdated(systems[i].getLastUpdated());
						//added new variable for Claims XML Download
						amsClaimInfo.setClaimSupported(YES.equals(systems[i].getClaimSupported()));
						amsClaimInfo.setPolicyXMLSupported(YES.equals(systems[i].getPolicyXMLSupported()));
						amsClaimInfo.setDefaultClaimDirectory(systems[i].getDefaultClaimDirectory());						
						amsClaimInfo.setCompanyClaimChangeDirectory(YES.equals(systems[i].getCompanyClaimChangeDirectory()));
						
						if (!amsClaimInfo.isCompanyClaimChangeDirectory() || amsClaimInfo.getCompanyClaimDir() == null || amsClaimInfo.getCompanyClaimDir().equals(""))
							amsClaimInfo.setCompanyDir(systems[i].getDefaultClaimDirectory());
						
						amsClaimInfo.setDirectoryClaimVariable(YES.equals(systems[i].getDirectoryClaimVariable()));
						amsClaimInfo.setCompanyChangeClaimFilename(YES.equals(systems[i].getCompanyChangeClaimFilename()));
						if (amsClaimInfo.getDeafultClaimFileName() == null)
							fileChanged = true;

						else if (!amsClaimInfo.isCompanyChangeClaimFilename() && !amsClaimInfo.getDeafultClaimFileName().equals(systems[i].getDeafultClaimFileName()))
							fileChanged = true;
						
						amsClaimInfo.setDeafultClaimFileName(systems[i].getDeafultClaimFileName());
						
						if (!amsClaimInfo.isCompanyChangeClaimFilename() || amsClaimInfo.getCompanyClaimFilename() == null || amsClaimInfo.getCompanyClaimFilename().equals(""))
							amsClaimInfo.setCompanyClaimFilename(systems[i].getDeafultClaimFileName());
						
						amsClaimInfo.setAgentChangeClaimFilename(YES.equals(systems[i].getAgentChangeClaimFilename()));
						amsClaimInfo.setFilenameClaimIncrementType(systems[i].getFilenameClaimIncrementType());
						amsClaimInfo.setRegClaimControlType(systems[i].getRegClaimControlType());
						
						//added variable for Policy XML download
						amsClaimInfo.setDefaultPolicyDirectory(systems[i].getDefaultPolicyDirectory());						
						amsClaimInfo.setCompanyPolicyChangeDirectory(YES.equals(systems[i].getCompanyPolicyChangeDirectory()));
						
						if (!amsClaimInfo.isCompanyPolicyChangeDirectory() || amsClaimInfo.getCompanyPolicyDir() == null || amsClaimInfo.getCompanyPolicyDir().equals(""))
							amsClaimInfo.setCompanyDir(systems[i].getDefaultPolicyDirectory());
						amsClaimInfo.setDirectoryPolicyVariable(YES.equals(systems[i].getDirectoryPolicyVariable()));
						
						amsClaimInfo.setCompanyChangePolicyFilename(YES.equals(systems[i].getCompanyChangePolicyFilename()));
						
						if (amsClaimInfo.getDeafultPolicyFileName() == null)
							fileChanged = true;
						else if (!amsClaimInfo.isCompanyChangePolicyFilename() && !amsClaimInfo.getDeafultPolicyFileName().equals(systems[i].getDeafultPolicyFileName()))
							fileChanged = true;
						
						amsClaimInfo.setDeafultPolicyFileName(systems[i].getDeafultPolicyFileName());
						
						if (!amsClaimInfo.isCompanyChangePolicyFilename() || amsClaimInfo.getCompanyPolicyFilename() == null || amsClaimInfo.getCompanyPolicyFilename().equals(""))
							amsClaimInfo.setCompanyPolicyFilename(systems[i].getDeafultPolicyFileName());
						
						amsClaimInfo.setAgentChangePolicyFilename(YES.equals(systems[i].getAgentChangePolicyFilename()));
						amsClaimInfo.setFilenamePolicyIncrementType(systems[i].getFilenamePolicyIncrementType());
						amsClaimInfo.setRegPolicyControlType(systems[i].getRegPolicyControlType());
						
						amsClaimInfo.save();
						sort = true;
						
						// Compare and update the runtime help notes
						CustomTextFactory runtimeFactory = 
								new CustomTextFactory(CustomTextFactory.VENDOR_HELP_RUNTIME, amsClaimInfo, serverInfo, op);
						
						String runtimeText = systems[i].getRuntimeNotes();
						String oldText = runtimeFactory.getText();
						if ((runtimeText == null || runtimeText.equals("")) && !oldText.equals(""))
							runtimeFactory.updateText("", op);
						else if (!runtimeText.equals(oldText))
							runtimeFactory.updateText(runtimeText, op);

						// Compare and update the setup help notes
						CustomTextFactory setupFactory = 
								new CustomTextFactory(CustomTextFactory.VENDOR_HELP_SETUP, amsClaimInfo, serverInfo, op);
						
						String setupText = systems[i].getSetupNotes();
						oldText = setupFactory.getText();
						if ((setupText == null || setupText.equals("")) && !oldText.equals(""))
							setupFactory.updateText("", op);
						else if (!setupText.equals(oldText))
							setupFactory.updateText(setupText, op);
						
						
						if (!newSystem && fileChanged && !amsClaimInfo.isAgentChangeFilenameFlag())
						{
							// Update the default filename for all agents using this vendor system
							ArrayList badUpdates = new ArrayList();
							String filename = amsClaimInfo.getCompanyFilename();
							AgentInfo[] agent = op.getAgentsForAms(amsClaimInfo.getId());
							Exception saveExc = null;
							for (int j=0; j < agent.length; j++)
							{
								try
								{
									agent[j].setDefaultFilename(filename);
									agent[j].setDefaultClaimFilename(amsClaimInfo.getDeafultClaimFileName());
									agent[j].setDefaultPolicyFilename(amsClaimInfo.getDeafultPolicyFileName());
									agent[j].updateFilenameToDb();
								}
								catch (Exception e)
								{
									LOGGER.error(e);
									if (saveExc == null)
										saveExc = e;
									badUpdates.add(agent[j]);
								}
							}
							
							if (agent.length > 1 && agent.length == badUpdates.size())
							{
								System.out.println("Vendor sync error: error occurred for every agent for system " + amsClaimInfo.getDisplayName());
								if (saveExc != null)
								{
									System.out.println(saveExc.getMessage());
									saveExc.printStackTrace();
								}
								
								// Every agent update failed -- send an error alert email
								String to = CarrierInfo.getInstance().getErrorsEmail();
								String subject = CarrierInfo.getInstance().getShortName() + " TEAM-UP Download - Vendor system sync error";
								String body = "TEAM-UP was unable to update the vendor system settings and archived files " + 
											  "for any of the agents currently configured to use " + amsClaimInfo.getDisplayName() + ".\n\n" +
											  "Please contact Ebix,Inc user support for assistance.";
								EmailService.getInstance().sendEMail(to, subject, body);
								
								// Send copy w/ stack trace to CTI tech support
								if (saveExc != null)
									EmailService.getInstance().sendTechSupportEmail(subject, body, saveExc);
								
								// Set error message and return to splash page
								serverInfo.setStatusMessage(req.getSession(), "Error occurred updating vendor system info - see email for details");
								nextPage = "splash";
							}
							else if (badUpdates.size() > 0)
							{
								// Retry all agent updates that failed the first time
								String agtList = null;
								int errorCount = 0;
								for (int j=0; j < badUpdates.size(); j++)
								{
									AgentInfo agentInfo = (AgentInfo) badUpdates.get(j);
									try
									{
										agentInfo.setDefaultFilename(filename);
										agentInfo.setDefaultClaimFilename(amsClaimInfo.getDeafultClaimFileName());
										agentInfo.setDefaultPolicyFilename(amsClaimInfo.getDeafultPolicyFileName());
										agentInfo.updateFilenameToDb();
									}
									catch (Exception e)
									{
										LOGGER.error(e);
										// Error occurred second time for agent
										System.out.println("Vendor sync error: error occurred twice for system " + amsClaimInfo.getDisplayName() + ", agent " + agentInfo.getAgentId());
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
											amsClaimInfo.getDisplayName() + " system update:\n\n" + agtList +
											"\nYou will need to update the vendor system defaults manually for the listed agent";
									if (errorCount > 1)
										body += "s";
									body += ".  Please contact Ebix,Inc user support if you have any questions.";
									
									String to = CarrierInfo.getInstance().getErrorsEmail();
									String subject = CarrierInfo.getInstance().getShortName() + " TEAM-UP Download - Vendor system sync error";
									EmailService.getInstance().sendEMail(to, subject, body);
									
									// Send copy w/ stack trace to CTI tech support
									if (saveExc != null)
										EmailService.getInstance().sendTechSupportEmail(subject, body, saveExc);
									
									// Set error message and return to splash page
									serverInfo.setStatusMessage(req.getSession(), "Error occurred updating vendor system info - see email for details");
									nextPage = "splash";
								}
							}
						}
					}
				}
				
				// Check remaining vendor systems (custom)
				Iterator it = hash.keySet().iterator();
				while (it.hasNext())
				{
					String key = (String) it.next();
					AmsClaimInfo ams = (AmsClaimInfo) hash.get(key);
					if (!ams.isCustomSystem())
					{
						// Update definition as a custom vendor system
						ams.setCustomSystem(true);
						ams.setCompanyChangeDirectory(true);
						ams.setCompanyChangeFilename(true);
						ams.save();
					}
				}
				
				if (sort)
					sortByName(serverInfo, op);
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			ActionException ax = new ActionException("Error updating vendor system definitions from central site", e);
			System.out.println(ax.getMessage());
			ax.printStackTrace(System.out);
			
			// Always send an error notification email, even though we are not throwing the exeption
			// (so that the splash page will be displayed with the note defined below, rather than a
			// white screen or other error notification) -- 12/13/2005, kwm
			EmailService.getInstance().sendErrorNotification(ax, serverInfo.getAppName(), null, serverInfo.getAppVersion(), serverInfo.getDbVersion());
			
			serverInfo.setStatusMessage(req.getSession(), "Error updating vendor system definitions from central site");
			nextPage = "splash";
		}
		
		return nextPage;
	}

}
