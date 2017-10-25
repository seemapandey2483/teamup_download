package connective.teamup.download.actions;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileInfo;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ImportTestFile extends ImportActionBase implements Action
{
	private static final Logger LOGGER = Logger.getLogger(ImportTestFile.class);
	/**
	 * Constructor for ImportTestFile.
	 */
	public ImportTestFile() {
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		try
		{
			// Get the agent ID
			AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
			String agentID = agent.getAgentId();
			if (agentID == null || agentID.trim().length() == 0)
				agentID = (String) req.getSession().getAttribute("agentID");
				
			// Check to see if a test file already exists in the database for this
			// agent.  If not, import a new test file for testing the download.
			FileInfo[] files = op.getAgentFilesByStatus(agentID, DownloadStatus.TEST);
			if (files.length == 0)
			{
				// Import the designated test file
				InputStream instr = op.getResource(DatabaseFactory.RES_TESTFILE);
				if (instr == null)
				{
					URL url = getClass().getResource(CustomTextFactory.DEFAULT_FILE_PATH + "DLTEST.DAT");
					instr = new BufferedInputStream(new FileInputStream(url.getFile()));
				}
				importFile(op, serverInfo, "DLTEST.DAT", agentID, 0, true, true, 0, instr, 0, 0, true);
				instr.close();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error importing test file", e);
			throw new ActionException("Error importing test file", e);
		}
		
		return "testdownload";
	}
}
