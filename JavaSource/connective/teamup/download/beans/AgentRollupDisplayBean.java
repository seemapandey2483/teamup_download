package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.ParticipantInfo;

/**
 * Display bean for building the Trading Partner Roll-Up parameters and summary pages.
 * 
 * @author Kyle McCreary
 */
public class AgentRollupDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(AgentRollupDisplayBean.class);
	
	public String oldAgentId = null;
	public String oldAgentName = null;
	public String rollToAgentId = null;
	public String rollToAgentName = null;
	public Vector displayText = null;
	public boolean rollupOkay = true;
	
	public CarrierInfo carrierInfo = null;
	

	/**
	 * Constructor for AgentRollupDisplayBean.
	 */
	public AgentRollupDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		carrierInfo = serverInfo.getCarrierInfo();
		
		String agentId = req.getParameter("agentID");
		if (agentId != null && !agentId.equals(""))
		{
			// Pre-fill the roll-up parameters page with the selected agent ID
			oldAgentId = agentId;
			return;
		}
		
		oldAgentId = req.getParameter("rollfrom");
		if (oldAgentId == null || oldAgentId.trim().equals(""))
		{
			// Display the roll-up parameters page
			return;
		}
		oldAgentId = oldAgentId.trim().toUpperCase();
		
		try
		{
			rollToAgentId = req.getParameter("rollinto");
			if (rollToAgentId != null)
				rollToAgentId = rollToAgentId.trim().toUpperCase();
			displayText = new Vector();
			
			AgentInfo oldAgent = op.getAgentInfo(oldAgentId);
			AgentInfo rollToAgent = op.getAgentInfo(rollToAgentId);
			if (oldAgent == null)
			{
				// Unknown agent ID
				rollupOkay = false;
				displayText.add("Cannot find a primary trading partner with the specified ID:  " + oldAgentId + ".");
				displayText.add("Please re-enter the roll-up parameters and try again.");
				return;
			}
			else if (!oldAgentId.equals(oldAgent.getAgentId()))
			{
				// ID is already a participant code
				rollupOkay = false;
				displayText.add("'" + oldAgentId + "' is already a participant code of trading partner '" +
								oldAgent.getAgentId() + "' (" + oldAgent.getName() + ").");
				if (rollToAgent == null || !oldAgent.getAgentId().equals(rollToAgent.getAgentId()))
				{
					String text = "You cannot use this feature to move a participant code from one trading partner to another.  ";
					text += "To do this, you should first delete the existing participant code, then add it to the desired trading partner.";
					displayText.add(text);
				}
				return;
			}
			oldAgentName = oldAgent.getName();
			
			if (rollToAgent == null)
			{
				// Unknown agent ID -- create a new trading partner
				rollToAgentName = oldAgentName;
				
				String text = "Cannot find the trading partner to roll into:  " + rollToAgentId;
				text += ".  Process will create a new trading partner using the contact information of the original partner.";
				displayText.add(text);
				
				text = "Trading partner '" + oldAgentId + "' will become a participant code under the new trading partner '" +
					   rollToAgentId + "' (" + rollToAgentName + ").";
				displayText.add(text);
			}
			else if (rollToAgentId.equals(oldAgentId))
			{
				// Source and target agent IDs are the same
				rollupOkay = false;
				rollToAgentName = rollToAgent.getName();
				String text = "The source and target trading partner IDs you specified are the same (" + rollToAgentId +
							  ").  You must designate two trading partners to combine.";
				displayText.add(text);
				return;
			}
			else if (!rollToAgentId.equals(rollToAgent.getAgentId()))
			{
				// Agent ID specified is actually a participant code
				rollupOkay = false;
				rollToAgentName = "(Participant code - see below)";
				String text = "The target ID you specified (" + rollToAgentId +
							  ") is actually a participant code of trading partner '" +
							  rollToAgent.getAgentId() + "' (" + rollToAgent.getName() + ").";
				displayText.add(text);
				
				text = "You must designate a valid trading partner ID to complete this trading partner roll-up.";
				displayText.add(text);
				return;
			}
			else
			{
				rollToAgentName = rollToAgent.getName();
				
				displayText.add("Trading partner '" + oldAgentId + "' will become a new participant code under the existing trading partner '" +
								rollToAgent.getAgentId() + "' (" + rollToAgentName + ").");
			}
			
			oldAgent.loadParticipantsFromDb();
			if (oldAgent.getParticipantCount() > 0)
			{
				StringBuffer tbuf = new StringBuffer("The following participant codes will be moved from trading partner '");
				tbuf.append(oldAgentId);
				tbuf.append("' to '");
				tbuf.append(rollToAgentId);
				tbuf.append("': <UL>");
				boolean useFilename = serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME);
				for (int i=0; i < oldAgent.getParticipantCount(); i++)
				{
					ParticipantInfo part = oldAgent.getParticipant(i);
					tbuf.append("<LI>");
					tbuf.append(part.getParticipantCode());
					if (useFilename)
					{
						tbuf.append(" (filename: ");
						tbuf.append(part.getFilename());
						tbuf.append(")");
					}
					tbuf.append("</LI>");
				}
				tbuf.append("</UL>");
				displayText.add(tbuf.toString());
			}
			
			FileInfo[] files = op.getAgentFiles(oldAgentId);
			if (files != null && files.length > 0)
			{
				int fileCount = 0;
				boolean current = false;
				boolean archived = false;
				for (int i=0; i < files.length; i++)
				{
					if (files[i].isFileComplete())
						fileCount++;
					if (files[i].getDownloadStatus().equals(DownloadStatus.CURRENT))
						current = true;
					else if (files[i].getDownloadStatus().equals(DownloadStatus.DB_CURRENT))
						current = true;
					else if (!files[i].getDownloadStatus().equals(DownloadStatus.TEST))
						archived = true;
				}
				if (fileCount > 0)
				{
					String text = "A total of " + fileCount;
					if (current && archived)
						text += " current and archived ";
					else if (current)
						text += " current ";
					else
						text += " archived ";
					if (fileCount == 1)
						text += "file";
					else
						text += "files";
					text += " will be moved to trading partner '" + rollToAgentId + "'.";
					displayText.add(text);
				}
			}
			
			String text = "NOTE:  Any transaction logs that currently exist for trading partner '";
			text += oldAgentId + "' <i>will not</i> be moved or copied to the new trading partner ID.";
			displayText.add(text);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			e.printStackTrace(System.out);
			throw new DisplayBeanException("Error occurred building the trading partner roll-up summary", e);
		}
	}

	/**
	 * Returns the display text.
	 * @param index The line number to return
	 * @return String
	 */
	public String getDisplayText(int index)
	{
		if (displayText == null || index >= displayText.size())
			return "";
		
		return (String) displayText.elementAt(index);
	}
	
	/**
	 * Returns the number of lines of display text.
	 * @return int
	 */
	public int getDisplayTextLineCount()
	{
		if (displayText == null)
			return 0;
		return displayText.size();
	}

	/**
	 * Returns the old agent ID.
	 * @return String
	 */
	public String getOldAgentId()
	{
		if (oldAgentId == null)
			return "";
		return oldAgentId;
	}

	/**
	 * Returns the roll-to agent ID.
	 * @return String
	 */
	public String getRollToAgentId()
	{
		return rollToAgentId;
	}

	/**
	 * Returns true if roll-up definition is good, otherwise false.
	 * @return boolean
	 */
	public boolean isRollupOkay()
	{
		return rollupOkay;
	}

	/**
	 * Returns the old agent name.
	 * @return String
	 */
	public String getOldAgentName()
	{
		return oldAgentName;
	}

	/**
	 * Returns the roll-to agent name.
	 * @return String
	 */
	public String getRollToAgentName()
	{
		return rollToAgentName;
	}

	/**
	 * Returns the carrier info.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

}
