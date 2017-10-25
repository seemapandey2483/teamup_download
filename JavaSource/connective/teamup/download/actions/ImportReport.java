package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;

/**
 * @author Kyle McCreary
 *
 * Action bean to complete a batch import process.
 */
public class ImportReport implements Action
{
	private static final Logger LOGGER = Logger.getLogger(ImportReport.class);
	/**
	 * Constructor for ImportComplete.
	 */
	public ImportReport()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		try
		{


			String notifyImportError = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_IMPORT_ERROR);
				if (notifyImportError!= null && "Y".equals(notifyImportError))
				{
					
					String message = req.getParameter("message");
					String subject ="";
					String type = req.getParameter("type");
					if("P".equals(type))
					subject = "TEAM-UP Policy Import Run Log";
					else if("C".equals(type))
						subject = "TEAM-UP Claim Import Run Log";
					else
						subject = "TEAM-UP Policy Import Run Log";
					
					String to = op.getPropertyValue(DatabaseFactory.PROP_MISC_EMAIL_REPORTS);
					if (to != null && !"".equals(to)){
						EmailService.getInstance().sendEMail(to, subject, message, null);					
					}

				}
			

			resp.getWriter().flush();
		}
		catch (Exception e)
		{
			LOGGER.error("Error in sending email import batch Report", e);
			throw new ActionException("Error in sending email import batch Report", e);
		}
		
		return null;
	}

}
