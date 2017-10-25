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
 * @author Kyle McCreary
 *
 * Parses the property values from the configuration wizard page and saves to the database.
 */
public class SaveCarrierAdvancedOptions implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveCarrierAdvancedOptions.class);
	/**
	 * Constructor for SaveDownloadConfig.
	 */
	public SaveCarrierAdvancedOptions()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		boolean invalidTestfile = false;
		String nextPage = null;

		try
		{
			Hashtable props = new Hashtable();
			
			// Parse the configuration settings from the request
			
			// Email Notification settings
			String agyMigFlag = req.getParameter("flag_agtmig");
			props.put(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_MIGRATION, agyMigFlag);
			String agyRegFlag = req.getParameter("flag_agtreg");
			props.put(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_REGISTRATION, agyRegFlag);
			String statusChgFlag = req.getParameter("flag_statuschg");
			props.put(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_STATUS_CHANGE, statusChgFlag);
			String agtVendorChgFlag = req.getParameter("flag_agtvendorchg");
			props.put(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_VENDOR_CHANGE, agtVendorChgFlag);
			String downloadErrsFlag = req.getParameter("flag_downloaderr");
			props.put(DatabaseFactory.PROP_NOTIFY_ON_DOWNLOAD_ERROR, downloadErrsFlag);
			String importErrsFlag = req.getParameter("flag_importerr");
			props.put(DatabaseFactory.PROP_NOTIFY_ON_IMPORT_ERROR, importErrsFlag);
			String emailAsHtmlFlag = req.getParameter("flag_html_email");
			props.put(DatabaseFactory.PROP_EMAIL_AS_HTML, emailAsHtmlFlag);
			
			// Trading Partner Configuration settings
			String pwdChangeFlag = req.getParameter("flag_chgpwd");
			props.put(DatabaseFactory.PROP_AGENT_PASSWORD_CHANGE, pwdChangeFlag);
			String agtInfoChangeFlag = req.getParameter("flag_chgagtinfo");
			props.put(DatabaseFactory.PROP_AGENT_INFO_CHANGE, agtInfoChangeFlag);
			String displayLoginShortcutCtrl = req.getParameter("flag_loginShortcutCtrl");
			props.put(DatabaseFactory.PROP_CONTROL_SHORTCUT_LOGIN_ALLOWED, displayLoginShortcutCtrl);
			
			
			// save the properties
			op.setProperties(props);
			
					
			CarrierInfo carrierBean = serverInfo.getCarrierInfo();
			if (carrierBean != null)
			{
				// Update carrier info
				if (pwdChangeFlag != null)
					carrierBean.setAgentPasswordChangeAllowed(pwdChangeFlag.equals("Y"));
				if (agtInfoChangeFlag != null)
					carrierBean.setAgentInfoChangeAllowed(agtInfoChangeFlag.equals("Y"));
				if (agyMigFlag != null)
					carrierBean.setNotifyOnAgentMigration(agyMigFlag.equals("Y"));
				if (agyRegFlag != null)
					carrierBean.setNotifyOnAgentRegister(agyRegFlag.equals("Y"));
				if (statusChgFlag != null)
					carrierBean.setNotifyOnStatusChange(statusChgFlag.equals("Y"));
				if (agtVendorChgFlag != null)
					carrierBean.setNotifyOnAgentChange(agtVendorChgFlag.equals("Y"));
				if (downloadErrsFlag != null)
					carrierBean.setNotifyOnDownloadError(downloadErrsFlag.equals("Y"));
				if (importErrsFlag != null)
					carrierBean.setNotifyOnImportError(importErrsFlag.equals("Y"));
				if (emailAsHtmlFlag != null)
					carrierBean.setEmailAsHtml(emailAsHtmlFlag.equals("Y"));
				if (displayLoginShortcutCtrl != null)
					carrierBean.setDisplayLoginShortcutControl(displayLoginShortcutCtrl.equals("Y"));
			}

			String configWizard = req.getParameter("config_wizard");
			if (configWizard != null && configWizard.equals("Y"))
			{
				// Navigate to the next page of the configuration wizard
				nextPage = "config5";
			}
			else
			{
				// Navigate back to the splash screen
				nextPage = "splash";
				serverInfo.setStatusMessage(req.getSession(), "Advanced option configuration changes saved successfully");
			}

		}
		catch (Exception e)
		{
			LOGGER.error("Error saving carrier config settings", e);
			throw new ActionException("Error saving carrier config settings", e);
		}
				
		return nextPage;
	}
}
