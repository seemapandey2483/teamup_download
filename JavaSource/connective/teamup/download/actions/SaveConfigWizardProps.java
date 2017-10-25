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
public class SaveConfigWizardProps implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveConfigWizardProps.class);
	/**
	 * Constructor for SaveConfigWizardProps.
	 */
	public SaveConfigWizardProps()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		boolean invalidGraphicFile = false;

		try
		{
			Hashtable props = new Hashtable();
			
			// Parse the configuration settings from the request
			
			// Carrier Customization settings
			String carrierId = req.getParameter("carrier_id");
			props.put(DatabaseFactory.PROP_CARRIER_ID, carrierId);
			String carrierName = req.getParameter("carriername");
			props.put(DatabaseFactory.PROP_CARRIER_NAME, carrierName);
			String shortName = req.getParameter("shortname");
			props.put(DatabaseFactory.PROP_CARRIER_SHORTNAME, shortName);
			String clientAppFlag = req.getParameter("clientAppFlag");
			boolean clientAppUsed = (clientAppFlag != null && clientAppFlag.equals("Y"));
			props.put(DatabaseFactory.PROP_CLIENT_APP_USED, clientAppFlag);
			String displayMigFlag = req.getParameter("displayMigFlag");
			boolean displayMigrationBanner = (displayMigFlag != null && displayMigFlag.equals("Y"));
			props.put(DatabaseFactory.PROP_DISPLAY_MIGRATION_BANNER, displayMigFlag);
			
//			String customPath = req.getParameter("custompath");
//			if (customPath != null)
//			{
//				customPath = customPath.trim();
//				if (customPath.length() > 0 && customPath.charAt(customPath.length()-1) != '\\')
//					customPath += "\\";
//			}
//			props.put(ServerInfo.PROP_CUSTOM_FILES_PATH, customPath);
/*			String graphicFile = req.getParameter("graphicfile");
			if (graphicFile.indexOf("\\") < 0 && graphicFile.indexOf("/") < 0)
			{
				// Assume file is in the default directory
				graphicFile = serverInfo.getConfigDir() + graphicFile;
			}
			props.put(ServerInfo.PROP_CARRIER_LOGO_FILENAME, graphicFile);
*/			
			// Email Server Configuration settings
			//*** This section was removed from the config wizard
			//props.put(DatabaseFactory.PROP_SMTP_SERVER, req.getParameter("server_addr"));
			//props.put(DatabaseFactory.PROP_SMTP_SENDER_ADDRESS, req.getParameter("sender_email"));
			
			// Security Provider Configuration settings
			//*** This section temporarily removed from the config wizard -- 06/03/2003, kwm
			//props.put(ServerInfo.PROP_SECURITY_PROVIDER, req.getParameter("secprovider"));
			//props.put(ServerInfo.PROP_SECURITY_USER, req.getParameter("secuser"));
			//props.put(ServerInfo.PROP_SECURITY_PASSWORD, req.getParameter("secpass"));
			
			// Agency Application settings
			String agentLoginDisabledFlag = req.getParameter("agentLoginDisabledFlag");
			boolean isAgentLogoutDisabled = (agentLoginDisabledFlag != null && agentLoginDisabledFlag.equals("Y"));
			props.put(DatabaseFactory.PROP_AGENT_LOGIN_DISABLED, agentLoginDisabledFlag);
			String agentLogoutUrl = req.getParameter("agentLogoutUrl");
			props.put(DatabaseFactory.PROP_AGENT_LOGOUT_URL, agentLogoutUrl);
			int bannerGraphicHeight = 0;
			String strBannerGraphicHeight = "";
			try
			{
				String height = req.getParameter("bannerGraphicHeight");
				if (height == null)
					bannerGraphicHeight = -1;
				else if (!height.equals(""))
					bannerGraphicHeight = Integer.parseInt(height);
			} catch (Exception e) {
				LOGGER.error(e);
			}
			if (bannerGraphicHeight > 0)
				strBannerGraphicHeight = String.valueOf(bannerGraphicHeight);
			if (bannerGraphicHeight >= 0)
				props.put(DatabaseFactory.PROP_BANNER_GRAPHIC_HEIGHT, strBannerGraphicHeight);
			String useNewDLControlFlag = req.getParameter("useNewDLFlag");
			boolean useNewDLControl = (useNewDLControlFlag != null && useNewDLControlFlag.equals("Y"));
			props.put(DatabaseFactory.PROP_USE_NEW_DL_CONTROL, useNewDLControlFlag);
			
			// Download File Spec settings
			String fileFormat = req.getParameter("fileformat");
			props.put(DatabaseFactory.PROP_IMPORT_FILE_FORMAT, fileFormat);
			String fileProcess = req.getParameter("fileprocess");
			props.put(DatabaseFactory.PROP_IMPORT_FILE_CREATION_PROCESS, fileProcess);
			String link2agent = req.getParameter("link2agt");
			props.put(DatabaseFactory.PROP_IMPORT_FILE_IDENTIFICATION, link2agent);
			
			// Web Server Spec settings
			String appServer = req.getParameter("appserver");
			props.put(DatabaseFactory.PROP_SERVER_APP_SOFTWARE, appServer);
			String importSize = req.getParameter("importsize");
			try
			{
				int size = 0;
				if (importSize != null && !importSize.equals(""))
					size = Integer.parseInt(importSize);
				props.put(DatabaseFactory.PROP_IMPORT_BLOCK_SIZE, String.valueOf(size));
			} catch (Exception e) {
				LOGGER.error(e);
			}
			String schedPort = req.getParameter("schedport");
			try
			{
				int port = 0;
				if (schedPort != null && !schedPort.equals(""))
					port = Integer.parseInt(schedPort);
				if (port > 0)
					schedPort = String.valueOf(port);
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				schedPort = "";
			}
			props.put(DatabaseFactory.PROP_SCHEDULED_DL_PORT, schedPort);
			
			
			
			// save the properties
			op.setProperties(props);
			
			CarrierInfo carrierBean = serverInfo.getCarrierInfo();
			if (carrierBean != null)
			{
				// Update carrier info
				if (carrierBean.getCarrierId() == null || !carrierId.equals(carrierBean.getCarrierId()))
					carrierBean.setCarrierId(carrierId);
				if (carrierBean.getName() == null || !carrierName.equals(carrierBean.getName()))
					carrierBean.setName(carrierName);
				if (carrierBean.getShortName() == null || !shortName.equals(carrierBean.getShortName()))
					carrierBean.setShortName(shortName);
				if (carrierBean.isAgentLoginDisabled() != isAgentLogoutDisabled)
					carrierBean.setAgentLoginDisabled(isAgentLogoutDisabled);
				if (carrierBean.getAgentLogoutUrl() == null || !agentLogoutUrl.equals(carrierBean.getAgentLogoutUrl()))
					carrierBean.setAgentLogoutUrl(agentLogoutUrl);
//				if (carrierBean.getBannerGraphicFile() == null || !graphicFile.equals(carrierBean.getBannerGraphicFile()))
//					carrierBean.setBannerGraphicFile(graphicFile);
				if (carrierBean.isNewDownloadControlUsed() != useNewDLControl)
					carrierBean.setUseNewDownloadControl(useNewDLControl);
				if (carrierBean.isDisplayMigrationBanner() != displayMigrationBanner)
					carrierBean.setDisplayMigrationBanner(displayMigrationBanner);
				if (carrierBean.getImportFileFormat() == null || !fileFormat.equals(carrierBean.getImportFileFormat()))
					carrierBean.setImportFileFormat(fileFormat);
				if (carrierBean.getImportFileCreator() == null || !fileProcess.equals(carrierBean.getImportFileCreator()))
					carrierBean.setImportFileCreator(fileProcess);
				if (carrierBean.getImportFileIdMode() == null || !link2agent.equals(carrierBean.getImportFileIdMode()))
					carrierBean.setImportFileIdMode(link2agent);
				if (carrierBean.isClientAppUsed() != clientAppUsed)
					carrierBean.setClientAppUsed(clientAppUsed);
				
				if (bannerGraphicHeight > 0)
					carrierBean.setBannerGraphicHeight(bannerGraphicHeight);
				else
					carrierBean.resetBannerGraphicHeight();
			}
			
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving carrier config settings", e);
			throw new ActionException("Error saving carrier config settings", e);
		}
				
		// Navigate to the next page of the configuration wizard
		String nextPage = "config3";
		if (invalidGraphicFile)
			nextPage = "config2.invalid.graphicfile";

		return nextPage;
	}
}
