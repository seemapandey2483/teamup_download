package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ArchiveService;
import connective.teamup.download.ws.objects.AgentIdInput;
import connective.teamup.download.ws.objects.DownloadFileArrayOutput;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetCurrentPaginatedFiles implements ICommand{
	
	//@Override
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception {
		// TODO Auto-generated method stub
		AgentIdInput agentIdInput = (AgentIdInput)inputData;
		int pageNo =0;
		int rowsPerSelected =0;
	
		String agentId =  agentIdInput.getAgentId();
		if(agentId!= null && !"".equals(agentId)) {
			if(agentId.contains("#")) {
				String [] agnt = agentId.split("#");
				pageNo = Integer.valueOf(agnt[1]);
				rowsPerSelected = Integer.valueOf(agnt[2]);
			}
		}
		
		DownloadFileArrayOutput downloadFileArrayOutput = null; 
		
		DatabaseOperation databaseOperation = null;
		DownloadFileInfo[] downloadFileInfos = null;
		
		try{
			ArchiveService archiveService = new ArchiveService();
			databaseOperation = DatabaseFactory.getInstance().startOperation();
			downloadFileInfos = archiveService.getCurrentFiles(databaseOperation, secInfo.getAgentId(), pageNo, rowsPerSelected, true);
		} finally {
			if (databaseOperation != null)
				databaseOperation.close();
		}
		
		downloadFileArrayOutput = new DownloadFileArrayOutput();
		downloadFileArrayOutput.setFiles(downloadFileInfos);
		return downloadFileArrayOutput;
	}

}
