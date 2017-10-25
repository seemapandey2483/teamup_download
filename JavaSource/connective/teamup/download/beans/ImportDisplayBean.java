package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author mccrearyk
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ImportDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(ImportDisplayBean.class);
	
	private String fileSource = "";
	private String importUrl = "";
	private String forwardUrl = "";
	private String interactiveFlag = "Y";
	private String servletPath = "";
	private String codebase = null;
	private String classid = null;
	
	private String carrierName = "";
	private String carrierShortName = "";
	private String carrierEmail = "";
	private String bannerGraphic = "";
	private CarrierInfo carrierInfo = null;


	/**
	 * Constructor for ImportDisplayBean.
	 */
	public ImportDisplayBean()
	{
		super();

	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException 
	{
		try
		{
			codebase = serverInfo.getImportCodebase();
			classid = serverInfo.getImportClassid();
	
			CarrierInfo carrier = serverInfo.getCarrierInfo();
			
			// set the path
			servletPath = req.getServletPath() + "/company";
			
			// Load the carrier info
			carrierName = carrier.getName();
			carrierShortName = carrier.getShortName();
			carrierEmail = carrier.getContactEmail();
			bannerGraphic = carrier.getBannerGraphicFile();
			carrierInfo = carrier;
					
			// Get the import source directory
			fileSource  = op.getPropertyValue(DatabaseFactory.PROP_IMPORT_SOURCE_PATH).trim();
			int n = fileSource.length();
			if (n > 0 && fileSource.charAt(n-1) != '\\')
				fileSource += "\\";
			
			// Build the import URL from the request
			importUrl = serverInfo.getRequestUrl(req) + "?";
			
			// Build the forwarding URL
			if (interactiveFlag != null && interactiveFlag.equals("Y"))
				forwardUrl = importUrl + "action=report_batchimport&";
		}
		catch (SQLException e)
		{
			LOGGER.error("Error occurred building the file import page", e);
			throw new DisplayBeanException("Error occurred building the file import page", e);
		}
	}

	/**
	 * Returns the directory/path to import from.
	 * @return String
	 */
	public String getFileSource()
	{
		return fileSource;
	}

	/**
	 * Returns the import Url.
	 * @return String
	 */
	public String getImportUrl()
	{
		return importUrl;
	}

	/**
	 * Returns the "interactive" flag.
	 * @return String
	 */
	public String getInteractiveFlag()
	{
		return interactiveFlag;
	}

	/**
	 * Returns the servletPath.
	 * @return String
	 */
	public String getServletPath()
	{
		return servletPath;
	}

	/**
	 * Sets the servletPath.
	 * @param servletPath The servletPath to set
	 */
	public void setServletPath(String servletPath)
	{
		if (servletPath != null && servletPath.length() > 0)
			this.servletPath = servletPath;
	}

	/**
	 * Returns the classid.
	 * @return String
	 */
	public String getClassid() {
		return classid;
	}

	/**
	 * Returns the codebase.
	 * @return String
	 */
	public String getCodebase() {
		return codebase;
	}

	/**
	 * Returns the carrier's contact email address.
	 * @return String
	 */
	public String getCarrierEmail()
	{
		return carrierEmail;
	}

	/**
	 * Returns the carrier's display name.
	 * @return String
	 */
	public String getCarrierName()
	 {
		return carrierName;
	}

	/**
	 * Returns the carrier's short name.
	 * @return String
	 */
	public String getCarrierShortName()
	{
		return carrierShortName;
	}

	/**
	 * Returns the banner graphic filename.
	 * @return String
	 */
	public String getBannerGraphic()
	{
		return bannerGraphic;
	}

	/**
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * Sets the interactive flag.
	 * @param interactive True to set for interactive import (default), false for batch import
	 */
	public void setInteractive(boolean interactive)
	{
		if (interactive)
			this.interactiveFlag = "Y";
		else
		{
			this.interactiveFlag = "N";
			this.forwardUrl = "";
		}
	}

	/**
	 * Returns the forwarding URL.
	 * @return String
	 */
	public String getForwardUrl()
	{
		return forwardUrl;
	}

}
