package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Action bean to save edited agency vendor system information from the carrier
 * admin pages.
 */
public class SaveAmsHelp implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveAmsHelp.class);
	/**
	 * Constructor for SaveAmsHelp.
	 */
	public SaveAmsHelp()
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
			String amsID = req.getParameter("amsid");
			if (amsID == null || amsID.equals(""))
				return nextPage;
			
			AmsInfo ams = op.getAmsInfo(amsID);
			
			// Compare and update the runtime help notes
			CustomTextFactory runtimeFactory = 
					new CustomTextFactory(CustomTextFactory.VENDOR_HELP_RUNTIME, ams, serverInfo, op);
			String runtimeText = req.getParameter("runtime_help");
			if (runtimeText != null && !runtimeText.equals(runtimeFactory.getText()))
				runtimeFactory.updateText(runtimeText, op);

			// Compare and update the setup help notes
			CustomTextFactory setupFactory = 
					new CustomTextFactory(CustomTextFactory.VENDOR_HELP_SETUP, ams, serverInfo, op);
			String setupText = req.getParameter("setup_help");
			if (setupText != null && !setupText.equals(setupFactory.getText()))
				setupFactory.updateText(setupText, op);
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving vendor system help notes", e);
			throw new ActionException("Error saving vendor system help notes", e);
		}
		
		return nextPage;
	}

}
