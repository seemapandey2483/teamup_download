package connective.teamup.download.actions;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DuplicateParticipantException;
import connective.teamup.download.db.ParticipantInfo;
import connective.teamup.download.db.Participants;

/**
 * @author Kyle McCreary
 *
 * Action bean to save participant codes for a trading partner
 * from the Carrier Admin pages.
 */
public class SaveParticipants implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveParticipants.class);
	/**
	 * Constructor for SaveParticipants.
	 */
	public SaveParticipants()
	{
		super();
	}
	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		 HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		String nextPage = TradingPartnerListView.getUserDefaultView(req, serverInfo);
		String jsonArray ="";
		Gson gson = new Gson();
		Participants part = null;
		// Check to see if any data changed on the page
		String dataChanged = req.getParameter("data_changed");
		/*if (dataChanged == null || !dataChanged.equals("Y"))
			return nextPage;*/
		
		try
		{
			// Parse the agency info from the page
			AgentInfo agent = null;
			String agentID = req.getParameter("agentId");
			String newPart = req.getParameter("newPart");
			agent = op.getAgentInfo(agentID);
			if (agent == null)
				return "tplist.edit";

			/*Vector participants = new Vector();
			int numParticipants = 0;
			try
			{
				numParticipants = Integer.parseInt(req.getParameter("num_participants"));
			}
			catch (Exception e)
			{
				numParticipants = 0;
			}*/
			
			// Parse all of the participant info from the page
			/*for (int i=0; i <= numParticipants; i++)
			{*/
				String partcode = req.getParameter("participantCode");
				partcode = partcode.trim().toUpperCase();
				String agnecyName = req.getParameter("agencyName");
				String agencyLocation = req.getParameter("agencyLocation");
				String agentDestAddress = req.getParameter("agentDestAddress");
				String contactName = req.getParameter("contactName");
				String contactPhone = req.getParameter("contactPhone");
				String contactEmail = req.getParameter("contactEmail");
				String keyLinkFile = req.getParameter("partKeyLink");
				String city = req.getParameter("city");
				String stateId = req.getParameter("stateId");
				if("-1".equals(stateId))
					stateId = null;
				
				String zip = req.getParameter("zip");
				String isPrimaryContact = req.getParameter("isPrimaryContact");
				String amsPartCode = req.getParameter("amsPartCode");
				String filename = req.getParameter("filename" );
				if(keyLinkFile!= null ){
					filename = keyLinkFile;
				}else{

					if (filename == null || filename.trim().equals(""))
						filename = partcode;
					else
						filename = filename.trim().toUpperCase();
					
				}	
					ParticipantInfo partBean = op.createParticipant(agentID);
					partBean.setFilename(filename);
					partBean.setParticipantCode(partcode);
					partBean.setAgencyName(agnecyName);
					partBean.setAgencyLocation(agencyLocation);
					partBean.setAgentDestAddress(agentDestAddress);
					partBean.setAmsPartCode(amsPartCode);

				//	partBean.setFilename(keyLinkFile);
					if("true".equals(isPrimaryContact)) {
						partBean.setAgencyName(agent.getName());
						partBean.setContactName(agent.getContactName());
						partBean.setContactPhone(agent.getContactPhone());
						partBean.setContactEmail(agent.getContactEmail());
						partBean.setCity(agent.getCity());
						partBean.setStateId(agent.getStateId());
						partBean.setZip(agent.getZip());
						partBean.setPrimaryContact("Y");

						
					}else{
						partBean.setContactName(contactName);
						partBean.setContactPhone(contactPhone);
						partBean.setContactEmail(contactEmail);
						partBean.setAmsPartCode(amsPartCode);
						partBean.setCity(city);
						partBean.setStateId(stateId);
						partBean.setZip(zip);
						partBean.setPrimaryContact("N");
						
					}

					
					
			// Save the updated participant info
			try
			{
				boolean returnPartInfo = op.getPartInfo(req.getParameter("participantCode"));
				if (returnPartInfo && "Y".equals(newPart)) {
					String msg = "Participant [" + partcode +"] already Exist";
					req.getSession().setAttribute("errorMessage", msg);
					JSONROOT.put("Result", "ERROR");
					JSONROOT.put("Message", "Partcipant alrday exist");
					jsonArray = gson.toJson(JSONROOT);
				} else {
					agent.saveParticipant(partBean);
					part = op.loadPartcipants(agent.getAgentId(), partBean.getFilename());
					JSONROOT.put("Record", part);
					JSONROOT.put("Result", "OK");
					jsonArray = gson.toJson(JSONROOT);
				}
				//JSONROOT.put("Record", part);
			
			}
			catch (DuplicateParticipantException dupEx)
			{
				LOGGER.error(dupEx);
				// If duplicate participants or filenames were encountered, 
				// save the error message and navigate back to the appropriate
				// participants page.
				serverInfo.setDuplicateParticipantInfo(req.getSession(), dupEx);
				if (serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME))
					nextPage = "tplist.participants.filenames";
				else
					nextPage = "tplist.participants.codes";
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", dupEx.getMessage(0));
				jsonArray = gson.toJson(JSONROOT);
			}
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", e.getMessage());
			jsonArray = gson.toJson(JSONROOT);
			throw new ActionException("Error saving participant code settings", e);
		}finally {
			return jsonArray;

		}
		
	}

}
