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
 * @author Kyle McCreary
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AdminMenuDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(AdminMenuDisplayBean.class);
	
	boolean importAvailable = false;
	
	private String baseControlClassid = "";
	private String baseControlCodebase = "";
	private String statusMessage = "";
	private CarrierInfo carrierInfo = null;
	

	/**
	 * Constructor for AdminMenuDisplayBean.
	 */
	public AdminMenuDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException 
	{
		// Load the carrier info
		this.carrierInfo = serverInfo.getCarrierInfo();
			
		// Load the base control attributes
		this.baseControlClassid = serverInfo.getBaseControlClassid();
		this.baseControlCodebase = serverInfo.getBaseControlCodebase();

		// Check to see if the import source path has been defined
		try
		{
			String importPath = op.getPropertyValue(DatabaseFactory.PROP_IMPORT_SOURCE_PATH);
			setImportAvailable(importPath != null && !importPath.trim().equals(""));
		}
		catch (SQLException e)
		{
			LOGGER.error("Error retrieving property values", e);
			throw new DisplayBeanException("Error retrieving property values", e);
		}
				
		// set the status message
		this.statusMessage = serverInfo.getStatusMessage(req.getSession());
	}

	/**
	 * Returns the importAvailable.
	 * @return boolean
	 */
	public boolean isImportAvailable()
	{
		return importAvailable;
	}

	/**
	 * Sets the importAvailable.
	 * @param importAvailable The importAvailable to set
	 */
	public void setImportAvailable(boolean importAvailable)
	{
		this.importAvailable = importAvailable;
	}

	/**
	 * Returns the carrier's contact email address.
	 * @return String
	 */
	public String getCarrierEmail()
	{
		return carrierInfo.getContactEmail();
	}

	/**
	 * Returns the carrier's display name.
	 * @return String
	 */
	public String getCarrierName()
	 {
		return carrierInfo.getName();
	}

	/**
	 * Returns the carrier's short name.
	 * @return String
	 */
	public String getCarrierShortName()
	{
		return carrierInfo.getShortName();
	}

	/**
	 * Returns the banner graphic filename.
	 * @return String
	 */
	public String getBannerGraphic()
	{
		return carrierInfo.getBannerGraphicFile();
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
	 * Returns the statusMessage.
	 * @return String
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Returns the baseControlClassid.
	 * @return String
	 */
	public String getBaseControlClassid() {
		return baseControlClassid;
	}

	/**
	 * Returns the baseControlCodebase.
	 * @return String
	 */
	public String getBaseControlCodebase() {
		return baseControlCodebase;
	}

}
