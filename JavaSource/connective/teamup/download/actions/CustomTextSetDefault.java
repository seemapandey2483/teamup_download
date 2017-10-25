/*
 * 12/12/2007 - Created
 */
package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Action bean to set the specified custom text to use the default file(s), and delete any saved
 * custom data for that text ID.
 * 
 * @author kmccreary
 */
public class CustomTextSetDefault implements Action
{
	private static final Logger LOGGER = Logger.getLogger(CustomTextSetDefault.class);
	/**
	 * Constructor for CustomTextSetDefault.
	 */
	public CustomTextSetDefault()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String textId = req.getParameter("text_file");
		try
		{
			if (textId != null && !textId.trim().equals(""))
			{
				CustomTextFactory textFactory = new CustomTextFactory(textId, 
										CustomTextFactory.TYPE_EMAIL, serverInfo, null, op);
				
				if (textFactory.isCustomSubject())
					textFactory.updateEmailSubject(null, op);
				if (textFactory.isCustomText())
					textFactory.updateText(null, op);
				if (textFactory.isCustomHtml())
					textFactory.updateHtml(null, op);
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			String msg = "Error setting custom file to use default text";
			if (textId != null && !textId.trim().equals(""))
				msg += ":  " + textId;
			throw new ActionException(msg, e);
		}
		
		return "custom.list";
	}

}
