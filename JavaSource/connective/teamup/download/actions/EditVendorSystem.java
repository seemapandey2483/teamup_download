package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AmsClaimInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Action bean used to edit an existing agency vendor system.
 * 
 * @author Kyle McCreary
 */
public class EditVendorSystem implements Action
{
	private static final Logger LOGGER = Logger.getLogger(EditVendorSystem.class);
	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "ams.edit.custom";
		
		try
		{
			AmsClaimInfo amsInfo = null;
			String systemId = req.getParameter("amsid");
			if (systemId != null && !systemId.equals(""))
				amsInfo = op.getAmsClaimInfo(systemId);
			if (amsInfo != null)
			{
				if (amsInfo.isCustomSystem())
					nextPage = "ams.edit.custom";
				else
					nextPage = "ams.edit";
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error retrieving agency vendor system", e);
			throw new ActionException("Error retrieving agency vendor system", e);
		}
		
		return nextPage;
	}
}
