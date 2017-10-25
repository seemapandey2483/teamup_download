package connective.teamup.download.actions;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.ParticipantInfo;
import connective.teamup.download.db.TransactionInfo;
import org.apache.log4j.Logger;



public class TradingPartnerMoveUp implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerMoveUp.class);
	
	public TradingPartnerMoveUp() {
		super();
	}
	
	//@Override
	public String perform(HttpServletRequest req, HttpServletResponse resp,
			ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException {
		String nextPage = "splash";
		
		try
		{
			String primaryAgentId = req.getParameter("newTPAgent");
			AgentInfo primaryAgent = op.getAgentInfo(primaryAgentId);
			String oldAgentId = req.getParameter("moveAgent");
			AgentInfo oldAgent = op.getAgentInfo(oldAgentId);
			boolean isDirectBillExist = false;
			boolean isClaimFileExist = false;
			if (oldAgent == null)
			{
				serverInfo.setStatusMessage(req.getSession(), 
						"No trading partner is currently defined for the specified ID '" + oldAgentId + "'");
			}
			
			if (primaryAgent == null)
			{
				// Create a new agent with the contact info and attributes of the old agent
				primaryAgent = op.createAgentInfo(primaryAgentId.toUpperCase());
				primaryAgent.setAms(oldAgent.getAms());
				primaryAgent.setAmsVer(oldAgent.getAmsVer());
				primaryAgent.setClientAppRegistered(oldAgent.isClientAppRegistered());
				primaryAgent.setContactEmail(oldAgent.getContactEmail());
				primaryAgent.setContactName(oldAgent.getContactName());
				primaryAgent.setContactPhone(oldAgent.getContactPhone());
				primaryAgent.setDefaultFilename(oldAgent.getDefaultFilename());
				primaryAgent.setInteractive(oldAgent.getInteractiveFlag());
				primaryAgent.setLastDownloadDate(oldAgent.getLastDownloadDate());
				primaryAgent.setLastLoginDate(oldAgent.getLastLoginDate());
				primaryAgent.setName(oldAgent.getName());
				primaryAgent.setPassword(oldAgent.getPassword());
				primaryAgent.setRemoteDir(oldAgent.getRemoteDir());
				primaryAgent.setStatus(oldAgent.getStatus());
				primaryAgent.setTestAgent(oldAgent.isTestAgent());
				primaryAgent.save();
				if (serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_AGENTID))
					primaryAgent.setKeylinkFile(primaryAgentId.toUpperCase());
				
				// Set next page navigation to point to the Trading Partner Maintenance
				// page for the newly created agent
				req.setAttribute("search.partcode.agentID", primaryAgentId);
				nextPage = "tplist.edit";
			}
			
			// Move participant codes -- add to the current list for the target agent
			boolean primaryParticipantCreated = false;
			oldAgent.loadParticipantsFromDb();
			primaryAgent.loadParticipantsFromDb();
			Vector newParticipants = primaryAgent.getParticipants();
			for (int i=0; i < oldAgent.getParticipantCount(); i++)
			{
				ParticipantInfo oldPart = oldAgent.getParticipant(i);
				oldPart.delete();
				
				ParticipantInfo newPart = op.createParticipant(primaryAgent.getAgentId());
				String agencyName ="";
				String contactName = "";
				if(oldPart.getAgencyName()!= null) {
					agencyName = oldPart.getAgencyName().replaceAll("'","\\''");
				}
				
				if(oldPart.getContactName()!= null)
					contactName = oldPart.getContactName().replaceAll("'","\\''");
				
				newPart.setFilename(oldPart.getFilename());
				newPart.setParticipantCode(oldPart.getParticipantCode());

				newPart.setAgencyLocation(oldPart.getAgencyLocation());
				newPart.setAgencyName(agencyName);
				newPart.setAgentDestAddress(oldPart.getAgentDestAddress());
				newPart.setContactName(contactName);
				newPart.setContactEmail(oldPart.getContactEmail());
				newPart.setContactPhone(oldPart.getContactPhone());
				newPart.setCity(oldPart.getCity());
				newPart.setStateId(oldPart.getStateId());
				newPart.setZip(oldPart.getZip());
				
				newParticipants.add(newPart);
				
				if (oldPart.getAgentId().equals(oldPart.getParticipantCode()))
					primaryParticipantCreated = true;
			}
			
			// Check for unique filename attached to the old agent
			if (oldAgent.getKeylinkFile() != null && !oldAgent.getKeylinkFile().equals(""))
				oldAgent.setKeylinkFile(null);
			
			if (!primaryParticipantCreated)
			{
				// Create a new participant code for the old agent ID
				ParticipantInfo newPart = op.createParticipant(primaryAgent.getAgentId());
				String agencyName ="";
				String contactName = "";
				if(oldAgent.getName()!= null) {
					agencyName = oldAgent.getName().replaceAll("'","\\''");
				}
				
				if(oldAgent.getContactName()!= null)
					contactName = oldAgent.getContactName().replaceAll("'","\\''");
				
				newPart.setFilename(oldAgent.getAgentId());
				newPart.setParticipantCode(oldAgent.getAgentId());

				newPart.setAgencyLocation(oldAgent.getLocationState());
				newPart.setAgencyName(agencyName);
				newPart.setAgentDestAddress(oldAgent.getAgentDestAddress());
				newPart.setContactName(contactName);
				newPart.setContactEmail(oldAgent.getContactEmail());
				newPart.setContactPhone(oldAgent.getContactPhone());
				newPart.setCity(oldAgent.getCity());
				newPart.setStateId(oldAgent.getStateId());
				newPart.setZip(oldAgent.getZip());
				
				newParticipants.add(newPart);
			}
			primaryAgent.saveParticipants(newParticipants);
			
			// Move archived files
			FileInfo[] archive = op.getAgentFiles(oldAgent.getAgentId());
			if (archive != null && archive.length > 0)
			{
				String agentId = primaryAgent.getAgentId();
				for (int i=0; i < archive.length; i++)
				{
					if (archive[i].isFileComplete())
					{
						// Copy the file
						FileInfo file = archive[i];
						if(file.isDirectBill())
							isDirectBillExist = true;
						if(file.isClaimFile())
							isClaimFileExist = true;
						
						file.loadTransFromDb();
						file.setAgentId(agentId);
						file.setFilename(primaryAgent.getDefaultFilename()!=null?primaryAgent.getDefaultFilename():"");
						file.save();
						int transCount = file.getTransactionCount();
						if(isDirectBillExist)
							transCount =1;
						for (int j=0; j < transCount; j++)
						{
							// Copy the transaction
							TransactionInfo trans = file.getTransaction(j);
							if(trans != null) {
								trans.loadTransactionData();
								trans.setAgentId(agentId);
								trans.save();
								trans.writeTransactionData();
								
							}
						}
					}
				}
				if(isDirectBillExist)
					op.updateDirectBill(oldAgentId,agentId);
				if(isClaimFileExist)
					op.updateClaimFile(oldAgentId,agentId);
			}
			
			// Delete the old agent and all remaining child records
			oldAgent.delete();
			
			// Set a final status message to display to the user
			serverInfo.setStatusMessage(req.getSession(), "Trading partner roll-up completed successfully:  " + oldAgent.getAgentId() + " -> " + primaryAgent.getAgentId());
		}
		catch (Exception e)
		{
			try{
				op.rollback();
			}catch(SQLException se){
				throw new ActionException("Error occurred during trading partner roll-up", e);
			}
			serverInfo.setStatusMessage(req.getSession(), "Error occurred during trading partner roll-up, process did not complete");
			throw new ActionException("Error occurred during trading partner roll-up", e);
		}
		// Navigate back to the splash screen
		return nextPage;
	}
}
