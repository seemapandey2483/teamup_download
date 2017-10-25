/*
 * 12/10/2007 - Created
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
 * Action bean to save custom text and/or HTML versions of the specified system-generated email.
 * 
 * @author kmccreary
 */
public class CustomTextSave implements Action
{
	private static final Logger LOGGER = Logger.getLogger(CustomTextSave.class);
	/**
	 * Constructor for CustomTextSave.
	 */
	public CustomTextSave()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "custom.list";
		String viewPage = "custom.view";
		
		String textId = req.getParameter("text_file");
		try
		{
			if (textId != null && !textId.trim().equals(""))
			{
				// Retrieve the custom text factory for the specified file
				CustomTextFactory textFactory = new CustomTextFactory(textId, 
										CustomTextFactory.TYPE_EMAIL, serverInfo, null, op);
				
				// Parse and update the text version of the file
				String defaultFlag = req.getParameter("text_default_check");
				String text = null;
				if (defaultFlag == null || !defaultFlag.equals("Y"))
					text = req.getParameter("text_editor");
				if (text != null && !text.trim().equals(""))
				{
					if (!textFactory.isCustomText() || textFactory.getRawText() == null ||
							!textFactory.getRawText().equals(text))
					{
						// save the updated custom text data
						textFactory.updateText(text, op);
					}
					
					// next page should show the custom version(s) of the file
					nextPage = viewPage;
				}
				else if (textFactory.isCustomText())
				{
					// remove the previously saved custom text data
					textFactory.updateText(null, op);
				}
				
				// Parse and update the HTML version of the file
				defaultFlag = req.getParameter("html_default_check");
				text = null;
				if (defaultFlag == null || !defaultFlag.equals("Y"))
					text = req.getParameter("html_editor");
				if (text != null && !text.trim().equals(""))
				{
					if (!textFactory.isCustomHtml() || textFactory.getRawHtml() == null ||
							!textFactory.getRawHtml().equals(text))
					{
						// save the updated custom HTML data
						textFactory.updateHtml(text, op);
					}
					
					// next page should show the custom version(s) of the file
					nextPage = viewPage;
				}
				else if (textFactory.isCustomHtml())
				{
					// remove the previously saved custom HTML data
					textFactory.updateHtml(null, op);
				}
				
				// Parse and update the email subject line text for the file
				String subject = req.getParameter("subject");
				if (subject == null || subject.trim().equals(""))
				{
					if (textFactory.isCustomSubject())
					{
						// remove the previously saved custom email subject text
						textFactory.updateEmailSubject(null, op);
					}
				}
				else if (!subject.equals(textFactory.getEmailSubject()))
				{
					if (subject.equals(textFactory.getDefaultSubject()))
					{
						// remove the previously saved custom email subject text
						textFactory.updateEmailSubject(null, op);
					}
					else
					{
						// save the updated custom email subject text
						textFactory.updateEmailSubject(subject, op);
						
						// next page should show the custom version(s) of the file
						nextPage = viewPage;
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			String msg = "Error saving custom email content";
			if (textId != null && !textId.trim().equals(""))
				msg += ":  " + textId;
			throw new ActionException(msg, e);
		}
		
		return nextPage;
	}

}
