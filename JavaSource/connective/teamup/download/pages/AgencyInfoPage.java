package connective.teamup.download.pages;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.GenericPage;
import connective.teamup.download.PageException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.beans.AgencyInfoDisplayBean;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the generic page bean to display the agency admin 'current configuration
 * settings' page.
 */
public class AgencyInfoPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(AgencyInfoPage.class);
	/**
	 * Constructor for AgencyInfoPage.
	 */
	public AgencyInfoPage()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		AgencyInfoDisplayBean bean = (AgencyInfoDisplayBean) super.createDisplayBean(req, resp, serverInfo, op, items);

		try
		{
			// Set additional settings to be displayed
			//bean.setDownloadTestFile(op.getPropertyValue(ServerInfo.PROP_TESTFILE));
			
			AgentInfo agency = serverInfo.getAgentInfo(req.getSession(), op);
			if (agency != null)
			{
				if (agency.getParticipantCount() == 0)
					agency.loadParticipantsFromDb();
				bean.setParticipants(agency.getParticipants());
			}
		}
		catch (SQLException e)
		{
			LOGGER.error("Error retrieving agency info", e);
			throw new PageException("Error retrieving agency info", e);
		}
		catch (Exception e) {
			LOGGER.error(e);
		}
		
		return bean;
	}

}
