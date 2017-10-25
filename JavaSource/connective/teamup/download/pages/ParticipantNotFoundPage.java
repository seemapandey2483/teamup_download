package connective.teamup.download.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.GenericPage;
import connective.teamup.download.PageException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.beans.ParticipantSearchDisplayBean;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Overrides the generic page bean to display an error message on the "Search
 * for Agent by Participant Code" page.
 * 
 * @author Kyle McCreary
 */
public class ParticipantNotFoundPage extends GenericPage
{

	/**
	 * Constructor for ParticipantNotFoundPage.
	 */
	public ParticipantNotFoundPage()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		ParticipantSearchDisplayBean bean = (ParticipantSearchDisplayBean) super.createDisplayBean(req, resp, serverInfo, op, items);
		
		// Set the error message
		String searchType = req.getParameter("search_type");
		if (searchType != null)
		{
			String msg = null;
			if (searchType.equals("partcode"))
			{
				String partcode = req.getParameter("partcode");
				if (partcode == null || partcode.trim().equals(""))
					msg = "You did not specify a valid participant code.";
				else
					msg = "The specified participant code (<B>" + partcode.trim() +
						  "</B>) could not be found.";
			}
			else if (searchType.equals("filename"))
			{
				String filename = req.getParameter("filename");
				if (filename == null || filename.trim().equals(""))
					msg = "You did not specify a valid import filename.";
				else
				{
					msg = "The specified import filename (<B>" + filename.trim() +
						  "</B>) could not be found.";
					if (filename.indexOf('.') >= 0)
						msg += "  Try your search again without specifying a file extension.";
				}
			}
			bean.setErrorMessage(msg);
		}
		
		return bean;
	}

}
