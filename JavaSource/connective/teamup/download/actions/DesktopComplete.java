package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Action bean to save edited agency vendor system settings from the Agency Admin pages.
 */
public class DesktopComplete implements Action
{

	/**
	 * Constructor for SaveAgencyAmsConfig.
	 */
	public DesktopComplete()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "menu.settings";

		// determine the next page		
		String scheduleTask = req.getParameter("scheduleTask");
		String removeSchedule = req.getParameter("removeSchedule");

		if (scheduleTask != null && scheduleTask.equals("Y"))
			nextPage = "config.scheduler";
		else if (removeSchedule != null && removeSchedule.equals("Y"))
			nextPage = "remove.scheduler";
		
		return nextPage;
	}

}
