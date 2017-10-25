package connective.teamup.download.ws.commands;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.DownloadService;
import connective.teamup.download.ws.objects.DownloadFileArrayOutput;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.GetDownloadFilesInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetCADownloadFiles implements ICommand{
	
	@SuppressWarnings("deprecation")
	//@Override
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception {
		// TODO Auto-generated method stub
		DownloadFileArrayOutput output = null;
		DownloadFileInfo[] files = null;
		DatabaseOperation op = null;
		GetDownloadFilesInput input = (GetDownloadFilesInput)inputData;
		int pageNo =0;
		int rowsPerSelected =0;
		
		try{
			
			String agentId =  input.getAgentId();
			if(agentId!= null && !"".equals(agentId)) {
				if(agentId.contains("#")) {
					String [] agnt = agentId.split("#");
					pageNo = Integer.valueOf(agnt[1]);
					rowsPerSelected = Integer.valueOf(agnt[2]);
					agentId = agnt[0]; 

				}
			}
			DownloadService downloadService = new DownloadService();
			op = DatabaseFactory.getInstance().startOperation();
			
			// retrieve the agent and verify it is not disabled
			//AgentInfo agent = op.getAgentInfo(secInfo.getAgentId());
			Boolean isValidandActiveAgent = op.validateAgent(secInfo.getAgentId());
			if (isValidandActiveAgent == null){
				throw new Exception("Invalid agent ID.");
			} else if (!isValidandActiveAgent){
				throw new Exception("This agent ID is not currently active.  Please contact your carrier representative to continue registration for TEAM-UP Download.");
			}
			
			// update the last login date for the agent
			//agent.setLastLoginDate(System.currentTimeMillis());
			//agent.save();
			op.updateLastLoginDateforAgent(agentId);
			files = downloadService.getDownloadFiles(op, agentId, input.getDlStatus(), pageNo, rowsPerSelected, true);
			if (files != null){
				for (int i = 0; i < files.length; i++) {
					files[i].setFileContentsEncoded("#OFFLINE#");
				}
			}
		} finally{
			if (op != null)
					op.close();
		}
		
		output = new DownloadFileArrayOutput();
		output.setFiles(files);
		return output;
	}

}
