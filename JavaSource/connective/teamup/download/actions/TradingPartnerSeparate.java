package connective.teamup.download.actions;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

public class TradingPartnerSeparate implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerSeparate.class);
	
	public TradingPartnerSeparate() {
		super();
	}
	
	//@Override
	public String perform(HttpServletRequest req, HttpServletResponse resp,
			ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException {
		
		String nextPage = "tplist.new";
		String partCode = (String)req.getSession().getAttribute("pCode");
		//String primaryAgentId = req.getParameter("rollinto");
		AgentInfo partToAgent  =null;
		try
		{
			/*primaryAgent = op.getAgentInfo(partCode);			
			if (partToAgent != null)
			{
				// Create a new agent with the contact info and attributes of the old agent
				partToAgent = op.createAgentInfo(partCode.toUpperCase());
				partToAgent.setAms(primaryAgent.getAms());
				partToAgent.setAmsVer(primaryAgent.getAmsVer());
				partToAgent.setClientAppRegistered(primaryAgent.isClientAppRegistered());
				partToAgent.setContactEmail(primaryAgent.getContactEmail());
				partToAgent.setContactName(primaryAgent.getContactName());
				partToAgent.setContactPhone(primaryAgent.getContactPhone());
				partToAgent.setDefaultFilename(primaryAgent.getDefaultFilename());
				partToAgent.setInteractive(primaryAgent.getInteractiveFlag());
				partToAgent.setLastDownloadDate(primaryAgent.getLastDownloadDate());
				partToAgent.setLastLoginDate(primaryAgent.getLastLoginDate());
				partToAgent.setName(primaryAgent.getName());
				partToAgent.setPassword(primaryAgent.getPassword());
				partToAgent.setRemoteDir(primaryAgent.getRemoteDir());
				partToAgent.setStatus(primaryAgent.getStatus());
				partToAgent.setTestAgent(primaryAgent.isTestAgent());
				partToAgent.save();
				partToAgent.tradingPartnerAsAgent(op, partCode, primaryAgent.getAgentId());
			}
			*/
			op.tradingPartnerAsAgent(partCode);
			req.getSession().setAttribute("separateAgent", partCode);
		}catch(SQLException  e) {
			LOGGER.error(e);
			try {
				if(partToAgent!= null)
					partToAgent.delete();
			} catch (SQLException e1) {
				LOGGER.error(e1);
				// TODO Auto-generated catch block
				serverInfo.setStatusMessage(req.getSession(), "Error occurred during trading partner roll-up, process did not complete");
				//throw new ActionException("Error occurred during trading partner roll-up", e);
				return nextPage;
			}
			serverInfo.setStatusMessage(req.getSession(), "Error occurred during trading partner roll-up, process did not complete");
			//throw new ActionException("Error occurred during trading partner roll-up", e);
			return nextPage;
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			serverInfo.setStatusMessage(req.getSession(), "Error occurred during trading partner roll-up, process did not complete");
			//throw new ActionException("Error occurred during trading partner roll-up", e);
			return nextPage;
		}
		
		// Navigate back to the splash screen
		return nextPage;
	}
	

}
