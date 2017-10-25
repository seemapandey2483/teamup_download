package connective.teamup.download.beans;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Display bean for building the "Search for Agent by Participant Code" page.
 */
public class ParticipantSearchDisplayBean implements Serializable, DisplayBean
{
	private String errorMessage = null;
	private boolean searchByFilename;
	private boolean searchByParticipantCode;
	
	private CarrierInfo carrierInfo = null;
	

	/**
	 * Constructor for ParticipantSearchDisplayBean.
	 */
	public ParticipantSearchDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		carrierInfo = serverInfo.getCarrierInfo();
		
		String importFileIdMode = carrierInfo.getImportFileIdMode();
		searchByFilename = (importFileIdMode.equals(CarrierInfo.IDMODE_FILENAME));
		searchByParticipantCode = (importFileIdMode.equals(CarrierInfo.IDMODE_AGENTID));
		
		serverInfo.resetTPListView(req.getSession());
	}

	/**
	 * Returns the error message (if any).
	 * @return String
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * Returns true if searching by filename.
	 * @return boolean
	 */
	public boolean isSearchByFilename()
	{
		return searchByFilename;
	}

	/**
	 * Returns the true if searching by participant code.
	 * @return boolean
	 */
	public boolean isSearchByParticipantCode()
	{
		return searchByParticipantCode;
	}

	/**
	 * Sets the error message.
	 * @param errorMessage The errorMessage to set
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

}
