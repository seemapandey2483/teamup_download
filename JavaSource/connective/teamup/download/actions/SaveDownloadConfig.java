package connective.teamup.download.actions;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SaveDownloadConfig implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(SaveDownloadConfig.class);

	/**
	 * Constructor for SaveDownloadConfig.
	 */
	public SaveDownloadConfig() {
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		boolean invalidTestfile = false;

		try
		{
			Hashtable props = new Hashtable();
			CarrierInfo carrierBean = serverInfo.getCarrierInfo();
			
			// Parse the configuration settings from the request
			String autoPurge = req.getParameter("autopurge");
			if (autoPurge == null || !autoPurge.equals("Y"))
				autoPurge = "N";
			props.put(DatabaseFactory.PROP_AUTO_PURGE, autoPurge);
			String excludeLob = req.getParameter("excludelob");
			if (excludeLob == null || !excludeLob.equals("Y"))
				excludeLob = "N";
			props.put(DatabaseFactory.PROP_EXCLUDE_LOB, excludeLob);
			String archivePeriod = req.getParameter("archive_period");
			if (archivePeriod == null)
				archivePeriod = "-1";
			props.put(DatabaseFactory.PROP_ARCHIVE_PERIOD, archivePeriod);

			String sourcePath = req.getParameter("source_path");
			if (sourcePath != null)
			{
				sourcePath = sourcePath.trim();
				if (sourcePath.length() > 0 && sourcePath.charAt(sourcePath.length()-1) != '\\')
					sourcePath += "\\";
			}
			props.put(DatabaseFactory.PROP_IMPORT_SOURCE_PATH, sourcePath);
			String deleteAllImportFiles = req.getParameter("import_delete");
			if (deleteAllImportFiles == null || !deleteAllImportFiles.equals("Y"))
				deleteAllImportFiles = "N";
			props.put(DatabaseFactory.PROP_DELETE_ALL_FILES_ON_IMPORT, deleteAllImportFiles);
			
			String defaultTPListView = req.getParameter("tplist_view");
			if (defaultTPListView != null)
			{
				props.put(DatabaseFactory.PROP_TPLIST_VIEW, defaultTPListView);
				carrierBean.setDefaultTPListView(defaultTPListView);
			}
			
/*			String filename = req.getParameter("testfile");
			if (filename.indexOf("\\") < 0 && filename.indexOf("/") < 0)
			{
				// Assume file is in the default directory
				filename = serverInfo.getConfigDir() + filename;
			}
			props.put(ServerInfo.PROP_TESTFILE, filename);
*/
			String emailErrors = req.getParameter("errors_email");
			props.put(DatabaseFactory.PROP_EMAIL_ERRORS, emailErrors);
			String emailReports = req.getParameter("reports_email");
			props.put(DatabaseFactory.PROP_EMAIL_REPORTS, emailReports);
			
			String miscemailReports = req.getParameter("misc_reports_email");
			props.put(DatabaseFactory.PROP_MISC_EMAIL_REPORTS, miscemailReports);
			
			String emailContact = req.getParameter("contact_email");
			if (emailContact == null || emailContact.trim().equals(""))
				emailContact = emailReports;
			props.put(DatabaseFactory.PROP_EMAIL_CONTACT, emailContact);
			
			String cliamXMLImportAllowed = req.getParameter("cliamXMLImportAllowed");
			if (cliamXMLImportAllowed == null || !cliamXMLImportAllowed.equals("Y"))
				cliamXMLImportAllowed = "N";
			props.put(DatabaseFactory.PROP_CLAIM_XML_ALLOWED, cliamXMLImportAllowed);
			
			String policyXMLImportAllowed = req.getParameter("policyXMLImportAllowed");
			if (policyXMLImportAllowed == null || !policyXMLImportAllowed.equals("Y"))
				policyXMLImportAllowed = "N";
			props.put(DatabaseFactory.PROP_POLICY_XML_ALLOWED, policyXMLImportAllowed);
			
			// save the properties
			op.setProperties(props);
					
			// Update carrier info
			carrierBean.setDeleteAllImportedFiles(deleteAllImportFiles.equals("Y"));
			if (emailContact != null && !emailContact.equals(carrierBean.getContactEmail()))
				carrierBean.setContactEmail(emailContact);
			if (emailErrors != null && !emailErrors.equals(carrierBean.getErrorsEmail()))
				carrierBean.setErrorsEmail(emailErrors);
			if (emailReports != null && !emailReports.equals(carrierBean.getReportsEmail()))
				carrierBean.setReportsEmail(emailReports);
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving carrier download config settings", e);
			throw new ActionException("Error saving carrier download config settings", e);
		}
				
		String nextPage = null;
		String configWizard = req.getParameter("config_wizard");
		if (invalidTestfile)
		{
			nextPage = "menu.settings.invalid";
		}
		else if (configWizard != null && configWizard.equals("Y"))
		{
			// Navigate to the next page of the configuration wizard
			nextPage = "config4";
		}
		else
		{
			// Navigate back to the splash screen
			nextPage = "splash";
			serverInfo.setStatusMessage(req.getSession(), "Download configuration changes saved successfully");
		}

		return nextPage;
	}
}
